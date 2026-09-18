package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.SelectionListTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the NetworkAddressType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.6">Model
 *     documentation</a>
 */
public interface NetworkAddressType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 21145L);

  /**
   * Returns the mandatory NetworkInterface child, a SelectionListType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.18">SelectionListType
   *     documentation</a>
   */
  SelectionListTypeNode getNetworkInterfaceNode();

  /**
   * Returns the Value of the NetworkInterface child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getNetworkInterface();

  /**
   * Sets the Value of the NetworkInterface child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setNetworkInterface(@Nullable String value);
}
