/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.4">https://reference.opcfoundation.org/v105/Core/docs/Part11/5.8.4</a>
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

  /**
   * Get the local value of the PerformInsertReplace Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the PerformInsertReplace Node.
   * @throws UaException if an error occurs creating or getting the PerformInsertReplace Node.
   */
  PerformUpdateType getPerformInsertReplace() throws UaException;

  /**
   * Set the local value of the PerformInsertReplace Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the PerformInsertReplace Node.
   * @throws UaException if an error occurs creating or getting the PerformInsertReplace Node.
   */
  void setPerformInsertReplace(PerformUpdateType value) throws UaException;

  /**
   * Read the value of the PerformInsertReplace Node from the server and update the local value if
   * the operation succeeds.
   *
   * @return the {@link PerformUpdateType} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  PerformUpdateType readPerformInsertReplace() throws UaException;

  /**
   * Write a new value for the PerformInsertReplace Node to the server and update the local value if
   * the operation succeeds.
   *
   * @param value the {@link PerformUpdateType} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writePerformInsertReplace(PerformUpdateType value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readPerformInsertReplace}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends PerformUpdateType> readPerformInsertReplaceAsync();

  /**
   * An asynchronous implementation of {@link #writePerformInsertReplace}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writePerformInsertReplaceAsync(PerformUpdateType value);

  /**
   * Get the PerformInsertReplace {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the PerformInsertReplace {@link PropertyType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getPerformInsertReplaceNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPerformInsertReplaceNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getPerformInsertReplaceNodeAsync();

  /**
   * Get the local value of the NewValues Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the NewValues Node.
   * @throws UaException if an error occurs creating or getting the NewValues Node.
   */
  Annotation[] getNewValues() throws UaException;

  /**
   * Set the local value of the NewValues Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the NewValues Node.
   * @throws UaException if an error occurs creating or getting the NewValues Node.
   */
  void setNewValues(Annotation[] value) throws UaException;

  /**
   * Read the value of the NewValues Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Annotation[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Annotation[] readNewValues() throws UaException;

  /**
   * Write a new value for the NewValues Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Annotation[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeNewValues(Annotation[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readNewValues}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Annotation[]> readNewValuesAsync();

  /**
   * An asynchronous implementation of {@link #writeNewValues}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeNewValuesAsync(Annotation[] value);

  /**
   * Get the NewValues {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the NewValues {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getNewValuesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getNewValuesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getNewValuesNodeAsync();

  /**
   * Get the local value of the OldValues Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the OldValues Node.
   * @throws UaException if an error occurs creating or getting the OldValues Node.
   */
  Annotation[] getOldValues() throws UaException;

  /**
   * Set the local value of the OldValues Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the OldValues Node.
   * @throws UaException if an error occurs creating or getting the OldValues Node.
   */
  void setOldValues(Annotation[] value) throws UaException;

  /**
   * Read the value of the OldValues Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link Annotation[]} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  Annotation[] readOldValues() throws UaException;

  /**
   * Write a new value for the OldValues Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link Annotation[]} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeOldValues(Annotation[] value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readOldValues}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends Annotation[]> readOldValuesAsync();

  /**
   * An asynchronous implementation of {@link #writeOldValues}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeOldValuesAsync(Annotation[] value);

  /**
   * Get the OldValues {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the OldValues {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getOldValuesNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getOldValuesNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getOldValuesNodeAsync();
}
