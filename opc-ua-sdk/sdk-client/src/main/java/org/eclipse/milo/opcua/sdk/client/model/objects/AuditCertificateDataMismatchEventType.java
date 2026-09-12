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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.13">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.13</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AuditCertificateDataMismatchEventType extends AuditCertificateEventType {
  QualifiedProperty<String> INVALID_HOSTNAME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InvalidHostname",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> INVALID_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InvalidUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /** Gets the existing node's local value. */
  @Nullable String getInvalidHostname() throws UaException;

  /** Sets the existing node's local value. */
  void setInvalidHostname(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readInvalidHostname() throws UaException;

  /** Writes the value remotely. */
  void writeInvalidHostname(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readInvalidHostnameAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeInvalidHostnameAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getInvalidHostnameNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getInvalidHostnameNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getInvalidUri() throws UaException;

  /** Sets the existing node's local value. */
  void setInvalidUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readInvalidUri() throws UaException;

  /** Writes the value remotely. */
  void writeInvalidUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readInvalidUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeInvalidUriAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getInvalidUriNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getInvalidUriNodeAsync();
}
