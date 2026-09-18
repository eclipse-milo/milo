package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AuditConditionRespondEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.10.5">Model
 *     documentation</a>
 */
public interface AuditConditionRespondEventType extends AuditConditionEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 8927L);

  /**
   * Returns the mandatory SelectedResponse child, a PropertyType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSelectedResponseNode();

  /**
   * Returns the Value of the SelectedResponse child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getSelectedResponse();

  /**
   * Sets the Value of the SelectedResponse child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSelectedResponse(@Nullable UInteger value);
}
