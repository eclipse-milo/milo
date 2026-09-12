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
import org.eclipse.milo.opcua.stack.core.types.structured.QosDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface DatagramConnectionTransportType extends ConnectionTransportType {
  QualifiedProperty<UInteger> DISCOVERY_ANNOUNCE_RATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DiscoveryAnnounceRate",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> DISCOVERY_MAX_MESSAGE_SIZE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DiscoveryMaxMessageSize",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<String> QOS_CATEGORY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "QosCategory",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<QosDataType[]> DATAGRAM_QOS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DatagramQos",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23603"),
          1,
          QosDataType[].class);

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
  @Nullable UInteger getDiscoveryMaxMessageSize() throws UaException;

  /** Sets the existing node's local value. */
  void setDiscoveryMaxMessageSize(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readDiscoveryMaxMessageSize() throws UaException;

  /** Writes the value remotely. */
  void writeDiscoveryMaxMessageSize(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readDiscoveryMaxMessageSizeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDiscoveryMaxMessageSizeAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDiscoveryMaxMessageSizeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getDiscoveryMaxMessageSizeNodeAsync();

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
  @Nullable QosDataType @Nullable [] getDatagramQos() throws UaException;

  /** Sets the existing node's local value. */
  void setDatagramQos(@Nullable QosDataType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable QosDataType @Nullable [] readDatagramQos() throws UaException;

  /** Writes the value remotely. */
  void writeDatagramQos(@Nullable QosDataType @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable QosDataType @Nullable []> readDatagramQosAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDatagramQosAsync(@Nullable QosDataType @Nullable [] value);

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

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  NetworkAddressType getDiscoveryAddressNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends NetworkAddressType> getDiscoveryAddressNodeAsync();
}
