package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/** Client API for the DataTypeDictionaryType VariableType. */
public interface DataTypeDictionaryType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 72L);

  QualifiedProperty<Boolean> Deprecated_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Deprecated",
          ExpandedNodeId.of(Namespaces.OPC_UA, 1L),
          -1,
          Boolean.class);

  QualifiedProperty<String> NamespaceUri_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "NamespaceUri",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<String> DataTypeVersion__PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DataTypeVersion",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  /**
   * Resolves the optional Deprecated child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getDeprecatedNode() throws UaException;

  /** Asynchronous form of {@link #getDeprecatedNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getDeprecatedNodeAsync();

  /**
   * Reads the Value of the Deprecated child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Boolean readDeprecated() throws UaException;

  /**
   * Writes the Value of the Deprecated child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDeprecated(@Nullable Boolean value) throws UaException;

  /** Asynchronous form of {@link #readDeprecated()}. */
  CompletableFuture<? extends @Nullable Boolean> readDeprecatedAsync();

  /** Asynchronous form of {@link #writeDeprecated}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDeprecatedAsync(@Nullable Boolean value);

  /**
   * Resolves the optional NamespaceUri child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getNamespaceUriNode() throws UaException;

  /** Asynchronous form of {@link #getNamespaceUriNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getNamespaceUriNodeAsync();

  /**
   * Reads the Value of the NamespaceUri child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readNamespaceUri() throws UaException;

  /**
   * Writes the Value of the NamespaceUri child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNamespaceUri(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readNamespaceUri()}. */
  CompletableFuture<? extends @Nullable String> readNamespaceUriAsync();

  /** Asynchronous form of {@link #writeNamespaceUri}; completes with the operation status. */
  CompletableFuture<StatusCode> writeNamespaceUriAsync(@Nullable String value);

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
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable ByteString readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable ByteString value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable ByteString> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable ByteString value);
}
