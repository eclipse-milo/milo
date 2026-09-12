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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.2/#9.3.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.3.2/#9.3.2.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface BrokerConnectionTransportType extends ConnectionTransportType {
  QualifiedProperty<String> RESOURCE_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ResourceUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<String> AUTHENTICATION_PROFILE_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AuthenticationProfileUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /** Gets the existing node's local value. */
  @Nullable String getResourceUri() throws UaException;

  /** Sets the existing node's local value. */
  void setResourceUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readResourceUri() throws UaException;

  /** Writes the value remotely. */
  void writeResourceUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readResourceUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeResourceUriAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getResourceUriNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getResourceUriNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable String getAuthenticationProfileUri() throws UaException;

  /** Sets the existing node's local value. */
  void setAuthenticationProfileUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readAuthenticationProfileUri() throws UaException;

  /** Writes the value remotely. */
  void writeAuthenticationProfileUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readAuthenticationProfileUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAuthenticationProfileUriAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getAuthenticationProfileUriNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getAuthenticationProfileUriNodeAsync();
}
