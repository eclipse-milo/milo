package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the MultiStateDiscreteType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.3/#5.3.3.3">Model
 *     documentation</a>
 */
public interface MultiStateDiscreteType extends DiscreteItemType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2376L);

  QualifiedProperty<LocalizedText[]> EnumStrings_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EnumStrings",
          ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
          1,
          LocalizedText[].class);

  /**
   * Resolves the mandatory EnumStrings child, a PropertyType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getEnumStringsNode() throws UaException;

  /** Asynchronous form of {@link #getEnumStringsNode()}. */
  CompletableFuture<? extends PropertyType> getEnumStringsNodeAsync();

  /**
   * Reads the Value of the EnumStrings child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  LocalizedText @Nullable [] readEnumStrings() throws UaException;

  /**
   * Writes the Value of the EnumStrings child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEnumStrings(LocalizedText @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readEnumStrings()}. */
  CompletableFuture<? extends LocalizedText @Nullable []> readEnumStringsAsync();

  /** Asynchronous form of {@link #writeEnumStrings}; completes with the operation status. */
  CompletableFuture<StatusCode> writeEnumStringsAsync(LocalizedText @Nullable [] value);

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
