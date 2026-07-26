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

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_MethodInvalid;
import static org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_NodeIdInvalid;
import static org.eclipse.milo.opcua.stack.core.StatusCodes.Bad_SubscriptionIdInvalid;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;
import org.junit.jupiter.api.Test;

public class ManagedAddressSpaceMethodDispatchTest extends AbstractClientServerTest {

  @Test
  void addCommentCannotBeInvokedOnConditionType() throws UaException {
    assertModelledConditionMethodCannotBeInvokedOnTypeNode(
        NodeIds.ConditionType, NodeIds.ConditionType_AddComment);
  }

  @Test
  void acknowledgeCannotBeInvokedOnAcknowledgeableConditionType() throws UaException {
    assertModelledConditionMethodCannotBeInvokedOnTypeNode(
        NodeIds.AcknowledgeableConditionType, NodeIds.AcknowledgeableConditionType_Acknowledge);
  }

  @Test
  void inheritedConditionMethodCannotBeInvokedOnConditionSubtype() throws UaException {
    assertModelledConditionMethodCannotBeInvokedOnTypeNode(
        NodeIds.AlarmConditionType, NodeIds.ConditionType_AddComment);
  }

  private void assertModelledConditionMethodCannotBeInvokedOnTypeNode(
      NodeId objectTypeId, NodeId methodId) throws UaException {

    var request = new CallMethodRequest(objectTypeId, methodId, new Variant[0]);

    CallMethodResult result = call(request);

    assertEquals(StatusCode.of(Bad_NodeIdInvalid), result.getStatusCode());
  }

  @Test
  void unrelatedModelledMethodOnObjectTypeReturnsBadMethodInvalid() throws UaException {
    var request =
        new CallMethodRequest(
            NodeIds.ServerType, NodeIds.ServerType_GetMonitoredItems, new Variant[0]);

    CallMethodResult result = call(request);

    assertEquals(StatusCode.of(Bad_MethodInvalid), result.getStatusCode());
  }

  @Test
  void unmodelledObjectTypeMethodRemainsCallable() throws UaException {
    var request =
        new CallMethodRequest(
            NodeIds.ConditionType,
            NodeIds.ConditionType_ConditionRefresh,
            new Variant[] {new Variant(UInteger.valueOf(0))});

    CallMethodResult result = call(request);

    assertEquals(StatusCode.of(Bad_SubscriptionIdInvalid), result.getStatusCode());
  }

  @Test
  void objectInstanceMethodRemainsCallable() throws UaException {
    var request =
        new CallMethodRequest(
            NodeIds.ObjectsFolder, newNodeId("sqrt(x)"), new Variant[] {new Variant(16.0)});

    CallMethodResult result = call(request);

    assertEquals(StatusCode.GOOD, result.getStatusCode());
    assertEquals(new Variant(4.0), requireNonNull(result.getOutputArguments())[0]);
  }

  private CallMethodResult call(CallMethodRequest request) throws UaException {
    CallResponse response = client.call(List.of(request));

    return requireNonNull(response.getResults())[0];
  }
}
