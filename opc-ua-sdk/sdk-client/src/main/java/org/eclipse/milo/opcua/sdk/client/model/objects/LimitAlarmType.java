package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the LimitAlarmType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.18">Model
 *     documentation</a>
 */
public interface LimitAlarmType extends AlarmConditionType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2955L);

  QualifiedProperty<Double> LowDeadband_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LowDeadband",
          ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
          -1,
          Double.class);

  QualifiedProperty<Double> LowLowLimit_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LowLowLimit",
          ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
          -1,
          Double.class);

  QualifiedProperty<UShort> SeverityLow_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SeverityLow",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  QualifiedProperty<Double> BaseLowLimit_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "BaseLowLimit",
          ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
          -1,
          Double.class);

  QualifiedProperty<Double> HighDeadband_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "HighDeadband",
          ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
          -1,
          Double.class);

  QualifiedProperty<UShort> SeverityHigh_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SeverityHigh",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  QualifiedProperty<Double> BaseHighLimit_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "BaseHighLimit",
          ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
          -1,
          Double.class);

  QualifiedProperty<Double> HighHighLimit_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "HighHighLimit",
          ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
          -1,
          Double.class);

  QualifiedProperty<Double> LowLowDeadband_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LowLowDeadband",
          ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
          -1,
          Double.class);

  QualifiedProperty<UShort> SeverityLowLow_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SeverityLowLow",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  QualifiedProperty<Double> BaseLowLowLimit_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "BaseLowLowLimit",
          ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
          -1,
          Double.class);

  QualifiedProperty<Double> HighHighDeadband_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "HighHighDeadband",
          ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
          -1,
          Double.class);

  QualifiedProperty<UShort> SeverityHighHigh_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SeverityHighHigh",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  QualifiedProperty<Double> BaseHighHighLimit_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "BaseHighHighLimit",
          ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
          -1,
          Double.class);

  QualifiedProperty<Double> LowLimit_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "LowLimit",
          ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
          -1,
          Double.class);

  QualifiedProperty<Double> HighLimit_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "HighLimit",
          ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
          -1,
          Double.class);

  /**
   * Resolves the optional LowDeadband child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getLowDeadbandNode() throws UaException;

  /** Asynchronous form of {@link #getLowDeadbandNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getLowDeadbandNodeAsync();

  /**
   * Reads the Value of the LowDeadband child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readLowDeadband() throws UaException;

  /**
   * Writes the Value of the LowDeadband child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLowDeadband(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readLowDeadband()}. */
  CompletableFuture<? extends @Nullable Double> readLowDeadbandAsync();

  /** Asynchronous form of {@link #writeLowDeadband}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLowDeadbandAsync(@Nullable Double value);

  /**
   * Resolves the optional LowLowLimit child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getLowLowLimitNode() throws UaException;

  /** Asynchronous form of {@link #getLowLowLimitNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getLowLowLimitNodeAsync();

  /**
   * Reads the Value of the LowLowLimit child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readLowLowLimit() throws UaException;

  /**
   * Writes the Value of the LowLowLimit child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLowLowLimit(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readLowLowLimit()}. */
  CompletableFuture<? extends @Nullable Double> readLowLowLimitAsync();

  /** Asynchronous form of {@link #writeLowLowLimit}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLowLowLimitAsync(@Nullable Double value);

  /**
   * Resolves the optional SeverityLow child, a PropertyType with DataType UInt16.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getSeverityLowNode() throws UaException;

  /** Asynchronous form of {@link #getSeverityLowNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getSeverityLowNodeAsync();

  /**
   * Reads the Value of the SeverityLow child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readSeverityLow() throws UaException;

  /**
   * Writes the Value of the SeverityLow child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSeverityLow(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readSeverityLow()}. */
  CompletableFuture<? extends @Nullable UShort> readSeverityLowAsync();

  /** Asynchronous form of {@link #writeSeverityLow}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSeverityLowAsync(@Nullable UShort value);

  /**
   * Resolves the optional BaseLowLimit child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getBaseLowLimitNode() throws UaException;

  /** Asynchronous form of {@link #getBaseLowLimitNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getBaseLowLimitNodeAsync();

  /**
   * Reads the Value of the BaseLowLimit child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readBaseLowLimit() throws UaException;

  /**
   * Writes the Value of the BaseLowLimit child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeBaseLowLimit(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readBaseLowLimit()}. */
  CompletableFuture<? extends @Nullable Double> readBaseLowLimitAsync();

  /** Asynchronous form of {@link #writeBaseLowLimit}; completes with the operation status. */
  CompletableFuture<StatusCode> writeBaseLowLimitAsync(@Nullable Double value);

  /**
   * Resolves the optional HighDeadband child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getHighDeadbandNode() throws UaException;

  /** Asynchronous form of {@link #getHighDeadbandNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getHighDeadbandNodeAsync();

  /**
   * Reads the Value of the HighDeadband child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readHighDeadband() throws UaException;

  /**
   * Writes the Value of the HighDeadband child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeHighDeadband(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readHighDeadband()}. */
  CompletableFuture<? extends @Nullable Double> readHighDeadbandAsync();

  /** Asynchronous form of {@link #writeHighDeadband}; completes with the operation status. */
  CompletableFuture<StatusCode> writeHighDeadbandAsync(@Nullable Double value);

  /**
   * Resolves the optional SeverityHigh child, a PropertyType with DataType UInt16.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getSeverityHighNode() throws UaException;

  /** Asynchronous form of {@link #getSeverityHighNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getSeverityHighNodeAsync();

  /**
   * Reads the Value of the SeverityHigh child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readSeverityHigh() throws UaException;

  /**
   * Writes the Value of the SeverityHigh child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSeverityHigh(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readSeverityHigh()}. */
  CompletableFuture<? extends @Nullable UShort> readSeverityHighAsync();

  /** Asynchronous form of {@link #writeSeverityHigh}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSeverityHighAsync(@Nullable UShort value);

  /**
   * Resolves the optional BaseHighLimit child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getBaseHighLimitNode() throws UaException;

  /** Asynchronous form of {@link #getBaseHighLimitNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getBaseHighLimitNodeAsync();

  /**
   * Reads the Value of the BaseHighLimit child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readBaseHighLimit() throws UaException;

  /**
   * Writes the Value of the BaseHighLimit child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeBaseHighLimit(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readBaseHighLimit()}. */
  CompletableFuture<? extends @Nullable Double> readBaseHighLimitAsync();

  /** Asynchronous form of {@link #writeBaseHighLimit}; completes with the operation status. */
  CompletableFuture<StatusCode> writeBaseHighLimitAsync(@Nullable Double value);

  /**
   * Resolves the optional HighHighLimit child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getHighHighLimitNode() throws UaException;

  /** Asynchronous form of {@link #getHighHighLimitNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getHighHighLimitNodeAsync();

  /**
   * Reads the Value of the HighHighLimit child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readHighHighLimit() throws UaException;

  /**
   * Writes the Value of the HighHighLimit child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeHighHighLimit(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readHighHighLimit()}. */
  CompletableFuture<? extends @Nullable Double> readHighHighLimitAsync();

  /** Asynchronous form of {@link #writeHighHighLimit}; completes with the operation status. */
  CompletableFuture<StatusCode> writeHighHighLimitAsync(@Nullable Double value);

  /**
   * Resolves the optional LowLowDeadband child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getLowLowDeadbandNode() throws UaException;

  /** Asynchronous form of {@link #getLowLowDeadbandNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getLowLowDeadbandNodeAsync();

  /**
   * Reads the Value of the LowLowDeadband child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readLowLowDeadband() throws UaException;

  /**
   * Writes the Value of the LowLowDeadband child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLowLowDeadband(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readLowLowDeadband()}. */
  CompletableFuture<? extends @Nullable Double> readLowLowDeadbandAsync();

  /** Asynchronous form of {@link #writeLowLowDeadband}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLowLowDeadbandAsync(@Nullable Double value);

  /**
   * Resolves the optional SeverityLowLow child, a PropertyType with DataType UInt16.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getSeverityLowLowNode() throws UaException;

  /** Asynchronous form of {@link #getSeverityLowLowNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getSeverityLowLowNodeAsync();

  /**
   * Reads the Value of the SeverityLowLow child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readSeverityLowLow() throws UaException;

  /**
   * Writes the Value of the SeverityLowLow child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSeverityLowLow(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readSeverityLowLow()}. */
  CompletableFuture<? extends @Nullable UShort> readSeverityLowLowAsync();

  /** Asynchronous form of {@link #writeSeverityLowLow}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSeverityLowLowAsync(@Nullable UShort value);

  /**
   * Resolves the optional BaseLowLowLimit child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getBaseLowLowLimitNode() throws UaException;

  /** Asynchronous form of {@link #getBaseLowLowLimitNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getBaseLowLowLimitNodeAsync();

  /**
   * Reads the Value of the BaseLowLowLimit child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readBaseLowLowLimit() throws UaException;

  /**
   * Writes the Value of the BaseLowLowLimit child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeBaseLowLowLimit(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readBaseLowLowLimit()}. */
  CompletableFuture<? extends @Nullable Double> readBaseLowLowLimitAsync();

  /** Asynchronous form of {@link #writeBaseLowLowLimit}; completes with the operation status. */
  CompletableFuture<StatusCode> writeBaseLowLowLimitAsync(@Nullable Double value);

  /**
   * Resolves the optional HighHighDeadband child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getHighHighDeadbandNode() throws UaException;

  /** Asynchronous form of {@link #getHighHighDeadbandNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getHighHighDeadbandNodeAsync();

  /**
   * Reads the Value of the HighHighDeadband child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readHighHighDeadband() throws UaException;

  /**
   * Writes the Value of the HighHighDeadband child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeHighHighDeadband(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readHighHighDeadband()}. */
  CompletableFuture<? extends @Nullable Double> readHighHighDeadbandAsync();

  /** Asynchronous form of {@link #writeHighHighDeadband}; completes with the operation status. */
  CompletableFuture<StatusCode> writeHighHighDeadbandAsync(@Nullable Double value);

  /**
   * Resolves the optional SeverityHighHigh child, a PropertyType with DataType UInt16.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getSeverityHighHighNode() throws UaException;

  /** Asynchronous form of {@link #getSeverityHighHighNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getSeverityHighHighNodeAsync();

  /**
   * Reads the Value of the SeverityHighHigh child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readSeverityHighHigh() throws UaException;

  /**
   * Writes the Value of the SeverityHighHigh child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSeverityHighHigh(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readSeverityHighHigh()}. */
  CompletableFuture<? extends @Nullable UShort> readSeverityHighHighAsync();

  /** Asynchronous form of {@link #writeSeverityHighHigh}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSeverityHighHighAsync(@Nullable UShort value);

  /**
   * Resolves the optional BaseHighHighLimit child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getBaseHighHighLimitNode() throws UaException;

  /** Asynchronous form of {@link #getBaseHighHighLimitNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getBaseHighHighLimitNodeAsync();

  /**
   * Reads the Value of the BaseHighHighLimit child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readBaseHighHighLimit() throws UaException;

  /**
   * Writes the Value of the BaseHighHighLimit child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeBaseHighHighLimit(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readBaseHighHighLimit()}. */
  CompletableFuture<? extends @Nullable Double> readBaseHighHighLimitAsync();

  /** Asynchronous form of {@link #writeBaseHighHighLimit}; completes with the operation status. */
  CompletableFuture<StatusCode> writeBaseHighHighLimitAsync(@Nullable Double value);

  /**
   * Resolves the optional LowLimit child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getLowLimitNode() throws UaException;

  /** Asynchronous form of {@link #getLowLimitNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getLowLimitNodeAsync();

  /**
   * Reads the Value of the LowLimit child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readLowLimit() throws UaException;

  /**
   * Writes the Value of the LowLimit child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeLowLimit(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readLowLimit()}. */
  CompletableFuture<? extends @Nullable Double> readLowLimitAsync();

  /** Asynchronous form of {@link #writeLowLimit}; completes with the operation status. */
  CompletableFuture<StatusCode> writeLowLimitAsync(@Nullable Double value);

  /**
   * Resolves the optional HighLimit child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getHighLimitNode() throws UaException;

  /** Asynchronous form of {@link #getHighLimitNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getHighLimitNodeAsync();

  /**
   * Reads the Value of the HighLimit child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readHighLimit() throws UaException;

  /**
   * Writes the Value of the HighLimit child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeHighLimit(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readHighLimit()}. */
  CompletableFuture<? extends @Nullable Double> readHighLimitAsync();

  /** Asynchronous form of {@link #writeHighLimit}; completes with the operation status. */
  CompletableFuture<StatusCode> writeHighLimitAsync(@Nullable Double value);
}
