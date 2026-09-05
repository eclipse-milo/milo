/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client.uasc;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.HashedWheelTimer;
import io.netty.util.ReferenceCountUtil;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.channel.ChannelParameters;
import org.eclipse.milo.opcua.stack.core.channel.ChannelSecurity;
import org.eclipse.milo.opcua.stack.core.channel.ChunkDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.security.CertificateIdentity;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ChannelSecurityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.GetEndpointsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

/** Authenticated Abort responses preserve the following response at the chunk-count boundary. */
class UascClientChunkLifecycleTest {

  // A response Abort may follow every permitted C chunk. It must release the message, report the
  // request failure, and preserve the authenticated stream for the next response.
  @ParameterizedTest
  @EnumSource(
      value = SecurityPolicy.class,
      names = {"None", "ECC_nistP256_AesGcm"})
  void abortAfterChunkCountLimitPreservesTheFollowingResponse(SecurityPolicy policy)
      throws Exception {
    var parameters = new ChannelParameters(65535, 8192, 8192, 1, 65535, 8192, 8192, 1);
    var wheelTimer = new HashedWheelTimer();
    var channel = new EmbeddedChannel();
    try {
      var handler =
          new UascClientMessageHandler(
              config(wheelTimer),
              application(),
              new AtomicLong(1L)::getAndIncrement,
              new CompletableFuture<>(),
              List.of(),
              parameters);
      channel.pipeline().addLast(handler);
      channel.runPendingTasks();
      Object outbound;
      while ((outbound = channel.readOutbound()) != null) {
        ReferenceCountUtil.release(outbound);
      }

      var keyPair = SelfSignedCertificateGenerator.generateNistP256KeyPair();
      var certificate =
          new SelfSignedCertificateBuilder(keyPair)
              .setCommonName("abort-test")
              .setApplicationUri("urn:test:client")
              .build();
      var secureChannel =
          new ClientSecureChannel(
              keyPair,
              certificate,
              List.of(certificate),
              certificate,
              List.of(certificate),
              policy,
              policy == SecurityPolicy.None
                  ? MessageSecurityMode.None
                  : MessageSecurityMode.SignAndEncrypt);
      secureChannel.setChannelId(1);
      secureChannel.setChannelSecurity(
          new ChannelSecurity(
              policy == SecurityPolicy.None
                  ? null
                  : ChannelSecurity.createAeadKeyPair(
                      new byte[16], new byte[12], new byte[16], new byte[12]),
              new ChannelSecurityToken(uint(1), uint(1), DateTime.now(), uint(60_000))));
      Field channelField = UascClientMessageHandler.class.getDeclaredField("secureChannel");
      channelField.setAccessible(true);
      channelField.set(handler, secureChannel);
      Field decoderField = UascClientMessageHandler.class.getDeclaredField("chunkDecoder");
      decoderField.setAccessible(true);
      Field sequenceField = ChunkDecoder.class.getDeclaredField("lastSequenceNumber");
      sequenceField.setAccessible(true);
      sequenceField.setLong(decoderField.get(handler), 0);

      ByteBuf partial = symmetricChunk(policy, 'C', 1, 0, new byte[] {1, 2, 3});
      byte[] errorBody =
          ByteBuffer.allocate(8)
              .order(ByteOrder.LITTLE_ENDIAN)
              .putInt((int) StatusCodes.Bad_EncodingLimitsExceeded)
              .putInt(-1)
              .array();
      ByteBuf abort = symmetricChunk(policy, 'A', 2, 1, errorBody);
      channel.writeInbound(partial);
      assertEquals(1, partial.refCnt());
      channel.writeInbound(abort);
      assertTrue(channel.isOpen());
      assertEquals(0, partial.refCnt());
      assertEquals(0, abort.refCnt());
      UascResponse failure = channel.readInbound();
      assertTrue(failure.isFailure());
      assertEquals(
          StatusCodes.Bad_EncodingLimitsExceeded, failure.getException().getStatusCode().value());

      ByteBuf body = Unpooled.buffer();
      try {
        var response =
            new GetEndpointsResponse(
                new ResponseHeader(DateTime.now(), uint(1), StatusCode.GOOD, null, null, null),
                new EndpointDescription[0]);
        new OpcUaBinaryEncoder(DefaultEncodingContext.INSTANCE)
            .setBuffer(body)
            .encodeMessage(null, response);
        byte[] bytes = new byte[body.readableBytes()];
        body.readBytes(bytes);
        ByteBuf following = symmetricChunk(policy, 'F', 3, 2, bytes);
        channel.writeInbound(following);
        UascResponse success = channel.readInbound();
        assertInstanceOf(GetEndpointsResponse.class, success.getResponseMessage());
        assertEquals(0, following.refCnt());
        assertTrue(channel.isOpen());
      } finally {
        body.release();
      }
    } finally {
      channel.finishAndReleaseAll();
      wheelTimer.stop();
    }
  }

  // Independent wire construction covers the Abort type, which the production encoder does not
  // emit.
  private static ByteBuf symmetricChunk(
      SecurityPolicy policy, char type, int sequence, int last, byte[] body) throws Exception {
    boolean encrypted = policy != SecurityPolicy.None;
    ByteBuf chunk = PooledByteBufAllocator.DEFAULT.directBuffer();
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

  static UascClientConfig config(HashedWheelTimer wheelTimer) {
    return new UascClientConfig() {
      public UInteger getAcknowledgeTimeout() {
        return uint(60000);
      }

      public UInteger getChannelLifetime() {
        return uint(3600000);
      }

      public HashedWheelTimer getWheelTimer() {
        return wheelTimer;
      }
    };
  }

  static ClientApplicationContext application() {
    EndpointDescription endpoint =
        new EndpointDescription(
            "opc.tcp://localhost:0/test",
            new ApplicationDescription(
                "urn:eclipse:milo:test",
                "urn:eclipse:milo:test",
                LocalizedText.english("test"),
                ApplicationType.Client,
                null,
                null,
                null),
            ByteString.NULL_VALUE,
            MessageSecurityMode.None,
            SecurityPolicy.None.getUri(),
            null,
            null,
            ubyte(0));
    return new ClientApplicationContext() {
      public EndpointDescription getEndpoint() {
        return endpoint;
      }

      public Optional<CertificateIdentity> getCertificateIdentity(SecurityPolicyProfile p) {
        return Optional.empty();
      }

      public CertificateValidator getCertificateValidator() {
        return (chain, uri, hosts) -> {};
      }

      public EncodingContext getEncodingContext() {
        return new DefaultEncodingContext();
      }

      public UInteger getRequestTimeout() {
        return uint(60000);
      }
    };
  }
}
