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
import org.eclipse.milo.opcua.stack.core.types.enumerated.RedundantServerMode;
import org.eclipse.milo.opcua.stack.core.types.structured.RedundantServerDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.15">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.15</a>
 */
public interface NonTransparentBackupRedundancyType extends NonTransparentRedundancyType {
  QualifiedProperty<RedundantServerDataType[]> REDUNDANT_SERVER_ARRAY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "RedundantServerArray",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=853"),
          1,
          RedundantServerDataType[].class);

  QualifiedProperty<RedundantServerMode> MODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Mode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=32417"),
          -1,
          RedundantServerMode.class);

  /**
   * Get the local value of the RedundantServerArray Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RedundantServerArray Node.
   * @throws UaException if an error occurs creating or getting the RedundantServerArray Node.
   */
  RedundantServerDataType[] getRedundantServerArray() throws UaException;

  /**
   * Set the local value of the RedundantServerArray Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RedundantServerArray Node.
   * @throws UaException if an error occurs creating or getting the RedundantServerArray Node.
   */
  void setRedundantServerArray(RedundantServerDataType[] value) throws UaException;

  /**
   * Read the value of the RedundantServerArray Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link RedundantServerDataType[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  RedundantServerDataType[] readRedundantServerArray() throws UaException;

  /**
   * Write a new value for the RedundantServerArray Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link RedundantServerDataType[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRedundantServerArray(RedundantServerDataType[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRedundantServerArray}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends RedundantServerDataType[]> readRedundantServerArrayAsync();

  /**
   * An asynchronous implementation of {@link #writeRedundantServerArray}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRedundantServerArrayAsync(RedundantServerDataType[] value);

  /**
   * Get the RedundantServerArray {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RedundantServerArray {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getRedundantServerArrayNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRedundantServerArrayNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getRedundantServerArrayNodeAsync();

  /**
   * Get the local value of the Mode Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Mode Node.
   * @throws UaException if an error occurs creating or getting the Mode Node.
   */
  RedundantServerMode getMode() throws UaException;

  /**
   * Set the local value of the Mode Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Mode Node.
   * @throws UaException if an error occurs creating or getting the Mode Node.
   */
  void setMode(RedundantServerMode value) throws UaException;

  /**
   * Read the value of the Mode Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link RedundantServerMode} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  RedundantServerMode readMode() throws UaException;

  /**
   * Write a new value for the Mode Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link RedundantServerMode} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMode(RedundantServerMode value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMode}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends RedundantServerMode> readModeAsync();

  /**
   * An asynchronous implementation of {@link #writeMode}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeModeAsync(RedundantServerMode value);

  /**
   * Get the Mode {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Mode {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getModeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getModeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getModeNodeAsync();
}
