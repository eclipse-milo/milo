package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the PubSubCommunicationFailureEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.13/#9.1.13.3">Model
 *     documentation</a>
 */
public interface PubSubCommunicationFailureEventType extends PubSubStatusEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15563L);

  QualifiedProperty<StatusCode> Error_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Error",
          ExpandedNodeId.of(Namespaces.OPC_UA, 19L),
          -1,
          StatusCode.class);

  /**
   * Resolves the mandatory Error child, a PropertyType with DataType StatusCode.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getErrorNode() throws UaException;

  /** Asynchronous form of {@link #getErrorNode()}. */
  CompletableFuture<? extends PropertyType> getErrorNodeAsync();

  /**
   * Reads the Value of the Error child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable StatusCode readError() throws UaException;

  /**
   * Writes the Value of the Error child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeError(@Nullable StatusCode value) throws UaException;

  /** Asynchronous form of {@link #readError()}. */
  CompletableFuture<? extends @Nullable StatusCode> readErrorAsync();

  /** Asynchronous form of {@link #writeError}; completes with the operation status. */
  CompletableFuture<StatusCode> writeErrorAsync(@Nullable StatusCode value);
}
