package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the PubSubCapabilitiesType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.12/#9.1.12.1">Model
 *     documentation</a>
 */
public interface PubSubCapabilitiesType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 23832L);

  QualifiedProperty<UInteger> MaxPushTargets_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxPushTargets",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxReaderGroups_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxReaderGroups",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxWriterGroups_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxWriterGroups",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxDataSetReaders_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxDataSetReaders",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxDataSetWriters_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxDataSetWriters",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxSecurityGroups_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxSecurityGroups",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxFieldsPerDataSet_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxFieldsPerDataSet",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxPubSubConnections_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxPubSubConnections",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxPublishedDataSets_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxPublishedDataSets",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<Boolean> SupportSecurityKeyPull_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SupportSecurityKeyPull",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> SupportSecurityKeyPush_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SupportSecurityKeyPush",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> SupportSecurityKeyServer_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SupportSecurityKeyServer",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<UInteger> MaxDataSetWritersPerGroup_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxDataSetWritersPerGroup",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxNetworkMessageSizeBroker_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxNetworkMessageSizeBroker",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxNetworkMessageSizeDatagram_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxNetworkMessageSizeDatagram",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxStandaloneSubscribedDataSets_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxStandaloneSubscribedDataSets",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  /**
   * Resolves the optional MaxPushTargets child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxPushTargetsNode() throws UaException;

  /** Asynchronous form of {@link #getMaxPushTargetsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxPushTargetsNodeAsync();

  /**
   * Reads the Value of the MaxPushTargets child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxPushTargets() throws UaException;

  /**
   * Writes the Value of the MaxPushTargets child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxPushTargets(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxPushTargets()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxPushTargetsAsync();

  /** Asynchronous form of {@link #writeMaxPushTargets}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxPushTargetsAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory MaxReaderGroups child, a PropertyType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMaxReaderGroupsNode() throws UaException;

  /** Asynchronous form of {@link #getMaxReaderGroupsNode()}. */
  CompletableFuture<? extends PropertyType> getMaxReaderGroupsNodeAsync();

  /**
   * Reads the Value of the MaxReaderGroups child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxReaderGroups() throws UaException;

  /**
   * Writes the Value of the MaxReaderGroups child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxReaderGroups(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxReaderGroups()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxReaderGroupsAsync();

  /** Asynchronous form of {@link #writeMaxReaderGroups}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxReaderGroupsAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory MaxWriterGroups child, a PropertyType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMaxWriterGroupsNode() throws UaException;

  /** Asynchronous form of {@link #getMaxWriterGroupsNode()}. */
  CompletableFuture<? extends PropertyType> getMaxWriterGroupsNodeAsync();

  /**
   * Reads the Value of the MaxWriterGroups child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxWriterGroups() throws UaException;

  /**
   * Writes the Value of the MaxWriterGroups child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxWriterGroups(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxWriterGroups()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxWriterGroupsAsync();

  /** Asynchronous form of {@link #writeMaxWriterGroups}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxWriterGroupsAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory MaxDataSetReaders child, a PropertyType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMaxDataSetReadersNode() throws UaException;

  /** Asynchronous form of {@link #getMaxDataSetReadersNode()}. */
  CompletableFuture<? extends PropertyType> getMaxDataSetReadersNodeAsync();

  /**
   * Reads the Value of the MaxDataSetReaders child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxDataSetReaders() throws UaException;

  /**
   * Writes the Value of the MaxDataSetReaders child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxDataSetReaders(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxDataSetReaders()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxDataSetReadersAsync();

  /** Asynchronous form of {@link #writeMaxDataSetReaders}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxDataSetReadersAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory MaxDataSetWriters child, a PropertyType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMaxDataSetWritersNode() throws UaException;

  /** Asynchronous form of {@link #getMaxDataSetWritersNode()}. */
  CompletableFuture<? extends PropertyType> getMaxDataSetWritersNodeAsync();

  /**
   * Reads the Value of the MaxDataSetWriters child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxDataSetWriters() throws UaException;

  /**
   * Writes the Value of the MaxDataSetWriters child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxDataSetWriters(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxDataSetWriters()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxDataSetWritersAsync();

  /** Asynchronous form of {@link #writeMaxDataSetWriters}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxDataSetWritersAsync(@Nullable UInteger value);

  /**
   * Resolves the optional MaxSecurityGroups child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxSecurityGroupsNode() throws UaException;

  /** Asynchronous form of {@link #getMaxSecurityGroupsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxSecurityGroupsNodeAsync();

  /**
   * Reads the Value of the MaxSecurityGroups child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxSecurityGroups() throws UaException;

  /**
   * Writes the Value of the MaxSecurityGroups child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxSecurityGroups(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxSecurityGroups()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxSecurityGroupsAsync();

  /** Asynchronous form of {@link #writeMaxSecurityGroups}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxSecurityGroupsAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory MaxFieldsPerDataSet child, a PropertyType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMaxFieldsPerDataSetNode() throws UaException;

  /** Asynchronous form of {@link #getMaxFieldsPerDataSetNode()}. */
  CompletableFuture<? extends PropertyType> getMaxFieldsPerDataSetNodeAsync();

  /**
   * Reads the Value of the MaxFieldsPerDataSet child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxFieldsPerDataSet() throws UaException;

  /**
   * Writes the Value of the MaxFieldsPerDataSet child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxFieldsPerDataSet(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxFieldsPerDataSet()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxFieldsPerDataSetAsync();

  /**
   * Asynchronous form of {@link #writeMaxFieldsPerDataSet}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeMaxFieldsPerDataSetAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory MaxPubSubConnections child, a PropertyType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMaxPubSubConnectionsNode() throws UaException;

  /** Asynchronous form of {@link #getMaxPubSubConnectionsNode()}. */
  CompletableFuture<? extends PropertyType> getMaxPubSubConnectionsNodeAsync();

  /**
   * Reads the Value of the MaxPubSubConnections child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxPubSubConnections() throws UaException;

  /**
   * Writes the Value of the MaxPubSubConnections child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxPubSubConnections(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxPubSubConnections()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxPubSubConnectionsAsync();

  /**
   * Asynchronous form of {@link #writeMaxPubSubConnections}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeMaxPubSubConnectionsAsync(@Nullable UInteger value);

  /**
   * Resolves the optional MaxPublishedDataSets child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxPublishedDataSetsNode() throws UaException;

  /** Asynchronous form of {@link #getMaxPublishedDataSetsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxPublishedDataSetsNodeAsync();

  /**
   * Reads the Value of the MaxPublishedDataSets child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxPublishedDataSets() throws UaException;

  /**
   * Writes the Value of the MaxPublishedDataSets child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxPublishedDataSets(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxPublishedDataSets()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxPublishedDataSetsAsync();

  /**
   * Asynchronous form of {@link #writeMaxPublishedDataSets}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeMaxPublishedDataSetsAsync(@Nullable UInteger value);

  /**
   * Resolves the optional SupportSecurityKeyPull child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getSupportSecurityKeyPullNode() throws UaException;

  /** Asynchronous form of {@link #getSupportSecurityKeyPullNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getSupportSecurityKeyPullNodeAsync();

  /**
   * Reads the Value of the SupportSecurityKeyPull child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readSupportSecurityKeyPull() throws UaException;

  /**
   * Writes the Value of the SupportSecurityKeyPull child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSupportSecurityKeyPull(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readSupportSecurityKeyPull()}. */
  CompletableFuture<? extends @Nullable Boolean> readSupportSecurityKeyPullAsync();

  /**
   * Asynchronous form of {@link #writeSupportSecurityKeyPull}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeSupportSecurityKeyPullAsync(@Nullable Boolean value);

  /**
   * Resolves the optional SupportSecurityKeyPush child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getSupportSecurityKeyPushNode() throws UaException;

  /** Asynchronous form of {@link #getSupportSecurityKeyPushNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getSupportSecurityKeyPushNodeAsync();

  /**
   * Reads the Value of the SupportSecurityKeyPush child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readSupportSecurityKeyPush() throws UaException;

  /**
   * Writes the Value of the SupportSecurityKeyPush child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSupportSecurityKeyPush(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readSupportSecurityKeyPush()}. */
  CompletableFuture<? extends @Nullable Boolean> readSupportSecurityKeyPushAsync();

  /**
   * Asynchronous form of {@link #writeSupportSecurityKeyPush}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeSupportSecurityKeyPushAsync(@Nullable Boolean value);

  /**
   * Resolves the optional SupportSecurityKeyServer child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getSupportSecurityKeyServerNode() throws UaException;

  /** Asynchronous form of {@link #getSupportSecurityKeyServerNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getSupportSecurityKeyServerNodeAsync();

  /**
   * Reads the Value of the SupportSecurityKeyServer child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readSupportSecurityKeyServer() throws UaException;

  /**
   * Writes the Value of the SupportSecurityKeyServer child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSupportSecurityKeyServer(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readSupportSecurityKeyServer()}. */
  CompletableFuture<? extends @Nullable Boolean> readSupportSecurityKeyServerAsync();

  /**
   * Asynchronous form of {@link #writeSupportSecurityKeyServer}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeSupportSecurityKeyServerAsync(@Nullable Boolean value);

  /**
   * Resolves the optional MaxDataSetWritersPerGroup child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxDataSetWritersPerGroupNode() throws UaException;

  /** Asynchronous form of {@link #getMaxDataSetWritersPerGroupNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxDataSetWritersPerGroupNodeAsync();

  /**
   * Reads the Value of the MaxDataSetWritersPerGroup child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxDataSetWritersPerGroup() throws UaException;

  /**
   * Writes the Value of the MaxDataSetWritersPerGroup child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxDataSetWritersPerGroup(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxDataSetWritersPerGroup()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxDataSetWritersPerGroupAsync();

  /**
   * Asynchronous form of {@link #writeMaxDataSetWritersPerGroup}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeMaxDataSetWritersPerGroupAsync(@Nullable UInteger value);

  /**
   * Resolves the optional MaxNetworkMessageSizeBroker child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxNetworkMessageSizeBrokerNode() throws UaException;

  /** Asynchronous form of {@link #getMaxNetworkMessageSizeBrokerNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNetworkMessageSizeBrokerNodeAsync();

  /**
   * Reads the Value of the MaxNetworkMessageSizeBroker child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxNetworkMessageSizeBroker() throws UaException;

  /**
   * Writes the Value of the MaxNetworkMessageSizeBroker child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxNetworkMessageSizeBroker(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxNetworkMessageSizeBroker()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNetworkMessageSizeBrokerAsync();

  /**
   * Asynchronous form of {@link #writeMaxNetworkMessageSizeBroker}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeMaxNetworkMessageSizeBrokerAsync(@Nullable UInteger value);

  /**
   * Resolves the optional MaxNetworkMessageSizeDatagram child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxNetworkMessageSizeDatagramNode() throws UaException;

  /** Asynchronous form of {@link #getMaxNetworkMessageSizeDatagramNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxNetworkMessageSizeDatagramNodeAsync();

  /**
   * Reads the Value of the MaxNetworkMessageSizeDatagram child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxNetworkMessageSizeDatagram() throws UaException;

  /**
   * Writes the Value of the MaxNetworkMessageSizeDatagram child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxNetworkMessageSizeDatagram(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxNetworkMessageSizeDatagram()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNetworkMessageSizeDatagramAsync();

  /**
   * Asynchronous form of {@link #writeMaxNetworkMessageSizeDatagram}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeMaxNetworkMessageSizeDatagramAsync(@Nullable UInteger value);

  /**
   * Resolves the optional MaxStandaloneSubscribedDataSets child, a PropertyType with DataType
   * UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxStandaloneSubscribedDataSetsNode() throws UaException;

  /** Asynchronous form of {@link #getMaxStandaloneSubscribedDataSetsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxStandaloneSubscribedDataSetsNodeAsync();

  /**
   * Reads the Value of the MaxStandaloneSubscribedDataSets child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxStandaloneSubscribedDataSets() throws UaException;

  /**
   * Writes the Value of the MaxStandaloneSubscribedDataSets child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxStandaloneSubscribedDataSets(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxStandaloneSubscribedDataSets()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxStandaloneSubscribedDataSetsAsync();

  /**
   * Asynchronous form of {@link #writeMaxStandaloneSubscribedDataSets}; completes with the
   * operation status.
   */
  CompletableFuture<StatusCode> writeMaxStandaloneSubscribedDataSetsAsync(@Nullable UInteger value);
}
