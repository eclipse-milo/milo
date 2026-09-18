package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.TransmitQosDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the DatagramWriterGroupTransportType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.2">Model
 *     documentation</a>
 */
public interface DatagramWriterGroupTransportType extends WriterGroupTransportType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 21133L);

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
   * Returns the optional DatagramQos child, a PropertyType with DataType TransmitQosDataType.
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
  @Nullable TransmitQosDataType @Nullable [] getDatagramQos();

  /**
   * Sets the Value of the DatagramQos child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDatagramQos(@Nullable TransmitQosDataType @Nullable [] value);

  /**
   * Returns the optional DiscoveryAnnounceRate child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getDiscoveryAnnounceRateNode();

  /**
   * Returns the Value of the DiscoveryAnnounceRate child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getDiscoveryAnnounceRate();

  /**
   * Sets the Value of the DiscoveryAnnounceRate child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDiscoveryAnnounceRate(@Nullable UInteger value);

  /**
   * Returns the optional MessageRepeatCount child, a PropertyType with DataType Byte.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMessageRepeatCountNode();

  /**
   * Returns the Value of the MessageRepeatCount child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UByte getMessageRepeatCount();

  /**
   * Sets the Value of the MessageRepeatCount child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMessageRepeatCount(@Nullable UByte value);

  /**
   * Returns the optional MessageRepeatDelay child, a PropertyType with DataType Duration.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getMessageRepeatDelayNode();

  /**
   * Returns the Value of the MessageRepeatDelay child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable Double getMessageRepeatDelay();

  /**
   * Sets the Value of the MessageRepeatDelay child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setMessageRepeatDelay(@Nullable Double value);

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
