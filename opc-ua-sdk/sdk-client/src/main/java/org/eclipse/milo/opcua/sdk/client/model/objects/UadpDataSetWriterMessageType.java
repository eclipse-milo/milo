package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UadpDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the UadpDataSetWriterMessageType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.1/#9.2.1.2">Model
 *     documentation</a>
 */
public interface UadpDataSetWriterMessageType extends DataSetWriterMessageType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 21111L);

  QualifiedProperty<UShort> DataSetOffset_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DataSetOffset",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  QualifiedProperty<UShort> ConfiguredSize_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ConfiguredSize",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

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
   * Resolves the mandatory ConfiguredSize child, a PropertyType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getConfiguredSizeNode() throws UaException;

  /** Asynchronous form of {@link #getConfiguredSizeNode()}. */
  CompletableFuture<? extends PropertyType> getConfiguredSizeNodeAsync();

  /**
   * Reads the Value of the ConfiguredSize child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readConfiguredSize() throws UaException;

  /**
   * Writes the Value of the ConfiguredSize child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeConfiguredSize(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readConfiguredSize()}. */
  CompletableFuture<? extends @Nullable UShort> readConfiguredSizeAsync();

  /** Asynchronous form of {@link #writeConfiguredSize}; completes with the operation status. */
  CompletableFuture<StatusCode> writeConfiguredSizeAsync(@Nullable UShort value);

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
}
