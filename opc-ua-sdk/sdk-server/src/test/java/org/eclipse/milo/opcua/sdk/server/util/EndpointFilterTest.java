/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.util;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for {@link EndpointFilter}.
 *
 * <p>Protects the OPC UA Part 4 (§5.5.1, §5.5.2.2, §5.5.4.2, §5.7.2.2) invariant that unrecognized
 * request hosts fall back to one deterministic default HostName per transport profile rather than
 * returning endpoints from every advertised HostName.
 */
class EndpointFilterTest {

  private static final String TCP = Stack.TCP_UASC_UABINARY_TRANSPORT_URI;
  private static final String HTTPS = Stack.HTTPS_UABINARY_TRANSPORT_URI;
  private static final String WSS = Stack.WSS_UASC_UABINARY_TRANSPORT_URI;

  private static final ApplicationDescription SERVER_DESCRIPTION =
      new ApplicationDescription(
          "urn:eclipse:milo:test:server",
          "urn:eclipse:milo:test",
          LocalizedText.english("test server"),
          ApplicationType.Server,
          null,
          null,
          new String[0]);

  @Nested
  class HostFiltering {

    // Part 4 §5.5.4.1 / §5.7.2.2: matching a configured host must return only that host's
    // endpoints, matched case-insensitively.
    @Test
    void matchingHostReturnsOnlyEndpointsForThatHostIgnoringCase() {
      EndpointDescription h1None = endpoint("opc.tcp://Primary.Test:4840/app", TCP, 0);
      EndpointDescription h1Sec = endpoint("opc.tcp://primary.test:4840/app", TCP, 1);
      EndpointDescription h2None = endpoint("opc.tcp://secondary.test:4840/app", TCP, 0);

      List<EndpointDescription> result =
          EndpointFilter.filterByHostOrDefault(
              List.of(h1None, h1Sec, h2None), "opc.tcp://PRIMARY.TEST:4840/app");

      assertEquals(List.of(h1None, h1Sec), result);
    }

    // Part 4 §5.5.4.2 / §5.7.2.2: when no advertised host matches, return only the first
    // configured host's endpoints for the transport profile so GetEndpoints and CreateSession
    // agree on the same endpoint set.
    @Test
    void unmatchedHostFallsBackToFirstConfiguredHostPerProfile() {
      EndpointDescription h1None = endpoint("opc.tcp://primary.test:4840/app", TCP, 0);
      EndpointDescription h1Sec = endpoint("opc.tcp://primary.test:4840/app", TCP, 1);
      EndpointDescription h2None = endpoint("opc.tcp://secondary.test:4840/app", TCP, 0);
      EndpointDescription h2Sec = endpoint("opc.tcp://secondary.test:4840/app", TCP, 1);

      List<EndpointDescription> result =
          EndpointFilter.filterByHostOrDefault(
              List.of(h1None, h1Sec, h2None, h2Sec), "opc.tcp://127.0.0.1:4840/app");

      assertEquals(List.of(h1None, h1Sec), result);
    }

    // Single-hostname servers must behave identically for both matching and unmatched request
    // hosts.
    @Test
    void singleHostnameServerReturnsAllEndpointsOnMatchAndFallback() {
      EndpointDescription none = endpoint("opc.tcp://localhost:4840/app", TCP, 0);
      EndpointDescription sec = endpoint("opc.tcp://localhost:4840/app", TCP, 1);
      List<EndpointDescription> candidates = List.of(none, sec);

      assertEquals(
          candidates,
          EndpointFilter.filterByHostOrDefault(candidates, "opc.tcp://localhost:4840/app"));
      assertEquals(
          candidates,
          EndpointFilter.filterByHostOrDefault(candidates, "opc.tcp://127.0.0.1:4840/app"));
    }

