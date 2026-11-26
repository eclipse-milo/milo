/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.netty.channel.Channel;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.server.servicesets.impl.DefaultDiscoveryServiceSet;
import org.eclipse.milo.opcua.stack.core.channel.SecureChannel;
import org.eclipse.milo.opcua.stack.core.channel.ServerSecureChannel;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.GetEndpointsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.GetEndpointsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Verifies that {@link SessionManager#createSession} applies the same per-transport-profile default
 * host fallback as {@link DefaultDiscoveryServiceSet#onGetEndpoints} (OPC UA Part 4 §5.5.1,
 * §5.5.4.2, §5.7.2.2).
 */
class SessionManagerEndpointFallbackTest {

  // Part 4 §5.5.1 / §5.7.2.2: GetEndpoints at an unadvertised address, CreateSession at the
  // returned default host, and CreateSession at the unadvertised address must all return the exact
  // same set of endpoints for the negotiated transport profile.
  @Test
  void createSessionAndGetEndpointsReturnIdenticalDefaultHostEndpointsOnFallback()
      throws Exception {

    OpcUaServer server =
        createServer(
            noneEndpoint(TransportProfile.TCP_UASC_UABINARY, "primary.test", 4840, "/test"),
            noneEndpoint(TransportProfile.TCP_UASC_UABINARY, "primary.test", 4841, "/alt"),
            noneEndpoint(
                TransportProfile.TCP_UASC_UABINARY, "primary.test", 4840, "/test/discovery"),
            noneEndpoint(TransportProfile.TCP_UASC_UABINARY, "secondary.test", 4840, "/test"),
            noneEndpoint(TransportProfile.TCP_UASC_UABINARY, "secondary.test", 4841, "/alt"),
            noneEndpoint(
                TransportProfile.TCP_UASC_UABINARY, "secondary.test", 4840, "/test/discovery"));

    try {
      var discovery = new DefaultDiscoveryServiceSet(server);
      EndpointDescription primaryEndpoint =
          endpointByUrl(server, "opc.tcp://primary.test:4840/test");
      EndpointDescription secondaryEndpoint =
          endpointByUrl(server, "opc.tcp://secondary.test:4840/test");

      GetEndpointsResponse discoveryFallback =
          discovery.onGetEndpoints(
              context("opc.tcp://127.0.0.1:4840/test", TransportProfile.TCP_UASC_UABINARY, null),
              getEndpointsRequest("opc.tcp://127.0.0.1:4840/test"));

      CreateSessionResponse sessionAtDefaultHost =
          server
              .getSessionManager()
              .createSession(
                  context(
                      "opc.tcp://primary.test:4840/test",
                      TransportProfile.TCP_UASC_UABINARY,
                      primaryEndpoint),
                  createSessionRequest("opc.tcp://primary.test:4840/test"));

      CreateSessionResponse sessionAtUnadvertisedHost =
          server
              .getSessionManager()
              .createSession(
                  context(
                      "opc.tcp://127.0.0.1:4840/test",
                      TransportProfile.TCP_UASC_UABINARY,
                      primaryEndpoint),
                  createSessionRequest("opc.tcp://127.0.0.1:4840/test"));

      List<String> expectedPrimaryUrls =
          List.of("opc.tcp://primary.test:4840/test", "opc.tcp://primary.test:4841/alt");

      assertEquals(expectedPrimaryUrls, urls(discoveryFallback.getEndpoints()));
      assertEquals(expectedPrimaryUrls, urls(sessionAtDefaultHost.getServerEndpoints()));
      assertEquals(expectedPrimaryUrls, urls(sessionAtUnadvertisedHost.getServerEndpoints()));

      // Control: CreateSession at the advertised secondary host returns only that host's endpoints.
      CreateSessionResponse sessionAtSecondaryHost =
          server
              .getSessionManager()
              .createSession(
                  context(
                      "opc.tcp://secondary.test:4840/test",
                      TransportProfile.TCP_UASC_UABINARY,
                      secondaryEndpoint),
                  createSessionRequest("opc.tcp://secondary.test:4840/test"));

      assertEquals(
          List.of("opc.tcp://secondary.test:4840/test", "opc.tcp://secondary.test:4841/alt"),
          urls(sessionAtSecondaryHost.getServerEndpoints()));
    } finally {
      server.getSessionManager().shutdown();
    }
  }

  // Fallback must respect configured endpoint insertion order so GetEndpoints and CreateSession
  // select the same default host regardless of hostname spelling.
  @Test
  void createSessionFallbackFollowsConfiguredEndpointOrder() throws Exception {
    OpcUaServer server =
        createServer(
            noneEndpoint(TransportProfile.TCP_UASC_UABINARY, "secondary.test", 4840, "/test"),
            noneEndpoint(TransportProfile.TCP_UASC_UABINARY, "primary.test", 4840, "/test"));

    try {
      EndpointDescription firstEndpoint =
          endpointByUrl(server, "opc.tcp://secondary.test:4840/test");

      CreateSessionResponse response =
          server
              .getSessionManager()
              .createSession(
                  context(
                      "opc.tcp://127.0.0.1:4840/test",
                      TransportProfile.TCP_UASC_UABINARY,
                      firstEndpoint),
                  createSessionRequest("opc.tcp://127.0.0.1:4840/test"));

      assertEquals(
          List.of("opc.tcp://secondary.test:4840/test"), urls(response.getServerEndpoints()));
    } finally {
      server.getSessionManager().shutdown();
    }
  }

  // On a server exposing multiple transport profiles with distinct host lists, CreateSession on
  // each transport must fall back to that transport's own default host, matching GetEndpoints
  // filtered to that transport profile (Part 4 §5.7.2.1).
  @Test
  void createSessionSelectsDefaultHostPerTransportProfile() throws Exception {
    OpcUaServer server =
        createServer(
            noneEndpoint(TransportProfile.TCP_UASC_UABINARY, "tcp-first.test", 4840, "/test"),
            noneEndpoint(TransportProfile.TCP_UASC_UABINARY, "tcp-second.test", 4840, "/test"),
            noneEndpoint(TransportProfile.HTTPS_UABINARY, "https-first.test", 8443, "/test"),
            noneEndpoint(TransportProfile.HTTPS_UABINARY, "https-second.test", 8443, "/test"));

    try {
      var discovery = new DefaultDiscoveryServiceSet(server);
      EndpointDescription tcpFirst = endpointByUrl(server, "opc.tcp://tcp-first.test:4840/test");
      EndpointDescription httpsFirst = endpointByUrl(server, "https://https-first.test:8443/test");

      GetEndpointsResponse discoveryResponse =
          discovery.onGetEndpoints(
              context("opc.tcp://127.0.0.1:4840/test", TransportProfile.TCP_UASC_UABINARY, null),
              getEndpointsRequest("opc.tcp://127.0.0.1:4840/test"));

      CreateSessionResponse tcpSession =
          server
              .getSessionManager()
              .createSession(
                  context(
                      "opc.tcp://127.0.0.1:4840/test",
                      TransportProfile.TCP_UASC_UABINARY,
                      tcpFirst),
                  createSessionRequest("opc.tcp://127.0.0.1:4840/test"));

      CreateSessionResponse httpsSession =
          server
              .getSessionManager()
              .createSession(
                  context(
                      "https://127.0.0.1:8443/test", TransportProfile.HTTPS_UABINARY, httpsFirst),
                  createSessionRequest("https://127.0.0.1:8443/test"));

      assertEquals(
          List.of("opc.tcp://tcp-first.test:4840/test", "https://https-first.test:8443/test"),
          urls(discoveryResponse.getEndpoints()));
      assertEquals(
          List.of("opc.tcp://tcp-first.test:4840/test"), urls(tcpSession.getServerEndpoints()));
      assertEquals(
          List.of("https://https-first.test:8443/test"), urls(httpsSession.getServerEndpoints()));
    } finally {
      server.getSessionManager().shutdown();
    }
  }

  // A null, empty, or unparseable CreateSession endpointUrl must fall back to the default host
  // rather than returning all hosts.
  @ParameterizedTest
  @NullAndEmptySource
  @ValueSource(strings = {"not-a-valid-url"})
  void createSessionWithNullOrUnparseableUrlFallsBackToDefaultHost(
      @Nullable String requestedEndpointUrl) throws Exception {

    OpcUaServer server =
        createServer(
            noneEndpoint(TransportProfile.TCP_UASC_UABINARY, "primary.test", 4840, "/test"),
            noneEndpoint(TransportProfile.TCP_UASC_UABINARY, "secondary.test", 4840, "/test"));

    try {
      EndpointDescription primaryEndpoint =
          endpointByUrl(server, "opc.tcp://primary.test:4840/test");

      CreateSessionResponse response =
          server
              .getSessionManager()
              .createSession(
                  context(
                      "opc.tcp://primary.test:4840/test",
                      TransportProfile.TCP_UASC_UABINARY,
                      primaryEndpoint),
                  createSessionRequest(requestedEndpointUrl));

      assertEquals(
          List.of("opc.tcp://primary.test:4840/test"), urls(response.getServerEndpoints()));
    } finally {
      server.getSessionManager().shutdown();
    }
  }

  // A discovery-only endpoint registered with a trailing slash before a session endpoint on
  // another host must be excluded consistently by both GetEndpoints and CreateSession.
  @Test
  void trailingSlashDiscoveryEndpointIsExcludedFromGetEndpointsAndCreateSessionFallback()
      throws Exception {

    OpcUaServer server =
        createServer(
            noneEndpoint(TransportProfile.TCP_UASC_UABINARY, "a.test", 4840, "/app/discovery/"),
            noneEndpoint(TransportProfile.TCP_UASC_UABINARY, "b.test", 4840, "/app"));

    try {
      var discovery = new DefaultDiscoveryServiceSet(server);
      EndpointDescription sessionEndpoint = endpointByUrl(server, "opc.tcp://b.test:4840/app");

      GetEndpointsResponse getEndpoints =
          discovery.onGetEndpoints(
              context("opc.tcp://127.0.0.1:4840/app", TransportProfile.TCP_UASC_UABINARY, null),
              getEndpointsRequest("opc.tcp://127.0.0.1:4840/app"));

      CreateSessionResponse createSession =
          server
              .getSessionManager()
              .createSession(
                  context(
                      "opc.tcp://127.0.0.1:4840/app",
                      TransportProfile.TCP_UASC_UABINARY,
                      sessionEndpoint),
                  createSessionRequest("opc.tcp://127.0.0.1:4840/app"));

      assertEquals(List.of("opc.tcp://b.test:4840/app"), urls(getEndpoints.getEndpoints()));
      assertEquals(List.of("opc.tcp://b.test:4840/app"), urls(createSession.getServerEndpoints()));
    } finally {
      server.getSessionManager().shutdown();
    }
  }

  private static OpcUaServer createServer(EndpointConfig... endpoints) {
    OpcUaServerConfig config =
        OpcUaServerConfig.builder()
            .setEndpoints(new LinkedHashSet<>(Arrays.asList(endpoints)))
            .setCertificateManager(new DefaultCertificateManager())
            .setApplicationUri("urn:eclipse:milo:test:server")
            .setProductUri("urn:eclipse:milo:test")
            .build();

    return new OpcUaServer(config, transportProfile -> null);
  }

  private static EndpointConfig noneEndpoint(
      TransportProfile transportProfile, String hostname, int port, String path) {

    return EndpointConfig.newBuilder()
        .setTransportProfile(transportProfile)
        .setBindAddress("127.0.0.1")
        .setBindPort(port)
        .setHostname(hostname)
        .setPath(path)
        .setSecurityPolicy(SecurityPolicy.None)
        .setSecurityMode(MessageSecurityMode.None)
        .addTokenPolicy(OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS)
        .build();
  }

  private static EndpointDescription endpointByUrl(OpcUaServer server, String url) {
    return server.getApplicationContext().getEndpointDescriptions().stream()
        .filter(e -> url.equals(e.getEndpointUrl()))
        .findFirst()
        .orElseThrow();
  }

  private static List<String> urls(EndpointDescription @Nullable [] endpoints) {
    if (endpoints == null) {
      return List.of();
    }
    return Arrays.stream(endpoints).map(EndpointDescription::getEndpointUrl).toList();
  }

  private static GetEndpointsRequest getEndpointsRequest(@Nullable String endpointUrl) {
    return new GetEndpointsRequest(requestHeader(), endpointUrl, null, null);
  }

  private static CreateSessionRequest createSessionRequest(@Nullable String endpointUrl) {
    return new CreateSessionRequest(
        requestHeader(),
        new ApplicationDescription(
            "urn:eclipse:milo:test:client",
            "urn:eclipse:milo:test",
            LocalizedText.english("test client"),
            ApplicationType.Client,
            null,
            null,
            null),
        null,
        endpointUrl,
        "test-session",
        NonceUtil.generateNonce(32),
        ByteString.NULL_VALUE,
        60_000.0,
        uint(0));
  }

  private static RequestHeader requestHeader() {
    return new RequestHeader(
        NodeId.NULL_VALUE, DateTime.now(), uint(1), uint(0), null, uint(10_000), null);
  }

  private static ServiceRequestContext context(
      String endpointUrl,
      TransportProfile transportProfile,
      @Nullable EndpointDescription endpoint) {

    var secureChannel = new ServerSecureChannel();
    secureChannel.setChannelId(1L);
    secureChannel.setSecurityPolicy(SecurityPolicy.None);
    secureChannel.setMessageSecurityMode(MessageSecurityMode.None);

    return new ServiceRequestContext() {
      @Override
      public String getEndpointUrl() {
        return endpointUrl;
      }

      @Override
      public TransportProfile getTransportProfile() {
        return transportProfile;
      }

      @Override
      public Channel getChannel() {
        throw new UnsupportedOperationException();
      }

      @Override
      public SecureChannel getSecureChannel() {
        return secureChannel;
      }

      @Override
      public Optional<EndpointDescription> getEndpoint() {
        return Optional.ofNullable(endpoint);
      }

      @Override
      public Long receivedAtNanos() {
        return System.nanoTime();
      }

      @Override
      public InetAddress clientAddress() {
        return InetAddress.getLoopbackAddress();
      }
    };
  }
}
