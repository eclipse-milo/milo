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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.KeyValuePair;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.2</a>
 */
public interface PublishSubscribeType extends PubSubKeyServiceType {
  QualifiedProperty<String[]> SUPPORTED_TRANSPORT_PROFILES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SupportedTransportProfiles",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  QualifiedProperty<ULong> DEFAULT_DATAGRAM_PUBLISHER_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DefaultDatagramPublisherId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=9"),
          -1,
          ULong.class);

  QualifiedProperty<UInteger> CONFIGURATION_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConfigurationVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998"),
          -1,
          UInteger.class);

  QualifiedProperty<EndpointDescription[]> DEFAULT_SECURITY_KEY_SERVICES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "DefaultSecurityKeyServices",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=312"),
          1,
          EndpointDescription[].class);

  QualifiedProperty<KeyValuePair[]> CONFIGURATION_PROPERTIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ConfigurationProperties",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=14533"),
          1,
          KeyValuePair[].class);

  /**
   * Get the local value of the SupportedTransportProfiles Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SupportedTransportProfiles Node.
   * @throws UaException if an error occurs creating or getting the SupportedTransportProfiles Node.
   */
  String[] getSupportedTransportProfiles() throws UaException;

  /**
   * Set the local value of the SupportedTransportProfiles Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SupportedTransportProfiles Node.
   * @throws UaException if an error occurs creating or getting the SupportedTransportProfiles Node.
   */
  void setSupportedTransportProfiles(String[] value) throws UaException;

  /**
   * Read the value of the SupportedTransportProfiles Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link String[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String[] readSupportedTransportProfiles() throws UaException;

  /**
   * Write a new value for the SupportedTransportProfiles Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link String[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSupportedTransportProfiles(String[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSupportedTransportProfiles}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String[]> readSupportedTransportProfilesAsync();

  /**
   * An asynchronous implementation of {@link #writeSupportedTransportProfiles}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSupportedTransportProfilesAsync(String[] value);

  /**
   * Get the SupportedTransportProfiles {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SupportedTransportProfiles {@link PropertyType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSupportedTransportProfilesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSupportedTransportProfilesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSupportedTransportProfilesNodeAsync();

  /**
   * Get the local value of the DefaultDatagramPublisherId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DefaultDatagramPublisherId Node.
   * @throws UaException if an error occurs creating or getting the DefaultDatagramPublisherId Node.
   */
  ULong getDefaultDatagramPublisherId() throws UaException;

  /**
   * Set the local value of the DefaultDatagramPublisherId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DefaultDatagramPublisherId Node.
   * @throws UaException if an error occurs creating or getting the DefaultDatagramPublisherId Node.
   */
  void setDefaultDatagramPublisherId(ULong value) throws UaException;

  /**
   * Read the value of the DefaultDatagramPublisherId Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link ULong} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ULong readDefaultDatagramPublisherId() throws UaException;

  /**
   * Write a new value for the DefaultDatagramPublisherId Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link ULong} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDefaultDatagramPublisherId(ULong value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDefaultDatagramPublisherId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ULong> readDefaultDatagramPublisherIdAsync();

  /**
   * An asynchronous implementation of {@link #writeDefaultDatagramPublisherId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDefaultDatagramPublisherIdAsync(ULong value);

  /**
   * Get the DefaultDatagramPublisherId {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DefaultDatagramPublisherId {@link PropertyType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDefaultDatagramPublisherIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDefaultDatagramPublisherIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDefaultDatagramPublisherIdNodeAsync();

  /**
   * Get the local value of the ConfigurationVersion Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ConfigurationVersion Node.
   * @throws UaException if an error occurs creating or getting the ConfigurationVersion Node.
   */
  UInteger getConfigurationVersion() throws UaException;

  /**
   * Set the local value of the ConfigurationVersion Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ConfigurationVersion Node.
   * @throws UaException if an error occurs creating or getting the ConfigurationVersion Node.
   */
  void setConfigurationVersion(UInteger value) throws UaException;

  /**
   * Read the value of the ConfigurationVersion Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readConfigurationVersion() throws UaException;

  /**
   * Write a new value for the ConfigurationVersion Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeConfigurationVersion(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readConfigurationVersion}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readConfigurationVersionAsync();

  /**
   * An asynchronous implementation of {@link #writeConfigurationVersion}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeConfigurationVersionAsync(UInteger value);

  /**
   * Get the ConfigurationVersion {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ConfigurationVersion {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getConfigurationVersionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getConfigurationVersionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getConfigurationVersionNodeAsync();

  /**
   * Get the local value of the DefaultSecurityKeyServices Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the DefaultSecurityKeyServices Node.
   * @throws UaException if an error occurs creating or getting the DefaultSecurityKeyServices Node.
   */
  EndpointDescription[] getDefaultSecurityKeyServices() throws UaException;

  /**
   * Set the local value of the DefaultSecurityKeyServices Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the DefaultSecurityKeyServices Node.
   * @throws UaException if an error occurs creating or getting the DefaultSecurityKeyServices Node.
   */
  void setDefaultSecurityKeyServices(EndpointDescription[] value) throws UaException;

  /**
   * Read the value of the DefaultSecurityKeyServices Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link EndpointDescription[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  EndpointDescription[] readDefaultSecurityKeyServices() throws UaException;

  /**
   * Write a new value for the DefaultSecurityKeyServices Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link EndpointDescription[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeDefaultSecurityKeyServices(EndpointDescription[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readDefaultSecurityKeyServices}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends EndpointDescription[]> readDefaultSecurityKeyServicesAsync();

  /**
   * An asynchronous implementation of {@link #writeDefaultSecurityKeyServices}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeDefaultSecurityKeyServicesAsync(EndpointDescription[] value);

  /**
   * Get the DefaultSecurityKeyServices {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DefaultSecurityKeyServices {@link PropertyType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getDefaultSecurityKeyServicesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDefaultSecurityKeyServicesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getDefaultSecurityKeyServicesNodeAsync();

  /**
   * Get the local value of the ConfigurationProperties Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ConfigurationProperties Node.
   * @throws UaException if an error occurs creating or getting the ConfigurationProperties Node.
   */
  KeyValuePair[] getConfigurationProperties() throws UaException;

  /**
   * Set the local value of the ConfigurationProperties Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ConfigurationProperties Node.
   * @throws UaException if an error occurs creating or getting the ConfigurationProperties Node.
   */
  void setConfigurationProperties(KeyValuePair[] value) throws UaException;

  /**
   * Read the value of the ConfigurationProperties Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link KeyValuePair[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  KeyValuePair[] readConfigurationProperties() throws UaException;

  /**
   * Write a new value for the ConfigurationProperties Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link KeyValuePair[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeConfigurationProperties(KeyValuePair[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readConfigurationProperties}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends KeyValuePair[]> readConfigurationPropertiesAsync();

  /**
   * An asynchronous implementation of {@link #writeConfigurationProperties}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeConfigurationPropertiesAsync(KeyValuePair[] value);

  /**
   * Get the ConfigurationProperties {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ConfigurationProperties {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getConfigurationPropertiesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getConfigurationPropertiesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getConfigurationPropertiesNodeAsync();

  /**
   * Get the PublishedDataSets {@link DataSetFolderType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PublishedDataSets {@link DataSetFolderType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  DataSetFolderType getPublishedDataSetsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPublishedDataSetsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the DataSetFolderType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends DataSetFolderType> getPublishedDataSetsNodeAsync();

  /**
   * Get the SubscribedDataSets {@link SubscribedDataSetFolderType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SubscribedDataSets {@link SubscribedDataSetFolderType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  SubscribedDataSetFolderType getSubscribedDataSetsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSubscribedDataSetsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the SubscribedDataSetFolderType
   *     Node or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends SubscribedDataSetFolderType> getSubscribedDataSetsNodeAsync();

  /**
   * Get the PubSubConfiguration {@link PubSubConfigurationType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PubSubConfiguration {@link PubSubConfigurationType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PubSubConfigurationType getPubSubConfigurationNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPubSubConfigurationNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PubSubConfigurationType Node
   *     or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PubSubConfigurationType> getPubSubConfigurationNodeAsync();

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

  /**
   * Get the Diagnostics {@link PubSubDiagnosticsRootType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Diagnostics {@link PubSubDiagnosticsRootType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PubSubDiagnosticsRootType getDiagnosticsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDiagnosticsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PubSubDiagnosticsRootType Node
   *     or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PubSubDiagnosticsRootType> getDiagnosticsNodeAsync();

  /**
   * Get the PubSubCapablities {@link PubSubCapabilitiesType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PubSubCapablities {@link PubSubCapabilitiesType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PubSubCapabilitiesType getPubSubCapablitiesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPubSubCapablitiesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PubSubCapabilitiesType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PubSubCapabilitiesType> getPubSubCapablitiesNodeAsync();

  /**
   * Get the DataSetClasses {@link FolderType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DataSetClasses {@link FolderType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  FolderType getDataSetClassesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDataSetClassesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the FolderType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends FolderType> getDataSetClassesNodeAsync();
}
