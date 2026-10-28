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

import static org.eclipse.milo.opcua.sdk.server.servicesets.AbstractServiceSet.createResponseHeader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
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

    // Shared by every path in this request, and only this request.
    var browsed = new HashMap<BrowseKey, List<TargetNode>>();

    for (BrowsePath browsePath : browsePaths) {
      results.add(translate(browsePath, browsed));
    }

    ResponseHeader header = createResponseHeader(request);

    return new TranslateBrowsePathsToNodeIdsResponse(
        header, results.toArray(BrowsePathResult[]::new), new DiagnosticInfo[0]);
  }

  private BrowsePathResult translate(
      BrowsePath browsePath, Map<BrowseKey, List<TargetNode>> browsed) {

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
      List<BrowsePathTarget> targets = follow(startingNode, relativePathElements, browsed);

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

  /**
   * Follow {@code elements} from {@code startingNode} and return the targets of the last element.
   *
   * <p>Part 4 §5.9.4.1: "If a Node has multiple targets with the same BrowseName, the Server shall
   * return a list of NodeIds." Every Node that matches an intermediate element is followed, not
   * only the first, and the targets reached through any of them are returned. A Node reached
   * through several matches is followed once, and each target is returned once.
   *
   * @throws UaException with Bad_NoMatch if no Node matches an intermediate element, or as thrown
   *     by {@link #matches}.
   */
  private List<BrowsePathTarget> follow(
      NodeId startingNode,
      List<RelativePathElement> elements,
      Map<BrowseKey, List<TargetNode>> browsed)
      throws UaException {

    Set<NodeId> nodeIds = Set.of(startingNode);

    for (RelativePathElement element : elements.subList(0, elements.size() - 1)) {
      var nextNodeIds = new LinkedHashSet<NodeId>();

      // Only Nodes on this Server have their BrowseNames read, so every match is one of them.
      for (ExpandedNodeId match : matches(nodeIds, element, browsed)) {
        match.toNodeId(server.getNamespaceTable()).ifPresent(nextNodeIds::add);
      }

      if (nextNodeIds.isEmpty()) {
        throw new UaException(StatusCodes.Bad_NoMatch);
      }

      nodeIds = nextNodeIds;
    }

    var targetIds = new LinkedHashSet<ExpandedNodeId>();

    for (ExpandedNodeId match : matches(nodeIds, elements.get(elements.size() - 1), browsed)) {
      targetIds.add(BrowseUtil.normalize(match, server.getNamespaceTable()));
    }

    return targetIds.stream().map(id -> new BrowsePathTarget(id, UInteger.MAX)).toList();
  }

  /**
   * Get the targets of {@code element}'s references from each of {@code nodeIds} whose BrowseName
   * matches the element's target name, in the order found.
   *
   * <p>A Node that can't be browsed, or has no reference that passes the filters, contributes no
   * matches. Only if every Node fails is the first failure thrown, so a path through a single Node
   * fails the same way it would on its own.
   *
   * @throws UaException with Bad_BrowseNameInvalid if the element's target name is null, or the
   *     first failure from {@link #targetNodes} if it failed for every Node.
   */
  private List<ExpandedNodeId> matches(
      Set<NodeId> nodeIds, RelativePathElement element, Map<BrowseKey, List<TargetNode>> browsed)
      throws UaException {

    QualifiedName targetName = element.getTargetName();

    if (targetName.isNull()) {
      throw new UaException(StatusCodes.Bad_BrowseNameInvalid);
    }

    var matches = new ArrayList<ExpandedNodeId>();

    UaException firstFailure = null;
    boolean browsedAny = false;

    for (NodeId nodeId : nodeIds) {
      try {
        for (TargetNode targetNode : targetNodes(nodeId, element, browsed)) {
          if (targetName.equals(targetNode.browseName())) {
            matches.add(targetNode.nodeId());
          }
        }

        browsedAny = true;
      } catch (UaException e) {
        if (firstFailure == null) {
          firstFailure = e;
        }
      }
    }

    if (!browsedAny && firstFailure != null) {
      throw firstFailure;
    }

    return matches;
  }

  /**
   * Get the Nodes that {@code element}'s references lead to from {@code nodeId}, with their
   * BrowseNames, before any matching on the element's target name.
   *
   * <p>Paths in one request often share their leading elements. Each Node and reference filter is
   * browsed and read once per request; later paths reuse the result from {@code browsed}.
   *
   * @throws UaException with Bad_NodeIdUnknown if {@code nodeId} can't be browsed, or Bad_NoMatch
   *     if no reference passes the filters.
   */
  private List<TargetNode> targetNodes(
      NodeId nodeId, RelativePathElement element, Map<BrowseKey, List<TargetNode>> browsed)
      throws UaException {

    var key =
        new BrowseKey(
            nodeId,
            element.getReferenceTypeId(),
            element.getIncludeSubtypes(),
            element.getIsInverse());

    List<TargetNode> targetNodes = browsed.get(key);

    if (targetNodes == null) {
      List<ExpandedNodeId> targetNodeIds = browseTargets(key);
      List<QualifiedName> browseNames = readBrowseNames(targetNodeIds);

      targetNodes = new ArrayList<>(targetNodeIds.size());

      for (int i = 0; i < targetNodeIds.size(); i++) {
        targetNodes.add(new TargetNode(targetNodeIds.get(i), browseNames.get(i)));
      }

      browsed.put(key, targetNodes);
    }

    return targetNodes;
  }

  /**
   * Browse {@code key}'s Node and return the targets of the references that pass its ReferenceType
   * and direction filters, in reference order.
   *
   * @throws UaException with Bad_NodeIdUnknown if the Node can't be browsed, or Bad_NoMatch if no
   *     reference passes the filters.
   */
  private List<ExpandedNodeId> browseTargets(BrowseKey key) throws UaException {
    NodeId referenceTypeId = key.referenceTypeId();

    var browseContext = new BrowseContext(server, context.getSession().orElse(null));

    var view = new ViewDescription(NodeId.NULL_VALUE, DateTime.NULL_VALUE, UInteger.valueOf(0));

    AddressSpace.ReferenceResult result =
        server.getAddressSpaceManager().browse(browseContext, view, List.of(key.nodeId())).get(0);

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
              || (key.includeSubtypes() && referenceTypeTree.isSubtypeOf(typeId, referenceTypeId));

      if (typeMatches && reference.isInverse() == key.isInverse()) {
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

  /** A Node to browse, and the ReferenceType and direction filters to apply to its references. */
  private record BrowseKey(
      NodeId nodeId, NodeId referenceTypeId, boolean includeSubtypes, boolean isInverse) {}

  /**
   * A target of a browsed reference and its BrowseName, or the null QualifiedName if it couldn't be
   * read.
   */
  private record TargetNode(ExpandedNodeId nodeId, QualifiedName browseName) {}
}
