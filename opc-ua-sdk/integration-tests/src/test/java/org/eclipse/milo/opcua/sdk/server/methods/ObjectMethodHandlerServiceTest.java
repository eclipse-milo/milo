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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaReferenceTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationRequest;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.MethodInstantiation;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

/** Object-owned Method handlers, exercised through the Call service with a real client. */
class ObjectMethodHandlerServiceTest extends AbstractClientServerTest {
  private OwnerNamespace namespace;

  @Override
  protected void customizeClientConfig(OpcUaClientConfigBuilder builder) {
    builder.setIdentityProvider(new UsernameProvider("user1", "password"));
  }

  @Override
  protected void configureTestNamespace(TestNamespace ignored) {
    namespace = new OwnerNamespace();
    namespace.startup();
    server.updateReferenceTypeTree();
  }

  @AfterAll
  void stopOwnerNamespace() {
    if (namespace != null) namespace.shutdown();
  }

  // Both owners reach the shared Method through a custom HasComponent subtype, so dispatch must
  // resolve ownership through the reference type tree before consulting the owner's handlers.
  // Access checks still apply: a non-executable Method is denied before any handler runs.
  @Test
  void ownersDispatchIndependentlyAndAccessChecksStillApply() throws Exception {
    AtomicInteger calls = new AtomicInteger();
    namespace.method.setInvocationHandler(reply("fallback", calls));
    namespace.first.setMethodHandler(namespace.method.getNodeId(), reply("first", calls));

    assertEquals("first", value(call(namespace.first, namespace.method)));
    assertEquals("fallback", value(call(namespace.second, namespace.method)));

    namespace.method.setUserExecutable(false);
    try {
      int before = calls.get();
      assertEquals(
          new StatusCode(StatusCodes.Bad_UserAccessDenied),
          call(namespace.first, namespace.method).getStatusCode());
      assertEquals(before, calls.get(), "denied calls never reach a handler");
    } finally {
      namespace.method.setUserExecutable(true);
    }

    namespace.first.setMethodHandler(namespace.method.getNodeId(), null);
    assertEquals("fallback", value(call(namespace.first, namespace.method)));
  }

  // Part 4 §5.12.2 permits an ObjectType as ObjectId only for Methods without a ModellingRule.
  // ObjectTypes have no handler map; their Methods keep dispatching to the Method node's handler.
  @Test
  void typeLevelMethodsUseTheMethodNodeHandlerAndInstanceDeclarationsAreRejected()
      throws Exception {
    namespace.typeMethod.setInvocationHandler(reply("type", new AtomicInteger()));
    assertEquals("type", value(call(namespace.type, namespace.typeMethod)));
    assertEquals(
        new StatusCode(StatusCodes.Bad_MethodInvalid),
        call(namespace.type, namespace.instanceDeclaration).getStatusCode());
  }

  // Part 9 specializes the ObjectType-target failure to Bad_NodeIdInvalid for Condition instance
  // Methods. Ownership is derived from any HasComponent-derived reference, not only HasComponent.
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

  // Part 9 Condition Methods are answered by the ConditionManager ahead of any node or Object
  // handler, so registering a Condition keeps that precedence.
  @Test
  void registeredConditionManagerRetainsMethodDispatchPrecedence() throws Exception {
    Condition condition = namespace.createCondition();
    server.getConditionManager().register(condition);
    try {
      UaMethodNode method = condition.getNode().getEnableMethodNode();
      assertTrue(
          server
              .getConditionManager()
              .findMethodInvocationHandler(condition.getConditionId(), method.getNodeId())
              .isPresent());
      condition
          .getNode()
          .setMethodHandler(method.getNodeId(), reply("ignored", new AtomicInteger()));
      // Adopted Conditions default to Enabled, so the Condition's own handler answers.
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

  private final class OwnerNamespace extends ManagedNamespaceWithLifecycle {
    final UaObjectNode first;
    final UaObjectNode second;
    final UaMethodNode method;
    final UaObjectTypeNode type;
    final UaMethodNode typeMethod;
    final UaMethodNode instanceDeclaration;
    final NodeId componentId;

    OwnerNamespace() {
      super(ObjectMethodHandlerServiceTest.this.server, "urn:milo:test:object-method-handlers");
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
      return UaMethodNode.builder(getNodeContext())
          .setNodeId(newNodeId(name))
          .setBrowseName(newQualifiedName(name))
          .setDisplayName(LocalizedText.english(name))
          .buildAndAdd();
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

    @Override
    public void onDataItemsCreated(List<DataItem> items) {}

    @Override
    public void onDataItemsModified(List<DataItem> items) {}

    @Override
    public void onDataItemsDeleted(List<DataItem> items) {}

    @Override
    public void onMonitoringModeChanged(List<MonitoredItem> items) {}
  }
}
