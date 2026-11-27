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
   * An attempt to establish or recover a Session failed at {@code step}, and the client will try
   * again.
   *
   * <p>Once {@link OpcUaClient#connect()} has been called, the client keeps a Session open on its
   * own. It reactivates the Session after a connection loss, or creates and activates a new one,
   * and retries each failed attempt after a delay, for as long as it stays connected. This is
   * called once for every failed attempt, the first one made by {@code connect()} included, whose
   * failure also completes {@code connect()} exceptionally. It is not called once the application
   * has started to disconnect, or once the transport no longer connects on its own: an attempt that
   * fails while the client is disconnecting, or over a transport that is {@link
   * ChannelStateObservable.ChannelState#DISCONNECTED}, such as one the application disconnected, is
   * not reported.
   *
   * <p>{@code step} says which request of the attempt failed; the failure alone does not. A Server
   * can refuse a client application it does not trust at {@link AttemptStep#CREATE_SESSION}, with
   * {@code Bad_CertificateUntrusted}, instead of refusing its SecureChannel, and it refuses the
   * client's identity at {@link AttemptStep#ACTIVATE_SESSION} or {@link
   * AttemptStep#REACTIVATE_SESSION}.
   *
   * <p>When the Server rejected a request, {@code failure} is the {@link UaServiceFaultException}
   * carrying the Server's StatusCode, e.g. {@code Bad_UserAccessDenied}, {@code
   * Bad_IdentityTokenRejected} or {@code Bad_IdentityTokenInvalid} when the Server does not accept
   * the client's identity. A step also fails when the client could not send its request, or did not
   * accept the Server's response. {@link UaException#extractStatusCode(Throwable)} finds the
   * StatusCode of any failure that has one. When no SecureChannel could be established, the step
   * whose request was waiting for it fails, {@link AttemptStep#CREATE_SESSION} or {@link
   * AttemptStep#REACTIVATE_SESSION}, and the failure can be just a timeout; {@link
   * ChannelStateObservable.TransitionListener#onConnectFailure} on the client's transport reports
   * why the SecureChannel failed.
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
   *   public void onSessionAttemptFailed(AttemptStep step, Throwable failure) {
   *     long status = UaException.extractStatusCode(failure)
   *         .map(StatusCode::value)
   *         .orElse(StatusCodes.Good);
   *
   *     boolean identityRejected =
   *         status == StatusCodes.Bad_UserAccessDenied
   *             || status == StatusCodes.Bad_IdentityTokenRejected
   *             || status == StatusCodes.Bad_IdentityTokenInvalid;
   *
   *     if (identityRejected
   *         && (step == AttemptStep.ACTIVATE_SESSION || step == AttemptStep.REACTIVATE_SESSION)) {
   *       // Retrying with the same identity cannot succeed.
   *       client.disconnectAsync();
   *     }
   *   }
   * });
   * }</pre>
   *
   * <p>The default implementation calls {@link #onSessionAttemptFailed(Throwable)}, so a listener
   * that overrides only that method still hears about every failed attempt.
   *
   * @param step the step of the attempt that failed.
   * @param failure the cause of the failed attempt.
   */
  default void onSessionAttemptFailed(AttemptStep step, Throwable failure) {
    onSessionAttemptFailed(failure);
  }

  /**
   * An attempt to establish or recover a Session failed, and the client will try again.
   *
   * <p>Called by the default {@link #onSessionAttemptFailed(AttemptStep, Throwable)} with the same
   * failure. That method says when attempts are reported and what {@code failure} carries; override
   * it instead of this one to learn which step failed.
   *
   * @param failure the cause of the failed attempt.
   */
  default void onSessionAttemptFailed(Throwable failure) {}

  /**
   * A step of an attempt to establish or recover a Session, as reported by {@link
   * #onSessionAttemptFailed(AttemptStep, Throwable)}.
   *
   * <p>An attempt to establish a new Session takes the steps {@link #CREATE_SESSION}, {@link
   * #ACTIVATE_SESSION}, {@link #TRANSFER_SUBSCRIPTIONS} and {@link #INITIALIZE}, in that order. An
   * attempt to recover the Session after a connection loss takes {@link #REACTIVATE_SESSION} and
   * then {@link #INITIALIZE}; when the Server refuses the reactivation, the next attempt
   * establishes a new Session instead.
   */
  enum AttemptStep {

    /**
     * CreateSession, which creates the Session on the Server.
     *
     * <p>The client opens a SecureChannel first if none is open, so a SecureChannel the Server
     * refused fails this step, with a {@link
     * org.eclipse.milo.opcua.stack.transport.client.SecureChannelHandshakeException} in the cause
     * chain when the handshake failed. The step also fails when the client does not accept the
     * Server's response, e.g. its certificate, signature or endpoints.
     */
    CREATE_SESSION,

    /**
     * ActivateSession, which activates the Session just created with the client's identity.
     *
     * <p>A Server that does not accept the identity token from the client's {@link
     * org.eclipse.milo.opcua.sdk.client.identity.IdentityProvider} fails this step, and so does an
     * IdentityProvider that cannot supply one.
     */
    ACTIVATE_SESSION,

    /**
     * TransferSubscriptions, which moves the client's Subscriptions to the new Session.
     *
     * <p>It is sent only when the client has Subscriptions left from an earlier Session. A Server
     * that does not support the service, or does not transfer some of the Subscriptions, does not
     * fail this step: each Subscription not transferred is reset, and its listener hears {@link
     * org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription.SubscriptionListener#onTransferFailed}.
     */
    TRANSFER_SUBSCRIPTIONS,

    /**
     * Running the initializers added with {@link OpcUaClient#addSessionInitializer}, on the
     * activated Session, before it becomes available.
     *
     * <p>An initializer whose future completes exceptionally is logged and does not fail this step.
     */
    INITIALIZE,

    /**
     * ActivateSession, which recovers the existing Session over a new SecureChannel after the
     * connection was lost.
     *
     * <p>The client waits for the transport to establish the new SecureChannel first, so a
     * SecureChannel the Server refused fails this step too.
     */
    REACTIVATE_SESSION
  }
}
