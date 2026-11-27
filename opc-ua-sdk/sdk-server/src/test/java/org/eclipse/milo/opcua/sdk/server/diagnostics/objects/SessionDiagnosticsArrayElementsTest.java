/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.diagnostics.objects;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.WriteMask;
import org.eclipse.milo.opcua.sdk.server.AccessContext;
import org.eclipse.milo.opcua.sdk.server.AddressSpaceFilter;
import org.eclipse.milo.opcua.sdk.server.AttributeReader;
import org.eclipse.milo.opcua.sdk.server.ManagedAddressSpaceFragment;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.SessionListener;
import org.eclipse.milo.opcua.sdk.server.SimpleAddressSpaceFilter;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.access.AccessController.AccessResult;
import org.eclipse.milo.opcua.sdk.server.access.DefaultAccessController;
import org.eclipse.milo.opcua.sdk.server.access.FallbackUserRolePermissionsFilter;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SessionDiagnosticsObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SessionsDiagnosticsSummaryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterContext;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationRequest;
import org.eclipse.milo.opcua.sdk.server.subscriptions.SubscriptionManager;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/**
 * The elements of SessionDiagnosticsArray and SessionSecurityDiagnosticsArray are the Session
 * diagnostics Object's own Variables (Part 5 §7.13 and §7.15).
 *
 * <p>These tests drive the summary's session listener directly, because the SessionManager offers
 * no way to invoke it for a Session that has no transport.
 */
class SessionDiagnosticsArrayElementsTest {

  enum SessionArray {
    SESSION_DIAGNOSTICS(
        "SessionDiagnostics",
        SessionsDiagnosticsSummaryTypeNode::getSessionDiagnosticsArrayNode,
        SessionDiagnosticsObjectTypeNode::getSessionDiagnosticsNode),
    SESSION_SECURITY_DIAGNOSTICS(
        "SessionSecurityDiagnostics",
        SessionsDiagnosticsSummaryTypeNode::getSessionSecurityDiagnosticsArrayNode,
        SessionDiagnosticsObjectTypeNode::getSessionSecurityDiagnosticsNode);

    final String browseName;
    final Function<SessionsDiagnosticsSummaryTypeNode, UaVariableNode> array;
    final Function<SessionDiagnosticsObjectTypeNode, UaVariableNode> objectComponent;

    SessionArray(
        String browseName,
        Function<SessionsDiagnosticsSummaryTypeNode, UaVariableNode> array,
        Function<SessionDiagnosticsObjectTypeNode, UaVariableNode> objectComponent) {

      this.browseName = browseName;
      this.array = array;
      this.objectComponent = objectComponent;
    }
  }

  private OpcUaServer server;
  private UaNodeManager target;
  private ServerDiagnosticsTypeNode diagnosticsNode;
  private SessionsDiagnosticsSummaryTypeNode summaryNode;
  private SessionsDiagnosticsSummaryObject summary;
  private SessionListener listener;

  @BeforeEach
  void startSummaryObject() throws Exception {
    var config =
        OpcUaServerConfig.builder().setCertificateManager(new DefaultCertificateManager()).build();
    server = new OpcUaServer(config, transportProfile -> null);
    target = new UaNodeManager();
    server.getAddressSpaceManager().register(target);

    diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();
    summaryNode = diagnosticsNode.getSessionsDiagnosticsSummaryNode();

    summary = new SessionsDiagnosticsSummaryObject(summaryNode, target);
    summary.startup();

    Field field = SessionsDiagnosticsSummaryObject.class.getDeclaredField("sessionListener");
    field.setAccessible(true);
    listener = (SessionListener) field.get(summary);
  }

  @AfterEach
  void shutdownSummaryObject() {
    summary.shutdown();
    server.getAddressSpaceManager().unregister(target);
  }

  // Part 5 §7.13 and §7.15 say the array elements are also referenced by the Session Objects, and
  // an instance keeps its InstanceDeclaration's BrowseName (Part 3 §4.6.4). A separate copy for the
  // array gives clients two NodeIds for one Session's diagnostics and a namespace 1 BrowseName.
  @ParameterizedTest
  @EnumSource(SessionArray.class)
  void arrayElementIsTheSessionObjectsOwnVariable(SessionArray kind) {
    diagnosticsNode.setEnabledFlag(true);
    listener.onSessionCreated(session("Session1"));

    UaVariableNode objectVariable = kind.objectComponent.apply(singleSessionObject());

    assertEquals(
        List.of(objectVariable.getNodeId()),
        elements(kind.array.apply(summaryNode)),
        "the only element is the Session Object's own Variable");
    assertEquals(new QualifiedName(0, kind.browseName), objectVariable.getBrowseName());
    assertEquals(
        1,
        target.getNodes().stream()
            .filter(n -> n.getBrowseName().name().equals(kind.browseName))
            .count(),
        "one " + kind.browseName + " Variable per Session");
  }

