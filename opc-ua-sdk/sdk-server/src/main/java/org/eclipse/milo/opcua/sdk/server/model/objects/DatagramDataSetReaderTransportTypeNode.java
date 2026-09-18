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
import org.eclipse.milo.opcua.stack.core.types.structured.ReceiveQosDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link DatagramDataSetReaderTransportType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.4">Model
 *     documentation</a>
 */
public class DatagramDataSetReaderTransportTypeNode extends DataSetReaderTransportTypeNode
    implements DatagramDataSetReaderTransportType {
  public DatagramDataSetReaderTransportTypeNode(
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

  public DatagramDataSetReaderTransportTypeNode(
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
        ExpandedNodeId.of(Namespaces.OPC_UA, 23608L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable ReceiveQosDataType @Nullable [] getDatagramQos() {
    return ServerNodeSupport.readArray(this, getDatagramQosNode(), ReceiveQosDataType.class, null);
  }

  @Override
  public void setDatagramQos(@Nullable ReceiveQosDataType @Nullable [] value) {
    ServerNodeSupport.write(
        this, getDatagramQosNode(), Namespaces.OPC_UA, "DatagramQos", value, true, false, true);
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
    getQosCategoryNode();
    getTopicNode();
  }
}
