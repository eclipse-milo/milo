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
import org.eclipse.milo.opcua.stack.core.types.structured.AxisInformation;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.4/#5.3.4.6">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.4/#5.3.4.6</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface NDimensionArrayItemType extends ArrayItemType {
  QualifiedProperty<AxisInformation[]> AXIS_DEFINITION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AxisDefinition",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=12079"),
          1,
          AxisInformation[].class);

  /** Gets the existing node's local value. */
  @Nullable AxisInformation @Nullable [] getAxisDefinition() throws UaException;

  /** Sets the existing node's local value. */
  void setAxisDefinition(@Nullable AxisInformation @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable AxisInformation @Nullable [] readAxisDefinition() throws UaException;

  /** Writes the value remotely. */
  void writeAxisDefinition(@Nullable AxisInformation @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable AxisInformation @Nullable []> readAxisDefinitionAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeAxisDefinitionAsync(
      @Nullable AxisInformation @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getAxisDefinitionNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getAxisDefinitionNodeAsync();
}
