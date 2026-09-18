package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ServerCapabilitiesType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.2">Model
 *     documentation</a>
 */
public interface ServerCapabilitiesType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2013L);

  QualifiedProperty<UInteger> MaxSessions_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxSessions",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<String[]> LocaleIdArray_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LocaleIdArray",
          ExpandedNodeId.of(Namespaces.OPC_UA, 295L),
          1,
          String[].class);

  QualifiedProperty<UInteger> MaxArrayLength_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxArrayLength",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxStringLength_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxStringLength",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<QualifiedName[]> ConformanceUnits_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ConformanceUnits",
          ExpandedNodeId.of(Namespaces.OPC_UA, 20L),
          1,
          QualifiedName[].class);

  QualifiedProperty<UInteger> MaxSubscriptions_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxSubscriptions",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxMonitoredItems_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxMonitoredItems",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<String[]> ServerProfileArray_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ServerProfileArray",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          1,
          String[].class);

  QualifiedProperty<UInteger> MaxByteStringLength_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxByteStringLength",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<SignedSoftwareCertificate[]> SoftwareCertificates_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SoftwareCertificates",
          ExpandedNodeId.of(Namespaces.OPC_UA, 344L),
          1,
          SignedSoftwareCertificate[].class);

  QualifiedProperty<Double> MinSupportedSampleRate_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MinSupportedSampleRate",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<UInteger> MaxWhereClauseParameters_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxWhereClauseParameters",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxSelectClauseParameters_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxSelectClauseParameters",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> MaxMonitoredItemsQueueSize_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxMonitoredItemsQueueSize",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UShort> MaxQueryContinuationPoints_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxQueryContinuationPoints",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  QualifiedProperty<UInteger> MaxSubscriptionsPerSession_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxSubscriptionsPerSession",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UShort> MaxBrowseContinuationPoints_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxBrowseContinuationPoints",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  QualifiedProperty<UShort> MaxHistoryContinuationPoints_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxHistoryContinuationPoints",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  QualifiedProperty<UShort> MaxLogObjectContinuationPoints_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxLogObjectContinuationPoints",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  QualifiedProperty<UInteger> MaxMonitoredItemsPerSubscription_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxMonitoredItemsPerSubscription",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  /**
   * Resolves the optional MaxSessions child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxSessionsNode() throws UaException;

  /** Asynchronous form of {@link #getMaxSessionsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxSessionsNodeAsync();

  /**
   * Reads the Value of the MaxSessions child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxSessions() throws UaException;

  /**
   * Writes the Value of the MaxSessions child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxSessions(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxSessions()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxSessionsAsync();

  /** Asynchronous form of {@link #writeMaxSessions}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxSessionsAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory LocaleIdArray child, a PropertyType with DataType LocaleId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getLocaleIdArrayNode() throws UaException;

  /** Asynchronous form of {@link #getLocaleIdArrayNode()}. */
  CompletableFuture<? extends PropertyType> getLocaleIdArrayNodeAsync();

  /**
   * Reads the Value of the LocaleIdArray child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String @Nullable [] readLocaleIdArray() throws UaException;

  /**
   * Writes the Value of the LocaleIdArray child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLocaleIdArray(@Nullable String @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readLocaleIdArray()}. */
  CompletableFuture<? extends @Nullable String @Nullable []> readLocaleIdArrayAsync();

  /** Asynchronous form of {@link #writeLocaleIdArray}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLocaleIdArrayAsync(@Nullable String @Nullable [] value);

  /**
   * Resolves the optional MaxArrayLength child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxArrayLengthNode() throws UaException;

  /** Asynchronous form of {@link #getMaxArrayLengthNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxArrayLengthNodeAsync();

  /**
   * Reads the Value of the MaxArrayLength child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxArrayLength() throws UaException;

  /**
   * Writes the Value of the MaxArrayLength child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxArrayLength(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxArrayLength()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxArrayLengthAsync();

  /** Asynchronous form of {@link #writeMaxArrayLength}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxArrayLengthAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory ModellingRules child, a FolderType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.6">FolderType
   *     documentation</a>
   */
  FolderType getModellingRulesNode() throws UaException;

  /** Asynchronous form of {@link #getModellingRulesNode()}. */
  CompletableFuture<? extends FolderType> getModellingRulesNodeAsync();

  /**
   * Resolves the optional MaxStringLength child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxStringLengthNode() throws UaException;

  /** Asynchronous form of {@link #getMaxStringLengthNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxStringLengthNodeAsync();

  /**
   * Reads the Value of the MaxStringLength child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxStringLength() throws UaException;

  /**
   * Writes the Value of the MaxStringLength child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxStringLength(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxStringLength()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxStringLengthAsync();

  /** Asynchronous form of {@link #writeMaxStringLength}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxStringLengthAsync(@Nullable UInteger value);

  /**
   * Resolves the optional OperationLimits child, a OperationLimitsType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.11">OperationLimitsType
   *     documentation</a>
   */
  @Nullable OperationLimitsType getOperationLimitsNode() throws UaException;

  /** Asynchronous form of {@link #getOperationLimitsNode()}. */
  CompletableFuture<? extends @Nullable OperationLimitsType> getOperationLimitsNodeAsync();

  /**
   * Resolves the optional ConformanceUnits child, a PropertyType with DataType QualifiedName.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getConformanceUnitsNode() throws UaException;

  /** Asynchronous form of {@link #getConformanceUnitsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getConformanceUnitsNodeAsync();

  /**
   * Reads the Value of the ConformanceUnits child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  QualifiedName @Nullable [] readConformanceUnits() throws UaException;

  /**
   * Writes the Value of the ConformanceUnits child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeConformanceUnits(QualifiedName @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readConformanceUnits()}. */
  CompletableFuture<? extends QualifiedName @Nullable []> readConformanceUnitsAsync();

  /** Asynchronous form of {@link #writeConformanceUnits}; completes with the operation status. */
  CompletableFuture<StatusCode> writeConformanceUnitsAsync(QualifiedName @Nullable [] value);

  /**
   * Resolves the optional MaxSubscriptions child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxSubscriptionsNode() throws UaException;

  /** Asynchronous form of {@link #getMaxSubscriptionsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxSubscriptionsNodeAsync();

  /**
   * Reads the Value of the MaxSubscriptions child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxSubscriptions() throws UaException;

  /**
   * Writes the Value of the MaxSubscriptions child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxSubscriptions(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxSubscriptions()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxSubscriptionsAsync();

  /** Asynchronous form of {@link #writeMaxSubscriptions}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxSubscriptionsAsync(@Nullable UInteger value);

  /**
   * Resolves the optional MaxMonitoredItems child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxMonitoredItemsNode() throws UaException;

  /** Asynchronous form of {@link #getMaxMonitoredItemsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxMonitoredItemsNodeAsync();

  /**
   * Reads the Value of the MaxMonitoredItems child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxMonitoredItems() throws UaException;

  /**
   * Writes the Value of the MaxMonitoredItems child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxMonitoredItems(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxMonitoredItems()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxMonitoredItemsAsync();

  /** Asynchronous form of {@link #writeMaxMonitoredItems}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxMonitoredItemsAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory AggregateFunctions child, a FolderType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.6">FolderType
   *     documentation</a>
   */
  FolderType getAggregateFunctionsNode() throws UaException;

  /** Asynchronous form of {@link #getAggregateFunctionsNode()}. */
  CompletableFuture<? extends FolderType> getAggregateFunctionsNodeAsync();

  /**
   * Resolves the mandatory ServerProfileArray child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getServerProfileArrayNode() throws UaException;

  /** Asynchronous form of {@link #getServerProfileArrayNode()}. */
  CompletableFuture<? extends PropertyType> getServerProfileArrayNodeAsync();

  /**
   * Reads the Value of the ServerProfileArray child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String @Nullable [] readServerProfileArray() throws UaException;

  /**
   * Writes the Value of the ServerProfileArray child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeServerProfileArray(@Nullable String @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readServerProfileArray()}. */
  CompletableFuture<? extends @Nullable String @Nullable []> readServerProfileArrayAsync();

  /** Asynchronous form of {@link #writeServerProfileArray}; completes with the operation status. */
  CompletableFuture<StatusCode> writeServerProfileArrayAsync(@Nullable String @Nullable [] value);

  /**
   * Resolves the optional MaxByteStringLength child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxByteStringLengthNode() throws UaException;

  /** Asynchronous form of {@link #getMaxByteStringLengthNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxByteStringLengthNodeAsync();

  /**
   * Reads the Value of the MaxByteStringLength child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxByteStringLength() throws UaException;

  /**
   * Writes the Value of the MaxByteStringLength child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxByteStringLength(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxByteStringLength()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxByteStringLengthAsync();

  /**
   * Asynchronous form of {@link #writeMaxByteStringLength}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeMaxByteStringLengthAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory SoftwareCertificates child, a PropertyType with DataType
   * SignedSoftwareCertificate.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSoftwareCertificatesNode() throws UaException;

  /** Asynchronous form of {@link #getSoftwareCertificatesNode()}. */
  CompletableFuture<? extends PropertyType> getSoftwareCertificatesNodeAsync();

  /**
   * Reads the Value of the SoftwareCertificates child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable SignedSoftwareCertificate @Nullable [] readSoftwareCertificates() throws UaException;

  /**
   * Writes the Value of the SoftwareCertificates child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSoftwareCertificates(@Nullable SignedSoftwareCertificate @Nullable [] value)
      throws UaException;

  /** Asynchronous form of {@link #readSoftwareCertificates()}. */
  CompletableFuture<? extends @Nullable SignedSoftwareCertificate @Nullable []>
      readSoftwareCertificatesAsync();

  /**
   * Asynchronous form of {@link #writeSoftwareCertificates}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeSoftwareCertificatesAsync(
      @Nullable SignedSoftwareCertificate @Nullable [] value);

  /**
   * Resolves the mandatory MinSupportedSampleRate child, a PropertyType with DataType Duration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMinSupportedSampleRateNode() throws UaException;

  /** Asynchronous form of {@link #getMinSupportedSampleRateNode()}. */
  CompletableFuture<? extends PropertyType> getMinSupportedSampleRateNodeAsync();

  /**
   * Reads the Value of the MinSupportedSampleRate child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readMinSupportedSampleRate() throws UaException;

  /**
   * Writes the Value of the MinSupportedSampleRate child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMinSupportedSampleRate(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readMinSupportedSampleRate()}. */
  CompletableFuture<? extends @Nullable Double> readMinSupportedSampleRateAsync();

  /**
   * Asynchronous form of {@link #writeMinSupportedSampleRate}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeMinSupportedSampleRateAsync(@Nullable Double value);

  /**
   * Resolves the optional MaxWhereClauseParameters child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxWhereClauseParametersNode() throws UaException;

  /** Asynchronous form of {@link #getMaxWhereClauseParametersNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxWhereClauseParametersNodeAsync();

  /**
   * Reads the Value of the MaxWhereClauseParameters child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxWhereClauseParameters() throws UaException;

  /**
   * Writes the Value of the MaxWhereClauseParameters child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxWhereClauseParameters(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxWhereClauseParameters()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxWhereClauseParametersAsync();

  /**
   * Asynchronous form of {@link #writeMaxWhereClauseParameters}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeMaxWhereClauseParametersAsync(@Nullable UInteger value);

  /**
   * Resolves the optional MaxSelectClauseParameters child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxSelectClauseParametersNode() throws UaException;

  /** Asynchronous form of {@link #getMaxSelectClauseParametersNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxSelectClauseParametersNodeAsync();

  /**
   * Reads the Value of the MaxSelectClauseParameters child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxSelectClauseParameters() throws UaException;

  /**
   * Writes the Value of the MaxSelectClauseParameters child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxSelectClauseParameters(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxSelectClauseParameters()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxSelectClauseParametersAsync();

  /**
   * Asynchronous form of {@link #writeMaxSelectClauseParameters}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeMaxSelectClauseParametersAsync(@Nullable UInteger value);

  /**
   * Resolves the optional MaxMonitoredItemsQueueSize child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxMonitoredItemsQueueSizeNode() throws UaException;

  /** Asynchronous form of {@link #getMaxMonitoredItemsQueueSizeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxMonitoredItemsQueueSizeNodeAsync();

  /**
   * Reads the Value of the MaxMonitoredItemsQueueSize child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxMonitoredItemsQueueSize() throws UaException;

  /**
   * Writes the Value of the MaxMonitoredItemsQueueSize child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxMonitoredItemsQueueSize(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxMonitoredItemsQueueSize()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxMonitoredItemsQueueSizeAsync();

  /**
   * Asynchronous form of {@link #writeMaxMonitoredItemsQueueSize}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeMaxMonitoredItemsQueueSizeAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory MaxQueryContinuationPoints child, a PropertyType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMaxQueryContinuationPointsNode() throws UaException;

  /** Asynchronous form of {@link #getMaxQueryContinuationPointsNode()}. */
  CompletableFuture<? extends PropertyType> getMaxQueryContinuationPointsNodeAsync();

  /**
   * Reads the Value of the MaxQueryContinuationPoints child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readMaxQueryContinuationPoints() throws UaException;

  /**
   * Writes the Value of the MaxQueryContinuationPoints child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxQueryContinuationPoints(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readMaxQueryContinuationPoints()}. */
  CompletableFuture<? extends @Nullable UShort> readMaxQueryContinuationPointsAsync();

  /**
   * Asynchronous form of {@link #writeMaxQueryContinuationPoints}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeMaxQueryContinuationPointsAsync(@Nullable UShort value);

  /**
   * Resolves the optional MaxSubscriptionsPerSession child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxSubscriptionsPerSessionNode() throws UaException;

  /** Asynchronous form of {@link #getMaxSubscriptionsPerSessionNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxSubscriptionsPerSessionNodeAsync();

  /**
   * Reads the Value of the MaxSubscriptionsPerSession child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxSubscriptionsPerSession() throws UaException;

  /**
   * Writes the Value of the MaxSubscriptionsPerSession child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxSubscriptionsPerSession(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxSubscriptionsPerSession()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxSubscriptionsPerSessionAsync();

  /**
   * Asynchronous form of {@link #writeMaxSubscriptionsPerSession}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeMaxSubscriptionsPerSessionAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory MaxBrowseContinuationPoints child, a PropertyType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMaxBrowseContinuationPointsNode() throws UaException;

  /** Asynchronous form of {@link #getMaxBrowseContinuationPointsNode()}. */
  CompletableFuture<? extends PropertyType> getMaxBrowseContinuationPointsNodeAsync();

  /**
   * Reads the Value of the MaxBrowseContinuationPoints child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readMaxBrowseContinuationPoints() throws UaException;

  /**
   * Writes the Value of the MaxBrowseContinuationPoints child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxBrowseContinuationPoints(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readMaxBrowseContinuationPoints()}. */
  CompletableFuture<? extends @Nullable UShort> readMaxBrowseContinuationPointsAsync();

  /**
   * Asynchronous form of {@link #writeMaxBrowseContinuationPoints}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeMaxBrowseContinuationPointsAsync(@Nullable UShort value);

  /**
   * Resolves the mandatory MaxHistoryContinuationPoints child, a PropertyType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMaxHistoryContinuationPointsNode() throws UaException;

  /** Asynchronous form of {@link #getMaxHistoryContinuationPointsNode()}. */
  CompletableFuture<? extends PropertyType> getMaxHistoryContinuationPointsNodeAsync();

  /**
   * Reads the Value of the MaxHistoryContinuationPoints child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readMaxHistoryContinuationPoints() throws UaException;

  /**
   * Writes the Value of the MaxHistoryContinuationPoints child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxHistoryContinuationPoints(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readMaxHistoryContinuationPoints()}. */
  CompletableFuture<? extends @Nullable UShort> readMaxHistoryContinuationPointsAsync();

  /**
   * Asynchronous form of {@link #writeMaxHistoryContinuationPoints}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeMaxHistoryContinuationPointsAsync(@Nullable UShort value);

  /**
   * Resolves the optional MaxLogObjectContinuationPoints child, a PropertyType with DataType
   * UInt16.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxLogObjectContinuationPointsNode() throws UaException;

  /** Asynchronous form of {@link #getMaxLogObjectContinuationPointsNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxLogObjectContinuationPointsNodeAsync();

  /**
   * Reads the Value of the MaxLogObjectContinuationPoints child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readMaxLogObjectContinuationPoints() throws UaException;

  /**
   * Writes the Value of the MaxLogObjectContinuationPoints child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxLogObjectContinuationPoints(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readMaxLogObjectContinuationPoints()}. */
  CompletableFuture<? extends @Nullable UShort> readMaxLogObjectContinuationPointsAsync();

  /**
   * Asynchronous form of {@link #writeMaxLogObjectContinuationPoints}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeMaxLogObjectContinuationPointsAsync(@Nullable UShort value);

  /**
   * Resolves the optional MaxMonitoredItemsPerSubscription child, a PropertyType with DataType
   * UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxMonitoredItemsPerSubscriptionNode() throws UaException;

  /** Asynchronous form of {@link #getMaxMonitoredItemsPerSubscriptionNode()}. */
  CompletableFuture<? extends @Nullable PropertyType>
      getMaxMonitoredItemsPerSubscriptionNodeAsync();

  /**
   * Reads the Value of the MaxMonitoredItemsPerSubscription child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxMonitoredItemsPerSubscription() throws UaException;

  /**
   * Writes the Value of the MaxMonitoredItemsPerSubscription child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxMonitoredItemsPerSubscription(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxMonitoredItemsPerSubscription()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxMonitoredItemsPerSubscriptionAsync();

  /**
   * Asynchronous form of {@link #writeMaxMonitoredItemsPerSubscription}; completes with the
   * operation status.
   */
  CompletableFuture<StatusCode> writeMaxMonitoredItemsPerSubscriptionAsync(
      @Nullable UInteger value);

  /**
   * Resolves the optional RoleSet child, a RoleSetType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.1">RoleSetType
   *     documentation</a>
   */
  @Nullable RoleSetType getRoleSetNode() throws UaException;

  /** Asynchronous form of {@link #getRoleSetNode()}. */
  CompletableFuture<? extends @Nullable RoleSetType> getRoleSetNodeAsync();
}
