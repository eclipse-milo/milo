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
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.client.identity.UsernameProvider;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.ManagedNamespaceWithLifecycle;
import org.eclipse.milo.opcua.sdk.server.conditions.AcknowledgeableCondition;
import org.eclipse.milo.opcua.sdk.server.conditions.Condition;
import org.eclipse.milo.opcua.sdk.server.items.DataItem;
import org.eclipse.milo.opcua.sdk.server.items.MonitoredItem;
import org.eclipse.milo.opcua.sdk.server.model.objects.AcknowledgeableConditionTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.*;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationRequest;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.MethodInstantiation;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.*;
import org.eclipse.milo.opcua.stack.core.types.structured.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

class MethodBindingsServiceTest extends AbstractClientServerTest {
  private BindingNamespace namespace;

  @Override
  protected void customizeClientConfig(OpcUaClientConfigBuilder builder) {
    builder.setIdentityProvider(new UsernameProvider("user1", "password"));
  }

  @Override
  protected void configureTestNamespace(TestNamespace ignored) {
    namespace = new BindingNamespace();
    namespace.startup();
    server.updateReferenceTypeTree();
  }

  @AfterAll
  void stopBindingNamespace() {
    if (namespace != null) namespace.shutdown();
  }

  // Both owners reach the shared Method through a custom HasComponent subtype.
  @Test
  void authenticatedCallsPreserveObjectDispatchAndDenyNonExecutableMethods() throws Exception {
    try (MethodBindings bindings = new MethodBindings()) {
      AtomicInteger calls = new AtomicInteger();
      namespace.method.setInvocationHandler(reply("fallback", calls));
      MethodBinding first = bindings.bind(namespace.first, namespace.method, reply("first", calls));
      MethodBinding second =
          bindings.bind(namespace.second, namespace.method, reply("second", calls));
      assertEquals("first", value(call(namespace.first, namespace.method)));
      assertEquals("second", value(call(namespace.second, namespace.method)));
      MethodBinding replacement =
          bindings.bind(namespace.first, namespace.method, reply("replacement", calls));
      first.close();
      assertEquals("replacement", value(call(namespace.first, namespace.method)));
      namespace.method.setUserExecutable(false);
      try {
        int before = calls.get();
        assertEquals(
            new StatusCode(StatusCodes.Bad_UserAccessDenied),
            call(namespace.first, namespace.method).getStatusCode());
        assertEquals(before, calls.get());
      } finally {
        namespace.method.setUserExecutable(true);
      }
      replacement.close();
      assertEquals("fallback", value(call(namespace.first, namespace.method)));
      second.close();
    }
  }

  @Test
  void typeLevelMethodsAreCallableButModelledInstanceDeclarationsAreRejected() throws Exception {
    try (MethodBindings bindings = new MethodBindings()) {
      bindings.bind(namespace.type, namespace.typeMethod, reply("type", new AtomicInteger()));
      assertEquals("type", value(call(namespace.type, namespace.typeMethod)));
      assertEquals(
          new StatusCode(StatusCodes.Bad_MethodInvalid),
          assertThrows(
                  UaException.class,
                  () ->
                      bindings.bind(
                          namespace.type,
                          namespace.instanceDeclaration,
                          reply("invalid", new AtomicInteger())))
              .getStatusCode());
      assertEquals(
          new StatusCode(StatusCodes.Bad_MethodInvalid),
          call(namespace.type, namespace.instanceDeclaration).getStatusCode());
    }
  }

  @Test
  void modelledConditionMethodsThroughReferenceSubtypesKeepConditionTypeFailure() throws Exception {
    UaObjectTypeNode conditionType =
        (UaObjectTypeNode)
            server.getAddressSpaceManager().getManagedNode(NodeIds.ConditionType).orElseThrow();
    Reference reference =
        new Reference(
            conditionType.getNodeId(),
            namespace.componentId,
            namespace.instanceDeclaration.getNodeId().expanded(),
            Reference.Direction.FORWARD);
    conditionType.addReference(reference);
    try {
      assertEquals(
          new StatusCode(StatusCodes.Bad_NodeIdInvalid),
          call(conditionType, namespace.instanceDeclaration).getStatusCode());
    } finally {
      conditionType.removeReference(reference);
    }
  }

  @Test
  void registeredConditionManagerRetainsMethodDispatchPrecedence() throws Exception {
    Condition condition = namespace.createCondition();
    server.getConditionManager().register(condition);
    try (MethodBindings bindings = new MethodBindings()) {
      UaMethodNode method = condition.getNode().getEnableMethodNode();
      assertTrue(
          server
              .getConditionManager()
              .findMethodInvocationHandler(condition.getConditionId(), method.getNodeId())
              .isPresent());
      assertEquals(
          new StatusCode(StatusCodes.Bad_NotSupported),
          assertThrows(
                  UaException.class,
                  () ->
                      bindings.bind(
                          condition.getNode(), method, reply("invalid", new AtomicInteger())))
              .getStatusCode());
      // Adopted Conditions default to Enabled, so the Condition's own handler answers, not a
      // fallback.
      assertEquals(
          new StatusCode(StatusCodes.Bad_ConditionAlreadyEnabled),
          call(condition.getNode(), method).getStatusCode());
    } finally {
      server.getConditionManager().unregister(condition);
    }
  }

