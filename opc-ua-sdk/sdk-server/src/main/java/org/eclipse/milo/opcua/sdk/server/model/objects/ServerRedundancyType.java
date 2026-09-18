package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.RedundancySupport;
import org.eclipse.milo.opcua.stack.core.types.structured.RedundantServerDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ServerRedundancyType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.7">Model
 *     documentation</a>
 */
public interface ServerRedundancyType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2034L);

  /**
   * Returns the mandatory RedundancySupport child, a PropertyType with DataType RedundancySupport.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getRedundancySupportNode();

  /**
   * Returns the Value of the RedundancySupport child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable RedundancySupport getRedundancySupport();

  /**
   * Sets the Value of the RedundancySupport child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRedundancySupport(@Nullable RedundancySupport value);

  /**
   * Returns the optional RedundantServerArray child, a PropertyType with DataType
   * RedundantServerDataType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getRedundantServerArrayNode();

  /**
   * Returns the Value of the RedundantServerArray child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable RedundantServerDataType @Nullable [] getRedundantServerArray();

  /**
   * Sets the Value of the RedundantServerArray child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setRedundantServerArray(@Nullable RedundantServerDataType @Nullable [] value);
}
