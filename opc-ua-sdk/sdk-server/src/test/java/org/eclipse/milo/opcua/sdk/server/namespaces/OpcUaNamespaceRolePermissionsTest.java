/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.namespaces;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.AccessContext;
import org.eclipse.milo.opcua.sdk.server.AttributeReader;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.model.objects.NamespaceMetadataTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.PermissionType;
import org.eclipse.milo.opcua.stack.core.types.structured.RolePermissionType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Verifies that namespace 0 omits default role permission metadata so {@code RolePermissions} and
 * {@code UserRolePermissions} stay symmetric (OPC 10000-3 §5.2.9, §5.2.10; OPC 10000-5 §6.3.13).
 */
class OpcUaNamespaceRolePermissionsTest {

  private static OpcUaServer server;

  @BeforeAll
  static void createServer() {
    var config =
        OpcUaServerConfig.builder().setCertificateManager(new DefaultCertificateManager()).build();
    server = new OpcUaServer(config, transportProfile -> null);
  }

  // OPC 10000-3 §5.2.9 & §5.2.10: publishing RolePermissions without UserRolePermissions is
  // invalid. Every loaded namespace-0 node must start with both attributes cleared.
  @Test
  void allLoadedNamespaceZeroNodesOmitRolePermissionsAndUserRolePermissions() {
    List<UaNode> nodes = server.getOpcUaNamespace().getNodeManager().getNodes();
    assertFalse(nodes.isEmpty(), "namespace 0 must contain loaded standard nodes");

    for (UaNode node : nodes) {
      assertNull(
          node.getRolePermissions(),
          () -> "node " + node.getNodeId() + " (" + node.getBrowseName() + ") has RolePermissions");
      assertNull(
          node.getUserRolePermissions(),
          () ->
              "node "
                  + node.getNodeId()
                  + " ("
                  + node.getBrowseName()
                  + ") has UserRolePermissions");
    }
  }

  // OPC 10000-3 §5.2.9 & OPC 10000-5 §6.3.13: when a server does not publish role permissions for
  // namespace 0, OPCUANamespaceMetadata must omit DefaultRolePermissions and
  // DefaultUserRolePermissions while keeping the type definition's InstanceDeclarations intact.
  @Test
  void opcUaNamespaceMetadataOmitsDefaultRolePermissionsProperties() {
    assertFalse(
        server
            .getOpcUaNamespace()
            .getNodeManager()
            .containsNode(NodeIds.OPCUANamespaceMetadata_DefaultRolePermissions),
        "OPCUANamespaceMetadata.DefaultRolePermissions (i=16134) must be deleted");
    assertFalse(
        server
            .getOpcUaNamespace()
            .getNodeManager()
            .containsNode(NodeIds.OPCUANamespaceMetadata_DefaultUserRolePermissions),
        "OPCUANamespaceMetadata.DefaultUserRolePermissions (i=16135) must be deleted");

    var metadata =
        (NamespaceMetadataTypeNode)
            server
                .getOpcUaNamespace()
                .getNodeManager()
                .getNode(NodeIds.OPCUANamespaceMetadata)
                .orElseThrow();

    assertNull(metadata.getDefaultRolePermissionsNode());
    assertNull(metadata.getDefaultUserRolePermissionsNode());
    assertNotNull(metadata.getNamespaceUriNode(), "OPCUANamespaceMetadata keeps its other nodes");

    List<NodeId> referencedTargets =
        metadata.getReferences().stream()
            .filter(Reference::isForward)
            .flatMap(r -> r.getTargetNodeId().toNodeId(server.getNamespaceTable()).stream())
            .toList();
    assertFalse(
        referencedTargets.contains(NodeIds.OPCUANamespaceMetadata_DefaultRolePermissions),
        "OPCUANamespaceMetadata must not reference DefaultRolePermissions");
    assertFalse(
        referencedTargets.contains(NodeIds.OPCUANamespaceMetadata_DefaultUserRolePermissions),
        "OPCUANamespaceMetadata must not reference DefaultUserRolePermissions");

    // Control assertion: NamespaceMetadataType's InstanceDeclarations remain available for
    // applications instantiating NamespaceMetadataType in custom namespaces.
    assertTrue(
        server
            .getOpcUaNamespace()
            .getNodeManager()
            .containsNode(NodeIds.NamespaceMetadataType_DefaultRolePermissions),
        "NamespaceMetadataType.DefaultRolePermissions declaration must remain");
    assertTrue(
        server
            .getOpcUaNamespace()
            .getNodeManager()
            .containsNode(NodeIds.NamespaceMetadataType_DefaultUserRolePermissions),
        "NamespaceMetadataType.DefaultUserRolePermissions declaration must remain");
  }

  static Stream<Arguments> standardNodes() {
    return Stream.of(
        Arguments.of("Server", NodeIds.Server),
        Arguments.of("ServerCapabilities", NodeIds.Server_ServerCapabilities),
        Arguments.of("ObjectsFolder", NodeIds.ObjectsFolder),
        Arguments.of("EnabledFlag", NodeIds.Server_ServerDiagnostics_EnabledFlag),
        Arguments.of(
            "SessionSecurityDiagnosticsArray",
            NodeIds
                .Server_ServerDiagnostics_SessionsDiagnosticsSummary_SessionSecurityDiagnosticsArray));
  }

