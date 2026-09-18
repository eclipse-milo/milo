package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.NumberRange;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the BaseAnalogType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.2/#5.3.2.2">Model
 *     documentation</a>
 */
public interface BaseAnalogType extends DataItemType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15318L);

  QualifiedProperty<NumberRange> EUNumberRange_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EUNumberRange",
          ExpandedNodeId.of(Namespaces.OPC_UA, 23903L),
          -1,
          NumberRange.class);

  QualifiedProperty<Range> InstrumentRange_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "InstrumentRange",
          ExpandedNodeId.of(Namespaces.OPC_UA, 884L),
          -1,
          Range.class);

  QualifiedProperty<EUInformation> EngineeringUnits__PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EngineeringUnits",
          ExpandedNodeId.of(Namespaces.OPC_UA, 887L),
          -1,
          EUInformation.class);

  QualifiedProperty<NumberRange> InstrumentNumberRange_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "InstrumentNumberRange",
          ExpandedNodeId.of(Namespaces.OPC_UA, 23903L),
          -1,
          NumberRange.class);

  QualifiedProperty<Range> EURange_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EURange",
          ExpandedNodeId.of(Namespaces.OPC_UA, 884L),
          -1,
          Range.class);

  /**
   * Resolves the optional EUNumberRange child, a PropertyType with DataType NumberRange.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getEUNumberRangeNode() throws UaException;

  /** Asynchronous form of {@link #getEUNumberRangeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getEUNumberRangeNodeAsync();

  /**
   * Reads the Value of the EUNumberRange child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NumberRange readEUNumberRange() throws UaException;

  /**
   * Writes the Value of the EUNumberRange child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEUNumberRange(@Nullable NumberRange value) throws UaException;

  /** Asynchronous form of {@link #readEUNumberRange()}. */
  CompletableFuture<? extends @Nullable NumberRange> readEUNumberRangeAsync();

  /** Asynchronous form of {@link #writeEUNumberRange}; completes with the operation status. */
  CompletableFuture<StatusCode> writeEUNumberRangeAsync(@Nullable NumberRange value);

  /**
   * Resolves the optional InstrumentRange child, a PropertyType with DataType Range.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getInstrumentRangeNode() throws UaException;

  /** Asynchronous form of {@link #getInstrumentRangeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getInstrumentRangeNodeAsync();

  /**
   * Reads the Value of the InstrumentRange child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Range readInstrumentRange() throws UaException;

  /**
   * Writes the Value of the InstrumentRange child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeInstrumentRange(@Nullable Range value) throws UaException;

  /** Asynchronous form of {@link #readInstrumentRange()}. */
  CompletableFuture<? extends @Nullable Range> readInstrumentRangeAsync();

  /** Asynchronous form of {@link #writeInstrumentRange}; completes with the operation status. */
  CompletableFuture<StatusCode> writeInstrumentRangeAsync(@Nullable Range value);

  /**
   * Resolves the optional EngineeringUnits child, a PropertyType with DataType EUInformation.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getEngineeringUnits_Node() throws UaException;

  /** Asynchronous form of {@link #getEngineeringUnits_Node()}. */
  CompletableFuture<? extends @Nullable PropertyType> getEngineeringUnits_NodeAsync();

  /**
   * Reads the Value of the EngineeringUnits child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable EUInformation readEngineeringUnits_() throws UaException;

  /**
   * Writes the Value of the EngineeringUnits child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEngineeringUnits_(@Nullable EUInformation value) throws UaException;

  /** Asynchronous form of {@link #readEngineeringUnits_()}. */
  CompletableFuture<? extends @Nullable EUInformation> readEngineeringUnits_Async();

  /** Asynchronous form of {@link #writeEngineeringUnits_}; completes with the operation status. */
  CompletableFuture<StatusCode> writeEngineeringUnits_Async(@Nullable EUInformation value);

  /**
   * Resolves the optional InstrumentNumberRange child, a PropertyType with DataType NumberRange.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getInstrumentNumberRangeNode() throws UaException;

  /** Asynchronous form of {@link #getInstrumentNumberRangeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getInstrumentNumberRangeNodeAsync();

  /**
   * Reads the Value of the InstrumentNumberRange child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NumberRange readInstrumentNumberRange() throws UaException;

  /**
   * Writes the Value of the InstrumentNumberRange child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeInstrumentNumberRange(@Nullable NumberRange value) throws UaException;

  /** Asynchronous form of {@link #readInstrumentNumberRange()}. */
  CompletableFuture<? extends @Nullable NumberRange> readInstrumentNumberRangeAsync();

  /**
   * Asynchronous form of {@link #writeInstrumentNumberRange}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeInstrumentNumberRangeAsync(@Nullable NumberRange value);

  /**
   * Resolves the optional EURange child, a PropertyType with DataType Range.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getEURangeNode() throws UaException;

  /** Asynchronous form of {@link #getEURangeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getEURangeNodeAsync();

  /**
   * Reads the Value of the EURange child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Range readEURange() throws UaException;

  /**
   * Writes the Value of the EURange child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEURange(@Nullable Range value) throws UaException;

  /** Asynchronous form of {@link #readEURange()}. */
  CompletableFuture<? extends @Nullable Range> readEURangeAsync();

  /** Asynchronous form of {@link #writeEURange}; completes with the operation status. */
  CompletableFuture<StatusCode> writeEURangeAsync(@Nullable Range value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Variant readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable Variant value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable Variant> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable Variant value);
}
