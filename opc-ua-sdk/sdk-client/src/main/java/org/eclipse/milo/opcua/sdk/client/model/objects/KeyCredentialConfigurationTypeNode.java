package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.KeyCredentialConfigurationTypeGetEncryptingKey;
import org.eclipse.milo.opcua.sdk.core.model.methods.KeyCredentialConfigurationTypeUpdateCredential;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link KeyCredentialConfigurationType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.5">Model
 *     documentation</a>
 */
public class KeyCredentialConfigurationTypeNode extends BaseObjectTypeNode
    implements KeyCredentialConfigurationType {
  public KeyCredentialConfigurationTypeNode(
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
  public PropertyTypeNode getProfileUriNode() throws UaException {
    return ClientNodeSupport.await(getProfileUriNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getProfileUriNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ProfileUri",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readProfileUri() throws UaException {
    return ClientNodeSupport.await(readProfileUriAsync());
  }

  @Override
  public void writeProfileUri(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeProfileUriAsync(value)),
        "http://opcfoundation.org/UA/}ProfileUri");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readProfileUriAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getProfileUriNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ProfileUri",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeProfileUriAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getProfileUriNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ProfileUri",
                        value,
                        String.class,
                        -1,
                        null)));
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
  public @Nullable PropertyTypeNode getCredentialIdNode() throws UaException {
    return ClientNodeSupport.await(getCredentialIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getCredentialIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CredentialId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readCredentialId() throws UaException {
    return ClientNodeSupport.await(readCredentialIdAsync());
  }

  @Override
  public void writeCredentialId(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCredentialIdAsync(value)),
        "http://opcfoundation.org/UA/}CredentialId");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readCredentialIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCredentialIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CredentialId",
                            false,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCredentialIdAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCredentialIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CredentialId",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getEndpointUrlsNode() throws UaException {
    return ClientNodeSupport.await(getEndpointUrlsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getEndpointUrlsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "EndpointUrls",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String @Nullable [] readEndpointUrls() throws UaException {
    return ClientNodeSupport.await(readEndpointUrlsAsync());
  }

  @Override
  public void writeEndpointUrls(@Nullable String @Nullable [] value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeEndpointUrlsAsync(value)),
        "http://opcfoundation.org/UA/}EndpointUrls");
  }

  @Override
  public CompletableFuture<? extends @Nullable String @Nullable []> readEndpointUrlsAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getEndpointUrlsNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}EndpointUrls",
                            false,
                            String.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeEndpointUrlsAsync(@Nullable String @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getEndpointUrlsNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}EndpointUrls",
                        value,
                        String.class,
                        1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getServiceStatusNode() throws UaException {
    return ClientNodeSupport.await(getServiceStatusNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getServiceStatusNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ServiceStatus",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable StatusCode readServiceStatus() throws UaException {
    return ClientNodeSupport.await(readServiceStatusAsync());
  }

  @Override
  public void writeServiceStatus(@Nullable StatusCode value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeServiceStatusAsync(value)),
        "http://opcfoundation.org/UA/}ServiceStatus");
  }

  @Override
  public CompletableFuture<? extends @Nullable StatusCode> readServiceStatusAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getServiceStatusNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ServiceStatus",
                            false,
                            StatusCode.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable StatusCode) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeServiceStatusAsync(@Nullable StatusCode value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getServiceStatusNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ServiceStatus",
                        value,
                        StatusCode.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable UaMethodNode getDeleteCredentialMethodNode() throws UaException {
    return ClientNodeSupport.await(getDeleteCredentialMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getDeleteCredentialMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "DeleteCredential",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void deleteCredential() throws UaException {
    ClientNodeSupport.await(deleteCredentialAsync());
  }

  @Override
  public MethodCallResult<Void> callDeleteCredential() throws UaException {
    return ClientNodeSupport.await(callDeleteCredentialAsync());
  }

  @Override
  public MethodCallResult<Void> callDeleteCredentialWith(MethodCallOptions options)
      throws UaException {
    return ClientNodeSupport.await(callDeleteCredentialWithAsync(options));
  }

  @Override
  public CompletableFuture<Void> deleteCredentialAsync() {
    return ClientNodeSupport.compose(
        callDeleteCredentialAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callDeleteCredentialAsync() {
    return callDeleteCredentialWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callDeleteCredentialWithAsync(
      MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getDeleteCredentialMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    if (values == null || values.length != 0)
                                      throw new UaException(
                                          StatusCodes.Bad_DecodingError,
                                          "expected no Method outputs");
                                    return null;
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getGetEncryptingKeyMethodNode() throws UaException {
    return ClientNodeSupport.await(getGetEncryptingKeyMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getGetEncryptingKeyMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "GetEncryptingKey",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public KeyCredentialConfigurationTypeGetEncryptingKey.Outputs getEncryptingKey(
      @Nullable String credentialId, @Nullable String requestedSecurityPolicyUri)
      throws UaException {
    return ClientNodeSupport.await(getEncryptingKeyAsync(credentialId, requestedSecurityPolicyUri));
  }

  @Override
  public MethodCallResult<KeyCredentialConfigurationTypeGetEncryptingKey.Outputs>
      callGetEncryptingKey(
          @Nullable String credentialId, @Nullable String requestedSecurityPolicyUri)
          throws UaException {
    return ClientNodeSupport.await(
        callGetEncryptingKeyAsync(credentialId, requestedSecurityPolicyUri));
  }

  @Override
  public MethodCallResult<KeyCredentialConfigurationTypeGetEncryptingKey.Outputs>
      callGetEncryptingKeyWith(
          MethodCallOptions options,
          @Nullable String credentialId,
          @Nullable String requestedSecurityPolicyUri)
          throws UaException {
    return ClientNodeSupport.await(
        callGetEncryptingKeyWithAsync(options, credentialId, requestedSecurityPolicyUri));
  }

  @Override
  public CompletableFuture<KeyCredentialConfigurationTypeGetEncryptingKey.Outputs>
      getEncryptingKeyAsync(
          @Nullable String credentialId, @Nullable String requestedSecurityPolicyUri) {
    return ClientNodeSupport.compose(
        callGetEncryptingKeyAsync(credentialId, requestedSecurityPolicyUri),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<KeyCredentialConfigurationTypeGetEncryptingKey.Outputs>>
      callGetEncryptingKeyAsync(
          @Nullable String credentialId, @Nullable String requestedSecurityPolicyUri) {
    return callGetEncryptingKeyWithAsync(
        MethodCallOptions.DEFAULT, credentialId, requestedSecurityPolicyUri);
  }

  @Override
  public CompletableFuture<MethodCallResult<KeyCredentialConfigurationTypeGetEncryptingKey.Outputs>>
      callGetEncryptingKeyWithAsync(
          MethodCallOptions options,
          @Nullable String credentialId,
          @Nullable String requestedSecurityPolicyUri) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new KeyCredentialConfigurationTypeGetEncryptingKey.Inputs(
                      credentialId, requestedSecurityPolicyUri)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getGetEncryptingKeyMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return KeyCredentialConfigurationTypeGetEncryptingKey.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values);
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getUpdateCredentialMethodNode() throws UaException {
    return ClientNodeSupport.await(getUpdateCredentialMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getUpdateCredentialMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "UpdateCredential",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void updateCredential(
      @Nullable String credentialId,
      @Nullable ByteString credentialSecret,
      @Nullable String certificateThumbprint,
      @Nullable String securityPolicyUri)
      throws UaException {
    ClientNodeSupport.await(
        updateCredentialAsync(
            credentialId, credentialSecret, certificateThumbprint, securityPolicyUri));
  }

  @Override
  public MethodCallResult<Void> callUpdateCredential(
      @Nullable String credentialId,
      @Nullable ByteString credentialSecret,
      @Nullable String certificateThumbprint,
      @Nullable String securityPolicyUri)
      throws UaException {
    return ClientNodeSupport.await(
        callUpdateCredentialAsync(
            credentialId, credentialSecret, certificateThumbprint, securityPolicyUri));
  }

  @Override
  public MethodCallResult<Void> callUpdateCredentialWith(
      MethodCallOptions options,
      @Nullable String credentialId,
      @Nullable ByteString credentialSecret,
      @Nullable String certificateThumbprint,
      @Nullable String securityPolicyUri)
      throws UaException {
    return ClientNodeSupport.await(
        callUpdateCredentialWithAsync(
            options, credentialId, credentialSecret, certificateThumbprint, securityPolicyUri));
  }

  @Override
  public CompletableFuture<Void> updateCredentialAsync(
      @Nullable String credentialId,
      @Nullable ByteString credentialSecret,
      @Nullable String certificateThumbprint,
      @Nullable String securityPolicyUri) {
    return ClientNodeSupport.compose(
        callUpdateCredentialAsync(
            credentialId, credentialSecret, certificateThumbprint, securityPolicyUri),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callUpdateCredentialAsync(
      @Nullable String credentialId,
      @Nullable ByteString credentialSecret,
      @Nullable String certificateThumbprint,
      @Nullable String securityPolicyUri) {
    return callUpdateCredentialWithAsync(
        MethodCallOptions.DEFAULT,
        credentialId,
        credentialSecret,
        certificateThumbprint,
        securityPolicyUri);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callUpdateCredentialWithAsync(
      MethodCallOptions options,
      @Nullable String credentialId,
      @Nullable ByteString credentialSecret,
      @Nullable String certificateThumbprint,
      @Nullable String securityPolicyUri) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new KeyCredentialConfigurationTypeUpdateCredential.Inputs(
                      credentialId, credentialSecret, certificateThumbprint, securityPolicyUri)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getUpdateCredentialMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    if (values == null || values.length != 0)
                                      throw new UaException(
                                          StatusCodes.Bad_DecodingError,
                                          "expected no Method outputs");
                                    return null;
                                  }))));
        });
  }
}
