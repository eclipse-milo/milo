/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.diagnostics.variables;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterChain;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.junit.jupiter.api.Test;

class UtilTest {

  @Test
  void roleBasedFiltersRestrictSecurityDiagnosticsToConfiguredRoles() throws UaException {
    var node = mock(UaVariableNode.class);
    var session = mock(Session.class);
    var equivalentRole = new NodeId(1, "SecurityDiagnosticsAdmin");
    var readOnly = AccessLevel.toValue(AccessLevel.READ_ONLY);
    var noAccess = AccessLevel.toValue(AccessLevel.NONE);
    var securityAdminPermissions =
        new RolePermissionType(
            NodeIds.WellKnownRole_SecurityAdmin,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.Write,
                PermissionType.Field.ReadRolePermissions));
    var equivalentRolePermissions =
        new RolePermissionType(
            equivalentRole,
            PermissionType.of(PermissionType.Field.Browse, PermissionType.Field.Read));
    var rolePermissions =
        new RolePermissionType[] {securityAdminPermissions, equivalentRolePermissions};
    var filterChain =
        new AttributeFilterChain(
            List.of(
                Util.roleBasedUserAccessLevelFilter(), Util.roleBasedUserRolePermissionsFilter()));

    when(node.getAttribute(AttributeId.UserAccessLevel)).thenReturn(readOnly);
    when(node.getAttribute(AttributeId.RolePermissions)).thenReturn(rolePermissions);

    assertEquals(readOnly, filterChain.readAttribute(null, node, AttributeId.UserAccessLevel));

    when(session.getRoleIds()).thenReturn(Optional.empty());
    assertEquals(noAccess, filterChain.readAttribute(session, node, AttributeId.UserAccessLevel));
    assertArrayEquals(
        new RolePermissionType[0],
        (RolePermissionType[])
            filterChain.readAttribute(session, node, AttributeId.UserRolePermissions));

    when(session.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_AuthenticatedUser)));
    assertEquals(noAccess, filterChain.readAttribute(session, node, AttributeId.UserAccessLevel));

    when(session.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_SecurityAdmin)));
    assertEquals(readOnly, filterChain.readAttribute(session, node, AttributeId.UserAccessLevel));
    assertArrayEquals(
        new RolePermissionType[] {securityAdminPermissions},
        (RolePermissionType[])
            filterChain.readAttribute(session, node, AttributeId.UserRolePermissions));

    when(session.getRoleIds()).thenReturn(Optional.of(List.of(equivalentRole)));
    assertEquals(readOnly, filterChain.readAttribute(session, node, AttributeId.UserAccessLevel));
    assertArrayEquals(
        new RolePermissionType[] {equivalentRolePermissions},
        (RolePermissionType[])
            filterChain.readAttribute(session, node, AttributeId.UserRolePermissions));
  }

  @Test
  void roleBasedUserAccessLevelFilterUsesEnabledFlagRolePermissions() throws UaException {
    var node = mock(UaVariableNode.class);
    var session = mock(Session.class);
    var readWrite = AccessLevel.toValue(AccessLevel.READ_WRITE);
    var readOnly = AccessLevel.toValue(AccessLevel.READ_ONLY);
    var rolePermissions =
        new RolePermissionType[] {
          new RolePermissionType(
              NodeIds.WellKnownRole_Anonymous,
              PermissionType.of(PermissionType.Field.Browse, PermissionType.Field.Read)),
          new RolePermissionType(
              NodeIds.WellKnownRole_ConfigureAdmin,
              PermissionType.of(
                  PermissionType.Field.Browse,
                  PermissionType.Field.Read,
                  PermissionType.Field.Write)),
          new RolePermissionType(
              NodeIds.WellKnownRole_SecurityAdmin,
              PermissionType.of(
                  PermissionType.Field.Browse,
                  PermissionType.Field.Read,
                  PermissionType.Field.Write))
        };
    var filterChain = new AttributeFilterChain(Util.roleBasedUserAccessLevelFilter());

    when(node.getAttribute(AttributeId.UserAccessLevel)).thenReturn(readWrite);
    when(node.getAttribute(AttributeId.RolePermissions)).thenReturn(rolePermissions);
    when(session.getRoleIds()).thenReturn(Optional.empty());

    assertEquals(readOnly, filterChain.readAttribute(session, node, AttributeId.UserAccessLevel));

    when(session.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_ConfigureAdmin)));
    assertEquals(readWrite, filterChain.readAttribute(session, node, AttributeId.UserAccessLevel));

    when(session.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_SecurityAdmin)));
    assertEquals(readWrite, filterChain.readAttribute(session, node, AttributeId.UserAccessLevel));
  }
}
