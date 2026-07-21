/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.typetree;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;
import static org.eclipse.milo.opcua.stack.core.util.Lists.partition;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.sdk.client.OperationLimits;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseNextResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
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
            .map(UInteger::intValue)
            .filter(v -> v > 0)
            .orElse(Integer.MAX_VALUE);

    var values = new ArrayList<DataValue>();

    for (List<ReadValueId> partitionList : partition(readValueIds, partitionSize).toList()) {
      ReadResponse response = client.read(0.0, TimestampsToReturn.Neither, partitionList);
      DataValue[] results = response.getResults();
      Collections.addAll(values, requireNonNull(results));
    }

    return values;
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
            .map(UInteger::intValue)
            .filter(v -> v > 0)
            .orElse(Integer.MAX_VALUE);

    var references = new ArrayList<List<ReferenceDescription>>();

    for (List<BrowseDescription> partitionList :
        partition(browseDescriptions, partitionSize).toList()) {
      references.addAll(browse(client, partitionList));
    }

    return references;
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

    final var referenceDescriptionLists = new ArrayList<List<ReferenceDescription>>();

    for (BrowseResult result : client.browse(browseDescriptions)) {
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

    while (continuationPoint != null && continuationPoint.isNotNull()) {
      BrowseNextResponse response = client.browseNext(false, List.of(continuationPoint));

      BrowseResult result = requireNonNull(response.getResults())[0];

      ReferenceDescription[] rds =
          requireNonNullElse(result.getReferences(), new ReferenceDescription[0]);

      references.addAll(List.of(rds));

      continuationPoint = result.getContinuationPoint();
    }

    return references;
  }
}
