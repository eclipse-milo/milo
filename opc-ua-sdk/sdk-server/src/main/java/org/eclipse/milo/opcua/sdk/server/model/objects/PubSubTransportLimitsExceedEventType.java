package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the PubSubTransportLimitsExceedEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.13/#9.1.13.2">Model
 *     documentation</a>
 */
public interface PubSubTransportLimitsExceedEventType extends PubSubStatusEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15548L);

  /**
   * Returns the mandatory Actual child, a PropertyType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getActualNode();

  /**
   * Returns the Value of the Actual child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getActual();

  /**
   * Sets the Value of the Actual child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setActual(@Nullable UInteger value);

  /**
   * Returns the mandatory Maximum child, a PropertyType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getMaximumNode();

  /**
   * Returns the Value of the Maximum child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaximum();

  /**
   * Sets the Value of the Maximum child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaximum(@Nullable UInteger value);
}
