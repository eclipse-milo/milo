package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.ReaderGroupTypeAddDataSetReader;
import org.eclipse.milo.opcua.sdk.core.model.methods.ReaderGroupTypeRemoveDataSetReader;
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
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetReaderDataType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ReaderGroupType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.9">Model
 *     documentation</a>
 */
public class ReaderGroupTypeNode extends PubSubGroupTypeNode implements ReaderGroupType {
  public ReaderGroupTypeNode(
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
  public @Nullable PubSubDiagnosticsReaderGroupTypeNode getDiagnosticsNode() throws UaException {
    return ClientNodeSupport.await(getDiagnosticsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PubSubDiagnosticsReaderGroupTypeNode>
      getDiagnosticsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Diagnostics",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        PubSubDiagnosticsReaderGroupTypeNode.class)));
  }

  @Override
  public @Nullable ReaderGroupMessageTypeNode getMessageSettingsNode() throws UaException {
    return ClientNodeSupport.await(getMessageSettingsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable ReaderGroupMessageTypeNode>
      getMessageSettingsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MessageSettings",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        ReaderGroupMessageTypeNode.class)));
  }

  @Override
  public @Nullable ReaderGroupTransportTypeNode getTransportSettingsNode() throws UaException {
    return ClientNodeSupport.await(getTransportSettingsNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable ReaderGroupTransportTypeNode>
      getTransportSettingsNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "TransportSettings",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                        NodeClass.Object,
                        ReaderGroupTransportTypeNode.class)));
  }

  @Override
  public @Nullable UaMethodNode getAddDataSetReaderMethodNode() throws UaException {
    return ClientNodeSupport.await(getAddDataSetReaderMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getAddDataSetReaderMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "AddDataSetReader",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable NodeId addDataSetReader(@Nullable DataSetReaderDataType configuration)
      throws UaException {
    return ClientNodeSupport.await(addDataSetReaderAsync(configuration));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddDataSetReader(
      @Nullable DataSetReaderDataType configuration) throws UaException {
    return ClientNodeSupport.await(callAddDataSetReaderAsync(configuration));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callAddDataSetReaderWith(
      MethodCallOptions options, @Nullable DataSetReaderDataType configuration) throws UaException {
    return ClientNodeSupport.await(callAddDataSetReaderWithAsync(options, configuration));
  }

  @Override
  public CompletableFuture<@Nullable NodeId> addDataSetReaderAsync(
      @Nullable DataSetReaderDataType configuration) {
    return ClientNodeSupport.compose(
        callAddDataSetReaderAsync(configuration),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddDataSetReaderAsync(
      @Nullable DataSetReaderDataType configuration) {
    return callAddDataSetReaderWithAsync(MethodCallOptions.DEFAULT, configuration);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callAddDataSetReaderWithAsync(
      MethodCallOptions options, @Nullable DataSetReaderDataType configuration) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new ReaderGroupTypeAddDataSetReader.Inputs(configuration)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getAddDataSetReaderMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return ReaderGroupTypeAddDataSetReader.Outputs.fromVariants(
                                            client.getStaticEncodingContext(), values)
                                        .dataSetReaderNodeId();
                                  }))));
        });
  }

  @Override
  public @Nullable UaMethodNode getRemoveDataSetReaderMethodNode() throws UaException {
    return ClientNodeSupport.await(getRemoveDataSetReaderMethodNodeAsync());
  }

  @Override
  public CompletableFuture<@Nullable UaMethodNode> getRemoveDataSetReaderMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.optionalChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "RemoveDataSetReader",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void removeDataSetReader(@Nullable NodeId dataSetReaderNodeId) throws UaException {
    ClientNodeSupport.await(removeDataSetReaderAsync(dataSetReaderNodeId));
  }

  @Override
  public MethodCallResult<Void> callRemoveDataSetReader(@Nullable NodeId dataSetReaderNodeId)
      throws UaException {
    return ClientNodeSupport.await(callRemoveDataSetReaderAsync(dataSetReaderNodeId));
  }

  @Override
  public MethodCallResult<Void> callRemoveDataSetReaderWith(
      MethodCallOptions options, @Nullable NodeId dataSetReaderNodeId) throws UaException {
    return ClientNodeSupport.await(callRemoveDataSetReaderWithAsync(options, dataSetReaderNodeId));
  }

  @Override
  public CompletableFuture<Void> removeDataSetReaderAsync(@Nullable NodeId dataSetReaderNodeId) {
    return ClientNodeSupport.compose(
        callRemoveDataSetReaderAsync(dataSetReaderNodeId),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveDataSetReaderAsync(
      @Nullable NodeId dataSetReaderNodeId) {
    return callRemoveDataSetReaderWithAsync(MethodCallOptions.DEFAULT, dataSetReaderNodeId);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callRemoveDataSetReaderWithAsync(
      MethodCallOptions options, @Nullable NodeId dataSetReaderNodeId) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new ReaderGroupTypeRemoveDataSetReader.Inputs(dataSetReaderNodeId)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getRemoveDataSetReaderMethodNodeAsync(),
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
