package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.FiniteTransitionVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ProgramDiagnostic2TypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ProgramDiagnostic2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ProgramStateMachineType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.1">Model
 *     documentation</a>
 */
public class ProgramStateMachineTypeNode extends FiniteStateMachineTypeNode
    implements ProgramStateMachineType {
  public ProgramStateMachineTypeNode(
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

  public ProgramStateMachineTypeNode(
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
  public PropertyTypeNode getAutoDeleteNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "AutoDelete",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getAutoDelete() {
    return ServerNodeSupport.read(this, getAutoDeleteNode(), Boolean.class, null);
  }

  @Override
  public void setAutoDelete(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getAutoDeleteNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getDeletableNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "Deletable",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getDeletable() {
    return ServerNodeSupport.read(this, getDeletableNode(), Boolean.class, null);
  }

  @Override
  public void setDeletable(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getDeletableNode(), value, false, false, false);
  }

  @Override
  public @Nullable BaseObjectTypeNode getFinalResultDataNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "FinalResultData",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 58L),
        null,
        -1,
        BaseObjectTypeNode.class);
  }

  @Override
  public FiniteTransitionVariableTypeNode getLastTransitionNode() {
    return ServerNodeSupport.mandatoryChild(
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
  public @Nullable ProgramDiagnostic2TypeNode getProgramDiagnosticNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ProgramDiagnostic",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 15383L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 24033L),
        -1,
        ProgramDiagnostic2TypeNode.class);
  }

  @Override
  public @Nullable ProgramDiagnostic2DataType getProgramDiagnostic() {
    return ServerNodeSupport.read(
        this, getProgramDiagnosticNode(), ProgramDiagnostic2DataType.class, null);
  }

  @Override
  public void setProgramDiagnostic(@Nullable ProgramDiagnostic2DataType value) {
    ServerNodeSupport.write(
        this,
        getProgramDiagnosticNode(),
        Namespaces.OPC_UA,
        "ProgramDiagnostic",
        value,
        false,
        false,
        true);
  }

  @Override
  public PropertyTypeNode getRecycleCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "RecycleCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 6L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Integer getRecycleCount() {
    return ServerNodeSupport.read(this, getRecycleCountNode(), Integer.class, null);
  }

  @Override
  public void setRecycleCount(@Nullable Integer value) {
    ServerNodeSupport.write(this, getRecycleCountNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getAutoDeleteNode();
    getDeletableNode();
    getFinalResultDataNode();
    getProgramDiagnosticNode();
    getRecycleCountNode();
  }
}
