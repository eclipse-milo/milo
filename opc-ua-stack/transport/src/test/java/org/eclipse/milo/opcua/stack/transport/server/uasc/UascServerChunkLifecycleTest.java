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
import static org.junit.jupiter.api.Assertions.assertThrows;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.DecoderException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.ChannelParameters;
import org.eclipse.milo.opcua.stack.core.channel.ChannelSecurity;
import org.eclipse.milo.opcua.stack.core.channel.ChunkDecoder;
import org.eclipse.milo.opcua.stack.core.channel.ChunkEncoder;
import org.eclipse.milo.opcua.stack.core.channel.ServerSecureChannel;
import org.eclipse.milo.opcua.stack.core.channel.headers.AsymmetricSecurityHeader;
import org.eclipse.milo.opcua.stack.core.channel.headers.SequenceHeader;
import org.eclipse.milo.opcua.stack.core.channel.messages.MessageType;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateManager;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ChannelSecurityToken;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext;
import org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext;
import org.junit.jupiter.api.Test;

class UascServerChunkLifecycleTest {

  private static final String ENDPOINT_URL = "opc.tcp://localhost:4840";

  private static final ChannelParameters CHANNEL_PARAMETERS =
      new ChannelParameters(8192, 8192, 8192, 16, 8192, 8192, 8192, 16);

  private static final ServerApplicationContext APPLICATION_CONTEXT =
      new ServerApplicationContext() {
        @Override
        public List<EndpointDescription> getEndpointDescriptions() {
          return List.of(
              new EndpointDescription(
                  ENDPOINT_URL,
                  new ApplicationDescription(
                      "urn:test:server",
                      "urn:test:product",
                      LocalizedText.NULL_VALUE,
                      ApplicationType.Server,
                      null,
                      null,
                      new String[] {ENDPOINT_URL}),
                  ByteString.NULL_VALUE,
                  MessageSecurityMode.None,
                  SecurityPolicy.None.getUri(),
                  null,
                  TransportProfile.TCP_UASC_UABINARY.getUri(),
                  ubyte(0)));
        }

        @Override
        public CertificateManager getCertificateManager() {
          throw new UnsupportedOperationException();
        }

        @Override
        public EncodingContext getEncodingContext() {
          return DefaultEncodingContext.INSTANCE;
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
            ServiceRequestContext context, UaRequestMessageType requestMessage) {
          return CompletableFuture.failedFuture(new UnsupportedOperationException());
        }
      };

  @Test
  void asymmetricPartialChunkIsReleasedOnDisconnectBeforeSession() {
    var handler =
        new UascServerAsymmetricHandler(
            null, APPLICATION_CONTEXT, TransportProfile.TCP_UASC_UABINARY, CHANNEL_PARAMETERS);
    var channel = new EmbeddedChannel(handler);
    channel.attr(UascServerHelloHandler.ENDPOINT_URL_KEY).set(ENDPOINT_URL);

    ByteBuf chunk = newAsymmetricPartialChunk();

    channel.writeInbound(chunk);
    assertEquals(1, chunk.refCnt());

    channel.close().syncUninterruptibly();
    assertEquals(0, chunk.refCnt());

    channel.finishAndReleaseAll();
  }

  @Test
  void asymmetricPartialChunkIsReleasedOnHandlerRemoval() {
    var handler =
        new UascServerAsymmetricHandler(
            null, APPLICATION_CONTEXT, TransportProfile.TCP_UASC_UABINARY, CHANNEL_PARAMETERS);
    var channel = new EmbeddedChannel(handler);
    channel.attr(UascServerHelloHandler.ENDPOINT_URL_KEY).set(ENDPOINT_URL);

    ByteBuf chunk = newAsymmetricPartialChunk();

    channel.writeInbound(chunk);
    assertEquals(1, chunk.refCnt());

    channel.pipeline().remove(handler);
    assertEquals(0, chunk.refCnt());

    channel.finishAndReleaseAll();
  }

