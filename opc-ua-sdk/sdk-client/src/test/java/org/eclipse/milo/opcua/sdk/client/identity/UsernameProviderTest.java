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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.EccEncryptedSecret;
import org.eclipse.milo.opcua.stack.core.security.EccUserTokenAdditionalHeader;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserNameIdentityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class UsernameProviderTest {

  private static final ByteString SERVER_NONCE =
      ByteString.of(
          new byte[] {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
            0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
            0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17,
            0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F
          });

  // Enhanced username tokens must use the receiver key negotiated in CreateSession.
  @ParameterizedTest
  @MethodSource("representativeEnhancedProfiles")
  void encryptsEnhancedPasswordWithNegotiatedReceiverKey(SecurityPolicyProfile profile)
      throws Exception {

    ApplicationIdentity clientIdentity = applicationIdentity(profile);
    KeyPair receiverEphemeralKeyPair = EccEncryptedSecret.generateEphemeralKeyPair(profile);
    ByteString receiverPublicKey =
        EccEncryptedSecret.encodeEphemeralPublicKey(profile, receiverEphemeralKeyPair.getPublic());

    EndpointDescription endpoint = endpoint(profile.securityPolicy(), clientIdentity.certificate());
    IdentityProviderContext context =
        new IdentityProviderContext(
            endpoint,
            SERVER_NONCE,
            receiverPublicKey,
            clientIdentity.keyPair(),
            new X509Certificate[] {clientIdentity.certificate()});

    SignedIdentityToken signedIdentityToken =
        new UsernameProvider("user", "password").getIdentityToken(context);

    UserNameIdentityToken token = (UserNameIdentityToken) signedIdentityToken.getToken();

    // UA Part 4, Table 188: enhanced (SecureChannelEnhancement) username tokens carry a null
    // encryptionAlgorithm; the EccEncryptedSecret format already identifies the encryption used.
    assertNull(token.getEncryptionAlgorithm());
    assertEquals(
        ByteString.of("password".getBytes(StandardCharsets.UTF_8)),
        EccEncryptedSecret.decrypt(
            profile,
            receiverEphemeralKeyPair,
            receiverPublicKey,
            clientIdentity.certificate(),
            SERVER_NONCE,
            token.getPassword()));
  }

  // Existing RSA username-token deployments must keep the legacy password layout and algorithm.
  @Test
  void preservesRsaUsernameTokenEncryption() throws Exception {
    ApplicationIdentity serverIdentity = rsaApplicationIdentity();
    EndpointDescription endpoint =
        endpoint(SecurityPolicy.Basic256Sha256, serverIdentity.certificate());

    SignedIdentityToken signedIdentityToken =
        new UsernameProvider("user", "password").getIdentityToken(endpoint, SERVER_NONCE);

    UserNameIdentityToken token = (UserNameIdentityToken) signedIdentityToken.getToken();

    assertEquals(
        SecurityPolicy.Basic256Sha256.getAsymmetricEncryptionAlgorithm().getUri(),
        token.getEncryptionAlgorithm());
    assertNotEquals(
        ByteString.of("password".getBytes(StandardCharsets.UTF_8)), token.getPassword());
  }

  // Multiple username policies are legal; the chosen policy drives the CreateSession request.
  @Test
  void createSessionHeaderUsesSelectedUsernameTokenPolicy() throws Exception {
    ApplicationIdentity serverIdentity =
        applicationIdentity(SecurityPolicy.ECC_nistP256_AesGcm.getProfile());
    EndpointDescription endpoint =
        endpoint(
            SecurityPolicy.ECC_nistP256_AesGcm,
            serverIdentity.certificate(),
            new UserTokenPolicy[] {
              new UserTokenPolicy(
                  "nist",
                  UserTokenType.UserName,
                  null,
                  null,
                  SecurityPolicy.ECC_nistP256_AesGcm.getUri()),
              new UserTokenPolicy(
                  "x25519",
                  UserTokenType.UserName,
                  null,
                  null,
                  SecurityPolicy.ECC_curve25519_ChaChaPoly.getUri())
            });

    UsernameProvider provider =
        new UsernameProvider(
            "user",
            "password",
            new CertificateValidator.InsecureCertificateValidator(),
            tokenPolicies -> tokenPolicies.get(1));

    assertEquals(
        new EccUserTokenAdditionalHeader.NegotiationRequest.Supported(
            SecurityPolicy.ECC_curve25519_ChaChaPoly),
        EccUserTokenAdditionalHeader.decodeRequest(
            DefaultEncodingContext.INSTANCE,
            provider.getCreateSessionAdditionalHeader(DefaultEncodingContext.INSTANCE, endpoint)));
  }

  // Certificate-token auth does not need the username-secret enhanced negotiation header.
  @Test
  void x509ProviderDoesNotRequestEnhancedUsernameTokenHeaderForCertificatePolicy()
      throws Exception {
    ApplicationIdentity identity =
        applicationIdentity(SecurityPolicy.ECC_nistP256_AesGcm.getProfile());
    EndpointDescription endpoint =
        endpoint(
            SecurityPolicy.ECC_nistP256_AesGcm,
            identity.certificate(),
            new UserTokenPolicy[] {
              new UserTokenPolicy(
                  "certificate",
                  UserTokenType.Certificate,
                  null,
                  null,
                  SecurityPolicy.ECC_nistP256_AesGcm.getUri())
            });

    assertNull(
        new X509IdentityProvider(identity.certificate(), identity.keyPair().getPrivate())
            .getCreateSessionAdditionalHeader(DefaultEncodingContext.INSTANCE, endpoint));
  }

  // Composite providers should probe in order without letting an unusable first provider block
  // enhanced username auth.
  @Test
  void compositeProviderSkipsUnavailableCertificateProviderAndNegotiatesUsername()
      throws Exception {

    ApplicationIdentity identity =
        applicationIdentity(SecurityPolicy.ECC_nistP256_AesGcm.getProfile());
    EndpointDescription endpoint =
        endpoint(SecurityPolicy.ECC_nistP256_AesGcm, identity.certificate());
    CompositeProvider provider =
        CompositeProvider.of(
            new X509IdentityProvider(identity.certificate(), identity.keyPair().getPrivate()),
            new UsernameProvider("user", "password"));

    assertEquals(
        new EccUserTokenAdditionalHeader.NegotiationRequest.Supported(
            SecurityPolicy.ECC_nistP256_AesGcm),
        EccUserTokenAdditionalHeader.decodeRequest(
            DefaultEncodingContext.INSTANCE,
            provider.getCreateSessionAdditionalHeader(DefaultEncodingContext.INSTANCE, endpoint)));
  }

  private static EndpointDescription endpoint(
      SecurityPolicy securityPolicy, X509Certificate serverCertificate) throws Exception {

    return endpoint(
        securityPolicy,
        serverCertificate,
        new UserTokenPolicy[] {
          new UserTokenPolicy(
              "username", UserTokenType.UserName, null, null, securityPolicy.getUri())
        });
  }

  private static EndpointDescription endpoint(
      SecurityPolicy securityPolicy,
      X509Certificate serverCertificate,
      UserTokenPolicy[] userTokenPolicies)
      throws Exception {

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
        ByteString.of(serverCertificate.getEncoded()),
        MessageSecurityMode.SignAndEncrypt,
        securityPolicy.getUri(),
        userTokenPolicies,
        Stack.TCP_UASC_UABINARY_TRANSPORT_URI,
        ubyte(0));
  }

  private static Stream<Arguments> representativeEnhancedProfiles() {
    return Stream.of(
        Arguments.of(SecurityPolicy.ECC_nistP256_AesGcm.getProfile()),
        Arguments.of(SecurityPolicy.ECC_curve25519_ChaChaPoly.getProfile()),
        Arguments.of(SecurityPolicy.RSA_DH_AesGcm.getProfile()));
  }

  private static ApplicationIdentity applicationIdentity(SecurityPolicyProfile profile)
      throws Exception {

    KeyPair keyPair =
        switch (profile.authAxis()) {
          case ECDSA_NIST_P256_SHA256 -> SelfSignedCertificateGenerator.generateNistP256KeyPair();
          case ECDSA_NIST_P384_SHA384 -> SelfSignedCertificateGenerator.generateNistP384KeyPair();
          case ECDSA_BRAINPOOL_P256R1_SHA256 ->
              SelfSignedCertificateGenerator.generateBrainpoolP256r1KeyPair();
          case ECDSA_BRAINPOOL_P384R1_SHA384 ->
              SelfSignedCertificateGenerator.generateBrainpoolP384r1KeyPair();
          case ED25519 -> SelfSignedCertificateGenerator.generateEd25519KeyPair();
          case ED448 -> SelfSignedCertificateGenerator.generateEd448KeyPair();
          case RSA_PKCS1_SHA256 -> SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
          default ->
              throw new IllegalArgumentException("unsupported auth axis: " + profile.authAxis());
        };

    X509Certificate certificate =
        certificateBuilder(profile, keyPair)
            .setCommonName("client-" + profile.securityPolicy().name())
            .setApplicationUri("urn:eclipse:milo:test:client")
            .addDnsName("localhost")
            .build();

    return new ApplicationIdentity(keyPair, certificate);
  }

  private static SelfSignedCertificateBuilder certificateBuilder(
      SecurityPolicyProfile profile, KeyPair keyPair) {

    return switch (profile.authAxis()) {
      case ECDSA_NIST_P256_SHA256,
          ECDSA_NIST_P384_SHA384,
          ECDSA_BRAINPOOL_P256R1_SHA256,
          ECDSA_BRAINPOOL_P384R1_SHA384,
          ED25519,
          ED448 ->
          SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair);
      case RSA_PKCS1_SHA256 -> new SelfSignedCertificateBuilder(keyPair);
      default -> throw new IllegalArgumentException("unsupported auth axis: " + profile.authAxis());
    };
  }

  private static ApplicationIdentity rsaApplicationIdentity() throws Exception {
    String commonName = "rsa-server";
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate certificate =
        new SelfSignedCertificateBuilder(keyPair)
            .setCommonName(commonName)
            .setApplicationUri("urn:eclipse:milo:test:" + commonName)
            .addDnsName("localhost")
            .build();

    return new ApplicationIdentity(keyPair, certificate);
  }

  private record ApplicationIdentity(KeyPair keyPair, X509Certificate certificate) {}
}
