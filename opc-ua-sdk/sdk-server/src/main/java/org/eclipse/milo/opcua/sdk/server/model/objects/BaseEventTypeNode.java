package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.TimeZoneDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link BaseEventType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.2">Model
 *     documentation</a>
 */
public class BaseEventTypeNode extends BaseObjectTypeNode implements BaseEventType {
  public BaseEventTypeNode(
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

  public BaseEventTypeNode(
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
  public @Nullable PropertyTypeNode getConditionClassIdNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ConditionClassId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable NodeId getConditionClassId() {
    return ServerNodeSupport.read(this, getConditionClassIdNode(), NodeId.class, null);
  }

  @Override
  public void setConditionClassId(@Nullable NodeId value) {
    ServerNodeSupport.write(
        this,
        getConditionClassIdNode(),
        Namespaces.OPC_UA,
        "ConditionClassId",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getConditionClassNameNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ConditionClassName",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getConditionClassName() {
    return ServerNodeSupport.read(this, getConditionClassNameNode(), LocalizedText.class, null);
  }

  @Override
  public void setConditionClassName(@Nullable LocalizedText value) {
    ServerNodeSupport.write(
        this,
        getConditionClassNameNode(),
        Namespaces.OPC_UA,
        "ConditionClassName",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getConditionSubClassIdNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ConditionSubClassId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public NodeId @Nullable [] getConditionSubClassId() {
    return ServerNodeSupport.readArray(this, getConditionSubClassIdNode(), NodeId.class, null);
  }

  @Override
  public void setConditionSubClassId(NodeId @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getConditionSubClassIdNode(),
        Namespaces.OPC_UA,
        "ConditionSubClassId",
        value,
        true,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getConditionSubClassNameNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ConditionSubClassName",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public LocalizedText @Nullable [] getConditionSubClassName() {
    return ServerNodeSupport.readArray(
        this, getConditionSubClassNameNode(), LocalizedText.class, null);
  }

  @Override
  public void setConditionSubClassName(LocalizedText @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getConditionSubClassNameNode(),
        Namespaces.OPC_UA,
        "ConditionSubClassName",
        value,
        true,
        false,
        false);
  }

  @Override
  public PropertyTypeNode getEventIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "EventId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 15L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable ByteString getEventId() {
    return ServerNodeSupport.read(this, getEventIdNode(), ByteString.class, null);
  }

  @Override
  public void setEventId(@Nullable ByteString value) {
    ServerNodeSupport.write(this, getEventIdNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getEventTypeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "EventType",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable NodeId getEventType() {
    return ServerNodeSupport.read(this, getEventTypeNode(), NodeId.class, null);
  }

  @Override
  public void setEventType(@Nullable NodeId value) {
    ServerNodeSupport.write(this, getEventTypeNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getLocalTimeNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "LocalTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 8912L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable TimeZoneDataType getLocalTime() {
    return ServerNodeSupport.read(this, getLocalTimeNode(), TimeZoneDataType.class, null);
  }

  @Override
  public void setLocalTime(@Nullable TimeZoneDataType value) {
    ServerNodeSupport.write(
        this, getLocalTimeNode(), Namespaces.OPC_UA, "LocalTime", value, false, false, true);
  }

  @Override
  public PropertyTypeNode getMessageNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Message",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getMessage() {
    return ServerNodeSupport.read(this, getMessageNode(), LocalizedText.class, null);
  }

  @Override
  public void setMessage(@Nullable LocalizedText value) {
    ServerNodeSupport.write(this, getMessageNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getReceiveTimeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ReceiveTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DateTime getReceiveTime() {
    return ServerNodeSupport.read(this, getReceiveTimeNode(), DateTime.class, null);
  }

  @Override
  public void setReceiveTime(@Nullable DateTime value) {
    ServerNodeSupport.write(this, getReceiveTimeNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getSeverityNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Severity",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UShort getSeverity() {
    return ServerNodeSupport.read(this, getSeverityNode(), UShort.class, null);
  }

  @Override
  public void setSeverity(@Nullable UShort value) {
    ServerNodeSupport.write(this, getSeverityNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getSourceNameNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SourceName",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getSourceName() {
    return ServerNodeSupport.read(this, getSourceNameNode(), String.class, null);
  }

  @Override
  public void setSourceName(@Nullable String value) {
    ServerNodeSupport.write(this, getSourceNameNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getSourceNodeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SourceNode",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable NodeId getSourceNode() {
    return ServerNodeSupport.read(this, getSourceNodeNode(), NodeId.class, null);
  }

  @Override
  public void setSourceNode(@Nullable NodeId value) {
    ServerNodeSupport.write(this, getSourceNodeNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getTimeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Time",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DateTime getTime() {
    return ServerNodeSupport.read(this, getTimeNode(), DateTime.class, null);
  }

  @Override
  public void setTime(@Nullable DateTime value) {
    ServerNodeSupport.write(this, getTimeNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getConditionClassIdNode();
    getConditionClassNameNode();
    getConditionSubClassIdNode();
    getConditionSubClassNameNode();
    getEventIdNode();
    getEventTypeNode();
    getLocalTimeNode();
    getMessageNode();
    getReceiveTimeNode();
    getSeverityNode();
    getSourceNameNode();
    getSourceNodeNode();
    getTimeNode();
  }
}
