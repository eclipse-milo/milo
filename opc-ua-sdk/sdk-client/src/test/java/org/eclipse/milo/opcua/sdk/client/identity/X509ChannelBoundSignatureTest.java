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
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.common.primitives.Bytes;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.cert.X509Certificate;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.ChannelBoundSignatureData;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Part 4 §6.1.8: channel-bound X509 user-token signatures always hash the leaf certificate. A
 * server may return its issuer chain in {@code CreateSessionResponse.serverCertificate}, and the
 * SDK passes those bytes through {@link ChannelSignatureInputs#serverCertificate()} unchanged.
 */
class X509ChannelBoundSignatureTest {

  private static final SecurityPolicy TOKEN_POLICY = SecurityPolicy.RSA_DH_AesGcm;

  private static KeyPair userKeys;
  private static X509Certificate userCertificate;
  private static X509Certificate serverLeaf;
  private static X509Certificate issuer;
  private static X509Certificate clientLeaf;

  @BeforeAll
  static void createCertificates() throws Exception {
    userKeys = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    userCertificate = certificate(userKeys, "user");
    serverLeaf = certificate(SelfSignedCertificateGenerator.generateRsaKeyPair(2048), "server");
    // Session signatures only concatenate or hash the chain's encodings; the issuer relationship
    // is not validated on this path, so another self-signed certificate stands in for the issuer.
    issuer = certificate(SelfSignedCertificateGenerator.generateRsaKeyPair(2048), "issuer");
    clientLeaf = certificate(SelfSignedCertificateGenerator.generateRsaKeyPair(2048), "client");
  }

  // The secured-channel layout and the reduced SecurityMode None layout both hash the leaf of a
  // chained CreateSession server certificate. A peer following the leaf-only rule would reject a
  // signature over the hash of the whole chain.
  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void userTokenSignatureHashesOnlyServerLeaf(boolean securedChannel) throws Exception {
    ByteString channelThumbprint =
        securedChannel ? NonceUtil.generateNonce(32) : ByteString.NULL_VALUE;
    ByteString serverNonce = NonceUtil.generateNonce(32);
    ByteString clientNonce = NonceUtil.generateNonce(32);
    ByteString serverChain =
        ByteString.of(Bytes.concat(serverLeaf.getEncoded(), issuer.getEncoded()));

    var inputs =
        new ChannelSignatureInputs(
            channelThumbprint,
            clientNonce,
            serverChain,
            securedChannel ? ByteString.of(serverLeaf.getEncoded()) : ByteString.NULL_VALUE,
            securedChannel ? ByteString.of(clientLeaf.getEncoded()) : ByteString.NULL_VALUE);

    X509IdentityProvider provider =
        new X509IdentityProvider(userCertificate, userKeys.getPrivate());
    SignatureData signature =
        provider
            .getIdentityToken(
                new IdentityProviderContext(
                    endpoint(securedChannel ? TOKEN_POLICY : SecurityPolicy.None),
                    serverNonce,
                    null,
                    null,
                    null,
                    null,
                    inputs))
            .getSignature();

    byte[] leafData =
        expectedSignatureData(
            securedChannel, channelThumbprint, serverNonce, clientNonce, serverLeaf.getEncoded());
    byte[] chainData =
        expectedSignatureData(
            securedChannel, channelThumbprint, serverNonce, clientNonce, serverChain.bytes());

    assertDoesNotThrow(
        () -> ChannelBoundSignatureData.verify(TOKEN_POLICY, userCertificate, leafData, signature));
    assertThrows(
        UaException.class,
        () ->
            ChannelBoundSignatureData.verify(TOKEN_POLICY, userCertificate, chainData, signature));
  }

  /**
   * Build the Table 101 UserTokenSignature bytes from the spec layout rather than with
   * ChannelBoundSignatureData, so a matching mistake on both sides cannot make the test pass.
   * SHA-256 is the RSA-DH CertificateThumbprintAlgorithm. The provider fills both ClientCertificate
   * and ClientChannelCertificate with the one application certificate.
   */
  private static byte[] expectedSignatureData(
      boolean securedChannel,
      ByteString channelThumbprint,
      ByteString serverNonce,
      ByteString clientNonce,
      byte[] hashedServerCertificate)
      throws Exception {

    if (securedChannel) {
      byte[] clientCertificateHash = sha256(clientLeaf.getEncoded());
      return Bytes.concat(
          channelThumbprint.bytesOrEmpty(),
          serverNonce.bytesOrEmpty(),
          sha256(hashedServerCertificate),
          sha256(serverLeaf.getEncoded()),
          clientCertificateHash,
          clientCertificateHash,
          clientNonce.bytesOrEmpty());
    } else {
      return Bytes.concat(
          serverNonce.bytesOrEmpty(), sha256(hashedServerCertificate), clientNonce.bytesOrEmpty());
    }
  }

  private static byte[] sha256(byte[] bytes) throws Exception {
    return MessageDigest.getInstance("SHA-256").digest(bytes);
  }

  private static X509Certificate certificate(KeyPair keyPair, String commonName) throws Exception {
    return SelfSignedCertificateBuilder.forRsaApplicationCertificate(keyPair)
        .setCommonName(commonName)
        .setApplicationUri("urn:test:" + commonName)
        .build();
  }

  private static EndpointDescription endpoint(SecurityPolicy channelPolicy) throws Exception {
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
        ByteString.of(serverLeaf.getEncoded()),
        channelPolicy == SecurityPolicy.None
            ? MessageSecurityMode.None
            : MessageSecurityMode.SignAndEncrypt,
        channelPolicy.getUri(),
        new UserTokenPolicy[] {
          new UserTokenPolicy("x509", UserTokenType.Certificate, null, null, TOKEN_POLICY.getUri())
        },
        "http://opcfoundation.org/UA-Profile/Transport/uatcp-uasc-uabinary",
        ubyte(0));
  }
}
