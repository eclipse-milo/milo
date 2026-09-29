/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.primitives.Bytes;
import io.netty.channel.Channel;
import java.io.ByteArrayOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.security.MessageDigest;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.SecureChannel;
import org.eclipse.milo.opcua.stack.core.channel.ServerSecureChannel;
import org.eclipse.milo.opcua.stack.core.security.CertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.CertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.ChannelBoundSignatureData;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.EnhancedUserTokenAdditionalHeader;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.TrustListManager;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.NonceUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.transport.server.EndpointSelectionKey;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransportFactory;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * OPC UA does not transmit an EndpointDescription identifier during OpenSecureChannel (Part 6,
 * 7.1.2.3), so the server must derive endpoint identity from wire-observable channel inputs. These
 * tests protect the invariant that one selection key identifies exactly one effective Session
 * endpoint: ambiguous configurations are rejected at startup, and Sessions are bound to the
 * endpoint their SecureChannel selected rather than an ordering-dependent re-derivation.
 */
public class SessionEndpointBindingTest {

  private static final NodeId GROUP_A = new NodeId(2, "certificate-group-a");
  private static final NodeId GROUP_B = new NodeId(2, "certificate-group-b");

  private static final UserTokenPolicy ANONYMOUS_POLICY =
      new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null);

  private static final UserTokenPolicy USERNAME_POLICY =
      new UserTokenPolicy("username", UserTokenType.UserName, null, null, null);

  private static final OpcServerTransportFactory NO_OP_TRANSPORTS =
      transportProfile ->
          new OpcServerTransport() {
            @Override
            public void bind(
                ServerApplicationContext applicationContext, InetSocketAddress bindAddress) {}

            @Override
            public void unbind() {}
          };

  private static CertificateMaterial certificateA;
  private static CertificateMaterial certificateB;
  private static CertificateMaterial clientCertificate;
  private static CertificateMaterial chainedServerCertificate;

  private static final ByteString CHANNEL_THUMBPRINT = NonceUtil.generateNonce(32);

  @BeforeAll
  static void generateCertificates() throws Exception {
    certificateA = rsaCertificate("server-a");
    certificateB = rsaCertificate("server-b");
    clientCertificate = rsaCertificate("client");

    // Session signatures only concatenate or hash the chain's encodings; the issuer relationship
    // is not validated on this path, so another self-signed certificate stands in for the issuer.
    X509Certificate issuer = rsaCertificate("issuer").certificate();
    chainedServerCertificate =
        new CertificateMaterial(
            NodeIds.RsaSha256ApplicationCertificateType,
            certificateA.keyPair(),
            new X509Certificate[] {certificateA.certificate(), issuer});
  }

  @Nested
  class StartupValidation {

    /**
     * Part 4, 5.5.4.1 distinguishes endpoints by security configuration, while user token policies
     * are advertised properties of an endpoint, not selectors. Two endpoints indistinguishable at
     * OpenSecureChannel that differ only in token policies would make Session authentication depend
     * on endpoint collection ordering, so startup must reject them in either insertion order rather
     * than document a sharp edge.
     */
    @Test
    void startupRejectsEndpointsDifferingOnlyInUserTokenPoliciesInEitherOrder() {
      EndpointConfig anonymousEndpoint = securedEndpoint(GROUP_A, ANONYMOUS_POLICY);
      EndpointConfig usernameEndpoint = securedEndpoint(GROUP_A, USERNAME_POLICY);

      for (List<EndpointConfig> ordering :
          List.of(
              List.of(anonymousEndpoint, usernameEndpoint),
              List.of(usernameEndpoint, anonymousEndpoint))) {

        OpcUaServer server = server(manager(group(GROUP_A, certificateA)), ordering);

        ExecutionException e =
            assertThrows(
                ExecutionException.class,
                () -> server.startup().get(5, TimeUnit.SECONDS),
                "ordering: " + ordering);

        UaException cause = assertInstanceOf(UaException.class, e.getCause());
        assertEquals(StatusCodes.Bad_ConfigurationError, cause.getStatusCode().value());
        assertTrue(
            cause.getMessage().contains("userTokenPolicies"),
            "collision diagnostic should identify the colliding endpoints: " + cause.getMessage());
      }
    }

    // The supported alternative to per-token-policy endpoints: one endpoint advertising all
    // supported user token policies must be accepted.
    @Test
    void singleEndpointWithMultipleUserTokenPoliciesIsAccepted() throws Exception {
      OpcUaServer server =
          server(
              manager(group(GROUP_A, certificateA)),
              List.of(securedEndpoint(GROUP_A, ANONYMOUS_POLICY, USERNAME_POLICY)));

      assertEquals(server, server.startup().get(5, TimeUnit.SECONDS));

      server.shutdown().get(5, TimeUnit.SECONDS);
    }

    /**
     * Otherwise-identical endpoints backed by distinct certificates in distinct CertificateGroups
     * are distinguishable by the receiver thumbprint at OpenSecureChannel, so they are a valid
     * configuration and must resolve uniquely regardless of insertion order.
     */
    @Test
    void endpointsWithDistinctCertificatesStartAndResolveUniquelyInEitherOrder() throws Exception {
      EndpointConfig endpointA = securedEndpoint(GROUP_A, ANONYMOUS_POLICY);
      EndpointConfig endpointB = securedEndpoint(GROUP_B, ANONYMOUS_POLICY);

      for (List<EndpointConfig> ordering :
          List.of(List.of(endpointA, endpointB), List.of(endpointB, endpointA))) {

        OpcUaServer server =
            server(manager(group(GROUP_A, certificateA), group(GROUP_B, certificateB)), ordering);

        assertEquals(server, server.startup().get(5, TimeUnit.SECONDS), "ordering: " + ordering);

        for (CertificateMaterial certificate : List.of(certificateA, certificateB)) {
          EndpointDescription selected =
              selectSecuredEndpoint(server, CertificateUtil.thumbprint(certificate.certificate()))
                  .orElseThrow();

          assertArrayEquals(
              certificate.certificate().getEncoded(),
              selected.getServerCertificate().bytesOrEmpty(),
              "selection must follow the thumbprint, not insertion order: " + ordering);
        }

        server.shutdown().get(5, TimeUnit.SECONDS);
      }
    }

    /**
     * Multi-hostname configurations advertise one endpoint per hostname with identical
     * Session-sensitive state. These are host substitution aliases of the same effective endpoint
     * (Part 6 permits clients to connect by IP or alternate hostname), not collisions, and the
     * alias matching the client's requested URL is preferred.
     */
    @Test
    void hostnameAliasesAreAcceptedAndResolvedByRequestedUrl() throws Exception {
      EndpointConfig alphaEndpoint = securedEndpointForHostname("alpha", GROUP_A);
      EndpointConfig betaEndpoint = securedEndpointForHostname("beta", GROUP_A);

      OpcUaServer server =
          server(manager(group(GROUP_A, certificateA)), List.of(alphaEndpoint, betaEndpoint));

      assertEquals(server, server.startup().get(5, TimeUnit.SECONDS));

      EndpointSelectionKey key =
          EndpointSelectionKey.of(
              TransportProfile.TCP_UASC_UABINARY,
              "opc.tcp://beta:4840/test",
              SecurityPolicy.Basic256Sha256,
              MessageSecurityMode.SignAndEncrypt,
              CertificateUtil.thumbprint(certificateA.certificate()));

      EndpointDescription selected =
          server
              .getApplicationContext()
              .selectEndpoint(key, "opc.tcp://beta:4840/test")
              .orElseThrow();

      assertEquals("opc.tcp://beta:4840/test", selected.getEndpointUrl());

      server.shutdown().get(5, TimeUnit.SECONDS);
    }

    /**
     * An unsecured channel may only select an explicit SecurityPolicy.None endpoint. When none is
     * configured the selection is empty -- the discovery-only state -- rather than an arbitrary
     * secured endpoint whose certificate and token policies the channel never negotiated.
     */
    @Test
    void unsecuredKeySelectsNothingWhenNoExplicitNoneEndpointExists() {
      OpcUaServer server =
          server(
              manager(group(GROUP_A, certificateA)),
              List.of(securedEndpoint(GROUP_A, ANONYMOUS_POLICY)));

      Optional<EndpointDescription> selected =
          server.getApplicationContext().selectEndpoint(noneKey("/test"), endpointUrl("/test"));

      assertTrue(selected.isEmpty());
    }
  }

  @Nested
  class SessionBinding {

    /**
     * The certificate returned by CreateSession must belong to the endpoint whose thumbprint
     * established the SecureChannel. Before endpoint propagation, SessionManager re-derived the
     * endpoint without a certificate discriminator and could return the other endpoint's
     * certificate depending on insertion order.
     */
    @Test
    void createSessionBindsSessionToChannelSelectedEndpointInEitherOrder() throws Exception {
      EndpointConfig endpointA = securedEndpoint(GROUP_A, ANONYMOUS_POLICY);
      EndpointConfig endpointB = securedEndpoint(GROUP_B, ANONYMOUS_POLICY);

      for (List<EndpointConfig> ordering :
          List.of(List.of(endpointA, endpointB), List.of(endpointB, endpointA))) {

        OpcUaServer server =
            server(manager(group(GROUP_A, certificateA), group(GROUP_B, certificateB)), ordering);

        // The channel was established against certificate B; the transport propagates the
        // endpoint it selected by receiver thumbprint.
        EndpointDescription channelEndpoint =
            selectSecuredEndpoint(server, CertificateUtil.thumbprint(certificateB.certificate()))
                .orElseThrow();

        ServiceRequestContext context =
            new TestServiceRequestContext(
                endpointUrl("/test"), securedChannel(1L, certificateB), channelEndpoint);

        CreateSessionResponse response =
            server
                .getSessionManager()
                .createSession(context, createSessionRequest(clientCertificate.byteString()));

        assertArrayEquals(
            certificateB.certificate().getEncoded(),
            response.getServerCertificate().bytesOrEmpty(),
            "CreateSession must return the channel endpoint's certificate; ordering: " + ordering);

        Session session = server.getSessionManager().getAllSessions().get(0);
        assertEquals(channelEndpoint, session.getEndpoint());

        session.close(true);
      }
    }

    /**
     * Contexts that do not propagate a channel endpoint selection fall back to selection-key
     * resolution, which discriminates by the channel certificate. The result must be the endpoint
     * matching the channel certificate in either insertion order -- the direct regression test for
     * ordering-dependent findFirst() selection.
     */
    @Test
    void fallbackEndpointResolutionFollowsChannelCertificateInEitherOrder() throws Exception {
      EndpointConfig endpointA = securedEndpoint(GROUP_A, ANONYMOUS_POLICY);
      EndpointConfig endpointB = securedEndpoint(GROUP_B, ANONYMOUS_POLICY);

      for (List<EndpointConfig> ordering :
          List.of(List.of(endpointA, endpointB), List.of(endpointB, endpointA))) {

        OpcUaServer server =
            server(manager(group(GROUP_A, certificateA), group(GROUP_B, certificateB)), ordering);

        ServiceRequestContext context =
            new TestServiceRequestContext(
                endpointUrl("/test"), securedChannel(1L, certificateB), null);

        CreateSessionResponse response =
            server
                .getSessionManager()
                .createSession(context, createSessionRequest(clientCertificate.byteString()));

        assertArrayEquals(
            certificateB.certificate().getEncoded(),
            response.getServerCertificate().bytesOrEmpty(),
            "fallback resolution must follow the channel certificate; ordering: " + ordering);

        server.getSessionManager().getAllSessions().forEach(s -> s.close(true));
      }
    }

    /**
     * A SecurityPolicy.None channel with no explicit None endpoint supports discovery only.
     * CreateSession on such a channel must be rejected instead of associating the Session with an
     * arbitrary secured endpoint's token policies and certificate.
     */
    @Test
    void createSessionOnDiscoveryOnlyUnsecuredChannelIsRejected() {
      OpcUaServer server =
          server(
              manager(group(GROUP_A, certificateA)),
              List.of(securedEndpoint(GROUP_A, ANONYMOUS_POLICY)));

      ServiceRequestContext context =
          new TestServiceRequestContext(endpointUrl("/test"), noneChannel(1L), null);

      UaException e =
          assertThrows(
              UaException.class,
              () ->
                  server
                      .getSessionManager()
                      .createSession(context, createSessionRequest(ByteString.NULL_VALUE)));

      assertEquals(StatusCodes.Bad_SecurityChecksFailed, e.getStatusCode().value());
    }

    // An explicitly configured SecurityPolicy.None endpoint continues to support unsecured
    // Sessions, including for contexts that do not propagate a channel endpoint selection.
    @Test
    void createSessionOnExplicitNoneEndpointSucceeds() throws Exception {
      OpcUaServer server =
          server(
              manager(group(GROUP_A, certificateA)),
              List.of(noneEndpoint("/test"), securedEndpoint(GROUP_A, ANONYMOUS_POLICY)));

      ServiceRequestContext context =
          new TestServiceRequestContext(endpointUrl("/test"), noneChannel(1L), null);

      server
          .getSessionManager()
          .createSession(context, createSessionRequest(ByteString.NULL_VALUE));

      Session session = server.getSessionManager().getAllSessions().get(0);
      assertEquals(SecurityPolicy.None.getUri(), session.getEndpoint().getSecurityPolicyUri());

      session.close(true);
    }

    // Part 4 §5.7.3.1 binds activation to the last ServerNonce received by the client. A failed
    // response-header negotiation must leave a signed retry using that delivered nonce valid.
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void securedReactivationRetriesWithTheLastDeliveredNonce(boolean replaceChannel)
        throws Exception {
      OpcUaServer server =
          server(
              manager(group(GROUP_A, certificateA)),
              List.of(securedEndpoint(GROUP_A, ANONYMOUS_POLICY)));
      SessionManager sessions = server.getSessionManager();
      try {
        EndpointDescription endpoint = endpointForPath(server, "/test");
        var original =
            new TestServiceRequestContext(
                endpointUrl("/test"), securedChannel(1L, certificateA), endpoint);
        var create = createSessionRequest(clientCertificate.byteString());
        CreateSessionResponse created = sessions.createSession(original, create);
        NodeId token = created.getAuthenticationToken();
        var activated =
            sessions.activateSession(
                original,
                signedActivation(token, created.getServerNonce(), create.getClientNonce(), null));
        ByteString deliveredNonce = activated.getServerNonce();
        var candidate =
            replaceChannel
                ? new TestServiceRequestContext(
                    endpointUrl("/test"), securedChannel(2L, certificateA), endpoint)
                : original;

        UaException wrongNonce =
            assertThrows(
                UaException.class,
                () ->
                    sessions.activateSession(
                        candidate,
                        signedActivation(
                            token, NonceUtil.generateNonce(32), create.getClientNonce(), null)));
        assertEquals(
            StatusCodes.Bad_ApplicationSignatureInvalid, wrongNonce.getStatusCode().value());

        ExtensionObject rejected =
            EnhancedUserTokenAdditionalHeader.createRequest(
                server.getStaticEncodingContext(), SecurityPolicy.ECC_nistP256_AesGcm);
        UaException failure =
            assertThrows(
                UaException.class,
                () ->
                    sessions.activateSession(
                        candidate,
                        signedActivation(
                            token, deliveredNonce, create.getClientNonce(), rejected)));
        assertEquals(StatusCodes.Bad_SecurityPolicyRejected, failure.getStatusCode().value());
        assertEquals(1L, sessions.getSession(original, requestHeader(token)).getSecureChannelId());

        // Sign only the nonce delivered in the previous successful response, never Session state.
        var retried =
            sessions.activateSession(
                candidate, signedActivation(token, deliveredNonce, create.getClientNonce(), null));
        assertNotEquals(deliveredNonce, retried.getServerNonce());
        assertEquals(
            replaceChannel ? 2L : 1L,
            sessions.getSession(candidate, requestHeader(token)).getSecureChannelId());
      } finally {
        sessions.shutdown();
      }
    }

    /**
     * Part 4 §6.1.8: a legacy ActivateSession signature may cover the server leaf certificate or
     * the whole transmitted chain, and verifiers try the leaf first, then the chain. Milo's client
     * signs the leaf, but older clients may sign the chain, so both must activate. A signature over
     * neither must still be rejected.
     */
    @Test
    void legacyClientSignatureVerifiesOverServerLeafOrChain() throws Exception {
      X509Certificate issuer = rsaCertificate("issuer").certificate();
      var chained =
          new CertificateMaterial(
              certificateA.certificateTypeId(),
              certificateA.keyPair(),
              new X509Certificate[] {certificateA.certificate(), issuer});
      OpcUaServer server =
          server(
              manager(group(GROUP_A, chained)),
              List.of(securedEndpoint(GROUP_A, ANONYMOUS_POLICY)));
      SessionManager sessions = server.getSessionManager();
      try {
        var context =
            new TestServiceRequestContext(
                endpointUrl("/test"),
                securedChannel(1L, chained),
                endpointForPath(server, "/test"));
        var create = createSessionRequest(clientCertificate.byteString());
        CreateSessionResponse created = sessions.createSession(context, create);
        NodeId token = created.getAuthenticationToken();
        ByteString clientNonce = create.getClientNonce();
        ByteString leaf = chained.byteString();
        var chainBytes = new ByteArrayOutputStream();
        chainBytes.write(certificateA.certificate().getEncoded());
        chainBytes.write(issuer.getEncoded());
        ByteString chain = ByteString.of(chainBytes.toByteArray());

        ActivateSessionResponse leafSigned =
            sessions.activateSession(
                context, signedActivationOver(leaf, token, created.getServerNonce(), clientNonce));
        ActivateSessionResponse chainSigned =
            sessions.activateSession(
                context,
                signedActivationOver(chain, token, leafSigned.getServerNonce(), clientNonce));

        ByteString issuerOnly = ByteString.of(issuer.getEncoded());
        UaException invalid =
            assertThrows(
                UaException.class,
                () ->
                    sessions.activateSession(
                        context,
                        signedActivationOver(
                            issuerOnly, token, chainSigned.getServerNonce(), clientNonce)));
        assertEquals(StatusCodes.Bad_ApplicationSignatureInvalid, invalid.getStatusCode().value());
      } finally {
        sessions.shutdown();
      }
    }

    /**
     * When a Session is reactivated onto a replacement SecureChannel, its endpoint association must
     * follow the endpoint selected by that new channel; identity validation runs against the new
     * endpoint's token policies before the Session's security state is changed.
     */
    @Test
    void reactivationOnReplacementChannelUsesThatChannelsEndpoint() throws Exception {
      OpcUaServer server =
          server(
              manager(group(GROUP_A, certificateA)),
              List.of(noneEndpoint("/a"), noneEndpoint("/b")));

      EndpointDescription endpointA = endpointForPath(server, "/a");
      EndpointDescription endpointB = endpointForPath(server, "/b");

      ServiceRequestContext context1 =
          new TestServiceRequestContext(endpointUrl("/a"), noneChannel(1L), endpointA);

      CreateSessionResponse createResponse =
          server
              .getSessionManager()
              .createSession(context1, createSessionRequest(ByteString.NULL_VALUE));

      NodeId authToken = createResponse.getAuthenticationToken();

      server.getSessionManager().activateSession(context1, activateSessionRequest(authToken));

      Session session = server.getSessionManager().getAllSessions().get(0);
      assertEquals(endpointA, session.getEndpoint());
      assertNotEquals(endpointA, endpointB, "control: the two endpoints must differ");

      ServiceRequestContext context2 =
          new TestServiceRequestContext(endpointUrl("/b"), noneChannel(2L), endpointB);

      server.getSessionManager().activateSession(context2, activateSessionRequest(authToken));

      assertEquals(endpointB, session.getEndpoint(), "session must follow the new channel");
      assertEquals(2L, session.getSecureChannelId());

      session.close(true);
    }
  }

  /**
   * Part 4 §6.1.8: a server certificate field may carry the issuer chain after the leaf. Legacy
   * ActivateSession signatures may cover either the leaf or that chain, while channel-bound
   * signatures always hash only the leaf. The expected signed bytes are built here from the spec
   * layout, not with ChannelBoundSignatureData, so a matching mistake in Milo's client and server
   * cannot make these tests pass.
   */
  @Nested
  class ClientSignatureCertificateChain {

    // Backward compatibility: legacy clients sign the CreateSession certificate bytes as received,
    // and some sign the full chain, so the server accepts both forms.
    @ParameterizedTest
    @ValueSource(booleans = {false, true})
    void legacyClientSignatureOverLeafOrChainIsAccepted(boolean signChain) throws Exception {
      byte[] serverCertificate =
          signChain ? chainedServerCertificate.chainBytes() : chainedServerCertificate.leafBytes();

      assertActivation(
          SecurityPolicy.Basic256Sha256,
          (serverNonce, clientNonce) -> Bytes.concat(serverCertificate, serverNonce));
    }

    // A legacy signature over bytes the server never sent must still be rejected.
    @Test
    void legacyClientSignatureOverOtherCertificateIsRejected() throws Exception {
      UaException failure =
          assertThrows(
              UaException.class,
              () ->
                  assertActivation(
                      SecurityPolicy.Basic256Sha256,
                      (serverNonce, clientNonce) ->
                          Bytes.concat(certificateB.certificate().getEncoded(), serverNonce)));

      assertEquals(StatusCodes.Bad_ApplicationSignatureInvalid, failure.getStatusCode().value());
    }

    // Channel-bound signatures hash only the leaf of the CreateSession server certificate.
    @Test
    void enhancedClientSignatureOverLeafHashIsAccepted() throws Exception {
      assertActivation(
          SecurityPolicy.RSA_DH_AesGcm,
          (serverNonce, clientNonce) ->
              enhancedClientSignatureData(
                  serverNonce, clientNonce, chainedServerCertificate.leafBytes()));
    }

    // Hashing the whole chain is not a valid channel-bound input, so the legacy leaf-then-chain
    // fallback must not accept it for an enhanced policy.
    @Test
    void enhancedClientSignatureOverChainHashIsRejected() throws Exception {
      UaException failure =
          assertThrows(
              UaException.class,
              () ->
                  assertActivation(
                      SecurityPolicy.RSA_DH_AesGcm,
                      (serverNonce, clientNonce) ->
                          enhancedClientSignatureData(
                              serverNonce, clientNonce, chainedServerCertificate.chainBytes())));

      assertEquals(StatusCodes.Bad_ApplicationSignatureInvalid, failure.getStatusCode().value());
    }

    /**
     * Create a Session on a channel whose server certificate group carries a leaf and issuer, then
     * activate it with a client signature over {@code signedData}.
     */
    private void assertActivation(SecurityPolicy securityPolicy, SignedData signedData)
        throws Exception {

      OpcUaServer server =
          server(
              manager(group(GROUP_A, chainedServerCertificate)),
              List.of(securedEndpoint(GROUP_A, securityPolicy, ANONYMOUS_POLICY)));
      SessionManager sessions = server.getSessionManager();
      try {
        EndpointDescription endpoint = endpointForPath(server, "/test");
        assertArrayEquals(
            chainedServerCertificate.leafBytes(),
            endpoint.getServerCertificate().bytesOrEmpty(),
            "control: the endpoint advertises only the leaf");

        var context =
            new TestServiceRequestContext(
                endpointUrl("/test"),
                securedChannel(1L, chainedServerCertificate, securityPolicy),
                endpoint);
        CreateSessionRequest create = createSessionRequest(clientCertificate.byteString());
        CreateSessionResponse created = sessions.createSession(context, create);

        byte[] data =
            signedData.bytes(
                created.getServerNonce().bytesOrEmpty(), create.getClientNonce().bytesOrEmpty());
        SignatureData signature =
            ChannelBoundSignatureData.sign(
                securityPolicy, clientCertificate.keyPair().getPrivate(), data);

        sessions.activateSession(
            context,
            new ActivateSessionRequest(
                requestHeader(created.getAuthenticationToken()),
                signature,
                null,
                null,
                null,
                new SignatureData(null, null)));
      } finally {
        sessions.shutdown();
      }
    }

    // Part 4 §6.1.8 Table 101 ClientSignature: ChannelThumbprint | ServerNonce |
    // HASH(ServerCertificate) | HASH(ServerChannelCertificate) | HASH(ClientChannelCertificate) |
    // ClientNonce, with SHA-256 as the RSA-DH CertificateThumbprintAlgorithm.
    private byte[] enhancedClientSignatureData(
        byte[] serverNonce, byte[] clientNonce, byte[] serverCertificate) throws Exception {

      return Bytes.concat(
          CHANNEL_THUMBPRINT.bytesOrEmpty(),
          serverNonce,
          sha256(serverCertificate),
          sha256(chainedServerCertificate.leafBytes()),
          sha256(clientCertificate.certificate().getEncoded()),
          clientNonce);
    }
  }

  @FunctionalInterface
  private interface SignedData {
    byte[] bytes(byte[] serverNonce, byte[] clientNonce) throws Exception;
  }

  private static byte[] sha256(byte[] bytes) throws Exception {
    return MessageDigest.getInstance("SHA-256").digest(bytes);
  }

  private static Optional<EndpointDescription> selectSecuredEndpoint(
      OpcUaServer server, ByteString thumbprint) {

    EndpointSelectionKey key =
        EndpointSelectionKey.of(
            TransportProfile.TCP_UASC_UABINARY,
            endpointUrl("/test"),
            SecurityPolicy.Basic256Sha256,
            MessageSecurityMode.SignAndEncrypt,
            thumbprint);

    return server.getApplicationContext().selectEndpoint(key, endpointUrl("/test"));
  }

  private static EndpointSelectionKey noneKey(String path) {
    return EndpointSelectionKey.of(
        TransportProfile.TCP_UASC_UABINARY,
        endpointUrl(path),
        SecurityPolicy.None,
        MessageSecurityMode.None,
        null);
  }

  private static EndpointDescription endpointForPath(OpcUaServer server, String path) {
    return server.getApplicationContext().getEndpointDescriptions().stream()
        .filter(e -> e.getEndpointUrl().endsWith(path))
        .findFirst()
        .orElseThrow();
  }

  private static String endpointUrl(String path) {
    return "opc.tcp://localhost:4840" + path;
  }

  private static EndpointConfig securedEndpoint(NodeId groupId, UserTokenPolicy... tokenPolicies) {
    return securedEndpoint(groupId, SecurityPolicy.Basic256Sha256, tokenPolicies);
  }

  private static EndpointConfig securedEndpoint(
      NodeId groupId, SecurityPolicy securityPolicy, UserTokenPolicy... tokenPolicies) {

    return EndpointConfig.newBuilder()
        .setBindAddress("localhost")
        .setBindPort(4840)
        .setHostname("localhost")
        .setPath("/test")
        .setSecurityPolicy(securityPolicy)
        .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
        .setEndpointCertificateConfig(
            EndpointCertificateConfig.newBuilder().setCertificateGroupId(groupId).build())
        .addTokenPolicies(tokenPolicies)
        .build();
  }

  private static EndpointConfig securedEndpointForHostname(String hostname, NodeId groupId) {
    return EndpointConfig.newBuilder()
        .setBindAddress("localhost")
        .setBindPort(4840)
        .setHostname(hostname)
        .setPath("/test")
        .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
        .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
        .setEndpointCertificateConfig(
            EndpointCertificateConfig.newBuilder().setCertificateGroupId(groupId).build())
        .addTokenPolicy(ANONYMOUS_POLICY)
        .build();
  }

  private static EndpointConfig noneEndpoint(String path) {
    return EndpointConfig.newBuilder()
        .setBindAddress("localhost")
        .setBindPort(4840)
        .setHostname("localhost")
        .setPath(path)
        .setSecurityPolicy(SecurityPolicy.None)
        .setSecurityMode(MessageSecurityMode.None)
        .addTokenPolicy(ANONYMOUS_POLICY)
        .build();
  }

  private static OpcUaServer server(
      CertificateManager certificateManager, List<EndpointConfig> endpoints) {

    OpcUaServerConfig config =
        OpcUaServerConfig.builder()
            .setEndpoints(new LinkedHashSet<>(endpoints))
            .setCertificateManager(certificateManager)
            .setApplicationUri("urn:test:server")
            .setProductUri("urn:test:product")
            .build();

    return new OpcUaServer(config, NO_OP_TRANSPORTS);
  }

  private static ServerSecureChannel noneChannel(long channelId) {
    var secureChannel = new ServerSecureChannel();
    secureChannel.setChannelId(channelId);
    secureChannel.setSecurityPolicy(SecurityPolicy.None);
    secureChannel.setMessageSecurityMode(MessageSecurityMode.None);
    return secureChannel;
  }

  private static ServerSecureChannel securedChannel(
      long channelId, CertificateMaterial serverCertificate) throws Exception {

    return securedChannel(channelId, serverCertificate, SecurityPolicy.Basic256Sha256);
  }

  private static ServerSecureChannel securedChannel(
      long channelId, CertificateMaterial serverCertificate, SecurityPolicy securityPolicy)
      throws Exception {

    var secureChannel = new ServerSecureChannel();
    secureChannel.setChannelId(channelId);
    secureChannel.setSecurityPolicy(securityPolicy);
    if (securityPolicy.getProfile().secureChannelEnhancements()) {
      secureChannel.setChannelThumbprint(CHANNEL_THUMBPRINT);
    }
    secureChannel.setMessageSecurityMode(MessageSecurityMode.SignAndEncrypt);
    secureChannel.setLocalCertificate(serverCertificate.certificate());
    secureChannel.setLocalCertificateChain(serverCertificate.certificateChain());
    secureChannel.setKeyPair(serverCertificate.keyPair());
    secureChannel.setRemoteCertificate(clientCertificate.byteString().bytesOrEmpty());
    return secureChannel;
  }

  private static CreateSessionRequest createSessionRequest(ByteString clientCertificateBytes) {
    return new CreateSessionRequest(
        requestHeader(NodeId.NULL_VALUE),
        new ApplicationDescription(
            "urn:test:client",
            "urn:test:client-product",
            LocalizedText.english("client"),
            ApplicationType.Client,
            null,
            null,
            null),
        null,
        endpointUrl("/test"),
        "test-session",
        NonceUtil.generateNonce(32),
        clientCertificateBytes,
        60_000.0,
        uint(0));
  }

  private static ActivateSessionRequest signedActivation(
      NodeId token,
      ByteString serverNonce,
      ByteString clientNonce,
      @Nullable ExtensionObject additionalHeader)
      throws Exception {
    return signedActivation(
        token, serverNonce, clientNonce, certificateA.byteString(), additionalHeader);
  }

  private static ActivateSessionRequest signedActivationOver(
      ByteString serverCertificate, NodeId token, ByteString serverNonce, ByteString clientNonce)
      throws Exception {
    return signedActivation(token, serverNonce, clientNonce, serverCertificate, null);
  }

  private static ActivateSessionRequest signedActivation(
      NodeId token,
      ByteString serverNonce,
      ByteString clientNonce,
      ByteString serverCertificate,
      @Nullable ExtensionObject additionalHeader)
      throws Exception {
    byte[] data =
        ChannelBoundSignatureData.clientSignatureData(
            SecurityPolicy.Basic256Sha256.getProfile(),
            ByteString.NULL_VALUE,
            serverNonce,
            serverCertificate,
            certificateA.byteString(),
            clientCertificate.byteString(),
            clientNonce);
    SignatureData signature =
        ChannelBoundSignatureData.sign(
            SecurityPolicy.Basic256Sha256, clientCertificate.keyPair().getPrivate(), data);
    return new ActivateSessionRequest(
        new RequestHeader(
            token, DateTime.now(), uint(1), uint(0), null, uint(10_000), additionalHeader),
        signature,
        null,
        null,
        null,
        new SignatureData(null, null));
  }

  private static ActivateSessionRequest activateSessionRequest(NodeId authToken) {
    return new ActivateSessionRequest(
        requestHeader(authToken),
        new SignatureData(null, null),
        null,
        null,
        null,
        new SignatureData(null, null));
  }

  private static RequestHeader requestHeader(NodeId authToken) {
    return new RequestHeader(authToken, DateTime.now(), uint(1), uint(0), null, uint(10_000), null);
  }

  private static CertificateMaterial rsaCertificate(String commonName) throws Exception {
    KeyPair keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate certificate =
        new SelfSignedCertificateBuilder(keyPair)
            .setCommonName(commonName)
            .setOrganization("Eclipse Milo")
            .setApplicationUri("urn:test:" + commonName)
            .build();

    return new CertificateMaterial(
        NodeIds.RsaSha256ApplicationCertificateType, keyPair, new X509Certificate[] {certificate});
  }

  private static CertificateManager manager(TestCertificateGroup... groups) {
    var certificateManager = new DefaultCertificateManager();

    for (TestCertificateGroup group : groups) {
      certificateManager.addCertificateGroup(group.certificateGroupId(), group);
    }

    return certificateManager;
  }

  private static TestCertificateGroup group(NodeId groupId, CertificateMaterial... certificates) {
    return new TestCertificateGroup(groupId, List.of(certificates));
  }

  private record CertificateMaterial(
      NodeId certificateTypeId, KeyPair keyPair, X509Certificate[] certificateChain) {

    X509Certificate certificate() {
      return certificateChain[0];
    }

    ByteString byteString() throws Exception {
      return ByteString.of(certificate().getEncoded());
    }

    byte[] leafBytes() throws Exception {
      return certificate().getEncoded();
    }

    byte[] chainBytes() throws Exception {
      byte[][] encodings = new byte[certificateChain.length][];
      for (int i = 0; i < certificateChain.length; i++) {
        encodings[i] = certificateChain[i].getEncoded();
      }
      return Bytes.concat(encodings);
    }
  }

  /** A {@link ServiceRequestContext} standing in for a request arriving over a UASC channel. */
  private static final class TestServiceRequestContext implements ServiceRequestContext {

    private final String endpointUrl;
    private final SecureChannel secureChannel;
    private final @Nullable EndpointDescription endpoint;

    private TestServiceRequestContext(
        String endpointUrl, SecureChannel secureChannel, @Nullable EndpointDescription endpoint) {

      this.endpointUrl = endpointUrl;
      this.secureChannel = secureChannel;
      this.endpoint = endpoint;
    }

    @Override
    public String getEndpointUrl() {
      return endpointUrl;
    }

    @Override
    public TransportProfile getTransportProfile() {
      return TransportProfile.TCP_UASC_UABINARY;
    }

    @Override
    public Channel getChannel() {
      throw new UnsupportedOperationException();
    }

    @Override
    public SecureChannel getSecureChannel() {
      return secureChannel;
    }

    @Override
    public Optional<EndpointDescription> getEndpoint() {
      return Optional.ofNullable(endpoint);
    }

    @Override
    public Long receivedAtNanos() {
      return System.nanoTime();
    }

    @Override
    public InetAddress clientAddress() {
      return InetAddress.getLoopbackAddress();
    }
  }

  private record TestCertificateGroup(
      NodeId certificateGroupId, Map<NodeId, CertificateMaterial> certificates)
      implements CertificateGroup {

    private TestCertificateGroup(
        NodeId certificateGroupId, List<CertificateMaterial> certificates) {
      this(certificateGroupId, toCertificateMap(certificates));
    }

    private static Map<NodeId, CertificateMaterial> toCertificateMap(
        List<CertificateMaterial> certificates) {

      Map<NodeId, CertificateMaterial> certificateMap =
          certificates.stream()
              .collect(
                  Collectors.toMap(
                      CertificateMaterial::certificateTypeId,
                      Function.identity(),
                      (left, right) -> right,
                      LinkedHashMap::new));

      return Collections.unmodifiableMap(certificateMap);
    }

    @Override
    public List<NodeId> getSupportedCertificateTypeIds() {
      return List.copyOf(certificates.keySet());
    }

    @Override
    public TrustListManager getTrustListManager() {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean hasCertificate(NodeId certificateTypeId) {
      return certificates.containsKey(certificateTypeId);
    }

    @Override
    public List<Entry> getCertificateEntries() {
      return certificates.values().stream()
          .map(
              certificate ->
                  new CertificateGroup.Entry(
                      certificate.certificateTypeId(), certificate.certificateChain()))
          .toList();
    }

    @Override
    public Optional<KeyPair> getKeyPair(NodeId certificateTypeId) {
      return Optional.ofNullable(certificates.get(certificateTypeId))
          .map(CertificateMaterial::keyPair);
    }

    @Override
    public Optional<X509Certificate[]> getCertificateChain(NodeId certificateTypeId) {
      return Optional.ofNullable(certificates.get(certificateTypeId))
          .map(CertificateMaterial::certificateChain)
          .map(X509Certificate[]::clone);
    }

    @Override
    public void updateCertificate(
        NodeId certificateTypeId, KeyPair keyPair, X509Certificate[] certificateChain) {
      throw new UnsupportedOperationException();
    }

    @Override
    public CertificateQuarantine getCertificateQuarantine() {
      throw new UnsupportedOperationException();
    }

    @Override
    public CertificateValidator getCertificateValidator() {
      return new CertificateValidator.InsecureCertificateValidator();
    }
  }
}
