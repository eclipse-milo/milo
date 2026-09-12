/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.model.objects;

import com.digitalpetri.opcua.uanodeset.runtime.methods.MethodHandlerResult;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface PubSubKeyPushTargetFolderType extends FolderType {
  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getAddPushTargetMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddPushTarget(MethodBindings bindings, AddPushTargetHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddPushTargetDetailed(
      MethodBindings bindings, AddPushTargetDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getRemovePushTargetMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemovePushTarget(MethodBindings bindings, RemovePushTargetHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemovePushTargetDetailed(
      MethodBindings bindings, RemovePushTargetDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getAddPushTargetFolderMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddPushTargetFolder(MethodBindings bindings, AddPushTargetFolderHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddPushTargetFolderDetailed(
      MethodBindings bindings, AddPushTargetFolderDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getRemovePushTargetFolderMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemovePushTargetFolder(
      MethodBindings bindings, RemovePushTargetFolderHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemovePushTargetFolderDetailed(
      MethodBindings bindings, RemovePushTargetFolderDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2 */
  @FunctionalInterface
  interface AddPushTargetHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable NodeId invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String applicationUri,
        @Nullable String endpointUrl,
        @Nullable String securityPolicyUri,
        @Nullable UserTokenPolicy userTokenType,
        @Nullable UShort requestedKeyCount,
        @Nullable Double retryInterval)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.2 */
  @FunctionalInterface
  interface AddPushTargetDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable NodeId> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String applicationUri,
        @Nullable String endpointUrl,
        @Nullable String securityPolicyUri,
        @Nullable UserTokenPolicy userTokenType,
        @Nullable UShort requestedKeyCount,
        @Nullable Double retryInterval)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3 */
  @FunctionalInterface
  interface RemovePushTargetHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId pushTargetId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.3 */
  @FunctionalInterface
  interface RemovePushTargetDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId pushTargetId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4 */
  @FunctionalInterface
  interface AddPushTargetFolderHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable NodeId invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String name)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.4 */
  @FunctionalInterface
  interface AddPushTargetFolderDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable NodeId> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String name)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5 */
  @FunctionalInterface
  interface RemovePushTargetFolderHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId pushTargetFolderNodeId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.7.5 */
  @FunctionalInterface
  interface RemovePushTargetFolderDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId pushTargetFolderNodeId)
        throws UaException;
  }
}
