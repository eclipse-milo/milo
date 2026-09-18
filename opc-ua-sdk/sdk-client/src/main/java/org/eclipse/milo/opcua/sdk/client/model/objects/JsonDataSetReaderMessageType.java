package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonNetworkMessageContentMask;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the JsonDataSetReaderMessageType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.2/#9.2.2.3">Model
 *     documentation</a>
 */
public interface JsonDataSetReaderMessageType extends DataSetReaderMessageType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 21130L);

  QualifiedProperty<JsonDataSetMessageContentMask> DataSetMessageContentMask_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DataSetMessageContentMask",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15658L),
          -1,
          JsonDataSetMessageContentMask.class);

  QualifiedProperty<JsonNetworkMessageContentMask> NetworkMessageContentMask_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "NetworkMessageContentMask",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15654L),
          -1,
          JsonNetworkMessageContentMask.class);

  /**
   * Resolves the mandatory DataSetMessageContentMask child, a PropertyType with DataType
   * JsonDataSetMessageContentMask.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getDataSetMessageContentMaskNode() throws UaException;

  /** Asynchronous form of {@link #getDataSetMessageContentMaskNode()}. */
  CompletableFuture<? extends PropertyType> getDataSetMessageContentMaskNodeAsync();

  /**
   * Reads the Value of the DataSetMessageContentMask child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable JsonDataSetMessageContentMask readDataSetMessageContentMask() throws UaException;

  /**
   * Writes the Value of the DataSetMessageContentMask child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDataSetMessageContentMask(@Nullable JsonDataSetMessageContentMask value)
      throws UaException;

  /** Asynchronous form of {@link #readDataSetMessageContentMask()}. */
  CompletableFuture<? extends @Nullable JsonDataSetMessageContentMask>
      readDataSetMessageContentMaskAsync();

  /**
   * Asynchronous form of {@link #writeDataSetMessageContentMask}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeDataSetMessageContentMaskAsync(
      @Nullable JsonDataSetMessageContentMask value);

  /**
   * Resolves the mandatory NetworkMessageContentMask child, a PropertyType with DataType
   * JsonNetworkMessageContentMask.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getNetworkMessageContentMaskNode() throws UaException;

  /** Asynchronous form of {@link #getNetworkMessageContentMaskNode()}. */
  CompletableFuture<? extends PropertyType> getNetworkMessageContentMaskNodeAsync();

  /**
   * Reads the Value of the NetworkMessageContentMask child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable JsonNetworkMessageContentMask readNetworkMessageContentMask() throws UaException;

  /**
   * Writes the Value of the NetworkMessageContentMask child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeNetworkMessageContentMask(@Nullable JsonNetworkMessageContentMask value)
      throws UaException;

  /** Asynchronous form of {@link #readNetworkMessageContentMask()}. */
  CompletableFuture<? extends @Nullable JsonNetworkMessageContentMask>
      readNetworkMessageContentMaskAsync();

  /**
   * Asynchronous form of {@link #writeNetworkMessageContentMask}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeNetworkMessageContentMaskAsync(
      @Nullable JsonNetworkMessageContentMask value);
}
