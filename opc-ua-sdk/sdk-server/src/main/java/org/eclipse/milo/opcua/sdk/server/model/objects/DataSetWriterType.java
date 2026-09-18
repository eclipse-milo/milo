package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetFieldContentMask;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the DataSetWriterType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.7/#9.1.7.2">Model
 *     documentation</a>
 */
public interface DataSetWriterType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15298L);

  /**
   * Returns the mandatory DataSetFieldContentMask child, a PropertyType with DataType
   * DataSetFieldContentMask.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getDataSetFieldContentMaskNode();

  /**
   * Returns the Value of the DataSetFieldContentMask child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable DataSetFieldContentMask getDataSetFieldContentMask();

  /**
   * Sets the Value of the DataSetFieldContentMask child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDataSetFieldContentMask(@Nullable DataSetFieldContentMask value);

  /**
   * Returns the mandatory DataSetWriterId child, a PropertyType with DataType UInt16.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getDataSetWriterIdNode();

  /**
   * Returns the Value of the DataSetWriterId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UShort getDataSetWriterId();

  /**
   * Sets the Value of the DataSetWriterId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDataSetWriterId(@Nullable UShort value);

  /**
   * Returns the mandatory DataSetWriterProperties child, a PropertyType with DataType KeyValuePair.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getDataSetWriterPropertiesNode();

  /**
   * Returns the Value of the DataSetWriterProperties child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable KeyValuePair @Nullable [] getDataSetWriterProperties();

  /**
   * Sets the Value of the DataSetWriterProperties child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDataSetWriterProperties(@Nullable KeyValuePair @Nullable [] value);

  /**
   * Returns the optional Diagnostics child, a PubSubDiagnosticsDataSetWriterType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.11/#9.1.11.11">PubSubDiagnosticsDataSetWriterType
   *     documentation</a>
   */
  @Nullable PubSubDiagnosticsDataSetWriterTypeNode getDiagnosticsNode();

  /**
   * Returns the optional KeyFrameCount child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getKeyFrameCountNode();

  /**
   * Returns the Value of the KeyFrameCount child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getKeyFrameCount();

  /**
   * Sets the Value of the KeyFrameCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setKeyFrameCount(@Nullable UInteger value);

  /**
   * Returns the optional MessageSettings child, a DataSetWriterMessageType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.7/#9.1.7.4">DataSetWriterMessageType
   *     documentation</a>
   */
  @Nullable DataSetWriterMessageTypeNode getMessageSettingsNode();

  /**
   * Returns the mandatory Status child, a PubSubStatusType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.1">PubSubStatusType
   *     documentation</a>
   */
  PubSubStatusTypeNode getStatusNode();

  /**
   * Returns the optional TransportSettings child, a DataSetWriterTransportType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.7/#9.1.7.3">DataSetWriterTransportType
   *     documentation</a>
   */
  @Nullable DataSetWriterTransportTypeNode getTransportSettingsNode();
}
