/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.gds.model.objects;

import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.sdk.client.model.objects.FolderType;
import org.eclipse.milo.opcua.stack.core.UaException;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/GDS/docs/6.5.3">https://reference.opcfoundation.org/GDS/docs/6.5.3</a>
 */
public interface DirectoryType extends FolderType {
  /**
   * Get the Applications {@link FolderType} Node, or {@code null} if it does not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the Applications {@link FolderType} Node, or {@code null} if it does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  FolderType getApplicationsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getApplicationsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the FolderType Node or completes
   *     exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends FolderType> getApplicationsNodeAsync();
}
