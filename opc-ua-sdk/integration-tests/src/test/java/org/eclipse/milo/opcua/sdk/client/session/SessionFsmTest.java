/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.session;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

@Timeout(value = 30, unit = TimeUnit.SECONDS)
public class SessionFsmTest {

  private static final Duration STATE_WAIT_TIMEOUT = Duration.ofSeconds(10);

  @Test
  public void testCloseSessionWhileInactive() throws Exception {
    OpcUaClientConfig clientConfig =
        OpcUaClientConfig.builder()
            .setEndpoint(
                new EndpointDescription(
                    "opc.tcp://localhost:12685",
                    null,
                    null,
                    MessageSecurityMode.None,
                    SecurityPolicy.None.getUri(),
                    null,
                    TransportProfile.TCP_UASC_UABINARY.getUri(),
                    null))
            .setApplicationName(LocalizedText.english("Eclipse Milo Test Client"))
            .setApplicationUri("urn:eclipse:milo:examples:client")
            .build();

    OpcUaClient client = OpcUaClient.create(clientConfig);

    SessionFsm sessionFsm = SessionFsmFactory.newSessionFsm(client);

    assertNotNull(sessionFsm.closeSession().get());
  }

  /**
   * Verify that SessionFuture instances are properly completed with an exception when
   * closeSession() is called in the CreatingWait state.
   */
  @Test
  public void testCloseSessionCompletesSessionFutureInCreatingWait() throws Exception {
    var transport = new CreateSessionFailureTransport();
    OpcUaClient client = new OpcUaClient(newClientConfig(), transport);

    try {
      client.connectAsync();

      SessionFsm sessionFsm = client.getSessionFsm();
      awaitState(sessionFsm, State.CreatingWait);

      CompletableFuture<OpcUaSession> sessionFuture = sessionFsm.getSession();
      CompletableFuture<?> closeFuture = sessionFsm.closeSession();

      assertThrows(ExecutionException.class, () -> sessionFuture.get(5, TimeUnit.SECONDS));
      assertNotNull(closeFuture.get(5, TimeUnit.SECONDS));
    } finally {
      disconnectQuietly(client);
    }
  }

  /**
   * Verify that SessionFuture instances are properly completed with an exception when
   * closeSession() is called in the ReactivatingWait state.
   */
  @Test
  public void testCloseSessionCompletesSessionFutureInReactivatingWait() throws Exception {
    OpcUaServer server = TestServer.create().getServer();
    OpcUaClient client = null;

    try {
      server.startup().get(10, TimeUnit.SECONDS);

      client = TestClient.create(server, cfg -> {});
      client.connect();

      Thread.sleep(1000);

      server.shutdown().get(10, TimeUnit.SECONDS);

      SessionFsm sessionFsm = client.getSessionFsm();
      awaitState(sessionFsm, State.ReactivatingWait);

      CompletableFuture<OpcUaSession> sessionFuture = sessionFsm.getSession();
      CompletableFuture<?> closeFuture = sessionFsm.closeSession();

      assertThrows(ExecutionException.class, () -> sessionFuture.get(5, TimeUnit.SECONDS));
      assertNotNull(closeFuture.get(5, TimeUnit.SECONDS));
    } finally {
      disconnectQuietly(client);
      shutdownQuietly(server);
    }
  }

  private static void awaitState(SessionFsm sessionFsm, State expected) throws Exception {
    long deadline = System.nanoTime() + STATE_WAIT_TIMEOUT.toNanos();
    while (System.nanoTime() < deadline && sessionFsm.getState() != expected) {
      Thread.sleep(25);
    }

    assertEquals(
        expected,
        sessionFsm.getState(),
        "SessionFsm did not reach " + expected + " within " + STATE_WAIT_TIMEOUT);
  }

  private static void disconnectQuietly(OpcUaClient client) {
    if (client == null) {
      return;
    }

    try {
      client.disconnectAsync().get(5, TimeUnit.SECONDS);
    } catch (Exception ignored) {
      // don't mask the original failure
    }
  }

  private static void shutdownQuietly(OpcUaServer server) {
    try {
      server.shutdown().get(5, TimeUnit.SECONDS);
    } catch (Exception ignored) {
      // don't mask the original failure
    }
  }

  private static OpcUaClientConfig newClientConfig() {
    return OpcUaClientConfig.builder()
        .setEndpoint(
            new EndpointDescription(
                "opc.tcp://localhost:12685",
                new ApplicationDescription(
                    "urn:eclipse:milo:test:server",
                    "urn:eclipse:milo:test:product",
                    LocalizedText.english("Eclipse Milo Test Server"),
                    ApplicationType.Server,
                    null,
                    null,
                    new String[0]),
                ByteString.NULL_VALUE,
                MessageSecurityMode.None,
                SecurityPolicy.None.getUri(),
                null,
                TransportProfile.TCP_UASC_UABINARY.getUri(),
                null))
        .setApplicationName(LocalizedText.english("Eclipse Milo Test Client"))
        .setApplicationUri("urn:eclipse:milo:test:client")
        .build();
  }

  private static final class CreateSessionFailureTransport implements OpcClientTransport {

    private final OpcClientTransportConfig config =
        OpcTcpClientTransportConfig.newBuilder().build();

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

      return CompletableFuture.failedFuture(
          new UaException(StatusCodes.Bad_ConnectionClosed, "create session failed"));
    }
  }
}
