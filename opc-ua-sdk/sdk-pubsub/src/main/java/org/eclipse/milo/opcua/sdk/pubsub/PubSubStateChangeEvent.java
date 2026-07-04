/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub;

import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;

/**
 * A state change of a PubSub component, delivered to {@link PubSubStateListener}s.
 *
 * @param component the handle of the component that changed state.
 * @param oldState the state the component transitioned from.
 * @param newState the state the component transitioned to.
 * @param statusCode a status code describing the reason for the transition.
 * @param cause why the component transitioned (Part 14 Table 311 attribution plus engine
 *     lifecycle). Deferred startups (e.g. a DataSetReader completing on its first message) carry
 *     the cause of the transition that entered {@code PreOperational} on their final hop to {@code
 *     Operational}.
 */
public record PubSubStateChangeEvent(
    PubSubHandle component,
    PubSubState oldState,
    PubSubState newState,
    StatusCode statusCode,
    Cause cause) {

  /** Why a component transitioned (Part 14 Table 311 attribution + engine lifecycle). */
  public enum Cause {
    /** An explicit enable/disable: the owner API or a remote Enable/Disable Method. */
    METHOD,
    /** A parent's state change cascaded to this component. */
    PARENT,
    /** A pending error moved the component to Error. */
    ERROR,
    /** The error condition cleared and the component returned to Operational. */
    ERROR_RECOVERY,
    /** Service startup, shutdown, or reconfiguration (re)started the component. */
    STARTUP,
    /** The component is being disposed (shutdown or removal by reconfiguration). */
    DISPOSE
  }
}
