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
import com.digitalpetri.opcua.uanodeset.runtime.server.ServerPropertyValues;
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
import org.eclipse.milo.opcua.stack.core.types.structured.QosDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

public class DatagramConnectionTransportTypeNode extends ConnectionTransportTypeNode
    implements DatagramConnectionTransportType {
  public DatagramConnectionTransportTypeNode(
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

  public DatagramConnectionTransportTypeNode(
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
  public @Nullable PropertyTypeNode getDiscoveryAnnounceRateNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DiscoveryAnnounceRate",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:DiscoveryAnnounceRate (declaration i=23839, owner"
                + " i=15064)"));
  }

  @Override
  public @Nullable UInteger getDiscoveryAnnounceRate() {
    var node = getDiscoveryAnnounceRateNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DiscoveryAnnounceRate (declaration i=23839, owner i=15064)"
              + " on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setDiscoveryAnnounceRate(@Nullable UInteger value) {
    var node = getDiscoveryAnnounceRateNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DiscoveryAnnounceRate (declaration i=23839, owner i=15064)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getDiscoveryMaxMessageSizeNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DiscoveryMaxMessageSize",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:DiscoveryMaxMessageSize (declaration i=23840, owner"
                + " i=15064)"));
  }

  @Override
  public @Nullable UInteger getDiscoveryMaxMessageSize() {
    var node = getDiscoveryMaxMessageSizeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DiscoveryMaxMessageSize (declaration i=23840, owner"
              + " i=15064) on "
              + getNodeId());
    }
    return (UInteger) node.getValue().getValue().getValue();
  }

  @Override
  public void setDiscoveryMaxMessageSize(@Nullable UInteger value) {
    var node = getDiscoveryMaxMessageSizeNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DiscoveryMaxMessageSize (declaration i=23840, owner"
              + " i=15064) on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getQosCategoryNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "QosCategory",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:QosCategory (declaration i=25525, owner i=15064)"));
  }

  @Override
  public @Nullable String getQosCategory() {
    var node = getQosCategoryNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:QosCategory (declaration i=25525, owner i=15064)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setQosCategory(@Nullable String value) {
    var node = getQosCategoryNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:QosCategory (declaration i=25525, owner i=15064)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getDatagramQosNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DatagramQos",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:DatagramQos (declaration i=25526, owner i=15064)"));
  }

  @Override
  public @Nullable QosDataType @Nullable [] getDatagramQos() {
    var node = getDatagramQosNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DatagramQos (declaration i=25526, owner i=15064)"
              + " on "
              + getNodeId());
    }
    return ServerPropertyValues.decode(
        getNodeContext().getServer().getStaticEncodingContext(),
        node.getValue().getValue().getValue(),
        QosDataType[].class,
        QosDataType.class,
        "http://opcfoundation.org/UA/:DatagramQos (declaration i=25526, owner i=15064)");
  }

  @Override
  public void setDatagramQos(@Nullable QosDataType @Nullable [] value) {
    var node = getDatagramQosNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DatagramQos (declaration i=25526, owner i=15064)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public NetworkAddressTypeNode getDiscoveryAddressNode() {
    return ServerMembers.lookup(
        this,
        NetworkAddressTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "DiscoveryAddress",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            false,
            "http://opcfoundation.org/UA/:DiscoveryAddress (declaration i=15072, owner i=15064)"));
  }
}
