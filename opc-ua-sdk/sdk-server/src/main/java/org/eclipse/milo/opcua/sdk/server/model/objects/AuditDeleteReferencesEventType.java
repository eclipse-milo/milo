package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesItem;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AuditDeleteReferencesEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.23">Model
 *     documentation</a>
 */
public interface AuditDeleteReferencesEventType extends AuditNodeManagementEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2097L);

  /**
   * Returns the mandatory ReferencesToDelete child, a PropertyType with DataType
   * DeleteReferencesItem.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getReferencesToDeleteNode();

  /**
   * Returns the Value of the ReferencesToDelete child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DeleteReferencesItem @Nullable [] getReferencesToDelete();

  /**
   * Sets the Value of the ReferencesToDelete child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setReferencesToDelete(@Nullable DeleteReferencesItem @Nullable [] value);
}
