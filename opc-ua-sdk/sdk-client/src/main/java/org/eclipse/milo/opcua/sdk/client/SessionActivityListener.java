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
}
