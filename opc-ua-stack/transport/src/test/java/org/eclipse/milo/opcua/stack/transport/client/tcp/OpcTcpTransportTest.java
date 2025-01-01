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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.net.InetSocketAddress;
import java.security.KeyPair;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.OpcServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport;
import org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransportConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class OpcTcpTransportTest extends SecurityFixture {

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
            return new CertificateValidator.InsecureCertificateValidator();
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

    System.out.println("connecting...");
    transport.connect(applicationContext).get();
    System.out.println("connected");

    System.out.println("disconnecting...");
    transport.disconnect().get();
    System.out.println("disconnected");

    System.out.println("unbinding server transport...");
    serverTransport.unbind();
    System.out.println("server transport unbound");
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
            return new CertificateValidator.InsecureCertificateValidator();
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

    System.out.println("connecting...");
    transport.connect(applicationContext).get();
    System.out.println("connected");

    System.out.println("disconnecting...");
    transport.disconnect().get();
    System.out.println("disconnected");

    System.out.println("unbinding server transport...");
    serverTransport.unbind();
    System.out.println("server transport unbound");
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
            return new CertificateValidator.InsecureCertificateValidator();
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

    System.out.println("connecting...");
    transport.connect(applicationContext).get();
    System.out.println("connected");

    assertThrows(ExecutionException.class, () -> createSession(transport));

    serverTransport.unbind();
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

  private OpcServerTransport bindServerTransport(
      SecurityPolicy securityPolicy, MessageSecurityMode messageSecurityMode) throws Exception {

    var applicationContext =
        new ServerApplicationContext() {

          private final AtomicLong secureChannelId = new AtomicLong(0L);
          private final AtomicLong secureChannelTokenId = new AtomicLong(0L);

          @Override
          public List<EndpointDescription> getEndpointDescriptions() {
            return List.of(newEndpointDescription(securityPolicy, messageSecurityMode));
          }

          @Override
          public EncodingContext getEncodingContext() {
            return DefaultEncodingContext.INSTANCE;
          }

          @Override
          public CertificateManager getCertificateManager() {
            return serverCertificateManager;
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

            System.out.println("request: " + requestMessage);

            return CompletableFuture.failedFuture(new RuntimeException("not implemented"));
          }
        };

    OpcTcpServerTransportConfig config = OpcTcpServerTransportConfig.newBuilder().build();

    var transport = new OpcTcpServerTransport(config);
    transport.bind(applicationContext, new InetSocketAddress("localhost", 12685));
    return transport;
  }

  private EndpointDescription newEndpointDescription(
      SecurityPolicy securityPolicy, MessageSecurityMode messageSecurityMode) {

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
        ByteString.of(serverCertificateBytes),
        messageSecurityMode,
        securityPolicy.getUri(),
        new UserTokenPolicy[] {
          new UserTokenPolicy("anonymous", UserTokenType.Anonymous, null, null, null)
        },
        TransportProfile.TCP_UASC_UABINARY.getUri(),
        ubyte(0));
  }
}
