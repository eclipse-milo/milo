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
import org.eclipse.milo.opcua.stack.core.types.structured.JsonNetworkMessageContentMask;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.2/#9.2.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.2/#9.2.2.1</a>
 */
public interface JsonWriterGroupMessageType extends WriterGroupMessageType {
  QualifiedProperty<JsonNetworkMessageContentMask> NETWORK_MESSAGE_CONTENT_MASK =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NetworkMessageContentMask",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15654"),
          -1,
          JsonNetworkMessageContentMask.class);

  /**
   * Get the local value of the NetworkMessageContentMask Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the NetworkMessageContentMask Node.
   * @throws UaException if an error occurs creating or getting the NetworkMessageContentMask Node.
   */
  JsonNetworkMessageContentMask getNetworkMessageContentMask() throws UaException;

  /**
   * Set the local value of the NetworkMessageContentMask Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the NetworkMessageContentMask Node.
   * @throws UaException if an error occurs creating or getting the NetworkMessageContentMask Node.
   */
  void setNetworkMessageContentMask(JsonNetworkMessageContentMask value) throws UaException;

  /**
   * Read the value of the NetworkMessageContentMask Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link JsonNetworkMessageContentMask} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  JsonNetworkMessageContentMask readNetworkMessageContentMask() throws UaException;

  /**
   * Write a new value for the NetworkMessageContentMask Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link JsonNetworkMessageContentMask} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeNetworkMessageContentMask(JsonNetworkMessageContentMask value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readNetworkMessageContentMask}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends JsonNetworkMessageContentMask> readNetworkMessageContentMaskAsync();

  /**
   * An asynchronous implementation of {@link #writeNetworkMessageContentMask}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeNetworkMessageContentMaskAsync(
      JsonNetworkMessageContentMask value);

  /**
   * Get the NetworkMessageContentMask {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the NetworkMessageContentMask {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getNetworkMessageContentMaskNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getNetworkMessageContentMaskNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getNetworkMessageContentMaskNodeAsync();
}
