package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AudioVariableType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.19">Model
 *     documentation</a>
 */
public interface AudioVariableType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 17986L);

  QualifiedProperty<String> ListId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA, "ListId", ExpandedNodeId.of(Namespaces.OPC_UA, 12L), -1, String.class);

  QualifiedProperty<String> AgencyId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "AgencyId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<String> VersionId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "VersionId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  /**
   * Resolves the optional ListId child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getListIdNode() throws UaException;

  /** Asynchronous form of {@link #getListIdNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getListIdNodeAsync();

  /**
   * Reads the Value of the ListId child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readListId() throws UaException;

  /**
   * Writes the Value of the ListId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeListId(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readListId()}. */
  CompletableFuture<? extends @Nullable String> readListIdAsync();

  /** Asynchronous form of {@link #writeListId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeListIdAsync(@Nullable String value);

  /**
   * Resolves the optional AgencyId child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getAgencyIdNode() throws UaException;

  /** Asynchronous form of {@link #getAgencyIdNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getAgencyIdNodeAsync();

  /**
   * Reads the Value of the AgencyId child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readAgencyId() throws UaException;

  /**
   * Writes the Value of the AgencyId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAgencyId(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readAgencyId()}. */
  CompletableFuture<? extends @Nullable String> readAgencyIdAsync();

  /** Asynchronous form of {@link #writeAgencyId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeAgencyIdAsync(@Nullable String value);

  /**
   * Resolves the optional VersionId child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getVersionIdNode() throws UaException;

  /** Asynchronous form of {@link #getVersionIdNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getVersionIdNodeAsync();

  /**
   * Reads the Value of the VersionId child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readVersionId() throws UaException;

  /**
   * Writes the Value of the VersionId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeVersionId(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readVersionId()}. */
  CompletableFuture<? extends @Nullable String> readVersionIdAsync();

  /** Asynchronous form of {@link #writeVersionId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeVersionIdAsync(@Nullable String value);

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
