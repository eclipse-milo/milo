/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.access;

/**
 * Hears that the read access answers in a scope may have changed.
 *
 * <p>{@link AccessControlManager#invalidateReadAccess(ReadAccessScope)} calls every listener added
 * with {@link AccessControlManager#addReadAccessListener} after the server's {@link
 * ReadAccessCache} has dropped the entries the scope covers, so a listener that re-checks items in
 * response sees the cache miss and fill with current answers. The SDK invalidates when a Session's
 * identity or endpoint changes and when a Subscription is transferred; an application invalidates,
 * through {@code invalidateReadAccess}, when something it knows about changes an answer, such as a
 * role mapping or a per-Session attribute filter.
 *
 * <p>A listener is called on the thread that called {@code invalidateReadAccess}, so it should
 * record what changed and return; a re-check belongs on the listener's own executor. An exception
 * from a listener is logged and the remaining listeners are still called.
 *
 * <pre>{@code
 * server.getAccessControlManager().addReadAccessListener(scope -> refresher.markStale(scope));
 * }</pre>
 */
@FunctionalInterface
public interface ReadAccessListener {

  /**
   * The read access answers in {@code scope} may have changed.
   *
   * @param scope the Sessions and Nodes whose answers may have changed.
   */
  void onReadAccessChanged(ReadAccessScope scope);
}
