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

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterChain;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterContext;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

/**
 * Shared helpers for resolving {@code RolePermissions} and deriving session-filtered {@code
 * UserRolePermissions} across built-in namespaces and diagnostics filters.
 */
public final class RolePermissionsSupport {

  private static final ThreadLocal<@Nullable Set<ResolutionKey>> RESOLVING_ROLE_PERMISSIONS =
      new ThreadLocal<>();

  /**
   * Stateless tail filter installed on loaded namespace-0 nodes so that configuring {@code
   * RolePermissions} on any ordinary standard node automatically derives session-filtered {@code
   * UserRolePermissions} (unless an explicit {@code UserRolePermissions} value or downstream filter
   * supplies one) and enforces {@code ReadRolePermissions} / {@code WriteRolePermissions}
   * consistently while keeping unconfigured nodes returning {@code Bad_AttributeIdInvalid}.
   */
  private static final AttributeFilter DERIVED_USER_ROLE_PERMISSIONS_FILTER =
      new FallbackUserRolePermissionsFilter() {
        @Override
        public @Nullable Object getAttribute(AttributeFilterContext ctx, AttributeId attributeId) {
          if (attributeId == AttributeId.UserRolePermissions
              && ctx.getSession().isPresent()
              && !hasOtherFallbackUserRolePermissionsFilter(ctx.getNode(), this)) {
            Object explicit = ctx.getAttribute(AttributeId.UserRolePermissions);
            if (explicit != null) {
              return explicit;
            }
            return deriveExplicitUserRolePermissions(ctx);
          }

          return ctx.getAttribute(attributeId);
        }

        @Override
        public @Nullable Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
            throws UaException {
          if (attributeId == AttributeId.UserRolePermissions
              && ctx.getSession().isPresent()
              && !hasOtherFallbackUserRolePermissionsFilter(ctx.getNode(), this)) {
            Object explicit = ctx.readAttribute(AttributeId.UserRolePermissions);
            if (explicit != null) {
              return explicit;
            }
            return deriveExplicitUserRolePermissions(ctx);
          }

          return ctx.readAttribute(attributeId);
        }

        @Override
        public void writeAttribute(
            AttributeFilterContext ctx, AttributeId attributeId, @Nullable Object value)
            throws UaException {
          ctx.writeAttribute(attributeId, value);
        }

        @Override
        public RolePermissionType @Nullable [] getFallbackUserRolePermissions(
            Session session, UaNode node) {
          if (hasOtherFallbackUserRolePermissionsFilter(node, this)) {
            return null;
          }

          Object value = readNodeRolePermissions(session, node, null);
          if (value == null) {
            return null;
          } else if (value instanceof RolePermissionType[] rolePermissions) {
            return filterForSession(session, Stream.of(rolePermissions));
          } else {
            // Malformed role metadata must not grant access.
            return new RolePermissionType[0];
          }
        }
      };

  private static final class ResolutionKey {
    private final UaNode node;
    private final @Nullable Session session;

