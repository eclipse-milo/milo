package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.AxisScaleEnumeration;
import org.eclipse.milo.opcua.stack.core.types.structured.EUInformation;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ArrayItemType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.4/#5.3.4.1">Model
 *     documentation</a>
 */
public interface ArrayItemType extends DataItemType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 12021L);

  QualifiedProperty<AxisScaleEnumeration> AxisScaleType_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "AxisScaleType",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12077L),
          -1,
          AxisScaleEnumeration.class);

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

  QualifiedProperty<LocalizedText> Title_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Title",
          ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
          -1,
          LocalizedText.class);

  QualifiedProperty<Range> EURange_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EURange",
          ExpandedNodeId.of(Namespaces.OPC_UA, 884L),
          -1,
          Range.class);

  /**
   * Resolves the mandatory AxisScaleType child, a PropertyType with DataType AxisScaleEnumeration.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getAxisScaleTypeNode() throws UaException;

  /** Asynchronous form of {@link #getAxisScaleTypeNode()}. */
  CompletableFuture<? extends PropertyType> getAxisScaleTypeNodeAsync();

  /**
   * Reads the Value of the AxisScaleType child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable AxisScaleEnumeration readAxisScaleType() throws UaException;

  /**
   * Writes the Value of the AxisScaleType child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAxisScaleType(@Nullable AxisScaleEnumeration value) throws UaException;

  /** Asynchronous form of {@link #readAxisScaleType()}. */
  CompletableFuture<? extends @Nullable AxisScaleEnumeration> readAxisScaleTypeAsync();

  /** Asynchronous form of {@link #writeAxisScaleType}; completes with the operation status. */
  CompletableFuture<StatusCode> writeAxisScaleTypeAsync(@Nullable AxisScaleEnumeration value);

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
   * Resolves the mandatory EngineeringUnits child, a PropertyType with DataType EUInformation.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getEngineeringUnits_Node() throws UaException;

  /** Asynchronous form of {@link #getEngineeringUnits_Node()}. */
  CompletableFuture<? extends PropertyType> getEngineeringUnits_NodeAsync();

  /**
   * Reads the Value of the EngineeringUnits child from the server.
   *
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
   * Resolves the mandatory Title child, a PropertyType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getTitleNode() throws UaException;

  /** Asynchronous form of {@link #getTitleNode()}. */
  CompletableFuture<? extends PropertyType> getTitleNodeAsync();

  /**
   * Reads the Value of the Title child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readTitle() throws UaException;

  /**
   * Writes the Value of the Title child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTitle(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readTitle()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readTitleAsync();

  /** Asynchronous form of {@link #writeTitle}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTitleAsync(@Nullable LocalizedText value);

  /**
   * Resolves the mandatory EURange child, a PropertyType with DataType Range.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getEURangeNode() throws UaException;

  /** Asynchronous form of {@link #getEURangeNode()}. */
  CompletableFuture<? extends PropertyType> getEURangeNodeAsync();

  /**
   * Reads the Value of the EURange child from the server.
   *
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
