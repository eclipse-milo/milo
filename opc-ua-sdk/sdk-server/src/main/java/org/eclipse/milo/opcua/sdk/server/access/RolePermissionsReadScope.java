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

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.Nullable;

/**
 * Thread-local memoization scope for a single default Read service call.
 *
 * <p>During a Read handled by {@link DefaultAccessController}, access control reads {@code
 * UserRolePermissions} before the AddressSpace serves {@code RolePermissions}. Memoizing the
 * Session's resolved role IDs and each {@link UaNode}'s resolved {@code RolePermissions} attribute
 * for the duration of that synchronous Read avoids duplicate filter-chain and {@code RoleMapper}
 * evaluations while keeping {@link DefaultAccessController} authorization active on every Read and
 * never caching across requests.
 */
public final class RolePermissionsReadScope implements AutoCloseable {

  private static final ThreadLocal<@Nullable RolePermissionsReadScope> CURRENT =
      new ThreadLocal<>();

  private final Session session;
  private final @Nullable RolePermissionsReadScope previous;
  private @Nullable Optional<List<NodeId>> cachedRoleIds;
  private @Nullable IdentityHashMap<UaNode, RolePermissionsResult> rolePermissionsByNode;
  private boolean closed;

  private RolePermissionsReadScope(Session session, @Nullable RolePermissionsReadScope previous) {
    this.session = session;
    this.previous = previous;
  }

  /**
   * Open a new memoization scope for {@code session} on the current thread. Always close the
   * returned scope in a {@code try}-with-resources block.
   *
   * @param session the requesting {@link Session}.
   * @return the opened scope.
   */
  public static RolePermissionsReadScope open(Session session) {
    RolePermissionsReadScope previous = CURRENT.get();
    var scope = new RolePermissionsReadScope(session, previous);
    CURRENT.set(scope);
    return scope;
  }

  /**
   * Returns {@code true} if a memoization scope for {@code session} is already active on the
   * current thread.
   *
   * @param session the requesting {@link Session}.
   * @return {@code true} when an active scope already matches {@code session}.
   */
  public static boolean isActive(Session session) {
    RolePermissionsReadScope scope = CURRENT.get();
    return scope != null && scope.session == session;
  }

  /**
   * Resolve {@link Session#getRoleIds()}, reusing the memoized result when a scope for {@code
   * session} is active on the current thread.
   *
   * @param session the requesting {@link Session}.
   * @return the Session's mapped role IDs, or empty when no {@code RoleMapper} is configured.
   */
  public static Optional<List<NodeId>> getRoleIds(Session session) {
    RolePermissionsReadScope scope = CURRENT.get();
    if (scope != null && scope.session == session) {
      Optional<List<NodeId>> roleIds = scope.cachedRoleIds;
      if (roleIds == null) {
        roleIds = session.getRoleIds();
        scope.cachedRoleIds = roleIds;
      }
      return roleIds;
    }

    return session.getRoleIds();
  }

  /**
   * Return the memoized {@code RolePermissions} evaluation result for {@code (session, node)} in
   * the active scope, or {@code null} when no scope is active for {@code session} or {@code node}
   * has not been evaluated in this scope.
   *
   * @param session the requesting {@link Session}.
   * @param node the target {@link UaNode}.
   * @return the memoized evaluation result, or {@code null} on cache miss.
   */
  public static @Nullable RolePermissionsResult getRolePermissions(Session session, UaNode node) {
    RolePermissionsReadScope scope = CURRENT.get();
    if (scope != null && scope.session == session && scope.rolePermissionsByNode != null) {
      return scope.rolePermissionsByNode.get(node);
    }

    return null;
  }

  /**
   * Record the resolved {@code RolePermissions} value for {@code (session, node)} when a scope for
   * {@code session} is active on the current thread.
   *
   * @param session the requesting {@link Session}.
   * @param node the target {@link UaNode}.
   * @param value the raw attribute value returned by the node's filter chain (may be {@code null}).
   */
  public static void putRolePermissionsValue(Session session, UaNode node, @Nullable Object value) {
    RolePermissionsReadScope scope = CURRENT.get();
    if (scope != null && scope.session == session) {
      scope.rolePermissionsMap().put(node, new RolePermissionsResult.Value(value));
    }
  }

  /**
   * Record a {@link UaException} thrown while reading {@code RolePermissions} for {@code (session,
   * node)} when a scope for {@code session} is active on the current thread.
   *
   * @param session the requesting {@link Session}.
   * @param node the target {@link UaNode}.
   * @param exception the exception thrown by the node's filter chain.
   */
  public static void putRolePermissionsFailure(
      Session session, UaNode node, UaException exception) {
    RolePermissionsReadScope scope = CURRENT.get();
    if (scope != null && scope.session == session) {
      scope.rolePermissionsMap().put(node, new RolePermissionsResult.Failure(exception));
    }
  }

  private IdentityHashMap<UaNode, RolePermissionsResult> rolePermissionsMap() {
    if (rolePermissionsByNode == null) {
      rolePermissionsByNode = new IdentityHashMap<>(4);
    }
    return rolePermissionsByNode;
  }

  @Override
  public void close() {
    if (closed) {
      return;
    }
    closed = true;

    if (previous != null) {
      CURRENT.set(previous);
    } else {
      CURRENT.remove();
    }
  }

  /** Memoized outcome of evaluating a node's {@code RolePermissions} filter chain. */
  public sealed interface RolePermissionsResult {

    /** Filter chain returned {@code value} (possibly {@code null}). */
    record Value(@Nullable Object value) implements RolePermissionsResult {}

    /** Filter chain threw {@code exception}. */
    record Failure(UaException exception) implements RolePermissionsResult {}
  }
}
