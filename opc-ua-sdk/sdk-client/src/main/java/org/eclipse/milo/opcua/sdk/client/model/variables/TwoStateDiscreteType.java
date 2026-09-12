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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.3/#5.3.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.3/#5.3.3.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface TwoStateDiscreteType extends DiscreteItemType {
  QualifiedProperty<LocalizedText> FALSE_STATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "FalseState",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  QualifiedProperty<LocalizedText> TRUE_STATE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "TrueState",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=21"),
          -1,
          LocalizedText.class);

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getFalseState() throws UaException;

  /** Sets the existing node's local value. */
  void setFalseState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readFalseState() throws UaException;

  /** Writes the value remotely. */
  void writeFalseState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readFalseStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeFalseStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getFalseStateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getFalseStateNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getTrueState() throws UaException;

  /** Sets the existing node's local value. */
  void setTrueState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable LocalizedText readTrueState() throws UaException;

  /** Writes the value remotely. */
  void writeTrueState(@Nullable LocalizedText value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable LocalizedText> readTrueStateAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeTrueStateAsync(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getTrueStateNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getTrueStateNodeAsync();
}
