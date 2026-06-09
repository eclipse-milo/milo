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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.identity.UsernameProvider;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.EccEncryptedSecret;
import org.eclipse.milo.opcua.stack.core.security.EccUserTokenAdditionalHeader;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EphemeralKeyType;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.SignedSoftwareCertificate;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SessionEndpointValidationTest {

  private static final String TRANSPORT_OPC_TCP = Stack.TCP_UASC_UABINARY_TRANSPORT_URI;

  private static final String TRANSPORT_HTTPS = Stack.HTTPS_UABINARY_TRANSPORT_URI;

  @Test
  @DisplayName("Returns without validation when no discovery endpoints match the transport profile")
  void testNoDiscoveryForTransportReturns() {
    List<EndpointDescription> discovery =
        List.of(
            endpoint(
                "urn:app",
                "opc.tcp://a:4840",
                MessageSecurityMode.None,
                "none",
                userTokens("anon"),
                TRANSPORT_HTTPS,
                (short) 1));

    List<EndpointDescription> session =
        List.of(
            endpoint(
                "urn:app",
                "opc.tcp://a:4840",
                MessageSecurityMode.None,
                "none",
                userTokens("anon"),
                TRANSPORT_OPC_TCP,
                (short) 1));

    assertDoesNotThrow(
        () -> SessionFsmFactory.validateSessionEndpoints(TRANSPORT_OPC_TCP, discovery, session));
  }

  @Test
  @DisplayName("Throws when filtered sizes differ")
  void testSizeMismatchThrows() {
    List<EndpointDescription> discovery = new ArrayList<>();
    discovery.add(
        endpoint(
            "urn:app",
            "opc.tcp://a:4840",
            MessageSecurityMode.None,
            "none",
            userTokens("anon"),
            TRANSPORT_OPC_TCP,
            (short) 1));
    discovery.add(
        endpoint(
            "urn:app",
            "opc.tcp://b:4840",
            MessageSecurityMode.None,
            "none",
            userTokens("anon"),
            TRANSPORT_OPC_TCP,
            (short) 1));

    List<EndpointDescription> session =
        List.of(
            endpoint(
                "urn:app",
                "opc.tcp://a:4840",
                MessageSecurityMode.None,
                "none",
                userTokens("anon"),
                TRANSPORT_OPC_TCP,
                (short) 1));

    UaException ex =
        assertThrows(
            UaException.class,
            () ->
                SessionFsmFactory.validateSessionEndpoints(TRANSPORT_OPC_TCP, discovery, session));

    assertEquals(StatusCodes.Bad_SecurityChecksFailed, ex.getStatusCode().getValue());
  }

  @Test
  @DisplayName("Passes when all endpoints match regardless of order")
  void testAllMatchDifferentOrder() {
    EndpointDescription a =
        endpoint(
            "urn:app",
            "opc.tcp://a:4840",
            MessageSecurityMode.None,
            "none",
            userTokens("anon"),
            TRANSPORT_OPC_TCP,
            (short) 1);
    EndpointDescription b =
        endpoint(
            "urn:app",
            "opc.tcp://b:4840",
            MessageSecurityMode.SignAndEncrypt,
            "policy#Basic256Sha256",
            userTokens("anon"),
            TRANSPORT_OPC_TCP,
            (short) 2);

    List<EndpointDescription> discovery = List.of(a, b);
    List<EndpointDescription> session = List.of(b, a);

    assertDoesNotThrow(
        () -> SessionFsmFactory.validateSessionEndpoints(TRANSPORT_OPC_TCP, discovery, session));
  }

  enum DiffKind {
    ApplicationUri,
    EndpointUrl,
    SecurityMode,
    SecurityPolicyUri,
    UserIdentityTokens,
    SecurityLevel
  }

  static Stream<Arguments> differingEndpoints() {
    return Stream.of(
        Arguments.of(DiffKind.ApplicationUri),
        Arguments.of(DiffKind.EndpointUrl),
        Arguments.of(DiffKind.SecurityMode),
        Arguments.of(DiffKind.SecurityPolicyUri),
        Arguments.of(DiffKind.UserIdentityTokens),
        Arguments.of(DiffKind.SecurityLevel));
  }

  @ParameterizedTest(name = "Mismatch due to differing {0} throws UaException")
  @MethodSource("differingEndpoints")
  void testEndpointMismatchThrows(DiffKind kind) {
    EndpointDescription discoveryEp =
        endpoint(
            "urn:app",
            "opc.tcp://a:4840",
            MessageSecurityMode.Sign,
            "policy#A",
            userTokens("anon"),
            TRANSPORT_OPC_TCP,
            (short) 1);

    EndpointDescription sessionEp;
    switch (kind) {
      case ApplicationUri ->
          sessionEp =
              endpoint(
                  "urn:app2",
                  "opc.tcp://a:4840",
                  MessageSecurityMode.Sign,
                  "policy#A",
                  userTokens("anon"),
                  TRANSPORT_OPC_TCP,
                  (short) 1);
      case EndpointUrl ->
          sessionEp =
              endpoint(
                  "urn:app",
                  "opc.tcp://DIFF:4840",
                  MessageSecurityMode.Sign,
                  "policy#A",
                  userTokens("anon"),
                  TRANSPORT_OPC_TCP,
                  (short) 1);
      case SecurityMode ->
          sessionEp =
              endpoint(
                  "urn:app",
                  "opc.tcp://a:4840",
                  MessageSecurityMode.None,
                  "policy#A",
                  userTokens("anon"),
                  TRANSPORT_OPC_TCP,
                  (short) 1);
      case SecurityPolicyUri ->
          sessionEp =
              endpoint(
                  "urn:app",
                  "opc.tcp://a:4840",
                  MessageSecurityMode.Sign,
                  "policy#B",
                  userTokens("anon"),
                  TRANSPORT_OPC_TCP,
                  (short) 1);
      case UserIdentityTokens ->
          sessionEp =
              endpoint(
                  "urn:app",
                  "opc.tcp://a:4840",
                  MessageSecurityMode.Sign,
                  "policy#A",
                  userTokens("DIFF"),
                  TRANSPORT_OPC_TCP,
                  (short) 1);
      case SecurityLevel ->
          sessionEp =
              endpoint(
                  "urn:app",
                  "opc.tcp://a:4840",
                  MessageSecurityMode.Sign,
                  "policy#A",
                  userTokens("anon"),
                  TRANSPORT_OPC_TCP,
                  (short) 2);
      default -> throw new IllegalStateException("Unexpected kind: " + kind);
    }

    List<EndpointDescription> discovery = List.of(discoveryEp);
    List<EndpointDescription> session = List.of(sessionEp);

    UaException ex =
        assertThrows(
            UaException.class,
            () ->
                SessionFsmFactory.validateSessionEndpoints(TRANSPORT_OPC_TCP, discovery, session));

    assertEquals(StatusCodes.Bad_SecurityChecksFailed, ex.getStatusCode().getValue());
  }

  // CreateSession only asks for enhanced key material when the chosen provider will use username
  // auth.
  @Test
  void buildCreateSessionAdditionalHeaderRequestsEccUsernameTokenKey() throws Exception {
    EndpointDescription endpoint =
        endpoint(
            "urn:app",
            "opc.tcp://a:4840",
            MessageSecurityMode.SignAndEncrypt,
            SecurityPolicy.ECC_nistP256_AesGcm.getUri(),
            new UserTokenPolicy[] {
              new UserTokenPolicy(
                  "username",
                  UserTokenType.UserName,
                  null,
                  null,
                  SecurityPolicy.ECC_nistP256_AesGcm.getUri())
            },
            TRANSPORT_OPC_TCP,
            (short) 1);

    ExtensionObject additionalHeader =
        SessionFsmFactory.buildCreateSessionAdditionalHeader(
            new UsernameProvider("user", "password"), DefaultEncodingContext.INSTANCE, endpoint);

    assertEquals(
        new EccUserTokenAdditionalHeader.NegotiationRequest.Supported(
            SecurityPolicy.ECC_nistP256_AesGcm),
        EccUserTokenAdditionalHeader.decodeRequest(
            DefaultEncodingContext.INSTANCE, additionalHeader));
  }

  // Anonymous auth on an enhanced endpoint must not trigger username-token key negotiation.
  @Test
  void buildCreateSessionAdditionalHeaderSkipsEccWhenAnonymousProviderSelected() throws Exception {

    EndpointDescription endpoint =
        endpoint(
            "urn:app",
            "opc.tcp://a:4840",
            MessageSecurityMode.SignAndEncrypt,
            SecurityPolicy.ECC_nistP256_AesGcm.getUri(),
            new UserTokenPolicy[] {
              new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null),
              new UserTokenPolicy(
                  "username",
                  UserTokenType.UserName,
                  null,
                  null,
                  SecurityPolicy.ECC_nistP256_AesGcm.getUri())
            },
            TRANSPORT_OPC_TCP,
            (short) 1);

    ExtensionObject additionalHeader =
        SessionFsmFactory.buildCreateSessionAdditionalHeader(
            AnonymousProvider.INSTANCE, DefaultEncodingContext.INSTANCE, endpoint);

    assertNull(additionalHeader);
  }

  // None endpoints that use enhanced username tokens still need a concrete certificate anchor
  // before the client encrypts the password.
  @Test
  void verifyCreateSessionEccUserTokenKeyRejectsMissingCertificate() {
    EndpointDescription endpoint =
        endpoint(
            "urn:app",
            "opc.tcp://a:4840",
            MessageSecurityMode.None,
            SecurityPolicy.None.getUri(),
            new UserTokenPolicy[] {
              new UserTokenPolicy(
                  "username",
                  UserTokenType.UserName,
                  null,
                  null,
                  SecurityPolicy.ECC_nistP256_AesGcm.getUri())
            },
            TRANSPORT_OPC_TCP,
            (short) 1);

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                SessionFsmFactory.verifyCreateSessionEccUserTokenKey(
                    DefaultEncodingContext.INSTANCE,
                    new CertificateValidator.InsecureCertificateValidator(),
                    endpoint,
                    createSessionResponse(ByteString.NULL_VALUE, null),
                    SecurityPolicy.ECC_nistP256_AesGcm));

    assertEquals(StatusCodes.Bad_ConfigurationError, exception.getStatusCode().getValue());
  }

  // A signed receiver key must be anchored to the certificate from the selected endpoint, not a
  // different certificate supplied in CreateSession.
  @Test
  void verifyCreateSessionEccUserTokenKeyRejectsResponseCertificateMismatch() throws Exception {
    X509Certificate endpointCertificate = eccCertificate("endpoint");
    X509Certificate responseCertificate = eccCertificate("response");
    EndpointDescription endpoint =
        endpoint(
            "urn:app",
            "opc.tcp://a:4840",
            ByteString.of(endpointCertificate.getEncoded()),
            MessageSecurityMode.None,
            SecurityPolicy.None.getUri(),
            new UserTokenPolicy[] {
              new UserTokenPolicy(
                  "username",
                  UserTokenType.UserName,
                  null,
                  null,
                  SecurityPolicy.ECC_nistP256_AesGcm.getUri())
            },
            TRANSPORT_OPC_TCP,
            (short) 1);
    ExtensionObject additionalHeader =
        EccUserTokenAdditionalHeader.createResponse(
            DefaultEncodingContext.INSTANCE,
            SecurityPolicy.ECC_nistP256_AesGcm,
            new EphemeralKeyType(ByteString.of(new byte[64]), ByteString.of(new byte[64])));

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                SessionFsmFactory.verifyCreateSessionEccUserTokenKey(
                    DefaultEncodingContext.INSTANCE,
                    new CertificateValidator.InsecureCertificateValidator(),
                    endpoint,
                    createSessionResponse(
                        ByteString.of(responseCertificate.getEncoded()), additionalHeader),
                    SecurityPolicy.ECC_nistP256_AesGcm));

    assertEquals(StatusCodes.Bad_SecurityChecksFailed, exception.getStatusCode().getValue());
  }

  // Part 6 6.8.2 makes the receiver EphemeralKey single-use: each ActivateSession response carries
  // a fresh signed key the client must adopt for the next activation. A key signed by the endpoint
  // certificate must verify and yield its public key so reactivation uses the most recent key.
  @Test
  void verifyActivateSessionEccUserTokenKeyAdoptsRotatedKey() throws Exception {
    SecurityPolicy tokenPolicy = SecurityPolicy.ECC_nistP256_AesGcm;
    KeyPair endpointKeyPair = SelfSignedCertificateGenerator.generateNistP256KeyPair();
    X509Certificate endpointCertificate = eccCertificate("endpoint", endpointKeyPair);

    EndpointDescription endpoint =
        endpoint(
            "urn:app",
            "opc.tcp://a:4840",
            ByteString.of(endpointCertificate.getEncoded()),
            MessageSecurityMode.SignAndEncrypt,
            tokenPolicy.getUri(),
            new UserTokenPolicy[] {
              new UserTokenPolicy(
                  "username", UserTokenType.UserName, null, null, tokenPolicy.getUri())
            },
            TRANSPORT_OPC_TCP,
            (short) 1);

    KeyPair rotatedEphemeralKeyPair =
        EccEncryptedSecret.generateEphemeralKeyPair(tokenPolicy.getProfile());
    EphemeralKeyType rotatedKey =
        EccEncryptedSecret.createEphemeralKey(
            tokenPolicy.getProfile(), endpointKeyPair, rotatedEphemeralKeyPair);
    ExtensionObject additionalHeader =
        EccUserTokenAdditionalHeader.createResponse(
            DefaultEncodingContext.INSTANCE, tokenPolicy, rotatedKey);

    ByteString adopted =
        SessionFsmFactory.verifyActivateSessionEccUserTokenKey(
                DefaultEncodingContext.INSTANCE,
                endpoint,
                activateSessionResponse(additionalHeader),
                tokenPolicy)
            .orElseThrow();

    assertEquals(rotatedKey.getPublicKey(), adopted);
  }

  // When a server returns no fresh key, the client must keep the most recent key rather than fail;
  // the helper signals this by returning empty so the caller falls back.
  @Test
  void verifyActivateSessionEccUserTokenKeyReturnsEmptyWhenNoKeyRotated() throws Exception {
    SecurityPolicy tokenPolicy = SecurityPolicy.ECC_nistP256_AesGcm;
    X509Certificate endpointCertificate =
        eccCertificate("endpoint", SelfSignedCertificateGenerator.generateNistP256KeyPair());

    EndpointDescription endpoint =
        endpoint(
            "urn:app",
            "opc.tcp://a:4840",
            ByteString.of(endpointCertificate.getEncoded()),
            MessageSecurityMode.SignAndEncrypt,
            tokenPolicy.getUri(),
            new UserTokenPolicy[] {
              new UserTokenPolicy(
                  "username", UserTokenType.UserName, null, null, tokenPolicy.getUri())
            },
            TRANSPORT_OPC_TCP,
            (short) 1);

    assertTrue(
        SessionFsmFactory.verifyActivateSessionEccUserTokenKey(
                DefaultEncodingContext.INSTANCE,
                endpoint,
                activateSessionResponse(null),
                tokenPolicy)
            .isEmpty());
  }

  // A rotated key signed by some other certificate must be rejected, otherwise a man-in-the-middle
  // could swap the receiver key the client encrypts the next password against.
  @Test
  void verifyActivateSessionEccUserTokenKeyRejectsForeignSigner() throws Exception {
    SecurityPolicy tokenPolicy = SecurityPolicy.ECC_nistP256_AesGcm;
    X509Certificate endpointCertificate =
        eccCertificate("endpoint", SelfSignedCertificateGenerator.generateNistP256KeyPair());
    KeyPair foreignKeyPair = SelfSignedCertificateGenerator.generateNistP256KeyPair();

    EndpointDescription endpoint =
        endpoint(
            "urn:app",
            "opc.tcp://a:4840",
            ByteString.of(endpointCertificate.getEncoded()),
            MessageSecurityMode.SignAndEncrypt,
            tokenPolicy.getUri(),
            new UserTokenPolicy[] {
              new UserTokenPolicy(
                  "username", UserTokenType.UserName, null, null, tokenPolicy.getUri())
            },
            TRANSPORT_OPC_TCP,
            (short) 1);

    KeyPair rotatedEphemeralKeyPair =
        EccEncryptedSecret.generateEphemeralKeyPair(tokenPolicy.getProfile());
    EphemeralKeyType foreignKey =
        EccEncryptedSecret.createEphemeralKey(
            tokenPolicy.getProfile(), foreignKeyPair, rotatedEphemeralKeyPair);
    ExtensionObject additionalHeader =
        EccUserTokenAdditionalHeader.createResponse(
            DefaultEncodingContext.INSTANCE, tokenPolicy, foreignKey);

    assertThrows(
        UaException.class,
        () ->
            SessionFsmFactory.verifyActivateSessionEccUserTokenKey(
                DefaultEncodingContext.INSTANCE,
                endpoint,
                activateSessionResponse(additionalHeader),
                tokenPolicy));
  }

  private static EndpointDescription endpoint(
      String applicationUri,
      String endpointUrl,
      MessageSecurityMode securityMode,
      String securityPolicyUri,
      UserTokenPolicy[] userTokens,
      String transportProfileUri,
      short securityLevel) {

    return endpoint(
        applicationUri,
        endpointUrl,
        null,
        securityMode,
        securityPolicyUri,
        userTokens,
        transportProfileUri,
        securityLevel);
  }

  private static EndpointDescription endpoint(
      String applicationUri,
      String endpointUrl,
      ByteString serverCertificate,
      MessageSecurityMode securityMode,
      String securityPolicyUri,
      UserTokenPolicy[] userTokens,
      String transportProfileUri,
      short securityLevel) {

    ApplicationDescription serverDesc =
        new ApplicationDescription(
            applicationUri,
            "product:uri",
            new LocalizedText("en", "name"),
            ApplicationType.Server,
            null,
            null,
            new String[0]);

    return new EndpointDescription(
        endpointUrl,
        serverDesc,
        serverCertificate,
        securityMode,
        securityPolicyUri,
        userTokens,
        transportProfileUri,
        UByte.valueOf(securityLevel));
  }

  private static CreateSessionResponse createSessionResponse(
      ByteString serverCertificate, ExtensionObject additionalHeader) {

    return new CreateSessionResponse(
        new ResponseHeader(
            DateTime.now(), UInteger.valueOf(1), StatusCode.GOOD, null, null, additionalHeader),
        new NodeId(1, "session"),
        new NodeId(1, "auth"),
        60_000d,
        ByteString.of(new byte[32]),
        serverCertificate,
        new EndpointDescription[0],
        new SignedSoftwareCertificate[0],
        new SignatureData(null, null),
        UInteger.MAX);
  }

  private static ActivateSessionResponse activateSessionResponse(ExtensionObject additionalHeader) {
    return new ActivateSessionResponse(
        new ResponseHeader(
            DateTime.now(), UInteger.valueOf(1), StatusCode.GOOD, null, null, additionalHeader),
        ByteString.of(new byte[32]),
        new StatusCode[0],
        new DiagnosticInfo[0]);
  }

  private static X509Certificate eccCertificate(String commonName) throws Exception {
    return eccCertificate(commonName, SelfSignedCertificateGenerator.generateNistP256KeyPair());
  }

  private static X509Certificate eccCertificate(String commonName, KeyPair keyPair)
      throws Exception {

    return SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair)
        .setCommonName(commonName)
        .setApplicationUri("urn:eclipse:milo:test:" + commonName)
        .addDnsName("localhost")
        .build();
  }

  private static UserTokenPolicy[] userTokens(String policyId) {
    return new UserTokenPolicy[] {
      new UserTokenPolicy(policyId, UserTokenType.Anonymous, null, null, null)
    };
  }
}
