package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubConfigurationTypeCloseAndUpdate;
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubConfigurationTypeReserveIds;
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
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfigurationRefDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link PubSubConfigurationType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.1">Model
 *     documentation</a>
 */
public class PubSubConfigurationTypeNode extends FileTypeNode implements PubSubConfigurationType {
  public PubSubConfigurationTypeNode(
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
  public UaMethodNode getCloseAndUpdateMethodNode() throws UaException {
    return ClientNodeSupport.await(getCloseAndUpdateMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getCloseAndUpdateMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "CloseAndUpdate",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public PubSubConfigurationTypeCloseAndUpdate.Outputs closeAndUpdate(
      @Nullable UInteger fileHandle,
      @Nullable Boolean requireCompleteUpdate,
      @Nullable PubSubConfigurationRefDataType @Nullable [] configurationReferences)
      throws UaException {
    return ClientNodeSupport.await(
        closeAndUpdateAsync(fileHandle, requireCompleteUpdate, configurationReferences));
  }

  @Override
  public MethodCallResult<PubSubConfigurationTypeCloseAndUpdate.Outputs> callCloseAndUpdate(
      @Nullable UInteger fileHandle,
      @Nullable Boolean requireCompleteUpdate,
      @Nullable PubSubConfigurationRefDataType @Nullable [] configurationReferences)
      throws UaException {
    return ClientNodeSupport.await(
        callCloseAndUpdateAsync(fileHandle, requireCompleteUpdate, configurationReferences));
  }

  @Override
  public MethodCallResult<PubSubConfigurationTypeCloseAndUpdate.Outputs> callCloseAndUpdateWith(
      MethodCallOptions options,
      @Nullable UInteger fileHandle,
      @Nullable Boolean requireCompleteUpdate,
      @Nullable PubSubConfigurationRefDataType @Nullable [] configurationReferences)
      throws UaException {
    return ClientNodeSupport.await(
        callCloseAndUpdateWithAsync(
            options, fileHandle, requireCompleteUpdate, configurationReferences));
  }

  @Override
  public CompletableFuture<PubSubConfigurationTypeCloseAndUpdate.Outputs> closeAndUpdateAsync(
      @Nullable UInteger fileHandle,
      @Nullable Boolean requireCompleteUpdate,
      @Nullable PubSubConfigurationRefDataType @Nullable [] configurationReferences) {
    return ClientNodeSupport.compose(
        callCloseAndUpdateAsync(fileHandle, requireCompleteUpdate, configurationReferences),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<PubSubConfigurationTypeCloseAndUpdate.Outputs>>
      callCloseAndUpdateAsync(
          @Nullable UInteger fileHandle,
          @Nullable Boolean requireCompleteUpdate,
          @Nullable PubSubConfigurationRefDataType @Nullable [] configurationReferences) {
    return callCloseAndUpdateWithAsync(
        MethodCallOptions.DEFAULT, fileHandle, requireCompleteUpdate, configurationReferences);
  }

  @Override
  public CompletableFuture<MethodCallResult<PubSubConfigurationTypeCloseAndUpdate.Outputs>>
      callCloseAndUpdateWithAsync(
          MethodCallOptions options,
          @Nullable UInteger fileHandle,
          @Nullable Boolean requireCompleteUpdate,
          @Nullable PubSubConfigurationRefDataType @Nullable [] configurationReferences) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new PubSubConfigurationTypeCloseAndUpdate.Inputs(
                      fileHandle, requireCompleteUpdate, configurationReferences)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getCloseAndUpdateMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return PubSubConfigurationTypeCloseAndUpdate.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values);
                                  }))));
        });
  }

  @Override
  public UaMethodNode getReserveIdsMethodNode() throws UaException {
    return ClientNodeSupport.await(getReserveIdsMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getReserveIdsMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "ReserveIds",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public PubSubConfigurationTypeReserveIds.Outputs reserveIds(
      @Nullable String transportProfileUri,
      @Nullable UShort numReqWriterGroupIds,
      @Nullable UShort numReqDataSetWriterIds)
      throws UaException {
    return ClientNodeSupport.await(
        reserveIdsAsync(transportProfileUri, numReqWriterGroupIds, numReqDataSetWriterIds));
  }

  @Override
  public MethodCallResult<PubSubConfigurationTypeReserveIds.Outputs> callReserveIds(
      @Nullable String transportProfileUri,
      @Nullable UShort numReqWriterGroupIds,
      @Nullable UShort numReqDataSetWriterIds)
      throws UaException {
    return ClientNodeSupport.await(
        callReserveIdsAsync(transportProfileUri, numReqWriterGroupIds, numReqDataSetWriterIds));
  }

  @Override
  public MethodCallResult<PubSubConfigurationTypeReserveIds.Outputs> callReserveIdsWith(
      MethodCallOptions options,
      @Nullable String transportProfileUri,
      @Nullable UShort numReqWriterGroupIds,
      @Nullable UShort numReqDataSetWriterIds)
      throws UaException {
    return ClientNodeSupport.await(
        callReserveIdsWithAsync(
            options, transportProfileUri, numReqWriterGroupIds, numReqDataSetWriterIds));
  }

  @Override
  public CompletableFuture<PubSubConfigurationTypeReserveIds.Outputs> reserveIdsAsync(
      @Nullable String transportProfileUri,
      @Nullable UShort numReqWriterGroupIds,
      @Nullable UShort numReqDataSetWriterIds) {
    return ClientNodeSupport.compose(
        callReserveIdsAsync(transportProfileUri, numReqWriterGroupIds, numReqDataSetWriterIds),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<PubSubConfigurationTypeReserveIds.Outputs>>
      callReserveIdsAsync(
          @Nullable String transportProfileUri,
          @Nullable UShort numReqWriterGroupIds,
          @Nullable UShort numReqDataSetWriterIds) {
    return callReserveIdsWithAsync(
        MethodCallOptions.DEFAULT,
        transportProfileUri,
        numReqWriterGroupIds,
        numReqDataSetWriterIds);
  }

  @Override
  public CompletableFuture<MethodCallResult<PubSubConfigurationTypeReserveIds.Outputs>>
      callReserveIdsWithAsync(
          MethodCallOptions options,
          @Nullable String transportProfileUri,
          @Nullable UShort numReqWriterGroupIds,
          @Nullable UShort numReqDataSetWriterIds) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new PubSubConfigurationTypeReserveIds.Inputs(
                      transportProfileUri, numReqWriterGroupIds, numReqDataSetWriterIds)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getReserveIdsMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return PubSubConfigurationTypeReserveIds.Outputs.fromVariants(
                                        client.getStaticEncodingContext(), values);
                                  }))));
        });
  }
}
