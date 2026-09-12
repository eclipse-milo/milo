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
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.sdk.client.model.variables.PubSubDiagnosticsCounterType;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface PubSubDiagnosticsType extends BaseObjectType {
  /** Gets the existing node's local value. */
  @Nullable DiagnosticsLevel getDiagnosticsLevel() throws UaException;

  /** Sets the existing node's local value. */
  void setDiagnosticsLevel(@Nullable DiagnosticsLevel value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DiagnosticsLevel readDiagnosticsLevel() throws UaException;

  /** Writes the value remotely. */
  void writeDiagnosticsLevel(@Nullable DiagnosticsLevel value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DiagnosticsLevel> readDiagnosticsLevelAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDiagnosticsLevelAsync(@Nullable DiagnosticsLevel value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getDiagnosticsLevelNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getDiagnosticsLevelNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getTotalInformation() throws UaException;

  /** Sets the existing node's local value. */
  void setTotalInformation(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readTotalInformation() throws UaException;

  /** Writes the value remotely. */
  void writeTotalInformation(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readTotalInformationAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTotalInformationAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PubSubDiagnosticsCounterType getTotalInformationNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PubSubDiagnosticsCounterType> getTotalInformationNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getTotalError() throws UaException;

  /** Sets the existing node's local value. */
  void setTotalError(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readTotalError() throws UaException;

  /** Writes the value remotely. */
  void writeTotalError(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readTotalErrorAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTotalErrorAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PubSubDiagnosticsCounterType getTotalErrorNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PubSubDiagnosticsCounterType> getTotalErrorNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  @NullMarked
  UaMethodNode getResetMethodNode() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.3
   *
   * <p>Returns the required node.
   *
   * @return a future completing with the required node.
   */
  @NullMarked
  CompletableFuture<? extends UaMethodNode> getResetMethodNodeAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.3
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @throws UaException if lookup, input validation, transport, service, operation status or output
   *     conversion fails.
   */
  @NullMarked
  void callReset() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.3
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Requires Good operation status.
   *
   * @return a future whose successful payload is null.
   */
  @NullMarked
  CompletableFuture<? extends @Nullable Void> callResetAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.3
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callResetDetailed() throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.3
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws UaException if lookup, input validation, transport, service or response envelope
   *     validation fails.
   * @throws NullPointerException if a required options or presence object is null.
   */
  @NullMarked
  MethodCallResult<? extends @Nullable Void> callResetDetailed(MethodCallOptions options)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.3
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @return the detailed outcome, or its future.
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callResetDetailedAsync();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.3
   *
   * <p>Invokes <code>Reset</code> on this node's ObjectId using the effective Method contract.
   * Retains the operation status, diagnostics and outputs.
   *
   * @param options request-wide diagnostics options for this Call only.
   * @return the detailed outcome, or its future.
   * @throws NullPointerException if a required options or presence object is null (exceptional
   *     completion).
   */
  @NullMarked
  CompletableFuture<? extends MethodCallResult<? extends @Nullable Void>> callResetDetailedAsync(
      MethodCallOptions options);

  /** Gets the existing node's local value. */
  @Nullable Boolean getSubError() throws UaException;

  /** Sets the existing node's local value. */
  void setSubError(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readSubError() throws UaException;

  /** Writes the value remotely. */
  void writeSubError(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readSubErrorAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSubErrorAsync(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getSubErrorNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getSubErrorNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseObjectType getCountersNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseObjectType> getCountersNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseObjectType getLiveValuesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseObjectType> getLiveValuesNodeAsync();
}
