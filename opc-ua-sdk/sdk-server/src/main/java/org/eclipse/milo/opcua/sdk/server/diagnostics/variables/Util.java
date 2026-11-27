/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.diagnostics.variables;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.core.typetree.ReferenceTypeTree;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.access.FallbackUserRolePermissionsFilter;
import org.eclipse.milo.opcua.sdk.server.access.RolePermissionsSupport;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterChain;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterContext;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilters;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

/** Internal helpers shared by the diagnostics node lifecycle implementations. */
class Util {

  /**
   * Default role permission policy for SessionSecurityDiagnosticsArray and its dynamic elements
   * when no explicit {@link AttributeId#RolePermissions} are configured on the node.
   */
  static final List<RolePermissionType> DEFAULT_SESSION_SECURITY_DIAGNOSTICS_ROLE_PERMISSIONS =
      List.of(
          new RolePermissionType(
              NodeIds.WellKnownRole_SecurityAdmin, new PermissionType(uint(59391))));

  /**
   * Default role permission policy for ServerDiagnostics.EnabledFlag when no explicit {@link
   * AttributeId#RolePermissions} are configured on the node.
   */
  static final List<RolePermissionType> DEFAULT_ENABLED_FLAG_ROLE_PERMISSIONS =
      List.of(
          new RolePermissionType(NodeIds.WellKnownRole_Anonymous, new PermissionType(uint(33))),
          new RolePermissionType(
              NodeIds.WellKnownRole_ConfigureAdmin, new PermissionType(uint(59391))),
          new RolePermissionType(
              NodeIds.WellKnownRole_SecurityAdmin, new PermissionType(uint(59391))));

  private static final ThreadLocal<@Nullable Set<UserRolePermissionsKey>>
      RESOLVING_USER_ROLE_PERMISSIONS = new ThreadLocal<>();

  private static final class UserRolePermissionsKey {
    private final UaNode node;
    private final @Nullable Session session;

