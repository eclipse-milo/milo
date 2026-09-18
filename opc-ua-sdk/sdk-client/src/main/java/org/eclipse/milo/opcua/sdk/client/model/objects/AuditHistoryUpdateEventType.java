package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the AuditHistoryUpdateEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.26">Model
 *     documentation</a>
 */
public interface AuditHistoryUpdateEventType extends AuditUpdateEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2104L);

  QualifiedProperty<NodeId> ParameterDataTypeId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ParameterDataTypeId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  /**
   * Resolves the mandatory ParameterDataTypeId child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getParameterDataTypeIdNode() throws UaException;

  /** Asynchronous form of {@link #getParameterDataTypeIdNode()}. */
  CompletableFuture<? extends PropertyType> getParameterDataTypeIdNodeAsync();

  /**
   * Reads the Value of the ParameterDataTypeId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readParameterDataTypeId() throws UaException;

  /**
   * Writes the Value of the ParameterDataTypeId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeParameterDataTypeId(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readParameterDataTypeId()}. */
  CompletableFuture<? extends @Nullable NodeId> readParameterDataTypeIdAsync();

  /**
   * Asynchronous form of {@link #writeParameterDataTypeId}; completes with the operation status.
   */
  CompletableFuture<StatusCode> writeParameterDataTypeIdAsync(@Nullable NodeId value);
}
