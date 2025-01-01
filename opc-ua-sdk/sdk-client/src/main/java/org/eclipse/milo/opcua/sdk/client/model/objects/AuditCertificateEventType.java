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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.12">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.12</a>
 */
public interface AuditCertificateEventType extends AuditSecurityEventType {
  QualifiedProperty<ByteString> CERTIFICATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Certificate",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15"),
          -1,
          ByteString.class);

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
