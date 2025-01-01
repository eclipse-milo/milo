/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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

  /**
   * Get the local value of the SecurityMode Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SecurityMode Node.
   * @throws UaException if an error occurs creating or getting the SecurityMode Node.
   */
  MessageSecurityMode getSecurityMode() throws UaException;

  /**
   * Set the local value of the SecurityMode Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SecurityMode Node.
   * @throws UaException if an error occurs creating or getting the SecurityMode Node.
   */
  void setSecurityMode(MessageSecurityMode value) throws UaException;

  /**
   * Read the value of the SecurityMode Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link MessageSecurityMode} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  MessageSecurityMode readSecurityMode() throws UaException;

  /**
   * Write a new value for the SecurityMode Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link MessageSecurityMode} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSecurityMode(MessageSecurityMode value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSecurityMode}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends MessageSecurityMode> readSecurityModeAsync();

  /**
   * An asynchronous implementation of {@link #writeSecurityMode}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSecurityModeAsync(MessageSecurityMode value);

  /**
   * Get the SecurityMode {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SecurityMode {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSecurityModeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSecurityModeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSecurityModeNodeAsync();

  /**
   * Get the local value of the SecurityGroupId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SecurityGroupId Node.
   * @throws UaException if an error occurs creating or getting the SecurityGroupId Node.
   */
  String getSecurityGroupId() throws UaException;

  /**
   * Set the local value of the SecurityGroupId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SecurityGroupId Node.
   * @throws UaException if an error occurs creating or getting the SecurityGroupId Node.
   */
  void setSecurityGroupId(String value) throws UaException;

  /**
   * Read the value of the SecurityGroupId Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readSecurityGroupId() throws UaException;

  /**
   * Write a new value for the SecurityGroupId Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSecurityGroupId(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSecurityGroupId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readSecurityGroupIdAsync();

  /**
   * An asynchronous implementation of {@link #writeSecurityGroupId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSecurityGroupIdAsync(String value);

  /**
   * Get the SecurityGroupId {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SecurityGroupId {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSecurityGroupIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSecurityGroupIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSecurityGroupIdNodeAsync();

  /**
   * Get the local value of the SecurityKeyServices Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SecurityKeyServices Node.
   * @throws UaException if an error occurs creating or getting the SecurityKeyServices Node.
   */
  EndpointDescription[] getSecurityKeyServices() throws UaException;

  /**
   * Set the local value of the SecurityKeyServices Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SecurityKeyServices Node.
   * @throws UaException if an error occurs creating or getting the SecurityKeyServices Node.
   */
  void setSecurityKeyServices(EndpointDescription[] value) throws UaException;

  /**
   * Read the value of the SecurityKeyServices Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link EndpointDescription[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  EndpointDescription[] readSecurityKeyServices() throws UaException;

  /**
   * Write a new value for the SecurityKeyServices Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link EndpointDescription[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSecurityKeyServices(EndpointDescription[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSecurityKeyServices}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends EndpointDescription[]> readSecurityKeyServicesAsync();

  /**
   * An asynchronous implementation of {@link #writeSecurityKeyServices}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSecurityKeyServicesAsync(EndpointDescription[] value);

  /**
   * Get the SecurityKeyServices {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SecurityKeyServices {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSecurityKeyServicesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSecurityKeyServicesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSecurityKeyServicesNodeAsync();

  /**
   * Get the local value of the MaxNetworkMessageSize Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxNetworkMessageSize Node.
   * @throws UaException if an error occurs creating or getting the MaxNetworkMessageSize Node.
   */
  UInteger getMaxNetworkMessageSize() throws UaException;

  /**
   * Set the local value of the MaxNetworkMessageSize Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxNetworkMessageSize Node.
   * @throws UaException if an error occurs creating or getting the MaxNetworkMessageSize Node.
   */
  void setMaxNetworkMessageSize(UInteger value) throws UaException;

  /**
   * Read the value of the MaxNetworkMessageSize Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxNetworkMessageSize() throws UaException;

  /**
   * Write a new value for the MaxNetworkMessageSize Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxNetworkMessageSize(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxNetworkMessageSize}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxNetworkMessageSizeAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxNetworkMessageSize}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxNetworkMessageSizeAsync(UInteger value);

  /**
   * Get the MaxNetworkMessageSize {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxNetworkMessageSize {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxNetworkMessageSizeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxNetworkMessageSizeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxNetworkMessageSizeNodeAsync();

  /**
   * Get the local value of the GroupProperties Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the GroupProperties Node.
   * @throws UaException if an error occurs creating or getting the GroupProperties Node.
   */
  KeyValuePair[] getGroupProperties() throws UaException;

  /**
   * Set the local value of the GroupProperties Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the GroupProperties Node.
   * @throws UaException if an error occurs creating or getting the GroupProperties Node.
   */
  void setGroupProperties(KeyValuePair[] value) throws UaException;

  /**
   * Read the value of the GroupProperties Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link KeyValuePair[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  KeyValuePair[] readGroupProperties() throws UaException;

  /**
   * Write a new value for the GroupProperties Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link KeyValuePair[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeGroupProperties(KeyValuePair[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readGroupProperties}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends KeyValuePair[]> readGroupPropertiesAsync();

  /**
   * An asynchronous implementation of {@link #writeGroupProperties}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeGroupPropertiesAsync(KeyValuePair[] value);

  /**
   * Get the GroupProperties {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the GroupProperties {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getGroupPropertiesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getGroupPropertiesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getGroupPropertiesNodeAsync();

  /**
   * Get the Status {@link PubSubStatusType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Status {@link PubSubStatusType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PubSubStatusType getStatusNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getStatusNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PubSubStatusType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PubSubStatusType> getStatusNodeAsync();
}
