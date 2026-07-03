/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.sks;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.junit.jupiter.api.Test;

class SksEndpointSelectorTest {

  private static final String SKS_URI = "urn:test:sks";
  private static final String OTHER_URI = "urn:test:other";

  private static final String BASIC256SHA256 = SecurityPolicy.Basic256Sha256.getUri();
  private static final String AES128_RSA_OAEP = SecurityPolicy.Aes128_Sha256_RsaOaep.getUri();

  @Test
  void filtersApplicationUriMismatch() {
    var match = endpoint("opc.tcp://a:4840", SKS_URI, MessageSecurityMode.SignAndEncrypt, 1);
    var mismatch = endpoint("opc.tcp://b:4840", OTHER_URI, MessageSecurityMode.SignAndEncrypt, 3);

    Optional<EndpointDescription> selected =
        SksEndpointSelector.selectEndpoint(List.of(mismatch, match), SKS_URI, null);

    assertEquals(Optional.of(match), selected);
  }

  @Test
  void filtersNonSignAndEncryptEndpoints() {
    var none = endpoint("opc.tcp://a:4840", SKS_URI, MessageSecurityMode.None, 0);
    var sign = endpoint("opc.tcp://a:4840/s", SKS_URI, MessageSecurityMode.Sign, 2);
    var signAndEncrypt =
        endpoint("opc.tcp://a:4840/se", SKS_URI, MessageSecurityMode.SignAndEncrypt, 1);

    Optional<EndpointDescription> selected =
        SksEndpointSelector.selectEndpoint(List.of(none, sign, signAndEncrypt), SKS_URI, null);

    assertEquals(Optional.of(signAndEncrypt), selected);
  }

  @Test
  void exactPolicyConstraintWinsOverSecurityLevel() {
    var higherLevel =
        endpoint("opc.tcp://a:4840/hi", SKS_URI, MessageSecurityMode.SignAndEncrypt, 10);
    var constrained =
        endpoint(
            "opc.tcp://a:4840/lo", SKS_URI, MessageSecurityMode.SignAndEncrypt, 1, AES128_RSA_OAEP);

    Optional<EndpointDescription> selected =
        SksEndpointSelector.selectEndpoint(
            List.of(higherLevel, constrained), SKS_URI, AES128_RSA_OAEP);

    assertEquals(Optional.of(constrained), selected);
  }

  @Test
  void unmatchedPolicyConstraintSelectsNothing() {
    var endpoint = endpoint("opc.tcp://a:4840", SKS_URI, MessageSecurityMode.SignAndEncrypt, 3);

    Optional<EndpointDescription> selected =
        SksEndpointSelector.selectEndpoint(
            List.of(endpoint), SKS_URI, "http://example.com/unmatched-policy");

    assertTrue(selected.isEmpty());
  }

  @Test
  void ranksBySecurityLevelWhenPolicyUnconstrained() {
    var low = endpoint("opc.tcp://a:4840/lo", SKS_URI, MessageSecurityMode.SignAndEncrypt, 1);
    var high = endpoint("opc.tcp://a:4840/hi", SKS_URI, MessageSecurityMode.SignAndEncrypt, 9);
    var mid = endpoint("opc.tcp://a:4840/mid", SKS_URI, MessageSecurityMode.SignAndEncrypt, 5);

    for (String constraint : new String[] {null, ""}) {
      Optional<EndpointDescription> selected =
          SksEndpointSelector.selectEndpoint(List.of(low, high, mid), SKS_URI, constraint);

      assertEquals(Optional.of(high), selected);
    }
  }

  @Test
  void emptyWhenNothingMatches() {
    var mismatch = endpoint("opc.tcp://b:4840", OTHER_URI, MessageSecurityMode.SignAndEncrypt, 3);

    assertTrue(SksEndpointSelector.selectEndpoint(List.of(mismatch), SKS_URI, null).isEmpty());
    assertTrue(SksEndpointSelector.selectEndpoint(List.of(), SKS_URI, null).isEmpty());
  }

  @Test
  void discoveryTargetsUseSpecUrls() {
    EndpointDescription entry =
        entry(new String[] {"opc.tcp://a:4840", null, "", "opc.tcp://b:4840"}, "opc.tcp://x:4840");

    SksEndpointSelector.DiscoveryTargets targets = SksEndpointSelector.discoveryTargets(entry);

    assertEquals(List.of("opc.tcp://a:4840", "opc.tcp://b:4840"), targets.urls());
    assertFalse(targets.fromEndpointUrlFallback());
  }

  @Test
  void discoveryTargetsFallBackToEndpointUrl() {
    EndpointDescription entry = entry(new String[] {null, ""}, "opc.tcp://x:4840");

    SksEndpointSelector.DiscoveryTargets targets = SksEndpointSelector.discoveryTargets(entry);

    assertEquals(List.of("opc.tcp://x:4840"), targets.urls());
    assertTrue(targets.fromEndpointUrlFallback());
  }

  @Test
  void discoveryTargetsEmptyWhenEntryProvidesNone() {
    EndpointDescription entry = entry(null, null);

    SksEndpointSelector.DiscoveryTargets targets = SksEndpointSelector.discoveryTargets(entry);

    assertTrue(targets.urls().isEmpty());
    assertFalse(targets.fromEndpointUrlFallback());
  }

  private static EndpointDescription endpoint(
      String url, String applicationUri, MessageSecurityMode securityMode, int securityLevel) {

    return endpoint(url, applicationUri, securityMode, securityLevel, BASIC256SHA256);
  }

  private static EndpointDescription endpoint(
      String url,
      String applicationUri,
      MessageSecurityMode securityMode,
      int securityLevel,
      String securityPolicyUri) {

    return new EndpointDescription(
        url,
        new ApplicationDescription(
            applicationUri,
            null,
            LocalizedText.NULL_VALUE,
            ApplicationType.Server,
            null,
            null,
            null),
        ByteString.NULL_VALUE,
        securityMode,
        securityPolicyUri,
        null,
        null,
        ubyte(securityLevel));
  }

  private static EndpointDescription entry(String[] discoveryUrls, String endpointUrl) {
    return new EndpointDescription(
        endpointUrl,
        new ApplicationDescription(
            SKS_URI,
            null,
            LocalizedText.NULL_VALUE,
            ApplicationType.Server,
            null,
            null,
            discoveryUrls),
        ByteString.NULL_VALUE,
        MessageSecurityMode.SignAndEncrypt,
        null,
        null,
        null,
        ubyte(0));
  }
}