  private CallMethodResult call(UaNode owner, UaMethodNode method) throws UaException {
    return client.call(
            List.of(new CallMethodRequest(owner.getNodeId(), method.getNodeId(), new Variant[0])))
        .getResults()[0];
  }

  private static MethodInvocationHandler reply(String value, AtomicInteger calls) {
    return (context, request) -> {
      calls.incrementAndGet();
      return new CallMethodResult(StatusCode.GOOD, null, null, new Variant[] {new Variant(value)});
    };
  }

  private static String value(CallMethodResult result) {
    assertEquals(StatusCode.GOOD, result.getStatusCode());
    return (String) result.getOutputArguments()[0].value();
  }

  private final class BindingNamespace extends ManagedNamespaceWithLifecycle {
    final UaObjectNode first;
    final UaObjectNode second;
    final UaMethodNode method;
    final UaObjectTypeNode type;
    final UaMethodNode typeMethod;
    final UaMethodNode instanceDeclaration;
    final NodeId componentId;

    BindingNamespace() {
      super(MethodBindingsServiceTest.this.server, "urn:milo:test:binding-service");
      componentId = newNodeId("CustomComponent");
      UaReferenceTypeNode reference =
          new UaReferenceTypeNode(
              getNodeContext(),
              componentId,
              newQualifiedName("CustomComponent"),
              LocalizedText.english("CustomComponent"),
              LocalizedText.NULL_VALUE,
              uint(0),
              uint(0),
              false,
              false,
              LocalizedText.english("CustomComponentOf"));
      getNodeManager().addNode(reference);
      reference.addReference(
          new Reference(
              componentId,
              NodeIds.HasSubtype,
              NodeIds.HasComponent.expanded(),
              Reference.Direction.INVERSE));
      first = object("first");
      second = object("second");
      method = method("shared");
      first.addReference(
          new Reference(
              first.getNodeId(),
              componentId,
              method.getNodeId().expanded(),
              Reference.Direction.FORWARD));
      second.addReference(
          new Reference(
              second.getNodeId(),
              componentId,
              method.getNodeId().expanded(),
              Reference.Direction.FORWARD));
      type =
          UaObjectTypeNode.builder(getNodeContext())
              .setNodeId(newNodeId("Type"))
              .setBrowseName(newQualifiedName("Type"))
              .setDisplayName(LocalizedText.english("Type"))
              .buildAndAdd();
      typeMethod = method("type");
      instanceDeclaration = method("instanceDeclaration");
      type.addReference(
          new Reference(
              type.getNodeId(),
              componentId,
              typeMethod.getNodeId().expanded(),
              Reference.Direction.FORWARD));
      type.addReference(
          new Reference(
              type.getNodeId(),
              componentId,
              instanceDeclaration.getNodeId().expanded(),
              Reference.Direction.FORWARD));
      instanceDeclaration.addReference(
          new Reference(
              instanceDeclaration.getNodeId(),
              NodeIds.HasModellingRule,
              NodeIds.ModellingRule_Mandatory.expanded(),
              Reference.Direction.FORWARD));
    }

    private UaObjectNode object(String name) {
      return UaObjectNode.builder(getNodeContext())
          .setNodeId(newNodeId(name))
          .setBrowseName(newQualifiedName(name))
          .setDisplayName(LocalizedText.english(name))
          .setTypeDefinition(NodeIds.BaseObjectType)
          .buildAndAdd();
    }

    private UaMethodNode method(String name) {
      UaMethodNode result =
          UaMethodNode.builder(getNodeContext())
              .setNodeId(newNodeId(name))
              .setBrowseName(newQualifiedName(name))
              .setDisplayName(LocalizedText.english(name))
              .buildAndAdd();
      result.setOutputArguments(
          new Argument[] {
            new Argument("Message", NodeIds.String, -1, null, LocalizedText.NULL_VALUE)
          });
      return result;
    }

    Condition createCondition() throws UaException {
      InstantiationRequest<AcknowledgeableConditionTypeNode> request =
          InstantiationRequest.of(
                  AcknowledgeableConditionTypeNode.class, NodeIds.AcknowledgeableConditionType)
              .nodeId(newNodeId("condition"))
              .browseName(newQualifiedName("Condition"))
              .target(getNodeManager())
              .methodInstantiation(MethodInstantiation.SHARE)
              .build();
      AcknowledgeableConditionTypeNode node =
          getServer().getNodeInstantiator().instantiate(request).root();
      node.setEventType(NodeIds.AcknowledgeableConditionType);
      node.setSeverity(ushort(500));
      return AcknowledgeableCondition.adopt(
          getNodeContext(), node.getNodeId(), builder -> builder.conditionSource(first));
    }

    public void onDataItemsCreated(List<DataItem> items) {}

    public void onDataItemsModified(List<DataItem> items) {}

    public void onDataItemsDeleted(List<DataItem> items) {}

    public void onMonitoringModeChanged(List<MonitoredItem> items) {}
  }
}
