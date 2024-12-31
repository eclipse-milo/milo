/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.1</a>
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

  UInteger getDiscoveryAnnounceRate();

  void setDiscoveryAnnounceRate(UInteger value);

  PropertyType getDiscoveryAnnounceRateNode();

  UInteger getDiscoveryMaxMessageSize();

  void setDiscoveryMaxMessageSize(UInteger value);

  PropertyType getDiscoveryMaxMessageSizeNode();

  String getQosCategory();

  void setQosCategory(String value);

  PropertyType getQosCategoryNode();

  QosDataType[] getDatagramQos();

  void setDatagramQos(QosDataType[] value);

  PropertyType getDatagramQosNode();

  NetworkAddressType getDiscoveryAddressNode();
}
