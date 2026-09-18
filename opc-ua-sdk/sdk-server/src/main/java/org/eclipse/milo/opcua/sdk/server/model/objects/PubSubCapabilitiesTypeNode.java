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
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link PubSubCapabilitiesType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.12/#9.1.12.1">Model
 *     documentation</a>
 */
public class PubSubCapabilitiesTypeNode extends BaseObjectTypeNode
    implements PubSubCapabilitiesType {
  public PubSubCapabilitiesTypeNode(
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

  public PubSubCapabilitiesTypeNode(
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
  public PropertyTypeNode getMaxDataSetReadersNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaxDataSetReaders",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxDataSetReaders() {
    return ServerNodeSupport.read(this, getMaxDataSetReadersNode(), UInteger.class, null);
  }

  @Override
  public void setMaxDataSetReaders(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getMaxDataSetReadersNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getMaxDataSetWritersNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaxDataSetWriters",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxDataSetWriters() {
    return ServerNodeSupport.read(this, getMaxDataSetWritersNode(), UInteger.class, null);
  }

  @Override
  public void setMaxDataSetWriters(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getMaxDataSetWritersNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxDataSetWritersPerGroupNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxDataSetWritersPerGroup",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxDataSetWritersPerGroup() {
    return ServerNodeSupport.read(this, getMaxDataSetWritersPerGroupNode(), UInteger.class, null);
  }

  @Override
  public void setMaxDataSetWritersPerGroup(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxDataSetWritersPerGroupNode(),
        Namespaces.OPC_UA,
        "MaxDataSetWritersPerGroup",
        value,
        false,
        false,
        false);
  }

  @Override
  public PropertyTypeNode getMaxFieldsPerDataSetNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaxFieldsPerDataSet",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxFieldsPerDataSet() {
    return ServerNodeSupport.read(this, getMaxFieldsPerDataSetNode(), UInteger.class, null);
  }

  @Override
  public void setMaxFieldsPerDataSet(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getMaxFieldsPerDataSetNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNetworkMessageSizeBrokerNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxNetworkMessageSizeBroker",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxNetworkMessageSizeBroker() {
    return ServerNodeSupport.read(this, getMaxNetworkMessageSizeBrokerNode(), UInteger.class, null);
  }

  @Override
  public void setMaxNetworkMessageSizeBroker(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxNetworkMessageSizeBrokerNode(),
        Namespaces.OPC_UA,
        "MaxNetworkMessageSizeBroker",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxNetworkMessageSizeDatagramNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxNetworkMessageSizeDatagram",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxNetworkMessageSizeDatagram() {
    return ServerNodeSupport.read(
        this, getMaxNetworkMessageSizeDatagramNode(), UInteger.class, null);
  }

  @Override
  public void setMaxNetworkMessageSizeDatagram(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxNetworkMessageSizeDatagramNode(),
        Namespaces.OPC_UA,
        "MaxNetworkMessageSizeDatagram",
        value,
        false,
        false,
        false);
  }

  @Override
  public PropertyTypeNode getMaxPubSubConnectionsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaxPubSubConnections",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxPubSubConnections() {
    return ServerNodeSupport.read(this, getMaxPubSubConnectionsNode(), UInteger.class, null);
  }

  @Override
  public void setMaxPubSubConnections(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getMaxPubSubConnectionsNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxPublishedDataSetsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxPublishedDataSets",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxPublishedDataSets() {
    return ServerNodeSupport.read(this, getMaxPublishedDataSetsNode(), UInteger.class, null);
  }

  @Override
  public void setMaxPublishedDataSets(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxPublishedDataSetsNode(),
        Namespaces.OPC_UA,
        "MaxPublishedDataSets",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxPushTargetsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxPushTargets",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxPushTargets() {
    return ServerNodeSupport.read(this, getMaxPushTargetsNode(), UInteger.class, null);
  }

  @Override
  public void setMaxPushTargets(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxPushTargetsNode(),
        Namespaces.OPC_UA,
        "MaxPushTargets",
        value,
        false,
        false,
        false);
  }

  @Override
  public PropertyTypeNode getMaxReaderGroupsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaxReaderGroups",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxReaderGroups() {
    return ServerNodeSupport.read(this, getMaxReaderGroupsNode(), UInteger.class, null);
  }

  @Override
  public void setMaxReaderGroups(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getMaxReaderGroupsNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxSecurityGroupsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxSecurityGroups",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxSecurityGroups() {
    return ServerNodeSupport.read(this, getMaxSecurityGroupsNode(), UInteger.class, null);
  }

  @Override
  public void setMaxSecurityGroups(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxSecurityGroupsNode(),
        Namespaces.OPC_UA,
        "MaxSecurityGroups",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getMaxStandaloneSubscribedDataSetsNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "MaxStandaloneSubscribedDataSets",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxStandaloneSubscribedDataSets() {
    return ServerNodeSupport.read(
        this, getMaxStandaloneSubscribedDataSetsNode(), UInteger.class, null);
  }

  @Override
  public void setMaxStandaloneSubscribedDataSets(@Nullable UInteger value) {
    ServerNodeSupport.write(
        this,
        getMaxStandaloneSubscribedDataSetsNode(),
        Namespaces.OPC_UA,
        "MaxStandaloneSubscribedDataSets",
        value,
        false,
        false,
        false);
  }

  @Override
  public PropertyTypeNode getMaxWriterGroupsNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "MaxWriterGroups",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable UInteger getMaxWriterGroups() {
    return ServerNodeSupport.read(this, getMaxWriterGroupsNode(), UInteger.class, null);
  }

  @Override
  public void setMaxWriterGroups(@Nullable UInteger value) {
    ServerNodeSupport.write(this, getMaxWriterGroupsNode(), value, false, false, false);
  }

  @Override
  public @Nullable PropertyTypeNode getSupportSecurityKeyPullNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SupportSecurityKeyPull",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getSupportSecurityKeyPull() {
    return ServerNodeSupport.read(this, getSupportSecurityKeyPullNode(), Boolean.class, null);
  }

  @Override
  public void setSupportSecurityKeyPull(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this,
        getSupportSecurityKeyPullNode(),
        Namespaces.OPC_UA,
        "SupportSecurityKeyPull",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getSupportSecurityKeyPushNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SupportSecurityKeyPush",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getSupportSecurityKeyPush() {
    return ServerNodeSupport.read(this, getSupportSecurityKeyPushNode(), Boolean.class, null);
  }

  @Override
  public void setSupportSecurityKeyPush(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this,
        getSupportSecurityKeyPushNode(),
        Namespaces.OPC_UA,
        "SupportSecurityKeyPush",
        value,
        false,
        false,
        false);
  }

  @Override
  public @Nullable PropertyTypeNode getSupportSecurityKeyServerNode() {
    return ServerNodeSupport.optionalChild(
        this,
        Namespaces.OPC_UA,
        "SupportSecurityKeyServer",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable Boolean getSupportSecurityKeyServer() {
    return ServerNodeSupport.read(this, getSupportSecurityKeyServerNode(), Boolean.class, null);
  }

  @Override
  public void setSupportSecurityKeyServer(@Nullable Boolean value) {
    ServerNodeSupport.write(
        this,
        getSupportSecurityKeyServerNode(),
        Namespaces.OPC_UA,
        "SupportSecurityKeyServer",
        value,
        false,
        false,
        false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getMaxDataSetReadersNode();
    getMaxDataSetWritersNode();
    getMaxDataSetWritersPerGroupNode();
    getMaxFieldsPerDataSetNode();
    getMaxNetworkMessageSizeBrokerNode();
    getMaxNetworkMessageSizeDatagramNode();
    getMaxPubSubConnectionsNode();
    getMaxPublishedDataSetsNode();
    getMaxPushTargetsNode();
    getMaxReaderGroupsNode();
    getMaxSecurityGroupsNode();
    getMaxStandaloneSubscribedDataSetsNode();
    getMaxWriterGroupsNode();
    getSupportSecurityKeyPullNode();
    getSupportSecurityKeyPushNode();
    getSupportSecurityKeyServerNode();
  }
}
