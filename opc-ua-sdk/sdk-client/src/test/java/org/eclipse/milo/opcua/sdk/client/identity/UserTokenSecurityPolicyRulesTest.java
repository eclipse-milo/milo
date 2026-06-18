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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.Test;

class UserTokenSecurityPolicyRulesTest {

  // ---- requireSamePublicKeyAlgorithmAsChannel: Part 4 (7.41) public-key-family rule, shared by
  // username and certificate identity tokens ----

  @Test
  void rejectsExplicitRsaUserTokenPolicyOnEccChannel() {
    EndpointDescription endpoint =
        endpoint(SecurityPolicy.ECC_nistP256_AesGcm, MessageSecurityMode.SignAndEncrypt);

    UaException ex =
        assertThrows(
            UaException.class,
            () ->
                UserTokenSecurityPolicyRules.requireSamePublicKeyAlgorithmAsChannel(
                    endpoint, SecurityPolicy.Basic256Sha256, true));

    assertEquals(StatusCodes.Bad_SecurityPolicyRejected, ex.getStatusCode().getValue());
    assertTrue(ex.getMessage().contains("public-key algorithm"));
  }

  @Test
  void rejectsExplicitEccUserTokenPolicyOnRsaChannel() {
    EndpointDescription endpoint =
        endpoint(SecurityPolicy.Basic256Sha256, MessageSecurityMode.SignAndEncrypt);

    UaException ex =
        assertThrows(
            UaException.class,
            () ->
                UserTokenSecurityPolicyRules.requireSamePublicKeyAlgorithmAsChannel(
                    endpoint, SecurityPolicy.ECC_nistP256_AesGcm, true));

    assertEquals(StatusCodes.Bad_SecurityPolicyRejected, ex.getStatusCode().getValue());
    assertTrue(ex.getMessage().contains("public-key algorithm"));
  }

  // RSA-DH authenticates with RSA application certificates, so it shares the RSA public-key family
  // with a legacy RSA channel: an explicit, same-family policy is allowed.
  @Test
  void allowsExplicitRsaDhUserTokenPolicyOnRsaChannel() {
    EndpointDescription endpoint =
        endpoint(SecurityPolicy.Basic256Sha256, MessageSecurityMode.SignAndEncrypt);

    assertDoesNotThrow(
        () ->
            UserTokenSecurityPolicyRules.requireSamePublicKeyAlgorithmAsChannel(
                endpoint, SecurityPolicy.RSA_DH_AesGcm, true));
  }

  // An inherited (non-explicit) policy matches the channel by definition, so the rule does not run.
  @Test
  void allowsInheritedUserTokenPolicy() {
    EndpointDescription endpoint =
        endpoint(SecurityPolicy.ECC_nistP256_AesGcm, MessageSecurityMode.SignAndEncrypt);

    assertDoesNotThrow(
        () ->
            UserTokenSecurityPolicyRules.requireSamePublicKeyAlgorithmAsChannel(
                endpoint, SecurityPolicy.Basic256Sha256, false));
  }

  // An explicit SecurityPolicy None user-token policy carries no public-key algorithm (the
  // plaintext-secret case), so the same-algorithm rule does not constrain it.
  @Test
  void allowsExplicitNoneUserTokenPolicyOnSecuredChannel() {
    EndpointDescription endpoint =
        endpoint(SecurityPolicy.Basic256Sha256, MessageSecurityMode.SignAndEncrypt);

    assertDoesNotThrow(
        () ->
            UserTokenSecurityPolicyRules.requireSamePublicKeyAlgorithmAsChannel(
                endpoint, SecurityPolicy.None, true));
  }

  // The family rule applies only on a secured channel; on a None channel it does not run (the
  // enhanced-secret rule governs None channels instead).
  @Test
  void doesNotApplyFamilyRuleOnNoneChannel() {
    EndpointDescription endpoint = endpoint(SecurityPolicy.None, MessageSecurityMode.None);

    assertDoesNotThrow(
        () ->
            UserTokenSecurityPolicyRules.requireSamePublicKeyAlgorithmAsChannel(
                endpoint, SecurityPolicy.Basic256Sha256, true));
  }

  // ---- requireSecuredChannelForEnhancedSecret: Part 4 (7.41) enhanced-secret rule, for
  // encrypted-secret token types only ----

  @Test
  void rejectsEnhancedEccSecretOnNoneChannel() {
    EndpointDescription endpoint = endpoint(SecurityPolicy.None, MessageSecurityMode.None);

    UaException ex =
        assertThrows(
            UaException.class,
            () ->
                UserTokenSecurityPolicyRules.requireSecuredChannelForEnhancedSecret(
                    endpoint, SecurityPolicy.ECC_nistP256_AesGcm));

    assertEquals(StatusCodes.Bad_SecurityPolicyRejected, ex.getStatusCode().getValue());
    assertTrue(ex.getMessage().contains("None SecureChannel"));
  }

