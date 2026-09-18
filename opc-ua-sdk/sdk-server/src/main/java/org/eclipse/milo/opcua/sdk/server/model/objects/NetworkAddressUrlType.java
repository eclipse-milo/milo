package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the NetworkAddressUrlType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.7">Model
 *     documentation</a>
 */
public interface NetworkAddressUrlType extends NetworkAddressType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 21147L);

  /**
   * Returns the mandatory Url child, a BaseDataVariableType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  BaseDataVariableTypeNode getUrlNode();

  /**
   * Returns the Value of the Url child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getUrl();

  /**
   * Sets the Value of the Url child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setUrl(@Nullable String value);
}