  @Test
  void symmetricPartialChunkIsReleasedOnDisconnect() {
    UascServerSymmetricHandler handler = newSymmetricHandler();
    var channel = new EmbeddedChannel(handler);

    ByteBuf chunk = newSymmetricPartialChunk();

    channel.writeInbound(chunk);
    assertEquals(1, chunk.refCnt());

    channel.close().syncUninterruptibly();
    assertEquals(0, chunk.refCnt());

    channel.finishAndReleaseAll();
  }

  @Test
  void symmetricPartialChunkIsReleasedOnHandlerRemoval() {
    UascServerSymmetricHandler handler = newSymmetricHandler();
    var channel = new EmbeddedChannel(handler);

    ByteBuf chunk = newSymmetricPartialChunk();

    channel.writeInbound(chunk);
    assertEquals(1, chunk.refCnt());

    channel.pipeline().remove(handler);
    assertEquals(0, chunk.refCnt());

    channel.finishAndReleaseAll();
  }

  @Test
  void symmetricPartialChunksAreReleasedOnException() {
    var channelParameters = new ChannelParameters(8192, 8192, 8192, 1, 8192, 8192, 8192, 16);
    UascServerSymmetricHandler handler = newSymmetricHandler(channelParameters);
    var channel = new EmbeddedChannel(handler);

    ByteBuf firstChunk = newSymmetricChunk('C');
    ByteBuf secondChunk = newSymmetricChunk('C');

    channel.writeInbound(firstChunk);
    assertEquals(1, firstChunk.refCnt());

    DecoderException exception =
        assertThrows(DecoderException.class, () -> channel.writeInbound(secondChunk));
    assertInstanceOf(UaException.class, exception.getCause());
    assertEquals(0, firstChunk.refCnt());
    assertEquals(0, secondChunk.refCnt());

    channel.finishAndReleaseAll();
  }

  @Test
  void symmetricPartialChunkIsReleasedOnAbort() {
    UascServerSymmetricHandler handler = newSymmetricHandler();
    var channel = new EmbeddedChannel(handler);

    ByteBuf partialChunk = newSymmetricPartialChunk();
    ByteBuf abortChunk = newSymmetricChunk('A');

    channel.writeInbound(partialChunk);
    assertEquals(1, partialChunk.refCnt());

    channel.writeInbound(abortChunk);
    assertEquals(0, partialChunk.refCnt());
    assertEquals(0, abortChunk.refCnt());

    channel.finishAndReleaseAll();
  }

  @Test
  void asymmetricMalformedFinalChunkIsReleasedBeforeSession() {
    var handler =
        new UascServerAsymmetricHandler(
            null, APPLICATION_CONTEXT, TransportProfile.TCP_UASC_UABINARY, CHANNEL_PARAMETERS);
    var channel = new EmbeddedChannel(handler);
    channel.attr(UascServerHelloHandler.ENDPOINT_URL_KEY).set(ENDPOINT_URL);

    ByteBuf chunk = newAsymmetricChunk('F', false);

    channel.writeInbound(chunk);
    assertEquals(0, chunk.refCnt());

    channel.finishAndReleaseAll();
  }

  @Test
  void symmetricMalformedFinalChunkIsReleased() {
    UascServerSymmetricHandler handler = newSymmetricHandler();
    var channel = new EmbeddedChannel(handler);

    ByteBuf chunk = newSymmetricFinalChunkWithoutSequenceHeader();

    assertThrows(DecoderException.class, () -> channel.writeInbound(chunk));
    assertEquals(0, chunk.refCnt());

    channel.finishAndReleaseAll();
  }

  @Test
  void coalescedAsymmetricChunksAreReleasedOnMalformedFinalChunk() {
    var handler =
        new UascServerAsymmetricHandler(
            null, APPLICATION_CONTEXT, TransportProfile.TCP_UASC_UABINARY, CHANNEL_PARAMETERS);
    var channel = new EmbeddedChannel(handler);
    channel.attr(UascServerHelloHandler.ENDPOINT_URL_KEY).set(ENDPOINT_URL);

    ByteBuf chunks = PooledByteBufAllocator.DEFAULT.directBuffer();
    writeAsymmetricChunk(chunks, 'C', true);
    writeAsymmetricChunk(chunks, 'F', false);

    channel.writeInbound(chunks);
    assertEquals(0, chunks.refCnt());

    channel.finishAndReleaseAll();
  }

