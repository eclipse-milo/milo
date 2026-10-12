/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.identity;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.primitives.Bytes;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.cert.X509Certificate;
import java.util.List;
import org.eclipse.milo.opcua.sdk.server.SecurityConfiguration;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.SessionServerCertificate;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.ChannelBoundSignatureData;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.types.structured.X509IdentityToken;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

class AbstractX509IdentityValidatorTest {

  private static final ByteString CHANNEL_THUMBPRINT = NonceUtil.generateNonce(32);

  private static final ByteString CLIENT_NONCE = NonceUtil.generateNonce(32);

  private static final ByteString SERVER_NONCE =
      ByteString.of(
          new byte[] {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
            0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
            0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17,
            0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F
          });

  // The current transport cannot provide channel-bound signatures over a legacy secured channel.
  // This runtime restriction is separate from the certificate-token policy's key algorithm.
  @Test
  void rejectsUnsupportedEnhancedSignatureOnLegacyChannel() throws Exception {
    CertificateMaterial user = eccCertificate("x509-user");
    Session session =
        session(SecurityPolicy.Basic256Sha256, MessageSecurityMode.SignAndEncrypt, null, null);
    TestX509Validator validator = new TestX509Validator();

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                validator.validateIdentityToken(
                    session,
                    token(user.certificate()),
                    policy(SecurityPolicy.ECC_nistP256_AesGcm),
                    new SignatureData(null, null)));

