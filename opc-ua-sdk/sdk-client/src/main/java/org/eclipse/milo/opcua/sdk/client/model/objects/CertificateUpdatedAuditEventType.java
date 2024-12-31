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
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.14">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.14</a>
 */
public interface CertificateUpdatedAuditEventType extends AuditEventType {
  QualifiedProperty<NodeId> CERTIFICATE_GROUP =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CertificateGroup",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<NodeId> CERTIFICATE_TYPE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "CertificateType",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  /**
   * Get the local value of the CertificateGroup Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the CertificateGroup Node.
   * @throws UaException if an error occurs creating or getting the CertificateGroup Node.
   */
  NodeId getCertificateGroup() throws UaException;

  /**
   * Set the local value of the CertificateGroup Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the CertificateGroup Node.
   * @throws UaException if an error occurs creating or getting the CertificateGroup Node.
   */
  void setCertificateGroup(NodeId value) throws UaException;

  /**
   * Read the value of the CertificateGroup Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link NodeId} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  NodeId readCertificateGroup() throws UaException;

  /**
   * Write a new value for the CertificateGroup Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link NodeId} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeCertificateGroup(NodeId value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readCertificateGroup}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends NodeId> readCertificateGroupAsync();

  /**
   * An asynchronous implementation of {@link #writeCertificateGroup}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeCertificateGroupAsync(NodeId value);

  /**
   * Get the CertificateGroup {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the CertificateGroup {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getCertificateGroupNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getCertificateGroupNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getCertificateGroupNodeAsync();

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
}
