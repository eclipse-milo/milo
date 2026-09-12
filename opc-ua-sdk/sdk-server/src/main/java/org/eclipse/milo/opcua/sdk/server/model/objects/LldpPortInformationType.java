/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
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
  @Nullable String getIetfBaseNetworkInterfaceName();

  /** Sets the existing node's local value. */
  void setIetfBaseNetworkInterfaceName(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getIetfBaseNetworkInterfaceNameNode();

  /** Gets the existing node's local value. */
  @Nullable UByte @Nullable [] getDestMacAddress();

  /** Sets the existing node's local value. */
  void setDestMacAddress(@Nullable UByte @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getDestMacAddressNode();

  /** Gets the existing node's local value. */
  @Nullable PortIdSubtype getPortIdSubtype();

  /** Sets the existing node's local value. */
  void setPortIdSubtype(@Nullable PortIdSubtype value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPortIdSubtypeNode();

  /** Gets the existing node's local value. */
  @Nullable String getPortId();

  /** Sets the existing node's local value. */
  void setPortId(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPortIdNode();

  /** Gets the existing node's local value. */
  @Nullable String getPortDescription();

  /** Sets the existing node's local value. */
  void setPortDescription(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getPortDescriptionNode();

  /** Gets the existing node's local value. */
  @Nullable LldpManagementAddressTxPortType @Nullable [] getManagementAddressTxPort();

  /** Sets the existing node's local value. */
  void setManagementAddressTxPort(@Nullable LldpManagementAddressTxPortType @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getManagementAddressTxPortNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable FolderType getRemoteSystemsDataNode();
}
