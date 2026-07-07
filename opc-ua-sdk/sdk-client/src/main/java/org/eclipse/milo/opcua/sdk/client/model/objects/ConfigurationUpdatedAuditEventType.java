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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.8">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.8</a>
 */
public interface ConfigurationUpdatedAuditEventType extends AuditEventType {
  QualifiedProperty<UInteger> OLD_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OldVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998"),
          -1,
          UInteger.class);

  QualifiedProperty<UInteger> NEW_VERSION =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "NewVersion",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=20998"),
          -1,
          UInteger.class);

  /**
   * Get the local value of the OldVersion Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the OldVersion Node.
   * @throws UaException if an error occurs creating or getting the OldVersion Node.
   */
  UInteger getOldVersion() throws UaException;

  /**
   * Set the local value of the OldVersion Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the OldVersion Node.
   * @throws UaException if an error occurs creating or getting the OldVersion Node.
   */
  void setOldVersion(UInteger value) throws UaException;

  /**
   * Read the value of the OldVersion Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readOldVersion() throws UaException;

  /**
   * Write a new value for the OldVersion Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeOldVersion(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readOldVersion}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readOldVersionAsync();

  /**
   * An asynchronous implementation of {@link #writeOldVersion}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeOldVersionAsync(UInteger value);

  /**
   * Get the OldVersion {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the OldVersion {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getOldVersionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getOldVersionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getOldVersionNodeAsync();

  /**
   * Get the local value of the NewVersion Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the NewVersion Node.
   * @throws UaException if an error occurs creating or getting the NewVersion Node.
   */
  UInteger getNewVersion() throws UaException;

  /**
   * Set the local value of the NewVersion Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the NewVersion Node.
   * @throws UaException if an error occurs creating or getting the NewVersion Node.
   */
  void setNewVersion(UInteger value) throws UaException;

  /**
   * Read the value of the NewVersion Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link UInteger} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  UInteger readNewVersion() throws UaException;

  /**
   * Write a new value for the NewVersion Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link UInteger} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeNewVersion(UInteger value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readNewVersion}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends UInteger> readNewVersionAsync();

  /**
   * An asynchronous implementation of {@link #writeNewVersion}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeNewVersionAsync(UInteger value);

  /**
   * Get the NewVersion {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the NewVersion {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getNewVersionNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getNewVersionNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getNewVersionNodeAsync();
}
