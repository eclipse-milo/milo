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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.security.KeyPair;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.channel.ChannelParameters;
import org.eclipse.milo.opcua.stack.core.channel.ChannelSecurity;
import org.eclipse.milo.opcua.stack.core.channel.ChunkDecoder;
import org.eclipse.milo.opcua.stack.core.channel.ChunkEncoder;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.channel.ServerSecureChannel;
import org.eclipse.milo.opcua.stack.core.channel.headers.AsymmetricSecurityHeader;
import org.eclipse.milo.opcua.stack.core.channel.messages.MessageType;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.SecurityTokenRequestType;
import org.eclipse.milo.opcua.stack.core.types.structured.ChannelSecurityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.GetEndpointsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.OpenSecureChannelRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.OpenSecureChannelResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.types.structured.ServiceFault;
import org.eclipse.milo.opcua.stack.core.util.DigestUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.transport.client.uasc.ClientSecureChannel;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/** Chunk failures must preserve the authenticated sequence stream for the next message. */
class UascChunkRecoveryTest {
  private static final EncodingContext ENCODING = DefaultEncodingContext.INSTANCE;
  private static final String ENDPOINT_URL = "opc.tcp://localhost:4840";
  private static final ChannelParameters PARAMETERS =
      new ChannelParameters(100_000, 8192, 8192, 20, 0, 8192, 8192, 1);
  private static final ChannelParameters ABORT_PARAMETERS =
      new ChannelParameters(100_000, 8192, 8192, 1, 0, 8192, 8192, 1);

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

  // The peer may discover overflow after sending the permitted C chunks. Abort must still pass
  // security checks, discard those chunks and leave the SecureChannel open (Part 6 §6.7.3).
  @ParameterizedTest
  @EnumSource(
      value = SecurityPolicy.class,
      names = {"None", "ECC_nistP256_AesGcm"})
  void authenticatedSymmetricAbortPreservesTheFollowingRequest(SecurityPolicy policy)
      throws Exception {
    var fixture = new Fixture(policy);
    var encoder = new ChunkEncoder(PARAMETERS);
    var decoder = new ChunkDecoder(PARAMETERS, EncodingLimits.DEFAULT);
    startAfterOpen(encoder, decoder, policy);
    EmbeddedChannel channel =
        fixture.symmetricChannel(new ChunkEncoder(PARAMETERS), decoder, ABORT_PARAMETERS);
    try {
      channel.writeInbound(encodeRequest(encoder, fixture.client));
      assertInstanceOf(UascServiceRequest.class, channel.readInbound());
      ByteBuf partial = symmetricChunk(policy, 'C', 2, 1, new byte[] {1, 2, 3});
      ByteBuf abort = symmetricChunk(policy, 'A', 3, 2, abortBody());
      channel.writeInbound(partial);
      assertEquals(1, partial.refCnt());
      channel.writeInbound(abort);
      assertEquals(0, partial.refCnt());
      assertEquals(0, abort.refCnt());
      assertTrue(channel.isOpen());

      if (policy == SecurityPolicy.None) {
        encodeRequest(encoder, fixture.client).release();
        encodeRequest(encoder, fixture.client).release();
      } else {
        setLong(encoder, "nonLegacyNextSequenceNumber", 4);
        setLong(encoder, "nonLegacyLastSequenceNumber", 3);
      }
      channel.writeInbound(encodeRequest(encoder, fixture.client));
      assertInstanceOf(UascServiceRequest.class, channel.readInbound());
      assertTrue(channel.isOpen());
    } finally {
      channel.finishAndReleaseAll();
    }
  }

  @Test
  void tamperedSymmetricAbortFailsAuthentication() throws Exception {
    var fixture = new Fixture(SecurityPolicy.ECC_nistP256_AesGcm);
    var decoder = new ChunkDecoder(PARAMETERS, EncodingLimits.DEFAULT);
    setLong(decoder, "lastSequenceNumber", 1);
    EmbeddedChannel channel =
        fixture.symmetricChannel(new ChunkEncoder(PARAMETERS), decoder, ABORT_PARAMETERS);
    try {
      ByteBuf partial = symmetricChunk(fixture.policy, 'C', 2, 1, new byte[] {1, 2, 3});
      ByteBuf abort = symmetricChunk(fixture.policy, 'A', 3, 2, abortBody());
      abort.setByte(abort.writerIndex() - 1, abort.getByte(abort.writerIndex() - 1) ^ 1);
      channel.writeInbound(partial);
      channel.writeInbound(abort);
      assertFalse(channel.isOpen(), "an invalid tag must not be accepted as an Abort");
      assertEquals(0, partial.refCnt());
      assertEquals(0, abort.refCnt());
    } finally {
      channel.finishAndReleaseAll();
    }
  }

