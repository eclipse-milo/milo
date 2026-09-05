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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import org.eclipse.milo.opcua.sdk.server.Lifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DiagnosticsArrayLifecycleTest {

  // Removing element zero from a two-element array must not make the next allocation collide with
  // the surviving element and starve all later diagnostics creation. Exercise real instantiation,
  // field setup, node removal, and parent references for each diagnostics array implementation.
  @ParameterizedTest
  @ValueSource(strings = {"session", "security", "subscription"})
  void removingAnEarlierElementDoesNotBlockLaterDiagnostics(String kind) throws Exception {
    var config =
        OpcUaServerConfig.builder().setCertificateManager(new DefaultCertificateManager()).build();
    var server = new OpcUaServer(config, transportProfile -> null);
    var target = new UaNodeManager();
    server.getAddressSpaceManager().register(target);
    var diagnostics =
        (ServerDiagnosticsTypeNode)
            server
                .getAddressSpaceManager()
                .getManagedNode(NodeIds.Server_ServerDiagnostics)
                .orElseThrow();
    var summary = diagnostics.getSessionsDiagnosticsSummaryNode();

    Object array;
    UaNode parent;
    Class<?> arrayClass;
    String createMethod;
    String elementsField;
    Class<?> ownerClass;
    switch (kind) {
      case "session" -> {
        parent = summary.getSessionDiagnosticsArrayNode();
        array =
            new SessionDiagnosticsVariableArray(summary.getSessionDiagnosticsArrayNode(), target);
        arrayClass = SessionDiagnosticsVariableArray.class;
        createMethod = "createSessionDiagnosticsVariable";
        elementsField = "sessionDiagnosticsVariables";
        ownerClass = Session.class;
      }
      case "security" -> {
        parent = summary.getSessionSecurityDiagnosticsArrayNode();
        array =
            new SessionSecurityDiagnosticsVariableArray(
                summary.getSessionSecurityDiagnosticsArrayNode(), target);
        arrayClass = SessionSecurityDiagnosticsVariableArray.class;
        createMethod = "createSessionSecurityDiagnosticsVariable";
        elementsField = "sessionSecurityDiagnosticsVariables";
        ownerClass = Session.class;
      }
      case "subscription" -> {
        parent = diagnostics.getSubscriptionDiagnosticsArrayNode();
        array =
            new SubscriptionDiagnosticsVariableArray(
                diagnostics.getSubscriptionDiagnosticsArrayNode(), target) {
              @Override
              protected List<Subscription> getSubscriptions() {
                return List.of();
              }
            };
        arrayClass = SubscriptionDiagnosticsVariableArray.class;
        createMethod = "createSubscriptionDiagnosticsNode";
        elementsField = "subscriptionDiagnosticsVariables";
        ownerClass = Subscription.class;
      }
      default -> throw new AssertionError(kind);
    }

    // Isolate allocation from event delivery: invoke the same creation entry point the listeners
    // call, then retire the first element exactly as their close callbacks do.
    Method create = arrayClass.getDeclaredMethod(createMethod, ownerClass);
    create.setAccessible(true);
    Field field = arrayClass.getDeclaredField(elementsField);
    field.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<Lifecycle> elements = (List<Lifecycle>) field.get(array);
    try {
      create.invoke(array, owner(ownerClass, 0));
      create.invoke(array, owner(ownerClass, 1));
      assertEquals(2, elements.size(), "both initial elements must be created");
      elements.remove(0).shutdown();
      create.invoke(array, owner(ownerClass, 2));
      create.invoke(array, owner(ownerClass, 3));
      assertEquals(
          3, elements.size(), "creation must continue after an earlier element is retired");
      var references =
          target.getReferences(parent.getNodeId()).stream()
              .filter(r -> r.isForward() && r.getReferenceTypeId().equals(NodeIds.HasComponent))
              .toList();
      assertEquals(3, references.size());
      assertEquals(3, references.stream().map(r -> r.getTargetNodeId()).distinct().count());
      assertTrue(
          references.stream()
              .allMatch(
                  r ->
                      target.getNode(r.getTargetNodeId(), server.getNamespaceTable()).isPresent()));
    } finally {
      elements.forEach(Lifecycle::shutdown);
      server.getAddressSpaceManager().unregister(target);
    }
  }

  private static Object owner(Class<?> ownerClass, int index) {
    if (ownerClass == Session.class) {
      var session = mock(Session.class);
      when(session.getSessionId()).thenReturn(new NodeId(1, "Session" + index));
      return session;
    }
    var subscription = mock(Subscription.class);
    when(subscription.getId()).thenReturn(uint(index + 1));
    return subscription;
  }
}
