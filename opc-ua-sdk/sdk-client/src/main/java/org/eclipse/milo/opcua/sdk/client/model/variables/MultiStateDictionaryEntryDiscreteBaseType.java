package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the MultiStateDictionaryEntryDiscreteBaseType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part19/7.1">Model
 *     documentation</a>
 */
public interface MultiStateDictionaryEntryDiscreteBaseType extends MultiStateValueDiscreteType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 19077L);

  QualifiedProperty<Object> EnumDictionaryEntries_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "EnumDictionaryEntries",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          2,
          Object.class);

  QualifiedProperty<NodeId[]> ValueAsDictionaryEntries_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ValueAsDictionaryEntries",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          1,
          NodeId[].class);

  /**
   * Resolves the mandatory EnumDictionaryEntries child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getEnumDictionaryEntriesNode() throws UaException;

  /** Asynchronous form of {@link #getEnumDictionaryEntriesNode()}. */
  CompletableFuture<? extends PropertyType> getEnumDictionaryEntriesNodeAsync();

  /**
   * Reads the Value of the EnumDictionaryEntries child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Object readEnumDictionaryEntries() throws UaException;

  /**
   * Writes the Value of the EnumDictionaryEntries child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeEnumDictionaryEntries(@Nullable Object value) throws UaException;

  /** Asynchronous form of {@link #readEnumDictionaryEntries()}. */
  CompletableFuture<? extends @Nullable Object> readEnumDictionaryEntriesAsync();

  /**
   * Asynchronous form of {@link #writeEnumDictionaryEntries}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeEnumDictionaryEntriesAsync(@Nullable Object value);

  /**
   * Resolves the optional ValueAsDictionaryEntries child, a PropertyType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getValueAsDictionaryEntriesNode() throws UaException;

  /** Asynchronous form of {@link #getValueAsDictionaryEntriesNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getValueAsDictionaryEntriesNodeAsync();

  /**
   * Reads the Value of the ValueAsDictionaryEntries child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  NodeId @Nullable [] readValueAsDictionaryEntries() throws UaException;

  /**
   * Writes the Value of the ValueAsDictionaryEntries child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeValueAsDictionaryEntries(NodeId @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readValueAsDictionaryEntries()}. */
  CompletableFuture<? extends NodeId @Nullable []> readValueAsDictionaryEntriesAsync();

  /**
   * Asynchronous form of {@link #writeValueAsDictionaryEntries}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeValueAsDictionaryEntriesAsync(NodeId @Nullable [] value);

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
