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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.3">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.5.3</a>
 */
public interface LldpInformationType extends BaseObjectType {
  /**
   * Get the RemoteStatistics {@link LldpRemoteStatisticsType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the RemoteStatistics {@link LldpRemoteStatisticsType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  LldpRemoteStatisticsType getRemoteStatisticsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getRemoteStatisticsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the LldpRemoteStatisticsType Node
   *     or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends LldpRemoteStatisticsType> getRemoteStatisticsNodeAsync();

  /**
   * Get the LocalSystemData {@link LldpLocalSystemType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the LocalSystemData {@link LldpLocalSystemType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  LldpLocalSystemType getLocalSystemDataNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getLocalSystemDataNode()}.
   *
   * @return a CompletableFuture that completes successfully with the LldpLocalSystemType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends LldpLocalSystemType> getLocalSystemDataNodeAsync();

  /**
   * Get the Ports {@link FolderType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Ports {@link FolderType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  FolderType getPortsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getPortsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the FolderType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends FolderType> getPortsNodeAsync();
}
