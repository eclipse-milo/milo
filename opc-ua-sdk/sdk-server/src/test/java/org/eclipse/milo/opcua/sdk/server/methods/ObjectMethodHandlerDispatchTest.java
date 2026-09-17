/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.methods;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.eclipse.milo.opcua.sdk.server.AddressSpace;
import org.eclipse.milo.opcua.sdk.server.ManagedAddressSpace;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.junit.jupiter.api.Test;

class ObjectMethodHandlerDispatchTest {

  private static MethodInvocationHandler reply(String value) {
    return (context, request) ->
        new CallMethodResult(StatusCode.GOOD, null, null, new Variant[] {new Variant(value)});
  }

  private static String value(CallMethodResult result) {
    assertEquals(StatusCode.GOOD, result.getStatusCode());
    return (String) result.getOutputArguments()[0].value();
  }

  // Companion models often share one Method node across every instance of a type; each Object's
  // own handler must win over the node's handler, and only for that Object.
  @Test
  void objectHandlerTakesPrecedenceOverTheMethodNodeHandlerPerObject() throws Exception {
    try (Fixture f = new Fixture()) {
      MethodInvocationHandler fallback = reply("fallback");
      f.method.setInvocationHandler(fallback);
      f.first.setMethodHandler(f.method.getNodeId(), reply("first"));

      assertEquals("first", value(f.call(f.first, f.method.getNodeId())));
      assertEquals("fallback", value(f.call(f.second, f.method.getNodeId())));
      assertSame(fallback, f.method.getInvocationHandler(), "the node's handler is untouched");

      f.first.setMethodHandler(f.method.getNodeId(), reply("replacement"));
      assertEquals("replacement", value(f.call(f.first, f.method.getNodeId())));

      f.first.setMethodHandler(f.method.getNodeId(), null);
      assertNull(f.first.getMethodHandler(f.method.getNodeId()));
      assertEquals("fallback", value(f.call(f.first, f.method.getNodeId())));
    }
  }

  // Part 4 §5.12.2.2 lets a client pass the ObjectType declaration's NodeId as MethodId. The
  // Object's handler map is keyed by its own Method node, so both forms must reach one handler.
  @Test
  void declarationMethodIdResolvesToTheInstanceMethodHandler() throws Exception {
    try (Fixture f = new Fixture()) {
      f.method.setInvocationHandler(reply("fallback"));
      f.typed.setMethodHandler(f.instanceMethod.getNodeId(), reply("typed"));

      assertEquals("typed", value(f.call(f.typed, f.instanceMethod.getNodeId())));
      assertEquals("typed", value(f.call(f.typed, f.declaration.getNodeId())));
    }
  }

  // The map is consulted only for Methods the Object actually owns; a handler stored under a
  // foreign NodeId never changes how an unrelated Method resolves.
  @Test
  void handlerUnderAForeignMethodIdDoesNotAffectDispatch() throws Exception {
    try (Fixture f = new Fixture()) {
      f.method.setInvocationHandler(reply("fallback"));
      f.first.setMethodHandler(new NodeId(f.namespace, "unrelated"), reply("stray"));

      assertEquals("fallback", value(f.call(f.first, f.method.getNodeId())));
      assertEquals(
          new StatusCode(StatusCodes.Bad_MethodInvalid),
          f.call(f.first, new NodeId(f.namespace, "unrelated")).getStatusCode());
    }
  }

  private static final class Fixture implements AutoCloseable {
    final ExecutorService executor = Executors.newSingleThreadExecutor();
    final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    final OpcUaServer server;
    final ManagedAddressSpace space;
    final int namespace;
    final UaObjectNode first;
    final UaObjectNode second;
    final UaMethodNode method;
    final UaObjectTypeNode type;
    final UaMethodNode declaration;
    final UaObjectNode typed;
    final UaMethodNode instanceMethod;

    Fixture() {
      server =
          new OpcUaServer(
              OpcUaServerConfig.builder()
                  .setApplicationUri("urn:milo:test:object-method-handlers")
                  .setCertificateManager(new DefaultCertificateManager())
                  .setExecutor(executor)
                  .setScheduledExecutor(scheduler)
                  .build(),
              profile -> {
                throw new AssertionError("No transport should start");
              });
      server.getEventFactory().startup();
      namespace = server.getNamespaceTable().add("urn:milo:test:object-method-handlers").intValue();
      space =
          new ManagedAddressSpace(server) {
            public void onDataItemsCreated(List<DataItem> items) {}

            public void onDataItemsModified(List<DataItem> items) {}

            public void onDataItemsDeleted(List<DataItem> items) {}

            public void onMonitoringModeChanged(List<MonitoredItem> items) {}
          };
      server.getAddressSpaceManager().register(space.getNodeManager());

      first = object("first", NodeIds.BaseObjectType);
      second = object("second", NodeIds.BaseObjectType);
      method = method("shared", "shared");
      first.addComponent(method);
      second.addComponent(method);

      type =
          UaObjectTypeNode.builder(space.getNodeContext())
              .setNodeId(new NodeId(namespace, "Type"))
              .setBrowseName(new QualifiedName(namespace, "Type"))
              .setDisplayName(LocalizedText.english("Type"))
              .buildAndAdd();
      declaration = method("Type.Do", "Do");
      type.addComponent(declaration);
      typed = object("typed", type.getNodeId());
      instanceMethod = method("typed.Do", "Do");
      typed.addComponent(instanceMethod);
    }

    UaMethodNode method(String id, String name) {
      UaMethodNode result =
          UaMethodNode.builder(space.getNodeContext())
              .setNodeId(new NodeId(namespace, id))
              .setBrowseName(new QualifiedName(namespace, name))
              .setDisplayName(LocalizedText.english(name))
              .buildAndAdd();
      result.setOutputArguments(
          new Argument[] {
            new Argument("Message", NodeIds.String, -1, null, LocalizedText.NULL_VALUE)
          });
      return result;
    }

    UaObjectNode object(String name, NodeId typeDefinition) {
      return UaObjectNode.builder(space.getNodeContext())
          .setNodeId(new NodeId(namespace, name))
          .setBrowseName(new QualifiedName(namespace, name))
          .setDisplayName(LocalizedText.english(name))
          .setTypeDefinition(typeDefinition)
          .buildAndAdd();
    }

    CallMethodResult call(UaObjectNode owner, NodeId methodId) {
      return space
          .call(
              new AddressSpace.CallContext(server, null),
              List.of(new CallMethodRequest(owner.getNodeId(), methodId, new Variant[0])))
          .get(0);
    }

    public void close() {
      server.getAddressSpaceManager().unregister(space.getNodeManager());
      server.shutdown().join();
      executor.shutdownNow();
      scheduler.shutdownNow();
    }
  }
}
