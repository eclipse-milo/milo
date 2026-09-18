package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AuditHistoryBulkInsertEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.10">Model
 *     documentation</a>
 */
public interface AuditHistoryBulkInsertEventType extends AuditEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 32803L);

  /**
   * Returns the mandatory EndTime child, a PropertyType with DataType UtcTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getEndTimeNode();

  /**
   * Returns the Value of the EndTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getEndTime();

  /**
   * Sets the Value of the EndTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEndTime(@Nullable DateTime value);

  /**
   * Returns the mandatory StartTime child, a PropertyType with DataType UtcTime.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getStartTimeNode();

  /**
   * Returns the Value of the StartTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getStartTime();

  /**
   * Sets the Value of the StartTime child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setStartTime(@Nullable DateTime value);

  /**
   * Returns the mandatory UpdatedNode child, a PropertyType with DataType NodeId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getUpdatedNodeNode();

  /**
   * Returns the Value of the UpdatedNode child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable NodeId getUpdatedNode();

  /**
   * Sets the Value of the UpdatedNode child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setUpdatedNode(@Nullable NodeId value);
}
