package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.DiagnosticsLevel;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubDiagnosticsCounterClassification;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the PubSubDiagnosticsCounterType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.5">Model
 *     documentation</a>
 */
public interface PubSubDiagnosticsCounterType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 19725L);

  QualifiedProperty<PubSubDiagnosticsCounterClassification> Classification_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Classification",
          ExpandedNodeId.of(Namespaces.OPC_UA, 19730L),
          -1,
          PubSubDiagnosticsCounterClassification.class);

  QualifiedProperty<DateTime> TimeFirstChange_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "TimeFirstChange",
          ExpandedNodeId.of(Namespaces.OPC_UA, 13L),
          -1,
          DateTime.class);

  QualifiedProperty<DiagnosticsLevel> DiagnosticsLevel_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DiagnosticsLevel",
          ExpandedNodeId.of(Namespaces.OPC_UA, 19723L),
          -1,
          DiagnosticsLevel.class);

  QualifiedProperty<Boolean> Active_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA, "Active", ExpandedNodeId.of(Namespaces.OPC_UA, 1L), -1, Boolean.class);

  /**
   * Resolves the mandatory Classification child, a PropertyType with DataType
   * PubSubDiagnosticsCounterClassification.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getClassificationNode() throws UaException;

  /** Asynchronous form of {@link #getClassificationNode()}. */
  CompletableFuture<? extends PropertyType> getClassificationNodeAsync();

  /**
   * Reads the Value of the Classification child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable PubSubDiagnosticsCounterClassification readClassification() throws UaException;

  /**
   * Writes the Value of the Classification child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeClassification(@Nullable PubSubDiagnosticsCounterClassification value)
      throws UaException;

  /** Asynchronous form of {@link #readClassification()}. */
  CompletableFuture<? extends @Nullable PubSubDiagnosticsCounterClassification>
      readClassificationAsync();

  /** Asynchronous form of {@link #writeClassification}; completes with the operation status. */
  CompletableFuture<StatusCode> writeClassificationAsync(
      @Nullable PubSubDiagnosticsCounterClassification value);

  /**
   * Resolves the optional TimeFirstChange child, a PropertyType with DataType DateTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getTimeFirstChangeNode() throws UaException;

  /** Asynchronous form of {@link #getTimeFirstChangeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getTimeFirstChangeNodeAsync();

  /**
   * Reads the Value of the TimeFirstChange child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DateTime readTimeFirstChange() throws UaException;

  /**
   * Writes the Value of the TimeFirstChange child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTimeFirstChange(@Nullable DateTime value) throws UaException;

  /** Asynchronous form of {@link #readTimeFirstChange()}. */
  CompletableFuture<? extends @Nullable DateTime> readTimeFirstChangeAsync();

  /** Asynchronous form of {@link #writeTimeFirstChange}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTimeFirstChangeAsync(@Nullable DateTime value);

  /**
   * Resolves the mandatory DiagnosticsLevel child, a PropertyType with DataType DiagnosticsLevel.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getDiagnosticsLevelNode() throws UaException;

  /** Asynchronous form of {@link #getDiagnosticsLevelNode()}. */
  CompletableFuture<? extends PropertyType> getDiagnosticsLevelNodeAsync();

  /**
   * Reads the Value of the DiagnosticsLevel child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable DiagnosticsLevel readDiagnosticsLevel() throws UaException;

  /**
   * Writes the Value of the DiagnosticsLevel child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDiagnosticsLevel(@Nullable DiagnosticsLevel value) throws UaException;

  /** Asynchronous form of {@link #readDiagnosticsLevel()}. */
  CompletableFuture<? extends @Nullable DiagnosticsLevel> readDiagnosticsLevelAsync();

  /** Asynchronous form of {@link #writeDiagnosticsLevel}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDiagnosticsLevelAsync(@Nullable DiagnosticsLevel value);

  /**
   * Resolves the mandatory Active child, a PropertyType with DataType Boolean.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getActiveNode() throws UaException;

  /** Asynchronous form of {@link #getActiveNode()}. */
  CompletableFuture<? extends PropertyType> getActiveNodeAsync();

  /**
   * Reads the Value of the Active child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readActive() throws UaException;

  /**
   * Writes the Value of the Active child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeActive(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readActive()}. */
  CompletableFuture<? extends @Nullable Boolean> readActiveAsync();

  /** Asynchronous form of {@link #writeActive}; completes with the operation status. */
  CompletableFuture<StatusCode> writeActiveAsync(@Nullable Boolean value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable UInteger> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable UInteger value);
}
