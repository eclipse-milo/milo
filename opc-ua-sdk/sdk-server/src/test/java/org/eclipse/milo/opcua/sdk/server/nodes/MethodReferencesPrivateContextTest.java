/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.milo.opcua.sdk.server.nodes;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.AddressSpace;
import org.eclipse.milo.opcua.sdk.server.ManagedAddressSpace;
import org.eclipse.milo.opcua.sdk.server.NodeManager;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.UaNodeManager;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.methods.MethodBindings;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.junit.jupiter.api.Test;

class MethodReferencesPrivateContextTest {
  // Removing only private type registration must not invalidate a still-live owner's Method.
  @Test
  void bindingAndActualDispatchUsePrivateReferenceDefinitionsAfterGlobalTreeRebuild()
      throws Exception {
    try (var f = new Fixture();
        var bindings = new MethodBindings()) {
      f.type(f.context, f.custom, NodeIds.HasComponent.expanded());
      f.server.getAddressSpaceManager().register(f.privateNodes);
      assertTrue(f.server.updateReferenceTypeTree().isSubtypeOf(f.custom, NodeIds.HasComponent));
      f.server.getAddressSpaceManager().unregister(f.privateNodes);
      var tree = f.server.updateReferenceTypeTree();
      assertFalse(tree.isSubtypeOf(f.custom, NodeIds.HasComponent));
      assertSame(
          f.owner,
          f.server.getAddressSpaceManager().getManagedNode(f.owner.getNodeId()).orElseThrow());
      assertSame(
          f.method,
          f.server.getAddressSpaceManager().getManagedNode(f.method.getNodeId()).orElseThrow());
      assertSame(f.method, f.owner.findMethodNode(f.method.getNodeId()));
      try (var token = bindings.bind(f.owner, f.method, (context, request) -> reply("private"))) {
        assertEquals(f.owner.getNodeId(), token.objectId());
        assertEquals("private", value(f.call()));
      }
      assertEquals("fallback", value(f.call()));
      assertSame(tree, f.server.getReferenceTypeTree());
    }
  }

  // The old cached-tree path remains useful when a provider has no managed type definition.
  @Test
  void absentManagedDefinitionsRetainCachedCompatibilityAndRegisteredControls() {
    try (var f = new Fixture()) {
      f.type(f.space.getNodeContext(), f.custom, NodeIds.HasComponent.expanded());
      f.server.updateReferenceTypeTree();
      assertTrue(f.matches(f.custom));
      f.space.getNodeManager().removeNode(f.custom);
      assertTrue(f.matches(f.custom), "unresolved managed definition uses the existing tree");
      assertTrue(f.matches(NodeIds.HasComponent));
      assertTrue(f.matches(NodeIds.HasOrderedComponent));
      assertFalse(f.matches(NodeIds.Organizes));
      assertFalse(
          MethodReferences.isForwardComponent(
              f.context,
              new Reference(
                  f.owner.getNodeId(), f.custom, f.method.getNodeId().expanded(), false)));
      f.server.updateReferenceTypeTree();
      assertFalse(f.matches(f.custom), "an unknown type is not invented after rebuilding");
    }
  }

  // A private definition is authoritative even when a cached global definition says otherwise.
  @Test
  void privateNonComponentDefinitionRejectsBindingDespiteStaleGlobalPositive() {
    try (var f = new Fixture();
        var bindings = new MethodBindings()) {
      f.type(f.space.getNodeContext(), f.custom, NodeIds.HasComponent.expanded());
      f.server.updateReferenceTypeTree();
      f.type(f.context, f.custom, NodeIds.HasProperty.expanded());
      assertTrue(f.server.getReferenceTypeTree().isSubtypeOf(f.custom, NodeIds.HasComponent));
      assertFalse(f.matches(f.custom));
      var failure =
          assertThrows(
              UaException.class,
              () -> bindings.bind(f.owner, f.method, (context, request) -> reply("wrong")));
      assertEquals(StatusCodes.Bad_MethodInvalid, failure.getStatusCode().getValue());
      assertEquals(StatusCodes.Bad_MethodInvalid, f.call().getStatusCode().getValue());
    }
  }

