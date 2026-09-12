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
  @Nullable MessageSecurityMode getSecurityMode() throws UaException;

  /** Sets the existing node's local value. */
  void setSecurityMode(@Nullable MessageSecurityMode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable MessageSecurityMode readSecurityMode() throws UaException;

  /** Writes the value remotely. */
  void writeSecurityMode(@Nullable MessageSecurityMode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable MessageSecurityMode> readSecurityModeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSecurityModeAsync(@Nullable MessageSecurityMode value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSecurityModeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSecurityModeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getSecurityGroupId() throws UaException;

  /** Sets the existing node's local value. */
  void setSecurityGroupId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readSecurityGroupId() throws UaException;

  /** Writes the value remotely. */
  void writeSecurityGroupId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readSecurityGroupIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSecurityGroupIdAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSecurityGroupIdNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getSecurityGroupIdNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable EndpointDescription @Nullable [] getSecurityKeyServices() throws UaException;

  /** Sets the existing node's local value. */
  void setSecurityKeyServices(@Nullable EndpointDescription @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable EndpointDescription @Nullable [] readSecurityKeyServices() throws UaException;

  /** Writes the value remotely. */
  void writeSecurityKeyServices(@Nullable EndpointDescription @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable EndpointDescription @Nullable []>
      readSecurityKeyServicesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSecurityKeyServicesAsync(
      @Nullable EndpointDescription @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getSecurityKeyServicesNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getSecurityKeyServicesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable UInteger getMaxNetworkMessageSize() throws UaException;

  /** Sets the existing node's local value. */
  void setMaxNetworkMessageSize(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable UInteger readMaxNetworkMessageSize() throws UaException;

  /** Writes the value remotely. */
  void writeMaxNetworkMessageSize(@Nullable UInteger value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable UInteger> readMaxNetworkMessageSizeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeMaxNetworkMessageSizeAsync(@Nullable UInteger value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getMaxNetworkMessageSizeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getMaxNetworkMessageSizeNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable KeyValuePair @Nullable [] getGroupProperties() throws UaException;

  /** Sets the existing node's local value. */
  void setGroupProperties(@Nullable KeyValuePair @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable KeyValuePair @Nullable [] readGroupProperties() throws UaException;

  /** Writes the value remotely. */
  void writeGroupProperties(@Nullable KeyValuePair @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable KeyValuePair @Nullable []> readGroupPropertiesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeGroupPropertiesAsync(
      @Nullable KeyValuePair @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getGroupPropertiesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getGroupPropertiesNodeAsync();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PubSubStatusType getStatusNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PubSubStatusType> getStatusNodeAsync();
}
