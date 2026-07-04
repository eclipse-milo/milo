/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.pubsub.config.SecurityGroupConfig;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.jspecify.annotations.Nullable;

/**
 * The default {@link PubSubMethodAuthorizer} posture: with a {@code RoleMapper} configured the
 * well-known roles apply; without one, callers are allowed unless a SecurityGroup carries explicit
 * non-empty RolePermissions (fail closed on explicit restrictions).
 *
 * <p>{@link ServerPubSub} resolves one effective authorizer per attachment — the options-configured
 * {@link PubSubMethodAuthorizer} if present, else an instance of this class — and hands the same
 * instance to every PubSub Method handler (SKS, remote configuration, and diagnostics), so one
 * posture governs every check.
 */
final class DefaultPubSubMethodAuthorizer implements PubSubMethodAuthorizer {

  private static final StatusCode DENIED = new StatusCode(StatusCodes.Bad_UserAccessDenied);

  private final Function<String, @Nullable SecurityGroupConfig> groupLookup;

  /**
   * Create a default authorizer whose {@link #checkKeyAccess} decisions consult {@code groupLookup}
   * for per-group RolePermissions.
   *
   * @param groupLookup resolves a SecurityGroupId to its configuration, or {@code null} for unknown
   *     groups. Only {@link #checkKeyAccess} consults it.
   */
  DefaultPubSubMethodAuthorizer(Function<String, @Nullable SecurityGroupConfig> groupLookup) {
    this.groupLookup = groupLookup;
  }

  @Override
  public StatusCode checkConfigure(Session session) {
    Optional<List<NodeId>> roleIds = session.getRoleIds();

    // no RoleMapper: allow, consistent with the core posture
    return roleIds
        .map(ids -> ids.contains(NodeIds.WellKnownRole_ConfigureAdmin) ? StatusCode.GOOD : DENIED)
        .orElse(StatusCode.GOOD);
  }

  @Override
  public StatusCode checkSksAdmin(Session session) {
    Optional<List<NodeId>> roleIds = session.getRoleIds();

    // no RoleMapper: allow, consistent with the core posture
    return roleIds
        .map(
            ids ->
                ids.contains(NodeIds.WellKnownRole_SecurityKeyServerAdmin)
                    ? StatusCode.GOOD
                    : DENIED)
        .orElse(StatusCode.GOOD);
  }

  @Override
  public StatusCode checkKeyAccess(Session session, String securityGroupId) {
    // getRoleIds() is recomputed per call; call it exactly once per decision
    Optional<List<NodeId>> maybeRoleIds = session.getRoleIds();

    @Nullable SecurityGroupConfig group = groupLookup.apply(securityGroupId);
    List<RolePermissionType> rolePermissions =
        group != null ? group.getRolePermissions() : List.of();

    if (maybeRoleIds.isEmpty()) {
      // no RoleMapper configured: allow encrypted callers, but fail closed on groups
      // carrying explicit restrictions; unknown groups are allowed through so the
      // handler's existence check can return Bad_NotFound
      return rolePermissions.isEmpty() ? StatusCode.GOOD : DENIED;
    }

    // a RoleMapper returning an empty list is still "mapper configured": the role rows
    // apply and deny
    List<NodeId> roleIds = maybeRoleIds.get();

    if (!rolePermissions.isEmpty()) {
      boolean allowed =
          rolePermissions.stream().anyMatch(rp -> roleIds.contains(rp.getRoleId()) && canCall(rp));

      return allowed ? StatusCode.GOOD : DENIED;
    }

    // no explicit per-group restrictions, or the group is unknown: require the default
    // pull roles; unauthorized callers cannot distinguish unknown from denied groups
    boolean allowed =
        roleIds.contains(NodeIds.WellKnownRole_SecurityKeyServerAccess)
            || roleIds.contains(NodeIds.WellKnownRole_SecurityKeyServerAdmin);

    return allowed ? StatusCode.GOOD : DENIED;
  }

  private static boolean canCall(RolePermissionType rolePermission) {
    PermissionType permissions = rolePermission.getPermissions();
    return permissions != null && permissions.getCall();
  }
}
