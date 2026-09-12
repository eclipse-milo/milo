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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.36">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.36</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AuditClientEventType extends AuditEventType {
  QualifiedProperty<String> SERVER_URI =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ServerUri",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=23751"),
          -1,
          String.class);

  /** Gets the existing node's local value. */
  @Nullable String getServerUri() throws UaException;

  /** Sets the existing node's local value. */
  void setServerUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readServerUri() throws UaException;

  /** Writes the value remotely. */
  void writeServerUri(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readServerUriAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeServerUriAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getServerUriNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getServerUriNodeAsync();
}
