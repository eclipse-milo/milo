package org.eclipse.milo.opcua.sdk.server.model.variables;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerDiagnosticsSummaryDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ServerDiagnosticsSummaryType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.8">Model
 *     documentation</a>
 */
public class ServerDiagnosticsSummaryTypeNode extends BaseDataVariableTypeNode
    implements ServerDiagnosticsSummaryType {
  public ServerDiagnosticsSummaryTypeNode(
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

  public ServerDiagnosticsSummaryTypeNode(
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
  public BaseDataVariableTypeNode getCumulatedSessionCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CumulatedSessionCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getCumulatedSessionCount() {
    return ServerNodeSupport.read(this, getCumulatedSessionCountNode(), UInteger.class, null);
  }

  @Override
  public void setCumulatedSessionCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getCumulatedSessionCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getCumulatedSubscriptionCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CumulatedSubscriptionCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getCumulatedSubscriptionCount() {
    return ServerNodeSupport.read(this, getCumulatedSubscriptionCountNode(), UInteger.class, null);
  }

  @Override
  public void setCumulatedSubscriptionCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getCumulatedSubscriptionCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getCurrentSessionCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CurrentSessionCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getCurrentSessionCount() {
    return ServerNodeSupport.read(this, getCurrentSessionCountNode(), UInteger.class, null);
  }

  @Override
  public void setCurrentSessionCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getCurrentSessionCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getCurrentSubscriptionCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "CurrentSubscriptionCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getCurrentSubscriptionCount() {
    return ServerNodeSupport.read(this, getCurrentSubscriptionCountNode(), UInteger.class, null);
  }

  @Override
  public void setCurrentSubscriptionCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getCurrentSubscriptionCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getPublishingIntervalCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "PublishingIntervalCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getPublishingIntervalCount() {
    return ServerNodeSupport.read(this, getPublishingIntervalCountNode(), UInteger.class, null);
  }

  @Override
  public void setPublishingIntervalCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getPublishingIntervalCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getRejectedRequestsCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "RejectedRequestsCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getRejectedRequestsCount() {
    return ServerNodeSupport.read(this, getRejectedRequestsCountNode(), UInteger.class, null);
  }

  @Override
  public void setRejectedRequestsCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getRejectedRequestsCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getRejectedSessionCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "RejectedSessionCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getRejectedSessionCount() {
    return ServerNodeSupport.read(this, getRejectedSessionCountNode(), UInteger.class, null);
  }

  @Override
  public void setRejectedSessionCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getRejectedSessionCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getSecurityRejectedRequestsCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SecurityRejectedRequestsCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getSecurityRejectedRequestsCount() {
    return ServerNodeSupport.read(
        this, getSecurityRejectedRequestsCountNode(), UInteger.class, null);
  }

  @Override
  public void setSecurityRejectedRequestsCount(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this, getSecurityRejectedRequestsCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getSecurityRejectedSessionCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SecurityRejectedSessionCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getSecurityRejectedSessionCount() {
    return ServerNodeSupport.read(
        this, getSecurityRejectedSessionCountNode(), UInteger.class, null);
  }

  @Override
  public void setSecurityRejectedSessionCount(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this, getSecurityRejectedSessionCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getServerViewCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ServerViewCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getServerViewCount() {
    return ServerNodeSupport.read(this, getServerViewCountNode(), UInteger.class, null);
  }

  @Override
  public void setServerViewCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getServerViewCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getSessionAbortCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SessionAbortCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getSessionAbortCount() {
    return ServerNodeSupport.read(this, getSessionAbortCountNode(), UInteger.class, null);
  }

  @Override
  public void setSessionAbortCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getSessionAbortCountNode(), value, false, false, false);
  }

  @Override
  public BaseDataVariableTypeNode getSessionTimeoutCountNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SessionTimeoutCount",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 63L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        BaseDataVariableTypeNode.class);
  }

  @Override
  public @Nullable UInteger getSessionTimeoutCount() {
    return ServerNodeSupport.read(this, getSessionTimeoutCountNode(), UInteger.class, null);
  }

  @Override
  public void setSessionTimeoutCount(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getSessionTimeoutCountNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getCumulatedSessionCountNode();
    getCumulatedSubscriptionCountNode();
    getCurrentSessionCountNode();
    getCurrentSubscriptionCountNode();
    getPublishingIntervalCountNode();
    getRejectedRequestsCountNode();
    getRejectedSessionCountNode();
    getSecurityRejectedRequestsCountNode();
    getSecurityRejectedSessionCountNode();
    getServerViewCountNode();
    getSessionAbortCountNode();
    getSessionTimeoutCountNode();
  }

  @Override
  public @Nullable ServerDiagnosticsSummaryDataType getTypedValue() {
    return ServerNodeSupport.read(this, this, ServerDiagnosticsSummaryDataType.class, null);
  }

  @Override
  public void setTypedValue(@Nullable ServerDiagnosticsSummaryDataType value) {
    ServerNodeSupport.write(this, this, value, false, false, true);
  }
}
