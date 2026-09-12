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
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface PubSubStatusType extends BaseObjectType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  BaseDataVariableType getStateNode();

  /** Gets the existing node's local value. */
  @Nullable PubSubState getState();

  /** Sets the existing node's local value. */
  void setState(@Nullable PubSubState value);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.2
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getEnableMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.2 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindEnable(MethodBindings bindings, EnableHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.2 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindEnableDetailed(MethodBindings bindings, EnableDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getDisableMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.3 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindDisable(MethodBindings bindings, DisableHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.3 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindDisableDetailed(MethodBindings bindings, DisableDetailedHandler handler)
      throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.2 */
  @FunctionalInterface
  interface EnableHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.2 */
  @FunctionalInterface
  interface EnableDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.3 */
  @FunctionalInterface
  interface DisableHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.10/#9.1.10.3 */
  @FunctionalInterface
  interface DisableDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }
}
