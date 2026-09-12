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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.10">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.10</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface OrderedListType extends BaseObjectType {
  QualifiedProperty<String> NODE_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NodeVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  /** Gets the existing node's local value. */
  @Nullable String getNodeVersionProperty() throws UaException;

  /** Sets the existing node's local value. */
  void setNodeVersionProperty(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readNodeVersionProperty() throws UaException;

  /** Writes the value remotely. */
  void writeNodeVersionProperty(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readNodeVersionPropertyAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeNodeVersionPropertyAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getNodeVersionNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getNodeVersionNodeAsync();
}
