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
 * Client API for the NonExclusiveDeviationAlarmType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.22/#5.8.22.2">Model
 *     documentation</a>
 */
public interface NonExclusiveDeviationAlarmType extends NonExclusiveLimitAlarmType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 10368L);

  QualifiedProperty<NodeId> SetpointNode_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "SetpointNode",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  QualifiedProperty<NodeId> BaseSetpointNode_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "BaseSetpointNode",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  /**
   * Resolves the mandatory SetpointNode child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getSetpointNodeNode() throws UaException;

  /** Asynchronous form of {@link #getSetpointNodeNode()}. */
  CompletableFuture<? extends PropertyType> getSetpointNodeNodeAsync();

  /**
   * Reads the Value of the SetpointNode child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readSetpointNode() throws UaException;

  /**
   * Writes the Value of the SetpointNode child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeSetpointNode(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readSetpointNode()}. */
  CompletableFuture<? extends @Nullable NodeId> readSetpointNodeAsync();

  /** Asynchronous form of {@link #writeSetpointNode}; completes with the operation status. */
  CompletableFuture<StatusCode> writeSetpointNodeAsync(@Nullable NodeId value);

  /**
   * Resolves the optional BaseSetpointNode child, a PropertyType with DataType NodeId.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getBaseSetpointNodeNode() throws UaException;

  /** Asynchronous form of {@link #getBaseSetpointNodeNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getBaseSetpointNodeNodeAsync();

  /**
   * Reads the Value of the BaseSetpointNode child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readBaseSetpointNode() throws UaException;

  /**
   * Writes the Value of the BaseSetpointNode child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeBaseSetpointNode(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readBaseSetpointNode()}. */
  CompletableFuture<? extends @Nullable NodeId> readBaseSetpointNodeAsync();

  /** Asynchronous form of {@link #writeBaseSetpointNode}; completes with the operation status. */
  CompletableFuture<StatusCode> writeBaseSetpointNodeAsync(@Nullable NodeId value);
}
