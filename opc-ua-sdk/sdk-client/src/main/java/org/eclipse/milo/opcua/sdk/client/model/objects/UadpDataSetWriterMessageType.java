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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface UadpDataSetWriterMessageType extends DataSetWriterMessageType {
  QualifiedProperty<UadpDataSetMessageContentMask> DATA_SET_MESSAGE_CONTENT_MASK =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetMessageContentMask",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15646"),
          -1,
          UadpDataSetMessageContentMask.class);

  QualifiedProperty<UShort> CONFIGURED_SIZE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConfiguredSize",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UShort> NETWORK_MESSAGE_NUMBER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NetworkMessageNumber",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  QualifiedProperty<UShort> DATA_SET_OFFSET =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetOffset",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=5"),
          -1,
          UShort.class);

  /** Gets the existing node's local value. */
  @Nullable UadpDataSetMessageContentMask getDataSetMessageContentMask() throws UaException;

  /** Sets the existing node's local value. */
  void setDataSetMessageContentMask(@Nullable UadpDataSetMessageContentMask value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UadpDataSetMessageContentMask readDataSetMessageContentMask() throws UaException;

  /** Writes the value remotely. */
  void writeDataSetMessageContentMask(@Nullable UadpDataSetMessageContentMask value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UadpDataSetMessageContentMask>
      readDataSetMessageContentMaskAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDataSetMessageContentMaskAsync(
      @Nullable UadpDataSetMessageContentMask value);

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

  /** Gets the existing node's local value. */
  @Nullable UShort getConfiguredSize() throws UaException;

  /** Sets the existing node's local value. */
  void setConfiguredSize(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readConfiguredSize() throws UaException;

  /** Writes the value remotely. */
  void writeConfiguredSize(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readConfiguredSizeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeConfiguredSizeAsync(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getConfiguredSizeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getConfiguredSizeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UShort getNetworkMessageNumber() throws UaException;

  /** Sets the existing node's local value. */
  void setNetworkMessageNumber(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readNetworkMessageNumber() throws UaException;

  /** Writes the value remotely. */
  void writeNetworkMessageNumber(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readNetworkMessageNumberAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeNetworkMessageNumberAsync(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getNetworkMessageNumberNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getNetworkMessageNumberNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UShort getDataSetOffset() throws UaException;

  /** Sets the existing node's local value. */
  void setDataSetOffset(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UShort readDataSetOffset() throws UaException;

  /** Writes the value remotely. */
  void writeDataSetOffset(@Nullable UShort value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UShort> readDataSetOffsetAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDataSetOffsetAsync(@Nullable UShort value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDataSetOffsetNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getDataSetOffsetNodeAsync();
}
