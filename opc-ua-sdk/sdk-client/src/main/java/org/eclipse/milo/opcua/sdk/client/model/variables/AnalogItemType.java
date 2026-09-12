/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.model.variables;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.2/#5.3.2.3">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.2/#5.3.2.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AnalogItemType extends BaseAnalogType {
  QualifiedProperty<Range> EU_RANGE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "EURange",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=884"),
          -1,
          Range.class);

  /** Gets the existing node's local value. */
  @Nullable Range getEuRange() throws UaException;

  /** Sets the existing node's local value. */
  void setEuRange(@Nullable Range value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Range readEuRange() throws UaException;

  /** Writes the value remotely. */
  void writeEuRange(@Nullable Range value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Range> readEuRangeAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeEuRangeAsync(@Nullable Range value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getEuRangeNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getEuRangeNodeAsync();
}
