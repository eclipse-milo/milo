package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.FileTypeClose;
import org.eclipse.milo.opcua.sdk.core.model.methods.FileTypeGetPosition;
import org.eclipse.milo.opcua.sdk.core.model.methods.FileTypeOpen;
import org.eclipse.milo.opcua.sdk.core.model.methods.FileTypeRead;
import org.eclipse.milo.opcua.sdk.core.model.methods.FileTypeSetPosition;
import org.eclipse.milo.opcua.sdk.core.model.methods.FileTypeWrite;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link FileType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.2.1">Model
 *     documentation</a>
 */
public class FileTypeNode extends BaseObjectTypeNode implements FileType {
  public FileTypeNode(
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
  public PropertyTypeNode getUserWritableNode() throws UaException {
    return ClientNodeSupport.await(getUserWritableNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getUserWritableNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "UserWritable",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readUserWritable() throws UaException {
    return ClientNodeSupport.await(readUserWritableAsync());
  }

  @Override
  public void writeUserWritable(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeUserWritableAsync(value)),
        "http://opcfoundation.org/UA/}UserWritable");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readUserWritableAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getUserWritableNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}UserWritable",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeUserWritableAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getUserWritableNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}UserWritable",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getLastModifiedTimeNode() throws UaException {
    return ClientNodeSupport.await(getLastModifiedTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getLastModifiedTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LastModifiedTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DateTime readLastModifiedTime() throws UaException {
    return ClientNodeSupport.await(readLastModifiedTimeAsync());
  }

  @Override
  public void writeLastModifiedTime(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLastModifiedTimeAsync(value)),
        "http://opcfoundation.org/UA/}LastModifiedTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readLastModifiedTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLastModifiedTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LastModifiedTime",
                            false,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLastModifiedTimeAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLastModifiedTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LastModifiedTime",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMaxByteStringLengthNode() throws UaException {
    return ClientNodeSupport.await(getMaxByteStringLengthNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMaxByteStringLengthNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MaxByteStringLength",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readMaxByteStringLength() throws UaException {
    return ClientNodeSupport.await(readMaxByteStringLengthAsync());
  }

  @Override
  public void writeMaxByteStringLength(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMaxByteStringLengthAsync(value)),
        "http://opcfoundation.org/UA/}MaxByteStringLength");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readMaxByteStringLengthAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMaxByteStringLengthNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MaxByteStringLength",
                            false,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMaxByteStringLengthAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMaxByteStringLengthNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MaxByteStringLength",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getSizeNode() throws UaException {
    return ClientNodeSupport.await(getSizeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSizeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Size",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable ULong readSize() throws UaException {
    return ClientNodeSupport.await(readSizeAsync());
  }

  @Override
  public void writeSize(@Nullable ULong value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSizeAsync(value)), "http://opcfoundation.org/UA/}Size");
  }

