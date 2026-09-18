package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubKeyPushTargetFolderTypeAddPushTarget;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubKeyPushTargetFolderTypeAddPushTargetFolder;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubKeyPushTargetFolderTypeRemovePushTarget;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubKeyPushTargetFolderTypeRemovePushTargetFolder;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link PubSubKeyPushTargetFolderType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.1">Model
 *     documentation</a>
 */
public class PubSubKeyPushTargetFolderTypeNode extends FolderTypeNode
    implements PubSubKeyPushTargetFolderType {
  public PubSubKeyPushTargetFolderTypeNode(
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
  public UaMethodNode getAddPushTargetMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddPushTargetMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getAddPushTargetMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddPushTarget",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable NodeId addPushTarget(
      @Nullable String applicationUri,
      @Nullable String endpointUrl,
      @Nullable String securityPolicyUri,
      @Nullable UserTokenPolicy userTokenType,
      @Nullable UShort requestedKeyCount,
      @Nullable Double retryInterval)
      throws UaException {
    return ClientNodeSupport.await(
        addPushTargetAsync(
            applicationUri,
            endpointUrl,
            securityPolicyUri,
            userTokenType,
            requestedKeyCount,
            retryInterval));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddPushTarget(
      @Nullable String applicationUri,
      @Nullable String endpointUrl,
      @Nullable String securityPolicyUri,
      @Nullable UserTokenPolicy userTokenType,
      @Nullable UShort requestedKeyCount,
      @Nullable Double retryInterval)
      throws UaException {
    return ClientNodeSupport.await(
        callAddPushTargetAsync(
            applicationUri,
            endpointUrl,
            securityPolicyUri,
            userTokenType,
            requestedKeyCount,
            retryInterval));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddPushTargetWith(
      MethodCallOptions options,
      @Nullable String applicationUri,
      @Nullable String endpointUrl,
      @Nullable String securityPolicyUri,
      @Nullable UserTokenPolicy userTokenType,
      @Nullable UShort requestedKeyCount,
      @Nullable Double retryInterval)
      throws UaException {
    return ClientNodeSupport.await(
        callAddPushTargetWithAsync(
            options,
            applicationUri,
            endpointUrl,
            securityPolicyUri,
            userTokenType,
            requestedKeyCount,
            retryInterval));
  }

  @Override
  public CompletableFuture<@Nullable NodeId> addPushTargetAsync(
      @Nullable String applicationUri,
      @Nullable String endpointUrl,
      @Nullable String securityPolicyUri,
      @Nullable UserTokenPolicy userTokenType,
      @Nullable UShort requestedKeyCount,
      @Nullable Double retryInterval) {
    return ClientNodeSupport.compose(
        callAddPushTargetAsync(
            applicationUri,
            endpointUrl,
            securityPolicyUri,
            userTokenType,
            requestedKeyCount,
            retryInterval),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddPushTargetAsync(
      @Nullable String applicationUri,
      @Nullable String endpointUrl,
      @Nullable String securityPolicyUri,
      @Nullable UserTokenPolicy userTokenType,
      @Nullable UShort requestedKeyCount,
      @Nullable Double retryInterval) {
    return callAddPushTargetWithAsync(
        MethodCallOptions.DEFAULT,
        applicationUri,
        endpointUrl,
        securityPolicyUri,
        userTokenType,
        requestedKeyCount,
        retryInterval);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddPushTargetWithAsync(
      MethodCallOptions options,
      @Nullable String applicationUri,
      @Nullable String endpointUrl,
      @Nullable String securityPolicyUri,
      @Nullable UserTokenPolicy userTokenType,
      @Nullable UShort requestedKeyCount,
      @Nullable Double retryInterval) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new PubSubKeyPushTargetFolderTypeAddPushTarget.Inputs(
                      applicationUri,
                      endpointUrl,
                      securityPolicyUri,
                      userTokenType,
                      requestedKeyCount,
                      retryInterval)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddPushTargetMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return PubSubKeyPushTargetFolderTypeAddPushTarget.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .pushTargetId();
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getAddPushTargetFolderMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddPushTargetFolderMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getAddPushTargetFolderMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddPushTargetFolder",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable NodeId addPushTargetFolder(@Nullable String name) throws UaException {
    return ClientNodeSupport.await(addPushTargetFolderAsync(name));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddPushTargetFolder(@Nullable String name)
      throws UaException {
    return ClientNodeSupport.await(callAddPushTargetFolderAsync(name));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddPushTargetFolderWith(
      MethodCallOptions options, @Nullable String name) throws UaException {
    return ClientNodeSupport.await(callAddPushTargetFolderWithAsync(options, name));
  }

  @Override
  public CompletableFuture<@Nullable NodeId> addPushTargetFolderAsync(@Nullable String name) {
    return ClientNodeSupport.compose(
        callAddPushTargetFolderAsync(name),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddPushTargetFolderAsync(
      @Nullable String name) {
    return callAddPushTargetFolderWithAsync(MethodCallOptions.DEFAULT, name);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddPushTargetFolderWithAsync(
      MethodCallOptions options, @Nullable String name) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new PubSubKeyPushTargetFolderTypeAddPushTargetFolder.Inputs(name)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddPushTargetFolderMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return PubSubKeyPushTargetFolderTypeAddPushTargetFolder.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .pushTargetFolderNodeId();
                                  }))));
        });
  }

  @Override
  public UaMethodNode getRemovePushTargetMethodNode() throws UaException {
    return ClientNodeSupport.await(getRemovePushTargetMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getRemovePushTargetMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RemovePushTarget",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void removePushTarget(@Nullable NodeId pushTargetId) throws UaException {
    ClientNodeSupport.await(removePushTargetAsync(pushTargetId));
  }

  @Override
  public MethodCallResult<Void> callRemovePushTarget(@Nullable NodeId pushTargetId)
      throws UaException {
    return ClientNodeSupport.await(callRemovePushTargetAsync(pushTargetId));
  }

  @Override
  public MethodCallResult<Void> callRemovePushTargetWith(
      MethodCallOptions options, @Nullable NodeId pushTargetId) throws UaException {
    return ClientNodeSupport.await(callRemovePushTargetWithAsync(options, pushTargetId));
  }

  @Override
  public CompletableFuture<Void> removePushTargetAsync(@Nullable NodeId pushTargetId) {
    return ClientNodeSupport.compose(
        callRemovePushTargetAsync(pushTargetId),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemovePushTargetAsync(
      @Nullable NodeId pushTargetId) {
    return callRemovePushTargetWithAsync(MethodCallOptions.DEFAULT, pushTargetId);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemovePushTargetWithAsync(
      MethodCallOptions options, @Nullable NodeId pushTargetId) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new PubSubKeyPushTargetFolderTypeRemovePushTarget.Inputs(pushTargetId)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRemovePushTargetMethodNodeAsync(),
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
  public @Nullable UaMethodNode getRemovePushTargetFolderMethodNode() throws UaException {
    return ClientNodeSupport.await(getRemovePushTargetFolderMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getRemovePushTargetFolderMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RemovePushTargetFolder",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void removePushTargetFolder(@Nullable NodeId pushTargetFolderNodeId) throws UaException {
    ClientNodeSupport.await(removePushTargetFolderAsync(pushTargetFolderNodeId));
  }

  @Override
  public MethodCallResult<Void> callRemovePushTargetFolder(@Nullable NodeId pushTargetFolderNodeId)
      throws UaException {
    return ClientNodeSupport.await(callRemovePushTargetFolderAsync(pushTargetFolderNodeId));
  }

  @Override
  public MethodCallResult<Void> callRemovePushTargetFolderWith(
      MethodCallOptions options, @Nullable NodeId pushTargetFolderNodeId) throws UaException {
    return ClientNodeSupport.await(
        callRemovePushTargetFolderWithAsync(options, pushTargetFolderNodeId));
  }

  @Override
  public CompletableFuture<Void> removePushTargetFolderAsync(
      @Nullable NodeId pushTargetFolderNodeId) {
    return ClientNodeSupport.compose(
        callRemovePushTargetFolderAsync(pushTargetFolderNodeId),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemovePushTargetFolderAsync(
      @Nullable NodeId pushTargetFolderNodeId) {
    return callRemovePushTargetFolderWithAsync(MethodCallOptions.DEFAULT, pushTargetFolderNodeId);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemovePushTargetFolderWithAsync(
      MethodCallOptions options, @Nullable NodeId pushTargetFolderNodeId) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new PubSubKeyPushTargetFolderTypeRemovePushTargetFolder.Inputs(pushTargetFolderNodeId)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRemovePushTargetFolderMethodNodeAsync(),
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
