/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.channel.headers.AsymmetricSecurityHeader;
import org.eclipse.milo.opcua.stack.core.channel.headers.SecureMessageHeader;
import org.eclipse.milo.opcua.stack.core.channel.headers.SequenceHeader;
import org.eclipse.milo.opcua.stack.core.channel.headers.SymmetricSecurityHeader;
import org.eclipse.milo.opcua.stack.core.channel.messages.MessageType;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.SequenceNumberMode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.util.BufferUtil;
import org.eclipse.milo.opcua.stack.core.util.LongSequence;
import org.jspecify.annotations.Nullable;

/**
 * Encodes service payload bytes into OPC UA Secure Conversation chunks ready for transport.
 *
 * <p>The encoder has two entry points because OpenSecureChannel chunks and normal service chunks
 * carry different security headers and use different protection material. {@link
 * #encodeAsymmetric(SecureChannel, long, ByteBuf, MessageType)} is used while opening or renewing a
 * channel, when the message is protected with endpoint certificates. {@link
 * #encodeSymmetric(SecureChannel, long, ByteBuf, MessageType)} is used after a channel token has
 * installed symmetric keys.
 *
 * <p>Both paths follow the same UASC chunk shape: secure message header, security header, sequence
 * header, body bytes, optional padding, optional signature or authentication data, and optional
 * encryption of the protected portion. Profile-specific signing, padding, and cipher setup are
 * resolved from the channel's security profile so the framing code stays independent of individual
 * security-policy families.
 *
 * <p>For profiles using SecureChannel Enhancements, the sequence-number stream starts at 0 and does
 * not use the legacy wrap window. AEAD profiles also feed the previous sequence number, called
 * {@code LastSequenceNumber} by the specification, into per-chunk nonce construction. In {@code
 * SignAndEncrypt} that nonce protects the encrypted SequenceHeader and body; in {@code Sign} it
 * protects the tag-only signature footer over the plaintext chunk bytes.
 *
 * <p>An encoder instance owns the outbound sequence-number stream for the connection using it.
 * Reuse one instance for ordered messages on the same channel; use a separate instance for a
 * separate channel or connection.
 */
public final class ChunkEncoder {

  private final AsymmetricEncoder asymmetricEncoder = new AsymmetricEncoder();
  private final SymmetricEncoder symmetricEncoder = new SymmetricEncoder();

  // The SequenceNumber shall monotonically increase for all Messages and shall not wrap around
  // until it is greater than 4_294_966_271 (UInt32.MaxValue – 1024). The first number after the
  // wrap around shall be less than 1024.
  private final LongSequence legacySequenceNumber =
      new LongSequence(1L, UInteger.MAX_VALUE - 1024 + 1);

  // The non-legacy sequence stream and its AEAD-token tracking are one logically shared piece of
  // state spanning the asymmetric and symmetric encode paths plus the renewal signal. Every read
  // and mutation is confined to the outer ChunkEncoder monitor (this) so allocation, last-number
  // tracking, token-install tracking, and the renewal predicate never observe a torn stream. In
  // normal operation a single transport's event loop confines all calls to one thread; the monitor
  // additionally guarantees correctness for any caller that touches one encoder from more than one
  // thread.
  private long nonLegacyNextSequenceNumber = 0L;
  private long nonLegacyLastSequenceNumber = -1L;

  // The next sequence number at the moment the current AEAD token was installed. The renewal signal
  // is measured relative to this position so that, once a renewal installs a fresh token, the
  // signal clears instead of latching true and triggering an OPN Renew for every subsequent chunk.
  private long nonLegacyTokenInstallSequenceNumber = 0L;
  private long currentNonLegacyTokenId = -1L;

  private final ChannelParameters parameters;

  public ChunkEncoder(ChannelParameters parameters) {
    this.parameters = parameters;
  }

  /**
   * Encodes an OpenSecureChannel message using the channel's asymmetric security header and
   * certificate-based protection settings.
   */
  public EncodedMessage encodeAsymmetric(
      SecureChannel channel, long requestId, ByteBuf messageBuffer, MessageType messageType)
      throws MessageEncodeException {

    return encodeAsymmetric(channel, requestId, messageBuffer, messageType, null);
  }

