package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType;
import org.eclipse.milo.opcua.stack.core.types.structured.EventFilter;
import org.eclipse.milo.opcua.stack.core.types.structured.HistoryEventFieldList;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the AuditHistoryEventUpdateEventType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.2">Model
 *     documentation</a>
 */
public interface AuditHistoryEventUpdateEventType extends AuditHistoryUpdateEventType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2999L);

  /**
   * Returns the mandatory Filter child, a PropertyType with DataType EventFilter.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getFilterNode();

  /**
   * Returns the Value of the Filter child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable EventFilter getFilter();

  /**
   * Sets the Value of the Filter child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setFilter(@Nullable EventFilter value);

  /**
   * Returns the mandatory NewValues child, a PropertyType with DataType HistoryEventFieldList.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getNewValuesNode();

  /**
   * Returns the Value of the NewValues child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable HistoryEventFieldList @Nullable [] getNewValues();

  /**
   * Sets the Value of the NewValues child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setNewValues(@Nullable HistoryEventFieldList @Nullable [] value);

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
  @Nullable HistoryEventFieldList @Nullable [] getOldValues();

  /**
   * Sets the Value of the OldValues child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setOldValues(@Nullable HistoryEventFieldList @Nullable [] value);

  /**
   * Returns the mandatory PerformInsertReplace child, a PropertyType with DataType
   * PerformUpdateType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getPerformInsertReplaceNode();

  /**
   * Returns the Value of the PerformInsertReplace child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable PerformUpdateType getPerformInsertReplace();

  /**
   * Sets the Value of the PerformInsertReplace child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPerformInsertReplace(@Nullable PerformUpdateType value);

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
