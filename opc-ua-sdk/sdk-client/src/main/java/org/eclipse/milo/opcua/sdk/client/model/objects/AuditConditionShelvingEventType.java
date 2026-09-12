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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.10.8">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.10.8</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AuditConditionShelvingEventType extends AuditConditionEventType {
  QualifiedProperty<Double> SHELVING_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ShelvingTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  /** Gets the existing node's local value. */
  @Nullable Double getShelvingTime() throws UaException;

  /** Sets the existing node's local value. */
  void setShelvingTime(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readShelvingTime() throws UaException;

  /** Writes the value remotely. */
  void writeShelvingTime(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readShelvingTimeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeShelvingTimeAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getShelvingTimeNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getShelvingTimeNodeAsync();
}
