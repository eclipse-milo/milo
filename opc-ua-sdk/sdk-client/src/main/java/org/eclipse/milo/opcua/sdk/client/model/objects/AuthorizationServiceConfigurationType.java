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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/9.7.4">https://reference.opcfoundation.org/v105/Core/docs/Part12/9.7.4</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AuthorizationServiceConfigurationType extends BaseObjectType {
  QualifiedProperty<String> SERVICE_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServiceUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<ByteString> SERVICE_CERTIFICATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServiceCertificate",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=15"),
          -1,
          ByteString.class);

  QualifiedProperty<String> ISSUER_ENDPOINT_URL =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "IssuerEndpointUrl",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /** Gets the existing node's local value. */
  @Nullable String getServiceUri() throws UaException;

  /** Sets the existing node's local value. */
  void setServiceUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readServiceUri() throws UaException;

  /** Writes the value remotely. */
  void writeServiceUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readServiceUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeServiceUriAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getServiceUriNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getServiceUriNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable ByteString getServiceCertificate() throws UaException;

  /** Sets the existing node's local value. */
  void setServiceCertificate(@Nullable ByteString value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ByteString readServiceCertificate() throws UaException;

  /** Writes the value remotely. */
  void writeServiceCertificate(@Nullable ByteString value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ByteString> readServiceCertificateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeServiceCertificateAsync(@Nullable ByteString value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getServiceCertificateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getServiceCertificateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getIssuerEndpointUrl() throws UaException;

  /** Sets the existing node's local value. */
  void setIssuerEndpointUrl(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readIssuerEndpointUrl() throws UaException;

  /** Writes the value remotely. */
  void writeIssuerEndpointUrl(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readIssuerEndpointUrlAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeIssuerEndpointUrlAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getIssuerEndpointUrlNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getIssuerEndpointUrlNodeAsync();
}
