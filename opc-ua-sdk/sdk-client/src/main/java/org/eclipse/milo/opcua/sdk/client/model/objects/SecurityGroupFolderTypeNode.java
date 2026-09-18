package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.SecurityGroupFolderTypeAddSecurityGroup;
import org.eclipse.milo.opcua.sdk.core.model.methods.SecurityGroupFolderTypeAddSecurityGroupFolder;
import org.eclipse.milo.opcua.sdk.core.model.methods.SecurityGroupFolderTypeRemoveSecurityGroup;
import org.eclipse.milo.opcua.sdk.core.model.methods.SecurityGroupFolderTypeRemoveSecurityGroupFolder;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
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
 * Node implementation of {@link SecurityGroupFolderType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.5.1">Model
 *     documentation</a>
 */
public class SecurityGroupFolderTypeNode extends FolderTypeNode implements SecurityGroupFolderType {
  public SecurityGroupFolderTypeNode(
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
  public @Nullable PropertyTypeNode getSupportedSecurityPolicyUrisNode() throws UaException {
    return ClientNodeSupport.await(getSupportedSecurityPolicyUrisNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode>
      getSupportedSecurityPolicyUrisNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SupportedSecurityPolicyUris",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String @Nullable [] readSupportedSecurityPolicyUris() throws UaException {
    return ClientNodeSupport.await(readSupportedSecurityPolicyUrisAsync());
  }

  @Override
  public void writeSupportedSecurityPolicyUris(@Nullable String @Nullable [] value)
      throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSupportedSecurityPolicyUrisAsync(value)),
        "http://opcfoundation.org/UA/}SupportedSecurityPolicyUris");
  }

  @Override
  public CompletableFuture<? extends @Nullable String @Nullable []>
      readSupportedSecurityPolicyUrisAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSupportedSecurityPolicyUrisNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SupportedSecurityPolicyUris",
                            false,
                            String.class,
                            1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String @Nullable []) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSupportedSecurityPolicyUrisAsync(
      @Nullable String @Nullable [] value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSupportedSecurityPolicyUrisNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SupportedSecurityPolicyUris",
                        value,
                        String.class,
                        1,
                        null)));
  }

  @Override
  public UaMethodNode getAddSecurityGroupMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddSecurityGroupMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getAddSecurityGroupMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddSecurityGroup",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public SecurityGroupFolderTypeAddSecurityGroup.Outputs addSecurityGroup(
      @Nullable String securityGroupName,
      @Nullable Double keyLifetime,
      @Nullable String securityPolicyUri,
      @Nullable UInteger maxFutureKeyCount,
      @Nullable UInteger maxPastKeyCount)
      throws UaException {
    return ClientNodeSupport.await(
        addSecurityGroupAsync(
            securityGroupName, keyLifetime, securityPolicyUri, maxFutureKeyCount, maxPastKeyCount));
  }

  @Override
  public MethodCallResult<SecurityGroupFolderTypeAddSecurityGroup.Outputs> callAddSecurityGroup(
      @Nullable String securityGroupName,
      @Nullable Double keyLifetime,
      @Nullable String securityPolicyUri,
      @Nullable UInteger maxFutureKeyCount,
      @Nullable UInteger maxPastKeyCount)
      throws UaException {
    return ClientNodeSupport.await(
        callAddSecurityGroupAsync(
            securityGroupName, keyLifetime, securityPolicyUri, maxFutureKeyCount, maxPastKeyCount));
  }

  @Override
  public MethodCallResult<SecurityGroupFolderTypeAddSecurityGroup.Outputs> callAddSecurityGroupWith(
      MethodCallOptions options,
      @Nullable String securityGroupName,
      @Nullable Double keyLifetime,
      @Nullable String securityPolicyUri,
      @Nullable UInteger maxFutureKeyCount,
      @Nullable UInteger maxPastKeyCount)
      throws UaException {
    return ClientNodeSupport.await(
        callAddSecurityGroupWithAsync(
            options,
            securityGroupName,
            keyLifetime,
            securityPolicyUri,
            maxFutureKeyCount,
            maxPastKeyCount));
  }

  @Override
  public CompletableFuture<SecurityGroupFolderTypeAddSecurityGroup.Outputs> addSecurityGroupAsync(
      @Nullable String securityGroupName,
      @Nullable Double keyLifetime,
      @Nullable String securityPolicyUri,
      @Nullable UInteger maxFutureKeyCount,
      @Nullable UInteger maxPastKeyCount) {
    return ClientNodeSupport.compose(
        callAddSecurityGroupAsync(
            securityGroupName, keyLifetime, securityPolicyUri, maxFutureKeyCount, maxPastKeyCount),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<SecurityGroupFolderTypeAddSecurityGroup.Outputs>>
      callAddSecurityGroupAsync(
          @Nullable String securityGroupName,
          @Nullable Double keyLifetime,
          @Nullable String securityPolicyUri,
          @Nullable UInteger maxFutureKeyCount,
          @Nullable UInteger maxPastKeyCount) {
    return callAddSecurityGroupWithAsync(
        MethodCallOptions.DEFAULT,
        securityGroupName,
        keyLifetime,
        securityPolicyUri,
        maxFutureKeyCount,
        maxPastKeyCount);
  }

  @Override
  public CompletableFuture<MethodCallResult<SecurityGroupFolderTypeAddSecurityGroup.Outputs>>
      callAddSecurityGroupWithAsync(
          MethodCallOptions options,
          @Nullable String securityGroupName,
          @Nullable Double keyLifetime,
          @Nullable String securityPolicyUri,
          @Nullable UInteger maxFutureKeyCount,
          @Nullable UInteger maxPastKeyCount) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new SecurityGroupFolderTypeAddSecurityGroup.Inputs(
                      securityGroupName,
                      keyLifetime,
                      securityPolicyUri,
                      maxFutureKeyCount,
                      maxPastKeyCount)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddSecurityGroupMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return SecurityGroupFolderTypeAddSecurityGroup.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values);
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getAddSecurityGroupFolderMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddSecurityGroupFolderMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getAddSecurityGroupFolderMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddSecurityGroupFolder",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable NodeId addSecurityGroupFolder(@Nullable String name) throws UaException {
    return ClientNodeSupport.await(addSecurityGroupFolderAsync(name));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddSecurityGroupFolder(@Nullable String name)
      throws UaException {
    return ClientNodeSupport.await(callAddSecurityGroupFolderAsync(name));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddSecurityGroupFolderWith(
      MethodCallOptions options, @Nullable String name) throws UaException {
    return ClientNodeSupport.await(callAddSecurityGroupFolderWithAsync(options, name));
  }

  @Override
  public CompletableFuture<@Nullable NodeId> addSecurityGroupFolderAsync(@Nullable String name) {
    return ClientNodeSupport.compose(
        callAddSecurityGroupFolderAsync(name),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddSecurityGroupFolderAsync(
      @Nullable String name) {
    return callAddSecurityGroupFolderWithAsync(MethodCallOptions.DEFAULT, name);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddSecurityGroupFolderWithAsync(
      MethodCallOptions options, @Nullable String name) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new SecurityGroupFolderTypeAddSecurityGroupFolder.Inputs(name)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddSecurityGroupFolderMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return SecurityGroupFolderTypeAddSecurityGroupFolder.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .securityGroupFolderNodeId();
                                  }))));
        });
  }

  @Override
  public UaMethodNode getRemoveSecurityGroupMethodNode() throws UaException {
    return ClientNodeSupport.await(getRemoveSecurityGroupMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getRemoveSecurityGroupMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RemoveSecurityGroup",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void removeSecurityGroup(@Nullable NodeId securityGroupNodeId) throws UaException {
    ClientNodeSupport.await(removeSecurityGroupAsync(securityGroupNodeId));
  }

  @Override
  public MethodCallResult<Void> callRemoveSecurityGroup(@Nullable NodeId securityGroupNodeId)
      throws UaException {
    return ClientNodeSupport.await(callRemoveSecurityGroupAsync(securityGroupNodeId));
  }

  @Override
  public MethodCallResult<Void> callRemoveSecurityGroupWith(
      MethodCallOptions options, @Nullable NodeId securityGroupNodeId) throws UaException {
    return ClientNodeSupport.await(callRemoveSecurityGroupWithAsync(options, securityGroupNodeId));
  }

  @Override
  public CompletableFuture<Void> removeSecurityGroupAsync(@Nullable NodeId securityGroupNodeId) {
    return ClientNodeSupport.compose(
        callRemoveSecurityGroupAsync(securityGroupNodeId),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveSecurityGroupAsync(
      @Nullable NodeId securityGroupNodeId) {
    return callRemoveSecurityGroupWithAsync(MethodCallOptions.DEFAULT, securityGroupNodeId);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveSecurityGroupWithAsync(
      MethodCallOptions options, @Nullable NodeId securityGroupNodeId) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new SecurityGroupFolderTypeRemoveSecurityGroup.Inputs(securityGroupNodeId)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRemoveSecurityGroupMethodNodeAsync(),
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
  public @Nullable UaMethodNode getRemoveSecurityGroupFolderMethodNode() throws UaException {
    return ClientNodeSupport.await(getRemoveSecurityGroupFolderMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getRemoveSecurityGroupFolderMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RemoveSecurityGroupFolder",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void removeSecurityGroupFolder(@Nullable NodeId securityGroupFolderNodeId)
      throws UaException {
    ClientNodeSupport.await(removeSecurityGroupFolderAsync(securityGroupFolderNodeId));
  }

  @Override
  public MethodCallResult<Void> callRemoveSecurityGroupFolder(
      @Nullable NodeId securityGroupFolderNodeId) throws UaException {
    return ClientNodeSupport.await(callRemoveSecurityGroupFolderAsync(securityGroupFolderNodeId));
  }

  @Override
  public MethodCallResult<Void> callRemoveSecurityGroupFolderWith(
      MethodCallOptions options, @Nullable NodeId securityGroupFolderNodeId) throws UaException {
    return ClientNodeSupport.await(
        callRemoveSecurityGroupFolderWithAsync(options, securityGroupFolderNodeId));
  }

  @Override
  public CompletableFuture<Void> removeSecurityGroupFolderAsync(
      @Nullable NodeId securityGroupFolderNodeId) {
    return ClientNodeSupport.compose(
        callRemoveSecurityGroupFolderAsync(securityGroupFolderNodeId),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveSecurityGroupFolderAsync(
      @Nullable NodeId securityGroupFolderNodeId) {
    return callRemoveSecurityGroupFolderWithAsync(
        MethodCallOptions.DEFAULT, securityGroupFolderNodeId);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveSecurityGroupFolderWithAsync(
      MethodCallOptions options, @Nullable NodeId securityGroupFolderNodeId) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new SecurityGroupFolderTypeRemoveSecurityGroupFolder.Inputs(securityGroupFolderNodeId)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRemoveSecurityGroupFolderMethodNodeAsync(),
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
