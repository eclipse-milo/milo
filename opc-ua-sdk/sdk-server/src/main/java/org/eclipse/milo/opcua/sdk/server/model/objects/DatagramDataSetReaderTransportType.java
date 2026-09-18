package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReceiveQosDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the DatagramDataSetReaderTransportType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.4">Model
 *     documentation</a>
 */
public interface DatagramDataSetReaderTransportType extends DataSetReaderTransportType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 24016L);

  /**
   * Returns the optional Address child, a NetworkAddressType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.6">NetworkAddressType
   *     documentation</a>
   */
  @Nullable NetworkAddressTypeNode getAddressNode();

  /**
   * Returns the optional DatagramQos child, a PropertyType with DataType ReceiveQosDataType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getDatagramQosNode();

  /**
   * Returns the Value of the DatagramQos child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable ReceiveQosDataType @Nullable [] getDatagramQos();

  /**
   * Sets the Value of the DatagramQos child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDatagramQos(@Nullable ReceiveQosDataType @Nullable [] value);

  /**
   * Returns the optional QosCategory child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getQosCategoryNode();

  /**
   * Returns the Value of the QosCategory child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getQosCategory();

  /**
   * Sets the Value of the QosCategory child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setQosCategory(@Nullable String value);

  /**
   * Returns the optional Topic child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getTopicNode();

  /**
   * Returns the Value of the Topic child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getTopic();

  /**
   * Sets the Value of the Topic child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setTopic(@Nullable String value);
}
