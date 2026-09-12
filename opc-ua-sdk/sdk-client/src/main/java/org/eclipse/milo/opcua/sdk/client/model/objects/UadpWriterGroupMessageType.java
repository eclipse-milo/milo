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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DataSetOrderingType;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface UadpWriterGroupMessageType extends WriterGroupMessageType {
  QualifiedProperty<UInteger> GROUP_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "GroupVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998"),
          -1,
          UInteger.class);

  QualifiedProperty<DataSetOrderingType> DATA_SET_ORDERING =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DataSetOrdering",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20408"),
          -1,
          DataSetOrderingType.class);

  QualifiedProperty<UadpNetworkMessageContentMask> NETWORK_MESSAGE_CONTENT_MASK =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NetworkMessageContentMask",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15642"),
          -1,
          UadpNetworkMessageContentMask.class);

  QualifiedProperty<Double> SAMPLING_OFFSET =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SamplingOffset",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double[]> PUBLISHING_OFFSET =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PublishingOffset",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          1,
          Double[].class);

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
  @Nullable DataSetOrderingType getDataSetOrdering() throws UaException;

  /** Sets the existing node's local value. */
  void setDataSetOrdering(@Nullable DataSetOrderingType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DataSetOrderingType readDataSetOrdering() throws UaException;

  /** Writes the value remotely. */
  void writeDataSetOrdering(@Nullable DataSetOrderingType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DataSetOrderingType> readDataSetOrderingAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDataSetOrderingAsync(@Nullable DataSetOrderingType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDataSetOrderingNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getDataSetOrderingNodeAsync();

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
  @Nullable Double getSamplingOffset() throws UaException;

  /** Sets the existing node's local value. */
  void setSamplingOffset(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readSamplingOffset() throws UaException;

  /** Writes the value remotely. */
  void writeSamplingOffset(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readSamplingOffsetAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSamplingOffsetAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSamplingOffsetNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getSamplingOffsetNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double @Nullable [] getPublishingOffset() throws UaException;

  /** Sets the existing node's local value. */
  void setPublishingOffset(@Nullable Double @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double @Nullable [] readPublishingOffset() throws UaException;

  /** Writes the value remotely. */
  void writePublishingOffset(@Nullable Double @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double @Nullable []> readPublishingOffsetAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePublishingOffsetAsync(@Nullable Double @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPublishingOffsetNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getPublishingOffsetNodeAsync();
}
