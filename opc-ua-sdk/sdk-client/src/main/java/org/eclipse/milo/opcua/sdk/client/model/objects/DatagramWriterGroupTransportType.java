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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.TransmitQosDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface DatagramWriterGroupTransportType extends WriterGroupTransportType {
  QualifiedProperty<UByte> MESSAGE_REPEAT_COUNT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MessageRepeatCount",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=3"),
          -1,
          UByte.class);

  QualifiedProperty<Double> MESSAGE_REPEAT_DELAY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MessageRepeatDelay",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<String> QOS_CATEGORY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "QosCategory",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<TransmitQosDataType[]> DATAGRAM_QOS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DatagramQos",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23604"),
          1,
          TransmitQosDataType[].class);

  QualifiedProperty<UInteger> DISCOVERY_ANNOUNCE_RATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DiscoveryAnnounceRate",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<String> TOPIC =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Topic",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /** Gets the existing node's local value. */
  @Nullable UByte getMessageRepeatCount() throws UaException;

  /** Sets the existing node's local value. */
  void setMessageRepeatCount(@Nullable UByte value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UByte readMessageRepeatCount() throws UaException;

  /** Writes the value remotely. */
  void writeMessageRepeatCount(@Nullable UByte value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UByte> readMessageRepeatCountAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMessageRepeatCountAsync(@Nullable UByte value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMessageRepeatCountNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMessageRepeatCountNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getMessageRepeatDelay() throws UaException;

  /** Sets the existing node's local value. */
  void setMessageRepeatDelay(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readMessageRepeatDelay() throws UaException;

  /** Writes the value remotely. */
  void writeMessageRepeatDelay(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readMessageRepeatDelayAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMessageRepeatDelayAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMessageRepeatDelayNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMessageRepeatDelayNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getQosCategory() throws UaException;

  /** Sets the existing node's local value. */
  void setQosCategory(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readQosCategory() throws UaException;

  /** Writes the value remotely. */
  void writeQosCategory(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readQosCategoryAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeQosCategoryAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getQosCategoryNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getQosCategoryNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable TransmitQosDataType @Nullable [] getDatagramQos() throws UaException;

  /** Sets the existing node's local value. */
  void setDatagramQos(@Nullable TransmitQosDataType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable TransmitQosDataType @Nullable [] readDatagramQos() throws UaException;

  /** Writes the value remotely. */
  void writeDatagramQos(@Nullable TransmitQosDataType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable TransmitQosDataType @Nullable []> readDatagramQosAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDatagramQosAsync(
      @Nullable TransmitQosDataType @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDatagramQosNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getDatagramQosNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getDiscoveryAnnounceRate() throws UaException;

  /** Sets the existing node's local value. */
  void setDiscoveryAnnounceRate(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readDiscoveryAnnounceRate() throws UaException;

  /** Writes the value remotely. */
  void writeDiscoveryAnnounceRate(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readDiscoveryAnnounceRateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDiscoveryAnnounceRateAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDiscoveryAnnounceRateNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getDiscoveryAnnounceRateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getTopic() throws UaException;

  /** Sets the existing node's local value. */
  void setTopic(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readTopic() throws UaException;

  /** Writes the value remotely. */
  void writeTopic(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readTopicAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTopicAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getTopicNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getTopicNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable NetworkAddressType getAddressNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable NetworkAddressType> getAddressNodeAsync();
}
