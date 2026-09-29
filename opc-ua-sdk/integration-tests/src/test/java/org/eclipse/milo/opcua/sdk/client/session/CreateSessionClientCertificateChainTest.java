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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.common.primitives.Bytes;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
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
import org.eclipse.milo.opcua.sdk.client.OpcUaSession;
import org.eclipse.milo.opcua.sdk.client.SessionActivityListener;
import org.eclipse.milo.opcua.sdk.client.UaSession;
import org.eclipse.milo.opcua.sdk.server.EndpointCertificateConfig;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig;
import org.eclipse.milo.opcua.sdk.server.identity.AnonymousIdentityValidator;
import org.eclipse.milo.opcua.sdk.test.TestPortAllocator;
import org.eclipse.milo.opcua.stack.core.NodeIds;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.validation.ValidationCheck;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * A client with a CA-issued application certificate sends the same certificate chain in the
 * OpenSecureChannel SenderCertificate and the CreateSession clientCertificate, while the Session
 * identity stays the client leaf.
 *
 * <p>Part 4 §5.7.2.2 requires CreateSession to use the application certificate that opened the
 * SecureChannel, and Part 6 §6.2.6 and §6.7.2.3 let both fields carry optional issuer certificates.
 * The Milo server signs the client leaf, so these tests also exercise the leaf-first CreateSession
 * signature check with a chain in the request.
 */
class CreateSessionClientCertificateChainTest {

  private static final String SERVER_URI = "urn:milo:client-chain:server";
  private static final String CLIENT_URI = "urn:milo:client-chain:client";

  static Stream<SecurityPolicy> policies() {
    return Stream.of(SecurityPolicy.Basic256Sha256, SecurityPolicy.ECC_nistP256_AesGcm);
  }

  @ParameterizedTest
  @MethodSource("policies")
  void createSessionSendsTheOpenSecureChannelCertificateChain(SecurityPolicy policy)
      throws Exception {

    try (var f = new Fixture(policy)) {
      f.connect();

      byte[] chain = Bytes.concat(f.clientCertificate.getEncoded(), f.ca.getEncoded());
      assertArrayEquals(chain, f.senderCertificates.poll().bytesOrEmpty());
      assertArrayEquals(chain, f.createSessionCertificates.poll().bytesOrEmpty());
      assertEquals(
          ByteString.of(f.clientCertificate.getEncoded()),
          f.client.getSession().getClientCertificate().orElseThrow());
    }
  }

  // Reactivating on a new SecureChannel must not create a new Session when the client leaf is
  // unchanged, even though the Session was created with a chain.
  @ParameterizedTest
  @MethodSource("policies")
  void reactivationKeepsSessionWithUnchangedClientChain(SecurityPolicy policy) throws Exception {
    try (var f = new Fixture(policy)) {
      f.connect();
      OpcUaSession original = f.client.getSession();

      OpcUaSession recovered = f.reconnect();

      assertEquals(original.getSessionId(), recovered.getSessionId());
      assertNull(f.createSessionCertificates.poll());
      f.read();
    }
  }

  // Part 4 §6.7 requires a new Session only when the application certificate changes. Dropping
  // the issuer from the configured chain leaves the leaf, and so the client identity, unchanged.
  @ParameterizedTest
  @MethodSource("policies")
  void reactivationKeepsSessionWhenOnlyIssuerCertificatesChange(SecurityPolicy policy)
      throws Exception {

    try (var f = new Fixture(policy)) {
      f.connect();
      OpcUaSession original = f.client.getSession();

      f.clientGroup.updateCertificate(
          f.certificateType, f.clientKey, new X509Certificate[] {f.clientCertificate});
      // An explicit connect refreshes the selected local identity without closing the Session.
      f.client.connectAsync().get(5, TimeUnit.SECONDS);

      OpcUaSession recovered = f.reconnect();

      assertEquals(original.getSessionId(), recovered.getSessionId());
      assertArrayEquals(
          f.clientCertificate.getEncoded(), f.senderCertificates.poll().bytesOrEmpty());
      assertNull(f.createSessionCertificates.poll());
      f.read();
    }
  }

  // A changed client leaf still requires a new Session, which then carries the new chain.
  @ParameterizedTest
  @MethodSource("policies")
  void reactivationCreatesNewSessionWhenClientLeafChanges(SecurityPolicy policy) throws Exception {
    try (var f = new Fixture(policy)) {
      f.connect();
      OpcUaSession original = f.client.getSession();

      KeyPair replacementKey = f.keyPair();
      X509Certificate replacement = signedCertificate(replacementKey, CLIENT_URI, f.caKey, f.ca);
      f.clientGroup.updateCertificate(
          f.certificateType, replacementKey, new X509Certificate[] {replacement, f.ca});
      f.client.connectAsync().get(5, TimeUnit.SECONDS);

      OpcUaSession recovered = f.reconnect();

      assertNotEquals(original.getSessionId(), recovered.getSessionId());
      byte[] chain = Bytes.concat(replacement.getEncoded(), f.ca.getEncoded());
      assertArrayEquals(chain, f.senderCertificates.poll().bytesOrEmpty());
      assertArrayEquals(chain, f.createSessionCertificates.poll().bytesOrEmpty());
      f.read();
    }
  }

