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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.1</a>
 */
public interface PubSubKeyServiceType extends BaseObjectType {
  /**
   * Get the SecurityGroups {@link SecurityGroupFolderType} Node, or {@code null} if it does not
   * exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the SecurityGroups {@link SecurityGroupFolderType} Node, or {@code null} if it does not
   *     exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  SecurityGroupFolderType getSecurityGroupsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getSecurityGroupsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the SecurityGroupFolderType Node
   *     or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends SecurityGroupFolderType> getSecurityGroupsNodeAsync();

  /**
   * Get the KeyPushTargets {@link PubSubKeyPushTargetFolderType} Node, or {@code null} if it does
   * not exist.
   *
   * <p>The Node is created when first accessed and cached for subsequent calls.
   *
   * @return the KeyPushTargets {@link PubSubKeyPushTargetFolderType} Node, or {@code null} if it
   *     does not exist.
   * @throws UaException if an error occurs creating or getting the Node.
   */
  PubSubKeyPushTargetFolderType getKeyPushTargetsNode() throws UaException;

  /**
   * Asynchronous implementation of {@link #getKeyPushTargetsNode()}.
   *
   * @return a CompletableFuture that completes successfully with the PubSubKeyPushTargetFolderType
   *     Node or completes exceptionally if an error occurs creating or getting the Node.
   */
  CompletableFuture<? extends PubSubKeyPushTargetFolderType> getKeyPushTargetsNodeAsync();
}
