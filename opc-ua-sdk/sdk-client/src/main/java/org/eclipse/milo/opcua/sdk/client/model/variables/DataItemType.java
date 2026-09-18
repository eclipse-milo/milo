package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the DataItemType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.1">Model
 *     documentation</a>
 */
public interface DataItemType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2365L);

  QualifiedProperty<String> Definition_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Definition",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<Double> ValuePrecision_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ValuePrecision",
          ExpandedNodeId.of(Namespaces.OPC_UA, 11L),
          -1,
          Double.class);

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
   * Resolves the optional ValuePrecision child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getValuePrecisionNode() throws UaException;

  /** Asynchronous form of {@link #getValuePrecisionNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getValuePrecisionNodeAsync();

  /**
   * Reads the Value of the ValuePrecision child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readValuePrecision() throws UaException;

  /**
   * Writes the Value of the ValuePrecision child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeValuePrecision(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readValuePrecision()}. */
  CompletableFuture<? extends @Nullable Double> readValuePrecisionAsync();

  /** Asynchronous form of {@link #writeValuePrecision}; completes with the operation status. */
  CompletableFuture<StatusCode> writeValuePrecisionAsync(@Nullable Double value);

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
