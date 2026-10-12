/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

/**
 * Listener for server-side session lifecycle notifications.
 *
 * <p>{@link SessionManager} delivers these callbacks asynchronously on its listener queue. During
 * server shutdown, callbacks that are already running are allowed to finish before diagnostics and
 * namespace teardown proceed, while callbacks that have not started may be skipped once shutdown
 * has been requested. Implementations should therefore treat these notifications as best-effort
 * lifecycle observations rather than as ownership of the Session itself.
 */
public interface SessionListener {

  /**
   * Called after a Session has been created and registered with the {@link SessionManager}.
   *
   * @param session the created Session.
   */
  default void onSessionCreated(Session session) {}

  /**
   * Called after ActivateSession has replaced the user identity of an active Session on the
   * Session's existing SecureChannel (Part 4 §5.7.3.1).
   *
   * <p>The first activation of a Session is not reported here, since {@link
   * #onSessionCreated(Session)} already covers that Session, and neither is re-activation on a
   * replacement SecureChannel, which requires the same identity. The new identity may represent the
   * same user as before, so a listener that caches anything derived from the identity should
   * refresh it either way.
   *
   * @param session the Session whose identity changed; {@link Session#getIdentity()} returns the
   *     new identity.
   */
  default void onSessionIdentityChanged(Session session) {}

  /**
   * Called after ActivateSession has moved an active Session onto a replacement SecureChannel (Part
   * 4 §5.7.3.1), so that {@link Session#getEndpoint()} and {@link
   * Session#getSecurityConfiguration()} describe the new channel.
   *
   * <p>The identity is unchanged, since re-activation on another channel requires the same user,
   * but the MessageSecurityMode and the endpoint a {@link RoleMapper} consults may differ. The new
   * endpoint may describe the same security as before, so a listener that caches anything derived
   * from the endpoint should refresh it either way. A re-activation that fails leaves the Session
   * on its previous channel and is not reported.
   *
   * @param session the Session whose SecureChannel and endpoint changed.
   */
  default void onSessionEndpointChanged(Session session) {}

  /**
   * Called after a Session has been closed and removed from the {@link SessionManager}.
   *
   * @param session the closed Session.
   */
  default void onSessionClosed(Session session) {}
}
