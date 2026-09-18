package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ExceptionDeviationFormat;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the HistoricalDataConfigurationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.2.2">Model
 *     documentation</a>
 */
public interface HistoricalDataConfigurationType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2318L);

  QualifiedProperty<String> Definition_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Definition",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<DateTime> StartOfArchive_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "StartOfArchive",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          -1,
          DateTime.class);

  QualifiedProperty<Double> MaxTimeInterval_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxTimeInterval",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<Double> MinTimeInterval_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MinTimeInterval",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<Double> ExceptionDeviation_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ExceptionDeviation",
          ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
          -1,
          Double.class);

  QualifiedProperty<Double> MaxTimeStoredValues_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxTimeStoredValues",
          ExpandedNodeId.of(Namespaces.OPC_UA, 290L),
          -1,
          Double.class);

  QualifiedProperty<UInteger> MaxCountStoredValues_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "MaxCountStoredValues",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<DateTime> StartOfOnlineArchive_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "StartOfOnlineArchive",
          ExpandedNodeId.of(Namespaces.OPC_UA, 294L),
          -1,
          DateTime.class);

  QualifiedProperty<ExceptionDeviationFormat> ExceptionDeviationFormat_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ExceptionDeviationFormat",
          ExpandedNodeId.of(Namespaces.OPC_UA, 890L),
          -1,
          ExceptionDeviationFormat.class);

  QualifiedProperty<Boolean> ServerTimestampSupported_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ServerTimestampSupported",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> Stepped_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Stepped",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  /**
   * Resolves the optional Definition child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getDefinitionNode() throws UaException;

  /** Asynchronous form of {@link #getDefinitionNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getDefinitionNodeAsync();

  /**
   * Reads the Value of the Definition child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readDefinition() throws UaException;

  /**
   * Writes the Value of the Definition child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDefinition(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readDefinition()}. */
  CompletableFuture<? extends @Nullable String> readDefinitionAsync();

  /** Asynchronous form of {@link #writeDefinition}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDefinitionAsync(@Nullable String value);

  /**
   * Resolves the optional StartOfArchive child, a PropertyType with DataType UtcTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getStartOfArchiveNode() throws UaException;

  /** Asynchronous form of {@link #getStartOfArchiveNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getStartOfArchiveNodeAsync();

  /**
   * Reads the Value of the StartOfArchive child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readStartOfArchive() throws UaException;

  /**
   * Writes the Value of the StartOfArchive child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeStartOfArchive(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readStartOfArchive()}. */
  CompletableFuture<? extends @Nullable DateTime> readStartOfArchiveAsync();

  /** Asynchronous form of {@link #writeStartOfArchive}; completes with the operation status. */
  CompletableFuture<StatusCode> writeStartOfArchiveAsync(@Nullable DateTime value);

  /**
   * Resolves the optional MaxTimeInterval child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxTimeIntervalNode() throws UaException;

  /** Asynchronous form of {@link #getMaxTimeIntervalNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxTimeIntervalNodeAsync();

  /**
   * Reads the Value of the MaxTimeInterval child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readMaxTimeInterval() throws UaException;

  /**
   * Writes the Value of the MaxTimeInterval child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxTimeInterval(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readMaxTimeInterval()}. */
  CompletableFuture<? extends @Nullable Double> readMaxTimeIntervalAsync();

  /** Asynchronous form of {@link #writeMaxTimeInterval}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaxTimeIntervalAsync(@Nullable Double value);

  /**
   * Resolves the optional MinTimeInterval child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMinTimeIntervalNode() throws UaException;

  /** Asynchronous form of {@link #getMinTimeIntervalNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMinTimeIntervalNodeAsync();

  /**
   * Reads the Value of the MinTimeInterval child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readMinTimeInterval() throws UaException;

  /**
   * Writes the Value of the MinTimeInterval child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMinTimeInterval(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readMinTimeInterval()}. */
  CompletableFuture<? extends @Nullable Double> readMinTimeIntervalAsync();

  /** Asynchronous form of {@link #writeMinTimeInterval}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMinTimeIntervalAsync(@Nullable Double value);

  /**
   * Resolves the optional AggregateFunctions child, a FolderType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.6">FolderType
   *     documentation</a>
   */
  @Nullable FolderType getAggregateFunctionsNode() throws UaException;

  /** Asynchronous form of {@link #getAggregateFunctionsNode()}. */
  CompletableFuture<? extends @Nullable FolderType> getAggregateFunctionsNodeAsync();

  /**
   * Resolves the optional ExceptionDeviation child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getExceptionDeviationNode() throws UaException;

  /** Asynchronous form of {@link #getExceptionDeviationNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getExceptionDeviationNodeAsync();

  /**
   * Reads the Value of the ExceptionDeviation child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readExceptionDeviation() throws UaException;

  /**
   * Writes the Value of the ExceptionDeviation child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeExceptionDeviation(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readExceptionDeviation()}. */
  CompletableFuture<? extends @Nullable Double> readExceptionDeviationAsync();

  /** Asynchronous form of {@link #writeExceptionDeviation}; completes with the operation status. */
  CompletableFuture<StatusCode> writeExceptionDeviationAsync(@Nullable Double value);

  /**
   * Resolves the optional MaxTimeStoredValues child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxTimeStoredValuesNode() throws UaException;

  /** Asynchronous form of {@link #getMaxTimeStoredValuesNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxTimeStoredValuesNodeAsync();

  /**
   * Reads the Value of the MaxTimeStoredValues child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readMaxTimeStoredValues() throws UaException;

  /**
   * Writes the Value of the MaxTimeStoredValues child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxTimeStoredValues(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readMaxTimeStoredValues()}. */
  CompletableFuture<? extends @Nullable Double> readMaxTimeStoredValuesAsync();

  /**
   * Asynchronous form of {@link #writeMaxTimeStoredValues}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeMaxTimeStoredValuesAsync(@Nullable Double value);

  /**
   * Resolves the optional MaxCountStoredValues child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getMaxCountStoredValuesNode() throws UaException;

  /** Asynchronous form of {@link #getMaxCountStoredValuesNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getMaxCountStoredValuesNodeAsync();

  /**
   * Reads the Value of the MaxCountStoredValues child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaxCountStoredValues() throws UaException;

  /**
   * Writes the Value of the MaxCountStoredValues child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaxCountStoredValues(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaxCountStoredValues()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaxCountStoredValuesAsync();

  /**
   * Asynchronous form of {@link #writeMaxCountStoredValues}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeMaxCountStoredValuesAsync(@Nullable UInteger value);

  /**
   * Resolves the optional StartOfOnlineArchive child, a PropertyType with DataType UtcTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getStartOfOnlineArchiveNode() throws UaException;

  /** Asynchronous form of {@link #getStartOfOnlineArchiveNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getStartOfOnlineArchiveNodeAsync();

  /**
   * Reads the Value of the StartOfOnlineArchive child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readStartOfOnlineArchive() throws UaException;

  /**
   * Writes the Value of the StartOfOnlineArchive child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeStartOfOnlineArchive(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readStartOfOnlineArchive()}. */
  CompletableFuture<? extends @Nullable DateTime> readStartOfOnlineArchiveAsync();

  /**
   * Asynchronous form of {@link #writeStartOfOnlineArchive}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeStartOfOnlineArchiveAsync(@Nullable DateTime value);

  /**
   * Resolves the mandatory AggregateConfiguration child, a AggregateConfigurationType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part13/4.2.1/#4.2.1.2">AggregateConfigurationType
   *     documentation</a>
   */
  AggregateConfigurationType getAggregateConfigurationNode() throws UaException;

  /** Asynchronous form of {@link #getAggregateConfigurationNode()}. */
  CompletableFuture<? extends AggregateConfigurationType> getAggregateConfigurationNodeAsync();

  /**
   * Resolves the optional ExceptionDeviationFormat child, a PropertyType with DataType
   * ExceptionDeviationFormat.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getExceptionDeviationFormatNode() throws UaException;

  /** Asynchronous form of {@link #getExceptionDeviationFormatNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getExceptionDeviationFormatNodeAsync();

  /**
   * Reads the Value of the ExceptionDeviationFormat child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ExceptionDeviationFormat readExceptionDeviationFormat() throws UaException;

  /**
   * Writes the Value of the ExceptionDeviationFormat child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeExceptionDeviationFormat(@Nullable ExceptionDeviationFormat value) throws UaException;

  /** Asynchronous form of {@link #readExceptionDeviationFormat()}. */
  CompletableFuture<? extends @Nullable ExceptionDeviationFormat>
      readExceptionDeviationFormatAsync();

  /**
   * Asynchronous form of {@link #writeExceptionDeviationFormat}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeExceptionDeviationFormatAsync(
      @Nullable ExceptionDeviationFormat value);

  /**
   * Resolves the optional ServerTimestampSupported child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getServerTimestampSupportedNode() throws UaException;

  /** Asynchronous form of {@link #getServerTimestampSupportedNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getServerTimestampSupportedNodeAsync();

  /**
   * Reads the Value of the ServerTimestampSupported child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readServerTimestampSupported() throws UaException;

  /**
   * Writes the Value of the ServerTimestampSupported child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeServerTimestampSupported(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readServerTimestampSupported()}. */
  CompletableFuture<? extends @Nullable Boolean> readServerTimestampSupportedAsync();

  /**
   * Asynchronous form of {@link #writeServerTimestampSupported}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeServerTimestampSupportedAsync(@Nullable Boolean value);

  /**
   * Resolves the mandatory Stepped child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSteppedNode() throws UaException;

  /** Asynchronous form of {@link #getSteppedNode()}. */
  CompletableFuture<? extends PropertyType> getSteppedNodeAsync();

  /**
   * Reads the Value of the Stepped child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readStepped() throws UaException;

  /**
   * Writes the Value of the Stepped child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeStepped(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readStepped()}. */
  CompletableFuture<? extends @Nullable Boolean> readSteppedAsync();

  /** Asynchronous form of {@link #writeStepped}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSteppedAsync(@Nullable Boolean value);
}
