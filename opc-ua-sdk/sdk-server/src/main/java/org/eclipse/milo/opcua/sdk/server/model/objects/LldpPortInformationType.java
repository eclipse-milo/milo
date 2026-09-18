package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpManagementAddressTxPortType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Server API for the LldpPortInformationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.5">Model
 *     documentation</a>
 */
public interface LldpPortInformationType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 19009L);

  /**
   * Returns the mandatory DestMacAddress child, a PropertyType with DataType Byte.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getDestMacAddressNode();

  /**
   * Returns the Value of the DestMacAddress child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  UByte @Nullable [] getDestMacAddress();

  /**
   * Sets the Value of the DestMacAddress child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setDestMacAddress(UByte @Nullable [] value);

  /**
   * Returns the mandatory IetfBaseNetworkInterfaceName child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getIetfBaseNetworkInterfaceNameNode();

  /**
   * Returns the Value of the IetfBaseNetworkInterfaceName child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getIetfBaseNetworkInterfaceName();

  /**
   * Sets the Value of the IetfBaseNetworkInterfaceName child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setIetfBaseNetworkInterfaceName(@Nullable String value);

  /**
   * Returns the optional ManagementAddressTxPort child, a PropertyType with DataType
   * LldpManagementAddressTxPortType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getManagementAddressTxPortNode();

  /**
   * Returns the Value of the ManagementAddressTxPort child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable LldpManagementAddressTxPortType @Nullable [] getManagementAddressTxPort();

  /**
   * Sets the Value of the ManagementAddressTxPort child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setManagementAddressTxPort(@Nullable LldpManagementAddressTxPortType @Nullable [] value);

  /**
   * Returns the optional PortDescription child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyTypeNode getPortDescriptionNode();

  /**
   * Returns the Value of the PortDescription child.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getPortDescription();

  /**
   * Sets the Value of the PortDescription child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPortDescription(@Nullable String value);

  /**
   * Returns the mandatory PortId child, a PropertyType with DataType String.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getPortIdNode();

  /**
   * Returns the Value of the PortId child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable String getPortId();

  /**
   * Sets the Value of the PortId child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPortId(@Nullable String value);

  /**
   * Returns the mandatory PortIdSubtype child, a PropertyType with DataType PortIdSubtype.
   *
   * @throws UaRuntimeException if the child is absent, ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyTypeNode getPortIdSubtypeNode();

  /**
   * Returns the Value of the PortIdSubtype child.
   *
   * @throws UaRuntimeException if the child is invalid or the Value does not convert.
   */
  @Nullable PortIdSubtype getPortIdSubtype();

  /**
   * Sets the Value of the PortIdSubtype child.
   *
   * @throws UaRuntimeException if the child is invalid or the value does not convert.
   */
  void setPortIdSubtype(@Nullable PortIdSubtype value);

  /**
   * Returns the optional RemoteSystemsData child, a FolderType.
   *
   * @return the child, or null if it is absent.
   * @throws UaRuntimeException if the child is ambiguous or incompatible.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.6">FolderType
   *     documentation</a>
   */
  @Nullable FolderTypeNode getRemoteSystemsDataNode();
}
