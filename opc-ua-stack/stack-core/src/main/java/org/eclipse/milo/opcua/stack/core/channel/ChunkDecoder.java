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

import static org.eclipse.milo.opcua.stack.core.channel.ChunkDecoder.LegacySequenceNumberValidator.validateSequenceNumber;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.CompositeByteBuf;
import io.netty.util.ReferenceCountUtil;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.util.List;
import javax.crypto.Cipher;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.headers.AsymmetricSecurityHeader;
import org.eclipse.milo.opcua.stack.core.channel.headers.SecureMessageHeader;
import org.eclipse.milo.opcua.stack.core.channel.headers.SequenceHeader;
import org.eclipse.milo.opcua.stack.core.channel.headers.SymmetricSecurityHeader;
import org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.BufferUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Decodes OPC UA Secure Conversation chunks into complete service payload messages.
 *
 * <p>The decoder mirrors {@link ChunkEncoder}: asymmetric chunks are used for OpenSecureChannel
 * traffic protected with endpoint certificates, and symmetric chunks are used after a channel token
 * has installed directional symmetric keys. The symmetric path accepts the current token and,
 * during token renewal, the previous token retained by {@link ChannelSecurity}.
 *
 * <p>Each chunk is processed in the order required by UASC framing: read the security header,
 * decrypt when needed, verify the signature or authentication data, validate the sequence header,
 * remove padding, and append the body bytes to the decoded message. Abort chunks are surfaced as
 * {@link MessageAbortException}; security and framing failures are surfaced as {@link
 * MessageDecodeException}.
 *
 * <p>A decoder instance tracks incoming sequence numbers for the stream it is assigned to. Use one
 * decoder per ordered channel stream so sequence validation reflects the peer's actual message
 * order.
 */
public final class ChunkDecoder {

  private final AsymmetricDecoder asymmetricDecoder = new AsymmetricDecoder();
  private final SymmetricDecoder symmetricDecoder = new SymmetricDecoder();

  private volatile long lastSequenceNumber = -1L;

  private final ChannelParameters parameters;
  private final EncodingLimits encodingLimits;

  public ChunkDecoder(ChannelParameters parameters, EncodingLimits encodingLimits) {
    this.parameters = parameters;
    this.encodingLimits = encodingLimits;
  }

  /** Decodes one OpenSecureChannel message from chunks carrying asymmetric security headers. */
  public DecodedMessage decodeAsymmetric(SecureChannel channel, List<ByteBuf> chunkBuffers)
      throws MessageAbortException, MessageDecodeException {

    return decode(asymmetricDecoder, channel, chunkBuffers);
  }

  /**
   * Decodes one service message from chunks carrying symmetric security headers.
   *
   * <p>Before decoding chunk bodies, this method verifies that every chunk references the channel's
   * current token or previous renewal token.
   */
  public DecodedMessage decodeSymmetric(SecureChannel channel, List<ByteBuf> chunkBuffers)
      throws MessageAbortException, MessageDecodeException {

    try {
      validateSymmetricSecurityHeaders(channel, chunkBuffers);
    } catch (UaException e) {
      chunkBuffers.forEach(ReferenceCountUtil::safeRelease);
      throw new MessageDecodeException(e);
    }

    return decode(symmetricDecoder, channel, chunkBuffers);
  }

  private static DecodedMessage decode(
      AbstractDecoder decoder, SecureChannel channel, List<ByteBuf> chunkBuffers)
      throws MessageAbortException, MessageDecodeException {

    CompositeByteBuf composite = BufferUtil.compositeBuffer();

    try {
      return decoder.decode(channel, composite, chunkBuffers);
    } catch (MessageAbortException e) {
      ReferenceCountUtil.safeRelease(composite);
      chunkBuffers.forEach(ReferenceCountUtil::safeRelease);
      throw e;
    } catch (UaException e) {
      ReferenceCountUtil.safeRelease(composite);
      chunkBuffers.forEach(ReferenceCountUtil::safeRelease);
      throw new MessageDecodeException(e);
    }
  }

