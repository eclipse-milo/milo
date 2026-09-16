/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.conditions;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * Shared construction of vendor Condition ObjectType fixtures: a subtype of a stock alarm type
 * declaring a mandatory UInt32 member, modeled the way the SDK's instantiation and adoption paths
 * expect it (HasSubtype on the type; HasTypeDefinition and a Mandatory ModellingRule on the member
 * declaration).
 */
final class VendorTypeFixtures {

  private VendorTypeFixtures() {}

  /** Define a vendor ObjectType subtyping {@code supertypeId} and add it to {@code nodeManager}. */
  static UaObjectTypeNode defineVendorSubtype(
      UaNodeContext context,
      UaNodeManager nodeManager,
      NodeId typeId,
      QualifiedName browseName,
      NodeId supertypeId) {

    UaObjectTypeNode typeNode =
        new UaObjectTypeNode(
            context,
            typeId,
            browseName,
            LocalizedText.english(browseName.name()),
            LocalizedText.NULL_VALUE,
            uint(0),
            uint(0),
            false);
    typeNode.addReference(new Reference(typeId, NodeIds.HasSubtype, supertypeId.expanded(), false));
    nodeManager.addNode(typeNode);

    return typeNode;
  }

  /**
   * Declare a mandatory UInt32 member on {@code typeNode} and add the declaration to {@code
   * nodeManager}.
   *
   * @param memberTypeDefinitionId the member's VariableType, e.g. PropertyType or
   *     BaseDataVariableType.
   * @param memberReferenceTypeId the declaring reference, e.g. HasProperty or HasComponent.
   */
  static UaVariableNode declareMandatoryUInt32Member(
      UaNodeManager nodeManager,
      UaObjectTypeNode typeNode,
      NodeId memberId,
      QualifiedName memberName,
      NodeId memberTypeDefinitionId,
      NodeId memberReferenceTypeId,
      UInteger initialValue) {

    UaVariableNode declaration =
        new UaVariableNode(
            typeNode.getNodeContext(),
            memberId,
            memberName,
            LocalizedText.english(memberName.name()),
            LocalizedText.NULL_VALUE,
            uint(0),
            uint(0));
    declaration.setDataType(NodeIds.UInt32);
    declaration.setValue(new DataValue(new Variant(initialValue)));
    declaration.addReference(
        new Reference(
            memberId, NodeIds.HasTypeDefinition, memberTypeDefinitionId.expanded(), true));
    declaration.addReference(
        new Reference(
            memberId, NodeIds.HasModellingRule, NodeIds.ModellingRule_Mandatory.expanded(), true));
    nodeManager.addNode(declaration);

    typeNode.addReference(
        new Reference(typeNode.getNodeId(), memberReferenceTypeId, memberId.expanded(), true));

    return declaration;
  }
}
