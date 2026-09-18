package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AuditWriteUpdateEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.25">Model
 *     documentation</a>
 */
public interface AuditWriteUpdateEventType extends AuditUpdateEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2100L);

  QualifiedProperty<String> IndexRange_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "IndexRange",
          ExpandedNodeId.of(Namespaces.OPC_UA, 291L),
          -1,
          String.class);

  QualifiedProperty<UInteger> AttributeId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "AttributeId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<Variant> NewValue_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "NewValue",
          ExpandedNodeId.of(Namespaces.OPC_UA, 24L),
          -1,
          Variant.class);

  QualifiedProperty<Variant> OldValue_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "OldValue",
          ExpandedNodeId.of(Namespaces.OPC_UA, 24L),
          -1,
          Variant.class);

  /**
   * Resolves the mandatory IndexRange child, a PropertyType with DataType NumericRange.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getIndexRangeNode() throws UaException;

  /** Asynchronous form of {@link #getIndexRangeNode()}. */
  CompletableFuture<? extends PropertyType> getIndexRangeNodeAsync();

  /**
   * Reads the Value of the IndexRange child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readIndexRange() throws UaException;

  /**
   * Writes the Value of the IndexRange child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeIndexRange(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readIndexRange()}. */
  CompletableFuture<? extends @Nullable String> readIndexRangeAsync();

  /** Asynchronous form of {@link #writeIndexRange}; completes with the operation status. */
  CompletableFuture<StatusCode> writeIndexRangeAsync(@Nullable String value);

  /**
   * Resolves the mandatory AttributeId child, a PropertyType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getAttributeIdNode() throws UaException;

  /** Asynchronous form of {@link #getAttributeIdNode()}. */
  CompletableFuture<? extends PropertyType> getAttributeIdNodeAsync();

  /**
   * Reads the Value of the AttributeId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readAttributeId() throws UaException;

  /**
   * Writes the Value of the AttributeId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeAttributeId(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readAttributeId()}. */
  CompletableFuture<? extends @Nullable UInteger> readAttributeIdAsync();

  /** Asynchronous form of {@link #writeAttributeId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeAttributeIdAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory NewValue child, a PropertyType with DataType BaseDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getNewValueNode() throws UaException;

  /** Asynchronous form of {@link #getNewValueNode()}. */
  CompletableFuture<? extends PropertyType> getNewValueNodeAsync();

  /**
   * Reads the Value of the NewValue child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Variant readNewValue() throws UaException;

  /**
   * Writes the Value of the NewValue child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNewValue(@Nullable Variant value) throws UaException;

  /** Asynchronous form of {@link #readNewValue()}. */
  CompletableFuture<? extends @Nullable Variant> readNewValueAsync();

  /** Asynchronous form of {@link #writeNewValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeNewValueAsync(@Nullable Variant value);

  /**
   * Resolves the mandatory OldValue child, a PropertyType with DataType BaseDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getOldValueNode() throws UaException;

  /** Asynchronous form of {@link #getOldValueNode()}. */
  CompletableFuture<? extends PropertyType> getOldValueNodeAsync();

  /**
   * Reads the Value of the OldValue child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Variant readOldValue() throws UaException;

  /**
   * Writes the Value of the OldValue child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeOldValue(@Nullable Variant value) throws UaException;

  /** Asynchronous form of {@link #readOldValue()}. */
  CompletableFuture<? extends @Nullable Variant> readOldValueAsync();

  /** Asynchronous form of {@link #writeOldValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeOldValueAsync(@Nullable Variant value);
}
