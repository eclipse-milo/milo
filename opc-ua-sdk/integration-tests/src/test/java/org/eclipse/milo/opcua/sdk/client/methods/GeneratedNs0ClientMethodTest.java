/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.milo.opcua.sdk.client.methods;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.variables.ServerStatusTypeNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.core.model.methods.ServerTypeGetMonitoredItems;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MonitoringMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoredItemCreateResult;
import org.eclipse.milo.opcua.stack.core.types.structured.MonitoringParameters;
import org.eclipse.milo.opcua.stack.core.types.structured.ReadValueId;
import org.junit.jupiter.api.Test;

/** The generated ns0 client Method call forms and typed child accessors, driven end to end. */
class GeneratedNs0ClientMethodTest extends AbstractClientServerTest {
  private static final UInteger CLIENT_HANDLE = uint(912);

  @Test
  void callFormsAgreeOnTypedOutputsForALiveSubscription() throws Exception {
    ServerTypeNode serverNode = serverNode();
    UaMethodNode methodNode = serverNode.getGetMonitoredItemsMethodNode();
    assertNotNull(methodNode);
    assertEquals(NodeIds.Server_GetMonitoredItems, methodNode.getNodeId());

    UInteger subscription = subscribe();
    try {
      UInteger serverHandle = monitor(subscription);

      ServerTypeGetMonitoredItems.Outputs outputs = serverNode.getMonitoredItems(subscription);
      assertArrayEquals(new UInteger[] {serverHandle}, outputs.serverHandles());
      assertArrayEquals(new UInteger[] {CLIENT_HANDLE}, outputs.clientHandles());

      MethodCallResult<ServerTypeGetMonitoredItems.Outputs> result =
          serverNode.callGetMonitoredItems(subscription);
      assertTrue(result.statusCode().isGood(), result::toString);
      assertTrue(result.hasOutputs());
      assertEquals(outputs, result.outputs());
      assertEquals(2, result.rawOutputs().length);

      assertEquals(
          outputs,
          serverNode.callGetMonitoredItemsWith(MethodCallOptions.DEFAULT, subscription).outputs());
      assertEquals(outputs, serverNode.getMonitoredItemsAsync(subscription).get());
      assertEquals(outputs, serverNode.callGetMonitoredItemsAsync(subscription).get().outputs());
    } finally {
      client.deleteSubscriptions(List.of(subscription));
    }
  }

  // A Bad operation status is a result for the call forms and an exception for the plain form.
  @Test
  void unknownSubscriptionIsAResultForCallFormsAndAnExceptionForThePlainForm() throws Exception {
    ServerTypeNode serverNode = serverNode();
    UInteger unknown = uint(0);

    MethodCallResult<ServerTypeGetMonitoredItems.Outputs> result =
        serverNode.callGetMonitoredItems(unknown);
    assertEquals(new StatusCode(StatusCodes.Bad_SubscriptionIdInvalid), result.statusCode());
    assertFalse(result.hasOutputs());

    UaMethodException failure =
        assertThrows(UaMethodException.class, () -> serverNode.getMonitoredItems(unknown));
    assertEquals(new StatusCode(StatusCodes.Bad_SubscriptionIdInvalid), failure.getStatusCode());
  }

  // Selected namespace-zero children narrow the generated return type.
  @Test
  void typedChildAccessorsResolveGeneratedNodeClasses() throws Exception {
    ServerStatusTypeNode serverStatusNode = serverNode().getServerStatusNode();
    assertEquals(NodeIds.Server_ServerStatus, serverStatusNode.getNodeId());
    assertNotNull(serverStatusNode.readBuildInfo());
    assertEquals(
        NodeIds.Server_ServerStatus_BuildInfo, serverStatusNode.getBuildInfoNode().getNodeId());
  }

  private ServerTypeNode serverNode() throws Exception {
    return assertInstanceOf(
        ServerTypeNode.class,
        client.getAddressSpace().getObjectNode(NodeIds.Server, NodeIds.ServerType));
  }

  private UInteger subscribe() throws Exception {
    return client
        .createSubscription(1000.0, uint(60), uint(10), uint(0), false, ubyte(0))
        .getSubscriptionId();
  }

  private UInteger monitor(UInteger subscription) throws Exception {
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
                        new MonitoringParameters(CLIENT_HANDLE, 1000.0, null, uint(1), true))))
            .getResults()[0];
    assertTrue(item.getStatusCode().isGood(), item::toString);
    return item.getMonitoredItemId();
  }
}
