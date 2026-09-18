package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the BrokerDataSetWriterTransportType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.2/#9.3.2.3">Model
 *     documentation</a>
 */
public interface BrokerDataSetWriterTransportType extends DataSetWriterTransportType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 21138L);

  QualifiedProperty<String> ResourceUri_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ResourceUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<String> MetaDataQueueName_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MetaDataQueueName",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<Double> MetaDataUpdateTime_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MetaDataUpdateTime",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<String> AuthenticationProfileUri_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "AuthenticationProfileUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<BrokerTransportQualityOfService> RequestedDeliveryGuarantee_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "RequestedDeliveryGuarantee",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15008L),
          -1,
          BrokerTransportQualityOfService.class);

  QualifiedProperty<String> QueueName_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "QueueName",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  /**
   * Resolves the mandatory ResourceUri child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getResourceUriNode() throws UaException;

  /** Asynchronous form of {@link #getResourceUriNode()}. */
  CompletableFuture<? extends PropertyType> getResourceUriNodeAsync();

  /**
   * Reads the Value of the ResourceUri child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readResourceUri() throws UaException;

  /**
   * Writes the Value of the ResourceUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeResourceUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readResourceUri()}. */
  CompletableFuture<? extends @Nullable String> readResourceUriAsync();

  /** Asynchronous form of {@link #writeResourceUri}; completes with the operation status. */
  CompletableFuture<StatusCode> writeResourceUriAsync(@Nullable String value);

  /**
   * Resolves the mandatory MetaDataQueueName child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMetaDataQueueNameNode() throws UaException;

  /** Asynchronous form of {@link #getMetaDataQueueNameNode()}. */
  CompletableFuture<? extends PropertyType> getMetaDataQueueNameNodeAsync();

  /**
   * Reads the Value of the MetaDataQueueName child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readMetaDataQueueName() throws UaException;

  /**
   * Writes the Value of the MetaDataQueueName child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMetaDataQueueName(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readMetaDataQueueName()}. */
  CompletableFuture<? extends @Nullable String> readMetaDataQueueNameAsync();

  /** Asynchronous form of {@link #writeMetaDataQueueName}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMetaDataQueueNameAsync(@Nullable String value);

  /**
   * Resolves the mandatory MetaDataUpdateTime child, a PropertyType with DataType Duration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMetaDataUpdateTimeNode() throws UaException;

  /** Asynchronous form of {@link #getMetaDataUpdateTimeNode()}. */
  CompletableFuture<? extends PropertyType> getMetaDataUpdateTimeNodeAsync();

  /**
   * Reads the Value of the MetaDataUpdateTime child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readMetaDataUpdateTime() throws UaException;

  /**
   * Writes the Value of the MetaDataUpdateTime child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMetaDataUpdateTime(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readMetaDataUpdateTime()}. */
  CompletableFuture<? extends @Nullable Double> readMetaDataUpdateTimeAsync();

  /** Asynchronous form of {@link #writeMetaDataUpdateTime}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMetaDataUpdateTimeAsync(@Nullable Double value);

  /**
   * Resolves the mandatory AuthenticationProfileUri child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getAuthenticationProfileUriNode() throws UaException;

  /** Asynchronous form of {@link #getAuthenticationProfileUriNode()}. */
  CompletableFuture<? extends PropertyType> getAuthenticationProfileUriNodeAsync();

  /**
   * Reads the Value of the AuthenticationProfileUri child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readAuthenticationProfileUri() throws UaException;

  /**
   * Writes the Value of the AuthenticationProfileUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAuthenticationProfileUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readAuthenticationProfileUri()}. */
  CompletableFuture<? extends @Nullable String> readAuthenticationProfileUriAsync();

  /**
   * Asynchronous form of {@link #writeAuthenticationProfileUri}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeAuthenticationProfileUriAsync(@Nullable String value);

  /**
   * Resolves the mandatory RequestedDeliveryGuarantee child, a PropertyType with DataType
   * BrokerTransportQualityOfService.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getRequestedDeliveryGuaranteeNode() throws UaException;

  /** Asynchronous form of {@link #getRequestedDeliveryGuaranteeNode()}. */
  CompletableFuture<? extends PropertyType> getRequestedDeliveryGuaranteeNodeAsync();

  /**
   * Reads the Value of the RequestedDeliveryGuarantee child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable BrokerTransportQualityOfService readRequestedDeliveryGuarantee() throws UaException;

  /**
   * Writes the Value of the RequestedDeliveryGuarantee child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRequestedDeliveryGuarantee(@Nullable BrokerTransportQualityOfService value)
      throws UaException;

  /** Asynchronous form of {@link #readRequestedDeliveryGuarantee()}. */
  CompletableFuture<? extends @Nullable BrokerTransportQualityOfService>
      readRequestedDeliveryGuaranteeAsync();

  /**
   * Asynchronous form of {@link #writeRequestedDeliveryGuarantee}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeRequestedDeliveryGuaranteeAsync(
      @Nullable BrokerTransportQualityOfService value);

  /**
   * Resolves the mandatory QueueName child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getQueueNameNode() throws UaException;

  /** Asynchronous form of {@link #getQueueNameNode()}. */
  CompletableFuture<? extends PropertyType> getQueueNameNodeAsync();

  /**
   * Reads the Value of the QueueName child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readQueueName() throws UaException;

  /**
   * Writes the Value of the QueueName child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeQueueName(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readQueueName()}. */
  CompletableFuture<? extends @Nullable String> readQueueNameAsync();

  /** Asynchronous form of {@link #writeQueueName}; completes with the operation status. */
  CompletableFuture<StatusCode> writeQueueNameAsync(@Nullable String value);
}
