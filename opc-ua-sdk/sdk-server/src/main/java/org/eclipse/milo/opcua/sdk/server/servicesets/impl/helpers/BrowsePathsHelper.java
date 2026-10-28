/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets.impl.helpers;

import static java.util.stream.Collectors.toList;
import static org.eclipse.milo.opcua.sdk.server.servicesets.AbstractServiceSet.createResponseHeader;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.server.AccessContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.BrowseContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.ReadContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.ReferenceResult.ReferenceList;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathResult;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathTarget;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePath;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePathElement;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.TranslateBrowsePathsToNodeIdsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.TranslateBrowsePathsToNodeIdsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ViewDescription;
import org.eclipse.milo.opcua.stack.core.util.Lists;

public class BrowsePathsHelper {

  private final AccessContext context;
  private final OpcUaServer server;

  public BrowsePathsHelper(AccessContext context, OpcUaServer server) {
    this.context = context;
    this.server = server;
  }

  public TranslateBrowsePathsToNodeIdsResponse translateBrowsePaths(
      TranslateBrowsePathsToNodeIdsRequest request) throws UaException {

    List<BrowsePath> browsePaths = Lists.ofNullable(request.getBrowsePaths());

    if (browsePaths.isEmpty()) {
      throw new UaException(StatusCodes.Bad_NothingToDo);
    }

    if (browsePaths.size()
        > server.getConfig().getLimits().getMaxNodesPerTranslateBrowsePathsToNodeIds().intValue()) {

      throw new UaException(StatusCodes.Bad_TooManyOperations);
    }

    var results = new ArrayList<BrowsePathResult>(browsePaths.size());

    for (BrowsePath browsePath : browsePaths) {
      results.add(translate(browsePath));
    }

    ResponseHeader header = createResponseHeader(request);

    return new TranslateBrowsePathsToNodeIdsResponse(
        header, results.toArray(BrowsePathResult[]::new), new DiagnosticInfo[0]);
  }

  private BrowsePathResult translate(BrowsePath browsePath) {
    NodeId startingNode = browsePath.getStartingNode();
    RelativePath relativePath = browsePath.getRelativePath();

    if (startingNode.isNull()) {
      return new BrowsePathResult(
          new StatusCode(StatusCodes.Bad_NodeIdInvalid), new BrowsePathTarget[0]);
    }

    List<RelativePathElement> relativePathElements = Lists.ofNullable(relativePath.getElements());

    if (relativePathElements.isEmpty()) {
      return new BrowsePathResult(
          new StatusCode(StatusCodes.Bad_NothingToDo), new BrowsePathTarget[0]);
    }

    try {
      List<BrowsePathTarget> targets = follow(startingNode, relativePathElements);

      if (!targets.isEmpty()) {
        return new BrowsePathResult(StatusCode.GOOD, targets.toArray(new BrowsePathTarget[0]));
      } else {
        return new BrowsePathResult(
            new StatusCode(StatusCodes.Bad_NoMatch), new BrowsePathTarget[0]);
      }
    } catch (UaException e) {
      return new BrowsePathResult(e.getStatusCode(), new BrowsePathTarget[0]);
    }
  }

  private List<BrowsePathTarget> follow(NodeId nodeId, List<RelativePathElement> elements)
      throws UaException {

    if (elements.isEmpty()) {
      return Collections.emptyList();
    } else if (elements.size() == 1) {
      List<ExpandedNodeId> targets = target(nodeId, elements.get(0));

      return targets.stream()
          .map(
              n -> {
                n = BrowseUtil.normalize(n, server.getNamespaceTable());
                return new BrowsePathTarget(n, UInteger.MAX);
              })
          .collect(toList());
    } else {
      RelativePathElement e = elements.get(0);

      ExpandedNodeId nextXni = next(nodeId, e);
      if (nextXni.isNull()) {
        // There was no match for the target name
        throw new UaException(StatusCodes.Bad_NoMatch);
      }

      List<RelativePathElement> nextElements = elements.subList(1, elements.size());

      Optional<NodeId> nextId = nextXni.toNodeId(server.getNamespaceTable());

      if (nextId.isPresent()) {
        return follow(nextId.get(), nextElements);
      } else {
        UInteger remaining = nextElements.isEmpty() ? UInteger.MAX : uint(nextElements.size());

        nextXni = BrowseUtil.normalize(nextXni, server.getNamespaceTable());
        return List.of(new BrowsePathTarget(nextXni, remaining));
      }
    }
  }

  private ExpandedNodeId next(NodeId nodeId, RelativePathElement element) throws UaException {
    QualifiedName targetName = element.getTargetName();

    if (targetName.isNull()) {
      throw new UaException(StatusCodes.Bad_BrowseNameInvalid);
    }

    List<ExpandedNodeId> targetNodeIds = browseTargets(nodeId, element);
    List<QualifiedName> browseNames = readBrowseNames(targetNodeIds);

    for (int i = 0; i < targetNodeIds.size(); i++) {
      if (targetName.equals(browseNames.get(i))) {
        return targetNodeIds.get(i);
      }
    }

    return ExpandedNodeId.NULL_VALUE;
  }

