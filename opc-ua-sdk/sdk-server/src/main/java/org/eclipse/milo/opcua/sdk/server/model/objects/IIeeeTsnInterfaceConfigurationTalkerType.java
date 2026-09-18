package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the IIeeeTsnInterfaceConfigurationTalkerType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.2.11">Model
 *     documentation</a>
 */
public interface IIeeeTsnInterfaceConfigurationTalkerType
    extends IIeeeTsnInterfaceConfigurationType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24191L);

  /**
   * Returns the optional TimeAwareOffset child, a BaseDataVariableType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.4">BaseDataVariableType
   *     documentation</a>
   */
  @Nullable BaseDataVariableTypeNode getTimeAwareOffsetNode();

  /**
   * Returns the Value of the TimeAwareOffset child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getTimeAwareOffset();

  /**
   * Sets the Value of the TimeAwareOffset child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTimeAwareOffset(@Nullable UInteger value);
}
