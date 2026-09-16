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
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterContext;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilters;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;

/** Internal helpers shared by the diagnostics node lifecycle implementations. */
class Util {

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

  static AttributeFilter diagnosticValueFilter(
      AtomicBoolean diagnosticsEnabled, Function<AttributeFilterContext, DataValue> get) {

    return AttributeFilters.getValue(
        ctx -> {
          if (diagnosticsEnabled.get()) {
            return get.apply(ctx);
          } else {
            return new DataValue(
                Variant.NULL_VALUE, new StatusCode(StatusCodes.Bad_NotReadable), DateTime.now());
          }
        });
  }

  /**
   * Creates a filter that derives {@link AttributeId#UserAccessLevel} from the current Session's
   * effective {@link AttributeId#RolePermissions}.
   *
   * <p>The node's configured access level remains the upper bound. The filter only removes read or
   * write access when none of the Session's roles has the corresponding permission. Internal reads
   * without a Session receive the unfiltered attribute value.
   *
   * @return a filter that exposes a Session-specific UserAccessLevel.
   */
  static AttributeFilter roleBasedUserAccessLevelFilter() {
    return new AttributeFilter() {
      @Override
      public Object getAttribute(AttributeFilterContext ctx, AttributeId attributeId) {
        if (attributeId != AttributeId.UserAccessLevel || ctx.getSession().isEmpty()) {
          return ctx.getAttribute(attributeId);
        }

        var userAccessLevels =
            AccessLevel.fromValue((UByte) ctx.getAttribute(AttributeId.UserAccessLevel));
        RolePermissionType[] userRolePermissions = getUserRolePermissions(ctx);

        // RolePermissions can remove rights from the configured AccessLevel, but never add them.
        if (!hasReadPermission(userRolePermissions)) {
          userAccessLevels.remove(AccessLevel.CurrentRead);
        }
        if (!hasWritePermission(userRolePermissions)) {
          userAccessLevels.remove(AccessLevel.CurrentWrite);
        }

        return AccessLevel.toValue(userAccessLevels);
      }
    };
  }

  /**
   * Creates a filter that exposes only the {@link AttributeId#RolePermissions} applicable to the
   * current Session as {@link AttributeId#UserRolePermissions}.
   *
   * <p>This lets the server's normal access controller authorize operations such as Browse using
   * the same role metadata that controls Value access. Internal reads without a Session receive the
   * unfiltered attribute value.
   *
   * @return a filter that exposes Session-specific UserRolePermissions.
   */
  static AttributeFilter roleBasedUserRolePermissionsFilter() {
    return new AttributeFilter() {
      @Override
      public Object getAttribute(AttributeFilterContext ctx, AttributeId attributeId) {
        if (attributeId == AttributeId.UserRolePermissions && ctx.getSession().isPresent()) {
          return getUserRolePermissions(ctx);
        } else {
          return ctx.getAttribute(attributeId);
        }
      }
    };
  }

  private static RolePermissionType[] getUserRolePermissions(AttributeFilterContext ctx) {
    // OPC UA assigns the Anonymous Role to every Session, including authenticated Sessions.
    Set<NodeId> roleIds = new HashSet<>();
    roleIds.add(NodeIds.WellKnownRole_Anonymous);
    ctx.getSession().flatMap(Session::getRoleIds).ifPresent(roleIds::addAll);

    Object value = ctx.getAttribute(AttributeId.RolePermissions);
    if (value instanceof RolePermissionType[] rolePermissions) {
      return Stream.of(rolePermissions)
          .filter(rolePermission -> roleIds.contains(rolePermission.getRoleId()))
          .toArray(RolePermissionType[]::new);
    } else {
      // Missing or malformed role metadata must not grant access.
      return new RolePermissionType[0];
    }
  }

  private static boolean hasReadPermission(RolePermissionType[] rolePermissions) {
    return Stream.of(rolePermissions)
        .anyMatch(rolePermission -> rolePermission.getPermissions().getRead());
  }

  private static boolean hasWritePermission(RolePermissionType[] rolePermissions) {
    return Stream.of(rolePermissions)
        .anyMatch(rolePermission -> rolePermission.getPermissions().getWrite());
  }
}
