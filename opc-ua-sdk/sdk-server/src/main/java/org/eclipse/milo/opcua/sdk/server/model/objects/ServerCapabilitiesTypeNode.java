package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.ServerNodeSupport;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNodeContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ServerCapabilitiesType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.2">Model
 *     documentation</a>
 */
public class ServerCapabilitiesTypeNode extends BaseObjectTypeNode
    implements ServerCapabilitiesType {
  public ServerCapabilitiesTypeNode(
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

  public ServerCapabilitiesTypeNode(
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
  public FolderTypeNode getAggregateFunctionsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "AggregateFunctions",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 61L),
        null,
        -1,
        FolderTypeNode.class);
  }

  @Override
  public @Nullable PropertyTypeNode getConformanceUnitsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "ConformanceUnits",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 20L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public QualifiedName @Nullable [] getConformanceUnits() {
    return ServerNodeSupport.readArray(this, getConformanceUnitsNode(), QualifiedName.class, null);
  }

  @Override
  public void setConformanceUnits(QualifiedName @Nullable [] value) {
    ServerNodeSupport.write(
        this,
        getConformanceUnitsNode(),
        Namespaces.OPC_UA,
        "ConformanceUnits",
        value,
        true,
        false,
        false);
  }

  @Override
  public PropertyTypeNode getLocaleIdArrayNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "LocaleIdArray",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 295L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String @Nullable [] getLocaleIdArray() {
    return ServerNodeSupport.readArray(this, getLocaleIdArrayNode(), String.class, null);
  }

  @Override
  public void setLocaleIdArray(@Nullable String @Nullable [] value) {
    ServerNodeSupport.write(this, getLocaleIdArrayNode(), value, true, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxArrayLengthNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxArrayLength",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxArrayLength() {
    return ServerNodeSupport.read(this, getMaxArrayLengthNode(), UInteger.class, null);
  }

  @Override
  public void setMaxArrayLength(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxArrayLengthNode(),
        Namespaces.OPC_UA,
        "MaxArrayLength",
        value,
        false,
        false,
        false);
  }

  @Override
  public PropertyTypeNode getMaxBrowseContinuationPointsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaxBrowseContinuationPoints",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UShort getMaxBrowseContinuationPoints() {
    return ServerNodeSupport.read(this, getMaxBrowseContinuationPointsNode(), UShort.class, null);
  }

  @Override
  public void setMaxBrowseContinuationPoints(@Nullable UShort value) {
    ServerNodeSupport.write(this, getMaxBrowseContinuationPointsNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxByteStringLengthNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxByteStringLength",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxByteStringLength() {
    return ServerNodeSupport.read(this, getMaxByteStringLengthNode(), UInteger.class, null);
  }

  @Override
  public void setMaxByteStringLength(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxByteStringLengthNode(),
        Namespaces.OPC_UA,
        "MaxByteStringLength",
        value,
        false,
        false,
        false);
  }

  @Override
  public PropertyTypeNode getMaxHistoryContinuationPointsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaxHistoryContinuationPoints",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UShort getMaxHistoryContinuationPoints() {
    return ServerNodeSupport.read(this, getMaxHistoryContinuationPointsNode(), UShort.class, null);
  }

  @Override
  public void setMaxHistoryContinuationPoints(@Nullable UShort value) {
    ServerNodeSupport.write(
        this, getMaxHistoryContinuationPointsNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxLogObjectContinuationPointsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxLogObjectContinuationPoints",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UShort getMaxLogObjectContinuationPoints() {
    return ServerNodeSupport.read(
        this, getMaxLogObjectContinuationPointsNode(), UShort.class, null);
  }

  @Override
  public void setMaxLogObjectContinuationPoints(@Nullable UShort value) {
    ServerNodeSupport.write(
        this,
        getMaxLogObjectContinuationPointsNode(),
        Namespaces.OPC_UA,
        "MaxLogObjectContinuationPoints",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxMonitoredItemsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxMonitoredItems",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxMonitoredItems() {
    return ServerNodeSupport.read(this, getMaxMonitoredItemsNode(), UInteger.class, null);
  }

  @Override
  public void setMaxMonitoredItems(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxMonitoredItemsNode(),
        Namespaces.OPC_UA,
        "MaxMonitoredItems",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxMonitoredItemsPerSubscriptionNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxMonitoredItemsPerSubscription",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxMonitoredItemsPerSubscription() {
    return ServerNodeSupport.read(
        this, getMaxMonitoredItemsPerSubscriptionNode(), UInteger.class, null);
  }

  @Override
  public void setMaxMonitoredItemsPerSubscription(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxMonitoredItemsPerSubscriptionNode(),
        Namespaces.OPC_UA,
        "MaxMonitoredItemsPerSubscription",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxMonitoredItemsQueueSizeNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxMonitoredItemsQueueSize",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxMonitoredItemsQueueSize() {
    return ServerNodeSupport.read(this, getMaxMonitoredItemsQueueSizeNode(), UInteger.class, null);
  }

  @Override
  public void setMaxMonitoredItemsQueueSize(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxMonitoredItemsQueueSizeNode(),
        Namespaces.OPC_UA,
        "MaxMonitoredItemsQueueSize",
        value,
        false,
        false,
        false);
  }

  @Override
  public PropertyTypeNode getMaxQueryContinuationPointsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaxQueryContinuationPoints",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UShort getMaxQueryContinuationPoints() {
    return ServerNodeSupport.read(this, getMaxQueryContinuationPointsNode(), UShort.class, null);
  }

  @Override
  public void setMaxQueryContinuationPoints(@Nullable UShort value) {
    ServerNodeSupport.write(this, getMaxQueryContinuationPointsNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxSelectClauseParametersNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxSelectClauseParameters",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxSelectClauseParameters() {
    return ServerNodeSupport.read(this, getMaxSelectClauseParametersNode(), UInteger.class, null);
  }

  @Override
  public void setMaxSelectClauseParameters(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxSelectClauseParametersNode(),
        Namespaces.OPC_UA,
        "MaxSelectClauseParameters",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxSessionsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxSessions",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxSessions() {
    return ServerNodeSupport.read(this, getMaxSessionsNode(), UInteger.class, null);
  }

  @Override
  public void setMaxSessions(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this, getMaxSessionsNode(), Namespaces.OPC_UA, "MaxSessions", value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxStringLengthNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxStringLength",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxStringLength() {
    return ServerNodeSupport.read(this, getMaxStringLengthNode(), UInteger.class, null);
  }

  @Override
  public void setMaxStringLength(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxStringLengthNode(),
        Namespaces.OPC_UA,
        "MaxStringLength",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxSubscriptionsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxSubscriptions",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxSubscriptions() {
    return ServerNodeSupport.read(this, getMaxSubscriptionsNode(), UInteger.class, null);
  }

  @Override
  public void setMaxSubscriptions(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxSubscriptionsNode(),
        Namespaces.OPC_UA,
        "MaxSubscriptions",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxSubscriptionsPerSessionNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxSubscriptionsPerSession",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxSubscriptionsPerSession() {
    return ServerNodeSupport.read(this, getMaxSubscriptionsPerSessionNode(), UInteger.class, null);
  }

  @Override
  public void setMaxSubscriptionsPerSession(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxSubscriptionsPerSessionNode(),
        Namespaces.OPC_UA,
        "MaxSubscriptionsPerSession",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxWhereClauseParametersNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxWhereClauseParameters",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxWhereClauseParameters() {
    return ServerNodeSupport.read(this, getMaxWhereClauseParametersNode(), UInteger.class, null);
  }

  @Override
  public void setMaxWhereClauseParameters(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxWhereClauseParametersNode(),
        Namespaces.OPC_UA,
        "MaxWhereClauseParameters",
        value,
        false,
        false,
        false);
  }

  @Override
  public PropertyTypeNode getMinSupportedSampleRateNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MinSupportedSampleRate",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Double getMinSupportedSampleRate() {
    return ServerNodeSupport.read(this, getMinSupportedSampleRateNode(), Double.class, null);
  }

  @Override
  public void setMinSupportedSampleRate(@Nullable Double value) {
    ServerNodeSupport.write(this, getMinSupportedSampleRateNode(), value, false, false, false);
  }

  @Override
  public FolderTypeNode getModellingRulesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ModellingRules",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 61L),
        null,
        -1,
        FolderTypeNode.class);
  }

  @Override
  public @Nullable OperationLimitsTypeNode getOperationLimitsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "OperationLimits",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 11564L),
        null,
        -1,
        OperationLimitsTypeNode.class);
  }

  @Override
  public @Nullable RoleSetTypeNode getRoleSetNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "RoleSet",
        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 15607L),
        null,
        -1,
        RoleSetTypeNode.class);
  }

  @Override
  public PropertyTypeNode getServerProfileArrayNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ServerProfileArray",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String @Nullable [] getServerProfileArray() {
    return ServerNodeSupport.readArray(this, getServerProfileArrayNode(), String.class, null);
  }

  @Override
  public void setServerProfileArray(@Nullable String @Nullable [] value) {
    ServerNodeSupport.write(this, getServerProfileArrayNode(), value, true, false, false);
  }

  @Override
  public PropertyTypeNode getSoftwareCertificatesNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "SoftwareCertificates",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 344L),
        1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable SignedSoftwareCertificate @Nullable [] getSoftwareCertificates() {
    return ServerNodeSupport.readArray(
        this, getSoftwareCertificatesNode(), SignedSoftwareCertificate.class, null);
  }

  @Override
  public void setSoftwareCertificates(@Nullable SignedSoftwareCertificate @Nullable [] value) {
    ServerNodeSupport.write(this, getSoftwareCertificatesNode(), value, true, false, true);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getAggregateFunctionsNode();
    getConformanceUnitsNode();
    getLocaleIdArrayNode();
    getMaxArrayLengthNode();
    getMaxBrowseContinuationPointsNode();
    getMaxByteStringLengthNode();
    getMaxHistoryContinuationPointsNode();
    getMaxLogObjectContinuationPointsNode();
    getMaxMonitoredItemsNode();
    getMaxMonitoredItemsPerSubscriptionNode();
    getMaxMonitoredItemsQueueSizeNode();
    getMaxQueryContinuationPointsNode();
    getMaxSelectClauseParametersNode();
    getMaxSessionsNode();
    getMaxStringLengthNode();
    getMaxSubscriptionsNode();
    getMaxSubscriptionsPerSessionNode();
    getMaxWhereClauseParametersNode();
    getMinSupportedSampleRateNode();
    getModellingRulesNode();
    getOperationLimitsNode();
    getRoleSetNode();
    getServerProfileArrayNode();
    getSoftwareCertificatesNode();
  }
}
