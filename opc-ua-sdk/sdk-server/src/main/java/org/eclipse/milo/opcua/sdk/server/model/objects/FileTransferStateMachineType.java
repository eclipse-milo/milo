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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.6">https://reference.opcfoundation.org/v105/Core/docs/Part20/4.4.6</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface FileTransferStateMachineType extends FiniteStateMachineType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  InitialStateType getIdleNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getReadPrepareNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getReadTransferNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getApplyWriteNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getErrorNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getIdleToReadPrepareNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getReadPrepareToReadTransferNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getReadTransferToIdleNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getIdleToApplyWriteNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getApplyWriteToIdleNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getReadPrepareToErrorNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getReadTransferToErrorNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getApplyWriteToErrorNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getErrorToIdleNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/1
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getResetMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/1 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindReset(MethodBindings bindings, ResetHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part20/1 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindResetDetailed(MethodBindings bindings, ResetDetailedHandler handler)
      throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/1 */
  @FunctionalInterface
  interface ResetHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part20/1 */
  @FunctionalInterface
  interface ResetDetailedHandler {
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
