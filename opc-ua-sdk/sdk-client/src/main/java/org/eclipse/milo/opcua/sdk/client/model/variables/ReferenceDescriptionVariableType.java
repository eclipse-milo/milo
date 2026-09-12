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
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceListEntryDataType;
import org.jspecify.annotations.Nullable;

/**
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ReferenceDescriptionVariableType extends BaseDataVariableType {
  QualifiedProperty<ReferenceListEntryDataType[]> REFERENCE_REFINEMENT =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ReferenceRefinement",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=32660"),
          1,
          ReferenceListEntryDataType[].class);

  /** Gets the existing node's local value. */
  @Nullable ReferenceListEntryDataType @Nullable [] getReferenceRefinement() throws UaException;

  /** Sets the existing node's local value. */
  void setReferenceRefinement(@Nullable ReferenceListEntryDataType @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable ReferenceListEntryDataType @Nullable [] readReferenceRefinement() throws UaException;

  /** Writes the value remotely. */
  void writeReferenceRefinement(@Nullable ReferenceListEntryDataType @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable ReferenceListEntryDataType @Nullable []>
      readReferenceRefinementAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeReferenceRefinementAsync(
      @Nullable ReferenceListEntryDataType @Nullable [] value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getReferenceRefinementNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable PropertyType> getReferenceRefinementNodeAsync();
}
