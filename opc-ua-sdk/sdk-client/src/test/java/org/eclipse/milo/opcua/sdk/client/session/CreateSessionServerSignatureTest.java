/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.session;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.primitives.Bytes;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.cert.X509Certificate;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.ChannelBoundSignatureData;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Part 4 §6.1.8: a server using a legacy policy may sign either the client leaf or the chain sent
 * in CreateSession, and the client verifies the leaf form first and then the chain. Enhanced
 * policies hash only the leaf.
 */
class CreateSessionServerSignatureTest {

  private static KeyPair rsaServerKeyPair;
  private static X509Certificate rsaServerCertificate;
  private static KeyPair eccServerKeyPair;
  private static X509Certificate eccServerCertificate;
  private static byte[] clientLeaf;
  private static byte[] clientIssuer;

  private final ByteString clientNonce = NonceUtil.generateNonce(32);
  private final ByteString serverNonce = NonceUtil.generateNonce(32);
  private final ByteString channelThumbprint = NonceUtil.generateNonce(64);

  @BeforeAll
  static void generateCertificates() throws Exception {
    rsaServerKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    rsaServerCertificate =
        new SelfSignedCertificateBuilder(rsaServerKeyPair)
            .setCommonName("server")
            .setApplicationUri("urn:test:server")
            .build();

    eccServerKeyPair = SelfSignedCertificateGenerator.generateNistP256KeyPair();
    eccServerCertificate =
        SelfSignedCertificateBuilder.forEccApplicationCertificate(eccServerKeyPair)
            .setCommonName("server")
            .setApplicationUri("urn:test:server")
            .build();

    // Verification treats the client certificates as opaque bytes, so two unrelated certificates
    // stand in for a leaf and its issuer.
    KeyPair clientKeyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    clientLeaf =
        new SelfSignedCertificateBuilder(clientKeyPair)
            .setCommonName("client")
            .setApplicationUri("urn:test:client")
            .build()
            .getEncoded();
    clientIssuer =
        new SelfSignedCertificateBuilder(SelfSignedCertificateGenerator.generateRsaKeyPair(2048))
            .setCommonName("issuer")
            .build()
            .getEncoded();
  }

  @Test
  void legacySignatureOverClientLeafIsAccepted() throws Exception {
    SignatureData signature = signLegacy(Bytes.concat(clientLeaf, clientNonce.bytesOrEmpty()));

    assertDoesNotThrow(() -> verifyLegacy(signature, clientChain()));
  }

  @Test
  void legacySignatureOverTransmittedClientChainIsAccepted() throws Exception {
    SignatureData signature =
        signLegacy(Bytes.concat(clientLeaf, clientIssuer, clientNonce.bytesOrEmpty()));

    assertDoesNotThrow(() -> verifyLegacy(signature, clientChain()));
  }

  @Test
  void legacySignatureOverOtherDataIsRejected() throws Exception {
    // Signed over a different nonce, so neither the leaf nor the chain input matches.
    SignatureData signature =
        signLegacy(
            Bytes.concat(clientLeaf, clientIssuer, NonceUtil.generateNonce(32).bytesOrEmpty()));

    assertThrows(UaException.class, () -> verifyLegacy(signature, clientChain()));
  }

  // The chain fallback only applies to the chain the client actually sent. With a single
  // certificate configured, a signature that includes issuer bytes cannot match either input.
  @Test
  void legacySignatureOverUnsentChainIsRejectedForSingleCertificate() throws Exception {
    SignatureData leafSignature = signLegacy(Bytes.concat(clientLeaf, clientNonce.bytesOrEmpty()));
    SignatureData chainSignature =
        signLegacy(Bytes.concat(clientLeaf, clientIssuer, clientNonce.bytesOrEmpty()));

    assertDoesNotThrow(() -> verifyLegacy(leafSignature, ByteString.of(clientLeaf)));
    assertThrows(UaException.class, () -> verifyLegacy(chainSignature, ByteString.of(clientLeaf)));
  }

  @Test
  void enhancedSignatureOverClientLeafHashIsAccepted() throws Exception {
    SignatureData signature = signEnhanced(clientLeaf);

    assertDoesNotThrow(() -> verifyEnhanced(signature));
  }

  // Enhanced policies bind the signature to leaf certificate hashes only, so the legacy chain
  // fallback must not accept a hash over the transmitted chain.
  @Test
  void enhancedSignatureOverClientChainHashIsRejected() throws Exception {
    SignatureData signature = signEnhanced(Bytes.concat(clientLeaf, clientIssuer));

    assertThrows(UaException.class, () -> verifyEnhanced(signature));
  }

  private ByteString clientChain() {
    return ByteString.of(Bytes.concat(clientLeaf, clientIssuer));
  }

  private SignatureData signLegacy(byte[] data) throws UaException {
    return ChannelBoundSignatureData.sign(
        SecurityPolicy.Basic256Sha256, rsaServerKeyPair.getPrivate(), data);
  }

  private void verifyLegacy(SignatureData signature, ByteString clientCertificateChain)
      throws Exception {

    SessionFsmFactory.verifyServerSignature(
        SecurityPolicy.Basic256Sha256,
        rsaServerCertificate,
        ByteString.NULL_VALUE,
        clientNonce,
        ByteString.of(rsaServerCertificate.getEncoded()),
        ByteString.of(clientLeaf),
        clientCertificateChain,
        serverNonce,
        signature);
  }

  /**
   * Sign the enhanced CreateSession layout: ChannelThumbprint | ClientNonce |
   * Hash(ServerChannelCertificate) | Hash(ClientChannelCertificate) | ServerNonce.
   */
  private SignatureData signEnhanced(byte[] clientCertificateInput) throws Exception {
    SecurityPolicy policy = SecurityPolicy.ECC_nistP256_AesGcm;
    String digest = policy.getProfile().certificateThumbprintAlgorithm().getTransformation();

    byte[] data =
        Bytes.concat(
            channelThumbprint.bytesOrEmpty(),
            clientNonce.bytesOrEmpty(),
            MessageDigest.getInstance(digest).digest(eccServerCertificate.getEncoded()),
            MessageDigest.getInstance(digest).digest(clientCertificateInput),
            serverNonce.bytesOrEmpty());

    return ChannelBoundSignatureData.sign(policy, eccServerKeyPair.getPrivate(), data);
  }

  private void verifyEnhanced(SignatureData signature) throws Exception {
    SessionFsmFactory.verifyServerSignature(
        SecurityPolicy.ECC_nistP256_AesGcm,
        eccServerCertificate,
        channelThumbprint,
        clientNonce,
        ByteString.of(eccServerCertificate.getEncoded()),
        ByteString.of(clientLeaf),
        clientChain(),
        serverNonce,
        signature);
  }
}