  /**
   * Encodes an OpenSecureChannel message and, when supplied, appends extra bytes to the
   * application-signature input.
   *
   * <p>The extra bytes are not written into the chunk. They are only included in the signature
   * calculation used by SecureChannel-enhancement profiles, where the first OpenSecureChannel
   * response signs both the response chunk bytes and the first request signature.
   *
   * @param channel the SecureChannel state used for certificates, policy, and nonces.
   * @param requestId the UASC request id written into the SequenceHeader.
   * @param messageBuffer the encoded OpenSecureChannel service payload.
   * @param messageType the Secure Conversation message type.
   * @param additionalSignedBytes extra bytes to include after the chunk bytes in the signature
   *     input.
   * @return the encoded chunks and the last asymmetric signature, when one was produced.
   * @throws MessageEncodeException if chunk framing or protection fails.
   */
  public EncodedMessage encodeAsymmetric(
      SecureChannel channel,
      long requestId,
      ByteBuf messageBuffer,
      MessageType messageType,
      byte @Nullable [] additionalSignedBytes)
      throws MessageEncodeException {

    return encode(
        asymmetricEncoder, channel, requestId, messageBuffer, messageType, additionalSignedBytes);
  }

  /**
   * Encodes a service message using the channel's current symmetric security token.
   *
   * <p>The returned chunks carry the current token id from {@link ChannelSecurity} when channel
   * security has been installed.
   */
  public EncodedMessage encodeSymmetric(
      SecureChannel channel, long requestId, ByteBuf messageBuffer, MessageType messageType)
      throws MessageEncodeException {

    return encode(symmetricEncoder, channel, requestId, messageBuffer, messageType, null);
  }

  private EncodedMessage encode(
      AbstractEncoder encoder,
      SecureChannel channel,
      long requestId,
      ByteBuf messageBuffer,
      MessageType messageType,
      byte @Nullable [] additionalSignedBytes)
      throws MessageEncodeException {

    List<ByteBuf> chunks = new ArrayList<>();

    try {
      return encoder.encode(
          chunks, channel, requestId, messageBuffer, messageType, additionalSignedBytes);
    } catch (UaException e) {
      chunks.forEach(ReferenceCountUtil::safeRelease);

      throw new MessageEncodeException(e);
    }
  }

  /**
   * Returns whether the next AEAD chunk should be preceded by SecureChannel renewal.
   *
   * <p>Client transports use this before writing a symmetric request. It is only true for AEAD
   * SecureChannel profiles, where nonce construction cannot safely continue once the non-legacy
   * sequence stream approaches {@code UInt32.MaxValue}.
   *
   * <p>The signal is measured relative to the current token: it trips once the stream has advanced
   * to within the renewal margin of a full UInt32 cycle since the token was installed, and clears
   * as soon as a renewal installs a fresh token. Measuring distance-since-install rather than
   * absolute stream position keeps the signal from latching true after a renewal (which would
   * trigger an OPN Renew for every remaining chunk) and re-arms it for tokens installed inside the
   * renewal window, whose nonce stream runs out one full cycle after their own install position.
   *
   * @param channel the channel whose selected security profile determines whether AEAD renewal
   *     tracking applies.
   * @return true when the client should start token renewal before sending more chunks.
   */
  public boolean isSecureChannelRenewalRequired(SecureChannel channel) {
    return channel.getSecurityPolicyProfile().secureChannelEnhancements()
        && SecureChannelStrategies.chunkProtection(channel.getSecurityPolicyProfile()).isAead()
        && isNonLegacyRenewalWindowReachedForCurrentToken();
  }

