package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallOptions;
import org.eclipse.milo.opcua.sdk.client.methods.MethodCallResult;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.model.methods.ConfigurationFileTypeCloseAndUpdate;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationUpdateTargetType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ConfigurationFileType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.1">Model
 *     documentation</a>
 */
public interface ConfigurationFileType extends FileType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15437L);

  QualifiedProperty<UInteger> CurrentVersion_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "CurrentVersion",
          ExpandedNodeId.of(Namespaces.OPC_UA, 20998L),
          -1,
          UInteger.class);

  QualifiedProperty<DateTime> LastUpdateTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LastUpdateTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          -1,
          DateTime.class);

  QualifiedProperty<Double> ActivityTimeout_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ActivityTimeout",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<NodeId> SupportedDataType_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SupportedDataType",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  /**
   * Resolves the mandatory CurrentVersion child, a PropertyType with DataType VersionTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getCurrentVersionNode() throws UaException;

  /** Asynchronous form of {@link #getCurrentVersionNode()}. */
  CompletableFuture<? extends PropertyType> getCurrentVersionNodeAsync();

  /**
   * Reads the Value of the CurrentVersion child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readCurrentVersion() throws UaException;

  /**
   * Writes the Value of the CurrentVersion child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeCurrentVersion(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readCurrentVersion()}. */
  CompletableFuture<? extends @Nullable UInteger> readCurrentVersionAsync();

  /** Asynchronous form of {@link #writeCurrentVersion}; completes with the operation status. */
  CompletableFuture<StatusCode> writeCurrentVersionAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory LastUpdateTime child, a PropertyType with DataType UtcTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getLastUpdateTimeNode() throws UaException;

  /** Asynchronous form of {@link #getLastUpdateTimeNode()}. */
  CompletableFuture<? extends PropertyType> getLastUpdateTimeNodeAsync();

  /**
   * Reads the Value of the LastUpdateTime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readLastUpdateTime() throws UaException;

  /**
   * Writes the Value of the LastUpdateTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLastUpdateTime(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readLastUpdateTime()}. */
  CompletableFuture<? extends @Nullable DateTime> readLastUpdateTimeAsync();

  /** Asynchronous form of {@link #writeLastUpdateTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLastUpdateTimeAsync(@Nullable DateTime value);

  /**
   * Resolves the mandatory ActivityTimeout child, a PropertyType with DataType Duration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getActivityTimeoutNode() throws UaException;

  /** Asynchronous form of {@link #getActivityTimeoutNode()}. */
  CompletableFuture<? extends PropertyType> getActivityTimeoutNodeAsync();

  /**
   * Reads the Value of the ActivityTimeout child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readActivityTimeout() throws UaException;

  /**
   * Writes the Value of the ActivityTimeout child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeActivityTimeout(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readActivityTimeout()}. */
  CompletableFuture<? extends @Nullable Double> readActivityTimeoutAsync();

  /** Asynchronous form of {@link #writeActivityTimeout}; completes with the operation status. */
  CompletableFuture<StatusCode> writeActivityTimeoutAsync(@Nullable Double value);

  /**
   * Resolves the mandatory SupportedDataType child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSupportedDataTypeNode() throws UaException;

  /** Asynchronous form of {@link #getSupportedDataTypeNode()}. */
  CompletableFuture<? extends PropertyType> getSupportedDataTypeNodeAsync();

  /**
   * Reads the Value of the SupportedDataType child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readSupportedDataType() throws UaException;

  /**
   * Writes the Value of the SupportedDataType child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSupportedDataType(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readSupportedDataType()}. */
  CompletableFuture<? extends @Nullable NodeId> readSupportedDataTypeAsync();

  /** Asynchronous form of {@link #writeSupportedDataType}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSupportedDataTypeAsync(@Nullable NodeId value);

  /**
   * Resolves the mandatory CloseAndUpdate Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.2">Model
   *     documentation</a>
   */
  UaMethodNode getCloseAndUpdateMethodNode() throws UaException;

  /** Asynchronous form of {@link #getCloseAndUpdateMethodNode()}. */
  CompletableFuture<UaMethodNode> getCloseAndUpdateMethodNodeAsync();

  /**
   * Calls the CloseAndUpdate Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.2">Model
   *     documentation</a>
   */
  ConfigurationFileTypeCloseAndUpdate.Outputs closeAndUpdate(
      @Nullable UInteger fileHandle,
      @Nullable UInteger versionToUpdate,
      @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
      @Nullable Double revertAfterTime,
      @Nullable Double restartDelayTime)
      throws UaException;

  /**
   * Calls the CloseAndUpdate Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<ConfigurationFileTypeCloseAndUpdate.Outputs> callCloseAndUpdate(
      @Nullable UInteger fileHandle,
      @Nullable UInteger versionToUpdate,
      @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
      @Nullable Double revertAfterTime,
      @Nullable Double restartDelayTime)
      throws UaException;

  /**
   * Calls the CloseAndUpdate Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<ConfigurationFileTypeCloseAndUpdate.Outputs> callCloseAndUpdateWith(
      MethodCallOptions options,
      @Nullable UInteger fileHandle,
      @Nullable UInteger versionToUpdate,
      @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
      @Nullable Double revertAfterTime,
      @Nullable Double restartDelayTime)
      throws UaException;

  /** Asynchronous form of {@link #closeAndUpdate}. */
  CompletableFuture<ConfigurationFileTypeCloseAndUpdate.Outputs> closeAndUpdateAsync(
      @Nullable UInteger fileHandle,
      @Nullable UInteger versionToUpdate,
      @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
      @Nullable Double revertAfterTime,
      @Nullable Double restartDelayTime);

  /** Asynchronous form of {@link #callCloseAndUpdate}. */
  CompletableFuture<MethodCallResult<ConfigurationFileTypeCloseAndUpdate.Outputs>>
      callCloseAndUpdateAsync(
          @Nullable UInteger fileHandle,
          @Nullable UInteger versionToUpdate,
          @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
          @Nullable Double revertAfterTime,
          @Nullable Double restartDelayTime);

  /** Asynchronous form of {@link #callCloseAndUpdateWith}. */
  CompletableFuture<MethodCallResult<ConfigurationFileTypeCloseAndUpdate.Outputs>>
      callCloseAndUpdateWithAsync(
          MethodCallOptions options,
          @Nullable UInteger fileHandle,
          @Nullable UInteger versionToUpdate,
          @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
          @Nullable Double revertAfterTime,
          @Nullable Double restartDelayTime);

  /**
   * Resolves the mandatory ConfirmUpdate Method node.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.3">Model
   *     documentation</a>
   */
  UaMethodNode getConfirmUpdateMethodNode() throws UaException;

  /** Asynchronous form of {@link #getConfirmUpdateMethodNode()}. */
  CompletableFuture<UaMethodNode> getConfirmUpdateMethodNodeAsync();

  /**
   * Calls the ConfirmUpdate Method and returns its outputs; requires a Good result.
   *
   * @throws UaException if lookup, transport or conversion fails or the result is not Good.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.3">Model
   *     documentation</a>
   */
  void confirmUpdate(@Nullable UUID updateId) throws UaException;

  /**
   * Calls the ConfirmUpdate Method and returns the complete result, including a Bad status.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callConfirmUpdate(@Nullable UUID updateId) throws UaException;

  /**
   * Calls the ConfirmUpdate Method with explicit options and returns the complete result.
   *
   * @throws UaException if lookup, transport or conversion fails.
   */
  MethodCallResult<Void> callConfirmUpdateWith(MethodCallOptions options, @Nullable UUID updateId)
      throws UaException;

  /** Asynchronous form of {@link #confirmUpdate}. */
  CompletableFuture<Void> confirmUpdateAsync(@Nullable UUID updateId);

  /** Asynchronous form of {@link #callConfirmUpdate}. */
  CompletableFuture<MethodCallResult<Void>> callConfirmUpdateAsync(@Nullable UUID updateId);

  /** Asynchronous form of {@link #callConfirmUpdateWith}. */
  CompletableFuture<MethodCallResult<Void>> callConfirmUpdateWithAsync(
      MethodCallOptions options, @Nullable UUID updateId);
}
