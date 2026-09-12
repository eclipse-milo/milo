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
import org.eclipse.milo.opcua.stack.core.types.structured.DeleteReferencesItem;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.23">https://reference.opcfoundation.org/v105/Core/docs/Part5/6.4.23</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AuditDeleteReferencesEventType extends AuditNodeManagementEventType {
  QualifiedProperty<DeleteReferencesItem[]> REFERENCES_TO_DELETE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ReferencesToDelete",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=385"),
          1,
          DeleteReferencesItem[].class);

  /** Gets the existing node's local value. */
  @Nullable DeleteReferencesItem @Nullable [] getReferencesToDelete() throws UaException;

  /** Sets the existing node's local value. */
  void setReferencesToDelete(@Nullable DeleteReferencesItem @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable DeleteReferencesItem @Nullable [] readReferencesToDelete() throws UaException;

  /** Writes the value remotely. */
  void writeReferencesToDelete(@Nullable DeleteReferencesItem @Nullable [] value)
      throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable DeleteReferencesItem @Nullable []>
      readReferencesToDeleteAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeReferencesToDeleteAsync(
      @Nullable DeleteReferencesItem @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getReferencesToDeleteNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getReferencesToDeleteNodeAsync();
}
