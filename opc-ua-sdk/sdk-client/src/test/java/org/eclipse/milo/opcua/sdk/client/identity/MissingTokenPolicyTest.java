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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.AnonymousIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Named;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * An endpoint whose user-token policies include none of a provider's kind is rejected with {@code
 * Bad_IdentityTokenRejected}, the same way by every built-in provider.
 *
 * <p>Part 4 §7.14: the Client shall pass one of the endpoint's user identity tokens in
 * ActivateSession. A provider finds that it has none to pass before anything is sent. A caller can
 * tell that mismatch from an unexpected failure only by its StatusCode, which {@code
 * OpcUaClient.connect()} reports, so a plain {@link Exception} there reads as {@code
 * Bad_UnexpectedError}.
 */
class MissingTokenPolicyTest {

  private static final ByteString SERVER_NONCE = NonceUtil.generateNonce(32);

  /** An endpoint that offers an IssuedToken policy alone, which no built-in provider can use. */
  private static final EndpointDescription ISSUED_TOKEN_ONLY =
      endpoint(new UserTokenPolicy("issued", UserTokenType.IssuedToken, null, null, null));

  private static X509Certificate userCertificate;
  private static KeyPair userKeyPair;

  @BeforeAll
  static void createUserCertificate() throws Exception {
    userKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    userCertificate =
        new SelfSignedCertificateBuilder(userKeyPair)
            .setCommonName("user")
            .setApplicationUri("urn:eclipse:milo:test:user")
            .build();
  }

  // Each entry point the SDK calls during CreateSession or ActivateSession can be the first to find
  // the mismatch, so each must report it the same way.
  @ParameterizedTest(name = "{0}.{1}")
  @MethodSource("providersAndEntryPoints")
  void providerRejectsEndpointWithoutItsTokenType(
      Supplier<IdentityProvider> provider, EntryPoint entryPoint) {

    UaException exception =
        assertThrows(UaException.class, () -> entryPoint.call(provider.get(), ISSUED_TOKEN_ONLY));

    assertEquals(
        StatusCodes.Bad_IdentityTokenRejected,
        exception.getStatusCode().value(),
        "unexpected status: " + exception.getStatusCode());
  }

  // A CompositeProvider with no providers has no token to offer for any endpoint.
  @Test
  void emptyCompositeProviderRejectsIdentityTokenRequests() {
    var provider = new CompositeProvider(List.of());

    UaException legacy =
        assertThrows(
            UaException.class, () -> provider.getIdentityToken(ISSUED_TOKEN_ONLY, SERVER_NONCE));
    UaException contextual =
        assertThrows(
            UaException.class,
            () ->
                provider.getIdentityToken(
                    new IdentityProviderContext(ISSUED_TOKEN_ONLY, SERVER_NONCE)));

    assertEquals(StatusCodes.Bad_IdentityTokenRejected, legacy.getStatusCode().value());
    assertEquals(StatusCodes.Bad_IdentityTokenRejected, contextual.getStatusCode().value());
  }

  // The rejection is how a CompositeProvider learns to try its next provider; it must still do so.
  @Test
  void compositeProviderFallsThroughRejectingProviderToNextOne() throws Exception {
    EndpointDescription anonymousOnly =
        endpoint(new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null));

    CompositeProvider provider =
        CompositeProvider.of(new UsernameProvider("user", "password"), new AnonymousProvider());

    SignedIdentityToken token =
        provider.getIdentityToken(new IdentityProviderContext(anonymousOnly, SERVER_NONCE));

    assertInstanceOf(AnonymousIdentityToken.class, token.getToken());
    assertEquals("anonymous", token.getToken().getPolicyId());
  }

  private static Stream<Arguments> providersAndEntryPoints() {
    List<Named<Supplier<IdentityProvider>>> providers =
        List.of(
            Named.of("AnonymousProvider", AnonymousProvider::new),
            Named.of("UsernameProvider", () -> new UsernameProvider("user", "password")),
            Named.of(
                "X509IdentityProvider",
                () -> new X509IdentityProvider(userCertificate, userKeyPair.getPrivate())),
            Named.of(
                "CompositeProvider",
                () ->
                    CompositeProvider.of(
                        new UsernameProvider("user", "password"), new AnonymousProvider())));

    List<Named<EntryPoint>> entryPoints =
        List.of(
            Named.of("getUserTokenSecurityPolicy", IdentityProvider::getUserTokenSecurityPolicy),
            Named.of(
                "getEnhancedUserTokenSecurityPolicy",
                IdentityProvider::getEnhancedUserTokenSecurityPolicy),
            Named.of(
                "getCreateSessionAdditionalHeader",
                (p, e) -> p.getCreateSessionAdditionalHeader(DefaultEncodingContext.INSTANCE, e)),
            Named.of(
                "getIdentityToken(endpoint, serverNonce)",
                (p, e) -> p.getIdentityToken(e, SERVER_NONCE)),
            Named.of(
                "getIdentityToken(context)",
                (p, e) -> p.getIdentityToken(new IdentityProviderContext(e, SERVER_NONCE))));

    return providers.stream()
        .flatMap(
            provider -> entryPoints.stream().map(entryPoint -> arguments(provider, entryPoint)));
  }

  private static EndpointDescription endpoint(UserTokenPolicy... userTokenPolicies) {
    return new EndpointDescription(
        "opc.tcp://localhost:4840/milo",
        null,
        ByteString.NULL_VALUE,
        MessageSecurityMode.None,
        SecurityPolicy.None.getUri(),
        userTokenPolicies,
        Stack.TCP_UASC_UABINARY_TRANSPORT_URI,
        ubyte(0));
  }

  /** One of the {@link IdentityProvider} methods the SDK calls for an endpoint. */
  @FunctionalInterface
  private interface EntryPoint {
    Object call(IdentityProvider provider, EndpointDescription endpoint) throws Exception;
  }
}
