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
 * Node implementation of {@link ApplicationConfigurationType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.14">Model
 *     documentation</a>
 */
public class ApplicationConfigurationTypeNode extends ServerConfigurationTypeNode
    implements ApplicationConfigurationType {
  public ApplicationConfigurationTypeNode(
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
  public PropertyTypeNode getProductUriNode() throws UaException {
    return ClientNodeSupport.await(getProductUriNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getProductUriNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ProductUri",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public PropertyTypeNode getApplicationUriNode() throws UaException {
    return ClientNodeSupport.await(getApplicationUriNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getApplicationUriNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ApplicationUri",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable KeyCredentialConfigurationFolderTypeNode getKeyCredentialsNode()
      throws UaException {
    return ClientNodeSupport.await(getKeyCredentialsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable KeyCredentialConfigurationFolderTypeNode>
      getKeyCredentialsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "KeyCredentials",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        KeyCredentialConfigurationFolderTypeNode.class)));
  }

  @Override
  public PropertyTypeNode getApplicationTypeNode() throws UaException {
    return ClientNodeSupport.await(getApplicationTypeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getApplicationTypeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ApplicationType",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable PropertyTypeNode getIsNonUaApplicationNode() throws UaException {
    return ClientNodeSupport.await(getIsNonUaApplicationNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getIsNonUaApplicationNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "IsNonUaApplication",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readIsNonUaApplication() throws UaException {
    return ClientNodeSupport.await(readIsNonUaApplicationAsync());
  }

  @Override
  public void writeIsNonUaApplication(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeIsNonUaApplicationAsync(value)),
        "http://opcfoundation.org/UA/}IsNonUaApplication");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readIsNonUaApplicationAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getIsNonUaApplicationNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}IsNonUaApplication",
                            false,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeIsNonUaApplicationAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getIsNonUaApplicationNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}IsNonUaApplication",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable AuthorizationServicesConfigurationFolderTypeNode getAuthorizationServicesNode()
      throws UaException {
    return ClientNodeSupport.await(getAuthorizationServicesNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable AuthorizationServicesConfigurationFolderTypeNode>
      getAuthorizationServicesNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "AuthorizationServices",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        AuthorizationServicesConfigurationFolderTypeNode.class)));
  }

  @Override
  public PropertyTypeNode getEnabledNode() throws UaException {
    return ClientNodeSupport.await(getEnabledNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getEnabledNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Enabled",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readEnabled() throws UaException {
    return ClientNodeSupport.await(readEnabledAsync());
  }

  @Override
  public void writeEnabled(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeEnabledAsync(value)), "http://opcfoundation.org/UA/}Enabled");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readEnabledAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getEnabledNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Enabled",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeEnabledAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getEnabledNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Enabled",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }
}
