package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.SubscribedDataSetFolderTypeAddDataSetFolder;
import org.eclipse.milo.opcua.sdk.core.model.methods.SubscribedDataSetFolderTypeAddSubscribedDataSet;
import org.eclipse.milo.opcua.sdk.core.model.methods.SubscribedDataSetFolderTypeRemoveDataSetFolder;
import org.eclipse.milo.opcua.sdk.core.model.methods.SubscribedDataSetFolderTypeRemoveSubscribedDataSet;
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
import org.eclipse.milo.opcua.stack.core.types.structured.StandaloneSubscribedDataSetDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link SubscribedDataSetFolderType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.9/#9.1.9.4.1">Model
 *     documentation</a>
 */
public class SubscribedDataSetFolderTypeNode extends FolderTypeNode
    implements SubscribedDataSetFolderType {
  public SubscribedDataSetFolderTypeNode(
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
  public @Nullable UaMethodNode getAddDataSetFolderMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddDataSetFolderMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getAddDataSetFolderMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddDataSetFolder",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable NodeId addDataSetFolder(@Nullable String name) throws UaException {
    return ClientNodeSupport.await(addDataSetFolderAsync(name));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddDataSetFolder(@Nullable String name)
      throws UaException {
    return ClientNodeSupport.await(callAddDataSetFolderAsync(name));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddDataSetFolderWith(
      MethodCallOptions options, @Nullable String name) throws UaException {
    return ClientNodeSupport.await(callAddDataSetFolderWithAsync(options, name));
  }

  @Override
  public CompletableFuture<@Nullable NodeId> addDataSetFolderAsync(@Nullable String name) {
    return ClientNodeSupport.compose(
        callAddDataSetFolderAsync(name),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddDataSetFolderAsync(
      @Nullable String name) {
    return callAddDataSetFolderWithAsync(MethodCallOptions.DEFAULT, name);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddDataSetFolderWithAsync(
      MethodCallOptions options, @Nullable String name) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new SubscribedDataSetFolderTypeAddDataSetFolder.Inputs(name)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddDataSetFolderMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return SubscribedDataSetFolderTypeAddDataSetFolder.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .dataSetFolderNodeId();
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getAddSubscribedDataSetMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddSubscribedDataSetMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getAddSubscribedDataSetMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddSubscribedDataSet",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable NodeId addSubscribedDataSet(
      @Nullable StandaloneSubscribedDataSetDataType subscribedDataSet) throws UaException {
    return ClientNodeSupport.await(addSubscribedDataSetAsync(subscribedDataSet));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddSubscribedDataSet(
      @Nullable StandaloneSubscribedDataSetDataType subscribedDataSet) throws UaException {
    return ClientNodeSupport.await(callAddSubscribedDataSetAsync(subscribedDataSet));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddSubscribedDataSetWith(
      MethodCallOptions options, @Nullable StandaloneSubscribedDataSetDataType subscribedDataSet)
      throws UaException {
    return ClientNodeSupport.await(callAddSubscribedDataSetWithAsync(options, subscribedDataSet));
  }

  @Override
  public CompletableFuture<@Nullable NodeId> addSubscribedDataSetAsync(
      @Nullable StandaloneSubscribedDataSetDataType subscribedDataSet) {
    return ClientNodeSupport.compose(
        callAddSubscribedDataSetAsync(subscribedDataSet),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddSubscribedDataSetAsync(
      @Nullable StandaloneSubscribedDataSetDataType subscribedDataSet) {
    return callAddSubscribedDataSetWithAsync(MethodCallOptions.DEFAULT, subscribedDataSet);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddSubscribedDataSetWithAsync(
      MethodCallOptions options, @Nullable StandaloneSubscribedDataSetDataType subscribedDataSet) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new SubscribedDataSetFolderTypeAddSubscribedDataSet.Inputs(subscribedDataSet)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddSubscribedDataSetMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return SubscribedDataSetFolderTypeAddSubscribedDataSet.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .subscribedDataSetNodeId();
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getRemoveDataSetFolderMethodNode() throws UaException {
    return ClientNodeSupport.await(getRemoveDataSetFolderMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getRemoveDataSetFolderMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RemoveDataSetFolder",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void removeDataSetFolder(@Nullable NodeId dataSetFolderNodeId) throws UaException {
    ClientNodeSupport.await(removeDataSetFolderAsync(dataSetFolderNodeId));
  }

  @Override
  public MethodCallResult<Void> callRemoveDataSetFolder(@Nullable NodeId dataSetFolderNodeId)
      throws UaException {
    return ClientNodeSupport.await(callRemoveDataSetFolderAsync(dataSetFolderNodeId));
  }

  @Override
  public MethodCallResult<Void> callRemoveDataSetFolderWith(
      MethodCallOptions options, @Nullable NodeId dataSetFolderNodeId) throws UaException {
    return ClientNodeSupport.await(callRemoveDataSetFolderWithAsync(options, dataSetFolderNodeId));
  }

  @Override
  public CompletableFuture<Void> removeDataSetFolderAsync(@Nullable NodeId dataSetFolderNodeId) {
    return ClientNodeSupport.compose(
        callRemoveDataSetFolderAsync(dataSetFolderNodeId),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveDataSetFolderAsync(
      @Nullable NodeId dataSetFolderNodeId) {
    return callRemoveDataSetFolderWithAsync(MethodCallOptions.DEFAULT, dataSetFolderNodeId);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveDataSetFolderWithAsync(
      MethodCallOptions options, @Nullable NodeId dataSetFolderNodeId) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new SubscribedDataSetFolderTypeRemoveDataSetFolder.Inputs(dataSetFolderNodeId)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRemoveDataSetFolderMethodNodeAsync(),
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
  public @Nullable UaMethodNode getRemoveSubscribedDataSetMethodNode() throws UaException {
    return ClientNodeSupport.await(getRemoveSubscribedDataSetMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getRemoveSubscribedDataSetMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RemoveSubscribedDataSet",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void removeSubscribedDataSet(@Nullable NodeId subscribedDataSetNodeId) throws UaException {
    ClientNodeSupport.await(removeSubscribedDataSetAsync(subscribedDataSetNodeId));
  }

  @Override
  public MethodCallResult<Void> callRemoveSubscribedDataSet(
      @Nullable NodeId subscribedDataSetNodeId) throws UaException {
    return ClientNodeSupport.await(callRemoveSubscribedDataSetAsync(subscribedDataSetNodeId));
  }

  @Override
  public MethodCallResult<Void> callRemoveSubscribedDataSetWith(
      MethodCallOptions options, @Nullable NodeId subscribedDataSetNodeId) throws UaException {
    return ClientNodeSupport.await(
        callRemoveSubscribedDataSetWithAsync(options, subscribedDataSetNodeId));
  }

  @Override
  public CompletableFuture<Void> removeSubscribedDataSetAsync(
      @Nullable NodeId subscribedDataSetNodeId) {
    return ClientNodeSupport.compose(
        callRemoveSubscribedDataSetAsync(subscribedDataSetNodeId),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveSubscribedDataSetAsync(
      @Nullable NodeId subscribedDataSetNodeId) {
    return callRemoveSubscribedDataSetWithAsync(MethodCallOptions.DEFAULT, subscribedDataSetNodeId);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveSubscribedDataSetWithAsync(
      MethodCallOptions options, @Nullable NodeId subscribedDataSetNodeId) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new SubscribedDataSetFolderTypeRemoveSubscribedDataSet.Inputs(subscribedDataSetNodeId)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRemoveSubscribedDataSetMethodNodeAsync(),
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