  private static void validateSymmetricSecurityHeaders(
      SecureChannel secureChannel, List<ByteBuf> chunkBuffers) throws UaException {

    ChannelSecurity channelSecurity = secureChannel.getChannelSecurity();
    long currentTokenId =
        channelSecurity != null ? channelSecurity.getCurrentToken().getTokenId().longValue() : 0L;
    long previousTokenId =
        channelSecurity != null
            ? channelSecurity.getPreviousToken().map(t -> t.getTokenId().longValue()).orElse(-1L)
            : -1L;

    for (ByteBuf chunkBuffer : chunkBuffers) {
      // tokenId starts after messageType + chunkType + messageSize + secureChannelId
      long tokenId = chunkBuffer.getUnsignedIntLE(3 + 1 + 4 + 4);

      if (tokenId != currentTokenId && tokenId != previousTokenId) {
        String message =
            String.format(
                "received unknown secure channel token: "
                    + "tokenId=%s currentTokenId=%s previousTokenId=%s",
                tokenId, currentTokenId, previousTokenId);

        throw new UaException(StatusCodes.Bad_SecureChannelTokenUnknown, message);
      }
    }
  }

  /** A full decoded message, assembled from one or more successfully decoded chunks. */
  public static class DecodedMessage {

    private final ByteBuf message;
    private final long requestId;

    private DecodedMessage(ByteBuf message, long requestId) {
      this.message = message;
      this.requestId = requestId;
    }

    public ByteBuf getMessage() {
      return message;
    }

    public long getRequestId() {
      return requestId;
    }
  }

  private abstract class AbstractDecoder {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    DecodedMessage decode(
        SecureChannel channel, CompositeByteBuf composite, List<ByteBuf> chunkBuffers)
        throws MessageAbortException, UaException {

      int signatureSize = getSignatureSize(channel);
      int cipherTextBlockSize = getCipherTextBlockSize(channel);

      boolean encrypted = isEncryptionEnabled(channel);
      boolean signed = isSigningEnabled(channel);

      long requestId = -1L;

      for (ByteBuf chunkBuffer : chunkBuffers) {
        final char chunkType = (char) chunkBuffer.getByte(3);

        chunkBuffer.skipBytes(SecureMessageHeader.SECURE_MESSAGE_HEADER_SIZE);

        readSecurityHeader(channel, chunkBuffer);

        if (encrypted) {
          decryptChunk(channel, chunkBuffer);
        }

        int encryptedStart = chunkBuffer.readerIndex();
        chunkBuffer.readerIndex(0);

        if (signed) {
          verifyChunk(channel, chunkBuffer);
        }

        final int paddingOverhead =
            encrypted ? getPaddingOverhead(channel, cipherTextBlockSize) : 0;
        final int paddingSize =
            encrypted
                ? getPaddingSize(channel, cipherTextBlockSize, signatureSize, chunkBuffer)
                : 0;
        final int bodyEnd =
            chunkBuffer.readableBytes() - signatureSize - paddingOverhead - paddingSize;

        chunkBuffer.readerIndex(encryptedStart);

        SequenceHeader sequenceHeader = SequenceHeader.decode(chunkBuffer);
        long sequenceNumber = sequenceHeader.getSequenceNumber();
        requestId = sequenceHeader.getRequestId();

        if (!validateSequenceNumber(lastSequenceNumber, sequenceNumber)) {
          throw new UaException(
              StatusCodes.Bad_SecurityChecksFailed,
              String.format(
                  "bad sequence number: %s, lastSequenceNumber=%s",
                  sequenceNumber, lastSequenceNumber));
        }

        lastSequenceNumber = sequenceNumber;

        ByteBuf bodyBuffer = chunkBuffer.readSlice(bodyEnd - chunkBuffer.readerIndex());

        if (encrypted) {
          verifyPadding(
              channel,
              cipherTextBlockSize,
              signatureSize,
              paddingOverhead,
              paddingSize,
              chunkBuffer);
        }

        if (chunkType == 'A') {
          ErrorMessage errorMessage = ErrorMessage.decode(bodyBuffer);

          throw new MessageAbortException(
              errorMessage.getReason(), requestId, errorMessage.getError());
        }

        composite.addComponent(bodyBuffer);
        composite.writerIndex(composite.writerIndex() + bodyBuffer.readableBytes());
      }

      if (parameters.getLocalMaxMessageSize() > 0
          && composite.readableBytes() > parameters.getLocalMaxMessageSize()) {

        String errorMessage =
            String.format(
                "message size exceeds configured limit: %s > %s",
                composite.readableBytes(), parameters.getLocalMaxMessageSize());

        throw new UaException(StatusCodes.Bad_TcpMessageTooLarge, errorMessage);
      }

      return new DecodedMessage(composite, requestId);
    }

