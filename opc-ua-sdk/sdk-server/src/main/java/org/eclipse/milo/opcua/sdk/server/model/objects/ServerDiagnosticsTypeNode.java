package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SamplingIntervalDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.ServerDiagnosticsSummaryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SubscriptionDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SamplingIntervalDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerDiagnosticsSummaryDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.SubscriptionDiagnosticsDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ServerDiagnosticsType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.3">Model
 *     documentation</a>
 */
public class ServerDiagnosticsTypeNode extends BaseObjectTypeNode implements ServerDiagnosticsType {
  public ServerDiagnosticsTypeNode(
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

  public ServerDiagnosticsTypeNode(
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
  public PropertyTypeNode getEnabledFlagNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "EnabledFlag",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getEnabledFlag() {
    return ServerNodeSupport.read(this, getEnabledFlagNode(), Boolean.class, null);
  }

  @Override
  public void setEnabledFlag(@Nullable Boolean value) {
    ServerNodeSupport.write(this, getEnabledFlagNode(), value, false, false, false);
  }

  @Override
  public @Nullable SamplingIntervalDiagnosticsArrayTypeNode
      getSamplingIntervalDiagnosticsArrayNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SamplingIntervalDiagnosticsArray",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 2164L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 856L),
        1,
        SamplingIntervalDiagnosticsArrayTypeNode.class);
  }

  @Override
  public @Nullable SamplingIntervalDiagnosticsDataType @Nullable []
      getSamplingIntervalDiagnosticsArray() {
    return ServerNodeSupport.readArray(
        this,
        getSamplingIntervalDiagnosticsArrayNode(),
        SamplingIntervalDiagnosticsDataType.class,
        null);
  }

  @Override
  public void setSamplingIntervalDiagnosticsArray(
      @Nullable SamplingIntervalDiagnosticsDataType @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getSamplingIntervalDiagnosticsArrayNode(),
        Namespaces.OPC_UA,
        "SamplingIntervalDiagnosticsArray",
        value,
        true,
        false,
        true);
  }

  @Override
  public ServerDiagnosticsSummaryTypeNode getServerDiagnosticsSummaryNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ServerDiagnosticsSummary",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 2150L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 859L),
        -1,
        ServerDiagnosticsSummaryTypeNode.class);
  }

  @Override
  public @Nullable ServerDiagnosticsSummaryDataType getServerDiagnosticsSummary() {
    return ServerNodeSupport.read(
        this, getServerDiagnosticsSummaryNode(), ServerDiagnosticsSummaryDataType.class, null);
  }

  @Override
  public void setServerDiagnosticsSummary(@Nullable ServerDiagnosticsSummaryDataType value) {
    ServerNodeSupport.write(this, getServerDiagnosticsSummaryNode(), value, false, false, true);
  }

  @Override
  public SessionsDiagnosticsSummaryTypeNode getSessionsDiagnosticsSummaryNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SessionsDiagnosticsSummary",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 2026L),
        null,
        -1,
        SessionsDiagnosticsSummaryTypeNode.class);
  }

  @Override
  public SubscriptionDiagnosticsArrayTypeNode getSubscriptionDiagnosticsArrayNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SubscriptionDiagnosticsArray",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 2171L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 874L),
        1,
        SubscriptionDiagnosticsArrayTypeNode.class);
  }

  @Override
  public @Nullable SubscriptionDiagnosticsDataType @Nullable [] getSubscriptionDiagnosticsArray() {
    return ServerNodeSupport.readArray(
        this, getSubscriptionDiagnosticsArrayNode(), SubscriptionDiagnosticsDataType.class, null);
  }

  @Override
  public void setSubscriptionDiagnosticsArray(
      @Nullable SubscriptionDiagnosticsDataType @Nullable [] value) {
    ServerNodeSupport.write(this, getSubscriptionDiagnosticsArrayNode(), value, true, false, true);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getEnabledFlagNode();
    getSamplingIntervalDiagnosticsArrayNode();
    getServerDiagnosticsSummaryNode();
    getSessionsDiagnosticsSummaryNode();
    getSubscriptionDiagnosticsArrayNode();
  }
}
