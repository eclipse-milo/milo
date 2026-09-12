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

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.3">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface UadpDataSetReaderMessageType extends DataSetReaderMessageType {
  QualifiedProperty<UInteger> GROUP_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "GroupVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998"),
          -1,
          UInteger.class);

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

  QualifiedProperty<UUID> DATA_SET_CLASS_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetClassId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14"),
          -1,
          UUID.class);

  QualifiedProperty<UadpNetworkMessageContentMask> NETWORK_MESSAGE_CONTENT_MASK =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NetworkMessageContentMask",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15642"),
          -1,
          UadpNetworkMessageContentMask.class);

  QualifiedProperty<UadpDataSetMessageContentMask> DATA_SET_MESSAGE_CONTENT_MASK =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetMessageContentMask",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15646"),
          -1,
          UadpDataSetMessageContentMask.class);

  QualifiedProperty<Double> PUBLISHING_INTERVAL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PublishingInterval",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double> PROCESSING_OFFSET =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ProcessingOffset",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double> RECEIVE_OFFSET =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ReceiveOffset",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  /** Gets the existing node's local value. */
  @Nullable UInteger getGroupVersion() throws UaException;

  /** Sets the existing node's local value. */
  void setGroupVersion(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readGroupVersion() throws UaException;

  /** Writes the value remotely. */
  void writeGroupVersion(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readGroupVersionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeGroupVersionAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getGroupVersionNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getGroupVersionNodeAsync();

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

  /** Gets the existing node's local value. */
  @Nullable UUID getDataSetClassId() throws UaException;

  /** Sets the existing node's local value. */
  void setDataSetClassId(@Nullable UUID value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UUID readDataSetClassId() throws UaException;

  /** Writes the value remotely. */
  void writeDataSetClassId(@Nullable UUID value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UUID> readDataSetClassIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDataSetClassIdAsync(@Nullable UUID value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDataSetClassIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getDataSetClassIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UadpNetworkMessageContentMask getNetworkMessageContentMask() throws UaException;

  /** Sets the existing node's local value. */
  void setNetworkMessageContentMask(@Nullable UadpNetworkMessageContentMask value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UadpNetworkMessageContentMask readNetworkMessageContentMask() throws UaException;

  /** Writes the value remotely. */
  void writeNetworkMessageContentMask(@Nullable UadpNetworkMessageContentMask value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UadpNetworkMessageContentMask>
      readNetworkMessageContentMaskAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeNetworkMessageContentMaskAsync(
      @Nullable UadpNetworkMessageContentMask value);

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
  @Nullable Double getPublishingInterval() throws UaException;

  /** Sets the existing node's local value. */
  void setPublishingInterval(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readPublishingInterval() throws UaException;

  /** Writes the value remotely. */
  void writePublishingInterval(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readPublishingIntervalAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePublishingIntervalAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPublishingIntervalNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getPublishingIntervalNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getProcessingOffset() throws UaException;

  /** Sets the existing node's local value. */
  void setProcessingOffset(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readProcessingOffset() throws UaException;

  /** Writes the value remotely. */
  void writeProcessingOffset(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readProcessingOffsetAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeProcessingOffsetAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getProcessingOffsetNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getProcessingOffsetNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getReceiveOffset() throws UaException;

  /** Sets the existing node's local value. */
  void setReceiveOffset(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readReceiveOffset() throws UaException;

  /** Writes the value remotely. */
  void writeReceiveOffset(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readReceiveOffsetAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeReceiveOffsetAsync(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getReceiveOffsetNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getReceiveOffsetNodeAsync();
}
