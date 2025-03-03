/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.12/#9.1.12.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.12/#9.1.12.1</a>
 */
public interface PubSubCapabilitiesType extends BaseObjectType {
  QualifiedProperty<UInteger> MAX_PUB_SUB_CONNECTIONS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxPubSubConnections",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_WRITER_GROUPS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxWriterGroups",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_READER_GROUPS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxReaderGroups",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_DATA_SET_WRITERS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxDataSetWriters",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_DATA_SET_READERS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxDataSetReaders",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_FIELDS_PER_DATA_SET =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxFieldsPerDataSet",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_DATA_SET_WRITERS_PER_GROUP =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxDataSetWritersPerGroup",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_SECURITY_GROUPS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxSecurityGroups",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_PUSH_TARGETS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxPushTargets",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_PUBLISHED_DATA_SETS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxPublishedDataSets",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_STANDALONE_SUBSCRIBED_DATA_SETS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxStandaloneSubscribedDataSets",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_NETWORK_MESSAGE_SIZE_DATAGRAM =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxNetworkMessageSizeDatagram",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MAX_NETWORK_MESSAGE_SIZE_BROKER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxNetworkMessageSizeBroker",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<Boolean> SUPPORT_SECURITY_KEY_PULL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SupportSecurityKeyPull",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> SUPPORT_SECURITY_KEY_PUSH =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SupportSecurityKeyPush",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> SUPPORT_SECURITY_KEY_SERVER =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SupportSecurityKeyServer",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  UInteger getMaxPubSubConnections();

  void setMaxPubSubConnections(UInteger value);

  PropertyType getMaxPubSubConnectionsNode();

  UInteger getMaxWriterGroups();

  void setMaxWriterGroups(UInteger value);

  PropertyType getMaxWriterGroupsNode();

  UInteger getMaxReaderGroups();

  void setMaxReaderGroups(UInteger value);

  PropertyType getMaxReaderGroupsNode();

  UInteger getMaxDataSetWriters();

  void setMaxDataSetWriters(UInteger value);

  PropertyType getMaxDataSetWritersNode();

  UInteger getMaxDataSetReaders();

  void setMaxDataSetReaders(UInteger value);

  PropertyType getMaxDataSetReadersNode();

  UInteger getMaxFieldsPerDataSet();

  void setMaxFieldsPerDataSet(UInteger value);

  PropertyType getMaxFieldsPerDataSetNode();

  UInteger getMaxDataSetWritersPerGroup();

  void setMaxDataSetWritersPerGroup(UInteger value);

  PropertyType getMaxDataSetWritersPerGroupNode();

  UInteger getMaxSecurityGroups();

  void setMaxSecurityGroups(UInteger value);

  PropertyType getMaxSecurityGroupsNode();

  UInteger getMaxPushTargets();

  void setMaxPushTargets(UInteger value);

  PropertyType getMaxPushTargetsNode();

  UInteger getMaxPublishedDataSets();

  void setMaxPublishedDataSets(UInteger value);

  PropertyType getMaxPublishedDataSetsNode();

  UInteger getMaxStandaloneSubscribedDataSets();

  void setMaxStandaloneSubscribedDataSets(UInteger value);

  PropertyType getMaxStandaloneSubscribedDataSetsNode();

  UInteger getMaxNetworkMessageSizeDatagram();

  void setMaxNetworkMessageSizeDatagram(UInteger value);

  PropertyType getMaxNetworkMessageSizeDatagramNode();

  UInteger getMaxNetworkMessageSizeBroker();

  void setMaxNetworkMessageSizeBroker(UInteger value);

  PropertyType getMaxNetworkMessageSizeBrokerNode();

  Boolean getSupportSecurityKeyPull();

  void setSupportSecurityKeyPull(Boolean value);

  PropertyType getSupportSecurityKeyPullNode();

  Boolean getSupportSecurityKeyPush();

  void setSupportSecurityKeyPush(Boolean value);

  PropertyType getSupportSecurityKeyPushNode();

  Boolean getSupportSecurityKeyServer();

  void setSupportSecurityKeyServer(Boolean value);

  PropertyType getSupportSecurityKeyServerNode();
}
