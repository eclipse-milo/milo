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

import org.eclipse.milo.opcua.sdk.core.nodes.MethodNode;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBinding;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.sdk.server.model.methods.AcknowledgeableConditionTypeAcknowledgeDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AcknowledgeableConditionTypeAcknowledgeHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AcknowledgeableConditionTypeConfirmDetailedHandler;
import org.eclipse.milo.opcua.sdk.server.model.methods.AcknowledgeableConditionTypeConfirmHandler;
import org.eclipse.milo.opcua.sdk.server.model.variables.TwoStateVariableType;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.2">https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.2</a>
 */
public interface AcknowledgeableConditionType extends ConditionType {
  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  TwoStateVariableType getEnabledStateNode();

  /**
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Use the node's raw DataValue to inspect quality and
   * timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   */
  @Nullable LocalizedText getEnabledState();

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  void setEnabledState(@Nullable LocalizedText value);

  /**
   * Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  TwoStateVariableType getAckedStateNode();

  /**
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Use the node's raw DataValue to inspect quality and
   * timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   */
  @Nullable LocalizedText getAckedState();

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  void setAckedState(@Nullable LocalizedText value);

  /**
   * Resolves the optional member by its namespace-qualified path. Returns null only for confirmed
   * absence. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  @Nullable TwoStateVariableType getConfirmedStateNode();

  /**
   * Gets the existing member's local value without checking its quality. A null value is valid; an
   * absent node fails with Bad_NotFound. Use the node's raw DataValue to inspect quality and
   * timestamps.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @return the value, which may be null on a present member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   * @throws ClassCastException if a plain payload cast encounters an incompatible Java
   *     representation
   */
  @Nullable LocalizedText getConfirmedState();

  /**
   * Sets the existing member's local value. A null value is valid. An absent node fails with
   * Bad_NotFound before conversion or mutation. This does not create nodes or silently skip writes.
   *
   * <p>Concrete enum conversions reject unknown numbers with Bad_OutOfRange. Structured decoding
   * and existing rank/type checks retain their failures.
   *
   * @param value the value to store; null is permitted
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  void setConfirmedState(@Nullable LocalizedText value);

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.3
   *
   * <p>Resolves the required member by its namespace-qualified path. A missing member fails with
   * Bad_NotFound. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  MethodNode getAcknowledgeMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.3
   *
   * <p>Binds a synchronous callback for this ObjectId through the supplied registry. The Method
   * must already exist and have compatible effective metadata; binding does not create nodes or
   * rewrite argument properties. Replacing this ObjectId does not replace another owner's
   * registration.
   *
   * <p>The returned token owns only this registration. Closing a stale token cannot remove its
   * replacement. Cleanup is non-draining: an already selected callback may finish. External raw
   * handler replacement is authoritative. An observed displacement prevents further binds through
   * that registry.
   *
   * @return an explicit registration lifetime
   * @throws UaException if the Method is absent, ownership or metadata validation fails, or a
   *     preempting ConditionManager makes binding unsupported
   * @throws UaRuntimeException if strict local lookup fails
   * @throws IllegalStateException if the registry is closed, displaced, or another registry owns
   *     the Method
   */
  MethodBinding bindAcknowledge(
      MethodBindings bindings, AcknowledgeableConditionTypeAcknowledgeHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.3
   *
   * <p>Binds a synchronous callback for this ObjectId through the supplied registry. The Method
   * must already exist and have compatible effective metadata; binding does not create nodes or
   * rewrite argument properties. Replacing this ObjectId does not replace another owner's
   * registration.
   *
   * <p>The returned token owns only this registration. Closing a stale token cannot remove its
   * replacement. Cleanup is non-draining: an already selected callback may finish. External raw
   * handler replacement is authoritative. An observed displacement prevents further binds through
   * that registry.
   *
   * @return an explicit registration lifetime
   * @throws UaException if the Method is absent, ownership or metadata validation fails, or a
   *     preempting ConditionManager makes binding unsupported
   * @throws UaRuntimeException if strict local lookup fails
   * @throws IllegalStateException if the registry is closed, displaced, or another registry owns
   *     the Method
   */
  MethodBinding bindAcknowledgeDetailed(
      MethodBindings bindings, AcknowledgeableConditionTypeAcknowledgeDetailedHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.4
   *
   * <p>Resolves the optional member by its namespace-qualified path. Returns null only for
   * confirmed absence. Resolution does not create a UA node. A reference can change after lookup.
   *
   * @return the existing member, or null for confirmed absence
   * @throws org.eclipse.milo.opcua.stack.core.UaRuntimeException if a required node is absent,
   *     resolution fails, or a checked conversion fails
   */
  @Nullable MethodNode getConfirmMethodNode();

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.4
   *
   * <p>Binds a synchronous callback for this ObjectId through the supplied registry. The Method
   * must already exist and have compatible effective metadata; binding does not create nodes or
   * rewrite argument properties. Replacing this ObjectId does not replace another owner's
   * registration.
   *
   * <p>The returned token owns only this registration. Closing a stale token cannot remove its
   * replacement. Cleanup is non-draining: an already selected callback may finish. External raw
   * handler replacement is authoritative. An observed displacement prevents further binds through
   * that registry.
   *
   * @return an explicit registration lifetime
   * @throws UaException if the Method is absent, ownership or metadata validation fails, or a
   *     preempting ConditionManager makes binding unsupported
   * @throws UaRuntimeException if strict local lookup fails
   * @throws IllegalStateException if the registry is closed, displaced, or another registry owns
   *     the Method
   */
  MethodBinding bindConfirm(
      MethodBindings bindings, AcknowledgeableConditionTypeConfirmHandler handler)
      throws UaException;

  /**
   * https://reference.opcfoundation.org/v105/Core/docs/Part9/5.7.4
   *
   * <p>Binds a synchronous callback for this ObjectId through the supplied registry. The Method
   * must already exist and have compatible effective metadata; binding does not create nodes or
   * rewrite argument properties. Replacing this ObjectId does not replace another owner's
   * registration.
   *
   * <p>The returned token owns only this registration. Closing a stale token cannot remove its
   * replacement. Cleanup is non-draining: an already selected callback may finish. External raw
   * handler replacement is authoritative. An observed displacement prevents further binds through
   * that registry.
   *
   * @return an explicit registration lifetime
   * @throws UaException if the Method is absent, ownership or metadata validation fails, or a
   *     preempting ConditionManager makes binding unsupported
   * @throws UaRuntimeException if strict local lookup fails
   * @throws IllegalStateException if the registry is closed, displaced, or another registry owns
   *     the Method
   */
  MethodBinding bindConfirmDetailed(
      MethodBindings bindings, AcknowledgeableConditionTypeConfirmDetailedHandler handler)
      throws UaException;
}