    private ResolutionKey(UaNode node, @Nullable Session session) {
      this.node = node;
      this.session = session;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof ResolutionKey other)) {
        return false;
      }
      return node == other.node && session == other.session;
    }

    @Override
    public int hashCode() {
      return 31 * System.identityHashCode(node) + System.identityHashCode(session);
    }
  }

  private RolePermissionsSupport() {}

  /**
   * Returns a shared stateless {@link AttributeFilter} that derives session-filtered {@code
   * UserRolePermissions} from a node's {@code RolePermissions} when no explicit {@code
   * UserRolePermissions} override or dedicated {@link FallbackUserRolePermissionsFilter} is
   * present.
   *
   * @return the shared derived {@code UserRolePermissions} filter.
   */
  public static AttributeFilter derivedUserRolePermissionsFilter() {
    return DERIVED_USER_ROLE_PERMISSIONS_FILTER;
  }

  /**
   * Read {@link AttributeId#RolePermissions} through {@code node}'s full filter chain with a
   * per-{@code (node, session)} recursion guard and {@link RolePermissionsReadScope} memoization.
   *
   * <p>Returns {@code null} when no explicit {@code RolePermissions} are configured, and fails
   * closed with an empty {@code RolePermissionType[0]} array on cycles or {@link UaException}
   * failures.
   *
   * @param session the requesting {@link Session}, or {@code null} for internal reads.
   * @param node the target {@link UaNode}.
   * @param ctx optional active {@link AttributeFilterContext} used when {@code node} has no filter
   *     chain.
   * @return the raw resolved {@code RolePermissions} value, or {@code null} when absent.
   */
  public static @Nullable Object readNodeRolePermissions(
      @Nullable Session session, UaNode node, @Nullable AttributeFilterContext ctx) {

    var key = new ResolutionKey(node, session);
    Set<ResolutionKey> active = RESOLVING_ROLE_PERMISSIONS.get();
    if (active != null && active.contains(key)) {
      // Fail closed on a resolution cycle for the same (node, session).
      return new RolePermissionType[0];
    }

    AttributeFilterChain filterChain = node.getFilterChain();
    if (session != null && filterChain != null) {
      RolePermissionsReadScope.RolePermissionsResult cached =
          RolePermissionsReadScope.getRolePermissions(session, node);
      if (cached != null) {
        if (cached instanceof RolePermissionsReadScope.RolePermissionsResult.Value v) {
          return v.value();
        } else {
          // Fail closed if a filter rejected reading RolePermissions.
          return new RolePermissionType[0];
        }
      }
    }

    boolean created = false;
    if (active == null) {
      active = new HashSet<>(2);
      RESOLVING_ROLE_PERMISSIONS.set(active);
      created = true;
    }

    active.add(key);
    try {
      if (filterChain != null) {
        try {
          Object value = filterChain.readAttribute(session, node, AttributeId.RolePermissions);
          if (session != null) {
            RolePermissionsReadScope.putRolePermissionsValue(session, node, value);
          }
          return value;
        } catch (UaException e) {
          if (session != null) {
            RolePermissionsReadScope.putRolePermissionsFailure(session, node, e);
          }
          // Fail closed if a filter rejects reading RolePermissions.
          return new RolePermissionType[0];
        }
      } else if (ctx != null) {
        try {
          return ctx.readAttribute(AttributeId.RolePermissions);
        } catch (UaException e) {
          return new RolePermissionType[0];
        }
      } else {
        return node.getAttribute(AttributeId.RolePermissions);
      }
    } finally {
      active.remove(key);
      if (created) {
        RESOLVING_ROLE_PERMISSIONS.remove();
      }
    }
  }

  /**
   * Filter {@code rolePermissions} to valid entries matching {@code session}'s assigned roles plus
   * the implicit {@link NodeIds#WellKnownRole_Anonymous} role (OPC 10000-3 §4.9.1).
   *
   * @param session the requesting {@link Session}, or {@code null}.
   * @param rolePermissions the candidate role permissions.
   * @return the filtered valid {@link RolePermissionType} entries.
   */
  public static RolePermissionType[] filterForSession(
      @Nullable Session session, Stream<RolePermissionType> rolePermissions) {

    // OPC 10000-3 §4.9.1 assigns the Anonymous Role to every Session, including authenticated
    // Sessions.
    Set<NodeId> roleIds = new HashSet<>();
    roleIds.add(NodeIds.WellKnownRole_Anonymous);
    if (session != null) {
      RolePermissionsReadScope.getRoleIds(session).ifPresent(roleIds::addAll);
    }

    return rolePermissions
        .filter(
            rolePermission ->
                isValidRolePermission(rolePermission)
                    && roleIds.contains(rolePermission.getRoleId()))
        .toArray(RolePermissionType[]::new);
  }

  /**
   * Strip malformed entries from a raw {@code RolePermissions} attribute value, failing closed with
   * an empty array when the value is not a {@code RolePermissionType[]}.
   *
   * @param value the raw attribute value.
   * @return a sanitized {@link RolePermissionType} array.
   */
  public static RolePermissionType[] sanitizeRolePermissions(Object value) {
    if (value instanceof RolePermissionType[] rolePermissions) {
      return Stream.of(rolePermissions)
          .filter(RolePermissionsSupport::isValidRolePermission)
          .toArray(RolePermissionType[]::new);
    } else {
      // Malformed role metadata must fail closed rather than falling back to defaults.
      return new RolePermissionType[0];
    }
  }

  /**
   * Returns {@code true} when {@code rolePermission} has non-null {@code RoleId}, {@code
   * Permissions}, and permission mask value.
   *
   * @param rolePermission the entry to validate.
   * @return {@code true} if valid.
   */
  public static boolean isValidRolePermission(@Nullable RolePermissionType rolePermission) {
    return rolePermission != null
        && rolePermission.getRoleId() != null
        && rolePermission.getPermissions() != null
        && rolePermission.getPermissions().getValue() != null;
  }

  private static RolePermissionType @Nullable [] deriveExplicitUserRolePermissions(
      AttributeFilterContext ctx) {

    Session session = ctx.getSession().orElse(null);
    Object value = readNodeRolePermissions(session, ctx.getNode(), ctx);
    if (value == null) {
      return null;
    } else if (value instanceof RolePermissionType[] rolePermissions) {
      return filterForSession(session, Stream.of(rolePermissions));
    } else {
      // Malformed role metadata must not grant access.
      return new RolePermissionType[0];
    }
  }

  private static boolean hasOtherFallbackUserRolePermissionsFilter(
      UaNode node, AttributeFilter self) {

    AttributeFilterChain filterChain = node.getFilterChain();
    if (filterChain == null) {
      return false;
    }

    for (AttributeFilter filter : filterChain.getFilters()) {
      if (filter != self && filter instanceof FallbackUserRolePermissionsFilter) {
        return true;
      }
    }
    return false;
  }
}
