/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.2</a>
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

  MessageSecurityMode getSecurityMode();

  void setSecurityMode(MessageSecurityMode value);

  PropertyType getSecurityModeNode();

  String getSecurityGroupId();

  void setSecurityGroupId(String value);

  PropertyType getSecurityGroupIdNode();

  EndpointDescription[] getSecurityKeyServices();

  void setSecurityKeyServices(EndpointDescription[] value);

  PropertyType getSecurityKeyServicesNode();

  UInteger getMaxNetworkMessageSize();

  void setMaxNetworkMessageSize(UInteger value);

  PropertyType getMaxNetworkMessageSizeNode();

  KeyValuePair[] getGroupProperties();

  void setGroupProperties(KeyValuePair[] value);

  PropertyType getGroupPropertiesNode();

  PubSubStatusType getStatusNode();
}