    // Each transport profile may advertise its own host order; fallback must choose one default
    // host independently per transportProfileUri while preserving candidate order.
    @Test
    void multipleTransportProfilesSelectOneDefaultHostPerProfileIndependently() {
      EndpointDescription tcpA = endpoint("opc.tcp://host-a:4840/app", TCP, 0);
      EndpointDescription tcpB = endpoint("opc.tcp://host-b:4840/app", TCP, 0);
      EndpointDescription httpsB = endpoint("https://host-b:8443/app", HTTPS, 0);
      EndpointDescription httpsA = endpoint("https://host-a:8443/app", HTTPS, 0);
      EndpointDescription wssC = endpoint("opc.wss://host-c:4843/app", WSS, 0);
      EndpointDescription wssD = endpoint("opc.wss://host-d:4843/app", WSS, 0);

      List<EndpointDescription> result =
          EndpointFilter.filterByHostOrDefault(
              List.of(tcpA, tcpB, httpsB, httpsA, wssC, wssD), "opc.tcp://127.0.0.1:4840/app");

      assertEquals(List.of(tcpA, httpsB, wssC), result);
    }

    // Mixed-case spellings of the default host must be grouped together, matching the
    // case-insensitive host check used on direct CreateSession requests.
    @Test
    void fallbackMatchesDefaultHostCaseInsensitively() {
      EndpointDescription upper = endpoint("opc.tcp://MyHost:4840/app", TCP, 0);
      EndpointDescription lower = endpoint("opc.tcp://myhost:4840/app", TCP, 1);
      EndpointDescription other = endpoint("opc.tcp://other:4840/app", TCP, 0);

      List<EndpointDescription> result =
          EndpointFilter.filterByHostOrDefault(
              List.of(upper, lower, other), "opc.tcp://127.0.0.1:4840/app");

      assertEquals(List.of(upper, lower), result);
    }

