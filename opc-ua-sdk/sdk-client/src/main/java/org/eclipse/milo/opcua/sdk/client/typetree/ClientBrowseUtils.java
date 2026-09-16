/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.typetree;

import static java.util.Objects.requireNonNullElse;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.sdk.client.OperationLimits;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.DataTypeEncoding;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseNextResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Shared utility methods for client-side browse and read operations with operation limit handling.
 *
 * <p>Service-level failures propagate as {@link UaException} so that callers building type trees
 * fail rather than silently caching incomplete results. Operation-level failures (bad status on an
 * individual node) are still tolerated and yield empty/bad results for that node only.
 */
final class ClientBrowseUtils {

  private static final Logger LOGGER = LoggerFactory.getLogger(ClientBrowseUtils.class);

  /**
   * Upper bound on BrowseNext calls for a single continuation point, guarding against non-compliant
   * servers that never return a null/empty continuation point.
   */
  private static final int MAX_BROWSE_NEXT_ITERATIONS = 1000;

  private ClientBrowseUtils() {}

  /**
   * Check that the client's currently-active session is the session identified by {@code
   * sessionId}.
   *
   * <p>Used to pin a multi-request type tree build to the session it started on, so that losing the
   * session aborts the build instead of letting it silently span sessions.
   *
   * @param client the OPC UA client.
   * @param sessionId the id of the session the operation started on.
   * @throws UaException with {@link StatusCodes#Bad_SessionClosed} if there is no active session or
   *     the active session is not the expected one.
   */
  static void checkSessionUnchanged(OpcUaClient client, NodeId sessionId) throws UaException {
    OpcUaSession session;
    try {
      session = client.getSessionAsync().getNow(null);
    } catch (RuntimeException e) {
      session = null;
    }

    if (session == null || !sessionId.equals(session.getSessionId())) {
      throw new UaException(
          StatusCodes.Bad_SessionClosed, "session closed or changed during type tree build");
    }
  }

  /**
   * Read values with operation limits, partitioning requests as necessary.
   *
   * @param client the OPC UA client.
   * @param readValueIds the list of ReadValueIds to read.
   * @param limits the operation limits from the server.
   * @return the list of DataValues corresponding to the read requests.
   * @throws UaException if a service-level error occurs.
   */
  static List<DataValue> readWithOperationLimits(
      OpcUaClient client, List<ReadValueId> readValueIds, OperationLimits limits)
      throws UaException {

    if (readValueIds.isEmpty()) {
      return List.of();
    }

    LOGGER.debug("readWithOperationLimits: {}", readValueIds.size());

    int partitionSize =
        limits
            .maxNodesPerRead()
            .map(ClientBrowseUtils::toPartitionSize)
            .filter(v -> v > 0)
            .orElse(1);

    return executeWithOperationLimit(
        readValueIds,
        partitionSize,
        partitionList -> {
          ReadResponse response;
          try {
            response = client.read(0.0, TimestampsToReturn.Neither, partitionList);
          } catch (UaException e) {
            throw retryableTooManyOperations(partitionList, e);
          }

          DataValue[] results = response.getResults();
          if (results == null || results.length != partitionList.size()) {
            throw new UaException(
                StatusCodes.Bad_UnexpectedError,
                "Read returned %s results, expected %s"
                    .formatted(results == null ? "null" : results.length, partitionList.size()));
          }

          return Arrays.asList(results);
        });
  }

  /**
   * Browse with operation limits, partitioning requests as necessary.
   *
   * @param client the OPC UA client.
   * @param browseDescriptions the list of BrowseDescriptions.
   * @param limits the operation limits from the server.
   * @return the list of reference description lists corresponding to each browse request.
   * @throws UaException if a service-level error occurs.
   */
  static List<List<ReferenceDescription>> browseWithOperationLimits(
      OpcUaClient client, List<BrowseDescription> browseDescriptions, OperationLimits limits)
      throws UaException {

    if (browseDescriptions.isEmpty()) {
      return List.of();
    }

    LOGGER.debug("browseWithOperationLimits: {}", browseDescriptions.size());

    int partitionSize =
        limits
            .maxNodesPerBrowse()
            .map(ClientBrowseUtils::toPartitionSize)
            .filter(v -> v > 0)
            .orElse(1);

    return executeWithOperationLimit(
        browseDescriptions, partitionSize, partitionList -> browsePartition(client, partitionList));
  }

  private static int toPartitionSize(UInteger operationLimit) {
    return (int) Math.min(operationLimit.longValue(), Integer.MAX_VALUE);
  }

