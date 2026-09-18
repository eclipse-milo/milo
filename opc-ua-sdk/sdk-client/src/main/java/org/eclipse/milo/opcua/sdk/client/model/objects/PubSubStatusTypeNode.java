package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link PubSubStatusType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.1">Model
 *     documentation</a>
 */
public class PubSubStatusTypeNode extends BaseObjectTypeNode implements PubSubStatusType {
  public PubSubStatusTypeNode(
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
  public UaVariableNode getStateNode() throws UaException {
    return ClientNodeSupport.await(getStateNodeAsync());
  }

  @Override
  public CompletableFuture<? extends UaVariableNode> getStateNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "State",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Variable,
                        UaVariableNode.class)));
  }

  @Override
  public @Nullable PubSubState readState() throws UaException {
    return ClientNodeSupport.await(readStateAsync());
  }

  @Override
  public void writeState(@Nullable PubSubState value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeStateAsync(value)), "http://opcfoundation.org/UA/}State");
  }

  @Override
  public CompletableFuture<? extends @Nullable PubSubState> readStateAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getStateNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}State",
                            true,
                            PubSubState.class,
                            -1,
                            PubSubState::from)),
                v -> CompletableFuture.completedFuture((@Nullable PubSubState) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeStateAsync(@Nullable PubSubState value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getStateNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}State",
                        value,
                        PubSubState.class,
                        -1,
                        PubSubState::from)));
  }

  @Override
  public @Nullable UaMethodNode getDisableMethodNode() throws UaException {
    return ClientNodeSupport.await(getDisableMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getDisableMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Disable",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void disable() throws UaException {
    ClientNodeSupport.await(disableAsync());
  }

  @Override
  public MethodCallResult<Void> callDisable() throws UaException {
    return ClientNodeSupport.await(callDisableAsync());
  }

  @Override
  public MethodCallResult<Void> callDisableWith(MethodCallOptions options) throws UaException {
    return ClientNodeSupport.await(callDisableWithAsync(options));
  }

  @Override
  public CompletableFuture<Void> disableAsync() {
    return ClientNodeSupport.compose(
        callDisableAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callDisableAsync() {
    return callDisableWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callDisableWithAsync(MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getDisableMethodNodeAsync(),
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
  public @Nullable UaMethodNode getEnableMethodNode() throws UaException {
    return ClientNodeSupport.await(getEnableMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getEnableMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Enable",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void enable() throws UaException {
    ClientNodeSupport.await(enableAsync());
  }

  @Override
  public MethodCallResult<Void> callEnable() throws UaException {
    return ClientNodeSupport.await(callEnableAsync());
  }

  @Override
  public MethodCallResult<Void> callEnableWith(MethodCallOptions options) throws UaException {
    return ClientNodeSupport.await(callEnableWithAsync(options));
  }

  @Override
  public CompletableFuture<Void> enableAsync() {
    return ClientNodeSupport.compose(
        callEnableAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callEnableAsync() {
    return callEnableWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callEnableWithAsync(MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getEnableMethodNodeAsync(),
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
