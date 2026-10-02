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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.core.AccessLevel;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.SessionListener;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SessionDiagnosticsObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.SessionsDiagnosticsSummaryTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SessionSecurityDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.subscriptions.SubscriptionManager;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
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

  // Part 5 §6.3.5: SessionSecurityDiagnostics should be accessible only to authorised users. The
  // security array's element and its fields carry the array's RolePermissions and
  // AccessRestrictions, and in the default restricted mode an anonymous Session cannot read them.
  @Test
  void securityArrayElementCarriesTheSecurityArraysAccessMetadata() throws Exception {
    diagnosticsNode.setEnabledFlag(true);
    listener.onSessionCreated(session("Session1"));

    SessionSecurityDiagnosticsArrayTypeNode array =
        summaryNode.getSessionSecurityDiagnosticsArrayNode();
    var element = (SessionSecurityDiagnosticsTypeNode) singleElement(array);

    RolePermissionType[] rolePermissions = array.getRolePermissions();
    assertNotNull(rolePermissions, "the security array has RolePermissions to copy");
    assertTrue(rolePermissions.length > 0, "the security array has RolePermissions to copy");

    Session anonymous = mock(Session.class);
    when(anonymous.getRoleIds()).thenReturn(Optional.empty());

    for (UaVariableNode node : List.of(element, element.getClientCertificateNode())) {
      String name = node.getBrowseName().name();
      assertArrayEquals(rolePermissions, node.getRolePermissions(), name + " RolePermissions");
      assertEquals(
          array.getAccessRestrictions(),
          node.getAccessRestrictions(),
          name + " AccessRestrictions");
      assertEquals(
          AccessLevel.toValue(AccessLevel.NONE),
          node.getFilterChain().readAttribute(anonymous, node, AttributeId.UserAccessLevel),
          name + " UserAccessLevel for an anonymous Session");
    }
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
