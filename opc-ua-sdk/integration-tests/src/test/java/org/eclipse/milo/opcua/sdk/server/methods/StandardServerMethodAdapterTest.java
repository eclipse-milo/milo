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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.client.identity.UsernameProvider;
import org.eclipse.milo.opcua.sdk.client.model.objects.ServerType;
import org.eclipse.milo.opcua.sdk.client.subscriptions.OpcUaSubscription;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class StandardServerMethodAdapterTest extends AbstractClientServerTest {

  @Override
  protected void customizeClientConfig(OpcUaClientConfigBuilder builder) {
    builder.setIdentityProvider(new UsernameProvider("user1", "password"));
  }

  // Both handwritten callbacks cast UInt32 inputs. The inherited validator must reject malformed
  // input before the subscription lookup or callback can run.
  @ParameterizedTest
  @MethodSource("subscriptionMethods")
  void invalidSubscriptionTypeReturnsPerInputFailure(NodeId methodId) throws UaException {
    CallMethodResult result = call(methodId, new Variant("not a subscription id"));

    assertEquals(new StatusCode(StatusCodes.Bad_InvalidArgument), result.getStatusCode());
    assertArrayEquals(
        new StatusCode[] {new StatusCode(StatusCodes.Bad_TypeMismatch)},
        result.getInputArgumentResults());
    assertEquals(0, result.getOutputArguments().length);
  }

  static Stream<NodeId> subscriptionMethods() {
    return Stream.of(NodeIds.Server_GetMonitoredItems, NodeIds.Server_ResendData);
  }

  // Returning arrays directly must retain the UInt32 wire discriminator even when both are empty.
  @Test
  void getMonitoredItemsPreservesTypedEmptyOutputs() throws UaException {
    OpcUaSubscription subscription = new OpcUaSubscription(client);
    subscription.create();
    try {
      CallMethodResult result =
          call(
              NodeIds.Server_GetMonitoredItems,
              new Variant(subscription.getSubscriptionId().orElseThrow()));

      assertEquals(StatusCode.GOOD, result.getStatusCode());
      assertEquals(2, result.getOutputArguments().length);
      assertArrayEquals(
          new UInteger[0],
          assertInstanceOf(UInteger[].class, result.getOutputArguments()[0].value()));
      assertArrayEquals(
          new UInteger[0],
          assertInstanceOf(UInteger[].class, result.getOutputArguments()[1].value()));

      // The generated convenience must preserve the same typed empty arrays after shared decoding.
      ServerType serverNode =
          assertInstanceOf(ServerType.class, client.getAddressSpace().getNode(NodeIds.Server));
      var outputs =
          serverNode.callGetMonitoredItems(subscription.getSubscriptionId().orElseThrow());
      assertArrayEquals(new UInteger[0], outputs.serverHandles());
      assertArrayEquals(new UInteger[0], outputs.clientHandles());
    } finally {
      subscription.delete();
    }
  }

  // Two authenticated sessions using the same username still own separate subscriptions.
  @Test
  void resendDataUsesTheCallingSessionsSubscription() throws Exception {
    OpcUaClient other = TestClient.create(server, this::customizeClientConfig);
    other.connectAsync().get(10, TimeUnit.SECONDS);
    try {
      OpcUaSubscription subscription = new OpcUaSubscription(other);
      subscription.create();
      NodeId methodId = NodeIds.Server_ResendData;
      Variant subscriptionId = new Variant(subscription.getSubscriptionId().orElseThrow());
      try {
        CallMethodResult denied = call(methodId, subscriptionId);
        assertEquals(new StatusCode(StatusCodes.Bad_UserAccessDenied), denied.getStatusCode());
        assertEquals(0, denied.getOutputArguments().length);

        CallMethodResult accepted =
            other.call(
                    List.of(
                        new CallMethodRequest(
                            NodeIds.Server, methodId, new Variant[] {subscriptionId})))
                .getResults()[0];
        assertEquals(StatusCode.GOOD, accepted.getStatusCode());
        assertEquals(0, accepted.getOutputArguments().length);
      } finally {
        subscription.delete();
      }

      CallMethodResult removed = call(methodId, subscriptionId);
      assertEquals(new StatusCode(StatusCodes.Bad_SubscriptionIdInvalid), removed.getStatusCode());
      assertEquals(0, removed.getOutputArguments().length);
    } finally {
      other.disconnectAsync().get(10, TimeUnit.SECONDS);
    }
  }

  private CallMethodResult call(NodeId methodId, Variant input) throws UaException {
    return client.call(
            List.of(new CallMethodRequest(NodeIds.Server, methodId, new Variant[] {input})))
        .getResults()[0];
  }
}
