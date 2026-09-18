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
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link HistoricalExternalEventSourceType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.5.2">Model
 *     documentation</a>
 */
public class HistoricalExternalEventSourceTypeNode extends BaseObjectTypeNode
    implements HistoricalExternalEventSourceType {
  public HistoricalExternalEventSourceTypeNode(
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
  public @Nullable PropertyTypeNode getEndpointUrlNode() throws UaException {
    return ClientNodeSupport.await(getEndpointUrlNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getEndpointUrlNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "EndpointUrl",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readEndpointUrl() throws UaException {
    return ClientNodeSupport.await(readEndpointUrlAsync());
  }

  @Override
  public void writeEndpointUrl(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeEndpointUrlAsync(value)),
        "http://opcfoundation.org/UA/}EndpointUrl");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readEndpointUrlAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getEndpointUrlNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}EndpointUrl",
                            false,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeEndpointUrlAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getEndpointUrlNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}EndpointUrl",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getSecurityModeNode() throws UaException {
    return ClientNodeSupport.await(getSecurityModeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getSecurityModeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SecurityMode",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable MessageSecurityMode readSecurityMode() throws UaException {
    return ClientNodeSupport.await(readSecurityModeAsync());
  }

  @Override
  public void writeSecurityMode(@Nullable MessageSecurityMode value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSecurityModeAsync(value)),
        "http://opcfoundation.org/UA/}SecurityMode");
  }

  @Override
  public CompletableFuture<? extends @Nullable MessageSecurityMode> readSecurityModeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSecurityModeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SecurityMode",
                            false,
                            MessageSecurityMode.class,
                            -1,
                            MessageSecurityMode::from)),
                v -> CompletableFuture.completedFuture((@Nullable MessageSecurityMode) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSecurityModeAsync(@Nullable MessageSecurityMode value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSecurityModeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SecurityMode",
                        value,
                        MessageSecurityMode.class,
                        -1,
                        MessageSecurityMode::from)));
  }

  @Override
  public @Nullable PropertyTypeNode getSecurityPolicyUriNode() throws UaException {
    return ClientNodeSupport.await(getSecurityPolicyUriNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getSecurityPolicyUriNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SecurityPolicyUri",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readSecurityPolicyUri() throws UaException {
    return ClientNodeSupport.await(readSecurityPolicyUriAsync());
  }

  @Override
  public void writeSecurityPolicyUri(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSecurityPolicyUriAsync(value)),
        "http://opcfoundation.org/UA/}SecurityPolicyUri");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readSecurityPolicyUriAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSecurityPolicyUriNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SecurityPolicyUri",
                            false,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSecurityPolicyUriAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSecurityPolicyUriNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SecurityPolicyUri",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getIdentityTokenPolicyNode() throws UaException {
    return ClientNodeSupport.await(getIdentityTokenPolicyNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getIdentityTokenPolicyNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "IdentityTokenPolicy",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UserTokenPolicy readIdentityTokenPolicy() throws UaException {
    return ClientNodeSupport.await(readIdentityTokenPolicyAsync());
  }

  @Override
  public void writeIdentityTokenPolicy(@Nullable UserTokenPolicy value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeIdentityTokenPolicyAsync(value)),
        "http://opcfoundation.org/UA/}IdentityTokenPolicy");
  }

  @Override
  public CompletableFuture<? extends @Nullable UserTokenPolicy> readIdentityTokenPolicyAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getIdentityTokenPolicyNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}IdentityTokenPolicy",
                            false,
                            UserTokenPolicy.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UserTokenPolicy) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeIdentityTokenPolicyAsync(
      @Nullable UserTokenPolicy value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getIdentityTokenPolicyNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}IdentityTokenPolicy",
                        value,
                        UserTokenPolicy.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getTransportProfileUriNode() throws UaException {
    return ClientNodeSupport.await(getTransportProfileUriNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getTransportProfileUriNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "TransportProfileUri",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readTransportProfileUri() throws UaException {
    return ClientNodeSupport.await(readTransportProfileUriAsync());
  }

  @Override
  public void writeTransportProfileUri(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeTransportProfileUriAsync(value)),
        "http://opcfoundation.org/UA/}TransportProfileUri");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readTransportProfileUriAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getTransportProfileUriNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}TransportProfileUri",
                            false,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeTransportProfileUriAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getTransportProfileUriNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}TransportProfileUri",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getHistoricalEventFilterNode() throws UaException {
    return ClientNodeSupport.await(getHistoricalEventFilterNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getHistoricalEventFilterNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "HistoricalEventFilter",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable EventFilter readHistoricalEventFilter() throws UaException {
    return ClientNodeSupport.await(readHistoricalEventFilterAsync());
  }

  @Override
  public void writeHistoricalEventFilter(@Nullable EventFilter value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeHistoricalEventFilterAsync(value)),
        "http://opcfoundation.org/UA/}HistoricalEventFilter");
  }

  @Override
  public CompletableFuture<? extends @Nullable EventFilter> readHistoricalEventFilterAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getHistoricalEventFilterNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}HistoricalEventFilter",
                            true,
                            EventFilter.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable EventFilter) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeHistoricalEventFilterAsync(
      @Nullable EventFilter value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getHistoricalEventFilterNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}HistoricalEventFilter",
                        value,
                        EventFilter.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getServerNode() throws UaException {
    return ClientNodeSupport.await(getServerNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getServerNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Server",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readServer() throws UaException {
    return ClientNodeSupport.await(readServerAsync());
  }

  @Override
  public void writeServer(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeServerAsync(value)), "http://opcfoundation.org/UA/}Server");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readServerAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getServerNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Server",
                            false,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeServerAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getServerNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Server",
                        value,
                        String.class,
                        -1,
                        null)));
  }
}
