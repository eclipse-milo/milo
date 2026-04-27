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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.GetEndpointsResponse;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.junit.jupiter.api.Test;

class DiscoveryClientTest {

  @Test
  void reverseDiscoverySuccessFailsWhenDisconnectFails() {
    var cleanupFailure = new UaException(StatusCodes.Bad_Shutdown, "cleanup failed");
    var discoveryClient =
        new DisconnectOnlyDiscoveryClient(CompletableFuture.failedFuture(cleanupFailure));

    CompletableFuture<List<String>> future =
        DiscoveryClient.disconnectAfterReverseDiscovery(discoveryClient, List.of("endpoint"), null);

    ExecutionException ex =
        assertThrows(ExecutionException.class, () -> future.get(1, TimeUnit.SECONDS));
    assertSame(cleanupFailure, ex.getCause());
  }

  @Test
  void reverseDiscoveryFailureSuppressesDisconnectFailure() {
    var discoveryFailure = new UaException(StatusCodes.Bad_Timeout, "discovery failed");
    var cleanupFailure = new UaException(StatusCodes.Bad_Shutdown, "cleanup failed");
    var discoveryClient =
        new DisconnectOnlyDiscoveryClient(CompletableFuture.failedFuture(cleanupFailure));

    CompletableFuture<List<String>> future =
        DiscoveryClient.disconnectAfterReverseDiscovery(discoveryClient, null, discoveryFailure);

    ExecutionException ex =
        assertThrows(ExecutionException.class, () -> future.get(1, TimeUnit.SECONDS));
    assertSame(discoveryFailure, ex.getCause());
    assertEquals(1, ex.getCause().getSuppressed().length);
    assertSame(cleanupFailure, ex.getCause().getSuppressed()[0]);
  }

  @Test
  void discoverySuccessWaitsForDisconnect() throws Exception {
    CompletableFuture<DiscoveryClient> disconnectFuture = new CompletableFuture<>();
    var discoveryClient = new DisconnectOnlyDiscoveryClient(disconnectFuture);

    CompletableFuture<List<String>> future =
        DiscoveryClient.disconnectAfterDiscovery(discoveryClient, List.of("endpoint"), null);

    assertFalse(future.isDone());

    disconnectFuture.complete(discoveryClient);

    assertEquals(List.of("endpoint"), future.get(1, TimeUnit.SECONDS));
  }

  @Test
  void reverseConnectGetEndpointsTimeoutIncludesServiceRequest() {
    var discoveryClient = new ScriptedDiscoveryClient();

    CompletableFuture<List<EndpointDescription>> future =
        DiscoveryClient.getReverseConnectEndpoints(discoveryClient, 50);

    ExecutionException ex =
        assertThrows(ExecutionException.class, () -> future.get(1, TimeUnit.SECONDS));

    assertInstanceOf(TimeoutException.class, ex.getCause());
    assertTrue(discoveryClient.disconnectCalled());
  }

  private static EndpointDescription endpoint() {
    return new EndpointDescription(
        "",
        null,
        null,
        MessageSecurityMode.None,
        SecurityPolicy.None.getUri(),
        null,
        Stack.TCP_UASC_UABINARY_TRANSPORT_URI,
        ubyte(0));
  }

  private static final class DisconnectOnlyDiscoveryClient extends DiscoveryClient {

    private final CompletableFuture<DiscoveryClient> disconnectFuture;

    private DisconnectOnlyDiscoveryClient(CompletableFuture<DiscoveryClient> disconnectFuture) {
      super(endpoint(), new NoopTransport());

      this.disconnectFuture = disconnectFuture;
    }

    @Override
    public CompletableFuture<DiscoveryClient> disconnectAsync() {
      return disconnectFuture;
    }
  }

  private static final class ScriptedDiscoveryClient extends DiscoveryClient {

    private final CompletableFuture<GetEndpointsResponse> getEndpointsFuture =
        new CompletableFuture<>();
    private final AtomicBoolean disconnectCalled = new AtomicBoolean();

    private ScriptedDiscoveryClient() {
      super(endpoint(), new NoopTransport());
    }

    @Override
    public CompletableFuture<DiscoveryClient> connectAsync() {
      return CompletableFuture.completedFuture(this);
    }

    @Override
    public CompletableFuture<GetEndpointsResponse> getEndpoints(
        String endpointUrl, String[] localeIds, String[] profileUris) {

      return getEndpointsFuture;
    }

    @Override
    public CompletableFuture<DiscoveryClient> disconnectAsync() {
      disconnectCalled.set(true);

      return CompletableFuture.completedFuture(this);
    }

    private boolean disconnectCalled() {
      return disconnectCalled.get();
    }
  }

  private static final class NoopTransport implements OpcClientTransport {

    @Override
    public OpcClientTransportConfig getConfig() {
      throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<Unit> connect(ClientApplicationContext applicationContext) {
      throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<Unit> disconnect() {
      throw new UnsupportedOperationException();
    }

    @Override
    public CompletableFuture<UaResponseMessageType> sendRequestMessage(
        UaRequestMessageType requestMessage) {

      throw new UnsupportedOperationException();
    }
  }
}
