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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.24/#5.8.24.7">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.24/#5.8.24.7</a>
 */
public interface CertificateExpirationAlarmType extends SystemOffNormalAlarmType {
  QualifiedProperty<DateTime> EXPIRATION_DATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ExpirationDate",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=13"),
          -1,
          DateTime.class);

  QualifiedProperty<Double> EXPIRATION_LIMIT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ExpirationLimit",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<NodeId> CERTIFICATE_TYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CertificateType",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<ByteString> CERTIFICATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Certificate",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15"),
          -1,
          ByteString.class);

  /**
   * Get the local value of the ExpirationDate Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ExpirationDate Node.
   * @throws UaException if an error occurs creating or getting the ExpirationDate Node.
   */
  DateTime getExpirationDate() throws UaException;

  /**
   * Set the local value of the ExpirationDate Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ExpirationDate Node.
   * @throws UaException if an error occurs creating or getting the ExpirationDate Node.
   */
  void setExpirationDate(DateTime value) throws UaException;

  /**
   * Read the value of the ExpirationDate Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link DateTime} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  DateTime readExpirationDate() throws UaException;

  /**
   * Write a new value for the ExpirationDate Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link DateTime} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeExpirationDate(DateTime value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readExpirationDate}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends DateTime> readExpirationDateAsync();

  /**
   * An asynchronous implementation of {@link #writeExpirationDate}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeExpirationDateAsync(DateTime value);

  /**
   * Get the ExpirationDate {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ExpirationDate {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getExpirationDateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getExpirationDateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getExpirationDateNodeAsync();

  /**
   * Get the local value of the ExpirationLimit Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the ExpirationLimit Node.
   * @throws UaException if an error occurs creating or getting the ExpirationLimit Node.
   */
  Double getExpirationLimit() throws UaException;

  /**
   * Set the local value of the ExpirationLimit Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the ExpirationLimit Node.
   * @throws UaException if an error occurs creating or getting the ExpirationLimit Node.
   */
  void setExpirationLimit(Double value) throws UaException;

  /**
   * Read the value of the ExpirationLimit Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Double} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Double readExpirationLimit() throws UaException;

  /**
   * Write a new value for the ExpirationLimit Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Double} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeExpirationLimit(Double value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readExpirationLimit}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Double> readExpirationLimitAsync();

  /**
   * An asynchronous implementation of {@link #writeExpirationLimit}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeExpirationLimitAsync(Double value);

  /**
   * Get the ExpirationLimit {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the ExpirationLimit {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getExpirationLimitNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getExpirationLimitNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getExpirationLimitNodeAsync();

  /**
   * Get the local value of the CertificateType Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CertificateType Node.
   * @throws UaException if an error occurs creating or getting the CertificateType Node.
   */
  NodeId getCertificateType() throws UaException;

  /**
   * Set the local value of the CertificateType Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CertificateType Node.
   * @throws UaException if an error occurs creating or getting the CertificateType Node.
   */
  void setCertificateType(NodeId value) throws UaException;

  /**
   * Read the value of the CertificateType Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NodeId} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId readCertificateType() throws UaException;

  /**
   * Write a new value for the CertificateType Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link NodeId} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCertificateType(NodeId value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCertificateType}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId> readCertificateTypeAsync();

  /**
   * An asynchronous implementation of {@link #writeCertificateType}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCertificateTypeAsync(NodeId value);

  /**
   * Get the CertificateType {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CertificateType {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getCertificateTypeNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCertificateTypeNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getCertificateTypeNodeAsync();

  /**
   * Get the local value of the Certificate Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the Certificate Node.
   * @throws UaException if an error occurs creating or getting the Certificate Node.
   */
  ByteString getCertificate() throws UaException;

  /**
   * Set the local value of the Certificate Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the Certificate Node.
   * @throws UaException if an error occurs creating or getting the Certificate Node.
   */
  void setCertificate(ByteString value) throws UaException;

  /**
   * Read the value of the Certificate Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link ByteString} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  ByteString readCertificate() throws UaException;

  /**
   * Write a new value for the Certificate Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link ByteString} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCertificate(ByteString value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCertificate}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends ByteString> readCertificateAsync();

  /**
   * An asynchronous implementation of {@link #writeCertificate}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCertificateAsync(ByteString value);

  /**
   * Get the Certificate {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Certificate {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getCertificateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCertificateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getCertificateNodeAsync();
}