    assertEquals(StatusCodes.Bad_IdentityTokenInvalid, exception.getStatusCode().getValue());
  }

  // A compatible explicit X509 token policy still verifies the user-token signature and
  // authenticates the certificate identity.
  @ParameterizedTest
  @EnumSource(
      value = SecurityPolicy.class,
      names = {"Basic256Sha256", "ECC_nistP256_AesGcm"})
  void acceptsCompatibleExplicitX509TokenPolicy(SecurityPolicy channelPolicy) throws Exception {
    CertificateMaterial server =
        channelPolicy == SecurityPolicy.Basic256Sha256
            ? rsaCertificate("server")
            : eccCertificate("server");
    CertificateMaterial user = rsaCertificate("x509-user");
    UserTokenPolicy policy = policy(SecurityPolicy.Basic256Sha256);
    Session session =
        session(channelPolicy, MessageSecurityMode.SignAndEncrypt, server.certificate(), policy);
    SignatureData signature =
        ChannelBoundSignatureData.sign(
            SecurityPolicy.Basic256Sha256,
            user.keyPair().getPrivate(),
            ChannelBoundSignatureData.legacyUserTokenSignatureData(
                ByteString.of(server.certificate().getEncoded()), SERVER_NONCE));
    TestX509Validator validator = new TestX509Validator();

    Identity identity =
        validator.validateIdentityToken(session, token(user.certificate()), policy, signature);

    assertEquals(UserTokenType.Certificate, identity.getUserTokenType());
    assertEquals(user.certificate(), ((Identity.X509UserIdentity) identity).getCertificate());
  }

  // Part 4 §6.1.8: legacy user-token signatures may cover the server leaf or the chain it sent, so
  // the server keeps accepting both for backward compatibility.
  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void legacyUserTokenSignatureOverServerLeafOrChainIsAccepted(boolean signChain) throws Exception {
    ChainedServer server = chainedServer();
    CertificateMaterial user = rsaCertificate("x509-user");
    UserTokenPolicy policy = policy(SecurityPolicy.Basic256Sha256);
    Session session = chainedSession(SecurityPolicy.Basic256Sha256, server, null);
    byte[] serverCertificate = signChain ? server.chainBytes() : server.leafBytes();
    SignatureData signature =
        ChannelBoundSignatureData.sign(
            SecurityPolicy.Basic256Sha256,
            user.keyPair().getPrivate(),
            Bytes.concat(serverCertificate, SERVER_NONCE.bytesOrEmpty()));

    Identity identity =
        new TestX509Validator()
            .validateIdentityToken(session, token(user.certificate()), policy, signature);

    assertEquals(user.certificate(), ((Identity.X509UserIdentity) identity).getCertificate());
  }

  // Part 4 §6.1.8 Table 101: channel-bound user-token signatures hash only the leaf of the
  // CreateSession server certificate, on a secured channel and in the reduced SecurityMode None
  // layout. A signature over the hash of the whole chain must be rejected.
  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void enhancedUserTokenSignatureHashesOnlyServerLeaf(boolean securedChannel) throws Exception {
    ChainedServer server = chainedServer();
    CertificateMaterial client = rsaCertificate("client");
    CertificateMaterial user = rsaCertificate("x509-user");
    SecurityPolicy tokenPolicy = SecurityPolicy.RSA_DH_AesGcm;
    UserTokenPolicy policy = policy(tokenPolicy);
    Session session =
        securedChannel
            ? chainedSession(tokenPolicy, server, client.certificate())
            : chainedSession(SecurityPolicy.None, server, null);
    TestX509Validator validator = new TestX509Validator();

    SignatureData leafSignature =
        ChannelBoundSignatureData.sign(
            tokenPolicy,
            user.keyPair().getPrivate(),
            enhancedUserTokenSignatureData(securedChannel, server.leafBytes(), server, client));

    Identity identity =
        validator.validateIdentityToken(session, token(user.certificate()), policy, leafSignature);
    assertEquals(user.certificate(), ((Identity.X509UserIdentity) identity).getCertificate());

    SignatureData chainSignature =
        ChannelBoundSignatureData.sign(
            tokenPolicy,
            user.keyPair().getPrivate(),
            enhancedUserTokenSignatureData(securedChannel, server.chainBytes(), server, client));

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                validator.validateIdentityToken(
                    session, token(user.certificate()), policy, chainSignature));
    assertEquals(StatusCodes.Bad_IdentityTokenInvalid, exception.getStatusCode().getValue());
  }

  /**
   * Build the Table 101 UserTokenSignature bytes from the spec layout rather than with
   * ChannelBoundSignatureData, so a matching mistake on both sides cannot make the test pass.
   * SHA-256 is the RSA-DH CertificateThumbprintAlgorithm. Milo uses one application certificate as
   * both ClientCertificate and ClientChannelCertificate.
   */
  private static byte[] enhancedUserTokenSignatureData(
      boolean securedChannel,
      byte[] hashedServerCertificate,
      ChainedServer server,
      CertificateMaterial client)
      throws Exception {

    if (securedChannel) {
      byte[] clientCertificateHash = sha256(client.certificate().getEncoded());
      return Bytes.concat(
          CHANNEL_THUMBPRINT.bytesOrEmpty(),
          SERVER_NONCE.bytesOrEmpty(),
          sha256(hashedServerCertificate),
          sha256(server.leafBytes()),
          clientCertificateHash,
          clientCertificateHash,
          CLIENT_NONCE.bytesOrEmpty());
    } else {
      return Bytes.concat(
          SERVER_NONCE.bytesOrEmpty(),
          sha256(hashedServerCertificate),
          CLIENT_NONCE.bytesOrEmpty());
    }
  }

  /**
   * A Session whose CreateSession response advertised the server leaf, with the creating channel
   * carrying the leaf and issuer. A {@code None} channel has no channel certificates; its
   * CreateSession certificate is the endpoint's advertised leaf.
   */
  private static Session chainedSession(
      SecurityPolicy channelPolicy,
      ChainedServer server,
      @Nullable X509Certificate clientCertificate)
      throws Exception {

    boolean secured = channelPolicy != SecurityPolicy.None;
    Session session = mock(Session.class);
    SecurityConfiguration securityConfiguration =
        new SecurityConfiguration(
            channelPolicy,
            secured ? MessageSecurityMode.SignAndEncrypt : MessageSecurityMode.None,
            null,
            secured ? server.leaf() : null,
            secured ? server.chain() : null,
            clientCertificate,
            clientCertificate != null ? List.of(clientCertificate) : null,
            channelPolicy.getProfile().secureChannelEnhancements()
                ? CHANNEL_THUMBPRINT
                : ByteString.NULL_VALUE);
    when(session.getSecurityConfiguration()).thenReturn(securityConfiguration);
    when(session.getLastNonce()).thenReturn(SERVER_NONCE);
    when(session.getClientNonce()).thenReturn(CLIENT_NONCE);
    when(session.getOriginalServerCertificate())
        .thenReturn(
            new SessionServerCertificate(
                ByteString.of(server.leafBytes()),
                securityConfiguration.getServerCertificate(),
                securityConfiguration.getServerCertificateChain(),
                null));

    return session;
  }

  // Session signatures only concatenate or hash the chain's encodings; the issuer relationship is
  // not validated on this path, so another self-signed certificate stands in for the issuer.
  private static ChainedServer chainedServer() throws Exception {
    return new ChainedServer(
        rsaCertificate("server").certificate(), rsaCertificate("issuer").certificate());
  }

  private static byte[] sha256(byte[] bytes) throws Exception {
    return MessageDigest.getInstance("SHA-256").digest(bytes);
  }

  private static Session session(
      SecurityPolicy channelPolicy,
      MessageSecurityMode securityMode,
      X509Certificate serverCertificate,
      UserTokenPolicy tokenPolicy)
      throws Exception {

    Session session = mock(Session.class);
    SecurityConfiguration securityConfiguration =
        new SecurityConfiguration(
            channelPolicy,
            securityMode,
            null,
            serverCertificate,
            serverCertificate != null ? List.of(serverCertificate) : null,
            null,
            null);
    when(session.getSecurityConfiguration()).thenReturn(securityConfiguration);
    when(session.getLastNonce()).thenReturn(SERVER_NONCE);
    when(session.getClientNonce()).thenReturn(ByteString.NULL_VALUE);

    ByteString createSessionCertificate = ByteString.NULL_VALUE;
    if (serverCertificate != null && tokenPolicy != null) {
      EndpointDescription endpoint =
          endpoint(channelPolicy, securityMode, tokenPolicy, serverCertificate);
      when(session.getEndpoint()).thenReturn(endpoint);
      createSessionCertificate = endpoint.getServerCertificate();
    }
    when(session.getOriginalServerCertificate())
        .thenReturn(
            new SessionServerCertificate(
                createSessionCertificate,
                securityConfiguration.getServerCertificate(),
                securityConfiguration.getServerCertificateChain(),
                null));

    return session;
  }

  private static EndpointDescription endpoint(
      SecurityPolicy channelPolicy,
      MessageSecurityMode securityMode,
      UserTokenPolicy tokenPolicy,
      X509Certificate serverCertificate)
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
        securityMode,
        channelPolicy.getUri(),
        new UserTokenPolicy[] {tokenPolicy},
        "http://opcfoundation.org/UA-Profile/Transport/uatcp-uasc-uabinary",
        ubyte(0));
  }

  private static X509IdentityToken token(X509Certificate certificate) throws Exception {
    return new X509IdentityToken("x509", ByteString.of(certificate.getEncoded()));
  }

  private static UserTokenPolicy policy(SecurityPolicy securityPolicy) {
    return new UserTokenPolicy(
        "x509", UserTokenType.Certificate, null, null, securityPolicy.getUri());
  }

  private static CertificateMaterial rsaCertificate(String commonName) throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate certificate =
        new SelfSignedCertificateBuilder(keyPair)
            .setCommonName(commonName)
            .setApplicationUri("urn:eclipse:milo:test:" + commonName)
            .addDnsName("localhost")
            .build();

    return new CertificateMaterial(keyPair, certificate);
  }

  private static CertificateMaterial eccCertificate(String commonName) throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateNistP256KeyPair();
    X509Certificate certificate =
        SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair)
            .setCommonName(commonName)
            .setApplicationUri("urn:eclipse:milo:test:" + commonName)
            .addDnsName("localhost")
            .build();

    return new CertificateMaterial(keyPair, certificate);
  }

  private static final class TestX509Validator extends AbstractX509IdentityValidator {

    @Override
    protected Identity.X509UserIdentity authenticateCertificate(
        Session session, X509Certificate certificate) {

      return new DefaultX509UserIdentity(certificate);
    }
  }

  private record CertificateMaterial(KeyPair keyPair, X509Certificate certificate) {}

  private record ChainedServer(X509Certificate leaf, X509Certificate issuer) {

    List<X509Certificate> chain() {
      return List.of(leaf, issuer);
    }

    byte[] leafBytes() throws Exception {
      return leaf.getEncoded();
    }

    byte[] chainBytes() throws Exception {
      return Bytes.concat(leaf.getEncoded(), issuer.getEncoded());
    }
  }
}
