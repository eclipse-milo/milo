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

public class HistoryServerCapabilitiesTypeNode extends BaseObjectTypeNode
    implements HistoryServerCapabilitiesType {
  public HistoryServerCapabilitiesTypeNode(
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

  public HistoryServerCapabilitiesTypeNode(
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
  public PropertyTypeNode getAccessHistoryDataCapabilityNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "AccessHistoryDataCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:AccessHistoryDataCapability (declaration i=2331, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable Boolean getAccessHistoryDataCapability() {
    var node = getAccessHistoryDataCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AccessHistoryDataCapability (declaration i=2331, owner"
              + " i=2330) on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setAccessHistoryDataCapability(@Nullable Boolean value) {
    var node = getAccessHistoryDataCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AccessHistoryDataCapability (declaration i=2331, owner"
              + " i=2330) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getAccessHistoryEventsCapabilityNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "AccessHistoryEventsCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:AccessHistoryEventsCapability (declaration i=2332, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable Boolean getAccessHistoryEventsCapability() {
    var node = getAccessHistoryEventsCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AccessHistoryEventsCapability (declaration i=2332, owner"
              + " i=2330) on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setAccessHistoryEventsCapability(@Nullable Boolean value) {
    var node = getAccessHistoryEventsCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:AccessHistoryEventsCapability (declaration i=2332, owner"
              + " i=2330) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getMaxReturnDataValuesNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxReturnDataValues",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxReturnDataValues (declaration i=11268, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable UInteger getMaxReturnDataValues() {
    var node = getMaxReturnDataValuesNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxReturnDataValues (declaration i=11268, owner i=2330)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxReturnDataValues(@Nullable UInteger value) {
    var node = getMaxReturnDataValuesNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxReturnDataValues (declaration i=11268, owner i=2330)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getMaxReturnEventValuesNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxReturnEventValues",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:MaxReturnEventValues (declaration i=11269, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable UInteger getMaxReturnEventValues() {
    var node = getMaxReturnEventValuesNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxReturnEventValues (declaration i=11269, owner i=2330)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxReturnEventValues(@Nullable UInteger value) {
    var node = getMaxReturnEventValuesNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxReturnEventValues (declaration i=11269, owner i=2330)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getInsertDataCapabilityNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "InsertDataCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:InsertDataCapability (declaration i=2334, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable Boolean getInsertDataCapability() {
    var node = getInsertDataCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InsertDataCapability (declaration i=2334, owner i=2330)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setInsertDataCapability(@Nullable Boolean value) {
    var node = getInsertDataCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InsertDataCapability (declaration i=2334, owner i=2330)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getReplaceDataCapabilityNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ReplaceDataCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ReplaceDataCapability (declaration i=2335, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable Boolean getReplaceDataCapability() {
    var node = getReplaceDataCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ReplaceDataCapability (declaration i=2335, owner i=2330)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setReplaceDataCapability(@Nullable Boolean value) {
    var node = getReplaceDataCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ReplaceDataCapability (declaration i=2335, owner i=2330)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getUpdateDataCapabilityNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "UpdateDataCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:UpdateDataCapability (declaration i=2336, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable Boolean getUpdateDataCapability() {
    var node = getUpdateDataCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UpdateDataCapability (declaration i=2336, owner i=2330)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setUpdateDataCapability(@Nullable Boolean value) {
    var node = getUpdateDataCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UpdateDataCapability (declaration i=2336, owner i=2330)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getDeleteRawCapabilityNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DeleteRawCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DeleteRawCapability (declaration i=2337, owner i=2330)"));
  }

  @Override
  public @Nullable Boolean getDeleteRawCapability() {
    var node = getDeleteRawCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteRawCapability (declaration i=2337, owner i=2330)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setDeleteRawCapability(@Nullable Boolean value) {
    var node = getDeleteRawCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteRawCapability (declaration i=2337, owner i=2330)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getDeleteAtTimeCapabilityNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DeleteAtTimeCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DeleteAtTimeCapability (declaration i=2338, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable Boolean getDeleteAtTimeCapability() {
    var node = getDeleteAtTimeCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteAtTimeCapability (declaration i=2338, owner i=2330)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setDeleteAtTimeCapability(@Nullable Boolean value) {
    var node = getDeleteAtTimeCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteAtTimeCapability (declaration i=2338, owner i=2330)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getInsertEventCapabilityNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "InsertEventCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:InsertEventCapability (declaration i=11278, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable Boolean getInsertEventCapability() {
    var node = getInsertEventCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InsertEventCapability (declaration i=11278, owner i=2330)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setInsertEventCapability(@Nullable Boolean value) {
    var node = getInsertEventCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InsertEventCapability (declaration i=11278, owner i=2330)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getReplaceEventCapabilityNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ReplaceEventCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:ReplaceEventCapability (declaration i=11279, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable Boolean getReplaceEventCapability() {
    var node = getReplaceEventCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ReplaceEventCapability (declaration i=11279, owner i=2330)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setReplaceEventCapability(@Nullable Boolean value) {
    var node = getReplaceEventCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ReplaceEventCapability (declaration i=11279, owner i=2330)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getUpdateEventCapabilityNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "UpdateEventCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:UpdateEventCapability (declaration i=11280, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable Boolean getUpdateEventCapability() {
    var node = getUpdateEventCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UpdateEventCapability (declaration i=11280, owner i=2330)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setUpdateEventCapability(@Nullable Boolean value) {
    var node = getUpdateEventCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:UpdateEventCapability (declaration i=11280, owner i=2330)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getDeleteEventCapabilityNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DeleteEventCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:DeleteEventCapability (declaration i=11501, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable Boolean getDeleteEventCapability() {
    var node = getDeleteEventCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteEventCapability (declaration i=11501, owner i=2330)"
              + " on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setDeleteEventCapability(@Nullable Boolean value) {
    var node = getDeleteEventCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DeleteEventCapability (declaration i=11501, owner i=2330)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public PropertyTypeNode getInsertAnnotationCapabilityNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "InsertAnnotationCapability",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            false,
            "http://opcfoundation.org/UA/:InsertAnnotationCapability (declaration i=11270, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable Boolean getInsertAnnotationCapability() {
    var node = getInsertAnnotationCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InsertAnnotationCapability (declaration i=11270, owner"
              + " i=2330) on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setInsertAnnotationCapability(@Nullable Boolean value) {
    var node = getInsertAnnotationCapabilityNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:InsertAnnotationCapability (declaration i=11270, owner"
              + " i=2330) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getServerTimestampSupportedNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "ServerTimestampSupported",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:ServerTimestampSupported (declaration i=19094, owner"
                + " i=2330)"));
  }

  @Override
  public @Nullable Boolean getServerTimestampSupported() {
    var node = getServerTimestampSupportedNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServerTimestampSupported (declaration i=19094, owner"
              + " i=2330) on "
              + getNodeId());
    }
    return (Boolean) node.getValue().getValue().getValue();
  }

  @Override
  public void setServerTimestampSupported(@Nullable Boolean value) {
    var node = getServerTimestampSupportedNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:ServerTimestampSupported (declaration i=19094, owner"
              + " i=2330) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public FolderTypeNode getAggregateFunctionsNode() {
    return ServerMembers.lookup(
        this,
        FolderTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "AggregateFunctions",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:AggregateFunctions (declaration i=11172, owner i=2330)"));
  }
}