  // The array References live in the diagnostics NodeManager beside the Object, not with the
  // arrays. Removing the Object, when its Session closes or diagnostics turn off (Part 5 §6.3.3),
  // must remove them from every NodeManager, or the arrays keep elements that no longer resolve.
  // The Sessions are created while diagnostics are on because the mocks are unknown to the
  // SessionManager, which the enable pass reads.
  @ParameterizedTest
  @EnumSource(SessionArray.class)
  void arrayElementIsRemovedWithItsSessionObject(SessionArray kind) {
    UaVariableNode array = kind.array.apply(summaryNode);
    diagnosticsNode.setEnabledFlag(true);

    Session closing = session("Session1");
    listener.onSessionCreated(closing);
    assertEquals(1, elements(array).size(), "element added with the Session Object");

    listener.onSessionClosed(closing);
    assertEquals(List.of(), elements(array), "element removed when its Session closes");

    listener.onSessionCreated(session("Session2"));
    assertEquals(1, elements(array).size(), "element added with the Session Object");

    diagnosticsNode.setEnabledFlag(false);
    assertEquals(List.of(), elements(array), "element removed when diagnostics turn off");
  }

  // Part 3 §5.6.2 makes AccessLevelEx mandatory from 1.04 on, with its low eight bits mirroring
  // AccessLevel. Element Variables used to get it from an explicit root attribute; they now take it
  // from the SessionDiagnosticsObjectType InstanceDeclarations.
  @ParameterizedTest
  @EnumSource(SessionArray.class)
  void arrayElementReportsAccessLevelExMatchingAccessLevel(SessionArray kind) {
    diagnosticsNode.setEnabledFlag(true);
    listener.onSessionCreated(session("Session1"));

    UaVariableNode element = singleElement(kind.array.apply(summaryNode));

    AccessLevelExType accessLevelEx = element.getAccessLevelEx();
    assertNotNull(accessLevelEx, "element must carry AccessLevelEx");
    assertTrue(accessLevelEx.getCurrentRead(), "element AccessLevelEx must allow CurrentRead");
    assertEquals(
        element.getAccessLevel().intValue(),
        accessLevelEx.getValue().intValue() & 0xFF,
        "low eight bits of AccessLevelEx must mirror AccessLevel");
  }

