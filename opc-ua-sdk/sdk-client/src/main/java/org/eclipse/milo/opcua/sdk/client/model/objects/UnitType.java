package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the UnitType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.4.2/#6.4.2.2">Model
 *     documentation</a>
 */
public interface UnitType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32442L);

  QualifiedProperty<String> Discipline_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Discipline",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<String> UnitSystem_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "UnitSystem",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<LocalizedText> Symbol_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Symbol",
          ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
          -1,
          LocalizedText.class);

  /**
   * Resolves the optional Discipline child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getDisciplineNode() throws UaException;

  /** Asynchronous form of {@link #getDisciplineNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getDisciplineNodeAsync();

  /**
   * Reads the Value of the Discipline child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readDiscipline() throws UaException;

  /**
   * Writes the Value of the Discipline child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDiscipline(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readDiscipline()}. */
  CompletableFuture<? extends @Nullable String> readDisciplineAsync();

  /** Asynchronous form of {@link #writeDiscipline}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDisciplineAsync(@Nullable String value);

  /**
   * Resolves the mandatory UnitSystem child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getUnitSystemNode() throws UaException;

  /** Asynchronous form of {@link #getUnitSystemNode()}. */
  CompletableFuture<? extends PropertyType> getUnitSystemNodeAsync();

  /**
   * Reads the Value of the UnitSystem child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readUnitSystem() throws UaException;

  /**
   * Writes the Value of the UnitSystem child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeUnitSystem(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readUnitSystem()}. */
  CompletableFuture<? extends @Nullable String> readUnitSystemAsync();

  /** Asynchronous form of {@link #writeUnitSystem}; completes with the operation status. */
  CompletableFuture<StatusCode> writeUnitSystemAsync(@Nullable String value);

  /**
   * Resolves the mandatory Symbol child, a PropertyType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSymbolNode() throws UaException;

  /** Asynchronous form of {@link #getSymbolNode()}. */
  CompletableFuture<? extends PropertyType> getSymbolNodeAsync();

  /**
   * Reads the Value of the Symbol child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readSymbol() throws UaException;

  /**
   * Writes the Value of the Symbol child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSymbol(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readSymbol()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readSymbolAsync();

  /** Asynchronous form of {@link #writeSymbol}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSymbolAsync(@Nullable LocalizedText value);
}
