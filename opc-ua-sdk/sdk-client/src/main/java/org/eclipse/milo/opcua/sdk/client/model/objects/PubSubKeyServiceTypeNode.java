package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubKeyServiceTypeGetSecurityGroup;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubKeyServiceTypeGetSecurityKeys;
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
 * Node implementation of {@link PubSubKeyServiceType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.1">Model
 *     documentation</a>
 */
public class PubSubKeyServiceTypeNode extends BaseObjectTypeNode implements PubSubKeyServiceType {
  public PubSubKeyServiceTypeNode(
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
  public @Nullable PubSubKeyPushTargetFolderTypeNode getKeyPushTargetsNode() throws UaException {
    return ClientNodeSupport.await(getKeyPushTargetsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PubSubKeyPushTargetFolderTypeNode>
      getKeyPushTargetsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "KeyPushTargets",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        PubSubKeyPushTargetFolderTypeNode.class)));
  }

  @Override
  public @Nullable SecurityGroupFolderTypeNode getSecurityGroupsNode() throws UaException {
    return ClientNodeSupport.await(getSecurityGroupsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable SecurityGroupFolderTypeNode>
      getSecurityGroupsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SecurityGroups",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        SecurityGroupFolderTypeNode.class)));
  }

  @Override
  public @Nullable UaMethodNode getGetSecurityGroupMethodNode() throws UaException {
    return ClientNodeSupport.await(getGetSecurityGroupMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getGetSecurityGroupMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "GetSecurityGroup",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable NodeId getSecurityGroup(@Nullable String securityGroupId) throws UaException {
    return ClientNodeSupport.await(getSecurityGroupAsync(securityGroupId));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callGetSecurityGroup(@Nullable String securityGroupId)
      throws UaException {
    return ClientNodeSupport.await(callGetSecurityGroupAsync(securityGroupId));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callGetSecurityGroupWith(
      MethodCallOptions options, @Nullable String securityGroupId) throws UaException {
    return ClientNodeSupport.await(callGetSecurityGroupWithAsync(options, securityGroupId));
  }

  @Override
  public CompletableFuture<@Nullable NodeId> getSecurityGroupAsync(
      @Nullable String securityGroupId) {
    return ClientNodeSupport.compose(
        callGetSecurityGroupAsync(securityGroupId),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callGetSecurityGroupAsync(
      @Nullable String securityGroupId) {
    return callGetSecurityGroupWithAsync(MethodCallOptions.DEFAULT, securityGroupId);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callGetSecurityGroupWithAsync(
      MethodCallOptions options, @Nullable String securityGroupId) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new PubSubKeyServiceTypeGetSecurityGroup.Inputs(securityGroupId)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getGetSecurityGroupMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return PubSubKeyServiceTypeGetSecurityGroup.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .securityGroupNodeId();
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getGetSecurityKeysMethodNode() throws UaException {
    return ClientNodeSupport.await(getGetSecurityKeysMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getGetSecurityKeysMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "GetSecurityKeys",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public PubSubKeyServiceTypeGetSecurityKeys.Outputs getSecurityKeys(
      @Nullable String securityGroupId,
      @Nullable UInteger startingTokenId,
      @Nullable UInteger requestedKeyCount)
      throws UaException {
    return ClientNodeSupport.await(
        getSecurityKeysAsync(securityGroupId, startingTokenId, requestedKeyCount));
  }

  @Override
  public MethodCallResult<PubSubKeyServiceTypeGetSecurityKeys.Outputs> callGetSecurityKeys(
      @Nullable String securityGroupId,
      @Nullable UInteger startingTokenId,
      @Nullable UInteger requestedKeyCount)
      throws UaException {
    return ClientNodeSupport.await(
        callGetSecurityKeysAsync(securityGroupId, startingTokenId, requestedKeyCount));
  }

  @Override
  public MethodCallResult<PubSubKeyServiceTypeGetSecurityKeys.Outputs> callGetSecurityKeysWith(
      MethodCallOptions options,
      @Nullable String securityGroupId,
      @Nullable UInteger startingTokenId,
      @Nullable UInteger requestedKeyCount)
      throws UaException {
    return ClientNodeSupport.await(
        callGetSecurityKeysWithAsync(options, securityGroupId, startingTokenId, requestedKeyCount));
  }

  @Override
  public CompletableFuture<PubSubKeyServiceTypeGetSecurityKeys.Outputs> getSecurityKeysAsync(
      @Nullable String securityGroupId,
      @Nullable UInteger startingTokenId,
      @Nullable UInteger requestedKeyCount) {
    return ClientNodeSupport.compose(
        callGetSecurityKeysAsync(securityGroupId, startingTokenId, requestedKeyCount),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<PubSubKeyServiceTypeGetSecurityKeys.Outputs>>
      callGetSecurityKeysAsync(
          @Nullable String securityGroupId,
          @Nullable UInteger startingTokenId,
          @Nullable UInteger requestedKeyCount) {
    return callGetSecurityKeysWithAsync(
        MethodCallOptions.DEFAULT, securityGroupId, startingTokenId, requestedKeyCount);
  }

  @Override
  public CompletableFuture<MethodCallResult<PubSubKeyServiceTypeGetSecurityKeys.Outputs>>
      callGetSecurityKeysWithAsync(
          MethodCallOptions options,
          @Nullable String securityGroupId,
          @Nullable UInteger startingTokenId,
          @Nullable UInteger requestedKeyCount) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new PubSubKeyServiceTypeGetSecurityKeys.Inputs(
                      securityGroupId, startingTokenId, requestedKeyCount)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getGetSecurityKeysMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return PubSubKeyServiceTypeGetSecurityKeys.Outputs.fromVariants(
                                        client.getStaticEncodingContext(), values);
                                  }))));
        });
  }
}
