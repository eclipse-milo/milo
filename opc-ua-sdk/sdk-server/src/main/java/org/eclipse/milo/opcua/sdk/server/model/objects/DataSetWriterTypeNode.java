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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link DataSetWriterType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.7/#9.1.7.2">Model
 *     documentation</a>
 */
public class DataSetWriterTypeNode extends BaseObjectTypeNode implements DataSetWriterType {
  public DataSetWriterTypeNode(
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

  public DataSetWriterTypeNode(
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
  public PropertyTypeNode getDataSetFieldContentMaskNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DataSetFieldContentMask",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 15583L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DataSetFieldContentMask getDataSetFieldContentMask() {
    return ServerNodeSupport.read(
        this, getDataSetFieldContentMaskNode(), DataSetFieldContentMask.class, null);
  }

  @Override
  public void setDataSetFieldContentMask(@Nullable DataSetFieldContentMask value) {
    ServerNodeSupport.write(this, getDataSetFieldContentMaskNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getDataSetWriterIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DataSetWriterId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UShort getDataSetWriterId() {
    return ServerNodeSupport.read(this, getDataSetWriterIdNode(), UShort.class, null);
  }

  @Override
  public void setDataSetWriterId(@Nullable UShort value) {
    ServerNodeSupport.write(this, getDataSetWriterIdNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getDataSetWriterPropertiesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DataSetWriterProperties",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 14533L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable KeyValuePair @Nullable [] getDataSetWriterProperties() {
    return ServerNodeSupport.readArray(
        this, getDataSetWriterPropertiesNode(), KeyValuePair.class, null);
  }

  @Override
  public void setDataSetWriterProperties(@Nullable KeyValuePair @Nullable [] value) {
    ServerNodeSupport.write(this, getDataSetWriterPropertiesNode(), value, true, false, true);
  }

  @Override
  public @Nullable PubSubDiagnosticsDataSetWriterTypeNode getDiagnosticsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "Diagnostics",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 19968L),
        null,
        -1,
        PubSubDiagnosticsDataSetWriterTypeNode.class);
  }

  @Override
  public @Nullable PropertyTypeNode getKeyFrameCountNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "KeyFrameCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getKeyFrameCount() {
    return ServerNodeSupport.read(this, getKeyFrameCountNode(), UInteger.class, null);
  }

  @Override
  public void setKeyFrameCount(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getKeyFrameCountNode(),
        Namespaces.OPC_UA,
        "KeyFrameCount",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable DataSetWriterMessageTypeNode getMessageSettingsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MessageSettings",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21096L),
        null,
        -1,
        DataSetWriterMessageTypeNode.class);
  }

  @Override
  public PubSubStatusTypeNode getStatusNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Status",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 14643L),
        null,
        -1,
        PubSubStatusTypeNode.class);
  }

  @Override
  public @Nullable DataSetWriterTransportTypeNode getTransportSettingsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "TransportSettings",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 15305L),
        null,
        -1,
        DataSetWriterTransportTypeNode.class);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getDataSetFieldContentMaskNode();
    getDataSetWriterIdNode();
    getDataSetWriterPropertiesNode();
    getDiagnosticsNode();
    getKeyFrameCountNode();
    getMessageSettingsNode();
    getStatusNode();
    getTransportSettingsNode();
  }
}
