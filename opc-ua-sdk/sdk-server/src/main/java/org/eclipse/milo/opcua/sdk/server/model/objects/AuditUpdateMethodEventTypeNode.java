package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link AuditUpdateMethodEventType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.27">Model
 *     documentation</a>
 */
public class AuditUpdateMethodEventTypeNode extends AuditEventTypeNode
    implements AuditUpdateMethodEventType {
  public AuditUpdateMethodEventTypeNode(
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

  public AuditUpdateMethodEventTypeNode(
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
  public PropertyTypeNode getInputArgumentsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "InputArguments",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 24L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Variant @Nullable [] getInputArguments() {
    return ServerNodeSupport.readArray(this, getInputArgumentsNode(), Variant.class, null);
  }

  @Override
  public void setInputArguments(@Nullable Variant @Nullable [] value) {
    ServerNodeSupport.write(this, getInputArgumentsNode(), value, true, false, false);
  }

  @Override
  public PropertyTypeNode getMethodIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MethodId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable NodeId getMethodId() {
    return ServerNodeSupport.read(this, getMethodIdNode(), NodeId.class, null);
  }

  @Override
  public void setMethodId(@Nullable NodeId value) {
    ServerNodeSupport.write(this, getMethodIdNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getOutputArgumentsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "OutputArguments",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 24L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Variant @Nullable [] getOutputArguments() {
    return ServerNodeSupport.readArray(this, getOutputArgumentsNode(), Variant.class, null);
  }

  @Override
  public void setOutputArguments(@Nullable Variant @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getOutputArgumentsNode(),
        Namespaces.OPC_UA,
        "OutputArguments",
        value,
        true,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getStatusCodeIdNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "StatusCodeId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 19L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable StatusCode getStatusCodeId() {
    return ServerNodeSupport.read(this, getStatusCodeIdNode(), StatusCode.class, null);
  }

  @Override
  public void setStatusCodeId(@Nullable StatusCode value) {
    ServerNodeSupport.write(
        this, getStatusCodeIdNode(), Namespaces.OPC_UA, "StatusCodeId", value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getInputArgumentsNode();
    getMethodIdNode();
    getOutputArgumentsNode();
    getStatusCodeIdNode();
  }
}
