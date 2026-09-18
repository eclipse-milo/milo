package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteStateVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteTransitionVariableTypeNode;
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
 * Node implementation of {@link FiniteStateMachineType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.5">Model
 *     documentation</a>
 */
public class FiniteStateMachineTypeNode extends StateMachineTypeNode
    implements FiniteStateMachineType {
  public FiniteStateMachineTypeNode(
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

  public FiniteStateMachineTypeNode(
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
  public @Nullable BaseDataVariableTypeNode getAvailableStatesNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "AvailableStates",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public NodeId @Nullable [] getAvailableStates() {
    return ServerNodeSupport.readArray(this, getAvailableStatesNode(), NodeId.class, null);
  }

  @Override
  public void setAvailableStates(NodeId @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getAvailableStatesNode(),
        Namespaces.OPC_UA,
        "AvailableStates",
        value,
        true,
        false,
        false);
  }

  @Override
  public @Nullable BaseDataVariableTypeNode getAvailableTransitionsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "AvailableTransitions",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public NodeId @Nullable [] getAvailableTransitions() {
    return ServerNodeSupport.readArray(this, getAvailableTransitionsNode(), NodeId.class, null);
  }

  @Override
  public void setAvailableTransitions(NodeId @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getAvailableTransitionsNode(),
        Namespaces.OPC_UA,
        "AvailableTransitions",
        value,
        true,
        false,
        false);
  }

  @Override
  public FiniteStateVariableTypeNode getCurrentStateNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CurrentState",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 2760L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        FiniteStateVariableTypeNode.class);
  }

  @Override
  public @Nullable FiniteTransitionVariableTypeNode getLastTransitionNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "LastTransition",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 2767L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        FiniteTransitionVariableTypeNode.class);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getAvailableStatesNode();
    getAvailableTransitionsNode();
  }
}