    private void decryptChunk(SecureChannel channel, ByteBuf chunkBuffer) throws UaException {
      int cipherTextBlockSize = getCipherTextBlockSize(channel);
      int blockCount = chunkBuffer.readableBytes() / cipherTextBlockSize;

      int plainTextBufferSize = cipherTextBlockSize * blockCount;

      ByteBuf plainTextBuffer = BufferUtil.pooledBuffer(plainTextBufferSize);

      ByteBuffer plainTextNioBuffer = plainTextBuffer.writerIndex(plainTextBufferSize).nioBuffer();

      ByteBuffer chunkNioBuffer = chunkBuffer.nioBuffer();

      try {
        Cipher cipher = getCipher(channel);

        assert (chunkBuffer.readableBytes() % cipherTextBlockSize == 0);

        if (isAsymmetric()) {
          for (int blockNumber = 0; blockNumber < blockCount; blockNumber++) {
            ((Buffer) chunkNioBuffer).limit(chunkNioBuffer.position() + cipherTextBlockSize);

            cipher.doFinal(chunkNioBuffer, plainTextNioBuffer);
          }
        } else {
          cipher.doFinal(chunkNioBuffer, plainTextNioBuffer);
        }

        /* Write plainTextBuffer back into the chunk buffer we decrypted from. */
        ((Buffer) plainTextNioBuffer).flip(); // limit = pos, pos = 0

        chunkBuffer.writerIndex(chunkBuffer.readerIndex());
        chunkBuffer.writeBytes(plainTextNioBuffer);
      } catch (GeneralSecurityException e) {
        throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
      } finally {
        plainTextBuffer.release();
      }
    }

    protected abstract void readSecurityHeader(SecureChannel channel, ByteBuf chunkBuffer)
        throws UaException;

    protected abstract Cipher getCipher(SecureChannel channel) throws UaException;

    protected abstract int getCipherTextBlockSize(SecureChannel channel);

    protected abstract int getSignatureSize(SecureChannel channel);

    protected abstract void verifyChunk(SecureChannel channel, ByteBuf chunkBuffer)
        throws UaException;

    protected abstract int getPaddingOverhead(SecureChannel channel, int cipherTextBlockSize);

    protected abstract int getPaddingSize(
        SecureChannel channel, int cipherTextBlockSize, int signatureSize, ByteBuf buffer);

    protected abstract void verifyPadding(
        SecureChannel channel,
        int cipherTextBlockSize,
        int signatureSize,
        int paddingOverhead,
        int paddingSize,
        ByteBuf buffer)
        throws UaException;

    protected abstract boolean isAsymmetric();

    protected abstract boolean isEncryptionEnabled(SecureChannel channel);

    protected abstract boolean isSigningEnabled(SecureChannel channel);
  }

  private final class AsymmetricDecoder extends AbstractDecoder {

