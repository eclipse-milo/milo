/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.conditions;

import org.eclipse.milo.opcua.sdk.server.methods.AbstractMethodInvocationHandler.InvocationContext;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.jspecify.annotations.Nullable;

/**
 * Application hook consulted before the SDK applies the default semantics of a Condition method.
 *
 * <p>Each hook is consulted after the SDK's spec-mandated validation (EventId resolution, state
 * checks, disabled gating) and before the default state transition:
 *
 * <ul>
 *   <li>return {@link Outcome#PROCEED} to let the SDK apply its default semantics.
 *   <li>return {@link Outcome#HANDLED} to suppress the default semantics; the call completes
 *       successfully with Condition state untouched by the SDK.
 *   <li>throw {@link UaException} to reject the call with a specific status code, e.g. {@code
 *       Bad_UserAccessDenied} for application-level authorization; Condition state is untouched.
 * </ul>
 *
 * <p>An unexpected {@link RuntimeException} thrown by a hook is mapped to {@code
 * Bad_InternalError}; the default semantics are not applied and Condition state is unchanged.
 *
 * <p>Hooks run while holding the Condition's lock: they must not block indefinitely and must not
 * interact with other Conditions, or deadlock can result.
 *
 * <p>All hooks are default methods so second-wave methods can extend this interface without
 * breaking implementors. The shelving hooks are consulted by the shelving runtime when a Condition
 * is built with shelving support.
 */
public interface ConditionMethodInterceptor {

  /** The result of an interceptor hook. */
  enum Outcome {
    /** Apply the SDK's default method semantics. */
    PROCEED,

    /** The application handled the call; suppress the SDK's default semantics. */
    HANDLED
  }

  /**
   * Called before the default Enable semantics are applied.
   *
   * @param context the {@link InvocationContext} of the method call.
   * @return the {@link Outcome} of the hook.
   * @throws UaException to reject the call with a specific status code.
   */
  default Outcome onEnable(InvocationContext context) throws UaException {
    return Outcome.PROCEED;
  }

  /**
   * Called before the default Disable semantics are applied.
   *
   * @param context the {@link InvocationContext} of the method call.
   * @return the {@link Outcome} of the hook.
   * @throws UaException to reject the call with a specific status code.
   */
  default Outcome onDisable(InvocationContext context) throws UaException {
    return Outcome.PROCEED;
  }

  /**
   * Called before the default AddComment semantics are applied.
   *
   * @param context the {@link InvocationContext} of the method call.
   * @param branch the {@link ConditionBranch} resolved from the EventId argument.
   * @param comment the non-NULL comment supplied by the client; AddComment rejects a NULL
   *     LocalizedText before consulting this hook (see Part 9 §5.5.6).
   * @return the {@link Outcome} of the hook.
   * @throws UaException to reject the call with a specific status code.
   */
  default Outcome onAddComment(
      InvocationContext context, ConditionBranch branch, @Nullable LocalizedText comment)
      throws UaException {
    return Outcome.PROCEED;
  }

  /**
   * Called before the default Acknowledge semantics are applied.
   *
   * @param context the {@link InvocationContext} of the method call.
   * @param branch the {@link ConditionBranch} resolved from the EventId argument.
   * @param comment the comment supplied by the client, possibly NULL (see Part 9 §5.5.6).
   * @return the {@link Outcome} of the hook.
   * @throws UaException to reject the call with a specific status code.
   */
  default Outcome onAcknowledge(
      InvocationContext context, ConditionBranch branch, @Nullable LocalizedText comment)
      throws UaException {
    return Outcome.PROCEED;
  }

  /**
   * Called before the default Confirm semantics are applied.
   *
   * @param context the {@link InvocationContext} of the method call.
   * @param branch the {@link ConditionBranch} resolved from the EventId argument.
   * @param comment the comment supplied by the client, possibly NULL (see Part 9 §5.5.6).
   * @return the {@link Outcome} of the hook.
   * @throws UaException to reject the call with a specific status code.
   */
  default Outcome onConfirm(
      InvocationContext context, ConditionBranch branch, @Nullable LocalizedText comment)
      throws UaException {
    return Outcome.PROCEED;
  }

  /**
   * Called before the default TimedShelve semantics are applied.
   *
   * @param context the {@link InvocationContext} of the method call.
   * @param shelvingTimeMillis the requested shelving time in milliseconds.
   * @return the {@link Outcome} of the hook.
   * @throws UaException to reject the call with a specific status code, e.g. {@link
   *     StatusCodes#Bad_ShelvingTimeOutOfRange}.
   */
  default Outcome onTimedShelve(InvocationContext context, double shelvingTimeMillis)
      throws UaException {
    return Outcome.PROCEED;
  }

  /**
   * Called before the default OneShotShelve semantics are applied.
   *
   * @param context the {@link InvocationContext} of the method call.
   * @return the {@link Outcome} of the hook.
   * @throws UaException to reject the call with a specific status code.
   */
  default Outcome onOneShotShelve(InvocationContext context) throws UaException {
    return Outcome.PROCEED;
  }

  /**
   * Called before the default Unshelve semantics are applied.
   *
   * @param context the {@link InvocationContext} of the method call.
   * @return the {@link Outcome} of the hook.
   * @throws UaException to reject the call with a specific status code.
   */
  default Outcome onUnshelve(InvocationContext context) throws UaException {
    return Outcome.PROCEED;
  }
}
