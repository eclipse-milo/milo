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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface DataItemType extends BaseDataVariableType {
  QualifiedProperty<String> DEFINITION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "Definition",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12"),
          -1,
          String.class);

  QualifiedProperty<Double> VALUE_PRECISION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ValuePrecision",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11"),
          -1,
          Double.class);

  /** Gets the existing node's local value. */
  @Nullable String getDefinition() throws UaException;

  /** Sets the existing node's local value. */
  void setDefinition(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable String readDefinition() throws UaException;

  /** Writes the value remotely. */
  void writeDefinition(@Nullable String value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable String> readDefinitionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeDefinitionAsync(@Nullable String value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getDefinitionNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getDefinitionNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Double getValuePrecision() throws UaException;

  /** Sets the existing node's local value. */
  void setValuePrecision(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Double readValuePrecision() throws UaException;

  /** Writes the value remotely. */
  void writeValuePrecision(@Nullable Double value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Double> readValuePrecisionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeValuePrecisionAsync(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getValuePrecisionNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getValuePrecisionNodeAsync();
}
