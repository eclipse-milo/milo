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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.11">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.11</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface IOrderedObjectType extends BaseInterfaceType {
  QualifiedProperty<Number> NUMBER_IN_LIST =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NumberInList",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=26"),
          -1,
          Number.class);

  /** Gets the existing node's local value. */
  @Nullable Number getNumberInList() throws UaException;

  /** Sets the existing node's local value. */
  void setNumberInList(@Nullable Number value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Number readNumberInList() throws UaException;

  /** Writes the value remotely. */
  void writeNumberInList(@Nullable Number value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Number> readNumberInListAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeNumberInListAsync(@Nullable Number value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getNumberInListNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getNumberInListNodeAsync();
}
