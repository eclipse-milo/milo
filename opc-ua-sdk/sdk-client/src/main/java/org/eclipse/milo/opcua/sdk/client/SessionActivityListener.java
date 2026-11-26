/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaServiceFaultException;
import org.eclipse.milo.opcua.stack.transport.client.ChannelStateObservable;

/**
 * Callbacks notified as the {@link OpcUaClient}'s Session becomes available, stops being available,
 * and fails to be established or recovered.
 *
 * <p>All callbacks run on one sequential executor backed by the transport's executor, in the order
 * the Session's state changed. A listener that throws is logged and does not affect the client or
 * the other listeners. Return quickly: a callback that blocks delays every notification after it.
 */
public interface SessionActivityListener {

  /**
   * An activated {@link UaSession} is now available.
   *
   * <p>Holding a reference to this session is not necessary or advised; it is provided merely for
   * informational purposes.
   */
  default void onSessionActive(UaSession session) {}

  /**
   * The previously activated {@link UaSession} is no longer available.
   *
   * <p>Holding a reference to this session is not necessary or advised; it is provided merely for
   * informational purposes.
   */
  default void onSessionInactive(UaSession session) {}

  /**
   * An attempt to establish or recover a Session failed, and the client will try again.
   *
   * <p>Once {@link OpcUaClient#connect()} has been called, the client keeps a Session open on its
   * own. It reactivates the Session after a connection loss, or creates and activates a new one,
   * and retries each failed attempt after a delay, for as long as it stays connected. This is
   * called once for every failed attempt, the first one made by {@code connect()} included, whose
   * failure also completes {@code connect()} exceptionally. It is not called once the application
   * has started to disconnect: an attempt that fails while the client is disconnecting, or over a
   * transport the application disconnected, is not reported.
   *
   * <p>When the Server rejected a request, {@code failure} is the {@link UaServiceFaultException}
   * carrying the Server's StatusCode, e.g. {@code Bad_UserAccessDenied}, {@code
   * Bad_IdentityTokenRejected} or {@code Bad_IdentityTokenInvalid} when the Server does not accept
   * the client's identity. {@link UaException#extractStatusCode(Throwable)} finds the StatusCode of
   * any failure that has one. When no SecureChannel could be established, the failure can be just a
   * timeout; {@link ChannelStateObservable.TransitionListener#onConnectFailure} on the client's
   * transport reports why the SecureChannel failed.
   *
   * <p>Retrying cannot fix every failure. Rejected credentials stay rejected, and a Server that
   * locks an account after repeated failed logins may lock the client's. To stop retrying,
   * disconnect the client with {@link OpcUaClient#disconnectAsync()}. Don't wait for it to complete
   * in this callback: the blocking {@link OpcUaClient#disconnect()} would hold up the executor
   * every callback runs on.
   *
   * <pre>{@code
   * client.addSessionActivityListener(new SessionActivityListener() {
   *   @Override
   *   public void onSessionAttemptFailed(Throwable failure) {
   *     long status = UaException.extractStatusCode(failure)
   *         .map(StatusCode::value)
   *         .orElse(StatusCodes.Good);
   *
   *     if (status == StatusCodes.Bad_UserAccessDenied
   *         || status == StatusCodes.Bad_IdentityTokenRejected
   *         || status == StatusCodes.Bad_IdentityTokenInvalid) {
   *       // Retrying with the same identity cannot succeed.
   *       client.disconnectAsync();
   *     }
   *   }
   * });
   * }</pre>
   *
   * @param failure the cause of the failed attempt.
   */
  default void onSessionAttemptFailed(Throwable failure) {}
}
