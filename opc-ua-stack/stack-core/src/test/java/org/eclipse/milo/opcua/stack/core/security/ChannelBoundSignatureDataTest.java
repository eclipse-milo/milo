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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.primitives.Bytes;
import java.nio.ByteBuffer;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.cert.X509Certificate;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.SignatureUtil;
import org.junit.jupiter.api.Test;

class ChannelBoundSignatureDataTest {

  // SecureChannelEnhancements bind CreateSession server signatures to the first OpenSecureChannel
  // response signature and channel certificates; the legacy nonce-only layout is not interoperable.
  @Test
  void enhancedServerSignatureDataIncludesChannelThumbprintAndCertificateHashes() throws Exception {
    SecurityPolicyProfile profile = SecurityPolicy.ECC_nistP256_AesGcm.getProfile();
    ByteString channelThumbprint = bytes(0x01, 0x02);
    ByteString clientNonce = bytes(0x03);
    ByteString serverChannelCertificate = bytes(0x04, 0x05);
    ByteString clientChannelCertificate = bytes(0x06);
    ByteString serverNonce = bytes(0x07);

    byte[] data =
        ChannelBoundSignatureData.serverSignatureData(
            profile,
            channelThumbprint,
            clientNonce,
            serverChannelCertificate,
            clientChannelCertificate,
            serverNonce,
            bytes(0x7f));

    assertArrayEquals(
        Bytes.concat(
            channelThumbprint.bytesOrEmpty(),
            clientNonce.bytesOrEmpty(),
            sha256(serverChannelCertificate),
            sha256(clientChannelCertificate),
            serverNonce.bytesOrEmpty()),
        data);
  }

  // ActivateSession client signatures add the endpoint server certificate hash to the same channel
  // binding, which catches certificate substitution across discovery and session activation.
  @Test
  void enhancedClientSignatureDataIncludesEndpointAndChannelCertificateHashes() throws Exception {
    SecurityPolicyProfile profile = SecurityPolicy.ECC_nistP256_AesGcm.getProfile();
    ByteString channelThumbprint = bytes(0x01, 0x02);
    ByteString serverNonce = bytes(0x03);
    ByteString serverCertificate = bytes(0x04);
    ByteString serverChannelCertificate = bytes(0x05);
    ByteString clientChannelCertificate = bytes(0x06);
    ByteString clientNonce = bytes(0x07);

    byte[] data =
        ChannelBoundSignatureData.clientSignatureData(
            profile,
            channelThumbprint,
            serverNonce,
            serverCertificate,
            serverChannelCertificate,
            clientChannelCertificate,
            clientNonce);

    assertArrayEquals(
        Bytes.concat(
            channelThumbprint.bytesOrEmpty(),
            serverNonce.bytesOrEmpty(),
            sha256(serverCertificate),
            sha256(serverChannelCertificate),
            sha256(clientChannelCertificate),
            clientNonce.bytesOrEmpty()),
        data);
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

  // CR-025: on the legacy path the ActivateSession clientSignature is computed over the raw
  // serverCertificate bytes exactly as received -- the FULL chain when a server returns one -- not
  // a re-extracted leaf encoding. This deliberately matches the OPC UA reference (.NET) stack and
  // wire semantics. If this ever regressed to signing only the leaf, a chain-returning peer that
  // verifies the raw transmitted bytes (and Milo's own server, which tries leaf-then-chain) would
  // reject the signature. This test pins the contract: the signature must verify against
  // rawChain||nonce and must NOT verify against leaf||nonce.
  @Test
  void legacyClientSignatureSignsRawServerCertificateChainNotLeaf() throws Exception {
    SecurityPolicyProfile profile = SecurityPolicy.Basic256Sha256.getProfile();
    SecurityAlgorithm signatureAlgorithm =
        SecurityPolicy.Basic256Sha256.getAsymmetricSignatureAlgorithm();

    // The server's certificate blob is a two-certificate chain (leaf || issuer). The leaf is the
    // FIRST certificate, which is what a "leaf-only" implementation would have re-extracted.
    X509Certificate leafCertificate = selfSignedCertificate("CR-025 Leaf");
    X509Certificate issuerCertificate = selfSignedCertificate("CR-025 Issuer");
    byte[] leafBytes = leafCertificate.getEncoded();
    byte[] chainBytes = Bytes.concat(leafBytes, issuerCertificate.getEncoded());

    // Sanity: decoding the chain yields the leaf as its first element, so "leaf extraction" is a
    // genuinely different (shorter) byte sequence than the raw chain.
    assertArrayEquals(
        leafBytes, CertificateUtil.decodeCertificate(chainBytes).getEncoded(), "leaf != chain");

    ByteString serverCertificate = ByteString.of(chainBytes);
    ByteString serverNonce = bytes(0x0a, 0x0b, 0x0c);

    byte[] dataToSign =
        ChannelBoundSignatureData.clientSignatureData(
            profile, null, serverNonce, serverCertificate, bytes(0x7f), bytes(0x7f), bytes(0x7f));

    // The signed input is the full raw chain concatenated with the nonce, not the leaf.
    assertArrayEquals(Bytes.concat(chainBytes, serverNonce.bytesOrEmpty()), dataToSign);

    // Sign with a client key and confirm the signature verifies against rawChain||nonce but not
    // against leaf||nonce, which is what a regression to leaf-extraction would produce.
    KeyPair clientKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate clientCertificate =
        new SelfSignedCertificateBuilder(clientKeyPair)
            .setCommonName("CR-025 Client")
            .setApplicationUri("urn:eclipse:milo:test:cr-025-client")
            .build();

    byte[] signature =
        SignatureUtil.sign(
            signatureAlgorithm, clientKeyPair.getPrivate(), ByteBuffer.wrap(dataToSign));

    assertTrue(
        verifies(
            signatureAlgorithm,
            clientCertificate,
            Bytes.concat(chainBytes, serverNonce.bytesOrEmpty()),
            signature),
        "signature must verify against rawChain||nonce");
    assertFalse(
        verifies(
            signatureAlgorithm,
            clientCertificate,
            Bytes.concat(leafBytes, serverNonce.bytesOrEmpty()),
            signature),
        "signature must NOT verify against leaf||nonce (would indicate leaf-extraction"
            + " regression)");
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

  private static ByteString bytes(int... values) {
    byte[] bytes = new byte[values.length];
    for (int i = 0; i < values.length; i++) {
      bytes[i] = (byte) values[i];
    }
    return ByteString.of(bytes);
  }

  private static byte[] sha256(ByteString value) throws Exception {
    return MessageDigest.getInstance("SHA-256").digest(value.bytesOrEmpty());
  }

  private static X509Certificate selfSignedCertificate(String commonName) throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    return new SelfSignedCertificateBuilder(keyPair)
        .setCommonName(commonName)
        .setApplicationUri("urn:eclipse:milo:test:" + commonName.toLowerCase().replace(" ", "-"))
        .build();
  }

  private static boolean verifies(
      SecurityAlgorithm algorithm,
      X509Certificate certificate,
      byte[] dataBytes,
      byte[] signatureBytes) {
    try {
      SignatureUtil.verify(algorithm, certificate, dataBytes, signatureBytes);
      return true;
    } catch (UaException e) {
      return false;
    }
  }
}
