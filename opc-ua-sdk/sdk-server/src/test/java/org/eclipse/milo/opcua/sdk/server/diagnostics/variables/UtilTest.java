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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.access.FallbackUserRolePermissionsFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterChain;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterContext;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
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

  // OPC 10000-3 §5.2.9 & §5.2.10: when RolePermissions is absent on a node, UserRolePermissions
  // must also be null so AttributeReader reports Bad_AttributeIdInvalid for both attributes.
  @Test
  void roleBasedUserRolePermissionsFilterReturnsNullWhenRolePermissionsAbsent() throws UaException {
    var node = mock(UaVariableNode.class);
    var session = mock(Session.class);
    var filterChain = new AttributeFilterChain(Util.roleBasedUserRolePermissionsFilter());

    when(node.getAttribute(AttributeId.RolePermissions)).thenReturn(null);
    when(node.getAttribute(AttributeId.UserRolePermissions)).thenReturn(null);

    assertNull(filterChain.readAttribute(null, node, AttributeId.UserRolePermissions));

    when(session.getRoleIds()).thenReturn(Optional.empty());
    assertNull(filterChain.readAttribute(session, node, AttributeId.UserRolePermissions));

    when(session.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_SecurityAdmin)));
    assertNull(filterChain.readAttribute(session, node, AttributeId.UserRolePermissions));
  }

  // Part 5 §6.3.5: even when namespace-0 nodes have no RolePermissions attribute, restricted
  // security diagnostics remain fail-closed unless the Session holds SecurityAdmin.
  @Test
  void roleBasedUserAccessLevelFilterFallsBackToSecurityDiagnosticsDefaultPolicy()
      throws UaException {
    var node = mock(UaVariableNode.class);
    var session = mock(Session.class);
    var readOnly = AccessLevel.toValue(AccessLevel.READ_ONLY);
    var noAccess = AccessLevel.toValue(AccessLevel.NONE);
    var filterChain =
        new AttributeFilterChain(
            Util.roleBasedUserAccessLevelFilter(
                Util.DEFAULT_SESSION_SECURITY_DIAGNOSTICS_ROLE_PERMISSIONS));

    when(node.getAttribute(AttributeId.UserAccessLevel)).thenReturn(readOnly);
    when(node.getAttribute(AttributeId.RolePermissions)).thenReturn(null);

    assertEquals(readOnly, filterChain.readAttribute(null, node, AttributeId.UserAccessLevel));

    when(session.getRoleIds()).thenReturn(Optional.empty());
    assertEquals(noAccess, filterChain.readAttribute(session, node, AttributeId.UserAccessLevel));

    when(session.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_AuthenticatedUser)));
    assertEquals(noAccess, filterChain.readAttribute(session, node, AttributeId.UserAccessLevel));

    when(session.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_ConfigureAdmin)));
    assertEquals(noAccess, filterChain.readAttribute(session, node, AttributeId.UserAccessLevel));

    when(session.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_SecurityAdmin)));
    assertEquals(readOnly, filterChain.readAttribute(session, node, AttributeId.UserAccessLevel));
  }

  // Without RolePermissions on EnabledFlag, the fallback policy must still allow reading broadly
  // while reserving writes for ConfigureAdmin and SecurityAdmin.
  @Test
  void roleBasedUserAccessLevelFilterFallsBackToEnabledFlagDefaultPolicy() throws UaException {
    var node = mock(UaVariableNode.class);
    var session = mock(Session.class);
    var readWrite = AccessLevel.toValue(AccessLevel.READ_WRITE);
    var readOnly = AccessLevel.toValue(AccessLevel.READ_ONLY);
    var filterChain =
        new AttributeFilterChain(
            Util.roleBasedUserAccessLevelFilter(Util.DEFAULT_ENABLED_FLAG_ROLE_PERMISSIONS));

    when(node.getAttribute(AttributeId.UserAccessLevel)).thenReturn(readWrite);
    when(node.getAttribute(AttributeId.RolePermissions)).thenReturn(null);

    assertEquals(readWrite, filterChain.readAttribute(null, node, AttributeId.UserAccessLevel));

    when(session.getRoleIds()).thenReturn(Optional.empty());
    assertEquals(readOnly, filterChain.readAttribute(session, node, AttributeId.UserAccessLevel));

    when(session.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_AuthenticatedUser)));
    assertEquals(readOnly, filterChain.readAttribute(session, node, AttributeId.UserAccessLevel));

    when(session.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_ConfigureAdmin)));
    assertEquals(readWrite, filterChain.readAttribute(session, node, AttributeId.UserAccessLevel));

    when(session.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_SecurityAdmin)));
    assertEquals(readWrite, filterChain.readAttribute(session, node, AttributeId.UserAccessLevel));
  }

  // An explicit RolePermissions override (including an empty array) replaces the default fallback
  // policy rather than merging with it.
  @Test
  void explicitEmptyRolePermissionsOverridesDefaultPolicyAndFailsClosed() throws UaException {
    var node = mock(UaVariableNode.class);
    var session = mock(Session.class);
    var readWrite = AccessLevel.toValue(AccessLevel.READ_WRITE);
    var noAccess = AccessLevel.toValue(AccessLevel.NONE);
    var filterChain =
        new AttributeFilterChain(
            List.of(
                Util.roleBasedUserAccessLevelFilter(Util.DEFAULT_ENABLED_FLAG_ROLE_PERMISSIONS),
                Util.roleBasedUserRolePermissionsFilter()));

    when(node.getAttribute(AttributeId.UserAccessLevel)).thenReturn(readWrite);
    when(node.getAttribute(AttributeId.RolePermissions)).thenReturn(new RolePermissionType[0]);
    when(session.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_SecurityAdmin)));

    assertEquals(noAccess, filterChain.readAttribute(session, node, AttributeId.UserAccessLevel));
    assertArrayEquals(
        new RolePermissionType[0],
        (RolePermissionType[])
            filterChain.readAttribute(session, node, AttributeId.UserRolePermissions));
  }

  // Malformed RolePermissions must fail closed rather than falling back to the default policy.
  @Test
  void malformedRolePermissionsFailsClosedWithoutFallingBackToDefault() throws UaException {
    var node = mock(UaVariableNode.class);
    var session = mock(Session.class);
    var readWrite = AccessLevel.toValue(AccessLevel.READ_WRITE);
    var noAccess = AccessLevel.toValue(AccessLevel.NONE);
    var filterChain =
        new AttributeFilterChain(
            List.of(
                Util.roleBasedUserAccessLevelFilter(Util.DEFAULT_ENABLED_FLAG_ROLE_PERMISSIONS),
                Util.roleBasedUserRolePermissionsFilter()));

    when(node.getAttribute(AttributeId.UserAccessLevel)).thenReturn(readWrite);
    when(node.getAttribute(AttributeId.RolePermissions)).thenReturn("invalid");
    when(session.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_SecurityAdmin)));

    assertEquals(noAccess, filterChain.readAttribute(session, node, AttributeId.UserAccessLevel));
    assertArrayEquals(
        new RolePermissionType[0],
        (RolePermissionType[])
            filterChain.readAttribute(session, node, AttributeId.UserRolePermissions));
  }

  // Malformed RolePermissionType entries inside an otherwise valid array (e.g. null permissions,
  // null permission mask value, or null roleId) must not escape through UserRolePermissions where
  // DefaultAccessController or binary encoding would dereference them.
  @Test
  void malformedEntriesInsideExplicitRolePermissionsArrayAreRejected() throws UaException {
    var node = mock(UaVariableNode.class);
    var session = mock(Session.class);
    var customRole = new NodeId(1, "CustomRole");
    var readOnly = AccessLevel.toValue(AccessLevel.READ_ONLY);
    var noAccess = AccessLevel.toValue(AccessLevel.NONE);
    var filterChain =
        new AttributeFilterChain(
            List.of(
                Util.roleBasedUserAccessLevelFilter(
                    Util.DEFAULT_SESSION_SECURITY_DIAGNOSTICS_ROLE_PERMISSIONS),
                Util.roleBasedUserRolePermissionsFilter(
                    Util.DEFAULT_SESSION_SECURITY_DIAGNOSTICS_ROLE_PERMISSIONS)));

    var onlyMalformed =
        new RolePermissionType[] {
          null,
          new RolePermissionType(customRole, null),
          new RolePermissionType(null, PermissionType.of(PermissionType.Field.Read)),
          new RolePermissionType(customRole, new PermissionType(null))
        };

    when(node.getAttribute(AttributeId.UserAccessLevel)).thenReturn(readOnly);
    when(node.getAttribute(AttributeId.RolePermissions)).thenReturn(onlyMalformed);
    when(session.getRoleIds()).thenReturn(Optional.of(List.of(customRole)));

    assertEquals(noAccess, filterChain.readAttribute(session, node, AttributeId.UserAccessLevel));
    assertArrayEquals(
        new RolePermissionType[0],
        (RolePermissionType[])
            filterChain.readAttribute(session, node, AttributeId.UserRolePermissions));

    var validEntry =
        new RolePermissionType(
            customRole, PermissionType.of(PermissionType.Field.Browse, PermissionType.Field.Read));
    var mixed =
        new RolePermissionType[] {new RolePermissionType(customRole, null), validEntry, null};
    when(node.getAttribute(AttributeId.RolePermissions)).thenReturn(mixed);

    assertEquals(readOnly, filterChain.readAttribute(session, node, AttributeId.UserAccessLevel));
    assertArrayEquals(
        new RolePermissionType[] {validEntry},
        (RolePermissionType[])
            filterChain.readAttribute(session, node, AttributeId.UserRolePermissions));
  }

  // Restricted security diagnostics keep UserRolePermissions publicly null when RolePermissions is
  // absent, while supplying the fallback policy internally to DefaultAccessController for Browse.
  @Test
  void roleBasedUserRolePermissionsFilterProvidesInternalFallbackWhenRolePermissionsAbsent()
      throws UaException {
    var node = mock(UaVariableNode.class);
    var nonAdmin = mock(Session.class);
    var securityAdmin = mock(Session.class);
    AttributeFilter filter =
        Util.roleBasedUserRolePermissionsFilter(
            Util.DEFAULT_SESSION_SECURITY_DIAGNOSTICS_ROLE_PERMISSIONS);
    var fallbackFilter = assertInstanceOf(FallbackUserRolePermissionsFilter.class, filter);
    var filterChain = new AttributeFilterChain(filter);

    when(node.getAttribute(AttributeId.RolePermissions)).thenReturn(null);
    when(node.getAttribute(AttributeId.UserRolePermissions)).thenReturn(null);
    when(nonAdmin.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_AuthenticatedUser)));
    when(securityAdmin.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_SecurityAdmin)));

    // Public UserRolePermissions stays null (Bad_AttributeIdInvalid) for every Session.
    assertNull(filterChain.readAttribute(nonAdmin, node, AttributeId.UserRolePermissions));
    assertNull(filterChain.readAttribute(securityAdmin, node, AttributeId.UserRolePermissions));

    // Internal fallback denies non-admin Sessions and grants SecurityAdmin.
    assertArrayEquals(
        new RolePermissionType[0], fallbackFilter.getFallbackUserRolePermissions(nonAdmin, node));
    assertArrayEquals(
        Util.DEFAULT_SESSION_SECURITY_DIAGNOSTICS_ROLE_PERMISSIONS.toArray(
            RolePermissionType[]::new),
        fallbackFilter.getFallbackUserRolePermissions(securityAdmin, node));

    // Once explicit RolePermissions are set, fallback returns the sanitized session-filtered
    // explicit permissions rather than null so authorization never weakens silently.
    when(node.getAttribute(AttributeId.RolePermissions)).thenReturn(new RolePermissionType[0]);
    assertArrayEquals(
        new RolePermissionType[0], fallbackFilter.getFallbackUserRolePermissions(nonAdmin, node));
    assertArrayEquals(
        new RolePermissionType[0],
        fallbackFilter.getFallbackUserRolePermissions(securityAdmin, node));

    var customRole = new NodeId(1, "CustomFallbackRole");
    var customPermission =
        new RolePermissionType(
            customRole, PermissionType.of(PermissionType.Field.Browse, PermissionType.Field.Read));
    var customSession = mock(Session.class);
    when(customSession.getRoleIds()).thenReturn(Optional.of(List.of(customRole)));
    when(node.getAttribute(AttributeId.RolePermissions))
        .thenReturn(new RolePermissionType[] {customPermission, null});

    assertArrayEquals(
        new RolePermissionType[0], fallbackFilter.getFallbackUserRolePermissions(nonAdmin, node));
    assertArrayEquals(
        new RolePermissionType[] {customPermission},
        fallbackFilter.getFallbackUserRolePermissions(customSession, node));
  }

  // An application AttributeFilter added at the front of the filter chain supplying RolePermissions
  // while the stored attribute stays null must govern UserAccessLevel, public UserRolePermissions,
  // and internal fallback permissions consistently.
  @Test
  void frontOfChainRolePermissionsFilterGovernsAccessLevelUserRolePermissionsAndFallback()
      throws UaException {
    var node = mock(UaVariableNode.class);
    var customRole = new NodeId(1, "FrontFilterRole");
    var customPermission =
        new RolePermissionType(
            customRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));
    var frontFilter =
        new AttributeFilter() {
          @Override
          public Object getAttribute(AttributeFilterContext ctx, AttributeId attributeId) {
            if (attributeId == AttributeId.RolePermissions) {
              return new RolePermissionType[] {customPermission};
            } else {
              return ctx.getAttribute(attributeId);
            }
          }
        };

    AttributeFilter rolePermissionsFilter =
        Util.roleBasedUserRolePermissionsFilter(
            Util.DEFAULT_SESSION_SECURITY_DIAGNOSTICS_ROLE_PERMISSIONS);
    var fallbackFilter =
        assertInstanceOf(FallbackUserRolePermissionsFilter.class, rolePermissionsFilter);
    var filterChain =
        new AttributeFilterChain(
            List.of(
                frontFilter,
                Util.roleBasedUserAccessLevelFilter(
                    Util.DEFAULT_SESSION_SECURITY_DIAGNOSTICS_ROLE_PERMISSIONS),
                rolePermissionsFilter));

    when(node.getFilterChain()).thenReturn(filterChain);
    when(node.getAttribute(AttributeId.UserAccessLevel))
        .thenReturn(AccessLevel.toValue(AccessLevel.READ_ONLY));
    when(node.getAttribute(AttributeId.RolePermissions)).thenReturn(null);

    var securityAdmin = mock(Session.class);
    when(securityAdmin.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_SecurityAdmin)));
    var customSession = mock(Session.class);
    when(customSession.getRoleIds()).thenReturn(Optional.of(List.of(customRole)));

    // SecurityAdmin is denied by the front-of-chain override across all three views.
    assertEquals(
        AccessLevel.toValue(AccessLevel.NONE),
        filterChain.readAttribute(securityAdmin, node, AttributeId.UserAccessLevel));
    assertArrayEquals(
        new RolePermissionType[0],
        (RolePermissionType[])
            filterChain.readAttribute(securityAdmin, node, AttributeId.UserRolePermissions));
    assertArrayEquals(
        new RolePermissionType[0],
        fallbackFilter.getFallbackUserRolePermissions(securityAdmin, node));

    // The custom role granted by the front-of-chain override is allowed across all three views.
    assertEquals(
        AccessLevel.toValue(AccessLevel.READ_ONLY),
        filterChain.readAttribute(customSession, node, AttributeId.UserAccessLevel));
    assertArrayEquals(
        new RolePermissionType[] {customPermission},
        (RolePermissionType[])
            filterChain.readAttribute(customSession, node, AttributeId.UserRolePermissions));
    assertArrayEquals(
        new RolePermissionType[] {customPermission},
        fallbackFilter.getFallbackUserRolePermissions(customSession, node));
  }

  // Util's inner RolePermissions filter sanitizes malformed entries without throwing
  // Bad_UserAccessDenied prematurely, so an outer delegating front filter can inspect and append a
  // ReadRolePermissions grant to an existing non-null policy before UaNode / AttributeReader
  // authorizes the final result.
  @Test
  void rolePermissionsFilterSanitizesMalformedEntriesAndAllowsOuterDelegatingFilterToAppendGrant()
      throws UaException {
    var node = mock(UaVariableNode.class);
    var unmappedSession = mock(Session.class);
    when(unmappedSession.getRoleIds()).thenReturn(Optional.empty());

    var filterChain =
        new AttributeFilterChain(
            List.of(
                Util.roleBasedUserAccessLevelFilter(Util.DEFAULT_ENABLED_FLAG_ROLE_PERMISSIONS),
                Util.roleBasedUserRolePermissionsFilter(
                    Util.DEFAULT_ENABLED_FLAG_ROLE_PERMISSIONS)));
    when(node.getFilterChain()).thenReturn(filterChain);

    // Absent RolePermissions still returns null (Bad_AttributeIdInvalid).
    when(node.getAttribute(AttributeId.RolePermissions)).thenReturn(null);
    assertNull(filterChain.readAttribute(unmappedSession, node, AttributeId.RolePermissions));

    // Malformed entries are stripped for Session-scoped reads while internal reads stay untouched.
    var customRole = new NodeId(1, "PolicyAdmin");
    var adminPermission =
        new RolePermissionType(
            customRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));
    var anonymousReadOnly =
        new RolePermissionType(
            NodeIds.WellKnownRole_Anonymous,
            PermissionType.of(PermissionType.Field.Browse, PermissionType.Field.Read));
    var mixedPolicy =
        new RolePermissionType[] {
          anonymousReadOnly,
          adminPermission,
          null,
          new RolePermissionType(customRole, null),
          new RolePermissionType(null, PermissionType.of(PermissionType.Field.Read)),
          new RolePermissionType(customRole, new PermissionType(null))
        };
    when(node.getAttribute(AttributeId.RolePermissions)).thenReturn(mixedPolicy);

    assertArrayEquals(
        new RolePermissionType[] {anonymousReadOnly, adminPermission},
        (RolePermissionType[])
            filterChain.readAttribute(unmappedSession, node, AttributeId.RolePermissions));
    assertArrayEquals(
        mixedPolicy,
        (RolePermissionType[]) filterChain.readAttribute(null, node, AttributeId.RolePermissions));

    // An outer addFirst filter delegating to ctx.readAttribute(RolePermissions) can append a
    // ReadRolePermissions grant to the existing non-null policy without being aborted prematurely.
    var appendedGrant =
        new RolePermissionType(
            NodeIds.WellKnownRole_Anonymous,
            PermissionType.of(PermissionType.Field.ReadRolePermissions));
    filterChain.addFirst(
        new AttributeFilter() {
          @Override
          public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
              throws UaException {
            if (attributeId == AttributeId.RolePermissions) {
              var base = (RolePermissionType[]) ctx.readAttribute(AttributeId.RolePermissions);
              RolePermissionType[] augmented = Arrays.copyOf(base, base.length + 1);
              augmented[base.length] = appendedGrant;
              return augmented;
            } else {
              return ctx.readAttribute(attributeId);
            }
          }
        });

    assertArrayEquals(
        new RolePermissionType[] {anonymousReadOnly, adminPermission, appendedGrant},
        (RolePermissionType[])
            filterChain.readAttribute(unmappedSession, node, AttributeId.RolePermissions));
    assertArrayEquals(
        new RolePermissionType[] {anonymousReadOnly, appendedGrant},
        (RolePermissionType[])
            filterChain.readAttribute(unmappedSession, node, AttributeId.UserRolePermissions));
  }

  // A front-of-chain AttributeFilter that supplies RolePermissions only via readAttribute (leaving
  // getAttribute and the stored attribute null) must still govern UserAccessLevel, public
  // UserRolePermissions, and internal fallback permissions.
  @Test
  void readAttributeOnlyFrontFilterGovernsAccessLevelUserRolePermissionsAndFallback()
      throws UaException {
    var node = mock(UaVariableNode.class);
    var customRole = new NodeId(1, "ReadAttrFrontRole");
    var customPermission =
        new RolePermissionType(
            customRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));
    var readOnlyFrontFilter =
        new AttributeFilter() {
          @Override
          public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
              throws UaException {
            if (attributeId == AttributeId.RolePermissions) {
              return new RolePermissionType[] {customPermission};
            } else {
              return ctx.readAttribute(attributeId);
            }
          }
        };

    AttributeFilter rolePermissionsFilter =
        Util.roleBasedUserRolePermissionsFilter(
            Util.DEFAULT_SESSION_SECURITY_DIAGNOSTICS_ROLE_PERMISSIONS);
    var fallbackFilter =
        assertInstanceOf(FallbackUserRolePermissionsFilter.class, rolePermissionsFilter);
    var filterChain =
        new AttributeFilterChain(
            List.of(
                readOnlyFrontFilter,
                Util.roleBasedUserAccessLevelFilter(
                    Util.DEFAULT_SESSION_SECURITY_DIAGNOSTICS_ROLE_PERMISSIONS),
                rolePermissionsFilter));

    when(node.getFilterChain()).thenReturn(filterChain);
    when(node.getAttribute(AttributeId.UserAccessLevel))
        .thenReturn(AccessLevel.toValue(AccessLevel.READ_ONLY));
    when(node.getAttribute(AttributeId.RolePermissions)).thenReturn(null);

    var unmapped = mock(Session.class);
    when(unmapped.getRoleIds()).thenReturn(Optional.empty());
    var securityAdmin = mock(Session.class);
    when(securityAdmin.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_SecurityAdmin)));
    var customSession = mock(Session.class);
    when(customSession.getRoleIds()).thenReturn(Optional.of(List.of(customRole)));

    for (Session denied : List.of(unmapped, securityAdmin)) {
      assertEquals(
          AccessLevel.toValue(AccessLevel.NONE),
          filterChain.readAttribute(denied, node, AttributeId.UserAccessLevel));
      assertEquals(
          AccessLevel.toValue(AccessLevel.NONE),
          filterChain.getAttribute(denied, node, AttributeId.UserAccessLevel));
      assertArrayEquals(
          new RolePermissionType[0],
          (RolePermissionType[])
              filterChain.readAttribute(denied, node, AttributeId.UserRolePermissions));
      assertArrayEquals(
          new RolePermissionType[0],
          (RolePermissionType[])
              filterChain.getAttribute(denied, node, AttributeId.UserRolePermissions));
      assertArrayEquals(
          new RolePermissionType[0], fallbackFilter.getFallbackUserRolePermissions(denied, node));
    }

    assertEquals(
        AccessLevel.toValue(AccessLevel.READ_ONLY),
        filterChain.readAttribute(customSession, node, AttributeId.UserAccessLevel));
    assertArrayEquals(
        new RolePermissionType[] {customPermission},
        (RolePermissionType[])
            filterChain.readAttribute(customSession, node, AttributeId.UserRolePermissions));
    assertArrayEquals(
        new RolePermissionType[] {customPermission},
        fallbackFilter.getFallbackUserRolePermissions(customSession, node));
  }

  // Recursion detection in readNodeRolePermissions must be scoped to (node, session) so a policy
  // filter on node A that inspects node B's UserRolePermissions still runs node B's front filter,
  // while a self-cycle on the same (node, session) fails closed without StackOverflowError.
  @Test
  void recursionGuardIsScopedPerNodeAndSessionAndFailsClosedOnSelfCycle() throws UaException {
    var roleB = new NodeId(1, "NodeBRole");
    var roleBPermission =
        new RolePermissionType(
            roleB,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));

    var nodeB = mock(UaVariableNode.class);
    var frontFilterB =
        new AttributeFilter() {
          @Override
          public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
              throws UaException {
            if (attributeId == AttributeId.RolePermissions) {
              return new RolePermissionType[] {roleBPermission};
            } else {
              return ctx.readAttribute(attributeId);
            }
          }
        };
    var chainB =
        new AttributeFilterChain(
            List.of(
                frontFilterB,
                Util.roleBasedUserAccessLevelFilter(
                    Util.DEFAULT_SESSION_SECURITY_DIAGNOSTICS_ROLE_PERMISSIONS),
                Util.roleBasedUserRolePermissionsFilter(
                    Util.DEFAULT_SESSION_SECURITY_DIAGNOSTICS_ROLE_PERMISSIONS)));
    when(nodeB.getFilterChain()).thenReturn(chainB);
    when(nodeB.getAttribute(AttributeId.UserAccessLevel))
        .thenReturn(AccessLevel.toValue(AccessLevel.READ_ONLY));
    when(nodeB.getAttribute(AttributeId.RolePermissions)).thenReturn(null);

    // Node A's front filter derives its RolePermissions by reading Node B's UserRolePermissions.
    var nodeA = mock(UaVariableNode.class);
    var frontFilterA =
        new AttributeFilter() {
          @Override
          public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
              throws UaException {
            if (attributeId == AttributeId.RolePermissions) {
              return chainB.readAttribute(
                  ctx.getSession().orElse(null), nodeB, AttributeId.UserRolePermissions);
            } else {
              return ctx.readAttribute(attributeId);
            }
          }
        };
    AttributeFilter rpFilterA =
        Util.roleBasedUserRolePermissionsFilter(
            Util.DEFAULT_SESSION_SECURITY_DIAGNOSTICS_ROLE_PERMISSIONS);
    var fallbackFilterA = assertInstanceOf(FallbackUserRolePermissionsFilter.class, rpFilterA);
    var chainA =
        new AttributeFilterChain(
            List.of(
                frontFilterA,
                Util.roleBasedUserAccessLevelFilter(
                    Util.DEFAULT_SESSION_SECURITY_DIAGNOSTICS_ROLE_PERMISSIONS),
                rpFilterA));
    when(nodeA.getFilterChain()).thenReturn(chainA);
    when(nodeA.getAttribute(AttributeId.UserAccessLevel))
        .thenReturn(AccessLevel.toValue(AccessLevel.READ_ONLY));
    when(nodeA.getAttribute(AttributeId.RolePermissions)).thenReturn(null);

    var sessionWithRoleB = mock(Session.class);
    when(sessionWithRoleB.getRoleIds()).thenReturn(Optional.of(List.of(roleB)));
    var otherSession = mock(Session.class);
    when(otherSession.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_SecurityAdmin)));

    // Node A sees Node B's front-filter policy without hitting the cycle guard.
    assertEquals(
        AccessLevel.toValue(AccessLevel.READ_ONLY),
        chainA.readAttribute(sessionWithRoleB, nodeA, AttributeId.UserAccessLevel));
    assertArrayEquals(
        new RolePermissionType[] {roleBPermission},
        (RolePermissionType[])
            chainA.readAttribute(sessionWithRoleB, nodeA, AttributeId.UserRolePermissions));
    assertArrayEquals(
        new RolePermissionType[] {roleBPermission},
        fallbackFilterA.getFallbackUserRolePermissions(sessionWithRoleB, nodeA));

    assertEquals(
        AccessLevel.toValue(AccessLevel.NONE),
        chainA.readAttribute(otherSession, nodeA, AttributeId.UserAccessLevel));
    assertArrayEquals(
        new RolePermissionType[0],
        (RolePermissionType[])
            chainA.readAttribute(otherSession, nodeA, AttributeId.UserRolePermissions));
    assertArrayEquals(
        new RolePermissionType[0],
        fallbackFilterA.getFallbackUserRolePermissions(otherSession, nodeA));

    // A self-cycle on node C (its RolePermissions filter queries node C's own UserRolePermissions)
    // must terminate and fail closed cleanly.
    var nodeC = mock(UaVariableNode.class);
    var chainC = new AttributeFilterChain();
    var cyclicFilterC =
        new AttributeFilter() {
          @Override
          public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
              throws UaException {
            if (attributeId == AttributeId.RolePermissions) {
              return chainC.readAttribute(
                  ctx.getSession().orElse(null), nodeC, AttributeId.UserRolePermissions);
            } else {
              return ctx.readAttribute(attributeId);
            }
          }
        };
    AttributeFilter rpFilterC =
        Util.roleBasedUserRolePermissionsFilter(
            Util.DEFAULT_SESSION_SECURITY_DIAGNOSTICS_ROLE_PERMISSIONS);
    var fallbackFilterC = assertInstanceOf(FallbackUserRolePermissionsFilter.class, rpFilterC);
    chainC.addLast(
        cyclicFilterC,
        Util.roleBasedUserAccessLevelFilter(
            Util.DEFAULT_SESSION_SECURITY_DIAGNOSTICS_ROLE_PERMISSIONS),
        rpFilterC);
    when(nodeC.getFilterChain()).thenReturn(chainC);
    when(nodeC.getAttribute(AttributeId.UserAccessLevel))
        .thenReturn(AccessLevel.toValue(AccessLevel.READ_ONLY));
    when(nodeC.getAttribute(AttributeId.RolePermissions)).thenReturn(null);

    assertEquals(
        AccessLevel.toValue(AccessLevel.NONE),
        chainC.readAttribute(otherSession, nodeC, AttributeId.UserAccessLevel));
    assertArrayEquals(
        new RolePermissionType[0],
        (RolePermissionType[])
            chainC.readAttribute(otherSession, nodeC, AttributeId.UserRolePermissions));
    assertArrayEquals(
        new RolePermissionType[0],
        fallbackFilterC.getFallbackUserRolePermissions(otherSession, nodeC));
  }

  // Util's roleBasedUserRolePermissionsFilter must honor explicit stored UserRolePermissions (such
  // as an empty RolePermissionType[0] array) and downstream UserRolePermissions filters before
  // deriving from RolePermissions or falling back, and both Util filters must forward
  // writeAttribute() to downstream filters rather than bypassing them via setAttribute().
  @Test
  void diagnosticsFiltersHonorExplicitUserRolePermissionsAndForwardWriteAttribute()
      throws UaException {
    var node = mock(UaVariableNode.class);
    var session = mock(Session.class);
    when(session.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_SecurityAdmin)));

    var downstreamWriteCalled = new AtomicBoolean(false);
    var downstreamReject = new AtomicBoolean(true);
    var downstreamFilter =
        new AttributeFilter() {
          @Override
          public void writeAttribute(
              AttributeFilterContext ctx, AttributeId attributeId, Object value)
              throws UaException {
            downstreamWriteCalled.set(true);
            if (downstreamReject.get()) {
              throw new UaException(StatusCodes.Bad_OutOfRange);
            }
            ctx.writeAttribute(attributeId, value);
          }
        };

    var filterChain =
        new AttributeFilterChain(
            List.of(
                Util.roleBasedUserAccessLevelFilter(Util.DEFAULT_ENABLED_FLAG_ROLE_PERMISSIONS),
                Util.roleBasedUserRolePermissionsFilter(Util.DEFAULT_ENABLED_FLAG_ROLE_PERMISSIONS),
                Util.diagnosticValueFilter(
                    new AtomicBoolean(true), ctx -> DataValue.valueOnly(Variant.ofBoolean(true))),
                downstreamFilter));
    when(node.getFilterChain()).thenReturn(filterChain);
    when(node.getAttribute(AttributeId.RolePermissions)).thenReturn(null);
    when(node.getAttribute(AttributeId.UserAccessLevel))
        .thenReturn(AccessLevel.toValue(AccessLevel.READ_WRITE));

    // Explicit stored empty UserRolePermissions is returned instead of null/fallback and removes
    // read/write bits from UserAccessLevel even for SecurityAdmin.
    when(node.getAttribute(AttributeId.UserRolePermissions)).thenReturn(new RolePermissionType[0]);
    assertArrayEquals(
        new RolePermissionType[0],
        (RolePermissionType[])
            filterChain.readAttribute(session, node, AttributeId.UserRolePermissions));
    assertArrayEquals(
        new RolePermissionType[0],
        (RolePermissionType[])
            filterChain.getAttribute(session, node, AttributeId.UserRolePermissions));
    assertEquals(
        AccessLevel.toValue(AccessLevel.NONE),
        filterChain.readAttribute(session, node, AttributeId.UserAccessLevel));

    // Once cleared back to null, UserRolePermissions returns null again (omitted) and
    // UserAccessLevel restores SecurityAdmin's default READ_WRITE access.
    when(node.getAttribute(AttributeId.UserRolePermissions)).thenReturn(null);
    assertNull(filterChain.readAttribute(session, node, AttributeId.UserRolePermissions));
    assertEquals(
        AccessLevel.toValue(AccessLevel.READ_WRITE),
        filterChain.readAttribute(session, node, AttributeId.UserAccessLevel));

    // writeAttribute forwards through all three Util filters (including diagnosticValueFilter) to
    // the downstream filter.
    UaException ex =
        assertThrows(
            UaException.class,
            () -> filterChain.writeAttribute(session, node, AttributeId.Value, Boolean.TRUE));
    assertEquals(StatusCodes.Bad_OutOfRange, ex.getStatusCode().getValue());
    assertTrue(downstreamWriteCalled.get());
  }

  // Dynamic security diagnostics elements and child fields inheriting from
  // SessionSecurityDiagnosticsArray must also inherit explicit UserRolePermissions restrictions
  // placed only on the source array (even when RolePermissions is null on both nodes), while
  // still letting deliberate per-child overrides take precedence and restoring default access once
  // the source array's explicit UserRolePermissions override is cleared.
  @Test
  void inheritedDiagnosticsNodesHonorSourceArrayExplicitUserRolePermissions() throws UaException {
    var arrayNode = mock(UaVariableNode.class);
    var childNode = mock(UaVariableNode.class);
    var adminSession = mock(Session.class);
    when(adminSession.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_SecurityAdmin)));

    var arrayChain =
        new AttributeFilterChain(
            List.of(
                Util.roleBasedUserAccessLevelFilter(
                    Util.DEFAULT_SESSION_SECURITY_DIAGNOSTICS_ROLE_PERMISSIONS),
                Util.roleBasedUserRolePermissionsFilter(
                    Util.DEFAULT_SESSION_SECURITY_DIAGNOSTICS_ROLE_PERMISSIONS)));
    when(arrayNode.getFilterChain()).thenReturn(arrayChain);
    when(arrayNode.getAttribute(AttributeId.RolePermissions)).thenReturn(null);
    when(arrayNode.getAttribute(AttributeId.UserAccessLevel))
        .thenReturn(AccessLevel.toValue(AccessLevel.READ_ONLY));

    AttributeFilter childRpFilter =
        Util.roleBasedUserRolePermissionsFilter(
            arrayNode, Util.DEFAULT_SESSION_SECURITY_DIAGNOSTICS_ROLE_PERMISSIONS);
    var childFallback = assertInstanceOf(FallbackUserRolePermissionsFilter.class, childRpFilter);
    var childChain =
        new AttributeFilterChain(
            List.of(
                Util.roleBasedUserAccessLevelFilter(
                    arrayNode, Util.DEFAULT_SESSION_SECURITY_DIAGNOSTICS_ROLE_PERMISSIONS),
                childRpFilter));
    when(childNode.getFilterChain()).thenReturn(childChain);
    when(childNode.getAttribute(AttributeId.RolePermissions)).thenReturn(null);
    when(childNode.getAttribute(AttributeId.UserRolePermissions)).thenReturn(null);
    when(childNode.getAttribute(AttributeId.UserAccessLevel))
        .thenReturn(AccessLevel.toValue(AccessLevel.READ_ONLY));

    // Baseline: no explicit UserRolePermissions on array -> SecurityAdmin gets default READ_ONLY
    // and UserRolePermissions stays omitted (null).
    when(arrayNode.getAttribute(AttributeId.UserRolePermissions)).thenReturn(null);
    assertEquals(
        AccessLevel.toValue(AccessLevel.READ_ONLY),
        childChain.readAttribute(adminSession, childNode, AttributeId.UserAccessLevel));
    assertNull(childChain.readAttribute(adminSession, childNode, AttributeId.UserRolePermissions));

    // Restrict ONLY the source array with explicit empty UserRolePermissions: child element/field
    // must inherit [] for UserRolePermissions, fallback, and drop CurrentRead from UserAccessLevel.
    when(arrayNode.getAttribute(AttributeId.UserRolePermissions))
        .thenReturn(new RolePermissionType[0]);
    assertEquals(
        AccessLevel.toValue(AccessLevel.NONE),
        childChain.readAttribute(adminSession, childNode, AttributeId.UserAccessLevel));
    assertArrayEquals(
        new RolePermissionType[0],
        (RolePermissionType[])
            childChain.readAttribute(adminSession, childNode, AttributeId.UserRolePermissions));
    assertArrayEquals(
        new RolePermissionType[0],
        childFallback.getFallbackUserRolePermissions(adminSession, childNode));

    // Deliberate local RolePermissions override on the child still takes precedence over the
    // source array's explicit UserRolePermissions.
    var adminReadPermission =
        new RolePermissionType[] {
          new RolePermissionType(
              NodeIds.WellKnownRole_SecurityAdmin,
              PermissionType.of(PermissionType.Field.Browse, PermissionType.Field.Read))
        };
    when(childNode.getAttribute(AttributeId.RolePermissions)).thenReturn(adminReadPermission);
    assertEquals(
        AccessLevel.toValue(AccessLevel.READ_ONLY),
        childChain.readAttribute(adminSession, childNode, AttributeId.UserAccessLevel));
    assertArrayEquals(
        adminReadPermission,
        (RolePermissionType[])
            childChain.readAttribute(adminSession, childNode, AttributeId.UserRolePermissions));

    // Clearing the child override re-applies the source array's empty UserRolePermissions, and
    // clearing the source array override restores default READ_ONLY + null UserRolePermissions.
    when(childNode.getAttribute(AttributeId.RolePermissions)).thenReturn(null);
    assertEquals(
        AccessLevel.toValue(AccessLevel.NONE),
        childChain.readAttribute(adminSession, childNode, AttributeId.UserAccessLevel));

    when(arrayNode.getAttribute(AttributeId.UserRolePermissions)).thenReturn(null);
    assertEquals(
        AccessLevel.toValue(AccessLevel.READ_ONLY),
        childChain.readAttribute(adminSession, childNode, AttributeId.UserAccessLevel));
    assertNull(childChain.readAttribute(adminSession, childNode, AttributeId.UserRolePermissions));
  }
}
