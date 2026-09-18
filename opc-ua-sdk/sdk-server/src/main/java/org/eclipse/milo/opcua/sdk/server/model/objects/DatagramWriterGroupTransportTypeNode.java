package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.TransmitQosDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link DatagramWriterGroupTransportType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.2">Model
 *     documentation</a>
 */
public class DatagramWriterGroupTransportTypeNode extends WriterGroupTransportTypeNode
    implements DatagramWriterGroupTransportType {
  public DatagramWriterGroupTransportTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      @Nullable LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType @Nullable [] rolePermissions,
      RolePermissionType @Nullable [] userRolePermissions,
      @Nullable AccessRestrictionType accessRestrictions) {
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

  public DatagramWriterGroupTransportTypeNode(
      UaNodeContext context,
      NodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      @Nullable LocalizedText description,
      UInteger writeMask,
      UInteger userWriteMask,
      RolePermissionType @Nullable [] rolePermissions,
      RolePermissionType @Nullable [] userRolePermissions,
      @Nullable AccessRestrictionType accessRestrictions,
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

  @Override
  public @Nullable NetworkAddressTypeNode getAddressNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "Address",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21145L),
        null,
        -1,
        NetworkAddressTypeNode.class);
  }

  @Override
  public @Nullable PropertyTypeNode getDatagramQosNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "DatagramQos",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 23604L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable TransmitQosDataType @Nullable [] getDatagramQos() {
    return ServerNodeSupport.readArray(this, getDatagramQosNode(), TransmitQosDataType.class, null);
  }

  @Override
  public void setDatagramQos(@Nullable TransmitQosDataType @Nullable [] value) {
    ServerNodeSupport.write(
        this, getDatagramQosNode(), Namespaces.OPC_UA, "DatagramQos", value, true, false, true);
  }

  @Override
  public @Nullable PropertyTypeNode getDiscoveryAnnounceRateNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "DiscoveryAnnounceRate",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getDiscoveryAnnounceRate() {
    return ServerNodeSupport.read(this, getDiscoveryAnnounceRateNode(), UInteger.class, null);
  }

  @Override
  public void setDiscoveryAnnounceRate(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getDiscoveryAnnounceRateNode(),
        Namespaces.OPC_UA,
        "DiscoveryAnnounceRate",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMessageRepeatCountNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MessageRepeatCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 3L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UByte getMessageRepeatCount() {
    return ServerNodeSupport.read(this, getMessageRepeatCountNode(), UByte.class, null);
  }

  @Override
  public void setMessageRepeatCount(@Nullable UByte value) {
    ServerNodeSupport.write(
        this,
        getMessageRepeatCountNode(),
        Namespaces.OPC_UA,
        "MessageRepeatCount",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMessageRepeatDelayNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MessageRepeatDelay",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getMessageRepeatDelay() {
    return ServerNodeSupport.read(this, getMessageRepeatDelayNode(), Double.class, null);
  }

  @Override
  public void setMessageRepeatDelay(@Nullable Double value) {
    ServerNodeSupport.write(
        this,
        getMessageRepeatDelayNode(),
        Namespaces.OPC_UA,
        "MessageRepeatDelay",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getQosCategoryNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "QosCategory",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getQosCategory() {
    return ServerNodeSupport.read(this, getQosCategoryNode(), String.class, null);
  }

  @Override
  public void setQosCategory(@Nullable String value) {
    ServerNodeSupport.write(
        this, getQosCategoryNode(), Namespaces.OPC_UA, "QosCategory", value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getTopicNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "Topic",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getTopic() {
    return ServerNodeSupport.read(this, getTopicNode(), String.class, null);
  }

  @Override
  public void setTopic(@Nullable String value) {
    ServerNodeSupport.write(
        this, getTopicNode(), Namespaces.OPC_UA, "Topic", value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getAddressNode();
    getDatagramQosNode();
    getDiscoveryAnnounceRateNode();
    getMessageRepeatCountNode();
    getMessageRepeatDelayNode();
    getQosCategoryNode();
    getTopicNode();
  }
}
