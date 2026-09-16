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
   * Called after a Session has been closed and removed from the {@link SessionManager}.
   *
   * @param session the closed Session.
   */
  default void onSessionClosed(Session session) {}
}
