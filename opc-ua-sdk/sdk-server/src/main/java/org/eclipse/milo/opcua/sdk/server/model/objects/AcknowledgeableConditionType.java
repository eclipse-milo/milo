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
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AcknowledgeableConditionType extends ConditionType {
  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TwoStateVariableType getEnabledStateNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getEnabledState();

  /** Sets the existing node's local value. */
  void setEnabledState(@Nullable LocalizedText value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TwoStateVariableType getAckedStateNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getAckedState();

  /** Sets the existing node's local value. */
  void setAckedState(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable TwoStateVariableType getConfirmedStateNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getConfirmedState();

  /** Sets the existing node's local value. */
  void setConfirmedState(@Nullable LocalizedText value);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.3
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getAcknowledgeMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.3 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAcknowledge(MethodBindings bindings, AcknowledgeHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.3 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindAcknowledgeDetailed(MethodBindings bindings, AcknowledgeDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.4
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getConfirmMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.4 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindConfirm(MethodBindings bindings, ConfirmHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.4 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindConfirmDetailed(MethodBindings bindings, ConfirmDetailedHandler handler)
      throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.3 */
  @FunctionalInterface
  interface AcknowledgeHandler {
    /**
     * @param eventId The identifier for the event to comment.
     * @param comment The comment to add to the condition.
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ByteString eventId,
        @Nullable LocalizedText comment)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.3 */
  @FunctionalInterface
  interface AcknowledgeDetailedHandler {
    /**
     * @param eventId The identifier for the event to comment.
     * @param comment The comment to add to the condition.
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ByteString eventId,
        @Nullable LocalizedText comment)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.4 */
  @FunctionalInterface
  interface ConfirmHandler {
    /**
     * @param eventId The identifier for the event to comment.
     * @param comment The comment to add to the condition.
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ByteString eventId,
        @Nullable LocalizedText comment)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.4 */
  @FunctionalInterface
  interface ConfirmDetailedHandler {
    /**
     * @param eventId The identifier for the event to comment.
     * @param comment The comment to add to the condition.
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable ByteString eventId,
        @Nullable LocalizedText comment)
        throws UaException;
  }
}
