/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.servicesets.impl;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.GetEndpointsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.GetEndpointsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class DefaultDiscoveryServiceSetTest {

  private static final String LOCAL_APPLICATION_URI = "urn:eclipse:milo:test:server";
  private static final String PEER_APPLICATION_URI = "urn:eclipse:milo:test:peer";

  private static final ApplicationDescription PEER_DESCRIPTION =
      new ApplicationDescription(
          PEER_APPLICATION_URI,
          "urn:eclipse:milo:test",
          LocalizedText.english("peer server"),
          ApplicationType.Server,
          null,
          null,
          new String[] {"opc.tcp://peer-host:4840/milo/discovery"});

  @Test
  void emptyAdditionalServersMatchesDefaultBehavior() {
    OpcUaServer server = createServer();

    FindServersRequest request = findServersRequest("opc.tcp://localhost:4840/milo", null);

    FindServersResponse defaultResponse =
        new DefaultDiscoveryServiceSet(server)
            .onFindServers(mock(ServiceRequestContext.class), request);
    FindServersResponse response =
        new DefaultDiscoveryServiceSet(server, List::of)
            .onFindServers(mock(ServiceRequestContext.class), request);

    assertArrayEquals(defaultResponse.getServers(), response.getServers());
  }

  @Test
  void additionalServersAreIncludedInFindServersResults() {
    OpcUaServer server = createServer();

    DefaultDiscoveryServiceSet serviceSet =
        new DefaultDiscoveryServiceSet(server, () -> List.of(PEER_DESCRIPTION));

    FindServersResponse response =
        serviceSet.onFindServers(
            mock(ServiceRequestContext.class),
            findServersRequest("opc.tcp://localhost:4840/milo", null));

    ApplicationDescription[] servers = response.getServers();
    assertEquals(2, servers.length);
    assertEquals(LOCAL_APPLICATION_URI, servers[0].getApplicationUri());
    assertEquals(PEER_DESCRIPTION, servers[1]);
  }

  @Test
  void serverUrisFilterSelectsOnlyAdditionalServer() {
    OpcUaServer server = createServer();

    DefaultDiscoveryServiceSet serviceSet =
        new DefaultDiscoveryServiceSet(server, () -> List.of(PEER_DESCRIPTION));

    FindServersResponse response =
        serviceSet.onFindServers(
            mock(ServiceRequestContext.class),
            findServersRequest(
                "opc.tcp://localhost:4840/milo", new String[] {PEER_APPLICATION_URI}));

    assertArrayEquals(new ApplicationDescription[] {PEER_DESCRIPTION}, response.getServers());
  }

  @Test
  void serverUrisFilterSelectsOnlyLocalServer() {
    OpcUaServer server = createServer();

    DefaultDiscoveryServiceSet serviceSet =
        new DefaultDiscoveryServiceSet(server, () -> List.of(PEER_DESCRIPTION));

    FindServersResponse response =
        serviceSet.onFindServers(
            mock(ServiceRequestContext.class),
            findServersRequest(
                "opc.tcp://localhost:4840/milo", new String[] {LOCAL_APPLICATION_URI}));

    ApplicationDescription[] servers = response.getServers();
    assertEquals(1, servers.length);
    assertEquals(LOCAL_APPLICATION_URI, servers[0].getApplicationUri());
  }

  @Test
  void serverUrisFilterWithUnknownUriReturnsNoServers() {
    OpcUaServer server = createServer();

    DefaultDiscoveryServiceSet serviceSet =
        new DefaultDiscoveryServiceSet(server, () -> List.of(PEER_DESCRIPTION));

    FindServersResponse response =
        serviceSet.onFindServers(
            mock(ServiceRequestContext.class),
            findServersRequest("opc.tcp://localhost:4840/milo", new String[] {"urn:unknown"}));

    assertEquals(0, response.getServers().length);
  }

  /**
   * OPC UA Part 4 (§5.5.2.2, §5.5.4.2): when a client discovers a multi-host Server using an
   * unrecognized HostName, GetEndpoints and FindServers should return URLs for one default HostName
   * per transport profile rather than every advertised HostName.
   */
  @Nested
  class MultiHostFallback {

    // Part 4 §5.5.4.2: an unrecognized request host must fall back to the first configured host
    // only, and each returned EndpointDescription's ApplicationDescription.discoveryUrls must be
    // filtered to that same default host.
    @Test
    void getEndpointsAtUnadvertisedHostReturnsDefaultHostAndFilteredDiscoveryUrls() {
      OpcUaServer server =
          createServer(
              endpoint(TransportProfile.TCP_UASC_UABINARY, "primary.test", 4840, "/milo"),
              endpoint(TransportProfile.TCP_UASC_UABINARY, "primary.test", 4841, "/alt"),
              endpoint(TransportProfile.TCP_UASC_UABINARY, "primary.test", 4840, "/milo/discovery"),
              endpoint(TransportProfile.TCP_UASC_UABINARY, "secondary.test", 4840, "/milo"),
              endpoint(TransportProfile.TCP_UASC_UABINARY, "secondary.test", 4841, "/alt"),
              endpoint(
                  TransportProfile.TCP_UASC_UABINARY, "secondary.test", 4840, "/milo/discovery"));

      var serviceSet = new DefaultDiscoveryServiceSet(server);

      GetEndpointsResponse fallback =
          serviceSet.onGetEndpoints(
              mock(ServiceRequestContext.class),
              getEndpointsRequest("opc.tcp://127.0.0.1:4840/milo", null));

      List<EndpointDescription> fallbackEndpoints = List.of(fallback.getEndpoints());
      assertEquals(
          List.of("opc.tcp://primary.test:4840/milo", "opc.tcp://primary.test:4841/alt"),
          urls(fallbackEndpoints),
          "fallback should return only the first configured host's endpoints");
      for (EndpointDescription ed : fallbackEndpoints) {
        assertArrayEquals(
            new String[] {"opc.tcp://primary.test:4840/milo/discovery"},
            ed.getServer().getDiscoveryUrls(),
            "fallback ApplicationDescription.discoveryUrls must not leak secondary host URLs");
      }

      // Control: requesting an advertised secondary host still returns that host's endpoints.
      GetEndpointsResponse matchedSecondary =
          serviceSet.onGetEndpoints(
              mock(ServiceRequestContext.class),
              getEndpointsRequest("opc.tcp://secondary.test:4840/milo", null));

      List<EndpointDescription> secondaryEndpoints = List.of(matchedSecondary.getEndpoints());
      assertEquals(
          List.of("opc.tcp://secondary.test:4840/milo", "opc.tcp://secondary.test:4841/alt"),
          urls(secondaryEndpoints));
      for (EndpointDescription ed : secondaryEndpoints) {
        assertArrayEquals(
            new String[] {"opc.tcp://secondary.test:4840/milo/discovery"},
            ed.getServer().getDiscoveryUrls());
      }
    }

    // Default-host selection must follow the configured endpoint iteration order preserved by
    // OpcUaServerConfigBuilder's LinkedHashSet.
    @Test
    void getEndpointsFallbackFollowsConfiguredEndpointOrder() {
      OpcUaServer server =
          createServer(
              endpoint(TransportProfile.TCP_UASC_UABINARY, "secondary.test", 4840, "/milo"),
              endpoint(TransportProfile.TCP_UASC_UABINARY, "primary.test", 4840, "/milo"));

      var serviceSet = new DefaultDiscoveryServiceSet(server);

      GetEndpointsResponse response =
          serviceSet.onGetEndpoints(
              mock(ServiceRequestContext.class),
              getEndpointsRequest("opc.tcp://127.0.0.1:4840/milo", null));

      assertEquals(
          List.of("opc.tcp://secondary.test:4840/milo"), urls(List.of(response.getEndpoints())));
      assertArrayEquals(
          new String[] {"opc.tcp://secondary.test:4840/milo"},
          response.getEndpoints()[0].getServer().getDiscoveryUrls());
    }

    // Multi-transport servers must fall back to one default host per transport profile and honor
    // profileUris before applying host matching/fallback (Part 4 §5.5.4.2).
    @Test
    void getEndpointsHandlesMultipleTransportProfilesAndProfileUrisFilter() {
      OpcUaServer server =
          createServer(
              endpoint(TransportProfile.TCP_UASC_UABINARY, "tcp-primary.test", 4840, "/milo"),
              endpoint(
                  TransportProfile.TCP_UASC_UABINARY, "tcp-primary.test", 4840, "/milo/discovery"),
              endpoint(TransportProfile.TCP_UASC_UABINARY, "tcp-secondary.test", 4840, "/milo"),
              endpoint(
                  TransportProfile.TCP_UASC_UABINARY,
                  "tcp-secondary.test",
                  4840,
                  "/milo/discovery"),
              endpoint(TransportProfile.HTTPS_UABINARY, "https-primary.test", 8443, "/milo"),
              endpoint(
                  TransportProfile.HTTPS_UABINARY, "https-primary.test", 8443, "/milo/discovery"),
              endpoint(TransportProfile.HTTPS_UABINARY, "https-secondary.test", 8443, "/milo"),
              endpoint(
                  TransportProfile.HTTPS_UABINARY,
                  "https-secondary.test",
                  8443,
                  "/milo/discovery"));

      var serviceSet = new DefaultDiscoveryServiceSet(server);

      GetEndpointsResponse allProfiles =
          serviceSet.onGetEndpoints(
              mock(ServiceRequestContext.class),
              getEndpointsRequest("opc.tcp://127.0.0.1:4840/milo", null));

      assertEquals(
          List.of("opc.tcp://tcp-primary.test:4840/milo", "https://https-primary.test:8443/milo"),
          urls(List.of(allProfiles.getEndpoints())));
      for (EndpointDescription ed : allProfiles.getEndpoints()) {
        assertArrayEquals(
            new String[] {
              "opc.tcp://tcp-primary.test:4840/milo/discovery",
              "https://https-primary.test:8443/milo/discovery"
            },
            ed.getServer().getDiscoveryUrls());
      }

      GetEndpointsResponse httpsOnly =
          serviceSet.onGetEndpoints(
              mock(ServiceRequestContext.class),
              getEndpointsRequest(
                  "opc.tcp://tcp-primary.test:4840/milo",
                  new String[] {Stack.HTTPS_UABINARY_TRANSPORT_URI}));

      assertEquals(
          List.of("https://https-primary.test:8443/milo"),
          urls(List.of(httpsOnly.getEndpoints())),
          "profileUris must filter before host fallback selects the HTTPS default host");
      assertArrayEquals(
          new String[] {"https://https-primary.test:8443/milo/discovery"},
          httpsOnly.getEndpoints()[0].getServer().getDiscoveryUrls(),
          "embedded discoveryUrls must align with the profileUris-filtered candidate set");

      GetEndpointsResponse unmatchedProfile =
          serviceSet.onGetEndpoints(
              mock(ServiceRequestContext.class),
              getEndpointsRequest(
                  "opc.tcp://127.0.0.1:4840/milo",
                  new String[] {Stack.WSS_UASC_UABINARY_TRANSPORT_URI}));

      assertEquals(0, unmatchedProfile.getEndpoints().length);
    }

    // OpcUaServer normalizes trailing slashes when marking /discovery paths discovery-only;
    // GetEndpoints must apply the same rule so a discovery-only endpoint with a trailing slash
    // never becomes the sole endpoint advertised on fallback.
    @Test
    void trailingSlashDiscoveryEndpointIsExcludedFromGetEndpointsFallback() {
      OpcUaServer server =
          createServer(
              endpoint(TransportProfile.TCP_UASC_UABINARY, "a.test", 4840, "/app/discovery/"),
              endpoint(TransportProfile.TCP_UASC_UABINARY, "b.test", 4840, "/app"));

      var serviceSet = new DefaultDiscoveryServiceSet(server);

      GetEndpointsResponse response =
          serviceSet.onGetEndpoints(
              mock(ServiceRequestContext.class),
              getEndpointsRequest("opc.tcp://127.0.0.1:4840/app", null));

      assertEquals(List.of("opc.tcp://b.test:4840/app"), urls(List.of(response.getEndpoints())));
      assertArrayEquals(
          new String[] {"opc.tcp://b.test:4840/app"},
          response.getEndpoints()[0].getServer().getDiscoveryUrls());
    }

    // Requesting GetEndpoints at a host that only has a /discovery/ endpoint falls back to the
    // default session hosts across profiles; embedded discoveryUrls must reflect those returned
    // transports/hosts rather than returning only the discovery-only host and dropping HTTPS.
    @Test
    void getEndpointsAtDiscoveryOnlyHostKeepsDiscoveryUrlsAlignedWithReturnedTransports() {
      OpcUaServer server =
          createServer(
              endpoint(TransportProfile.TCP_UASC_UABINARY, "a.test", 4840, "/app/discovery/"),
              endpoint(TransportProfile.TCP_UASC_UABINARY, "b.test", 4840, "/app"),
              endpoint(TransportProfile.HTTPS_UABINARY, "c.test", 8443, "/app"));

      var serviceSet = new DefaultDiscoveryServiceSet(server);

      GetEndpointsResponse atDiscoveryOnlyHost =
          serviceSet.onGetEndpoints(
              mock(ServiceRequestContext.class),
              getEndpointsRequest("opc.tcp://a.test:4840/app/discovery/", null));

      assertEquals(
          List.of("opc.tcp://b.test:4840/app", "https://c.test:8443/app"),
          urls(List.of(atDiscoveryOnlyHost.getEndpoints())));
      for (EndpointDescription ed : atDiscoveryOnlyHost.getEndpoints()) {
        assertArrayEquals(
            new String[] {"opc.tcp://b.test:4840/app", "https://c.test:8443/app"},
            ed.getServer().getDiscoveryUrls(),
            "embedded discoveryUrls must align with the returned TCP and HTTPS endpoints");
      }

      // Control: FindServers at an unadvertised address also returns both default session hosts.
      FindServersResponse findServersFallback =
          serviceSet.onFindServers(
              mock(ServiceRequestContext.class),
              findServersRequest("opc.tcp://127.0.0.1:4840/app", null));

      assertEquals(1, findServersFallback.getServers().length);
      assertArrayEquals(
          new String[] {"opc.tcp://b.test:4840/app", "https://c.test:8443/app"},
          findServersFallback.getServers()[0].getDiscoveryUrls());
    }

    // When one transport defines a dedicated /discovery endpoint and another does not, both
    // FindServers and GetEndpoints must preserve each transport's usable discovery URL.
    @Test
    void dedicatedDiscoveryUrlOnTcpDoesNotSuppressHttpsDiscoveryUrl() {
      OpcUaServer server =
          createServer(
              endpoint(TransportProfile.TCP_UASC_UABINARY, "a.test", 4840, "/app"),
              endpoint(TransportProfile.TCP_UASC_UABINARY, "a.test", 4840, "/app/discovery"),
              endpoint(TransportProfile.TCP_UASC_UABINARY, "a2.test", 4840, "/app"),
              endpoint(TransportProfile.TCP_UASC_UABINARY, "a2.test", 4840, "/app/discovery"),
              endpoint(TransportProfile.HTTPS_UABINARY, "b.test", 8443, "/app"),
              endpoint(TransportProfile.HTTPS_UABINARY, "b2.test", 8443, "/app"));

      var serviceSet = new DefaultDiscoveryServiceSet(server);
      String[] expectedDiscoveryUrls =
          new String[] {"opc.tcp://a.test:4840/app/discovery", "https://b.test:8443/app"};

      FindServersResponse findServers =
          serviceSet.onFindServers(
              mock(ServiceRequestContext.class),
              findServersRequest("opc.tcp://127.0.0.1:4840/app", null));

      assertEquals(1, findServers.getServers().length);
      assertArrayEquals(expectedDiscoveryUrls, findServers.getServers()[0].getDiscoveryUrls());

      GetEndpointsResponse getEndpoints =
          serviceSet.onGetEndpoints(
              mock(ServiceRequestContext.class),
              getEndpointsRequest("opc.tcp://127.0.0.1:4840/app", null));

      assertEquals(
          List.of("opc.tcp://a.test:4840/app", "https://b.test:8443/app"),
          urls(List.of(getEndpoints.getEndpoints())));
      for (EndpointDescription ed : getEndpoints.getEndpoints()) {
        assertArrayEquals(expectedDiscoveryUrls, ed.getServer().getDiscoveryUrls());
      }
    }

    // Part 4 §5.5.2.2: FindServers with an unrecognized host must also return only the default
    // host's discovery URL(s), while matching hosts continue to return their own discovery URLs.
    @Test
    void findServersAtUnadvertisedHostReturnsDefaultHostDiscoveryUrlsOnly() {
      OpcUaServer server =
          createServer(
              endpoint(TransportProfile.TCP_UASC_UABINARY, "primary.test", 4840, "/milo"),
              endpoint(TransportProfile.TCP_UASC_UABINARY, "primary.test", 4840, "/milo/discovery"),
              endpoint(TransportProfile.TCP_UASC_UABINARY, "secondary.test", 4840, "/milo"),
              endpoint(
                  TransportProfile.TCP_UASC_UABINARY, "secondary.test", 4840, "/milo/discovery"));

      var serviceSet = new DefaultDiscoveryServiceSet(server);

      FindServersResponse fallback =
          serviceSet.onFindServers(
              mock(ServiceRequestContext.class),
              findServersRequest("opc.tcp://127.0.0.1:4840/milo", null));

      assertEquals(1, fallback.getServers().length);
      assertArrayEquals(
          new String[] {"opc.tcp://primary.test:4840/milo/discovery"},
          fallback.getServers()[0].getDiscoveryUrls());

      FindServersResponse matched =
          serviceSet.onFindServers(
              mock(ServiceRequestContext.class),
              findServersRequest("opc.tcp://secondary.test:4840/milo", null));

      assertEquals(1, matched.getServers().length);
      assertArrayEquals(
          new String[] {"opc.tcp://secondary.test:4840/milo/discovery"},
          matched.getServers()[0].getDiscoveryUrls());
    }

    // Null or unparseable request endpoint URLs must fall back cleanly to the default host.
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"not-a-valid-url"})
    void getEndpointsWithNullOrUnparseableUrlFallsBackToDefaultHost(@Nullable String endpointUrl) {
      OpcUaServer server =
          createServer(
              endpoint(TransportProfile.TCP_UASC_UABINARY, "primary.test", 4840, "/milo"),
              endpoint(TransportProfile.TCP_UASC_UABINARY, "secondary.test", 4840, "/milo"));

      var serviceSet = new DefaultDiscoveryServiceSet(server);

      GetEndpointsResponse response =
          serviceSet.onGetEndpoints(
              mock(ServiceRequestContext.class), getEndpointsRequest(endpointUrl, null));

      assertEquals(
          List.of("opc.tcp://primary.test:4840/milo"), urls(List.of(response.getEndpoints())));
    }
  }

  private static OpcUaServer createServer() {
    return createServer(endpoint("/milo"), endpoint("/milo/discovery"));
  }

  private static OpcUaServer createServer(EndpointConfig... endpointConfigs) {
    Set<EndpointConfig> endpoints = new LinkedHashSet<>(Arrays.asList(endpointConfigs));

    OpcUaServerConfig config =
        OpcUaServerConfig.builder()
            .setApplicationUri(LOCAL_APPLICATION_URI)
            .setApplicationName(LocalizedText.english("test server"))
            .setProductUri("urn:eclipse:milo:test")
            .setCertificateManager(new DefaultCertificateManager())
            .setEndpoints(endpoints)
            .build();

    return new OpcUaServer(config, transportProfile -> null);
  }

  private static EndpointConfig endpoint(String path) {
    return endpoint(TransportProfile.TCP_UASC_UABINARY, "localhost", 4840, path);
  }

  private static EndpointConfig endpoint(
      TransportProfile transportProfile, String hostname, int port, String path) {
    return EndpointConfig.newBuilder()
        .setTransportProfile(transportProfile)
        .setBindPort(port)
        .setHostname(hostname)
        .setPath(path)
        .setSecurityPolicy(SecurityPolicy.None)
        .setSecurityMode(MessageSecurityMode.None)
        .addTokenPolicy(OpcUaServerConfig.USER_TOKEN_POLICY_ANONYMOUS)
        .build();
  }

  private static List<String> urls(List<EndpointDescription> endpoints) {
    return endpoints.stream().map(EndpointDescription::getEndpointUrl).toList();
  }

  private static GetEndpointsRequest getEndpointsRequest(
      @Nullable String endpointUrl, String @Nullable [] profileUris) {
    RequestHeader requestHeader =
        new RequestHeader(NodeId.NULL_VALUE, DateTime.now(), uint(1), uint(0), null, uint(0), null);

    return new GetEndpointsRequest(requestHeader, endpointUrl, null, profileUris);
  }

  private static FindServersRequest findServersRequest(String endpointUrl, String[] serverUris) {
    RequestHeader requestHeader =
        new RequestHeader(NodeId.NULL_VALUE, DateTime.now(), uint(1), uint(0), null, uint(0), null);

    return new FindServersRequest(requestHeader, endpointUrl, null, serverUris);
  }
}