  // Falling back from the original custom id would erase the already-proved private prefix.
  @Test
  void cachedFallbackStartsAtTheMissingAncestor() {
    try (var f = new Fixture()) {
      NodeId missing = f.id("MissingParent");
      f.type(f.space.getNodeContext(), f.custom, NodeIds.HasComponent.expanded());
      f.type(f.space.getNodeContext(), missing, NodeIds.HasProperty.expanded());
      f.server.updateReferenceTypeTree();
      f.space.getNodeManager().removeNode(missing);
      f.type(f.context, f.custom, missing.expanded());
      assertFalse(f.matches(f.custom));
      f.type(f.space.getNodeContext(), missing, NodeIds.HasComponent.expanded());
      f.server.updateReferenceTypeTree();
      f.space.getNodeManager().removeNode(missing);
      assertTrue(f.matches(f.custom));
    }
  }

  // Part 3 §5.3.3.3 gives ReferenceTypes one supertype; malformed local graphs cannot grant
  // ownership.
  @Test
  void wrongKindCyclesAndMultipleParentsCannotUseGlobalPositive() {
    try (var f = new Fixture()) {
      f.type(f.space.getNodeContext(), f.custom, NodeIds.HasComponent.expanded());
      f.server.updateReferenceTypeTree();
      var wrong =
          UaObjectNode.builder(f.context)
              .setNodeId(f.custom)
              .setBrowseName(new QualifiedName(f.namespace, "Wrong"))
              .setDisplayName(LocalizedText.english("Wrong"))
              .buildAndAdd();
      assertFalse(f.matches(f.custom));
      f.privateNodes.removeNode(wrong.getNodeId());
      f.type(f.context, f.custom, f.custom.expanded());
      assertFalse(f.matches(f.custom));
      f.privateNodes.removeNode(f.custom);
      var multiple = f.type(f.context, f.custom, NodeIds.HasComponent.expanded());
      multiple.addReference(
          new Reference(f.custom, NodeIds.HasSubtype, NodeIds.HasProperty.expanded(), false));
      assertFalse(f.matches(f.custom));
      multiple.removeReference(
          new Reference(f.custom, NodeIds.HasSubtype, NodeIds.HasProperty.expanded(), false));
      // Even a matched HasComponent prefix must not hide a malformed managed ancestor.
      f.type(f.context, NodeIds.HasComponent, f.custom.expanded());
      assertFalse(f.matches(f.custom));
    }
  }

  // Qualified duplicate parent edges denote one identity, while external or unknown parents cannot.
  @Test
  void parentIdentityNormalizationPreservesRegisteredFallbackAndRejectsInvalidTargets() {
    try (var f = new Fixture()) {
      NodeId parent = f.id("Parent");
      f.type(f.space.getNodeContext(), parent, NodeIds.HasComponent.expanded());
      var custom = f.type(f.context, f.custom, parent.expanded());
      var qualified = ExpandedNodeId.parse("nsu=" + Fixture.URI + ";s=Parent");
      custom.addReference(new Reference(f.custom, NodeIds.HasSubtype, qualified, false));
      assertTrue(f.matches(f.custom));
      custom.removeReference(new Reference(f.custom, NodeIds.HasSubtype, parent.expanded(), false));
      custom.removeReference(new Reference(f.custom, NodeIds.HasSubtype, qualified, false));
      assertFalse(f.matches(f.custom));
      var unknown = ExpandedNodeId.parse("nsu=urn:unknown;s=Parent");
      custom.addReference(new Reference(f.custom, NodeIds.HasSubtype, unknown, false));
      assertFalse(f.matches(f.custom));
      custom.removeReference(new Reference(f.custom, NodeIds.HasSubtype, unknown, false));
      custom.addReference(
          new Reference(f.custom, NodeIds.HasSubtype, ExpandedNodeId.parse("svr=1;i=47"), false));
      assertFalse(f.matches(f.custom));
    }
  }

  // Operational failures must not silently select the global cached alternative.
  @Test
  void nodeManagerFailureKeepsItsOriginalIdentity() {
    try (var f = new Fixture()) {
      var failure = new UaRuntimeException(StatusCodes.Bad_NoCommunication);
      var broken =
          new UaNodeManager() {
            @Override
            public Optional<UaNode> getNode(NodeId id) {
              throw failure;
            }
          };
      assertSame(
          failure,
          assertThrows(
              UaRuntimeException.class,
              () -> MethodReferences.isForwardComponent(f.context(broken), f.reference(f.custom))));
    }
  }

