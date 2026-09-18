package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.TransmitQosDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the DatagramWriterGroupTransportType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.2">Model
 *     documentation</a>
 */
public interface DatagramWriterGroupTransportType extends WriterGroupTransportType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 21133L);

  QualifiedProperty<TransmitQosDataType[]> DatagramQos_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DatagramQos",
          ExpandedNodeId.of(Namespaces.OPC_UA, 23604L),
          1,
          TransmitQosDataType[].class);

  QualifiedProperty<String> QosCategory_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "QosCategory",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<UByte> MessageRepeatCount_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MessageRepeatCount",
          ExpandedNodeId.of(Namespaces.OPC_UA, 3L),
          -1,
          UByte.class);

  QualifiedProperty<Double> MessageRepeatDelay_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MessageRepeatDelay",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<UInteger> DiscoveryAnnounceRate_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DiscoveryAnnounceRate",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<String> Topic_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA, "Topic", ExpandedNodeId.of(Namespaces.OPC_UA, 12L), -1, String.class);

  /**
   * Resolves the optional DatagramQos child, a PropertyType with DataType TransmitQosDataType.
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
  @Nullable TransmitQosDataType @Nullable [] readDatagramQos() throws UaException;

  /**
   * Writes the Value of the DatagramQos child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDatagramQos(@Nullable TransmitQosDataType @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readDatagramQos()}. */
  CompletableFuture<? extends @Nullable TransmitQosDataType @Nullable []> readDatagramQosAsync();

  /** Asynchronous form of {@link #writeDatagramQos}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDatagramQosAsync(
      @Nullable TransmitQosDataType @Nullable [] value);

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
   * Resolves the optional MessageRepeatCount child, a PropertyType with DataType Byte.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMessageRepeatCountNode() throws UaException;

  /** Asynchronous form of {@link #getMessageRepeatCountNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMessageRepeatCountNodeAsync();

  /**
   * Reads the Value of the MessageRepeatCount child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UByte readMessageRepeatCount() throws UaException;

  /**
   * Writes the Value of the MessageRepeatCount child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMessageRepeatCount(@Nullable UByte value) throws UaException;

  /** Asynchronous form of {@link #readMessageRepeatCount()}. */
  CompletableFuture<? extends @Nullable UByte> readMessageRepeatCountAsync();

  /** Asynchronous form of {@link #writeMessageRepeatCount}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMessageRepeatCountAsync(@Nullable UByte value);

  /**
   * Resolves the optional MessageRepeatDelay child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMessageRepeatDelayNode() throws UaException;

  /** Asynchronous form of {@link #getMessageRepeatDelayNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMessageRepeatDelayNodeAsync();

  /**
   * Reads the Value of the MessageRepeatDelay child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readMessageRepeatDelay() throws UaException;

  /**
   * Writes the Value of the MessageRepeatDelay child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMessageRepeatDelay(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readMessageRepeatDelay()}. */
  CompletableFuture<? extends @Nullable Double> readMessageRepeatDelayAsync();

  /** Asynchronous form of {@link #writeMessageRepeatDelay}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMessageRepeatDelayAsync(@Nullable Double value);

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
   * Resolves the optional Topic child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getTopicNode() throws UaException;

  /** Asynchronous form of {@link #getTopicNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getTopicNodeAsync();

  /**
   * Reads the Value of the Topic child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readTopic() throws UaException;

  /**
   * Writes the Value of the Topic child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTopic(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readTopic()}. */
  CompletableFuture<? extends @Nullable String> readTopicAsync();

  /** Asynchronous form of {@link #writeTopic}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTopicAsync(@Nullable String value);

  /**
   * Resolves the optional Address child, a NetworkAddressType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.6">NetworkAddressType
   *     documentation</a>
   */
  @Nullable NetworkAddressType getAddressNode() throws UaException;

  /** Asynchronous form of {@link #getAddressNode()}. */
  CompletableFuture<? extends @Nullable NetworkAddressType> getAddressNodeAsync();
}
