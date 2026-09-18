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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.client.identity.UsernameProvider;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerTypeGetMonitoredItems;
import org.eclipse.milo.opcua.sdk.server.model.ObjectTypeInitializer;
import org.eclipse.milo.opcua.sdk.server.model.VariableTypeInitializer;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseObjectTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.FolderTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.objects.ServerTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseDataVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.model.variables.BaseVariableTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.BrowsePath;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.InstantiationRequest;
import org.eclipse.milo.opcua.sdk.server.nodes.instantiation.MethodInstantiation;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.junit.jupiter.api.Test;

/** Regenerated ns0 constructors, properties and typed handlers exercised through a real service. */
class GeneratedNs0MethodServiceTest extends AbstractClientServerTest {
  @Override
  protected void customizeClientConfig(OpcUaClientConfigBuilder builder) {
    builder.setIdentityProvider(new UsernameProvider("user1", "password"));
  }

  @Test
  void startupRegistersGeneratedConstructorsAndReadsLiveProperties() throws Exception {
    ObjectTypeInitializer.initialize(server.getNamespaceTable(), server.getObjectTypeManager());
    VariableTypeInitializer.initialize(server.getNamespaceTable(), server.getVariableTypeManager());
    assertEquals(
        BaseObjectTypeNode.class,
        server
            .getObjectTypeManager()
            .getRegisteredType(NodeIds.BaseObjectType)
            .orElseThrow()
            .nodeClass());
    assertEquals(
        BaseVariableTypeNode.class,
        server
            .getVariableTypeManager()
            .getRegisteredType(NodeIds.BaseVariableType)
            .orElseThrow()
            .nodeClass());
    assertEquals(
        BaseDataVariableTypeNode.class,
        server
            .getVariableTypeManager()
            .getRegisteredType(NodeIds.BaseDataVariableType)
            .orElseThrow()
            .nodeClass());
    ServerTypeNode node =
        assertInstanceOf(
            ServerTypeNode.class,
            server.getAddressSpaceManager().getManagedNode(NodeIds.Server).orElseThrow());
    assertArrayEquals(server.getNamespaceTable().toArray(), node.getNamespaceArray());
    assertNotNull(node.getServerStatusNode().getCurrentTime());
    FolderTypeNode folder =
        server
            .getNodeInstantiator()
            .instantiate(
                InstantiationRequest.of(FolderTypeNode.class, NodeIds.FolderType)
                    .nodeId(newNodeId("generated-folder"))
                    .browseName(newQualifiedName("GeneratedFolder"))
                    .target(testNamespace.getNodeManager())
                    .build())
            .root();
    assertSame(folder, testNamespace.getNodeManager().get(folder.getNodeId()));
  }

  @Test
  void getMonitoredItemsReturnsRealSubscriptionHandlesThroughBothMethodIdentities()
      throws Exception {
    UInteger subscription =
        client
            .createSubscription(1000.0, uint(60), uint(10), uint(0), false, ubyte(0))
            .getSubscriptionId();
    try {
      MonitoredItemCreateResult item =
          client.createMonitoredItems(
                  subscription,
                  TimestampsToReturn.Both,
                  List.of(
                      new MonitoredItemCreateRequest(
                          new ReadValueId(
                              NodeIds.Server_ServerStatus_CurrentTime,
                              AttributeId.Value.uid(),
                              null,
                              QualifiedName.NULL_VALUE),
                          MonitoringMode.Sampling,
                          new MonitoringParameters(uint(741), 1000.0, null, uint(1), true))))
              .getResults()[0];
      assertTrue(item.getStatusCode().isGood());
      for (NodeId method :
          List.of(NodeIds.Server_GetMonitoredItems, NodeIds.ServerType_GetMonitoredItems)) {
        CallMethodResult result = call(NodeIds.Server, method, subscription);
        assertTrue(result.getStatusCode().isGood(), result.toString());
        ServerTypeGetMonitoredItems.Outputs outputs =
            ServerTypeGetMonitoredItems.Outputs.fromVariants(
                client.getStaticEncodingContext(), result.getOutputArguments());
        assertArrayEquals(new UInteger[] {item.getMonitoredItemId()}, outputs.serverHandles());
        assertArrayEquals(new UInteger[] {uint(741)}, outputs.clientHandles());
      }
    } finally {
      client.deleteSubscriptions(List.of(subscription));
    }
  }

