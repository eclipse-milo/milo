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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.SessionListener;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The server's authorization subsystem: the {@link AccessController} that decides, the {@link
 * ReadAccessCache} that remembers read decisions for components that re-check them, and the
 * invalidation that forgets them and tells every {@link ReadAccessListener}.
 *
 * <p>{@link OpcUaServer} owns one instance, available from {@link
 * OpcUaServer#getAccessControlManager()}. The controller comes from the configuration's {@link
 * org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig#getAccessControllerFactory() factory}, or is
 * a {@link DefaultAccessController} when none is configured. The service implementations ask the
 * controller directly; components that refresh read access results on their own go through the
 * cache.
 *
 * <p>Call {@link #invalidateReadAccess(ReadAccessScope)} after committing a change that can alter a
 * read access answer: a role mapping, a per-Session attribute filter, a Node removed and re-added
 * under the same NodeId. The SDK calls it for a Session whose identity or endpoint changed, and
 * drops a closed Session's entries itself.
 *
 * <pre>{@code
 * node.setUserAccessLevel(AccessLevel.toValue(AccessLevel.NONE));
 * server.getAccessControlManager().invalidateReadAccess(node.getNodeId());
 * }</pre>
 *
 * <p>All methods are safe to call from any thread. Listeners are called on the invalidating thread,
 * after the cache has dropped the entries.
 */
public final class AccessControlManager {

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final List<ReadAccessListener> readAccessListeners = new CopyOnWriteArrayList<>();

  private final AccessController accessController;
  private final ReadAccessCache readAccessCache;

  /**
   * Create the manager for {@code server}: its controller from the configured factory or the
   * default, its cache, and the eviction of a closed Session's cached decisions.
   *
   * @param server the server this manager authorizes for. Its configuration and {@code
   *     SessionManager} must already exist.
   */
  public AccessControlManager(OpcUaServer server) {
    accessController =
        server
            .getConfig()
            .getAccessControllerFactory()
            .map(factory -> factory.apply(server))
            .orElseGet(() -> new DefaultAccessController(server));

    readAccessCache = new ReadAccessCache(accessController);

    // A closed Session's read access decisions can never be asked for again.
    server
        .getSessionManager()
        .addSessionListener(
            new SessionListener() {
              @Override
              public void onSessionClosed(Session session) {
                readAccessCache.invalidate(ReadAccessScope.session(session));
              }
            });
  }

  /**
   * Get the {@link AccessController} every service implementation authorizes requests with.
   *
   * @return the {@link AccessController}.
   */
  public AccessController getAccessController() {
    return accessController;
  }

  /**
   * Get the server-wide cache of read access decisions.
   *
   * <p>Components that refresh read access results on their own share it. Drop entries through
   * {@link #invalidateReadAccess(ReadAccessScope)}, not on the cache directly, so listeners hear
   * about it too.
   *
   * @return the {@link ReadAccessCache}.
   */
  public ReadAccessCache getReadAccessCache() {
    return readAccessCache;
  }

  /**
   * Add a {@link ReadAccessListener} to be told, after every {@link
   * #invalidateReadAccess(ReadAccessScope)}, which read access answers may have changed.
   *
   * @param listener the {@link ReadAccessListener} to add.
   */
  public void addReadAccessListener(ReadAccessListener listener) {
    readAccessListeners.add(listener);
  }

  /**
   * Remove a previously added {@link ReadAccessListener}.
   *
   * @param listener the {@link ReadAccessListener} to remove.
   */
  public void removeReadAccessListener(ReadAccessListener listener) {
    readAccessListeners.remove(listener);
  }

  /**
   * Say that the read access answers in {@code scope} may have changed.
   *
   * <p>Drops the cached decisions the scope covers and then tells every {@link ReadAccessListener},
   * on the calling thread, so a component that refreshes read access results on its own can
   * re-check the items the scope covers and see current answers. A listener that throws is logged
   * and the remaining listeners are still called.
   *
   * <pre>{@code
   * // Permissions on one device's Nodes changed for every user.
   * manager.invalidateReadAccess(ReadAccessScope.matching(nodeId -> isDeviceNode(nodeId)));
   * }</pre>
   *
   * @param scope the Sessions and Nodes whose answers may have changed.
   */
  public void invalidateReadAccess(ReadAccessScope scope) {
    readAccessCache.invalidate(scope);

    for (ReadAccessListener listener : readAccessListeners) {
      try {
        listener.onReadAccessChanged(scope);
      } catch (Throwable t) {
        logger.error("Uncaught Throwable in ReadAccessListener.onReadAccessChanged", t);
      }
    }
  }

  /**
   * Say that every read access answer for every Session may have changed.
   *
   * @see #invalidateReadAccess(ReadAccessScope)
   */
  public void invalidateReadAccess() {
    invalidateReadAccess(ReadAccessScope.all());
  }

  /**
   * Say that every read access answer for {@code session} may have changed.
   *
   * @param session the Session whose answers may have changed.
   * @see #invalidateReadAccess(ReadAccessScope)
   */
  public void invalidateReadAccess(Session session) {
    invalidateReadAccess(ReadAccessScope.session(session));
  }

  /**
   * Say that every Session's read access answer for {@code nodeId} may have changed.
   *
   * @param nodeId the Node whose answers may have changed.
   * @see #invalidateReadAccess(ReadAccessScope)
   */
  public void invalidateReadAccess(NodeId nodeId) {
    invalidateReadAccess(ReadAccessScope.node(nodeId));
  }
}
