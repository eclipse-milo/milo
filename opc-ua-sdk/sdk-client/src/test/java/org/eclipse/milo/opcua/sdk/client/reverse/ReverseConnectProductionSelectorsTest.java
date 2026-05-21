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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.time.Instant;
import java.util.List;
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

class ReverseConnectProductionSelectorsTest {

  private static final String SERVER_URI = "urn:eclipse:milo:test:reverse-production:server";
  private static final String ENDPOINT_URL = "opc.tcp://localhost:12685/milo";

  @Test
  void defaultProductionSelectorDoesNotMatchHintlessCandidates() {
    ReverseConnectSelector selector =
        ReverseConnectProductionSelectors.matchDiscoveryRoutingHints(candidate(null, null));

    assertFalse(selector.matches(candidate(null, null)));
    assertFalse(selector.matches(candidate("urn:other", "opc.tcp://localhost:12686/other")));
  }

  @Test
  void defaultProductionSelectorStillMatchesExactRoutingHints() {
    ReverseConnectSelector selector =
        ReverseConnectProductionSelectors.matchDiscoveryRoutingHints(
            candidate(SERVER_URI, ENDPOINT_URL));

    assertTrue(selector.matches(candidate(SERVER_URI, ENDPOINT_URL)));
    assertFalse(selector.matches(candidate(SERVER_URI, "opc.tcp://localhost:12686/other")));
  }

  @Test
  void discoveryFirstDefaultProductionSelectorFailsWhenRoutingHintsAreMissing() throws Exception {
    ReverseConnectManager manager = newManager();

    try {
      DiscoveryFirstReverseConnectClient helper =
          DiscoveryFirstReverseConnectClient.builder(manager).build();

      CompletableFuture<OpcUaClient> future =
          invokeDiscoveryFirstConnectProductionClient(
              helper,
              new ReverseConnectDiscoveryResult(candidate(null, null), List.of(endpoint())));

      assertConfigurationError(future);
    } finally {
      manager.shutdown();
    }
  }

  @Test
  void acceptorDefaultProductionSelectorFailsWhenRoutingHintsAreMissing() throws Exception {
    ReverseConnectManager manager = newManager();

    try {
      ReverseConnectAcceptor acceptor = ReverseConnectAcceptor.builder(manager).build();

      CompletableFuture<OpcUaClient> future =
          invokeAcceptorConnectProductionClient(
              acceptor,
              new ReverseConnectDiscoveryResult(candidate(null, null), List.of(endpoint())));

      assertConfigurationError(future);
    } finally {
      manager.shutdown();
    }
  }

  @SuppressWarnings("unchecked")
  private static CompletableFuture<OpcUaClient> invokeDiscoveryFirstConnectProductionClient(
      DiscoveryFirstReverseConnectClient helper, ReverseConnectDiscoveryResult discovery)
      throws Exception {

    Method method =
        DiscoveryFirstReverseConnectClient.class.getDeclaredMethod(
            "connectProductionClient",
            ReverseConnectDiscoveryResult.class,
            EndpointDescription.class);
    method.setAccessible(true);

    return (CompletableFuture<OpcUaClient>) method.invoke(helper, discovery, endpoint());
  }

  @SuppressWarnings("unchecked")
  private static CompletableFuture<OpcUaClient> invokeAcceptorConnectProductionClient(
      ReverseConnectAcceptor acceptor, ReverseConnectDiscoveryResult discovery) throws Exception {

    Method method =
        ReverseConnectAcceptor.class.getDeclaredMethod(
            "connectProductionClient",
            String.class,
            ReverseConnectDiscoveryResult.class,
            EndpointDescription.class);
    method.setAccessible(true);

    return (CompletableFuture<OpcUaClient>) method.invoke(acceptor, "key", discovery, endpoint());
  }

  private static void assertConfigurationError(CompletableFuture<?> future) {
    CompletionException exception = assertThrows(CompletionException.class, future::join);
    UaException cause = assertInstanceOf(UaException.class, exception.getCause());

    assertEquals(StatusCodes.Bad_ConfigurationError, cause.getStatusCode().value());
  }

  private static ReverseConnectManager newManager() {
    return ReverseConnectManager.builder()
        .addBindAddress(new InetSocketAddress("127.0.0.1", 0))
        .setExecutor(Runnable::run)
        .build();
  }

  private static ReverseConnectCandidateSnapshot candidate(String serverUri, String endpointUrl) {
    Instant now = Instant.now();

    return new ReverseConnectCandidateSnapshot(
        UUID.randomUUID(),
        ReverseConnectCandidateState.PENDING,
        serverUri,
        endpointUrl,
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
            "urn:eclipse:milo:test:reverse-production:product",
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
