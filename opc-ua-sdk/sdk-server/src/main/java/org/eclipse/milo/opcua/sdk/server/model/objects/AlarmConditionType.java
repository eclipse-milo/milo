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
import org.eclipse.milo.opcua.sdk.server.model.variables.AudioVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableType;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyType;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.2</a>
 * @see com.digitalpetri.opcua.uanodeset.runtime.members
 */
public interface AlarmConditionType extends AcknowledgeableConditionType {
  QualifiedProperty<NodeId> INPUT_NODE =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "InputNode",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=17"),
          -1,
          NodeId.class);

  QualifiedProperty<Boolean> SUPPRESSED_OR_SHELVED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "SuppressedOrShelved",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Double> MAX_TIME_SHELVED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "MaxTimeShelved",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Boolean> AUDIBLE_ENABLED =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "AudibleEnabled",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=1"),
          -1,
          Boolean.class);

  QualifiedProperty<Double> ON_DELAY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OnDelay",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double> OFF_DELAY =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "OffDelay",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  QualifiedProperty<Double> RE_ALARM_TIME =
      new QualifiedProperty<>(
          "http://opcfoundation.org/UA/",
          "ReAlarmTime",
          ExpandedNodeId.parse("nsu=http://opcfoundation.org/UA/;i=290"),
          -1,
          Double.class);

  /** Gets the existing node's local value. */
  @Nullable NodeId getInputNode();

  /** Sets the existing node's local value. */
  void setInputNode(@Nullable NodeId value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getInputNodeNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getSuppressedOrShelved();

  /** Sets the existing node's local value. */
  void setSuppressedOrShelved(@Nullable Boolean value);

  /**
   * Returns the required node.
   *
   * @return the required node.
   */
  PropertyType getSuppressedOrShelvedNode();

  /** Gets the existing node's local value. */
  @Nullable Double getMaxTimeShelved();

  /** Sets the existing node's local value. */
  void setMaxTimeShelved(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getMaxTimeShelvedNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getAudibleEnabled();

  /** Sets the existing node's local value. */
  void setAudibleEnabled(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getAudibleEnabledNode();

  /** Gets the existing node's local value. */
  @Nullable Double getOnDelay();

  /** Sets the existing node's local value. */
  void setOnDelay(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getOnDelayNode();

  /** Gets the existing node's local value. */
  @Nullable Double getOffDelay();

  /** Sets the existing node's local value. */
  void setOffDelay(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getOffDelayNode();

  /** Gets the existing node's local value. */
  @Nullable Double getReAlarmTime();

  /** Sets the existing node's local value. */
  void setReAlarmTime(@Nullable Double value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable PropertyType getReAlarmTimeNode();

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
  TwoStateVariableType getActiveStateNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getActiveState();

  /** Sets the existing node's local value. */
  void setActiveState(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable TwoStateVariableType getSuppressedStateNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getSuppressedState();

  /** Sets the existing node's local value. */
  void setSuppressedState(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable TwoStateVariableType getOutOfServiceStateNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getOutOfServiceState();

  /** Sets the existing node's local value. */
  void setOutOfServiceState(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable ShelvedStateMachineType getShelvingStateNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable AudioVariableType getAudibleSoundNode();

  /** Gets the existing node's local value. */
  @Nullable ByteString getAudibleSound();

  /** Sets the existing node's local value. */
  void setAudibleSound(@Nullable ByteString value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable TwoStateVariableType getSilenceStateNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getSilenceState();

  /** Sets the existing node's local value. */
  void setSilenceState(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getFirstInGroupFlagNode();

  /** Gets the existing node's local value. */
  @Nullable Boolean getFirstInGroupFlag();

  /** Sets the existing node's local value. */
  void setFirstInGroupFlag(@Nullable Boolean value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable AlarmGroupType getFirstInGroupNode();

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable TwoStateVariableType getLatchedStateNode();

  /** Gets the existing node's local value. */
  @Nullable LocalizedText getLatchedState();

  /** Sets the existing node's local value. */
  void setLatchedState(@Nullable LocalizedText value);

  /**
   * Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable BaseDataVariableType getReAlarmRepeatCountNode();

  /** Gets the existing node's local value. */
  @Nullable Short getReAlarmRepeatCount();

  /** Sets the existing node's local value. */
  void setReAlarmRepeatCount(@Nullable Short value);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.7
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getSilenceMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.7 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindSilence(MethodBindings bindings, SilenceHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.7 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindSilenceDetailed(MethodBindings bindings, SilenceDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.8
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getSuppressMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.8 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindSuppress(MethodBindings bindings, SuppressHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.8 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindSuppressDetailed(MethodBindings bindings, SuppressDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.9
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getSuppress2MethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.9 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindSuppress2(MethodBindings bindings, Suppress2Handler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.9 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindSuppress2Detailed(MethodBindings bindings, Suppress2DetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.10
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getUnsuppressMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.10 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindUnsuppress(MethodBindings bindings, UnsuppressHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.10 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindUnsuppressDetailed(MethodBindings bindings, UnsuppressDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.11
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getUnsuppress2MethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.11 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindUnsuppress2(MethodBindings bindings, Unsuppress2Handler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.11 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindUnsuppress2Detailed(MethodBindings bindings, Unsuppress2DetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.12
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getRemoveFromServiceMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.12 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveFromService(MethodBindings bindings, RemoveFromServiceHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.12 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveFromServiceDetailed(
      MethodBindings bindings, RemoveFromServiceDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.13
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getRemoveFromService2MethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.13 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveFromService2(MethodBindings bindings, RemoveFromService2Handler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.13 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindRemoveFromService2Detailed(
      MethodBindings bindings, RemoveFromService2DetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.14
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getPlaceInServiceMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.14 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindPlaceInService(MethodBindings bindings, PlaceInServiceHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.14 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindPlaceInServiceDetailed(
      MethodBindings bindings, PlaceInServiceDetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.15
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getPlaceInService2MethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.15 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindPlaceInService2(MethodBindings bindings, PlaceInService2Handler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.15 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindPlaceInService2Detailed(
      MethodBindings bindings, PlaceInService2DetailedHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.5
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getResetMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.5 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindReset(MethodBindings bindings, ResetHandler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.5 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindResetDetailed(MethodBindings bindings, ResetDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.6
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getReset2MethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.6 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindReset2(MethodBindings bindings, Reset2Handler handler) throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.6 Binds a synchronous callback for
   * this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindReset2Detailed(MethodBindings bindings, Reset2DetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.16
   *
   * <p>Returns the node, or null if absent.
   *
   * @return the node, or null if absent.
   */
  @Nullable MethodNode getGetGroupMembershipsMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.16 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGetGroupMemberships(MethodBindings bindings, GetGroupMembershipsHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.16 Binds a synchronous callback
   * for this ObjectId. Close the returned token to unbind.
   *
   * @see MethodBindings
   */
  MethodBinding bindGetGroupMembershipsDetailed(
      MethodBindings bindings, GetGroupMembershipsDetailedHandler handler) throws UaException;

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.7 */
  @FunctionalInterface
  interface SilenceHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.7 */
  @FunctionalInterface
  interface SilenceDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.8 */
  @FunctionalInterface
  interface SuppressHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.8 */
  @FunctionalInterface
  interface SuppressDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.9 */
  @FunctionalInterface
  interface Suppress2Handler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable LocalizedText comment)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.9 */
  @FunctionalInterface
  interface Suppress2DetailedHandler {
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

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.10 */
  @FunctionalInterface
  interface UnsuppressHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.10 */
  @FunctionalInterface
  interface UnsuppressDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.11 */
  @FunctionalInterface
  interface Unsuppress2Handler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable LocalizedText comment)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.11 */
  @FunctionalInterface
  interface Unsuppress2DetailedHandler {
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

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.12 */
  @FunctionalInterface
  interface RemoveFromServiceHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.12 */
  @FunctionalInterface
  interface RemoveFromServiceDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.13 */
  @FunctionalInterface
  interface RemoveFromService2Handler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable LocalizedText comment)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.13 */
  @FunctionalInterface
  interface RemoveFromService2DetailedHandler {
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

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.14 */
  @FunctionalInterface
  interface PlaceInServiceHandler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.14 */
  @FunctionalInterface
  interface PlaceInServiceDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable Void> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.15 */
  @FunctionalInterface
  interface PlaceInService2Handler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable LocalizedText comment)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.15 */
  @FunctionalInterface
  interface PlaceInService2DetailedHandler {
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

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.5 */
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

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.5 */
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

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.6 */
  @FunctionalInterface
  interface Reset2Handler {
    /**
     * @throws UaException for an operation failure
     */
    void invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context,
        @Nullable LocalizedText comment)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.6 */
  @FunctionalInterface
  interface Reset2DetailedHandler {
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

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.16 */
  @FunctionalInterface
  interface GetGroupMembershipsHandler {
    /**
     * @return the output value, including null
     * @throws UaException for an operation failure
     */
    @Nullable NodeId @Nullable [] invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }

  /** https://reference.opcfoundation.org/v105/Core/docs/Part9/5.8.16 */
  @FunctionalInterface
  interface GetGroupMembershipsDetailedHandler {
    /**
     * @return a non-null complete operation outcome
     * @throws UaException for an operation failure
     */
    MethodHandlerResult<@Nullable NodeId @Nullable []> invoke(
        org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext
            context)
        throws UaException;
  }
}
