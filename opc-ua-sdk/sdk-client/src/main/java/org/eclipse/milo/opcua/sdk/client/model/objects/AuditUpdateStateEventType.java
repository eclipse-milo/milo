/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.17">https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.17</a>
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

  /**
   * Get the local value of the OldStateId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the OldStateId Node.
   * @throws UaException if an error occurs creating or getting the OldStateId Node.
   */
  Object getOldStateId() throws UaException;

  /**
   * Set the local value of the OldStateId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the OldStateId Node.
   * @throws UaException if an error occurs creating or getting the OldStateId Node.
   */
  void setOldStateId(Object value) throws UaException;

  /**
   * Read the value of the OldStateId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Object} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Object readOldStateId() throws UaException;

  /**
   * Write a new value for the OldStateId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Object} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeOldStateId(Object value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readOldStateId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<?> readOldStateIdAsync();

  /**
   * An asynchronous implementation of {@link #writeOldStateId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeOldStateIdAsync(Object value);

  /**
   * Get the OldStateId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the OldStateId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getOldStateIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getOldStateIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getOldStateIdNodeAsync();

  /**
   * Get the local value of the NewStateId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the NewStateId Node.
   * @throws UaException if an error occurs creating or getting the NewStateId Node.
   */
  Object getNewStateId() throws UaException;

  /**
   * Set the local value of the NewStateId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the NewStateId Node.
   * @throws UaException if an error occurs creating or getting the NewStateId Node.
   */
  void setNewStateId(Object value) throws UaException;

  /**
   * Read the value of the NewStateId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Object} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Object readNewStateId() throws UaException;

  /**
   * Write a new value for the NewStateId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Object} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeNewStateId(Object value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readNewStateId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<?> readNewStateIdAsync();

  /**
   * An asynchronous implementation of {@link #writeNewStateId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeNewStateIdAsync(Object value);

  /**
   * Get the NewStateId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the NewStateId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getNewStateIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getNewStateIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getNewStateIdNodeAsync();
}
