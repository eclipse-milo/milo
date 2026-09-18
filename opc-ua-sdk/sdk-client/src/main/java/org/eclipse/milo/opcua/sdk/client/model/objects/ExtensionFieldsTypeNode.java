package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.ExtensionFieldsTypeAddExtensionField;
import org.eclipse.milo.opcua.sdk.core.model.methods.ExtensionFieldsTypeRemoveExtensionField;
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
 * Node implementation of {@link ExtensionFieldsType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.4/#9.1.4.2.2">Model
 *     documentation</a>
 */
public class ExtensionFieldsTypeNode extends BaseObjectTypeNode implements ExtensionFieldsType {
  public ExtensionFieldsTypeNode(
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
  public UaMethodNode getAddExtensionFieldMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddExtensionFieldMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getAddExtensionFieldMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddExtensionField",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable NodeId addExtensionField(
      @Nullable QualifiedName fieldName, @Nullable Variant fieldValue) throws UaException {
    return ClientNodeSupport.await(addExtensionFieldAsync(fieldName, fieldValue));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddExtensionField(
      @Nullable QualifiedName fieldName, @Nullable Variant fieldValue) throws UaException {
    return ClientNodeSupport.await(callAddExtensionFieldAsync(fieldName, fieldValue));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddExtensionFieldWith(
      MethodCallOptions options, @Nullable QualifiedName fieldName, @Nullable Variant fieldValue)
      throws UaException {
    return ClientNodeSupport.await(callAddExtensionFieldWithAsync(options, fieldName, fieldValue));
  }

  @Override
  public CompletableFuture<@Nullable NodeId> addExtensionFieldAsync(
      @Nullable QualifiedName fieldName, @Nullable Variant fieldValue) {
    return ClientNodeSupport.compose(
        callAddExtensionFieldAsync(fieldName, fieldValue),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddExtensionFieldAsync(
      @Nullable QualifiedName fieldName, @Nullable Variant fieldValue) {
    return callAddExtensionFieldWithAsync(MethodCallOptions.DEFAULT, fieldName, fieldValue);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddExtensionFieldWithAsync(
      MethodCallOptions options, @Nullable QualifiedName fieldName, @Nullable Variant fieldValue) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new ExtensionFieldsTypeAddExtensionField.Inputs(fieldName, fieldValue)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddExtensionFieldMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return ExtensionFieldsTypeAddExtensionField.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .fieldId();
                                  }))));
        });
  }

  @Override
  public UaMethodNode getRemoveExtensionFieldMethodNode() throws UaException {
    return ClientNodeSupport.await(getRemoveExtensionFieldMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getRemoveExtensionFieldMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RemoveExtensionField",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void removeExtensionField(@Nullable NodeId fieldId) throws UaException {
    ClientNodeSupport.await(removeExtensionFieldAsync(fieldId));
  }

  @Override
  public MethodCallResult<Void> callRemoveExtensionField(@Nullable NodeId fieldId)
      throws UaException {
    return ClientNodeSupport.await(callRemoveExtensionFieldAsync(fieldId));
  }

  @Override
  public MethodCallResult<Void> callRemoveExtensionFieldWith(
      MethodCallOptions options, @Nullable NodeId fieldId) throws UaException {
    return ClientNodeSupport.await(callRemoveExtensionFieldWithAsync(options, fieldId));
  }

  @Override
  public CompletableFuture<Void> removeExtensionFieldAsync(@Nullable NodeId fieldId) {
    return ClientNodeSupport.compose(
        callRemoveExtensionFieldAsync(fieldId),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveExtensionFieldAsync(
      @Nullable NodeId fieldId) {
    return callRemoveExtensionFieldWithAsync(MethodCallOptions.DEFAULT, fieldId);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveExtensionFieldWithAsync(
      MethodCallOptions options, @Nullable NodeId fieldId) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new ExtensionFieldsTypeRemoveExtensionField.Inputs(fieldId)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRemoveExtensionFieldMethodNodeAsync(),
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
