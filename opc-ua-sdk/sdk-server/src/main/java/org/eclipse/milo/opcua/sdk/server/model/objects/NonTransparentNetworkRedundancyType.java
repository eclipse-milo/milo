package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.NetworkGroupDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the NonTransparentNetworkRedundancyType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.10">Model
 *     documentation</a>
 */
public interface NonTransparentNetworkRedundancyType extends NonTransparentRedundancyType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 11945L);

  /**
   * Returns the mandatory ServerNetworkGroups child, a PropertyType with DataType
   * NetworkGroupDataType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getServerNetworkGroupsNode();

  /**
   * Returns the Value of the ServerNetworkGroups child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NetworkGroupDataType @Nullable [] getServerNetworkGroups();

  /**
   * Sets the Value of the ServerNetworkGroups child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setServerNetworkGroups(@Nullable NetworkGroupDataType @Nullable [] value);
}