    // A null, empty, or unparseable request URL has no host to match and must fall back cleanly
    // to the default host rather than matching null-host endpoints or throwing.
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"not-a-url", "ftp://unsupported:4840/app"})
    void nullOrUnparseableRequestUrlFallsBackToDefaultHost(@Nullable String requestedUrl) {
      EndpointDescription h1 = endpoint("opc.tcp://primary.test:4840/app", TCP, 0);
      EndpointDescription h2 = endpoint("opc.tcp://secondary.test:4840/app", TCP, 0);

      List<EndpointDescription> result =
          EndpointFilter.filterByHostOrDefault(List.of(h1, h2), requestedUrl);

      assertEquals(List.of(h1), result);
    }

    // When an earlier candidate has a null or unparseable URL, fallback must skip it when picking
    // the default host and exclude it once a valid default host exists so CreateSession at that
    // default host returns the same list.
    @Test
    void nullAndUnparseableCandidateHostsAreSkippedWhenValidDefaultHostExists() {
      EndpointDescription nullUrl = endpoint(null, TCP, 0);
      EndpointDescription badUrl = endpoint("not-a-url", TCP, 1);
      EndpointDescription h1 = endpoint("opc.tcp://primary.test:4840/app", TCP, 2);
      EndpointDescription h2 = endpoint("opc.tcp://secondary.test:4840/app", TCP, 3);

      List<EndpointDescription> candidates = List.of(nullUrl, badUrl, h1, h2);

      List<EndpointDescription> onUnadvertised =
          EndpointFilter.filterByHostOrDefault(candidates, "opc.tcp://127.0.0.1:4840/app");
      List<EndpointDescription> onNullRequest =
          EndpointFilter.filterByHostOrDefault(candidates, null);
      List<EndpointDescription> onDefaultHost =
          EndpointFilter.filterByHostOrDefault(candidates, "opc.tcp://primary.test:4840/app");

      assertEquals(List.of(h1), onUnadvertised);
      assertEquals(List.of(h1), onNullRequest);
      assertEquals(List.of(h1), onDefaultHost);
    }

    // If every candidate for a transport profile has an unparseable or null host, retaining those
    // candidates avoids returning an empty list when no default host can be determined.
    @Test
    void profileWithOnlyUnparseableHostsRetainsAllItsCandidatesOnFallback() {
      EndpointDescription badTcp1 = endpoint("custom-1", TCP, 0);
      EndpointDescription badTcp2 = endpoint("custom-2", TCP, 1);
      EndpointDescription httpsH1 = endpoint("https://h1:8443/app", HTTPS, 0);
      EndpointDescription httpsH2 = endpoint("https://h2:8443/app", HTTPS, 0);

      List<EndpointDescription> result =
          EndpointFilter.filterByHostOrDefault(
              List.of(badTcp1, badTcp2, httpsH1, httpsH2), "opc.tcp://127.0.0.1:4840/app");

      assertEquals(List.of(badTcp1, badTcp2, httpsH1), result);
    }

    // Synthetic endpoints with a null transportProfileUri must not cause a NullPointerException.
    @Test
    void nullTransportProfileUriIsHandledWithoutThrowing() {
      EndpointDescription first = endpoint("opc.tcp://primary.test:4840/app", null, 0);
      EndpointDescription second = endpoint("opc.tcp://secondary.test:4840/app", null, 0);

      assertEquals(
          List.of(first),
          EndpointFilter.filterByHostOrDefault(
              List.of(first, second), "opc.tcp://127.0.0.1:4840/app"));
    }

    @Test
    void emptyCandidatesReturnsEmptyList() {
      assertEquals(
          List.of(), EndpointFilter.filterByHostOrDefault(List.of(), "opc.tcp://localhost:4840"));
    }

    @Test
    void isDiscoveryEndpointHandlesNullUrlAndTrailingSlashConsistentlyWithServerRegistration() {
      assertFalse(EndpointFilter.isDiscoveryEndpoint(endpoint(null, TCP, 0)));
      assertFalse(EndpointFilter.isDiscoveryEndpoint(endpoint("opc.tcp://h1:4840/app", TCP, 0)));
      assertTrue(
          EndpointFilter.isDiscoveryEndpoint(endpoint("opc.tcp://h1:4840/app/discovery", TCP, 0)));
      assertTrue(
          EndpointFilter.isDiscoveryEndpoint(endpoint("opc.tcp://h1:4840/app/discovery/", TCP, 0)));
      assertTrue(
          EndpointFilter.isDiscoveryEndpoint(endpoint("opc.tcp://h1:4840/discovery/", TCP, 0)));
    }
  }

  @Nested
  class DiscoveryUrlFiltering {

    // OpcUaServer registers /app/discovery/ as discovery-only via EndpointUtil.getPath; fallback
    // must exclude it as a discovery endpoint rather than selecting its host as the default
    // session host.
    @Test
    void trailingSlashDiscoveryEndpointDoesNotBecomeDefaultHost() {
      EndpointDescription discoveryWithSlash = endpoint("opc.tcp://a:4840/app/discovery/", TCP, 0);
      EndpointDescription sessionEndpoint = endpoint("opc.tcp://b:4840/app", TCP, 0);
      List<EndpointDescription> all = List.of(discoveryWithSlash, sessionEndpoint);

      List<EndpointDescription> nonDiscovery =
          all.stream().filter(ed -> !EndpointFilter.isDiscoveryEndpoint(ed)).toList();

      assertEquals(
          List.of(sessionEndpoint),
          EndpointFilter.filterByHostOrDefault(nonDiscovery, "opc.tcp://127.0.0.1:4840/app"));
      assertEquals(
          List.of("opc.tcp://b:4840/app"),
          EndpointFilter.filterDiscoveryUrls(all, "opc.tcp://127.0.0.1:4840/app"));
    }

    // One transport profile's dedicated /discovery URL must not suppress another transport
    // profile's regular endpoint URL when that second profile has no /discovery endpoint.
    @Test
    void dedicatedDiscoveryUrlOnOneProfileDoesNotSuppressAnotherProfilesRegularUrl() {
      List<EndpointDescription> all =
          List.of(
              endpoint("opc.tcp://a:4840/app", TCP, 0),
              endpoint("opc.tcp://a:4840/app/discovery", TCP, 0),
              endpoint("opc.tcp://a2:4840/app", TCP, 0),
              endpoint("opc.tcp://a2:4840/app/discovery", TCP, 0),
              endpoint("https://b:8443/app", HTTPS, 0),
              endpoint("https://b2:8443/app", HTTPS, 0));

      assertEquals(
          List.of("opc.tcp://a:4840/app/discovery", "https://b:8443/app"),
          EndpointFilter.filterDiscoveryUrls(all, "opc.tcp://127.0.0.1:4840/app"));

      // Same per-profile preference applies when both transports share a matched host.
      List<EndpointDescription> sharedHost =
          List.of(
              endpoint("opc.tcp://shared:4840/app", TCP, 0),
              endpoint("opc.tcp://shared:4840/app/discovery", TCP, 0),
              endpoint("https://shared:8443/app", HTTPS, 0));

      assertEquals(
          List.of("opc.tcp://shared:4840/app/discovery", "https://shared:8443/app"),
          EndpointFilter.filterDiscoveryUrls(sharedHost, "opc.tcp://shared:4840/app"));
    }

    // Matching a configured host must return only that host's /discovery URLs and never leak
    // another host's discovery URLs.
    @Test
    void matchingHostReturnsOnlyThatHostsDiscoveryUrls() {
      List<EndpointDescription> all =
          List.of(
              endpoint("opc.tcp://h1:4840/app", TCP, 0),
              endpoint("opc.tcp://h1:4840/app/discovery", TCP, 0),
              endpoint("opc.tcp://h2:4840/app", TCP, 0),
              endpoint("opc.tcp://h2:4840/app/discovery", TCP, 0));

      assertEquals(
          List.of("opc.tcp://h2:4840/app/discovery"),
          EndpointFilter.filterDiscoveryUrls(all, "opc.tcp://H2:4840/app"));
    }

    // On fallback, discoveryUrls must be filtered to the per-profile default host(s) rather than
    // exposing every advertised HostName.
    @Test
    void unmatchedHostReturnsDefaultHostDiscoveryUrlsPerProfile() {
      List<EndpointDescription> all =
          List.of(
              endpoint("opc.tcp://tcp-a:4840/app", TCP, 0),
              endpoint("opc.tcp://tcp-a:4840/app/discovery", TCP, 0),
              endpoint("opc.tcp://tcp-b:4840/app", TCP, 0),
              endpoint("opc.tcp://tcp-b:4840/app/discovery", TCP, 0),
              endpoint("https://https-a:8443/app", HTTPS, 0),
              endpoint("https://https-a:8443/app/discovery", HTTPS, 0),
              endpoint("https://https-b:8443/app", HTTPS, 0),
              endpoint("https://https-b:8443/app/discovery", HTTPS, 0));

      assertEquals(
          List.of("opc.tcp://tcp-a:4840/app/discovery", "https://https-a:8443/app/discovery"),
          EndpointFilter.filterDiscoveryUrls(all, "opc.tcp://127.0.0.1:4840/app"));
    }

    // Even when /discovery endpoints are registered in a different host order than regular
    // endpoints, fallback discoveryUrls must follow the default host chosen for regular endpoints.
    @Test
    void reversedDiscoveryEndpointOrderFollowsDefaultNonDiscoveryHost() {
      List<EndpointDescription> all =
          List.of(
              endpoint("opc.tcp://h1:4840/app", TCP, 0),
              endpoint("opc.tcp://h2:4840/app", TCP, 0),
              endpoint("opc.tcp://h2:4840/app/discovery", TCP, 0),
              endpoint("opc.tcp://h1:4840/app/discovery", TCP, 0));

      assertEquals(
          List.of("opc.tcp://h1:4840/app/discovery"),
          EndpointFilter.filterDiscoveryUrls(all, "opc.tcp://127.0.0.1:4840/app"));
    }

    // When a server has no dedicated /discovery endpoints, distinct URLs of the default host are
    // returned on fallback without leaking other hosts.
    @Test
    void withoutDedicatedDiscoveryEndpointsReturnsDistinctDefaultHostUrls() {
      List<EndpointDescription> all =
          List.of(
              endpoint("opc.tcp://h1:4840/app", TCP, 0),
              endpoint("opc.tcp://h1:4840/app", TCP, 1),
              endpoint("opc.tcp://h2:4840/app", TCP, 0));

      assertEquals(
          List.of("opc.tcp://h1:4840/app"),
          EndpointFilter.filterDiscoveryUrls(all, "opc.tcp://127.0.0.1:4840/app"));
    }

    // If only a non-default host defines a /discovery endpoint, requesting the default host (or
    // falling back to it) must use the default host's own endpoint URL rather than leaking the
    // other host's /discovery URL.
    @Test
    void defaultHostWithoutDedicatedDiscoveryEndpointDoesNotLeakOtherHostsDiscoveryUrl() {
      List<EndpointDescription> all =
          List.of(
              endpoint("opc.tcp://h1:4840/app", TCP, 0),
              endpoint("opc.tcp://h2:4840/app", TCP, 0),
              endpoint("opc.tcp://h2:4840/app/discovery", TCP, 0));

      assertEquals(
          List.of("opc.tcp://h1:4840/app"),
          EndpointFilter.filterDiscoveryUrls(all, "opc.tcp://127.0.0.1:4840/app"));
      assertEquals(
          List.of("opc.tcp://h1:4840/app"),
          EndpointFilter.filterDiscoveryUrls(all, "opc.tcp://h1:4840/app"));
      assertEquals(
          List.of("opc.tcp://h2:4840/app/discovery"),
          EndpointFilter.filterDiscoveryUrls(all, "opc.tcp://h2:4840/app"));
    }

    // Interleaved endpoints across multiple transport profiles on the same host must retain
    // original candidate encounter order rather than grouping URLs by profile.
    @Test
    void discoveryUrlsPreserveInterleavedCandidateOrderAcrossProfiles() {
      List<EndpointDescription> all =
          List.of(
              endpoint("opc.tcp://h1:4840/app1", TCP, 0),
              endpoint("https://h1:8443/app1", HTTPS, 0),
              endpoint("opc.tcp://h1:4841/app2", TCP, 0));

      List<String> expected =
          List.of("opc.tcp://h1:4840/app1", "https://h1:8443/app1", "opc.tcp://h1:4841/app2");

      assertEquals(expected, EndpointFilter.filterDiscoveryUrls(all, "opc.tcp://h1:4840/app1"));
      assertEquals(
          expected, EndpointFilter.filterDiscoveryUrls(all, "opc.tcp://127.0.0.1:4840/app1"));
      assertEquals(expected, EndpointFilter.filterDiscoveryUrlsForEndpoints(all, all));
    }

    // When GetEndpoints falls back to session endpoints on hosts b (TCP) and c (HTTPS) despite a
    // discovery-only endpoint on host a, discoveryUrls derived for the selected endpoints must
    // match hosts b and c rather than being suppressed by host a's discovery-only endpoint.
    @Test
    void filterDiscoveryUrlsForEndpointsAlignsWithSelectedHostsAndProfiles() {
      EndpointDescription tcpDiscoveryA = endpoint("opc.tcp://a:4840/app/discovery/", TCP, 0);
      EndpointDescription tcpB = endpoint("opc.tcp://b:4840/app", TCP, 0);
      EndpointDescription httpsC = endpoint("https://c:8443/app", HTTPS, 0);
      List<EndpointDescription> all = List.of(tcpDiscoveryA, tcpB, httpsC);

      assertEquals(
          List.of("opc.tcp://b:4840/app", "https://c:8443/app"),
          EndpointFilter.filterDiscoveryUrlsForEndpoints(all, List.of(tcpB, httpsC)));
    }

    // A server advertising only /discovery endpoints must still pick the first /discovery host per
    // profile on fallback.
    @Test
    void discoveryOnlyEndpointsSelectFirstHostPerProfileOnFallback() {
      List<EndpointDescription> all =
          List.of(
              endpoint("opc.tcp://h1:4840/app/discovery", TCP, 0),
              endpoint("opc.tcp://h2:4840/app/discovery", TCP, 0));

      assertEquals(
          List.of("opc.tcp://h1:4840/app/discovery"),
          EndpointFilter.filterDiscoveryUrls(all, "opc.tcp://127.0.0.1:4840/app"));
    }
  }

  private static EndpointDescription endpoint(
      @Nullable String url, @Nullable String transportProfileUri, int securityLevel) {

    return new EndpointDescription(
        url,
        SERVER_DESCRIPTION,
        ByteString.NULL_VALUE,
        MessageSecurityMode.None,
        SecurityPolicy.None.getUri(),
        new UserTokenPolicy[0],
        transportProfileUri,
        ubyte(securityLevel));
  }
}
