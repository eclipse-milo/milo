/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.channel;

import static org.eclipse.milo.opcua.stack.core.channel.headers.SecureMessageHeader.SECURE_MESSAGE_HEADER_SIZE;
import static org.eclipse.milo.opcua.stack.core.channel.headers.SequenceHeader.SEQUENCE_HEADER_SIZE;

import io.netty.buffer.ByteBuf;
import io.netty.util.ReferenceCountUtil;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.headers.AsymmetricSecurityHeader;
import org.eclipse.milo.opcua.stack.core.channel.headers.SecureMessageHeader;
import org.eclipse.milo.opcua.stack.core.channel.headers.SequenceHeader;
import org.eclipse.milo.opcua.stack.core.channel.headers.SymmetricSecurityHeader;
import org.eclipse.milo.opcua.stack.core.channel.messages.MessageType;
import org.eclipse.milo.opcua.stack.core.security.SecurityAlgorithm;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.BufferUtil;
import org.eclipse.milo.opcua.stack.core.util.LongSequence;
import org.eclipse.milo.opcua.stack.core.util.SignatureUtil;

public final class ChunkEncoder {

  private final AsymmetricEncoder asymmetricEncoder = new AsymmetricEncoder();
  private final SymmetricEncoder symmetricEncoder = new SymmetricEncoder();

  // The SequenceNumber shall monotonically increase for all Messages and shall not wrap around
  // until it is greater than 4_294_966_271 (UInt32.MaxValue – 1024). The first number after the
  // wrap around shall be less than 1024.
  private final LongSequence sequenceNumber = new LongSequence(1L, UInteger.MAX_VALUE - 1024 + 1);

  private final ChannelParameters parameters;

  public ChunkEncoder(ChannelParameters parameters) {
    this.parameters = parameters;
  }

  public EncodedMessage encodeAsymmetric(
      SecureChannel channel, long requestId, ByteBuf messageBuffer, MessageType messageType)
      throws MessageEncodeException {

    return encode(asymmetricEncoder, channel, requestId, messageBuffer, messageType);
  }

  public EncodedMessage encodeSymmetric(
      SecureChannel channel, long requestId, ByteBuf messageBuffer, MessageType messageType)
      throws MessageEncodeException {

    return encode(symmetricEncoder, channel, requestId, messageBuffer, messageType);
  }

  private EncodedMessage encode(
      AbstractEncoder encoder,
      SecureChannel channel,
      long requestId,
      ByteBuf messageBuffer,
      MessageType messageType)
      throws MessageEncodeException {

    List<ByteBuf> chunks = new ArrayList<>();

    try {
      return encoder.encode(chunks, channel, requestId, messageBuffer, messageType);
    } catch (UaException e) {
      chunks.forEach(ReferenceCountUtil::safeRelease);

      throw new MessageEncodeException(e);
    }
  }

  private abstract class AbstractEncoder {

