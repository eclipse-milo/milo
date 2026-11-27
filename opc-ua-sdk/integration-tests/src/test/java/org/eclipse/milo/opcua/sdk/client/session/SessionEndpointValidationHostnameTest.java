/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.session;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.client.DiscoveryClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfigLimits;
import org.eclipse.milo.opcua.sdk.test.DelegatingSessionServiceSet;
import org.eclipse.milo.opcua.sdk.test.TestPortAllocator;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * End-to-end regression tests for session endpoint validation against a multi-host Milo Server
 * discovered at an address it does not advertise (OPC UA Part 4 §5.5.1, §5.5.4.2, §5.7.2.2).
 *
 * <p>The Server binds {@code 127.0.0.1} and advertises two HostNames ({@code localhost} first,
 * {@code milo-test.invalid} second). Because {@code 127.0.0.1} is not advertised, both {@code
 * GetEndpoints} and {@code CreateSession} must fall back to the default HostName ({@code
 * localhost}) so session endpoint validation succeeds whether the client connects to the returned
 * HostName or rewrites the endpoint back to the discovery address.
 */
class SessionEndpointValidationHostnameTest {

  private static final String PATH = "/test";

  private int port;
  private OpcUaServer server;
  private final List<List<String>> recordedCreateSessionUrls = new CopyOnWriteArrayList<>();

  @BeforeEach
  void startServer() throws Exception {
    port = TestPortAllocator.allocatePort();

    server =
        TestServer.create(
                port,
                new OpcUaServerConfigLimits() {},
                config -> config.setEndpoints(endpoints("localhost", "milo-test.invalid")))
            .getServer();

    server.addServiceSet(PATH, new RecordingSessionServiceSet(server, recordedCreateSessionUrls));
    server.startup().get(10, SECONDS);
  }

  @AfterEach
  void stopServer() throws Exception {
    server.shutdown().get(10, SECONDS);
  }

  // Part 4 §5.5.4.2: GetEndpoints at an unadvertised address falls back to the first configured
  // HostName only (including all of its security endpoints, excluding /discovery), and filters
  // ApplicationDescription.discoveryUrls to that HostName.
  @Test
  void discoveryAtUnadvertisedHostReturnsOnlyDefaultHostAndDiscoveryUrls() throws Exception {
    assertEquals(
        List.of(
            url("localhost"),
            url("localhost"),
            discoveryUrl("localhost"),
            url("milo-test.invalid"),
            url("milo-test.invalid"),
            discoveryUrl("milo-test.invalid")),
        urls(server.getApplicationContext().getEndpointDescriptions()),
        "control: server must advertise both HostNames with multiple endpoints and /discovery");

    List<EndpointDescription> atUnadvertisedHost =
        DiscoveryClient.getEndpoints(url("127.0.0.1")).get(10, SECONDS);
    List<EndpointDescription> atAdvertisedDefault =
        DiscoveryClient.getEndpoints(url("localhost")).get(10, SECONDS);

    assertEquals(List.of(url("localhost"), url("localhost")), urls(atUnadvertisedHost));
    assertEquals(List.of(url("localhost"), url("localhost")), urls(atAdvertisedDefault));
    for (EndpointDescription ed : atUnadvertisedHost) {
      assertArrayEquals(
          new String[] {discoveryUrl("localhost")},
          ed.getServer().getDiscoveryUrls(),
          "fallback endpoint's discoveryUrls must not leak secondary HostName");
    }
  }

  // Part 4 §5.5.1 / §5.7.2.2: discovering a multi-host server via an unadvertised address and
  // connecting to the returned default HostName must pass session endpoint validation.
  @Test
  void sessionValidationSucceedsWhenConnectingToReturnedDefaultHost() throws Exception {
    OpcUaClient client = createClient(url("127.0.0.1"), noneEndpointAtHost("localhost"));

    try {
      client.connectAsync().get(10, SECONDS);

      assertEquals(url("localhost"), client.getConfig().getEndpoint().getEndpointUrl());
      assertEquals(
          List.of(url("localhost"), url("localhost")),
          urls(client.getConfig().getDiscoveryEndpoints()));
      assertEquals(List.of(List.of(url("localhost"), url("localhost"))), recordedCreateSessionUrls);
    } finally {
      client.disconnectAsync().get(10, SECONDS);
    }
  }

