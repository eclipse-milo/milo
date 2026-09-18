package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumValueType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the MultiStateValueDiscreteType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.3/#5.3.3.4">Model
 *     documentation</a>
 */
public interface MultiStateValueDiscreteType extends DiscreteItemType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 11238L);

  QualifiedProperty<EnumValueType[]> EnumValues_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EnumValues",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7594L),
          1,
          EnumValueType[].class);

  QualifiedProperty<LocalizedText> ValueAsText__PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ValueAsText",
          ExpandedNodeId.of(Namespaces.OPC_UA, 21L),
          -1,
          LocalizedText.class);

  /**
   * Resolves the mandatory EnumValues child, a PropertyType with DataType EnumValueType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getEnumValuesNode() throws UaException;

  /** Asynchronous form of {@link #getEnumValuesNode()}. */
  CompletableFuture<? extends PropertyType> getEnumValuesNodeAsync();

  /**
   * Reads the Value of the EnumValues child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable EnumValueType @Nullable [] readEnumValues() throws UaException;

  /**
   * Writes the Value of the EnumValues child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEnumValues(@Nullable EnumValueType @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readEnumValues()}. */
  CompletableFuture<? extends @Nullable EnumValueType @Nullable []> readEnumValuesAsync();

  /** Asynchronous form of {@link #writeEnumValues}; completes with the operation status. */
  CompletableFuture<StatusCode> writeEnumValuesAsync(@Nullable EnumValueType @Nullable [] value);

  /**
   * Resolves the mandatory ValueAsText child, a PropertyType with DataType LocalizedText.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getValueAsText_Node() throws UaException;

  /** Asynchronous form of {@link #getValueAsText_Node()}. */
  CompletableFuture<? extends PropertyType> getValueAsText_NodeAsync();

  /**
   * Reads the Value of the ValueAsText child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LocalizedText readValueAsText_() throws UaException;

  /**
   * Writes the Value of the ValueAsText child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeValueAsText_(@Nullable LocalizedText value) throws UaException;

  /** Asynchronous form of {@link #readValueAsText_()}. */
  CompletableFuture<? extends @Nullable LocalizedText> readValueAsText_Async();

  /** Asynchronous form of {@link #writeValueAsText_}; completes with the operation status. */
  CompletableFuture<StatusCode> writeValueAsText_Async(@Nullable LocalizedText value);

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
