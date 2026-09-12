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
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.30">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.30</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface SystemStatusChangeEventType extends SystemEventType {
  QualifiedProperty<ServerState> SYSTEM_STATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SystemState",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=852"),
          -1,
          ServerState.class);

  /** Gets the existing node's local value. */
  @Nullable ServerState getSystemState() throws UaException;

  /** Sets the existing node's local value. */
  void setSystemState(@Nullable ServerState value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ServerState readSystemState() throws UaException;

  /** Writes the value remotely. */
  void writeSystemState(@Nullable ServerState value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ServerState> readSystemStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeSystemStateAsync(@Nullable ServerState value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSystemStateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getSystemStateNodeAsync();
}
