package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.RoleSetTypeAddRole;
import org.eclipse.milo.opcua.sdk.core.model.methods.RoleSetTypeRemoveRole;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
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
 * Node implementation of {@link RoleSetType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.1">Model
 *     documentation</a>
 */
public class RoleSetTypeNode extends BaseObjectTypeNode implements RoleSetType {
  public RoleSetTypeNode(
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
  public UaMethodNode getAddRoleMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddRoleMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getAddRoleMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddRole",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable NodeId addRole(@Nullable String roleName, @Nullable String namespaceUri)
      throws UaException {
    return ClientNodeSupport.await(addRoleAsync(roleName, namespaceUri));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddRole(
      @Nullable String roleName, @Nullable String namespaceUri) throws UaException {
    return ClientNodeSupport.await(callAddRoleAsync(roleName, namespaceUri));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddRoleWith(
      MethodCallOptions options, @Nullable String roleName, @Nullable String namespaceUri)
      throws UaException {
    return ClientNodeSupport.await(callAddRoleWithAsync(options, roleName, namespaceUri));
  }

  @Override
  public CompletableFuture<@Nullable NodeId> addRoleAsync(
      @Nullable String roleName, @Nullable String namespaceUri) {
    return ClientNodeSupport.compose(
        callAddRoleAsync(roleName, namespaceUri),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddRoleAsync(
      @Nullable String roleName, @Nullable String namespaceUri) {
    return callAddRoleWithAsync(MethodCallOptions.DEFAULT, roleName, namespaceUri);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddRoleWithAsync(
      MethodCallOptions options, @Nullable String roleName, @Nullable String namespaceUri) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new RoleSetTypeAddRole.Inputs(roleName, namespaceUri)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddRoleMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return RoleSetTypeAddRole.Outputs.fromVariants(
                                            client.getStaticEncodingContext(), values)
                                        .roleNodeId();
                                  }))));
        });
  }

  @Override
  public UaMethodNode getRemoveRoleMethodNode() throws UaException {
    return ClientNodeSupport.await(getRemoveRoleMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getRemoveRoleMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RemoveRole",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void removeRole(@Nullable NodeId roleNodeId) throws UaException {
    ClientNodeSupport.await(removeRoleAsync(roleNodeId));
  }

  @Override
  public MethodCallResult<Void> callRemoveRole(@Nullable NodeId roleNodeId) throws UaException {
    return ClientNodeSupport.await(callRemoveRoleAsync(roleNodeId));
  }

  @Override
  public MethodCallResult<Void> callRemoveRoleWith(
      MethodCallOptions options, @Nullable NodeId roleNodeId) throws UaException {
    return ClientNodeSupport.await(callRemoveRoleWithAsync(options, roleNodeId));
  }

  @Override
  public CompletableFuture<Void> removeRoleAsync(@Nullable NodeId roleNodeId) {
    return ClientNodeSupport.compose(
        callRemoveRoleAsync(roleNodeId),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveRoleAsync(
      @Nullable NodeId roleNodeId) {
    return callRemoveRoleWithAsync(MethodCallOptions.DEFAULT, roleNodeId);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveRoleWithAsync(
      MethodCallOptions options, @Nullable NodeId roleNodeId) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new RoleSetTypeRemoveRole.Inputs(roleNodeId)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRemoveRoleMethodNodeAsync(),
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