  private static CallMethodResult reply(String value) {
    return new CallMethodResult(StatusCode.GOOD, null, null, new Variant[] {new Variant(value)});
  }

  private static String value(CallMethodResult result) {
    assertEquals(StatusCode.GOOD, result.getStatusCode());
    return (String) result.getOutputArguments()[0].value();
  }

  private static final class Fixture implements AutoCloseable {
    static final String URI = "urn:milo:test:private-method-references";
    final java.util.concurrent.ExecutorService executor = Executors.newSingleThreadExecutor();
    final java.util.concurrent.ScheduledExecutorService scheduler =
        Executors.newSingleThreadScheduledExecutor();
    final OpcUaServer server;
    final ManagedAddressSpace space;
    final UaNodeManager privateNodes = new UaNodeManager();
    final UaNodeContext context;
    final int namespace;
    final NodeId custom;
    final UaObjectNode owner;
    final UaMethodNode method;

    Fixture() {
      server =
          new OpcUaServer(
              OpcUaServerConfig.builder()
                  .setApplicationUri(URI)
                  .setCertificateManager(new DefaultCertificateManager())
                  .setExecutor(executor)
                  .setScheduledExecutor(scheduler)
                  .build(),
              p -> {
                throw new AssertionError("No transport");
              });
      server.getEventFactory().startup();
      namespace = server.getNamespaceTable().add(URI).intValue();
      context = context(privateNodes);
      custom = id("CustomComponent");
      space =
          new ManagedAddressSpace(server) {
            public void onDataItemsCreated(List<DataItem> items) {}

            public void onDataItemsModified(List<DataItem> items) {}

            public void onDataItemsDeleted(List<DataItem> items) {}

            public void onMonitoringModeChanged(List<MonitoredItem> items) {}
          };
      server.getAddressSpaceManager().register(space.getNodeManager());
      owner =
          UaObjectNode.builder(context)
              .setNodeId(id("Owner"))
              .setBrowseName(new QualifiedName(namespace, "Owner"))
              .setDisplayName(LocalizedText.english("Owner"))
              .setTypeDefinition(NodeIds.BaseObjectType)
              .buildAndAdd();
      method =
          UaMethodNode.builder(context)
              .setNodeId(id("Method"))
              .setBrowseName(new QualifiedName(namespace, "Method"))
              .setDisplayName(LocalizedText.english("Method"))
              .buildAndAdd();
      method.setOutputArguments(
          new Argument[] {
            new Argument("Message", NodeIds.String, -1, null, LocalizedText.NULL_VALUE)
          });
      method.setInvocationHandler((call, request) -> reply("fallback"));
      owner.addReference(reference(custom));
      // Registry liveness is global; the exact live instances still retain their private context.
      space.getNodeManager().addNode(owner);
      space.getNodeManager().addNode(method);
    }

    NodeId id(String name) {
      return new NodeId(namespace, name);
    }

    UaNodeContext context(UaNodeManager nodes) {
      return new UaNodeContext() {
        public OpcUaServer getServer() {
          return server;
        }

        public NodeManager<UaNode> getNodeManager() {
          return nodes;
        }
      };
    }

    Reference reference(NodeId type) {
      return new Reference(owner.getNodeId(), type, method.getNodeId().expanded(), true);
    }

    boolean matches(NodeId type) {
      return MethodReferences.isForwardComponent(context, reference(type));
    }

    UaReferenceTypeNode type(UaNodeContext nodes, NodeId id, ExpandedNodeId parent) {
      var node =
          new UaReferenceTypeNode(
              nodes,
              id,
              new QualifiedName(id.getNamespaceIndex(), "Reference"),
              LocalizedText.NULL_VALUE,
              null,
              null,
              null,
              false,
              false,
              null);
      nodes.getNodeManager().addNode(node);
      node.addReference(new Reference(id, NodeIds.HasSubtype, parent, false));
      return node;
    }

    CallMethodResult call() {
      return space
          .call(
              new AddressSpace.CallContext(server, null),
              List.of(new CallMethodRequest(owner.getNodeId(), method.getNodeId(), new Variant[0])))
          .get(0);
    }

    @Override
    public void close() {
      server.getAddressSpaceManager().unregister(privateNodes);
      server.getAddressSpaceManager().unregister(space.getNodeManager());
      server.shutdown().join();
      executor.shutdownNow();
      scheduler.shutdownNow();
    }
  }
}