  private synchronized boolean isNonLegacyRenewalWindowReachedForCurrentToken() {
    // The current token first used LastSequenceNumber = install position - 1, and its nonce inputs
    // repeat once the stream completes a full UInt32 cycle back to that value. Distance consumed
    // since install must therefore be measured modulo the wrap: an absolute-position comparison
    // would never re-trip for a token installed at or past the renewal window, leaving a long-lived
    // token to run into AEAD nonce-reuse rejection instead of renewing.
    long consumedByCurrentToken =
        (lastNonLegacySequenceNumberForRenewal() - nonLegacyTokenInstallSequenceNumber + 1L)
            & UInteger.MAX_VALUE;

    return consumedByCurrentToken >= ChannelSecurity.AEAD_RENEWAL_SEQUENCE_NUMBER;
  }

  private synchronized long lastNonLegacySequenceNumberForRenewal() {
    return nonLegacyLastSequenceNumber == -1L ? 0L : nonLegacyLastSequenceNumber;
  }

  /**
   * Records the AEAD token id carried by the chunk being encoded so the renewal signal can be
   * measured relative to the stream position where the current token was installed.
   *
   * <p>Called from the symmetric encoder while writing each chunk's security header. The first
   * observation of a new token id captures the next sequence number as the token's install
   * position.
   */
  private synchronized void noteNonLegacyToken(long tokenId) {
    if (tokenId != currentNonLegacyTokenId) {
      currentNonLegacyTokenId = tokenId;
      nonLegacyTokenInstallSequenceNumber = nonLegacyNextSequenceNumber;
    }
  }

  /**
   * Allocates the next non-legacy {@link SequenceNumbers} pair, advancing the shared stream.
   *
   * <p>{@code current} is the value written into the plaintext SequenceHeader; {@code last} is the
   * protocol {@code LastSequenceNumber} fed into AEAD nonce construction. The stream wraps from
   * {@code UInt32.MaxValue} back to 0 per Part 6 6.7.2.4, with no legacy wrap window.
   */
  private synchronized SequenceNumbers nextNonLegacySequenceNumbers() {
    long current = nonLegacyNextSequenceNumber;
    long last = nonLegacyLastSequenceNumber == -1L ? 0L : nonLegacyLastSequenceNumber;

    nonLegacyLastSequenceNumber = current;
    nonLegacyNextSequenceNumber = current == UInteger.MAX_VALUE ? 0L : current + 1L;

    return new SequenceNumbers(current, last);
  }

  /*
   * current is written into the plaintext SequenceHeader. last is the protocol
   * LastSequenceNumber value used while building an AEAD nonce before encryption.
   */
  private record SequenceNumbers(long current, long last) {}

  private abstract class AbstractEncoder {

