/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.5">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.5</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
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

  /** Gets the existing node's local value. */
  @Nullable String getIetfBaseNetworkInterfaceName() throws UaException;

  /** Sets the existing node's local value. */
  void setIetfBaseNetworkInterfaceName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readIetfBaseNetworkInterfaceName() throws UaException;

  /** Writes the value remotely. */
  void writeIetfBaseNetworkInterfaceName(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readIetfBaseNetworkInterfaceNameAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeIetfBaseNetworkInterfaceNameAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getIetfBaseNetworkInterfaceNameNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getIetfBaseNetworkInterfaceNameNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UByte @Nullable [] getDestMacAddress() throws UaException;

  /** Sets the existing node's local value. */
  void setDestMacAddress(@Nullable UByte @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UByte @Nullable [] readDestMacAddress() throws UaException;

  /** Writes the value remotely. */
  void writeDestMacAddress(@Nullable UByte @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UByte @Nullable []> readDestMacAddressAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDestMacAddressAsync(@Nullable UByte @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDestMacAddressNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getDestMacAddressNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable PortIdSubtype getPortIdSubtype() throws UaException;

  /** Sets the existing node's local value. */
  void setPortIdSubtype(@Nullable PortIdSubtype value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable PortIdSubtype readPortIdSubtype() throws UaException;

  /** Writes the value remotely. */
  void writePortIdSubtype(@Nullable PortIdSubtype value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable PortIdSubtype> readPortIdSubtypeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePortIdSubtypeAsync(@Nullable PortIdSubtype value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPortIdSubtypeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getPortIdSubtypeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getPortId() throws UaException;

  /** Sets the existing node's local value. */
  void setPortId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readPortId() throws UaException;

  /** Writes the value remotely. */
  void writePortId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readPortIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePortIdAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPortIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getPortIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getPortDescription() throws UaException;

  /** Sets the existing node's local value. */
  void setPortDescription(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readPortDescription() throws UaException;

  /** Writes the value remotely. */
  void writePortDescription(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readPortDescriptionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePortDescriptionAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getPortDescriptionNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getPortDescriptionNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LldpManagementAddressTxPortType @Nullable [] getManagementAddressTxPort()
      throws UaException;

  /** Sets the existing node's local value. */
  void setManagementAddressTxPort(@Nullable LldpManagementAddressTxPortType @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LldpManagementAddressTxPortType @Nullable [] readManagementAddressTxPort()
      throws UaException;

  /** Writes the value remotely. */
  void writeManagementAddressTxPort(@Nullable LldpManagementAddressTxPortType @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LldpManagementAddressTxPortType @Nullable []>
      readManagementAddressTxPortAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeManagementAddressTxPortAsync(
      @Nullable LldpManagementAddressTxPortType @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getManagementAddressTxPortNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getManagementAddressTxPortNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable FolderType getRemoteSystemsDataNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable FolderType> getRemoteSystemsDataNodeAsync();
}