  @Override
  public CompletableFuture<? extends @Nullable ULong> readSizeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSizeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Size",
                            true,
                            ULong.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable ULong) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSizeAsync(@Nullable ULong value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSizeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Size",
                        value,
                        ULong.class,
                        -1,
                        null)));
  }

  @Override
  public @Nullable PropertyTypeNode getMimeTypeNode() throws UaException {
    return ClientNodeSupport.await(getMimeTypeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends @Nullable PropertyTypeNode> getMimeTypeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.optionalChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "MimeType",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable String readMimeType() throws UaException {
    return ClientNodeSupport.await(readMimeTypeAsync());
  }

  @Override
  public void writeMimeType(@Nullable String value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeMimeTypeAsync(value)),
        "http://opcfoundation.org/UA/}MimeType");
  }

  @Override
  public CompletableFuture<? extends @Nullable String> readMimeTypeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getMimeTypeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}MimeType",
                            false,
                            String.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable String) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeMimeTypeAsync(@Nullable String value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getMimeTypeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}MimeType",
                        value,
                        String.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getWritableNode() throws UaException {
    return ClientNodeSupport.await(getWritableNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getWritableNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "Writable",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Boolean readWritable() throws UaException {
    return ClientNodeSupport.await(readWritableAsync());
  }

  @Override
  public void writeWritable(@Nullable Boolean value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeWritableAsync(value)),
        "http://opcfoundation.org/UA/}Writable");
  }

  @Override
  public CompletableFuture<? extends @Nullable Boolean> readWritableAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getWritableNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}Writable",
                            true,
                            Boolean.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Boolean) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeWritableAsync(@Nullable Boolean value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getWritableNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}Writable",
                        value,
                        Boolean.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getOpenCountNode() throws UaException {
    return ClientNodeSupport.await(getOpenCountNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getOpenCountNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "OpenCount",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UShort readOpenCount() throws UaException {
    return ClientNodeSupport.await(readOpenCountAsync());
  }

  @Override
  public void writeOpenCount(@Nullable UShort value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeOpenCountAsync(value)),
        "http://opcfoundation.org/UA/}OpenCount");
  }

  @Override
  public CompletableFuture<? extends @Nullable UShort> readOpenCountAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getOpenCountNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}OpenCount",
                            true,
                            UShort.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UShort) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeOpenCountAsync(@Nullable UShort value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getOpenCountNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}OpenCount",
                        value,
                        UShort.class,
                        -1,
                        null)));
  }

  @Override
  public UaMethodNode getCloseMethodNode() throws UaException {
    return ClientNodeSupport.await(getCloseMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getCloseMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Close",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void close(@Nullable UInteger fileHandle) throws UaException {
    ClientNodeSupport.await(closeAsync(fileHandle));
  }

  @Override
  public MethodCallResult<Void> callClose(@Nullable UInteger fileHandle) throws UaException {
    return ClientNodeSupport.await(callCloseAsync(fileHandle));
  }

  @Override
  public MethodCallResult<Void> callCloseWith(
      MethodCallOptions options, @Nullable UInteger fileHandle) throws UaException {
    return ClientNodeSupport.await(callCloseWithAsync(options, fileHandle));
  }

  @Override
  public CompletableFuture<Void> closeAsync(@Nullable UInteger fileHandle) {
    return ClientNodeSupport.compose(
        callCloseAsync(fileHandle),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callCloseAsync(@Nullable UInteger fileHandle) {
    return callCloseWithAsync(MethodCallOptions.DEFAULT, fileHandle);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callCloseWithAsync(
      MethodCallOptions options, @Nullable UInteger fileHandle) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new FileTypeClose.Inputs(fileHandle).toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getCloseMethodNodeAsync(),
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
  public UaMethodNode getGetPositionMethodNode() throws UaException {
    return ClientNodeSupport.await(getGetPositionMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getGetPositionMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "GetPosition",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable ULong getPosition(@Nullable UInteger fileHandle) throws UaException {
    return ClientNodeSupport.await(getPositionAsync(fileHandle));
  }

  @Override
  public MethodCallResult<@Nullable ULong> callGetPosition(@Nullable UInteger fileHandle)
      throws UaException {
    return ClientNodeSupport.await(callGetPositionAsync(fileHandle));
  }

  @Override
  public MethodCallResult<@Nullable ULong> callGetPositionWith(
      MethodCallOptions options, @Nullable UInteger fileHandle) throws UaException {
    return ClientNodeSupport.await(callGetPositionWithAsync(options, fileHandle));
  }

  @Override
  public CompletableFuture<@Nullable ULong> getPositionAsync(@Nullable UInteger fileHandle) {
    return ClientNodeSupport.compose(
        callGetPositionAsync(fileHandle),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable ULong>> callGetPositionAsync(
      @Nullable UInteger fileHandle) {
    return callGetPositionWithAsync(MethodCallOptions.DEFAULT, fileHandle);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable ULong>> callGetPositionWithAsync(
      MethodCallOptions options, @Nullable UInteger fileHandle) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new FileTypeGetPosition.Inputs(fileHandle)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getGetPositionMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return FileTypeGetPosition.Outputs.fromVariants(
                                            client.getStaticEncodingContext(), values)
                                        .position();
                                  }))));
        });
  }

  @Override
  public UaMethodNode getOpenMethodNode() throws UaException {
    return ClientNodeSupport.await(getOpenMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getOpenMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Open",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable UInteger open(@Nullable UByte mode) throws UaException {
    return ClientNodeSupport.await(openAsync(mode));
  }

  @Override
  public MethodCallResult<@Nullable UInteger> callOpen(@Nullable UByte mode) throws UaException {
    return ClientNodeSupport.await(callOpenAsync(mode));
  }

  @Override
  public MethodCallResult<@Nullable UInteger> callOpenWith(
      MethodCallOptions options, @Nullable UByte mode) throws UaException {
    return ClientNodeSupport.await(callOpenWithAsync(options, mode));
  }

  @Override
  public CompletableFuture<@Nullable UInteger> openAsync(@Nullable UByte mode) {
    return ClientNodeSupport.compose(
        callOpenAsync(mode),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable UInteger>> callOpenAsync(
      @Nullable UByte mode) {
    return callOpenWithAsync(MethodCallOptions.DEFAULT, mode);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable UInteger>> callOpenWithAsync(
      MethodCallOptions options, @Nullable UByte mode) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new FileTypeOpen.Inputs(mode).toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getOpenMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return FileTypeOpen.Outputs.fromVariants(
                                            client.getStaticEncodingContext(), values)
                                        .fileHandle();
                                  }))));
        });
  }

  @Override
  public UaMethodNode getReadMethodNode() throws UaException {
    return ClientNodeSupport.await(getReadMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getReadMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Read",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public @Nullable ByteString read(@Nullable UInteger fileHandle, @Nullable Integer length)
      throws UaException {
    return ClientNodeSupport.await(readAsync(fileHandle, length));
  }

  @Override
  public MethodCallResult<@Nullable ByteString> callRead(
      @Nullable UInteger fileHandle, @Nullable Integer length) throws UaException {
    return ClientNodeSupport.await(callReadAsync(fileHandle, length));
  }

  @Override
  public MethodCallResult<@Nullable ByteString> callReadWith(
      MethodCallOptions options, @Nullable UInteger fileHandle, @Nullable Integer length)
      throws UaException {
    return ClientNodeSupport.await(callReadWithAsync(options, fileHandle, length));
  }

  @Override
  public CompletableFuture<@Nullable ByteString> readAsync(
      @Nullable UInteger fileHandle, @Nullable Integer length) {
    return ClientNodeSupport.compose(
        callReadAsync(fileHandle, length),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable ByteString>> callReadAsync(
      @Nullable UInteger fileHandle, @Nullable Integer length) {
    return callReadWithAsync(MethodCallOptions.DEFAULT, fileHandle, length);
  }

  @Override
  public CompletableFuture<MethodCallResult<@Nullable ByteString>> callReadWithAsync(
      MethodCallOptions options, @Nullable UInteger fileHandle, @Nullable Integer length) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new FileTypeRead.Inputs(fileHandle, length)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getReadMethodNodeAsync(),
              node ->
                  ClientNodeSupport.compose(
                      ClientNodeSupport.call(client, this, node, suppliedInputs, options),
                      result ->
                          CompletableFuture.completedFuture(
                              result.map(
                                  values -> {
                                    return FileTypeRead.Outputs.fromVariants(
                                            client.getStaticEncodingContext(), values)
                                        .data();
                                  }))));
        });
  }

  @Override
  public UaMethodNode getSetPositionMethodNode() throws UaException {
    return ClientNodeSupport.await(getSetPositionMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getSetPositionMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "SetPosition",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void setPosition(@Nullable UInteger fileHandle, @Nullable ULong position)
      throws UaException {
    ClientNodeSupport.await(setPositionAsync(fileHandle, position));
  }

  @Override
  public MethodCallResult<Void> callSetPosition(
      @Nullable UInteger fileHandle, @Nullable ULong position) throws UaException {
    return ClientNodeSupport.await(callSetPositionAsync(fileHandle, position));
  }

  @Override
  public MethodCallResult<Void> callSetPositionWith(
      MethodCallOptions options, @Nullable UInteger fileHandle, @Nullable ULong position)
      throws UaException {
    return ClientNodeSupport.await(callSetPositionWithAsync(options, fileHandle, position));
  }

  @Override
  public CompletableFuture<Void> setPositionAsync(
      @Nullable UInteger fileHandle, @Nullable ULong position) {
    return ClientNodeSupport.compose(
        callSetPositionAsync(fileHandle, position),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callSetPositionAsync(
      @Nullable UInteger fileHandle, @Nullable ULong position) {
    return callSetPositionWithAsync(MethodCallOptions.DEFAULT, fileHandle, position);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callSetPositionWithAsync(
      MethodCallOptions options, @Nullable UInteger fileHandle, @Nullable ULong position) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new FileTypeSetPosition.Inputs(fileHandle, position)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getSetPositionMethodNodeAsync(),
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
  public UaMethodNode getWriteMethodNode() throws UaException {
    return ClientNodeSupport.await(getWriteMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getWriteMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "Write",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void write(@Nullable UInteger fileHandle, @Nullable ByteString data) throws UaException {
    ClientNodeSupport.await(writeAsync(fileHandle, data));
  }

  @Override
  public MethodCallResult<Void> callWrite(@Nullable UInteger fileHandle, @Nullable ByteString data)
      throws UaException {
    return ClientNodeSupport.await(callWriteAsync(fileHandle, data));
  }

  @Override
  public MethodCallResult<Void> callWriteWith(
      MethodCallOptions options, @Nullable UInteger fileHandle, @Nullable ByteString data)
      throws UaException {
    return ClientNodeSupport.await(callWriteWithAsync(options, fileHandle, data));
  }

  @Override
  public CompletableFuture<Void> writeAsync(
      @Nullable UInteger fileHandle, @Nullable ByteString data) {
    return ClientNodeSupport.compose(
        callWriteAsync(fileHandle, data),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callWriteAsync(
      @Nullable UInteger fileHandle, @Nullable ByteString data) {
    return callWriteWithAsync(MethodCallOptions.DEFAULT, fileHandle, data);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callWriteWithAsync(
      MethodCallOptions options, @Nullable UInteger fileHandle, @Nullable ByteString data) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new FileTypeWrite.Inputs(fileHandle, data)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getWriteMethodNodeAsync(),
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
