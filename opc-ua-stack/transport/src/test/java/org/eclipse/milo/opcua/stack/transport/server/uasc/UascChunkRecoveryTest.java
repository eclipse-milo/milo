/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.uasc;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.channel.embedded.EmbeddedChannel;
import java.lang.reflect.Field;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.channel.ChannelParameters;
import org.eclipse.milo.opcua.stack.core.channel.ChannelSecurity;
import org.eclipse.milo.opcua.stack.core.channel.ChunkDecoder;
import org.eclipse.milo.opcua.stack.core.channel.ChunkEncoder;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.channel.ServerSecureChannel;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ChannelSecurityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceFault;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.transport.client.uasc.ClientSecureChannel;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/** Chunk failures must preserve the authenticated sequence stream for the next message. */
class UascChunkRecoveryTest {
  private static final EncodingContext ENCODING = DefaultEncodingContext.INSTANCE;
  private static final String ENDPOINT_URL = "opc.tcp://localhost:4840";
  private static final ChannelParameters PARAMETERS =
      new ChannelParameters(100_000, 8192, 8192, 20, 0, 8192, 8192, 1);

  // A peer may advertise unlimited message size but limit the number of chunks. The fallback
  // ServiceFault must use the next sequence number the peer expects, including its AEAD nonce.
  @ParameterizedTest
  @EnumSource(
      value = SecurityPolicy.class,
      names = {"None", "Basic256Sha256", "ECC_nistP256_AesGcm", "ECC_nistP256_ChaChaPoly"})
  void oversizedResponseStillDeliversItsServiceFault(SecurityPolicy policy) throws Exception {
    var fixture = new Fixture(policy);
    var encoder = new ChunkEncoder(PARAMETERS);
    var decoder = new ChunkDecoder(PARAMETERS, EncodingLimits.DEFAULT);
    startAfterOpen(encoder, decoder, policy);
    EmbeddedChannel channel =
        fixture.symmetricChannel(encoder, new ChunkDecoder(PARAMETERS, EncodingLimits.DEFAULT));
    try {
      channel.writeOutbound(response(0));
      assertEquals(
          StatusCode.GOOD,
          decodeResponse(decoder, fixture.client, outbound(channel))
              .getResponseHeader()
              .getServiceResult());

      channel.writeOutbound(response(9000));
      UaResponseMessageType fault = decodeResponse(decoder, fixture.client, outbound(channel));
      assertInstanceOf(ServiceFault.class, fault);
      assertEquals(
          new StatusCode(StatusCodes.Bad_EncodingLimitsExceeded),
          fault.getResponseHeader().getServiceResult());
      assertTrue(channel.isOpen());

      channel.writeOutbound(response(0));
      assertEquals(
          StatusCode.GOOD,
          decodeResponse(decoder, fixture.client, outbound(channel))
              .getResponseHeader()
              .getServiceResult());
    } finally {
      channel.finishAndReleaseAll();
    }
  }

  private static UascServiceResponse response(int textLength) {
    return new UascServiceResponse(
        new ServiceFault(
            new ResponseHeader(
                DateTime.now(),
                uint(1),
                StatusCode.GOOD,
                null,
                textLength == 0 ? null : new String[] {"x".repeat(textLength)},
                null)),
        1);
  }

  private static UaResponseMessageType decodeResponse(
      ChunkDecoder decoder, ClientSecureChannel client, ByteBuf chunk) throws Exception {
    var decoded = decoder.decodeSymmetric(client, List.of(chunk));
    try {
      return (UaResponseMessageType)
          new OpcUaBinaryDecoder(ENCODING).setBuffer(decoded.getMessage()).decodeMessage(null);
    } finally {
      decoded.getMessage().release();
    }
  }

  private static ByteBuf outbound(EmbeddedChannel channel) {
    ByteBuf buffer;
    while ((buffer = channel.readOutbound()) != null) {
      if (buffer.isReadable()) {
        return buffer;
      }
      buffer.release();
    }
    throw new AssertionError("no response was sent");
  }

  private static void startAfterOpen(
      ChunkEncoder encoder, ChunkDecoder decoder, SecurityPolicy policy) throws Exception {
    if (policy.getProfile().secureChannelEnhancements()) {
      setLong(encoder, "nonLegacyNextSequenceNumber", 1);
      setLong(encoder, "nonLegacyLastSequenceNumber", 0);
      setLong(decoder, "lastSequenceNumber", 0);
    }
  }