  @Test
  void transferredChunksRemainOwnedByCaller() {
    var accumulator = new ChunkBufferAccumulator();
    ByteBuf chunk = PooledByteBufAllocator.DEFAULT.directBuffer(1);

    accumulator.add(chunk);
    chunk.release();

    List<ByteBuf> transferred = accumulator.takeAll();
    accumulator.releaseAll();

    assertEquals(1, chunk.refCnt());

    transferred.forEach(ByteBuf::release);
    assertEquals(0, chunk.refCnt());
  }

  private static UascServerSymmetricHandler newSymmetricHandler() {
    return newSymmetricHandler(CHANNEL_PARAMETERS);
  }

  private static UascServerSymmetricHandler newSymmetricHandler(
      ChannelParameters channelParameters) {
    var secureChannel = new ServerSecureChannel();
    secureChannel.setChannelId(1L);
    secureChannel.setSecurityPolicy(SecurityPolicy.None);
    secureChannel.setMessageSecurityMode(MessageSecurityMode.None);
    secureChannel.setChannelSecurity(
        new ChannelSecurity(
            null, new ChannelSecurityToken(uint(1), uint(1), DateTime.now(), uint(60_000))));

    var chunkEncoder = new ChunkEncoder(channelParameters);
    var chunkDecoder =
        new ChunkDecoder(
            channelParameters, APPLICATION_CONTEXT.getEncodingContext().getEncodingLimits());

    return new UascServerSymmetricHandler(
        null,
        APPLICATION_CONTEXT,
        TransportProfile.TCP_UASC_UABINARY,
        channelParameters,
        chunkEncoder,
        chunkDecoder,
        secureChannel);
  }

  private static ByteBuf newAsymmetricPartialChunk() {
    return newAsymmetricChunk('C', false);
  }

  private static ByteBuf newAsymmetricChunk(char chunkType, boolean includeSequenceHeader) {
    ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer();

    writeAsymmetricChunk(buffer, chunkType, includeSequenceHeader);

    return buffer;
  }

  private static void writeAsymmetricChunk(
      ByteBuf buffer, char chunkType, boolean includeSequenceHeader) {
    int chunkStart = buffer.writerIndex();

    buffer.writeMediumLE(MessageType.toMediumInt(MessageType.OpenSecureChannel));
    buffer.writeByte(chunkType);
    buffer.writeIntLE(0);
    buffer.writeIntLE(0);
    AsymmetricSecurityHeader.encode(
        new AsymmetricSecurityHeader(
            SecurityPolicy.None.getUri(), ByteString.NULL_VALUE, ByteString.NULL_VALUE),
        buffer);
    if (includeSequenceHeader) {
      SequenceHeader.encode(new SequenceHeader(1L, 1L), buffer);
    }
    buffer.setIntLE(chunkStart + 4, buffer.writerIndex() - chunkStart);
  }

  private static ByteBuf newSymmetricPartialChunk() {
    return newSymmetricChunk('C');
  }

  private static ByteBuf newSymmetricChunk(char chunkType) {
    ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(12);

    buffer.writeMediumLE(MessageType.toMediumInt(MessageType.SecureMessage));
    buffer.writeByte(chunkType);
    buffer.writeIntLE(12);
    buffer.writeIntLE(1);

    return buffer;
  }

  private static ByteBuf newSymmetricFinalChunkWithoutSequenceHeader() {
    ByteBuf buffer = PooledByteBufAllocator.DEFAULT.directBuffer(16);

    buffer.writeMediumLE(MessageType.toMediumInt(MessageType.SecureMessage));
    buffer.writeByte('F');
    buffer.writeIntLE(16);
    buffer.writeIntLE(1);
    buffer.writeIntLE(1);

    return buffer;
  }
}
