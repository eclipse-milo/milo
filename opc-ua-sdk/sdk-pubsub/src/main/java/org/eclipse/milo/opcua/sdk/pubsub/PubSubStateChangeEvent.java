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
 */
public record PubSubStateChangeEvent(
    PubSubHandle component, PubSubState oldState, PubSubState newState, StatusCode statusCode) {}
