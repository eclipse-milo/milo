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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.netty.channel.Channel;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.KeyUsage;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.eclipse.milo.opcua.sdk.client.DiscoveryClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.SessionActivityListener;
import org.eclipse.milo.opcua.sdk.client.UaSession;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.identity.AnonymousIdentityValidator;
import org.eclipse.milo.opcua.sdk.test.TestPortAllocator;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.ChannelBoundSignatureData;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateGroup;
import org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager;
import org.eclipse.milo.opcua.stack.core.security.DefaultClientCertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateQuarantine;
import org.eclipse.milo.opcua.stack.core.security.MemoryCertificateStore;
import org.eclipse.milo.opcua.stack.core.security.MemoryTrustListManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.ActivateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Covers the legacy ActivateSession {@code clientSignature} a Milo client sends when the server
 * returns a certificate chain in {@code CreateSessionResponse.serverCertificate}.
 */
class LegacyClientSignatureTest {

  private static final String SERVER_URI = "urn:eclipse:milo:test:legacy-signature-server";
  private static final String CLIENT_URI = "urn:eclipse:milo:test:legacy-signature-client";

  /**
   * Part 4 §6.1.8 permits a legacy signature over the whole chain, but some servers verify only
   * {@code leaf | ServerNonce}. Milo's server accepts either, so this test checks the signatures
   * the client sent rather than whether activation succeeded. Reactivation after a channel loss
   * must sign the same leaf with the nonce from the previous ActivateSession response.
   */
  @ParameterizedTest(name = "serverSendsChain={0}")
  @ValueSource(booleans = {true, false})
  void legacyActivationAndReactivationSignServerLeafAndLatestNonce(boolean serverSendsChain)
      throws Exception {

    KeyPair caKey = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate ca = caCertificate(caKey);
    KeyPair serverKey = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate serverLeaf = issuedCertificate(serverKey, SERVER_URI, caKey, ca);
    KeyPair clientKey = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    X509Certificate clientCertificate = issuedCertificate(clientKey, CLIENT_URI, caKey, ca);

    ByteString leafBytes = ByteString.of(serverLeaf.getEncoded());
    var chain = new ByteArrayOutputStream();
    chain.write(serverLeaf.getEncoded());
    chain.write(ca.getEncoded());
    ByteString chainBytes = ByteString.of(chain.toByteArray());

    EndpointConfig endpointConfig =
        EndpointConfig.newBuilder()
            .setBindAddress("localhost")
            .setHostname("localhost")
            .setBindPort(TestPortAllocator.allocatePort())
            .setPath("/legacy-signature")
            .setSecurityPolicy(SecurityPolicy.Basic256Sha256)
            .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
            .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
            .addTokenPolicy(
                new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null))
            .build();