  private static void setLong(Object target, String name, long value) throws Exception {
    Field field = target.getClass().getDeclaredField(name);
    field.setAccessible(true);
    field.setLong(target, value);
  }

  private static final UascServerConfig CONFIG =
      new UascServerConfig() {
        @Override
        public ExecutorService getExecutor() {
          return null;
        }

        @Override
        public UInteger getHelloDeadline() {
          return uint(60_000);
        }

        @Override
        public UInteger getMinimumSecureChannelLifetime() {
          return uint(1000);
        }

        @Override
        public UInteger getMaximumSecureChannelLifetime() {
          return uint(60_000);
        }
      };

  private static class Fixture {
    final SecurityPolicy policy;
    final KeyPair keyPair;
    final X509Certificate certificate;
    final ServerSecureChannel server = new ServerSecureChannel();
    final ClientSecureChannel client;

    Fixture(SecurityPolicy policy) throws Exception {
      this.policy = policy;
      keyPair = SelfSignedCertificateGenerator.generateNistP256KeyPair();
      certificate =
          new SelfSignedCertificateBuilder(keyPair)
              .setCommonName("chunk-test")
              .setApplicationUri("urn:test:server")
              .build();
      MessageSecurityMode mode =
          policy == SecurityPolicy.None
              ? MessageSecurityMode.None
              : MessageSecurityMode.SignAndEncrypt;
      server.setChannelId(1);
      server.setSecurityPolicy(policy);
      server.setMessageSecurityMode(mode);
      server.setKeyPair(keyPair);
      server.setLocalCertificate(certificate);
      server.setRemoteCertificate(certificate.getEncoded());
      client =
          new ClientSecureChannel(
              keyPair,
              certificate,
              List.of(certificate),
              certificate,
              List.of(certificate),
              policy,
              mode);
      client.setChannelId(1);
      server.setChannelSecurity(security());
      client.setChannelSecurity(security());
    }

    private ChannelSecurity security() {
      ChannelSecurity.SecurityKeys keys = null;
      if (policy.getProfile().secureChannelEnhancements()) {
        int keySize = policy == SecurityPolicy.ECC_nistP256_ChaChaPoly ? 32 : 16;
        keys =
            ChannelSecurity.createAeadKeyPair(
                new byte[keySize], new byte[12], new byte[keySize], new byte[12]);
      } else if (policy != SecurityPolicy.None) {
        keys =
            ChannelSecurity.generateKeyPair(
                server, ByteString.of(new byte[32]), ByteString.of(new byte[32]));
      }
      return new ChannelSecurity(
          keys, new ChannelSecurityToken(uint(1), uint(1), DateTime.now(), uint(60_000)));
    }

    EmbeddedChannel symmetricChannel(ChunkEncoder encoder, ChunkDecoder decoder) {
      return symmetricChannel(encoder, decoder, PARAMETERS);
    }

    EmbeddedChannel symmetricChannel(
        ChunkEncoder encoder, ChunkDecoder decoder, ChannelParameters parameters) {
      var channel =
          new EmbeddedChannel(
              new UascServerSymmetricHandler(
                  CONFIG,
                  application(),
                  TransportProfile.TCP_UASC_UABINARY,
                  parameters,
                  encoder,
                  decoder,
                  server));
      channel.pipeline().remove(UascServiceRequestHandler.class);
      return channel;
    }

    ServerApplicationContext application() {
      return new ServerApplicationContext() {
        @Override
        public List<EndpointDescription> getEndpointDescriptions() {
          try {
            return List.of(
                new EndpointDescription(
                    ENDPOINT_URL,
                    null,
                    ByteString.of(certificate.getEncoded()),
                    server.getMessageSecurityMode(),
                    policy.getUri(),
                    null,
                    TransportProfile.TCP_UASC_UABINARY.getUri(),
                    ubyte(0)));
          } catch (Exception e) {
            throw new AssertionError(e);
          }
        }

        @Override
        public CertificateManager getCertificateManager() {
          throw new AssertionError("identity already installed");
        }

        @Override
        public EncodingContext getEncodingContext() {
          return ENCODING;
        }

        @Override
        public Long getNextSecureChannelId() {
          return 1L;
        }

        @Override
        public Long getNextSecureChannelTokenId() {
          return 1L;
        }

        @Override
        public CompletableFuture<UaResponseMessageType> handleServiceRequest(
            ServiceRequestContext context, UaRequestMessageType request) {
          throw new AssertionError(request);
        }
      };
    }
  }
}
