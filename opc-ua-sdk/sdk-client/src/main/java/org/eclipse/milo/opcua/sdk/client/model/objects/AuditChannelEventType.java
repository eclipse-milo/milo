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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.5">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.5</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AuditChannelEventType extends AuditSecurityEventType {
  QualifiedProperty<String> SECURE_CHANNEL_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SecureChannelId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /** Gets the existing node's local value. */
  @Nullable String getSecureChannelId() throws UaException;

  /** Sets the existing node's local value. */
  void setSecureChannelId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readSecureChannelId() throws UaException;

  /** Writes the value remotely. */
  void writeSecureChannelId(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readSecureChannelIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSecureChannelIdAsync(@Nullable String value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSecureChannelIdNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSecureChannelIdNodeAsync();
}
