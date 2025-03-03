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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.SecurityTokenRequestType;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.6">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.6</a>
 */
public interface AuditOpenSecureChannelEventType extends AuditChannelEventType {
  QualifiedProperty<ByteString> CLIENT_CERTIFICATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ClientCertificate",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15"),
          -1,
          ByteString.class);

  QualifiedProperty<String> CLIENT_CERTIFICATE_THUMBPRINT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ClientCertificateThumbprint",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<SecurityTokenRequestType> REQUEST_TYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "RequestType",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=315"),
          -1,
          SecurityTokenRequestType.class);

  QualifiedProperty<String> SECURITY_POLICY_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityPolicyUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<MessageSecurityMode> SECURITY_MODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecurityMode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=302"),
          -1,
          MessageSecurityMode.class);

  QualifiedProperty<Double> REQUESTED_LIFETIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "RequestedLifetime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<ByteString> CERTIFICATE_ERROR_EVENT_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CertificateErrorEventId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15"),
          -1,
          ByteString.class);

  /**
   * Get the local value of the ClientCertificate Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ClientCertificate Node.
   * @throws UaException if an error occurs creating or getting the ClientCertificate Node.
   */
  ByteString getClientCertificate() throws UaException;

  /**
   * Set the local value of the ClientCertificate Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ClientCertificate Node.
   * @throws UaException if an error occurs creating or getting the ClientCertificate Node.
   */
  void setClientCertificate(ByteString value) throws UaException;

  /**
   * Read the value of the ClientCertificate Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ByteString} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ByteString readClientCertificate() throws UaException;

  /**
   * Write a new value for the ClientCertificate Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link ByteString} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeClientCertificate(ByteString value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readClientCertificate}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ByteString> readClientCertificateAsync();

  /**
   * An asynchronous implementation of {@link #writeClientCertificate}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeClientCertificateAsync(ByteString value);

  /**
   * Get the ClientCertificate {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ClientCertificate {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getClientCertificateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getClientCertificateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getClientCertificateNodeAsync();

  /**
   * Get the local value of the ClientCertificateThumbprint Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ClientCertificateThumbprint Node.
   * @throws UaException if an error occurs creating or getting the ClientCertificateThumbprint
   *     Node.
   */
  String getClientCertificateThumbprint() throws UaException;

  /**
   * Set the local value of the ClientCertificateThumbprint Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ClientCertificateThumbprint Node.
   * @throws UaException if an error occurs creating or getting the ClientCertificateThumbprint
   *     Node.
   */
  void setClientCertificateThumbprint(String value) throws UaException;

  /**
   * Read the value of the ClientCertificateThumbprint Node from the server and update the local
   * value if the operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readClientCertificateThumbprint() throws UaException;

  /**
   * Write a new value for the ClientCertificateThumbprint Node to the server and update the local
   * value if the operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeClientCertificateThumbprint(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readClientCertificateThumbprint}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readClientCertificateThumbprintAsync();

  /**
   * An asynchronous implementation of {@link #writeClientCertificateThumbprint}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeClientCertificateThumbprintAsync(String value);

  /**
   * Get the ClientCertificateThumbprint {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ClientCertificateThumbprint {@link PropertyType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getClientCertificateThumbprintNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getClientCertificateThumbprintNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getClientCertificateThumbprintNodeAsync();

  /**
   * Get the local value of the RequestType Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RequestType Node.
   * @throws UaException if an error occurs creating or getting the RequestType Node.
   */
  SecurityTokenRequestType getRequestType() throws UaException;

  /**
   * Set the local value of the RequestType Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RequestType Node.
   * @throws UaException if an error occurs creating or getting the RequestType Node.
   */
  void setRequestType(SecurityTokenRequestType value) throws UaException;

  /**
   * Read the value of the RequestType Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link SecurityTokenRequestType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  SecurityTokenRequestType readRequestType() throws UaException;

  /**
   * Write a new value for the RequestType Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link SecurityTokenRequestType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRequestType(SecurityTokenRequestType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRequestType}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends SecurityTokenRequestType> readRequestTypeAsync();

  /**
   * An asynchronous implementation of {@link #writeRequestType}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRequestTypeAsync(SecurityTokenRequestType value);

  /**
   * Get the RequestType {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RequestType {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getRequestTypeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRequestTypeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getRequestTypeNodeAsync();

  /**
   * Get the local value of the SecurityPolicyUri Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the SecurityPolicyUri Node.
   * @throws UaException if an error occurs creating or getting the SecurityPolicyUri Node.
   */
  String getSecurityPolicyUri() throws UaException;

  /**
   * Set the local value of the SecurityPolicyUri Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the SecurityPolicyUri Node.
   * @throws UaException if an error occurs creating or getting the SecurityPolicyUri Node.
   */
  void setSecurityPolicyUri(String value) throws UaException;

  /**
   * Read the value of the SecurityPolicyUri Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link String} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  String readSecurityPolicyUri() throws UaException;

  /**
   * Write a new value for the SecurityPolicyUri Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link String} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeSecurityPolicyUri(String value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readSecurityPolicyUri}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends String> readSecurityPolicyUriAsync();

  /**
   * An asynchronous implementation of {@link #writeSecurityPolicyUri}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeSecurityPolicyUriAsync(String value);

  /**
   * Get the SecurityPolicyUri {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SecurityPolicyUri {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getSecurityPolicyUriNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSecurityPolicyUriNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getSecurityPolicyUriNodeAsync();

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
   * Get the local value of the RequestedLifetime Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the RequestedLifetime Node.
   * @throws UaException if an error occurs creating or getting the RequestedLifetime Node.
   */
  Double getRequestedLifetime() throws UaException;

  /**
   * Set the local value of the RequestedLifetime Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the RequestedLifetime Node.
   * @throws UaException if an error occurs creating or getting the RequestedLifetime Node.
   */
  void setRequestedLifetime(Double value) throws UaException;

  /**
   * Read the value of the RequestedLifetime Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readRequestedLifetime() throws UaException;

  /**
   * Write a new value for the RequestedLifetime Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeRequestedLifetime(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readRequestedLifetime}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readRequestedLifetimeAsync();

  /**
   * An asynchronous implementation of {@link #writeRequestedLifetime}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeRequestedLifetimeAsync(Double value);

  /**
   * Get the RequestedLifetime {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RequestedLifetime {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getRequestedLifetimeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRequestedLifetimeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getRequestedLifetimeNodeAsync();

  /**
   * Get the local value of the CertificateErrorEventId Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CertificateErrorEventId Node.
   * @throws UaException if an error occurs creating or getting the CertificateErrorEventId Node.
   */
  ByteString getCertificateErrorEventId() throws UaException;

  /**
   * Set the local value of the CertificateErrorEventId Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CertificateErrorEventId Node.
   * @throws UaException if an error occurs creating or getting the CertificateErrorEventId Node.
   */
  void setCertificateErrorEventId(ByteString value) throws UaException;

  /**
   * Read the value of the CertificateErrorEventId Node from the server and update the local value
   * if the operation succeeds.
   *
   * @return the {@link ByteString} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ByteString readCertificateErrorEventId() throws UaException;

  /**
   * Write a new value for the CertificateErrorEventId Node to the server and update the local value
   * if the operation succeeds.
   *
   * @param value the {@link ByteString} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCertificateErrorEventId(ByteString value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCertificateErrorEventId}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ByteString> readCertificateErrorEventIdAsync();

  /**
   * An asynchronous implementation of {@link #writeCertificateErrorEventId}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCertificateErrorEventIdAsync(ByteString value);

  /**
   * Get the CertificateErrorEventId {@link PropertyType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CertificateErrorEventId {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getCertificateErrorEventIdNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCertificateErrorEventIdNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getCertificateErrorEventIdNodeAsync();
}