  @Test
  void generatedInstanceHandlersReplaceClearAndFallBackThroughTheCallService() throws Exception {
    ServerTypeNode first = instance("first");
    ServerTypeNode second = instance("second");
    UaMethodNode method = first.getGetMonitoredItemsMethodNode();
    assertNotNull(method);
    method.setInvocationHandler(
        (context, request) ->
            new CallMethodResult(new StatusCode(StatusCodes.Bad_NotSupported), null, null, null));
    first.setGetMonitoredItemsHandler((context, subscription) -> handles(11));
    second.setGetMonitoredItemsHandler((context, subscription) -> handles(22));
    for (NodeId methodId : List.of(method.getNodeId(), NodeIds.ServerType_GetMonitoredItems)) {
      assertEquals(
          uint(11), decoded(call(first.getNodeId(), methodId, uint(1))).serverHandles()[0]);
    }
    assertEquals(
        uint(22),
        decoded(call(second.getNodeId(), NodeIds.ServerType_GetMonitoredItems, uint(1)))
            .serverHandles()[0]);
    first.setGetMonitoredItemsHandler(
        (context, subscription) -> {
          context.setStatusCode(new StatusCode(StatusCodes.Uncertain));
          return handles(33);
        });
    CallMethodResult replaced = call(first.getNodeId(), method.getNodeId(), uint(1));
    assertTrue(replaced.getStatusCode().isUncertain());
    assertEquals(uint(33), decoded(replaced).serverHandles()[0]);
    first.setGetMonitoredItemsHandler(null);
    assertEquals(
        new StatusCode(StatusCodes.Bad_NotSupported),
        call(first.getNodeId(), method.getNodeId(), uint(1)).getStatusCode());
    assertEquals(
        uint(22),
        decoded(call(second.getNodeId(), NodeIds.ServerType_GetMonitoredItems, uint(1)))
            .serverHandles()[0]);
  }

  // Reusing validators must not freeze the live Method declaration at handler installation.
  @Test
  void installedHandlerObservesChangedArgumentProperties() throws Exception {
    ServerTypeNode owner = instance("live-arguments");
    UaMethodNode method = owner.getGetMonitoredItemsMethodNode();
    assertNotNull(method);
    owner.setGetMonitoredItemsHandler((context, subscription) -> handles(44));
    Argument[] inputs = method.getInputArguments();
    Argument[] outputs = method.getOutputArguments();
    assertNotNull(inputs);
    assertNotNull(outputs);
    assertTrue(call(owner.getNodeId(), method.getNodeId(), uint(1)).getStatusCode().isGood());
    method.setInputArguments(
        new Argument[] {
          new Argument("SubscriptionId", NodeIds.String, -1, null, LocalizedText.NULL_VALUE)
        });
    assertTrue(call(owner.getNodeId(), method.getNodeId(), uint(1)).getStatusCode().isBad());
    method.setInputArguments(inputs);
    method.setOutputArguments(
        new Argument[] {
          new Argument("ServerHandles", NodeIds.String, 1, null, LocalizedText.NULL_VALUE),
          outputs[1]
        });
    assertEquals(
        new StatusCode(StatusCodes.Bad_TypeMismatch),
        call(owner.getNodeId(), method.getNodeId(), uint(1)).getStatusCode());
    method.setOutputArguments(outputs);
    assertEquals(
        uint(44), decoded(call(owner.getNodeId(), method.getNodeId(), uint(1))).serverHandles()[0]);
  }

  private ServerTypeNode instance(String name) throws Exception {
    return server
        .getNodeInstantiator()
        .instantiate(
            InstantiationRequest.of(ServerTypeNode.class, NodeIds.ServerType)
                .nodeId(newNodeId("generated-" + name))
                .browseName(newQualifiedName(name))
                .target(testNamespace.getNodeManager())
                .methodInstantiation(MethodInstantiation.COPY)
                .includeOptional(BrowsePath.of(new QualifiedName(0, "GetMonitoredItems")))
                .build())
        .root();
  }

  private CallMethodResult call(NodeId owner, NodeId method, UInteger subscription)
      throws Exception {
    return client.call(
            List.of(
                new CallMethodRequest(owner, method, new Variant[] {new Variant(subscription)})))
        .getResults()[0];
  }

  private ServerTypeGetMonitoredItems.Outputs decoded(CallMethodResult result) throws Exception {
    assertFalse(result.getStatusCode().isBad(), result.toString());
    return ServerTypeGetMonitoredItems.Outputs.fromVariants(
        client.getStaticEncodingContext(), result.getOutputArguments());
  }

  private static ServerTypeGetMonitoredItems.Outputs handles(int value) {
    return new ServerTypeGetMonitoredItems.Outputs(
        new UInteger[] {uint(value)}, new UInteger[] {uint(value + 1)});
  }
}
