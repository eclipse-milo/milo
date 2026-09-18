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
 * Client API for the OptionSetType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.17">Model
 *     documentation</a>
 */
public interface OptionSetType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 11487L);

  QualifiedProperty<LocalizedText[]> OptionSetValues_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "OptionSetValues",
          ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
          1,
          LocalizedText[].class);

  QualifiedProperty<Boolean[]> BitMask_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "BitMask",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          1,
          Boolean[].class);

  /**
   * Resolves the mandatory OptionSetValues child, a PropertyType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getOptionSetValuesNode() throws UaException;

  /** Asynchronous form of {@link #getOptionSetValuesNode()}. */
  CompletableFuture<? extends PropertyType> getOptionSetValuesNodeAsync();

  /**
   * Reads the Value of the OptionSetValues child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  LocalizedText @Nullable [] readOptionSetValues() throws UaException;

  /**
   * Writes the Value of the OptionSetValues child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeOptionSetValues(LocalizedText @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readOptionSetValues()}. */
  CompletableFuture<? extends LocalizedText @Nullable []> readOptionSetValuesAsync();

  /** Asynchronous form of {@link #writeOptionSetValues}; completes with the operation status. */
  CompletableFuture<StatusCode> writeOptionSetValuesAsync(LocalizedText @Nullable [] value);

  /**
   * Resolves the optional BitMask child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getBitMaskNode() throws UaException;

  /** Asynchronous form of {@link #getBitMaskNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getBitMaskNodeAsync();

  /**
   * Reads the Value of the BitMask child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  Boolean @Nullable [] readBitMask() throws UaException;

  /**
   * Writes the Value of the BitMask child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeBitMask(Boolean @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readBitMask()}. */
  CompletableFuture<? extends Boolean @Nullable []> readBitMaskAsync();

  /** Asynchronous form of {@link #writeBitMask}; completes with the operation status. */
  CompletableFuture<StatusCode> writeBitMaskAsync(Boolean @Nullable [] value);

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
