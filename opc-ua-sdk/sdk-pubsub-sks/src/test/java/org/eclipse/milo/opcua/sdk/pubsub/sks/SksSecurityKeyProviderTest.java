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
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;
import org.eclipse.milo.opcua.sdk.client.identity.IdentityProvider;
import org.eclipse.milo.opcua.sdk.client.identity.UsernameProvider;
import org.eclipse.milo.opcua.sdk.pubsub.security.InMemoryKeyCredentialStore;
import org.eclipse.milo.opcua.sdk.pubsub.security.KeyCredential;
import org.eclipse.milo.opcua.sdk.pubsub.security.KeyCredentialStore;
import org.eclipse.milo.opcua.sdk.pubsub.security.SecurityKeySet;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class SksSecurityKeyProviderTest {

  private static final String CLIENT_APP_URI = "urn:test:sks:client";
  private static final String POLICY_URI =
      "http://opcfoundation.org/UA/SecurityPolicy#PubSub-Aes256-CTR";

  private static final String SKS1_URI = "urn:test:sks:one";
  private static final String SKS2_URI = "urn:test:sks:two";
  private static final String SKS1_DISCOVERY_URL = "opc.tcp://sks1:4840";
  private static final String SKS2_DISCOVERY_URL = "opc.tcp://sks2:4840";
  private static final String SKS1_ENDPOINT_URL = "opc.tcp://sks1:4840/secure";
  private static final String SKS2_ENDPOINT_URL = "opc.tcp://sks2:4840/secure";

  private static final ByteString KEY_DATA = ByteString.of(new byte[68]);

  private static KeyPair clientKeyPair;
  private static X509Certificate clientCertificate;

  @BeforeAll
  static void generateClientCertificate() throws Exception {
    clientKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    clientCertificate =
        new SelfSignedCertificateBuilder(clientKeyPair)
            .setCommonName("SKS Provider Test Client")
            .setApplicationUri(CLIENT_APP_URI)
            .build();
  }

  @Test
  void fetchesKeysAndPassesSpiArgumentsVerbatim() throws Exception {
    ScriptedOps ops = new ScriptedOps();
    ops.scriptEntry(SKS1_DISCOVERY_URL, SKS1_URI, SKS1_ENDPOINT_URL);

    try (SksSecurityKeyProvider provider = builder(ops, serverEntry1()).build()) {
      SecurityKeySet keySet = provider.getKeys("Group1", uint(5), uint(3)).get(5, TimeUnit.SECONDS);

      assertEquals(POLICY_URI, keySet.securityPolicyUri());
      assertEquals(uint(1), keySet.firstTokenId());
      assertEquals(List.of(KEY_DATA), keySet.keys());
      assertEquals(Duration.ofSeconds(30), keySet.timeToNextKey());
      assertEquals(Duration.ofMinutes(1), keySet.keyLifetime());

      assertEquals(1, ops.requests.size());
      CallMethodRequest request = ops.requests.get(0);
      assertEquals(NodeIds.PublishSubscribe, request.getObjectId());
      assertEquals(NodeIds.PublishSubscribe_GetSecurityKeys, request.getMethodId());
      assertArrayEquals(
          new Variant[] {
            Variant.ofString("Group1"), Variant.ofUInt32(uint(5)), Variant.ofUInt32(uint(3))
          },
          request.getInputArguments());
    }
  }

  @Test
  void resolutionIsCachedAcrossFetches() throws Exception {
    ScriptedOps ops = new ScriptedOps();
    ops.scriptEntry(SKS1_DISCOVERY_URL, SKS1_URI, SKS1_ENDPOINT_URL);

    try (SksSecurityKeyProvider provider = builder(ops, serverEntry1()).build()) {
      provider.getKeys("Group1", uint(0), uint(1)).get(5, TimeUnit.SECONDS);
      provider.getKeys("Group1", uint(0), uint(1)).get(5, TimeUnit.SECONDS);

      assertEquals(List.of(SKS1_DISCOVERY_URL), ops.discoveryCalls);
      assertEquals(List.of(SKS1_ENDPOINT_URL, SKS1_ENDPOINT_URL), ops.connectCalls);
      assertEquals(2, ops.closedSessions.get());
    }
  }

  @Test
  void failsOverToNextEntryOnConnectFailure() throws Exception {
    ScriptedOps ops = new ScriptedOps();
    ops.scriptEntry(SKS1_DISCOVERY_URL, SKS1_URI, SKS1_ENDPOINT_URL);
    ops.scriptEntry(SKS2_DISCOVERY_URL, SKS2_URI, SKS2_ENDPOINT_URL);
    ops.failConnect.add(SKS1_ENDPOINT_URL);

    try (SksSecurityKeyProvider provider = builder(ops, serverEntry1(), serverEntry2()).build()) {
      SecurityKeySet keySet = provider.getKeys("Group1", uint(0), uint(1)).get(5, TimeUnit.SECONDS);

      assertEquals(uint(1), keySet.firstTokenId());
      assertEquals(List.of(SKS1_ENDPOINT_URL, SKS2_ENDPOINT_URL), ops.connectCalls);
    }
  }

  @Test
  void successfulEntryIsStickyPreferred() throws Exception {
    ScriptedOps ops = new ScriptedOps();
    ops.scriptEntry(SKS1_DISCOVERY_URL, SKS1_URI, SKS1_ENDPOINT_URL);
    ops.scriptEntry(SKS2_DISCOVERY_URL, SKS2_URI, SKS2_ENDPOINT_URL);
    ops.failConnect.add(SKS1_ENDPOINT_URL);

    try (SksSecurityKeyProvider provider = builder(ops, serverEntry1(), serverEntry2()).build()) {
      provider.getKeys("Group1", uint(0), uint(1)).get(5, TimeUnit.SECONDS);
      provider.getKeys("Group1", uint(0), uint(1)).get(5, TimeUnit.SECONDS);

      // The second fetch goes straight to the previously successful entry; entry 0 is not
      // re-attempted while entry 1 keeps working.
      assertEquals(
          List.of(SKS1_ENDPOINT_URL, SKS2_ENDPOINT_URL, SKS2_ENDPOINT_URL), ops.connectCalls);
    }
  }

  @Test
  void failedEntryResolutionIsInvalidatedAndRetried() throws Exception {
    ScriptedOps ops = new ScriptedOps();
    ops.scriptEntry(SKS1_DISCOVERY_URL, SKS1_URI, SKS1_ENDPOINT_URL);
    ops.scriptEntry(SKS2_DISCOVERY_URL, SKS2_URI, SKS2_ENDPOINT_URL);
    ops.failConnect.add(SKS1_ENDPOINT_URL);

    try (SksSecurityKeyProvider provider = builder(ops, serverEntry1(), serverEntry2()).build()) {
      // Fetch 1: entry 0 fails (its cached resolution is invalidated), entry 1 succeeds.
      provider.getKeys("Group1", uint(0), uint(1)).get(5, TimeUnit.SECONDS);

      // Entry 1 now fails; entry 0 recovers.
      ops.failConnect.remove(SKS1_ENDPOINT_URL);
      ops.failConnect.add(SKS2_ENDPOINT_URL);

      // Fetch 2: preferred entry 1 fails over to entry 0, which must be re-resolved.
      provider.getKeys("Group1", uint(0), uint(1)).get(5, TimeUnit.SECONDS);

      assertEquals(
          List.of(SKS1_DISCOVERY_URL, SKS2_DISCOVERY_URL, SKS1_DISCOVERY_URL), ops.discoveryCalls);
      // Entry 1's resolution from fetch 1 was still cached when fetch 2 tried it first.
      assertEquals(
          List.of(SKS1_ENDPOINT_URL, SKS2_ENDPOINT_URL, SKS2_ENDPOINT_URL, SKS1_ENDPOINT_URL),
          ops.connectCalls);
    }
  }

  @Test
  void badNotFoundIsFatalWithoutFailover() throws Exception {
    ScriptedOps ops = new ScriptedOps();
    ops.scriptEntry(SKS1_DISCOVERY_URL, SKS1_URI, SKS1_ENDPOINT_URL);
    ops.scriptEntry(SKS2_DISCOVERY_URL, SKS2_URI, SKS2_ENDPOINT_URL);
    ops.callResults.put(
        SKS1_ENDPOINT_URL,
        new CallMethodResult(new StatusCode(StatusCodes.Bad_NotFound), null, null, null));

    try (SksSecurityKeyProvider provider = builder(ops, serverEntry1(), serverEntry2()).build()) {
      ExecutionException e =
          assertThrows(
              ExecutionException.class,
              () -> provider.getKeys("NoSuchGroup", uint(0), uint(1)).get(5, TimeUnit.SECONDS));

      UaException cause = assertInstanceOf(UaException.class, e.getCause());
      assertEquals(StatusCodes.Bad_NotFound, cause.getStatusCode().value());
      assertTrue(cause.getMessage().contains("NoSuchGroup"), cause.getMessage());

      // No failover: the second entry was never resolved or contacted.
      assertEquals(List.of(SKS1_ENDPOINT_URL), ops.connectCalls);
      assertEquals(List.of(SKS1_DISCOVERY_URL), ops.discoveryCalls);
    }
  }

  @Test
  void badUserAccessDeniedFailsOverToNextEntry() throws Exception {
    ScriptedOps ops = new ScriptedOps();
    ops.scriptEntry(SKS1_DISCOVERY_URL, SKS1_URI, SKS1_ENDPOINT_URL);
    ops.scriptEntry(SKS2_DISCOVERY_URL, SKS2_URI, SKS2_ENDPOINT_URL);
    ops.callResults.put(
        SKS1_ENDPOINT_URL,
        new CallMethodResult(new StatusCode(StatusCodes.Bad_UserAccessDenied), null, null, null));

    try (SksSecurityKeyProvider provider = builder(ops, serverEntry1(), serverEntry2()).build()) {
      SecurityKeySet keySet = provider.getKeys("Group1", uint(0), uint(1)).get(5, TimeUnit.SECONDS);

      assertEquals(uint(1), keySet.firstTokenId());
      assertEquals(List.of(SKS1_ENDPOINT_URL, SKS2_ENDPOINT_URL), ops.connectCalls);
    }
  }

  @Test
  void exhaustedEntriesAggregateFailures() throws Exception {
    ScriptedOps ops = new ScriptedOps();
    ops.scriptEntry(SKS1_DISCOVERY_URL, SKS1_URI, SKS1_ENDPOINT_URL);
    ops.scriptEntry(SKS2_DISCOVERY_URL, SKS2_URI, SKS2_ENDPOINT_URL);
    ops.failConnect.add(SKS1_ENDPOINT_URL);
    ops.failConnect.add(SKS2_ENDPOINT_URL);

    try (SksSecurityKeyProvider provider = builder(ops, serverEntry1(), serverEntry2()).build()) {
      ExecutionException e =
          assertThrows(
              ExecutionException.class,
              () -> provider.getKeys("Group1", uint(0), uint(1)).get(5, TimeUnit.SECONDS));

      UaException cause = assertInstanceOf(UaException.class, e.getCause());
      assertTrue(cause.getMessage().contains("securityKeyServices[0]"), cause.getMessage());
      assertTrue(cause.getMessage().contains("securityKeyServices[1]"), cause.getMessage());
      assertTrue(cause.getMessage().contains(SKS1_URI), cause.getMessage());
      assertTrue(cause.getMessage().contains(SKS2_URI), cause.getMessage());
    }
  }

  @Test
  void toleranceFallbackUsesEndpointUrlAsDiscoveryTarget() throws Exception {
    ScriptedOps ops = new ScriptedOps();
    String legacyUrl = "opc.tcp://legacy:4840";
    ops.scriptEntry(legacyUrl, SKS1_URI, SKS1_ENDPOINT_URL);

    EndpointDescription entry = entry(SKS1_URI, null, legacyUrl, (UserTokenPolicy[]) null);

    try (SksSecurityKeyProvider provider = builder(ops, entry).build()) {
      SecurityKeySet keySet = provider.getKeys("Group1", uint(0), uint(1)).get(5, TimeUnit.SECONDS);

      assertEquals(uint(1), keySet.firstTokenId());
      assertEquals(List.of(legacyUrl), ops.discoveryCalls);
    }
  }

  @Test
  void toleranceFallbackRetriesWithDiscoverySuffix() throws Exception {
    ScriptedOps ops = new ScriptedOps();
    String legacyUrl = "opc.tcp://legacy:4840";
    // GetEndpoints only answers at the Part 6 "/discovery" suffix.
    ops.scriptEntry(legacyUrl + "/discovery", SKS1_URI, SKS1_ENDPOINT_URL);

    EndpointDescription entry = entry(SKS1_URI, null, legacyUrl, (UserTokenPolicy[]) null);

    try (SksSecurityKeyProvider provider = builder(ops, entry).build()) {
      SecurityKeySet keySet = provider.getKeys("Group1", uint(0), uint(1)).get(5, TimeUnit.SECONDS);

      assertEquals(uint(1), keySet.firstTokenId());
      assertEquals(List.of(legacyUrl, legacyUrl + "/discovery"), ops.discoveryCalls);
    }
  }

  @Test
  void strictModeRejectsEndpointUrlOnlyEntriesAtBuild() {
    ScriptedOps ops = new ScriptedOps();
    EndpointDescription entry =
        entry(SKS1_URI, null, "opc.tcp://legacy:4840", (UserTokenPolicy[]) null);

    UaException e =
        assertThrows(UaException.class, () -> builder(ops, entry).toleranceFallback(false).build());

    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
    assertTrue(e.getMessage().contains("tolerance fallback"), e.getMessage());
  }

  @Test
  void clientTypedEntriesAreSkippedForPull() throws Exception {
    ScriptedOps ops = new ScriptedOps();
    ops.scriptEntry(SKS1_DISCOVERY_URL, SKS1_URI, SKS1_ENDPOINT_URL);

    EndpointDescription pushEntry = clientEntry();

    try (SksSecurityKeyProvider provider = builder(ops, pushEntry, serverEntry1()).build()) {
      SecurityKeySet keySet = provider.getKeys("Group1", uint(0), uint(1)).get(5, TimeUnit.SECONDS);

      assertEquals(uint(1), keySet.firstTokenId());
      assertEquals(List.of(SKS1_ENDPOINT_URL), ops.connectCalls);
    }
  }

  @Test
  void buildValidatesEntriesAgainstTable40() {
    // ClientAndServer is "Invalid value" per Table 40. The shared SecurityKeyServicesValidator
    // runs at build(), so a hard-invalid entry fails construction, not the first fetch.
    var invalid =
        new EndpointDescription(
            null,
            new ApplicationDescription(
                SKS1_URI,
                null,
                LocalizedText.NULL_VALUE,
                ApplicationType.ClientAndServer,
                null,
                null,
                new String[] {SKS1_DISCOVERY_URL}),
            ByteString.NULL_VALUE,
            MessageSecurityMode.SignAndEncrypt,
            null,
            null,
            null,
            ubyte(0));

    UaException e =
        assertThrows(
            UaException.class, () -> builder(new ScriptedOps(), invalid, serverEntry1()).build());

    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
    assertTrue(e.getMessage().contains("securityKeyServices[0]"), e.getMessage());
    assertTrue(e.getMessage().contains("ApplicationType"), e.getMessage());
  }

  @Test
  void buildFailsFastOnServerEntryWithoutApplicationUri() {
    EndpointDescription invalid =
        entry(null, new String[] {SKS1_DISCOVERY_URL}, null, (UserTokenPolicy[]) null);

    UaException e =
        assertThrows(UaException.class, () -> builder(new ScriptedOps(), invalid).build());

    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
    assertTrue(e.getMessage().contains("server.applicationUri"), e.getMessage());
  }

  @Test
  void buildFailsWithoutServerTypedEntries() {
    UaException e =
        assertThrows(UaException.class, () -> builder(new ScriptedOps(), clientEntry()).build());

    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
    assertTrue(e.getMessage().contains("no Server-typed"), e.getMessage());
  }

  @Test
  void buildRequiresExplicitCertificateValidator() {
    UaException e =
        assertThrows(
            UaException.class,
            () ->
                SksSecurityKeyProvider.builder()
                    .keyServices(List.of(serverEntry1()))
                    .keyPair(clientKeyPair)
                    .certificate(clientCertificate)
                    .applicationUri(CLIENT_APP_URI)
                    .build());

    assertEquals(StatusCodes.Bad_ConfigurationError, e.getStatusCode().value());
    assertTrue(e.getMessage().contains("certificateValidator"), e.getMessage());
  }

  @Test
  void buildRequiresKeyPairAndCertificateAndApplicationUriAndEntries() {
    assertThrows(UaException.class, () -> SksSecurityKeyProvider.builder().build());

    assertThrows(
        UaException.class,
        () ->
            SksSecurityKeyProvider.builder()
                .keyServices(List.of(serverEntry1()))
                .certificate(clientCertificate)
                .applicationUri(CLIENT_APP_URI)
                .certificateValidator(new CertificateValidator.InsecureCertificateValidator())
                .build());

    assertThrows(
        UaException.class,
        () ->
            SksSecurityKeyProvider.builder()
                .keyServices(List.of(serverEntry1()))
                .keyPair(clientKeyPair)
                .applicationUri(CLIENT_APP_URI)
                .certificateValidator(new CertificateValidator.InsecureCertificateValidator())
                .build());

    assertThrows(
        UaException.class,
        () ->
            SksSecurityKeyProvider.builder()
                .keyServices(List.of(serverEntry1()))
                .keyPair(clientKeyPair)
                .certificate(clientCertificate)
                .certificateValidator(new CertificateValidator.InsecureCertificateValidator())
                .build());
  }

  @Test
  void closedProviderRejectsFetches() throws Exception {
    ScriptedOps ops = new ScriptedOps();
    ops.scriptEntry(SKS1_DISCOVERY_URL, SKS1_URI, SKS1_ENDPOINT_URL);

    SksSecurityKeyProvider provider = builder(ops, serverEntry1()).build();
    provider.close();

    ExecutionException e =
        assertThrows(
            ExecutionException.class,
            () -> provider.getKeys("Group1", uint(0), uint(1)).get(5, TimeUnit.SECONDS));

    UaException cause = assertInstanceOf(UaException.class, e.getCause());
    assertEquals(StatusCodes.Bad_InvalidState, cause.getStatusCode().value());
  }

  @Test
  void endpointTransformIsAppliedBeforeConnectAndCache() throws Exception {
    ScriptedOps ops = new ScriptedOps();
    ops.scriptEntry(SKS1_DISCOVERY_URL, SKS1_URI, SKS1_ENDPOINT_URL);

    String transformedUrl = "opc.tcp://localhost:4840/secure";
    ops.callResults.put(transformedUrl, goodResult());

    UnaryOperator<EndpointDescription> transform =
        e ->
            new EndpointDescription(
                transformedUrl,
                e.getServer(),
                e.getServerCertificate(),
                e.getSecurityMode(),
                e.getSecurityPolicyUri(),
                e.getUserIdentityTokens(),
                e.getTransportProfileUri(),
                e.getSecurityLevel());

    try (SksSecurityKeyProvider provider =
        builder(ops, serverEntry1()).endpointTransform(transform).build()) {

      provider.getKeys("Group1", uint(0), uint(1)).get(5, TimeUnit.SECONDS);
      provider.getKeys("Group1", uint(0), uint(1)).get(5, TimeUnit.SECONDS);

      assertEquals(List.of(transformedUrl, transformedUrl), ops.connectCalls);
    }
  }

  @Test
  void usernameIdentityIsSelectedAndSecretWipedAfterFetch() throws Exception {
    ScriptedOps ops = new ScriptedOps();
    UserTokenPolicy usernamePolicy =
        new UserTokenPolicy("username", UserTokenType.UserName, null, null, null);
    ops.scriptEntry(SKS1_DISCOVERY_URL, SKS1_URI, SKS1_ENDPOINT_URL, usernamePolicy);

    EndpointDescription entry =
        entry(SKS1_URI, new String[] {SKS1_DISCOVERY_URL}, null, usernamePolicy);

    // A store that hands out a known array so the provider's wipe is observable.
    char[] handedOutSecret = "password1".toCharArray();
    KeyCredentialStore observableStore =
        resourceUri -> Optional.of(new KeyCredential("user1", handedOutSecret));

    try (SksSecurityKeyProvider provider =
        builder(ops, entry).keyCredentialStore(observableStore).build()) {

      provider.getKeys("Group1", uint(0), uint(1)).get(5, TimeUnit.SECONDS);

      assertInstanceOf(UsernameProvider.class, ops.identities.get(0));
      // The provider wipes the borrowed secret once the session attempt is over.
      assertArrayEquals(new char[handedOutSecret.length], handedOutSecret);
    }
  }

  @Test
  void identityNonIntersectionFailsTheEntryAndFailsOver() throws Exception {
    ScriptedOps ops = new ScriptedOps();
    // Entry 0 lists UserName only, but its discovered endpoint offers Anonymous only: no
    // intersection means the ENTRY fails (K12 — never silently downgrade past the list), before
    // any connect, and the fetch fails over to entry 1 in array order.
    ops.scriptEntry(SKS1_DISCOVERY_URL, SKS1_URI, SKS1_ENDPOINT_URL);
    ops.scriptEntry(SKS2_DISCOVERY_URL, SKS2_URI, SKS2_ENDPOINT_URL);

    EndpointDescription entry1 =
        entry(
            SKS1_URI,
            new String[] {SKS1_DISCOVERY_URL},
            null,
            new UserTokenPolicy("username", UserTokenType.UserName, null, null, null));

    // A credential IS available; the failure is strictly the endpoint not offering UserName.
    var store = new InMemoryKeyCredentialStore();
    store.put(SKS1_URI, "user1", "password1".toCharArray());

    try (SksSecurityKeyProvider provider =
        builder(ops, entry1, serverEntry2()).keyCredentialStore(store).build()) {

      SecurityKeySet keySet = provider.getKeys("Group1", uint(0), uint(1)).get(5, TimeUnit.SECONDS);

      assertEquals(uint(1), keySet.firstTokenId());
      // Entry 0 failed at identity selection, so only entry 1 was ever connected to.
      assertEquals(List.of(SKS2_ENDPOINT_URL), ops.connectCalls);
      assertEquals(List.of(SKS1_DISCOVERY_URL, SKS2_DISCOVERY_URL), ops.discoveryCalls);
    }
  }

  private static SksSecurityKeyProvider.Builder builder(
      ScriptedOps ops, EndpointDescription... entries) {

    return SksSecurityKeyProvider.builder()
        .keyServices(List.of(entries))
        .keyPair(clientKeyPair)
        .certificate(clientCertificate)
        .applicationUri(CLIENT_APP_URI)
        .certificateValidator(new CertificateValidator.InsecureCertificateValidator())
        .clientOperations(ops);
  }

  private static EndpointDescription serverEntry1() {
    return entry(SKS1_URI, new String[] {SKS1_DISCOVERY_URL}, null, (UserTokenPolicy[]) null);
  }

  private static EndpointDescription serverEntry2() {
    return entry(SKS2_URI, new String[] {SKS2_DISCOVERY_URL}, null, (UserTokenPolicy[]) null);
  }

  private static EndpointDescription clientEntry() {
    return new EndpointDescription(
        null,
        new ApplicationDescription(
            "urn:test:sks:push",
            null,
            LocalizedText.NULL_VALUE,
            ApplicationType.Client,
            null,
            null,
            null),
        ByteString.NULL_VALUE,
        MessageSecurityMode.SignAndEncrypt,
        null,
        null,
        null,
        ubyte(0));
  }

  private static EndpointDescription entry(
      String applicationUri,
      String[] discoveryUrls,
      String endpointUrl,
      UserTokenPolicy... userIdentityTokens) {

    return new EndpointDescription(
        endpointUrl,
        new ApplicationDescription(
            applicationUri,
            null,
            LocalizedText.NULL_VALUE,
            ApplicationType.Server,
            null,
            null,
            discoveryUrls),
        ByteString.NULL_VALUE,
        MessageSecurityMode.SignAndEncrypt,
        null,
        userIdentityTokens,
        null,
        ubyte(0));
  }

  private static EndpointDescription discoveredEndpoint(
      String endpointUrl, String applicationUri, UserTokenPolicy... tokenPolicies) {

    return new EndpointDescription(
        endpointUrl,
        new ApplicationDescription(
            applicationUri,
            null,
            LocalizedText.NULL_VALUE,
            ApplicationType.Server,
            null,
            null,
            null),
        ByteString.NULL_VALUE,
        MessageSecurityMode.SignAndEncrypt,
        "http://opcfoundation.org/UA/SecurityPolicy#Basic256Sha256",
        tokenPolicies.length == 0
            ? new UserTokenPolicy[] {
              new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null)
            }
            : tokenPolicies,
        null,
        ubyte(3));
  }

  private static CallMethodResult goodResult() {
    return new CallMethodResult(
        StatusCode.GOOD,
        null,
        null,
        new Variant[] {
          Variant.ofString(POLICY_URI),
          Variant.ofUInt32(uint(1)),
          Variant.ofByteStringArray(new ByteString[] {KEY_DATA}),
          Variant.ofDouble(30_000.0),
          Variant.ofDouble(60_000.0)
        });
  }

  /** Scripted {@link SksClientOperations}: discovery keyed by URL, call results by endpoint URL. */
  private static final class ScriptedOps implements SksClientOperations {

    final Map<String, List<EndpointDescription>> endpointsByDiscoveryUrl = new HashMap<>();
    final Map<String, CallMethodResult> callResults = new HashMap<>();
    final Set<String> failConnect = new HashSet<>();

    final List<String> discoveryCalls = new ArrayList<>();
    final List<String> connectCalls = new ArrayList<>();
    final List<IdentityProvider> identities = new ArrayList<>();
    final List<CallMethodRequest> requests = new ArrayList<>();
    final AtomicInteger closedSessions = new AtomicInteger();

    /** Script a healthy entry: discovery at {@code discoveryUrl} and a Good call result. */
    void scriptEntry(
        String discoveryUrl,
        String applicationUri,
        String endpointUrl,
        UserTokenPolicy... tokenPolicies) {

      endpointsByDiscoveryUrl.put(
          discoveryUrl, List.of(discoveredEndpoint(endpointUrl, applicationUri, tokenPolicies)));
      callResults.putIfAbsent(endpointUrl, goodResult());
    }

    @Override
    public List<EndpointDescription> getEndpoints(String discoveryUrl) throws UaException {
      discoveryCalls.add(discoveryUrl);
      List<EndpointDescription> endpoints = endpointsByDiscoveryUrl.get(discoveryUrl);
      if (endpoints == null) {
        throw new UaException(StatusCodes.Bad_CommunicationError, "no route to " + discoveryUrl);
      }
      return endpoints;
    }

    @Override
    public SksSession connect(
        EndpointDescription endpoint,
        List<EndpointDescription> discoveredEndpoints,
        IdentityProvider identityProvider)
        throws UaException {

      String endpointUrl = endpoint.getEndpointUrl();
      connectCalls.add(endpointUrl);
      identities.add(identityProvider);

      if (failConnect.contains(endpointUrl)) {
        throw new UaException(
            StatusCodes.Bad_CommunicationError, "connection refused: " + endpointUrl);
      }

      CallMethodResult result = callResults.get(endpointUrl);

      return new SksSession() {
        @Override
        public CallMethodResult call(CallMethodRequest request) throws UaException {
          requests.add(request);
          if (result == null) {
            throw new UaException(
                StatusCodes.Bad_UnexpectedError, "no scripted result for " + endpointUrl);
          }
          return result;
        }

        @Override
        public void close() {
          closedSessions.incrementAndGet();
        }
      };
    }
  }
}
