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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.stack.core.security.ChannelBoundSignatureData;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/** Policy advertisement order must not prevent a compatible certificate identity from signing. */
class X509PolicySelectionTest {

  private static KeyPair userKeys;
  private static X509Certificate userCertificate;
  private static ByteString serverCertificate;

  @BeforeAll
  static void createCertificates() throws Exception {
    userKeys = SelfSignedCertificateGenerator.generateNistP256KeyPair();
    userCertificate =
        SelfSignedCertificateBuilder.forEccApplicationCertificate(userKeys)
            .setCommonName("user")
            .setApplicationUri("urn:test:user")
            .build();
    KeyPair serverKeys = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    serverCertificate =
        ByteString.of(
            SelfSignedCertificateBuilder.forRsaApplicationCertificate(serverKeys)
                .setCommonName("server")
                .setApplicationUri("urn:test:server")
                .build()
                .getEncoded());
  }

  /** Includes a different EC curve, since a shared key algorithm alone is insufficient. */
  @ParameterizedTest
  @EnumSource(
      value = SecurityPolicy.class,
      names = {"Basic256Sha256", "ECC_nistP384_AesGcm"})
  void skipsIncompatiblePolicyAndProducesVerifiableSignature(SecurityPolicy incompatible)
      throws Exception {
    SecurityPolicy compatible = SecurityPolicy.ECC_nistP256_AesGcm;
    EndpointDescription endpoint =
        endpoint(
            SecurityPolicy.None,
            policy("incompatible", incompatible),
            policy("compatible", compatible));
    AtomicInteger privateKeyReads = new AtomicInteger();
    X509IdentityProvider provider =
        new X509IdentityProvider(
            userCertificate,
            () -> {
              privateKeyReads.incrementAndGet();
              return userKeys.getPrivate();
            });

    assertEquals(compatible, provider.getUserTokenSecurityPolicy(endpoint).orElseThrow());
    assertTrue(provider.getEnhancedUserTokenSecurityPolicy(endpoint).isEmpty());
    assertEquals(0, privateKeyReads.get(), "policy selection must not access the private key");

    ByteString serverNonce = NonceUtil.generateNonce(32);
    ByteString clientNonce = NonceUtil.generateNonce(32);
    var inputs =
        new ChannelSignatureInputs(
            ByteString.NULL_VALUE,
            clientNonce,
            serverCertificate,
            serverCertificate,
            ByteString.NULL_VALUE);
    SignedIdentityToken signed =
        provider.getIdentityToken(
            new IdentityProviderContext(endpoint, serverNonce, null, null, null, null, inputs));

    assertEquals("compatible", signed.getToken().getPolicyId());
    assertEquals(1, privateKeyReads.get());
    byte[] signedData =
        ChannelBoundSignatureData.userTokenSignatureData(
            compatible.getProfile(),
            false,
            ByteString.NULL_VALUE,
            serverNonce,
            serverCertificate,
            ByteString.NULL_VALUE,
            ByteString.NULL_VALUE,
            ByteString.NULL_VALUE,
            clientNonce);
    ChannelBoundSignatureData.verify(
        compatible, userCertificate, signedData, signed.getSignature());
  }

  @Test
  void certificatePolicyCanUseADifferentKeyAlgorithmFromTheChannel() throws Exception {
    KeyPair rsaKeys = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate rsaCertificate =
        SelfSignedCertificateBuilder.forRsaApplicationCertificate(rsaKeys)
            .setCommonName("rsa-user")
            .setApplicationUri("urn:test:rsa-user")
            .build();
    X509IdentityProvider provider = new X509IdentityProvider(rsaCertificate, rsaKeys.getPrivate());
    EndpointDescription endpoint =
        endpoint(
            SecurityPolicy.ECC_nistP256_AesGcm,
            policy("ecc", SecurityPolicy.ECC_nistP256_AesGcm),
            policy("rsa", SecurityPolicy.Basic256Sha256));
    ByteString nonce = NonceUtil.generateNonce(32);

    assertEquals(
        SecurityPolicy.Basic256Sha256, provider.getUserTokenSecurityPolicy(endpoint).orElseThrow());
    SignedIdentityToken signed = provider.getIdentityToken(endpoint, nonce);
    assertEquals("rsa", signed.getToken().getPolicyId());
    ChannelBoundSignatureData.verify(
        SecurityPolicy.Basic256Sha256,
        rsaCertificate,
        ChannelBoundSignatureData.legacyUserTokenSignatureData(serverCertificate, nonce),
        signed.getSignature());
  }

  @Test
  void failsSelectionWhenNoPolicyMatchesTheCertificate() {
    X509IdentityProvider provider =
        new X509IdentityProvider(userCertificate, userKeys.getPrivate());
    assertThrows(
        Exception.class,
        () ->
            provider.getUserTokenSecurityPolicy(
                endpoint(SecurityPolicy.None, policy("rsa", SecurityPolicy.Basic256Sha256))));
  }

  @Test
  void preservesAdvertisedOrderAmongCompatiblePolicies() throws Exception {
    X509IdentityProvider provider =
        new X509IdentityProvider(userCertificate, userKeys.getPrivate());
    SecurityPolicy first = SecurityPolicy.ECC_nistP256_ChaChaPoly;
    assertEquals(
        first,
        provider
            .getUserTokenSecurityPolicy(
                endpoint(
                    SecurityPolicy.None,
                    policy("first", first),
                    policy("second", SecurityPolicy.ECC_nistP256_AesGcm)))
            .orElseThrow());
  }

  @Test
  void inheritsChannelPolicyWhenTokenPolicyOmitsItsUri() throws Exception {
    X509IdentityProvider provider =
        new X509IdentityProvider(userCertificate, userKeys.getPrivate());
    SecurityPolicy channel = SecurityPolicy.ECC_nistP256_AesGcm;
    assertEquals(
        channel,
        provider
            .getUserTokenSecurityPolicy(
                endpoint(
                    channel,
                    new UserTokenPolicy("inherited", UserTokenType.Certificate, null, null, null)))
            .orElseThrow());
  }

  private static UserTokenPolicy policy(String id, SecurityPolicy policy) {
    return new UserTokenPolicy(id, UserTokenType.Certificate, null, null, policy.getUri());
  }

  private static EndpointDescription endpoint(SecurityPolicy channel, UserTokenPolicy... policies) {
    return new EndpointDescription(
        "opc.tcp://localhost:4840/test",
        new ApplicationDescription(
            "urn:test:server",
            "urn:test:product",
            LocalizedText.english("test"),
            ApplicationType.Server,
            null,
            null,
            null),
        serverCertificate,
        channel == SecurityPolicy.None
            ? MessageSecurityMode.None
            : MessageSecurityMode.SignAndEncrypt,
        channel.getUri(),
        policies,
        "http://opcfoundation.org/UA-Profile/Transport/uatcp-uasc-uabinary",
        ubyte(0));
  }
}
