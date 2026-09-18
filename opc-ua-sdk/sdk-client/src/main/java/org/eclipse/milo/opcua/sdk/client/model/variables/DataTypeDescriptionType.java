package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/** Client API for the DataTypeDescriptionType VariableType. */
public interface DataTypeDescriptionType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 69L);

  QualifiedProperty<String> DataTypeVersion__PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DataTypeVersion",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<ByteString> DictionaryFragment__PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DictionaryFragment",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15L),
          -1,
          ByteString.class);

  /**
   * Resolves the optional DataTypeVersion child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getDataTypeVersion_Node() throws UaException;

  /** Asynchronous form of {@link #getDataTypeVersion_Node()}. */
  CompletableFuture<? extends @Nullable PropertyType> getDataTypeVersion_NodeAsync();

  /**
   * Reads the Value of the DataTypeVersion child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readDataTypeVersion_() throws UaException;

  /**
   * Writes the Value of the DataTypeVersion child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDataTypeVersion_(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readDataTypeVersion_()}. */
  CompletableFuture<? extends @Nullable String> readDataTypeVersion_Async();

  /** Asynchronous form of {@link #writeDataTypeVersion_}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDataTypeVersion_Async(@Nullable String value);

  /**
   * Resolves the optional DictionaryFragment child, a PropertyType with DataType ByteString.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getDictionaryFragment_Node() throws UaException;

  /** Asynchronous form of {@link #getDictionaryFragment_Node()}. */
  CompletableFuture<? extends @Nullable PropertyType> getDictionaryFragment_NodeAsync();

  /**
   * Reads the Value of the DictionaryFragment child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ByteString readDictionaryFragment_() throws UaException;

  /**
   * Writes the Value of the DictionaryFragment child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDictionaryFragment_(@Nullable ByteString value) throws UaException;

  /** Asynchronous form of {@link #readDictionaryFragment_()}. */
  CompletableFuture<? extends @Nullable ByteString> readDictionaryFragment_Async();

  /**
   * Asynchronous form of {@link #writeDictionaryFragment_}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeDictionaryFragment_Async(@Nullable ByteString value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable String> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable String value);
}