  private static final class Fixture implements AutoCloseable {
    final SecurityPolicy policy;
    final NodeId certificateType;
    final KeyPair caKey;
    final X509Certificate ca;
    final KeyPair clientKey;
    final X509Certificate clientCertificate;
    final DefaultCertificateGroup clientGroup;
    final OpcUaServer server;
    final OpcUaClient client;

    /** SenderCertificate of each OpenSecureChannel request, read from the encoded chunk. */
    final LinkedBlockingQueue<ByteString> senderCertificates = new LinkedBlockingQueue<>();

    /** clientCertificate of each CreateSession request. */
    final LinkedBlockingQueue<ByteString> createSessionCertificates = new LinkedBlockingQueue<>();

    final LinkedBlockingQueue<OpcUaSession> activeSessions = new LinkedBlockingQueue<>();

    Fixture(SecurityPolicy policy) throws Exception {
      this.policy = policy;
      certificateType =
          policy == SecurityPolicy.Basic256Sha256
              ? NodeIds.RsaSha256ApplicationCertificateType
              : NodeIds.EccNistP256ApplicationCertificateType;
      caKey = keyPair();
      ca = caCertificate(caKey);

      KeyPair serverKey = keyPair();
      DefaultCertificateGroup serverGroup =
          group(serverKey, signedCertificate(serverKey, SERVER_URI, caKey, ca));

      int port = TestPortAllocator.allocatePort();
      EndpointConfig endpoint =
          EndpointConfig.newBuilder()
              .setBindAddress("localhost")
              .setHostname("localhost")
              .setBindPort(port)
              .setPath("/client-chain")
              .setEndpointCertificateConfig(
                  EndpointCertificateConfig.newBuilder()
                      .setCertificateTypeId(certificateType)
                      .build())
              .setSecurityPolicy(policy)
              .setSecurityMode(MessageSecurityMode.SignAndEncrypt)
              .setTransportProfile(TransportProfile.TCP_UASC_UABINARY)
              .addTokenPolicy(
                  new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null))
              .build();

      server =
          new OpcUaServer(
              OpcUaServerConfig.builder()
                  .setApplicationUri(SERVER_URI)
                  .setApplicationName(LocalizedText.english("Client chain test server"))
                  .setProductUri("urn:milo:client-chain")
                  .setEndpoints(Set.of(endpoint))
                  .setCertificateManager(new DefaultCertificateManager(serverGroup))
                  .setIdentityValidator(AnonymousIdentityValidator.INSTANCE)
                  .build(),
              profile ->
                  new OpcTcpServerTransport(OpcTcpServerTransportConfig.newBuilder().build()));
      server.startup().get(5, TimeUnit.SECONDS);

      EndpointDescription selected =
          DiscoveryClient.getEndpoints(endpoint.getEndpointUrl()).get(5, TimeUnit.SECONDS).stream()
              .filter(e -> policy.getUri().equals(e.getSecurityPolicyUri()))
              .findFirst()
              .orElseThrow();

      clientKey = keyPair();
      clientCertificate = signedCertificate(clientKey, CLIENT_URI, caKey, ca);
      clientGroup = group(clientKey, clientCertificate);

      var transportConfig =
          OpcTcpClientTransportConfig.newBuilder()
              .setChannelPipelineCustomizer(
                  p -> p.addFirst(new OpenSecureChannelRecorder(senderCertificates)))
              .build();

      var transport =
          new OpcTcpClientTransport(transportConfig) {
            @Override
            public CompletableFuture<UaResponseMessageType> sendRequestMessage(
                Callable<UaRequestMessageType> requestSupplier, long channelTimeoutMillis) {

              return super.sendRequestMessage(
                  () -> {
                    UaRequestMessageType request = requestSupplier.call();
                    if (request instanceof CreateSessionRequest createSession) {
                      createSessionCertificates.add(createSession.getClientCertificate());
                    }
                    return request;
                  },
                  channelTimeoutMillis);
            }
          };

      client =
          new OpcUaClient(
              OpcUaClientConfig.builder()
                  .setApplicationUri(CLIENT_URI)
                  .setApplicationName(LocalizedText.english("Client chain test client"))
                  .setEndpoint(selected)
                  .setCertificateGroup(clientGroup)
                  .build(),
              transport);

      client.addSessionActivityListener(
          new SessionActivityListener() {
            @Override
            public void onSessionActive(UaSession session) {
              activeSessions.add((OpcUaSession) session);
            }

            @Override
            public void onSessionInactive(UaSession session) {}
          });
    }

    void connect() throws Exception {
      client.connectAsync().get(10, TimeUnit.SECONDS);
      assertNotNull(activeSessions.poll(5, TimeUnit.SECONDS));
      read();
    }

