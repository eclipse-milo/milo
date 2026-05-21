/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.reverse;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.junit.jupiter.api.Test;

class ReverseConnectEndpointSelectionFailureTest {

  private static final String SERVER_URI = "urn:eclipse:milo:test:reverse-selection:server";
  private static final String ENDPOINT_URL = "opc.tcp://localhost:12685/milo";
  private static final String EMPTY_ENDPOINTS_MESSAGE =
      "Reverse Connect discovery returned no endpoints";
  private static final String NO_ENDPOINT_SELECTED_MESSAGE =
      "no endpoint selected from Reverse Connect discovery result";

  @Test
  void discoveryFirstReportsEmptyDiscoveryEndpointList() throws Exception {
    ReverseConnectManager manager = newManager();

    try {
      DiscoveryFirstReverseConnectClient helper =
          DiscoveryFirstReverseConnectClient.builder(manager)
              .setEndpointSelector(
                  (candidate, endpoints) -> {
                    throw new AssertionError("selector should not run for empty endpoints");
                  })
              .build();

      CompletableFuture<OpcUaClient> future =
          invokeDiscoveryFirstConnectProductionClient(helper, discovery(List.of()));

      assertConfigurationError(future, EMPTY_ENDPOINTS_MESSAGE);
    } finally {
      manager.shutdown();
    }
  }

  @Test
  void discoveryFirstReportsEndpointSelectionMismatchSeparately() throws Exception {
    ReverseConnectManager manager = newManager();

    try {
      DiscoveryFirstReverseConnectClient helper =
          DiscoveryFirstReverseConnectClient.builder(manager)
              .setEndpointSelector((candidate, endpoints) -> Optional.empty())
              .build();

      CompletableFuture<OpcUaClient> future =
          invokeDiscoveryFirstConnectProductionClient(helper, discovery(List.of(endpoint())));

      assertConfigurationError(future, NO_ENDPOINT_SELECTED_MESSAGE);
    } finally {
      manager.shutdown();
    }
  }

  @Test
  void acceptorReportsEmptyDiscoveryEndpointList() throws Exception {
    ReverseConnectManager manager = newManager();
    ReverseConnectAcceptor acceptor =
        ReverseConnectAcceptor.builder(manager)
            .setEndpointSelector(
                (candidate, endpoints) -> {
                  throw new AssertionError("selector should not run for empty endpoints");
                })
            .build();

    try {
      acceptor.start();

      CompletableFuture<OpcUaClient> future =
          invokeAcceptorConnectProductionClient(acceptor, discovery(List.of()));

      assertConfigurationError(future, EMPTY_ENDPOINTS_MESSAGE);
    } finally {
      acceptor.stop();
      manager.shutdown();
    }
  }

  @Test
  void acceptorReportsEndpointSelectionMismatchSeparately() throws Exception {
    ReverseConnectManager manager = newManager();
    ReverseConnectAcceptor acceptor =
        ReverseConnectAcceptor.builder(manager)
            .setEndpointSelector((candidate, endpoints) -> Optional.empty())
            .build();

    try {
      acceptor.start();

      CompletableFuture<OpcUaClient> future =
          invokeAcceptorConnectProductionClient(acceptor, discovery(List.of(endpoint())));

      assertConfigurationError(future, NO_ENDPOINT_SELECTED_MESSAGE);
    } finally {
      acceptor.stop();
      manager.shutdown();
    }
  }

  @SuppressWarnings("unchecked")
  private static CompletableFuture<OpcUaClient> invokeDiscoveryFirstConnectProductionClient(
      DiscoveryFirstReverseConnectClient helper, ReverseConnectDiscoveryResult discovery)
      throws Exception {

    Method method =
        DiscoveryFirstReverseConnectClient.class.getDeclaredMethod(
            "connectProductionClient", ReverseConnectDiscoveryResult.class);
    method.setAccessible(true);

    return (CompletableFuture<OpcUaClient>) method.invoke(helper, discovery);
  }

  @SuppressWarnings("unchecked")
  private static CompletableFuture<OpcUaClient> invokeAcceptorConnectProductionClient(
      ReverseConnectAcceptor acceptor, ReverseConnectDiscoveryResult discovery) throws Exception {

    Method method =
        ReverseConnectAcceptor.class.getDeclaredMethod(
            "connectProductionClient", String.class, ReverseConnectDiscoveryResult.class);
    method.setAccessible(true);

    return (CompletableFuture<OpcUaClient>) method.invoke(acceptor, "key", discovery);
  }

  private static void assertConfigurationError(
      CompletableFuture<?> future, String expectedMessage) {

    CompletionException exception = assertThrows(CompletionException.class, future::join);
    UaException cause = assertInstanceOf(UaException.class, exception.getCause());

    assertEquals(StatusCodes.Bad_ConfigurationError, cause.getStatusCode().value());
    assertEquals(expectedMessage, cause.getMessage());
  }

  private static ReverseConnectDiscoveryResult discovery(List<EndpointDescription> endpoints) {
    return new ReverseConnectDiscoveryResult(candidate(), endpoints);
  }

  private static ReverseConnectManager newManager() {
    return ReverseConnectManager.builder()
        .addBindAddress(new InetSocketAddress("127.0.0.1", 0))
        .setExecutor(Runnable::run)
        .build();
  }

  private static ReverseConnectCandidateSnapshot candidate() {
    Instant now = Instant.now();

    return new ReverseConnectCandidateSnapshot(
        UUID.randomUUID(),
        ReverseConnectCandidateState.PENDING,
        SERVER_URI,
        ENDPOINT_URL,
        null,
        null,
        now,
        now,
        null,
        null,
        null,
        null);
  }

  private static EndpointDescription endpoint() {
    ApplicationDescription server =
        new ApplicationDescription(
            SERVER_URI,
            "urn:eclipse:milo:test:reverse-selection:product",
            LocalizedText.english("test server"),
            ApplicationType.Server,
            null,
            null,
            null);

    UserTokenPolicy anonymousPolicy =
        new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null);

    return new EndpointDescription(
        ENDPOINT_URL,
        server,
        ByteString.NULL_VALUE,
        MessageSecurityMode.None,
        SecurityPolicy.None.getUri(),
        new UserTokenPolicy[] {anonymousPolicy},
        Stack.TCP_UASC_UABINARY_TRANSPORT_URI,
        ubyte(0));
  }
}
