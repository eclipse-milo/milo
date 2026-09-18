package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.AddNodesItem;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AuditAddNodesEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.20">Model
 *     documentation</a>
 */
public interface AuditAddNodesEventType extends AuditNodeManagementEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2091L);

  /**
   * Returns the mandatory NodesToAdd child, a PropertyType with DataType AddNodesItem.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getNodesToAddNode();

  /**
   * Returns the Value of the NodesToAdd child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable AddNodesItem @Nullable [] getNodesToAdd();

  /**
   * Sets the Value of the NodesToAdd child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setNodesToAdd(@Nullable AddNodesItem @Nullable [] value);
}
