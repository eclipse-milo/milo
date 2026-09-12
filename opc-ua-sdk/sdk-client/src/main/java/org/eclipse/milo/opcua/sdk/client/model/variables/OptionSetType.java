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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/7.17">https://reference.opcfoundation.org/v105/Core/docs/Part5/7.17</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface OptionSetType extends BaseDataVariableType {
  QualifiedProperty<LocalizedText[]> OPTION_SET_VALUES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OptionSetValues",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          1,
          LocalizedText[].class);

  QualifiedProperty<Boolean[]> BIT_MASK =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "BitMask",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          1,
          Boolean[].class);

  /** Gets the existing node's local value. */
  @Nullable LocalizedText @Nullable [] getOptionSetValues() throws UaException;

  /** Sets the existing node's local value. */
  void setOptionSetValues(@Nullable LocalizedText @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText @Nullable [] readOptionSetValues() throws UaException;

  /** Writes the value remotely. */
  void writeOptionSetValues(@Nullable LocalizedText @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText @Nullable []> readOptionSetValuesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeOptionSetValuesAsync(
      @Nullable LocalizedText @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getOptionSetValuesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getOptionSetValuesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Boolean @Nullable [] getBitMask() throws UaException;

  /** Sets the existing node's local value. */
  void setBitMask(@Nullable Boolean @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Boolean @Nullable [] readBitMask() throws UaException;

  /** Writes the value remotely. */
  void writeBitMask(@Nullable Boolean @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Boolean @Nullable []> readBitMaskAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeBitMaskAsync(@Nullable Boolean @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getBitMaskNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getBitMaskNodeAsync();
}