  // OPC 10000-3 §5.2.9 & §5.2.10: standard namespace-0 nodes must answer Bad_AttributeIdInvalid
  // for both RolePermissions and UserRolePermissions whether or not the Session has roles.
  @ParameterizedTest(name = "{0}")
  @MethodSource("standardNodes")
  void standardNodesReturnBadAttributeIdInvalidForRoleAndUserRolePermissions(
      String name, NodeId nodeId) {

    UaNode node = server.getAddressSpaceManager().getManagedNode(nodeId).orElseThrow();
    var expected = new StatusCode(StatusCodes.Bad_AttributeIdInvalid);

    Session noRoles = sessionWithRoles(null);
    Session anonymousRole = sessionWithRoles(List.of(NodeIds.WellKnownRole_Anonymous));
    Session securityAdmin = sessionWithRoles(List.of(NodeIds.WellKnownRole_SecurityAdmin));

    for (Session session : List.of(noRoles, anonymousRole, securityAdmin)) {
      AccessContext context = () -> Optional.of(session);

      DataValue rolePermissions =
          AttributeReader.readAttribute(
              context, node, AttributeId.RolePermissions, TimestampsToReturn.Neither, null, null);
      DataValue userRolePermissions =
          AttributeReader.readAttribute(
              context,
              node,
              AttributeId.UserRolePermissions,
              TimestampsToReturn.Neither,
              null,
              null);

      assertEquals(expected, rolePermissions.statusCode(), name + " RolePermissions");
      assertEquals(expected, userRolePermissions.statusCode(), name + " UserRolePermissions");
    }
  }

  // Setting RolePermissions on an ordinary namespace-0 node (e.g. Server i=2253) derives
  // session-filtered UserRolePermissions and enforces ReadRolePermissions while leaving the
  // sessionless getter null and reverting cleanly to Bad_AttributeIdInvalid when cleared.
  @Test
  void explicitRolePermissionsOnOrdinaryNamespaceZeroNodeDerivesUserRolePermissions() {
    UaNode serverNode =
        server.getAddressSpaceManager().getManagedNode(NodeIds.Server).orElseThrow();
    var adminEntry =
        new RolePermissionType(
            NodeIds.WellKnownRole_SecurityAdmin,
            PermissionType.of(
                PermissionType.Field.Browse,
                PermissionType.Field.Read,
                PermissionType.Field.ReadRolePermissions));
    var userEntry =
        new RolePermissionType(
            NodeIds.WellKnownRole_AuthenticatedUser,
            PermissionType.of(PermissionType.Field.Browse, PermissionType.Field.Read));
    var policy = new RolePermissionType[] {adminEntry, userEntry};

    Session securityAdmin = sessionWithRoles(List.of(NodeIds.WellKnownRole_SecurityAdmin));
    Session authenticated = sessionWithRoles(List.of(NodeIds.WellKnownRole_AuthenticatedUser));

    serverNode.setRolePermissions(policy);
    try {
      assertNull(serverNode.getUserRolePermissions(), "sessionless UserRolePermissions stays null");

      DataValue adminRp =
          AttributeReader.readAttribute(
              () -> Optional.of(securityAdmin),
              serverNode,
              AttributeId.RolePermissions,
              TimestampsToReturn.Neither,
              null,
              null);
      assertEquals(StatusCode.GOOD, adminRp.statusCode());
      assertArrayEquals(policy, (RolePermissionType[]) adminRp.value().value());

      DataValue adminUrp =
          AttributeReader.readAttribute(
              () -> Optional.of(securityAdmin),
              serverNode,
              AttributeId.UserRolePermissions,
              TimestampsToReturn.Neither,
              null,
              null);
      assertEquals(StatusCode.GOOD, adminUrp.statusCode());
      assertArrayEquals(
          new RolePermissionType[] {adminEntry}, (RolePermissionType[]) adminUrp.value().value());

      DataValue userRp =
          AttributeReader.readAttribute(
              () -> Optional.of(authenticated),
              serverNode,
              AttributeId.RolePermissions,
              TimestampsToReturn.Neither,
              null,
              null);
      assertEquals(new StatusCode(StatusCodes.Bad_UserAccessDenied), userRp.statusCode());

      DataValue userUrp =
          AttributeReader.readAttribute(
              () -> Optional.of(authenticated),
              serverNode,
              AttributeId.UserRolePermissions,
              TimestampsToReturn.Neither,
              null,
              null);
      assertEquals(StatusCode.GOOD, userUrp.statusCode());
      assertArrayEquals(
          new RolePermissionType[] {userEntry}, (RolePermissionType[]) userUrp.value().value());
    } finally {
      serverNode.setRolePermissions(null);
    }
  }

  private static Session sessionWithRoles(List<NodeId> roles) {
    Session session = mock(Session.class);
    when(session.getRoleIds()).thenReturn(Optional.ofNullable(roles));
    return session;
  }
}
