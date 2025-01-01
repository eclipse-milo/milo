/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack;

import static org.eclipse.milo.opcua.stack.core.channel.EncodingLimits.DEFAULT_MAX_CHUNK_SIZE;
import static org.eclipse.milo.opcua.stack.core.channel.EncodingLimits.DEFAULT_MAX_MESSAGE_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import java.security.Security;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.milo.opcua.stack.core.channel.ChannelParameters;
import org.eclipse.milo.opcua.stack.core.channel.ChunkDecoder;
import org.eclipse.milo.opcua.stack.core.channel.ChunkEncoder;
import org.eclipse.milo.opcua.stack.core.channel.EncodingLimits;
import org.eclipse.milo.opcua.stack.core.channel.MessageEncodeException;
import org.eclipse.milo.opcua.stack.core.channel.SecureChannel;
import org.eclipse.milo.opcua.stack.core.channel.ServerSecureChannel;
import org.eclipse.milo.opcua.stack.core.channel.messages.MessageType;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.util.BufferUtil;
import org.eclipse.milo.opcua.stack.core.util.LongSequence;
import org.eclipse.milo.opcua.stack.transport.client.uasc.ClientSecureChannel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChunkSerializationTest extends SecureChannelFixture {

  static {
    // Required for SecurityPolicy.Aes256_Sha256_RsaPss
    Security.addProvider(new BouncyCastleProvider());
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final ChannelParameters smallParameters =
      new ChannelParameters(32 * 8196, 8196, 8196, 64, 32 * 8196, 8196, 8196, 64);

  private final ChannelParameters defaultParameters =
      new ChannelParameters(
          DEFAULT_MAX_MESSAGE_SIZE,
          DEFAULT_MAX_CHUNK_SIZE,
          DEFAULT_MAX_CHUNK_SIZE,
          0,
          DEFAULT_MAX_MESSAGE_SIZE,
          DEFAULT_MAX_CHUNK_SIZE,
          DEFAULT_MAX_CHUNK_SIZE,
          0);

  private final ChannelParameters unlimitedChunkCountParameters =
      new ChannelParameters(
          EncodingLimits.DEFAULT_MAX_MESSAGE_SIZE,
          EncodingLimits.DEFAULT_MAX_CHUNK_SIZE,
          EncodingLimits.DEFAULT_MAX_CHUNK_SIZE,
          0,
          EncodingLimits.DEFAULT_MAX_MESSAGE_SIZE,
          EncodingLimits.DEFAULT_MAX_CHUNK_SIZE,
          EncodingLimits.DEFAULT_MAX_CHUNK_SIZE,
          0);

  private final ChannelParameters unlimitedMessageSizeParameters =
      new ChannelParameters(
          0,
          EncodingLimits.DEFAULT_MAX_CHUNK_SIZE,
          EncodingLimits.DEFAULT_MAX_CHUNK_SIZE,
          0,
          0,
          EncodingLimits.DEFAULT_MAX_CHUNK_SIZE,
          EncodingLimits.DEFAULT_MAX_CHUNK_SIZE,
          0);

  public static Object[][] getAsymmetricSecurityParameters() {
    return new Object[][] {
      {SecurityPolicy.None, MessageSecurityMode.None, 128},
      {SecurityPolicy.Basic128Rsa15, MessageSecurityMode.Sign, 128},
      {SecurityPolicy.Basic128Rsa15, MessageSecurityMode.SignAndEncrypt, 128},
      {SecurityPolicy.Basic256, MessageSecurityMode.Sign, 128},
      {SecurityPolicy.Basic256, MessageSecurityMode.SignAndEncrypt, 128},
      {SecurityPolicy.Basic256Sha256, MessageSecurityMode.Sign, 128},
      {SecurityPolicy.Basic256Sha256, MessageSecurityMode.SignAndEncrypt, 128},
      {SecurityPolicy.Aes128_Sha256_RsaOaep, MessageSecurityMode.Sign, 128},
      {SecurityPolicy.Aes128_Sha256_RsaOaep, MessageSecurityMode.SignAndEncrypt, 128},
      {SecurityPolicy.Aes256_Sha256_RsaPss, MessageSecurityMode.Sign, 128},
      {SecurityPolicy.Aes256_Sha256_RsaPss, MessageSecurityMode.SignAndEncrypt, 128},
      {SecurityPolicy.None, MessageSecurityMode.None, DEFAULT_MAX_CHUNK_SIZE},
      {SecurityPolicy.Basic128Rsa15, MessageSecurityMode.Sign, DEFAULT_MAX_CHUNK_SIZE},
      {SecurityPolicy.Basic128Rsa15, MessageSecurityMode.SignAndEncrypt, DEFAULT_MAX_CHUNK_SIZE},
      {SecurityPolicy.Basic256, MessageSecurityMode.Sign, DEFAULT_MAX_CHUNK_SIZE},
      {SecurityPolicy.Basic256, MessageSecurityMode.SignAndEncrypt, DEFAULT_MAX_CHUNK_SIZE},
      {SecurityPolicy.Basic256Sha256, MessageSecurityMode.Sign, DEFAULT_MAX_CHUNK_SIZE},
      {SecurityPolicy.Basic256Sha256, MessageSecurityMode.SignAndEncrypt, DEFAULT_MAX_CHUNK_SIZE},
      {SecurityPolicy.Aes128_Sha256_RsaOaep, MessageSecurityMode.Sign, DEFAULT_MAX_CHUNK_SIZE},
      {
        SecurityPolicy.Aes128_Sha256_RsaOaep,
        MessageSecurityMode.SignAndEncrypt,
        DEFAULT_MAX_CHUNK_SIZE
      },
      {SecurityPolicy.Aes256_Sha256_RsaPss, MessageSecurityMode.Sign, DEFAULT_MAX_CHUNK_SIZE},
      {
        SecurityPolicy.Aes256_Sha256_RsaPss,
        MessageSecurityMode.SignAndEncrypt,
        DEFAULT_MAX_CHUNK_SIZE
      },
    };
  }

  @Test
  public void testAsymmetric4096() throws Exception {
    ChannelParameters parameters = defaultParameters;

    ChunkEncoder encoder = new ChunkEncoder(parameters);

    ChunkDecoder decoder = new ChunkDecoder(parameters, EncodingLimits.DEFAULT);

    SecureChannel[] channels = generateChannels4096();

    ClientSecureChannel clientChannel = (ClientSecureChannel) channels[0];
    ServerSecureChannel serverChannel = (ServerSecureChannel) channels[1];

    LongSequence requestId = new LongSequence(1L, UInteger.MAX_VALUE);

    for (int messageSize = 0; messageSize < 512; messageSize++) {
      byte[] messageBytes = new byte[messageSize];
      for (int i = 0; i < messageBytes.length; i++) {
        messageBytes[i] = (byte) i;
      }

      ByteBuf messageBuffer = BufferUtil.pooledBuffer().writeBytes(messageBytes);

      List<ByteBuf> chunkBuffers = new ArrayList<>();

      try {
        ChunkEncoder.EncodedMessage message =
            encoder.encodeAsymmetric(
                clientChannel,
                requestId.getAndIncrement(),
                messageBuffer,
                MessageType.OpenSecureChannel);

        chunkBuffers.addAll(message.getMessageChunks());
      } catch (MessageEncodeException e) {
        fail("encoding error", e);
      }

      try {
        ChunkDecoder.DecodedMessage decodedMessage =
            decoder.decodeAsymmetric(serverChannel, chunkBuffers);

        ByteBuf message = decodedMessage.getMessage();

        messageBuffer.readerIndex(0);
        assertEquals(messageBuffer, message);

        ReferenceCountUtil.release(message);
        ReferenceCountUtil.release(messageBuffer);
      } catch (Throwable t) {
        fail("decoding error", t);
      }
    }
  }

  @Test
  public void testSymmetric4096() throws Exception {
    ChannelParameters parameters = defaultParameters;

    ChunkEncoder encoder = new ChunkEncoder(parameters);

    ChunkDecoder decoder = new ChunkDecoder(parameters, EncodingLimits.DEFAULT);

    SecureChannel[] channels = generateChannels4096();

    ClientSecureChannel clientChannel = (ClientSecureChannel) channels[0];
    ServerSecureChannel serverChannel = (ServerSecureChannel) channels[1];

    LongSequence requestId = new LongSequence(1L, UInteger.MAX_VALUE);

    for (int messageSize = 0; messageSize < 1024; messageSize++) {
      byte[] messageBytes = new byte[messageSize];
      for (int i = 0; i < messageBytes.length; i++) {
        messageBytes[i] = (byte) i;
      }

      ByteBuf messageBuffer = BufferUtil.pooledBuffer().writeBytes(messageBytes);

      List<ByteBuf> chunkBuffers = new ArrayList<>();

      try {
        ChunkEncoder.EncodedMessage message =
            encoder.encodeSymmetric(
                clientChannel,
                requestId.getAndIncrement(),
                messageBuffer,
                MessageType.OpenSecureChannel);

        chunkBuffers.addAll(message.getMessageChunks());
      } catch (MessageEncodeException e) {
        fail("encoding error", e);
      }

      try {
        ChunkDecoder.DecodedMessage decodedMessage =
            decoder.decodeSymmetric(serverChannel, chunkBuffers);

        ByteBuf message = decodedMessage.getMessage();

        messageBuffer.readerIndex(0);
        assertEquals(messageBuffer, message);

        ReferenceCountUtil.release(message);
        ReferenceCountUtil.release(messageBuffer);
      } catch (Throwable t) {
        fail("decoding error", t);
      }
    }
  }

  @ParameterizedTest
  @MethodSource("getAsymmetricSecurityParameters")
  public void testAsymmetricMessage(
      SecurityPolicy securityPolicy, MessageSecurityMode messageSecurity, int messageSize)
      throws Exception {

    logger.info(
        "Asymmetric chunk serialization, securityPolicy={}, messageSecurityMode={}, messageSize={}",
        securityPolicy,
        messageSecurity,
        messageSize);

    ChannelParameters[] channelParameters = {
      smallParameters,
      defaultParameters,
      unlimitedChunkCountParameters,
      unlimitedMessageSizeParameters
    };

    for (ChannelParameters parameters : channelParameters) {
      ChunkEncoder encoder = new ChunkEncoder(parameters);

      ChunkDecoder decoder = new ChunkDecoder(parameters, EncodingLimits.DEFAULT);

      SecureChannel[] channels = generateChannels(securityPolicy, messageSecurity);
      ClientSecureChannel clientChannel = (ClientSecureChannel) channels[0];
      ServerSecureChannel serverChannel = (ServerSecureChannel) channels[1];

      LongSequence requestId = new LongSequence(1L, UInteger.MAX_VALUE);

      byte[] messageBytes = new byte[messageSize];
      for (int i = 0; i < messageBytes.length; i++) {
        messageBytes[i] = (byte) i;
      }

      ByteBuf messageBuffer = BufferUtil.pooledBuffer().writeBytes(messageBytes);

      List<ByteBuf> chunkBuffers = new ArrayList<>();

      try {
        ChunkEncoder.EncodedMessage message =
            encoder.encodeAsymmetric(
                clientChannel,
                requestId.getAndIncrement(),
                messageBuffer,
                MessageType.OpenSecureChannel);

        chunkBuffers.addAll(message.getMessageChunks());
      } catch (MessageEncodeException e) {
        fail("encoding error", e);
      }

      try {
        ChunkDecoder.DecodedMessage decodedMessage =
            decoder.decodeAsymmetric(serverChannel, chunkBuffers);

        ByteBuf message = decodedMessage.getMessage();

        messageBuffer.readerIndex(0);
        assertEquals(messageBuffer, message);

        ReferenceCountUtil.release(message);
        ReferenceCountUtil.release(messageBuffer);
      } catch (Throwable t) {
        fail("decoding error", t);
      }
    }
  }

  public static Object[][] getSymmetricSecurityParameters() {
    return new Object[][] {
      {SecurityPolicy.None, MessageSecurityMode.None},
      {SecurityPolicy.Basic128Rsa15, MessageSecurityMode.Sign},
      {SecurityPolicy.Basic128Rsa15, MessageSecurityMode.SignAndEncrypt},
      {SecurityPolicy.Basic256, MessageSecurityMode.Sign},
      {SecurityPolicy.Basic256, MessageSecurityMode.SignAndEncrypt},
      {SecurityPolicy.Basic256Sha256, MessageSecurityMode.Sign},
      {SecurityPolicy.Basic256Sha256, MessageSecurityMode.SignAndEncrypt},
      {SecurityPolicy.Aes128_Sha256_RsaOaep, MessageSecurityMode.Sign},
      {SecurityPolicy.Aes128_Sha256_RsaOaep, MessageSecurityMode.SignAndEncrypt},
      {SecurityPolicy.Aes256_Sha256_RsaPss, MessageSecurityMode.Sign},
      {SecurityPolicy.Aes256_Sha256_RsaPss, MessageSecurityMode.SignAndEncrypt}
    };
  }

  @ParameterizedTest
  @MethodSource("getSymmetricSecurityParameters")
  public void testSymmetricMessage(
      SecurityPolicy securityPolicy, MessageSecurityMode messageSecurity) throws Exception {

    logger.info(
        "Symmetric chunk serialization, " + "securityPolicy={}, messageSecurityMode={}",
        securityPolicy,
        messageSecurity);

    ChannelParameters[] channelParameters = {
      smallParameters,
      defaultParameters,
      unlimitedChunkCountParameters,
      unlimitedMessageSizeParameters
    };

    for (ChannelParameters parameters : channelParameters) {
      int[] messageSizes = new int[] {128, parameters.getRemoteMaxMessageSize()};

      for (int messageSize : messageSizes) {
        ChunkEncoder encoder = new ChunkEncoder(parameters);

        ChunkDecoder decoder = new ChunkDecoder(parameters, EncodingLimits.DEFAULT);

        SecureChannel[] channels = generateChannels(securityPolicy, messageSecurity);
        ClientSecureChannel clientChannel = (ClientSecureChannel) channels[0];
        ServerSecureChannel serverChannel = (ServerSecureChannel) channels[1];

        LongSequence requestId = new LongSequence(1L, UInteger.MAX_VALUE);

        byte[] messageBytes = new byte[messageSize];
        for (int i = 0; i < messageBytes.length; i++) {
          messageBytes[i] = (byte) i;
        }

        ByteBuf messageBuffer = BufferUtil.pooledBuffer().writeBytes(messageBytes);

        List<ByteBuf> chunkBuffers = new ArrayList<>();

        try {
          ChunkEncoder.EncodedMessage message =
              encoder.encodeSymmetric(
                  clientChannel,
                  requestId.getAndIncrement(),
                  messageBuffer,
                  MessageType.SecureMessage);

          chunkBuffers.addAll(message.getMessageChunks());
        } catch (MessageEncodeException e) {
          fail("encoding error", e);
        }

        try {
          ChunkDecoder.DecodedMessage decodedMessage =
              decoder.decodeSymmetric(serverChannel, chunkBuffers);

          ByteBuf message = decodedMessage.getMessage();

          messageBuffer.readerIndex(0);
          assertEquals(messageBuffer, message);

          ReferenceCountUtil.release(messageBuffer);
          ReferenceCountUtil.release(message);
        } catch (Throwable t) {
          fail("decoding error", t);
        }
      }
    }
  }
}