    @Override
    public void readSecurityHeader(SecureChannel channel, ByteBuf chunkBuffer) {
      AsymmetricSecurityHeader.decode(chunkBuffer, encodingLimits);
    }

    @Override
    public Cipher getCipher(SecureChannel channel) throws UaException {
      return SecureChannelStrategies.asymmetricEncryption(channel.getSecurityPolicyProfile())
          .getDecryptionCipher(
              channel.getSecurityPolicyProfile(), channel.getKeyPair().getPrivate());
    }

    @Override
    public int getCipherTextBlockSize(SecureChannel channel) {
      return channel.getLocalAsymmetricCipherTextBlockSize();
    }

    @Override
    public int getSignatureSize(SecureChannel channel) {
      return channel.getRemoteAsymmetricSignatureSize();
    }

    @Override
    public void verifyChunk(SecureChannel channel, ByteBuf chunkBuffer) throws UaException {
      int signatureSize = channel.getRemoteAsymmetricSignatureSize();

      ByteBuffer signedBytes = chunkBuffer.nioBuffer(0, chunkBuffer.writerIndex() - signatureSize);
      ByteBuffer signatureBuffer =
          chunkBuffer.nioBuffer(chunkBuffer.writerIndex() - signatureSize, signatureSize);

      byte[] signatureBytes = new byte[signatureSize];
      signatureBuffer.get(signatureBytes);

      SecureChannelStrategies.authentication(channel.getSecurityPolicyProfile())
          .verify(
              channel.getSecurityPolicyProfile(),
              channel.getRemoteCertificate(),
              signedBytes,
              signatureBytes);
    }

    @Override
    protected int getPaddingOverhead(SecureChannel channel, int cipherTextBlockSize) {
      return SecureChannelStrategies.asymmetricEncryption(channel.getSecurityPolicyProfile())
          .paddingOverhead(cipherTextBlockSize);
    }

    @Override
    protected int getPaddingSize(
        SecureChannel channel, int cipherTextBlockSize, int signatureSize, ByteBuf buffer) {

      return SecureChannelStrategies.asymmetricEncryption(channel.getSecurityPolicyProfile())
          .getPaddingSize(cipherTextBlockSize, signatureSize, buffer);
    }

