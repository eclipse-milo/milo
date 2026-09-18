package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the PubSubStatusEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.13/#9.1.13.1">Model
 *     documentation</a>
 */
public interface PubSubStatusEventType extends SystemEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15535L);

  /**
   * Returns the mandatory ConnectionId child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getConnectionIdNode();

  /**
   * Returns the Value of the ConnectionId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NodeId getConnectionId();

  /**
   * Sets the Value of the ConnectionId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setConnectionId(@Nullable NodeId value);

  /**
   * Returns the mandatory GroupId child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getGroupIdNode();

  /**
   * Returns the Value of the GroupId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NodeId getGroupId();

  /**
   * Sets the Value of the GroupId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setGroupId(@Nullable NodeId value);

  /**
   * Returns the mandatory State child, a PropertyType with DataType PubSubState.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getStateNode();

  /**
   * Returns the Value of the State child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable PubSubState getState();

  /**
   * Sets the Value of the State child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setState(@Nullable PubSubState value);
}