  private List<ExpandedNodeId> target(NodeId nodeId, RelativePathElement element)
      throws UaException {

    QualifiedName targetName = element.getTargetName();

    if (targetName.isNull()) {
      throw new UaException(StatusCodes.Bad_BrowseNameInvalid);
    }

    List<ExpandedNodeId> targetNodeIds = browseTargets(nodeId, element);
    List<QualifiedName> browseNames = readBrowseNames(targetNodeIds);

    var targets = new ArrayList<ExpandedNodeId>();

    for (int i = 0; i < targetNodeIds.size(); i++) {
      if (targetName.equals(browseNames.get(i))) {
        targets.add(targetNodeIds.get(i));
      }
    }

    return targets;
  }

  /**
   * Browse {@code nodeId} and return the targets of the references that pass {@code element}'s
   * ReferenceType and direction filters, in reference order.
   *
   * @throws UaException with Bad_NodeIdUnknown if {@code nodeId} can't be browsed, or Bad_NoMatch
   *     if no reference passes the filters.
   */
  private List<ExpandedNodeId> browseTargets(NodeId nodeId, RelativePathElement element)
      throws UaException {

    NodeId referenceTypeId = element.getReferenceTypeId();
    boolean includeSubtypes = element.getIncludeSubtypes();
    boolean isInverse = element.getIsInverse();

    var browseContext = new BrowseContext(server, context.getSession().orElse(null));

    var view = new ViewDescription(NodeId.NULL_VALUE, DateTime.NULL_VALUE, UInteger.valueOf(0));

    AddressSpace.ReferenceResult result =
        server.getAddressSpaceManager().browse(browseContext, view, List.of(nodeId)).get(0);

    if (!(result instanceof ReferenceList rl)) {
      throw new UaException(StatusCodes.Bad_NodeIdUnknown);
    }

    ReferenceTypeTree referenceTypeTree = server.getReferenceTypeTree();

    var targetNodeIds = new ArrayList<ExpandedNodeId>();

    for (Reference reference : rl.references()) {
      NodeId typeId = reference.getReferenceTypeId();

      boolean typeMatches =
          referenceTypeId.isNull()
              || typeId.equals(referenceTypeId)
              || (includeSubtypes && referenceTypeTree.isSubtypeOf(typeId, referenceTypeId));

      if (typeMatches && reference.isInverse() == isInverse) {
        targetNodeIds.add(reference.getTargetNodeId());
      }
    }

    if (targetNodeIds.isEmpty()) {
      throw new UaException(StatusCodes.Bad_NoMatch);
    }

    return targetNodeIds;
  }

  /**
   * Read the BrowseName of each target in {@code targetNodeIds}, in one Read through the {@link
   * AddressSpaceManager}, so each name is the one a Read of that Node returns.
   *
   * <p>A target on another Server, or whose Read fails, gets the null QualifiedName, which matches
   * no target name.
   *
   * @param targetNodeIds the targets to read the BrowseNames of.
   * @return the BrowseName of each target, in the order of {@code targetNodeIds}.
   */
  private List<QualifiedName> readBrowseNames(List<ExpandedNodeId> targetNodeIds) {
    var browseNames =
        new ArrayList<>(Collections.nCopies(targetNodeIds.size(), QualifiedName.NULL_VALUE));

    // Only targets on this Server are read; readIndices maps each Read back to its target.
    var readValueIds = new ArrayList<ReadValueId>(targetNodeIds.size());
    var readIndices = new ArrayList<Integer>(targetNodeIds.size());

    for (int i = 0; i < targetNodeIds.size(); i++) {
      Optional<NodeId> nodeId = targetNodeIds.get(i).toNodeId(server.getNamespaceTable());

      if (nodeId.isPresent()) {
        readValueIds.add(
            new ReadValueId(
                nodeId.get(), AttributeId.BrowseName.uid(), null, QualifiedName.NULL_VALUE));
        readIndices.add(i);
      }
    }

    List<DataValue> values = readAll(readValueIds);

    for (int i = 0; i < values.size(); i++) {
      if (values.get(i).value().value() instanceof QualifiedName browseName) {
        browseNames.set(readIndices.get(i), browseName);
      }
    }

    return browseNames;
  }

  /**
   * Read {@code readValueIds} in one call to the {@link AddressSpaceManager}.
   *
   * <p>If that call throws, read them again one at a time, so the failure only affects the
   * operation that caused it. An operation whose own Read throws gets a Bad_InternalError value.
   */
  private List<DataValue> readAll(List<ReadValueId> readValueIds) {
    AddressSpaceManager addressSpaceManager = server.getAddressSpaceManager();

    var readContext = new ReadContext(server, null);

    try {
      return addressSpaceManager.read(readContext, 0.0, TimestampsToReturn.Neither, readValueIds);
    } catch (Exception batchFailure) {
      var values = new ArrayList<DataValue>(readValueIds.size());

      for (ReadValueId readValueId : readValueIds) {
        try {
          DataValue value =
              addressSpaceManager
                  .read(readContext, 0.0, TimestampsToReturn.Neither, List.of(readValueId))
                  .get(0);

          values.add(value);
        } catch (Exception failure) {
          values.add(new DataValue(StatusCodes.Bad_InternalError));
        }
      }

      return values;
    }
  }
}
