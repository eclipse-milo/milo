/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.objects;

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallOptions;
import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodCallResult;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.model.methods.ConfigurationFileTypeCloseAndUpdateOutputs;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ConfigurationUpdateTargetType;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.1">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ConfigurationFileType extends FileType {
  QualifiedProperty<DateTime> LAST_UPDATE_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "LastUpdateTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=294"),
          -1,
          DateTime.class);

  QualifiedProperty<UInteger> CURRENT_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CurrentVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998"),
          -1,
          UInteger.class);

  QualifiedProperty<Double> ACTIVITY_TIMEOUT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ActivityTimeout",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<NodeId> SUPPORTED_DATA_TYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SupportedDataType",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  /** Gets the existing node's local value. */
  @Nullable DateTime getLastUpdateTime() throws UaException;

  /** Sets the existing node's local value. */
  void setLastUpdateTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DateTime readLastUpdateTime() throws UaException;

  /** Writes the value remotely. */
  void writeLastUpdateTime(@Nullable DateTime value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DateTime> readLastUpdateTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastUpdateTimeAsync(@Nullable DateTime value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getLastUpdateTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getLastUpdateTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getCurrentVersion() throws UaException;

  /** Sets the existing node's local value. */
  void setCurrentVersion(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readCurrentVersion() throws UaException;

  /** Writes the value remotely. */
  void writeCurrentVersion(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readCurrentVersionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCurrentVersionAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getCurrentVersionNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getCurrentVersionNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getActivityTimeout() throws UaException;

  /** Sets the existing node's local value. */
  void setActivityTimeout(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readActivityTimeout() throws UaException;

  /** Writes the value remotely. */
  void writeActivityTimeout(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readActivityTimeoutAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeActivityTimeoutAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getActivityTimeoutNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getActivityTimeoutNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable NodeId getSupportedDataType() throws UaException;

  /** Sets the existing node's local value. */
  void setSupportedDataType(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable NodeId readSupportedDataType() throws UaException;

  /** Writes the value remotely. */
  void writeSupportedDataType(@Nullable NodeId value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable NodeId> readSupportedDataTypeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSupportedDataTypeAsync(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSupportedDataTypeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSupportedDataTypeNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getConfirmUpdateMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.3
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getConfirmUpdateMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.3
   *
   * <p>Invokes <code>ConfirmUpdate</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callConfirmUpdate(@Nullable UUID updateId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.3
   *
   * <p>Invokes <code>ConfirmUpdate</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callConfirmUpdateAsync(@Nullable UUID updateId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.3
   *
   * <p>Invokes <code>ConfirmUpdate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callConfirmUpdateDetailed(@Nullable UUID updateId)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.3
   *
   * <p>Invokes <code>ConfirmUpdate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callConfirmUpdateDetailed(
      MethodCallOptions options, @Nullable UUID updateId) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.3
   *
   * <p>Invokes <code>ConfirmUpdate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callConfirmUpdateDetailedAsync(@Nullable UUID updateId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.3
   *
   * <p>Invokes <code>ConfirmUpdate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>>
      callConfirmUpdateDetailedAsync(MethodCallOptions options, @Nullable UUID updateId);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.2
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getCloseAndUpdateMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.2
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getCloseAndUpdateMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.2
   *
   * <p>Invokes <code>CloseAndUpdate</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  ConfigurationFileTypeCloseAndUpdateOutputs callCloseAndUpdate(
      @Nullable UInteger fileHandle,
      @Nullable UInteger versionToUpdate,
      @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
      @Nullable Double revertAfterTime,
      @Nullable Double restartDelayTime)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.2
   *
   * <p>Invokes <code>CloseAndUpdate</code> on this node's ObjectId using the effective Method
   * contract. Requires Good operation status.
   *
   * @return the output value or object, or its future; a single value may be null.
   */
  @NullMarked
  CompletableFuture<? extends ConfigurationFileTypeCloseAndUpdateOutputs> callCloseAndUpdateAsync(
      @Nullable UInteger fileHandle,
      @Nullable UInteger versionToUpdate,
      @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
      @Nullable Double revertAfterTime,
      @Nullable Double restartDelayTime);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.2
   *
   * <p>Invokes <code>CloseAndUpdate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends ConfigurationFileTypeCloseAndUpdateOutputs> callCloseAndUpdateDetailed(
      @Nullable UInteger fileHandle,
      @Nullable UInteger versionToUpdate,
      @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
      @Nullable Double revertAfterTime,
      @Nullable Double restartDelayTime)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.2
   *
   * <p>Invokes <code>CloseAndUpdate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends ConfigurationFileTypeCloseAndUpdateOutputs> callCloseAndUpdateDetailed(
      MethodCallOptions options,
      @Nullable UInteger fileHandle,
      @Nullable UInteger versionToUpdate,
      @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
      @Nullable Double revertAfterTime,
      @Nullable Double restartDelayTime)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.2
   *
   * <p>Invokes <code>CloseAndUpdate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<
          ? extends MethodCallResult<? extends ConfigurationFileTypeCloseAndUpdateOutputs>>
      callCloseAndUpdateDetailedAsync(
          @Nullable UInteger fileHandle,
          @Nullable UInteger versionToUpdate,
          @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
          @Nullable Double revertAfterTime,
          @Nullable Double restartDelayTime);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.2
   *
   * <p>Invokes <code>CloseAndUpdate</code> on this node's ObjectId using the effective Method
   * contract. Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<
          ? extends MethodCallResult<? extends ConfigurationFileTypeCloseAndUpdateOutputs>>
      callCloseAndUpdateDetailedAsync(
          MethodCallOptions options,
          @Nullable UInteger fileHandle,
          @Nullable UInteger versionToUpdate,
          @Nullable ConfigurationUpdateTargetType @Nullable [] targets,
          @Nullable Double revertAfterTime,
          @Nullable Double restartDelayTime);
}
