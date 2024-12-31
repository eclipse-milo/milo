/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.3">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.3/#7.8.3.3</a>
 */
public interface CertificateGroupFolderType extends FolderType {
  /**
   * Get the DefaultApplicationGroup {@link CertificateGroupType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DefaultApplicationGroup {@link CertificateGroupType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  CertificateGroupType getDefaultApplicationGroupNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDefaultApplicationGroupNode()}.
   *
   * @return a CompletableFuture that completes successfully with the CertificateGroupType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends CertificateGroupType> getDefaultApplicationGroupNodeAsync();

  /**
   * Get the DefaultHttpsGroup {@link CertificateGroupType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DefaultHttpsGroup {@link CertificateGroupType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  CertificateGroupType getDefaultHttpsGroupNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDefaultHttpsGroupNode()}.
   *
   * @return a CompletableFuture that completes successfully with the CertificateGroupType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends CertificateGroupType> getDefaultHttpsGroupNodeAsync();

  /**
   * Get the DefaultUserTokenGroup {@link CertificateGroupType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the DefaultUserTokenGroup {@link CertificateGroupType} Node, or {@code null} if it does
   *     not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  CertificateGroupType getDefaultUserTokenGroupNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getDefaultUserTokenGroupNode()}.
   *
   * @return a CompletableFuture that completes successfully with the CertificateGroupType Node or
   *     completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends CertificateGroupType> getDefaultUserTokenGroupNodeAsync();
}
