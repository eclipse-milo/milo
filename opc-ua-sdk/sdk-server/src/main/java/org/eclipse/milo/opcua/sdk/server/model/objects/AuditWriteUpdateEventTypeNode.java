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
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
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

public class AuditWriteUpdateEventTypeNode extends AuditUpdateEventTypeNode
    implements AuditWriteUpdateEventType {
  public AuditWriteUpdateEventTypeNode(
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

  public AuditWriteUpdateEventTypeNode(
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
  public PropertyTypeNode getAttributeIdNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "AttributeId",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:AttributeId (declaration i=2750, owner i=2100)"));
  }

  @Override
  public @Nullable UInteger getAttributeId() {
    var node = getAttributeIdNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AttributeId (declaration i=2750, owner i=2100)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setAttributeId(@Nullable UInteger value) {
    var node = getAttributeIdNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AttributeId (declaration i=2750, owner i=2100)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getIndexRangeNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "IndexRange",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:IndexRange (declaration i=2101, owner i=2100)"));
  }

  @Override
  public @Nullable String getIndexRange() {
    var node = getIndexRangeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:IndexRange (declaration i=2101, owner i=2100)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setIndexRange(@Nullable String value) {
    var node = getIndexRangeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:IndexRange (declaration i=2101, owner i=2100)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getOldValueNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "OldValue",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:OldValue (declaration i=2102, owner i=2100)"));
  }

  @Override
  public @Nullable Object getOldValue() {
    var node = getOldValueNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:OldValue (declaration i=2102, owner i=2100)"
              + " on "
              + getNodeId());
    }
    return (Object) node.getValue().getValue().getValue();
  }

  @Override
  public void setOldValue(@Nullable Object value) {
    var node = getOldValueNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:OldValue (declaration i=2102, owner i=2100)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getNewValueNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "NewValue",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:NewValue (declaration i=2103, owner i=2100)"));
  }

  @Override
  public @Nullable Object getNewValue() {
    var node = getNewValueNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NewValue (declaration i=2103, owner i=2100)"
              + " on "
              + getNodeId());
    }
    return (Object) node.getValue().getValue().getValue();
  }

  @Override
  public void setNewValue(@Nullable Object value) {
    var node = getNewValueNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:NewValue (declaration i=2103, owner i=2100)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }
}
