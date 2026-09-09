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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class EndpointResolverTest {
  // A peer that accepts TCP but never answers Hello must not retain a discovery socket or retry
  // loop.
  @ParameterizedTest(name = "cancel={0}")
  @ValueSource(booleans = {false, true})
  void timeoutAndCancellationCloseTemporaryTransport(boolean cancel) throws Exception {
    try (var listener = new ServerSocket(0, 1, InetAddress.getLoopbackAddress())) {
      listener.setSoTimeout(2000);
      EndpointResolver resolver =
          EndpointResolver.create(
              "opc.tcp://localhost:" + listener.getLocalPort() + "/discovery",
              endpoints -> Optional.empty(),
              b -> b.setConnectTimeout(uint(2000)).setAcknowledgeTimeout(uint(5000)),
              Duration.ofSeconds(1));
      CompletableFuture<EndpointConfiguration> operation = resolver.resolve();
      try (Socket accepted = listener.accept()) {
        accepted.setSoTimeout(3000);
        if (cancel) assertTrue(operation.cancel(true));
        else assertThrows(ExecutionException.class, () -> operation.get(2, TimeUnit.SECONDS));
        // Drain the client's Hello. EOF proves timeout/cancellation closed the underlying socket.
        while (accepted.getInputStream().read() != -1) {}
      }
      listener.setSoTimeout(300);
      assertThrows(
          SocketTimeoutException.class,
          listener::accept,
          "the temporary transport must not reconnect after resolution ends");
    }
  }
}
