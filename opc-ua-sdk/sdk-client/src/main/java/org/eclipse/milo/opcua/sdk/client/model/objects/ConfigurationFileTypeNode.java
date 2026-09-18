package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.ClientNodeSupport;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.ConfigurationFileTypeCloseAndUpdate;
import org.eclipse.milo.opcua.sdk.core.model.methods.ConfigurationFileTypeConfirmUpdate;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
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
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationUpdateTargetType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Node implementation of {@link ConfigurationFileType}.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.1">Model
 *     documentation</a>
 */
public class ConfigurationFileTypeNode extends FileTypeNode implements ConfigurationFileType {
  public ConfigurationFileTypeNode(
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
  public PropertyTypeNode getCurrentVersionNode() throws UaException {
    return ClientNodeSupport.await(getCurrentVersionNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getCurrentVersionNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "CurrentVersion",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable UInteger readCurrentVersion() throws UaException {
    return ClientNodeSupport.await(readCurrentVersionAsync());
  }

  @Override
  public void writeCurrentVersion(@Nullable UInteger value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeCurrentVersionAsync(value)),
        "http://opcfoundation.org/UA/}CurrentVersion");
  }

  @Override
  public CompletableFuture<? extends @Nullable UInteger> readCurrentVersionAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getCurrentVersionNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}CurrentVersion",
                            true,
                            UInteger.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable UInteger) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeCurrentVersionAsync(@Nullable UInteger value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getCurrentVersionNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}CurrentVersion",
                        value,
                        UInteger.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getLastUpdateTimeNode() throws UaException {
    return ClientNodeSupport.await(getLastUpdateTimeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getLastUpdateTimeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "LastUpdateTime",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable DateTime readLastUpdateTime() throws UaException {
    return ClientNodeSupport.await(readLastUpdateTimeAsync());
  }

  @Override
  public void writeLastUpdateTime(@Nullable DateTime value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeLastUpdateTimeAsync(value)),
        "http://opcfoundation.org/UA/}LastUpdateTime");
  }

  @Override
  public CompletableFuture<? extends @Nullable DateTime> readLastUpdateTimeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getLastUpdateTimeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}LastUpdateTime",
                            true,
                            DateTime.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable DateTime) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeLastUpdateTimeAsync(@Nullable DateTime value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getLastUpdateTimeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}LastUpdateTime",
                        value,
                        DateTime.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getActivityTimeoutNode() throws UaException {
    return ClientNodeSupport.await(getActivityTimeoutNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getActivityTimeoutNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "ActivityTimeout",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable Double readActivityTimeout() throws UaException {
    return ClientNodeSupport.await(readActivityTimeoutAsync());
  }

  @Override
  public void writeActivityTimeout(@Nullable Double value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeActivityTimeoutAsync(value)),
        "http://opcfoundation.org/UA/}ActivityTimeout");
  }

  @Override
  public CompletableFuture<? extends @Nullable Double> readActivityTimeoutAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getActivityTimeoutNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}ActivityTimeout",
                            true,
                            Double.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable Double) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeActivityTimeoutAsync(@Nullable Double value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getActivityTimeoutNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}ActivityTimeout",
                        value,
                        Double.class,
                        -1,
                        null)));
  }

  @Override
  public PropertyTypeNode getSupportedDataTypeNode() throws UaException {
    return ClientNodeSupport.await(getSupportedDataTypeNodeAsync());
  }

  @Override
  public CompletableFuture<? extends PropertyTypeNode> getSupportedDataTypeNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                CompletableFuture.completedFuture(this),
                parent ->
                    ClientNodeSupport.mandatoryChild(
                        client,
                        parent,
                        Namespaces.OPC_UA,
                        "SupportedDataType",
                        ExpandedNodeId.of(Namespaces.OPC_UA, 46L),
                        NodeClass.Variable,
                        PropertyTypeNode.class)));
  }

  @Override
  public @Nullable NodeId readSupportedDataType() throws UaException {
    return ClientNodeSupport.await(readSupportedDataTypeAsync());
  }

  @Override
  public void writeSupportedDataType(@Nullable NodeId value) throws UaException {
    ClientNodeSupport.good(
        ClientNodeSupport.await(writeSupportedDataTypeAsync(value)),
        "http://opcfoundation.org/UA/}SupportedDataType");
  }

  @Override
  public CompletableFuture<? extends @Nullable NodeId> readSupportedDataTypeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                ClientNodeSupport.compose(
                    getSupportedDataTypeNodeAsync(),
                    n ->
                        ClientNodeSupport.read(
                            client,
                            n,
                            this,
                            "http://opcfoundation.org/UA/}SupportedDataType",
                            true,
                            NodeId.class,
                            -1,
                            null)),
                v -> CompletableFuture.completedFuture((@Nullable NodeId) v)));
  }

  @Override
  public CompletableFuture<StatusCode> writeSupportedDataTypeAsync(@Nullable NodeId value) {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.compose(
                getSupportedDataTypeNodeAsync(),
                n ->
                    ClientNodeSupport.write(
                        client,
                        n,
                        this,
                        "http://opcfoundation.org/UA/}SupportedDataType",
                        value,
                        NodeId.class,
                        -1,
                        null)));
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
  public ConfigurationFileTypeCloseAndUpdate.Outputs closeAndUpdate(
      @Nullable UInteger fileHandle,
      @Nullable UInteger versionToUpdate,
      @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
      @Nullable Double revertAfterTime,
      @Nullable Double restartDelayTime)
      throws UaException {
    return ClientNodeSupport.await(
        closeAndUpdateAsync(
            fileHandle, versionToUpdate, targets, revertAfterTime, restartDelayTime));
  }

  @Override
  public MethodCallResult<ConfigurationFileTypeCloseAndUpdate.Outputs> callCloseAndUpdate(
      @Nullable UInteger fileHandle,
      @Nullable UInteger versionToUpdate,
      @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
      @Nullable Double revertAfterTime,
      @Nullable Double restartDelayTime)
      throws UaException {
    return ClientNodeSupport.await(
        callCloseAndUpdateAsync(
            fileHandle, versionToUpdate, targets, revertAfterTime, restartDelayTime));
  }

  @Override
  public MethodCallResult<ConfigurationFileTypeCloseAndUpdate.Outputs> callCloseAndUpdateWith(
      MethodCallOptions options,
      @Nullable UInteger fileHandle,
      @Nullable UInteger versionToUpdate,
      @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
      @Nullable Double revertAfterTime,
      @Nullable Double restartDelayTime)
      throws UaException {
    return ClientNodeSupport.await(
        callCloseAndUpdateWithAsync(
            options, fileHandle, versionToUpdate, targets, revertAfterTime, restartDelayTime));
  }

  @Override
  public CompletableFuture<ConfigurationFileTypeCloseAndUpdate.Outputs> closeAndUpdateAsync(
      @Nullable UInteger fileHandle,
      @Nullable UInteger versionToUpdate,
      @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
      @Nullable Double revertAfterTime,
      @Nullable Double restartDelayTime) {
    return ClientNodeSupport.compose(
        callCloseAndUpdateAsync(
            fileHandle, versionToUpdate, targets, revertAfterTime, restartDelayTime),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<ConfigurationFileTypeCloseAndUpdate.Outputs>>
      callCloseAndUpdateAsync(
          @Nullable UInteger fileHandle,
          @Nullable UInteger versionToUpdate,
          @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
          @Nullable Double revertAfterTime,
          @Nullable Double restartDelayTime) {
    return callCloseAndUpdateWithAsync(
        MethodCallOptions.DEFAULT,
        fileHandle,
        versionToUpdate,
        targets,
        revertAfterTime,
        restartDelayTime);
  }

  @Override
  public CompletableFuture<MethodCallResult<ConfigurationFileTypeCloseAndUpdate.Outputs>>
      callCloseAndUpdateWithAsync(
          MethodCallOptions options,
          @Nullable UInteger fileHandle,
          @Nullable UInteger versionToUpdate,
          @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
          @Nullable Double revertAfterTime,
          @Nullable Double restartDelayTime) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new ConfigurationFileTypeCloseAndUpdate.Inputs(
                      fileHandle, versionToUpdate, targets, revertAfterTime, restartDelayTime)
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
                                    return ConfigurationFileTypeCloseAndUpdate.Outputs.fromVariants(
                                        client.getStaticEncodingContext(), values);
                                  }))));
        });
  }

  @Override
  public UaMethodNode getConfirmUpdateMethodNode() throws UaException {
    return ClientNodeSupport.await(getConfirmUpdateMethodNodeAsync());
  }

  @Override
  public CompletableFuture<UaMethodNode> getConfirmUpdateMethodNodeAsync() {
    return ClientNodeSupport.defer(
        () ->
            ClientNodeSupport.mandatoryChild(
                client,
                this,
                "http://opcfoundation.org/UA/",
                "ConfirmUpdate",
                ExpandedNodeId.of(Namespaces.OPC_UA, 47L),
                NodeClass.Method,
                UaMethodNode.class));
  }

  @Override
  public void confirmUpdate(@Nullable UUID updateId) throws UaException {
    ClientNodeSupport.await(confirmUpdateAsync(updateId));
  }

  @Override
  public MethodCallResult<Void> callConfirmUpdate(@Nullable UUID updateId) throws UaException {
    return ClientNodeSupport.await(callConfirmUpdateAsync(updateId));
  }

  @Override
  public MethodCallResult<Void> callConfirmUpdateWith(
      MethodCallOptions options, @Nullable UUID updateId) throws UaException {
    return ClientNodeSupport.await(callConfirmUpdateWithAsync(options, updateId));
  }

  @Override
  public CompletableFuture<Void> confirmUpdateAsync(@Nullable UUID updateId) {
    return ClientNodeSupport.compose(
        callConfirmUpdateAsync(updateId),
        result ->
            ClientNodeSupport.defer(() -> CompletableFuture.completedFuture(result.requireGood())));
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callConfirmUpdateAsync(@Nullable UUID updateId) {
    return callConfirmUpdateWithAsync(MethodCallOptions.DEFAULT, updateId);
  }

  @Override
  public CompletableFuture<MethodCallResult<Void>> callConfirmUpdateWithAsync(
      MethodCallOptions options, @Nullable UUID updateId) {
    return ClientNodeSupport.defer(
        () -> {
          Variant[] inputs =
              new ConfigurationFileTypeConfirmUpdate.Inputs(updateId)
                  .toVariants(client.getStaticEncodingContext());
          Variant[] suppliedInputs = inputs;
          return ClientNodeSupport.compose(
              getConfirmUpdateMethodNodeAsync(),
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
