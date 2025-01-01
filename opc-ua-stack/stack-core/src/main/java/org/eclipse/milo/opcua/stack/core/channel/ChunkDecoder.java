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
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
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
import org.eclipse.milo.opcua.stack.core.channel.messages.ErrorMessage;
import org.eclipse.milo.opcua.stack.core.security.SecurityAlgorithm;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.BufferUtil;
import org.eclipse.milo.opcua.stack.core.util.SignatureUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

  public DecodedMessage decodeAsymmetric(SecureChannel channel, List<ByteBuf> chunkBuffers)
      throws MessageAbortException, MessageDecodeException {

    return decode(asymmetricDecoder, channel, chunkBuffers);
  }

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
    long currentTokenId = channelSecurity.getCurrentToken().getTokenId().longValue();
    long previousTokenId =
        channelSecurity.getPreviousToken().map(t -> t.getTokenId().longValue()).orElse(-1L);

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

        final int paddingOverhead = encrypted ? (cipherTextBlockSize > 256 ? 2 : 1) : 0;
        final int paddingSize =
            encrypted ? getPaddingSize(cipherTextBlockSize, signatureSize, chunkBuffer) : 0;
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
          int expectedPaddingSize = chunkBuffer.readableBytes() - signatureSize - paddingOverhead;
          if (paddingSize != expectedPaddingSize) {
            throw new UaException(StatusCodes.Bad_SecurityChecksFailed, "bad padding size");
          }
          byte expectedPaddingByte = (byte) (paddingSize & 0xFF);
          for (int i = chunkBuffer.readerIndex();
              i < chunkBuffer.readerIndex() + paddingSize + 1;
              i++) {
            if (chunkBuffer.getByte(i) != expectedPaddingByte) {
              throw new UaException(StatusCodes.Bad_SecurityChecksFailed, "bad padding sequence");
            }
          }
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

    private int getPaddingSize(int cipherTextBlockSize, int signatureSize, ByteBuf buffer) {
      int lastPaddingByteOffset = buffer.readableBytes() - signatureSize - 1;

      return cipherTextBlockSize <= 256
          ? buffer.getUnsignedByte(lastPaddingByteOffset)
          : buffer.getUnsignedShortLE(lastPaddingByteOffset - 1);
    }

    protected abstract void readSecurityHeader(SecureChannel channel, ByteBuf chunkBuffer)
        throws UaException;

    protected abstract Cipher getCipher(SecureChannel channel) throws UaException;

    protected abstract int getCipherTextBlockSize(SecureChannel channel);

    protected abstract int getSignatureSize(SecureChannel channel);

    protected abstract void verifyChunk(SecureChannel channel, ByteBuf chunkBuffer)
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
      try {
        String transformation =
            channel.getSecurityPolicy().getAsymmetricEncryptionAlgorithm().getTransformation();
        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, channel.getKeyPair().getPrivate());
        return cipher;
      } catch (GeneralSecurityException e) {
        throw new UaException(StatusCodes.Bad_InternalError, e);
      }
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
      String transformation =
          channel.getSecurityPolicy().getAsymmetricSignatureAlgorithm().getTransformation();
      int signatureSize = channel.getRemoteAsymmetricSignatureSize();

      ByteBuffer chunkNioBuffer = chunkBuffer.nioBuffer(0, chunkBuffer.writerIndex());
      ((Buffer) chunkNioBuffer).position(0);
      ((Buffer) chunkNioBuffer).limit(chunkBuffer.writerIndex() - signatureSize);

      try {
        Signature signature = Signature.getInstance(transformation);

        signature.initVerify(channel.getRemoteCertificate().getPublicKey());
        signature.update(chunkNioBuffer);

        byte[] signatureBytes = new byte[signatureSize];
        ((Buffer) chunkNioBuffer).limit(chunkNioBuffer.position() + signatureSize);
        chunkNioBuffer.get(signatureBytes);

        if (!signature.verify(signatureBytes)) {
          throw new UaException(StatusCodes.Bad_SecurityChecksFailed, "could not verify signature");
        }
      } catch (NoSuchAlgorithmException e) {
        throw new UaException(StatusCodes.Bad_InternalError, e);
      } catch (SignatureException e) {
        throw new UaException(StatusCodes.Bad_ApplicationSignatureInvalid, e);
      } catch (InvalidKeyException e) {
        throw new UaException(StatusCodes.Bad_CertificateInvalid, e);
      }
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
      return channel.isAsymmetricEncryptionEnabled();
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
      return channel.getSymmetricBlockSize();
    }

    @Override
    public int getSignatureSize(SecureChannel channel) {
      return channel.getSymmetricSignatureSize();
    }

    @Override
    public void verifyChunk(SecureChannel channel, ByteBuf chunkBuffer) throws UaException {
      SecurityAlgorithm securityAlgorithm =
          channel.getSecurityPolicy().getSymmetricSignatureAlgorithm();
      byte[] secretKey = channel.getDecryptionKeys(securityKeys).getSignatureKey();
      int signatureSize = channel.getSymmetricSignatureSize();

      ByteBuffer chunkNioBuffer = chunkBuffer.nioBuffer(0, chunkBuffer.writerIndex());
      ((Buffer) chunkNioBuffer).position(0);
      ((Buffer) chunkNioBuffer).limit(chunkBuffer.writerIndex() - signatureSize);

      byte[] signature = SignatureUtil.hmac(securityAlgorithm, secretKey, chunkNioBuffer);

      byte[] signatureBytes = new byte[signatureSize];
      ((Buffer) chunkNioBuffer).limit(chunkNioBuffer.position() + signatureSize);
      chunkNioBuffer.get(signatureBytes);

      if (!MessageDigest.isEqual(signature, signatureBytes)) {
        throw new UaException(StatusCodes.Bad_SecurityChecksFailed, "could not verify signature");
      }
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

        ChannelSecurity.SecretKeys decryptionKeys = channel.getDecryptionKeys(securityKeys);

        SecretKeySpec keySpec = new SecretKeySpec(decryptionKeys.getEncryptionKey(), "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(decryptionKeys.getInitializationVector());

        Cipher cipher = Cipher.getInstance(transformation);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        return cipher;
      } catch (GeneralSecurityException e) {
        throw new UaException(StatusCodes.Bad_InternalError, e);
      }
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
