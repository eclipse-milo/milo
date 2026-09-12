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
  @Nullable UInteger getMaxPubSubConnections() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxPubSubConnections(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxPubSubConnections() throws UaException;

  /** Writes the value remotely. */
  void writeMaxPubSubConnections(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxPubSubConnectionsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxPubSubConnectionsAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxPubSubConnectionsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMaxPubSubConnectionsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxWriterGroups() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxWriterGroups(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxWriterGroups() throws UaException;

  /** Writes the value remotely. */
  void writeMaxWriterGroups(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxWriterGroupsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxWriterGroupsAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxWriterGroupsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMaxWriterGroupsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxReaderGroups() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxReaderGroups(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxReaderGroups() throws UaException;

  /** Writes the value remotely. */
  void writeMaxReaderGroups(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxReaderGroupsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxReaderGroupsAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxReaderGroupsNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMaxReaderGroupsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxDataSetWriters() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxDataSetWriters(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxDataSetWriters() throws UaException;

  /** Writes the value remotely. */
  void writeMaxDataSetWriters(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxDataSetWritersAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxDataSetWritersAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxDataSetWritersNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMaxDataSetWritersNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxDataSetReaders() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxDataSetReaders(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxDataSetReaders() throws UaException;

  /** Writes the value remotely. */
  void writeMaxDataSetReaders(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxDataSetReadersAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxDataSetReadersAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxDataSetReadersNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMaxDataSetReadersNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxFieldsPerDataSet() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxFieldsPerDataSet(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxFieldsPerDataSet() throws UaException;

  /** Writes the value remotely. */
  void writeMaxFieldsPerDataSet(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxFieldsPerDataSetAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxFieldsPerDataSetAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxFieldsPerDataSetNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMaxFieldsPerDataSetNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxDataSetWritersPerGroup() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxDataSetWritersPerGroup(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxDataSetWritersPerGroup() throws UaException;

  /** Writes the value remotely. */
  void writeMaxDataSetWritersPerGroup(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxDataSetWritersPerGroupAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxDataSetWritersPerGroupAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxDataSetWritersPerGroupNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxDataSetWritersPerGroupNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxSecurityGroups() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxSecurityGroups(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxSecurityGroups() throws UaException;

  /** Writes the value remotely. */
  void writeMaxSecurityGroups(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxSecurityGroupsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxSecurityGroupsAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxSecurityGroupsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxSecurityGroupsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxPushTargets() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxPushTargets(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxPushTargets() throws UaException;

  /** Writes the value remotely. */
  void writeMaxPushTargets(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxPushTargetsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxPushTargetsAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxPushTargetsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxPushTargetsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxPublishedDataSets() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxPublishedDataSets(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxPublishedDataSets() throws UaException;

  /** Writes the value remotely. */
  void writeMaxPublishedDataSets(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxPublishedDataSetsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxPublishedDataSetsAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxPublishedDataSetsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxPublishedDataSetsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxStandaloneSubscribedDataSets() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxStandaloneSubscribedDataSets(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxStandaloneSubscribedDataSets() throws UaException;

  /** Writes the value remotely. */
  void writeMaxStandaloneSubscribedDataSets(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxStandaloneSubscribedDataSetsAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxStandaloneSubscribedDataSetsAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxStandaloneSubscribedDataSetsNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxStandaloneSubscribedDataSetsNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNetworkMessageSizeDatagram() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxNetworkMessageSizeDatagram(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxNetworkMessageSizeDatagram() throws UaException;

  /** Writes the value remotely. */
  void writeMaxNetworkMessageSizeDatagram(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNetworkMessageSizeDatagramAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxNetworkMessageSizeDatagramAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNetworkMessageSizeDatagramNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNetworkMessageSizeDatagramNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNetworkMessageSizeBroker() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxNetworkMessageSizeBroker(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxNetworkMessageSizeBroker() throws UaException;

  /** Writes the value remotely. */
  void writeMaxNetworkMessageSizeBroker(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNetworkMessageSizeBrokerAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxNetworkMessageSizeBrokerAsync(@Nullable UInteger value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxNetworkMessageSizeBrokerNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNetworkMessageSizeBrokerNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getSupportSecurityKeyPull() throws UaException;

  /** Sets the existing node's local value. */
  void setSupportSecurityKeyPull(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readSupportSecurityKeyPull() throws UaException;

  /** Writes the value remotely. */
  void writeSupportSecurityKeyPull(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readSupportSecurityKeyPullAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSupportSecurityKeyPullAsync(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSupportSecurityKeyPullNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getSupportSecurityKeyPullNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getSupportSecurityKeyPush() throws UaException;

  /** Sets the existing node's local value. */
  void setSupportSecurityKeyPush(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readSupportSecurityKeyPush() throws UaException;

  /** Writes the value remotely. */
  void writeSupportSecurityKeyPush(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readSupportSecurityKeyPushAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSupportSecurityKeyPushAsync(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSupportSecurityKeyPushNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getSupportSecurityKeyPushNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean getSupportSecurityKeyServer() throws UaException;

  /** Sets the existing node's local value. */
  void setSupportSecurityKeyServer(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean readSupportSecurityKeyServer() throws UaException;

  /** Writes the value remotely. */
  void writeSupportSecurityKeyServer(@Nullable Boolean value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean> readSupportSecurityKeyServerAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSupportSecurityKeyServerAsync(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSupportSecurityKeyServerNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getSupportSecurityKeyServerNodeAsync();
}
