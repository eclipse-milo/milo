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

import java.util.Collection;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Part 14 §6.2.1 PubSubState machine, applied to the {@link AbstractComponentRuntime} tree.
 *
 * <p>Transition rules (Table 2, plus the pinned resolution of its ambiguities):
 *
 * <ul>
 *   <li>a disabled component is {@code Disabled}, regardless of its parent;
 *   <li>an enabled component whose parent chain is not fully {@code Operational} is {@code Paused}
 *       — a parent in {@code Error} is treated like a parent in {@code Paused} (the spec leaves
 *       this case undefined);
 *   <li>an enabled component with an {@code Operational} parent goes {@code PreOperational} and,
 *       once its startup steps complete, {@code Operational}. Most components complete startup
 *       synchronously in {@link AbstractComponentRuntime#activate()}; DataSetReaders wait for their
 *       first DataSetMessage and complete via {@link #startupCompleted};
 *   <li>a pending error moves an active component to {@code Error}; the error clearing moves it
 *       back to {@code Operational} via {@link #recover}.
 * </ul>
 *
 * <p>All transitions happen under the engine lock; the state change listener is invoked while
 * holding it and must not run user code synchronously.
 */
final class PubSubStateMachine {

  private static final Logger LOGGER = LoggerFactory.getLogger(PubSubStateMachine.class);

  private final Object lock;
  private final StateChangeListener listener;

  private boolean rootOperational = false;

  PubSubStateMachine(Object lock, StateChangeListener listener) {
    this.lock = lock;
    this.listener = listener;
  }

  /** Listener invoked, under the engine lock, for every state transition. */
  @FunctionalInterface
  interface StateChangeListener {

    void onStateChange(
        AbstractComponentRuntime component,
        PubSubState oldState,
        PubSubState newState,
        StatusCode statusCode);
  }

  /**
   * Set whether the (implicit) root PublishSubscribe component is operational, i.e. the service is
   * started and the configuration root is enabled, and recompute the state of every connection.
   */
  void setRootOperational(
      boolean operational, Collection<? extends AbstractComponentRuntime> connections) {

    synchronized (lock) {
      rootOperational = operational;
      connections.forEach(this::recompute);
    }
  }

  /** Compute the initial state of a newly registered component (and its descendants). */
  void initialize(AbstractComponentRuntime component) {
    synchronized (lock) {
      recompute(component);
    }
  }

  /** Set the enabled intent of a component and recompute its (and its descendants') state. */
  void setEnabled(AbstractComponentRuntime component, boolean enabled) {
    synchronized (lock) {
      if (component.isEnabled() == enabled) {
        return;
      }
      component.setEnabled(enabled);
      recompute(component);
    }
  }

  /** Move an active component to {@code Error} because of a pending error situation. */
  void fail(AbstractComponentRuntime component, StatusCode statusCode) {
    synchronized (lock) {
      PubSubState state = component.state();
      if (state == PubSubState.PreOperational || state == PubSubState.Operational) {
        apply(component, PubSubState.Error, statusCode);
      }
    }
  }

  /** Move a component in {@code Error} back to {@code Operational}: the error was resolved. */
  void recover(AbstractComponentRuntime component) {
    synchronized (lock) {
      if (component.state() == PubSubState.Error) {
        apply(component, PubSubState.Operational, StatusCode.GOOD);
      }
    }
  }

  /** Complete the startup of a {@code PreOperational} component, moving it to Operational. */
  void startupCompleted(AbstractComponentRuntime component) {
    synchronized (lock) {
      if (component.state() == PubSubState.PreOperational) {
        apply(component, PubSubState.Operational, StatusCode.GOOD);
      }
    }
  }

  /**
   * Transition a component subtree to {@code Disabled} (children first) ahead of its removal from
   * the runtime tree, running the deactivate hooks of active components.
   */
  void disposeSubtree(AbstractComponentRuntime component) {
    synchronized (lock) {
      disposeRecursive(component);
    }
  }

  private void disposeRecursive(AbstractComponentRuntime component) {
    component.children().forEach(this::disposeRecursive);

    component.setEnabled(false);

    PubSubState oldState = component.state();
    if (oldState != PubSubState.Disabled) {
      component.setState(PubSubState.Disabled);
      notifyListener(component, oldState, PubSubState.Disabled, StatusCode.GOOD);

      if (isActive(oldState)) {
        deactivateQuietly(component);
      }
    }
  }

  private void recompute(AbstractComponentRuntime component) {
    PubSubState current = component.state();

    PubSubState target;
    if (!component.isEnabled()) {
      target = PubSubState.Disabled;
    } else if (!parentOperational(component)) {
      target = PubSubState.Paused;
    } else if (current == PubSubState.Disabled || current == PubSubState.Paused) {
      target = PubSubState.PreOperational;
    } else {
      target = current;
    }

    if (target != current) {
      apply(component, target, StatusCode.GOOD);
    } else {
      component.children().forEach(this::recompute);
    }
  }

  private boolean parentOperational(AbstractComponentRuntime component) {
    AbstractComponentRuntime parent = component.parent();
    return parent == null ? rootOperational : parent.state() == PubSubState.Operational;
  }

  private void apply(AbstractComponentRuntime component, PubSubState newState, StatusCode status) {
    PubSubState oldState = component.state();
    if (oldState == newState) {
      return;
    }

    component.setState(newState);
    notifyListener(component, oldState, newState, status);

    if (isActive(oldState) && !isActive(newState)) {
      deactivateQuietly(component);
    }

    if (newState == PubSubState.PreOperational) {
      try {
        component.activate();

        if (component.state() == PubSubState.PreOperational
            && component.startupCompletesImmediately()) {

          apply(component, PubSubState.Operational, StatusCode.GOOD);
          return;
        }
      } catch (UaException e) {
        LOGGER.debug("Activation of '{}' failed: {}", component.path(), e.getMessage(), e);
        apply(component, PubSubState.Error, e.getStatusCode());
        return;
      } catch (RuntimeException e) {
        LOGGER.warn("Activation of '{}' failed", component.path(), e);
        apply(component, PubSubState.Error, new StatusCode(StatusCodes.Bad_InternalError));
        return;
      }
    }

    if (newState == PubSubState.Operational) {
      try {
        component.onEnterOperational();
      } catch (RuntimeException e) {
        LOGGER.warn("onEnterOperational hook of '{}' failed", component.path(), e);
      }
    }

    component.children().forEach(this::recompute);
  }

  private void notifyListener(
      AbstractComponentRuntime component,
      PubSubState oldState,
      PubSubState newState,
      StatusCode status) {

    try {
      listener.onStateChange(component, oldState, newState, status);
    } catch (RuntimeException e) {
      LOGGER.warn("State change listener failed for '{}'", component.path(), e);
    }
  }

  private void deactivateQuietly(AbstractComponentRuntime component) {
    try {
      component.deactivate();
    } catch (RuntimeException e) {
      LOGGER.warn("Deactivation of '{}' failed", component.path(), e);
    }
  }

  private static boolean isActive(PubSubState state) {
    return state == PubSubState.PreOperational
        || state == PubSubState.Operational
        || state == PubSubState.Error;
  }
}