  // Part 5 §6.3.5: SessionSecurityDiagnostics should be accessible only to authorised users.
  // By default, namespace-0 nodes omit RolePermissions and UserRolePermissions (OPC 10000-3
  // §5.2.9, §5.2.10), while the security array's AccessRestrictions propagate to its element and
  // fields and restricted mode keeps UserAccessLevel fail-closed except for SecurityAdmin.
  @Test
  void securityArrayElementCarriesTheSecurityArraysAccessMetadata() throws Exception {
    diagnosticsNode.setEnabledFlag(true);
    listener.onSessionCreated(session("Session1"));

    SessionSecurityDiagnosticsArrayTypeNode array =
        summaryNode.getSessionSecurityDiagnosticsArrayNode();
    var element = (SessionSecurityDiagnosticsTypeNode) singleElement(array);

    assertNull(array.getRolePermissions(), "the security array omits RolePermissions by default");

    Session anonymous = mock(Session.class);
    when(anonymous.getRoleIds()).thenReturn(Optional.empty());

    Session authenticated = mock(Session.class);
    when(authenticated.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_AuthenticatedUser)));

    Session securityAdmin = mock(Session.class);
    when(securityAdmin.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_SecurityAdmin)));

    for (UaVariableNode node : List.of(element, element.getClientCertificateNode())) {
      String name = node.getBrowseName().name();
      assertNull(node.getRolePermissions(), name + " RolePermissions");
      assertNull(
          node.getFilterChain().readAttribute(anonymous, node, AttributeId.UserRolePermissions),
          name + " UserRolePermissions for anonymous Session");
      assertNull(
          node.getFilterChain().readAttribute(securityAdmin, node, AttributeId.UserRolePermissions),
          name + " UserRolePermissions for SecurityAdmin Session");
      assertEquals(
          array.getAccessRestrictions(),
          node.getAccessRestrictions(),
          name + " AccessRestrictions");
      assertEquals(
          AccessLevel.toValue(AccessLevel.NONE),
          node.getFilterChain().readAttribute(anonymous, node, AttributeId.UserAccessLevel),
          name + " UserAccessLevel for an anonymous Session");
      assertEquals(
          AccessLevel.toValue(AccessLevel.NONE),
          node.getFilterChain().readAttribute(authenticated, node, AttributeId.UserAccessLevel),
          name + " UserAccessLevel for an AuthenticatedUser Session");
      assertEquals(
          AccessLevel.toValue(AccessLevel.READ_ONLY),
          node.getFilterChain().readAttribute(securityAdmin, node, AttributeId.UserAccessLevel),
          name + " UserAccessLevel for a SecurityAdmin Session");
    }
  }

  // Applications that explicitly configure RolePermissions on SessionSecurityDiagnosticsArray
  // override the default fallback policy and must see those RolePermissions propagate to dynamic
  // elements and their child fields along with filtered UserRolePermissions.
  @Test
  void explicitSecurityArrayRolePermissionsPropagateToDynamicElements() throws Exception {
    SessionSecurityDiagnosticsArrayTypeNode array =
        summaryNode.getSessionSecurityDiagnosticsArrayNode();
    var customRole = new NodeId(1, "SecurityDiagnosticsAuditor");
    var customRolePermission =
        new RolePermissionType(
            customRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));
    var customPolicy = new RolePermissionType[] {customRolePermission};
    array.setRolePermissions(customPolicy);

    diagnosticsNode.setEnabledFlag(true);
    listener.onSessionCreated(session("Session1"));

    var element = (SessionSecurityDiagnosticsTypeNode) singleElement(array);

    Session anonymous = mock(Session.class);
    when(anonymous.getRoleIds()).thenReturn(Optional.empty());

    Session securityAdmin = mock(Session.class);
    when(securityAdmin.getRoleIds())
        .thenReturn(Optional.of(List.of(NodeIds.WellKnownRole_SecurityAdmin)));

    Session auditor = mock(Session.class);
    when(auditor.getRoleIds()).thenReturn(Optional.of(List.of(customRole)));

    for (UaVariableNode node : List.of(element, element.getClientCertificateNode())) {
      String name = node.getBrowseName().name();
      // Dynamic elements inherit the array's authorization policy without substituting the parent
      // array's RolePermissions into their own RolePermissions attribute (OPC 10000-3 §5.2.9).
      assertNull(node.getRolePermissions(), name + " local RolePermissions stays null");
      assertArrayEquals(
          new RolePermissionType[0],
          (RolePermissionType[])
              node.getFilterChain().readAttribute(anonymous, node, AttributeId.UserRolePermissions),
          name + " UserRolePermissions for anonymous Session");
      assertArrayEquals(
          new RolePermissionType[] {customRolePermission},
          (RolePermissionType[])
              node.getFilterChain().readAttribute(auditor, node, AttributeId.UserRolePermissions),
          name + " UserRolePermissions for custom auditor Session");
      assertEquals(
          AccessLevel.toValue(AccessLevel.NONE),
          node.getFilterChain().readAttribute(anonymous, node, AttributeId.UserAccessLevel),
          name + " UserAccessLevel for anonymous Session");
      assertEquals(
          AccessLevel.toValue(AccessLevel.NONE),
          node.getFilterChain().readAttribute(securityAdmin, node, AttributeId.UserAccessLevel),
          name + " UserAccessLevel for SecurityAdmin when overridden by custom policy");
      assertEquals(
          AccessLevel.toValue(AccessLevel.READ_ONLY),
          node.getFilterChain().readAttribute(auditor, node, AttributeId.UserAccessLevel),
          name + " UserAccessLevel for custom auditor Session");
    }
  }

  // A readAttribute-only front filter installed on SessionSecurityDiagnosticsArray (leaving its
  // stored RolePermissions null) must still be inherited dynamically by dynamic element Variables
  // and their child fields rather than falling back to SecurityAdmin.
  @Test
  void readAttributeOnlyFrontFilterOnSecurityArrayPropagatesToDynamicElementsAndFields()
      throws Exception {
    SessionSecurityDiagnosticsArrayTypeNode array =
        summaryNode.getSessionSecurityDiagnosticsArrayNode();
    var customRole = new NodeId(1, "DynamicReadAttrAuditor");
    var customRolePermission =
        new RolePermissionType(
            customRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));
    var customPolicy = new RolePermissionType[] {customRolePermission};

    array
        .getFilterChain()
        .addFirst(
            new AttributeFilter() {
              @Override
              public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
                  throws UaException {
                if (attributeId == AttributeId.RolePermissions) {
                  return customPolicy;
                } else {
                  return ctx.readAttribute(attributeId);
                }
              }
            });

    diagnosticsNode.setEnabledFlag(true);
    listener.onSessionCreated(session("Session1"));

    var element = (SessionSecurityDiagnosticsTypeNode) singleElement(array);
    var addressSpace =
        new ManagedAddressSpaceFragment(server, target) {
          @Override
          public AddressSpaceFilter getFilter() {
            return SimpleAddressSpaceFilter.create(target::containsNode);
          }
        };
    server.getAddressSpaceManager().register(addressSpace);
    try {
      var controller = new DefaultAccessController(server);

      Session anonymous = sessionWithRoles(null);
      Session securityAdmin = sessionWithRoles(List.of(NodeIds.WellKnownRole_SecurityAdmin));
      Session auditor = sessionWithRoles(List.of(customRole));

      for (UaVariableNode node : List.of(element, element.getClientCertificateNode())) {
        String name = node.getBrowseName().name();
        NodeId nodeId = node.getNodeId();
        var readRolePermissions =
            new ReadValueId(nodeId, AttributeId.RolePermissions.uid(), null, null);

        assertEquals(
            AccessLevel.toValue(AccessLevel.NONE),
            node.getFilterChain().readAttribute(anonymous, node, AttributeId.UserAccessLevel),
            name + " UserAccessLevel for anonymous Session");
        assertEquals(
            AccessLevel.toValue(AccessLevel.NONE),
            node.getFilterChain().readAttribute(securityAdmin, node, AttributeId.UserAccessLevel),
            name + " UserAccessLevel for SecurityAdmin excluded by array front filter");
        assertEquals(
            AccessLevel.toValue(AccessLevel.READ_ONLY),
            node.getFilterChain().readAttribute(auditor, node, AttributeId.UserAccessLevel),
            name + " UserAccessLevel for custom auditor Session");

        assertArrayEquals(
            new RolePermissionType[0],
            (RolePermissionType[])
                node.getFilterChain()
                    .readAttribute(securityAdmin, node, AttributeId.UserRolePermissions),
            name + " UserRolePermissions for SecurityAdmin");
        assertArrayEquals(
            customPolicy,
            (RolePermissionType[])
                node.getFilterChain().readAttribute(auditor, node, AttributeId.UserRolePermissions),
            name + " UserRolePermissions for custom auditor");

        assertEquals(
            AccessResult.DENIED_USER_ACCESS,
            controller.checkBrowseAccess(securityAdmin, List.of(nodeId)).get(nodeId),
            name + " Browse denied for SecurityAdmin");
        assertEquals(
            AccessResult.ALLOWED,
            controller.checkBrowseAccess(auditor, List.of(nodeId)).get(nodeId),
            name + " Browse allowed for custom auditor");

        for (Session denied : List.of(anonymous, securityAdmin)) {
          assertEquals(
              AccessResult.DENIED_USER_ACCESS,
              controller
                  .checkReadAccess(denied, List.of(readRolePermissions))
                  .get(readRolePermissions),
              name + " checkReadAccess(RolePermissions) denied");
          assertEquals(
              new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
              readAttribute(denied, node, AttributeId.RolePermissions).statusCode(),
              name + " non-writable node without local override omits RolePermissions");
        }

        assertEquals(
            AccessResult.ALLOWED,
            controller
                .checkReadAccess(auditor, List.of(readRolePermissions))
                .get(readRolePermissions),
            name + " checkReadAccess(RolePermissions) allowed for custom auditor");
        assertEquals(
            new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
            readAttribute(auditor, node, AttributeId.RolePermissions).statusCode(),
            name + " non-writable node without local override omits RolePermissions for auditor");
      }
    } finally {
      server.getAddressSpaceManager().unregister(addressSpace);
    }
  }

  // Tightening or revoking stored RolePermissions on SessionSecurityDiagnosticsArray after a
  // Session element has been created must immediately deny the revoked role on the exact same
  // already-created element and its ClientCertificate field, while an explicit local override on a
  // specific child node still takes precedence.
  @Test
  void revokingStoredSecurityArrayRolePermissionsImmediatelyDeniesExistingElementsAndFields()
      throws Exception {
    SessionSecurityDiagnosticsArrayTypeNode array =
        summaryNode.getSessionSecurityDiagnosticsArrayNode();
    var auditorRole = new NodeId(1, "RevocableAuditor");
    var replacementRole = new NodeId(1, "ReplacementAuditor");
    var auditorPermission =
        new RolePermissionType(
            auditorRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));
    var replacementPermission =
        new RolePermissionType(
            replacementRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));

    array.setRolePermissions(new RolePermissionType[] {auditorPermission});

    diagnosticsNode.setEnabledFlag(true);
    listener.onSessionCreated(session("Session1"));

    var element = (SessionSecurityDiagnosticsTypeNode) singleElement(array);
    UaVariableNode clientCert = element.getClientCertificateNode();
    var addressSpace =
        new ManagedAddressSpaceFragment(server, target) {
          @Override
          public AddressSpaceFilter getFilter() {
            return SimpleAddressSpaceFilter.create(target::containsNode);
          }
        };
    server.getAddressSpaceManager().register(addressSpace);
    try {
      var controller = new DefaultAccessController(server);
      Session auditor = sessionWithRoles(List.of(auditorRole));
      Session replacement = sessionWithRoles(List.of(replacementRole));

      // Before revocation, auditor can access the newly created element and ClientCertificate,
      // while non-writable dynamic nodes without local overrides omit RolePermissions.
      for (UaVariableNode node : List.of(element, clientCert)) {
        var readRp =
            new ReadValueId(node.getNodeId(), AttributeId.RolePermissions.uid(), null, null);
        assertEquals(
            AccessLevel.toValue(AccessLevel.READ_ONLY),
            node.getFilterChain().readAttribute(auditor, node, AttributeId.UserAccessLevel));
        assertArrayEquals(
            new RolePermissionType[] {auditorPermission},
            (RolePermissionType[])
                node.getFilterChain()
                    .readAttribute(auditor, node, AttributeId.UserRolePermissions));
        assertEquals(
            AccessResult.ALLOWED,
            controller.checkBrowseAccess(auditor, List.of(node.getNodeId())).get(node.getNodeId()));
        assertEquals(
            AccessResult.ALLOWED, controller.checkReadAccess(auditor, List.of(readRp)).get(readRp));
        assertEquals(
            new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
            readAttribute(auditor, node, AttributeId.RolePermissions).statusCode());
      }

      // Revoke auditor on the array without recreating the Session element.
      var tightenedPolicy = new RolePermissionType[] {replacementPermission};
      array.setRolePermissions(tightenedPolicy);

      // The exact same existing element and ClientCertificate immediately deny auditor and allow
      // replacementRole.
      for (UaVariableNode node : List.of(element, clientCert)) {
        String name = node.getBrowseName().name();
        NodeId nodeId = node.getNodeId();
        var readRp = new ReadValueId(nodeId, AttributeId.RolePermissions.uid(), null, null);

        assertNull(node.getRolePermissions(), name + " local RolePermissions stays null");
        assertEquals(
            AccessLevel.toValue(AccessLevel.NONE),
            node.getFilterChain().readAttribute(auditor, node, AttributeId.UserAccessLevel),
            name + " revoked auditor UserAccessLevel");
        assertArrayEquals(
            new RolePermissionType[0],
            (RolePermissionType[])
                node.getFilterChain().readAttribute(auditor, node, AttributeId.UserRolePermissions),
            name + " revoked auditor UserRolePermissions");
        assertEquals(
            AccessResult.DENIED_USER_ACCESS,
            controller.checkBrowseAccess(auditor, List.of(nodeId)).get(nodeId),
            name + " revoked auditor Browse");
        assertEquals(
            AccessResult.DENIED_USER_ACCESS,
            controller.checkReadAccess(auditor, List.of(readRp)).get(readRp),
            name + " revoked auditor checkReadAccess(RolePermissions)");
        assertEquals(
            new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
            readAttribute(auditor, node, AttributeId.RolePermissions).statusCode(),
            name + " non-writable node without local override omits RolePermissions");

        assertEquals(
            AccessLevel.toValue(AccessLevel.READ_ONLY),
            node.getFilterChain().readAttribute(replacement, node, AttributeId.UserAccessLevel));
        assertArrayEquals(
            tightenedPolicy,
            (RolePermissionType[])
                node.getFilterChain()
                    .readAttribute(replacement, node, AttributeId.UserRolePermissions));
        assertEquals(
            AccessResult.ALLOWED,
            controller.checkBrowseAccess(replacement, List.of(nodeId)).get(nodeId));
        assertEquals(
            AccessResult.ALLOWED,
            controller.checkReadAccess(replacement, List.of(readRp)).get(readRp));
        assertEquals(
            new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
            readAttribute(replacement, node, AttributeId.RolePermissions).statusCode());
      }

      // When WriteMask.RolePermissions is set on a dynamic element without a local override,
      // direct RolePermissions reads still enforce the inherited policy (denying revoked auditor)
      // and return Good empty RolePermissionType[0] to the authorized replacement (OPC 10000-3
      // §5.2.9).
      element.setWriteMask(UInteger.valueOf(WriteMask.RolePermissions.getValue()));
      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          readAttribute(auditor, element, AttributeId.RolePermissions).statusCode(),
          "writable element without override denies revoked auditor on direct AttributeReader");
      UaException directDenied =
          assertThrows(
              UaException.class,
              () -> element.readAttribute(() -> Optional.of(auditor), AttributeId.RolePermissions));
      assertEquals(StatusCodes.Bad_UserAccessDenied, directDenied.getStatusCode().getValue());
      DataValue writableReplacementRp =
          readAttribute(replacement, element, AttributeId.RolePermissions);
      assertEquals(StatusCode.GOOD, writableReplacementRp.statusCode());
      assertArrayEquals(
          new RolePermissionType[0], (RolePermissionType[]) writableReplacementRp.value().value());

      // An explicit local override on ClientCertificate still takes precedence over the array and
      // is exposed directly on ClientCertificate's own RolePermissions attribute.
      var localOverride = new RolePermissionType[] {auditorPermission};
      clientCert.setRolePermissions(localOverride);
      assertArrayEquals(localOverride, clientCert.getRolePermissions());
      DataValue localAuditorRp = readAttribute(auditor, clientCert, AttributeId.RolePermissions);
      assertEquals(StatusCode.GOOD, localAuditorRp.statusCode());
      assertArrayEquals(localOverride, (RolePermissionType[]) localAuditorRp.value().value());
      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          readAttribute(replacement, clientCert, AttributeId.RolePermissions).statusCode());
      assertEquals(
          AccessLevel.toValue(AccessLevel.READ_ONLY),
          clientCert
              .getFilterChain()
              .readAttribute(auditor, clientCert, AttributeId.UserAccessLevel));
      assertEquals(
          AccessLevel.toValue(AccessLevel.NONE),
          element.getFilterChain().readAttribute(auditor, element, AttributeId.UserAccessLevel));
    } finally {
      server.getAddressSpaceManager().unregister(addressSpace);
    }
  }

  // In RESTRICTED mode, live inheritance filters must be installed during staged instantiation so
  // dynamic security diagnostics nodes are protected from Browse and Value reads immediately after
  // instantiate() returns, before SessionDiagnosticsObject.startup() runs, and startup() must not
  // duplicate those filters.
  @Test
  void restrictedSecurityDiagnosticsNodesAreProtectedBeforeLifecycleStartupRuns() throws Exception {
    SessionSecurityDiagnosticsArrayTypeNode array =
        summaryNode.getSessionSecurityDiagnosticsArrayNode();
    var customRole = new NodeId(1, "PreStartupAuditor");
    var customPermission =
        new RolePermissionType(
            customRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));
    array.setRolePermissions(new RolePermissionType[] {customPermission});

    InstantiationRequest<SessionDiagnosticsObjectTypeNode> request =
        InstantiationRequest.of(
                SessionDiagnosticsObjectTypeNode.class, NodeIds.SessionDiagnosticsObjectType)
            .nodeId(new NodeId(1, UUID.randomUUID()))
            .browseName(new QualifiedName(1, "PreStartupSession"))
            .displayName(LocalizedText.english("PreStartupSession"))
            .parent(summaryNode.getNodeId(), NodeIds.HasComponent)
            .target(target)
            .legacyPathStrings()
            .onNode(SessionsDiagnosticsSummaryObject.securityDiagnosticsAccessControl(summaryNode))
            .build();

    // Instantiate ONLY; do not start SessionDiagnosticsObject yet.
    SessionDiagnosticsObjectTypeNode sdoNode =
        server.getNodeInstantiator().instantiate(request).root();
    SessionSecurityDiagnosticsTypeNode element = sdoNode.getSessionSecurityDiagnosticsNode();
    UaVariableNode clientCert = element.getClientCertificateNode();

    var addressSpace =
        new ManagedAddressSpaceFragment(server, target) {
          @Override
          public AddressSpaceFilter getFilter() {
            return SimpleAddressSpaceFilter.create(target::containsNode);
          }
        };
    server.getAddressSpaceManager().register(addressSpace);
    try {
      var controller = new DefaultAccessController(server);
      Session authenticated = sessionWithRoles(List.of(NodeIds.WellKnownRole_AuthenticatedUser));
      Session securityAdmin = sessionWithRoles(List.of(NodeIds.WellKnownRole_SecurityAdmin));
      Session auditor = sessionWithRoles(List.of(customRole));

      // Immediately after instantiate() — before startup() — mapped sessions excluded by the array
      // policy must be denied Browse and Value access on the element and its fields.
      for (UaVariableNode node : List.of(element, clientCert)) {
        String name = node.getBrowseName().name();
        NodeId nodeId = node.getNodeId();

        assertEquals(
            AccessResult.DENIED_USER_ACCESS,
            controller.checkBrowseAccess(authenticated, List.of(nodeId)).get(nodeId),
            name + " must deny Browse to AuthenticatedUser before startup()");
        assertEquals(
            AccessResult.DENIED_USER_ACCESS,
            controller.checkBrowseAccess(securityAdmin, List.of(nodeId)).get(nodeId),
            name + " must deny Browse to excluded SecurityAdmin before startup()");
        assertEquals(
            AccessResult.ALLOWED,
            controller.checkBrowseAccess(auditor, List.of(nodeId)).get(nodeId),
            name + " must allow Browse to authorized auditor before startup()");
        assertEquals(
            AccessLevel.toValue(AccessLevel.NONE),
            node.getFilterChain().readAttribute(securityAdmin, node, AttributeId.UserAccessLevel),
            name + " must deny Value access to excluded SecurityAdmin before startup()");
        assertEquals(
            AccessLevel.toValue(AccessLevel.READ_ONLY),
            node.getFilterChain().readAttribute(auditor, node, AttributeId.UserAccessLevel),
            name + " must allow Value access to authorized auditor before startup()");
      }

      // Also verify the default SecurityAdmin-only fallback works before startup() when the array
      // has no explicit RolePermissions.
      array.setRolePermissions(null);
      assertEquals(
          AccessResult.DENIED_USER_ACCESS,
          controller
              .checkBrowseAccess(authenticated, List.of(element.getNodeId()))
              .get(element.getNodeId()),
          "default fallback must deny Browse to AuthenticatedUser before startup()");
      assertEquals(
          AccessResult.ALLOWED,
          controller
              .checkBrowseAccess(securityAdmin, List.of(element.getNodeId()))
              .get(element.getNodeId()),
          "default fallback must allow Browse to SecurityAdmin before startup()");

      // Starting SessionDiagnosticsObject afterwards must not install duplicate fallback filters.
      SessionDiagnosticsObject sdo =
          new SessionDiagnosticsObject(sdoNode, session("PreStartupSession"), target);
      sdo.startup();
      try {
        for (UaVariableNode node : List.of(element, clientCert)) {
          long fallbackFilterCount =
              node.getFilterChain().getFilters().stream()
                  .filter(FallbackUserRolePermissionsFilter.class::isInstance)
                  .count();
          assertEquals(
              1L,
              fallbackFilterCount,
              node.getBrowseName().name()
                  + " must carry exactly one fallback filter after startup");
        }
      } finally {
        sdo.shutdown();
      }
    } finally {
      server.getAddressSpaceManager().unregister(addressSpace);
    }
  }

  // AttributeReader must honor session-scoped WriteMask filters (never falling back to unfiltered
  // node.getWriteMask()) and fail closed if the WriteMask read fails or returns a malformed type.
  @Test
  void attributeReaderRespectsSessionScopedWriteMaskForOmittedRolePermissions() throws Exception {
    SessionSecurityDiagnosticsArrayTypeNode array =
        summaryNode.getSessionSecurityDiagnosticsArrayNode();
    Session securityAdmin = sessionWithRoles(List.of(NodeIds.WellKnownRole_SecurityAdmin));
    Session unprivileged = sessionWithRoles(List.of(NodeIds.WellKnownRole_AuthenticatedUser));
    UInteger writableMask = UInteger.valueOf(WriteMask.RolePermissions.getValue());

    // Stored WriteMask has RolePermissions set, while the filter varies WriteMask per Session.
    array.setWriteMask(writableMask);
    var mode = new AtomicReference<>("clear-for-admin");
    array
        .getFilterChain()
        .addFirst(
            new AttributeFilter() {
              @Override
              public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
                  throws UaException {
                if (attributeId == AttributeId.WriteMask) {
                  Session s = ctx.getSession().orElse(null);
                  return switch (mode.get()) {
                    case "clear-for-admin" ->
                        s == securityAdmin ? UInteger.MIN : ctx.readAttribute(attributeId);
                    case "set-for-all" -> writableMask;
                    case "fail-denied" -> throw new UaException(StatusCodes.Bad_UserAccessDenied);
                    case "fail-internal" -> throw new UaException(StatusCodes.Bad_InternalError);
                    case "fail-attr-invalid" ->
                        throw new UaException(StatusCodes.Bad_AttributeIdInvalid);
                    case "return-null" -> null;
                    case "malformed" -> "not-a-mask";
                    case "clear-sessionless" ->
                        s == null ? UInteger.MIN : ctx.readAttribute(attributeId);
                    default -> ctx.readAttribute(attributeId);
                  };
                }
                return ctx.readAttribute(attributeId);
              }
            });

    // 1. Stored WriteMask retains RolePermissions, but session filter clears it for securityAdmin:
    // AttributeReader must return Bad_AttributeIdInvalid rather than promoting to an empty array.
    assertEquals(
        new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
        readAttribute(securityAdmin, array, AttributeId.RolePermissions).statusCode());
    // Meanwhile unprivileged still sees the writable bit and is denied by the fallback policy.
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(unprivileged, array, AttributeId.RolePermissions).statusCode());

    // 2. Stored WriteMask clears RolePermissions, while session filter sets it:
    // securityAdmin receives Good empty RolePermissionType[0] and unprivileged is denied.
    array.setWriteMask(UInteger.MIN);
    mode.set("set-for-all");
    DataValue adminEmpty = readAttribute(securityAdmin, array, AttributeId.RolePermissions);
    assertEquals(StatusCode.GOOD, adminEmpty.statusCode());
    assertArrayEquals(new RolePermissionType[0], (RolePermissionType[]) adminEmpty.value().value());
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(unprivileged, array, AttributeId.RolePermissions).statusCode());

    // 3. Failing or malformed session-scoped WriteMask reads fail closed (never Good empty array).
    array.setWriteMask(writableMask);
    mode.set("fail-denied");
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(securityAdmin, array, AttributeId.RolePermissions).statusCode());

    mode.set("fail-internal");
    assertEquals(
        new StatusCode(StatusCodes.Bad_InternalError),
        readAttribute(securityAdmin, array, AttributeId.RolePermissions).statusCode());

    mode.set("malformed");
    assertEquals(
        new StatusCode(StatusCodes.Bad_TypeMismatch),
        readAttribute(securityAdmin, array, AttributeId.RolePermissions).statusCode());

    // 4. WriteMask returning Bad_AttributeIdInvalid or raw null treats WriteMask as optional/absent
    // consistently for both direct WriteMask reads and RolePermissions reads.
    mode.set("fail-attr-invalid");
    assertEquals(
        new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
        readAttribute(securityAdmin, array, AttributeId.RolePermissions).statusCode());

    mode.set("return-null");
    assertEquals(
        new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
        readAttribute(securityAdmin, array, AttributeId.WriteMask).statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
        readAttribute(securityAdmin, array, AttributeId.RolePermissions).statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
        readAttribute(unprivileged, array, AttributeId.RolePermissions).statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
        readAttribute(null, array, AttributeId.RolePermissions).statusCode());

    // 5. Sessionless AttributeReader reads also honor the WriteMask filter chain instead of raw
    // node.getWriteMask().
    mode.set("clear-sessionless");
    assertEquals(
        new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
        readAttribute(null, array, AttributeId.RolePermissions).statusCode());

    array.setWriteMask(UInteger.MIN);
    mode.set("set-for-all");
    DataValue sessionlessEmpty = readAttribute(null, array, AttributeId.RolePermissions);
    assertEquals(StatusCode.GOOD, sessionlessEmpty.statusCode());
    assertArrayEquals(
        new RolePermissionType[0], (RolePermissionType[]) sessionlessEmpty.value().value());
  }

  private static Session sessionWithRoles(List<NodeId> roles) {
    var session = mock(Session.class);
    when(session.getRoleIds()).thenReturn(Optional.ofNullable(roles));
    when(session.getEndpoint())
        .thenReturn(
            new EndpointDescription(
                null, null, null, MessageSecurityMode.SignAndEncrypt, null, null, null, null));
    return session;
  }

  private static DataValue readAttribute(
      Session session, UaVariableNode node, AttributeId attributeId) {
    AccessContext context = () -> Optional.ofNullable(session);
    return AttributeReader.readAttribute(
        context, node, attributeId, TimestampsToReturn.Neither, null, null);
  }

  private SessionDiagnosticsObjectTypeNode singleSessionObject() {
    List<NodeId> objectIds =
        target.getReferences(summaryNode.getNodeId()).stream()
            .filter(r -> r.isForward() && r.getReferenceTypeId().equals(NodeIds.HasComponent))
            .map(r -> r.getTargetNodeId().toNodeId(server.getNamespaceTable()).orElseThrow())
            .toList();
    assertEquals(1, objectIds.size(), "exactly one Session Object");

    return (SessionDiagnosticsObjectTypeNode) target.getNode(objectIds.get(0)).orElseThrow();
  }

  private UaVariableNode singleElement(UaNode array) {
    List<NodeId> elementIds = elements(array);
    assertEquals(1, elementIds.size(), "exactly one element");

    return (UaVariableNode) target.getNode(elementIds.get(0)).orElseThrow();
  }

  /** The array's elements, gathered from every NodeManager as Browse does. */
  private List<NodeId> elements(UaNode array) {
    return server.getAddressSpaceManager().getManagedReferences(array.getNodeId()).stream()
        .filter(r -> r.isForward() && r.getReferenceTypeId().equals(NodeIds.HasComponent))
        .map(r -> r.getTargetNodeId().toNodeId(server.getNamespaceTable()).orElseThrow())
        .toList();
  }

  private static Session session(String name) {
    var subscriptionManager = mock(SubscriptionManager.class);
    when(subscriptionManager.getSubscriptions()).thenReturn(List.of());

    var session = mock(Session.class);
    when(session.getSessionId()).thenReturn(new NodeId(1, name));
    when(session.getSessionName()).thenReturn(name);
    when(session.getSubscriptionManager()).thenReturn(subscriptionManager);
    return session;
  }
}
