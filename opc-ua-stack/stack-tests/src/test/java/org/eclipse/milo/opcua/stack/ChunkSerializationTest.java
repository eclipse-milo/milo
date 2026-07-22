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
import static org.eclipse.milo.opcua.stack.core.channel.headers.SecureMessageHeader.SECURE_MESSAGE_HEADER_SIZE;
import static org.eclipse.milo.opcua.stack.core.channel.headers.SequenceHeader.SEQUENCE_HEADER_SIZE;
import static org.eclipse.milo.opcua.stack.core.channel.headers.SymmetricSecurityHeader.SYMMETRIC_SECURITY_HEADER_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.eclipse.milo.opcua.stack.core.channel.MessageDecodeException;
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

  private static final Logger LOGGER = LoggerFactory.getLogger(ChunkSerializationTest.class);

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

  // Token id 0 is valid before channel security is installed; null-security handshakes depend on
  // this bootstrap path instead of requiring a ChannelSecurity instance.
  @Test
  public void symmetricTokenZeroDecodesBeforeChannelSecurityIsInstalled() throws Exception {
    ChunkEncoder encoder = new ChunkEncoder(defaultParameters);
    ChunkDecoder decoder = new ChunkDecoder(defaultParameters, EncodingLimits.DEFAULT);

    ClientSecureChannel clientChannel =
        new ClientSecureChannel(SecurityPolicy.None, MessageSecurityMode.None);

    ServerSecureChannel serverChannel = new ServerSecureChannel();
    serverChannel.setSecurityPolicy(SecurityPolicy.None);
    serverChannel.setMessageSecurityMode(MessageSecurityMode.None);

    ByteBuf messageBuffer = BufferUtil.pooledBuffer().writeBytes(new byte[] {0x01, 0x02, 0x03});

    ChunkEncoder.EncodedMessage encodedMessage =
        encoder.encodeSymmetric(clientChannel, 1L, messageBuffer, MessageType.SecureMessage);
    ChunkDecoder.DecodedMessage decodedMessage =
        decoder.decodeSymmetric(serverChannel, encodedMessage.getMessageChunks());

    try {
      messageBuffer.readerIndex(0);
      assertEquals(messageBuffer, decodedMessage.getMessage());
    } finally {
      ReferenceCountUtil.release(decodedMessage.getMessage());
      ReferenceCountUtil.release(messageBuffer);
    }
  }

  @ParameterizedTest
  @MethodSource("getAsymmetricSecurityParameters")
  public void testAsymmetricMessage(
      SecurityPolicy securityPolicy, MessageSecurityMode messageSecurity, int messageSize)
      throws Exception {

    LOGGER.debug(
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

  public static Object[][] getAeadSecurityParameters() {
    return new Object[][] {
      {SecurityPolicy.ECC_nistP256_AesGcm},
      {SecurityPolicy.ECC_nistP256_ChaChaPoly},
      {SecurityPolicy.ECC_nistP384_AesGcm},
      {SecurityPolicy.ECC_nistP384_ChaChaPoly},
      {SecurityPolicy.ECC_brainpoolP256r1_AesGcm},
      {SecurityPolicy.ECC_brainpoolP256r1_ChaChaPoly},
      {SecurityPolicy.ECC_curve25519_AesGcm},
      {SecurityPolicy.ECC_curve25519_ChaChaPoly},
      {SecurityPolicy.ECC_curve448_AesGcm},
      {SecurityPolicy.ECC_curve448_ChaChaPoly},
      {SecurityPolicy.ECC_brainpoolP384r1_AesGcm},
      {SecurityPolicy.ECC_brainpoolP384r1_ChaChaPoly},
      {SecurityPolicy.RSA_DH_AesGcm},
      {SecurityPolicy.RSA_DH_ChaChaPoly}
    };
  }

  @ParameterizedTest
  @MethodSource("getSymmetricSecurityParameters")
  public void testSymmetricMessage(
      SecurityPolicy securityPolicy, MessageSecurityMode messageSecurity) throws Exception {

    LOGGER.debug(
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

  // Current AEAD policies use the signature footer as an authentication tag instead of a legacy
  // HMAC, so round-trip coverage needs to exercise the full symmetric chunk framing path.
  @ParameterizedTest
  @MethodSource("getAeadSecurityParameters")
  public void testAeadSymmetricMessage(SecurityPolicy securityPolicy) throws Exception {
    LOGGER.debug("AEAD symmetric chunk serialization, securityPolicy={}", securityPolicy);

    assertAeadSymmetricMessage(securityPolicy, MessageSecurityMode.SignAndEncrypt);
  }

  // AEAD Sign mode uses the tag as a signature footer: no symmetric encryption, but the tag still
  // authenticates the secure message header, symmetric security header, sequence header, and body
  // bytes.
  @ParameterizedTest
  @MethodSource("getAeadSecurityParameters")
  public void testAeadSignSymmetricMessage(SecurityPolicy securityPolicy) throws Exception {
    LOGGER.debug("AEAD Sign chunk serialization, securityPolicy={}", securityPolicy);

    assertAeadSymmetricMessage(securityPolicy, MessageSecurityMode.Sign);
  }

  private void assertAeadSymmetricMessage(
      SecurityPolicy securityPolicy, MessageSecurityMode messageSecurity) throws Exception {

    ChannelParameters[] channelParameters = {smallParameters, defaultParameters};

    for (ChannelParameters parameters : channelParameters) {
      int[] messageSizes = new int[] {1, 128, 8196};

      for (int messageSize : messageSizes) {
        ChunkEncoder encoder = new ChunkEncoder(parameters);
        ChunkDecoder decoder = new ChunkDecoder(parameters, EncodingLimits.DEFAULT);

        advanceNonLegacySequence(encoder, decoder, securityPolicy, messageSecurity);

        SecureChannel[] channels = generateAeadChannels(securityPolicy, messageSecurity);
        SecureChannel clientChannel = channels[0];
        SecureChannel serverChannel = channels[1];

        byte[] messageBytes = new byte[messageSize];
        for (int i = 0; i < messageBytes.length; i++) {
          messageBytes[i] = (byte) i;
        }

        ByteBuf messageBuffer = BufferUtil.pooledBuffer().writeBytes(messageBytes);

        try {
          ChunkEncoder.EncodedMessage message =
              encoder.encodeSymmetric(clientChannel, 1L, messageBuffer, MessageType.SecureMessage);

          ChunkDecoder.DecodedMessage decodedMessage =
              decoder.decodeSymmetric(serverChannel, message.getMessageChunks());

          ByteBuf decoded = decodedMessage.getMessage();

          try {
            messageBuffer.readerIndex(0);
            assertEquals(messageBuffer, decoded);
          } finally {
            ReferenceCountUtil.release(decoded);
          }
        } finally {
          ReferenceCountUtil.release(messageBuffer);
        }
      }
    }
  }

  // Receivers must reject corrupted AEAD tags before plaintext sequence headers or bodies are used.
  @ParameterizedTest
  @MethodSource("getAeadSecurityParameters")
  public void aeadRejectsTagCorruption(SecurityPolicy securityPolicy) throws Exception {
    ChunkEncoder encoder = new ChunkEncoder(defaultParameters);
    ChunkDecoder decoder = new ChunkDecoder(defaultParameters, EncodingLimits.DEFAULT);

    SecureChannel[] channels = generateAeadChannels(securityPolicy);
    SecureChannel clientChannel = channels[0];
    SecureChannel serverChannel = channels[1];

    advanceNonLegacySequence(encoder, decoder, securityPolicy);

    ByteBuf messageBuffer = BufferUtil.pooledBuffer().writeBytes(new byte[] {0x01, 0x02, 0x03});

    try {
      ChunkEncoder.EncodedMessage message =
          encoder.encodeSymmetric(clientChannel, 1L, messageBuffer, MessageType.SecureMessage);

      ByteBuf chunk = message.getMessageChunks().get(0);
      chunk.setByte(chunk.writerIndex() - 1, chunk.getByte(chunk.writerIndex() - 1) ^ 0x01);

      assertThrows(
          MessageDecodeException.class,
          () -> decoder.decodeSymmetric(serverChannel, message.getMessageChunks()));
    } finally {
      ReferenceCountUtil.release(messageBuffer);
    }
  }

  // The secure message and symmetric security headers are AEAD associated data; changes outside the
  // ciphertext must still invalidate the chunk.
  @ParameterizedTest
  @MethodSource("getAeadSecurityParameters")
  public void aeadRejectsAssociatedDataMismatch(SecurityPolicy securityPolicy) throws Exception {
    ChunkEncoder encoder = new ChunkEncoder(defaultParameters);
    ChunkDecoder decoder = new ChunkDecoder(defaultParameters, EncodingLimits.DEFAULT);

    SecureChannel[] channels = generateAeadChannels(securityPolicy);
    SecureChannel clientChannel = channels[0];
    SecureChannel serverChannel = channels[1];

    advanceNonLegacySequence(encoder, decoder, securityPolicy);

    ByteBuf messageBuffer = BufferUtil.pooledBuffer().writeBytes(new byte[] {0x04, 0x05, 0x06});

    try {
      ChunkEncoder.EncodedMessage message =
          encoder.encodeSymmetric(clientChannel, 1L, messageBuffer, MessageType.SecureMessage);

      ByteBuf chunk = message.getMessageChunks().get(0);
      chunk.setByte(8, chunk.getByte(8) ^ 0x01);

      assertThrows(
          MessageDecodeException.class,
          () -> decoder.decodeSymmetric(serverChannel, message.getMessageChunks()));
    } finally {
      ReferenceCountUtil.release(messageBuffer);
    }
  }

  @ParameterizedTest
  @MethodSource("getAeadSecurityParameters")
  public void aeadSignRejectsHeaderCorruption(SecurityPolicy securityPolicy) throws Exception {
    assertAeadSignRejectsCorruption(securityPolicy, Corruption.HEADER);
  }

  @ParameterizedTest
  @MethodSource("getAeadSecurityParameters")
  public void aeadSignRejectsBodyCorruption(SecurityPolicy securityPolicy) throws Exception {
    assertAeadSignRejectsCorruption(securityPolicy, Corruption.BODY);
  }

  @ParameterizedTest
  @MethodSource("getAeadSecurityParameters")
  public void aeadSignRejectsTagCorruption(SecurityPolicy securityPolicy) throws Exception {
    assertAeadSignRejectsCorruption(securityPolicy, Corruption.TAG);
  }

  private void assertAeadSignRejectsCorruption(SecurityPolicy securityPolicy, Corruption corruption)
      throws Exception {

    ChunkEncoder encoder = new ChunkEncoder(defaultParameters);
    ChunkDecoder decoder = new ChunkDecoder(defaultParameters, EncodingLimits.DEFAULT);

    SecureChannel[] channels = generateAeadChannels(securityPolicy, MessageSecurityMode.Sign);
    SecureChannel clientChannel = channels[0];
    SecureChannel serverChannel = channels[1];

    advanceNonLegacySequence(encoder, decoder, securityPolicy, MessageSecurityMode.Sign);

    ByteBuf messageBuffer = BufferUtil.pooledBuffer().writeBytes(new byte[] {0x04, 0x05, 0x06});

    try {
      ChunkEncoder.EncodedMessage message =
          encoder.encodeSymmetric(clientChannel, 1L, messageBuffer, MessageType.SecureMessage);

      ByteBuf chunk = message.getMessageChunks().get(0);
      switch (corruption) {
        case HEADER -> chunk.setByte(8, chunk.getByte(8) ^ 0x01);
        case BODY -> {
          int bodyOffset =
              SECURE_MESSAGE_HEADER_SIZE + SYMMETRIC_SECURITY_HEADER_SIZE + SEQUENCE_HEADER_SIZE;
          chunk.setByte(bodyOffset, chunk.getByte(bodyOffset) ^ 0x01);
        }
        case TAG ->
            chunk.setByte(chunk.writerIndex() - 1, chunk.getByte(chunk.writerIndex() - 1) ^ 0x01);
      }

      assertThrows(
          MessageDecodeException.class,
          () -> decoder.decodeSymmetric(serverChannel, message.getMessageChunks()));
    } finally {
      ReferenceCountUtil.release(messageBuffer);
    }
  }

  private static void advanceNonLegacySequence(
      ChunkEncoder encoder, ChunkDecoder decoder, SecurityPolicy securityPolicy) throws Exception {

    advanceNonLegacySequence(encoder, decoder, securityPolicy, MessageSecurityMode.SignAndEncrypt);
  }

  private static void advanceNonLegacySequence(
      ChunkEncoder encoder,
      ChunkDecoder decoder,
      SecurityPolicy securityPolicy,
      MessageSecurityMode messageSecurity)
      throws Exception {

    // AEAD symmetric chunks use the previous chunk's SequenceNumber in their nonce. In a real
    // connection the OpenSecureChannel chunk consumes sequence 0 before the first service chunk, so
    // these tests advance both codecs to match that channel state.
    ClientSecureChannel clientChannel = new ClientSecureChannel(securityPolicy, messageSecurity);

    ServerSecureChannel serverChannel = new ServerSecureChannel();
    serverChannel.setSecurityPolicy(securityPolicy);
    serverChannel.setMessageSecurityMode(messageSecurity);

    ByteBuf messageBuffer = BufferUtil.pooledBuffer().writeByte(0x00);

    try {
      ChunkEncoder.EncodedMessage encodedMessage =
          encoder.encodeAsymmetric(clientChannel, 0L, messageBuffer, MessageType.OpenSecureChannel);
      ChunkDecoder.DecodedMessage decodedMessage =
          decoder.decodeAsymmetric(serverChannel, encodedMessage.getMessageChunks());

      ReferenceCountUtil.release(decodedMessage.getMessage());
    } finally {
      ReferenceCountUtil.release(messageBuffer);
    }
  }

  private enum Corruption {
    HEADER,
    BODY,
    TAG
  }
}