  @Test
  void headerOnlySymmetricAbortCannotDiscardAnAccumulatingMessage() throws Exception {
    var fixture = new Fixture(SecurityPolicy.None);
    EmbeddedChannel channel =
        fixture.symmetricChannel(
            new ChunkEncoder(PARAMETERS), new ChunkDecoder(PARAMETERS, EncodingLimits.DEFAULT));
    try {
      channel
          .pipeline()
          .addLast(
              new ChannelInboundHandlerAdapter() {
                @Override
                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                  ctx.close();
                }
              });
      ByteBuf partial = symmetricChunk(SecurityPolicy.None, 'C', 1, 0, new byte[] {1});
      ByteBuf abort =
          Unpooled.buffer()
              .writeBytes(new byte[] {'M', 'S', 'G', 'A'})
              .writeIntLE(12)
              .writeIntLE(1);
      channel.writeInbound(partial);
      channel.writeInbound(abort);
      assertFalse(channel.isOpen(), "a header without security or sequence fields is not an Abort");
      assertEquals(0, partial.refCnt());
      assertEquals(0, abort.refCnt());
    } finally {
      channel.finishAndReleaseAll();
    }
  }

  @Test
  void asymmetricAbortPreservesTheFollowingOpenRequest() throws Exception {
    var fixture = new Fixture(SecurityPolicy.None);
    var handler =
        new UascServerAsymmetricHandler(
            CONFIG, fixture.application(), TransportProfile.TCP_UASC_UABINARY, ABORT_PARAMETERS);
    EmbeddedChannel channel = new EmbeddedChannel(handler);
    try {
      channel.attr(UascServerHelloHandler.ENDPOINT_URL_KEY).set(ENDPOINT_URL);
      ByteBuf partial = fixture.asymmetricChunk('C', 1, new byte[] {1, 2, 3});
      ByteBuf abort = fixture.asymmetricChunk('A', 2, abortBody());
      channel.writeInbound(partial);
      channel.writeInbound(abort);
      assertEquals(0, partial.refCnt());
      assertEquals(0, abort.refCnt());
      assertTrue(channel.isOpen());

      ByteBuf body = Unpooled.buffer();
      try {
        new OpcUaBinaryEncoder(ENCODING)
            .setBuffer(body)
            .encodeMessage(
                null,
                new OpenSecureChannelRequest(
                    requestHeader(),
                    uint(0),
                    SecurityTokenRequestType.Issue,
                    MessageSecurityMode.None,
                    ByteString.NULL_VALUE,
                    uint(60_000)));
        byte[] bytes = new byte[body.readableBytes()];
        body.readBytes(bytes);
        channel.writeInbound(fixture.asymmetricChunk('F', 3, bytes));
      } finally {
        body.release();
      }
      ByteBuf response = outbound(channel);
      try {
        var decoded =
            new ChunkDecoder(PARAMETERS, EncodingLimits.DEFAULT)
                .decodeAsymmetric(fixture.client, List.of(response));
        try {
          assertInstanceOf(
              OpenSecureChannelResponse.class,
              new OpcUaBinaryDecoder(ENCODING).setBuffer(decoded.getMessage()).decodeMessage(null));
        } finally {
          decoded.getMessage().release();
        }
      } finally {
        if (response.refCnt() > 0) {
          response.release();
        }
      }
      assertTrue(channel.isOpen());
    } finally {
      channel.finishAndReleaseAll();
    }
  }

  @Test
  void asymmetricAbortMustHaveAValidSignature() throws Exception {
    assertAsymmetricAbortAuthentication(true);
  }

  @Test
  void authenticatedAsymmetricAbortsKeepTheChannelOpen() throws Exception {
    assertAsymmetricAbortAuthentication(false);
  }

  private void assertAsymmetricAbortAuthentication(boolean tamper) throws Exception {
    var fixture = new Fixture(SecurityPolicy.ECC_nistP256_AesGcm);
    var handler =
        new UascServerAsymmetricHandler(
            CONFIG, fixture.application(), TransportProfile.TCP_UASC_UABINARY, ABORT_PARAMETERS);
    Field field = UascServerAsymmetricHandler.class.getDeclaredField("secureChannel");
    field.setAccessible(true);
    field.set(handler, fixture.server);
    EmbeddedChannel channel = new EmbeddedChannel(handler);
    try {
      channel.attr(UascServerHelloHandler.ENDPOINT_URL_KEY).set(ENDPOINT_URL);
      ByteBuf partial = fixture.asymmetricChunk('C', 0, new byte[] {1, 2, 3});
      ByteBuf abort = fixture.asymmetricChunk('A', 1, abortBody());
      if (tamper) {
        abort.setByte(abort.writerIndex() - 1, abort.getByte(abort.writerIndex() - 1) ^ 1);
      }
      channel.writeInbound(partial);
      assertEquals(1, partial.refCnt());
      channel.writeInbound(abort);
      channel.advanceTimeBy(1, TimeUnit.SECONDS);
      channel.runScheduledPendingTasks();
      assertEquals(
          !tamper, channel.isOpen(), "only an authenticated OPN Abort may leave the channel open");
      assertEquals(0, partial.refCnt());
      assertEquals(0, abort.refCnt());
      if (!tamper) {
        channel.writeInbound(fixture.asymmetricChunk('A', 2, abortBody()));
        assertTrue(channel.isOpen());
      }
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

  private static RequestHeader requestHeader() {
    return new RequestHeader(
        NodeId.NULL_VALUE, DateTime.now(), uint(1), uint(0), null, uint(1000), null);
  }

  private static ByteBuf encodeRequest(ChunkEncoder encoder, ClientSecureChannel channel)
      throws Exception {
    ByteBuf body = Unpooled.buffer();
    try {
      new OpcUaBinaryEncoder(ENCODING)
          .setBuffer(body)
          .encodeMessage(null, new GetEndpointsRequest(requestHeader(), ENDPOINT_URL, null, null));
      return encoder
          .encodeSymmetric(channel, 1, body, MessageType.SecureMessage)
          .getMessageChunks()
          .get(0);
    } finally {
      body.release();
    }
  }

  private static byte[] abortBody() {
    return ByteBuffer.allocate(8)
        .order(ByteOrder.LITTLE_ENDIAN)
        .putInt((int) StatusCodes.Bad_EncodingLimitsExceeded)
        .putInt(-1)
        .array();
  }

  // Assemble independently because production encoders only emit C/F chunks. With a zero IV base,
  // the GCM nonce is token id, previous sequence number, zero, all UInt32 little endian (Part 6).
  private static ByteBuf symmetricChunk(
      SecurityPolicy policy, char type, int sequence, int last, byte[] body) throws Exception {
    boolean encrypted = policy != SecurityPolicy.None;
    ByteBuf chunk = Unpooled.buffer();
    chunk.writeBytes(new byte[] {'M', 'S', 'G', (byte) type});
    chunk.writeIntLE(24 + body.length + (encrypted ? 16 : 0)).writeIntLE(1).writeIntLE(1);
    byte[] plaintext =
        ByteBuffer.allocate(8 + body.length)
            .order(ByteOrder.LITTLE_ENDIAN)
            .putInt(sequence)
            .putInt(1)
            .put(body)
            .array();
    if (encrypted) {
      byte[] nonce =
          ByteBuffer.allocate(12)
              .order(ByteOrder.LITTLE_ENDIAN)
              .putInt(1)
              .putInt(last)
              .putInt(0)
              .array();
      Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
      cipher.init(
          Cipher.ENCRYPT_MODE,
          new SecretKeySpec(new byte[16], "AES"),
          new GCMParameterSpec(128, nonce));
      byte[] aad = new byte[16];
      chunk.getBytes(0, aad);
      cipher.updateAAD(aad);
      chunk.writeBytes(cipher.doFinal(plaintext));
    } else {
      chunk.writeBytes(plaintext);
    }
    return chunk;
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

    ByteBuf asymmetricChunk(char type, int sequence, byte[] body) throws Exception {
      ByteBuf chunk = Unpooled.buffer();
      chunk
          .writeBytes(new byte[] {'O', 'P', 'N', (byte) type})
          .writeIntLE(0)
          .writeIntLE(policy == SecurityPolicy.None ? 0 : 1);
      AsymmetricSecurityHeader.encode(
          new AsymmetricSecurityHeader(
              policy.getUri(),
              policy == SecurityPolicy.None
                  ? ByteString.NULL_VALUE
                  : ByteString.of(certificate.getEncoded()),
              policy == SecurityPolicy.None
                  ? ByteString.NULL_VALUE
                  : ByteString.of(DigestUtil.sha1(certificate.getEncoded()))),
          chunk);
      chunk.writeIntLE(sequence).writeIntLE(1).writeBytes(body);
      chunk.setIntLE(4, chunk.writerIndex() + (policy == SecurityPolicy.None ? 0 : 64));
      if (policy != SecurityPolicy.None) {
        Signature signature = Signature.getInstance("SHA256withECDSAinP1363Format");
        signature.initSign(keyPair.getPrivate());
        signature.update(chunk.nioBuffer());
        chunk.writeBytes(signature.sign());
      }
      return chunk;
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
