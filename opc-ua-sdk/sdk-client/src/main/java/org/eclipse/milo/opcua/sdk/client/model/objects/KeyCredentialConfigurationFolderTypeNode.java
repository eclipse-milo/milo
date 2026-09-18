package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.KeyCredentialConfigurationFolderTypeCreateCredential;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link KeyCredentialConfigurationFolderType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/8.6.2">Model
 *     documentation</a>
 */
public class KeyCredentialConfigurationFolderTypeNode extends FolderTypeNode
    implements KeyCredentialConfigurationFolderType {
  public KeyCredentialConfigurationFolderTypeNode(
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
  public @Nullable UaMethodNode getCreateCredentialMethodNode() throws UaException {
    return ClientNodeSupport.await(getCreateCredentialMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getCreateCredentialMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "CreateCredential",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable NodeId createCredential(
      @Nullable String name,
      @Nullable String resourceUri,
      @Nullable String profileUri,
      @Nullable String @Nullable [] endpointUrls)
      throws UaException {
    return ClientNodeSupport.await(
        createCredentialAsync(name, resourceUri, profileUri, endpointUrls));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callCreateCredential(
      @Nullable String name,
      @Nullable String resourceUri,
      @Nullable String profileUri,
      @Nullable String @Nullable [] endpointUrls)
      throws UaException {
    return ClientNodeSupport.await(
        callCreateCredentialAsync(name, resourceUri, profileUri, endpointUrls));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callCreateCredentialWith(
      MethodCallOptions options,
      @Nullable String name,
      @Nullable String resourceUri,
      @Nullable String profileUri,
      @Nullable String @Nullable [] endpointUrls)
      throws UaException {
    return ClientNodeSupport.await(
        callCreateCredentialWithAsync(options, name, resourceUri, profileUri, endpointUrls));
  }

  @Override
  public CompletableFuture<@Nullable NodeId> createCredentialAsync(
      @Nullable String name,
      @Nullable String resourceUri,
      @Nullable String profileUri,
      @Nullable String @Nullable [] endpointUrls) {
    return ClientNodeSupport.compose(
        callCreateCredentialAsync(name, resourceUri, profileUri, endpointUrls),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callCreateCredentialAsync(
      @Nullable String name,
      @Nullable String resourceUri,
      @Nullable String profileUri,
      @Nullable String @Nullable [] endpointUrls) {
    return callCreateCredentialWithAsync(
        MethodCallOptions.DEFAULT, name, resourceUri, profileUri, endpointUrls);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callCreateCredentialWithAsync(
      MethodCallOptions options,
      @Nullable String name,
      @Nullable String resourceUri,
      @Nullable String profileUri,
      @Nullable String @Nullable [] endpointUrls) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new KeyCredentialConfigurationFolderTypeCreateCredential.Inputs(
                      name, resourceUri, profileUri, endpointUrls)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getCreateCredentialMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return KeyCredentialConfigurationFolderTypeCreateCredential
                                        .Outputs.fromVariants(
                                            client.getStaticEncodingContext(), values)
                                        .credentialNodeId();
                                  }))));
        });
  }
}
