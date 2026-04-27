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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransportConfig;
import org.junit.jupiter.api.Test;

class DiscoveryClientTest {

  @Test
  void reverseDiscoverySuccessFailsWhenDisconnectFails() {
    var cleanupFailure = new UaException(StatusCodes.Bad_Shutdown, "cleanup failed");
    var discoveryClient = new DisconnectOnlyDiscoveryClient(cleanupFailure);

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
    var discoveryClient = new DisconnectOnlyDiscoveryClient(cleanupFailure);

    CompletableFuture<List<String>> future =
        DiscoveryClient.disconnectAfterReverseDiscovery(discoveryClient, null, discoveryFailure);

    ExecutionException ex =
        assertThrows(ExecutionException.class, () -> future.get(1, TimeUnit.SECONDS));
    assertSame(discoveryFailure, ex.getCause());
    assertEquals(1, ex.getCause().getSuppressed().length);
    assertSame(cleanupFailure, ex.getCause().getSuppressed()[0]);
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

    private final Throwable cleanupFailure;

    private DisconnectOnlyDiscoveryClient(Throwable cleanupFailure) {
      super(endpoint(), new NoopTransport());

      this.cleanupFailure = cleanupFailure;
    }

    @Override
    public CompletableFuture<DiscoveryClient> disconnectAsync() {
      return CompletableFuture.failedFuture(cleanupFailure);
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