    /**
     * Drop the current SecureChannel and wait for the Session to become active again on a new one.
     *
     * @return the Session that became active after the reconnect.
     */
    OpcUaSession reconnect() throws Exception {
      senderCertificates.clear();
      createSessionCertificates.clear();
      activeSessions.clear();

      ((OpcTcpClientTransport) client.getTransport())
          .getChannelFsm()
          .getChannel()
          .get(5, TimeUnit.SECONDS)
          .close()
          .sync();

      OpcUaSession recovered = activeSessions.poll(15, TimeUnit.SECONDS);
      assertNotNull(recovered);
      return recovered;
    }

    void read() throws Exception {
      List<DataValue> values =
          client
              .readValuesAsync(
                  0, TimestampsToReturn.Neither, List.of(NodeIds.Server_ServerStatus_CurrentTime))
              .get(5, TimeUnit.SECONDS);
      assertTrue(values.get(0).getStatusCode().isGood());
    }

    DefaultCertificateGroup group(KeyPair key, X509Certificate certificate) throws Exception {
      var trust = new MemoryTrustListManager();
      trust.setTrustedCertificates(List.of(ca));
      var quarantine = new MemoryCertificateQuarantine();
      var validator =
          new DefaultClientCertificateValidator(
              trust,
              EnumSet.of(ValidationCheck.APPLICATION_URI, ValidationCheck.HOSTNAME),
              quarantine);
      var group =
          new DefaultCertificateGroup(
              trust, new MemoryCertificateStore(), quarantine, validator, List.of(certificateType));
      group.updateCertificate(certificateType, key, new X509Certificate[] {certificate, ca});
      return group;
    }

    KeyPair keyPair() throws Exception {
      return policy == SecurityPolicy.Basic256Sha256
          ? SelfSignedCertificateGenerator.generateRsaKeyPair(2048)
          : SelfSignedCertificateGenerator.generateNistP256KeyPair();
    }

    @Override
    public void close() throws Exception {
      try {
        client.disconnectAsync().get(8, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(5, TimeUnit.SECONDS);
      }
    }
  }

  /**
   * Records the SenderCertificate from each outgoing OpenSecureChannel chunk.
   *
   * <p>The asymmetric security header is not encrypted. Part 6 §6.7.2.3 lays it out after the
   * 12-byte message header as SecurityPolicyUri (String) then SenderCertificate (ByteString), each
   * with an Int32 length prefix.
   */
  private static final class OpenSecureChannelRecorder extends ChannelOutboundHandlerAdapter {

    private final LinkedBlockingQueue<ByteString> senderCertificates;

    OpenSecureChannelRecorder(LinkedBlockingQueue<ByteString> senderCertificates) {
      this.senderCertificates = senderCertificates;
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise)
        throws Exception {

      if (msg instanceof ByteBuf buf && isOpenSecureChannel(buf)) {
        int index = buf.readerIndex() + 12;
        int uriLength = buf.getIntLE(index);
        index += 4 + uriLength;
        int certificateLength = buf.getIntLE(index);
        var certificate = new byte[certificateLength];
        buf.getBytes(index + 4, certificate);
        senderCertificates.add(ByteString.of(certificate));
      }

      super.write(ctx, msg, promise);
    }

    private static boolean isOpenSecureChannel(ByteBuf buf) {
      return buf.readableBytes() >= 3
          && buf.toString(buf.readerIndex(), 3, StandardCharsets.US_ASCII).equals("OPN");
    }
  }

  private static X509Certificate caCertificate(KeyPair key) throws Exception {
    var name = new X500Name("CN=Client chain test CA");
    var builder =
        new JcaX509v3CertificateBuilder(
            name,
            serial(),
            new Date(System.currentTimeMillis() - 60000),
            new Date(System.currentTimeMillis() + 86400000),
            name,
            key.getPublic());
    builder.addExtension(Extension.basicConstraints, true, new BasicConstraints(true));
    builder.addExtension(
        Extension.keyUsage, true, new KeyUsage(KeyUsage.keyCertSign | KeyUsage.cRLSign));
    return sign(builder, key);
  }

  private static X509Certificate signedCertificate(
      KeyPair key, String uri, KeyPair caKey, X509Certificate ca) throws Exception {

    SelfSignedCertificateBuilder leafBuilder =
        key.getPublic().getAlgorithm().equals("RSA")
            ? new SelfSignedCertificateBuilder(key)
            : SelfSignedCertificateBuilder.forEccApplicationCertificate(key);
    X509Certificate template =
        leafBuilder
            .setCommonName("Client chain application")
            .setApplicationUri(uri)
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

    String algorithm =
        key.getPublic().getAlgorithm().equals("RSA") ? "SHA256withRSA" : "SHA256withECDSA";
    return new JcaX509CertificateConverter()
        .getCertificate(
            builder.build(new JcaContentSignerBuilder(algorithm).build(key.getPrivate())));
  }

  private static BigInteger serial() {
    return new BigInteger(120, new SecureRandom());
  }
}
