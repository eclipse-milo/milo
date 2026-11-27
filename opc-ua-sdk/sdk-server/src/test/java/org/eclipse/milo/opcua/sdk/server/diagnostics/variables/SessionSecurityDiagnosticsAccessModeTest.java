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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.AccessContext;
import org.eclipse.milo.opcua.sdk.server.AttributeReader;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.sdk.server.access.DefaultAccessController;
import org.eclipse.milo.opcua.sdk.server.diagnostics.SessionSecurityDiagnosticsAccessMode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterContext;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.junit.jupiter.api.Test;

class SessionSecurityDiagnosticsAccessModeTest {

  @Test
  void restrictedModeIsDefaultAndRestrictsAnonymousAccess() throws Exception {
    OpcUaServerConfig config = newConfig();

    assertEquals(
        SessionSecurityDiagnosticsAccessMode.RESTRICTED,
        config.getSessionSecurityDiagnosticsAccessMode());

    OpcUaServer server = new OpcUaServer(config, transportProfile -> null);
    Session anonymous = sessionWithRoles(null);
    Session authenticated = sessionWithRoles(List.of(NodeIds.WellKnownRole_AuthenticatedUser));
    Session configureAdmin = sessionWithRoles(List.of(NodeIds.WellKnownRole_ConfigureAdmin));
    Session securityAdmin = sessionWithRoles(List.of(NodeIds.WellKnownRole_SecurityAdmin));

    ServerDiagnosticsTypeNode diagnosticsNode = getDiagnosticsNode(server);
    PropertyTypeNode enabledFlag = diagnosticsNode.getEnabledFlagNode();
    SessionSecurityDiagnosticsArrayTypeNode securityArray =
        diagnosticsNode
            .getSessionsDiagnosticsSummaryNode()
            .getSessionSecurityDiagnosticsArrayNode();

    assertEquals(
        AccessLevel.toValue(AccessLevel.NONE), readUserAccessLevel(anonymous, securityArray));
    assertEquals(
        AccessLevel.toValue(AccessLevel.NONE), readUserAccessLevel(authenticated, securityArray));
    assertEquals(
        AccessLevel.toValue(AccessLevel.NONE), readUserAccessLevel(configureAdmin, securityArray));
    assertEquals(
        AccessLevel.toValue(AccessLevel.READ_ONLY),
        readUserAccessLevel(securityAdmin, securityArray));

    assertEquals(
        AccessLevel.toValue(AccessLevel.READ_ONLY), readUserAccessLevel(anonymous, enabledFlag));
    assertEquals(
        AccessLevel.toValue(AccessLevel.READ_ONLY),
        readUserAccessLevel(authenticated, enabledFlag));
    assertEquals(
        AccessLevel.toValue(AccessLevel.READ_WRITE),
        readUserAccessLevel(configureAdmin, enabledFlag));
    assertEquals(
        AccessLevel.toValue(AccessLevel.READ_WRITE),
        readUserAccessLevel(securityAdmin, enabledFlag));

    assertTrue(securityArray.getAccessRestrictions().getSigningRequired());
    assertTrue(securityArray.getAccessRestrictions().getEncryptionRequired());

    // OPC 10000-3 §5.2.9 & §5.2.10: RolePermissions and UserRolePermissions are omitted by default
    // and must both answer Bad_AttributeIdInvalid regardless of the Session's roles.
    for (UaVariableNode node : List.of(securityArray, enabledFlag)) {
      for (Session session : List.of(anonymous, securityAdmin)) {
        assertEquals(
            new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
            readAttribute(session, node, AttributeId.RolePermissions).statusCode());
        assertEquals(
            new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
            readAttribute(session, node, AttributeId.UserRolePermissions).statusCode());
      }
    }
  }

