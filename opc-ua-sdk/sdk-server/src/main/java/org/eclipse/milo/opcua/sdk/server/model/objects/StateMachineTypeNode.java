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
 * Node implementation of {@link StateMachineType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.2">Model
 *     documentation</a>
 */
public class StateMachineTypeNode extends BaseObjectTypeNode implements StateMachineType {
  public StateMachineTypeNode(
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

  public StateMachineTypeNode(
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
  public StateVariableTypeNode getCurrentStateNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CurrentState",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 2755L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        StateVariableTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getCurrentState() {
    return ServerNodeSupport.read(this, getCurrentStateNode(), LocalizedText.class, null);
  }

  @Override
  public void setCurrentState(@Nullable LocalizedText value) {
    ServerNodeSupport.write(this, getCurrentStateNode(), value, false, false, false);
  }

  @Override
  public @Nullable TransitionVariableTypeNode getLastTransitionNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "LastTransition",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 2762L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        TransitionVariableTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getLastTransition() {
    return ServerNodeSupport.read(this, getLastTransitionNode(), LocalizedText.class, null);
  }

  @Override
  public void setLastTransition(@Nullable LocalizedText value) {
    ServerNodeSupport.write(
        this,
        getLastTransitionNode(),
        Namespaces.OPC_UA,
        "LastTransition",
        value,
        false,
        false,
        false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getCurrentStateNode();
    getLastTransitionNode();
  }
}
