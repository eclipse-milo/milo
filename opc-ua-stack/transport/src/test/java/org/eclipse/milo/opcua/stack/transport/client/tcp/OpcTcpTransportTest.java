/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client.tcp;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.KeyPair;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.channel.ChannelParameters;
import org.eclipse.milo.opcua.stack.core.channel.ChannelSecurity;
import org.eclipse.milo.opcua.stack.core.channel.ChunkEncoder;
import org.eclipse.milo.opcua.stack.core.channel.ChunkEncoder.EncodedMessage;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.channel.SecurityKeysListener;
import org.eclipse.milo.opcua.stack.core.channel.SecurityKeyset;
import org.eclipse.milo.opcua.stack.core.channel.messages.AcknowledgeMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.HelloMessage;
import org.eclipse.milo.opcua.stack.core.channel.messages.MessageType;
import org.eclipse.milo.opcua.stack.core.channel.messages.TcpMessageEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.SecurityTokenRequestType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.OpenSecureChannelRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.uasc.ClientSecureChannel;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class OpcTcpTransportTest extends SecurityFixture {

  private static final Logger LOGGER = LoggerFactory.getLogger(OpcTcpTransportTest.class);

  static {
    // Required for SecurityPolicy.Aes256_Sha256_RsaPss
    Security.addProvider(new BouncyCastleProvider());
  }

  private static Stream<Arguments> provideSecurityParameters() {
    return Stream.of(
        Arguments.of(SecurityPolicy.None, MessageSecurityMode.None),
        Arguments.of(SecurityPolicy.Basic256Sha256, MessageSecurityMode.Sign),
        Arguments.of(SecurityPolicy.Basic256Sha256, MessageSecurityMode.SignAndEncrypt),
        Arguments.of(SecurityPolicy.Aes128_Sha256_RsaOaep, MessageSecurityMode.Sign),
        Arguments.of(SecurityPolicy.Aes128_Sha256_RsaOaep, MessageSecurityMode.SignAndEncrypt),
        Arguments.of(SecurityPolicy.Aes256_Sha256_RsaPss, MessageSecurityMode.Sign),
        Arguments.of(SecurityPolicy.Aes256_Sha256_RsaPss, MessageSecurityMode.SignAndEncrypt));
  }

  private static Stream<Arguments> provideM1EccSecurityParameters() throws Exception {
    return Stream.of(
        Arguments.of(
            SecurityPolicy.ECC_nistP256_AesGcm,
            nistP256Certificate("ecc-nist-client"),
            nistP256Certificate("ecc-nist-server")),
        Arguments.of(
            SecurityPolicy.ECC_curve25519_ChaChaPoly,
            ed25519Certificate("ecc-curve25519-client"),
            ed25519Certificate("ecc-curve25519-server")));
  }

  @ParameterizedTest
  @MethodSource("provideSecurityParameters")
  void connectThenDisconnect(SecurityPolicy securityPolicy, MessageSecurityMode messageSecurityMode)
      throws Exception {
    OpcServerTransport serverTransport = bindServerTransport(securityPolicy, messageSecurityMode);

    var applicationContext =
        new ClientApplicationContext() {
          @Override
          public EndpointDescription getEndpoint() {
            return newEndpointDescription(securityPolicy, messageSecurityMode);
          }

          @Override
          public Optional<KeyPair> getKeyPair() {
            return Optional.of(clientKeyPair);
          }

          @Override
          public Optional<X509Certificate> getCertificate() {
            return Optional.of(clientCertificate);
          }

          @Override
          public Optional<X509Certificate[]> getCertificateChain() {
            return Optional.of(new X509Certificate[] {clientCertificate});
          }

          @Override
          public CertificateValidator getCertificateValidator() {
            return new TestServerCertificateValidator(serverCertificate);
          }

          @Override
          public EncodingContext getEncodingContext() {
            return DefaultEncodingContext.INSTANCE;
          }

          @Override
          public UInteger getRequestTimeout() {
            return uint(5_000);
          }
        };

    OpcTcpClientTransportConfig config = OpcTcpClientTransportConfig.newBuilder().build();

    var transport = new OpcTcpClientTransport(config);

    LOGGER.debug("connecting...");
    transport.connect(applicationContext).get();
    LOGGER.debug("connected");

    LOGGER.debug("disconnecting...");
    transport.disconnect().get();
    LOGGER.debug("disconnected");

    LOGGER.debug("unbinding server transport...");
    serverTransport.unbind();
    LOGGER.debug("server transport unbound");
  }

  @Test
  void openUnsecuredChannelAgainstSecuredEndpoint() throws Exception {
    // Opening a SecureChannel with no security should be allowed even if all the configured
    // endpoints require security. This is to give the receiving server application a chance
    // to allow unsecured Discovery services to be implemented even when security is otherwise
    // required.

    OpcServerTransport serverTransport =
        bindServerTransport(SecurityPolicy.Basic256Sha256, MessageSecurityMode.SignAndEncrypt);

    var applicationContext =
        new ClientApplicationContext() {
          @Override
          public EndpointDescription getEndpoint() {
            return newEndpointDescription(SecurityPolicy.None, MessageSecurityMode.None);
          }

          @Override
          public Optional<KeyPair> getKeyPair() {
            return Optional.of(clientKeyPair);
          }

          @Override
          public Optional<X509Certificate> getCertificate() {
            return Optional.of(clientCertificate);
          }

          @Override
          public Optional<X509Certificate[]> getCertificateChain() {
            return Optional.of(new X509Certificate[] {clientCertificate});
          }

          @Override
          public CertificateValidator getCertificateValidator() {
            return new TestServerCertificateValidator(serverCertificate);
          }

          @Override
          public EncodingContext getEncodingContext() {
            return DefaultEncodingContext.INSTANCE;
          }

          @Override
          public UInteger getRequestTimeout() {
            return uint(5_000);
          }
        };

    OpcTcpClientTransportConfig config = OpcTcpClientTransportConfig.newBuilder().build();

    var transport = new OpcTcpClientTransport(config);

    LOGGER.debug("connecting...");
    transport.connect(applicationContext).get();
    LOGGER.debug("connected");

    LOGGER.debug("disconnecting...");
    transport.disconnect().get();
    LOGGER.debug("disconnected");

    LOGGER.debug("unbinding server transport...");
    serverTransport.unbind();
    LOGGER.debug("server transport unbound");
  }

  @Test
  void securityPolicyRejectedOnUnsecuredChannel() throws Exception {
    // We opened an unsecured channel even though only secured endpoints are available.
    // Only Discovery services should be allowed. This is simulated by the test server
    // transport. In reality, it's the server application responsible for this behavior.
    OpcServerTransport serverTransport =
        bindServerTransport(SecurityPolicy.Basic256Sha256, MessageSecurityMode.SignAndEncrypt);

    var applicationContext =
        new ClientApplicationContext() {
          @Override
          public EndpointDescription getEndpoint() {
            return newEndpointDescription(SecurityPolicy.None, MessageSecurityMode.None);
          }

          @Override
          public Optional<KeyPair> getKeyPair() {
            return Optional.of(clientKeyPair);
          }

          @Override
          public Optional<X509Certificate> getCertificate() {
            return Optional.of(clientCertificate);
          }

          @Override
          public Optional<X509Certificate[]> getCertificateChain() {
            return Optional.of(new X509Certificate[] {clientCertificate});
          }

          @Override
          public CertificateValidator getCertificateValidator() {
            return new TestServerCertificateValidator(serverCertificate);
          }

          @Override
          public EncodingContext getEncodingContext() {
            return DefaultEncodingContext.INSTANCE;
          }

          @Override
          public UInteger getRequestTimeout() {
            return uint(5_000);
          }
        };

    OpcTcpClientTransportConfig config = OpcTcpClientTransportConfig.newBuilder().build();

    var transport = new OpcTcpClientTransport(config);

    LOGGER.debug("connecting...");
    transport.connect(applicationContext).get();
    LOGGER.debug("connected");

    assertThrows(ExecutionException.class, () -> createSession(transport));

    serverTransport.unbind();
  }

  @ParameterizedTest
  @MethodSource("provideM1EccSecurityParameters")
  void connectThenDisconnectWithM1EccPolicies(
      SecurityPolicy securityPolicy, CertificateMaterial client, CertificateMaterial server)
      throws Exception {

    MessageSecurityMode messageSecurityMode = MessageSecurityMode.SignAndEncrypt;
    OpcServerTransport serverTransport =
        bindServerTransport(securityPolicy, messageSecurityMode, server, client.certificate());

    try {
      var applicationContext = clientApplicationContext(securityPolicy, client, server);

      OpcTcpClientTransportConfig config = OpcTcpClientTransportConfig.newBuilder().build();

      var transport = new OpcTcpClientTransport(config);

      LOGGER.debug("connecting with {}...", securityPolicy);
      transport.connect(applicationContext).get();
      LOGGER.debug("connected with {}", securityPolicy);

      LOGGER.debug("disconnecting...");
      transport.disconnect().get();
      LOGGER.debug("disconnected");
    } finally {
      LOGGER.debug("unbinding server transport...");
      serverTransport.unbind();
      LOGGER.debug("server transport unbound");
    }
  }

  // Renewal must perform a second ECC ephemeral-key exchange and install a distinct token, not
  // reuse the Issue exchange key material.
  @ParameterizedTest
  @MethodSource("provideM1EccSecurityParameters")
  void secureChannelRenewsWithM1EccPolicies(
      SecurityPolicy securityPolicy, CertificateMaterial client, CertificateMaterial server)
      throws Exception {

    assertSecureChannelRenews(
        securityPolicy,
        client,
        server,
        OpcTcpClientTransportConfig.newBuilder().setChannelLifetime(uint(300)).build(),
        OpcTcpServerTransportConfig.newBuilder()
            .setMinimumSecureChannelLifetime(uint(300))
            .setMaximumSecureChannelLifetime(uint(300))
            .build());
  }

  // RSA renewal is the long-standing path; exercising it here protects the Phase 9 ECC changes
  // from changing legacy OpenSecureChannel Issue/Renew behavior.
  @Test
  void secureChannelRenewsWithRsaPolicy() throws Exception {
    assertSecureChannelRenews(
        SecurityPolicy.Basic256Sha256,
        new CertificateMaterial(clientKeyPair, clientCertificate, clientCertificateBytes),
        new CertificateMaterial(serverKeyPair, serverCertificate, serverCertificateBytes),
        OpcTcpClientTransportConfig.newBuilder().setChannelLifetime(uint(300)).build(),
        OpcTcpServerTransportConfig.newBuilder()
            .setMinimumSecureChannelLifetime(uint(300))
            .setMaximumSecureChannelLifetime(uint(300))
            .build());
  }

  // A malformed ClientNonce for ECC policies is malformed key-agreement input; the server must
  // reject it before issuing a usable token.
  @ParameterizedTest
  @MethodSource("provideM1EccSecurityParameters")
  void openSecureChannelRejectsMalformedEccEphemeralPublicKeys(
      SecurityPolicy securityPolicy, CertificateMaterial client, CertificateMaterial server)
      throws Exception {

    OpcServerTransport serverTransport =
        bindServerTransport(
            securityPolicy, MessageSecurityMode.SignAndEncrypt, server, client.certificate());

    try (Socket socket = new Socket()) {
      ChannelParameters channelParameters = openRawTcpChannel(socket);

      writeRawOpenSecureChannelRequest(
          socket,
          channelParameters,
          securityPolicy,
          client.keyPair(),
          client.certificate(),
          server.certificate(),
          malformedEphemeralPublicKey(securityPolicy));

      assertRawOpenSecureChannelRejected(socket);
    } finally {
      serverTransport.unbind();
    }
  }

  // The asymmetric header thumbprint selects the server certificate identity. A client that points
  // at a different certificate must not be allowed to open a channel.
  @ParameterizedTest
  @MethodSource("provideM1EccSecurityParameters")
  void openSecureChannelRejectsWrongReceiverThumbprint(
      SecurityPolicy securityPolicy, CertificateMaterial client, CertificateMaterial server)
      throws Exception {

    CertificateMaterial wrongServer =
        switch (securityPolicy) {
          case ECC_nistP256_AesGcm -> nistP256Certificate("wrong-ecc-nist-server");
          case ECC_curve25519_ChaChaPoly -> ed25519Certificate("wrong-ecc-curve25519-server");
          default -> throw new IllegalArgumentException("securityPolicy: " + securityPolicy);
        };

    OpcServerTransport serverTransport =
        bindServerTransport(
            securityPolicy, MessageSecurityMode.SignAndEncrypt, server, client.certificate());
    OpcTcpClientTransport transport =
        new OpcTcpClientTransport(OpcTcpClientTransportConfig.newBuilder().build());

    try {
      ClientApplicationContext applicationContext =
          clientApplicationContext(securityPolicy, client, wrongServer);

      assertThrows(ExecutionException.class, () -> transport.connect(applicationContext).get());
    } finally {
      disconnectQuietly(transport);
      serverTransport.unbind();
    }
  }

  // The OPN signature authenticates the sender certificate. Signing with a different private key
  // must fail even when the certificate itself is trusted.
  @ParameterizedTest
  @MethodSource("provideM1EccSecurityParameters")
  void openSecureChannelRejectsInvalidEccOpnSignatures(
      SecurityPolicy securityPolicy, CertificateMaterial client, CertificateMaterial server)
      throws Exception {

    CertificateMaterial mismatchedClient =
        switch (securityPolicy) {
          case ECC_nistP256_AesGcm -> nistP256Certificate("wrong-ecc-nist-client-key");
          case ECC_curve25519_ChaChaPoly -> ed25519Certificate("wrong-ecc-curve25519-client-key");
          default -> throw new IllegalArgumentException("securityPolicy: " + securityPolicy);
        };

    OpcServerTransport serverTransport =
        bindServerTransport(
            securityPolicy, MessageSecurityMode.SignAndEncrypt, server, client.certificate());

    try (Socket socket = new Socket()) {
      ChannelParameters channelParameters = openRawTcpChannel(socket);
      ByteString clientNonce =
          ChannelSecurity.encodeEphemeralPublicKey(
              securityPolicy.getProfile(),
              ChannelSecurity.generateEphemeralKeyPair(securityPolicy.getProfile()));

      writeRawOpenSecureChannelRequest(
          socket,
          channelParameters,
          securityPolicy,
          mismatchedClient.keyPair(),
          client.certificate(),
          server.certificate(),
          clientNonce);

      assertRawOpenSecureChannelRejected(socket);
    } finally {
      serverTransport.unbind();
    }
  }

  // Local fallback identities still need to match the selected ECC authentication family.
  @ParameterizedTest
  @MethodSource("provideM1EccSecurityParameters")
  void openSecureChannelRejectsWrongLocalCertificateType(
      SecurityPolicy securityPolicy, CertificateMaterial ignoredClient, CertificateMaterial server)
      throws Exception {

    CertificateMaterial rsaClient =
        new CertificateMaterial(clientKeyPair, clientCertificate, clientCertificateBytes);
    OpcServerTransport serverTransport =
        bindServerTransport(
            securityPolicy, MessageSecurityMode.SignAndEncrypt, server, rsaClient.certificate());
    OpcTcpClientTransport transport =
        new OpcTcpClientTransport(OpcTcpClientTransportConfig.newBuilder().build());

    try {
      ClientApplicationContext applicationContext =
          clientApplicationContext(securityPolicy, rsaClient, server);

      assertThrows(ExecutionException.class, () -> transport.connect(applicationContext).get());
    } finally {
      disconnectQuietly(transport);
      serverTransport.unbind();
    }
  }

  private static void createSession(OpcTcpClientTransport transport) throws Exception {
    var header =
        new RequestHeader(
            NodeId.NULL_VALUE, DateTime.now(), uint(0), uint(0), null, uint(5_000), null);

    var request =
        new CreateSessionRequest(
            header,
            new ApplicationDescription(
                "", "", LocalizedText.NULL_VALUE, ApplicationType.Client, null, null, null),
            null,
            "opc.tcp://localhost:12685",
            "sessionName",
            ByteString.NULL_VALUE,
            ByteString.NULL_VALUE,
            60_000d,
            UInteger.MAX);

    transport.sendRequestMessage(request).get();
  }

  private void assertSecureChannelRenews(
      SecurityPolicy securityPolicy,
      CertificateMaterial client,
      CertificateMaterial server,
      OpcTcpClientTransportConfig clientConfig,
      OpcTcpServerTransportConfig serverConfig)
      throws Exception {

    MessageSecurityMode messageSecurityMode = MessageSecurityMode.SignAndEncrypt;
    CountDownLatch clientTokensCreated = new CountDownLatch(2);
    CountDownLatch serverTokensCreated = new CountDownLatch(2);
    var clientTokenIds = ConcurrentHashMap.<Long>newKeySet();
    var serverTokenIds = ConcurrentHashMap.<Long>newKeySet();

    SecurityKeysListener clientKeysListener =
        keyset -> recordToken(keyset, clientTokenIds, clientTokensCreated);
    SecurityKeysListener serverKeysListener =
        keyset -> recordToken(keyset, serverTokenIds, serverTokensCreated);

    OpcServerTransport serverTransport =
        bindServerTransport(
            securityPolicy,
            messageSecurityMode,
            server,
            client.certificate(),
            serverKeysListener,
            serverConfig);
    OpcTcpClientTransport transport = new OpcTcpClientTransport(clientConfig);

    try {
      ClientApplicationContext applicationContext =
          clientApplicationContext(securityPolicy, client, server, clientKeysListener);

      transport.connect(applicationContext).get();

      assertTrue(
          clientTokensCreated.await(5, TimeUnit.SECONDS),
          "client did not install a renewed SecureChannel token");
      assertTrue(
          serverTokensCreated.await(5, TimeUnit.SECONDS),
          "server did not install a renewed SecureChannel token");
      assertTrue(clientTokenIds.size() >= 2, "client token ids were not renewed");
      assertTrue(serverTokenIds.size() >= 2, "server token ids were not renewed");
    } finally {
      disconnectQuietly(transport);
      serverTransport.unbind();
    }
  }

  private static void recordToken(
      SecurityKeyset keyset, java.util.Set<Long> tokenIds, CountDownLatch latch) {

    tokenIds.add(keyset.tokenId());
    latch.countDown();
  }

  private static void disconnectQuietly(OpcTcpClientTransport transport) {
    try {
      transport.disconnect().get(5, TimeUnit.SECONDS);
    } catch (Exception ignored) {
      // The assertions in these tests are about the OpenSecureChannel path; failed handshakes can
      // leave no connected channel to close.
    }
  }

  private ClientApplicationContext clientApplicationContext(
      SecurityPolicy securityPolicy, CertificateMaterial client, CertificateMaterial server) {

    return clientApplicationContext(securityPolicy, client, server, null);
  }

  private ClientApplicationContext clientApplicationContext(
      SecurityPolicy securityPolicy,
      CertificateMaterial client,
      CertificateMaterial server,
      SecurityKeysListener securityKeysListener) {

    MessageSecurityMode messageSecurityMode = MessageSecurityMode.SignAndEncrypt;

    return new ClientApplicationContext() {
      @Override
      public EndpointDescription getEndpoint() {
        return newEndpointDescription(
            securityPolicy, messageSecurityMode, server.certificateBytes());
      }

      @Override
      public Optional<KeyPair> getKeyPair() {
        return Optional.of(client.keyPair());
      }

      @Override
      public Optional<X509Certificate> getCertificate() {
        return Optional.of(client.certificate());
      }

      @Override
      public Optional<X509Certificate[]> getCertificateChain() {
        return Optional.of(new X509Certificate[] {client.certificate()});
      }

      @Override
      public CertificateValidator getCertificateValidator() {
        return new TestServerCertificateValidator(server.certificate());
      }

      @Override
      public EncodingContext getEncodingContext() {
        return DefaultEncodingContext.INSTANCE;
      }

      @Override
      public UInteger getRequestTimeout() {
        return uint(5_000);
      }

      @Override
      public SecurityKeysListener getSecurityKeysListener() {
        return securityKeysListener;
      }
    };
  }

  private OpcServerTransport bindServerTransport(
      SecurityPolicy securityPolicy, MessageSecurityMode messageSecurityMode) throws Exception {

    return bindServerTransport(
        securityPolicy,
        messageSecurityMode,
        new CertificateMaterial(serverKeyPair, serverCertificate, serverCertificateBytes),
        clientCertificate);
  }

  private OpcServerTransport bindServerTransport(
      SecurityPolicy securityPolicy,
      MessageSecurityMode messageSecurityMode,
      CertificateMaterial server,
      X509Certificate trustedClientCertificate)
      throws Exception {

    return bindServerTransport(
        securityPolicy,
        messageSecurityMode,
        server,
        trustedClientCertificate,
        null,
        OpcTcpServerTransportConfig.newBuilder().build());
  }

  private OpcServerTransport bindServerTransport(
      SecurityPolicy securityPolicy,
      MessageSecurityMode messageSecurityMode,
      CertificateMaterial server,
      X509Certificate trustedClientCertificate,
      SecurityKeysListener securityKeysListener,
      OpcTcpServerTransportConfig config)
      throws Exception {

    var applicationContext =
        new ServerApplicationContext() {

          private final AtomicLong secureChannelId = new AtomicLong(0L);
          private final AtomicLong secureChannelTokenId = new AtomicLong(0L);

          @Override
          public List<EndpointDescription> getEndpointDescriptions() {
            return List.of(
                newEndpointDescription(
                    securityPolicy, messageSecurityMode, server.certificateBytes()));
          }

          @Override
          public EncodingContext getEncodingContext() {
            return DefaultEncodingContext.INSTANCE;
          }

          @Override
          public CertificateManager getCertificateManager() {
            try {
              return new TestCertificateManager(
                  server.keyPair(),
                  server.certificate(),
                  new TestServerCertificateValidator(trustedClientCertificate));
            } catch (Exception e) {
              throw new RuntimeException(e);
            }
          }

          @Override
          public Long getNextSecureChannelId() {
            return secureChannelId.getAndIncrement();
          }

          @Override
          public Long getNextSecureChannelTokenId() {
            return secureChannelTokenId.getAndIncrement();
          }

          @Override
          public CompletableFuture<UaResponseMessageType> handleServiceRequest(
              ServiceRequestContext context, UaRequestMessageType requestMessage) {

            if (context.getSecureChannel().getSecurityPolicy() == SecurityPolicy.None) {
              if (getEndpointDescriptions().stream()
                  .noneMatch(
                      e ->
                          Objects.equals(e.getSecurityPolicyUri(), SecurityPolicy.None.getUri()))) {

                var errorMessage =
                    new ErrorMessage(
                        StatusCodes.Bad_SecurityPolicyRejected,
                        StatusCodes.lookup(StatusCodes.Bad_SecurityPolicyRejected)
                            .map(ss -> ss[1])
                            .orElse(""));

                context.getChannel().pipeline().fireUserEventTriggered(errorMessage);

                // won't complete, doesn't matter, we're closing down
                return new CompletableFuture<>();
              }
            }

            LOGGER.debug("request: {}", requestMessage);

            return CompletableFuture.failedFuture(new RuntimeException("not implemented"));
          }

          @Override
          public SecurityKeysListener getSecurityKeysListener() {
            return securityKeysListener;
          }
        };

    var transport = new OpcTcpServerTransport(config);
    transport.bind(applicationContext, new InetSocketAddress("localhost", 12685));
    return transport;
  }

  private static ChannelParameters openRawTcpChannel(Socket socket) throws Exception {
    socket.connect(new InetSocketAddress("localhost", 12685));
    socket.setSoTimeout(3_000);

    EncodingLimits encodingLimits = DefaultEncodingContext.INSTANCE.getEncodingLimits();
    var hello =
        new HelloMessage(
            0L,
            encodingLimits.getMaxChunkSize(),
            encodingLimits.getMaxChunkSize(),
            encodingLimits.getMaxMessageSize(),
            encodingLimits.getMaxChunkCount(),
            "opc.tcp://localhost:12685");

    ByteBuf helloBuffer = TcpMessageEncoder.encode(hello);
    try {
      writeByteBuf(socket.getOutputStream(), helloBuffer);
    } finally {
      helloBuffer.release();
    }

    byte[] acknowledgeBytes = readRawMessage(socket);
    ByteBuf acknowledgeBuffer = Unpooled.wrappedBuffer(acknowledgeBytes);

    try {
      assertEquals(
          MessageType.Acknowledge, MessageType.fromMediumInt(acknowledgeBuffer.getMediumLE(0)));

      acknowledgeBuffer.skipBytes(3 + 1 + 4);

      AcknowledgeMessage acknowledge = AcknowledgeMessage.decode(acknowledgeBuffer);

      long localReceiveBufferSize =
          Math.min(acknowledge.getSendBufferSize(), encodingLimits.getMaxChunkSize());
      long localSendBufferSize =
          Math.min(acknowledge.getReceiveBufferSize(), encodingLimits.getMaxChunkSize());

      return new ChannelParameters(
          encodingLimits.getMaxMessageSize(),
          Math.toIntExact(localReceiveBufferSize),
          Math.toIntExact(localSendBufferSize),
          encodingLimits.getMaxChunkCount(),
          Math.toIntExact(acknowledge.getMaxMessageSize()),
          Math.toIntExact(acknowledge.getReceiveBufferSize()),
          Math.toIntExact(acknowledge.getSendBufferSize()),
          Math.toIntExact(acknowledge.getMaxChunkCount()));
    } finally {
      acknowledgeBuffer.release();
    }
  }

  private static void writeRawOpenSecureChannelRequest(
      Socket socket,
      ChannelParameters channelParameters,
      SecurityPolicy securityPolicy,
      KeyPair signingKeyPair,
      X509Certificate senderCertificate,
      X509Certificate receiverCertificate,
      ByteString clientNonce)
      throws Exception {

    var secureChannel =
        new ClientSecureChannel(
            signingKeyPair,
            senderCertificate,
            List.of(senderCertificate),
            receiverCertificate,
            List.of(receiverCertificate),
            securityPolicy,
            MessageSecurityMode.SignAndEncrypt);

    secureChannel.setLocalNonce(clientNonce);

    var header =
        new RequestHeader(
            NodeId.NULL_VALUE, DateTime.now(), uint(0), uint(0), null, uint(5_000), null);

    var request =
        new OpenSecureChannelRequest(
            header,
            uint(0),
            SecurityTokenRequestType.Issue,
            MessageSecurityMode.SignAndEncrypt,
            clientNonce,
            uint(60_000));

    ByteBuf messageBuffer = Unpooled.buffer();
    var binaryEncoder = new OpcUaBinaryEncoder(DefaultEncodingContext.INSTANCE);

    try {
      binaryEncoder.setBuffer(messageBuffer);
      binaryEncoder.encodeMessage(null, request);

      EncodedMessage encodedMessage =
          new ChunkEncoder(channelParameters)
              .encodeAsymmetric(secureChannel, 1L, messageBuffer, MessageType.OpenSecureChannel);

      try {
        for (ByteBuf chunk : encodedMessage.getMessageChunks()) {
          writeByteBuf(socket.getOutputStream(), chunk);
        }
      } finally {
        encodedMessage.getMessageChunks().forEach(ByteBuf::release);
      }
    } finally {
      messageBuffer.release();
    }
  }

  private static void assertRawOpenSecureChannelRejected(Socket socket) throws Exception {
    byte[] responseBytes;

    try {
      responseBytes = readRawMessage(socket);
    } catch (EOFException e) {
      return;
    } catch (SocketTimeoutException e) {
      fail("server did not reject the OpenSecureChannel request");
      return;
    }

    ByteBuf responseBuffer = Unpooled.wrappedBuffer(responseBytes);
    try {
      assertNotEquals(
          MessageType.OpenSecureChannel, MessageType.fromMediumInt(responseBuffer.getMediumLE(0)));
    } finally {
      responseBuffer.release();
    }
  }

  private static void writeByteBuf(OutputStream outputStream, ByteBuf buffer) throws IOException {
    byte[] bytes = new byte[buffer.readableBytes()];
    buffer.getBytes(buffer.readerIndex(), bytes);
    outputStream.write(bytes);
    outputStream.flush();
  }

  private static byte[] readRawMessage(Socket socket) throws IOException {
    InputStream inputStream = socket.getInputStream();
    byte[] header = inputStream.readNBytes(8);

    if (header.length == 0) {
      throw new EOFException("connection closed");
    }
    if (header.length < 8) {
      throw new EOFException("incomplete message header");
    }

    int messageLength =
        (header[4] & 0xFF)
            | ((header[5] & 0xFF) << 8)
            | ((header[6] & 0xFF) << 16)
            | ((header[7] & 0xFF) << 24);
    byte[] message = Arrays.copyOf(header, messageLength);
    byte[] body = inputStream.readNBytes(messageLength - 8);

    if (body.length < messageLength - 8) {
      throw new EOFException("incomplete message body");
    }

    System.arraycopy(body, 0, message, 8, body.length);

    return message;
  }

  private static ByteString malformedEphemeralPublicKey(SecurityPolicy securityPolicy) {
    return switch (securityPolicy) {
      case ECC_nistP256_AesGcm -> {
        byte[] offCurvePoint = new byte[64];
        offCurvePoint[63] = 1;

        yield ByteString.of(offCurvePoint);
      }
      case ECC_curve25519_ChaChaPoly -> {
        byte[] lowOrderPoint = new byte[32];
        lowOrderPoint[0] = 1;

        yield ByteString.of(lowOrderPoint);
      }
      default -> throw new IllegalArgumentException("securityPolicy: " + securityPolicy);
    };
  }

  private EndpointDescription newEndpointDescription(
      SecurityPolicy securityPolicy, MessageSecurityMode messageSecurityMode) {

    return newEndpointDescription(securityPolicy, messageSecurityMode, serverCertificateBytes);
  }

  private EndpointDescription newEndpointDescription(
      SecurityPolicy securityPolicy, MessageSecurityMode messageSecurityMode, byte[] certificate) {

    return new EndpointDescription(
        "opc.tcp://localhost:12685",
        new ApplicationDescription(
            "uri:server",
            "productUri",
            LocalizedText.NULL_VALUE,
            ApplicationType.Server,
            null,
            null,
            new String[] {"opc.tcp://localhost:12685"}),
        ByteString.of(certificate),
        messageSecurityMode,
        securityPolicy.getUri(),
        new UserTokenPolicy[] {
          new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null)
        },
        TransportProfile.TCP_UASC_UABINARY.getUri(),
        ubyte(0));
  }

  private static CertificateMaterial nistP256Certificate(String commonName) throws Exception {
    return eccCertificate(SelfSignedCertificateGenerator.generateNistP256KeyPair(), commonName);
  }

  private static CertificateMaterial ed25519Certificate(String commonName) throws Exception {
    return eccCertificate(SelfSignedCertificateGenerator.generateEd25519KeyPair(), commonName);
  }

  private static CertificateMaterial eccCertificate(KeyPair keyPair, String commonName)
      throws Exception {

    X509Certificate certificate =
        SelfSignedCertificateBuilder.forEccApplicationCertificate(keyPair)
            .setCommonName(commonName)
            .setOrganization("Eclipse Milo")
            .setApplicationUri("urn:test:" + commonName)
            .build();

    return new CertificateMaterial(keyPair, certificate, certificate.getEncoded());
  }

  private record CertificateMaterial(
      KeyPair keyPair, X509Certificate certificate, byte[] certificateBytes) {}
}
