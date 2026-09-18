package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.JsonDataSetMessageContentMask;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the JsonDataSetWriterMessageType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.2.2/#9.2.2.2">Model
 *     documentation</a>
 */
public interface JsonDataSetWriterMessageType extends DataSetWriterMessageType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 21128L);

  QualifiedProperty<JsonDataSetMessageContentMask> DataSetMessageContentMask_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DataSetMessageContentMask",
          ExpandedNodeId.of(Namespaces.OPC_UA, 15658L),
          -1,
          JsonDataSetMessageContentMask.class);

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
}
