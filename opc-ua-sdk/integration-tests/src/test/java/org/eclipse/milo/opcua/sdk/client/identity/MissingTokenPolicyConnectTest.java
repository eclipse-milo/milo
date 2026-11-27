/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.identity;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestPortAllocator;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * {@link OpcUaClient#connect()} reports {@code Bad_IdentityTokenRejected} when the endpoint offers
 * no user-token policy of the configured provider's kind.
 *
 * <p>The provider finds the mismatch before the client sends CreateSession. {@code connect()} takes
 * the StatusCode of the failure's cause, so the caller sees the same code whichever built-in
 * provider it configured, rather than {@code Bad_UnexpectedError}.
 */
class MissingTokenPolicyConnectTest {

  @ParameterizedTest(name = "{1} on an endpoint offering {0}")
  @MethodSource("endpointsWithoutTheProvidersTokenType")
  void connectReportsIdentityTokenRejected(
      UserTokenPolicy offeredPolicy, Supplier<IdentityProvider> provider) throws Exception {

    OpcUaServer server = startServer(offeredPolicy);

    try {
      OpcUaClient client =
          TestClient.create(server, config -> config.setIdentityProvider(provider.get()));

      try {
        UaException failure = assertThrows(UaException.class, client::connect);

        assertEquals(
            StatusCodes.Bad_IdentityTokenRejected,
            failure.getStatusCode().value(),
            "unexpected status: " + failure.getStatusCode());
      } finally {
        client.disconnectAsync().get(10, SECONDS);
      }
    } finally {
      server.shutdown().get(10, SECONDS);
    }
  }

  private static Stream<Arguments> endpointsWithoutTheProvidersTokenType() {
    return Stream.of(
        Arguments.of(
            Named.of("UserName", OpcUaServerConfig.USER_TOKEN_POLICY_USERNAME),
            Named.of("AnonymousProvider", (Supplier<IdentityProvider>) AnonymousProvider::new)),
        Arguments.of(
            Named.of("Anonymous", OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS),
            Named.of(
                "UsernameProvider",
                (Supplier<IdentityProvider>) () -> new UsernameProvider("user1", "password"))));
  }

  /** Start a Server whose only endpoint offers {@code policy} as its only user-token policy. */
  private static OpcUaServer startServer(UserTokenPolicy policy) throws Exception {
    int port = TestPortAllocator.allocatePort();

    EndpointConfig endpoint =
        EndpointConfig.newBuilder()
            .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
            .setBindAddress("localhost")
            .setHostname("localhost")
            .setBindPort(port)
            .setPath("/test")
            .setSecurityPolicy(SecurityPolicy.None)
            .setSecurityMode(MessageSecurityMode.None)
            .addTokenPolicy(policy)
            .build();

    OpcUaServer server =
        TestServer.create(
                port,
                new OpcUaServerConfigLimits() {},
                config -> config.setEndpoints(Set.of(endpoint)))
            .getServer();

    server.startup().get(10, SECONDS);

    return server;
  }
}
