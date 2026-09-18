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
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link BrokerWriterGroupTransportType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.2/#9.3.2.2">Model
 *     documentation</a>
 */
public class BrokerWriterGroupTransportTypeNode extends WriterGroupTransportTypeNode
    implements BrokerWriterGroupTransportType {
  public BrokerWriterGroupTransportTypeNode(
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

  public BrokerWriterGroupTransportTypeNode(
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
  public PropertyTypeNode getAuthenticationProfileUriNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "AuthenticationProfileUri",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getAuthenticationProfileUri() {
    return ServerNodeSupport.read(this, getAuthenticationProfileUriNode(), String.class, null);
  }

  @Override
  public void setAuthenticationProfileUri(@Nullable String value) {
    ServerNodeSupport.write(this, getAuthenticationProfileUriNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getQueueNameNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "QueueName",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getQueueName() {
    return ServerNodeSupport.read(this, getQueueNameNode(), String.class, null);
  }

  @Override
  public void setQueueName(@Nullable String value) {
    ServerNodeSupport.write(this, getQueueNameNode(), value, false, false, false);
  }

  @Override
  public PropertyTypeNode getRequestedDeliveryGuaranteeNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "RequestedDeliveryGuarantee",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 15008L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable BrokerTransportQualityOfService getRequestedDeliveryGuarantee() {
    return ServerNodeSupport.read(
        this,
        getRequestedDeliveryGuaranteeNode(),
        BrokerTransportQualityOfService.class,
        BrokerTransportQualityOfService::from);
  }

  @Override
  public void setRequestedDeliveryGuarantee(@Nullable BrokerTransportQualityOfService value) {
    ServerNodeSupport.write(this, getRequestedDeliveryGuaranteeNode(), value, false, true, false);
  }

  @Override
  public PropertyTypeNode getResourceUriNode() {
    return ServerNodeSupport.mandatoryChild(
        this,
        Namespaces.OPC_UA,
        "ResourceUri",
        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 68L),
        ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
        -1,
        PropertyTypeNode.class);
  }

  @Override
  public @Nullable String getResourceUri() {
    return ServerNodeSupport.read(this, getResourceUriNode(), String.class, null);
  }

  @Override
  public void setResourceUri(@Nullable String value) {
    ServerNodeSupport.write(this, getResourceUriNode(), value, false, false, false);
  }

  @Override
  public void validateChildren() {
    super.validateChildren();
    getAuthenticationProfileUriNode();
    getQueueNameNode();
    getRequestedDeliveryGuaranteeNode();
    getResourceUriNode();
  }
}
