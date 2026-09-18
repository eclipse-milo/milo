package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.ProgramDiagnostic2DataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ProgramDiagnostic2Type}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part10/5.2.9">Model
 *     documentation</a>
 */
public class ProgramDiagnostic2TypeNode extends BaseDataVariableTypeNode
    implements ProgramDiagnostic2Type {
  public ProgramDiagnostic2TypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger @Nullable [] arrayDimensions) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions);
  }

  public ProgramDiagnostic2TypeNode(
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
      DataValue value,
      NodeId dataType,
      Integer valueRank,
      UInteger @Nullable [] arrayDimensions,
      UByte accessLevel,
      UByte userAccessLevel,
      Double minimumSamplingInterval,
      boolean historizing,
      AccessLevelExType accessLevelEx) {
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
        value,
        dataType,
        valueRank,
        arrayDimensions,
        accessLevel,
        userAccessLevel,
        minimumSamplingInterval,
        historizing,
        accessLevelEx);
  }

  @Override
  public BaseDataVariableTypeNode getCreateClientNameNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CreateClientName",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String getCreateClientName() {
    return ServerNodeSupport.read(this, getCreateClientNameNode(), String.class, null);
  }

  @Override
  public void setCreateClientName(@Nullable String value) {
    ServerNodeSupport.write(this, getCreateClientNameNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getCreateSessionIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CreateSessionId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable NodeId getCreateSessionId() {
    return ServerNodeSupport.read(this, getCreateSessionIdNode(), NodeId.class, null);
  }

  @Override
  public void setCreateSessionId(@Nullable NodeId value) {
    ServerNodeSupport.write(this, getCreateSessionIdNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getInvocationCreationTimeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "InvocationCreationTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable DateTime getInvocationCreationTime() {
    return ServerNodeSupport.read(this, getInvocationCreationTimeNode(), DateTime.class, null);
  }

  @Override
  public void setInvocationCreationTime(@Nullable DateTime value) {
    ServerNodeSupport.write(this, getInvocationCreationTimeNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getLastMethodCallNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LastMethodCall",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable String getLastMethodCall() {
    return ServerNodeSupport.read(this, getLastMethodCallNode(), String.class, null);
  }

  @Override
  public void setLastMethodCall(@Nullable String value) {
    ServerNodeSupport.write(this, getLastMethodCallNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getLastMethodCallTimeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LastMethodCallTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable DateTime getLastMethodCallTime() {
    return ServerNodeSupport.read(this, getLastMethodCallTimeNode(), DateTime.class, null);
  }

  @Override
  public void setLastMethodCallTime(@Nullable DateTime value) {
    ServerNodeSupport.write(this, getLastMethodCallTimeNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getLastMethodInputArgumentsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LastMethodInputArguments",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 296L),
        1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Argument @Nullable [] getLastMethodInputArguments() {
    return ServerNodeSupport.readArray(
        this, getLastMethodInputArgumentsNode(), Argument.class, null);
  }

  @Override
  public void setLastMethodInputArguments(@Nullable Argument @Nullable [] value) {
    ServerNodeSupport.write(this, getLastMethodInputArgumentsNode(), value, true, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getLastMethodInputValuesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LastMethodInputValues",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 24L),
        1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Variant @Nullable [] getLastMethodInputValues() {
    return ServerNodeSupport.readArray(this, getLastMethodInputValuesNode(), Variant.class, null);
  }

  @Override
  public void setLastMethodInputValues(@Nullable Variant @Nullable [] value) {
    ServerNodeSupport.write(this, getLastMethodInputValuesNode(), value, true, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getLastMethodOutputArgumentsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LastMethodOutputArguments",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 296L),
        1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Argument @Nullable [] getLastMethodOutputArguments() {
    return ServerNodeSupport.readArray(
        this, getLastMethodOutputArgumentsNode(), Argument.class, null);
  }

  @Override
  public void setLastMethodOutputArguments(@Nullable Argument @Nullable [] value) {
    ServerNodeSupport.write(this, getLastMethodOutputArgumentsNode(), value, true, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getLastMethodOutputValuesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LastMethodOutputValues",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 24L),
        1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable Variant @Nullable [] getLastMethodOutputValues() {
    return ServerNodeSupport.readArray(this, getLastMethodOutputValuesNode(), Variant.class, null);
  }

  @Override
  public void setLastMethodOutputValues(@Nullable Variant @Nullable [] value) {
    ServerNodeSupport.write(this, getLastMethodOutputValuesNode(), value, true, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getLastMethodReturnStatusNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LastMethodReturnStatus",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 19L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable StatusCode getLastMethodReturnStatus() {
    return ServerNodeSupport.read(this, getLastMethodReturnStatusNode(), StatusCode.class, null);
  }

  @Override
  public void setLastMethodReturnStatus(@Nullable StatusCode value) {
    ServerNodeSupport.write(this, getLastMethodReturnStatusNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getLastMethodSessionIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LastMethodSessionId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable NodeId getLastMethodSessionId() {
    return ServerNodeSupport.read(this, getLastMethodSessionIdNode(), NodeId.class, null);
  }

  @Override
  public void setLastMethodSessionId(@Nullable NodeId value) {
    ServerNodeSupport.write(this, getLastMethodSessionIdNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getLastTransitionTimeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LastTransitionTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable DateTime getLastTransitionTime() {
    return ServerNodeSupport.read(this, getLastTransitionTimeNode(), DateTime.class, null);
  }

  @Override
  public void setLastTransitionTime(@Nullable DateTime value) {
    ServerNodeSupport.write(this, getLastTransitionTimeNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getCreateClientNameNode();
    getCreateSessionIdNode();
    getInvocationCreationTimeNode();
    getLastMethodCallNode();
    getLastMethodCallTimeNode();
    getLastMethodInputArgumentsNode();
    getLastMethodInputValuesNode();
    getLastMethodOutputArgumentsNode();
    getLastMethodOutputValuesNode();
    getLastMethodReturnStatusNode();
    getLastMethodSessionIdNode();
    getLastTransitionTimeNode();
  }

  @Override
  public @Nullable ProgramDiagnostic2DataType getTypedValue() {
    return ServerNodeSupport.read(this, this, ProgramDiagnostic2DataType.class, null);
  }

  @Override
  public void setTypedValue(@Nullable ProgramDiagnostic2DataType value) {
    ServerNodeSupport.write(this, this, value, false, false, true);
  }
}
