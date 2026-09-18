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
import org.eclipse.milo.opcua.stack.core.types.enumerated.RedundantServerMode;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link NonTransparentBackupRedundancyType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.15">Model
 *     documentation</a>
 */
public class NonTransparentBackupRedundancyTypeNode extends NonTransparentRedundancyTypeNode
    implements NonTransparentBackupRedundancyType {
  public NonTransparentBackupRedundancyTypeNode(
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
  public PropertyTypeNode getRedundantServerArrayNode() throws UaException {
    return ClientNodeSupport.await(getRedundantServerArrayNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getRedundantServerArrayNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "RedundantServerArray",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public PropertyTypeNode getModeNode() throws UaException {
    return ClientNodeSupport.await(getModeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getModeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Mode",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable RedundantServerMode readMode() throws UaException {
    return ClientNodeSupport.await(readModeAsync());
  }

  @Override
  public void writeMode(@Nullable RedundantServerMode value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeModeAsync(value)), "http://opcfoundation.org/UA/}Mode");
  }

  @Override
  public CompletableFuture<? extends @Nullable RedundantServerMode> readModeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getModeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Mode",
                            true,
                            RedundantServerMode.class,
                            -1,
                            RedundantServerMode::from)),
                v -> CompletableFuture.completedFuture((@Nullable RedundantServerMode) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeModeAsync(@Nullable RedundantServerMode value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getModeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Mode",
                        value,
                        RedundantServerMode.class,
                        -1,
                        RedundantServerMode::from)));
  }

  @Override
  public UaMethodNode getFailoverMethodNode() throws UaException {
    return ClientNodeSupport.await(getFailoverMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getFailoverMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Failover",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void failover() throws UaException {
    ClientNodeSupport.await(failoverAsync());
  }

  @Override
  public MethodCallResult<Void> callFailover() throws UaException {
    return ClientNodeSupport.await(callFailoverAsync());
  }

  @Override
  public MethodCallResult<Void> callFailoverWith(MethodCallOptions options) throws UaException {
    return ClientNodeSupport.await(callFailoverWithAsync(options));
  }

  @Override
  public CompletableFuture<Void> failoverAsync() {
    return ClientNodeSupport.compose(
        callFailoverAsync(),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callFailoverAsync() {
    return callFailoverWithAsync(MethodCallOptions.DEFAULT);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callFailoverWithAsync(
      MethodCallOptions options) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs = new Variant[0];
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getFailoverMethodNodeAsync(),
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
