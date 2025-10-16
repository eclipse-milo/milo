/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.util;

import java.util.List;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.AddressSpace;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;

public class AnalogItemUtil {

  private static final NodeId ANALOG_ITEM_TYPE = NodeIds.AnalogItemType;

  /**
   * Check if the given NodeId is an AnalogItemType or a subtype of AnalogItemType.
   *
   * @param server the OpcUaServer instance
   * @param nodeId the NodeId to check
   * @return true if the node is an AnalogItemType or a subtype of AnalogItemType
   */
  public static boolean isAnalogItemType(OpcUaServer server, NodeId nodeId) {
    try {
      NodeId typeDefinitionId = getTypeDefinition(server, nodeId);
      return typeDefinitionId != null
          && (typeDefinitionId.equals(ANALOG_ITEM_TYPE)
              || isSubtypeOf(server, typeDefinitionId, ANALOG_ITEM_TYPE));
    } catch (Exception e) {
      return false;
    }
  }

  /**
   * Get the EURange property for an AnalogItemType node.
   *
   * @param server the OpcUaServer instance
   * @param nodeId the NodeId of the AnalogItemType node
   * @return the EURange property value, or null if not found
   */
  public static Range getEuRange(OpcUaServer server, NodeId nodeId) {
    try {
      NodeId euRangeNodeId = getEuRangeNodeId(server, nodeId);
      if (euRangeNodeId != null) {

        AddressSpaceManager addressSpaceManager = server.getAddressSpaceManager();

        DataValue rangeValue =
            addressSpaceManager
                .read(
                    new AddressSpace.ReadContext(server, null),
                    0.0,
                    TimestampsToReturn.Neither,
                    List.of(
                        new ReadValueId(
                            euRangeNodeId,
                            AttributeId.Value.uid(),
                            null,
                            QualifiedName.NULL_VALUE)))
                .get(0);

        if (rangeValue.getValue().isNotNull()) {
          return (Range) rangeValue.getValue().getValue();
        }
      }
      return null;
    } catch (Exception e) {
      return null;
    }
  }

  private static NodeId getTypeDefinition(OpcUaServer server, NodeId nodeId) throws UaException {

    try {
      AddressSpaceManager addressSpaceManager = server.getAddressSpaceManager();
      List<Reference> references = addressSpaceManager.getManagedReferences(nodeId);

      // Find HasTypeDefinition reference
      for (Reference reference : references) {
        if (reference.getReferenceTypeId().equals(NodeIds.HasTypeDefinition)) {
          Optional<NodeId> targetIdOpt =
              reference.getTargetNodeId().toNodeId(server.getNamespaceTable());
          if (targetIdOpt.isPresent()) {
            return targetIdOpt.get();
          }
        }
      }

      return null;
    } catch (Exception e) {
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  private static NodeId getEuRangeNodeId(OpcUaServer server, NodeId nodeId) throws UaException {

    try {
      AddressSpaceManager addressSpaceManager = server.getAddressSpaceManager();
      List<Reference> references = addressSpaceManager.getManagedReferences(nodeId);

      for (Reference reference : references) {
        if (reference.getReferenceTypeId().equals(NodeIds.HasProperty)) {
          Optional<NodeId> targetIdOpt =
              reference.getTargetNodeId().toNodeId(server.getNamespaceTable());
          if (targetIdOpt.isPresent()) {
            NodeId rangeNode = targetIdOpt.get();
            // Get the BrowseName attribute directly instead of using UaNode
            DataValue browseNameValue =
                addressSpaceManager
                    .read(
                        new AddressSpace.ReadContext(server, null),
                        0.0,
                        TimestampsToReturn.Neither,
                        List.of(
                            new ReadValueId(
                                rangeNode,
                                AttributeId.BrowseName.uid(),
                                null,
                                QualifiedName.NULL_VALUE)))
                    .get(0);

            if (browseNameValue.getStatusCode().isGood()) {
              QualifiedName browseName = (QualifiedName) browseNameValue.getValue().getValue();
              if (browseName != null && browseName.equals(new QualifiedName(0, "EURange"))) {
                return rangeNode;
              }
            }
          }
        }
      }

      return null;

    } catch (Exception e) {
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  private static boolean isSubtypeOf(OpcUaServer server, NodeId typeId, NodeId superTypeId) {
    if (typeId.equals(superTypeId)) {
      return true;
    }

    try {

      List<Reference> references = server.getAddressSpaceManager().getManagedReferences(typeId);

      // Filter for inverse HasSubtype references (these point to parent types)
      for (Reference reference : references) {
        if (reference.isInverse() && reference.getReferenceTypeId().equals(NodeIds.HasSubtype)) {
          Optional<NodeId> targetIdOpt =
              reference.getTargetNodeId().toNodeId(server.getNamespaceTable());
          if (targetIdOpt.isPresent()) {
            NodeId targetId = targetIdOpt.get();
            // Check if this parent is our target superTypeId
            if (targetId.equals(superTypeId)) {
              return true;
            }
            // Recursively check the parent's hierarchy
            if (isSubtypeOf(server, targetId, superTypeId)) {
              return true;
            }
          }
        }
      }

      return false;
    } catch (Exception e) {
      return false;
    }
  }
}
