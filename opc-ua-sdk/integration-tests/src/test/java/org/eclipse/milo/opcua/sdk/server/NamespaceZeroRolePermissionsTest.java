/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.identity.IdentityProvider;
import org.eclipse.milo.opcua.sdk.client.identity.UsernameProvider;
import org.eclipse.milo.opcua.sdk.client.subscriptions.MonitoredItemSynchronizationException;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaMonitoredItem;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.core.WriteMask;
import org.eclipse.milo.opcua.sdk.server.identity.Identity;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.PropertyTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilter;
import org.eclipse.milo.opcua.sdk.server.nodes.filters.AttributeFilterContext;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.DefaultClientCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseResultMask;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathResult;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePath;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePathElement;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.WriteValue;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

/**
 * End-to-end verification that standard namespace-0 nodes omit default role permission metadata
 * (OPC 10000-3 §5.2.9, §5.2.10; OPC 10000-5 §6.3.13) while keeping restricted session security
 * diagnostics fail-closed and customizable via explicit {@code RolePermissions}.
 */
class NamespaceZeroRolePermissionsTest {

  private static final List<NodeId> STANDARD_NODES =
      List.of(
          NodeIds.Server,
          NodeIds.Server_ServerCapabilities,
          NodeIds.ObjectsFolder,
          NodeIds.Server_ServerDiagnostics_EnabledFlag,
          NodeIds
              .Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray);

  private final List<OpcUaClient> clients = new ArrayList<>();
  private OpcUaServer server;

  @AfterEach
  void tearDown() throws Exception {
    for (OpcUaClient client : clients) {
      try {
        client.disconnectAsync().get(5, TimeUnit.SECONDS);
      } catch (Exception ignored) {
        // Assertion failures should surface rather than cleanup errors.
      }
    }
    clients.clear();

    if (server != null) {
      server.shutdown().get(10, TimeUnit.SECONDS);
      server = null;
    }
  }

  // OPC 10000-3 §5.2.9 & §5.2.10: on a default server with no RoleMapper, standard namespace-0
  // nodes answer Bad_AttributeIdInvalid for both RolePermissions and UserRolePermissions,
  // OPCUANamespaceMetadata omits DefaultRolePermissions and DefaultUserRolePermissions, and
  // restricted security diagnostics stay fail-closed.
  @Test
  void defaultServerOmitsRolePermissionsAndKeepsSecurityDiagnosticsRestricted() throws Exception {
    TestServer testServer = TestServer.create();
    server = testServer.getServer();
    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient anonymous = connectSecure(testServer, AnonymousProvider.INSTANCE);

    assertRolePermissionsAbsent(anonymous, STANDARD_NODES);
    assertDefaultRolePermissionPropertiesRemoved(anonymous);

    DataValue securityArrayValue =
        readAttribute(
            anonymous,
            NodeIds
                .Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray,
            AttributeId.Value);
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        securityArrayValue.statusCode(),
        "anonymous client without RoleMapper cannot read SessionSecurityDiagnosticsArray");

    DataValue securityArrayUserAccess =
        readAttribute(
            anonymous,
            NodeIds
                .Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray,
            AttributeId.UserAccessLevel);
    assertEquals(StatusCode.GOOD, securityArrayUserAccess.statusCode());
    assertEquals(
        AccessLevel.toValue(AccessLevel.NONE), (UByte) securityArrayUserAccess.value().value());

    DataValue enabledFlagValue =
        readAttribute(anonymous, NodeIds.Server_ServerDiagnostics_EnabledFlag, AttributeId.Value);
    assertEquals(StatusCode.GOOD, enabledFlagValue.statusCode());
    assertEquals(Boolean.FALSE, enabledFlagValue.value().value());

