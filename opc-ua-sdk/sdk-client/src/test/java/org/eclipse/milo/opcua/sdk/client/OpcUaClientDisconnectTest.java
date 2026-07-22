/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OpcUaClientDisconnectTest {

  private long savedTimeoutMillis;

  @BeforeEach
  void setUp() {
    savedTimeoutMillis = OpcUaClient.disconnectCloseSessionTimeoutMillis;
  }

  @AfterEach
  void tearDown() {
    OpcUaClient.disconnectCloseSessionTimeoutMillis = savedTimeoutMillis;
  }

  /**
   * Reproduces CR-002: when the SessionFsm is in a non-terminal state (Creating/Activating/...)
   * waiting on a request whose transport channel is hung, the CloseSession event is shelved
   * indefinitely. disconnectAsync() must still complete in bounded time so callers aren't
   * deadlocked.
   */
  @Test
  void disconnectAsyncCompletesEvenWhenSessionFsmCloseIsShelved() throws Exception {
    OpcUaClient.disconnectCloseSessionTimeoutMillis = 250L;

    HangingClientTransport transport = new HangingClientTransport();
    OpcUaClientConfig config =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint())
            .setDiscoveryEndpoints(List.of())
            .build();
    OpcUaClient client = new OpcUaClient(config, transport);

    // Start connect: transport.connect() succeeds, then sessionFsm.openSession() fires
    // CreateSessionRequest via sendRequestMessage(), which never completes.
    CompletableFuture<OpcUaClient> connectFuture = client.connectAsync();

    assertTrue(
        transport.createSessionSent.await(5, TimeUnit.SECONDS),
        "SessionFsm should reach Creating and call sendRequestMessage");

    long startNanos = System.nanoTime();
    client.disconnectAsync().get(5, TimeUnit.SECONDS);
    long elapsedMillis = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNanos);

    assertTrue(
        elapsedMillis < 3_000L,
        "disconnectAsync must complete in bounded time despite shelved CloseSession; took "
            + elapsedMillis
            + "ms");

    // Free the dangling futures so JUnit doesn't leak threads.
    transport.releasePending();
    connectFuture.completeExceptionally(new RuntimeException("test cleanup"));
  }

  private static EndpointDescription endpoint() {
    var server =
        new ApplicationDescription(
            "urn:eclipse:milo:test:server",
            "urn:eclipse:milo:test:product",
            LocalizedText.english("test server"),
            ApplicationType.Server,
            null,
            null,
            null);

    var anonymousPolicy =
        new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null);

    return new EndpointDescription(
        "opc.tcp://localhost:12685/test",
        server,
        ByteString.NULL_VALUE,
        MessageSecurityMode.None,
        SecurityPolicy.None.getUri(),
        new UserTokenPolicy[] {anonymousPolicy},
        Stack.TCP_UASC_UABINARY_TRANSPORT_URI,
        ubyte(0));
  }

  private static final class HangingClientTransport implements OpcClientTransport {

    private final OpcClientTransportConfig config =
        OpcTcpClientTransportConfig.newBuilder().build();
    private final CountDownLatch createSessionSent = new CountDownLatch(1);
    private final CompletableFuture<UaResponseMessageType> pendingResponse =
        new CompletableFuture<>();

    @Override
    public OpcClientTransportConfig getConfig() {
      return config;
    }

    @Override
    public CompletableFuture<Unit> connect(ClientApplicationContext applicationContext) {
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    @Override
    public CompletableFuture<Unit> disconnect() {
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    @Override
    public CompletableFuture<UaResponseMessageType> sendRequestMessage(
        UaRequestMessageType requestMessage) {

      createSessionSent.countDown();
      return pendingResponse;
    }

    void releasePending() {
      pendingResponse.completeExceptionally(new RuntimeException("test cleanup"));
    }
  }
}
