package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.StateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.TransitionVariableTypeNode;
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
 * Node implementation of {@link TransitionEventType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.16">Model
 *     documentation</a>
 */
public class TransitionEventTypeNode extends BaseEventTypeNode implements TransitionEventType {
  public TransitionEventTypeNode(
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

  public TransitionEventTypeNode(
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
  public StateVariableTypeNode getFromStateNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "FromState",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 2755L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        StateVariableTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getFromState() {
    return ServerNodeSupport.read(this, getFromStateNode(), LocalizedText.class, null);
  }

  @Override
  public void setFromState(@Nullable LocalizedText value) {
    ServerNodeSupport.write(this, getFromStateNode(), value, false, false, false);
  }

  @Override
  public StateVariableTypeNode getToStateNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ToState",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 2755L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        StateVariableTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getToState() {
    return ServerNodeSupport.read(this, getToStateNode(), LocalizedText.class, null);
  }

  @Override
  public void setToState(@Nullable LocalizedText value) {
    ServerNodeSupport.write(this, getToStateNode(), value, false, false, false);
  }

  @Override
  public TransitionVariableTypeNode getTransitionNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Transition",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 2762L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        TransitionVariableTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getTransition() {
    return ServerNodeSupport.read(this, getTransitionNode(), LocalizedText.class, null);
  }

  @Override
  public void setTransition(@Nullable LocalizedText value) {
    ServerNodeSupport.write(this, getTransitionNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getFromStateNode();
    getToStateNode();
    getTransitionNode();
  }
}
