/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.5">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.5</a>
 */
public interface LldpPortInformationType extends BaseObjectType {
  QualifiedProperty<String> IETF_BASE_NETWORK_INTERFACE_NAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IetfBaseNetworkInterfaceName",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<UByte[]> DEST_MAC_ADDRESS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DestMacAddress",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=3"),
          1,
          UByte[].class);

  QualifiedProperty<PortIdSubtype> PORT_ID_SUBTYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PortIdSubtype",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=18949"),
          -1,
          PortIdSubtype.class);

  QualifiedProperty<String> PORT_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PortId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> PORT_DESCRIPTION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PortDescription",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<LldpManagementAddressTxPortType[]> MANAGEMENT_ADDRESS_TX_PORT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ManagementAddressTxPort",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=18953"),
          1,
          LldpManagementAddressTxPortType[].class);

  /**
   * Get the local value of the IetfBaseNetworkInterfaceName Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the IetfBaseNetworkInterfaceName Node.
   * @throws UaException if an error occurs creating or getting the IetfBaseNetworkInterfaceName
   *     Node.
   */
  String getIetfBaseNetworkInterfaceName() throws UaException;

  /**
   * Set the local value of the IetfBaseNetworkInterfaceName Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the IetfBaseNetworkInterfaceName Node.
   * @throws UaException if an error occurs creating or getting the IetfBaseNetworkInterfaceName
   *     Node.
   */
  void setIetfBaseNetworkInterfaceName(String value) throws UaException;

  /**
   * Read the value of the IetfBaseNetworkInterfaceName Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readIetfBaseNetworkInterfaceName() throws UaException;

  /**
   * Write a new value for the IetfBaseNetworkInterfaceName Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeIetfBaseNetworkInterfaceName(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readIetfBaseNetworkInterfaceName}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readIetfBaseNetworkInterfaceNameAsync();

  /**
   * An asynchronous implementation of {@link #writeIetfBaseNetworkInterfaceName}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeIetfBaseNetworkInterfaceNameAsync(String value);

  /**
   * Get the IetfBaseNetworkInterfaceName {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the IetfBaseNetworkInterfaceName {@link PropertyType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getIetfBaseNetworkInterfaceNameNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getIetfBaseNetworkInterfaceNameNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getIetfBaseNetworkInterfaceNameNodeAsync();

  /**
   * Get the local value of the DestMacAddress Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DestMacAddress Node.
   * @throws UaException if an error occurs creating or getting the DestMacAddress Node.
   */
  UByte[] getDestMacAddress() throws UaException;

  /**
   * Set the local value of the DestMacAddress Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DestMacAddress Node.
   * @throws UaException if an error occurs creating or getting the DestMacAddress Node.
   */
  void setDestMacAddress(UByte[] value) throws UaException;

  /**
   * Read the value of the DestMacAddress Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UByte[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UByte[] readDestMacAddress() throws UaException;

  /**
   * Write a new value for the DestMacAddress Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UByte[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDestMacAddress(UByte[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDestMacAddress}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UByte[]> readDestMacAddressAsync();

  /**
   * An asynchronous implementation of {@link #writeDestMacAddress}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDestMacAddressAsync(UByte[] value);

  /**
   * Get the DestMacAddress {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DestMacAddress {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDestMacAddressNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDestMacAddressNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDestMacAddressNodeAsync();

  /**
   * Get the local value of the PortIdSubtype Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PortIdSubtype Node.
   * @throws UaException if an error occurs creating or getting the PortIdSubtype Node.
   */
  PortIdSubtype getPortIdSubtype() throws UaException;

  /**
   * Set the local value of the PortIdSubtype Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PortIdSubtype Node.
   * @throws UaException if an error occurs creating or getting the PortIdSubtype Node.
   */
  void setPortIdSubtype(PortIdSubtype value) throws UaException;

  /**
   * Read the value of the PortIdSubtype Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link PortIdSubtype} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  PortIdSubtype readPortIdSubtype() throws UaException;

  /**
   * Write a new value for the PortIdSubtype Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link PortIdSubtype} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePortIdSubtype(PortIdSubtype value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPortIdSubtype}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends PortIdSubtype> readPortIdSubtypeAsync();

  /**
   * An asynchronous implementation of {@link #writePortIdSubtype}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePortIdSubtypeAsync(PortIdSubtype value);

  /**
   * Get the PortIdSubtype {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PortIdSubtype {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getPortIdSubtypeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPortIdSubtypeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getPortIdSubtypeNodeAsync();

  /**
   * Get the local value of the PortId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PortId Node.
   * @throws UaException if an error occurs creating or getting the PortId Node.
   */
  String getPortId() throws UaException;

  /**
   * Set the local value of the PortId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PortId Node.
   * @throws UaException if an error occurs creating or getting the PortId Node.
   */
  void setPortId(String value) throws UaException;

  /**
   * Read the value of the PortId Node from the server and update the local value if the operation
   * succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readPortId() throws UaException;

  /**
   * Write a new value for the PortId Node to the server and update the local value if the operation
   * succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePortId(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPortId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readPortIdAsync();

  /**
   * An asynchronous implementation of {@link #writePortId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePortIdAsync(String value);

  /**
   * Get the PortId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PortId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getPortIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPortIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getPortIdNodeAsync();

  /**
   * Get the local value of the PortDescription Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PortDescription Node.
   * @throws UaException if an error occurs creating or getting the PortDescription Node.
   */
  String getPortDescription() throws UaException;

  /**
   * Set the local value of the PortDescription Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PortDescription Node.
   * @throws UaException if an error occurs creating or getting the PortDescription Node.
   */
  void setPortDescription(String value) throws UaException;

  /**
   * Read the value of the PortDescription Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readPortDescription() throws UaException;

  /**
   * Write a new value for the PortDescription Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePortDescription(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPortDescription}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readPortDescriptionAsync();

  /**
   * An asynchronous implementation of {@link #writePortDescription}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePortDescriptionAsync(String value);

  /**
   * Get the PortDescription {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PortDescription {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getPortDescriptionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPortDescriptionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getPortDescriptionNodeAsync();

  /**
   * Get the local value of the ManagementAddressTxPort Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ManagementAddressTxPort Node.
   * @throws UaException if an error occurs creating or getting the ManagementAddressTxPort Node.
   */
  LldpManagementAddressTxPortType[] getManagementAddressTxPort() throws UaException;

  /**
   * Set the local value of the ManagementAddressTxPort Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ManagementAddressTxPort Node.
   * @throws UaException if an error occurs creating or getting the ManagementAddressTxPort Node.
   */
  void setManagementAddressTxPort(LldpManagementAddressTxPortType[] value) throws UaException;

  /**
   * Read the value of the ManagementAddressTxPort Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link LldpManagementAddressTxPortType[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LldpManagementAddressTxPortType[] readManagementAddressTxPort() throws UaException;

  /**
   * Write a new value for the ManagementAddressTxPort Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link LldpManagementAddressTxPortType[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeManagementAddressTxPort(LldpManagementAddressTxPortType[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readManagementAddressTxPort}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LldpManagementAddressTxPortType[]> readManagementAddressTxPortAsync();

  /**
   * An asynchronous implementation of {@link #writeManagementAddressTxPort}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeManagementAddressTxPortAsync(
      LldpManagementAddressTxPortType[] value);

  /**
   * Get the ManagementAddressTxPort {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ManagementAddressTxPort {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getManagementAddressTxPortNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getManagementAddressTxPortNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getManagementAddressTxPortNodeAsync();

  /**
   * Get the RemoteSystemsData {@link FolderType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RemoteSystemsData {@link FolderType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  FolderType getRemoteSystemsDataNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRemoteSystemsDataNode()}.
   *
   * @return a CompletableFuture that completes successfully with the FolderType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends FolderType> getRemoteSystemsDataNodeAsync();
}