  // Applications may explicitly set RolePermissions on SessionSecurityDiagnosticsArray or
  // EnabledFlag to override the default fallback policies and opt in to UserRolePermissions.
  @Test
  void restrictedModeHonorsExplicitRolePermissionsOverrides() throws Exception {
    OpcUaServer server = new OpcUaServer(newConfig(), transportProfile -> null);
    ServerDiagnosticsTypeNode diagnosticsNode = getDiagnosticsNode(server);
    PropertyTypeNode enabledFlag = diagnosticsNode.getEnabledFlagNode();
    SessionSecurityDiagnosticsArrayTypeNode securityArray =
        diagnosticsNode
            .getSessionsDiagnosticsSummaryNode()
            .getSessionSecurityDiagnosticsArrayNode();

    var customRole = new NodeId(1, "DiagnosticsOperator");
    var customReadWrite =
        new RolePermissionType(
            customRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.Write,
                PermissionType.Field.ReadRolePermissions));

    securityArray.setRolePermissions(new RolePermissionType[] {customReadWrite});
    enabledFlag.setRolePermissions(new RolePermissionType[] {customReadWrite});

    Session anonymous = sessionWithRoles(null);
    Session securityAdmin = sessionWithRoles(List.of(NodeIds.WellKnownRole_SecurityAdmin));
    Session customSession = sessionWithRoles(List.of(customRole));

    assertEquals(
        AccessLevel.toValue(AccessLevel.NONE), readUserAccessLevel(anonymous, securityArray));
    assertEquals(
        AccessLevel.toValue(AccessLevel.NONE), readUserAccessLevel(securityAdmin, securityArray));
    assertEquals(
        AccessLevel.toValue(AccessLevel.READ_ONLY),
        readUserAccessLevel(customSession, securityArray));

    assertEquals(
        AccessLevel.toValue(AccessLevel.NONE), readUserAccessLevel(anonymous, enabledFlag));
    assertEquals(
        AccessLevel.toValue(AccessLevel.NONE), readUserAccessLevel(securityAdmin, enabledFlag));
    assertEquals(
        AccessLevel.toValue(AccessLevel.READ_WRITE),
        readUserAccessLevel(customSession, enabledFlag));

    var controller = new DefaultAccessController(server);

    for (UaVariableNode node : List.of(securityArray, enabledFlag)) {
      DataValue customUrp = readAttribute(customSession, node, AttributeId.UserRolePermissions);
      assertEquals(StatusCode.GOOD, customUrp.statusCode());
      assertArrayEquals(
          new RolePermissionType[] {customReadWrite},
          (RolePermissionType[]) customUrp.value().value());

      DataValue anonymousUrp = readAttribute(anonymous, node, AttributeId.UserRolePermissions);
      assertEquals(StatusCode.GOOD, anonymousUrp.statusCode());
      assertArrayEquals(
          new RolePermissionType[0], (RolePermissionType[]) anonymousUrp.value().value());

      // Even without a RoleMapper (anonymous has no mapped roles), explicit RolePermissions must
      // not be disclosed unless Anonymous holds ReadRolePermissions.
      var readRolePermissions =
          new ReadValueId(node.getNodeId(), AttributeId.RolePermissions.uid(), null, null);
      assertEquals(
          AccessResult.DENIED_USER_ACCESS,
          controller
              .checkReadAccess(anonymous, List.of(readRolePermissions))
              .get(readRolePermissions));
      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          readAttribute(anonymous, node, AttributeId.RolePermissions).statusCode());

