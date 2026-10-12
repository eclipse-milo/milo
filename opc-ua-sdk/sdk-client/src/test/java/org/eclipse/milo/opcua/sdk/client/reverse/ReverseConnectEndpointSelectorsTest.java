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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.Stack;
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

class ReverseConnectEndpointSelectorsTest {

  private static final String SERVER_URI = "urn:eclipse:milo:test:reverse-selector:server";
  private static final String ENDPOINT_URL_1 = "opc.tcp://localhost:12685/milo";
  private static final String ENDPOINT_URL_2 = "opc.tcp://localhost:12686/milo";

  @Test
  void preferReverseHelloEndpointUrlSelectsMatchingEndpointBeforeEarlierNoSecurityEndpoint() {
    EndpointDescription first = endpoint(ENDPOINT_URL_1, MessageSecurityMode.None);
    EndpointDescription matchingReverseHello = endpoint(ENDPOINT_URL_2, MessageSecurityMode.None);

    Optional<EndpointDescription> selected =
        ReverseConnectEndpointSelectors.preferReverseHelloEndpointUrl(
                ReverseConnectEndpointSelectors::isNoSecurity)
            .select(candidate(ENDPOINT_URL_2), List.of(first, matchingReverseHello));

    assertSame(matchingReverseHello, selected.orElseThrow());
  }

  @Test
  void preferReverseHelloEndpointUrlFallsBackWhenReverseHelloUrlDoesNotMatchEndpoint() {
    EndpointDescription secure = endpoint(ENDPOINT_URL_1, MessageSecurityMode.SignAndEncrypt);
    EndpointDescription noSecurity = endpoint(ENDPOINT_URL_2, MessageSecurityMode.None);

    Optional<EndpointDescription> selected =
        ReverseConnectEndpointSelectors.preferReverseHelloEndpointUrl(
                ReverseConnectEndpointSelectors::isNoSecurity)
            .select(candidate("opc.tcp://localhost:12687/milo"), List.of(secure, noSecurity));

    assertSame(noSecurity, selected.orElseThrow());
  }

  @Test
  void selectorFallbackRunsWhenPrimarySelectorDoesNotChooseEndpoint() {
    EndpointDescription endpoint = endpoint(ENDPOINT_URL_1, MessageSecurityMode.None);

    ReverseConnectEndpointSelector selector =
        ((ReverseConnectEndpointSelector) (candidate, endpoints) -> Optional.empty())
            .orElse(ReverseConnectEndpointSelectors.noSecurity());

    assertSame(
        endpoint, selector.select(candidate(ENDPOINT_URL_1), List.of(endpoint)).orElseThrow());
  }

  @Test
  void noSecurityAnonymousRequiresAnonymousUserTokenPolicy() {
    EndpointDescription usernameOnly =
        endpoint(ENDPOINT_URL_1, MessageSecurityMode.None, UserTokenType.UserName);
    EndpointDescription anonymous =
        endpoint(ENDPOINT_URL_2, MessageSecurityMode.None, UserTokenType.Anonymous);

    assertFalse(ReverseConnectEndpointSelectors.isNoSecurityAndAnonymous(usernameOnly));
    assertTrue(ReverseConnectEndpointSelectors.isNoSecurityAndAnonymous(anonymous));

    Optional<EndpointDescription> selected =
        ReverseConnectEndpointSelectors.preferReverseHelloEndpointUrl(
                ReverseConnectEndpointSelectors::isNoSecurityAndAnonymous)
            .select(candidate(ENDPOINT_URL_1), List.of(usernameOnly, anonymous));

    assertSame(anonymous, selected.orElseThrow());
  }

  @Test
  void discoveryResultCopiesEndpointListAndExposesRoutingHints() {
    EndpointDescription endpoint = endpoint(ENDPOINT_URL_1, MessageSecurityMode.None);
    List<EndpointDescription> endpoints = new ArrayList<>(List.of(endpoint));

    ReverseConnectDiscoveryResult result =
        new ReverseConnectDiscoveryResult(candidate(ENDPOINT_URL_1), endpoints);

    endpoints.clear();

    assertEquals(SERVER_URI, result.serverUri());
    assertEquals(ENDPOINT_URL_1, result.endpointUrl());
    assertEquals(List.of(endpoint), result.endpoints());
    assertThrows(
        UnsupportedOperationException.class,
        () -> result.endpoints().add(endpoint(ENDPOINT_URL_2, MessageSecurityMode.None)));
  }

  private static ReverseConnectCandidateSnapshot candidate(String endpointUrl) {
    Instant now = Instant.now();

    return new ReverseConnectCandidateSnapshot(
        UUID.randomUUID(),
        ReverseConnectCandidateState.PENDING,
        SERVER_URI,
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

  private static EndpointDescription endpoint(
      String endpointUrl, MessageSecurityMode securityMode) {
    return endpoint(endpointUrl, securityMode, UserTokenType.Anonymous);
  }

  private static EndpointDescription endpoint(
      String endpointUrl, MessageSecurityMode securityMode, UserTokenType userTokenType) {

    var server =
        new ApplicationDescription(
            SERVER_URI,
            "urn:eclipse:milo:test:reverse-selector:product",
            LocalizedText.english("test server"),
            ApplicationType.Server,
            null,
            null,
            null);

    var userTokenPolicy =
        new UserTokenPolicy(userTokenType.name(), userTokenType, null, null, null);

    String securityPolicyUri =
        securityMode == MessageSecurityMode.None
            ? SecurityPolicy.None.getUri()
            : SecurityPolicy.Basic256Sha256.getUri();

    return new EndpointDescription(
        endpointUrl,
        server,
        ByteString.NULL_VALUE,
        securityMode,
        securityPolicyUri,
        new UserTokenPolicy[] {userTokenPolicy},
        Stack.TCP_UASC_UABINARY_TRANSPORT_URI,
        ubyte(0));
  }
}
