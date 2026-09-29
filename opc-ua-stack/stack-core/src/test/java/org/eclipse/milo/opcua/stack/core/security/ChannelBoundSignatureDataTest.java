/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.security;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.primitives.Bytes;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.validation.CaSignedCertificateBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class ChannelBoundSignatureDataTest {

  private static X509Certificate issuer;
  private static X509Certificate serverLeaf;
  private static X509Certificate serverChannelLeaf;
  private static X509Certificate clientLeaf;
  private static X509Certificate clientChannelLeaf;

  @BeforeAll
  static void createCertificates() throws Exception {
    KeyPair issuerKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    issuer =
        new SelfSignedCertificateBuilder(issuerKeyPair)
            .setCommonName("Test CA")
            .setApplicationUri("urn:eclipse:milo:test:ca")
            .build();
    serverLeaf = caSignedCertificate(issuerKeyPair, "server");
    serverChannelLeaf = caSignedCertificate(issuerKeyPair, "server-channel");
    clientLeaf = caSignedCertificate(issuerKeyPair, "client");
    clientChannelLeaf = caSignedCertificate(issuerKeyPair, "client-channel");
  }

  // SecureChannelEnhancements bind CreateSession server signatures to the first OpenSecureChannel
  // response signature and channel certificates; the legacy nonce-only layout is not interoperable.
  // Part 4 §6.1.8: channel-bound signatures always hash the leaf, even when a certificate field
  // carries the issuer chain.
  @Test
  void enhancedServerSignatureDataIncludesChannelThumbprintAndCertificateHashes() throws Exception {
    SecurityPolicyProfile profile = SecurityPolicy.ECC_nistP256_AesGcm.getProfile();
    ByteString channelThumbprint = bytes(0x01, 0x02);
    ByteString clientNonce = bytes(0x03);
    ByteString serverNonce = bytes(0x07);

    byte[] data =
        ChannelBoundSignatureData.serverSignatureData(
            profile,
            channelThumbprint,
            clientNonce,
            chain(serverLeaf),
            chain(clientLeaf),
            serverNonce,
            chain(clientLeaf));

    assertArrayEquals(
        Bytes.concat(
            channelThumbprint.bytesOrEmpty(),
            clientNonce.bytesOrEmpty(),
            sha256(serverLeaf),
            sha256(clientLeaf),
            serverNonce.bytesOrEmpty()),
        data);
  }

  // ActivateSession client signatures add the endpoint server certificate hash to the same channel
  // binding, which catches certificate substitution across discovery and session activation. A
  // server may return its issuer chain in CreateSession, but only the leaf is hashed.
  @Test
  void enhancedClientSignatureDataIncludesEndpointAndChannelCertificateHashes() throws Exception {
    SecurityPolicyProfile profile = SecurityPolicy.ECC_nistP256_AesGcm.getProfile();
    ByteString channelThumbprint = bytes(0x01, 0x02);
    ByteString serverNonce = bytes(0x03);
    ByteString clientNonce = bytes(0x07);

    byte[] data =
        ChannelBoundSignatureData.clientSignatureData(
            profile,
            channelThumbprint,
            serverNonce,
            chain(serverLeaf),
            leaf(serverChannelLeaf),
            chain(clientLeaf),
            clientNonce);

    assertArrayEquals(
        Bytes.concat(
            channelThumbprint.bytesOrEmpty(),
            serverNonce.bytesOrEmpty(),
            sha256(serverLeaf),
            sha256(serverChannelLeaf),
            sha256(clientLeaf),
            clientNonce.bytesOrEmpty()),
        data);
  }

  // Part 4 §6.1.8: HASH() of a null or empty certificate is a zero-length value, so the slot
  // contributes no bytes rather than a digest of nothing.
  @Test
  void enhancedSignatureDataOmitsHashOfEmptyCertificate() throws Exception {
    SecurityPolicyProfile profile = SecurityPolicy.ECC_nistP256_AesGcm.getProfile();
    ByteString channelThumbprint = bytes(0x01, 0x02);
    ByteString serverNonce = bytes(0x03);
    ByteString clientNonce = bytes(0x07);

    byte[] data =
        ChannelBoundSignatureData.clientSignatureData(
            profile,
            channelThumbprint,
            serverNonce,
            chain(serverLeaf),
            ByteString.NULL_VALUE,
            ByteString.of(new byte[0]),
            clientNonce);

    assertArrayEquals(
        Bytes.concat(
            channelThumbprint.bytesOrEmpty(),
            serverNonce.bytesOrEmpty(),
            sha256(serverLeaf),
            clientNonce.bytesOrEmpty()),
        data);
  }

  // Hashing the leaf requires decoding it; bytes that are not a certificate must fail rather than
  // be hashed into a signature the peer cannot reproduce.
  @Test
  void enhancedSignatureDataRejectsNonCertificateBytes() {
    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                ChannelBoundSignatureData.clientSignatureData(
                    SecurityPolicy.ECC_nistP256_AesGcm.getProfile(),
                    bytes(0x01, 0x02),
                    bytes(0x03),
                    bytes(0x04),
                    chain(serverLeaf),
                    chain(clientLeaf),
                    bytes(0x07)));

    assertEquals(StatusCodes.Bad_CertificateInvalid, exception.getStatusCode().getValue());
  }

  // Part 6 §6.2.6 encodes a chain as DER certificates appended leaf first. A PKCS#7 certificate
  // set has no leaf-first guarantee and may hold no certificates, so it must not be searched for a
  // leaf to hash.
  @Test
  void enhancedSignatureDataRejectsPkcs7CertificateContainers() throws Exception {
    CertificateFactory factory = CertificateFactory.getInstance("X.509");
    ByteString issuerFirst =
        ByteString.of(factory.generateCertPath(List.of(issuer, serverLeaf)).getEncoded("PKCS7"));
    ByteString noCertificates =
        ByteString.of(factory.generateCertPath(List.of()).getEncoded("PKCS7"));

    for (ByteString serverCertificate : List.of(issuerFirst, noCertificates)) {
      UaException exception =
          assertThrows(
              UaException.class,
              () ->
                  ChannelBoundSignatureData.clientSignatureData(
                      SecurityPolicy.ECC_nistP256_AesGcm.getProfile(),
                      bytes(0x01, 0x02),
                      bytes(0x03),
                      serverCertificate,
                      chain(serverLeaf),
                      chain(clientLeaf),
                      bytes(0x07)));

      assertEquals(StatusCodes.Bad_CertificateInvalid, exception.getStatusCode().getValue());
    }
  }

  // RSA-era policies keep the historical CreateSession/ActivateSession signature layout so ECC
  // conformance work does not silently break legacy username-token deployments.
  @Test
  void legacyProfilesKeepCertificateAndNonceLayouts() throws Exception {
    SecurityPolicyProfile profile = SecurityPolicy.Basic256Sha256.getProfile();

    assertArrayEquals(
        new byte[] {0x04, 0x05, 0x06},
        ChannelBoundSignatureData.serverSignatureData(
            profile, null, bytes(0x06), bytes(0x7f), bytes(0x7f), bytes(0x7f), bytes(0x04, 0x05)));

    assertArrayEquals(
        new byte[] {0x01, 0x02, 0x03},
        ChannelBoundSignatureData.clientSignatureData(
            profile, null, bytes(0x03), bytes(0x01, 0x02), bytes(0x7f), bytes(0x7f), bytes(0x7f)));
  }

  // Part 4 §6.1.8 has legacy verifiers try the leaf certificate first and then the transmitted
  // chain. Milo's server builds both candidates with this helper, so it must keep a supplied chain
  // intact; the client selects the leaf before calling it.
  @Test
  void legacyClientSignatureDataKeepsSuppliedServerCertificateChain() throws Exception {
    SecurityPolicyProfile profile = SecurityPolicy.Basic256Sha256.getProfile();
    byte[] leafBytes = selfSignedCertificate("Leaf").getEncoded();
    byte[] chainBytes = Bytes.concat(leafBytes, selfSignedCertificate("Issuer").getEncoded());
    ByteString serverNonce = bytes(0x0a, 0x0b, 0x0c);

    byte[] data =
        ChannelBoundSignatureData.clientSignatureData(
            profile,
            null,
            serverNonce,
            ByteString.of(chainBytes),
            bytes(0x7f),
            bytes(0x7f),
            bytes(0x7f));

    assertArrayEquals(
        Bytes.concat(chainBytes, serverNonce.bytesOrEmpty()),
        data,
        "a supplied chain must not be reduced to its leaf");
  }

  // Without the channel thumbprint, session signatures are not tied to the OpenSecureChannel issue
  // exchange and can appear valid for the wrong channel.
  @Test
  void enhancedProfilesRequireChannelThumbprint() {
    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                ChannelBoundSignatureData.clientSignatureData(
                    SecurityPolicy.ECC_nistP256_AesGcm.getProfile(),
                    ByteString.NULL_VALUE,
                    bytes(0x01),
                    bytes(0x02),
                    bytes(0x03),
                    bytes(0x04),
                    bytes(0x05)));

    assertEquals(StatusCodes.Bad_SecurityChecksFailed, exception.getStatusCode().getValue());
  }

  // ActivateSession user-token (X509) signatures use the client-signature channel binding plus an
  // extra HASH(ClientCertificate), per OPC UA Part 4 §6.1.8 Table 101. This ties the user-token
  // signature to the application certificate that created the session. Every hash covers only the
  // leaf of a chained certificate field.
  @Test
  void enhancedUserTokenSignatureDataInsertsClientCertificateHash() throws Exception {
    SecurityPolicyProfile profile = SecurityPolicy.RSA_DH_AesGcm.getProfile();
    ByteString channelThumbprint = bytes(0x01, 0x02);
    ByteString serverNonce = bytes(0x03);
    ByteString clientNonce = bytes(0x07);

    byte[] data =
        ChannelBoundSignatureData.userTokenSignatureData(
            profile,
            true,
            channelThumbprint,
            serverNonce,
            chain(serverLeaf),
            leaf(serverChannelLeaf),
            chain(clientLeaf),
            chain(clientChannelLeaf),
            clientNonce);

    assertArrayEquals(
        Bytes.concat(
            channelThumbprint.bytesOrEmpty(),
            serverNonce.bytesOrEmpty(),
            sha256(serverLeaf),
            sha256(serverChannelLeaf),
            sha256(clientLeaf),
            sha256(clientChannelLeaf),
            clientNonce.bytesOrEmpty()),
        data);
  }

  // When an enhanced user-token policy is used over an unsecured (SecurityMode None) channel there
  // is no channel thumbprint or channel certificate to bind, so Table 101 uses a reduced layout.
  // The CreateSession server certificate may still be a chain; only its leaf is hashed.
  @Test
  void unsecuredChannelUserTokenSignatureDataUsesReducedLayout() throws Exception {
    SecurityPolicyProfile profile = SecurityPolicy.RSA_DH_AesGcm.getProfile();
    ByteString serverNonce = bytes(0x03);
    ByteString clientNonce = bytes(0x07);

    byte[] data =
        ChannelBoundSignatureData.userTokenSignatureData(
            profile,
            false,
            null,
            serverNonce,
            chain(serverLeaf),
            bytes(0x7f),
            bytes(0x7f),
            bytes(0x7f),
            clientNonce);

    assertArrayEquals(
        Bytes.concat(serverNonce.bytesOrEmpty(), sha256(serverLeaf), clientNonce.bytesOrEmpty()),
        data);
  }

  // Legacy user-token policies keep the historical ServerCertificate || ServerNonce layout
  // regardless of the secured-channel flag, which only the enhancement layouts consume.
  @Test
  void legacyUserTokenSignatureDataKeepsServerCertificateAndNonce() throws Exception {
    SecurityPolicyProfile profile = SecurityPolicy.Basic256Sha256.getProfile();

    assertArrayEquals(
        new byte[] {0x04, 0x05, 0x03},
        ChannelBoundSignatureData.userTokenSignatureData(
            profile,
            true,
            bytes(0x01),
            bytes(0x03),
            bytes(0x04, 0x05),
            bytes(0x7f),
            bytes(0x7f),
            bytes(0x7f),
            bytes(0x7f)));
  }

  // An enhanced (ECC/RSA-DH) user-token policy binds to the SecureChannel or uses the reduced
  // SecurityMode-None layout; a legacy secured channel supplies neither, so the combination is
  // rejected up front rather than failing deep in the byte builder (Part 4 §6.1.8 Table 101).
  @Test
  void enhancedUserTokenOverLegacySecuredChannelIsRejected() {
    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                ChannelBoundSignatureData.checkUserTokenChannelCompatibility(
                    SecurityPolicy.ECC_nistP256_AesGcm.getProfile(),
                    SecurityPolicy.Basic256Sha256));

    assertEquals(StatusCodes.Bad_IdentityTokenInvalid, exception.getStatusCode().getValue());
  }

  // An enhanced user-token policy is allowed over an enhanced channel (full layout) and over an
  // unsecured None channel (reduced layout); legacy user-token policies impose no constraint.
  @Test
  void compatibleUserTokenChannelCombinationsAreAllowed() {
    assertDoesNotThrow(
        () -> {
          ChannelBoundSignatureData.checkUserTokenChannelCompatibility(
              SecurityPolicy.ECC_nistP256_AesGcm.getProfile(), SecurityPolicy.ECC_nistP256_AesGcm);
          ChannelBoundSignatureData.checkUserTokenChannelCompatibility(
              SecurityPolicy.ECC_nistP256_AesGcm.getProfile(), SecurityPolicy.None);
          ChannelBoundSignatureData.checkUserTokenChannelCompatibility(
              SecurityPolicy.Basic256Sha256.getProfile(), SecurityPolicy.Basic256Sha256);
          ChannelBoundSignatureData.checkUserTokenChannelCompatibility(
              SecurityPolicy.Basic256Sha256.getProfile(), SecurityPolicy.None);
        });
  }

  // Part 4 §7.36: legacy policies carry the algorithm URI on the wire and read it back to verify;
  // SecureChannel-enhancement policies leave it empty and imply it from the policy.
  @Test
  void wireAndVerifyAlgorithmFollowLegacyVsEnhancedUriRule() throws Exception {
    SecurityPolicy legacy = SecurityPolicy.Basic256Sha256;
    SecurityAlgorithm legacyAlgorithm = legacy.getAsymmetricSignatureAlgorithm();
    assertEquals(
        legacyAlgorithm.getUri(),
        ChannelBoundSignatureData.wireSignatureAlgorithm(legacy, legacyAlgorithm));
    assertEquals(
        legacyAlgorithm,
        ChannelBoundSignatureData.verifySignatureAlgorithm(
            legacy, new SignatureData(legacyAlgorithm.getUri(), ByteString.NULL_VALUE)));

    SecurityPolicy enhanced = SecurityPolicy.RSA_DH_AesGcm;
    SecurityAlgorithm enhancedAlgorithm = enhanced.getAsymmetricSignatureAlgorithm();
    assertNull(ChannelBoundSignatureData.wireSignatureAlgorithm(enhanced, enhancedAlgorithm));
    assertEquals(
        enhancedAlgorithm,
        ChannelBoundSignatureData.verifySignatureAlgorithm(
            enhanced, new SignatureData(null, ByteString.NULL_VALUE)));
  }

  // sign()/verify() round-trip for a legacy policy: the wire URI is populated, verification works
  // against the signed bytes, and tampered data fails.
  @Test
  void signAndVerifyRoundTripLegacyPolicyCarriesWireUri() throws Exception {
    SecurityPolicy policy = SecurityPolicy.Basic256Sha256;
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate certificate =
        new SelfSignedCertificateBuilder(keyPair)
            .setCommonName("sign-verify")
            .setApplicationUri("urn:eclipse:milo:test:sign-verify")
            .build();
    byte[] data = {0x01, 0x02, 0x03, 0x04};

    SignatureData signature = ChannelBoundSignatureData.sign(policy, keyPair.getPrivate(), data);

    assertEquals(policy.getAsymmetricSignatureAlgorithm().getUri(), signature.getAlgorithm());
    assertDoesNotThrow(
        () -> ChannelBoundSignatureData.verify(policy, certificate, data, signature));
    assertThrows(
        UaException.class,
        () -> ChannelBoundSignatureData.verify(policy, certificate, new byte[] {0x09}, signature));
  }

  private static ByteString bytes(int... values) {
    byte[] bytes = new byte[values.length];
    for (int i = 0; i < values.length; i++) {
      bytes[i] = (byte) values[i];
    }
    return ByteString.of(bytes);
  }

  // The expected hash input is the leaf encoding produced by the certificate builder, independent
  // of how ChannelBoundSignatureData extracts the leaf from a chain.
  private static byte[] sha256(X509Certificate certificate) throws Exception {
    return MessageDigest.getInstance("SHA-256").digest(certificate.getEncoded());
  }

  private static ByteString leaf(X509Certificate certificate) throws Exception {
    return ByteString.of(certificate.getEncoded());
  }

  // A certificate field carrying the leaf followed by its issuer, as allowed on the wire.
  private static ByteString chain(X509Certificate certificate) throws Exception {
    return ByteString.of(Bytes.concat(certificate.getEncoded(), issuer.getEncoded()));
  }

  private static X509Certificate caSignedCertificate(KeyPair issuerKeyPair, String commonName)
      throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    return new CaSignedCertificateBuilder(keyPair, issuer, issuerKeyPair.getPrivate())
        .setCommonName(commonName)
        .setApplicationUri("urn:eclipse:milo:test:" + commonName)
        .build();
  }

  private static X509Certificate selfSignedCertificate(String commonName) throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    return new SelfSignedCertificateBuilder(keyPair)
        .setCommonName(commonName)
        .setApplicationUri("urn:eclipse:milo:test:" + commonName.toLowerCase().replace(" ", "-"))
        .build();
  }
}