    EncodedMessage encode(
        List<ByteBuf> chunks,
        SecureChannel channel,
        long requestId,
        ByteBuf messageBuffer,
        MessageType messageType,
        byte @Nullable [] additionalSignedBytes)
        throws UaException {

      boolean encrypted = isEncryptionEnabled(channel);
      boolean signed = isSigningEnabled(channel);

      int securityHeaderSize = getSecurityHeaderSize(channel);
      int cipherTextBlockSize = getCipherTextBlockSize(channel);
      int plainTextBlockSize = getPlainTextBlockSize(channel);
      int signatureSize = signed || encrypted ? getSignatureSize(channel) : 0;
      boolean aead = isAead(channel);
      boolean aeadEncryption = encrypted && aead;

      int maxChunkSize = parameters.getLocalSendBufferSize();
      int paddingOverhead = encrypted ? getPaddingOverhead(channel, cipherTextBlockSize) : 0;

      int maxCipherTextSize = maxChunkSize - SECURE_MESSAGE_HEADER_SIZE - securityHeaderSize;
      int maxCipherTextBlocks = maxCipherTextSize / cipherTextBlockSize;
      int maxPlainTextSize = maxCipherTextBlocks * plainTextBlockSize;
      int maxBodySize = maxPlainTextSize - SEQUENCE_HEADER_SIZE - paddingOverhead - signatureSize;

      assert (maxPlainTextSize + securityHeaderSize + SECURE_MESSAGE_HEADER_SIZE <= maxChunkSize);

      byte[] lastSignature = null;

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
        SequenceNumbers sequenceNumbers = nextSequenceNumbers(channel);
        SequenceHeader sequenceHeader = new SequenceHeader(sequenceNumbers.current(), requestId);

        SequenceHeader.encode(sequenceHeader, chunkBuffer);

        /* Message Body */
        chunkBuffer.writeBytes(messageBuffer, bodySize);

        /* Padding and Signature */
        if (encrypted) {
          writePadding(channel, cipherTextBlockSize, paddingSize, chunkBuffer);
        }

        if (signed && !aeadEncryption) {
          ByteBuffer chunkNioBuffer = chunkBuffer.nioBuffer(0, chunkBuffer.writerIndex());

          byte[] signature =
              signChunk(channel, sequenceNumbers, chunkNioBuffer, additionalSignedBytes);

          lastSignature = signature;

          chunkBuffer.writeBytes(signature);
        }

        /* Encryption */
        if (encrypted) {
          chunkBuffer.readerIndex(SECURE_MESSAGE_HEADER_SIZE + securityHeaderSize);

          assert (chunkBuffer.readableBytes() % plainTextBlockSize == 0);

          if (aeadEncryption) {
            encryptAeadChunk(channel, sequenceNumbers, chunkBuffer, signatureSize);
          } else {
            try {
              int blockCount = chunkBuffer.readableBytes() / plainTextBlockSize;

              ByteBuffer chunkNioBuffer =
                  chunkBuffer.nioBuffer(
                      chunkBuffer.readerIndex(), blockCount * cipherTextBlockSize);

              ByteBuf copyBuffer = chunkBuffer.copy();

              try {
                ByteBuffer plainTextNioBuffer = copyBuffer.nioBuffer();

                Cipher cipher = getCipher(channel, sequenceNumbers, chunkBuffer);

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
              } finally {
                copyBuffer.release();
              }
            } catch (GeneralSecurityException e) {
              throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
            }
          }
        }

        chunkBuffer.readerIndex(0).writerIndex(chunkSize);
      }

      return new EncodedMessage(chunks, requestId, lastSignature);
    }

    private void encryptAeadChunk(
        SecureChannel channel, SequenceNumbers sequenceNumbers, ByteBuf chunkBuffer, int tagSize)
        throws UaException {

      int plainTextStart = chunkBuffer.readerIndex();
      int plainTextSize = chunkBuffer.readableBytes();
      ByteBuf plainTextBuffer = chunkBuffer.copy(plainTextStart, plainTextSize);

      try {
        ByteBuffer plainTextNioBuffer = plainTextBuffer.nioBuffer();
        ByteBuffer chunkNioBuffer = chunkBuffer.nioBuffer(plainTextStart, plainTextSize + tagSize);

        Cipher cipher = getCipher(channel, sequenceNumbers, chunkBuffer);

        int bytesWritten = cipher.doFinal(plainTextNioBuffer, chunkNioBuffer);

        assert (bytesWritten == plainTextSize + tagSize);

        chunkBuffer.writerIndex(plainTextStart + bytesWritten);
      } catch (GeneralSecurityException e) {
        throw new UaException(StatusCodes.Bad_SecurityChecksFailed, e);
      } finally {
        plainTextBuffer.release();
      }
    }

    private SequenceNumbers nextSequenceNumbers(SecureChannel channel) {
      if (channel.getSecurityPolicyProfile().sequenceNumberMode() == SequenceNumberMode.LEGACY) {
        long sequenceNumber = legacySequenceNumber.getAndIncrement();

        return new SequenceNumbers(sequenceNumber, sequenceNumber);
      }

      return nextNonLegacySequenceNumbers();
    }

    protected abstract byte[] signChunk(
        SecureChannel channel,
        SequenceNumbers sequenceNumbers,
        ByteBuffer chunkNioBuffer,
        byte @Nullable [] additionalSignedBytes)
        throws UaException;

    protected abstract void encodeSecurityHeader(SecureChannel channel, ByteBuf buffer)
        throws UaException;

    protected abstract Cipher getCipher(
        SecureChannel channel, SequenceNumbers sequenceNumbers, ByteBuf chunkBuffer)
        throws UaException;

    protected abstract int getSecurityHeaderSize(SecureChannel channel) throws UaException;

