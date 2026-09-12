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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.4">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.4</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AuditSecurityEventType extends AuditEventType {
  QualifiedProperty<StatusCode> STATUS_CODE_ID =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "StatusCodeId",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=19"),
          -1,
          StatusCode.class);

  /** Gets the existing node's local value. */
  @Nullable StatusCode getStatusCodeId() throws UaException;

  /** Sets the existing node's local value. */
  void setStatusCodeId(@Nullable StatusCode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable StatusCode readStatusCodeId() throws UaException;

  /** Writes the value remotely. */
  void writeStatusCodeId(@Nullable StatusCode value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable StatusCode> readStatusCodeIdAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeStatusCodeIdAsync(@Nullable StatusCode value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getStatusCodeIdNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getStatusCodeIdNodeAsync();
}
