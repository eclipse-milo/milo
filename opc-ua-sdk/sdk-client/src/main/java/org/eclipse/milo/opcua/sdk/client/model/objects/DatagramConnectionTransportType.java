package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.QosDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the DatagramConnectionTransportType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.1">Model
 *     documentation</a>
 */
public interface DatagramConnectionTransportType extends ConnectionTransportType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15064L);

  QualifiedProperty<QosDataType[]> DatagramQos_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DatagramQos",
          ExpandedNodeId.of(Namespaces.OPC_UA, 23603L),
          1,
          QosDataType[].class);

  QualifiedProperty<String> QosCategory_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "QosCategory",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<UInteger> DiscoveryAnnounceRate_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DiscoveryAnnounceRate",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> DiscoveryMaxMessageSize_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DiscoveryMaxMessageSize",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  /**
   * Resolves the optional DatagramQos child, a PropertyType with DataType QosDataType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getDatagramQosNode() throws UaException;

  /** Asynchronous form of {@link #getDatagramQosNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getDatagramQosNodeAsync();

  /**
   * Reads the Value of the DatagramQos child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable QosDataType @Nullable [] readDatagramQos() throws UaException;

  /**
   * Writes the Value of the DatagramQos child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDatagramQos(@Nullable QosDataType @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readDatagramQos()}. */
  CompletableFuture<? extends @Nullable QosDataType @Nullable []> readDatagramQosAsync();

  /** Asynchronous form of {@link #writeDatagramQos}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDatagramQosAsync(@Nullable QosDataType @Nullable [] value);

  /**
   * Resolves the optional QosCategory child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getQosCategoryNode() throws UaException;

  /** Asynchronous form of {@link #getQosCategoryNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getQosCategoryNodeAsync();

  /**
   * Reads the Value of the QosCategory child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readQosCategory() throws UaException;

  /**
   * Writes the Value of the QosCategory child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeQosCategory(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readQosCategory()}. */
  CompletableFuture<? extends @Nullable String> readQosCategoryAsync();

  /** Asynchronous form of {@link #writeQosCategory}; completes with the operation status. */
  CompletableFuture<StatusCode> writeQosCategoryAsync(@Nullable String value);

  /**
   * Resolves the mandatory DiscoveryAddress child, a NetworkAddressType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.6">NetworkAddressType
   *     documentation</a>
   */
  NetworkAddressType getDiscoveryAddressNode() throws UaException;

  /** Asynchronous form of {@link #getDiscoveryAddressNode()}. */
  CompletableFuture<? extends NetworkAddressType> getDiscoveryAddressNodeAsync();

  /**
   * Resolves the optional DiscoveryAnnounceRate child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getDiscoveryAnnounceRateNode() throws UaException;

  /** Asynchronous form of {@link #getDiscoveryAnnounceRateNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getDiscoveryAnnounceRateNodeAsync();

  /**
   * Reads the Value of the DiscoveryAnnounceRate child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readDiscoveryAnnounceRate() throws UaException;

  /**
   * Writes the Value of the DiscoveryAnnounceRate child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDiscoveryAnnounceRate(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readDiscoveryAnnounceRate()}. */
  CompletableFuture<? extends @Nullable UInteger> readDiscoveryAnnounceRateAsync();

  /**
   * Asynchronous form of {@link #writeDiscoveryAnnounceRate}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeDiscoveryAnnounceRateAsync(@Nullable UInteger value);

  /**
   * Resolves the optional DiscoveryMaxMessageSize child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getDiscoveryMaxMessageSizeNode() throws UaException;

  /** Asynchronous form of {@link #getDiscoveryMaxMessageSizeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getDiscoveryMaxMessageSizeNodeAsync();

  /**
   * Reads the Value of the DiscoveryMaxMessageSize child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readDiscoveryMaxMessageSize() throws UaException;

  /**
   * Writes the Value of the DiscoveryMaxMessageSize child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDiscoveryMaxMessageSize(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readDiscoveryMaxMessageSize()}. */
  CompletableFuture<? extends @Nullable UInteger> readDiscoveryMaxMessageSizeAsync();

  /**
   * Asynchronous form of {@link #writeDiscoveryMaxMessageSize}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeDiscoveryMaxMessageSizeAsync(@Nullable UInteger value);
}