    protected abstract int getCipherTextBlockSize(SecureChannel channel);

    protected abstract int getPlainTextBlockSize(SecureChannel channel);

    protected abstract int getSignatureSize(SecureChannel channel);

    protected abstract int getPaddingOverhead(SecureChannel channel, int cipherTextBlockSize);

    protected abstract void writePadding(
        SecureChannel channel, int cipherTextBlockSize, int paddingSize, ByteBuf buffer)
        throws UaException;

    protected abstract boolean isAsymmetric();

    protected abstract boolean isAead(SecureChannel channel);

    protected abstract boolean isEncryptionEnabled(SecureChannel channel);

    protected abstract boolean isSigningEnabled(SecureChannel channel);
  }

  /** A fully encoded message, in one or more chunks ready to send. */
  public static class EncodedMessage {

    private final List<ByteBuf> messageChunks;
    private final long requestId;
    private final byte @Nullable [] signature;

    public EncodedMessage(
        List<ByteBuf> messageChunks, long requestId, byte @Nullable [] signature) {
      this.messageChunks = messageChunks;
      this.requestId = requestId;
      this.signature = signature;
    }

    public List<ByteBuf> getMessageChunks() {
      return messageChunks;
    }

    public long getRequestId() {
      return requestId;
    }

    /**
     * Returns the last signature-like footer produced for this message.
     *
     * <p>OpenSecureChannel issue/response callers use this value as the SecureChannel-enhancement
     * binding input when an asymmetric application signature was produced. Symmetric signed chunks
     * may also produce footer bytes, including legacy HMAC signatures or AEAD Sign tags, but those
     * bytes are already written into the encoded chunks and are not channel-binding input.
     *
     * @return the last signature-like footer, or {@code null}.
     */
    public byte @Nullable [] getSignature() {
      return signature;
    }
  }

  private final class AsymmetricEncoder extends AbstractEncoder {

    @Override
    public byte[] signChunk(
        SecureChannel channel,
        SequenceNumbers sequenceNumbers,
        ByteBuffer chunkNioBuffer,
        byte @Nullable [] additionalSignedBytes)
        throws UaException {

      if (additionalSignedBytes != null && additionalSignedBytes.length > 0) {
        return SecureChannelStrategies.authentication(channel.getSecurityPolicyProfile())
            .sign(
                channel.getSecurityPolicyProfile(),
                channel.getKeyPair().getPrivate(),
                chunkNioBuffer,
                ByteBuffer.wrap(additionalSignedBytes));
      }

      return SecureChannelStrategies.authentication(channel.getSecurityPolicyProfile())
          .sign(
              channel.getSecurityPolicyProfile(),
              channel.getKeyPair().getPrivate(),
              chunkNioBuffer);
    }

    @Override
    public Cipher getCipher(
        SecureChannel channel, SequenceNumbers sequenceNumbers, ByteBuf chunkBuffer)
        throws UaException {
      Certificate remoteCertificate = channel.getRemoteCertificate();

      assert (remoteCertificate != null);

      return SecureChannelStrategies.asymmetricEncryption(channel.getSecurityPolicyProfile())
          .getEncryptionCipher(channel.getSecurityPolicyProfile(), remoteCertificate);
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
    protected int getPaddingOverhead(SecureChannel channel, int cipherTextBlockSize) {
      return SecureChannelStrategies.asymmetricEncryption(channel.getSecurityPolicyProfile())
          .paddingOverhead(cipherTextBlockSize);
    }

    @Override
    protected void writePadding(
        SecureChannel channel, int cipherTextBlockSize, int paddingSize, ByteBuf buffer) {

      SecureChannelStrategies.asymmetricEncryption(channel.getSecurityPolicyProfile())
          .writePadding(cipherTextBlockSize, paddingSize, buffer);
    }

    @Override
    protected boolean isAsymmetric() {
      return true;
    }

    @Override
    protected boolean isAead(SecureChannel channel) {
      return false;
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
    private volatile long currentTokenId = 0L;

    @Override
    public void encodeSecurityHeader(SecureChannel channel, ByteBuf buffer) throws UaException {
      ChannelSecurity channelSecurity = channel.getChannelSecurity();
      currentTokenId =
          channelSecurity != null ? channelSecurity.getCurrentToken().getTokenId().longValue() : 0L;

      SymmetricSecurityHeader.encode(new SymmetricSecurityHeader(currentTokenId), buffer);

      securityKeys = channelSecurity != null ? channelSecurity.getCurrentKeys() : null;

      if (chunkProtection(channel).isAead()) {
        // Track where each AEAD token was installed in the non-legacy stream so the renewal signal
        // un-latches after a renewal instead of firing for every remaining chunk.
        noteNonLegacyToken(currentTokenId);
      } else if (cipherId != currentTokenId && channel.isSymmetricEncryptionEnabled()) {
        cipher = initCipher(channel);
        cipherId = currentTokenId;
      }
    }

    @Override
    public byte[] signChunk(
        SecureChannel channel,
        SequenceNumbers sequenceNumbers,
        ByteBuffer chunkNioBuffer,
        byte @Nullable [] additionalSignedBytes)
        throws UaException {

      SecureChannelStrategies.ChunkProtectionStrategy chunkProtection = chunkProtection(channel);

      if (chunkProtection.isAead()) {
        return chunkProtection.sign(
            channel, securityKeys, currentTokenId, sequenceNumbers.last(), chunkNioBuffer);
      }

      return chunkProtection.sign(channel, securityKeys, chunkNioBuffer);
    }

    @Override
    public Cipher getCipher(
        SecureChannel channel, SequenceNumbers sequenceNumbers, ByteBuf chunkBuffer)
        throws UaException {

      SecureChannelStrategies.ChunkProtectionStrategy chunkProtection = chunkProtection(channel);

      if (chunkProtection.isAead()) {
        ByteBuffer aad =
            chunkBuffer.nioBuffer(
                0,
                SECURE_MESSAGE_HEADER_SIZE
                    + SymmetricSecurityHeader.SYMMETRIC_SECURITY_HEADER_SIZE);

        return chunkProtection.getEncryptionCipher(
            channel, securityKeys, currentTokenId, sequenceNumbers.last(), aad);
      }

      assert cipher != null;
      return cipher;
    }

    @Override
    public int getSecurityHeaderSize(SecureChannel channel) {
      return SymmetricSecurityHeader.SYMMETRIC_SECURITY_HEADER_SIZE;
    }

    @Override
    public int getCipherTextBlockSize(SecureChannel channel) {
      return channel.isSymmetricEncryptionEnabled()
          ? chunkProtection(channel).cipherTextBlockSize(channel.getSecurityPolicyProfile())
          : 1;
    }

    @Override
    public int getPlainTextBlockSize(SecureChannel channel) {
      return channel.isSymmetricEncryptionEnabled()
          ? chunkProtection(channel).plainTextBlockSize(channel.getSecurityPolicyProfile())
          : 1;
    }

    @Override
    public int getSignatureSize(SecureChannel channel) {
      return chunkProtection(channel).signatureSize(channel.getSecurityPolicyProfile());
    }

    @Override
    protected int getPaddingOverhead(SecureChannel channel, int cipherTextBlockSize) {
      return chunkProtection(channel).paddingOverhead(cipherTextBlockSize);
    }

    @Override
    protected void writePadding(
        SecureChannel channel, int cipherTextBlockSize, int paddingSize, ByteBuf buffer) {

      chunkProtection(channel).writePadding(cipherTextBlockSize, paddingSize, buffer);
    }

    @Override
    protected boolean isAsymmetric() {
      return false;
    }

    @Override
    protected boolean isAead(SecureChannel channel) {
      return chunkProtection(channel).isAead();
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
      return chunkProtection(channel).getEncryptionCipher(channel, securityKeys);
    }

    private SecureChannelStrategies.ChunkProtectionStrategy chunkProtection(SecureChannel channel) {
      return SecureChannelStrategies.chunkProtection(channel.getSecurityPolicyProfile());
    }
  }
}