  private static <T, R> List<R> executeWithOperationLimit(
      List<T> inputs, int partitionSize, PartitionOperation<T, R> operation) throws UaException {

    var results = new ArrayList<R>(inputs.size());

    int index = 0;
    int currentPartitionSize = partitionSize;

    while (index < inputs.size()) {
      int count = Math.min(currentPartitionSize, inputs.size() - index);
      List<T> partitionList = inputs.subList(index, index + count);

      try {
        results.addAll(operation.apply(partitionList));
        index += count;
      } catch (RetryableTooManyOperationsException e) {
        currentPartitionSize = 1;
      }
    }

    return results;
  }

  @FunctionalInterface
  private interface PartitionOperation<T, R> {

    List<R> apply(List<T> partitionList) throws UaException;
  }

  private static UaException retryableTooManyOperations(List<?> partition, UaException failure) {
    if (partition.size() > 1
        && failure.getStatusCode().value() == StatusCodes.Bad_TooManyOperations) {

      return new RetryableTooManyOperationsException(failure);
    }

    return failure;
  }

  private static final class RetryableTooManyOperationsException extends UaException {

    private RetryableTooManyOperationsException(UaException cause) {
      super(cause);
    }
  }

  /**
   * Browse a list of nodes and return all reference descriptions, handling continuation points.
   *
   * @param client the OPC UA client.
   * @param browseDescriptions the list of BrowseDescriptions.
   * @return a list of reference description lists, one per browse description.
   * @throws UaException if a service-level error occurs.
   */
  static List<List<ReferenceDescription>> browse(
      OpcUaClient client, List<BrowseDescription> browseDescriptions) throws UaException {

    if (browseDescriptions.isEmpty()) {
      return List.of();
    }

    List<BrowseResult> browseResults = client.browse(browseDescriptions);

    return collectBrowseResults(client, browseDescriptions, browseResults);
  }

  private static List<List<ReferenceDescription>> browsePartition(
      OpcUaClient client, List<BrowseDescription> browseDescriptions) throws UaException {

    List<BrowseResult> browseResults;
    try {
      browseResults = client.browse(browseDescriptions);
    } catch (UaException e) {
      throw retryableTooManyOperations(browseDescriptions, e);
    }

    return collectBrowseResults(client, browseDescriptions, browseResults);
  }

  private static List<List<ReferenceDescription>> collectBrowseResults(
      OpcUaClient client,
      List<BrowseDescription> browseDescriptions,
      List<BrowseResult> browseResults)
      throws UaException {

    if (browseResults.size() != browseDescriptions.size()) {
      throw new UaException(
          StatusCodes.Bad_UnexpectedError,
          "Browse returned %d results, expected %d"
              .formatted(browseResults.size(), browseDescriptions.size()));
    }

    final var referenceDescriptionLists = new ArrayList<List<ReferenceDescription>>();

    for (BrowseResult result : browseResults) {
      if (result.getStatusCode().isGood()) {
        var references = new ArrayList<ReferenceDescription>();

        ReferenceDescription[] refs =
            requireNonNullElse(result.getReferences(), new ReferenceDescription[0]);
        Collections.addAll(references, refs);

        ByteString continuationPoint = result.getContinuationPoint();
        List<ReferenceDescription> nextRefs = maybeBrowseNext(client, continuationPoint);
        references.addAll(nextRefs);

        referenceDescriptionLists.add(references);
      } else {
        referenceDescriptionLists.add(List.of());
      }
    }

    return referenceDescriptionLists;
  }

  /**
   * Continue browsing using a continuation point until all references are retrieved.
   *
   * @param client the OPC UA client.
   * @param continuationPoint the continuation point from a previous browse.
   * @return the list of additional reference descriptions.
   * @throws UaException if a service-level error occurs.
   */
  static List<ReferenceDescription> maybeBrowseNext(
      OpcUaClient client, @Nullable ByteString continuationPoint) throws UaException {

    var references = new ArrayList<ReferenceDescription>();

    int iterations = 0;

    while (continuationPoint != null && !continuationPoint.isNullOrEmpty()) {
      if (++iterations > MAX_BROWSE_NEXT_ITERATIONS) {
        var limitException =
            new UaException(
                StatusCodes.Bad_UnexpectedError,
                "BrowseNext did not complete after %d calls".formatted(MAX_BROWSE_NEXT_ITERATIONS));

        try {
          client.browseNext(true, List.of(continuationPoint));
        } catch (UaException e) {
          limitException.addSuppressed(e);
        }

        throw limitException;
      }

      BrowseNextResponse response = client.browseNext(false, List.of(continuationPoint));

      BrowseResult[] results = response.getResults();
      if (results == null || results.length == 0) {
        throw new UaException(StatusCodes.Bad_UnexpectedError, "BrowseNext returned no results");
      }

      BrowseResult result = results[0];

      ReferenceDescription[] rds =
          requireNonNullElse(result.getReferences(), new ReferenceDescription[0]);

      references.addAll(List.of(rds));

      continuationPoint = result.getContinuationPoint();
    }

    return references;
  }

