package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AuditHistoryRawModifyDeleteEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.6">Model
 *     documentation</a>
 */
public interface AuditHistoryRawModifyDeleteEventType extends AuditHistoryDeleteEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 3014L);

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
   * Returns the mandatory IsDeleteModified child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getIsDeleteModifiedNode();

  /**
   * Returns the Value of the IsDeleteModified child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getIsDeleteModified();

  /**
   * Sets the Value of the IsDeleteModified child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setIsDeleteModified(@Nullable Boolean value);

  /**
   * Returns the mandatory OldValues child, a PropertyType with DataType DataValue.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getOldValuesNode();

  /**
   * Returns the Value of the OldValues child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  DataValue @Nullable [] getOldValues();

  /**
   * Sets the Value of the OldValues child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setOldValues(DataValue @Nullable [] value);

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
}
