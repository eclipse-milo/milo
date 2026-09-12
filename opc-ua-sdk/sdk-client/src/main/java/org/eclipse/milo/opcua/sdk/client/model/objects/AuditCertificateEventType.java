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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.12">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.12</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AuditCertificateEventType extends AuditSecurityEventType {
  QualifiedProperty<ByteString> CERTIFICATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Certificate",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15"),
          -1,
          ByteString.class);

  /** Gets the existing node's local value. */
  @Nullable ByteString getCertificate() throws UaException;

  /** Sets the existing node's local value. */
  void setCertificate(@Nullable ByteString value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ByteString readCertificate() throws UaException;

  /** Writes the value remotely. */
  void writeCertificate(@Nullable ByteString value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ByteString> readCertificateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeCertificateAsync(@Nullable ByteString value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getCertificateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getCertificateNodeAsync();
}
