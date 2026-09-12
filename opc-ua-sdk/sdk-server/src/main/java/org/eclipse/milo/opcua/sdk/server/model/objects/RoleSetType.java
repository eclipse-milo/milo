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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface RoleSetType extends BaseObjectType {
  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.2
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getAddRoleMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.2 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddRole(MethodBindings bindings, AddRoleHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.2 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAddRoleDetailed(MethodBindings bindings, AddRoleDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getRemoveRoleMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveRole(MethodBindings bindings, RemoveRoleHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.3 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveRoleDetailed(MethodBindings bindings, RemoveRoleDetailedHandler handler)
      throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.2 */
  @FunctionalInterface
  interface AddRoleHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable NodeId invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String roleName,
        @Nullable String namespaceUri)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.2 */
  @FunctionalInterface
  interface AddRoleDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable NodeId> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable String roleName,
        @Nullable String namespaceUri)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.3 */
  @FunctionalInterface
  interface RemoveRoleHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId roleNodeId)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part18/4.2.3 */
  @FunctionalInterface
  interface RemoveRoleDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable NodeId roleNodeId)
        throws UaException;
  }
}