    EncodedMessage encode(
        List<ByteBuf> chunks,
        SecureChannel channel,
        long requestId,
        ByteBuf messageBuffer,
        MessageType messageType)
        throws UaException {

      boolean encrypted = isEncryptionEnabled(channel);

      int securityHeaderSize = getSecurityHeaderSize(channel);
      int cipherTextBlockSize = getCipherTextBlockSize(channel);
      int plainTextBlockSize = getPlainTextBlockSize(channel);
      int signatureSize = getSignatureSize(channel);

      int maxChunkSize = parameters.getLocalSendBufferSize();
      int paddingOverhead = encrypted ? (cipherTextBlockSize > 256 ? 2 : 1) : 0;

      int maxCipherTextSize = maxChunkSize - SECURE_MESSAGE_HEADER_SIZE - securityHeaderSize;
      int maxCipherTextBlocks = maxCipherTextSize / cipherTextBlockSize;
      int maxPlainTextSize = maxCipherTextBlocks * plainTextBlockSize;
      int maxBodySize = maxPlainTextSize - SEQUENCE_HEADER_SIZE - paddingOverhead - signatureSize;

      assert (maxPlainTextSize + securityHeaderSize + SECURE_MESSAGE_HEADER_SIZE <= maxChunkSize);

      while (messageBuffer.readableBytes() > 0) {
        int bodySize = Math.min(messageBuffer.readableBytes(), maxBodySize);

        int paddingSize;
        if (encrypted) {
          int plainTextSize = SEQUENCE_HEADER_SIZE + bodySize + paddingOverhead + signatureSize;
          int remaining = plainTextSize % plainTextBlockSize;
          paddingSize = remaining > 0 ? plainTextBlockSize - remaining : 0;
        } else {
          paddingSize = 0;
        }

        int plainTextContentSize =
            SEQUENCE_HEADER_SIZE + bodySize + signatureSize + paddingSize + paddingOverhead;

        assert (plainTextContentSize % plainTextBlockSize == 0);

        int chunkSize =
            SecureMessageHeader.SECURE_MESSAGE_HEADER_SIZE
                + securityHeaderSize
                + (plainTextContentSize / plainTextBlockSize) * cipherTextBlockSize;

        assert (chunkSize <= maxChunkSize);

        ByteBuf chunkBuffer = BufferUtil.pooledBuffer(chunkSize);

        chunks.add(chunkBuffer);

        int remoteMaxChunkCount = parameters.getRemoteMaxChunkCount();
        if (remoteMaxChunkCount > 0 && chunks.size() > remoteMaxChunkCount) {
          throw new UaException(
              StatusCodes.Bad_EncodingLimitsExceeded,
              "remote chunk count exceeded: " + remoteMaxChunkCount);
        }

        /* Message Header */
        SecureMessageHeader messageHeader =
            new SecureMessageHeader(
                messageType,
                messageBuffer.readableBytes() > bodySize ? 'C' : 'F',
                chunkSize,
                channel.getChannelId());

        SecureMessageHeader.encode(messageHeader, chunkBuffer);

        /* Security Header */
        encodeSecurityHeader(channel, chunkBuffer);

        /* Sequence Header */
        SequenceHeader sequenceHeader =
            new SequenceHeader(sequenceNumber.getAndIncrement(), requestId);

        SequenceHeader.encode(sequenceHeader, chunkBuffer);

        /* Message Body */
        chunkBuffer.writeBytes(messageBuffer, bodySize);

        /* Padding and Signature */
        if (encrypted) {
          writePadding(cipherTextBlockSize, paddingSize, chunkBuffer);
        }

        if (isSigningEnabled(channel)) {
          ByteBuffer chunkNioBuffer = chunkBuffer.nioBuffer(0, chunkBuffer.writerIndex());

          byte[] signature = signChunk(channel, chunkNioBuffer);

          chunkBuffer.writeBytes(signature);
        }

        /* Encryption */
        if (encrypted) {
          chunkBuffer.readerIndex(SECURE_MESSAGE_HEADER_SIZE + securityHeaderSize);

          assert (chunkBuffer.readableBytes() % plainTextBlockSize == 0);

          try {
            int blockCount = chunkBuffer.readableBytes() / plainTextBlockSize;

            ByteBuffer chunkNioBuffer =
                chunkBuffer.nioBuffer(chunkBuffer.readerIndex(), blockCount * cipherTextBlockSize);

            ByteBuf copyBuffer = chunkBuffer.copy();
            ByteBuffer plainTextNioBuffer = copyBuffer.nioBuffer();

            Cipher cipher = getCipher(channel);

            if (isAsymmetric()) {
              for (int blockNumber = 0; blockNumber < blockCount; blockNumber++) {
                int position = blockNumber * plainTextBlockSize;
                int limit = (blockNumber + 1) * plainTextBlockSize;
                ((Buffer) plainTextNioBuffer).position(position);
                ((Buffer) plainTextNioBuffer).limit(limit);

                int bytesWritten = cipher.doFinal(plainTextNioBuffer, chunkNioBuffer);

                assert (bytesWritten == cipherTextBlockSize);
              }
            } else {
              cipher.doFinal(plainTextNioBuffer, chunkNioBuffer);
            }

            copyBuffer.release();
          } catch (GeneralSecurityException e) {
            throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
          }
        }

        chunkBuffer.readerIndex(0).writerIndex(chunkSize);
      }

      return new EncodedMessage(chunks, requestId);
    }

