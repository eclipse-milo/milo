package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the ServerCapabilitiesType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.2">Model
 *     documentation</a>
 */
public interface ServerCapabilitiesType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2013L);

  /**
   * Returns the mandatory AggregateFunctions child, a FolderType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.6">FolderType
   *     documentation</a>
   */
  FolderTypeNode getAggregateFunctionsNode();

  /**
   * Returns the optional ConformanceUnits child, a PropertyType with DataType QualifiedName.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getConformanceUnitsNode();

  /**
   * Returns the Value of the ConformanceUnits child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  QualifiedName @Nullable [] getConformanceUnits();

  /**
   * Sets the Value of the ConformanceUnits child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setConformanceUnits(QualifiedName @Nullable [] value);

  /**
   * Returns the mandatory LocaleIdArray child, a PropertyType with DataType LocaleId.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getLocaleIdArrayNode();

  /**
   * Returns the Value of the LocaleIdArray child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String @Nullable [] getLocaleIdArray();

  /**
   * Sets the Value of the LocaleIdArray child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setLocaleIdArray(@Nullable String @Nullable [] value);

  /**
   * Returns the optional MaxArrayLength child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxArrayLengthNode();

  /**
   * Returns the Value of the MaxArrayLength child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxArrayLength();

  /**
   * Sets the Value of the MaxArrayLength child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxArrayLength(@Nullable UInteger value);

  /**
   * Returns the mandatory MaxBrowseContinuationPoints child, a PropertyType with DataType UInt16.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getMaxBrowseContinuationPointsNode();

  /**
   * Returns the Value of the MaxBrowseContinuationPoints child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getMaxBrowseContinuationPoints();

  /**
   * Sets the Value of the MaxBrowseContinuationPoints child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxBrowseContinuationPoints(@Nullable UShort value);

  /**
   * Returns the optional MaxByteStringLength child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxByteStringLengthNode();

  /**
   * Returns the Value of the MaxByteStringLength child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxByteStringLength();

  /**
   * Sets the Value of the MaxByteStringLength child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxByteStringLength(@Nullable UInteger value);

  /**
   * Returns the mandatory MaxHistoryContinuationPoints child, a PropertyType with DataType UInt16.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getMaxHistoryContinuationPointsNode();

  /**
   * Returns the Value of the MaxHistoryContinuationPoints child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getMaxHistoryContinuationPoints();

  /**
   * Sets the Value of the MaxHistoryContinuationPoints child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxHistoryContinuationPoints(@Nullable UShort value);

  /**
   * Returns the optional MaxLogObjectContinuationPoints child, a PropertyType with DataType UInt16.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxLogObjectContinuationPointsNode();

  /**
   * Returns the Value of the MaxLogObjectContinuationPoints child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getMaxLogObjectContinuationPoints();

  /**
   * Sets the Value of the MaxLogObjectContinuationPoints child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxLogObjectContinuationPoints(@Nullable UShort value);

  /**
   * Returns the optional MaxMonitoredItems child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxMonitoredItemsNode();

  /**
   * Returns the Value of the MaxMonitoredItems child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxMonitoredItems();

  /**
   * Sets the Value of the MaxMonitoredItems child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxMonitoredItems(@Nullable UInteger value);

  /**
   * Returns the optional MaxMonitoredItemsPerSubscription child, a PropertyType with DataType
   * UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxMonitoredItemsPerSubscriptionNode();

  /**
   * Returns the Value of the MaxMonitoredItemsPerSubscription child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxMonitoredItemsPerSubscription();

  /**
   * Sets the Value of the MaxMonitoredItemsPerSubscription child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxMonitoredItemsPerSubscription(@Nullable UInteger value);

  /**
   * Returns the optional MaxMonitoredItemsQueueSize child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxMonitoredItemsQueueSizeNode();

  /**
   * Returns the Value of the MaxMonitoredItemsQueueSize child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxMonitoredItemsQueueSize();

  /**
   * Sets the Value of the MaxMonitoredItemsQueueSize child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxMonitoredItemsQueueSize(@Nullable UInteger value);

  /**
   * Returns the mandatory MaxQueryContinuationPoints child, a PropertyType with DataType UInt16.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getMaxQueryContinuationPointsNode();

  /**
   * Returns the Value of the MaxQueryContinuationPoints child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getMaxQueryContinuationPoints();

  /**
   * Sets the Value of the MaxQueryContinuationPoints child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxQueryContinuationPoints(@Nullable UShort value);

  /**
   * Returns the optional MaxSelectClauseParameters child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxSelectClauseParametersNode();

  /**
   * Returns the Value of the MaxSelectClauseParameters child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxSelectClauseParameters();

  /**
   * Sets the Value of the MaxSelectClauseParameters child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxSelectClauseParameters(@Nullable UInteger value);

  /**
   * Returns the optional MaxSessions child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxSessionsNode();

  /**
   * Returns the Value of the MaxSessions child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxSessions();

  /**
   * Sets the Value of the MaxSessions child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxSessions(@Nullable UInteger value);

  /**
   * Returns the optional MaxStringLength child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxStringLengthNode();

  /**
   * Returns the Value of the MaxStringLength child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxStringLength();

  /**
   * Sets the Value of the MaxStringLength child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxStringLength(@Nullable UInteger value);

  /**
   * Returns the optional MaxSubscriptions child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxSubscriptionsNode();

  /**
   * Returns the Value of the MaxSubscriptions child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxSubscriptions();

  /**
   * Sets the Value of the MaxSubscriptions child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxSubscriptions(@Nullable UInteger value);

  /**
   * Returns the optional MaxSubscriptionsPerSession child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxSubscriptionsPerSessionNode();

  /**
   * Returns the Value of the MaxSubscriptionsPerSession child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxSubscriptionsPerSession();

  /**
   * Sets the Value of the MaxSubscriptionsPerSession child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxSubscriptionsPerSession(@Nullable UInteger value);

  /**
   * Returns the optional MaxWhereClauseParameters child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxWhereClauseParametersNode();

  /**
   * Returns the Value of the MaxWhereClauseParameters child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxWhereClauseParameters();

  /**
   * Sets the Value of the MaxWhereClauseParameters child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxWhereClauseParameters(@Nullable UInteger value);

  /**
   * Returns the mandatory MinSupportedSampleRate child, a PropertyType with DataType Duration.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getMinSupportedSampleRateNode();

  /**
   * Returns the Value of the MinSupportedSampleRate child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getMinSupportedSampleRate();

  /**
   * Sets the Value of the MinSupportedSampleRate child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMinSupportedSampleRate(@Nullable Double value);

  /**
   * Returns the mandatory ModellingRules child, a FolderType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.6">FolderType
   *     documentation</a>
   */
  FolderTypeNode getModellingRulesNode();

  /**
   * Returns the optional OperationLimits child, a OperationLimitsType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.3.11">OperationLimitsType
   *     documentation</a>
   */
  @Nullable OperationLimitsTypeNode getOperationLimitsNode();

  /**
   * Returns the optional RoleSet child, a RoleSetType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.1">RoleSetType
   *     documentation</a>
   */
  @Nullable RoleSetTypeNode getRoleSetNode();

  /**
   * Returns the mandatory ServerProfileArray child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getServerProfileArrayNode();

  /**
   * Returns the Value of the ServerProfileArray child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String @Nullable [] getServerProfileArray();

  /**
   * Sets the Value of the ServerProfileArray child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setServerProfileArray(@Nullable String @Nullable [] value);

  /**
   * Returns the mandatory SoftwareCertificates child, a PropertyType with DataType
   * SignedSoftwareCertificate.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSoftwareCertificatesNode();

  /**
   * Returns the Value of the SoftwareCertificates child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable SignedSoftwareCertificate @Nullable [] getSoftwareCertificates();

  /**
   * Sets the Value of the SoftwareCertificates child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setSoftwareCertificates(@Nullable SignedSoftwareCertificate @Nullable [] value);
}
