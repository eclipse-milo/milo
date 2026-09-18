package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the HistoryServerCapabilitiesType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.7.2">Model
 *     documentation</a>
 */
public interface HistoryServerCapabilitiesType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2330L);

  /**
   * Returns the mandatory AccessHistoryDataCapability child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getAccessHistoryDataCapabilityNode();

  /**
   * Returns the Value of the AccessHistoryDataCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getAccessHistoryDataCapability();

  /**
   * Sets the Value of the AccessHistoryDataCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAccessHistoryDataCapability(@Nullable Boolean value);

  /**
   * Returns the mandatory AccessHistoryEventsCapability child, a PropertyType with DataType
   * Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getAccessHistoryEventsCapabilityNode();

  /**
   * Returns the Value of the AccessHistoryEventsCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getAccessHistoryEventsCapability();

  /**
   * Sets the Value of the AccessHistoryEventsCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setAccessHistoryEventsCapability(@Nullable Boolean value);

  /**
   * Returns the mandatory AggregateFunctions child, a FolderType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.6">FolderType
   *     documentation</a>
   */
  FolderTypeNode getAggregateFunctionsNode();

  /**
   * Returns the mandatory DeleteAtTimeCapability child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getDeleteAtTimeCapabilityNode();

  /**
   * Returns the Value of the DeleteAtTimeCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getDeleteAtTimeCapability();

  /**
   * Sets the Value of the DeleteAtTimeCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDeleteAtTimeCapability(@Nullable Boolean value);

  /**
   * Returns the mandatory DeleteEventCapability child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getDeleteEventCapabilityNode();

  /**
   * Returns the Value of the DeleteEventCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getDeleteEventCapability();

  /**
   * Sets the Value of the DeleteEventCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDeleteEventCapability(@Nullable Boolean value);

  /**
   * Returns the mandatory DeleteRawCapability child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getDeleteRawCapabilityNode();

  /**
   * Returns the Value of the DeleteRawCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getDeleteRawCapability();

  /**
   * Sets the Value of the DeleteRawCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDeleteRawCapability(@Nullable Boolean value);

  /**
   * Returns the mandatory InsertAnnotationCapability child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getInsertAnnotationCapabilityNode();

  /**
   * Returns the Value of the InsertAnnotationCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getInsertAnnotationCapability();

  /**
   * Sets the Value of the InsertAnnotationCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setInsertAnnotationCapability(@Nullable Boolean value);

  /**
   * Returns the mandatory InsertDataCapability child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getInsertDataCapabilityNode();

  /**
   * Returns the Value of the InsertDataCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getInsertDataCapability();

  /**
   * Sets the Value of the InsertDataCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setInsertDataCapability(@Nullable Boolean value);

  /**
   * Returns the mandatory InsertEventCapability child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getInsertEventCapabilityNode();

  /**
   * Returns the Value of the InsertEventCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getInsertEventCapability();

  /**
   * Sets the Value of the InsertEventCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setInsertEventCapability(@Nullable Boolean value);

  /**
   * Returns the mandatory MaxReturnDataValues child, a PropertyType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getMaxReturnDataValuesNode();

  /**
   * Returns the Value of the MaxReturnDataValues child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxReturnDataValues();

  /**
   * Sets the Value of the MaxReturnDataValues child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxReturnDataValues(@Nullable UInteger value);

  /**
   * Returns the mandatory MaxReturnEventValues child, a PropertyType with DataType UInt32.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getMaxReturnEventValuesNode();

  /**
   * Returns the Value of the MaxReturnEventValues child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxReturnEventValues();

  /**
   * Sets the Value of the MaxReturnEventValues child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxReturnEventValues(@Nullable UInteger value);

  /**
   * Returns the mandatory ReplaceDataCapability child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getReplaceDataCapabilityNode();

  /**
   * Returns the Value of the ReplaceDataCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getReplaceDataCapability();

  /**
   * Sets the Value of the ReplaceDataCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setReplaceDataCapability(@Nullable Boolean value);

  /**
   * Returns the mandatory ReplaceEventCapability child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getReplaceEventCapabilityNode();

  /**
   * Returns the Value of the ReplaceEventCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getReplaceEventCapability();

  /**
   * Sets the Value of the ReplaceEventCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setReplaceEventCapability(@Nullable Boolean value);

  /**
   * Returns the optional ServerTimestampSupported child, a PropertyType with DataType Boolean.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getServerTimestampSupportedNode();

  /**
   * Returns the Value of the ServerTimestampSupported child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getServerTimestampSupported();

  /**
   * Sets the Value of the ServerTimestampSupported child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setServerTimestampSupported(@Nullable Boolean value);

  /**
   * Returns the mandatory UpdateDataCapability child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getUpdateDataCapabilityNode();

  /**
   * Returns the Value of the UpdateDataCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getUpdateDataCapability();

  /**
   * Sets the Value of the UpdateDataCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setUpdateDataCapability(@Nullable Boolean value);

  /**
   * Returns the mandatory UpdateEventCapability child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getUpdateEventCapabilityNode();

  /**
   * Returns the Value of the UpdateEventCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getUpdateEventCapability();

  /**
   * Sets the Value of the UpdateEventCapability child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setUpdateEventCapability(@Nullable Boolean value);
}
