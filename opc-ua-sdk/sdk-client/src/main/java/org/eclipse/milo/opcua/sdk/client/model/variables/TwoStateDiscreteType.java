/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.3/#5.3.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part8/5.3.3/#5.3.3.2</a>
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

  /**
   * Get the local value of the FalseState Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the FalseState Node.
   * @throws UaException if an error occurs creating or getting the FalseState Node.
   */
  LocalizedText getFalseState() throws UaException;

  /**
   * Set the local value of the FalseState Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the FalseState Node.
   * @throws UaException if an error occurs creating or getting the FalseState Node.
   */
  void setFalseState(LocalizedText value) throws UaException;

  /**
   * Read the value of the FalseState Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readFalseState() throws UaException;

  /**
   * Write a new value for the FalseState Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeFalseState(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readFalseState}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readFalseStateAsync();

  /**
   * An asynchronous implementation of {@link #writeFalseState}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeFalseStateAsync(LocalizedText value);

  /**
   * Get the FalseState {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the FalseState {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getFalseStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getFalseStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getFalseStateNodeAsync();

  /**
   * Get the local value of the TrueState Node.
   *
   * <p>The returned value is the last seen; it is not read live from the server.
   *
   * @return the local value of the TrueState Node.
   * @throws UaException if an error occurs creating or getting the TrueState Node.
   */
  LocalizedText getTrueState() throws UaException;

  /**
   * Set the local value of the TrueState Node.
   *
   * <p>The value is only updated locally; it is not written to the server.
   *
   * @param value the local value to set for the TrueState Node.
   * @throws UaException if an error occurs creating or getting the TrueState Node.
   */
  void setTrueState(LocalizedText value) throws UaException;

  /**
   * Read the value of the TrueState Node from the server and update the local value if the
   * operation succeeds.
   *
   * @return the {@link LocalizedText} value read from the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  LocalizedText readTrueState() throws UaException;

  /**
   * Write a new value for the TrueState Node to the server and update the local value if the
   * operation succeeds.
   *
   * @param value the {@link LocalizedText} value to write to the server.
   * @throws UaException if a service- or operation-level error occurs.
   */
  void writeTrueState(LocalizedText value) throws UaException;

  /**
   * An asynchronous implementation of {@link #readTrueState}.
   *
   * @return a CompletableFuture that completes successfully with the value or completes
   *     exceptionally if an operation- or service-level error occurs.
   */
  CompletableFuture<? extends LocalizedText> readTrueStateAsync();

  /**
   * An asynchronous implementation of {@link #writeTrueState}.
   *
   * @return a CompletableFuture that completes successfully with the operation result or completes
   *     exceptionally if a service-level error occurs.
   */
  CompletableFuture<StatusCode> writeTrueStateAsync(LocalizedText value);

  /**
   * Get the TrueState {@link PropertyType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TrueState {@link PropertyType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PropertyType getTrueStateNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTrueStateNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PropertyType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PropertyType> getTrueStateNodeAsync();
}
