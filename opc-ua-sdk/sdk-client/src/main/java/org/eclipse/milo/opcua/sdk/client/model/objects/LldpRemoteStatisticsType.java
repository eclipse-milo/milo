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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.4</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface LldpRemoteStatisticsType extends BaseObjectType {
  /** Gets the existing node's local value. */
  @Nullable UInteger getLastChangeTime() throws UaException;

  /** Sets the existing node's local value. */
  void setLastChangeTime(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readLastChangeTime() throws UaException;

  /** Writes the value remotely. */
  void writeLastChangeTime(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readLastChangeTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeLastChangeTimeAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getLastChangeTimeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getLastChangeTimeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getRemoteInserts() throws UaException;

  /** Sets the existing node's local value. */
  void setRemoteInserts(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readRemoteInserts() throws UaException;

  /** Writes the value remotely. */
  void writeRemoteInserts(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readRemoteInsertsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRemoteInsertsAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRemoteInsertsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRemoteInsertsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getRemoteDeletes() throws UaException;

  /** Sets the existing node's local value. */
  void setRemoteDeletes(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readRemoteDeletes() throws UaException;

  /** Writes the value remotely. */
  void writeRemoteDeletes(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readRemoteDeletesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRemoteDeletesAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRemoteDeletesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRemoteDeletesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getRemoteDrops() throws UaException;

  /** Sets the existing node's local value. */
  void setRemoteDrops(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readRemoteDrops() throws UaException;

  /** Writes the value remotely. */
  void writeRemoteDrops(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readRemoteDropsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRemoteDropsAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRemoteDropsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRemoteDropsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getRemoteAgeouts() throws UaException;

  /** Sets the existing node's local value. */
  void setRemoteAgeouts(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readRemoteAgeouts() throws UaException;

  /** Writes the value remotely. */
  void writeRemoteAgeouts(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readRemoteAgeoutsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRemoteAgeoutsAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getRemoteAgeoutsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends BaseDataVariableType> getRemoteAgeoutsNodeAsync();
}
