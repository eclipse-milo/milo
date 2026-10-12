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
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SubscriptionDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.junit.jupiter.api.Test;

class DiagnosticsArrayLifecycleTest {

  // Removing element zero from a two-element array must not make the next allocation collide with
  // the surviving element and starve all later diagnostics creation. Exercise real instantiation,
  // field setup, node removal, and parent references.
  @Test
  void removingAnEarlierElementDoesNotBlockLaterDiagnostics() throws Exception {
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

    SubscriptionDiagnosticsArrayTypeNode parent = diagnostics.getSubscriptionDiagnosticsArrayNode();
    var array =
        new SubscriptionDiagnosticsVariableArray(parent, target) {
          @Override
          protected List<Subscription> getSubscriptions() {
            return List.of();
          }
        };

    // Isolate allocation from event delivery: invoke the same creation entry point the event
    // subscriber calls, then retire the first element exactly as its deletion callback does.
    Method create =
        SubscriptionDiagnosticsVariableArray.class.getDeclaredMethod(
            "createSubscriptionDiagnosticsNode", Subscription.class);
    create.setAccessible(true);
    Field field =
        SubscriptionDiagnosticsVariableArray.class.getDeclaredField(
            "subscriptionDiagnosticsVariables");
    field.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<Lifecycle> elements = (List<Lifecycle>) field.get(array);
    try {
      create.invoke(array, subscription(0));
      create.invoke(array, subscription(1));
      assertEquals(2, elements.size(), "both initial elements must be created");
      elements.remove(0).shutdown();
      create.invoke(array, subscription(2));
      create.invoke(array, subscription(3));
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

  private static Subscription subscription(int index) {
    var subscription = mock(Subscription.class);
    when(subscription.getId()).thenReturn(uint(index + 1));
    return subscription;
  }
}
