package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ServerStatusType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.6">Model
 *     documentation</a>
 */
public class ServerStatusTypeNode extends BaseDataVariableTypeNode implements ServerStatusType {
  public ServerStatusTypeNode(
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

  public ServerStatusTypeNode(
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
  public BuildInfoTypeNode getBuildInfoNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "BuildInfo",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 3051L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 338L),
        -1,
        BuildInfoTypeNode.class);
  }

  @Override
  public @Nullable BuildInfo getBuildInfo() {
    return ServerNodeSupport.read(this, getBuildInfoNode(), BuildInfo.class, null);
  }

  @Override
  public void setBuildInfo(@Nullable BuildInfo value) {
    ServerNodeSupport.write(this, getBuildInfoNode(), value, false, false, true);
  }

  @Override
  public BaseDataVariableTypeNode getCurrentTimeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CurrentTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable DateTime getCurrentTime() {
    return ServerNodeSupport.read(this, getCurrentTimeNode(), DateTime.class, null);
  }

  @Override
  public void setCurrentTime(@Nullable DateTime value) {
    ServerNodeSupport.write(this, getCurrentTimeNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getSecondsTillShutdownNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SecondsTillShutdown",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getSecondsTillShutdown() {
    return ServerNodeSupport.read(this, getSecondsTillShutdownNode(), UInteger.class, null);
  }

  @Override
  public void setSecondsTillShutdown(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getSecondsTillShutdownNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getShutdownReasonNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ShutdownReason",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable LocalizedText getShutdownReason() {
    return ServerNodeSupport.read(this, getShutdownReasonNode(), LocalizedText.class, null);
  }

  @Override
  public void setShutdownReason(@Nullable LocalizedText value) {
    ServerNodeSupport.write(this, getShutdownReasonNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getStartTimeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "StartTime",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable DateTime getStartTime() {
    return ServerNodeSupport.read(this, getStartTimeNode(), DateTime.class, null);
  }

  @Override
  public void setStartTime(@Nullable DateTime value) {
    ServerNodeSupport.write(this, getStartTimeNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getStateNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "State",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 852L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable ServerState getState() {
    return ServerNodeSupport.read(this, getStateNode(), ServerState.class, ServerState::from);
  }

  @Override
  public void setState(@Nullable ServerState value) {
    ServerNodeSupport.write(this, getStateNode(), value, false, true, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getBuildInfoNode();
    getCurrentTimeNode();
    getSecondsTillShutdownNode();
    getShutdownReasonNode();
    getStartTimeNode();
    getStateNode();
  }

  @Override
  public @Nullable ServerStatusDataType getTypedValue() {
    return ServerNodeSupport.read(this, this, ServerStatusDataType.class, null);
  }

  @Override
  public void setTypedValue(@Nullable ServerStatusDataType value) {
    ServerNodeSupport.write(this, this, value, false, false, true);
  }
}
