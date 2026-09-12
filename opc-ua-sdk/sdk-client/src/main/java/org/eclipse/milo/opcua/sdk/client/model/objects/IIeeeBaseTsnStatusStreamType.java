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

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnFailureCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnListenerStatus;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TsnTalkerStatus;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.9">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.9</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface IIeeeBaseTsnStatusStreamType extends BaseInterfaceType {
  /** Gets the existing node's local value. */
  @Nullable TsnTalkerStatus getTalkerStatus() throws UaException;

  /** Sets the existing node's local value. */
  void setTalkerStatus(@Nullable TsnTalkerStatus value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable TsnTalkerStatus readTalkerStatus() throws UaException;

  /** Writes the value remotely. */
  void writeTalkerStatus(@Nullable TsnTalkerStatus value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable TsnTalkerStatus> readTalkerStatusAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTalkerStatusAsync(@Nullable TsnTalkerStatus value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getTalkerStatusNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getTalkerStatusNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable TsnListenerStatus getListenerStatus() throws UaException;

  /** Sets the existing node's local value. */
  void setListenerStatus(@Nullable TsnListenerStatus value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable TsnListenerStatus readListenerStatus() throws UaException;

  /** Writes the value remotely. */
  void writeListenerStatus(@Nullable TsnListenerStatus value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable TsnListenerStatus> readListenerStatusAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeListenerStatusAsync(@Nullable TsnListenerStatus value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getListenerStatusNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable BaseDataVariableType> getListenerStatusNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable TsnFailureCode getFailureCode() throws UaException;

  /** Sets the existing node's local value. */
  void setFailureCode(@Nullable TsnFailureCode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable TsnFailureCode readFailureCode() throws UaException;

  /** Writes the value remotely. */
  void writeFailureCode(@Nullable TsnFailureCode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable TsnFailureCode> readFailureCodeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeFailureCodeAsync(@Nullable TsnFailureCode value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getFailureCodeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getFailureCodeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Object getFailureSystemIdentifier() throws UaException;

  /** Sets the existing node's local value. */
  void setFailureSystemIdentifier(@Nullable Object value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Object readFailureSystemIdentifier() throws UaException;

  /** Writes the value remotely. */
  void writeFailureSystemIdentifier(@Nullable Object value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Object> readFailureSystemIdentifierAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeFailureSystemIdentifierAsync(@Nullable Object value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getFailureSystemIdentifierNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getFailureSystemIdentifierNodeAsync();
}
