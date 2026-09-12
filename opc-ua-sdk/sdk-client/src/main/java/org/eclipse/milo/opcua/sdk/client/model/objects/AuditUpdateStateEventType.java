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
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.17">https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.17</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AuditUpdateStateEventType extends AuditUpdateMethodEventType {
  QualifiedProperty<Object> OLD_STATE_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OldStateId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          -1,
          Object.class);

  QualifiedProperty<Object> NEW_STATE_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NewStateId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=24"),
          -1,
          Object.class);

  /** Gets the existing node's local value. */
  @Nullable Object getOldStateId() throws UaException;

  /** Sets the existing node's local value. */
  void setOldStateId(@Nullable Object value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Object readOldStateId() throws UaException;

  /** Writes the value remotely. */
  void writeOldStateId(@Nullable Object value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Object> readOldStateIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeOldStateIdAsync(@Nullable Object value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getOldStateIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getOldStateIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Object getNewStateId() throws UaException;

  /** Sets the existing node's local value. */
  void setNewStateId(@Nullable Object value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Object readNewStateId() throws UaException;

  /** Writes the value remotely. */
  void writeNewStateId(@Nullable Object value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Object> readNewStateIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeNewStateIdAsync(@Nullable Object value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getNewStateIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getNewStateIdNodeAsync();
}
