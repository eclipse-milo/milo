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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.TransmitQosDataType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.2</a>
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

  UByte getMessageRepeatCount();

  void setMessageRepeatCount(UByte value);

  PropertyType getMessageRepeatCountNode();

  Double getMessageRepeatDelay();

  void setMessageRepeatDelay(Double value);

  PropertyType getMessageRepeatDelayNode();

  String getQosCategory();

  void setQosCategory(String value);

  PropertyType getQosCategoryNode();

  TransmitQosDataType[] getDatagramQos();

  void setDatagramQos(TransmitQosDataType[] value);

  PropertyType getDatagramQosNode();

  UInteger getDiscoveryAnnounceRate();

  void setDiscoveryAnnounceRate(UInteger value);

  PropertyType getDiscoveryAnnounceRateNode();

  String getTopic();

  void setTopic(String value);

  PropertyType getTopicNode();

  NetworkAddressType getAddressNode();
}
