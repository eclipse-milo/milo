package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the PubSubStatusEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.13/#9.1.13.1">Model
 *     documentation</a>
 */
public interface PubSubStatusEventType extends SystemEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15535L);

  QualifiedProperty<NodeId> ConnectionId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ConnectionId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  QualifiedProperty<PubSubState> State_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "State",
          ExpandedNodeId.of(Namespaces.OPC_UA, 14647L),
          -1,
          PubSubState.class);

  QualifiedProperty<NodeId> GroupId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "GroupId",
          ExpandedNodeId.of(Namespaces.OPC_UA, 17L),
          -1,
          NodeId.class);

  /**
   * Resolves the mandatory ConnectionId child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getConnectionIdNode() throws UaException;

  /** Asynchronous form of {@link #getConnectionIdNode()}. */
  CompletableFuture<? extends PropertyType> getConnectionIdNodeAsync();

  /**
   * Reads the Value of the ConnectionId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readConnectionId() throws UaException;

  /**
   * Writes the Value of the ConnectionId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeConnectionId(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readConnectionId()}. */
  CompletableFuture<? extends @Nullable NodeId> readConnectionIdAsync();

  /** Asynchronous form of {@link #writeConnectionId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeConnectionIdAsync(@Nullable NodeId value);

  /**
   * Resolves the mandatory State child, a PropertyType with DataType PubSubState.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getStateNode() throws UaException;

  /** Asynchronous form of {@link #getStateNode()}. */
  CompletableFuture<? extends PropertyType> getStateNodeAsync();

  /**
   * Reads the Value of the State child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable PubSubState readState() throws UaException;

  /**
   * Writes the Value of the State child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeState(@Nullable PubSubState value) throws UaException;

  /** Asynchronous form of {@link #readState()}. */
  CompletableFuture<? extends @Nullable PubSubState> readStateAsync();

  /** Asynchronous form of {@link #writeState}; completes with the operation status. */
  CompletableFuture<StatusCode> writeStateAsync(@Nullable PubSubState value);

  /**
   * Resolves the mandatory GroupId child, a PropertyType with DataType NodeId.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getGroupIdNode() throws UaException;

  /** Asynchronous form of {@link #getGroupIdNode()}. */
  CompletableFuture<? extends PropertyType> getGroupIdNodeAsync();

  /**
   * Reads the Value of the GroupId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable NodeId readGroupId() throws UaException;

  /**
   * Writes the Value of the GroupId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeGroupId(@Nullable NodeId value) throws UaException;

  /** Asynchronous form of {@link #readGroupId()}. */
  CompletableFuture<? extends @Nullable NodeId> readGroupIdAsync();

  /** Asynchronous form of {@link #writeGroupId}; completes with the operation status. */
  CompletableFuture<StatusCode> writeGroupIdAsync(@Nullable NodeId value);
}
