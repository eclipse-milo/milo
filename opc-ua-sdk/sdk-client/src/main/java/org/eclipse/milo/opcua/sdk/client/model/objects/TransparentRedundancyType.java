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
import org.eclipse.milo.opcua.stack.core.types.structured.RedundantServerDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.8">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.8</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface TransparentRedundancyType extends ServerRedundancyType {
  QualifiedProperty<RedundantServerDataType[]> REDUNDANT_SERVER_ARRAY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "RedundantServerArray",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=853"),
          1,
          RedundantServerDataType[].class);

  QualifiedProperty<String> CURRENT_SERVER_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CurrentServerId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /** Gets the existing node's local value. */
  @Nullable RedundantServerDataType @Nullable [] getRedundantServerArray() throws UaException;

  /** Sets the existing node's local value. */
  void setRedundantServerArray(@Nullable RedundantServerDataType @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable RedundantServerDataType @Nullable [] readRedundantServerArray() throws UaException;

  /** Writes the value remotely. */
  void writeRedundantServerArray(@Nullable RedundantServerDataType @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable RedundantServerDataType @Nullable []>
      readRedundantServerArrayAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRedundantServerArrayAsync(
      @Nullable RedundantServerDataType @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getRedundantServerArrayNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getRedundantServerArrayNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getCurrentServerId() throws UaException;

  /** Sets the existing node's local value. */
  void setCurrentServerId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readCurrentServerId() throws UaException;

  /** Writes the value remotely. */
  void writeCurrentServerId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readCurrentServerIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCurrentServerIdAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getCurrentServerIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getCurrentServerIdNodeAsync();
}
