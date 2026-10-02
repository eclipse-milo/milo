/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.sampling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.AccessController.AccessResult;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;

/**
 * Server-wide cache of read access decisions, keyed by Session, Node, and Attribute.
 *
 * <p>The server owns one instance, available from {@link OpcUaServer#getReadAccessCache()}. A
 * sampler on the {@link ReadAccessPolicy#cached()} policy looks its items up here on every cycle
 * and pays a map lookup for a hit; a miss is answered by the server's {@code AccessController} and
 * stored. A component outside the sampling framework that refreshes read access results on its own
 * can share the same entries through {@link #getOrCheck}.
 *
 * <p>Entries stay until something drops them: {@link OpcUaServer#invalidateReadAccess} for a change
 * the application or the SDK knows about, and the close of a Session for its entries. A cached
 * answer is as current as the last invalidation, so an application on the cached policy must call
 * {@code invalidateReadAccess} when its security configuration changes; an application that cannot
 * tell when that happens should stay on {@link ReadAccessPolicy#perCycle()}.
 *
 * <p>The key holds the Session object, not the user it represents, so a Subscription transferred to
 * another Session, or a user who reconnects, starts from a miss. {@link AccessResult#NODE_UNKNOWN}
 * is no decision and is never stored. All methods are safe to call from any thread.
 */
public final class ReadAccessCache {

  private record Key(NodeId nodeId, UInteger attributeId) {}

  /** How many times a check is made before answers overtaken by invalidations are given up on. */
  private static final int MAX_CHECK_ATTEMPTS = 3;

  private final Map<Session, Map<Key, AccessResult>> entries = new ConcurrentHashMap<>();

  // An invalidation bumps the generation under the write lock; a fill stores its answers under the
  // read lock only if the generation it read before checking is still current, so a check that
  // was in flight while its answers were invalidated cannot store them and is made again.
  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  private volatile long generation = 0;

  private final OpcUaServer server;

  /**
   * Create a cache that answers misses with {@code server}'s {@code AccessController}.
   *
   * @param server the server whose AccessController answers misses.
   */
  public ReadAccessCache(OpcUaServer server) {
    this.server = server;
  }

  /**
   * Look up a cached decision without checking.
   *
   * @param session the Session the decision is for.
   * @param readValueId the Node and Attribute the decision is for.
   * @return the cached decision, or empty if there is none.
   */
  public Optional<AccessResult> get(Session session, ReadValueId readValueId) {
    Map<Key, AccessResult> sessionEntries = entries.get(session);

    return sessionEntries == null
        ? Optional.empty()
        : Optional.ofNullable(sessionEntries.get(key(readValueId)));
  }

  /**
   * Get the decision for each of {@code readValueIds} under {@code session}, checking the misses
   * with the server's {@code AccessController} in one call and storing the decisions it returns.
   *
   * <p>An invalidation that arrives while the check is in flight says its answers may already be
   * wrong, so the misses are checked again, up to {@value #MAX_CHECK_ATTEMPTS} times in all; if
   * invalidations keep arriving, the last answers are returned without being stored, which leaves
   * them no staler than a check made one cycle earlier. Nothing is stored for a Session that has
   * closed or for an answer that is not a decision ({@link AccessResult#NODE_UNKNOWN}); those are
   * answered again on the next call. If the check throws, nothing is stored and the exception
   * propagates.
   *
   * @param session the Session to decide for.
   * @param readValueIds the Nodes and Attributes to decide for.
   * @return the decision for each of {@code readValueIds} the cache or the check answered for.
   */
  public Map<ReadValueId, AccessResult> getOrCheck(
      Session session, List<ReadValueId> readValueIds) {
    Map<Key, AccessResult> sessionEntries = entries.get(session);

    var results = new HashMap<ReadValueId, AccessResult>(readValueIds.size());
    var misses = new ArrayList<ReadValueId>();

    for (ReadValueId readValueId : readValueIds) {
      AccessResult cached = sessionEntries == null ? null : sessionEntries.get(key(readValueId));

      if (cached != null) {
        results.put(readValueId, cached);
      } else {
        misses.add(readValueId);
      }
    }

    if (misses.isEmpty()) {
      return results;
    }

    Map<ReadValueId, AccessResult> checked = Map.of();

    for (int attempt = 1; attempt <= MAX_CHECK_ATTEMPTS; attempt++) {
      long generationBeforeCheck = generation;

      checked = server.getAccessController().checkReadAccess(session, misses);

      if (session.isClosed() || store(session, checked, generationBeforeCheck)) {
        break;
      }

      // Overtaken by an invalidation: these answers may describe the state before it. Ask again.
    }

    results.putAll(checked);

    return results;
  }

  /**
   * Store the decisions in {@code checked} for {@code session}, unless an invalidation has run
   * since {@code generationBeforeCheck}, in which case nothing is stored.
   *
   * @return {@code true} if the answers were stored, {@code false} if they were overtaken.
   */
  private boolean store(
      Session session, Map<ReadValueId, AccessResult> checked, long generationBeforeCheck) {

    lock.readLock().lock();
    try {
      if (generation != generationBeforeCheck) {
        return false;
      }

      Map<Key, AccessResult> target =
          entries.computeIfAbsent(session, s -> new ConcurrentHashMap<>());

      checked.forEach(
          (readValueId, result) -> {
            if (result.isDecision()) {
              target.put(key(readValueId), result);
            }
          });

      return true;
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Drop every decision {@code scope} covers.
   *
   * <p>Application code calls {@link OpcUaServer#invalidateReadAccess} instead, which also tells
   * other components that the answers changed.
   *
   * @param scope the Sessions and Nodes whose decisions to drop.
   */
  public void invalidate(ReadAccessScope scope) {
    lock.writeLock().lock();
    try {
      generation++;

      Optional<Session> session = scope.session();

      if (session.isPresent()) {
        if (scope.coversAllNodes()) {
          entries.remove(session.get());
        } else {
          Map<Key, AccessResult> sessionEntries = entries.get(session.get());

          if (sessionEntries != null) {
            evict(sessionEntries, scope);
          }
        }
      } else if (scope.coversAllNodes()) {
        entries.clear();
      } else {
        entries.values().forEach(sessionEntries -> evict(sessionEntries, scope));
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * Get the number of cached decisions, across all Sessions.
   *
   * @return the number of cached decisions.
   */
  public int size() {
    return entries.values().stream().mapToInt(Map::size).sum();
  }

  private static void evict(Map<Key, AccessResult> sessionEntries, ReadAccessScope scope) {
    sessionEntries.keySet().removeIf(key -> scope.includesNode(key.nodeId()));
  }

  private static Key key(ReadValueId readValueId) {
    return new Key(readValueId.getNodeId(), readValueId.getAttributeId());
  }
}
