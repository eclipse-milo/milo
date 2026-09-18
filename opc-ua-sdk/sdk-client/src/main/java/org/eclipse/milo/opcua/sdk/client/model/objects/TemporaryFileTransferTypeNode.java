package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.TemporaryFileTransferTypeCloseAndCommit;
import org.eclipse.milo.opcua.sdk.core.model.methods.TemporaryFileTransferTypeGenerateFileForRead;
import org.eclipse.milo.opcua.sdk.core.model.methods.TemporaryFileTransferTypeGenerateFileForWrite;
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
 * Node implementation of {@link TemporaryFileTransferType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.1">Model
 *     documentation</a>
 */
public class TemporaryFileTransferTypeNode extends BaseObjectTypeNode
    implements TemporaryFileTransferType {
  public TemporaryFileTransferTypeNode(
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
  public PropertyTypeNode getClientProcessingTimeoutNode() throws UaException {
    return ClientNodeSupport.await(getClientProcessingTimeoutNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getClientProcessingTimeoutNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ClientProcessingTimeout",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readClientProcessingTimeout() throws UaException {
    return ClientNodeSupport.await(readClientProcessingTimeoutAsync());
  }

  @Override
  public void writeClientProcessingTimeout(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeClientProcessingTimeoutAsync(value)),
        "http://opcfoundation.org/UA/}ClientProcessingTimeout");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readClientProcessingTimeoutAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getClientProcessingTimeoutNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ClientProcessingTimeout",
                            true,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeClientProcessingTimeoutAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getClientProcessingTimeoutNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ClientProcessingTimeout",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public UaMethodNode getCloseAndCommitMethodNode() throws UaException {
    return ClientNodeSupport.await(getCloseAndCommitMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getCloseAndCommitMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "CloseAndCommit",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable NodeId closeAndCommit(@Nullable UInteger fileHandle) throws UaException {
    return ClientNodeSupport.await(closeAndCommitAsync(fileHandle));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callCloseAndCommit(@Nullable UInteger fileHandle)
      throws UaException {
    return ClientNodeSupport.await(callCloseAndCommitAsync(fileHandle));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callCloseAndCommitWith(
      MethodCallOptions options, @Nullable UInteger fileHandle) throws UaException {
    return ClientNodeSupport.await(callCloseAndCommitWithAsync(options, fileHandle));
  }

  @Override
  public CompletableFuture<@Nullable NodeId> closeAndCommitAsync(@Nullable UInteger fileHandle) {
    return ClientNodeSupport.compose(
        callCloseAndCommitAsync(fileHandle),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callCloseAndCommitAsync(
      @Nullable UInteger fileHandle) {
    return callCloseAndCommitWithAsync(MethodCallOptions.DEFAULT, fileHandle);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callCloseAndCommitWithAsync(
      MethodCallOptions options, @Nullable UInteger fileHandle) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new TemporaryFileTransferTypeCloseAndCommit.Inputs(fileHandle)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getCloseAndCommitMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return TemporaryFileTransferTypeCloseAndCommit.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values)
                                        .completionStateMachine();
                                  }))));
        });
  }

  @Override
  public UaMethodNode getGenerateFileForReadMethodNode() throws UaException {
    return ClientNodeSupport.await(getGenerateFileForReadMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getGenerateFileForReadMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "GenerateFileForRead",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public TemporaryFileTransferTypeGenerateFileForRead.Outputs generateFileForRead(
      @Nullable Variant generateOptions) throws UaException {
    return ClientNodeSupport.await(generateFileForReadAsync(generateOptions));
  }

  @Override
  public MethodCallResult<TemporaryFileTransferTypeGenerateFileForRead.Outputs>
      callGenerateFileForRead(@Nullable Variant generateOptions) throws UaException {
    return ClientNodeSupport.await(callGenerateFileForReadAsync(generateOptions));
  }

  @Override
  public MethodCallResult<TemporaryFileTransferTypeGenerateFileForRead.Outputs>
      callGenerateFileForReadWith(MethodCallOptions options, @Nullable Variant generateOptions)
          throws UaException {
    return ClientNodeSupport.await(callGenerateFileForReadWithAsync(options, generateOptions));
  }

  @Override
  public CompletableFuture<TemporaryFileTransferTypeGenerateFileForRead.Outputs>
      generateFileForReadAsync(@Nullable Variant generateOptions) {
    return ClientNodeSupport.compose(
        callGenerateFileForReadAsync(generateOptions),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<TemporaryFileTransferTypeGenerateFileForRead.Outputs>>
      callGenerateFileForReadAsync(@Nullable Variant generateOptions) {
    return callGenerateFileForReadWithAsync(MethodCallOptions.DEFAULT, generateOptions);
  }

  @Override
  public CompletableFuture<MethodCallResult<TemporaryFileTransferTypeGenerateFileForRead.Outputs>>
      callGenerateFileForReadWithAsync(
          MethodCallOptions options, @Nullable Variant generateOptions) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new TemporaryFileTransferTypeGenerateFileForRead.Inputs(generateOptions)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getGenerateFileForReadMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return TemporaryFileTransferTypeGenerateFileForRead.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values);
                                  }))));
        });
  }

  @Override
  public UaMethodNode getGenerateFileForWriteMethodNode() throws UaException {
    return ClientNodeSupport.await(getGenerateFileForWriteMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getGenerateFileForWriteMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "GenerateFileForWrite",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public TemporaryFileTransferTypeGenerateFileForWrite.Outputs generateFileForWrite(
      @Nullable Variant generateOptions) throws UaException {
    return ClientNodeSupport.await(generateFileForWriteAsync(generateOptions));
  }

  @Override
  public MethodCallResult<TemporaryFileTransferTypeGenerateFileForWrite.Outputs>
      callGenerateFileForWrite(@Nullable Variant generateOptions) throws UaException {
    return ClientNodeSupport.await(callGenerateFileForWriteAsync(generateOptions));
  }

  @Override
  public MethodCallResult<TemporaryFileTransferTypeGenerateFileForWrite.Outputs>
      callGenerateFileForWriteWith(MethodCallOptions options, @Nullable Variant generateOptions)
          throws UaException {
    return ClientNodeSupport.await(callGenerateFileForWriteWithAsync(options, generateOptions));
  }

  @Override
  public CompletableFuture<TemporaryFileTransferTypeGenerateFileForWrite.Outputs>
      generateFileForWriteAsync(@Nullable Variant generateOptions) {
    return ClientNodeSupport.compose(
        callGenerateFileForWriteAsync(generateOptions),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<TemporaryFileTransferTypeGenerateFileForWrite.Outputs>>
      callGenerateFileForWriteAsync(@Nullable Variant generateOptions) {
    return callGenerateFileForWriteWithAsync(MethodCallOptions.DEFAULT, generateOptions);
  }

  @Override
  public CompletableFuture<MethodCallResult<TemporaryFileTransferTypeGenerateFileForWrite.Outputs>>
      callGenerateFileForWriteWithAsync(
          MethodCallOptions options, @Nullable Variant generateOptions) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new TemporaryFileTransferTypeGenerateFileForWrite.Inputs(generateOptions)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getGenerateFileForWriteMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return TemporaryFileTransferTypeGenerateFileForWrite.Outputs
                                        .fromVariants(client.getStaticEncodingContext(), values);
                                  }))));
        });
  }
}
