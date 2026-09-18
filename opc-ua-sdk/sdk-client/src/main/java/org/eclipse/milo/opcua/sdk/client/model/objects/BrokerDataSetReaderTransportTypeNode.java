package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link BrokerDataSetReaderTransportType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.2/#9.3.2.4">Model
 *     documentation</a>
 */
public class BrokerDataSetReaderTransportTypeNode extends DataSetReaderTransportTypeNode
    implements BrokerDataSetReaderTransportType {
  public BrokerDataSetReaderTransportTypeNode(
      OpcUaClient client,
      NodeId nodeId,
      NodeClass nodeClass,
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
        client,
        nodeId,
        nodeClass,
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
  public PropertyTypeNode getResourceUriNode() throws UaException {
    return ClientNodeSupport.await(getResourceUriNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getResourceUriNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ResourceUri",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readResourceUri() throws UaException {
    return ClientNodeSupport.await(readResourceUriAsync());
  }

  @Override
  public void writeResourceUri(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeResourceUriAsync(value)),
        "http://opcfoundation.org/UA/}ResourceUri");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readResourceUriAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getResourceUriNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ResourceUri",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeResourceUriAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getResourceUriNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ResourceUri",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getMetaDataQueueNameNode() throws UaException {
    return ClientNodeSupport.await(getMetaDataQueueNameNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMetaDataQueueNameNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MetaDataQueueName",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readMetaDataQueueName() throws UaException {
    return ClientNodeSupport.await(readMetaDataQueueNameAsync());
  }

  @Override
  public void writeMetaDataQueueName(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMetaDataQueueNameAsync(value)),
        "http://opcfoundation.org/UA/}MetaDataQueueName");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readMetaDataQueueNameAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMetaDataQueueNameNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MetaDataQueueName",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMetaDataQueueNameAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMetaDataQueueNameNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MetaDataQueueName",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getAuthenticationProfileUriNode() throws UaException {
    return ClientNodeSupport.await(getAuthenticationProfileUriNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getAuthenticationProfileUriNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AuthenticationProfileUri",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readAuthenticationProfileUri() throws UaException {
    return ClientNodeSupport.await(readAuthenticationProfileUriAsync());
  }

  @Override
  public void writeAuthenticationProfileUri(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeAuthenticationProfileUriAsync(value)),
        "http://opcfoundation.org/UA/}AuthenticationProfileUri");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readAuthenticationProfileUriAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getAuthenticationProfileUriNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}AuthenticationProfileUri",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeAuthenticationProfileUriAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getAuthenticationProfileUriNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}AuthenticationProfileUri",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getRequestedDeliveryGuaranteeNode() throws UaException {
    return ClientNodeSupport.await(getRequestedDeliveryGuaranteeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getRequestedDeliveryGuaranteeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RequestedDeliveryGuarantee",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable BrokerTransportQualityOfService readRequestedDeliveryGuarantee()
      throws UaException {
    return ClientNodeSupport.await(readRequestedDeliveryGuaranteeAsync());
  }

  @Override
  public void writeRequestedDeliveryGuarantee(@Nullable BrokerTransportQualityOfService value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeRequestedDeliveryGuaranteeAsync(value)),
        "http://opcfoundation.org/UA/}RequestedDeliveryGuarantee");
  }

  @Override
  public CompletableFuture<? extends @Nullable BrokerTransportQualityOfService>
      readRequestedDeliveryGuaranteeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getRequestedDeliveryGuaranteeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}RequestedDeliveryGuarantee",
                            true,
                            BrokerTransportQualityOfService.class,
                            -1,
                            BrokerTransportQualityOfService::from)),
                v ->
                    CompletableFuture.completedFuture(
                        (@Nullable BrokerTransportQualityOfService) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeRequestedDeliveryGuaranteeAsync(
      @Nullable BrokerTransportQualityOfService value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getRequestedDeliveryGuaranteeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}RequestedDeliveryGuarantee",
                        value,
                        BrokerTransportQualityOfService.class,
                        -1,
                        BrokerTransportQualityOfService::from)));
  }

  @Override
  public PropertyTypeNode getQueueNameNode() throws UaException {
    return ClientNodeSupport.await(getQueueNameNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getQueueNameNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "QueueName",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readQueueName() throws UaException {
    return ClientNodeSupport.await(readQueueNameAsync());
  }

  @Override
  public void writeQueueName(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeQueueNameAsync(value)),
        "http://opcfoundation.org/UA/}QueueName");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readQueueNameAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getQueueNameNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}QueueName",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeQueueNameAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getQueueNameNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}QueueName",
                        value,
                        String.class,
                        -1,
                        null)));
  }
}
