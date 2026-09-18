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
 * Client API for the PubSubTransportLimitsExceedEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.13/#9.1.13.2">Model
 *     documentation</a>
 */
public interface PubSubTransportLimitsExceedEventType extends PubSubStatusEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15548L);

  QualifiedProperty<UInteger> Actual_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Actual",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> Maximum_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Maximum",
          ExpandedNodeId.of(Namespaces.OPC_UA, 7L),
          -1,
          UInteger.class);

  /**
   * Resolves the mandatory Actual child, a PropertyType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getActualNode() throws UaException;

  /** Asynchronous form of {@link #getActualNode()}. */
  CompletableFuture<? extends PropertyType> getActualNodeAsync();

  /**
   * Reads the Value of the Actual child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readActual() throws UaException;

  /**
   * Writes the Value of the Actual child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeActual(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readActual()}. */
  CompletableFuture<? extends @Nullable UInteger> readActualAsync();

  /** Asynchronous form of {@link #writeActual}; completes with the operation status. */
  CompletableFuture<StatusCode> writeActualAsync(@Nullable UInteger value);

  /**
   * Resolves the mandatory Maximum child, a PropertyType with DataType UInt32.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getMaximumNode() throws UaException;

  /** Asynchronous form of {@link #getMaximumNode()}. */
  CompletableFuture<? extends PropertyType> getMaximumNodeAsync();

  /**
   * Reads the Value of the Maximum child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UInteger readMaximum() throws UaException;

  /**
   * Writes the Value of the Maximum child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeMaximum(@Nullable UInteger value) throws UaException;

  /** Asynchronous form of {@link #readMaximum()}. */
  CompletableFuture<? extends @Nullable UInteger> readMaximumAsync();

  /** Asynchronous form of {@link #writeMaximum}; completes with the operation status. */
  CompletableFuture<StatusCode> writeMaximumAsync(@Nullable UInteger value);
}