    private void writePadding(int cipherTextBlockSize, int paddingSize, ByteBuf buffer) {
      buffer.writeByte(paddingSize);

      for (int i = 0; i < paddingSize; i++) {
        buffer.writeByte(paddingSize);
      }

      if (cipherTextBlockSize > 256) {
        // Add the extra padding byte containing
        // the MSB of the 2-byte padding length.
        int paddingLengthMsb = (paddingSize >> 8) & 0xFF;
        buffer.writeByte(paddingLengthMsb);
      }
    }

    protected abstract byte[] signChunk(SecureChannel channel, ByteBuffer chunkNioBuffer)
        throws UaException;

    protected abstract void encodeSecurityHeader(SecureChannel channel, ByteBuf buffer)
        throws UaException;

    protected abstract Cipher getCipher(SecureChannel channel) throws UaException;

    protected abstract int getSecurityHeaderSize(SecureChannel channel) throws UaException;

    protected abstract int getCipherTextBlockSize(SecureChannel channel);

    protected abstract int getPlainTextBlockSize(SecureChannel channel);

    protected abstract int getSignatureSize(SecureChannel channel);

    protected abstract boolean isAsymmetric();

    protected abstract boolean isEncryptionEnabled(SecureChannel channel);

    protected abstract boolean isSigningEnabled(SecureChannel channel);
  }

  /** A fully encoded message, in one more more chunks ready to send. */
  public static class EncodedMessage {

    private final List<ByteBuf> messageChunks;
    private final long requestId;

    public EncodedMessage(List<ByteBuf> messageChunks, long requestId) {
      this.messageChunks = messageChunks;
      this.requestId = requestId;
    }

    public List<ByteBuf> getMessageChunks() {
      return messageChunks;
    }

    public long getRequestId() {
      return requestId;
    }
  }

  private final class AsymmetricEncoder extends AbstractEncoder {

    @Override
    public byte[] signChunk(SecureChannel channel, ByteBuffer chunkNioBuffer) throws UaException {
      return SignatureUtil.sign(
          channel.getSecurityPolicy().getAsymmetricSignatureAlgorithm(),
          channel.getKeyPair().getPrivate(),
          chunkNioBuffer);
    }

    @Override
    public Cipher getCipher(SecureChannel channel) throws UaException {
      Certificate remoteCertificate = channel.getRemoteCertificate();

      assert (remoteCertificate != null);

      try {
        String transformation =
            channel.getSecurityPolicy().getAsymmetricEncryptionAlgorithm().getTransformation();
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, remoteCertificate.getPublicKey());
        return cipher;
      } catch (GeneralSecurityException e) {
        throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
      }
    }

    @Override
    public void encodeSecurityHeader(SecureChannel channel, ByteBuf buffer) throws UaException {
      AsymmetricSecurityHeader header =
          new AsymmetricSecurityHeader(
              channel.getSecurityPolicy().getUri(),
              channel.getLocalCertificateChainBytes(),
              channel.getRemoteCertificateThumbprint());

      AsymmetricSecurityHeader.encode(header, buffer);
    }

    @Override
    public int getSecurityHeaderSize(SecureChannel channel) throws UaException {
      String securityPolicyUri = channel.getSecurityPolicy().getUri();
      byte[] localCertificateChainBytes = channel.getLocalCertificateChainBytes().bytes();
      byte[] remoteCertificateThumbprint = channel.getRemoteCertificateThumbprint().bytes();

      return 12
          + securityPolicyUri.length()
          + (localCertificateChainBytes != null ? localCertificateChainBytes.length : 0)
          + (remoteCertificateThumbprint != null ? remoteCertificateThumbprint.length : 0);
    }

