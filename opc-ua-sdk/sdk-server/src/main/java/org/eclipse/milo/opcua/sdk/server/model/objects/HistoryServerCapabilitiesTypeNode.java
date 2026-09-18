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
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link HistoryServerCapabilitiesType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.7.2">Model
 *     documentation</a>
 */
public class HistoryServerCapabilitiesTypeNode extends BaseObjectTypeNode
    implements HistoryServerCapabilitiesType {
  public HistoryServerCapabilitiesTypeNode(
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

  public HistoryServerCapabilitiesTypeNode(
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
  public PropertyTypeNode getAccessHistoryDataCapabilityNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "AccessHistoryDataCapability",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getAccessHistoryDataCapability() {
    return ServerNodeSupport.read(this, getAccessHistoryDataCapabilityNode(), Boolean.class, null);
  }

  @Override
  public void setAccessHistoryDataCapability(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getAccessHistoryDataCapabilityNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getAccessHistoryEventsCapabilityNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "AccessHistoryEventsCapability",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getAccessHistoryEventsCapability() {
    return ServerNodeSupport.read(
        this, getAccessHistoryEventsCapabilityNode(), Boolean.class, null);
  }

  @Override
  public void setAccessHistoryEventsCapability(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this, getAccessHistoryEventsCapabilityNode(), value, false, false, false);
  }

  @Override
  public FolderTypeNode getAggregateFunctionsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "AggregateFunctions",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 61L),
        null,
        -1,
        FolderTypeNode.class);
  }

  @Override
  public PropertyTypeNode getDeleteAtTimeCapabilityNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DeleteAtTimeCapability",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getDeleteAtTimeCapability() {
    return ServerNodeSupport.read(this, getDeleteAtTimeCapabilityNode(), Boolean.class, null);
  }

  @Override
  public void setDeleteAtTimeCapability(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getDeleteAtTimeCapabilityNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getDeleteEventCapabilityNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DeleteEventCapability",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getDeleteEventCapability() {
    return ServerNodeSupport.read(this, getDeleteEventCapabilityNode(), Boolean.class, null);
  }

  @Override
  public void setDeleteEventCapability(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getDeleteEventCapabilityNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getDeleteRawCapabilityNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "DeleteRawCapability",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getDeleteRawCapability() {
    return ServerNodeSupport.read(this, getDeleteRawCapabilityNode(), Boolean.class, null);
  }

  @Override
  public void setDeleteRawCapability(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getDeleteRawCapabilityNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getInsertAnnotationCapabilityNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "InsertAnnotationCapability",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getInsertAnnotationCapability() {
    return ServerNodeSupport.read(this, getInsertAnnotationCapabilityNode(), Boolean.class, null);
  }

  @Override
  public void setInsertAnnotationCapability(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getInsertAnnotationCapabilityNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getInsertDataCapabilityNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "InsertDataCapability",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getInsertDataCapability() {
    return ServerNodeSupport.read(this, getInsertDataCapabilityNode(), Boolean.class, null);
  }

  @Override
  public void setInsertDataCapability(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getInsertDataCapabilityNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getInsertEventCapabilityNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "InsertEventCapability",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getInsertEventCapability() {
    return ServerNodeSupport.read(this, getInsertEventCapabilityNode(), Boolean.class, null);
  }

  @Override
  public void setInsertEventCapability(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getInsertEventCapabilityNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getMaxReturnDataValuesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaxReturnDataValues",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxReturnDataValues() {
    return ServerNodeSupport.read(this, getMaxReturnDataValuesNode(), UInteger.class, null);
  }

  @Override
  public void setMaxReturnDataValues(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getMaxReturnDataValuesNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getMaxReturnEventValuesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaxReturnEventValues",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxReturnEventValues() {
    return ServerNodeSupport.read(this, getMaxReturnEventValuesNode(), UInteger.class, null);
  }

  @Override
  public void setMaxReturnEventValues(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getMaxReturnEventValuesNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getReplaceDataCapabilityNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ReplaceDataCapability",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getReplaceDataCapability() {
    return ServerNodeSupport.read(this, getReplaceDataCapabilityNode(), Boolean.class, null);
  }

  @Override
  public void setReplaceDataCapability(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getReplaceDataCapabilityNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getReplaceEventCapabilityNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ReplaceEventCapability",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getReplaceEventCapability() {
    return ServerNodeSupport.read(this, getReplaceEventCapabilityNode(), Boolean.class, null);
  }

  @Override
  public void setReplaceEventCapability(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getReplaceEventCapabilityNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getServerTimestampSupportedNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ServerTimestampSupported",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getServerTimestampSupported() {
    return ServerNodeSupport.read(this, getServerTimestampSupportedNode(), Boolean.class, null);
  }

  @Override
  public void setServerTimestampSupported(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this,
        getServerTimestampSupportedNode(),
        Namespaces.OPC_UA,
        "ServerTimestampSupported",
        value,
        false,
        false,
        false);
  }

  @Override
  public PropertyTypeNode getUpdateDataCapabilityNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "UpdateDataCapability",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getUpdateDataCapability() {
    return ServerNodeSupport.read(this, getUpdateDataCapabilityNode(), Boolean.class, null);
  }

  @Override
  public void setUpdateDataCapability(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getUpdateDataCapabilityNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getUpdateEventCapabilityNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "UpdateEventCapability",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getUpdateEventCapability() {
    return ServerNodeSupport.read(this, getUpdateEventCapabilityNode(), Boolean.class, null);
  }

  @Override
  public void setUpdateEventCapability(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getUpdateEventCapabilityNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getAccessHistoryDataCapabilityNode();
    getAccessHistoryEventsCapabilityNode();
    getAggregateFunctionsNode();
    getDeleteAtTimeCapabilityNode();
    getDeleteEventCapabilityNode();
    getDeleteRawCapabilityNode();
    getInsertAnnotationCapabilityNode();
    getInsertDataCapabilityNode();
    getInsertEventCapabilityNode();
    getMaxReturnDataValuesNode();
    getMaxReturnEventValuesNode();
    getReplaceDataCapabilityNode();
    getReplaceEventCapabilityNode();
    getServerTimestampSupportedNode();
    getUpdateDataCapabilityNode();
    getUpdateEventCapabilityNode();
  }
}