  /**
   * Browse the HasEncoding references of {@code dataTypeIds} to find their encoding Nodes.
   *
   * @param client the OPC UA client.
   * @param dataTypeIds the {@link NodeId}s of the DataType Nodes to browse.
   * @param limits the operation limits from the server.
   * @return a list of reference description lists, one per DataType id.
   * @throws UaException if a service-level error occurs.
   */
  static List<List<ReferenceDescription>> browseEncodings(
      OpcUaClient client, List<NodeId> dataTypeIds, OperationLimits limits) throws UaException {

    List<BrowseDescription> browseDescriptions =
        dataTypeIds.stream()
            .map(
                dataTypeId ->
                    new BrowseDescription(
                        dataTypeId,
                        BrowseDirection.Forward,
                        NodeIds.HasEncoding,
                        false,
                        uint(NodeClass.Object.getValue()),
                        uint(BrowseResultMask.All.getValue())))
            .toList();

    return browseWithOperationLimits(client, browseDescriptions, limits);
  }

  /** The encoding Node ids of a DataType, extracted from its HasEncoding references. */
  record EncodingIds(
      @Nullable NodeId binaryEncodingId,
      @Nullable NodeId xmlEncodingId,
      @Nullable NodeId jsonEncodingId) {}

  /**
   * Extract the Default Binary/XML/JSON encoding Node ids from a DataType Node's HasEncoding
   * references.
   *
   * @param encodings the HasEncoding {@link ReferenceDescription}s of a DataType Node.
   * @param namespaceTable the namespace table for converting ExpandedNodeIds.
   * @return the extracted {@link EncodingIds}.
   */
  static EncodingIds extractEncodingIds(
      List<ReferenceDescription> encodings, NamespaceTable namespaceTable) {

    NodeId binaryEncodingId = null;
    NodeId xmlEncodingId = null;
    NodeId jsonEncodingId = null;

    for (ReferenceDescription r : encodings) {
      // Observed multiple servers at IOP using the wrong namespace index...
      // Be lenient and also allow matching on the unqualified browse name.

      if (r.getBrowseName().equals(DataTypeEncoding.BINARY_ENCODING_NAME)
          || Objects.equals(r.getBrowseName().name(), "Default Binary")) {

        binaryEncodingId = r.getNodeId().toNodeId(namespaceTable).orElse(null);
      } else if (r.getBrowseName().equals(DataTypeEncoding.XML_ENCODING_NAME)
          || Objects.equals(r.getBrowseName().name(), "Default XML")) {

        xmlEncodingId = r.getNodeId().toNodeId(namespaceTable).orElse(null);
      } else if (r.getBrowseName().equals(DataTypeEncoding.JSON_ENCODING_NAME)
          || Objects.equals(r.getBrowseName().name(), "Default JSON")) {

        jsonEncodingId = r.getNodeId().toNodeId(namespaceTable).orElse(null);
      }
    }

    return new EncodingIds(binaryEncodingId, xmlEncodingId, jsonEncodingId);
  }

  /**
   * Get the Binary Encoding Node id for {@code dataType}.
   *
   * <p>Falls back to the DefaultEncodingId from the type's {@link StructureDefinition} as a
   * workaround for non-compliant Servers that don't have encoding nodes in their address space. The
   * DefaultEncodingId in a StructureDefinition shall always be the Default Binary encoding, so use
   * it if the Server at least set that correctly. See <a
   * href="https://reference.opcfoundation.org/Core/Part3/v105/docs/8.48">Part 3, 8.48</a>.
   *
   * @param dataType the {@link DataType} to get the Binary Encoding Node id for.
   * @return the Binary Encoding Node id, or {@code null} if none is available.
   */
  static @Nullable NodeId getBinaryEncodingId(DataType dataType) {
    NodeId binaryEncodingId = dataType.getBinaryEncodingId();

    if (binaryEncodingId == null
        && dataType.getDataTypeDefinition() instanceof StructureDefinition definition) {

      binaryEncodingId = definition.getDefaultEncodingId();
    }

    return binaryEncodingId;
  }
}
