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
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;
import java.util.function.BooleanSupplier;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectConnection;
import org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectManager;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.messages.ReverseHelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class DiscoveryClientReverseConnectTest {

  private static final String SERVER_URI = "urn:eclipse:milo:test:reverse-discovery:server";
  private static final String ENDPOINT_URL = "opc.tcp://localhost:12685/milo";

  private ReverseConnectManager manager;
  private Socket socket;

  @AfterEach
  void tearDown() throws Exception {
    if (socket != null) {
      socket.close();
    }
    if (manager != null) {
      manager.shutdown();
    }
  }

  @Test
  void reverseGetEndpointsUsesNoSecurityTcpDiscoveryEndpoint() {
    EndpointDescription endpoint =
        DiscoveryClient.newDiscoveryEndpoint(ENDPOINT_URL, Stack.TCP_UASC_UABINARY_TRANSPORT_URI);

    assertEquals(ENDPOINT_URL, endpoint.getEndpointUrl());
    assertEquals(MessageSecurityMode.None, endpoint.getSecurityMode());
    assertEquals(SecurityPolicy.None.getUri(), endpoint.getSecurityPolicyUri());
    assertEquals(Stack.TCP_UASC_UABINARY_TRANSPORT_URI, endpoint.getTransportProfileUri());
    assertEquals(ubyte(0), endpoint.getSecurityLevel());
  }

  @Test
  void reverseGetEndpointsRejectsBlankReverseHelloEndpointUrlAndClosesConnection()
      throws Exception {

    ReverseConnectConnection connection = claimConnection(" ");

    CompletableFuture<?> endpoints = DiscoveryClient.getEndpoints(connection);

    ExecutionException exception =
        assertThrows(ExecutionException.class, () -> endpoints.get(5, TimeUnit.SECONDS));
    UaException cause = assertInstanceOf(UaException.class, exception.getCause());

    assertEquals(StatusCodes.Bad_TcpEndpointUrlInvalid, cause.getStatusCode().value());

    waitUntil(() -> !connection.channel().isOpen());
  }

  @Test
  void reverseGetEndpointsClosesConnectionWhenSynchronousSetupFails() throws Exception {
    ReverseConnectConnection connection = claimConnection(ENDPOINT_URL);
    RuntimeException failure = new RuntimeException("setup failed");

    CompletableFuture<?> endpoints =
        DiscoveryClient.getEndpoints(
            connection,
            transport -> {
              throw failure;
            });

    ExecutionException exception =
        assertThrows(ExecutionException.class, () -> endpoints.get(5, TimeUnit.SECONDS));

    assertSame(failure, exception.getCause());

    waitUntil(() -> !connection.channel().isOpen());
  }

  @Test
  void reverseGetEndpointsPropagatesDiscoveryFailureAndClosesConnection() throws Exception {
    ReverseConnectConnection connection = claimConnection(ENDPOINT_URL);

    CompletableFuture<?> endpoints =
        DiscoveryClient.getEndpoints(
            connection, transport -> transport.setAcknowledgeTimeout(uint(100)));

    ExecutionException exception =
        assertThrows(ExecutionException.class, () -> endpoints.get(5, TimeUnit.SECONDS));
    UaException cause = assertInstanceOf(UaException.class, exception.getCause());

    assertEquals(StatusCodes.Bad_Timeout, cause.getStatusCode().value());

    waitUntil(() -> !connection.channel().isOpen());
  }

  private ReverseConnectConnection claimConnection(String endpointUrl) throws Exception {
    manager =
        ReverseConnectManager.builder()
            .addBindAddress(new InetSocketAddress("127.0.0.1", 0))
            .setExecutor(Runnable::run)
            .setFirstMessageTimeout(Duration.ofSeconds(1))
            .setPendingConnectionHoldTime(Duration.ofSeconds(5))
            .build();
    manager.startup();

    socket = new Socket();
    socket.connect(boundAddress());
    writeMessage(
        socket, TcpMessageEncoder.encode(new ReverseHelloMessage(SERVER_URI, endpointUrl)));

    waitUntil(() -> !manager.snapshot().pendingCandidates().isEmpty());

    UUID candidateId = manager.snapshot().pendingCandidates().get(0).id();
    return manager.claim(candidateId).orElseThrow();
  }

  private InetSocketAddress boundAddress() {
    SocketAddress boundAddress = manager.snapshot().listeners().get(0).boundAddress();

    return assertInstanceOf(InetSocketAddress.class, boundAddress);
  }

  private static void writeMessage(Socket socket, ByteBuf buffer) throws Exception {
    try {
      byte[] bytes = new byte[buffer.readableBytes()];
      buffer.readBytes(bytes);

      OutputStream outputStream = socket.getOutputStream();
      outputStream.write(bytes);
      outputStream.flush();
    } finally {
      buffer.release();
    }
  }

  private static void waitUntil(BooleanSupplier condition) {
    long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(5);

    while (!condition.getAsBoolean() && System.nanoTime() < deadline) {
      LockSupport.parkNanos(TimeUnit.MILLISECONDS.toNanos(10));
    }

    assertTrue(condition.getAsBoolean());
  }
}
