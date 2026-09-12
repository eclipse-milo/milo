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
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonNetworkMessageContentMask;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.2/#9.2.2.3">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.2/#9.2.2.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface JsonDataSetReaderMessageType extends DataSetReaderMessageType {
  QualifiedProperty<JsonNetworkMessageContentMask> NETWORK_MESSAGE_CONTENT_MASK =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NetworkMessageContentMask",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15654"),
          -1,
          JsonNetworkMessageContentMask.class);

  QualifiedProperty<JsonDataSetMessageContentMask> DATA_SET_MESSAGE_CONTENT_MASK =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetMessageContentMask",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15658"),
          -1,
          JsonDataSetMessageContentMask.class);

  /** Gets the existing node's local value. */
  @Nullable JsonNetworkMessageContentMask getNetworkMessageContentMask() throws UaException;

  /** Sets the existing node's local value. */
  void setNetworkMessageContentMask(@Nullable JsonNetworkMessageContentMask value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable JsonNetworkMessageContentMask readNetworkMessageContentMask() throws UaException;

  /** Writes the value remotely. */
  void writeNetworkMessageContentMask(@Nullable JsonNetworkMessageContentMask value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable JsonNetworkMessageContentMask>
      readNetworkMessageContentMaskAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeNetworkMessageContentMaskAsync(
      @Nullable JsonNetworkMessageContentMask value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getNetworkMessageContentMaskNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getNetworkMessageContentMaskNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable JsonDataSetMessageContentMask getDataSetMessageContentMask() throws UaException;

  /** Sets the existing node's local value. */
  void setDataSetMessageContentMask(@Nullable JsonDataSetMessageContentMask value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable JsonDataSetMessageContentMask readDataSetMessageContentMask() throws UaException;

  /** Writes the value remotely. */
  void writeDataSetMessageContentMask(@Nullable JsonDataSetMessageContentMask value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable JsonDataSetMessageContentMask>
      readDataSetMessageContentMaskAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDataSetMessageContentMaskAsync(
      @Nullable JsonDataSetMessageContentMask value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDataSetMessageContentMaskNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getDataSetMessageContentMaskNodeAsync();
}
