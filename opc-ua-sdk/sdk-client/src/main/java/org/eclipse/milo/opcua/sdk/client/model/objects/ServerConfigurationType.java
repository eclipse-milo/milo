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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.4">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.4</a>
 */
public interface ServerConfigurationType extends BaseObjectType {
  QualifiedProperty<String> APPLICATION_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ApplicationUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23751"),
          -1,
          String.class);

  QualifiedProperty<String> PRODUCT_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ProductUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23751"),
          -1,
          String.class);

  QualifiedProperty<ApplicationType> APPLICATION_TYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ApplicationType",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=307"),
          -1,
          ApplicationType.class);

  QualifiedProperty<LocalizedText[]> APPLICATION_NAMES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ApplicationNames",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          1,
          LocalizedText[].class);

  QualifiedProperty<String[]> SERVER_CAPABILITIES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServerCapabilities",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  QualifiedProperty<String[]> SUPPORTED_PRIVATE_KEY_FORMATS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SupportedPrivateKeyFormats",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          1,
          String[].class);

  QualifiedProperty<UInteger> MAX_TRUST_LIST_SIZE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxTrustListSize",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=7"),
          -1,
          UInteger.class);

  QualifiedProperty<Boolean> MULTICAST_DNS_ENABLED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MulticastDnsEnabled",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> HAS_SECURE_ELEMENT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "HasSecureElement",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> SUPPORTS_TRANSACTIONS =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SupportsTransactions",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Boolean> IN_APPLICATION_SETUP =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InApplicationSetup",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  /**
   * Get the local value of the ApplicationUri Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ApplicationUri Node.
   * @throws UaException if an error occurs creating or getting the ApplicationUri Node.
   */
  String getApplicationUri() throws UaException;

  /**
   * Set the local value of the ApplicationUri Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ApplicationUri Node.
   * @throws UaException if an error occurs creating or getting the ApplicationUri Node.
   */
  void setApplicationUri(String value) throws UaException;

  /**
   * Read the value of the ApplicationUri Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readApplicationUri() throws UaException;

  /**
   * Write a new value for the ApplicationUri Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeApplicationUri(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readApplicationUri}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readApplicationUriAsync();

  /**
   * An asynchronous implementation of {@link #writeApplicationUri}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeApplicationUriAsync(String value);

  /**
   * Get the ApplicationUri {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ApplicationUri {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getApplicationUriNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getApplicationUriNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getApplicationUriNodeAsync();

  /**
   * Get the local value of the ProductUri Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ProductUri Node.
   * @throws UaException if an error occurs creating or getting the ProductUri Node.
   */
  String getProductUri() throws UaException;

  /**
   * Set the local value of the ProductUri Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ProductUri Node.
   * @throws UaException if an error occurs creating or getting the ProductUri Node.
   */
  void setProductUri(String value) throws UaException;

  /**
   * Read the value of the ProductUri Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readProductUri() throws UaException;

  /**
   * Write a new value for the ProductUri Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeProductUri(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readProductUri}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readProductUriAsync();

  /**
   * An asynchronous implementation of {@link #writeProductUri}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeProductUriAsync(String value);

  /**
   * Get the ProductUri {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ProductUri {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getProductUriNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getProductUriNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getProductUriNodeAsync();

  /**
   * Get the local value of the ApplicationType Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ApplicationType Node.
   * @throws UaException if an error occurs creating or getting the ApplicationType Node.
   */
  ApplicationType getApplicationType() throws UaException;

  /**
   * Set the local value of the ApplicationType Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ApplicationType Node.
   * @throws UaException if an error occurs creating or getting the ApplicationType Node.
   */
  void setApplicationType(ApplicationType value) throws UaException;

  /**
   * Read the value of the ApplicationType Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ApplicationType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ApplicationType readApplicationType() throws UaException;

  /**
   * Write a new value for the ApplicationType Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link ApplicationType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeApplicationType(ApplicationType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readApplicationType}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ApplicationType> readApplicationTypeAsync();

  /**
   * An asynchronous implementation of {@link #writeApplicationType}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeApplicationTypeAsync(ApplicationType value);

  /**
   * Get the ApplicationType {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ApplicationType {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getApplicationTypeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getApplicationTypeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getApplicationTypeNodeAsync();

  /**
   * Get the local value of the ApplicationNames Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ApplicationNames Node.
   * @throws UaException if an error occurs creating or getting the ApplicationNames Node.
   */
  LocalizedText[] getApplicationNames() throws UaException;

  /**
   * Set the local value of the ApplicationNames Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ApplicationNames Node.
   * @throws UaException if an error occurs creating or getting the ApplicationNames Node.
   */
  void setApplicationNames(LocalizedText[] value) throws UaException;

  /**
   * Read the value of the ApplicationNames Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText[] readApplicationNames() throws UaException;

  /**
   * Write a new value for the ApplicationNames Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeApplicationNames(LocalizedText[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readApplicationNames}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText[]> readApplicationNamesAsync();

  /**
   * An asynchronous implementation of {@link #writeApplicationNames}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeApplicationNamesAsync(LocalizedText[] value);

  /**
   * Get the ApplicationNames {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ApplicationNames {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getApplicationNamesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getApplicationNamesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getApplicationNamesNodeAsync();

  /**
   * Get the local value of the ServerCapabilities Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ServerCapabilities Node.
   * @throws UaException if an error occurs creating or getting the ServerCapabilities Node.
   */
  String[] getServerCapabilities() throws UaException;

  /**
   * Set the local value of the ServerCapabilities Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ServerCapabilities Node.
   * @throws UaException if an error occurs creating or getting the ServerCapabilities Node.
   */
  void setServerCapabilities(String[] value) throws UaException;

  /**
   * Read the value of the ServerCapabilities Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String[] readServerCapabilities() throws UaException;

  /**
   * Write a new value for the ServerCapabilities Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link String[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeServerCapabilities(String[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readServerCapabilities}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String[]> readServerCapabilitiesAsync();

  /**
   * An asynchronous implementation of {@link #writeServerCapabilities}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeServerCapabilitiesAsync(String[] value);

  /**
   * Get the ServerCapabilities {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ServerCapabilities {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getServerCapabilitiesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getServerCapabilitiesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getServerCapabilitiesNodeAsync();

  /**
   * Get the local value of the SupportedPrivateKeyFormats Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SupportedPrivateKeyFormats Node.
   * @throws UaException if an error occurs creating or getting the SupportedPrivateKeyFormats Node.
   */
  String[] getSupportedPrivateKeyFormats() throws UaException;

  /**
   * Set the local value of the SupportedPrivateKeyFormats Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SupportedPrivateKeyFormats Node.
   * @throws UaException if an error occurs creating or getting the SupportedPrivateKeyFormats Node.
   */
  void setSupportedPrivateKeyFormats(String[] value) throws UaException;

  /**
   * Read the value of the SupportedPrivateKeyFormats Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link String[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String[] readSupportedPrivateKeyFormats() throws UaException;

  /**
   * Write a new value for the SupportedPrivateKeyFormats Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link String[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSupportedPrivateKeyFormats(String[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSupportedPrivateKeyFormats}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String[]> readSupportedPrivateKeyFormatsAsync();

  /**
   * An asynchronous implementation of {@link #writeSupportedPrivateKeyFormats}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSupportedPrivateKeyFormatsAsync(String[] value);

  /**
   * Get the SupportedPrivateKeyFormats {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SupportedPrivateKeyFormats {@link PropertyType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSupportedPrivateKeyFormatsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSupportedPrivateKeyFormatsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSupportedPrivateKeyFormatsNodeAsync();

  /**
   * Get the local value of the MaxTrustListSize Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MaxTrustListSize Node.
   * @throws UaException if an error occurs creating or getting the MaxTrustListSize Node.
   */
  UInteger getMaxTrustListSize() throws UaException;

  /**
   * Set the local value of the MaxTrustListSize Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MaxTrustListSize Node.
   * @throws UaException if an error occurs creating or getting the MaxTrustListSize Node.
   */
  void setMaxTrustListSize(UInteger value) throws UaException;

  /**
   * Read the value of the MaxTrustListSize Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readMaxTrustListSize() throws UaException;

  /**
   * Write a new value for the MaxTrustListSize Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMaxTrustListSize(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMaxTrustListSize}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readMaxTrustListSizeAsync();

  /**
   * An asynchronous implementation of {@link #writeMaxTrustListSize}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMaxTrustListSizeAsync(UInteger value);

  /**
   * Get the MaxTrustListSize {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MaxTrustListSize {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMaxTrustListSizeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMaxTrustListSizeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMaxTrustListSizeNodeAsync();

  /**
   * Get the local value of the MulticastDnsEnabled Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the MulticastDnsEnabled Node.
   * @throws UaException if an error occurs creating or getting the MulticastDnsEnabled Node.
   */
  Boolean getMulticastDnsEnabled() throws UaException;

  /**
   * Set the local value of the MulticastDnsEnabled Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the MulticastDnsEnabled Node.
   * @throws UaException if an error occurs creating or getting the MulticastDnsEnabled Node.
   */
  void setMulticastDnsEnabled(Boolean value) throws UaException;

  /**
   * Read the value of the MulticastDnsEnabled Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readMulticastDnsEnabled() throws UaException;

  /**
   * Write a new value for the MulticastDnsEnabled Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeMulticastDnsEnabled(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readMulticastDnsEnabled}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readMulticastDnsEnabledAsync();

  /**
   * An asynchronous implementation of {@link #writeMulticastDnsEnabled}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeMulticastDnsEnabledAsync(Boolean value);

  /**
   * Get the MulticastDnsEnabled {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MulticastDnsEnabled {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getMulticastDnsEnabledNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMulticastDnsEnabledNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getMulticastDnsEnabledNodeAsync();

  /**
   * Get the local value of the HasSecureElement Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the HasSecureElement Node.
   * @throws UaException if an error occurs creating or getting the HasSecureElement Node.
   */
  Boolean getHasSecureElement() throws UaException;

  /**
   * Set the local value of the HasSecureElement Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the HasSecureElement Node.
   * @throws UaException if an error occurs creating or getting the HasSecureElement Node.
   */
  void setHasSecureElement(Boolean value) throws UaException;

  /**
   * Read the value of the HasSecureElement Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readHasSecureElement() throws UaException;

  /**
   * Write a new value for the HasSecureElement Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeHasSecureElement(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readHasSecureElement}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readHasSecureElementAsync();

  /**
   * An asynchronous implementation of {@link #writeHasSecureElement}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeHasSecureElementAsync(Boolean value);

  /**
   * Get the HasSecureElement {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the HasSecureElement {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getHasSecureElementNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getHasSecureElementNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getHasSecureElementNodeAsync();

  /**
   * Get the local value of the SupportsTransactions Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SupportsTransactions Node.
   * @throws UaException if an error occurs creating or getting the SupportsTransactions Node.
   */
  Boolean getSupportsTransactions() throws UaException;

  /**
   * Set the local value of the SupportsTransactions Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SupportsTransactions Node.
   * @throws UaException if an error occurs creating or getting the SupportsTransactions Node.
   */
  void setSupportsTransactions(Boolean value) throws UaException;

  /**
   * Read the value of the SupportsTransactions Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readSupportsTransactions() throws UaException;

  /**
   * Write a new value for the SupportsTransactions Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSupportsTransactions(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSupportsTransactions}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readSupportsTransactionsAsync();

  /**
   * An asynchronous implementation of {@link #writeSupportsTransactions}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSupportsTransactionsAsync(Boolean value);

  /**
   * Get the SupportsTransactions {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SupportsTransactions {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSupportsTransactionsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSupportsTransactionsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSupportsTransactionsNodeAsync();

  /**
   * Get the local value of the InApplicationSetup Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the InApplicationSetup Node.
   * @throws UaException if an error occurs creating or getting the InApplicationSetup Node.
   */
  Boolean getInApplicationSetup() throws UaException;

  /**
   * Set the local value of the InApplicationSetup Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the InApplicationSetup Node.
   * @throws UaException if an error occurs creating or getting the InApplicationSetup Node.
   */
  void setInApplicationSetup(Boolean value) throws UaException;

  /**
   * Read the value of the InApplicationSetup Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Boolean} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Boolean readInApplicationSetup() throws UaException;

  /**
   * Write a new value for the InApplicationSetup Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link Boolean} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeInApplicationSetup(Boolean value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readInApplicationSetup}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Boolean> readInApplicationSetupAsync();

  /**
   * An asynchronous implementation of {@link #writeInApplicationSetup}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeInApplicationSetupAsync(Boolean value);

  /**
   * Get the InApplicationSetup {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the InApplicationSetup {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getInApplicationSetupNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getInApplicationSetupNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getInApplicationSetupNodeAsync();

  /**
   * Get the CertificateGroups {@link CertificateGroupFolderType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CertificateGroups {@link CertificateGroupFolderType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  CertificateGroupFolderType getCertificateGroupsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCertificateGroupsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the CertificateGroupFolderType
   *     Node or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends CertificateGroupFolderType> getCertificateGroupsNodeAsync();

  /**
   * Get the TransactionDiagnostics {@link TransactionDiagnosticsType} Node, or {@code null} if it
   * does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TransactionDiagnostics {@link TransactionDiagnosticsType} Node, or {@code null} if
   *     it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  TransactionDiagnosticsType getTransactionDiagnosticsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTransactionDiagnosticsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the TransactionDiagnosticsType
   *     Node or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends TransactionDiagnosticsType> getTransactionDiagnosticsNodeAsync();
}
