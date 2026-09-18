package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AuditCancelEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.11">Model
 *     documentation</a>
 */
public interface AuditCancelEventType extends AuditSessionEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2078L);

  QualifiedProperty<UInteger> RequestHandle_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "RequestHandle",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  /**
   * Resolves the mandatory RequestHandle child, a PropertyType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getRequestHandleNode() throws UaException;

  /** Asynchronous form of {@link #getRequestHandleNode()}. */
  CompletableFuture<? extends PropertyType> getRequestHandleNodeAsync();

  /**
   * Reads the Value of the RequestHandle child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readRequestHandle() throws UaException;

  /**
   * Writes the Value of the RequestHandle child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRequestHandle(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readRequestHandle()}. */
  CompletableFuture<? extends @Nullable UInteger> readRequestHandleAsync();

  /** Asynchronous form of {@link #writeRequestHandle}; completes with the operation status. */
  CompletableFuture<StatusCode> writeRequestHandleAsync(@Nullable UInteger value);
}
