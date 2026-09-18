package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ProgramDiagnosticDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.StatusResult;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/** Node implementation of {@link ProgramDiagnosticType}. */
public class ProgramDiagnosticTypeNode extends BaseDataVariableTypeNode
    implements ProgramDiagnosticType {
  public ProgramDiagnosticTypeNode(
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

  public ProgramDiagnosticTypeNode(
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
  public PropertyTypeNode getCreateClientNameNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CreateClientName",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
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
  public PropertyTypeNode getCreateSessionIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CreateSessionId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        -1,
        PropertyTypeNode.class);
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
  public PropertyTypeNode getInvocationCreationTimeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "InvocationCreationTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        PropertyTypeNode.class);
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
  public PropertyTypeNode getLastMethodCallNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LastMethodCall",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
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
  public PropertyTypeNode getLastMethodCallTimeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LastMethodCallTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        PropertyTypeNode.class);
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
  public PropertyTypeNode getLastMethodInputArgumentsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LastMethodInputArguments",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 24L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Variant @Nullable [] getLastMethodInputArguments() {
    return ServerNodeSupport.readArray(
        this, getLastMethodInputArgumentsNode(), Variant.class, null);
  }

  @Override
  public void setLastMethodInputArguments(@Nullable Variant @Nullable [] value) {
    ServerNodeSupport.write(this, getLastMethodInputArgumentsNode(), value, true, false, false);
  }

  @Override
  public PropertyTypeNode getLastMethodOutputArgumentsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LastMethodOutputArguments",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 24L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Variant @Nullable [] getLastMethodOutputArguments() {
    return ServerNodeSupport.readArray(
        this, getLastMethodOutputArgumentsNode(), Variant.class, null);
  }

  @Override
  public void setLastMethodOutputArguments(@Nullable Variant @Nullable [] value) {
    ServerNodeSupport.write(this, getLastMethodOutputArgumentsNode(), value, true, false, false);
  }

  @Override
  public PropertyTypeNode getLastMethodReturnStatusNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LastMethodReturnStatus",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 299L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable StatusResult getLastMethodReturnStatus() {
    return ServerNodeSupport.read(this, getLastMethodReturnStatusNode(), StatusResult.class, null);
  }

  @Override
  public void setLastMethodReturnStatus(@Nullable StatusResult value) {
    ServerNodeSupport.write(this, getLastMethodReturnStatusNode(), value, false, false, true);
  }

  @Override
  public PropertyTypeNode getLastMethodSessionIdNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LastMethodSessionId",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
        -1,
        PropertyTypeNode.class);
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
    getLastMethodOutputArgumentsNode();
    getLastMethodReturnStatusNode();
    getLastMethodSessionIdNode();
    getLastTransitionTimeNode();
  }

  @Override
  public @Nullable ProgramDiagnosticDataType getTypedValue() {
    return ServerNodeSupport.read(this, this, ProgramDiagnosticDataType.class, null);
  }

  @Override
  public void setTypedValue(@Nullable ProgramDiagnosticDataType value) {
    ServerNodeSupport.write(this, this, value, false, false, true);
  }
}
