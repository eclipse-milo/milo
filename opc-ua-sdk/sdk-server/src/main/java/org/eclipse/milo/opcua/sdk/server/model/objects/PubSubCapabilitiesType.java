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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.12/#9.1.12.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.12/#9.1.12.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxPubSubConnections();

  /** Sets the existing node's local value. */
  void setMaxPubSubConnections(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxPubSubConnectionsNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxWriterGroups();

  /** Sets the existing node's local value. */
  void setMaxWriterGroups(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxWriterGroupsNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxReaderGroups();

  /** Sets the existing node's local value. */
  void setMaxReaderGroups(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxReaderGroupsNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxDataSetWriters();

  /** Sets the existing node's local value. */
  void setMaxDataSetWriters(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxDataSetWritersNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxDataSetReaders();

  /** Sets the existing node's local value. */
  void setMaxDataSetReaders(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxDataSetReadersNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxFieldsPerDataSet();

  /** Sets the existing node's local value. */
  void setMaxFieldsPerDataSet(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxFieldsPerDataSetNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxDataSetWritersPerGroup();

  /** Sets the existing node's local value. */
  void setMaxDataSetWritersPerGroup(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxDataSetWritersPerGroupNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxSecurityGroups();

  /** Sets the existing node's local value. */
  void setMaxSecurityGroups(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxSecurityGroupsNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxPushTargets();

  /** Sets the existing node's local value. */
  void setMaxPushTargets(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxPushTargetsNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxPublishedDataSets();

  /** Sets the existing node's local value. */
  void setMaxPublishedDataSets(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxPublishedDataSetsNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxStandaloneSubscribedDataSets();

  /** Sets the existing node's local value. */
  void setMaxStandaloneSubscribedDataSets(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxStandaloneSubscribedDataSetsNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNetworkMessageSizeDatagram();

  /** Sets the existing node's local value. */
  void setMaxNetworkMessageSizeDatagram(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNetworkMessageSizeDatagramNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNetworkMessageSizeBroker();

  /** Sets the existing node's local value. */
  void setMaxNetworkMessageSizeBroker(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNetworkMessageSizeBrokerNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getSupportSecurityKeyPull();

  /** Sets the existing node's local value. */
  void setSupportSecurityKeyPull(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSupportSecurityKeyPullNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getSupportSecurityKeyPush();

  /** Sets the existing node's local value. */
  void setSupportSecurityKeyPush(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSupportSecurityKeyPushNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getSupportSecurityKeyServer();

  /** Sets the existing node's local value. */
  void setSupportSecurityKeyServer(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSupportSecurityKeyServerNode();
}