      assertEquals(
          AccessResult.ALLOWED,
          controller
              .checkReadAccess(customSession, List.of(readRolePermissions))
              .get(readRolePermissions));
      DataValue customRp = readAttribute(customSession, node, AttributeId.RolePermissions);
      assertEquals(StatusCode.GOOD, customRp.statusCode());
      assertArrayEquals(
          new RolePermissionType[] {customReadWrite},
          (RolePermissionType[]) customRp.value().value());
    }
  }

  // An application AttributeFilter inserted at the front of the chain supplying a restrictive
  // RolePermissions policy while the stored attribute stays null must continue protecting Browse,
  // UserAccessLevel, and RolePermissions reads.
  @Test
  void frontOfChainRolePermissionsFilterRestrictsBrowseAndReadsWithoutStoredAttribute()
      throws Exception {
    OpcUaServer server = new OpcUaServer(newConfig(), transportProfile -> null);
    var controller = new DefaultAccessController(server);
    ServerDiagnosticsTypeNode diagnosticsNode = getDiagnosticsNode(server);
    SessionSecurityDiagnosticsArrayTypeNode securityArray =
        diagnosticsNode
            .getSessionsDiagnosticsSummaryNode()
            .getSessionSecurityDiagnosticsArrayNode();

    var customRole = new NodeId(1, "FrontFilterAuditor");
    var customPolicy =
        new RolePermissionType[] {
          new RolePermissionType(
              customRole,
              PermissionType.of(
                  PermissionType.Field.Browse,
                  PermissionType.Field.Read,
                  PermissionType.Field.ReadRolePermissions))
        };
    securityArray
        .getFilterChain()
        .addFirst(
            new AttributeFilter() {
              @Override
              public Object getAttribute(AttributeFilterContext ctx, AttributeId attributeId) {
                if (attributeId == AttributeId.RolePermissions) {
                  return customPolicy;
                } else {
                  return ctx.getAttribute(attributeId);
                }
              }
            });

    Session unmapped = sessionWithRoles(null);
    Session securityAdmin = sessionWithRoles(List.of(NodeIds.WellKnownRole_SecurityAdmin));
    Session customSession = sessionWithRoles(List.of(customRole));

    NodeId securityArrayId = securityArray.getNodeId();
    var readRolePermissions =
        new ReadValueId(securityArrayId, AttributeId.RolePermissions.uid(), null, null);

    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        controller.checkBrowseAccess(securityAdmin, List.of(securityArrayId)).get(securityArrayId),
        "SecurityAdmin must be denied Browse under front-of-chain override");
    assertEquals(
        AccessResult.ALLOWED,
        controller.checkBrowseAccess(customSession, List.of(securityArrayId)).get(securityArrayId),
        "custom role granted by front-of-chain override can Browse");

    assertEquals(
        AccessLevel.toValue(AccessLevel.NONE), readUserAccessLevel(unmapped, securityArray));
    assertEquals(
        AccessLevel.toValue(AccessLevel.NONE), readUserAccessLevel(securityAdmin, securityArray));
    assertEquals(
        AccessLevel.toValue(AccessLevel.READ_ONLY),
        readUserAccessLevel(customSession, securityArray));

    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        controller
            .checkReadAccess(unmapped, List.of(readRolePermissions))
            .get(readRolePermissions));
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        controller
            .checkReadAccess(securityAdmin, List.of(readRolePermissions))
            .get(readRolePermissions));
    assertEquals(
        AccessResult.ALLOWED,
        controller
            .checkReadAccess(customSession, List.of(readRolePermissions))
            .get(readRolePermissions));
  }

  // A readAttribute-only front filter supplying RolePermissions (including malformed sibling
  // entries) must be enforced consistently by both DefaultAccessController and direct
  // AttributeReader reads for mapped and unmapped Sessions, and malformed siblings must be
  // stripped before returning an authorized response.
  @Test
  void readAttributeOnlyFrontFilterEnforcesRolePermissionsAcrossControllerAndAttributeReader()
      throws Exception {
    OpcUaServer server = new OpcUaServer(newConfig(), transportProfile -> null);
    var controller = new DefaultAccessController(server);
    ServerDiagnosticsTypeNode diagnosticsNode = getDiagnosticsNode(server);
    SessionSecurityDiagnosticsArrayTypeNode securityArray =
        diagnosticsNode
            .getSessionsDiagnosticsSummaryNode()
            .getSessionSecurityDiagnosticsArrayNode();

    var customRole = new NodeId(1, "ReadAttrAuditor");
    var validEntry =
        new RolePermissionType(
            customRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));
    var mixedPolicy =
        new RolePermissionType[] {
          validEntry,
          new RolePermissionType(new NodeId(1, "BrokenRole"), null),
          new RolePermissionType(null, PermissionType.of(PermissionType.Field.Read)),
          null
        };
    securityArray
        .getFilterChain()
        .addFirst(
            new AttributeFilter() {
              @Override
              public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
                  throws UaException {
                if (attributeId == AttributeId.RolePermissions) {
                  return mixedPolicy;
                } else {
                  return ctx.readAttribute(attributeId);
                }
              }
            });

    Session unmapped = sessionWithRoles(null);
    Session securityAdmin = sessionWithRoles(List.of(NodeIds.WellKnownRole_SecurityAdmin));
    Session customSession = sessionWithRoles(List.of(customRole));

    NodeId securityArrayId = securityArray.getNodeId();
    var readRolePermissions =
        new ReadValueId(securityArrayId, AttributeId.RolePermissions.uid(), null, null);

    // Both DefaultAccessController and direct AttributeReader deny unauthorized mapped and
    // unmapped Sessions.
    for (Session denied : List.of(unmapped, securityAdmin)) {
      assertEquals(
          AccessResult.DENIED_USER_ACCESS,
          controller
              .checkReadAccess(denied, List.of(readRolePermissions))
              .get(readRolePermissions));
      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          readAttribute(denied, securityArray, AttributeId.RolePermissions).statusCode());
      assertEquals(
          AccessLevel.toValue(AccessLevel.NONE), readUserAccessLevel(denied, securityArray));
    }

    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        controller.checkBrowseAccess(securityAdmin, List.of(securityArrayId)).get(securityArrayId));

    // Authorized Session passes both checks and receives only the valid entry.
    assertEquals(
        AccessResult.ALLOWED,
        controller.checkBrowseAccess(customSession, List.of(securityArrayId)).get(securityArrayId));
    assertEquals(
        AccessResult.ALLOWED,
        controller
            .checkReadAccess(customSession, List.of(readRolePermissions))
            .get(readRolePermissions));

    DataValue authorizedRp =
        readAttribute(customSession, securityArray, AttributeId.RolePermissions);
    assertEquals(StatusCode.GOOD, authorizedRp.statusCode());
    assertArrayEquals(
        new RolePermissionType[] {validEntry}, (RolePermissionType[]) authorizedRp.value().value());
  }

  // AttributeReader must authorize the exact RolePermissionType[] returned by the single filter
  // chain read rather than re-reading RolePermissions, and must authorize via UaNode.readAttribute
  // only once so neither the front filter nor the RoleMapper runs twice per read.
  @Test
  void attributeReaderAuthorizesExactPolicyReturnedWhenFrontFilterChangesBetweenCalls() {
    OpcUaServer server = new OpcUaServer(newConfig(), transportProfile -> null);
    ServerDiagnosticsTypeNode diagnosticsNode = getDiagnosticsNode(server);
    SessionSecurityDiagnosticsArrayTypeNode securityArray =
        diagnosticsNode
            .getSessionsDiagnosticsSummaryNode()
            .getSessionSecurityDiagnosticsArrayNode();

    var customRole = new NodeId(1, "SecretOwner");
    var secretEntry =
        new RolePermissionType(
            customRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));
    var permissiveEntry =
        new RolePermissionType(
            NodeIds.WellKnownRole_Anonymous,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));

    var readCalls = new AtomicInteger(0);
    securityArray
        .getFilterChain()
        .addFirst(
            new AttributeFilter() {
              @Override
              public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
                  throws UaException {
                if (attributeId == AttributeId.RolePermissions) {
                  int call = readCalls.incrementAndGet();
                  if (call == 1) {
                    // First read returns a restrictive policy denying Anonymous / Authenticated.
                    return new RolePermissionType[] {secretEntry};
                  } else if (call == 2) {
                    // Second read returns a permissive policy plus a malformed sibling.
                    return new RolePermissionType[] {permissiveEntry, secretEntry, null};
                  } else {
                    return new RolePermissionType[0];
                  }
                } else {
                  return ctx.readAttribute(attributeId);
                }
              }
            });

    var roleMapperCalls = new AtomicInteger(0);
    Session mappedSession = mock(Session.class);
    when(mappedSession.getRoleIds())
        .thenAnswer(
            invocation -> {
              roleMapperCalls.incrementAndGet();
              return Optional.of(List.of(NodeIds.WellKnownRole_AuthenticatedUser));
            });

    // Call 1: returns secretEntry (no ReadRolePermissions for AuthenticatedUser or Anonymous).
    // Must deny while invoking both the front filter and RoleMapper exactly once.
    DataValue denied = readAttribute(mappedSession, securityArray, AttributeId.RolePermissions);
    assertEquals(new StatusCode(StatusCodes.Bad_UserAccessDenied), denied.statusCode());
    assertEquals(1, readCalls.get(), "AttributeReader must invoke RolePermissions filter once");
    assertEquals(1, roleMapperCalls.get(), "AttributeReader must invoke RoleMapper once per read");

    // Call 2: returns permissiveEntry + secretEntry + null. Must authorize that exact sanitized
    // array without invoking call 3 (which would deny) and with only one RoleMapper invocation.
    DataValue allowed = readAttribute(mappedSession, securityArray, AttributeId.RolePermissions);
    assertEquals(StatusCode.GOOD, allowed.statusCode());
    assertArrayEquals(
        new RolePermissionType[] {permissiveEntry, secretEntry},
        (RolePermissionType[]) allowed.value().value());
    assertEquals(2, readCalls.get(), "AttributeReader must invoke RolePermissions filter once");
    assertEquals(2, roleMapperCalls.get(), "AttributeReader must invoke RoleMapper once per read");
  }

  // An addFirst front filter that delegates to ctx.readAttribute(RolePermissions) and appends a
  // ReadRolePermissions grant to an existing non-null policy must succeed through
  // DefaultAccessController, AttributeReader, and direct UaNode.readAttribute, while sessions not
  // granted ReadRolePermissions are still denied across all three paths.
  @Test
  void delegatingFrontFilterAppendingReadRolePermissionsSucceedsAcrossAllReadPaths()
      throws Exception {
    OpcUaServer server = new OpcUaServer(newConfig(), transportProfile -> null);
    var controller = new DefaultAccessController(server);
    ServerDiagnosticsTypeNode diagnosticsNode = getDiagnosticsNode(server);
    SessionSecurityDiagnosticsArrayTypeNode securityArray =
        diagnosticsNode
            .getSessionsDiagnosticsSummaryNode()
            .getSessionSecurityDiagnosticsArrayNode();

    var existingRole = new NodeId(1, "ExistingOwner");
    var appendedRole = new NodeId(1, "AppendedReader");
    var existingEntry =
        new RolePermissionType(
            existingRole,
            PermissionType.of(PermissionType.Field.Browse, PermissionType.Field.Read));
    var appendedEntry =
        new RolePermissionType(
            appendedRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));

    // Stored policy is non-null and denies ReadRolePermissions to everyone on its own.
    securityArray.setRolePermissions(new RolePermissionType[] {existingEntry});

    securityArray
        .getFilterChain()
        .addFirst(
            new AttributeFilter() {
              @Override
              public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
                  throws UaException {
                if (attributeId == AttributeId.RolePermissions) {
                  var base = (RolePermissionType[]) ctx.readAttribute(AttributeId.RolePermissions);
                  RolePermissionType[] augmented = Arrays.copyOf(base, base.length + 1);
                  augmented[base.length] = appendedEntry;
                  return augmented;
                } else {
                  return ctx.readAttribute(attributeId);
                }
              }
            });

    Session unmapped = sessionWithRoles(null);
    Session existingSession = sessionWithRoles(List.of(existingRole));
    Session appendedSession = sessionWithRoles(List.of(appendedRole));

    var readRolePermissions =
        new ReadValueId(securityArray.getNodeId(), AttributeId.RolePermissions.uid(), null, null);
    var expectedAugmented = new RolePermissionType[] {existingEntry, appendedEntry};

    // Appended role succeeds via DefaultAccessController, AttributeReader, and
    // UaNode.readAttribute.
    assertEquals(
        AccessResult.ALLOWED,
        controller
            .checkReadAccess(appendedSession, List.of(readRolePermissions))
            .get(readRolePermissions));
    DataValue readerResult =
        readAttribute(appendedSession, securityArray, AttributeId.RolePermissions);
    assertEquals(StatusCode.GOOD, readerResult.statusCode());
    assertArrayEquals(expectedAugmented, (RolePermissionType[]) readerResult.value().value());
    assertArrayEquals(
        expectedAugmented,
        (RolePermissionType[])
            securityArray.readAttribute(
                () -> Optional.of(appendedSession), AttributeId.RolePermissions));

    // Sessions without ReadRolePermissions (mapped or unmapped) are still denied across all three
    // paths, including direct UaNode.readAttribute.
    for (Session denied : List.of(unmapped, existingSession)) {
      assertEquals(
          AccessResult.DENIED_USER_ACCESS,
          controller
              .checkReadAccess(denied, List.of(readRolePermissions))
              .get(readRolePermissions));
      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          readAttribute(denied, securityArray, AttributeId.RolePermissions).statusCode());
      UaException directDenied =
          assertThrows(
              UaException.class,
              () ->
                  securityArray.readAttribute(
                      () -> Optional.of(denied), AttributeId.RolePermissions));
      assertEquals(StatusCodes.Bad_UserAccessDenied, directDenied.getStatusCode().getValue());
    }
  }

  @Test
  void legacyModeRestoresPreviousAccessWithoutRoleMapping() throws Exception {
    OpcUaServerConfig config = newConfig(SessionSecurityDiagnosticsAccessMode.LEGACY);

    OpcUaServer server = new OpcUaServer(config, transportProfile -> null);
    Session session = sessionWithRoles(null);
    ServerDiagnosticsTypeNode diagnosticsNode = getDiagnosticsNode(server);
    PropertyTypeNode enabledFlag = diagnosticsNode.getEnabledFlagNode();
    SessionSecurityDiagnosticsArrayTypeNode securityArray =
        diagnosticsNode
            .getSessionsDiagnosticsSummaryNode()
            .getSessionSecurityDiagnosticsArrayNode();

    assertEquals(
        AccessLevel.toValue(AccessLevel.READ_ONLY), readUserAccessLevel(session, securityArray));
    assertEquals(
        AccessLevel.toValue(AccessLevel.READ_WRITE), readUserAccessLevel(session, enabledFlag));
    assertTrue(securityArray.getAccessRestrictions().getSigningRequired());
    assertTrue(securityArray.getAccessRestrictions().getEncryptionRequired());
    assertNull(securityArray.getRolePermissions());
    assertNull(enabledFlag.getRolePermissions());
    assertEquals(
        new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
        readAttribute(session, securityArray, AttributeId.RolePermissions).statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
        readAttribute(session, securityArray, AttributeId.UserRolePermissions).statusCode());
  }

  // In RESTRICTED mode, DefaultAccessController enforces the fallback Browse policy for mapped
  // Sessions without publishing RolePermissions or UserRolePermissions publicly.
  @Test
  void restrictedModeDeniesBrowseForMappedNonAdminWhileOmittingUserRolePermissions() {
    OpcUaServer server = new OpcUaServer(newConfig(), transportProfile -> null);
    var controller = new DefaultAccessController(server);

    NodeId securityArrayId =
        NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray;
    NodeId enabledFlagId = NodeIds.Server_ServerDiagnostics_EnabledFlag;

    Session unmapped = sessionWithRoles(null);
    Session authenticated = sessionWithRoles(List.of(NodeIds.WellKnownRole_AuthenticatedUser));
    Session configureAdmin = sessionWithRoles(List.of(NodeIds.WellKnownRole_ConfigureAdmin));
    Session securityAdmin = sessionWithRoles(List.of(NodeIds.WellKnownRole_SecurityAdmin));

    assertEquals(
        AccessResult.ALLOWED,
        controller.checkBrowseAccess(unmapped, List.of(securityArrayId)).get(securityArrayId),
        "without a RoleMapper, Browse is unaffected");
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        controller.checkBrowseAccess(authenticated, List.of(securityArrayId)).get(securityArrayId),
        "mapped AuthenticatedUser cannot Browse SessionSecurityDiagnosticsArray");
    assertEquals(
        AccessResult.DENIED_USER_ACCESS,
        controller.checkBrowseAccess(configureAdmin, List.of(securityArrayId)).get(securityArrayId),
        "mapped ConfigureAdmin cannot Browse SessionSecurityDiagnosticsArray");
    assertEquals(
        AccessResult.ALLOWED,
        controller.checkBrowseAccess(securityAdmin, List.of(securityArrayId)).get(securityArrayId),
        "mapped SecurityAdmin can Browse SessionSecurityDiagnosticsArray");

    assertEquals(
        AccessResult.ALLOWED,
        controller.checkBrowseAccess(authenticated, List.of(enabledFlagId)).get(enabledFlagId),
        "EnabledFlag remains browsable via implicit Anonymous Browse permission");
    assertEquals(
        AccessResult.ALLOWED,
        controller.checkBrowseAccess(securityAdmin, List.of(enabledFlagId)).get(enabledFlagId));
  }

  @Test
  void copyPreservesConfiguredAccessMode() {
    OpcUaServerConfig config = newConfig(SessionSecurityDiagnosticsAccessMode.LEGACY);

    OpcUaServerConfig copiedConfig = OpcUaServerConfig.copy(config).build();

    assertEquals(
        SessionSecurityDiagnosticsAccessMode.LEGACY,
        copiedConfig.getSessionSecurityDiagnosticsAccessMode());
  }

  private static OpcUaServerConfig newConfig(SessionSecurityDiagnosticsAccessMode accessMode) {
    return OpcUaServerConfig.builder()
        .setCertificateManager(new DefaultCertificateManager())
        .setSessionSecurityDiagnosticsAccessMode(accessMode)
        .build();
  }

  private static OpcUaServerConfig newConfig() {
    return OpcUaServerConfig.builder()
        .setCertificateManager(new DefaultCertificateManager())
        .build();
  }

  private static ServerDiagnosticsTypeNode getDiagnosticsNode(OpcUaServer server) {
    return (ServerDiagnosticsTypeNode)
        server
            .getAddressSpaceManager()
            .getManagedNode(NodeIds.Server_ServerDiagnostics)
            .orElseThrow();
  }

  private static Session sessionWithRoles(List<NodeId> roles) {
    Session session = mock(Session.class);
    when(session.getRoleIds()).thenReturn(Optional.ofNullable(roles));
    when(session.getEndpoint())
        .thenReturn(
            new EndpointDescription(
                null, null, null, MessageSecurityMode.SignAndEncrypt, null, null, null, null));
    return session;
  }

  private static UByte readUserAccessLevel(Session session, UaVariableNode node) throws Exception {
    return (UByte) node.getFilterChain().readAttribute(session, node, AttributeId.UserAccessLevel);
  }

  private static DataValue readAttribute(
      Session session, UaVariableNode node, AttributeId attributeId) {
    AccessContext context = () -> Optional.ofNullable(session);
    return AttributeReader.readAttribute(
        context, node, attributeId, TimestampsToReturn.Neither, null, null);
  }
}
