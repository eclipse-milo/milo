/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

/**
 * Establishes, recovers, and updates the Session used by {@link
 * org.eclipse.milo.opcua.sdk.client.OpcUaClient}. The state machine creates and activates Sessions,
 * switches user identity on an active Session, runs initializers, and monitors connectivity. A
 * reactivation can reuse the same Session object, so object identity alone does not identify an
 * activation.
 *
 * <p>Session activity notifications run in transition order on a serial executor backed by the
 * transport's caller-owned executor. The state machine can continue while an earlier notification
 * waits to run. Consumers such as the publishing manager observe inactive before the subsequent
 * active notification, which lets them suspend and recover their work in that order. Rejected
 * dispatch runs inline to preserve this lifecycle ordering; the client does not shut down the
 * caller's executor. Every failed attempt that the state machine will retry is reported in the same
 * order, with the step that failed and its cause, so an application can see why the Session is not
 * coming back and disconnect to stop the retries. The step is the state the attempt failed in:
 * Creating, Activating, Transferring, Initializing or Reactivating. A SecureChannel that cannot be
 * established fails the step whose request waits for it, Creating or Reactivating. An attempt that
 * fails after the application started to disconnect is not reported, whether it was in progress
 * then or runs over a transport that is disconnected. A listener that throws is logged and does not
 * affect the state machine or the other listeners.
 *
 * <p>Changing the user identity of an active Session transitions from {@link
 * org.eclipse.milo.opcua.sdk.client.session.State#Active} to {@link
 * org.eclipse.milo.opcua.sdk.client.session.State#ChangingIdentity} and sends {@code
 * ActivateSession} on the Session's current SecureChannel. While the change is in flight, the
 * Session remains active: keep-alives, the transport connection listener, and the publishing
 * pipeline stay attached, and neither {@code onSessionInactive} nor {@code onSessionActive} fires
 * for transitions between {@code Active} and {@code ChangingIdentity}. Leaving that active pair
 * (for {@code Closing} or {@code ReactivatingWait}) aborts the pending identity change, preserves
 * the previous identity provider, tears down keep-alives and listeners, and fires {@code
 * onSessionInactive}. Calls outside {@code Active} fail fast with {@code Bad_SessionClosed} ({@code
 * Inactive}/{@code Closing}), {@code Bad_SessionNotActivated} (creating, activating, transferring,
 * initializing, or reactivating), or {@code Bad_InvalidState} (concurrent change while {@code
 * ChangingIdentity}).
 *
 * <p>The active {@link org.eclipse.milo.opcua.sdk.client.identity.IdentityProvider} is seeded from
 * {@link org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig#getIdentityProvider()} and kept in the
 * state machine context so the client config stays immutable. A successful identity change replaces
 * that active provider for all later reactivations and new Sessions, notifies {@code
 * onSessionIdentityChanged}, and updates the Session's last {@code ActivateSession} service result.
 * A refused identity change returns to {@code Active} under the previous identity without
 * abandoning the Session or firing {@code onSessionAttemptFailed}. When the new provider requires
 * an enhanced user-token policy (ECC or RSA-DH) and the Session does not already hold an ephemeral
 * key for that policy, the state machine first sends a pre-negotiation {@code ActivateSession} with
 * the current identity and the target {@code ECDHPolicyUri} (OPC 10000-6 §6.8.2), then sends the
 * identity-change {@code ActivateSession} encrypted with the returned key.
 *
 * <p>Initializers participate in Session activation. They must complete before operations waiting
 * for an active Session can proceed. Cleanup during initialization must therefore avoid waiting on
 * work that itself needs an active Session.
 *
 * <p>A Session retains its creation endpoint and application certificates. Resolver updates apply
 * to replacement channels and new Sessions. Reactivation keeps the Session's original server
 * certificate and client nonce, but uses the replacement channel certificate and thumbprint for
 * enhanced-policy signature inputs. CreateSession captures the selected endpoint and discovery list
 * together after channel establishment so endpoint comparisons use one resolution result. Once a
 * {@code CreateSessionResponse} arrives, any subsequent client-side verification refusal or
 * activation, transfer, or initialization failure closes that unactivated Session on the Server
 * best-effort while the channel is still connected so refused Sessions are not left to accumulate
 * until timeout. When disconnecting after the Session's SecureChannel has been lost, the state
 * machine skips {@code CloseSession} and transitions directly to {@code Inactive}, leaving the
 * Session to expire on the Server instead of waiting for a reconnect to send a request the Server
 * would reject on the new channel.
 */
package org.eclipse.milo.opcua.sdk.client.session;