    @Override
    protected void verifyPadding(
        SecureChannel channel,
        int cipherTextBlockSize,
        int signatureSize,
        int paddingOverhead,
        int paddingSize,
        ByteBuf buffer)
        throws UaException {

      SecureChannelStrategies.asymmetricEncryption(channel.getSecurityPolicyProfile())
          .verifyPadding(cipherTextBlockSize, signatureSize, paddingOverhead, paddingSize, buffer);
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

  private final class SymmetricDecoder extends AbstractDecoder {

    private volatile ChannelSecurity.SecurityKeys securityKeys;
    private volatile Cipher cipher = null;
    private volatile long cipherId = -1;

    @Override
    public void readSecurityHeader(SecureChannel channel, ByteBuf chunkBuffer) throws UaException {
      long receivedTokenId = SymmetricSecurityHeader.decode(chunkBuffer).getTokenId();

      ChannelSecurity channelSecurity = channel.getChannelSecurity();

      if (channelSecurity == null) {
        if (receivedTokenId != 0L) {
          throw new UaException(
              StatusCodes.Bad_SecureChannelTokenUnknown,
              "unknown secure channel token: " + receivedTokenId);
        }
      } else {
        long currentTokenId = channelSecurity.getCurrentToken().getTokenId().longValue();

        if (receivedTokenId == currentTokenId) {
          securityKeys = channelSecurity.getCurrentKeys();
        } else {
          long previousTokenId =
              channelSecurity.getPreviousToken().map(t -> t.getTokenId().longValue()).orElse(-1L);

          logger.debug("Attempting to use SecurityKeys from previousTokenId={}", previousTokenId);

          if (receivedTokenId != previousTokenId) {
            logger.warn(
                "receivedTokenId={} did not match previousTokenId={}",
                receivedTokenId,
                previousTokenId);

            throw new UaException(
                StatusCodes.Bad_SecureChannelTokenUnknown,
                "unknown secure channel token: " + receivedTokenId);
          }

          if (channel.isSymmetricEncryptionEnabled()
              && channelSecurity.getPreviousKeys().isPresent()) {
            securityKeys = channelSecurity.getPreviousKeys().get();
          }
        }

        if (cipherId != receivedTokenId && channel.isSymmetricEncryptionEnabled()) {
          cipher = initCipher(channel);
          cipherId = receivedTokenId;
        }
      }
    }

    @Override
    public Cipher getCipher(SecureChannel channel) {
      assert cipher != null;
      return cipher;
    }

    @Override
    public int getCipherTextBlockSize(SecureChannel channel) {
      return channel.isSymmetricEncryptionEnabled()
          ? chunkProtection(channel).cipherTextBlockSize(channel.getSecurityPolicyProfile())
          : 1;
    }

    @Override
    public int getSignatureSize(SecureChannel channel) {
      return chunkProtection(channel).signatureSize(channel.getSecurityPolicyProfile());
    }

    @Override
    public void verifyChunk(SecureChannel channel, ByteBuf chunkBuffer) throws UaException {
      chunkProtection(channel).verify(channel, securityKeys, chunkBuffer);
    }

    @Override
    protected int getPaddingOverhead(SecureChannel channel, int cipherTextBlockSize) {
      return chunkProtection(channel).paddingOverhead(cipherTextBlockSize);
    }

    @Override
    protected int getPaddingSize(
        SecureChannel channel, int cipherTextBlockSize, int signatureSize, ByteBuf buffer) {

      return chunkProtection(channel).getPaddingSize(cipherTextBlockSize, signatureSize, buffer);
    }

    @Override
    protected void verifyPadding(
        SecureChannel channel,
        int cipherTextBlockSize,
        int signatureSize,
        int paddingOverhead,
        int paddingSize,
        ByteBuf buffer)
        throws UaException {

      chunkProtection(channel)
          .verifyPadding(cipherTextBlockSize, signatureSize, paddingOverhead, paddingSize, buffer);
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
      return chunkProtection(channel).getDecryptionCipher(channel, securityKeys);
    }

    private SecureChannelStrategies.ChunkProtectionStrategy chunkProtection(SecureChannel channel) {
      return SecureChannelStrategies.chunkProtection(channel.getSecurityPolicyProfile());
    }
  }

  /**
   * Validate incoming chunk sequence numbers based on the "legacy" rules.
   *
   * <p>These rules apply to all non-ECC security profiles.
   *
   * <p>Sequence numbers monotonically increase and shall not wrap around until greater than
   * UInteger.MAX_VALUE - 1024. The first number after the wrap around shall be less than 1024.
   */
  static class LegacySequenceNumberValidator {

    private LegacySequenceNumberValidator() {}

    static boolean validateSequenceNumber(long lastSequenceNumber, long sequenceNumber) {
      if (lastSequenceNumber == -1) {
        // awaiting first chunk, no expectation on sequence number, it can technically
        // start at any value
        return true;
      } else if (lastSequenceNumber >= UInteger.MAX_VALUE - 1024
          && lastSequenceNumber < UInteger.MAX_VALUE) {
        // sequence number must be either:
        //  - wrapped to less than 1024
        //  - equal to sequence number + 1
        return sequenceNumber < 1024 || sequenceNumber == lastSequenceNumber + 1;
      } else if (lastSequenceNumber == UInteger.MAX_VALUE) {
        // must wrap at this point
        return sequenceNumber >= 0 && sequenceNumber < 1024;
      } else {
        return sequenceNumber == lastSequenceNumber + 1;
      }
    }
  }
}