    @Override
    public int getCipherTextBlockSize(SecureChannel channel) {
      return channel.getRemoteAsymmetricCipherTextBlockSize();
    }

    @Override
    public int getPlainTextBlockSize(SecureChannel channel) {
      return channel.getRemoteAsymmetricPlainTextBlockSize();
    }

    @Override
    public int getSignatureSize(SecureChannel channel) {
      return channel.getLocalAsymmetricSignatureSize();
    }

    @Override
    protected boolean isAsymmetric() {
      return true;
    }

    @Override
    public boolean isEncryptionEnabled(SecureChannel channel) {
      return channel.isAsymmetricEncryptionEnabled();
    }

    @Override
    public boolean isSigningEnabled(SecureChannel channel) {
      return channel.isAsymmetricSigningEnabled();
    }
  }

  private final class SymmetricEncoder extends AbstractEncoder {

    private volatile ChannelSecurity.SecurityKeys securityKeys;
    private volatile Cipher cipher = null;
    private volatile long cipherId = -1;

    @Override
    public void encodeSecurityHeader(SecureChannel channel, ByteBuf buffer) throws UaException {
      ChannelSecurity channelSecurity = channel.getChannelSecurity();
      long tokenId =
          channelSecurity != null ? channelSecurity.getCurrentToken().getTokenId().longValue() : 0L;

      SymmetricSecurityHeader.encode(new SymmetricSecurityHeader(tokenId), buffer);

      securityKeys = channelSecurity != null ? channelSecurity.getCurrentKeys() : null;

      if (cipherId != tokenId && channel.isSymmetricEncryptionEnabled()) {
        cipher = initCipher(channel);
        cipherId = tokenId;
      }
    }

    @Override
    public byte[] signChunk(SecureChannel channel, ByteBuffer chunkNioBuffer) throws UaException {
      SecurityAlgorithm signatureAlgorithm =
          channel.getSecurityPolicy().getSymmetricSignatureAlgorithm();
      byte[] signatureKey = channel.getEncryptionKeys(securityKeys).getSignatureKey();

      return SignatureUtil.hmac(signatureAlgorithm, signatureKey, chunkNioBuffer);
    }

    @Override
    public Cipher getCipher(SecureChannel channel) {
      assert cipher != null;
      return cipher;
    }

    @Override
    public int getSecurityHeaderSize(SecureChannel channel) {
      return SymmetricSecurityHeader.SYMMETRIC_SECURITY_HEADER_SIZE;
    }

    @Override
    public int getCipherTextBlockSize(SecureChannel channel) {
      return channel.getSymmetricBlockSize();
    }

    @Override
    public int getPlainTextBlockSize(SecureChannel channel) {
      return channel.getSymmetricBlockSize();
    }

    @Override
    public int getSignatureSize(SecureChannel channel) {
      return channel.getSymmetricSignatureSize();
    }

    @Override
    protected boolean isAsymmetric() {
      return false;
    }

    @Override
    public boolean isEncryptionEnabled(SecureChannel channel) {
      return channel.isSymmetricEncryptionEnabled();
    }

    @Override
    public boolean isSigningEnabled(SecureChannel channel) {
      return channel.isSymmetricSigningEnabled();
    }

    private Cipher initCipher(SecureChannel channel) throws UaException {
      try {
        String transformation =
            channel.getSecurityPolicy().getSymmetricEncryptionAlgorithm().getTransformation();
        ChannelSecurity.SecretKeys secretKeys = channel.getEncryptionKeys(securityKeys);

        SecretKeySpec keySpec = new SecretKeySpec(secretKeys.getEncryptionKey(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(secretKeys.getInitializationVector());

        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

        assert (cipher.getBlockSize() == channel.getSymmetricBlockSize());

        return cipher;
      } catch (GeneralSecurityException e) {
        throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
      }
    }
  }
}
