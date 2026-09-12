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
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.TransmitQosDataType;
import org.jspecify.annotations.Nullable;

public class DatagramWriterGroupTransportTypeNode extends WriterGroupTransportTypeNode
    implements DatagramWriterGroupTransportType {
  public DatagramWriterGroupTransportTypeNode(
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

  public DatagramWriterGroupTransportTypeNode(
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
  public @Nullable PropertyTypeNode getMessageRepeatCountNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MessageRepeatCount",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MessageRepeatCount (declaration i=21134, owner"
                + " i=21133)"));
  }

  @Override
  public @Nullable UByte getMessageRepeatCount() {
    var node = getMessageRepeatCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MessageRepeatCount (declaration i=21134, owner i=21133)"
              + " on "
              + getNodeId());
    }
    return (UByte) node.getValue().getValue().getValue();
  }

  @Override
  public void setMessageRepeatCount(@Nullable UByte value) {
    var node = getMessageRepeatCountNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MessageRepeatCount (declaration i=21134, owner i=21133)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getMessageRepeatDelayNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "MessageRepeatDelay",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:MessageRepeatDelay (declaration i=21135, owner"
                + " i=21133)"));
  }

  @Override
  public @Nullable Double getMessageRepeatDelay() {
    var node = getMessageRepeatDelayNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MessageRepeatDelay (declaration i=21135, owner i=21133)"
              + " on "
              + getNodeId());
    }
    return (Double) node.getValue().getValue().getValue();
  }

  @Override
  public void setMessageRepeatDelay(@Nullable Double value) {
    var node = getMessageRepeatDelayNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:MessageRepeatDelay (declaration i=21135, owner i=21133)"
              + " on "
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
            "http://opcfoundation.org/UA/:QosCategory (declaration i=25527, owner i=21133)"));
  }

  @Override
  public @Nullable String getQosCategory() {
    var node = getQosCategoryNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:QosCategory (declaration i=25527, owner i=21133)"
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
          "http://opcfoundation.org/UA/:QosCategory (declaration i=25527, owner i=21133)"
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
            "http://opcfoundation.org/UA/:DatagramQos (declaration i=23847, owner i=21133)"));
  }

  @Override
  public @Nullable TransmitQosDataType @Nullable [] getDatagramQos() {
    var node = getDatagramQosNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DatagramQos (declaration i=23847, owner i=21133)"
              + " on "
              + getNodeId());
    }
    return ServerPropertyValues.decode(
        getNodeContext().getServer().getStaticEncodingContext(),
        node.getValue().getValue().getValue(),
        TransmitQosDataType[].class,
        TransmitQosDataType.class,
        "http://opcfoundation.org/UA/:DatagramQos (declaration i=23847, owner i=21133)");
  }

  @Override
  public void setDatagramQos(@Nullable TransmitQosDataType @Nullable [] value) {
    var node = getDatagramQosNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DatagramQos (declaration i=23847, owner i=21133)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
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
            "http://opcfoundation.org/UA/:DiscoveryAnnounceRate (declaration i=23848, owner"
                + " i=21133)"));
  }

  @Override
  public @Nullable UInteger getDiscoveryAnnounceRate() {
    var node = getDiscoveryAnnounceRateNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:DiscoveryAnnounceRate (declaration i=23848, owner i=21133)"
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
          "http://opcfoundation.org/UA/:DiscoveryAnnounceRate (declaration i=23848, owner i=21133)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable PropertyTypeNode getTopicNode() {
    return ServerMembers.lookup(
        this,
        PropertyTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Topic",
            ExpandedNodeId.parse("i=46"),
            true,
            NodeClass.Variable,
            true,
            "http://opcfoundation.org/UA/:Topic (declaration i=23849, owner i=21133)"));
  }

  @Override
  public @Nullable String getTopic() {
    var node = getTopicNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Topic (declaration i=23849, owner i=21133)"
              + " on "
              + getNodeId());
    }
    return (String) node.getValue().getValue().getValue();
  }

  @Override
  public void setTopic(@Nullable String value) {
    var node = getTopicNode();
    if (node == null) {
      throw new UaRuntimeException(
          StatusCodes.Bad_NotFound,
          "http://opcfoundation.org/UA/:Topic (declaration i=23849, owner i=21133)"
              + " on "
              + getNodeId());
    }
    node.setValue(new DataValue(new Variant(value)));
  }

  @Override
  public @Nullable NetworkAddressTypeNode getAddressNode() {
    return ServerMembers.lookup(
        this,
        NetworkAddressTypeNode.class,
        new MemberDeclaration(
            "http://opcfoundation.org/UA/",
            "Address",
            ExpandedNodeId.parse("i=47"),
            true,
            NodeClass.Object,
            true,
            "http://opcfoundation.org/UA/:Address (declaration i=23842, owner i=21133)"));
  }
}