    private UserRolePermissionsKey(UaNode node, @Nullable Session session) {
      this.node = node;
      this.session = session;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof UserRolePermissionsKey other)) {
        return false;
      }
      return node == other.node && session == other.session;
    }

    @Override
    public int hashCode() {
      return 31 * System.identityHashCode(node) + System.identityHashCode(session);
    }
  }

  private Util() {}

  static String buildBrowseNamePath(UaNode node) {
    return buildBrowseNamePath(node, new ArrayList<>());
  }

  private static String buildBrowseNamePath(UaNode node, List<String> browseNames) {
    if (node == null || node.getNodeId().equals(NodeIds.ObjectsFolder)) {
      Collections.reverse(browseNames);

      return String.join(".", browseNames);
    }

    ReferenceTypeTree referenceTypeTree = node.getNodeContext().getServer().getReferenceTypeTree();

    browseNames.add(node.getBrowseName().toParseableString());

    Optional<Reference> referenceToParent =
        node.getReferences().stream()
            .filter(
                r ->
                    r.isInverse()
                        && referenceTypeTree.isSubtypeOf(
                            r.getReferenceTypeId(), NodeIds.HierarchicalReferences))
            .findFirst();

    Optional<UaNode> parentNode =
        referenceToParent.flatMap(
            r ->
                node.getNodeContext()
                    .getServer()
                    .getAddressSpaceManager()
                    .getManagedNode(r.getTargetNodeId()));

    return buildBrowseNamePath(parentNode.orElse(null), browseNames);
  }

  /**
   * Creates a filter that answers Value reads from {@code get} while diagnostics are enabled.
   *
   * <p>While diagnostics are disabled, the Value is null with status {@link
   * StatusCodes#Bad_OutOfService}. The node's access levels still allow reading, so the status
   * reports that the diagnostics source is off rather than that the node is unreadable.
   *
   * @param diagnosticsEnabled the current state of the ServerDiagnostics EnabledFlag.
   * @param get supplies the Value while diagnostics are enabled.
   * @return a filter for the Value attribute of a diagnostics Variable.
   */
  static AttributeFilter diagnosticValueFilter(
      AtomicBoolean diagnosticsEnabled, Function<AttributeFilterContext, DataValue> get) {

    return AttributeFilters.getValue(
        ctx -> {
          if (diagnosticsEnabled.get()) {
            return get.apply(ctx);
          } else {
            return new DataValue(
                Variant.NULL_VALUE, new StatusCode(StatusCodes.Bad_OutOfService), DateTime.now());
          }
        });
  }

  /**
   * Creates a filter that derives {@link AttributeId#UserAccessLevel} from the node's explicit
   * {@link AttributeId#RolePermissions}, failing closed when none are configured.
   *
   * @return a filter that exposes a Session-specific UserAccessLevel.
   */
  static AttributeFilter roleBasedUserAccessLevelFilter() {
    return roleBasedUserAccessLevelFilter(List.of());
  }

  /**
   * Creates a filter that derives {@link AttributeId#UserAccessLevel} from the node's explicit
   * {@link AttributeId#RolePermissions}, falling back to {@code defaultRolePermissions} when the
   * node has no explicit {@code RolePermissions} configured.
   *
   * <p>The node's configured access level remains the upper bound. The filter only removes read or
   * write access when none of the Session's roles has the corresponding permission. Internal reads
   * without a Session receive the unfiltered attribute value.
   *
   * @param defaultRolePermissions the fallback role permissions used when the node's {@code
   *     RolePermissions} attribute is {@code null}.
   * @return a filter that exposes a Session-specific UserAccessLevel.
   */
  static AttributeFilter roleBasedUserAccessLevelFilter(
      List<RolePermissionType> defaultRolePermissions) {
    return roleBasedUserAccessLevelFilter(null, defaultRolePermissions);
  }

  /**
   * Creates a filter that derives {@link AttributeId#UserAccessLevel} from the node's explicit
   * {@link AttributeId#RolePermissions}, inheriting {@code inheritedPolicySource}'s live {@code
   * RolePermissions} when this node has no local override and falling back to {@code
   * defaultRolePermissions} when neither defines an explicit policy.
   *
   * <p>The node's configured access level remains the upper bound. The filter only removes read or
   * write access when none of the Session's roles has the corresponding permission. Internal reads
   * without a Session receive the unfiltered attribute value.
   *
   * @param inheritedPolicySource optional source node whose effective {@code RolePermissions}
   *     policy is inherited when this node has no local explicit {@code RolePermissions}, or {@code
   *     null} for local-only resolution.
   * @param defaultRolePermissions the fallback role permissions used when neither this node nor
   *     {@code inheritedPolicySource} has explicit {@code RolePermissions} configured.
   * @return a filter that exposes a Session-specific UserAccessLevel.
   */
  static AttributeFilter roleBasedUserAccessLevelFilter(
      @Nullable UaNode inheritedPolicySource, List<RolePermissionType> defaultRolePermissions) {

    List<RolePermissionType> fallbackPolicy = List.copyOf(defaultRolePermissions);

    return new AttributeFilter() {
      @Override
      public @Nullable Object getAttribute(AttributeFilterContext ctx, AttributeId attributeId) {
        if (attributeId != AttributeId.UserAccessLevel || ctx.getSession().isEmpty()) {
          return ctx.getAttribute(attributeId);
        }

        Object baseValue = ctx.getAttribute(AttributeId.UserAccessLevel);
        if (!(baseValue instanceof UByte baseUserAccessLevel)) {
          return AccessLevel.toValue(AccessLevel.NONE);
        }

        return applyRoleBasedUserAccessLevel(
            ctx, baseUserAccessLevel, inheritedPolicySource, fallbackPolicy);
      }

      @Override
      public @Nullable Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
          throws UaException {
        if (attributeId != AttributeId.UserAccessLevel || ctx.getSession().isEmpty()) {
          return ctx.readAttribute(attributeId);
        }

        Object baseValue = ctx.readAttribute(AttributeId.UserAccessLevel);
        if (!(baseValue instanceof UByte baseUserAccessLevel)) {
          return AccessLevel.toValue(AccessLevel.NONE);
        }

        return applyRoleBasedUserAccessLevel(
            ctx, baseUserAccessLevel, inheritedPolicySource, fallbackPolicy);
      }

      @Override
      public void writeAttribute(
          AttributeFilterContext ctx, AttributeId attributeId, @Nullable Object value)
          throws UaException {
        ctx.writeAttribute(attributeId, value);
      }
    };
  }

  private static UByte applyRoleBasedUserAccessLevel(
      AttributeFilterContext ctx,
      UByte baseUserAccessLevel,
      @Nullable UaNode inheritedPolicySource,
      List<RolePermissionType> fallbackPolicy) {

    Set<AccessLevel> userAccessLevels = AccessLevel.fromValue(baseUserAccessLevel);
    RolePermissionType[] userRolePermissions =
        getEffectiveUserRolePermissions(ctx, inheritedPolicySource, fallbackPolicy);

    // RolePermissions can remove rights from the configured AccessLevel, but never add them.
    if (!hasReadPermission(userRolePermissions)) {
      userAccessLevels.remove(AccessLevel.CurrentRead);
    }
    if (!hasWritePermission(userRolePermissions)) {
      userAccessLevels.remove(AccessLevel.CurrentWrite);
    }

    return AccessLevel.toValue(userAccessLevels);
  }

  /**
   * Creates a filter that exposes only the node's explicit {@link AttributeId#RolePermissions}
   * applicable to the current Session as {@link AttributeId#UserRolePermissions}.
   *
   * <p>When the node's {@code RolePermissions} attribute is {@code null}, this filter returns
   * {@code null} so {@code UserRolePermissions} reports {@code Bad_AttributeIdInvalid}
   * symmetrically with {@code RolePermissions} (OPC 10000-3 §5.2.9, §5.2.10). Internal reads
   * without a Session receive the unfiltered attribute value.
   *
   * @return a filter that exposes Session-specific UserRolePermissions when RolePermissions are
   *     explicitly configured.
   */
  static AttributeFilter roleBasedUserRolePermissionsFilter() {
    return roleBasedUserRolePermissionsFilter(null);
  }

  /**
   * Creates a filter that exposes only the node's explicit {@link AttributeId#RolePermissions}
   * applicable to the current Session as {@link AttributeId#UserRolePermissions}, while supplying
   * {@code defaultRolePermissions} as an internal fallback policy for access control checks (such
   * as Browse) when the node's {@code RolePermissions} attribute is {@code null}.
   *
   * @param defaultRolePermissions optional fallback role permissions used internally by {@link
   *     org.eclipse.milo.opcua.sdk.server.access.DefaultAccessController} when the node's {@code
   *     RolePermissions} attribute is {@code null}, or {@code null} for no fallback policy.
   * @return a filter that exposes explicit UserRolePermissions and supplies internal fallback
   *     permissions when configured.
   */
  static AttributeFilter roleBasedUserRolePermissionsFilter(
      @Nullable List<RolePermissionType> defaultRolePermissions) {
    return roleBasedUserRolePermissionsFilter(null, defaultRolePermissions);
  }

  /** Marker interface for {@link Util}'s role-based {@code UserRolePermissions} filter. */
  private interface RoleBasedUserRolePermissionsFilter extends FallbackUserRolePermissionsFilter {}

  static boolean hasRoleBasedUserRolePermissionsFilter(AttributeFilterChain filterChain) {
    for (AttributeFilter filter : filterChain.getFilters()) {
      if (filter instanceof RoleBasedUserRolePermissionsFilter) {
        return true;
      }
    }
    return false;
  }

  /**
   * Creates a filter that exposes explicit {@link AttributeId#RolePermissions} applicable to the
   * current Session as {@link AttributeId#UserRolePermissions}, dynamically inheriting {@code
   * RolePermissions} from {@code inheritedPolicySource} when this node has no local explicit {@code
   * RolePermissions}, and falling back to {@code defaultRolePermissions} internally when neither
   * node defines an explicit policy.
   *
   * @param inheritedPolicySource optional source node whose effective {@code RolePermissions}
   *     policy (including front-of-chain {@code readAttribute} filters) is inherited when this node
   *     has no explicit {@code RolePermissions}, or {@code null} for local-only resolution.
   * @param defaultRolePermissions optional fallback role permissions used internally by {@link
   *     org.eclipse.milo.opcua.sdk.server.access.DefaultAccessController} when no explicit {@code
   *     RolePermissions} apply, or {@code null} for no fallback policy.
   * @return a filter that exposes explicit UserRolePermissions and supplies internal fallback
   *     permissions when configured.
   */
  static AttributeFilter roleBasedUserRolePermissionsFilter(
      @Nullable UaNode inheritedPolicySource,
      @Nullable List<RolePermissionType> defaultRolePermissions) {

    List<RolePermissionType> fallbackPolicy =
        defaultRolePermissions != null ? List.copyOf(defaultRolePermissions) : null;

    return new RoleBasedUserRolePermissionsFilter() {
      @Override
      public @Nullable Object getAttribute(AttributeFilterContext ctx, AttributeId attributeId) {
        if (attributeId == AttributeId.UserRolePermissions && ctx.getSession().isPresent()) {
          Object explicit = ctx.getAttribute(AttributeId.UserRolePermissions);
          if (explicit != null) {
            return explicit;
          }
          return getExplicitUserRolePermissions(ctx, inheritedPolicySource);
        } else if (attributeId == AttributeId.RolePermissions) {
          // Do not substitute inheritedPolicySource into the Node's own RolePermissions attribute
          // (OPC 10000-3 §5.2.9): when no local override is configured, let UaNode/AttributeReader
          // see null so writable nodes expose an empty array and non-writable nodes omit it while
          // UserRolePermissions and fallback enforcement continue inheriting inheritedPolicySource.
          Object value = ctx.getAttribute(AttributeId.RolePermissions);
          if (ctx.getSession().isPresent()) {
            return value != null ? sanitizeRolePermissions(value) : null;
          } else {
            return value;
          }
        }

        return ctx.getAttribute(attributeId);
      }

      @Override
      public @Nullable Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
          throws UaException {

        if (attributeId == AttributeId.UserRolePermissions && ctx.getSession().isPresent()) {
          Object explicit = ctx.readAttribute(AttributeId.UserRolePermissions);
          if (explicit != null) {
            return explicit;
          }
          return getExplicitUserRolePermissions(ctx, inheritedPolicySource);
        } else if (attributeId == AttributeId.RolePermissions) {
          // Return only the Node's own RolePermissions (without throwing Bad_UserAccessDenied here
          // so outer delegating front filters can inspect and augment it, and without substituting
          // inheritedPolicySource so cleared overrides expose an empty array on writable nodes or
          // Bad_AttributeIdInvalid on non-writable nodes per OPC 10000-3 §5.2.9).
          Object value = ctx.readAttribute(AttributeId.RolePermissions);
          if (ctx.getSession().isPresent()) {
            return value != null ? sanitizeRolePermissions(value) : null;
          } else {
            return value;
          }
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

        Object value = resolveExplicitPolicy(session, node, null, inheritedPolicySource);

        if (value == null) {
          return fallbackPolicy != null ? filterForSession(session, fallbackPolicy.stream()) : null;
        } else if (value instanceof RolePermissionType[] rolePermissions) {
          return filterForSession(session, Stream.of(rolePermissions));
        } else {
          // Malformed role metadata must not grant access.
          return new RolePermissionType[0];
        }
      }
    };
  }

  private static @Nullable Object resolveExplicitPolicy(
      @Nullable Session session,
      UaNode node,
      @Nullable AttributeFilterContext ctx,
      @Nullable UaNode inheritedPolicySource) {

    Object value = RolePermissionsSupport.readNodeRolePermissions(session, node, ctx);
    if (value == null && inheritedPolicySource != null && inheritedPolicySource != node) {
      value = readNodeUserRolePermissions(session, inheritedPolicySource, null);
      if (value == null) {
        value =
            RolePermissionsSupport.readNodeRolePermissions(session, inheritedPolicySource, null);
      }
    }
    return value;
  }

  private static RolePermissionType[] getEffectiveUserRolePermissions(
      AttributeFilterContext ctx,
      @Nullable UaNode inheritedPolicySource,
      List<RolePermissionType> defaultRolePermissions) {

    Session session = ctx.getSession().orElse(null);
    UaNode node = ctx.getNode();

    // Honor any explicit UserRolePermissions override (front filter, stored attribute, or
    // downstream filter — including an explicit empty array) before falling back to local
    // RolePermissions, inherited source policy, or the default diagnostics policy.
    Object explicitUserRolePermissions = readNodeUserRolePermissions(session, node, ctx);
    if (explicitUserRolePermissions != null) {
      if (explicitUserRolePermissions instanceof RolePermissionType[] rolePermissions) {
        return RolePermissionsSupport.filterForSession(session, Stream.of(rolePermissions));
      } else {
        // Malformed role metadata must not grant access.
        return new RolePermissionType[0];
      }
    }

    Object value = resolveExplicitPolicy(session, node, ctx, inheritedPolicySource);
    if (value != null) {
      if (value instanceof RolePermissionType[] rolePermissions) {
        return RolePermissionsSupport.filterForSession(session, Stream.of(rolePermissions));
      } else {
        // Malformed role metadata must not grant access.
        return new RolePermissionType[0];
      }
    }

    return RolePermissionsSupport.filterForSession(session, defaultRolePermissions.stream());
  }

  private static @Nullable Object readNodeUserRolePermissions(
      @Nullable Session session, UaNode node, @Nullable AttributeFilterContext ctx) {

    var key = new UserRolePermissionsKey(node, session);
    Set<UserRolePermissionsKey> active = RESOLVING_USER_ROLE_PERMISSIONS.get();
    if (active != null && active.contains(key)) {
      // Fail closed on a UserAccessLevel <-> UserRolePermissions cycle.
      return new RolePermissionType[0];
    }

    boolean created = false;
    if (active == null) {
      active = new HashSet<>(2);
      RESOLVING_USER_ROLE_PERMISSIONS.set(active);
      created = true;
    }

    active.add(key);
    try {
      AttributeFilterChain filterChain = node.getFilterChain();
      if (filterChain != null) {
        try {
          return filterChain.readAttribute(session, node, AttributeId.UserRolePermissions);
        } catch (UaException e) {
          if (e.getStatusCode().getValue() == StatusCodes.Bad_AttributeIdInvalid) {
            return null;
          }
          // Fail closed if a filter rejects reading UserRolePermissions.
          return new RolePermissionType[0];
        }
      } else if (ctx != null) {
        try {
          return ctx.readAttribute(AttributeId.UserRolePermissions);
        } catch (UaException e) {
          if (e.getStatusCode().getValue() == StatusCodes.Bad_AttributeIdInvalid) {
            return null;
          }
          return new RolePermissionType[0];
        }
      } else {
        return node.getAttribute(AttributeId.UserRolePermissions);
      }
    } finally {
      active.remove(key);
      if (created) {
        RESOLVING_USER_ROLE_PERMISSIONS.remove();
      }
    }
  }

  private static RolePermissionType @Nullable [] getExplicitUserRolePermissions(
      AttributeFilterContext ctx, @Nullable UaNode inheritedPolicySource) {

    Object value =
        resolveExplicitPolicy(
            ctx.getSession().orElse(null), ctx.getNode(), ctx, inheritedPolicySource);
    if (value == null) {
      return null;
    } else if (value instanceof RolePermissionType[] rolePermissions) {
      return RolePermissionsSupport.filterForSession(
          ctx.getSession().orElse(null), Stream.of(rolePermissions));
    } else {
      // Malformed role metadata must not grant access.
      return new RolePermissionType[0];
    }
  }

  private static RolePermissionType[] sanitizeRolePermissions(Object value) {
    return RolePermissionsSupport.sanitizeRolePermissions(value);
  }

  private static RolePermissionType[] filterForSession(
      @Nullable Session session, Stream<RolePermissionType> rolePermissions) {
    return RolePermissionsSupport.filterForSession(session, rolePermissions);
  }

  private static boolean hasReadPermission(RolePermissionType[] rolePermissions) {
    return Stream.of(rolePermissions)
        .anyMatch(
            rolePermission ->
                RolePermissionsSupport.isValidRolePermission(rolePermission)
                    && rolePermission.getPermissions().getRead());
  }

  private static boolean hasWritePermission(RolePermissionType[] rolePermissions) {
    return Stream.of(rolePermissions)
        .anyMatch(
            rolePermission ->
                RolePermissionsSupport.isValidRolePermission(rolePermission)
                    && rolePermission.getPermissions().getWrite());
  }
}
