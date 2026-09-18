package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.FileDirectoryTypeCreateDirectory;
import org.eclipse.milo.opcua.sdk.core.model.methods.FileDirectoryTypeCreateFile;
import org.eclipse.milo.opcua.sdk.core.model.methods.FileDirectoryTypeDelete;
import org.eclipse.milo.opcua.sdk.core.model.methods.FileDirectoryTypeMoveOrCopy;
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
 * Node implementation of {@link FileDirectoryType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.3.1">Model
 *     documentation</a>
 */
public class FileDirectoryTypeNode extends FolderTypeNode implements FileDirectoryType {
  public FileDirectoryTypeNode(
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
  public UaMethodNode getCreateDirectoryMethodNode() throws UaException {
    return ClientNodeSupport.await(getCreateDirectoryMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getCreateDirectoryMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "CreateDirectory",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable NodeId createDirectory(@Nullable String directoryName) throws UaException {
    return ClientNodeSupport.await(createDirectoryAsync(directoryName));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callCreateDirectory(@Nullable String directoryName)
      throws UaException {
    return ClientNodeSupport.await(callCreateDirectoryAsync(directoryName));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callCreateDirectoryWith(
      MethodCallOptions options, @Nullable String directoryName) throws UaException {
    return ClientNodeSupport.await(callCreateDirectoryWithAsync(options, directoryName));
  }

  @Override
  public CompletableFuture<@Nullable NodeId> createDirectoryAsync(@Nullable String directoryName) {
    return ClientNodeSupport.compose(
        callCreateDirectoryAsync(directoryName),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callCreateDirectoryAsync(
      @Nullable String directoryName) {
    return callCreateDirectoryWithAsync(MethodCallOptions.DEFAULT, directoryName);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callCreateDirectoryWithAsync(
      MethodCallOptions options, @Nullable String directoryName) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new FileDirectoryTypeCreateDirectory.Inputs(directoryName)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getCreateDirectoryMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return FileDirectoryTypeCreateDirectory.Outputs.fromVariants(
                                            client.getStaticEncodingContext(), values)
                                        .directoryNodeId();
                                  }))));
        });
  }

  @Override
  public UaMethodNode getCreateFileMethodNode() throws UaException {
    return ClientNodeSupport.await(getCreateFileMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getCreateFileMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "CreateFile",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public FileDirectoryTypeCreateFile.Outputs createFile(
      @Nullable String fileName, @Nullable Boolean requestFileOpen) throws UaException {
    return ClientNodeSupport.await(createFileAsync(fileName, requestFileOpen));
  }

  @Override
  public MethodCallResult<FileDirectoryTypeCreateFile.Outputs> callCreateFile(
      @Nullable String fileName, @Nullable Boolean requestFileOpen) throws UaException {
    return ClientNodeSupport.await(callCreateFileAsync(fileName, requestFileOpen));
  }

  @Override
  public MethodCallResult<FileDirectoryTypeCreateFile.Outputs> callCreateFileWith(
      MethodCallOptions options, @Nullable String fileName, @Nullable Boolean requestFileOpen)
      throws UaException {
    return ClientNodeSupport.await(callCreateFileWithAsync(options, fileName, requestFileOpen));
  }

  @Override
  public CompletableFuture<FileDirectoryTypeCreateFile.Outputs> createFileAsync(
      @Nullable String fileName, @Nullable Boolean requestFileOpen) {
    return ClientNodeSupport.compose(
        callCreateFileAsync(fileName, requestFileOpen),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<FileDirectoryTypeCreateFile.Outputs>>
      callCreateFileAsync(@Nullable String fileName, @Nullable Boolean requestFileOpen) {
    return callCreateFileWithAsync(MethodCallOptions.DEFAULT, fileName, requestFileOpen);
  }

  @Override
  public CompletableFuture<MethodCallResult<FileDirectoryTypeCreateFile.Outputs>>
      callCreateFileWithAsync(
          MethodCallOptions options, @Nullable String fileName, @Nullable Boolean requestFileOpen) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new FileDirectoryTypeCreateFile.Inputs(fileName, requestFileOpen)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getCreateFileMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return FileDirectoryTypeCreateFile.Outputs.fromVariants(
                                        client.getStaticEncodingContext(), values);
                                  }))));
        });
  }

  @Override
  public UaMethodNode getDelete_MethodNode() throws UaException {
    return ClientNodeSupport.await(getDelete_MethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getDelete_MethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Delete",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void delete_(@Nullable NodeId objectToDelete) throws UaException {
    ClientNodeSupport.await(delete_Async(objectToDelete));
  }

  @Override
  public MethodCallResult<Void> callDelete_(@Nullable NodeId objectToDelete) throws UaException {
    return ClientNodeSupport.await(callDelete_Async(objectToDelete));
  }

  @Override
  public MethodCallResult<Void> callDelete_With(
      MethodCallOptions options, @Nullable NodeId objectToDelete) throws UaException {
    return ClientNodeSupport.await(callDelete_WithAsync(options, objectToDelete));
  }

  @Override
  public CompletableFuture<Void> delete_Async(@Nullable NodeId objectToDelete) {
    return ClientNodeSupport.compose(
        callDelete_Async(objectToDelete),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callDelete_Async(
      @Nullable NodeId objectToDelete) {
    return callDelete_WithAsync(MethodCallOptions.DEFAULT, objectToDelete);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callDelete_WithAsync(
      MethodCallOptions options, @Nullable NodeId objectToDelete) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new FileDirectoryTypeDelete.Inputs(objectToDelete)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getDelete_MethodNodeAsync(),
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
  public UaMethodNode getMoveOrCopyMethodNode() throws UaException {
    return ClientNodeSupport.await(getMoveOrCopyMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getMoveOrCopyMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "MoveOrCopy",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable NodeId moveOrCopy(
      @Nullable NodeId objectToMoveOrCopy,
      @Nullable NodeId targetDirectory,
      @Nullable Boolean createCopy,
      @Nullable String newName)
      throws UaException {
    return ClientNodeSupport.await(
        moveOrCopyAsync(objectToMoveOrCopy, targetDirectory, createCopy, newName));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callMoveOrCopy(
      @Nullable NodeId objectToMoveOrCopy,
      @Nullable NodeId targetDirectory,
      @Nullable Boolean createCopy,
      @Nullable String newName)
      throws UaException {
    return ClientNodeSupport.await(
        callMoveOrCopyAsync(objectToMoveOrCopy, targetDirectory, createCopy, newName));
  }

  @Override
  public MethodCallResult<@Nullable NodeId> callMoveOrCopyWith(
      MethodCallOptions options,
      @Nullable NodeId objectToMoveOrCopy,
      @Nullable NodeId targetDirectory,
      @Nullable Boolean createCopy,
      @Nullable String newName)
      throws UaException {
    return ClientNodeSupport.await(
        callMoveOrCopyWithAsync(options, objectToMoveOrCopy, targetDirectory, createCopy, newName));
  }

  @Override
  public CompletableFuture<@Nullable NodeId> moveOrCopyAsync(
      @Nullable NodeId objectToMoveOrCopy,
      @Nullable NodeId targetDirectory,
      @Nullable Boolean createCopy,
      @Nullable String newName) {
    return ClientNodeSupport.compose(
        callMoveOrCopyAsync(objectToMoveOrCopy, targetDirectory, createCopy, newName),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callMoveOrCopyAsync(
      @Nullable NodeId objectToMoveOrCopy,
      @Nullable NodeId targetDirectory,
      @Nullable Boolean createCopy,
      @Nullable String newName) {
    return callMoveOrCopyWithAsync(
        MethodCallOptions.DEFAULT, objectToMoveOrCopy, targetDirectory, createCopy, newName);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable NodeId>> callMoveOrCopyWithAsync(
      MethodCallOptions options,
      @Nullable NodeId objectToMoveOrCopy,
      @Nullable NodeId targetDirectory,
      @Nullable Boolean createCopy,
      @Nullable String newName) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new FileDirectoryTypeMoveOrCopy.Inputs(
                      objectToMoveOrCopy, targetDirectory, createCopy, newName)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getMoveOrCopyMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return FileDirectoryTypeMoveOrCopy.Outputs.fromVariants(
                                            client.getStaticEncodingContext(), values)
                                        .newNodeId();
                                  }))));
        });
  }
}
