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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.4">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.4</a>
 */
public interface AuditSecurityEventType extends AuditEventType {
  QualifiedProperty<StatusCode> STATUS_CODE_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "StatusCodeId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19"),
          -1,
          StatusCode.class);

  /**
   * Get the local value of the StatusCodeId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the StatusCodeId Node.
   * @throws UaException if an error occurs creating or getting the StatusCodeId Node.
   */
  StatusCode getStatusCodeId() throws UaException;

  /**
   * Set the local value of the StatusCodeId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the StatusCodeId Node.
   * @throws UaException if an error occurs creating or getting the StatusCodeId Node.
   */
  void setStatusCodeId(StatusCode value) throws UaException;

  /**
   * Read the value of the StatusCodeId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link StatusCode} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  StatusCode readStatusCodeId() throws UaException;

  /**
   * Write a new value for the StatusCodeId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link StatusCode} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeStatusCodeId(StatusCode value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readStatusCodeId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends StatusCode> readStatusCodeIdAsync();

  /**
   * An asynchronous implementation of {@link #writeStatusCodeId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeStatusCodeIdAsync(StatusCode value);

  /**
   * Get the StatusCodeId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the StatusCodeId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getStatusCodeIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getStatusCodeIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getStatusCodeIdNodeAsync();
}
