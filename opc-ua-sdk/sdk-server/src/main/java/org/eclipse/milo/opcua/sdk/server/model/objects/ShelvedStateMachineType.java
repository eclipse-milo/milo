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
import org.eclipse.milo.opcua.sdk.core.QualifiedProperty;
import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.1">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.1</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface ShelvedStateMachineType extends FiniteStateMachineType {
  QualifiedProperty<Double> UNSHELVE_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "UnshelveTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  /** Gets the existing node's local value. */
  @Nullable Double getUnshelveTime();

  /** Sets the existing node's local value. */
  void setUnshelveTime(@Nullable Double value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getUnshelveTimeNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getUnshelvedNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getTimedShelvedNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  StateType getOneShotShelvedNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getUnshelvedToTimedShelvedNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getUnshelvedToOneShotShelvedNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getTimedShelvedToUnshelvedNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getTimedShelvedToOneShotShelvedNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getOneShotShelvedToUnshelvedNode();

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  TransitionType getOneShotShelvedToTimedShelvedNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.4
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getTimedShelveMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.4 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindTimedShelve(MethodBindings bindings, TimedShelveHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.4 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindTimedShelveDetailed(MethodBindings bindings, TimedShelveDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getTimedShelve2MethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.5 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindTimedShelve2(MethodBindings bindings, TimedShelve2Handler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.5 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindTimedShelve2Detailed(
      MethodBindings bindings, TimedShelve2DetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.2
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getUnshelveMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.2 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindUnshelve(MethodBindings bindings, UnshelveHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.2 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindUnshelveDetailed(MethodBindings bindings, UnshelveDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.3
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getUnshelve2MethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.3 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindUnshelve2(MethodBindings bindings, Unshelve2Handler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.3 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindUnshelve2Detailed(MethodBindings bindings, Unshelve2DetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.6
   *
   * <p>Returns the required node.
   *
   * @return the required node.
   */
  MethodNode getOneShotShelveMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.6 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindOneShotShelve(MethodBindings bindings, OneShotShelveHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.6 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindOneShotShelveDetailed(
      MethodBindings bindings, OneShotShelveDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.7
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getOneShotShelve2MethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.7 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindOneShotShelve2(MethodBindings bindings, OneShotShelve2Handler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.7 Binds a synchronous
   * callback for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindOneShotShelve2Detailed(
      MethodBindings bindings, OneShotShelve2DetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.4 */
  @FunctionalInterface
  interface TimedShelveHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable Double shelvingTime)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.4 */
  @FunctionalInterface
  interface TimedShelveDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable Double shelvingTime)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.5 */
  @FunctionalInterface
  interface TimedShelve2Handler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable Double shelvingTime,
        @Nullable LocalizedText comment)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.5 */
  @FunctionalInterface
  interface TimedShelve2DetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable Double shelvingTime,
        @Nullable LocalizedText comment)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.2 */
  @FunctionalInterface
  interface UnshelveHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.2 */
  @FunctionalInterface
  interface UnshelveDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.3 */
  @FunctionalInterface
  interface Unshelve2Handler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable LocalizedText comment)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.3 */
  @FunctionalInterface
  interface Unshelve2DetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable LocalizedText comment)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.6 */
  @FunctionalInterface
  interface OneShotShelveHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.6 */
  @FunctionalInterface
  interface OneShotShelveDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.7 */
  @FunctionalInterface
  interface OneShotShelve2Handler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable LocalizedText comment)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.17/#5.8.17.7 */
  @FunctionalInterface
  interface OneShotShelve2DetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable LocalizedText comment)
        throws UaException;
  }
}
