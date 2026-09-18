package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.QosDataType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the DatagramConnectionTransportType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.1/#9.3.1.1">Model
 *     documentation</a>
 */
public interface DatagramConnectionTransportType extends ConnectionTransportType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 15064L);

  /**
   * Returns the optional DatagramQos child, a PropertyType with DataType QosDataType.
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
  @Nullable QosDataType @Nullable [] getDatagramQos();

  /**
   * Sets the Value of the DatagramQos child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDatagramQos(@Nullable QosDataType @Nullable [] value);

  /**
   * Returns the mandatory DiscoveryAddress child, a NetworkAddressType.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a
   *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.5/#9.1.5.6">NetworkAddressType
   *     documentation</a>
   */
  NetworkAddressTypeNode getDiscoveryAddressNode();

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
   * Returns the optional DiscoveryMaxMessageSize child, a PropertyType with DataType UInt32.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getDiscoveryMaxMessageSizeNode();

  /**
   * Returns the Value of the DiscoveryMaxMessageSize child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable UInteger getDiscoveryMaxMessageSize();

  /**
   * Sets the Value of the DiscoveryMaxMessageSize child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDiscoveryMaxMessageSize(@Nullable UInteger value);

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
}
