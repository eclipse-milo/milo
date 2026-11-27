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

import static org.eclipse.milo.opcua.sdk.server.util.UaEnumUtil.browseResultMasks;

import com.google.common.primitives.Ints;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.server.AccessContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.BrowseContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpace.ReadContext;
import org.eclipse.milo.opcua.sdk.server.ContinuationPoint;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.stack.core.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ViewDescription;
import org.eclipse.milo.opcua.stack.core.util.Lists;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowseHelper {

  private static final Logger LOGGER = LoggerFactory.getLogger(BrowseHelper.class);

  private static final BrowseAttributes EMPTY_BROWSE_ATTRIBUTES =
      new BrowseAttributes(
          QualifiedName.NULL_VALUE, LocalizedText.NULL_VALUE, NodeClass.Unspecified);

  public static List<BrowseResult> browse(
      OpcUaServer server, AccessContext context, BrowseRequest browseRequest) {

    Session session = context.getSession().orElseThrow();

    List<PendingBrowse> pending =
        Lists.ofNullable(browseRequest.getNodesToBrowse()).stream()
            .map(PendingBrowse::new)
            .toList();

    List<NodeId> nodeIds = pending.stream().map(pb -> pb.browseDescription.getNodeId()).toList();

    Map<NodeId, AccessResult> accessResults =
        server.getAccessController().checkBrowseAccess(session, nodeIds);

    for (PendingBrowse pb : pending) {
      if (pb.browseDescription.getBrowseDirection() == null
          || pb.browseDescription.getBrowseDirection() == BrowseDirection.Invalid) {

        pb.referenceDescriptionResult =
            ReferenceDescriptionResult.of(StatusCodes.Bad_BrowseDirectionInvalid);
      } else {
        NodeId referenceTypeId = pb.browseDescription.getReferenceTypeId();

        if (referenceTypeId.isNotNull()
            && !server.getReferenceTypeTree().containsType(referenceTypeId)) {

          pb.referenceDescriptionResult =
              ReferenceDescriptionResult.of(StatusCodes.Bad_ReferenceTypeIdInvalid);
        } else {
          AccessResult result = accessResults.get(pb.browseDescription.getNodeId());

          if (result.isDenied()) {
            pb.referenceDescriptionResult = ReferenceDescriptionResult.of(Collections.emptyList());
          }
        }
      }
    }

    // Operations resolved above keep their results; the rest are browsed, and each result goes
    // back to the operation it came from.
    List<PendingBrowse> toBrowse =
        pending.stream().filter(pb -> pb.referenceDescriptionResult == null).toList();

    List<BrowseDescription> nodesToBrowse =
        toBrowse.stream().map(pb -> pb.browseDescription).toList();

    List<AddressSpace.ReferenceResult> referenceResults =
        server
            .getAddressSpaceManager()
            .browse(
                new BrowseContext(server, session),
                browseRequest.getView(),
                nodesToBrowse.stream()
                    .map(BrowseDescription::getNodeId)
                    .collect(Collectors.toList()));

    List<ReferenceDescriptionResult> referenceDescriptionLists =
        createReferenceDescriptions(server, nodesToBrowse, referenceResults);

    for (int i = 0; i < toBrowse.size(); i++) {
      PendingBrowse pb = toBrowse.get(i);
      ReferenceDescriptionResult referenceDescriptionResult = referenceDescriptionLists.get(i);

      if (referenceDescriptionResult instanceof ReferenceDescriptionResult.BadNoReferences) {
        pb.referenceDescriptionResult = referenceDescriptionResult;
      } else if (referenceDescriptionResult
          instanceof ReferenceDescriptionResult.ReferenceDescriptionList rdl) {
        // Every target in rdl was normalized against ServerTable and NamespaceTable above, so
        // local targets (including any originally addressed by this server's URI) resolve to a
        // local NodeId and are checked; only remote targets skip checkBrowseAccess.
        List<NodeId> nodeIdsToCheck =
            rdl.referenceDescriptions.stream()
                .flatMap(r -> r.getNodeId().toNodeId(server.getNamespaceTable()).stream())
                .toList();

        Map<NodeId, AccessResult> referenceAccessResults =
            nodeIdsToCheck.isEmpty()
                ? Map.of()
                : server.getAccessController().checkBrowseAccess(session, nodeIdsToCheck);

        var filteredReferences = new ArrayList<ReferenceDescription>();

        for (ReferenceDescription reference : rdl.referenceDescriptions) {
          Optional<NodeId> localNodeId = reference.getNodeId().toNodeId(server.getNamespaceTable());

          if (localNodeId.isEmpty()) {
            filteredReferences.add(reference);
          } else {
            AccessResult result = referenceAccessResults.get(localNodeId.get());

            if (result != null && result.isAllowed()) {
              filteredReferences.add(reference);
            }
          }
        }

        // Filter out references to local Nodes that the Session doesn't have Browse permission for.
        pb.referenceDescriptionResult = ReferenceDescriptionResult.of(filteredReferences);
      }
    }

    // Gather all the ReferenceDescription lists: the ones we initially filtered out due to
    // lack of Browse permission and the ones from the actual Browse.
    List<ReferenceDescriptionResult> allReferenceDescriptionResults =
        pending.stream().map(pb -> pb.referenceDescriptionResult).toList();

    int max =
        browseRequest.getRequestedMaxReferencesPerNode().longValue() == 0
            ? Integer.MAX_VALUE
            : Ints.saturatedCast(browseRequest.getRequestedMaxReferencesPerNode().longValue());

    return createBrowseResults(server, session, max, allReferenceDescriptionResults);
  }

  private static List<ReferenceDescriptionResult> createReferenceDescriptions(
      OpcUaServer server,
      List<BrowseDescription> nodesToBrowse,
      List<AddressSpace.ReferenceResult> referenceResults) {

    var referenceDescriptionsList = new ArrayList<ReferenceDescriptionResult>();

    NamespaceTable namespaceTable = server.getNamespaceTable();
    ServerTable serverTable = server.getServerTable();

    for (int i = 0; i < nodesToBrowse.size(); i++) {
      BrowseDescription browseDescription = nodesToBrowse.get(i);
      AddressSpace.ReferenceResult result = referenceResults.get(i);

      List<Reference> references;
      if (result instanceof AddressSpace.ReferenceResult.ReferenceList r) {
        references = r.references();
      } else {
        referenceDescriptionsList.add(ReferenceDescriptionResult.of(StatusCodes.Bad_NodeIdUnknown));
        continue;
      }

      List<Reference> stage1Refs =
          references.stream()
              .filter(reference -> filterDirection(browseDescription, reference))
              .filter(reference -> filterReferenceType(server, browseDescription, reference))
              .toList();

      // Resolve and normalize every target once against ServerTable and NamespaceTable so local
      // targets addressed by this server's URI are classified as local, and unrepresentable targets
      // (unregistered server/namespace) are skipped before attribute reads, filtering, or encoding.
      var resolvedRefs = new ArrayList<ResolvedReference>(stage1Refs.size());
      for (Reference reference : stage1Refs) {
        Optional<ExpandedNodeId> normalizedTarget =
            BrowseUtil.normalize(reference.getTargetNodeId(), serverTable, namespaceTable);

        if (normalizedTarget.isEmpty()) {
          LOGGER.warn(
              "Skipping Browse reference from {} to unrepresentable target {}",
              browseDescription.getNodeId(),
              reference.getTargetNodeId().toParseableString());
          continue;
        }

        resolvedRefs.add(new ResolvedReference(reference, normalizedTarget.get()));
      }

      List<ExpandedNodeId> targetNodeIds =
          resolvedRefs.stream().map(ResolvedReference::targetNodeId).toList();

      List<BrowseAttributes> browseAttributes = readBrowseAttributes(server, targetNodeIds);

      List<ExpandedNodeId> typeDefinitionIds =
          browseTypeDefinitions(
              server, targetNodeIds, browseAttributes, browseDescription.getResultMask());

      var referenceDescriptions = new ArrayList<ReferenceDescription>();

      for (int j = 0; j < resolvedRefs.size(); j++) {
        ResolvedReference resolved = resolvedRefs.get(j);
        BrowseAttributes attributes = browseAttributes.get(j);
        ExpandedNodeId typeDefinitionId = typeDefinitionIds.get(j);

        if (filterNodeClass(
            browseDescription, resolved.targetNodeId(), serverTable, attributes.nodeClass())) {
          ReferenceDescription referenceDescription =
              createReferenceDescription(
                  browseDescription,
                  resolved.reference(),
                  resolved.targetNodeId(),
                  attributes,
                  typeDefinitionId);

          referenceDescriptions.add(referenceDescription);
        }
      }

      referenceDescriptionsList.add(ReferenceDescriptionResult.of(referenceDescriptions));
    }

    return referenceDescriptionsList;
  }

  private static ReferenceDescription createReferenceDescription(
      BrowseDescription browseDescription,
      Reference reference,
      ExpandedNodeId normalizedTargetNodeId,
      BrowseAttributes browseAttributes,
      ExpandedNodeId typeDefinitionId) {

    EnumSet<BrowseResultMask> masks =
        browseResultMasks(browseDescription.getResultMask().longValue());

    NodeId referenceTypeId =
        masks.contains(BrowseResultMask.ReferenceTypeId)
            ? reference.getReferenceTypeId()
            : NodeId.NULL_VALUE;

    boolean forward = masks.contains(BrowseResultMask.IsForward) && reference.isForward();

    return new ReferenceDescription(
        referenceTypeId,
        forward,
        normalizedTargetNodeId,
        masks.contains(BrowseResultMask.BrowseName)
            ? Objects.requireNonNullElse(browseAttributes.browseName(), QualifiedName.NULL_VALUE)
            : QualifiedName.NULL_VALUE,
        masks.contains(BrowseResultMask.DisplayName)
            ? Objects.requireNonNullElse(browseAttributes.displayName(), LocalizedText.NULL_VALUE)
            : LocalizedText.NULL_VALUE,
        masks.contains(BrowseResultMask.NodeClass)
            ? Objects.requireNonNullElse(browseAttributes.nodeClass(), NodeClass.Unspecified)
            : NodeClass.Unspecified,
        typeDefinitionId);
  }

  private static List<BrowseResult> createBrowseResults(
      OpcUaServer server,
      Session session,
      int max,
      List<ReferenceDescriptionResult> allReferenceDescriptionResults) {

    int maxBrowseContinuationPoints =
        server.getConfig().getLimits().getMaxBrowseContinuationPoints().intValue();

    var browseResults = new BrowseResult[allReferenceDescriptionResults.size()];
    var pagedResults = new ArrayList<PendingContinuation>();

    for (int i = 0; i < allReferenceDescriptionResults.size(); i++) {
      ReferenceDescriptionResult referenceDescriptionResult = allReferenceDescriptionResults.get(i);

      if (referenceDescriptionResult instanceof ReferenceDescriptionResult.BadNoReferences bnr) {
        browseResults[i] = new BrowseResult(bnr.statusCode, null, new ReferenceDescription[0]);
      } else if (referenceDescriptionResult
          instanceof ReferenceDescriptionResult.ReferenceDescriptionList rdl) {
        List<ReferenceDescription> references = rdl.referenceDescriptions;

        if (references.size() > max) {
          pagedResults.add(new PendingContinuation(i, references));
        } else {
          browseResults[i] =
              new BrowseResult(
                  StatusCode.GOOD, null, references.toArray(new ReferenceDescription[0]));
        }
      } else {
        // Skipping an operation would shift every later result onto the wrong operation.
        throw new IllegalStateException("no result for a BrowseDescription");
      }
    }

    if (!pagedResults.isEmpty()) {
      int candidateCount =
          maxBrowseContinuationPoints > 0
              ? Math.min(pagedResults.size(), maxBrowseContinuationPoints)
              : pagedResults.size();

      var continuationPoints = new ArrayList<ContinuationPoint>(candidateCount);
      var firstPages = new ArrayList<List<ReferenceDescription>>(candidateCount);

      for (int i = 0; i < candidateCount; i++) {
        List<ReferenceDescription> snapshot = List.copyOf(pagedResults.get(i).references());
        List<ReferenceDescription> firstPage = List.copyOf(snapshot.subList(0, max));

        firstPages.add(firstPage);
        continuationPoints.add(ContinuationPoint.ofRemaining(snapshot, max, max));
      }

      int allocated =
          session.addBrowseContinuationPoints(continuationPoints, maxBrowseContinuationPoints);

      for (int i = 0; i < pagedResults.size(); i++) {
        int resultIndex = pagedResults.get(i).index();

        if (i < allocated) {
          browseResults[resultIndex] =
              new BrowseResult(
                  StatusCode.GOOD,
                  continuationPoints.get(i).id(),
                  firstPages.get(i).toArray(new ReferenceDescription[0]));
        } else {
          browseResults[resultIndex] =
              new BrowseResult(
                  new StatusCode(StatusCodes.Bad_NoContinuationPoints),
                  null,
                  new ReferenceDescription[0]);
        }
      }
    }

    return new ArrayList<>(Arrays.asList(browseResults));
  }

  private static List<BrowseAttributes> readBrowseAttributes(
      OpcUaServer server, List<ExpandedNodeId> nodeIds) {

    var browseAttributes =
        new ArrayList<>(Collections.nCopies(nodeIds.size(), EMPTY_BROWSE_ATTRIBUTES));

    // Only targets that resolve to a local NodeId are read from the local AddressSpaceManager;
    // remote targets and unregistered namespaces keep EMPTY_BROWSE_ATTRIBUTES.
    var readValueIds = new ArrayList<ReadValueId>(nodeIds.size() * 3);
    var readIndices = new ArrayList<Integer>(nodeIds.size());

    NamespaceTable namespaceTable = server.getNamespaceTable();

    for (int i = 0; i < nodeIds.size(); i++) {
      Optional<NodeId> localNodeId = nodeIds.get(i).toNodeId(namespaceTable);

      if (localNodeId.isPresent()) {
        NodeId nodeId = localNodeId.get();
        readValueIds.add(
            new ReadValueId(nodeId, AttributeId.BrowseName.uid(), null, QualifiedName.NULL_VALUE));
        readValueIds.add(
            new ReadValueId(nodeId, AttributeId.DisplayName.uid(), null, QualifiedName.NULL_VALUE));
        readValueIds.add(
            new ReadValueId(nodeId, AttributeId.NodeClass.uid(), null, QualifiedName.NULL_VALUE));
        readIndices.add(i);
      }
    }

    if (readValueIds.isEmpty()) {
      return browseAttributes;
    }

    var context = new ReadContext(server, null);

    List<DataValue> values =
        server
            .getAddressSpaceManager()
            .read(context, 0.0, TimestampsToReturn.Neither, readValueIds);

    for (int i = 0; i < readIndices.size(); i++) {
      int base = i * 3;
      QualifiedName browseName =
          extractAttribute(values.get(base), QualifiedName.class, QualifiedName.NULL_VALUE);
      LocalizedText displayName =
          extractAttribute(values.get(base + 1), LocalizedText.class, LocalizedText.NULL_VALUE);
      NodeClass nodeClass =
          extractAttribute(values.get(base + 2), NodeClass.class, NodeClass.Unspecified);

      browseAttributes.set(
          readIndices.get(i), new BrowseAttributes(browseName, displayName, nodeClass));
    }

    return browseAttributes;
  }

  private static <T> T extractAttribute(@Nullable DataValue dataValue, Class<T> type, T fallback) {
    if (dataValue == null) {
      return fallback;
    }

    StatusCode statusCode = dataValue.statusCode();
    if (statusCode != null && statusCode.isBad()) {
      return fallback;
    }

    Object value = dataValue.value() != null ? dataValue.value().value() : null;
    return type.isInstance(value) ? type.cast(value) : fallback;
  }

  private static List<ExpandedNodeId> browseTypeDefinitions(
      OpcUaServer server,
      List<ExpandedNodeId> nodeIds,
      List<BrowseAttributes> browseAttributes,
      UInteger resultMask) {

    EnumSet<BrowseResultMask> resultMasks = browseResultMasks(resultMask.longValue());

    if (!resultMasks.contains(BrowseResultMask.TypeDefinition)) {
      return Collections.nCopies(nodeIds.size(), ExpandedNodeId.NULL_VALUE);
    }

    var typeDefinitions = new ArrayList<ExpandedNodeId>();

    for (int i = 0; i < nodeIds.size(); i++) {
      NamespaceTable namespaceTable = server.getNamespaceTable();
      NodeId nodeId = nodeIds.get(i).toNodeId(namespaceTable).orElse(NodeId.NULL_VALUE);

      BrowseAttributes attributes = browseAttributes.get(i);
      NodeClass nodeClass = attributes.nodeClass;

      if (nodeId.isNull() || nodeClass == null) {
        typeDefinitions.add(ExpandedNodeId.NULL_VALUE);
      } else {
        switch (attributes.nodeClass) {
          case Object, Variable -> {
            try {
              ExpandedNodeId typeDefinitionId = browseTypeDefinition(server, nodeId);
              typeDefinitionId =
                  BrowseUtil.normalize(typeDefinitionId, server.getServerTable(), namespaceTable)
                      .orElse(ExpandedNodeId.NULL_VALUE);

              typeDefinitions.add(typeDefinitionId);
            } catch (UaException e) {
              LOGGER.error("Error browsing TypeDefinition for nodeId={}", nodeId, e);

              typeDefinitions.add(ExpandedNodeId.NULL_VALUE);
            }
          }
          default -> typeDefinitions.add(ExpandedNodeId.NULL_VALUE);
        }
      }
    }

    return typeDefinitions;
  }

  private static ExpandedNodeId browseTypeDefinition(OpcUaServer server, NodeId nodeId)
      throws UaException {
    ExpandedNodeId typeDefinitionId =
        server
            .getAddressSpaceManager()
            .getManagedReferences(nodeId, Reference.HAS_TYPE_DEFINITION_PREDICATE)
            .stream()
            .findFirst()
            .map(Reference::getTargetNodeId)
            .orElse(null);

    if (typeDefinitionId != null) {
      return typeDefinitionId;
    } else {
      LOGGER.debug("No managed TypeDefinition for nodeId={}, browsing...", nodeId);

      var browseContext = new BrowseContext(server, null);

      var view = new ViewDescription(NodeId.NULL_VALUE, DateTime.NULL_VALUE, UInteger.valueOf(0));

      AddressSpace.ReferenceResult result =
          server.getAddressSpaceManager().browse(browseContext, view, List.of(nodeId)).get(0);

      if (result instanceof AddressSpace.ReferenceResult.ReferenceList rl) {
        List<Reference> references = rl.references();

        return references.stream()
            .filter(r -> NodeIds.HasTypeDefinition.equals(r.getReferenceTypeId()))
            .filter(Reference::isForward)
            .findFirst()
            .map(Reference::getTargetNodeId)
            .orElse(ExpandedNodeId.NULL_VALUE);
      } else {
        return ExpandedNodeId.NULL_VALUE;
      }
    }
  }

  private static boolean filterDirection(BrowseDescription browseDescription, Reference reference) {

    return switch (browseDescription.getBrowseDirection()) {
      case Forward -> reference.isForward();
      case Inverse -> reference.isInverse();
      default -> true;
    };
  }

  private static boolean filterReferenceType(
      OpcUaServer server, BrowseDescription browseDescription, Reference reference) {

    ReferenceTypeTree referenceTypeTree = server.getReferenceTypeTree();
    NodeId referenceTypeId = browseDescription.getReferenceTypeId();

    boolean includeAny = referenceTypeId == null || referenceTypeId.isNull();
    boolean includeSubtypes = browseDescription.getIncludeSubtypes();

    return includeAny
        || reference.getReferenceTypeId().equals(referenceTypeId)
        || (includeSubtypes
            && referenceTypeTree.isSubtypeOf(reference.getReferenceTypeId(), referenceTypeId));
  }

  /**
   * Filter a target Node against {@link BrowseDescription#getNodeClassMask()} per OPC 10000-4
   * §5.9.2.2 Table 34.
   *
   * <p>A mask of {@code 0} matches every target, including remote targets and local targets whose
   * NodeClass is unknown ({@link NodeClass#Unspecified}). For a non-zero mask, a remote target
   * whose NodeClass is unknown ignores the mask and is returned, while a local target with an
   * unknown NodeClass is excluded because {@link NodeClass#Unspecified} has no mask bit.
   */
  static boolean filterNodeClass(
      BrowseDescription browseDescription,
      ExpandedNodeId targetNodeId,
      ServerTable serverTable,
      NodeClass nodeClass) {

    long mask = browseDescription.getNodeClassMask().longValue();

    if (mask == 0L) {
      return true;
    }

    if (!targetNodeId.isLocal(serverTable)
        && (nodeClass == null || nodeClass == NodeClass.Unspecified)) {
      return true;
    }

    return nodeClass != null
        && nodeClass != NodeClass.Unspecified
        && (mask & nodeClass.getValue()) == nodeClass.getValue();
  }

  private record ResolvedReference(Reference reference, ExpandedNodeId targetNodeId) {}

  private record BrowseAttributes(
      QualifiedName browseName, LocalizedText displayName, NodeClass nodeClass) {

    private BrowseAttributes {
      browseName = Objects.requireNonNullElse(browseName, QualifiedName.NULL_VALUE);
      displayName = Objects.requireNonNullElse(displayName, LocalizedText.NULL_VALUE);
      nodeClass = Objects.requireNonNullElse(nodeClass, NodeClass.Unspecified);
    }
  }

  private record PendingContinuation(int index, List<ReferenceDescription> references) {}

  private static class PendingBrowse {

    ReferenceDescriptionResult referenceDescriptionResult;
    final BrowseDescription browseDescription;

    PendingBrowse(BrowseDescription browseDescription) {
      this.browseDescription = browseDescription;
    }
  }

  private sealed interface ReferenceDescriptionResult {

    record BadNoReferences(StatusCode statusCode) implements ReferenceDescriptionResult {}

    record ReferenceDescriptionList(List<ReferenceDescription> referenceDescriptions)
        implements ReferenceDescriptionResult {}

    static BadNoReferences of(long statusCode) {
      return of(new StatusCode(statusCode));
    }

    static BadNoReferences of(StatusCode statusCode) {
      return new BadNoReferences(statusCode);
    }

    static ReferenceDescriptionList of(List<ReferenceDescription> referenceDescriptions) {
      return new ReferenceDescriptionList(referenceDescriptions);
    }
  }
}
