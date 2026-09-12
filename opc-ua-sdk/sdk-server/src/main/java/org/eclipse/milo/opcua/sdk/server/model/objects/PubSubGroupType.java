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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface PubSubGroupType extends BaseObjectType {
  QualifiedProperty<MessageSecurityMode> SECURITY_MODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityMode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=302"),
          -1,
          MessageSecurityMode.class);

  QualifiedProperty<String> SECURITY_GROUP_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityGroupId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<EndpointDescription[]> SECURITY_KEY_SERVICES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityKeyServices",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=312"),
          1,
          EndpointDescription[].class);

  QualifiedProperty<UInteger> MAX_NETWORK_MESSAGE_SIZE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxNetworkMessageSize",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<KeyValuePair[]> GROUP_PROPERTIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "GroupProperties",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14533"),
          1,
          KeyValuePair[].class);

  /** Gets the existing node's local value. */
  @Nullable MessageSecurityMode getSecurityMode();

  /** Sets the existing node's local value. */
  void setSecurityMode(@Nullable MessageSecurityMode value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSecurityModeNode();

  /** Gets the existing node's local value. */
  @Nullable String getSecurityGroupId();

  /** Sets the existing node's local value. */
  void setSecurityGroupId(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSecurityGroupIdNode();

  /** Gets the existing node's local value. */
  @Nullable EndpointDescription @Nullable [] getSecurityKeyServices();

  /** Sets the existing node's local value. */
  void setSecurityKeyServices(@Nullable EndpointDescription @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSecurityKeyServicesNode();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNetworkMessageSize();

  /** Sets the existing node's local value. */
  void setMaxNetworkMessageSize(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxNetworkMessageSizeNode();

  /** Gets the existing node's local value. */
  @Nullable KeyValuePair @Nullable [] getGroupProperties();

  /** Sets the existing node's local value. */
  void setGroupProperties(@Nullable KeyValuePair @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getGroupPropertiesNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PubSubStatusType getStatusNode();
}
