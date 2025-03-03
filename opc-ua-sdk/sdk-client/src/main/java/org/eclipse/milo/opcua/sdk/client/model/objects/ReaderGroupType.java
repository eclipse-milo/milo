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
import org.eclipse.milo.opcua.stack.core.UaException;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.9">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.6/#9.1.6.9</a>
 */
public interface ReaderGroupType extends PubSubGroupType {
  /**
   * Get the Diagnostics {@link PubSubDiagnosticsReaderGroupType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Diagnostics {@link PubSubDiagnosticsReaderGroupType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PubSubDiagnosticsReaderGroupType getDiagnosticsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDiagnosticsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the
   *     PubSubDiagnosticsReaderGroupType Node or completes exceptionally if an error occurs
   *     creating or getting the Node.
   */
  CompletableFuture<? extends PubSubDiagnosticsReaderGroupType> getDiagnosticsNodeAsync();

  /**
   * Get the TransportSettings {@link ReaderGroupTransportType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the TransportSettings {@link ReaderGroupTransportType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  ReaderGroupTransportType getTransportSettingsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getTransportSettingsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the ReaderGroupTransportType Node
   *     or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends ReaderGroupTransportType> getTransportSettingsNodeAsync();

  /**
   * Get the MessageSettings {@link ReaderGroupMessageType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the MessageSettings {@link ReaderGroupMessageType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  ReaderGroupMessageType getMessageSettingsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getMessageSettingsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the ReaderGroupMessageType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends ReaderGroupMessageType> getMessageSettingsNodeAsync();
}
