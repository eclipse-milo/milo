package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
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
 * Node implementation of {@link SecurityGroupType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.4.1">Model
 *     documentation</a>
 */
public class SecurityGroupTypeNode extends BaseObjectTypeNode implements SecurityGroupType {
  public SecurityGroupTypeNode(
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
  public PropertyTypeNode getKeyLifetimeNode() throws UaException {
    return ClientNodeSupport.await(getKeyLifetimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getKeyLifetimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "KeyLifetime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readKeyLifetime() throws UaException {
    return ClientNodeSupport.await(readKeyLifetimeAsync());
  }

  @Override
  public void writeKeyLifetime(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeKeyLifetimeAsync(value)),
        "http://opcfoundation.org/UA/}KeyLifetime");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readKeyLifetimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getKeyLifetimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}KeyLifetime",
                            true,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeKeyLifetimeAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getKeyLifetimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}KeyLifetime",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getMaxPastKeyCountNode() throws UaException {
    return ClientNodeSupport.await(getMaxPastKeyCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxPastKeyCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxPastKeyCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxPastKeyCount() throws UaException {
    return ClientNodeSupport.await(readMaxPastKeyCountAsync());
  }

  @Override
  public void writeMaxPastKeyCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxPastKeyCountAsync(value)),
        "http://opcfoundation.org/UA/}MaxPastKeyCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxPastKeyCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxPastKeyCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxPastKeyCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxPastKeyCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxPastKeyCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxPastKeyCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getSecurityGroupIdNode() throws UaException {
    return ClientNodeSupport.await(getSecurityGroupIdNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSecurityGroupIdNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SecurityGroupId",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readSecurityGroupId() throws UaException {
    return ClientNodeSupport.await(readSecurityGroupIdAsync());
  }

  @Override
  public void writeSecurityGroupId(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSecurityGroupIdAsync(value)),
        "http://opcfoundation.org/UA/}SecurityGroupId");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readSecurityGroupIdAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSecurityGroupIdNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SecurityGroupId",
                            true,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSecurityGroupIdAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSecurityGroupIdNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SecurityGroupId",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getMaxFutureKeyCountNode() throws UaException {
    return ClientNodeSupport.await(getMaxFutureKeyCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getMaxFutureKeyCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxFutureKeyCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxFutureKeyCount() throws UaException {
    return ClientNodeSupport.await(readMaxFutureKeyCountAsync());
  }

  @Override
  public void writeMaxFutureKeyCount(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxFutureKeyCountAsync(value)),
        "http://opcfoundation.org/UA/}MaxFutureKeyCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxFutureKeyCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxFutureKeyCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxFutureKeyCount",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxFutureKeyCountAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxFutureKeyCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxFutureKeyCount",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getSecurityPolicyUriNode() throws UaException {
    return ClientNodeSupport.await(getSecurityPolicyUriNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSecurityPolicyUriNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
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
                            true,
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
  public @Nullable UaMethodNode getForceKeyRotationMethodNode() throws UaException {
    return ClientNodeSupport.await(getForceKeyRotationMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getForceKeyRotationMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "ForceKeyRotation",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void forceKeyRotation() throws UaException {
    ClientNodeSupport.await(forceKeyRotationAsync());
  }

  @Override
  public MethodCallResult<Void> callForceKeyRotation() throws UaException {
    return ClientNodeSupport.await(callForceKeyRotationAsync());
  }

  @Override
  public MethodCallResult<Void> callForceKeyRotationWith(MethodCallOptions options)
      throws UaException {
    return ClientNodeSupport.await(callForceKeyRotationWithAsync(options));
  }

  @Override
  public CompletableFuture<Void> forceKeyRotationAsync() {
    return ClientNodeSupport.compose(
        callForceKeyRotationAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callForceKeyRotationAsync() {
    return callForceKeyRotationWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callForceKeyRotationWithAsync(
      MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getForceKeyRotationMethodNodeAsync(),
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
  public @Nullable UaMethodNode getInvalidateKeysMethodNode() throws UaException {
    return ClientNodeSupport.await(getInvalidateKeysMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getInvalidateKeysMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "InvalidateKeys",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void invalidateKeys() throws UaException {
    ClientNodeSupport.await(invalidateKeysAsync());
  }

  @Override
  public MethodCallResult<Void> callInvalidateKeys() throws UaException {
    return ClientNodeSupport.await(callInvalidateKeysAsync());
  }

  @Override
  public MethodCallResult<Void> callInvalidateKeysWith(MethodCallOptions options)
      throws UaException {
    return ClientNodeSupport.await(callInvalidateKeysWithAsync(options));
  }

  @Override
  public CompletableFuture<Void> invalidateKeysAsync() {
    return ClientNodeSupport.compose(
        callInvalidateKeysAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callInvalidateKeysAsync() {
    return callInvalidateKeysWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callInvalidateKeysWithAsync(
      MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getInvalidateKeysMethodNodeAsync(),
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
