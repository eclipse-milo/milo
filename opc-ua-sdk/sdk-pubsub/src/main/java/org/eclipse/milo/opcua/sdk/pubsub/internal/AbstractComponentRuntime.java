/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.internal;

import java.util.List;
import org.eclipse.milo.opcua.sdk.pubsub.ComponentType;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubStateChangeEvent;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;

/**
 * Base class for the runtime component tree: connections, writer/reader groups, and dataset
 * writers/readers.
 *
 * <p>Each component carries its enabled intent (the configured {@code Enabled} flag, possibly
 * overridden at runtime via handles) and its current {@link PubSubState}. Both are only mutated by
 * {@link PubSubStateMachine} while holding the engine lock; they are {@code volatile} so the
 * publish and dispatch paths can read them without locking.
 */
abstract class AbstractComponentRuntime {

  private final PubSubHandle handle;
  private final @Nullable AbstractComponentRuntime parent;

  private volatile boolean enabled;
  private volatile PubSubState state = PubSubState.Disabled;

  /**
   * The cause of the transition that entered {@code PreOperational}, remembered so a deferred
   * startup ({@link PubSubStateMachine#startupCompleted}) can attribute the final hop to {@code
   * Operational} to its original trigger. Engine-lock confined, like {@link #enabled}/{@link
   * #state} mutation: only read and written inside {@link PubSubStateMachine} methods.
   */
  private PubSubStateChangeEvent.@Nullable Cause pendingStartupCause;

  AbstractComponentRuntime(
      ComponentType componentType,
      String path,
      @Nullable AbstractComponentRuntime parent,
      boolean enabled) {

    this.handle = new PubSubHandle(componentType, path);
    this.parent = parent;
    this.enabled = enabled;
  }

  final PubSubHandle handle() {
    return handle;
  }

  final String path() {
    return handle.path();
  }

  final @Nullable AbstractComponentRuntime parent() {
    return parent;
  }

  final boolean isEnabled() {
    return enabled;
  }

  /** Set the enabled intent. Only called by {@link PubSubStateMachine} under the engine lock. */
  final void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  final PubSubState state() {
    return state;
  }

  /** Set the current state. Only called by {@link PubSubStateMachine} under the engine lock. */
  final void setState(PubSubState state) {
    this.state = state;
  }

  final PubSubStateChangeEvent.@Nullable Cause pendingStartupCause() {
    return pendingStartupCause;
  }

  /**
   * Set the remembered startup cause. Only called by {@link PubSubStateMachine} under the engine
   * lock.
   */
  final void setPendingStartupCause(PubSubStateChangeEvent.@Nullable Cause cause) {
    this.pendingStartupCause = cause;
  }

  /** The direct children of this component; empty for leaves. */
  abstract List<? extends AbstractComponentRuntime> children();

  /**
   * Hook invoked when this component enters {@code PreOperational}: acquire runtime resources
   * (channels, mapping providers, scheduled tasks). Called under the engine lock.
   *
   * @throws UaException if startup resources could not be acquired; the component transitions to
   *     {@code Error}.
   */
  void activate() throws UaException {}

  /**
   * Whether startup completes as soon as {@link #activate()} returns. Components that wait for an
   * external condition (e.g. a DataSetReader waiting for its first message) return {@code false}
   * and stay {@code PreOperational} until {@link PubSubStateMachine#startupCompleted} is called.
   */
  boolean startupCompletesImmediately() {
    return true;
  }

  /** Hook invoked whenever this component enters {@code Operational}. */
  void onEnterOperational() {}

  /**
   * Hook invoked when this component leaves the active states ({@code PreOperational}, {@code
   * Operational}, {@code Error}) for {@code Disabled} or {@code Paused}: release runtime resources
   * acquired by {@link #activate()}. Called under the engine lock.
   */
  void deactivate() {}
}
