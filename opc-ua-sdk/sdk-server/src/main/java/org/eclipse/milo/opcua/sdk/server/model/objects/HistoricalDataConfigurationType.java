package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ExceptionDeviationFormat;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the HistoricalDataConfigurationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.2.2">Model
 *     documentation</a>
 */
public interface HistoricalDataConfigurationType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 2318L);

  /**
   * Returns the mandatory AggregateConfiguration child, a AggregateConfigurationType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part13/4.2.1/#4.2.1.2">AggregateConfigurationType
   *     documentation</a>
   */
  AggregateConfigurationTypeNode getAggregateConfigurationNode();

  /**
   * Returns the optional AggregateFunctions child, a FolderType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.6">FolderType
   *     documentation</a>
   */
  @Nullable FolderTypeNode getAggregateFunctionsNode();

  /**
   * Returns the optional Definition child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getDefinitionNode();

  /**
   * Returns the Value of the Definition child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getDefinition();

  /**
   * Sets the Value of the Definition child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDefinition(@Nullable String value);

  /**
   * Returns the optional ExceptionDeviation child, a PropertyType with DataType Double.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getExceptionDeviationNode();

  /**
   * Returns the Value of the ExceptionDeviation child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getExceptionDeviation();

  /**
   * Sets the Value of the ExceptionDeviation child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setExceptionDeviation(@Nullable Double value);

  /**
   * Returns the optional ExceptionDeviationFormat child, a PropertyType with DataType
   * ExceptionDeviationFormat.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getExceptionDeviationFormatNode();

  /**
   * Returns the Value of the ExceptionDeviationFormat child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ExceptionDeviationFormat getExceptionDeviationFormat();

  /**
   * Sets the Value of the ExceptionDeviationFormat child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setExceptionDeviationFormat(@Nullable ExceptionDeviationFormat value);

  /**
   * Returns the optional MaxCountStoredValues child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxCountStoredValuesNode();

  /**
   * Returns the Value of the MaxCountStoredValues child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getMaxCountStoredValues();

  /**
   * Sets the Value of the MaxCountStoredValues child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxCountStoredValues(@Nullable UInteger value);

  /**
   * Returns the optional MaxTimeInterval child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxTimeIntervalNode();

  /**
   * Returns the Value of the MaxTimeInterval child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getMaxTimeInterval();

  /**
   * Sets the Value of the MaxTimeInterval child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxTimeInterval(@Nullable Double value);

  /**
   * Returns the optional MaxTimeStoredValues child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMaxTimeStoredValuesNode();

  /**
   * Returns the Value of the MaxTimeStoredValues child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getMaxTimeStoredValues();

  /**
   * Sets the Value of the MaxTimeStoredValues child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMaxTimeStoredValues(@Nullable Double value);

  /**
   * Returns the optional MinTimeInterval child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMinTimeIntervalNode();

  /**
   * Returns the Value of the MinTimeInterval child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getMinTimeInterval();

  /**
   * Sets the Value of the MinTimeInterval child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMinTimeInterval(@Nullable Double value);

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
   * Returns the optional StartOfArchive child, a PropertyType with DataType UtcTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getStartOfArchiveNode();

  /**
   * Returns the Value of the StartOfArchive child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getStartOfArchive();

  /**
   * Sets the Value of the StartOfArchive child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setStartOfArchive(@Nullable DateTime value);

  /**
   * Returns the optional StartOfOnlineArchive child, a PropertyType with DataType UtcTime.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getStartOfOnlineArchiveNode();

  /**
   * Returns the Value of the StartOfOnlineArchive child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DateTime getStartOfOnlineArchive();

  /**
   * Sets the Value of the StartOfOnlineArchive child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setStartOfOnlineArchive(@Nullable DateTime value);

  /**
   * Returns the mandatory Stepped child, a PropertyType with DataType Boolean.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getSteppedNode();

  /**
   * Returns the Value of the Stepped child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Boolean getStepped();

  /**
   * Sets the Value of the Stepped child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setStepped(@Nullable Boolean value);
}
