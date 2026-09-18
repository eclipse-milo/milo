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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link BrokerConnectionTransportType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.2/#9.3.2.1">Model
 *     documentation</a>
 */
public class BrokerConnectionTransportTypeNode extends ConnectionTransportTypeNode
    implements BrokerConnectionTransportType {
  public BrokerConnectionTransportTypeNode(
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
}
