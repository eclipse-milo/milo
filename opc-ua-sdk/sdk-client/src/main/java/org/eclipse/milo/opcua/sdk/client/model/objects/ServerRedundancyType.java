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
import org.eclipse.milo.opcua.stack.core.types.enumerated.RedundancySupport;
import org.eclipse.milo.opcua.stack.core.types.structured.RedundantServerDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.7">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.7</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ServerRedundancyType extends BaseObjectType {
  QualifiedProperty<RedundancySupport> REDUNDANCY_SUPPORT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "RedundancySupport",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=851"),
          -1,
          RedundancySupport.class);

  QualifiedProperty<RedundantServerDataType[]> REDUNDANT_SERVER_ARRAY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "RedundantServerArray",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=853"),
          1,
          RedundantServerDataType[].class);

  /** Gets the existing node's local value. */
  @Nullable RedundancySupport getRedundancySupport() throws UaException;

  /** Sets the existing node's local value. */
  void setRedundancySupport(@Nullable RedundancySupport value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable RedundancySupport readRedundancySupport() throws UaException;

  /** Writes the value remotely. */
  void writeRedundancySupport(@Nullable RedundancySupport value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable RedundancySupport> readRedundancySupportAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeRedundancySupportAsync(@Nullable RedundancySupport value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getRedundancySupportNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getRedundancySupportNodeAsync();

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
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getRedundantServerArrayNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getRedundantServerArrayNodeAsync();
}
