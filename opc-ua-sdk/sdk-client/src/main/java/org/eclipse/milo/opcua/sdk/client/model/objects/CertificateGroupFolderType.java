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
import org.eclipse.milo.opcua.stack.core.UaException;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.3">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.3</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface CertificateGroupFolderType extends FolderType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  CertificateGroupType getDefaultApplicationGroupNode() throws UaException;

  /**
   * Returns the required node.
   *
   * @return a future completing with the required node.
   */
  CompletableFuture<? extends CertificateGroupType> getDefaultApplicationGroupNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable CertificateGroupType getDefaultHttpsGroupNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable CertificateGroupType> getDefaultHttpsGroupNodeAsync();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable CertificateGroupType getDefaultUserTokenGroupNode() throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return a future completing with the node, or null if absent.
   */
  CompletableFuture<? extends @Nullable CertificateGroupType> getDefaultUserTokenGroupNodeAsync();
}
