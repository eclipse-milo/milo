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
import org.eclipse.milo.opcua.sdk.server.model.variables.AnalogUnitTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceAdminStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceOperStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

public class IIetfBaseNetworkInterfaceTypeNode extends BaseInterfaceTypeNode
    implements IIetfBaseNetworkInterfaceType {
  public IIetfBaseNetworkInterfaceTypeNode(
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

  public IIetfBaseNetworkInterfaceTypeNode(
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
  public BaseDataVariableTypeNode getAdminStatusNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "AdminStatus",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner i=24148)"));
  }

  @Override
  public @Nullable InterfaceAdminStatus getAdminStatus() {
    var node = getAdminStatusNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner i=24148)"
              + " on "
              + getNodeId());
    }
    Object value = node.getValue().getValue().getValue();
    Object convertedValue;
    {
      if (value == null || value instanceof Matrix && ((Matrix) value).isNull()) {
        convertedValue = null;
      } else {
        Object elements = value instanceof Matrix ? ((Matrix) value).getElements() : value;
        int rank =
            value instanceof Matrix
                ? ((Matrix) value).getValueRank()
                : ArrayUtil.getValueRank(value);
        boolean permitted = rank == -1;
        if (!permitted) {
          throw new UaRuntimeException(
              StatusCodes.Bad_TypeMismatch,
              "AdminStatus: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof InterfaceAdminStatus)) {
          if (!(value instanceof Integer)) {
            throw new UaRuntimeException(
                StatusCodes.Bad_TypeMismatch,
                "AdminStatus: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceAdminStatus or"
                    + " Int32, got "
                    + value);
          }
          if (InterfaceAdminStatus.from((Integer) value) == null) {
            throw new UaRuntimeException(
                StatusCodes.Bad_OutOfRange,
                "AdminStatus: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceAdminStatus"
                    + " value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof InterfaceAdminStatus
                ? (InterfaceAdminStatus) value
                : InterfaceAdminStatus.from((Integer) value);
      }
    }
    return (InterfaceAdminStatus) convertedValue;
  }

  @Override
  public void setAdminStatus(@Nullable InterfaceAdminStatus value) {
    var node = getAdminStatusNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AdminStatus (declaration i=24149, owner i=24148)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public BaseDataVariableTypeNode getOperStatusNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "OperStatus",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner i=24148)"));
  }

  @Override
  public @Nullable InterfaceOperStatus getOperStatus() {
    var node = getOperStatusNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner i=24148)"
              + " on "
              + getNodeId());
    }
    Object value = node.getValue().getValue().getValue();
    Object convertedValue;
    {
      if (value == null || value instanceof Matrix && ((Matrix) value).isNull()) {
        convertedValue = null;
      } else {
        Object elements = value instanceof Matrix ? ((Matrix) value).getElements() : value;
        int rank =
            value instanceof Matrix
                ? ((Matrix) value).getValueRank()
                : ArrayUtil.getValueRank(value);
        boolean permitted = rank == -1;
        if (!permitted) {
          throw new UaRuntimeException(
              StatusCodes.Bad_TypeMismatch,
              "OperStatus: ValueRank=-1 does not permit rank " + rank);
        }
        if (value != null && !((Object) value instanceof InterfaceOperStatus)) {
          if (!(value instanceof Integer)) {
            throw new UaRuntimeException(
                StatusCodes.Bad_TypeMismatch,
                "OperStatus: expected"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceOperStatus or"
                    + " Int32, got "
                    + value);
          }
          if (InterfaceOperStatus.from((Integer) value) == null) {
            throw new UaRuntimeException(
                StatusCodes.Bad_OutOfRange,
                "OperStatus: unknown"
                    + " org.eclipse.milo.opcua.stack.core.types.enumerated.InterfaceOperStatus"
                    + " value "
                    + value);
          }
        }
        convertedValue =
            value == null || value instanceof InterfaceOperStatus
                ? (InterfaceOperStatus) value
                : InterfaceOperStatus.from((Integer) value);
      }
    }
    return (InterfaceOperStatus) convertedValue;
  }

  @Override
  public void setOperStatus(@Nullable InterfaceOperStatus value) {
    var node = getOperStatusNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:OperStatus (declaration i=24150, owner i=24148)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getPhysAddressNode() {
    return ServerMembers.lookup(
        this,
        BaseDataVariableTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "PhysAddress",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner i=24148)"));
  }

  @Override
  public @Nullable String getPhysAddress() {
    var node = getPhysAddressNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner i=24148)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setPhysAddress(@Nullable String value) {
    var node = getPhysAddressNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:PhysAddress (declaration i=24151, owner i=24148)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public AnalogUnitTypeNode getSpeedNode() {
    return ServerMembers.lookup(
        this,
        AnalogUnitTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Speed",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner i=24148)"));
  }

  @Override
  public @Nullable ULong getSpeed() {
    var node = getSpeedNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner i=24148)"
              + " on "
              + getNodeId());
    }
    return (ULong) node.getValue().getValue().getValue();
  }

  @Override
  public void setSpeed(@Nullable ULong value) {
    var node = getSpeedNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Speed (declaration i=24152, owner i=24148)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }
}
