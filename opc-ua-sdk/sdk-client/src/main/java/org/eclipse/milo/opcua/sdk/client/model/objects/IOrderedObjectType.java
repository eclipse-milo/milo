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
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.11">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.11</a>
 */
public interface IOrderedObjectType extends BaseInterfaceType {
  QualifiedProperty<Variant> NUMBER_IN_LIST =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NumberInList",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=26"),
          -1,
          Variant.class);

  /**
   * Get the local value of the NumberInList Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the NumberInList Node.
   * @throws UaException if an error occurs creating or getting the NumberInList Node.
   */
  Variant getNumberInList() throws UaException;

  /**
   * Set the local value of the NumberInList Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the NumberInList Node.
   * @throws UaException if an error occurs creating or getting the NumberInList Node.
   */
  void setNumberInList(Variant value) throws UaException;

  /**
   * Read the value of the NumberInList Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Variant} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Variant readNumberInList() throws UaException;

  /**
   * Write a new value for the NumberInList Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Variant} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeNumberInList(Variant value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readNumberInList}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Variant> readNumberInListAsync();

  /**
   * An asynchronous implementation of {@link #writeNumberInList}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeNumberInListAsync(Variant value);

  /**
   * Get the NumberInList {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the NumberInList {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getNumberInListNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getNumberInListNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getNumberInListNodeAsync();
}
