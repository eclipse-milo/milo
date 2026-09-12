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
import org.eclipse.milo.opcua.sdk.core.model.methods.PubSubKeyServiceTypeGetSecurityKeysOutputs;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface PubSubKeyServiceType extends BaseObjectType {
  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getGetSecurityKeysMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.2 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGetSecurityKeys(MethodBindings bindings, GetSecurityKeysHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.2 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGetSecurityKeysDetailed(
      MethodBindings bindings, GetSecurityKeysDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getGetSecurityGroupMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGetSecurityGroup(MethodBindings bindings, GetSecurityGroupHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGetSecurityGroupDetailed(
      MethodBindings bindings, GetSecurityGroupDetailedHandler handler) throws UaException;

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable SecurityGroupFolderType getSecurityGroupsNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PubSubKeyPushTargetFolderType getKeyPushTargetsNode();

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.2 */
  @FunctionalInterface
  interface GetSecurityKeysHandler {
    /**
     * @return a non-null container holding all output values
     * @throws UaException for an operation failure
     */
    PubSubKeyServiceTypeGetSecurityKeysOutputs invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String securityGroupId,
        @Nullable UInteger startingTokenId,
        @Nullable UInteger requestedKeyCount)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.2 */
  @FunctionalInterface
  interface GetSecurityKeysDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<PubSubKeyServiceTypeGetSecurityKeysOutputs> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String securityGroupId,
        @Nullable UInteger startingTokenId,
        @Nullable UInteger requestedKeyCount)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.3 */
  @FunctionalInterface
  interface GetSecurityGroupHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable NodeId invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String securityGroupId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/8.3.3 */
  @FunctionalInterface
  interface GetSecurityGroupDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable NodeId> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String securityGroupId)
        throws UaException;
  }
}