    OpcUaServer server = server(endpointConfig, serverKey, serverLeaf, ca);
    try {
      EndpointDescription endpoint =
          DiscoveryClient.getEndpoints(endpointConfig.getEndpointUrl())
              .get(5, TimeUnit.SECONDS)
              .stream()
              .filter(e -> SecurityPolicy.Basic256Sha256.getUri().equals(e.getSecurityPolicyUri()))
              .findFirst()
              .orElseThrow();

      // Milo's server returns only the leaf, so the chain case substitutes leaf | CA in the
      // CreateSession response before the client sees it.
      var transport =
          new RecordingTransport(
              OpcTcpClientTransportConfig.newBuilder().build(),
              serverSendsChain ? chainBytes : null);
      var sessionsActivated = new LinkedBlockingQueue<UaSession>();
      var client =
          new OpcUaClient(
              OpcUaClientConfig.builder()
                  .setApplicationUri(CLIENT_URI)
                  .setEndpoint(endpoint)
                  .setCertificateGroup(certificateGroup(clientKey, clientCertificate, ca))
                  .setRequestTimeout(uint(5000))
                  .build(),
              transport);
      client.addSessionActivityListener(
          new SessionActivityListener() {
            @Override
            public void onSessionActive(UaSession session) {
              sessionsActivated.add(session);
            }
          });

      try {
        client.connectAsync().get(10, TimeUnit.SECONDS);
        assertNotNull(sessionsActivated.poll(5, TimeUnit.SECONDS));

        Channel channel = transport.getChannelFsm().getChannel().get(5, TimeUnit.SECONDS);
        channel.close().sync();
        assertNotNull(sessionsActivated.poll(25, TimeUnit.SECONDS), "session was not reactivated");

        CreateSessionResponse created = transport.createSessionResponses.poll();
        assertNotNull(created);
        assertNull(
            transport.createSessionResponses.poll(), "session was recreated, not reactivated");
        assertEquals(
            serverSendsChain ? 2 : 1,
            CertificateUtil.decodeCertificates(created.getServerCertificate().bytesOrEmpty())
                .size(),
            "certificates in the CreateSession response the client received");

        ActivateSessionRequest activation = transport.activateSessionRequests.poll();
        ActivateSessionResponse activated = transport.activateSessionResponses.poll();
        ActivateSessionRequest reactivation = transport.activateSessionRequests.poll();
        assertNotNull(activation);
        assertNotNull(activated);
        assertNotNull(reactivation);

        assertSignedOver(clientCertificate, activation, leafBytes, created.getServerNonce());
        assertSignedOver(clientCertificate, reactivation, leafBytes, activated.getServerNonce());

        if (serverSendsChain) {
          assertNotSignedOver(clientCertificate, activation, chainBytes, created.getServerNonce());
          assertNotSignedOver(
              clientCertificate, reactivation, chainBytes, activated.getServerNonce());
        }
      } finally {
        client.disconnectAsync().get(5, TimeUnit.SECONDS);
      }
    } finally {
      server.shutdown().get(5, TimeUnit.SECONDS);
    }
  }

  private static void assertSignedOver(
      X509Certificate clientCertificate,
      ActivateSessionRequest request,
      ByteString serverCertificate,
      ByteString serverNonce) {

    assertDoesNotThrow(
        () ->
            ChannelBoundSignatureData.verify(
                SecurityPolicy.Basic256Sha256,
                clientCertificate,
                concat(serverCertificate, serverNonce),
                request.getClientSignature()),
        "clientSignature must cover serverCertificate | serverNonce");
  }

  private static void assertNotSignedOver(
      X509Certificate clientCertificate,
      ActivateSessionRequest request,
      ByteString serverCertificate,
      ByteString serverNonce) {

    assertThrows(
        UaException.class,
        () ->
            ChannelBoundSignatureData.verify(
                SecurityPolicy.Basic256Sha256,
                clientCertificate,
                concat(serverCertificate, serverNonce),
                request.getClientSignature()),
        "clientSignature must not cover the full chain");
  }

  private static byte[] concat(ByteString first, ByteString second) {
    var bytes = new ByteArrayOutputStream();
    bytes.writeBytes(first.bytesOrEmpty());
    bytes.writeBytes(second.bytesOrEmpty());
    return bytes.toByteArray();
  }

  private static OpcUaServer server(
      EndpointConfig endpoint, KeyPair serverKey, X509Certificate serverLeaf, X509Certificate ca)
      throws Exception {

    OpcUaServer server =
        new OpcUaServer(
            OpcUaServerConfig.builder()
                .setApplicationUri(SERVER_URI)
                .setApplicationName(LocalizedText.english("Legacy signature test server"))
                .setProductUri("urn:eclipse:milo:test:legacy-signature")
                .setEndpoints(Set.of(endpoint))
                .setCertificateManager(
                    new DefaultCertificateManager(certificateGroup(serverKey, serverLeaf, ca)))
                .setIdentityValidator(AnonymousIdentityValidator.INSTANCE)
                .build(),
            transportProfile ->
                new OpcTcpServerTransport(OpcTcpServerTransportConfig.newBuilder().build()));

    server.startup().get(5, TimeUnit.SECONDS);

    return server;
  }

  private static DefaultCertificateGroup certificateGroup(
      KeyPair key, X509Certificate certificate, X509Certificate ca) throws Exception {

    var trustList = new MemoryTrustListManager();
    trustList.setTrustedCertificates(List.of(ca));
    var quarantine = new MemoryCertificateQuarantine();
    var group =
        new DefaultCertificateGroup(
            trustList,
            new MemoryCertificateStore(),
            quarantine,
            new DefaultClientCertificateValidator(trustList, quarantine),
            List.of(NodeIds.RsaSha256ApplicationCertificateType));
    group.updateCertificate(
        NodeIds.RsaSha256ApplicationCertificateType, key, new X509Certificate[] {certificate, ca});

    return group;
  }

  private static X509Certificate caCertificate(KeyPair key) throws Exception {
    var name = new X500Name("CN=Legacy signature test CA");
    var builder =
        new JcaX509v3CertificateBuilder(
            name,
            serial(),
            new Date(System.currentTimeMillis() - 60_000),
            new Date(System.currentTimeMillis() + 86_400_000),
            name,
            key.getPublic());
    builder.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
    builder.addExtension(
        Extension.keyUsage, true, new KeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign));

    return sign(builder, key);
  }

  private static X509Certificate issuedCertificate(
      KeyPair key, String applicationUri, KeyPair caKey, X509Certificate ca) throws Exception {

    // Borrow the application-certificate extensions from a self-signed template, then reissue
    // the certificate under the CA.
    X509Certificate template =
        new SelfSignedCertificateBuilder(key)
            .setCommonName("Legacy signature application")
            .setApplicationUri(applicationUri)
            .addDnsName("localhost")
            .build();
    var holder = new X509CertificateHolder(template.getEncoded());
    var builder =
        new JcaX509v3CertificateBuilder(
            ca,
            serial(),
            template.getNotBefore(),
            template.getNotAfter(),
            template.getSubjectX500Principal(),
            key.getPublic());
    for (var oid : holder.getExtensions().getExtensionOIDs()) {
      builder.addExtension(holder.getExtension(oid));
    }

    return sign(builder, caKey);
  }

  private static X509Certificate sign(X509v3CertificateBuilder builder, KeyPair key)
      throws Exception {

    return new JcaX509CertificateConverter()
        .getCertificate(
            builder.build(new JcaContentSignerBuilder("SHA256withRSA").build(key.getPrivate())));
  }

  private static BigInteger serial() {
    return new BigInteger(120, new SecureRandom());
  }

  /**
   * Records session service traffic and optionally replaces the CreateSession server certificate.
   */
  private static final class RecordingTransport extends OpcTcpClientTransport {

    final LinkedBlockingQueue<CreateSessionResponse> createSessionResponses =
        new LinkedBlockingQueue<>();
    final LinkedBlockingQueue<ActivateSessionRequest> activateSessionRequests =
        new LinkedBlockingQueue<>();
    final LinkedBlockingQueue<ActivateSessionResponse> activateSessionResponses =
        new LinkedBlockingQueue<>();

    private final @Nullable ByteString createSessionCertificate;

    RecordingTransport(
        OpcTcpClientTransportConfig config, @Nullable ByteString createSessionCertificate) {
      super(config);
      this.createSessionCertificate = createSessionCertificate;
    }

    @Override
    public CompletableFuture<UaResponseMessageType> sendRequestMessage(
        UaRequestMessageType request) {

      return record(request, super.sendRequestMessage(request));
    }

    @Override
    protected CompletableFuture<UaResponseMessageType> sendRequestMessage(
        UaRequestMessageType request, Channel channel) {

      return record(request, super.sendRequestMessage(request, channel));
    }

    private CompletableFuture<UaResponseMessageType> record(
        UaRequestMessageType request, CompletableFuture<UaResponseMessageType> response) {

      if (request instanceof ActivateSessionRequest activate) {
        activateSessionRequests.add(activate);
      }

      return response.thenApply(
          message -> {
            if (message instanceof CreateSessionResponse created) {
              CreateSessionResponse replaced = withServerCertificate(created);
              createSessionResponses.add(replaced);
              return replaced;
            } else if (message instanceof ActivateSessionResponse activated) {
              activateSessionResponses.add(activated);
            }
            return message;
          });
    }

    private CreateSessionResponse withServerCertificate(CreateSessionResponse response) {
      if (createSessionCertificate == null) {
        return response;
      }

      return new CreateSessionResponse(
          response.getResponseHeader(),
          response.getSessionId(),
          response.getAuthenticationToken(),
          response.getRevisedSessionTimeout(),
          response.getServerNonce(),
          createSessionCertificate,
          response.getServerEndpoints(),
          response.getServerSoftwareCertificates(),
          response.getServerSignature(),
          response.getMaxRequestMessageSize());
    }
  }
}
