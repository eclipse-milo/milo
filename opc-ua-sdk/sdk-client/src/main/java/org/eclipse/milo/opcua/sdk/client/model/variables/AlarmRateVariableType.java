package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AlarmRateVariableType VariableType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/9.3">Model
 *     documentation</a>
 */
public interface AlarmRateVariableType extends BaseDataVariableType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 17277L);

  QualifiedProperty<UShort> Rate_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA, "Rate", ExpandedNodeId.of(Namespaces.OPC_UA, 5L), -1, UShort.class);

  /**
   * Resolves the mandatory Rate child, a PropertyType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getRateNode() throws UaException;

  /** Asynchronous form of {@link #getRateNode()}. */
  CompletableFuture<? extends PropertyType> getRateNodeAsync();

  /**
   * Reads the Value of the Rate child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readRate() throws UaException;

  /**
   * Writes the Value of the Rate child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRate(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readRate()}. */
  CompletableFuture<? extends @Nullable UShort> readRateAsync();

  /** Asynchronous form of {@link #writeRate}; completes with the operation status. */
  CompletableFuture<StatusCode> writeRateAsync(@Nullable UShort value);

  /**
   * Reads the Value of this node from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Double readTypedValue() throws UaException;

  /**
   * Writes the Value of this node to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeTypedValue(@Nullable Double value) throws UaException;

  /** Asynchronous form of {@link #readTypedValue()}. */
  CompletableFuture<? extends @Nullable Double> readTypedValueAsync();

  /** Asynchronous form of {@link #writeTypedValue}; completes with the operation status. */
  CompletableFuture<StatusCode> writeTypedValueAsync(@Nullable Double value);
}
