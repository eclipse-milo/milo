package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ProgressEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.35">Model
 *     documentation</a>
 */
public interface ProgressEventType extends BaseEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 11436L);

  QualifiedProperty<Variant> Context_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Context",
          ExpandedNodeId.of(Namespaces.OPC_UA, 24L),
          -1,
          Variant.class);

  QualifiedProperty<UShort> Progress_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "Progress",
          ExpandedNodeId.of(Namespaces.OPC_UA, 5L),
          -1,
          UShort.class);

  /**
   * Resolves the mandatory Context child, a PropertyType with DataType BaseDataType.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getContextNode() throws UaException;

  /** Asynchronous form of {@link #getContextNode()}. */
  CompletableFuture<? extends PropertyType> getContextNodeAsync();

  /**
   * Reads the Value of the Context child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable Variant readContext() throws UaException;

  /**
   * Writes the Value of the Context child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeContext(@Nullable Variant value) throws UaException;

  /** Asynchronous form of {@link #readContext()}. */
  CompletableFuture<? extends @Nullable Variant> readContextAsync();

  /** Asynchronous form of {@link #writeContext}; completes with the operation status. */
  CompletableFuture<StatusCode> writeContextAsync(@Nullable Variant value);

  /**
   * Resolves the mandatory Progress child, a PropertyType with DataType UInt16.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getProgressNode() throws UaException;

  /** Asynchronous form of {@link #getProgressNode()}. */
  CompletableFuture<? extends PropertyType> getProgressNodeAsync();

  /**
   * Reads the Value of the Progress child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable UShort readProgress() throws UaException;

  /**
   * Writes the Value of the Progress child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeProgress(@Nullable UShort value) throws UaException;

  /** Asynchronous form of {@link #readProgress()}. */
  CompletableFuture<? extends @Nullable UShort> readProgressAsync();

  /** Asynchronous form of {@link #writeProgress}; completes with the operation status. */
  CompletableFuture<StatusCode> writeProgressAsync(@Nullable UShort value);
}
