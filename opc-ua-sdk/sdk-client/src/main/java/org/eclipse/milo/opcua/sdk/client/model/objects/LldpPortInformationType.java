package org.eclipse.milo.opcua.sdk.client.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PortIdSubtype;
import org.eclipse.milo.opcua.stack.core.types.structured.LldpManagementAddressTxPortType;
import org.eclipse.milo.opcua.stack.core.util.Namespaces;
import org.jspecify.annotations.Nullable;

/**
 * Client API for the LldpPortInformationType ObjectType.
 *
 * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.5">Model
 *     documentation</a>
 */
public interface LldpPortInformationType extends BaseObjectType {
  ExpandedNodeId TYPE_ID = ExpandedNodeId.of(Namespaces.OPC_UA, 19009L);

  QualifiedProperty<PortIdSubtype> PortIdSubtype_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "PortIdSubtype",
          ExpandedNodeId.of(Namespaces.OPC_UA, 18949L),
          -1,
          PortIdSubtype.class);

  QualifiedProperty<UByte[]> DestMacAddress_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "DestMacAddress",
          ExpandedNodeId.of(Namespaces.OPC_UA, 3L),
          1,
          UByte[].class);

  QualifiedProperty<String> PortDescription_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "PortDescription",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<LldpManagementAddressTxPortType[]> ManagementAddressTxPort_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "ManagementAddressTxPort",
          ExpandedNodeId.of(Namespaces.OPC_UA, 18953L),
          1,
          LldpManagementAddressTxPortType[].class);

  QualifiedProperty<String> IetfBaseNetworkInterfaceName_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA,
          "IetfBaseNetworkInterfaceName",
          ExpandedNodeId.of(Namespaces.OPC_UA, 12L),
          -1,
          String.class);

  QualifiedProperty<String> PortId_PROPERTY =
      new QualifiedProperty<>(
          Namespaces.OPC_UA, "PortId", ExpandedNodeId.of(Namespaces.OPC_UA, 12L), -1, String.class);

  /**
   * Resolves the mandatory PortIdSubtype child, a PropertyType with DataType PortIdSubtype.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getPortIdSubtypeNode() throws UaException;

  /** Asynchronous form of {@link #getPortIdSubtypeNode()}. */
  CompletableFuture<? extends PropertyType> getPortIdSubtypeNodeAsync();

  /**
   * Reads the Value of the PortIdSubtype child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable PortIdSubtype readPortIdSubtype() throws UaException;

  /**
   * Writes the Value of the PortIdSubtype child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePortIdSubtype(@Nullable PortIdSubtype value) throws UaException;

  /** Asynchronous form of {@link #readPortIdSubtype()}. */
  CompletableFuture<? extends @Nullable PortIdSubtype> readPortIdSubtypeAsync();

  /** Asynchronous form of {@link #writePortIdSubtype}; completes with the operation status. */
  CompletableFuture<StatusCode> writePortIdSubtypeAsync(@Nullable PortIdSubtype value);

  /**
   * Resolves the mandatory DestMacAddress child, a PropertyType with DataType Byte.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getDestMacAddressNode() throws UaException;

  /** Asynchronous form of {@link #getDestMacAddressNode()}. */
  CompletableFuture<? extends PropertyType> getDestMacAddressNodeAsync();

  /**
   * Reads the Value of the DestMacAddress child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  UByte @Nullable [] readDestMacAddress() throws UaException;

  /**
   * Writes the Value of the DestMacAddress child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeDestMacAddress(UByte @Nullable [] value) throws UaException;

  /** Asynchronous form of {@link #readDestMacAddress()}. */
  CompletableFuture<? extends UByte @Nullable []> readDestMacAddressAsync();

  /** Asynchronous form of {@link #writeDestMacAddress}; completes with the operation status. */
  CompletableFuture<StatusCode> writeDestMacAddressAsync(UByte @Nullable [] value);

  /**
   * Resolves the optional PortDescription child, a PropertyType with DataType String.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getPortDescriptionNode() throws UaException;

  /** Asynchronous form of {@link #getPortDescriptionNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getPortDescriptionNodeAsync();

  /**
   * Reads the Value of the PortDescription child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readPortDescription() throws UaException;

  /**
   * Writes the Value of the PortDescription child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePortDescription(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readPortDescription()}. */
  CompletableFuture<? extends @Nullable String> readPortDescriptionAsync();

  /** Asynchronous form of {@link #writePortDescription}; completes with the operation status. */
  CompletableFuture<StatusCode> writePortDescriptionAsync(@Nullable String value);

  /**
   * Resolves the optional RemoteSystemsData child, a FolderType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.6">FolderType
   *     documentation</a>
   */
  @Nullable FolderType getRemoteSystemsDataNode() throws UaException;

  /** Asynchronous form of {@link #getRemoteSystemsDataNode()}. */
  CompletableFuture<? extends @Nullable FolderType> getRemoteSystemsDataNodeAsync();

  /**
   * Resolves the optional ManagementAddressTxPort child, a PropertyType with DataType
   * LldpManagementAddressTxPortType.
   *
   * @return the child, or null if it is absent.
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  @Nullable PropertyType getManagementAddressTxPortNode() throws UaException;

  /** Asynchronous form of {@link #getManagementAddressTxPortNode()}. */
  CompletableFuture<? extends @Nullable PropertyType> getManagementAddressTxPortNodeAsync();

  /**
   * Reads the Value of the ManagementAddressTxPort child from the server.
   *
   * @return the value, or null if the child is absent or the Value is null.
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable LldpManagementAddressTxPortType @Nullable [] readManagementAddressTxPort()
      throws UaException;

  /**
   * Writes the Value of the ManagementAddressTxPort child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeManagementAddressTxPort(@Nullable LldpManagementAddressTxPortType @Nullable [] value)
      throws UaException;

  /** Asynchronous form of {@link #readManagementAddressTxPort()}. */
  CompletableFuture<? extends @Nullable LldpManagementAddressTxPortType @Nullable []>
      readManagementAddressTxPortAsync();

  /**
   * Asynchronous form of {@link #writeManagementAddressTxPort}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeManagementAddressTxPortAsync(
      @Nullable LldpManagementAddressTxPortType @Nullable [] value);

  /**
   * Resolves the mandatory IetfBaseNetworkInterfaceName child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getIetfBaseNetworkInterfaceNameNode() throws UaException;

  /** Asynchronous form of {@link #getIetfBaseNetworkInterfaceNameNode()}. */
  CompletableFuture<? extends PropertyType> getIetfBaseNetworkInterfaceNameNodeAsync();

  /**
   * Reads the Value of the IetfBaseNetworkInterfaceName child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readIetfBaseNetworkInterfaceName() throws UaException;

  /**
   * Writes the Value of the IetfBaseNetworkInterfaceName child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writeIetfBaseNetworkInterfaceName(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readIetfBaseNetworkInterfaceName()}. */
  CompletableFuture<? extends @Nullable String> readIetfBaseNetworkInterfaceNameAsync();

  /**
   * Asynchronous form of {@link #writeIetfBaseNetworkInterfaceName}; completes with the operation
   * status.
   */
  CompletableFuture<StatusCode> writeIetfBaseNetworkInterfaceNameAsync(@Nullable String value);

  /**
   * Resolves the mandatory PortId child, a PropertyType with DataType String.
   *
   * @throws UaException if lookup or validation fails.
   * @see <a href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.3">PropertyType
   *     documentation</a>
   */
  PropertyType getPortIdNode() throws UaException;

  /** Asynchronous form of {@link #getPortIdNode()}. */
  CompletableFuture<? extends PropertyType> getPortIdNodeAsync();

  /**
   * Reads the Value of the PortId child from the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  @Nullable String readPortId() throws UaException;

  /**
   * Writes the Value of the PortId child to the server.
   *
   * @throws UaException if lookup, conversion or the operation fails.
   */
  void writePortId(@Nullable String value) throws UaException;

  /** Asynchronous form of {@link #readPortId()}. */
  CompletableFuture<? extends @Nullable String> readPortIdAsync();

  /** Asynchronous form of {@link #writePortId}; completes with the operation status. */
  CompletableFuture<StatusCode> writePortIdAsync(@Nullable String value);
}
