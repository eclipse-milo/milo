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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.*;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.nodes.*;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class MethodBindingsTest {
  private static MethodInvocationHandler reply(String value) {
    return (context, request) ->
        new CallMethodResult(StatusCode.GOOD, null, null, new Variant[] {new Variant(value)});
  }

  private static String value(CallMethodResult result) {
    assertEquals(StatusCode.GOOD, result.getStatusCode());
    return (String) result.getOutputArguments()[0].value();
  }

  @Test
  void sharedMethodDispatchesByObjectAndStaleTokensCannotRemoveReplacement() throws Exception {
    try (Fixture f = new Fixture();
        MethodBindings bindings = new MethodBindings()) {
      MethodInvocationHandler fallback = reply("fallback");
      f.method.setInvocationHandler(fallback);
      MethodBinding old = bindings.bind(f.first, f.method, reply("old"));
      MethodBinding other = bindings.bind(f.second, f.method, reply("other"));
      MethodBinding replacement = bindings.bind(f.first, f.method, reply("new"));
      assertSame(fallback, f.method.getInvocationHandler());
      old.close();
      old.close();
      assertEquals("new", value(f.call(f.first)));
      assertEquals("other", value(f.call(f.second)));
      replacement.close();
      assertEquals("fallback", value(f.call(f.first)));
      other.close();
      assertSame(fallback, f.method.getInvocationHandler());
    }
  }

  // Selection happens before invocation and cleanup must never wait on application code.
  @Test
  void replacementAndCloseDoNotDrainAlreadySelectedCallback() throws Exception {
    try (Fixture f = new Fixture();
        MethodBindings bindings = new MethodBindings()) {
      CountDownLatch entered = new CountDownLatch(1);
      CountDownLatch release = new CountDownLatch(1);
      ExecutorService executor = Executors.newSingleThreadExecutor();
      f.method.setInvocationHandler(reply("fallback"));
      MethodBinding old =
          bindings.bind(
              f.first,
              f.method,
              (context, request) -> {
                entered.countDown();
                try {
                  assertTrue(release.await(5, TimeUnit.SECONDS));
                } catch (InterruptedException e) {
                  throw new AssertionError(e);
                }
                return reply("old").invoke(context, request);
              });
      try {
        Future<CallMethodResult> inFlight = executor.submit(() -> f.call(f.first));
        assertTrue(entered.await(5, TimeUnit.SECONDS));
        bindings.bind(f.first, f.method, reply("new"));
        old.close();
        assertEquals("new", value(f.call(f.first)));
        bindings.close();
        assertEquals("fallback", value(f.call(f.first)));
        release.countDown();
        assertEquals("old", value(inFlight.get(5, TimeUnit.SECONDS)));
      } finally {
        release.countDown();
        executor.shutdownNow();
      }
    }
  }

  // The default handler and ObjectId registrations are independent.
  @Test
  void rawReplacementChangesOnlyTheDefaultHandler() throws Exception {
    try (Fixture f = new Fixture();
        MethodBindings bindings = new MethodBindings()) {
      MethodBinding token = bindings.bind(f.first, f.method, reply("bound"));
      MethodInvocationHandler raw = reply("raw");
      f.method.setInvocationHandler(raw);
      assertEquals("bound", value(f.call(f.first)));
      assertEquals("raw", value(f.call(f.second)));
      bindings.bind(f.second, f.method, reply("late"));
      assertEquals("late", value(f.call(f.second)));
      token.close();
      assertEquals("raw", value(f.call(f.first)));
      bindings.close();
      assertSame(raw, f.method.getInvocationHandler());
      assertEquals("raw", value(f.call(f.second)));
    }
  }

  @Test
  void lastTokenRestoresFallbackAndSameLifetimeCanBindAgain() throws Exception {
    try (Fixture f = new Fixture();
        MethodBindings bindings = new MethodBindings()) {
      MethodInvocationHandler fallback = reply("fallback");
      f.method.setInvocationHandler(fallback);
      bindings.bind(f.first, f.method, reply("first")).close();
      assertSame(fallback, f.method.getInvocationHandler());
      bindings.bind(f.first, f.method, reply("second"));
      assertEquals("second", value(f.call(f.first)));
      bindings.removeObject(f.first.getNodeId());
      assertSame(fallback, f.method.getInvocationHandler());
      bindings.close();
      assertThrows(
          IllegalStateException.class, () -> bindings.bind(f.first, f.method, reply("closed")));
    }
  }

  @Test
  void registriesShareMethodsAndTheLatestBindWinsPerObject() throws Exception {
    try (Fixture f = new Fixture();
        MethodBindings first = new MethodBindings();
        MethodBindings second = new MethodBindings()) {
      MethodBinding token = first.bind(f.first, f.method, reply("first"));
      second.bind(f.second, f.method, reply("second"));
      assertEquals("first", value(f.call(f.first)));
      assertEquals("second", value(f.call(f.second)));
      second.bind(f.first, f.method, reply("override"));
      assertEquals("override", value(f.call(f.first)));
      token.close();
      first.close();
      assertEquals("override", value(f.call(f.first)));
      second.close();
      assertEquals(new StatusCode(StatusCodes.Bad_NotImplemented), f.call(f.first).getStatusCode());
    }
  }

  @Test
  void removeObjectLeavesOtherRegistrationsAndSupportsExplicitNodeRecreation() throws Exception {
    try (Fixture f = new Fixture();
        MethodBindings bindings = new MethodBindings()) {
      MethodBinding old = bindings.bind(f.first, f.method, reply("old"));
      bindings.bind(f.second, f.method, reply("other"));
      bindings.removeObject(f.first.getNodeId());
      f.space.getNodeManager().removeNode(f.first);
      UaObjectNode recreated = f.object("first");
      recreated.addComponent(f.method);
      bindings.bind(recreated, f.method, reply("recreated"));
      old.close();
      assertEquals("recreated", value(f.call(recreated)));
      assertEquals("other", value(f.call(f.second)));
      assertEquals(
          new StatusCode(StatusCodes.Bad_NodeIdUnknown),
          assertThrows(UaException.class, () -> bindings.bind(f.first, f.method, reply("removed")))
              .getStatusCode());
    }
  }

  @Test
  void invalidOwnershipIsRejectedBeforeHandlerInstallation() throws Exception {
    try (Fixture f = new Fixture();
        MethodBindings bindings = new MethodBindings()) {
      MethodInvocationHandler fallback = f.method.getInvocationHandler();
      UaObjectNode unrelated = f.object("unrelated");
      assertEquals(
          new StatusCode(StatusCodes.Bad_MethodInvalid),
          assertThrows(UaException.class, () -> bindings.bind(unrelated, f.method, reply("wrong")))
              .getStatusCode());
      assertSame(fallback, f.method.getInvocationHandler());
      assertEquals(
          new StatusCode(StatusCodes.Bad_MethodInvalid), f.call(unrelated).getStatusCode());
    }
  }

  @Test
  void crossServerOwnerAndMethodAreRejected() throws Exception {
    try (Fixture f = new Fixture();
        Fixture other = new Fixture();
        MethodBindings bindings = new MethodBindings()) {
      assertThrows(
          IllegalArgumentException.class,
          () -> bindings.bind(f.first, other.method, reply("wrong")));
      assertSame(MethodInvocationHandler.NOT_IMPLEMENTED, other.method.getInvocationHandler());
    }
  }

  // Declaration lookup must tolerate an owner whose type definition does not resolve.
  @Test
  void ownerWithUnresolvableTypeDefinitionCanBindItsSecondMethod() throws Exception {
    try (Fixture f = new Fixture();
        MethodBindings bindings = new MethodBindings()) {
      UaObjectNode untyped =
          UaObjectNode.builder(f.space.getNodeContext())
              .setNodeId(new NodeId(f.namespace, "untyped"))
              .setBrowseName(new QualifiedName(f.namespace, "untyped"))
              .setDisplayName(LocalizedText.english("untyped"))
              .setTypeDefinition(new NodeId(f.namespace, "NoSuchType"))
              .buildAndAdd();
      assertNull(untyped.getTypeDefinitionNode());
      UaMethodNode other = f.method("other");
      untyped.addComponent(other);
      untyped.addComponent(f.method);
      assertNull(untyped.findMethodNode(new NodeId(f.namespace, "missing")));
      bindings.bind(untyped, f.method, reply("untyped"));
      assertEquals("untyped", value(f.call(untyped)));
    }
  }

  @Test
  void incompatibleTypedMetadataIsRejectedWithoutRewritingProperties() throws Exception {
    try (Fixture f = new Fixture();
        MethodBindings bindings = new MethodBindings()) {
      Argument[] declared = {
        new Argument("Value", NodeIds.String, -1, null, LocalizedText.NULL_VALUE)
      };
      f.method.setInputArguments(declared);
      AbstractMethodInvocationHandler handler =
          new AbstractMethodInvocationHandler(f.method) {
            public Argument[] getInputArguments() {
              return new Argument[] {
                new Argument("Value", NodeIds.Int32, -1, null, LocalizedText.NULL_VALUE)
              };
            }

            public Argument[] getOutputArguments() {
              return f.method.getOutputArguments();
            }

            protected Variant[] invoke(InvocationContext context, Variant[] values) {
              return values;
            }
          };
      assertEquals(
          new StatusCode(StatusCodes.Bad_TypeMismatch),
          assertThrows(UaException.class, () -> bindings.bind(f.first, f.method, handler))
              .getStatusCode());
      assertArrayEquals(declared, f.method.getInputArguments());
    }
  }

  @Test
  void closeDuringMetadataValidationPreventsInstallationWithoutWaitingForTheBinder()
      throws Exception {
    try (Fixture f = new Fixture();
        MethodBindings bindings = new MethodBindings()) {
      CountDownLatch entered = new CountDownLatch(1);
      CountDownLatch release = new CountDownLatch(1);
      ExecutorService executor = Executors.newSingleThreadExecutor();
      MethodInvocationHandler fallback = f.method.getInvocationHandler();
      AbstractMethodInvocationHandler handler =
          new AbstractMethodInvocationHandler(f.method) {
            public Argument[] getInputArguments() {
              entered.countDown();
              try {
                assertTrue(release.await(5, TimeUnit.SECONDS));
              } catch (InterruptedException e) {
                throw new AssertionError(e);
              }
              return new Argument[0];
            }

            public Argument[] getOutputArguments() {
              return f.method.getOutputArguments();
            }

            protected Variant[] invoke(InvocationContext context, Variant[] values) {
              return values;
            }
          };
      try {
        Future<MethodBinding> pending =
            executor.submit(() -> bindings.bind(f.first, f.method, handler));
        assertTrue(entered.await(5, TimeUnit.SECONDS));
        bindings.close();
        release.countDown();
        assertInstanceOf(
            IllegalStateException.class,
            assertThrows(ExecutionException.class, () -> pending.get(5, TimeUnit.SECONDS))
                .getCause());
        assertSame(fallback, f.method.getInvocationHandler());
      } finally {
        release.countDown();
        executor.shutdownNow();
      }
    }
  }

  static Stream<Arguments> incompatibleInputMetadata() {
    return Stream.of(
        Arguments.of("null argument", (Argument) null),
        Arguments.of(
            "null data type",
            new Argument("Value", null, 1, new UInteger[] {uint(2)}, LocalizedText.NULL_VALUE)),
        Arguments.of(
            "null rank",
            new Argument(
                "Value", NodeIds.String, null, new UInteger[] {uint(2)}, LocalizedText.NULL_VALUE)),
        Arguments.of(
            "name",
            new Argument(
                "Other", NodeIds.String, 1, new UInteger[] {uint(2)}, LocalizedText.NULL_VALUE)),
        Arguments.of(
            "rank", new Argument("Value", NodeIds.String, -1, null, LocalizedText.NULL_VALUE)),
        Arguments.of(
            "dimensions",
            new Argument(
                "Value", NodeIds.String, 1, new UInteger[] {uint(3)}, LocalizedText.NULL_VALUE)),
        Arguments.of(
            "malformed dimensions",
            new Argument(
                "Value", NodeIds.String, 1, new UInteger[] {null}, LocalizedText.NULL_VALUE)));
  }

  @ParameterizedTest(name = "{0}")
  @MethodSource("incompatibleInputMetadata")
  void incompatibleMetadataReturnsTypeMismatchWithoutChangingProperties(
      String scenario, Argument argument) throws Exception {
    try (Fixture f = new Fixture();
        MethodBindings bindings = new MethodBindings()) {
      Argument[] declared = {
        new Argument("Value", NodeIds.String, 1, new UInteger[] {uint(2)}, LocalizedText.NULL_VALUE)
      };
      f.method.setInputArguments(declared);
      MethodInvocationHandler fallback = f.method.getInvocationHandler();
      AbstractMethodInvocationHandler handler =
          typed(f.method, new Argument[] {argument}, f.method.getOutputArguments());
      assertEquals(
          new StatusCode(StatusCodes.Bad_TypeMismatch),
          assertThrows(UaException.class, () -> bindings.bind(f.first, f.method, handler))
              .getStatusCode());
      assertArrayEquals(declared, f.method.getInputArguments());
      assertSame(fallback, f.method.getInvocationHandler());
    }
  }

  @Test
  void outputMetadataAndTypedNodeMustMatchTheSharedMethod() throws Exception {
    try (Fixture f = new Fixture();
        MethodBindings bindings = new MethodBindings()) {
      MethodInvocationHandler fallback = f.method.getInvocationHandler();
      assertEquals(
          new StatusCode(StatusCodes.Bad_TypeMismatch),
          assertThrows(
                  UaException.class,
                  () ->
                      bindings.bind(
                          f.first, f.method, typed(f.method, new Argument[0], new Argument[0])))
              .getStatusCode());
      UaMethodNode other = f.method("other");
      assertEquals(
          new StatusCode(StatusCodes.Bad_TypeMismatch),
          assertThrows(
                  UaException.class,
                  () ->
                      bindings.bind(
                          f.first,
                          f.method,
                          typed(other, new Argument[0], f.method.getOutputArguments())))
              .getStatusCode());
      assertSame(fallback, f.method.getInvocationHandler());
    }
  }

  @Test
  void nullAndEmptyDimensionsDescribeTheSameUnspecifiedShape() throws Exception {
    try (Fixture f = new Fixture();
        MethodBindings bindings = new MethodBindings()) {
      Argument[] declared = {
        new Argument("Value", NodeIds.String, -1, null, LocalizedText.NULL_VALUE)
      };
      Argument[] expected = {
        new Argument("Value", NodeIds.String, -1, new UInteger[0], LocalizedText.NULL_VALUE)
      };
      f.method.setInputArguments(declared);
      MethodBinding token =
          bindings.bind(
              f.first, f.method, typed(f.method, expected, f.method.getOutputArguments()));
      assertEquals(f.first.getNodeId(), token.objectId());
      assertArrayEquals(declared, f.method.getInputArguments());
    }
  }

  private static AbstractMethodInvocationHandler typed(
      UaMethodNode method, Argument[] inputs, Argument[] outputs) {
    return new AbstractMethodInvocationHandler(method) {
      public Argument[] getInputArguments() {
        return inputs;
      }

      public Argument[] getOutputArguments() {
        return outputs;
      }

      protected Variant[] invoke(InvocationContext context, Variant[] values) {
        return new Variant[] {new Variant("typed")};
      }
    };
  }

  @Test
  void componentReferenceSubtypesSupportBindingAndActualOwnershipDispatch() throws Exception {
    try (Fixture f = new Fixture();
        MethodBindings bindings = new MethodBindings()) {
      NodeId referenceId = new NodeId(f.namespace, "CustomComponent");
      UaReferenceTypeNode referenceType =
          new UaReferenceTypeNode(
              f.space.getNodeContext(),
              referenceId,
              new QualifiedName(f.namespace, "CustomComponent"),
              LocalizedText.english("CustomComponent"),
              LocalizedText.NULL_VALUE,
              UInteger.MIN,
              UInteger.MIN,
              false,
              false,
              LocalizedText.english("CustomComponentOf"));
      f.space.getNodeManager().addNode(referenceType);
      referenceType.addReference(
          new Reference(
              referenceId,
              NodeIds.HasSubtype,
              NodeIds.HasComponent.expanded(),
              Reference.Direction.INVERSE));
      f.server.updateReferenceTypeTree();
      UaObjectNode owner = f.object("customOwner");
      owner.addReference(
          new Reference(
              owner.getNodeId(),
              referenceId,
              f.method.getNodeId().expanded(),
              Reference.Direction.FORWARD));
      f.method.setInvocationHandler(reply("raw"));
      assertEquals(StatusCode.GOOD, f.call(owner).getStatusCode());
      bindings.bind(owner, f.method, reply("custom"));
      assertEquals("custom", value(f.call(owner)));
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

    Fixture() {
      server =
          new OpcUaServer(
              OpcUaServerConfig.builder()
                  .setApplicationUri("urn:milo:test:method-bindings")
                  .setCertificateManager(new DefaultCertificateManager())
                  .setExecutor(executor)
                  .setScheduledExecutor(scheduler)
                  .build(),
              profile -> {
                throw new AssertionError("No transport should start");
              });
      server.getEventFactory().startup();
      namespace = server.getNamespaceTable().add("urn:milo:test:method-bindings").intValue();
      space =
          new ManagedAddressSpace(server) {
            public void onDataItemsCreated(List<DataItem> items) {}

            public void onDataItemsModified(List<DataItem> items) {}

            public void onDataItemsDeleted(List<DataItem> items) {}

            public void onMonitoringModeChanged(List<MonitoredItem> items) {}
          };
      server.getAddressSpaceManager().register(space.getNodeManager());
      first = object("first");
      second = object("second");
      method = method("shared");
      first.addComponent(method);
      second.addComponent(method);
    }

    UaMethodNode method(String name) {
      UaMethodNode result =
          UaMethodNode.builder(space.getNodeContext())
              .setNodeId(new NodeId(namespace, name))
              .setBrowseName(new QualifiedName(namespace, name))
              .setDisplayName(LocalizedText.english(name))
              .buildAndAdd();
      result.setOutputArguments(
          new Argument[] {
            new Argument("Message", NodeIds.String, -1, null, LocalizedText.NULL_VALUE)
          });
      return result;
    }

    UaObjectNode object(String name) {
      return UaObjectNode.builder(space.getNodeContext())
          .setNodeId(new NodeId(namespace, name))
          .setBrowseName(new QualifiedName(namespace, name))
          .setDisplayName(LocalizedText.english(name))
          .setTypeDefinition(NodeIds.BaseObjectType)
          .buildAndAdd();
    }

    CallMethodResult call(UaObjectNode owner) {
      return space
          .call(
              new AddressSpace.CallContext(server, null),
              List.of(new CallMethodRequest(owner.getNodeId(), method.getNodeId(), new Variant[0])))
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
