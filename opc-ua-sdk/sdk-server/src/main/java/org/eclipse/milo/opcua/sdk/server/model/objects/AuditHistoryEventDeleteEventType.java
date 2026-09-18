package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryEventFieldList;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AuditHistoryEventDeleteEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.8">Model
 *     documentation</a>
 */
public interface AuditHistoryEventDeleteEventType extends AuditHistoryDeleteEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 3022L);

  /**
   * Returns the mandatory EventIds child, a PropertyType with DataType ByteString.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getEventIdsNode();

  /**
   * Returns the Value of the EventIds child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  ByteString @Nullable [] getEventIds();

  /**
   * Sets the Value of the EventIds child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setEventIds(ByteString @Nullable [] value);

  /**
   * Returns the mandatory OldValues child, a PropertyType with DataType HistoryEventFieldList.
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
  @Nullable HistoryEventFieldList getOldValues();

  /**
   * Sets the Value of the OldValues child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setOldValues(@Nullable HistoryEventFieldList value);
}
