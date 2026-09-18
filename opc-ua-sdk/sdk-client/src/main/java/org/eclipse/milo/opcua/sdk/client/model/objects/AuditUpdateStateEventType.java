package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AuditUpdateStateEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part16/4.4.17">Model
 *     documentation</a>
 */
public interface AuditUpdateStateEventType extends AuditUpdateMethodEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2315L);

  QualifiedProperty<Variant> NewStateId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "NewStateId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 24L),
          -1,
          Variant.class);

  QualifiedProperty<Variant> OldStateId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "OldStateId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 24L),
          -1,
          Variant.class);

  /**
   * Resolves the mandatory NewStateId child, a PropertyType with DataType BaseDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getNewStateIdNode() throws UaException;

  /** Asynchronous form of {@link #getNewStateIdNode()}. */
  CompletableFuture<? extends PropertyType> getNewStateIdNodeAsync();

  /**
   * Reads the Value of the NewStateId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Variant readNewStateId() throws UaException;

  /**
   * Writes the Value of the NewStateId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNewStateId(@Nullable Variant value) throws UaException;

  /** Asynchronous form of {@link #readNewStateId()}. */
  CompletableFuture<? extends @Nullable Variant> readNewStateIdAsync();

  /** Asynchronous form of {@link #writeNewStateId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeNewStateIdAsync(@Nullable Variant value);

  /**
   * Resolves the mandatory OldStateId child, a PropertyType with DataType BaseDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getOldStateIdNode() throws UaException;

  /** Asynchronous form of {@link #getOldStateIdNode()}. */
  CompletableFuture<? extends PropertyType> getOldStateIdNodeAsync();

  /**
   * Reads the Value of the OldStateId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Variant readOldStateId() throws UaException;

  /**
   * Writes the Value of the OldStateId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeOldStateId(@Nullable Variant value) throws UaException;

  /** Asynchronous form of {@link #readOldStateId()}. */
  CompletableFuture<? extends @Nullable Variant> readOldStateIdAsync();

  /** Asynchronous form of {@link #writeOldStateId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeOldStateIdAsync(@Nullable Variant value);
}
