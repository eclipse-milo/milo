package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.RedundancySupport;
import org.eclipse.milo.opcua.stack.core.types.structured.RedundantServerDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the ServerRedundancyType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.7">Model
 *     documentation</a>
 */
public interface ServerRedundancyType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2034L);

  QualifiedProperty<RedundancySupport> RedundancySupport_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "RedundancySupport",
          ExpandedNodeId.of(Namespaces.OPC_UA, 851L),
          -1,
          RedundancySupport.class);

  QualifiedProperty<RedundantServerDataType[]> RedundantServerArray_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "RedundantServerArray",
          ExpandedNodeId.of(Namespaces.OPC_UA, 853L),
          1,
          RedundantServerDataType[].class);

  /**
   * Resolves the mandatory RedundancySupport child, a PropertyType with DataType RedundancySupport.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getRedundancySupportNode() throws UaException;

  /** Asynchronous form of {@link #getRedundancySupportNode()}. */
  CompletableFuture<? extends PropertyType> getRedundancySupportNodeAsync();

  /**
   * Reads the Value of the RedundancySupport child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable RedundancySupport readRedundancySupport() throws UaException;

  /**
   * Writes the Value of the RedundancySupport child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRedundancySupport(@Nullable RedundancySupport value) throws UaException;

  /** Asynchronous form of {@link #readRedundancySupport()}. */
  CompletableFuture<? extends @Nullable RedundancySupport> readRedundancySupportAsync();

  /** Asynchronous form of {@link #writeRedundancySupport}; completes with the operation status. */
  CompletableFuture<StatusCode> writeRedundancySupportAsync(@Nullable RedundancySupport value);

  /**
   * Resolves the optional RedundantServerArray child, a PropertyType with DataType
   * RedundantServerDataType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getRedundantServerArrayNode() throws UaException;

  /** Asynchronous form of {@link #getRedundantServerArrayNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getRedundantServerArrayNodeAsync();

  /**
   * Reads the Value of the RedundantServerArray child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable RedundantServerDataType @Nullable [] readRedundantServerArray() throws UaException;

  /**
   * Writes the Value of the RedundantServerArray child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeRedundantServerArray(@Nullable RedundantServerDataType @Nullable [] value)
      throws UaException;

  /** Asynchronous form of {@link #readRedundantServerArray()}. */
  CompletableFuture<? extends @Nullable RedundantServerDataType @Nullable []>
      readRedundantServerArrayAsync();

  /**
   * Asynchronous form of {@link #writeRedundantServerArray}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeRedundantServerArrayAsync(
      @Nullable RedundantServerDataType @Nullable [] value);
}
