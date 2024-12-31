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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.10">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.10</a>
 */
public interface OrderedListType extends BaseObjectType {
  QualifiedProperty<String> NODE_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NodeVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /**
   * Get the local value of the NodeVersion Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the NodeVersion Node.
   * @throws UaException if an error occurs creating or getting the NodeVersion Node.
   */
  String getNodeVersion() throws UaException;

  /**
   * Set the local value of the NodeVersion Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the NodeVersion Node.
   * @throws UaException if an error occurs creating or getting the NodeVersion Node.
   */
  void setNodeVersion(String value) throws UaException;

  /**
   * Read the value of the NodeVersion Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readNodeVersion() throws UaException;

  /**
   * Write a new value for the NodeVersion Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeNodeVersion(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readNodeVersion}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readNodeVersionAsync();

  /**
   * An asynchronous implementation of {@link #writeNodeVersion}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeNodeVersionAsync(String value);

  /**
   * Get the NodeVersion {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the NodeVersion {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getNodeVersionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getNodeVersionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getNodeVersionNodeAsync();
}
