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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import org.eclipse.milo.opcua.sdk.server.AccessContext;
import org.eclipse.milo.opcua.sdk.server.AttributeReader;
import org.eclipse.milo.opcua.sdk.server.Lifecycle;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerDiagnosticsTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.SubscriptionDiagnosticsArrayTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.server.subscriptions.Subscription;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.junit.jupiter.api.Test;

class DiagnosticsArrayElementAccessLevelExTest {

  /**
   * Part 3 §5.6.2 makes AccessLevelEx mandatory for Variables in profiles from version 1.04 on, and
   * its low eight bits must mirror AccessLevel. SubscriptionDiagnosticsArray elements are
   * instantiated from a VariableType, which carries no AccessLevelEx, so the array class must
   * supply it as a root attribute alongside AccessLevel. Without it, reading AccessLevelEx on an
   * element returns Bad_AttributeIdInvalid while the array itself reports CurrentRead.
   */
  @Test
  void arrayElementReportsAccessLevelExMatchingAccessLevel() throws Exception {
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

    SubscriptionDiagnosticsArrayTypeNode arrayNode =
        diagnostics.getSubscriptionDiagnosticsArrayNode();
    var array =
        new SubscriptionDiagnosticsVariableArray(arrayNode, target) {
          @Override
          protected List<Subscription> getSubscriptions() {
            return List.of();
          }
        };

    // Invoke the same creation entry point the subscription event subscriber calls, so the element
    // goes through real instantiation with the array class's root attribute overrides.
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
      var subscription = mock(Subscription.class);
      when(subscription.getId()).thenReturn(uint(1));
      create.invoke(array, subscription);
      assertEquals(1, elements.size(), "the element must be created");

      UaVariableNode element = singleElement(server, target, arrayNode);

      AccessLevelExType accessLevelEx = element.getAccessLevelEx();
      assertNotNull(accessLevelEx, "element must carry AccessLevelEx");
      assertTrue(accessLevelEx.getCurrentRead(), "element AccessLevelEx must allow CurrentRead");
      assertEquals(
          element.getAccessLevel().intValue(),
          accessLevelEx.getValue().intValue() & 0xFF,
          "low eight bits of AccessLevelEx must mirror AccessLevel");

      // The caller-visible contract: a Read of AccessLevelEx succeeds instead of returning
      // Bad_AttributeIdInvalid, and reports the same value as the array Variable itself.
      DataValue read =
          AttributeReader.readAttribute(
              AccessContext.INTERNAL, element, AttributeId.AccessLevelEx, null, null, null);
      assertTrue(read.statusCode().isGood(), "read AccessLevelEx: " + read.statusCode());
      assertEquals(arrayNode.getAccessLevelEx(), read.value().value());
    } finally {
      elements.forEach(Lifecycle::shutdown);
      server.getAddressSpaceManager().unregister(target);
    }
  }

  private static UaVariableNode singleElement(
      OpcUaServer server, UaNodeManager target, UaNode arrayNode) {

    List<NodeId> elementIds =
        target.getReferences(arrayNode.getNodeId()).stream()
            .filter(r -> r.isForward() && r.getReferenceTypeId().equals(NodeIds.HasComponent))
            .map(r -> r.getTargetNodeId().toNodeId(server.getNamespaceTable()).orElseThrow())
            .toList();
    assertEquals(1, elementIds.size(), "exactly one element must hang off the array");

    return (UaVariableNode) target.getNode(elementIds.get(0)).orElseThrow();
  }
}
