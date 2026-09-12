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

public class OperationLimitsTypeNode extends FolderTypeNode implements OperationLimitsType {
  public OperationLimitsTypeNode(
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

  public OperationLimitsTypeNode(
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
  public @Nullable PropertyTypeNode getMaxNodesPerReadNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerRead",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNodesPerRead (declaration i=11565, owner i=11564)"));
  }

  @Override
  public @Nullable UInteger getMaxNodesPerRead() {
    var node = getMaxNodesPerReadNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerRead (declaration i=11565, owner i=11564)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerRead(@Nullable UInteger value) {
    var node = getMaxNodesPerReadNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerRead (declaration i=11565, owner i=11564)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerHistoryReadDataNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerHistoryReadData",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNodesPerHistoryReadData (declaration i=12161, owner"
                + " i=11564)"));
  }

  @Override
  public @Nullable UInteger getMaxNodesPerHistoryReadData() {
    var node = getMaxNodesPerHistoryReadDataNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerHistoryReadData (declaration i=12161, owner"
              + " i=11564) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerHistoryReadData(@Nullable UInteger value) {
    var node = getMaxNodesPerHistoryReadDataNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerHistoryReadData (declaration i=12161, owner"
              + " i=11564) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerHistoryReadEventsNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerHistoryReadEvents",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNodesPerHistoryReadEvents (declaration i=12162, owner"
                + " i=11564)"));
  }

  @Override
  public @Nullable UInteger getMaxNodesPerHistoryReadEvents() {
    var node = getMaxNodesPerHistoryReadEventsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerHistoryReadEvents (declaration i=12162, owner"
              + " i=11564) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerHistoryReadEvents(@Nullable UInteger value) {
    var node = getMaxNodesPerHistoryReadEventsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerHistoryReadEvents (declaration i=12162, owner"
              + " i=11564) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerWriteNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerWrite",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNodesPerWrite (declaration i=11567, owner i=11564)"));
  }

  @Override
  public @Nullable UInteger getMaxNodesPerWrite() {
    var node = getMaxNodesPerWriteNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerWrite (declaration i=11567, owner i=11564)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerWrite(@Nullable UInteger value) {
    var node = getMaxNodesPerWriteNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerWrite (declaration i=11567, owner i=11564)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerHistoryUpdateDataNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerHistoryUpdateData",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNodesPerHistoryUpdateData (declaration i=12163, owner"
                + " i=11564)"));
  }

  @Override
  public @Nullable UInteger getMaxNodesPerHistoryUpdateData() {
    var node = getMaxNodesPerHistoryUpdateDataNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerHistoryUpdateData (declaration i=12163, owner"
              + " i=11564) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerHistoryUpdateData(@Nullable UInteger value) {
    var node = getMaxNodesPerHistoryUpdateDataNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerHistoryUpdateData (declaration i=12163, owner"
              + " i=11564) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerHistoryUpdateEventsNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerHistoryUpdateEvents",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNodesPerHistoryUpdateEvents (declaration i=12164,"
                + " owner i=11564)"));
  }

  @Override
  public @Nullable UInteger getMaxNodesPerHistoryUpdateEvents() {
    var node = getMaxNodesPerHistoryUpdateEventsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerHistoryUpdateEvents (declaration i=12164, owner"
              + " i=11564) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerHistoryUpdateEvents(@Nullable UInteger value) {
    var node = getMaxNodesPerHistoryUpdateEventsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerHistoryUpdateEvents (declaration i=12164, owner"
              + " i=11564) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerMethodCallNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerMethodCall",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNodesPerMethodCall (declaration i=11569, owner"
                + " i=11564)"));
  }

  @Override
  public @Nullable UInteger getMaxNodesPerMethodCall() {
    var node = getMaxNodesPerMethodCallNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerMethodCall (declaration i=11569, owner i=11564)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerMethodCall(@Nullable UInteger value) {
    var node = getMaxNodesPerMethodCallNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerMethodCall (declaration i=11569, owner i=11564)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerBrowseNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerBrowse",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNodesPerBrowse (declaration i=11570, owner i=11564)"));
  }

  @Override
  public @Nullable UInteger getMaxNodesPerBrowse() {
    var node = getMaxNodesPerBrowseNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerBrowse (declaration i=11570, owner i=11564)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerBrowse(@Nullable UInteger value) {
    var node = getMaxNodesPerBrowseNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerBrowse (declaration i=11570, owner i=11564)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerRegisterNodesNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerRegisterNodes",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNodesPerRegisterNodes (declaration i=11571, owner"
                + " i=11564)"));
  }

  @Override
  public @Nullable UInteger getMaxNodesPerRegisterNodes() {
    var node = getMaxNodesPerRegisterNodesNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerRegisterNodes (declaration i=11571, owner"
              + " i=11564) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerRegisterNodes(@Nullable UInteger value) {
    var node = getMaxNodesPerRegisterNodesNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerRegisterNodes (declaration i=11571, owner"
              + " i=11564) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerTranslateBrowsePathsToNodeIdsNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerTranslateBrowsePathsToNodeIds",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNodesPerTranslateBrowsePathsToNodeIds (declaration"
                + " i=11572, owner i=11564)"));
  }

  @Override
  public @Nullable UInteger getMaxNodesPerTranslateBrowsePathsToNodeIds() {
    var node = getMaxNodesPerTranslateBrowsePathsToNodeIdsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerTranslateBrowsePathsToNodeIds (declaration"
              + " i=11572, owner i=11564) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerTranslateBrowsePathsToNodeIds(@Nullable UInteger value) {
    var node = getMaxNodesPerTranslateBrowsePathsToNodeIdsNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerTranslateBrowsePathsToNodeIds (declaration"
              + " i=11572, owner i=11564) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNodesPerNodeManagementNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxNodesPerNodeManagement",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxNodesPerNodeManagement (declaration i=11573, owner"
                + " i=11564)"));
  }

  @Override
  public @Nullable UInteger getMaxNodesPerNodeManagement() {
    var node = getMaxNodesPerNodeManagementNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerNodeManagement (declaration i=11573, owner"
              + " i=11564) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxNodesPerNodeManagement(@Nullable UInteger value) {
    var node = getMaxNodesPerNodeManagementNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxNodesPerNodeManagement (declaration i=11573, owner"
              + " i=11564) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxMonitoredItemsPerCallNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MaxMonitoredItemsPerCall",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MaxMonitoredItemsPerCall (declaration i=11574, owner"
                + " i=11564)"));
  }

  @Override
  public @Nullable UInteger getMaxMonitoredItemsPerCall() {
    var node = getMaxMonitoredItemsPerCallNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxMonitoredItemsPerCall (declaration i=11574, owner"
              + " i=11564) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setMaxMonitoredItemsPerCall(@Nullable UInteger value) {
    var node = getMaxMonitoredItemsPerCallNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MaxMonitoredItemsPerCall (declaration i=11574, owner"
              + " i=11564) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }
}