  // Clients behind NAT or DNS aliases often rewrite the selected endpoint's host back to the
  // discovery address via EndpointUtil.updateUrl; CreateSession at the unadvertised address must
  // fall back to the same default HostName so session endpoint validation still passes.
  @Test
  void sessionValidationSucceedsWhenConnectingViaRewrittenUnadvertisedHost() throws Exception {
    Function<List<EndpointDescription>, Optional<EndpointDescription>> rewriteToDiscoveryHost =
        endpoints ->
            noneEndpointAtHost("localhost")
                .apply(endpoints)
                .map(e -> EndpointUtil.updateUrl(e, "127.0.0.1"));

    OpcUaClient client = createClient(url("127.0.0.1"), rewriteToDiscoveryHost);

    try {
      client.connectAsync().get(10, SECONDS);

      assertEquals(url("127.0.0.1"), client.getConfig().getEndpoint().getEndpointUrl());
      assertEquals(
          List.of(url("localhost"), url("localhost")),
          urls(client.getConfig().getDiscoveryEndpoints()));
      assertEquals(List.of(List.of(url("localhost"), url("localhost"))), recordedCreateSessionUrls);
    } finally {
      client.disconnectAsync().get(10, SECONDS);
    }
  }

  // Baseline control: discovering and connecting at an advertised HostName continues to pass
  // session endpoint validation.
  @Test
  void sessionValidationSucceedsWhenDiscoveredAtAdvertisedHost() throws Exception {
    OpcUaClient client = createClient(url("localhost"), noneEndpointAtHost("localhost"));

    try {
      client.connectAsync().get(10, SECONDS);

      assertEquals(url("localhost"), client.getConfig().getEndpoint().getEndpointUrl());
      assertEquals(
          List.of(url("localhost"), url("localhost")),
          urls(client.getConfig().getDiscoveryEndpoints()));
      assertEquals(List.of(List.of(url("localhost"), url("localhost"))), recordedCreateSessionUrls);
    } finally {
      client.disconnectAsync().get(10, SECONDS);
    }
  }

  private OpcUaClient createClient(
      String discoveryUrl,
      Function<List<EndpointDescription>, Optional<EndpointDescription>> selectEndpoint)
      throws UaException {

    recordedCreateSessionUrls.clear();

    return OpcUaClient.create(
        discoveryUrl,
        selectEndpoint,
        transport -> {},
        config ->
            config
                .setApplicationName(LocalizedText.english("eclipse milo test client"))
                .setApplicationUri("urn:eclipse:milo:test:client")
                .setSessionEndpointValidationEnabled(true));
  }

  private String url(String host) {
    return "opc.tcp://" + host + ":" + port + PATH;
  }

  private String discoveryUrl(String host) {
    return url(host) + "/discovery";
  }

  private static Function<List<EndpointDescription>, Optional<EndpointDescription>>
      noneEndpointAtHost(String host) {
    return endpoints ->
        endpoints.stream()
            .filter(e -> SecurityPolicy.None.getUri().equals(e.getSecurityPolicyUri()))
            .filter(e -> host.equalsIgnoreCase(EndpointUtil.getHost(e.getEndpointUrl())))
            .findFirst();
  }

  private static List<String> urls(List<EndpointDescription> endpoints) {
    return endpoints.stream().map(EndpointDescription::getEndpointUrl).toList();
  }

  private Set<EndpointConfig> endpoints(String... hostnames) {
    var endpoints = new LinkedHashSet<EndpointConfig>();

    for (String hostname : hostnames) {
      EndpointConfig.Builder base =
          EndpointConfig.newBuilder()
              .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
              .setBindAddress("127.0.0.1")
              .setBindPort(port)
              .setHostname(hostname)
              .setPath(PATH)
              .addTokenPolicies(USER_TOKEN_POLICY_ANONYMOUS);

      endpoints.add(
          base.copy()
              .setSecurityPolicy(SecurityPolicy.None)
              .setSecurityMode(MessageSecurityMode.None)
              .build());

      endpoints.add(
          base.copy()
              .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
              .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
              .build());

      endpoints.add(
          base.copy()
              .setPath(PATH + "/discovery")
              .setSecurityPolicy(SecurityPolicy.None)
              .setSecurityMode(MessageSecurityMode.None)
              .build());
    }

    return endpoints;
  }

  /** Records the {@code serverEndpoints} URLs returned by each CreateSession response. */
  private static final class RecordingSessionServiceSet extends DelegatingSessionServiceSet {

    private final List<List<String>> recordedUrls;

    RecordingSessionServiceSet(OpcUaServer server, List<List<String>> recordedUrls) {
      super(server);
      this.recordedUrls = recordedUrls;
    }

    @Override
    public CreateSessionResponse onCreateSession(
        ServiceRequestContext context, CreateSessionRequest request) throws UaException {

      CreateSessionResponse response = super.onCreateSession(context, request);

      recordedUrls.add(urls(List.of(response.getServerEndpoints())));

      return response;
    }
  }
}
