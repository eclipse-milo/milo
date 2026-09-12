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
import org.eclipse.milo.opcua.stack.core.types.enumerated.PerformUpdateType;
import org.eclipse.milo.opcua.stack.core.types.structured.Annotation;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.4">https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.4</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AuditHistoryAnnotationUpdateEventType extends AuditHistoryUpdateEventType {
  QualifiedProperty<PerformUpdateType> PERFORM_INSERT_REPLACE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "PerformInsertReplace",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=11293"),
          -1,
          PerformUpdateType.class);

  QualifiedProperty<Annotation[]> NEW_VALUES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NewValues",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=891"),
          1,
          Annotation[].class);

  QualifiedProperty<Annotation[]> OLD_VALUES =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OldValues",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=891"),
          1,
          Annotation[].class);

  /** Gets the existing node's local value. */
  @Nullable PerformUpdateType getPerformInsertReplace() throws UaException;

  /** Sets the existing node's local value. */
  void setPerformInsertReplace(@Nullable PerformUpdateType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable PerformUpdateType readPerformInsertReplace() throws UaException;

  /** Writes the value remotely. */
  void writePerformInsertReplace(@Nullable PerformUpdateType value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable PerformUpdateType> readPerformInsertReplaceAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writePerformInsertReplaceAsync(@Nullable PerformUpdateType value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getPerformInsertReplaceNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getPerformInsertReplaceNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Annotation @Nullable [] getNewValues() throws UaException;

  /** Sets the existing node's local value. */
  void setNewValues(@Nullable Annotation @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Annotation @Nullable [] readNewValues() throws UaException;

  /** Writes the value remotely. */
  void writeNewValues(@Nullable Annotation @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Annotation @Nullable []> readNewValuesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeNewValuesAsync(@Nullable Annotation @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getNewValuesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getNewValuesNodeAsync();

  /** Gets the existing node's local value. */
  @Nullable Annotation @Nullable [] getOldValues() throws UaException;

  /** Sets the existing node's local value. */
  void setOldValues(@Nullable Annotation @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  @Nullable Annotation @Nullable [] readOldValues() throws UaException;

  /** Writes the value remotely. */
  void writeOldValues(@Nullable Annotation @Nullable [] value) throws UaException;

  /** Reads the value remotely; requires Good status. */
  CompletableFuture<? extends @Nullable Annotation @Nullable []> readOldValuesAsync();

  /** Writes the value remotely. */
  CompletableFuture<StatusCode> writeOldValuesAsync(@Nullable Annotation @Nullable [] value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getOldValuesNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends PropertyType> getOldValuesNodeAsync();
}
