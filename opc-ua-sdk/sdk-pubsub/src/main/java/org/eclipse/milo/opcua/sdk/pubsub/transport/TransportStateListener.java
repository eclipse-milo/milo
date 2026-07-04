/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.transport;

import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;

/**
 * Listener a transport channel uses to report the liveness of its underlying connection (e.g. an
 * MQTT broker session). Optional: connectionless transports (UDP) never signal.
 *
 * <p>Invocations may occur on transport threads and must return quickly; the engine implementation
 * hops to its own executor. Implementations must tolerate duplicate notifications (both channels of
 * a connection may share one session) and must be edge-tolerant: {@link #onTransportUp()} may be
 * invoked for the initial establishment.
 *
 * <p>Connection state reflects broker liveness only after the first established session: the
 * initial connect is asynchronous and sends fail fast until it completes, without a down
 * notification.
 */
public interface TransportStateListener {

  /**
   * The transport connection was lost.
   *
   * @param statusCode a {@link StatusCode} describing the failure.
   */
  void onTransportDown(StatusCode statusCode);

  /** The transport connection is (re)established and subscriptions are re-issued. */
  void onTransportUp();
}
