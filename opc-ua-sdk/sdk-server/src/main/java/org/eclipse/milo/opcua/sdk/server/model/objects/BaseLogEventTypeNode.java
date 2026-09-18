package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.TraceContextDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link BaseLogEventType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part26/6.3">Model
 *     documentation</a>
 */
public class BaseLogEventTypeNode extends BaseEventTypeNode implements BaseLogEventType {
  public BaseLogEventTypeNode(
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

  public BaseLogEventTypeNode(
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
  public PropertyTypeNode getConditionClassIdNode() {
    return ServerNodeSupport.mandatoryChild(
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
  public PropertyTypeNode getConditionClassNameNode() {
    return ServerNodeSupport.mandatoryChild(
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
  public @Nullable PropertyTypeNode getErrorCodeNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ErrorCode",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 19L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable StatusCode getErrorCode() {
    return ServerNodeSupport.read(this, getErrorCodeNode(), StatusCode.class, null);
  }

  @Override
  public void setErrorCode(@Nullable StatusCode value) {
    ServerNodeSupport.write(
        this, getErrorCodeNode(), Namespaces.OPC_UA, "ErrorCode", value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getErrorCodeNode_Node() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ErrorCodeNode",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable NodeId getErrorCodeNode_() {
    return ServerNodeSupport.read(this, getErrorCodeNode_Node(), NodeId.class, null);
  }

  @Override
  public void setErrorCodeNode_(@Nullable NodeId value) {
    ServerNodeSupport.write(
        this,
        getErrorCodeNode_Node(),
        Namespaces.OPC_UA,
        "ErrorCodeNode",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getTraceContextNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "TraceContext",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 19747L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable TraceContextDataType getTraceContext() {
    return ServerNodeSupport.read(this, getTraceContextNode(), TraceContextDataType.class, null);
  }

  @Override
  public void setTraceContext(@Nullable TraceContextDataType value) {
    ServerNodeSupport.write(
        this, getTraceContextNode(), Namespaces.OPC_UA, "TraceContext", value, false, false, true);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getErrorCodeNode();
    getErrorCodeNode_Node();
    getTraceContextNode();
  }
}