    StatusCode writeStatus =
        writeValue(
            anonymous,
            NodeIds.Server_ServerDiagnostics_EnabledFlag,
            DataValue.valueOnly(new Variant(Boolean.TRUE)));
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        writeStatus,
        "anonymous client without RoleMapper cannot write EnabledFlag");
  }

  // OPC 10000-3 §5.2.9, §5.2.10 & Part 5 §6.3.5: with a RoleMapper configured, namespace-0 nodes
  // still omit RolePermissions and UserRolePermissions by default, while restricted diagnostics
  // enforce the fallback policies for EnabledFlag, SessionSecurityDiagnosticsArray, and dynamic
  // SessionSecurityDiagnostics elements.
  @Test
  void roleMapperEnforcesFallbackPoliciesWhileOmittingDefaultRolePermissions() throws Exception {
    RoleMapper roleMapper =
        identity -> {
          if (identity instanceof Identity.UsernameIdentity username) {
            return switch (username.getUsername()) {
              case "admin" -> List.of(NodeIds.WellKnownRole_SecurityAdmin);
              case "user2" -> List.of(NodeIds.WellKnownRole_ConfigureAdmin);
              default -> List.of(NodeIds.WellKnownRole_AuthenticatedUser);
            };
          }
          return List.of(NodeIds.WellKnownRole_Anonymous);
        };

    TestServer testServer = TestServer.create(cfg -> cfg.setRoleMapper(roleMapper));
    server = testServer.getServer();
    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient anonymous = connectSecure(testServer, AnonymousProvider.INSTANCE);
    OpcUaClient user1 = connectSecure(testServer, new UsernameProvider("user1", "password"));
    OpcUaClient configureAdmin =
        connectSecure(testServer, new UsernameProvider("user2", "password"));
    OpcUaClient securityAdmin =
        connectSecure(testServer, new UsernameProvider("admin", "password"));

    for (OpcUaClient client : List.of(anonymous, user1, configureAdmin, securityAdmin)) {
      assertRolePermissionsAbsent(client, STANDARD_NODES);
    }
    assertDefaultRolePermissionPropertiesRemoved(securityAdmin);

    // Non-admin sessions cannot enable diagnostics or read security diagnostics.
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        writeValue(
            user1,
            NodeIds.Server_ServerDiagnostics_EnabledFlag,
            DataValue.valueOnly(new Variant(Boolean.TRUE))),
        "AuthenticatedUser cannot write EnabledFlag");

    // ConfigureAdmin can enable diagnostics, but still cannot read security diagnostics.
    assertEquals(
        StatusCode.GOOD,
        writeValue(
            configureAdmin,
            NodeIds.Server_ServerDiagnostics_EnabledFlag,
            DataValue.valueOnly(new Variant(Boolean.TRUE))),
        "ConfigureAdmin can write EnabledFlag");

    NodeId securityArrayId =
        NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray;
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        user1.readValue(0.0, TimestampsToReturn.Neither, securityArrayId).statusCode(),
        "AuthenticatedUser cannot read SessionSecurityDiagnosticsArray");
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        configureAdmin.readValue(0.0, TimestampsToReturn.Neither, securityArrayId).statusCode(),
        "ConfigureAdmin cannot read SessionSecurityDiagnosticsArray");

    DataValue adminArrayValue =
        securityAdmin.readValue(0.0, TimestampsToReturn.Neither, securityArrayId);
    assertEquals(
        StatusCode.GOOD,
        adminArrayValue.statusCode(),
        "SecurityAdmin can read SessionSecurityDiagnosticsArray");
    assertNotNull(adminArrayValue.value().value());

    // Dynamic SessionSecurityDiagnostics elements and their child fields share the same rules:
    // omitted RolePermissions/UserRolePermissions, fail-closed for non-SecurityAdmin (including
    // Browse and TranslateBrowsePathsToNodeIds), and accessible to SecurityAdmin.
    List<String> adminSummaryChildren =
        browse(securityAdmin, NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary).stream()
            .map(r -> r.getBrowseName().name())
            .toList();
    assertTrue(
        adminSummaryChildren.contains("SessionSecurityDiagnosticsArray"),
        "SecurityAdmin discovers SessionSecurityDiagnosticsArray via Browse");

    List<ReferenceDescription> elements = browse(securityAdmin, securityArrayId);
    assertFalse(elements.isEmpty(), "enabled diagnostics publish dynamic Session elements");

    NodeId elementId =
        elements.get(0).getNodeId().toNodeId(securityAdmin.getNamespaceTable()).orElseThrow();
    NodeId clientCertId = childNodeId(securityAdmin, elementId, "ClientCertificate");

    NodeId sessionObjectId =
        browse(securityAdmin, NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary).stream()
            .filter(r -> r.getNodeClass() == NodeClass.Object)
            .findFirst()
            .flatMap(r -> r.getNodeId().toNodeId(securityAdmin.getNamespaceTable()))
            .orElseThrow();
    List<String> adminSessionChildren =
        browse(securityAdmin, sessionObjectId).stream().map(r -> r.getBrowseName().name()).toList();
    assertTrue(
        adminSessionChildren.contains("SessionSecurityDiagnostics"),
        "SecurityAdmin discovers SessionSecurityDiagnostics under Session Object");

    for (OpcUaClient nonAdmin : List.of(anonymous, user1, configureAdmin)) {
      List<String> summaryChildren =
          browse(nonAdmin, NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary).stream()
              .map(r -> r.getBrowseName().name())
              .toList();
      assertTrue(
          summaryChildren.contains("SessionDiagnosticsArray"),
          "ordinary SessionDiagnosticsArray remains browsable");
      assertFalse(
          summaryChildren.contains("SessionSecurityDiagnosticsArray"),
          "mapped non-admin must not discover SessionSecurityDiagnosticsArray via Browse");

      List<String> sessionChildren =
          browse(nonAdmin, sessionObjectId).stream().map(r -> r.getBrowseName().name()).toList();
      assertTrue(
          sessionChildren.contains("SessionDiagnostics"),
          "ordinary SessionDiagnostics remains browsable");
      assertFalse(
          sessionChildren.contains("SessionSecurityDiagnostics"),
          "mapped non-admin must not discover SessionSecurityDiagnostics via Browse");

      assertEquals(
          List.of(),
          browseAll(nonAdmin, securityArrayId),
          "direct Browse of SessionSecurityDiagnosticsArray denied for non-admin");
      assertEquals(
          List.of(),
          browseAll(nonAdmin, elementId),
          "direct Browse of dynamic SessionSecurityDiagnostics denied for non-admin");
      assertEquals(
          List.of(),
          browseAll(nonAdmin, clientCertId),
          "direct Browse of dynamic SessionSecurityDiagnostics field denied for non-admin");
    }

    assertEquals(
        new StatusCode(StatusCodes.Bad_NoMatch),
        translatePath(
                user1,
                NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary,
                "SessionSecurityDiagnosticsArray")
            .getStatusCode(),
        "TranslateBrowsePathsToNodeIds to SessionSecurityDiagnosticsArray denied for non-admin");
    assertEquals(
        new StatusCode(StatusCodes.Bad_NoMatch),
        translatePath(user1, sessionObjectId, "SessionSecurityDiagnostics").getStatusCode(),
        "TranslateBrowsePathsToNodeIds to SessionSecurityDiagnostics denied for non-admin");
    assertEquals(
        StatusCode.GOOD,
        translatePath(
                securityAdmin,
                NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary,
                "SessionSecurityDiagnosticsArray")
            .getStatusCode(),
        "TranslateBrowsePathsToNodeIds to SessionSecurityDiagnosticsArray succeeds for"
            + " SecurityAdmin");
    assertEquals(
        StatusCode.GOOD,
        translatePath(securityAdmin, sessionObjectId, "SessionSecurityDiagnostics").getStatusCode(),
        "TranslateBrowsePathsToNodeIds to SessionSecurityDiagnostics succeeds for SecurityAdmin");

    for (NodeId dynamicNodeId : List.of(elementId, clientCertId)) {
      assertRolePermissionsAbsent(user1, List.of(dynamicNodeId));
      assertRolePermissionsAbsent(securityAdmin, List.of(dynamicNodeId));

      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          user1.readValue(0.0, TimestampsToReturn.Neither, dynamicNodeId).statusCode(),
          "AuthenticatedUser cannot read dynamic security diagnostics node " + dynamicNodeId);
      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          configureAdmin.readValue(0.0, TimestampsToReturn.Neither, dynamicNodeId).statusCode(),
          "ConfigureAdmin cannot read dynamic security diagnostics node " + dynamicNodeId);
      assertEquals(
          StatusCode.GOOD,
          securityAdmin.readValue(0.0, TimestampsToReturn.Neither, dynamicNodeId).statusCode(),
          "SecurityAdmin can read dynamic security diagnostics node " + dynamicNodeId);
    }
  }

  // Applications that explicitly configure RolePermissions on SessionSecurityDiagnosticsArray and
  // EnabledFlag override the default fallback policies, propagate custom RolePermissions to dynamic
  // elements, and expose symmetric UserRolePermissions enforced by DefaultAccessController.
  @Test
  void explicitRolePermissionsOverridePoliciesAndExposeSymmetricUserRolePermissions()
      throws Exception {

    var customRole = new NodeId(1, "SecurityDiagnosticsAuditor");
    var customRolePermission =
        new RolePermissionType(
            customRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.Write,
                PermissionType.Field.ReadRolePermissions));
    var anonymousReadOnly =
        new RolePermissionType(
            NodeIds.WellKnownRole_Anonymous,
            PermissionType.of(PermissionType.Field.Browse, PermissionType.Field.Read));

    RoleMapper roleMapper =
        identity -> {
          if (identity instanceof Identity.UsernameIdentity username) {
            return switch (username.getUsername()) {
              case "user1" -> List.of(customRole);
              case "admin" -> List.of(NodeIds.WellKnownRole_SecurityAdmin);
              default -> List.of(NodeIds.WellKnownRole_AuthenticatedUser);
            };
          }
          return List.of(NodeIds.WellKnownRole_Anonymous);
        };

    TestServer testServer = TestServer.create(cfg -> cfg.setRoleMapper(roleMapper));
    server = testServer.getServer();

    ServerDiagnosticsTypeNode diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();
    PropertyTypeNode enabledFlagNode = diagnosticsNode.getEnabledFlagNode();
    SessionSecurityDiagnosticsArrayTypeNode securityArrayNode =
        diagnosticsNode
            .getSessionsDiagnosticsSummaryNode()
            .getSessionSecurityDiagnosticsArrayNode();

    var enabledFlagPolicy = new RolePermissionType[] {anonymousReadOnly, customRolePermission};
    var securityArrayPolicy = new RolePermissionType[] {customRolePermission};
    enabledFlagNode.setRolePermissions(enabledFlagPolicy);
    securityArrayNode.setRolePermissions(securityArrayPolicy);

    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient auditor = connectSecure(testServer, new UsernameProvider("user1", "password"));
    OpcUaClient securityAdmin =
        connectSecure(testServer, new UsernameProvider("admin", "password"));

    // Custom role takes over EnabledFlag writes; SecurityAdmin is now denied.
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        writeValue(
            securityAdmin,
            NodeIds.Server_ServerDiagnostics_EnabledFlag,
            DataValue.valueOnly(new Variant(Boolean.TRUE))),
        "explicit EnabledFlag policy overrides SecurityAdmin write access");
    assertEquals(
        StatusCode.GOOD,
        writeValue(
            auditor,
            NodeIds.Server_ServerDiagnostics_EnabledFlag,
            DataValue.valueOnly(new Variant(Boolean.TRUE))),
        "custom role can write EnabledFlag");

    // EnabledFlag exposes symmetric RolePermissions (gated by ReadRolePermissions) and filtered
    // UserRolePermissions. Even though the RoleMapper maps admin solely to SecurityAdmin without
    // listing Anonymous, OPC 10000-3 §4.9.1 assigns Anonymous to every Session so advertised and
    // enforced Browse permissions on EnabledFlag must agree.
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(
                securityAdmin,
                NodeIds.Server_ServerDiagnostics_EnabledFlag,
                AttributeId.RolePermissions)
            .statusCode(),
        "DefaultAccessController denies RolePermissions read without ReadRolePermissions");
    assertArrayEquals(
        new RolePermissionType[] {anonymousReadOnly},
        readDecodedRolePermissions(
            securityAdmin,
            NodeIds.Server_ServerDiagnostics_EnabledFlag,
            AttributeId.UserRolePermissions));
    assertTrue(
        browse(securityAdmin, NodeIds.Server_ServerDiagnostics).stream()
            .anyMatch(r -> "EnabledFlag".equals(r.getBrowseName().name())),
        "SecurityAdmin discovers EnabledFlag via Browse when Anonymous grants Browse");
    assertFalse(
        browseAll(securityAdmin, NodeIds.Server_ServerDiagnostics_EnabledFlag).isEmpty(),
        "SecurityAdmin can directly Browse EnabledFlag when Anonymous grants Browse");
    assertEquals(
        StatusCode.GOOD,
        translatePath(securityAdmin, NodeIds.Server_ServerDiagnostics, "EnabledFlag")
            .getStatusCode(),
        "SecurityAdmin can translate path to EnabledFlag when Anonymous grants Browse");
    assertArrayEquals(
        enabledFlagPolicy,
        readDecodedRolePermissions(
            auditor, NodeIds.Server_ServerDiagnostics_EnabledFlag, AttributeId.RolePermissions));
    assertArrayEquals(
        enabledFlagPolicy,
        readDecodedRolePermissions(
            auditor,
            NodeIds.Server_ServerDiagnostics_EnabledFlag,
            AttributeId.UserRolePermissions));

    // Custom role takes over SessionSecurityDiagnosticsArray and its dynamic elements.
    NodeId securityArrayId =
        NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray;
    List<ReferenceDescription> elements = browse(auditor, securityArrayId);
    assertFalse(elements.isEmpty(), "custom role can browse dynamic security elements");
    NodeId elementId =
        elements.get(0).getNodeId().toNodeId(auditor.getNamespaceTable()).orElseThrow();

    assertFalse(
        browse(securityAdmin, NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary).stream()
            .anyMatch(r -> "SessionSecurityDiagnosticsArray".equals(r.getBrowseName().name())),
        "SecurityAdmin cannot browse SessionSecurityDiagnosticsArray once overridden");
    assertEquals(
        List.of(),
        browseAll(securityAdmin, securityArrayId),
        "SecurityAdmin direct Browse of SessionSecurityDiagnosticsArray denied once overridden");

    for (NodeId securityNodeId : List.of(securityArrayId, elementId)) {
      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          securityAdmin.readValue(0.0, TimestampsToReturn.Neither, securityNodeId).statusCode(),
          "SecurityAdmin denied Value read when overridden by custom policy");
      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          readAttribute(securityAdmin, securityNodeId, AttributeId.RolePermissions).statusCode(),
          "SecurityAdmin denied RolePermissions read without ReadRolePermissions");
      assertArrayEquals(
          new RolePermissionType[0],
          readDecodedRolePermissions(
              securityAdmin, securityNodeId, AttributeId.UserRolePermissions));

      assertEquals(
          StatusCode.GOOD,
          auditor.readValue(0.0, TimestampsToReturn.Neither, securityNodeId).statusCode(),
          "custom role can read security diagnostics Value");
      if (securityArrayId.equals(securityNodeId)) {
        assertArrayEquals(
            securityArrayPolicy,
            readDecodedRolePermissions(auditor, securityNodeId, AttributeId.RolePermissions));
      } else {
        assertEquals(
            new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
            readAttribute(auditor, securityNodeId, AttributeId.RolePermissions).statusCode(),
            "non-writable dynamic element without local override omits RolePermissions");
      }
      assertArrayEquals(
          securityArrayPolicy,
          readDecodedRolePermissions(auditor, securityNodeId, AttributeId.UserRolePermissions));
    }
  }

  // OPC 10000-3 §4.9.1 & §5.2.9: even when no RoleMapper is configured, an explicit RolePermissions
  // override on EnabledFlag or SessionSecurityDiagnosticsArray must not disclose RolePermissions to
  // an anonymous session unless Anonymous holds ReadRolePermissions, and clearing RolePermissions
  // restores Bad_AttributeIdInvalid for both attributes.
  @Test
  void explicitDiagnosticsRolePermissionsEnforceReadRolePermissionsWithoutRoleMapper()
      throws Exception {

    var customRole = new NodeId(1, "DiagnosticsOwner");
    var customRolePermission =
        new RolePermissionType(
            customRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.Write,
                PermissionType.Field.ReadRolePermissions));
    var anonymousReadOnly =
        new RolePermissionType(
            NodeIds.WellKnownRole_Anonymous,
            PermissionType.of(PermissionType.Field.Browse, PermissionType.Field.Read));

    TestServer testServer = TestServer.create();
    server = testServer.getServer();

    ServerDiagnosticsTypeNode diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();
    PropertyTypeNode enabledFlagNode = diagnosticsNode.getEnabledFlagNode();
    SessionSecurityDiagnosticsArrayTypeNode securityArrayNode =
        diagnosticsNode
            .getSessionsDiagnosticsSummaryNode()
            .getSessionSecurityDiagnosticsArrayNode();

    enabledFlagNode.setRolePermissions(
        new RolePermissionType[] {anonymousReadOnly, customRolePermission});
    securityArrayNode.setRolePermissions(new RolePermissionType[] {customRolePermission});

    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient anonymous = connectSecure(testServer, AnonymousProvider.INSTANCE);
    NodeId enabledFlagId = NodeIds.Server_ServerDiagnostics_EnabledFlag;
    NodeId securityArrayId =
        NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray;

    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(anonymous, enabledFlagId, AttributeId.RolePermissions).statusCode(),
        "anonymous without RoleMapper must not read EnabledFlag RolePermissions without"
            + " ReadRolePermissions");
    assertArrayEquals(
        new RolePermissionType[] {anonymousReadOnly},
        readDecodedRolePermissions(anonymous, enabledFlagId, AttributeId.UserRolePermissions));

    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(anonymous, securityArrayId, AttributeId.RolePermissions).statusCode(),
        "anonymous without RoleMapper must not read SessionSecurityDiagnosticsArray"
            + " RolePermissions without ReadRolePermissions");
    assertArrayEquals(
        new RolePermissionType[0],
        readDecodedRolePermissions(anonymous, securityArrayId, AttributeId.UserRolePermissions));

    // Granting ReadRolePermissions to Anonymous allows reading the full explicit policy.
    var anonymousCanReadPolicy =
        new RolePermissionType(
            NodeIds.WellKnownRole_Anonymous,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));
    var readablePolicy = new RolePermissionType[] {anonymousCanReadPolicy, customRolePermission};
    enabledFlagNode.setRolePermissions(readablePolicy);

    assertArrayEquals(
        readablePolicy,
        readDecodedRolePermissions(anonymous, enabledFlagId, AttributeId.RolePermissions));

    // Clearing RolePermissions restores Bad_AttributeIdInvalid for both attributes.
    enabledFlagNode.setRolePermissions(null);
    assertRolePermissionsAbsent(anonymous, List.of(enabledFlagId));
  }

  // An application AttributeFilter added at the front of the chain supplying a restrictive
  // RolePermissions override while the stored attribute stays null must protect Browse, Value
  // reads, and RolePermissions reads consistently.
  @Test
  void frontOfChainRolePermissionsFilterEnforcesBrowseAndReadProtection() throws Exception {
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

    RoleMapper roleMapper =
        identity -> {
          if (identity instanceof Identity.UsernameIdentity username) {
            return switch (username.getUsername()) {
              case "user1" -> List.of(customRole);
              case "admin" -> List.of(NodeIds.WellKnownRole_SecurityAdmin);
              default -> List.of(NodeIds.WellKnownRole_AuthenticatedUser);
            };
          }
          return List.of(NodeIds.WellKnownRole_Anonymous);
        };

    TestServer testServer = TestServer.create(cfg -> cfg.setRoleMapper(roleMapper));
    server = testServer.getServer();

    ServerDiagnosticsTypeNode diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();
    SessionSecurityDiagnosticsArrayTypeNode securityArrayNode =
        diagnosticsNode
            .getSessionsDiagnosticsSummaryNode()
            .getSessionSecurityDiagnosticsArrayNode();

    securityArrayNode
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

    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient auditor = connectSecure(testServer, new UsernameProvider("user1", "password"));
    OpcUaClient securityAdmin =
        connectSecure(testServer, new UsernameProvider("admin", "password"));

    // Enable diagnostics so Value reads return GOOD rather than Bad_OutOfService.
    assertEquals(
        StatusCode.GOOD,
        writeValue(
            securityAdmin,
            NodeIds.Server_ServerDiagnostics_EnabledFlag,
            DataValue.valueOnly(new Variant(Boolean.TRUE))));

    NodeId securityArrayId =
        NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray;

    // SecurityAdmin is denied Browse, Value read, and RolePermissions read under the front filter.
    assertFalse(
        browse(securityAdmin, NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary).stream()
            .anyMatch(r -> "SessionSecurityDiagnosticsArray".equals(r.getBrowseName().name())),
        "SecurityAdmin must not discover SessionSecurityDiagnosticsArray under front filter");
    assertEquals(List.of(), browseAll(securityAdmin, securityArrayId));
    assertEquals(
        new StatusCode(StatusCodes.Bad_NoMatch),
        translatePath(
                securityAdmin,
                NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary,
                "SessionSecurityDiagnosticsArray")
            .getStatusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        securityAdmin.readValue(0.0, TimestampsToReturn.Neither, securityArrayId).statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(securityAdmin, securityArrayId, AttributeId.RolePermissions).statusCode());
    assertArrayEquals(
        new RolePermissionType[0],
        readDecodedRolePermissions(
            securityAdmin, securityArrayId, AttributeId.UserRolePermissions));

    // The custom role granted by the front filter can Browse and read all three attributes.
    assertTrue(
        browse(auditor, NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary).stream()
            .anyMatch(r -> "SessionSecurityDiagnosticsArray".equals(r.getBrowseName().name())));
    assertEquals(
        StatusCode.GOOD,
        auditor.readValue(0.0, TimestampsToReturn.Neither, securityArrayId).statusCode());
    assertArrayEquals(
        customPolicy,
        readDecodedRolePermissions(auditor, securityArrayId, AttributeId.RolePermissions));
    assertArrayEquals(
        customPolicy,
        readDecodedRolePermissions(auditor, securityArrayId, AttributeId.UserRolePermissions));
  }

  // Malformed entries inside an explicit RolePermissionType[] (e.g. null permissions, null mask
  // value, or null roleId) must be stripped before reaching DefaultAccessController or binary
  // encoding, including on authorized RolePermissions reads.
  @Test
  void malformedExplicitRolePermissionEntryDoesNotBreakServiceRequests() throws Exception {
    var brokenRole = new NodeId(1, "BrokenRole");
    var validRole = new NodeId(1, "ValidPolicyReader");
    var validEntry =
        new RolePermissionType(
            validRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));

    RoleMapper roleMapper =
        identity -> {
          if (identity instanceof Identity.UsernameIdentity username) {
            return switch (username.getUsername()) {
              case "user1" -> List.of(brokenRole);
              case "admin" -> List.of(validRole);
              default -> List.of(NodeIds.WellKnownRole_AuthenticatedUser);
            };
          }
          return List.of(NodeIds.WellKnownRole_Anonymous);
        };

    TestServer testServer = TestServer.create(cfg -> cfg.setRoleMapper(roleMapper));
    server = testServer.getServer();

    ServerDiagnosticsTypeNode diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();
    SessionSecurityDiagnosticsArrayTypeNode securityArrayNode =
        diagnosticsNode
            .getSessionsDiagnosticsSummaryNode()
            .getSessionSecurityDiagnosticsArrayNode();
    securityArrayNode.setRolePermissions(
        new RolePermissionType[] {
          validEntry,
          new RolePermissionType(brokenRole, null),
          new RolePermissionType(brokenRole, new PermissionType(null)),
          new RolePermissionType(null, PermissionType.of(PermissionType.Field.Read)),
          null
        });

    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient brokenClient = connectSecure(testServer, new UsernameProvider("user1", "password"));
    OpcUaClient validClient = connectSecure(testServer, new UsernameProvider("admin", "password"));
    NodeId securityArrayId =
        NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray;

    assertArrayEquals(
        new RolePermissionType[0],
        readDecodedRolePermissions(brokenClient, securityArrayId, AttributeId.UserRolePermissions));
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(brokenClient, securityArrayId, AttributeId.RolePermissions).statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        brokenClient.readValue(0.0, TimestampsToReturn.Neither, securityArrayId).statusCode());
    assertEquals(List.of(), browseAll(brokenClient, securityArrayId));

    // Authorized Read(RolePermissions) succeeds over the wire with malformed entries stripped so
    // binary encoding does not dereference null fields.
    assertArrayEquals(
        new RolePermissionType[] {validEntry},
        readDecodedRolePermissions(validClient, securityArrayId, AttributeId.RolePermissions));
    assertArrayEquals(
        new RolePermissionType[] {validEntry},
        readDecodedRolePermissions(validClient, securityArrayId, AttributeId.UserRolePermissions));
  }

  // A readAttribute-only addFirst front filter supplying RolePermissions (including malformed
  // sibling entries) while stored RolePermissions stays null must enforce ReadRolePermissions for
  // unauthorized mapped Sessions and sanitize malformed siblings before binary encoding for
  // authorized readers.
  @Test
  void readAttributeOnlyFrontFilterEnforcesAuthorizationAndSanitizesMalformedSiblingsOverWire()
      throws Exception {
    var customRole = new NodeId(1, "ReadAttrFrontAuditor");
    var brokenRole = new NodeId(1, "ReadAttrBrokenRole");
    var validEntry =
        new RolePermissionType(
            customRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));
    var mixedFrontPolicy =
        new RolePermissionType[] {
          validEntry,
          new RolePermissionType(brokenRole, null),
          new RolePermissionType(brokenRole, new PermissionType(null)),
          new RolePermissionType(null, PermissionType.of(PermissionType.Field.Read)),
          null
        };

    RoleMapper roleMapper =
        identity -> {
          if (identity instanceof Identity.UsernameIdentity username) {
            return switch (username.getUsername()) {
              case "user1" -> List.of(customRole);
              case "user2" -> List.of(brokenRole);
              case "admin" -> List.of(NodeIds.WellKnownRole_SecurityAdmin);
              default -> List.of(NodeIds.WellKnownRole_AuthenticatedUser);
            };
          }
          return List.of(NodeIds.WellKnownRole_Anonymous);
        };

    TestServer testServer = TestServer.create(cfg -> cfg.setRoleMapper(roleMapper));
    server = testServer.getServer();

    ServerDiagnosticsTypeNode diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();
    SessionSecurityDiagnosticsArrayTypeNode securityArrayNode =
        diagnosticsNode
            .getSessionsDiagnosticsSummaryNode()
            .getSessionSecurityDiagnosticsArrayNode();

    securityArrayNode
        .getFilterChain()
        .addFirst(
            new AttributeFilter() {
              @Override
              public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
                  throws UaException {
                if (attributeId == AttributeId.RolePermissions) {
                  return mixedFrontPolicy;
                } else {
                  return ctx.readAttribute(attributeId);
                }
              }
            });

    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient auditor = connectSecure(testServer, new UsernameProvider("user1", "password"));
    OpcUaClient brokenClient = connectSecure(testServer, new UsernameProvider("user2", "password"));
    OpcUaClient securityAdmin =
        connectSecure(testServer, new UsernameProvider("admin", "password"));

    assertEquals(
        StatusCode.GOOD,
        writeValue(
            securityAdmin,
            NodeIds.Server_ServerDiagnostics_EnabledFlag,
            DataValue.valueOnly(new Variant(Boolean.TRUE))));

    NodeId securityArrayId =
        NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray;

    // Unauthorized mapped Sessions cannot read RolePermissions, read Value, or Browse.
    for (OpcUaClient denied : List.of(securityAdmin, brokenClient)) {
      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          readAttribute(denied, securityArrayId, AttributeId.RolePermissions).statusCode());
      assertArrayEquals(
          new RolePermissionType[0],
          readDecodedRolePermissions(denied, securityArrayId, AttributeId.UserRolePermissions));
      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          denied.readValue(0.0, TimestampsToReturn.Neither, securityArrayId).statusCode());
      assertEquals(List.of(), browseAll(denied, securityArrayId));
    }

    // Authorized Session succeeds over the wire with malformed sibling entries stripped before
    // binary encoding.
    assertArrayEquals(
        new RolePermissionType[] {validEntry},
        readDecodedRolePermissions(auditor, securityArrayId, AttributeId.RolePermissions));
    assertArrayEquals(
        new RolePermissionType[] {validEntry},
        readDecodedRolePermissions(auditor, securityArrayId, AttributeId.UserRolePermissions));
    assertEquals(
        StatusCode.GOOD,
        auditor.readValue(0.0, TimestampsToReturn.Neither, securityArrayId).statusCode());
    assertTrue(
        browse(auditor, NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary).stream()
            .anyMatch(r -> "SessionSecurityDiagnosticsArray".equals(r.getBrowseName().name())));

    // Dynamic SessionSecurityDiagnostics elements and their child fields dynamically inherit the
    // array's readAttribute-only front filter policy rather than falling back to SecurityAdmin.
    List<ReferenceDescription> elements = browse(auditor, securityArrayId);
    assertFalse(elements.isEmpty(), "custom auditor discovers dynamic security elements");
    NodeId elementId =
        elements.get(0).getNodeId().toNodeId(auditor.getNamespaceTable()).orElseThrow();
    NodeId clientCertId = childNodeId(auditor, elementId, "ClientCertificate");

    for (NodeId dynamicNodeId : List.of(elementId, clientCertId)) {
      for (OpcUaClient denied : List.of(securityAdmin, brokenClient)) {
        assertEquals(
            new StatusCode(StatusCodes.Bad_UserAccessDenied),
            readAttribute(denied, dynamicNodeId, AttributeId.RolePermissions).statusCode());
        assertArrayEquals(
            new RolePermissionType[0],
            readDecodedRolePermissions(denied, dynamicNodeId, AttributeId.UserRolePermissions));
        assertEquals(
            new StatusCode(StatusCodes.Bad_UserAccessDenied),
            denied.readValue(0.0, TimestampsToReturn.Neither, dynamicNodeId).statusCode());
        assertEquals(List.of(), browseAll(denied, dynamicNodeId));
      }

      // Non-writable dynamic elements without a local RolePermissions override omit
      // RolePermissions (OPC 10000-3 §5.2.9) while continuing to enforce and expose the inherited
      // array policy via UserRolePermissions, Value, and Browse.
      assertEquals(
          new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
          readAttribute(auditor, dynamicNodeId, AttributeId.RolePermissions).statusCode());
      assertArrayEquals(
          new RolePermissionType[] {validEntry},
          readDecodedRolePermissions(auditor, dynamicNodeId, AttributeId.UserRolePermissions));
      assertEquals(
          StatusCode.GOOD,
          auditor.readValue(0.0, TimestampsToReturn.Neither, dynamicNodeId).statusCode());
    }
  }

  // Even without a RoleMapper configured, a readAttribute-only addFirst front filter supplying
  // RolePermissions on a diagnostics node must deny anonymous reads lacking ReadRolePermissions and
  // sanitize malformed siblings once Anonymous is granted ReadRolePermissions.
  @Test
  void readAttributeOnlyFrontFilterEnforcesReadRolePermissionsWithoutRoleMapper() throws Exception {
    var customRole = new NodeId(1, "UnmappedFrontOwner");
    var customPermission =
        new RolePermissionType(
            customRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.Write,
                PermissionType.Field.ReadRolePermissions));
    var anonymousReadOnly =
        new RolePermissionType(
            NodeIds.WellKnownRole_Anonymous,
            PermissionType.of(PermissionType.Field.Browse, PermissionType.Field.Read));
    var anonymousCanReadPolicy =
        new RolePermissionType(
            NodeIds.WellKnownRole_Anonymous,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));

    var activePolicy =
        new AtomicReference<>(new RolePermissionType[] {anonymousReadOnly, customPermission});

    TestServer testServer = TestServer.create();
    server = testServer.getServer();

    ServerDiagnosticsTypeNode diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();
    PropertyTypeNode enabledFlagNode = diagnosticsNode.getEnabledFlagNode();

    enabledFlagNode
        .getFilterChain()
        .addFirst(
            new AttributeFilter() {
              @Override
              public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
                  throws UaException {
                if (attributeId == AttributeId.RolePermissions) {
                  return activePolicy.get();
                } else {
                  return ctx.readAttribute(attributeId);
                }
              }
            });

    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient anonymous = connectSecure(testServer, AnonymousProvider.INSTANCE);
    NodeId enabledFlagId = NodeIds.Server_ServerDiagnostics_EnabledFlag;

    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(anonymous, enabledFlagId, AttributeId.RolePermissions).statusCode(),
        "unmapped Session must not read RolePermissions from readAttribute-only front filter"
            + " without Anonymous.ReadRolePermissions");
    assertArrayEquals(
        new RolePermissionType[] {anonymousReadOnly},
        readDecodedRolePermissions(anonymous, enabledFlagId, AttributeId.UserRolePermissions));

    // Once Anonymous grants ReadRolePermissions, wire response succeeds and strips malformed
    // siblings returned by the front filter.
    activePolicy.set(
        new RolePermissionType[] {
          anonymousCanReadPolicy, customPermission, new RolePermissionType(customRole, null), null
        });
    assertArrayEquals(
        new RolePermissionType[] {anonymousCanReadPolicy, customPermission},
        readDecodedRolePermissions(anonymous, enabledFlagId, AttributeId.RolePermissions));
  }

  // Tightening or revoking stored RolePermissions on SessionSecurityDiagnosticsArray after a
  // Session element has been created must immediately deny the revoked auditor on the exact same
  // already-created dynamic element and ClientCertificate node IDs over the wire.
  @Test
  void revokingStoredSecurityArrayRolePermissionsImmediatelyDeniesExistingElementsOverWire()
      throws Exception {
    var auditorRole = new NodeId(1, "WireRevocableAuditor");
    var auditorPermission =
        new RolePermissionType(
            auditorRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.Write,
                PermissionType.Field.ReadRolePermissions));
    var securityAdminPermission =
        new RolePermissionType(
            NodeIds.WellKnownRole_SecurityAdmin,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));

    RoleMapper roleMapper =
        identity -> {
          if (identity instanceof Identity.UsernameIdentity username) {
            return switch (username.getUsername()) {
              case "user1" -> List.of(auditorRole);
              case "admin" -> List.of(NodeIds.WellKnownRole_SecurityAdmin);
              default -> List.of(NodeIds.WellKnownRole_AuthenticatedUser);
            };
          }
          return List.of(NodeIds.WellKnownRole_Anonymous);
        };

    TestServer testServer = TestServer.create(cfg -> cfg.setRoleMapper(roleMapper));
    server = testServer.getServer();

    ServerDiagnosticsTypeNode diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();
    SessionSecurityDiagnosticsArrayTypeNode securityArrayNode =
        diagnosticsNode
            .getSessionsDiagnosticsSummaryNode()
            .getSessionSecurityDiagnosticsArrayNode();

    var initialPolicy = new RolePermissionType[] {auditorPermission};
    securityArrayNode.setRolePermissions(initialPolicy);

    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient auditor = connectSecure(testServer, new UsernameProvider("user1", "password"));
    OpcUaClient securityAdmin =
        connectSecure(testServer, new UsernameProvider("admin", "password"));

    assertEquals(
        StatusCode.GOOD,
        writeValue(
            securityAdmin,
            NodeIds.Server_ServerDiagnostics_EnabledFlag,
            DataValue.valueOnly(new Variant(Boolean.TRUE))));

    NodeId securityArrayId =
        NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray;
    List<ReferenceDescription> elements = browse(auditor, securityArrayId);
    assertFalse(
        elements.isEmpty(), "auditor discovers dynamic security elements before revocation");
    NodeId elementId =
        elements.get(0).getNodeId().toNodeId(auditor.getNamespaceTable()).orElseThrow();
    NodeId clientCertId = childNodeId(auditor, elementId, "ClientCertificate");

    // Before revocation, auditor can read Value and inherited UserRolePermissions on both, while
    // non-writable dynamic nodes without local overrides omit RolePermissions (OPC 10000-3 §5.2.9).
    for (NodeId dynamicNodeId : List.of(elementId, clientCertId)) {
      assertEquals(
          StatusCode.GOOD,
          auditor.readValue(0.0, TimestampsToReturn.Neither, dynamicNodeId).statusCode());
      assertEquals(
          new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
          readAttribute(auditor, dynamicNodeId, AttributeId.RolePermissions).statusCode());
      assertArrayEquals(
          initialPolicy,
          readDecodedRolePermissions(auditor, dynamicNodeId, AttributeId.UserRolePermissions));
    }

    // Revoke auditor on the security array without recreating the Session or its diagnostics nodes.
    var revokedPolicy = new RolePermissionType[] {securityAdminPermission};
    securityArrayNode.setRolePermissions(revokedPolicy);

    for (NodeId dynamicNodeId : List.of(elementId, clientCertId)) {
      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          auditor.readValue(0.0, TimestampsToReturn.Neither, dynamicNodeId).statusCode(),
          "revoked auditor must immediately lose Value read on existing " + dynamicNodeId);
      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          readAttribute(auditor, dynamicNodeId, AttributeId.RolePermissions).statusCode(),
          "revoked auditor must immediately lose RolePermissions read on existing "
              + dynamicNodeId);
      assertArrayEquals(
          new RolePermissionType[0],
          readDecodedRolePermissions(auditor, dynamicNodeId, AttributeId.UserRolePermissions));
      assertEquals(List.of(), browseAll(auditor, dynamicNodeId));

      assertEquals(
          StatusCode.GOOD,
          securityAdmin.readValue(0.0, TimestampsToReturn.Neither, dynamicNodeId).statusCode());
      assertEquals(
          new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
          readAttribute(securityAdmin, dynamicNodeId, AttributeId.RolePermissions).statusCode());
      assertArrayEquals(
          revokedPolicy,
          readDecodedRolePermissions(
              securityAdmin, dynamicNodeId, AttributeId.UserRolePermissions));
    }

    // Clearing the array's explicit RolePermissions restores Bad_AttributeIdInvalid and the
    // default SecurityAdmin-only fallback on the exact same existing nodes.
    securityArrayNode.setRolePermissions(null);
    for (NodeId dynamicNodeId : List.of(elementId, clientCertId)) {
      assertRolePermissionsAbsent(auditor, List.of(dynamicNodeId));
      assertRolePermissionsAbsent(securityAdmin, List.of(dynamicNodeId));
      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          auditor.readValue(0.0, TimestampsToReturn.Neither, dynamicNodeId).statusCode());
      assertEquals(
          StatusCode.GOOD,
          securityAdmin.readValue(0.0, TimestampsToReturn.Neither, dynamicNodeId).statusCode());
    }
  }

  // An addFirst front filter that delegates to ctx.readAttribute(RolePermissions) and appends a
  // ReadRolePermissions grant to an existing non-null policy must succeed over the wire and return
  // the augmented policy, rather than failing inside Util's inner diagnostics filter.
  @Test
  void delegatingFrontFilterAppendingReadRolePermissionsSucceedsOverWire() throws Exception {
    var existingRole = new NodeId(1, "BasePolicyRole");
    var appendedRole = new NodeId(1, "AppendedPolicyReader");
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

    RoleMapper roleMapper =
        identity -> {
          if (identity instanceof Identity.UsernameIdentity username) {
            return switch (username.getUsername()) {
              case "user1" -> List.of(appendedRole);
              case "user2" -> List.of(existingRole);
              default -> List.of(NodeIds.WellKnownRole_AuthenticatedUser);
            };
          }
          return List.of(NodeIds.WellKnownRole_Anonymous);
        };

    TestServer testServer = TestServer.create(cfg -> cfg.setRoleMapper(roleMapper));
    server = testServer.getServer();

    ServerDiagnosticsTypeNode diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();
    SessionSecurityDiagnosticsArrayTypeNode securityArrayNode =
        diagnosticsNode
            .getSessionsDiagnosticsSummaryNode()
            .getSessionSecurityDiagnosticsArrayNode();

    securityArrayNode.setRolePermissions(new RolePermissionType[] {existingEntry});
    securityArrayNode
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

    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient appendedClient =
        connectSecure(testServer, new UsernameProvider("user1", "password"));
    OpcUaClient existingClient =
        connectSecure(testServer, new UsernameProvider("user2", "password"));
    NodeId securityArrayId =
        NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray;

    assertArrayEquals(
        new RolePermissionType[] {existingEntry, appendedEntry},
        readDecodedRolePermissions(appendedClient, securityArrayId, AttributeId.RolePermissions));
    assertArrayEquals(
        new RolePermissionType[] {appendedEntry},
        readDecodedRolePermissions(
            appendedClient, securityArrayId, AttributeId.UserRolePermissions));

    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(existingClient, securityArrayId, AttributeId.RolePermissions).statusCode());
    assertArrayEquals(
        new RolePermissionType[] {existingEntry},
        readDecodedRolePermissions(
            existingClient, securityArrayId, AttributeId.UserRolePermissions));
  }

  // On the default wire Read(RolePermissions) path, a FallbackUserRolePermissionsFilter-governed
  // node must evaluate its front filter and RoleMapper exactly once per Read (preventing a
  // policy flip on call 2 from authorizing call 1), preserve indexRange status precedence, and
  // still enforce ReadRolePermissions for MonitoredItems through DefaultAccessController.
  @Test
  void wireReadRolePermissionsEvaluatesPolicyAndRoleMapperOnceAndMonitoredItemsStayProtected()
      throws Exception {
    var ownerRole = new NodeId(1, "SinglePassOwner");
    var secretEntry =
        new RolePermissionType(
            ownerRole,
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

    var roleMapperCalls = new AtomicInteger(0);
    RoleMapper roleMapper =
        identity -> {
          roleMapperCalls.incrementAndGet();
          if (identity instanceof Identity.UsernameIdentity username
              && "admin".equals(username.getUsername())) {
            return List.of(ownerRole);
          }
          return List.of(NodeIds.WellKnownRole_AuthenticatedUser);
        };

    TestServer testServer = TestServer.create(cfg -> cfg.setRoleMapper(roleMapper));
    server = testServer.getServer();

    ServerDiagnosticsTypeNode diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();
    SessionSecurityDiagnosticsArrayTypeNode securityArrayNode =
        diagnosticsNode
            .getSessionsDiagnosticsSummaryNode()
            .getSessionSecurityDiagnosticsArrayNode();

    var filterCalls = new AtomicInteger(0);
    var mode = new AtomicReference<>("flipDenyToAllow");
    securityArrayNode
        .getFilterChain()
        .addFirst(
            new AttributeFilter() {
              @Override
              public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
                  throws UaException {
                if (attributeId == AttributeId.RolePermissions) {
                  int call = filterCalls.incrementAndGet();
                  return switch (mode.get()) {
                    case "flipDenyToAllow" ->
                        call == 1
                            ? new RolePermissionType[] {secretEntry}
                            : new RolePermissionType[] {permissiveEntry};
                    case "flipAllowToDeny" ->
                        call == 1
                            ? new RolePermissionType[] {permissiveEntry, secretEntry, null}
                            : new RolePermissionType[0];
                    default -> new RolePermissionType[] {secretEntry};
                  };
                } else {
                  return ctx.readAttribute(attributeId);
                }
              }
            });

    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient user1 = connectSecure(testServer, new UsernameProvider("user1", "password"));
    OpcUaClient owner = connectSecure(testServer, new UsernameProvider("admin", "password"));
    NodeId securityArrayId =
        NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray;

    // Call 1 returns secretEntry (denying user1); call 2 would allow everyone. A single wire Read
    // must invoke both the front filter and RoleMapper exactly once and deny.
    filterCalls.set(0);
    roleMapperCalls.set(0);
    mode.set("flipDenyToAllow");
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(user1, securityArrayId, AttributeId.RolePermissions).statusCode());
    assertEquals(1, filterCalls.get(), "wire Read(RolePermissions) must run front filter once");
    assertEquals(1, roleMapperCalls.get(), "wire Read(RolePermissions) must run RoleMapper once");

    // Call 1 returns permissiveEntry + secretEntry + null; call 2 would deny. A single wire Read
    // must authorize and return the sanitized first array with one filter + one RoleMapper call.
    filterCalls.set(0);
    roleMapperCalls.set(0);
    mode.set("flipAllowToDeny");
    assertArrayEquals(
        new RolePermissionType[] {permissiveEntry, secretEntry},
        readDecodedRolePermissions(user1, securityArrayId, AttributeId.RolePermissions));
    assertEquals(1, filterCalls.get(), "wire Read(RolePermissions) must run front filter once");
    assertEquals(1, roleMapperCalls.get(), "wire Read(RolePermissions) must run RoleMapper once");

    // Invalid indexRange preserves status precedence: unauthorized gets Bad_UserAccessDenied
    // (single pass), while authorized gets Bad_IndexRangeInvalid.
    filterCalls.set(0);
    roleMapperCalls.set(0);
    mode.set("staticSecret");
    var invalidRangeRead =
        new ReadValueId(
            securityArrayId,
            AttributeId.RolePermissions.uid(),
            "invalid",
            QualifiedName.NULL_VALUE);
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        user1
            .read(0.0, TimestampsToReturn.Neither, List.of(invalidRangeRead))
            .getResults()[0]
            .statusCode());
    assertEquals(1, filterCalls.get());
    assertEquals(1, roleMapperCalls.get());
    assertEquals(
        new StatusCode(StatusCodes.Bad_IndexRangeInvalid),
        owner
            .read(0.0, TimestampsToReturn.Neither, List.of(invalidRangeRead))
            .getResults()[0]
            .statusCode());

    // MonitoredItems on RolePermissions still enforce DefaultAccessController.checkReadAccess().
    var deniedInitial = new CompletableFuture<DataValue>();
    var deniedSub = new OpcUaSubscription(user1);
    deniedSub.create();
    try {
      var item = new OpcUaMonitoredItem(readId(securityArrayId, AttributeId.RolePermissions));
      item.setDataValueListener((monitoredItem, value) -> deniedInitial.complete(value));
      deniedSub.addMonitoredItem(item);
      deniedSub.synchronizeMonitoredItems();

      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          deniedInitial.get(5, TimeUnit.SECONDS).statusCode(),
          "unauthorized MonitoredItem on RolePermissions must receive Bad_UserAccessDenied");
    } finally {
      deniedSub.delete();
    }

    var allowedInitial = new CompletableFuture<DataValue>();
    var allowedSub = new OpcUaSubscription(owner);
    allowedSub.create();
    try {
      var item = new OpcUaMonitoredItem(readId(securityArrayId, AttributeId.RolePermissions));
      item.setDataValueListener((monitoredItem, value) -> allowedInitial.complete(value));
      allowedSub.addMonitoredItem(item);
      allowedSub.synchronizeMonitoredItems();

      DataValue initial = allowedInitial.get(5, TimeUnit.SECONDS);
      assertEquals(
          StatusCode.GOOD,
          initial.statusCode(),
          "authorized MonitoredItem on RolePermissions must receive Good");
    } finally {
      allowedSub.delete();
    }
  }

  // An application filter on a fallback-protected node that narrows UserRolePermissions (removing
  // ReadRolePermissions) while leaving RolePermissions unchanged must deny BOTH wire
  // Read(RolePermissions) and MonitoredItems on RolePermissions with Bad_UserAccessDenied.
  @Test
  void userRolePermissionsNarrowingFilterDeniesWireReadAndMonitoredItem() throws Exception {
    var auditorRole = new NodeId(1, "NarrowedAuditor");
    var fullEntry =
        new RolePermissionType(
            auditorRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));
    var narrowedEntry =
        new RolePermissionType(
            auditorRole, PermissionType.of(PermissionType.Field.Browse, PermissionType.Field.Read));

    RoleMapper roleMapper =
        identity -> {
          if (identity instanceof Identity.UsernameIdentity username
              && "admin".equals(username.getUsername())) {
            return List.of(auditorRole);
          }
          return List.of(NodeIds.WellKnownRole_AuthenticatedUser);
        };

    TestServer testServer = TestServer.create(cfg -> cfg.setRoleMapper(roleMapper));
    server = testServer.getServer();

    ServerDiagnosticsTypeNode diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();
    SessionSecurityDiagnosticsArrayTypeNode securityArrayNode =
        diagnosticsNode
            .getSessionsDiagnosticsSummaryNode()
            .getSessionSecurityDiagnosticsArrayNode();

    securityArrayNode.setRolePermissions(new RolePermissionType[] {fullEntry});

    var delegateFirst = new AtomicBoolean(true);
    securityArrayNode
        .getFilterChain()
        .addFirst(
            new AttributeFilter() {
              @Override
              public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
                  throws UaException {
                if (attributeId == AttributeId.UserRolePermissions) {
                  if (delegateFirst.get()) {
                    // Delegate to Util first (populating RolePermissionsReadScope), then narrow.
                    ctx.readAttribute(AttributeId.UserRolePermissions);
                  }
                  return new RolePermissionType[] {narrowedEntry};
                } else {
                  return ctx.readAttribute(attributeId);
                }
              }
            });

    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient auditor = connectSecure(testServer, new UsernameProvider("admin", "password"));
    NodeId securityArrayId =
        NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray;

    // Delegating narrowing filter (Util ran and memoized RolePermissions with ReadRolePermissions,
    // then the outer filter stripped ReadRolePermissions from UserRolePermissions).
    assertArrayEquals(
        new RolePermissionType[] {narrowedEntry},
        readDecodedRolePermissions(auditor, securityArrayId, AttributeId.UserRolePermissions));
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(auditor, securityArrayId, AttributeId.RolePermissions).statusCode(),
        "wire Read(RolePermissions) must honor delegating UserRolePermissions narrowing filter");

    // Non-delegating narrowing filter (Util never ran for UserRolePermissions).
    delegateFirst.set(false);
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(auditor, securityArrayId, AttributeId.RolePermissions).statusCode(),
        "wire Read(RolePermissions) must honor non-delegating UserRolePermissions narrowing"
            + " filter");

    // MonitoredItem on RolePermissions also denies consistently with wire Read.
    var deniedInitial = new CompletableFuture<DataValue>();
    var sub = new OpcUaSubscription(auditor);
    sub.create();
    try {
      var item = new OpcUaMonitoredItem(readId(securityArrayId, AttributeId.RolePermissions));
      item.setDataValueListener((monitoredItem, value) -> deniedInitial.complete(value));
      sub.addMonitoredItem(item);
      sub.synchronizeMonitoredItems();

      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          deniedInitial.get(5, TimeUnit.SECONDS).statusCode(),
          "MonitoredItem on RolePermissions must honor UserRolePermissions narrowing filter");
    } finally {
      sub.delete();
    }
  }

  // A higher-priority AddressSpaceFragment serving RolePermissions for a NodeId that also exists
  // in a UaNodeManager with FallbackUserRolePermissionsFilter must still be authorized by
  // DefaultAccessController's UserRolePermissions check on wire Read(RolePermissions).
  @Test
  void higherPriorityAddressSpaceFragmentRolePermissionsStillEnforcesAccessControl()
      throws Exception {
    var ownerRole = new NodeId(1, "FragmentOwner");
    var ownerEntry =
        new RolePermissionType(
            ownerRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));
    var fragmentSecretPolicy = new RolePermissionType[] {ownerEntry};

    RoleMapper roleMapper =
        identity -> {
          if (identity instanceof Identity.UsernameIdentity username
              && "admin".equals(username.getUsername())) {
            return List.of(ownerRole);
          }
          return List.of(NodeIds.WellKnownRole_AuthenticatedUser);
        };

    TestServer testServer = TestServer.create(cfg -> cfg.setRoleMapper(roleMapper));
    server = testServer.getServer();

    NodeId securityArrayId =
        NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray;
    var fragmentRolePermissionsReads = new AtomicInteger(0);

    var higherPriorityFragment =
        new ManagedAddressSpaceFragment(server) {
          private final AddressSpaceFilter filter =
              SimpleAddressSpaceFilter.create(securityArrayId::equals);

          @Override
          public AddressSpaceFilter getFilter() {
            return filter;
          }

          @Override
          public List<DataValue> read(
              ReadContext context,
              Double maxAge,
              TimestampsToReturn timestamps,
              List<ReadValueId> readValueIds) {

            var results = new ArrayList<DataValue>(readValueIds.size());
            Session session = context.getSession().orElse(null);
            boolean isOwner =
                session != null
                    && session.getRoleIds().map(roles -> roles.contains(ownerRole)).orElse(false);

            for (ReadValueId readValueId : readValueIds) {
              if (AttributeId.NodeClass.uid().equals(readValueId.getAttributeId())) {
                results.add(DataValue.valueOnly(new Variant(NodeClass.Variable)));
              } else if (AttributeId.AccessRestrictions.uid()
                  .equals(readValueId.getAttributeId())) {
                results.add(new DataValue(StatusCodes.Bad_AttributeIdInvalid));
              } else if (AttributeId.UserRolePermissions.uid()
                  .equals(readValueId.getAttributeId())) {
                RolePermissionType[] userPerms =
                    isOwner ? fragmentSecretPolicy : new RolePermissionType[0];
                results.add(DataValue.valueOnly(new Variant(userPerms)));
              } else if (AttributeId.RolePermissions.uid().equals(readValueId.getAttributeId())) {
                fragmentRolePermissionsReads.incrementAndGet();
                results.add(DataValue.valueOnly(new Variant(fragmentSecretPolicy)));
              } else {
                results.add(new DataValue(StatusCodes.Bad_AttributeIdInvalid));
              }
            }
            return results;
          }
        };

    server.getAddressSpaceManager().registerFirst(higherPriorityFragment);
    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient user1 = connectSecure(testServer, new UsernameProvider("user1", "password"));
    OpcUaClient owner = connectSecure(testServer, new UsernameProvider("admin", "password"));

    // Unauthorized wire Read(RolePermissions) must be denied by DefaultAccessController before the
    // higher-priority fragment's RolePermissions handler ever executes.
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(user1, securityArrayId, AttributeId.RolePermissions).statusCode());
    assertEquals(
        0,
        fragmentRolePermissionsReads.get(),
        "denied Read(RolePermissions) must not reach higher-priority fragment");

    // Authorized wire Read(RolePermissions) succeeds and returns the fragment's policy.
    assertArrayEquals(
        fragmentSecretPolicy,
        readDecodedRolePermissions(owner, securityArrayId, AttributeId.RolePermissions));
    assertEquals(1, fragmentRolePermissionsReads.get());
  }

  // A mixed wire Read batch containing a fallback-protected node and an ordinary node must request
  // access attributes per node and evaluate the protected node's front filter and RoleMapper at
  // most once (or zero times when only non-permission attributes of the protected node are read).
  @Test
  void mixedReadBatchEvaluatesProtectedPolicyAndRoleMapperOnce() throws Exception {
    var ownerRole = new NodeId(1, "MixedBatchOwner");
    var protectedEntry =
        new RolePermissionType(
            ownerRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));
    var ordinaryEntry =
        new RolePermissionType(
            ownerRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));

    var roleMapperCalls = new AtomicInteger(0);
    RoleMapper roleMapper =
        identity -> {
          roleMapperCalls.incrementAndGet();
          if (identity instanceof Identity.UsernameIdentity username
              && "admin".equals(username.getUsername())) {
            return List.of(ownerRole);
          }
          return List.of(NodeIds.WellKnownRole_AuthenticatedUser);
        };

    TestServer testServer = TestServer.create(cfg -> cfg.setRoleMapper(roleMapper));
    server = testServer.getServer();

    ServerDiagnosticsTypeNode diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();
    SessionSecurityDiagnosticsArrayTypeNode protectedNode =
        diagnosticsNode
            .getSessionsDiagnosticsSummaryNode()
            .getSessionSecurityDiagnosticsArrayNode();

    var filterCalls = new AtomicInteger(0);
    protectedNode
        .getFilterChain()
        .addFirst(
            new AttributeFilter() {
              @Override
              public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
                  throws UaException {
                if (attributeId == AttributeId.RolePermissions) {
                  filterCalls.incrementAndGet();
                  return new RolePermissionType[] {protectedEntry};
                } else {
                  return ctx.readAttribute(attributeId);
                }
              }
            });

    // Configure an ordinary node (no FallbackUserRolePermissionsFilter) with explicit
    // RolePermissions and UserRolePermissions.
    UaNode ordinaryNode =
        server.getAddressSpaceManager().getManagedNode(NodeIds.Server).orElseThrow();
    ordinaryNode.setRolePermissions(new RolePermissionType[] {ordinaryEntry});
    ordinaryNode
        .getFilterChain()
        .addFirst(
            new AttributeFilter() {
              @Override
              public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
                  throws UaException {
                if (attributeId == AttributeId.UserRolePermissions) {
                  return ctx.getSession()
                          .flatMap(Session::getRoleIds)
                          .map(roles -> roles.contains(ownerRole))
                          .orElse(false)
                      ? new RolePermissionType[] {ordinaryEntry}
                      : new RolePermissionType[0];
                } else {
                  return ctx.readAttribute(attributeId);
                }
              }
            });

    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient user1 = connectSecure(testServer, new UsernameProvider("user1", "password"));
    OpcUaClient owner = connectSecure(testServer, new UsernameProvider("admin", "password"));
    NodeId protectedNodeId =
        NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray;
    NodeId ordinaryNodeId = NodeIds.Server;

    // Batch 1: only DisplayName on protectedNode + RolePermissions on ordinaryNode must NOT read
    // protectedNode's UserRolePermissions or invoke protectedNode's front filter.
    filterCalls.set(0);
    roleMapperCalls.set(0);
    DataValue[] displayOnlyBatch =
        user1
            .read(
                0.0,
                TimestampsToReturn.Neither,
                List.of(
                    readId(protectedNodeId, AttributeId.DisplayName),
                    readId(ordinaryNodeId, AttributeId.RolePermissions)))
            .getResults();
    assertEquals(StatusCode.GOOD, displayOnlyBatch[0].statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied), displayOnlyBatch[1].statusCode());
    assertEquals(
        0,
        filterCalls.get(),
        "protectedNode front filter must not run when only DisplayName is read on protectedNode");

    // Batch 2 (unauthorized): (protectedNode, RolePermissions), (protectedNode, DisplayName),
    // (ordinaryNode, RolePermissions) must evaluate protectedNode's front filter once.
    filterCalls.set(0);
    roleMapperCalls.set(0);
    List<ReadValueId> mixedBatch =
        List.of(
            readId(protectedNodeId, AttributeId.RolePermissions),
            readId(protectedNodeId, AttributeId.DisplayName),
            readId(ordinaryNodeId, AttributeId.RolePermissions));
    DataValue[] deniedResults =
        user1.read(0.0, TimestampsToReturn.Neither, mixedBatch).getResults();
    assertEquals(new StatusCode(StatusCodes.Bad_UserAccessDenied), deniedResults[0].statusCode());
    assertEquals(StatusCode.GOOD, deniedResults[1].statusCode());
    assertEquals(new StatusCode(StatusCodes.Bad_UserAccessDenied), deniedResults[2].statusCode());
    assertEquals(1, filterCalls.get(), "protectedNode front filter must run once in mixed batch");

    // Batch 3 (authorized): same mixed batch must evaluate protectedNode's front filter once and
    // return Good for all three items. (ordinaryNode's test filter calls session.getRoleIds()
    // directly, so also verify RoleMapper == 1 with a static UserRolePermissions ordinary node.)
    filterCalls.set(0);
    DataValue[] allowedResults =
        owner.read(0.0, TimestampsToReturn.Neither, mixedBatch).getResults();
    assertEquals(StatusCode.GOOD, allowedResults[0].statusCode());
    assertEquals(StatusCode.GOOD, allowedResults[1].statusCode());
    assertEquals(StatusCode.GOOD, allowedResults[2].statusCode());
    assertEquals(1, filterCalls.get(), "protectedNode front filter must run once in mixed batch");

    // Also verify exact RoleMapper == 1 when ordinaryNode has static UserRolePermissions.
    UaNode staticOrdinaryNode =
        server
            .getAddressSpaceManager()
            .getManagedNode(NodeIds.Server_ServerCapabilities)
            .orElseThrow();
    staticOrdinaryNode.setRolePermissions(new RolePermissionType[] {ordinaryEntry});
    staticOrdinaryNode.setUserRolePermissions(new RolePermissionType[] {ordinaryEntry});

    List<ReadValueId> staticMixedBatch =
        List.of(
            readId(protectedNodeId, AttributeId.RolePermissions),
            readId(protectedNodeId, AttributeId.DisplayName),
            readId(NodeIds.Server_ServerCapabilities, AttributeId.RolePermissions));

    filterCalls.set(0);
    roleMapperCalls.set(0);
    DataValue[] staticOwnerResults =
        owner.read(0.0, TimestampsToReturn.Neither, staticMixedBatch).getResults();
    assertEquals(StatusCode.GOOD, staticOwnerResults[0].statusCode());
    assertEquals(StatusCode.GOOD, staticOwnerResults[1].statusCode());
    assertEquals(StatusCode.GOOD, staticOwnerResults[2].statusCode());
    assertEquals(1, filterCalls.get(), "protectedNode front filter must run once");
    assertEquals(1, roleMapperCalls.get(), "RoleMapper must run once across entire mixed batch");

    filterCalls.set(0);
    roleMapperCalls.set(0);
    DataValue[] staticDeniedResults =
        user1.read(0.0, TimestampsToReturn.Neither, staticMixedBatch).getResults();
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied), staticDeniedResults[0].statusCode());
    assertEquals(StatusCode.GOOD, staticDeniedResults[1].statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied), staticDeniedResults[2].statusCode());
    assertEquals(1, filterCalls.get(), "protectedNode front filter must run once");
    assertEquals(1, roleMapperCalls.get(), "RoleMapper must run once across entire mixed batch");
  }

  // When a filter or higher-priority AddressSpaceFragment fails the UserRolePermissions lookup
  // with Bad_UserAccessDenied (or any non-Bad_AttributeIdInvalid bad status), both wire
  // Read(RolePermissions) and MonitoredItems on RolePermissions must fail closed rather than
  // treating UserRolePermissions as omitted, while Bad_AttributeIdInvalid continues to behave as
  // an omitted optional attribute.
  @Test
  void failedUserRolePermissionsLookupDeniesWireReadAndMonitoredItem() throws Exception {
    var auditorRole = new NodeId(1, "LookupFailAuditor");
    var auditorEntry =
        new RolePermissionType(
            auditorRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));

    RoleMapper roleMapper =
        identity -> {
          if (identity instanceof Identity.UsernameIdentity username
              && "admin".equals(username.getUsername())) {
            return List.of(auditorRole);
          }
          return List.of(NodeIds.WellKnownRole_AuthenticatedUser);
        };

    TestServer testServer = TestServer.create(cfg -> cfg.setRoleMapper(roleMapper));
    server = testServer.getServer();

    ServerDiagnosticsTypeNode diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();
    SessionSecurityDiagnosticsArrayTypeNode securityArrayNode =
        diagnosticsNode
            .getSessionsDiagnosticsSummaryNode()
            .getSessionSecurityDiagnosticsArrayNode();

    securityArrayNode.setRolePermissions(new RolePermissionType[] {auditorEntry});

    var filterFailureStatus =
        new AtomicReference<>(new StatusCode(StatusCodes.Bad_UserAccessDenied));
    securityArrayNode
        .getFilterChain()
        .addFirst(
            new AttributeFilter() {
              @Override
              public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
                  throws UaException {
                if (attributeId == AttributeId.UserRolePermissions) {
                  throw new UaException(filterFailureStatus.get());
                } else {
                  return ctx.readAttribute(attributeId);
                }
              }
            });

    // Also register a higher-priority AddressSpaceFragment on EnabledFlag whose
    // UserRolePermissions lookup can be switched between Bad_UserAccessDenied and
    // Bad_AttributeIdInvalid (omitted optional attribute).
    NodeId enabledFlagId = NodeIds.Server_ServerDiagnostics_EnabledFlag;
    var fragmentUserRolePermissionsStatus =
        new AtomicReference<>(new StatusCode(StatusCodes.Bad_UserAccessDenied));
    var fragmentRolePermissionsReads = new AtomicInteger(0);
    var fragmentSecretPolicy = new RolePermissionType[] {auditorEntry};

    var higherPriorityFragment =
        new ManagedAddressSpaceFragment(server) {
          private final AddressSpaceFilter filter =
              SimpleAddressSpaceFilter.create(enabledFlagId::equals);

          @Override
          public AddressSpaceFilter getFilter() {
            return filter;
          }

          @Override
          public List<DataValue> read(
              ReadContext context,
              Double maxAge,
              TimestampsToReturn timestamps,
              List<ReadValueId> readValueIds) {

            var results = new ArrayList<DataValue>(readValueIds.size());
            for (ReadValueId readValueId : readValueIds) {
              if (AttributeId.NodeClass.uid().equals(readValueId.getAttributeId())) {
                results.add(DataValue.valueOnly(new Variant(NodeClass.Variable)));
              } else if (AttributeId.AccessRestrictions.uid()
                  .equals(readValueId.getAttributeId())) {
                results.add(new DataValue(StatusCodes.Bad_AttributeIdInvalid));
              } else if (AttributeId.UserRolePermissions.uid()
                  .equals(readValueId.getAttributeId())) {
                results.add(new DataValue(fragmentUserRolePermissionsStatus.get()));
              } else if (AttributeId.RolePermissions.uid().equals(readValueId.getAttributeId())) {
                fragmentRolePermissionsReads.incrementAndGet();
                results.add(DataValue.valueOnly(new Variant(fragmentSecretPolicy)));
              } else {
                results.add(new DataValue(StatusCodes.Bad_AttributeIdInvalid));
              }
            }
            return results;
          }
        };

    server.getAddressSpaceManager().registerFirst(higherPriorityFragment);
    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient auditor = connectSecure(testServer, new UsernameProvider("admin", "password"));
    NodeId securityArrayId =
        NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray;

    // 1. AttributeFilter throwing Bad_UserAccessDenied on UserRolePermissions denies both wire
    // Read(RolePermissions) and MonitoredItem on RolePermissions.
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(auditor, securityArrayId, AttributeId.RolePermissions).statusCode(),
        "wire Read(RolePermissions) must fail closed when UserRolePermissions filter throws"
            + " Bad_UserAccessDenied");

    var filterDeniedInitial = new CompletableFuture<DataValue>();
    var filterSub = new OpcUaSubscription(auditor);
    filterSub.create();
    try {
      var item = new OpcUaMonitoredItem(readId(securityArrayId, AttributeId.RolePermissions));
      item.setDataValueListener((monitoredItem, value) -> filterDeniedInitial.complete(value));
      filterSub.addMonitoredItem(item);
      filterSub.synchronizeMonitoredItems();

      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          filterDeniedInitial.get(5, TimeUnit.SECONDS).statusCode(),
          "MonitoredItem on RolePermissions must fail closed when UserRolePermissions filter"
              + " throws Bad_UserAccessDenied");
    } finally {
      filterSub.delete();
    }

    // Any other bad status (e.g. Bad_InternalError) also fails closed without disclosing
    // RolePermissions.
    filterFailureStatus.set(new StatusCode(StatusCodes.Bad_InternalError));
    assertEquals(
        new StatusCode(StatusCodes.Bad_InternalError),
        readAttribute(auditor, securityArrayId, AttributeId.RolePermissions).statusCode());

    // 2. Higher-priority AddressSpaceFragment failing UserRolePermissions with
    // Bad_UserAccessDenied denies both wire Read(RolePermissions) (without invoking the fragment's
    // RolePermissions handler) and MonitoredItem on RolePermissions.
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(auditor, enabledFlagId, AttributeId.RolePermissions).statusCode(),
        "wire Read(RolePermissions) must fail closed when fragment UserRolePermissions returns"
            + " Bad_UserAccessDenied");
    assertEquals(
        0,
        fragmentRolePermissionsReads.get(),
        "failed UserRolePermissions lookup must not reach fragment RolePermissions read");

    var fragmentDeniedInitial = new CompletableFuture<DataValue>();
    var fragmentSub = new OpcUaSubscription(auditor);
    fragmentSub.create();
    try {
      var item = new OpcUaMonitoredItem(readId(enabledFlagId, AttributeId.RolePermissions));
      item.setDataValueListener((monitoredItem, value) -> fragmentDeniedInitial.complete(value));
      fragmentSub.addMonitoredItem(item);
      fragmentSub.synchronizeMonitoredItems();

      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          fragmentDeniedInitial.get(5, TimeUnit.SECONDS).statusCode(),
          "MonitoredItem on RolePermissions must fail closed when fragment UserRolePermissions"
              + " returns Bad_UserAccessDenied");
    } finally {
      fragmentSub.delete();
    }

    // 3. Control: Bad_AttributeIdInvalid on UserRolePermissions is treated as an omitted optional
    // attribute, preserving existing behavior on both standard ns=0 nodes and the fragment.
    assertRolePermissionsAbsent(auditor, List.of(NodeIds.Server));

    fragmentUserRolePermissionsStatus.set(new StatusCode(StatusCodes.Bad_AttributeIdInvalid));
    assertArrayEquals(
        fragmentSecretPolicy,
        readDecodedRolePermissions(auditor, enabledFlagId, AttributeId.RolePermissions));
    assertEquals(1, fragmentRolePermissionsReads.get());
  }

  // Even without a RoleMapper configured, an attribute-level Bad_NodeIdUnknown on
  // UserRolePermissions of a known node or a malformed Good non-RolePermissionType[] payload must
  // fail closed over the wire for both Read(RolePermissions) and MonitoredItems on RolePermissions.
  @Test
  void badNodeIdUnknownAndMalformedGoodUserRolePermissionsFailClosedOverWireWithoutRoleMapper()
      throws Exception {
    var secretEntry =
        new RolePermissionType(
            NodeIds.WellKnownRole_SecurityAdmin,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));
    var fragmentSecretPolicy = new RolePermissionType[] {secretEntry};

    TestServer testServer = TestServer.create();
    server = testServer.getServer();

    NodeId targetNodeId = NodeIds.Server_ServerDiagnostics_EnabledFlag;
    var userRolePermissionsResponse =
        new AtomicReference<>(new DataValue(StatusCodes.Bad_NodeIdUnknown));
    var fragmentRolePermissionsReads = new AtomicInteger(0);

    var higherPriorityFragment =
        new ManagedAddressSpaceFragment(server) {
          private final AddressSpaceFilter filter =
              SimpleAddressSpaceFilter.create(targetNodeId::equals);

          @Override
          public AddressSpaceFilter getFilter() {
            return filter;
          }

          @Override
          public List<DataValue> read(
              ReadContext context,
              Double maxAge,
              TimestampsToReturn timestamps,
              List<ReadValueId> readValueIds) {

            var results = new ArrayList<DataValue>(readValueIds.size());
            for (ReadValueId readValueId : readValueIds) {
              if (AttributeId.NodeClass.uid().equals(readValueId.getAttributeId())) {
                results.add(DataValue.valueOnly(new Variant(NodeClass.Variable)));
              } else if (AttributeId.AccessRestrictions.uid()
                  .equals(readValueId.getAttributeId())) {
                results.add(new DataValue(StatusCodes.Bad_AttributeIdInvalid));
              } else if (AttributeId.UserRolePermissions.uid()
                  .equals(readValueId.getAttributeId())) {
                results.add(userRolePermissionsResponse.get());
              } else if (AttributeId.RolePermissions.uid().equals(readValueId.getAttributeId())) {
                fragmentRolePermissionsReads.incrementAndGet();
                results.add(DataValue.valueOnly(new Variant(fragmentSecretPolicy)));
              } else {
                results.add(new DataValue(StatusCodes.Bad_AttributeIdInvalid));
              }
            }
            return results;
          }
        };

    server.getAddressSpaceManager().registerFirst(higherPriorityFragment);
    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient anonymous = connectSecure(testServer, AnonymousProvider.INSTANCE);

    // 1. Attribute-level Bad_NodeIdUnknown on a known node fails closed without reaching the
    // fragment's RolePermissions read.
    assertEquals(
        new StatusCode(StatusCodes.Bad_NodeIdUnknown),
        readAttribute(anonymous, targetNodeId, AttributeId.RolePermissions).statusCode());
    assertEquals(0, fragmentRolePermissionsReads.get());

    // 2. Malformed Good non-RolePermissionType[] payload fails closed with Bad_TypeMismatch for
    // both wire Read(RolePermissions) and MonitoredItem on RolePermissions without a RoleMapper.
    userRolePermissionsResponse.set(DataValue.valueOnly(Variant.ofString("not-permissions")));
    assertEquals(
        new StatusCode(StatusCodes.Bad_TypeMismatch),
        readAttribute(anonymous, targetNodeId, AttributeId.RolePermissions).statusCode());
    assertEquals(0, fragmentRolePermissionsReads.get());

    var sub = new OpcUaSubscription(anonymous);
    sub.create();
    try {
      var item = new OpcUaMonitoredItem(readId(targetNodeId, AttributeId.RolePermissions));
      sub.addMonitoredItem(item);

      // SubscriptionManager fails CreateMonitoredItems immediately for non-read-access denials
      // such as Bad_TypeMismatch.
      MonitoredItemSynchronizationException ex =
          assertThrows(MonitoredItemSynchronizationException.class, sub::synchronizeMonitoredItems);
      assertEquals(
          new StatusCode(StatusCodes.Bad_TypeMismatch),
          ex.getCreateResults().get(0).operationResult().orElseThrow());
      assertEquals(0, fragmentRolePermissionsReads.get());
    } finally {
      sub.delete();
    }
  }

  // A forwarding AddressSpaceFragment returning Good ExtensionObject[] UserRolePermissions must
  // decode valid RolePermissionType entries (allowing authorized Browse and Read(RolePermissions)
  // while denying unauthorized roles) and fail closed with Bad_TypeMismatch on undecodable or
  // wrong-type ExtensionObject[] payloads.
  @Test
  void forwardingAddressSpaceWithEncodedExtensionObjectUserRolePermissions() throws Exception {
    var ownerRole = new NodeId(1, "ForwardedOwner");
    var ownerGrant =
        new RolePermissionType(
            ownerRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));

    RoleMapper roleMapper =
        identity -> {
          if (identity instanceof Identity.UsernameIdentity username
              && "admin".equals(username.getUsername())) {
            return List.of(ownerRole);
          }
          return List.of(NodeIds.WellKnownRole_AuthenticatedUser);
        };

    TestServer testServer = TestServer.create(cfg -> cfg.setRoleMapper(roleMapper));
    server = testServer.getServer();

    NodeId targetNodeId = NodeIds.Server_ServerDiagnostics_EnabledFlag;
    ExtensionObject[] encodedOwnerPolicy =
        ExtensionObject.encodeArray(
            server.getStaticEncodingContext(), new RolePermissionType[] {ownerGrant});
    ExtensionObject[] encodedEmptyPolicy = new ExtensionObject[0];

    var overrideBadUrpPayload = new AtomicReference<ExtensionObject[]>(null);
    var fragmentRolePermissionsReads = new AtomicInteger(0);

    var forwardingFragment =
        new ManagedAddressSpaceFragment(server) {
          private final AddressSpaceFilter filter =
              new SimpleAddressSpaceFilter() {
                @Override
                protected boolean filterNode(NodeId nodeId) {
                  return false;
                }

                @Override
                protected boolean filterMonitoredItem(NodeId nodeId) {
                  return false;
                }

                @Override
                public boolean filterRead(OpcUaServer server, ReadValueId readValueId) {
                  return targetNodeId.equals(readValueId.getNodeId());
                }
              };

          @Override
          public AddressSpaceFilter getFilter() {
            return filter;
          }

          @Override
          public List<DataValue> read(
              ReadContext context,
              Double maxAge,
              TimestampsToReturn timestamps,
              List<ReadValueId> readValueIds) {

            var results = new ArrayList<DataValue>(readValueIds.size());
            Session session = context.getSession().orElse(null);
            boolean isOwner =
                session != null
                    && session.getRoleIds().map(roles -> roles.contains(ownerRole)).orElse(false);

            for (ReadValueId readValueId : readValueIds) {
              if (AttributeId.NodeClass.uid().equals(readValueId.getAttributeId())) {
                results.add(DataValue.valueOnly(new Variant(NodeClass.Variable)));
              } else if (AttributeId.AccessRestrictions.uid()
                  .equals(readValueId.getAttributeId())) {
                results.add(new DataValue(StatusCodes.Bad_AttributeIdInvalid));
              } else if (AttributeId.UserRolePermissions.uid()
                  .equals(readValueId.getAttributeId())) {
                ExtensionObject[] badPayload = overrideBadUrpPayload.get();
                ExtensionObject[] payload =
                    badPayload != null
                        ? badPayload
                        : (isOwner ? encodedOwnerPolicy : encodedEmptyPolicy);
                results.add(DataValue.valueOnly(new Variant(payload)));
              } else if (AttributeId.RolePermissions.uid().equals(readValueId.getAttributeId())) {
                fragmentRolePermissionsReads.incrementAndGet();
                results.add(DataValue.valueOnly(new Variant(encodedOwnerPolicy)));
              } else {
                results.add(new DataValue(StatusCodes.Bad_AttributeIdInvalid));
              }
            }
            return results;
          }
        };

    server.getAddressSpaceManager().registerFirst(forwardingFragment);
    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient user1 = connectSecure(testServer, new UsernameProvider("user1", "password"));
    OpcUaClient owner = connectSecure(testServer, new UsernameProvider("admin", "password"));

    // 1. Valid encoded ExtensionObject[] allows authorized Browse and Read(RolePermissions) and
    // denies unauthorized role.
    assertFalse(
        browseAll(owner, targetNodeId).isEmpty(),
        "authorized role must browse node with encoded ExtensionObject[] UserRolePermissions");
    assertArrayEquals(
        new RolePermissionType[] {ownerGrant},
        readDecodedRolePermissions(owner, targetNodeId, AttributeId.RolePermissions));
    assertEquals(1, fragmentRolePermissionsReads.get());

    assertEquals(
        List.of(), browseAll(user1, targetNodeId), "unauthorized role must be denied Browse");
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(user1, targetNodeId, AttributeId.RolePermissions).statusCode());
    assertEquals(1, fragmentRolePermissionsReads.get());

    // 2. Wrong-type ExtensionObject[] fails closed with Bad_TypeMismatch (never reaching
    // RolePermissions read, and denying Browse).
    ExtensionObject wrongType =
        ExtensionObject.encode(
            server.getStaticEncodingContext(), readId(targetNodeId, AttributeId.RolePermissions));
    overrideBadUrpPayload.set(new ExtensionObject[] {wrongType});
    assertEquals(
        new StatusCode(StatusCodes.Bad_TypeMismatch),
        readAttribute(owner, targetNodeId, AttributeId.RolePermissions).statusCode());
    assertEquals(List.of(), browseAll(owner, targetNodeId));
    assertEquals(1, fragmentRolePermissionsReads.get());

    // 3. Undecodable ExtensionObject[] fails closed with Bad_TypeMismatch.
    ExtensionObject undecodable =
        ExtensionObject.of(
            ByteString.of(new byte[] {1, 2, 3}), NodeIds.RolePermissionType_Encoding_DefaultBinary);
    overrideBadUrpPayload.set(new ExtensionObject[] {undecodable});
    assertEquals(
        new StatusCode(StatusCodes.Bad_TypeMismatch),
        readAttribute(owner, targetNodeId, AttributeId.RolePermissions).statusCode());
    assertEquals(List.of(), browseAll(owner, targetNodeId));
    assertEquals(1, fragmentRolePermissionsReads.get());
  }

  // OPC 10000-3 §5.2.9: writing an empty RolePermissionType[0] array through the Write service on
  // a diagnostics node configured to allow RolePermissions writes removes the override so the node
  // reverts to its inherited or default policy without locking administrators out, while an empty
  // session-specific UserRolePermissions evaluation still denies unauthorized sessions.
  @Test
  void writingEmptyRolePermissionsViaWriteServiceRevertsDiagnosticsNodeToInheritedAndDefaultPolicy()
      throws Exception {
    var auditorRole = new NodeId(1, "WritableDiagAuditor");
    var auditorPolicy =
        new RolePermissionType[] {
          new RolePermissionType(
              auditorRole,
              PermissionType.of(
                  PermissionType.Field.Browse,
                  PermissionType.Field.Read,
                  PermissionType.Field.ReadRolePermissions,
                  PermissionType.Field.WriteRolePermissions))
        };
    var adminOverridePolicy =
        new RolePermissionType[] {
          new RolePermissionType(
              NodeIds.WellKnownRole_SecurityAdmin,
              PermissionType.of(
                  PermissionType.Field.Browse,
                  PermissionType.Field.Read,
                  PermissionType.Field.ReadRolePermissions,
                  PermissionType.Field.WriteRolePermissions))
        };

    RoleMapper roleMapper =
        identity -> {
          if (identity instanceof Identity.UsernameIdentity username) {
            return switch (username.getUsername()) {
              case "admin" -> List.of(NodeIds.WellKnownRole_SecurityAdmin);
              case "user1" -> List.of(auditorRole);
              default -> List.of(NodeIds.WellKnownRole_AuthenticatedUser);
            };
          }
          return List.of(NodeIds.WellKnownRole_Anonymous);
        };

    TestServer testServer = TestServer.create(cfg -> cfg.setRoleMapper(roleMapper));
    server = testServer.getServer();

    ServerDiagnosticsTypeNode diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();
    SessionSecurityDiagnosticsArrayTypeNode securityArrayNode =
        diagnosticsNode
            .getSessionsDiagnosticsSummaryNode()
            .getSessionSecurityDiagnosticsArrayNode();

    var writableRolePermsMask = uint(WriteMask.RolePermissions.getValue());
    securityArrayNode.setWriteMask(writableRolePermsMask);
    securityArrayNode.setUserWriteMask(writableRolePermsMask);
    securityArrayNode.setRolePermissions(auditorPolicy);

    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient admin = connectSecure(testServer, new UsernameProvider("admin", "password"));
    OpcUaClient auditor = connectSecure(testServer, new UsernameProvider("user1", "password"));
    OpcUaClient unprivileged = connectSecure(testServer, new UsernameProvider("user2", "password"));

    assertEquals(
        StatusCode.GOOD,
        writeValue(
            admin,
            NodeIds.Server_ServerDiagnostics_EnabledFlag,
            DataValue.valueOnly(new Variant(Boolean.TRUE))));

    NodeId securityArrayId =
        NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray;

    // 1. On a dynamic SessionSecurityDiagnostics element, writing a local override and then
    // clearing it with an empty RolePermissionType[0] array reverts the element to inheriting
    // SessionSecurityDiagnosticsArray's live policy.
    List<ReferenceDescription> elements = browse(auditor, securityArrayId);
    assertFalse(elements.isEmpty(), "auditor discovers dynamic security elements");
    NodeId elementId =
        elements.get(0).getNodeId().toNodeId(auditor.getNamespaceTable()).orElseThrow();
    UaNode elementNode = server.getAddressSpaceManager().getManagedNode(elementId).orElseThrow();
    elementNode.setWriteMask(writableRolePermsMask);
    elementNode.setUserWriteMask(writableRolePermsMask);

    assertEquals(
        StatusCode.GOOD,
        writeRolePermissions(auditor, elementId, adminOverridePolicy),
        "auditor with inherited WriteRolePermissions sets local element override");
    assertArrayEquals(
        new RolePermissionType[0],
        readDecodedRolePermissions(auditor, elementId, AttributeId.UserRolePermissions),
        "empty session-specific UserRolePermissions must still deny auditor");
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        auditor.readValue(0.0, TimestampsToReturn.Neither, elementId).statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(auditor, elementId, AttributeId.RolePermissions).statusCode());
    assertEquals(List.of(), browseAll(auditor, elementId));
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        writeRolePermissions(auditor, elementId, new RolePermissionType[0]));

    assertEquals(
        StatusCode.GOOD,
        writeRolePermissions(admin, elementId, new RolePermissionType[0]),
        "admin clears local element override via empty RolePermissionType[0] write");
    // OPC 10000-3 §5.2.9: with its local override cleared, the writable element exposes an empty
    // RolePermissionType[0] array to authorized readers rather than publishing the parent array's
    // policy, while UserRolePermissions, Value, and access checks continue inheriting the array's
    // live auditorPolicy.
    assertArrayEquals(
        new RolePermissionType[0],
        readDecodedRolePermissions(auditor, elementId, AttributeId.RolePermissions),
        "cleared writable element exposes Good empty RolePermissionType[0] to authorized reader");
    assertArrayEquals(
        auditorPolicy,
        readDecodedRolePermissions(auditor, elementId, AttributeId.UserRolePermissions),
        "cleared element continues inheriting SessionSecurityDiagnosticsArray UserRolePermissions");
    assertEquals(
        StatusCode.GOOD,
        auditor.readValue(0.0, TimestampsToReturn.Neither, elementId).statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(admin, elementId, AttributeId.RolePermissions).statusCode(),
        "unauthorized admin is denied Read(RolePermissions) on cleared writable element");
    assertArrayEquals(
        new RolePermissionType[0],
        readDecodedRolePermissions(admin, elementId, AttributeId.UserRolePermissions));
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        admin.readValue(0.0, TimestampsToReturn.Neither, elementId).statusCode());

    // By contrast, a non-writable diagnostics child without a local override omits RolePermissions
    // (Bad_AttributeIdInvalid) for authorized readers while still inheriting auditorPolicy.
    NodeId clientCertId = childNodeId(auditor, elementId, "ClientCertificate");
    assertEquals(
        new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
        readAttribute(auditor, clientCertId, AttributeId.RolePermissions).statusCode(),
        "non-writable diagnostics child without override omits RolePermissions");
    assertArrayEquals(
        auditorPolicy,
        readDecodedRolePermissions(auditor, clientCertId, AttributeId.UserRolePermissions));
    assertEquals(
        StatusCode.GOOD,
        auditor.readValue(0.0, TimestampsToReturn.Neither, clientCertId).statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(admin, clientCertId, AttributeId.RolePermissions).statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        admin.readValue(0.0, TimestampsToReturn.Neither, clientCertId).statusCode());

    // 2. Clearing SessionSecurityDiagnosticsArray's override via an empty RolePermissionType[0]
    // write reverts to the built-in SecurityAdmin default policy rather than locking SecurityAdmin
    // out, exposes Good empty RolePermissionType[0] to authorized readers on writable nodes (OPC
    // 10000-3 §5.2.9), denies unauthorized readers on RolePermissions, and keeps non-writable ns=0
    // nodes returning Bad_AttributeIdInvalid.
    assertEquals(
        StatusCode.GOOD,
        writeRolePermissions(auditor, securityArrayId, new RolePermissionType[0]),
        "auditor clears security array override via empty RolePermissionType[0] write");

    // On writable diagnostics nodes without an override, authorized SecurityAdmin reads a Good
    // empty RolePermissionType[0] array (and UserRolePermissions remains omitted), while
    // unauthorized readers are denied with Bad_UserAccessDenied.
    for (NodeId writableNodeId : List.of(securityArrayId, elementId)) {
      assertArrayEquals(
          new RolePermissionType[0],
          readDecodedRolePermissions(admin, writableNodeId, AttributeId.RolePermissions),
          "authorized reader on writable node without override must receive Good empty array");
      assertEquals(
          new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
          readAttribute(admin, writableNodeId, AttributeId.UserRolePermissions).statusCode());

      for (OpcUaClient denied : List.of(auditor, unprivileged)) {
        assertEquals(
            new StatusCode(StatusCodes.Bad_UserAccessDenied),
            readAttribute(denied, writableNodeId, AttributeId.RolePermissions).statusCode(),
            "unauthorized reader on writable diagnostics node without override must be denied");
        assertEquals(
            new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
            readAttribute(denied, writableNodeId, AttributeId.UserRolePermissions).statusCode());
      }
    }

    // MonitoredItems on cleared writable diagnostics RolePermissions also deny unauthorized
    // clients and deliver Good empty RolePermissionType[0] to SecurityAdmin.
    var deniedInitial = new CompletableFuture<DataValue>();
    var deniedSub = new OpcUaSubscription(auditor);
    deniedSub.create();
    try {
      var item = new OpcUaMonitoredItem(readId(securityArrayId, AttributeId.RolePermissions));
      item.setDataValueListener((monitoredItem, value) -> deniedInitial.complete(value));
      deniedSub.addMonitoredItem(item);
      deniedSub.synchronizeMonitoredItems();
      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          deniedInitial.get(5, TimeUnit.SECONDS).statusCode());
    } finally {
      deniedSub.delete();
    }

    var adminInitial = new CompletableFuture<DataValue>();
    var adminSub = new OpcUaSubscription(admin);
    adminSub.create();
    try {
      var item = new OpcUaMonitoredItem(readId(securityArrayId, AttributeId.RolePermissions));
      item.setDataValueListener((monitoredItem, value) -> adminInitial.complete(value));
      adminSub.addMonitoredItem(item);
      adminSub.synchronizeMonitoredItems();
      DataValue initial = adminInitial.get(5, TimeUnit.SECONDS);
      assertEquals(StatusCode.GOOD, initial.statusCode());
      assertArrayEquals(new ExtensionObject[0], (ExtensionObject[]) initial.value().value());
    } finally {
      adminSub.delete();
    }

    // Control: non-writable ns=0 nodes without overrides still return Bad_AttributeIdInvalid.
    assertRolePermissionsAbsent(
        admin, List.of(NodeIds.Server, NodeIds.Server_ServerDiagnostics_EnabledFlag));
    assertRolePermissionsAbsent(
        unprivileged, List.of(NodeIds.Server, NodeIds.Server_ServerDiagnostics_EnabledFlag));

    // SecurityAdmin immediately regains read, browse, and WriteRolePermissions access on both the
    // array and its dynamic element.
    assertEquals(
        StatusCode.GOOD,
        admin.readValue(0.0, TimestampsToReturn.Neither, securityArrayId).statusCode());
    assertEquals(
        StatusCode.GOOD, admin.readValue(0.0, TimestampsToReturn.Neither, elementId).statusCode());
    assertFalse(browseAll(admin, securityArrayId).isEmpty());

    // Unauthorized sessions remain denied across Value, Browse, and Write(RolePermissions).
    for (OpcUaClient denied : List.of(auditor, unprivileged)) {
      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          denied.readValue(0.0, TimestampsToReturn.Neither, securityArrayId).statusCode());
      assertEquals(List.of(), browseAll(denied, securityArrayId));
      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          writeRolePermissions(denied, securityArrayId, auditorPolicy));
    }

    // SecurityAdmin is not locked out and can write a new RolePermissions override over the wire.
    assertEquals(
        StatusCode.GOOD,
        writeRolePermissions(admin, securityArrayId, auditorPolicy),
        "SecurityAdmin can still write a new RolePermissions override after clearing");
    assertArrayEquals(
        auditorPolicy,
        readDecodedRolePermissions(auditor, securityArrayId, AttributeId.RolePermissions));
  }

  // Over the wire, Read(RolePermissions) and Write(RolePermissions) with no local override must
  // follow the session-scoped WriteMask filter decision (never reverting to unfiltered
  // node.getWriteMask()), treat raw null WriteMask from a managed-node filter as optional
  // (Bad_AttributeIdInvalid, not Bad_TypeMismatch), and fail closed if the WriteMask filter fails.
  @Test
  void sessionScopedWriteMaskFilterGovernsRolePermissionsExposureOverWire() throws Exception {
    RoleMapper roleMapper =
        identity -> {
          if (identity instanceof Identity.UsernameIdentity username) {
            return switch (username.getUsername()) {
              case "admin" -> List.of(NodeIds.WellKnownRole_SecurityAdmin);
              default -> List.of(NodeIds.WellKnownRole_AuthenticatedUser);
            };
          }
          return List.of(NodeIds.WellKnownRole_Anonymous);
        };

    TestServer testServer = TestServer.create(cfg -> cfg.setRoleMapper(roleMapper));
    server = testServer.getServer();

    ServerDiagnosticsTypeNode diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();
    SessionSecurityDiagnosticsArrayTypeNode securityArrayNode =
        diagnosticsNode
            .getSessionsDiagnosticsSummaryNode()
            .getSessionSecurityDiagnosticsArrayNode();

    var writableMask = uint(WriteMask.RolePermissions.getValue());
    var filterMode = new AtomicReference<>("clear-for-admin");

    securityArrayNode.setWriteMask(writableMask);
    securityArrayNode.setUserWriteMask(writableMask);
    securityArrayNode
        .getFilterChain()
        .addFirst(
            new AttributeFilter() {
              @Override
              public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
                  throws UaException {
                if (attributeId == AttributeId.WriteMask) {
                  boolean isAdmin =
                      ctx.getSession()
                          .flatMap(Session::getRoleIds)
                          .map(roles -> roles.contains(NodeIds.WellKnownRole_SecurityAdmin))
                          .orElse(false);
                  return switch (filterMode.get()) {
                    case "clear-for-admin" -> isAdmin ? uint(0) : writableMask;
                    case "set-for-all" -> writableMask;
                    case "return-null" -> null;
                    case "fail-internal" -> throw new UaException(StatusCodes.Bad_InternalError);
                    case "malformed" -> "not-a-mask";
                    default -> ctx.readAttribute(attributeId);
                  };
                } else if (attributeId == AttributeId.UserWriteMask
                    && "set-for-all".equals(filterMode.get())) {
                  return writableMask;
                }
                return ctx.readAttribute(attributeId);
              }
            });

    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient admin = connectSecure(testServer, new UsernameProvider("admin", "password"));
    OpcUaClient unprivileged = connectSecure(testServer, new UsernameProvider("user1", "password"));
    NodeId securityArrayId =
        NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray;
    var adminOverridePolicy =
        new RolePermissionType[] {
          new RolePermissionType(
              NodeIds.WellKnownRole_SecurityAdmin,
              PermissionType.of(
                  PermissionType.Field.Browse,
                  PermissionType.Field.Read,
                  PermissionType.Field.ReadRolePermissions,
                  PermissionType.Field.WriteRolePermissions))
        };

    // 1. Stored WriteMask retains RolePermissions (and UserWriteMask allows writing), but the
    // session filter clears WriteMask.RolePermissions for admin: admin must see
    // Bad_AttributeIdInvalid on Read and Bad_NotWritable on Write, while unprivileged still sees
    // the writable bit and is denied by the fallback policy.
    assertEquals(
        new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
        readAttribute(admin, securityArrayId, AttributeId.RolePermissions).statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_NotWritable),
        writeRolePermissions(admin, securityArrayId, adminOverridePolicy));
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(unprivileged, securityArrayId, AttributeId.RolePermissions).statusCode());

    // 2. Stored WriteMask and UserWriteMask are 0, while the session filter sets
    // RolePermissions on both: admin sees Good empty RolePermissionType[0] and can write/clear
    // RolePermissions over the wire, while unprivileged remains denied on both Read and Write.
    securityArrayNode.setWriteMask(uint(0));
    securityArrayNode.setUserWriteMask(uint(0));
    filterMode.set("set-for-all");
    assertArrayEquals(
        new RolePermissionType[0],
        readDecodedRolePermissions(admin, securityArrayId, AttributeId.RolePermissions));
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(unprivileged, securityArrayId, AttributeId.RolePermissions).statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        writeRolePermissions(unprivileged, securityArrayId, adminOverridePolicy));

    assertEquals(
        StatusCode.GOOD, writeRolePermissions(admin, securityArrayId, adminOverridePolicy));
    assertArrayEquals(
        adminOverridePolicy,
        readDecodedRolePermissions(admin, securityArrayId, AttributeId.RolePermissions));
    assertEquals(
        StatusCode.GOOD, writeRolePermissions(admin, securityArrayId, new RolePermissionType[0]));
    assertArrayEquals(
        new RolePermissionType[0],
        readDecodedRolePermissions(admin, securityArrayId, AttributeId.RolePermissions));

    // 3. A managed-node filter returning raw null for WriteMask translates to
    // Bad_AttributeIdInvalid consistently for WriteMask and RolePermissions reads over the wire
    // (not Bad_TypeMismatch) and Bad_NotWritable on Write.
    securityArrayNode.setWriteMask(writableMask);
    securityArrayNode.setUserWriteMask(writableMask);
    filterMode.set("return-null");
    for (OpcUaClient client : List.of(admin, unprivileged)) {
      assertEquals(
          new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
          readAttribute(client, securityArrayId, AttributeId.WriteMask).statusCode());
      assertEquals(
          new StatusCode(StatusCodes.Bad_AttributeIdInvalid),
          readAttribute(client, securityArrayId, AttributeId.RolePermissions).statusCode());
    }
    assertEquals(
        new StatusCode(StatusCodes.Bad_NotWritable),
        writeRolePermissions(admin, securityArrayId, adminOverridePolicy));

    // 4. Failing or malformed session-scoped WriteMask lookups fail closed on both Read and Write.
    filterMode.set("fail-internal");
    assertEquals(
        new StatusCode(StatusCodes.Bad_InternalError),
        readAttribute(admin, securityArrayId, AttributeId.RolePermissions).statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_InternalError),
        readAttribute(unprivileged, securityArrayId, AttributeId.RolePermissions).statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_InternalError),
        writeRolePermissions(admin, securityArrayId, adminOverridePolicy));

    filterMode.set("malformed");
    assertEquals(
        new StatusCode(StatusCodes.Bad_TypeMismatch),
        readAttribute(admin, securityArrayId, AttributeId.RolePermissions).statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_TypeMismatch),
        readAttribute(unprivileged, securityArrayId, AttributeId.RolePermissions).statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_TypeMismatch),
        writeRolePermissions(admin, securityArrayId, adminOverridePolicy));
  }

  // Setting RolePermissions on an ordinary namespace-0 node (such as Server i=2253) without also
  // setting UserRolePermissions must automatically derive session-filtered UserRolePermissions and
  // enforce ReadRolePermissions (for both mapped and unmapped sessions), while still preserving
  // application-supplied UserRolePermissions filters and keeping unconfigured namespace-0 nodes
  // returning Bad_AttributeIdInvalid.
  @Test
  void ordinaryNamespaceZeroNodeDerivesUserRolePermissionsAndEnforcesReadRolePermissions()
      throws Exception {
    var auditorRole = new NodeId(1, "ServerObjectAuditor");
    var auditorEntry =
        new RolePermissionType(
            auditorRole,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions,
                PermissionType.Field.WriteRolePermissions));
    var authenticatedEntry =
        new RolePermissionType(
            NodeIds.WellKnownRole_AuthenticatedUser,
            PermissionType.of(PermissionType.Field.Browse, PermissionType.Field.Read));
    var serverPolicy = new RolePermissionType[] {auditorEntry, authenticatedEntry};

    RoleMapper roleMapper =
        identity -> {
          if (identity instanceof Identity.UsernameIdentity username) {
            return switch (username.getUsername()) {
              case "admin" -> List.of(auditorRole);
              default -> List.of(NodeIds.WellKnownRole_AuthenticatedUser);
            };
          }
          return List.of(NodeIds.WellKnownRole_Anonymous);
        };

    TestServer testServer = TestServer.create(cfg -> cfg.setRoleMapper(roleMapper));
    server = testServer.getServer();

    UaNode serverNode =
        server.getAddressSpaceManager().getManagedNode(NodeIds.Server).orElseThrow();
    serverNode.setRolePermissions(serverPolicy);

    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient auditor = connectSecure(testServer, new UsernameProvider("admin", "password"));
    OpcUaClient authenticated =
        connectSecure(testServer, new UsernameProvider("user1", "password"));
    OpcUaClient anonymous = connectSecure(testServer, AnonymousProvider.INSTANCE);

    // 1. Unconfigured ordinary namespace-0 nodes continue returning Bad_AttributeIdInvalid.
    assertRolePermissionsAbsent(auditor, List.of(NodeIds.Server_ServerCapabilities));
    assertRolePermissionsAbsent(authenticated, List.of(NodeIds.Server_ServerCapabilities));

    // 2. Authorized auditor reads full RolePermissions and its own filtered UserRolePermissions.
    assertArrayEquals(
        serverPolicy,
        readDecodedRolePermissions(auditor, NodeIds.Server, AttributeId.RolePermissions));
    assertArrayEquals(
        new RolePermissionType[] {auditorEntry},
        readDecodedRolePermissions(auditor, NodeIds.Server, AttributeId.UserRolePermissions));

    // 3. Unauthorized AuthenticatedUser is denied RolePermissions and sees only its own entry in
    // UserRolePermissions.
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(authenticated, NodeIds.Server, AttributeId.RolePermissions).statusCode());
    assertArrayEquals(
        new RolePermissionType[] {authenticatedEntry},
        readDecodedRolePermissions(authenticated, NodeIds.Server, AttributeId.UserRolePermissions));

    // 4. Unlisted Anonymous role is denied RolePermissions and sees an empty UserRolePermissions.
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(anonymous, NodeIds.Server, AttributeId.RolePermissions).statusCode());
    assertArrayEquals(
        new RolePermissionType[0],
        readDecodedRolePermissions(anonymous, NodeIds.Server, AttributeId.UserRolePermissions));

    // 5. An application-supplied downstream (addLast) UserRolePermissions filter takes precedence
    // over the derived fallback.
    var narrowDownstream = new AtomicBoolean(true);
    AttributeFilter appDownstreamUrpFilter =
        new AttributeFilter() {
          @Override
          public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
              throws UaException {
            if (attributeId == AttributeId.UserRolePermissions && narrowDownstream.get()) {
              return new RolePermissionType[0];
            }
            return ctx.readAttribute(attributeId);
          }
        };
    serverNode.getFilterChain().addLast(appDownstreamUrpFilter);
    try {
      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          readAttribute(auditor, NodeIds.Server, AttributeId.RolePermissions).statusCode());
      assertArrayEquals(
          new RolePermissionType[0],
          readDecodedRolePermissions(auditor, NodeIds.Server, AttributeId.UserRolePermissions));
    } finally {
      narrowDownstream.set(false);
    }

    // 6. Clearing RolePermissions reverts Server back to Bad_AttributeIdInvalid for both
    // attributes; supplying RolePermissions via a front AttributeFilter derives UserRolePermissions
    // and enforces ReadRolePermissions identically.
    serverNode.setRolePermissions(null);
    assertRolePermissionsAbsent(auditor, List.of(NodeIds.Server));
    assertRolePermissionsAbsent(authenticated, List.of(NodeIds.Server));

    var frontFilterActive = new AtomicBoolean(true);
    serverNode
        .getFilterChain()
        .addFirst(
            new AttributeFilter() {
              @Override
              public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
                  throws UaException {
                if (attributeId == AttributeId.RolePermissions && frontFilterActive.get()) {
                  return serverPolicy;
                }
                return ctx.readAttribute(attributeId);
              }
            });

    assertArrayEquals(
        serverPolicy,
        readDecodedRolePermissions(auditor, NodeIds.Server, AttributeId.RolePermissions));
    assertArrayEquals(
        new RolePermissionType[] {auditorEntry},
        readDecodedRolePermissions(auditor, NodeIds.Server, AttributeId.UserRolePermissions));
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(authenticated, NodeIds.Server, AttributeId.RolePermissions).statusCode());
    assertArrayEquals(
        new RolePermissionType[] {authenticatedEntry},
        readDecodedRolePermissions(authenticated, NodeIds.Server, AttributeId.UserRolePermissions));

    // 7. Even without a RoleMapper, explicit RolePermissions on Server (i=2253) must deny an
    // unmapped anonymous client lacking Anonymous.ReadRolePermissions while leaving unconfigured
    // ordinary ns=0 nodes absent.
    tearDown();
    TestServer unmappedTestServer = TestServer.create();
    server = unmappedTestServer.getServer();
    UaNode unmappedServerNode =
        server.getAddressSpaceManager().getManagedNode(NodeIds.Server).orElseThrow();
    unmappedServerNode.setRolePermissions(new RolePermissionType[] {auditorEntry});
    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient unmappedAnonymous = connectSecure(unmappedTestServer, AnonymousProvider.INSTANCE);
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(unmappedAnonymous, NodeIds.Server, AttributeId.RolePermissions).statusCode());
    assertArrayEquals(
        new RolePermissionType[0],
        readDecodedRolePermissions(
            unmappedAnonymous, NodeIds.Server, AttributeId.UserRolePermissions));
    assertRolePermissionsAbsent(unmappedAnonymous, List.of(NodeIds.Server_ServerCapabilities));

    // 8. Without a RoleMapper, an ordinary namespace-0 node (carrying
    // RolePermissionsSupport.derivedUserRolePermissionsFilter) with RolePermissions == null,
    // writable WriteMask/UserWriteMask for RolePermissions, and an explicit empty
    // UserRolePermissions override must deny unmapped writes and reads of RolePermissions until
    // the explicit UserRolePermissions override is cleared.
    unmappedServerNode.setRolePermissions(null);
    var rolePermissionsWritableMask = uint(WriteMask.RolePermissions.getValue());
    unmappedServerNode.setWriteMask(rolePermissionsWritableMask);
    unmappedServerNode.setUserWriteMask(rolePermissionsWritableMask);
    unmappedServerNode.setUserRolePermissions(new RolePermissionType[0]);

    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        writeRolePermissions(
            unmappedAnonymous, NodeIds.Server, new RolePermissionType[] {auditorEntry}),
        "explicit empty UserRolePermissions must deny unmapped RolePermissions writes");
    assertEquals(
        null,
        unmappedServerNode.getRolePermissions(),
        "denied unmapped RolePermissions write must leave stored RolePermissions null");
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(unmappedAnonymous, NodeIds.Server, AttributeId.RolePermissions).statusCode(),
        "explicit empty UserRolePermissions must deny unmapped RolePermissions reads");

    unmappedServerNode.setUserRolePermissions(null);
    assertEquals(
        StatusCode.GOOD,
        writeRolePermissions(
            unmappedAnonymous, NodeIds.Server, new RolePermissionType[] {auditorEntry}),
        "clearing explicit empty UserRolePermissions restores unmapped RolePermissions write");
    assertArrayEquals(
        new RolePermissionType[] {auditorEntry}, unmappedServerNode.getRolePermissions());
  }

  // Built-in filters installed on namespace-0 nodes (RolePermissionsSupport) and diagnostics nodes
  // (Util, including diagnosticValueFilter) must forward both readAttribute() and writeAttribute()
  // rather than falling back to getAttribute()/setAttribute(), so application filters appended via
  // addLast() after initialization are honored on both reads and writes.
  @Test
  void downstreamWriteAttributeFilterOnNamespaceZeroAndDiagnosticsNodesRejectsWrites()
      throws Exception {
    RoleMapper roleMapper =
        identity -> {
          if (identity instanceof Identity.UsernameIdentity username
              && "admin".equals(username.getUsername())) {
            return List.of(NodeIds.WellKnownRole_SecurityAdmin);
          }
          return List.of(NodeIds.WellKnownRole_AuthenticatedUser);
        };

    TestServer testServer = TestServer.create(cfg -> cfg.setRoleMapper(roleMapper));
    server = testServer.getServer();

    // 1. Ordinary namespace-0 node (Server i=2253) with writable Description and an addLast
    // writeAttribute filter that rejects writes with Bad_OutOfRange.
    UaNode serverNode =
        server.getAddressSpaceManager().getManagedNode(NodeIds.Server).orElseThrow();
    LocalizedText originalDescription = LocalizedText.english("Original Server Description");
    serverNode.setDescription(originalDescription);
    var descWritableMask = uint(WriteMask.Description.getValue());
    serverNode.setWriteMask(descWritableMask);
    serverNode.setUserWriteMask(descWritableMask);
    serverNode
        .getFilterChain()
        .addLast(
            new AttributeFilter() {
              @Override
              public void writeAttribute(
                  AttributeFilterContext ctx, AttributeId attributeId, Object value)
                  throws UaException {
                if (attributeId == AttributeId.Description) {
                  throw new UaException(StatusCodes.Bad_OutOfRange);
                }
                ctx.writeAttribute(attributeId, value);
              }
            });

    // 2. Diagnostics node (ServerDiagnostics.EnabledFlag) with an addLast writeAttribute filter
    // that rejects Value writes once enabled.
    ServerDiagnosticsTypeNode diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();
    PropertyTypeNode enabledFlagNode = diagnosticsNode.getEnabledFlagNode();
    var vetoEnabledFlagWrite = new AtomicBoolean(false);
    enabledFlagNode
        .getFilterChain()
        .addLast(
            new AttributeFilter() {
              @Override
              public void writeAttribute(
                  AttributeFilterContext ctx, AttributeId attributeId, Object value)
                  throws UaException {
                if (attributeId == AttributeId.Value && vetoEnabledFlagWrite.get()) {
                  throw new UaException(StatusCodes.Bad_OutOfRange);
                }
                ctx.writeAttribute(attributeId, value);
              }
            });

    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient admin = connectSecure(testServer, new UsernameProvider("admin", "password"));

    // Enable diagnostics so dynamic SessionSecurityDiagnostics elements exist before enabling the
    // EnabledFlag write veto.
    assertEquals(
        StatusCode.GOOD,
        writeValue(
            admin,
            NodeIds.Server_ServerDiagnostics_EnabledFlag,
            DataValue.valueOnly(new Variant(Boolean.TRUE))));
    vetoEnabledFlagWrite.set(true);

    StatusCode descWriteStatus =
        admin.write(
                List.of(
                    new WriteValue(
                        NodeIds.Server,
                        AttributeId.Description.uid(),
                        null,
                        DataValue.valueOnly(
                            new Variant(LocalizedText.english("Rejected Description"))))))
            .getResults()[0];
    assertEquals(new StatusCode(StatusCodes.Bad_OutOfRange), descWriteStatus);
    assertEquals(
        originalDescription,
        serverNode.getDescription(),
        "rejected Description write must leave stored attribute unchanged");

    StatusCode flagWriteStatus =
        writeValue(
            admin,
            NodeIds.Server_ServerDiagnostics_EnabledFlag,
            DataValue.valueOnly(new Variant(Boolean.FALSE)));
    assertEquals(new StatusCode(StatusCodes.Bad_OutOfRange), flagWriteStatus);
    assertEquals(
        Boolean.TRUE,
        enabledFlagNode.getValue().value().value(),
        "rejected EnabledFlag write must leave stored Value unchanged");

    // 3. Initialized SessionSecurityDiagnosticsArray and a dynamic SessionSecurityDiagnostics
    // element (both carrying Util.diagnosticValueFilter) with addLast filters appended after
    // startup: writeAttribute(Description) must veto writes, and readAttribute(UserRolePermissions)
    // must be honored for Read(UserRolePermissions), Browse, and Read(Value).
    NodeId securityArrayId =
        NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray;
    List<ReferenceDescription> securityElements = browse(admin, securityArrayId);
    assertFalse(securityElements.isEmpty(), "expected dynamic SessionSecurityDiagnostics element");
    NodeId securityElementId =
        securityElements.get(0).getNodeId().toNodeId(admin.getNamespaceTable()).orElseThrow();

    for (NodeId diagnosticNodeId : List.of(securityArrayId, securityElementId)) {
      UaNode diagNode =
          server.getAddressSpaceManager().getManagedNode(diagnosticNodeId).orElseThrow();
      LocalizedText origDiagDesc = LocalizedText.english("Original Diag Description");
      diagNode.setDescription(origDiagDesc);
      diagNode.setWriteMask(descWritableMask);
      diagNode.setUserWriteMask(descWritableMask);

      var restrictUserRolePermissions = new AtomicBoolean(false);
      diagNode
          .getFilterChain()
          .addLast(
              new AttributeFilter() {
                @Override
                public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
                    throws UaException {
                  if (attributeId == AttributeId.UserRolePermissions
                      && restrictUserRolePermissions.get()) {
                    return new RolePermissionType[0];
                  }
                  return ctx.readAttribute(attributeId);
                }

                @Override
                public void writeAttribute(
                    AttributeFilterContext ctx, AttributeId attributeId, Object value)
                    throws UaException {
                  if (attributeId == AttributeId.Description) {
                    throw new UaException(StatusCodes.Bad_OutOfRange);
                  }
                  ctx.writeAttribute(attributeId, value);
                }
              });

      StatusCode diagDescWrite =
          admin.write(
                  List.of(
                      new WriteValue(
                          diagnosticNodeId,
                          AttributeId.Description.uid(),
                          null,
                          DataValue.valueOnly(
                              new Variant(LocalizedText.english("Rejected Diag Description"))))))
              .getResults()[0];
      assertEquals(
          new StatusCode(StatusCodes.Bad_OutOfRange),
          diagDescWrite,
          "addLast writeAttribute filter after Util.diagnosticValueFilter must veto writes on "
              + diagnosticNodeId);
      assertEquals(
          origDiagDesc,
          diagNode.getDescription(),
          "vetoed Description write must leave stored attribute unchanged on " + diagnosticNodeId);

      restrictUserRolePermissions.set(true);
      assertArrayEquals(
          new RolePermissionType[0],
          readDecodedRolePermissions(admin, diagnosticNodeId, AttributeId.UserRolePermissions),
          "addLast readAttribute(UserRolePermissions) after Util.diagnosticValueFilter must apply"
              + " on "
              + diagnosticNodeId);
      assertEquals(
          List.of(),
          browseAll(admin, diagnosticNodeId),
          "downstream empty UserRolePermissions must hide Browse results on " + diagnosticNodeId);
      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          readAttribute(admin, diagnosticNodeId, AttributeId.Value).statusCode(),
          "downstream empty UserRolePermissions must deny Value read on " + diagnosticNodeId);
    }
  }

  // If a downstream filter on ServerDiagnostics.EnabledFlag fails reading UserAccessLevel with
  // Bad_UserAccessDenied, Bad_InternalError, or Bad_AttributeIdInvalid (mandatory on Variables per
  // OPC 10000-3 §5.6.2), DefaultAccessController must fail closed and reject a non-admin write
  // rather than skipping the user-level write check.
  @Test
  void failedUserAccessLevelLookupDeniesRestrictedDiagnosticsWrite() throws Exception {
    RoleMapper roleMapper =
        identity -> {
          if (identity instanceof Identity.UsernameIdentity username
              && "admin".equals(username.getUsername())) {
            return List.of(NodeIds.WellKnownRole_SecurityAdmin);
          }
          return List.of(NodeIds.WellKnownRole_AuthenticatedUser);
        };

    TestServer testServer = TestServer.create(cfg -> cfg.setRoleMapper(roleMapper));
    server = testServer.getServer();

    ServerDiagnosticsTypeNode diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();
    PropertyTypeNode enabledFlagNode = diagnosticsNode.getEnabledFlagNode();

    var failureCode = new AtomicReference<>(new StatusCode(StatusCodes.Bad_UserAccessDenied));
    enabledFlagNode
        .getFilterChain()
        .addLast(
            new AttributeFilter() {
              @Override
              public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
                  throws UaException {
                if (attributeId == AttributeId.UserAccessLevel) {
                  throw new UaException(failureCode.get());
                }
                return ctx.readAttribute(attributeId);
              }
            });

    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient nonAdmin = connectSecure(testServer, new UsernameProvider("user1", "password"));

    StatusCode deniedStatus =
        writeValue(
            nonAdmin,
            NodeIds.Server_ServerDiagnostics_EnabledFlag,
            DataValue.valueOnly(new Variant(Boolean.TRUE)));
    assertEquals(new StatusCode(StatusCodes.Bad_UserAccessDenied), deniedStatus);
    assertEquals(
        Boolean.FALSE,
        enabledFlagNode.getValue().value().value(),
        "EnabledFlag must retain its original value when UserAccessLevel read throws"
            + " Bad_UserAccessDenied");

    failureCode.set(new StatusCode(StatusCodes.Bad_InternalError));
    StatusCode internalErrorStatus =
        writeValue(
            nonAdmin,
            NodeIds.Server_ServerDiagnostics_EnabledFlag,
            DataValue.valueOnly(new Variant(Boolean.TRUE)));
    assertEquals(new StatusCode(StatusCodes.Bad_InternalError), internalErrorStatus);
    assertEquals(
        Boolean.FALSE,
        enabledFlagNode.getValue().value().value(),
        "EnabledFlag must retain its original value when UserAccessLevel read throws"
            + " Bad_InternalError");

    failureCode.set(new StatusCode(StatusCodes.Bad_AttributeIdInvalid));
    StatusCode invalidAttrStatus =
        writeValue(
            nonAdmin,
            NodeIds.Server_ServerDiagnostics_EnabledFlag,
            DataValue.valueOnly(new Variant(Boolean.TRUE)));
    assertEquals(new StatusCode(StatusCodes.Bad_AttributeIdInvalid), invalidAttrStatus);
    assertEquals(
        Boolean.FALSE,
        enabledFlagNode.getValue().value().value(),
        "EnabledFlag must retain its original value when UserAccessLevel read throws"
            + " Bad_AttributeIdInvalid");
  }

  // Setting an explicit empty UserRolePermissions array on ServerDiagnostics.EnabledFlag (with a
  // RoleMapper configured and RolePermissions left null) must be honored by Util's diagnostics
  // filters so Browse is denied, UserAccessLevel drops CurrentRead/CurrentWrite even for
  // SecurityAdmin, and SecurityAdmin Value writes are rejected with Bad_UserAccessDenied.
  @Test
  void explicitEmptyUserRolePermissionsOnEnabledFlagDeniesBrowse() throws Exception {
    RoleMapper roleMapper =
        identity -> {
          if (identity instanceof Identity.UsernameIdentity username
              && "admin".equals(username.getUsername())) {
            return List.of(NodeIds.WellKnownRole_SecurityAdmin);
          }
          return List.of(NodeIds.WellKnownRole_AuthenticatedUser);
        };

    TestServer testServer = TestServer.create(cfg -> cfg.setRoleMapper(roleMapper));
    server = testServer.getServer();

    ServerDiagnosticsTypeNode diagnosticsNode =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();
    PropertyTypeNode enabledFlagNode = diagnosticsNode.getEnabledFlagNode();
    enabledFlagNode.setUserRolePermissions(new RolePermissionType[0]);

    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient user = connectSecure(testServer, new UsernameProvider("user1", "password"));
    OpcUaClient admin = connectSecure(testServer, new UsernameProvider("admin", "password"));
    OpcUaClient anonymous = connectSecure(testServer, AnonymousProvider.INSTANCE);
    NodeId enabledFlagId = NodeIds.Server_ServerDiagnostics_EnabledFlag;

    for (OpcUaClient client : List.of(user, admin, anonymous)) {
      assertFalse(
          browse(client, NodeIds.Server_ServerDiagnostics).stream()
              .anyMatch(r -> "EnabledFlag".equals(r.getBrowseName().name())),
          "EnabledFlag must be hidden from Browse when UserRolePermissions is explicitly empty");
      assertEquals(List.of(), browseAll(client, enabledFlagId));
      assertArrayEquals(
          new RolePermissionType[0],
          readDecodedRolePermissions(client, enabledFlagId, AttributeId.UserRolePermissions));
    }

    DataValue adminUserAccessLevel =
        readAttribute(admin, enabledFlagId, AttributeId.UserAccessLevel);
    assertEquals(StatusCode.GOOD, adminUserAccessLevel.statusCode());
    assertEquals(
        AccessLevel.NONE,
        AccessLevel.fromValue((UByte) adminUserAccessLevel.value().value()),
        "explicit empty UserRolePermissions must remove CurrentRead/CurrentWrite from"
            + " UserAccessLevel even for SecurityAdmin");
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        writeValue(admin, enabledFlagId, DataValue.valueOnly(new Variant(Boolean.TRUE))),
        "explicit empty UserRolePermissions must reject SecurityAdmin write to EnabledFlag");
    assertEquals(
        Boolean.FALSE,
        enabledFlagNode.getValue().value().value(),
        "rejected EnabledFlag write must leave stored Value unchanged");

    // Clearing the explicit UserRolePermissions restores the default fallback policy and
    // SecurityAdmin write access.
    enabledFlagNode.setUserRolePermissions(null);
    assertTrue(
        browse(user, NodeIds.Server_ServerDiagnostics).stream()
            .anyMatch(r -> "EnabledFlag".equals(r.getBrowseName().name())),
        "clearing explicit UserRolePermissions restores default Browse access on EnabledFlag");
    assertRolePermissionsAbsent(user, List.of(enabledFlagId));
    DataValue restoredAdminAccess =
        readAttribute(admin, enabledFlagId, AttributeId.UserAccessLevel);
    assertEquals(StatusCode.GOOD, restoredAdminAccess.statusCode());
    assertEquals(
        AccessLevel.READ_WRITE,
        AccessLevel.fromValue((UByte) restoredAdminAccess.value().value()),
        "clearing explicit UserRolePermissions restores SecurityAdmin READ_WRITE access");
    assertEquals(
        StatusCode.GOOD,
        writeValue(admin, enabledFlagId, DataValue.valueOnly(new Variant(Boolean.TRUE))),
        "clearing explicit UserRolePermissions restores SecurityAdmin write access to EnabledFlag");
    assertEquals(Boolean.TRUE, enabledFlagNode.getValue().value().value());
  }

  // Installing an explicit empty UserRolePermissions restriction ONLY on
  // SessionSecurityDiagnosticsArray (leaving RolePermissions null everywhere and leaving dynamic
  // elements/fields untouched) must immediately remove CurrentRead and deny Value reads, Browse,
  // and UserRolePermissions on already-created dynamic SessionSecurityDiagnostics elements and
  // their child fields (such as ClientCertificate) even for SecurityAdmin.
  @Test
  void explicitEmptyUserRolePermissionsOnSecurityArrayRestrictsExistingElementsAndFields()
      throws Exception {
    RoleMapper roleMapper =
        identity -> {
          if (identity instanceof Identity.UsernameIdentity username
              && "admin".equals(username.getUsername())) {
            return List.of(NodeIds.WellKnownRole_SecurityAdmin);
          }
          return List.of(NodeIds.WellKnownRole_AuthenticatedUser);
        };

    TestServer testServer = TestServer.create(cfg -> cfg.setRoleMapper(roleMapper));
    server = testServer.getServer();
    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient admin = connectSecure(testServer, new UsernameProvider("admin", "password"));

    assertEquals(
        StatusCode.GOOD,
        writeValue(
            admin,
            NodeIds.Server_ServerDiagnostics_EnabledFlag,
            DataValue.valueOnly(new Variant(Boolean.TRUE))));

    NodeId securityArrayId =
        NodeIds.Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray;
    List<ReferenceDescription> elements = browse(admin, securityArrayId);
    assertFalse(elements.isEmpty(), "expected dynamic SessionSecurityDiagnostics element");
    NodeId elementId =
        elements.get(0).getNodeId().toNodeId(admin.getNamespaceTable()).orElseThrow();
    NodeId clientCertId = childNodeId(admin, elementId, "ClientCertificate");

    List<NodeId> restrictedNodes = List.of(securityArrayId, elementId, clientCertId);

    // Baseline: SecurityAdmin can read UserAccessLevel (CurrentRead) and Value on array, element,
    // and ClientCertificate while RolePermissions/UserRolePermissions stay omitted.
    assertRolePermissionsAbsent(admin, restrictedNodes);
    for (NodeId nodeId : restrictedNodes) {
      DataValue ual = readAttribute(admin, nodeId, AttributeId.UserAccessLevel);
      assertEquals(StatusCode.GOOD, ual.statusCode());
      assertEquals(AccessLevel.READ_ONLY, AccessLevel.fromValue((UByte) ual.value().value()));
      assertEquals(StatusCode.GOOD, readAttribute(admin, nodeId, AttributeId.Value).statusCode());
    }

    // Apply explicit empty UserRolePermissions ONLY on the source array node.
    SessionSecurityDiagnosticsArrayTypeNode securityArrayNode =
        (SessionSecurityDiagnosticsArrayTypeNode)
            server.getAddressSpaceManager().getManagedNode(securityArrayId).orElseThrow();
    securityArrayNode.setUserRolePermissions(new RolePermissionType[0]);

    for (NodeId nodeId : restrictedNodes) {
      DataValue ual = readAttribute(admin, nodeId, AttributeId.UserAccessLevel);
      assertEquals(StatusCode.GOOD, ual.statusCode());
      assertEquals(
          AccessLevel.NONE,
          AccessLevel.fromValue((UByte) ual.value().value()),
          "array-only explicit empty UserRolePermissions must remove CurrentRead on " + nodeId);
      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          readAttribute(admin, nodeId, AttributeId.Value).statusCode(),
          "array-only explicit empty UserRolePermissions must deny Value read on " + nodeId);
      assertArrayEquals(
          new RolePermissionType[0],
          readDecodedRolePermissions(admin, nodeId, AttributeId.UserRolePermissions),
          "array-only explicit empty UserRolePermissions must propagate UserRolePermissions to "
              + nodeId);
      assertEquals(
          List.of(),
          browseAll(admin, nodeId),
          "array-only explicit empty UserRolePermissions must deny Browse on " + nodeId);
    }

    // Clearing the array's explicit UserRolePermissions immediately restores default SecurityAdmin
    // access and omitted RolePermissions/UserRolePermissions on array, element, and field.
    securityArrayNode.setUserRolePermissions(null);
    assertRolePermissionsAbsent(admin, restrictedNodes);
    for (NodeId nodeId : restrictedNodes) {
      DataValue ual = readAttribute(admin, nodeId, AttributeId.UserAccessLevel);
      assertEquals(StatusCode.GOOD, ual.statusCode());
      assertEquals(AccessLevel.READ_ONLY, AccessLevel.fromValue((UByte) ual.value().value()));
      assertEquals(StatusCode.GOOD, readAttribute(admin, nodeId, AttributeId.Value).statusCode());
    }
  }

  // When RolePermissions is populated (e.g. granting Anonymous.ReadRolePermissions) AND explicit
  // UserRolePermissions restricts access (empty array or admin-only override), BOTH wire Read and
  // direct session-scoped UaNode.readAttribute(context, RolePermissions) must deny unauthorized
  // sessions consistently with Bad_UserAccessDenied.
  @Test
  void populatedRolePermissionsDirectAndWireReadsHonorExplicitUserRolePermissions()
      throws Exception {
    RoleMapper roleMapper =
        identity -> {
          if (identity instanceof Identity.UsernameIdentity username
              && "admin".equals(username.getUsername())) {
            return List.of(NodeIds.WellKnownRole_SecurityAdmin);
          }
          return List.of(NodeIds.WellKnownRole_Anonymous);
        };

    TestServer testServer = TestServer.create(cfg -> cfg.setRoleMapper(roleMapper));
    server = testServer.getServer();

    var anonymousReadRp =
        new RolePermissionType[] {
          new RolePermissionType(
              NodeIds.WellKnownRole_Anonymous,
              PermissionType.of(PermissionType.Field.ReadRolePermissions))
        };
    var adminOnlyReadRp =
        new RolePermissionType[] {
          new RolePermissionType(
              NodeIds.WellKnownRole_SecurityAdmin,
              PermissionType.of(PermissionType.Field.ReadRolePermissions))
        };

    UaNode serverNode =
        server.getAddressSpaceManager().getManagedNode(NodeIds.Server).orElseThrow();
    serverNode.setRolePermissions(anonymousReadRp);

    server.startup().get(10, TimeUnit.SECONDS);

    OpcUaClient anonymous = connectSecure(testServer, AnonymousProvider.INSTANCE);
    Session anonymousSession = server.getSessionManager().getAllSessions().iterator().next();
    AccessContext anonymousContext = () -> Optional.of(anonymousSession);

    OpcUaClient admin = connectSecure(testServer, new UsernameProvider("admin", "password"));
    Session adminSession =
        server.getSessionManager().getAllSessions().stream()
            .filter(s -> s != anonymousSession)
            .findFirst()
            .orElseThrow();
    AccessContext adminContext = () -> Optional.of(adminSession);

    // 1. Baseline: no explicit UserRolePermissions -> both wire and direct reads succeed.
    assertArrayEquals(
        anonymousReadRp,
        readDecodedRolePermissions(anonymous, NodeIds.Server, AttributeId.RolePermissions));
    assertArrayEquals(
        anonymousReadRp,
        (RolePermissionType[])
            serverNode.readAttribute(anonymousContext, AttributeId.RolePermissions));

    // 2. Explicit empty UserRolePermissions denies both wire and direct reads for both anonymous
    // and admin even though RolePermissions grants Anonymous.ReadRolePermissions.
    serverNode.setUserRolePermissions(new RolePermissionType[0]);
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(anonymous, NodeIds.Server, AttributeId.RolePermissions).statusCode());
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(admin, NodeIds.Server, AttributeId.RolePermissions).statusCode());
    assertEquals(
        StatusCodes.Bad_UserAccessDenied,
        assertThrows(
                UaException.class,
                () -> serverNode.readAttribute(anonymousContext, AttributeId.RolePermissions))
            .getStatusCode()
            .getValue());
    assertEquals(
        StatusCodes.Bad_UserAccessDenied,
        assertThrows(
                UaException.class,
                () -> serverNode.readAttribute(adminContext, AttributeId.RolePermissions))
            .getStatusCode()
            .getValue());

    // 3. Restricting UserRolePermissions to SecurityAdmin denies anonymous on both wire and direct
    // reads while allowing SecurityAdmin on both.
    serverNode.setUserRolePermissions(adminOnlyReadRp);
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(anonymous, NodeIds.Server, AttributeId.RolePermissions).statusCode());
    assertEquals(
        StatusCodes.Bad_UserAccessDenied,
        assertThrows(
                UaException.class,
                () -> serverNode.readAttribute(anonymousContext, AttributeId.RolePermissions))
            .getStatusCode()
            .getValue());
    assertArrayEquals(
        anonymousReadRp,
        readDecodedRolePermissions(admin, NodeIds.Server, AttributeId.RolePermissions));
    assertArrayEquals(
        anonymousReadRp,
        (RolePermissionType[]) serverNode.readAttribute(adminContext, AttributeId.RolePermissions));

    // 4. Clearing explicit UserRolePermissions restores both wire and direct reads for anonymous.
    serverNode.setUserRolePermissions(null);
    assertArrayEquals(
        anonymousReadRp,
        readDecodedRolePermissions(anonymous, NodeIds.Server, AttributeId.RolePermissions));
    assertArrayEquals(
        anonymousReadRp,
        (RolePermissionType[])
            serverNode.readAttribute(anonymousContext, AttributeId.RolePermissions));
  }

  // Standard namespace-0 nodes carrying built-in AttributeFilters.getValue(...) (such as
  // Server.NamespaceArray) must forward non-Value readAttribute() and writeAttribute() calls to
  // downstream filters appended via addLast(). When a downstream filter restricts
  // readAttribute(UserRolePermissions) to empty, BOTH wire Read(RolePermissions) and direct SDK
  // UaNode.readAttribute(context, RolePermissions) must deny access even if stored RolePermissions
  // grants Anonymous.ReadRolePermissions.
  @Test
  void namespaceArrayDownstreamReadAttributeUserRolePermissionsDeniesDirectAndWireReads()
      throws Exception {
    TestServer testServer = TestServer.create();
    server = testServer.getServer();
    server.startup().get(10, TimeUnit.SECONDS);

    var anonymousReadRp =
        new RolePermissionType[] {
          new RolePermissionType(
              NodeIds.WellKnownRole_Anonymous,
              PermissionType.of(PermissionType.Field.ReadRolePermissions))
        };

    UaNode namespaceArrayNode =
        server.getAddressSpaceManager().getManagedNode(NodeIds.Server_NamespaceArray).orElseThrow();
    namespaceArrayNode.setRolePermissions(anonymousReadRp);
    LocalizedText originalDescription = LocalizedText.english("Original NamespaceArray Desc");
    namespaceArrayNode.setDescription(originalDescription);
    var descWritableMask = uint(WriteMask.Description.getValue());
    namespaceArrayNode.setWriteMask(descWritableMask);
    namespaceArrayNode.setUserWriteMask(descWritableMask);

    var restrictUserRolePermissions = new AtomicBoolean(false);
    namespaceArrayNode
        .getFilterChain()
        .addLast(
            new AttributeFilter() {
              @Override
              public Object readAttribute(AttributeFilterContext ctx, AttributeId attributeId)
                  throws UaException {
                if (attributeId == AttributeId.UserRolePermissions
                    && restrictUserRolePermissions.get()) {
                  return new RolePermissionType[0];
                }
                return ctx.readAttribute(attributeId);
              }

              @Override
              public void writeAttribute(
                  AttributeFilterContext ctx, AttributeId attributeId, Object value)
                  throws UaException {
                if (attributeId == AttributeId.Description) {
                  throw new UaException(StatusCodes.Bad_OutOfRange);
                }
                ctx.writeAttribute(attributeId, value);
              }
            });

    OpcUaClient anonymous = connectSecure(testServer, AnonymousProvider.INSTANCE);
    Session anonymousSession = server.getSessionManager().getAllSessions().iterator().next();
    AccessContext anonymousContext = () -> Optional.of(anonymousSession);

    // Baseline: AttributeFilters.getValue still serves Value; wire and direct RolePermissions reads
    // succeed while downstream writeAttribute(Description) vetoes Description writes.
    assertEquals(
        StatusCode.GOOD,
        readAttribute(anonymous, NodeIds.Server_NamespaceArray, AttributeId.Value).statusCode());
    assertArrayEquals(
        anonymousReadRp,
        readDecodedRolePermissions(
            anonymous, NodeIds.Server_NamespaceArray, AttributeId.RolePermissions));
    assertArrayEquals(
        anonymousReadRp,
        (RolePermissionType[])
            namespaceArrayNode.readAttribute(anonymousContext, AttributeId.RolePermissions));

    StatusCode descWrite =
        anonymous.write(
                List.of(
                    new WriteValue(
                        NodeIds.Server_NamespaceArray,
                        AttributeId.Description.uid(),
                        null,
                        DataValue.valueOnly(
                            new Variant(LocalizedText.english("Rejected NamespaceArray Desc"))))))
            .getResults()[0];
    assertEquals(new StatusCode(StatusCodes.Bad_OutOfRange), descWrite);
    assertEquals(originalDescription, namespaceArrayNode.getDescription());

    // Activate downstream readAttribute(UserRolePermissions) restriction after
    // AttributeFilters.getValue: both wire Read and direct UaNode.readAttribute must deny access.
    restrictUserRolePermissions.set(true);
    assertArrayEquals(
        new RolePermissionType[0],
        readDecodedRolePermissions(
            anonymous, NodeIds.Server_NamespaceArray, AttributeId.UserRolePermissions));
    assertEquals(
        new StatusCode(StatusCodes.Bad_UserAccessDenied),
        readAttribute(anonymous, NodeIds.Server_NamespaceArray, AttributeId.RolePermissions)
            .statusCode());
    assertEquals(
        StatusCodes.Bad_UserAccessDenied,
        assertThrows(
                UaException.class,
                () ->
                    namespaceArrayNode.readAttribute(anonymousContext, AttributeId.RolePermissions))
            .getStatusCode()
            .getValue());

    // Disabling the downstream restriction restores both wire and direct RolePermissions reads.
    restrictUserRolePermissions.set(false);
    assertArrayEquals(
        anonymousReadRp,
        readDecodedRolePermissions(
            anonymous, NodeIds.Server_NamespaceArray, AttributeId.RolePermissions));
    assertArrayEquals(
        anonymousReadRp,
        (RolePermissionType[])
            namespaceArrayNode.readAttribute(anonymousContext, AttributeId.RolePermissions));
  }

  private static StatusCode writeRolePermissions(
      OpcUaClient client, NodeId nodeId, RolePermissionType[] rolePermissions) throws UaException {

    return client.write(
            List.of(
                new WriteValue(
                    nodeId,
                    AttributeId.RolePermissions.uid(),
                    null,
                    DataValue.valueOnly(new Variant(rolePermissions)))))
        .getResults()[0];
  }

  private void assertRolePermissionsAbsent(OpcUaClient client, List<NodeId> nodeIds)
      throws UaException {

    var expected = new StatusCode(StatusCodes.Bad_AttributeIdInvalid);
    var readValueIds = new ArrayList<ReadValueId>(nodeIds.size() * 2);
    for (NodeId nodeId : nodeIds) {
      readValueIds.add(readId(nodeId, AttributeId.RolePermissions));
      readValueIds.add(readId(nodeId, AttributeId.UserRolePermissions));
    }

    DataValue[] results = client.read(0.0, TimestampsToReturn.Neither, readValueIds).getResults();
    for (int i = 0; i < nodeIds.size(); i++) {
      NodeId nodeId = nodeIds.get(i);
      assertEquals(
          expected, results[i * 2].statusCode(), nodeId + " RolePermissions must be unsupported");
      assertEquals(
          expected,
          results[i * 2 + 1].statusCode(),
          nodeId + " UserRolePermissions must be unsupported");
    }
  }

  private void assertDefaultRolePermissionPropertiesRemoved(OpcUaClient client) throws UaException {
    DataValue[] results =
        client
            .read(
                0.0,
                TimestampsToReturn.Neither,
                List.of(
                    readId(
                        NodeIds.OPCUANamespaceMetadata_DefaultRolePermissions, AttributeId.Value),
                    readId(
                        NodeIds.OPCUANamespaceMetadata_DefaultUserRolePermissions,
                        AttributeId.Value)))
            .getResults();

    var nodeIdUnknown = new StatusCode(StatusCodes.Bad_NodeIdUnknown);
    assertEquals(
        nodeIdUnknown,
        results[0].statusCode(),
        "OPCUANamespaceMetadata.DefaultRolePermissions must not exist");
    assertEquals(
        nodeIdUnknown,
        results[1].statusCode(),
        "OPCUANamespaceMetadata.DefaultUserRolePermissions must not exist");

    List<String> metadataChildren =
        browse(client, NodeIds.OPCUANamespaceMetadata).stream()
            .map(r -> r.getBrowseName().name())
            .toList();
    assertTrue(
        metadataChildren.contains("NamespaceUri"),
        "OPCUANamespaceMetadata still exposes its standard properties");
    assertFalse(
        metadataChildren.contains("DefaultRolePermissions"),
        "OPCUANamespaceMetadata browse results must omit DefaultRolePermissions");
    assertFalse(
        metadataChildren.contains("DefaultUserRolePermissions"),
        "OPCUANamespaceMetadata browse results must omit DefaultUserRolePermissions");
  }

  private OpcUaClient connectSecure(TestServer testServer, IdentityProvider identityProvider)
      throws Exception {

    //noinspection resource
    var trustList = new MemoryTrustListManager();
    trustList.addTrustedCertificate(
        server.getConfig().getEndpoints().iterator().next().getCertificate());

    OpcUaClient client =
        OpcUaClient.create(
            server.getConfig().getEndpoints().iterator().next().getEndpointUrl(),
            endpoints ->
                endpoints.stream()
                    .filter(
                        e ->
                            SecurityPolicy.Basic256Sha256.getUri().equals(e.getSecurityPolicyUri())
                                && e.getSecurityMode() == MessageSecurityMode.SignAndEncrypt)
                    .findFirst(),
            transportBuilder -> {},
            builder ->
                builder
                    .setApplicationName(LocalizedText.english("eclipse milo test client"))
                    .setCertificateIdentity(
                        testServer.getClientKeyPair(), testServer.getClientCertificateChain())
                    .setCertificateValidator(
                        new DefaultClientCertificateValidator(
                            trustList, new MemoryCertificateQuarantine()))
                    .setIdentityProvider(identityProvider)
                    .setRequestTimeout(uint(5_000)));

    client.connectAsync().get(10, TimeUnit.SECONDS);
    clients.add(client);
    return client;
  }

  private static ReadValueId readId(NodeId nodeId, AttributeId attributeId) {
    return new ReadValueId(nodeId, attributeId.uid(), null, QualifiedName.NULL_VALUE);
  }

  private static DataValue readAttribute(OpcUaClient client, NodeId nodeId, AttributeId attributeId)
      throws UaException {

    return client.read(0.0, TimestampsToReturn.Neither, List.of(readId(nodeId, attributeId)))
        .getResults()[0];
  }

  private static StatusCode writeValue(OpcUaClient client, NodeId nodeId, DataValue value)
      throws UaException {

    return client.writeValues(List.of(nodeId), List.of(value)).get(0);
  }

  private static RolePermissionType[] readDecodedRolePermissions(
      OpcUaClient client, NodeId nodeId, AttributeId attributeId) throws UaException {

    DataValue dataValue = readAttribute(client, nodeId, attributeId);
    assertEquals(StatusCode.GOOD, dataValue.statusCode(), nodeId + " " + attributeId + " status");

    Object raw = dataValue.value().value();
    if (raw instanceof RolePermissionType[] rolePermissions) {
      return rolePermissions;
    } else if (raw instanceof ExtensionObject[] xos) {
      var decoded = new RolePermissionType[xos.length];
      for (int i = 0; i < xos.length; i++) {
        decoded[i] = (RolePermissionType) xos[i].decode(client.getStaticEncodingContext());
      }
      return decoded;
    } else {
      throw new AssertionError("unexpected " + attributeId + " payload: " + raw);
    }
  }

  private static List<ReferenceDescription> browse(OpcUaClient client, NodeId nodeId)
      throws UaException {

    var description =
        new BrowseDescription(
            nodeId,
            BrowseDirection.Forward,
            NodeIds.HierarchicalReferences,
            true,
            uint(0),
            uint(BrowseResultMask.All.getValue()));

    BrowseResult result = client.browse(description);
    assertTrue(
        result.getStatusCode().isGood(),
        "Browse of " + nodeId + " failed with " + result.getStatusCode());
    ReferenceDescription[] references = result.getReferences();
    return references == null ? List.of() : List.of(references);
  }

  private static List<ReferenceDescription> browseAll(OpcUaClient client, NodeId nodeId)
      throws UaException {

    var description =
        new BrowseDescription(
            nodeId,
            BrowseDirection.Both,
            NodeIds.References,
            true,
            uint(0),
            uint(BrowseResultMask.All.getValue()));

    BrowseResult result = client.browse(description);
    assertTrue(
        result.getStatusCode().isGood(),
        "Browse of " + nodeId + " failed with " + result.getStatusCode());
    ReferenceDescription[] references = result.getReferences();
    return references == null ? List.of() : List.of(references);
  }

  private static BrowsePathResult translatePath(
      OpcUaClient client, NodeId startingNode, String targetBrowseName) throws UaException {

    var path =
        new BrowsePath(
            startingNode,
            new RelativePath(
                new RelativePathElement[] {
                  new RelativePathElement(
                      NodeIds.HierarchicalReferences,
                      false,
                      true,
                      new QualifiedName(0, targetBrowseName))
                }));

    return client.translateBrowsePaths(List.of(path)).getResults()[0];
  }

  private static NodeId childNodeId(OpcUaClient client, NodeId parent, String browseName)
      throws UaException {

    return browse(client, parent).stream()
        .filter(r -> r.getBrowseName().name().equals(browseName))
        .findFirst()
        .flatMap(r -> r.getNodeId().toNodeId(client.getNamespaceTable()))
        .orElseThrow(() -> new AssertionError("no child named " + browseName));
  }
}
