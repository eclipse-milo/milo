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

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import org.eclipse.milo.opcua.sdk.server.SecurityConfiguration;
import org.eclipse.milo.opcua.sdk.server.Session;
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
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.Test;

class AbstractX509IdentityValidatorTest {

  private static final ByteString SERVER_NONCE =
      ByteString.of(
          new byte[] {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
            0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F,
            0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17,
            0x18, 0x19, 0x1A, 0x1B, 0x1C, 0x1D, 0x1E, 0x1F
          });

  // Part 4 (7.41): an explicit certificate user-token policy must use the same public-key family
  // as the SecureChannel; the server rejects the mismatch before signature verification.
  @Test
  void rejectsExplicitCrossFamilyX509TokenPolicy() throws Exception {
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

    assertEquals(StatusCodes.Bad_SecurityPolicyRejected, exception.getStatusCode().getValue());
  }

  // A compatible explicit X509 token policy still verifies the user-token signature and
  // authenticates the certificate identity.
  @Test
  void acceptsCompatibleExplicitX509TokenPolicy() throws Exception {
    CertificateMaterial server = rsaCertificate("server");
    CertificateMaterial user = rsaCertificate("x509-user");
    UserTokenPolicy policy = policy(SecurityPolicy.Basic256Sha256);
    Session session =
        session(
            SecurityPolicy.Basic256Sha256,
            MessageSecurityMode.SignAndEncrypt,
            server.certificate(),
            policy);
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

  private static Session session(
      SecurityPolicy channelPolicy,
      MessageSecurityMode securityMode,
      X509Certificate serverCertificate,
      UserTokenPolicy tokenPolicy)
      throws Exception {

    Session session = mock(Session.class);
    when(session.getSecurityConfiguration())
        .thenReturn(
            new SecurityConfiguration(
                channelPolicy,
                securityMode,
                null,
                serverCertificate,
                serverCertificate != null ? List.of(serverCertificate) : null,
                null,
                null));
    when(session.getLastNonce()).thenReturn(SERVER_NONCE);
    when(session.getClientNonce()).thenReturn(ByteString.NULL_VALUE);

    if (serverCertificate != null && tokenPolicy != null) {
      when(session.getEndpoint())
          .thenReturn(endpoint(channelPolicy, securityMode, tokenPolicy, serverCertificate));
    }

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
}
