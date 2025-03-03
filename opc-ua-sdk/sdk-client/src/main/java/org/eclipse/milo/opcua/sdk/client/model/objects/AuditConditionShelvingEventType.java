/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.10.8">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.10.8</a>
 */
public interface AuditConditionShelvingEventType extends AuditConditionEventType {
  QualifiedProperty<Double> SHELVING_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ShelvingTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  /**
   * Get the local value of the ShelvingTime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ShelvingTime Node.
   * @throws UaException if an error occurs creating or getting the ShelvingTime Node.
   */
  Double getShelvingTime() throws UaException;

  /**
   * Set the local value of the ShelvingTime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ShelvingTime Node.
   * @throws UaException if an error occurs creating or getting the ShelvingTime Node.
   */
  void setShelvingTime(Double value) throws UaException;

  /**
   * Read the value of the ShelvingTime Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readShelvingTime() throws UaException;

  /**
   * Write a new value for the ShelvingTime Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeShelvingTime(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readShelvingTime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readShelvingTimeAsync();

  /**
   * An asynchronous implementation of {@link #writeShelvingTime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeShelvingTimeAsync(Double value);

  /**
   * Get the ShelvingTime {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ShelvingTime {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getShelvingTimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getShelvingTimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getShelvingTimeNodeAsync();
}
