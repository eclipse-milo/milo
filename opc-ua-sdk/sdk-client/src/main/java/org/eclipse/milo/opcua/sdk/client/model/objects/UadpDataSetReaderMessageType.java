package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the UadpDataSetReaderMessageType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.3">Model
 *     documentation</a>
 */
public interface UadpDataSetReaderMessageType extends DataSetReaderMessageType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 21116L);

  QualifiedProperty<UInteger> GroupVersion_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "GroupVersion",
          ExpandedNodeId.of(Namespaces.OPC_UA, 20998L),
          -1,
          UInteger.class);

  QualifiedProperty<UShort> DataSetOffset_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DataSetOffset",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  QualifiedProperty<Double> ReceiveOffset_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ReceiveOffset",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<UUID> DataSetClassId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DataSetClassId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 14L),
          -1,
          UUID.class);

  QualifiedProperty<Double> ProcessingOffset_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ProcessingOffset",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<Double> PublishingInterval_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "PublishingInterval",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<UShort> NetworkMessageNumber_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "NetworkMessageNumber",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  QualifiedProperty<UadpDataSetMessageContentMask> DataSetMessageContentMask_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DataSetMessageContentMask",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15646L),
          -1,
          UadpDataSetMessageContentMask.class);

  QualifiedProperty<UadpNetworkMessageContentMask> NetworkMessageContentMask_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "NetworkMessageContentMask",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15642L),
          -1,
          UadpNetworkMessageContentMask.class);

  /**
   * Resolves the mandatory GroupVersion child, a PropertyType with DataType VersionTime.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getGroupVersionNode() throws UaException;

  /** Asynchronous form of {@link #getGroupVersionNode()}. */
  CompletableFuture<? extends PropertyType> getGroupVersionNodeAsync();

  /**
   * Reads the Value of the GroupVersion child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readGroupVersion() throws UaException;

  /**
   * Writes the Value of the GroupVersion child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeGroupVersion(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readGroupVersion()}. */
  CompletableFuture<? extends @Nullable UInteger> readGroupVersionAsync();

  /** Asynchronous form of {@link #writeGroupVersion}; completes with the operation status. */
  CompletableFuture<StatusCode> writeGroupVersionAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory DataSetOffset child, a PropertyType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getDataSetOffsetNode() throws UaException;

  /** Asynchronous form of {@link #getDataSetOffsetNode()}. */
  CompletableFuture<? extends PropertyType> getDataSetOffsetNodeAsync();

  /**
   * Reads the Value of the DataSetOffset child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readDataSetOffset() throws UaException;

  /**
   * Writes the Value of the DataSetOffset child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDataSetOffset(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readDataSetOffset()}. */
  CompletableFuture<? extends @Nullable UShort> readDataSetOffsetAsync();

  /** Asynchronous form of {@link #writeDataSetOffset}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDataSetOffsetAsync(@Nullable UShort value);

  /**
   * Resolves the mandatory ReceiveOffset child, a PropertyType with DataType Duration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getReceiveOffsetNode() throws UaException;

  /** Asynchronous form of {@link #getReceiveOffsetNode()}. */
  CompletableFuture<? extends PropertyType> getReceiveOffsetNodeAsync();

  /**
   * Reads the Value of the ReceiveOffset child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readReceiveOffset() throws UaException;

  /**
   * Writes the Value of the ReceiveOffset child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeReceiveOffset(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readReceiveOffset()}. */
  CompletableFuture<? extends @Nullable Double> readReceiveOffsetAsync();

  /** Asynchronous form of {@link #writeReceiveOffset}; completes with the operation status. */
  CompletableFuture<StatusCode> writeReceiveOffsetAsync(@Nullable Double value);

  /**
   * Resolves the mandatory DataSetClassId child, a PropertyType with DataType Guid.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getDataSetClassIdNode() throws UaException;

  /** Asynchronous form of {@link #getDataSetClassIdNode()}. */
  CompletableFuture<? extends PropertyType> getDataSetClassIdNodeAsync();

  /**
   * Reads the Value of the DataSetClassId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UUID readDataSetClassId() throws UaException;

  /**
   * Writes the Value of the DataSetClassId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDataSetClassId(@Nullable UUID value) throws UaException;

  /** Asynchronous form of {@link #readDataSetClassId()}. */
  CompletableFuture<? extends @Nullable UUID> readDataSetClassIdAsync();

  /** Asynchronous form of {@link #writeDataSetClassId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDataSetClassIdAsync(@Nullable UUID value);

  /**
   * Resolves the mandatory ProcessingOffset child, a PropertyType with DataType Duration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getProcessingOffsetNode() throws UaException;

  /** Asynchronous form of {@link #getProcessingOffsetNode()}. */
  CompletableFuture<? extends PropertyType> getProcessingOffsetNodeAsync();

  /**
   * Reads the Value of the ProcessingOffset child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readProcessingOffset() throws UaException;

  /**
   * Writes the Value of the ProcessingOffset child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeProcessingOffset(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readProcessingOffset()}. */
  CompletableFuture<? extends @Nullable Double> readProcessingOffsetAsync();

  /** Asynchronous form of {@link #writeProcessingOffset}; completes with the operation status. */
  CompletableFuture<StatusCode> writeProcessingOffsetAsync(@Nullable Double value);

  /**
   * Resolves the mandatory PublishingInterval child, a PropertyType with DataType Duration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getPublishingIntervalNode() throws UaException;

  /** Asynchronous form of {@link #getPublishingIntervalNode()}. */
  CompletableFuture<? extends PropertyType> getPublishingIntervalNodeAsync();

  /**
   * Reads the Value of the PublishingInterval child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readPublishingInterval() throws UaException;

  /**
   * Writes the Value of the PublishingInterval child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePublishingInterval(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readPublishingInterval()}. */
  CompletableFuture<? extends @Nullable Double> readPublishingIntervalAsync();

  /** Asynchronous form of {@link #writePublishingInterval}; completes with the operation status. */
  CompletableFuture<StatusCode> writePublishingIntervalAsync(@Nullable Double value);

  /**
   * Resolves the mandatory NetworkMessageNumber child, a PropertyType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getNetworkMessageNumberNode() throws UaException;

  /** Asynchronous form of {@link #getNetworkMessageNumberNode()}. */
  CompletableFuture<? extends PropertyType> getNetworkMessageNumberNodeAsync();

  /**
   * Reads the Value of the NetworkMessageNumber child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readNetworkMessageNumber() throws UaException;

  /**
   * Writes the Value of the NetworkMessageNumber child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNetworkMessageNumber(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readNetworkMessageNumber()}. */
  CompletableFuture<? extends @Nullable UShort> readNetworkMessageNumberAsync();

  /**
   * Asynchronous form of {@link #writeNetworkMessageNumber}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeNetworkMessageNumberAsync(@Nullable UShort value);

  /**
   * Resolves the mandatory DataSetMessageContentMask child, a PropertyType with DataType
   * UadpDataSetMessageContentMask.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getDataSetMessageContentMaskNode() throws UaException;

  /** Asynchronous form of {@link #getDataSetMessageContentMaskNode()}. */
  CompletableFuture<? extends PropertyType> getDataSetMessageContentMaskNodeAsync();

  /**
   * Reads the Value of the DataSetMessageContentMask child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UadpDataSetMessageContentMask readDataSetMessageContentMask() throws UaException;

  /**
   * Writes the Value of the DataSetMessageContentMask child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDataSetMessageContentMask(@Nullable UadpDataSetMessageContentMask value)
      throws UaException;

  /** Asynchronous form of {@link #readDataSetMessageContentMask()}. */
  CompletableFuture<? extends @Nullable UadpDataSetMessageContentMask>
      readDataSetMessageContentMaskAsync();

  /**
   * Asynchronous form of {@link #writeDataSetMessageContentMask}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeDataSetMessageContentMaskAsync(
      @Nullable UadpDataSetMessageContentMask value);

  /**
   * Resolves the mandatory NetworkMessageContentMask child, a PropertyType with DataType
   * UadpNetworkMessageContentMask.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getNetworkMessageContentMaskNode() throws UaException;

  /** Asynchronous form of {@link #getNetworkMessageContentMaskNode()}. */
  CompletableFuture<? extends PropertyType> getNetworkMessageContentMaskNodeAsync();

  /**
   * Reads the Value of the NetworkMessageContentMask child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UadpNetworkMessageContentMask readNetworkMessageContentMask() throws UaException;

  /**
   * Writes the Value of the NetworkMessageContentMask child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNetworkMessageContentMask(@Nullable UadpNetworkMessageContentMask value)
      throws UaException;

  /** Asynchronous form of {@link #readNetworkMessageContentMask()}. */
  CompletableFuture<? extends @Nullable UadpNetworkMessageContentMask>
      readNetworkMessageContentMaskAsync();

  /**
   * Asynchronous form of {@link #writeNetworkMessageContentMask}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeNetworkMessageContentMaskAsync(
      @Nullable UadpNetworkMessageContentMask value);
}