  // RSA-DH is enhanced too (it negotiates an ephemeral secret), so it is equally refused on a None
  // channel even though it reports the RSA public-key family.
  @Test
  void rejectsEnhancedRsaDhSecretOnNoneChannel() {
    EndpointDescription endpoint = endpoint(SecurityPolicy.None, MessageSecurityMode.None);

    UaException ex =
        assertThrows(
            UaException.class,
            () ->
                UserTokenSecurityPolicyRules.requireSecuredChannelForEnhancedSecret(
                    endpoint, SecurityPolicy.RSA_DH_AesGcm));

    assertEquals(StatusCodes.Bad_SecurityPolicyRejected, ex.getStatusCode().getValue());
    assertTrue(ex.getMessage().contains("None SecureChannel"));
  }

  // A legacy RSA secret on a None channel is permitted (Part 4 (7.41) allows it with a trusted
  // ServerCertificate); the enhanced-secret rule must not reject it.
  @Test
  void allowsLegacyRsaSecretOnNoneChannel() {
    EndpointDescription endpoint = endpoint(SecurityPolicy.None, MessageSecurityMode.None);

    assertDoesNotThrow(
        () ->
            UserTokenSecurityPolicyRules.requireSecuredChannelForEnhancedSecret(
                endpoint, SecurityPolicy.Basic256Sha256));
  }

  @Test
  void allowsEnhancedSecretOnSecuredChannel() {
    EndpointDescription endpoint =
        endpoint(SecurityPolicy.ECC_nistP256_AesGcm, MessageSecurityMode.SignAndEncrypt);

    assertDoesNotThrow(
        () ->
            UserTokenSecurityPolicyRules.requireSecuredChannelForEnhancedSecret(
                endpoint, SecurityPolicy.ECC_nistP256_AesGcm));
  }

  // ---- X509IdentityProvider wiring: the certificate provider applies the public-key-family rule,
  // but not the enhanced-secret rule (its token is signed, not encrypted) ----

  @Test
  void x509ProviderRejectsExplicitCrossFamilyCertificateTokenPolicy() throws Exception {
    X509IdentityProvider provider = rsaX509Provider();
    EndpointDescription endpoint =
        endpointWithCertificateToken(
            SecurityPolicy.Basic256Sha256,
            MessageSecurityMode.SignAndEncrypt,
            SecurityPolicy.ECC_nistP256_AesGcm.getUri());

    UaException ex =
        assertThrows(UaException.class, () -> provider.getUserTokenSecurityPolicy(endpoint));

    assertEquals(StatusCodes.Bad_SecurityPolicyRejected, ex.getStatusCode().getValue());
    assertTrue(ex.getMessage().contains("public-key algorithm"));
  }

  // The enhanced-secret rule must NOT be applied to certificate tokens: an enhanced X509 signature
  // is supported on a None channel (reduced layout), so the policy resolves rather than being
  // rejected the way an enhanced username secret would be.
  @Test
  void x509ProviderAllowsEnhancedCertificateTokenPolicyOnNoneChannel() throws Exception {
    X509IdentityProvider provider = rsaX509Provider();
    EndpointDescription endpoint =
        endpointWithCertificateToken(
            SecurityPolicy.None,
            MessageSecurityMode.None,
            SecurityPolicy.ECC_nistP256_AesGcm.getUri());

    assertEquals(
        SecurityPolicy.ECC_nistP256_AesGcm,
        provider.getUserTokenSecurityPolicy(endpoint).orElseThrow());
  }

  private static X509IdentityProvider rsaX509Provider() throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate certificate =
        new SelfSignedCertificateBuilder(keyPair)
            .setCommonName("client")
            .setApplicationUri("urn:eclipse:milo:test:client")
            .addDnsName("localhost")
            .build();

    return new X509IdentityProvider(certificate, keyPair.getPrivate());
  }

  private static EndpointDescription endpoint(
      SecurityPolicy channelPolicy, MessageSecurityMode securityMode) {

    return endpoint(channelPolicy, securityMode, new UserTokenPolicy[0]);
  }

  private static EndpointDescription endpointWithCertificateToken(
      SecurityPolicy channelPolicy, MessageSecurityMode securityMode, String tokenPolicyUri) {

    return endpoint(
        channelPolicy,
        securityMode,
        new UserTokenPolicy[] {
          new UserTokenPolicy("x509", UserTokenType.Certificate, null, null, tokenPolicyUri)
        });
  }

  private static EndpointDescription endpoint(
      SecurityPolicy channelPolicy,
      MessageSecurityMode securityMode,
      UserTokenPolicy[] userTokenPolicies) {

    ApplicationDescription server =
        new ApplicationDescription(
            "urn:eclipse:milo:test:server",
            "urn:eclipse:milo:test",
            null,
            ApplicationType.Server,
            null,
            null,
            null);

    return new EndpointDescription(
        "opc.tcp://localhost:12686/milo",
        server,
        ByteString.NULL_VALUE,
        securityMode,
        channelPolicy.getUri(),
        userTokenPolicies,
        Stack.TCP_UASC_UABINARY_TRANSPORT_URI,
        ubyte(0));
  }
}
