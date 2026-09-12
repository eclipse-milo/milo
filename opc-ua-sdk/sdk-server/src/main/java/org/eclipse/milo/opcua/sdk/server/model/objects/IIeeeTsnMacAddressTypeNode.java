/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import com.digitalpetri.opcua.uanodeset.runtime.members.MemberDeclaration;
import com.digitalpetri.opcua.uanodeset.runtime.server.ServerMembers;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.nodes.VariableNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

public class IIeeeTsnMacAddressTypeNode extends BaseInterfaceTypeNode
    implements IIeeeTsnMacAddressType {
  public IIeeeTsnMacAddressTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions,
      UByte eventNotifier) {
    super(
        context,
        nodeId,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions,
        eventNotifier);
  }

  public IIeeeTsnMacAddressTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType[] rolePermissions,
      RolePermissionType[] userRolePermissions,
      AccessRestrictionType accessRestrictions) {
    super(
        context,
        nodeId,
        browseName,
        displayName,
        description,
        writeMask,
        userWriteMask,
        rolePermissions,
        userRolePermissions,
        accessRestrictions);
  }

  @Override
  public Optional<VariableNode> getPropertyNode(QualifiedName browseName) {
    return findNode(
            browseName,
            n -> n instanceof VariableNode,
            r ->
                r.isForward()
                    && (r.getReferenceTypeId().equals(NodeIds.HasProperty)
                        || getNodeContext()
                            .getServer()
                            .getReferenceTypeTree()
                            .isSubtypeOf(r.getReferenceTypeId(), NodeIds.HasProperty)))
        .map(n -> (VariableNode) n);
  }

  @Override
  public BaseDataVariableTypeNode getDestinationAddressNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DestinationAddress",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DestinationAddress (declaration i=24200, owner"
                + " i=24199)"));
  }

  @Override
  public @Nullable UByte @Nullable [] getDestinationAddress() {
    var node = getDestinationAddressNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DestinationAddress (declaration i=24200, owner i=24199)"
              + " on "
              + getNodeId());
    }
    return (UByte[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setDestinationAddress(@Nullable UByte @Nullable [] value) {
    var node = getDestinationAddressNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DestinationAddress (declaration i=24200, owner i=24199)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getSourceAddressNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "SourceAddress",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:SourceAddress (declaration i=24201, owner i=24199)"));
  }

  @Override
  public @Nullable UByte @Nullable [] getSourceAddress() {
    var node = getSourceAddressNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SourceAddress (declaration i=24201, owner i=24199)"
              + " on "
              + getNodeId());
    }
    return (UByte[]) node.getValue().getValue().getValue();
  }

  @Override
  public void setSourceAddress(@Nullable UByte @Nullable [] value) {
    var node = getSourceAddressNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:SourceAddress (declaration i=24201, owner i=24199)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }
}
