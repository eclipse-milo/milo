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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;

/**
 * Server-wide cache of read access decisions, keyed by Session, Node, and Attribute.
 *
 * <p>The server owns one instance, available from {@link
 * AccessControlManager#getReadAccessCache()}. A sampler on the {@link
 * org.eclipse.milo.opcua.sdk.server.sampling.ReadAccessPolicy#cached()} policy looks its items up
 * here on every cycle and pays a map lookup for a hit; a miss is answered by the server's {@code
 * AccessController} and stored. A component outside the sampling framework that refreshes read
 * access results on its own can share the same entries through {@link #getOrCheck}.
 *
 * <p>Entries stay until something drops them: {@link AccessControlManager#invalidateReadAccess} for
 * a change the application or the SDK knows about, and the close of a Session for its entries. A
 * cached answer is as current as the last invalidation, so an application on the cached policy must
 * call {@code invalidateReadAccess} when its security configuration changes; an application that
 * cannot tell when that happens should stay on {@link
 * org.eclipse.milo.opcua.sdk.server.sampling.ReadAccessPolicy#perCycle()}.
 *
 * <p>The key holds the Session object, not the user it represents, so a Subscription transferred to
 * another Session, or a user who reconnects, starts from a miss. {@link AccessResult#NODE_UNKNOWN}
 * is no decision and is never stored. All methods are safe to call from any thread.
 */
public final class ReadAccessCache {

  private record Key(NodeId nodeId, UInteger attributeId) {}

  /** The generations a lookup for one Session started under. */
  private record Generation(long shared, long session) {}

  /** How many times a lookup is made before answers overtaken by invalidations are given up on. */
  private static final int MAX_CHECK_ATTEMPTS = 3;

  private final Map<Session, Map<Key, AccessResult>> entries = new ConcurrentHashMap<>();

  // An invalidation bumps a generation under the write lock: the Session's own for a scope with a
  // Session, the shared one for a scope that covers every Session. A lookup reads both before it
  // starts and uses its answers, hits included, and stores them under the read lock, only if
  // neither has moved since. So a lookup that an invalidation of its answers overtook is made
  // again, and an invalidation of another Session does not disturb it.
  private final ReadWriteLock lock = new ReentrantReadWriteLock();
  private volatile long sharedGeneration = 0;
  private final Map<Session, Long> sessionGenerations = new ConcurrentHashMap<>();

  private final AccessController accessController;

  /**
   * Create a cache that answers misses with {@code accessController}.
   *
   * @param accessController the controller that answers misses.
   */
  public ReadAccessCache(AccessController accessController) {
    this.accessController = accessController;
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
   * <p>An invalidation that covers {@code session} and arrives during the lookup says that its hits
   * and its answers may already be wrong, so the whole lookup is made again, up to {@value
   * #MAX_CHECK_ATTEMPTS} times in all; if invalidations keep arriving, the last answers are
   * returned without being stored, which leaves them no staler than a check made one cycle earlier.
   * An invalidation of another Session's answers does not count. Nothing is stored for a Session
   * that has closed or for an answer that is not a decision ({@link AccessResult#NODE_UNKNOWN});
   * those are answered again on the next call. If the check throws, nothing is stored and the
   * exception propagates.
   *
   * @param session the Session to decide for.
   * @param readValueIds the Nodes and Attributes to decide for.
   * @return the decision for each of {@code readValueIds} the cache or the check answered for.
   */
  public Map<ReadValueId, AccessResult> getOrCheck(
      Session session, List<ReadValueId> readValueIds) {

    var results = new HashMap<ReadValueId, AccessResult>(readValueIds.size());

    for (int attempt = 1; attempt <= MAX_CHECK_ATTEMPTS; attempt++) {
      Generation before = generation(session);
      Map<Key, AccessResult> sessionEntries = entries.get(session);

      results.clear();
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
        if (before.equals(generation(session))) {
          return results;
        }
      } else {
        Map<ReadValueId, AccessResult> checked = accessController.checkReadAccess(session, misses);
        results.putAll(checked);

        if (store(session, checked, before)) {
          return results;
        }
      }

      // Overtaken by an invalidation: the hits and the answers may describe the state before it.
    }

    return results;
  }

  /**
   * Store the decisions in {@code checked} for {@code session}, unless an invalidation that covers
   * it has run since {@code before}, in which case nothing is stored. Nothing is stored for a
   * closed Session either, and its answers are used as they are, since it reads nothing again.
   *
   * @return {@code true} if the answers stand, {@code false} if they were overtaken.
   */
  private boolean store(
      Session session, Map<ReadValueId, AccessResult> checked, Generation before) {
    lock.readLock().lock();
    try {
      // Checked under the lock: the close sets the flag before its invalidation takes the write
      // lock, so whatever is stored before that invalidation is dropped by it.
      if (session.isClosed()) {
        return true;
      }

      if (!before.equals(generation(session))) {
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

  private Generation generation(Session session) {
    return new Generation(sharedGeneration, sessionGenerations.getOrDefault(session, 0L));
  }

  /**
   * Drop every decision {@code scope} covers.
   *
   * <p>Application code calls {@link AccessControlManager#invalidateReadAccess} instead, which also
   * tells other components that the answers changed.
   *
   * @param scope the Sessions and Nodes whose decisions to drop.
   */
  public void invalidate(ReadAccessScope scope) {
    lock.writeLock().lock();
    try {
      Optional<Session> session = scope.session();

      if (session.isPresent()) {
        // A closed Session's answers are never stored again, so it needs no generation, and
        // keeping one would hold it for good.
        if (session.get().isClosed()) {
          sessionGenerations.remove(session.get());
        } else {
          sessionGenerations.merge(session.get(), 1L, Long::sum);
        }

        if (scope.coversAllNodes()) {
          entries.remove(session.get());
        } else {
          Map<Key, AccessResult> sessionEntries = entries.get(session.get());

          if (sessionEntries != null) {
            evict(sessionEntries, scope);
          }
        }
      } else {
        //noinspection NonAtomicOperationOnVolatileField
        sharedGeneration++;

        if (scope.coversAllNodes()) {
          entries.clear();
        } else {
          entries.values().forEach(sessionEntries -> evict(sessionEntries, scope));
        }
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
