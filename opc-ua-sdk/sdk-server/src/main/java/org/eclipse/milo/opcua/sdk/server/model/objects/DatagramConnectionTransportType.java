/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
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
  @Nullable UInteger getDiscoveryAnnounceRate();

  /** Sets the existing node's local value. */
  void setDiscoveryAnnounceRate(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDiscoveryAnnounceRateNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getDiscoveryMaxMessageSize();

  /** Sets the existing node's local value. */
  void setDiscoveryMaxMessageSize(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDiscoveryMaxMessageSizeNode();

  /** Gets the existing node's local value. */
  @Nullable String getQosCategory();

  /** Sets the existing node's local value. */
  void setQosCategory(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getQosCategoryNode();

  /** Gets the existing node's local value. */
  @Nullable QosDataType @Nullable [] getDatagramQos();

  /** Sets the existing node's local value. */
  void setDatagramQos(@Nullable QosDataType @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDatagramQosNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  NetworkAddressType getDiscoveryAddressNode();
}
