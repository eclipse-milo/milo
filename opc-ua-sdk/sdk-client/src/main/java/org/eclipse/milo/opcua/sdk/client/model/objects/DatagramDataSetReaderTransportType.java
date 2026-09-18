package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ReceiveQosDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the DatagramDataSetReaderTransportType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.4">Model
 *     documentation</a>
 */
public interface DatagramDataSetReaderTransportType extends DataSetReaderTransportType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24016L);

  QualifiedProperty<ReceiveQosDataType[]> DatagramQos_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DatagramQos",
          ExpandedNodeId.of(Namespaces.OPC_UA, 23608L),
          1,
          ReceiveQosDataType[].class);

  QualifiedProperty<String> QosCategory_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "QosCategory",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<String> Topic_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA, "Topic", ExpandedNodeId.of(Namespaces.OPC_UA, 12L), -1, String.class);

  /**
   * Resolves the optional DatagramQos child, a PropertyType with DataType ReceiveQosDataType.
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
  @Nullable ReceiveQosDataType @Nullable [] readDatagramQos() throws UaException;

  /**
   * Writes the Value of the DatagramQos child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDatagramQos(@Nullable ReceiveQosDataType @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readDatagramQos()}. */
  CompletableFuture<? extends @Nullable ReceiveQosDataType @Nullable []> readDatagramQosAsync();

  /** Asynchronous form of {@link #writeDatagramQos}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDatagramQosAsync(
      @Nullable ReceiveQosDataType @Nullable [] value);

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
