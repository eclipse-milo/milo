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

import java.util.Arrays;
import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ChannelSecurityToken;
import org.jspecify.annotations.Nullable;

/**
 * Token-specific symmetric security state installed on a {@link SecureChannel}.
 *
 * <p>Opening or renewing a SecureChannel produces a {@link ChannelSecurityToken} and, for secured
 * policies, a matching pair of directional key sets. The current token is used for outgoing chunks
 * and for most incoming chunks. During token renewal, the previous token can remain valid long
 * enough for in-flight chunks to arrive, so decoders may accept either the current or previous
 * token id.
 *
 * <p>Keys are stored by OPC UA direction: client keys protect chunks sent by the client, and server
 * keys protect chunks sent by the server. {@link SecureChannel#getEncryptionKeys(SecurityKeys)} and
 * {@link SecureChannel#getDecryptionKeys(SecurityKeys)} map those directional keys onto the local
 * endpoint's send and receive roles.
 */
public class ChannelSecurity {

  // OPC UA AEAD SecureChannel profiles use a 96-bit nonce. The first two UInt32 words are
  // combined with the token id and LastSequenceNumber; the final word remains the derived IV
  // suffix.
  static final int AEAD_INITIALIZATION_VECTOR_LENGTH = 12;
  static final long AEAD_RENEWAL_MARGIN = 1024L;
  static final long AEAD_RENEWAL_SEQUENCE_NUMBER = UInteger.MAX_VALUE - AEAD_RENEWAL_MARGIN;

  private final SecurityKeys currentKeys;
  private final ChannelSecurityToken currentToken;

  private final SecurityKeys previousKeys;
  private final ChannelSecurityToken previousToken;

  /**
   * Creates channel security state for a token with no previous token retained for decoding.
   *
   * @param currentSecurityKeys the symmetric keys associated with {@code currentToken}, or {@code
   *     null} when no keys are installed.
   * @param currentToken the token currently installed on the SecureChannel.
   */
  public ChannelSecurity(SecurityKeys currentSecurityKeys, ChannelSecurityToken currentToken) {
    this(currentSecurityKeys, currentToken, null, null);
  }

  /**
   * Creates channel security state for a token and, optionally, its predecessor.
   *
   * @param currentKeys the symmetric keys associated with {@code currentToken}, or {@code null}
   *     when no keys are installed.
   * @param currentToken the token currently installed on the SecureChannel.
   * @param previousKeys the symmetric keys for the prior token, or {@code null}.
   * @param previousToken the prior token accepted during renewal, or {@code null}.
   */
  public ChannelSecurity(
      SecurityKeys currentKeys,
      ChannelSecurityToken currentToken,
      @Nullable SecurityKeys previousKeys,
      @Nullable ChannelSecurityToken previousToken) {

    this.currentKeys = currentKeys;
    this.currentToken = currentToken;
    this.previousKeys = previousKeys;
    this.previousToken = previousToken;
  }

  /** Returns the symmetric keys associated with the current token, or {@code null}. */
  public SecurityKeys getCurrentKeys() {
    return currentKeys;
  }

  /** Returns the currently installed SecureChannel token. */
  public ChannelSecurityToken getCurrentToken() {
    return currentToken;
  }

  /** Returns the previous token's keys when the channel is retaining them during renewal. */
  public Optional<SecurityKeys> getPreviousKeys() {
    return Optional.ofNullable(previousKeys);
  }

  /** Returns the previous token when the channel is retaining it during renewal. */
  public Optional<ChannelSecurityToken> getPreviousToken() {
    return Optional.ofNullable(previousToken);
  }

  /**
   * Derives the directional symmetric keys for a newly issued SecureChannel token.
   *
   * <p>The channel supplies both the selected profile and the negotiated message security mode, so
   * the derived key material is sized exactly as the chunk codecs will consume it.
   *
   * @param channel the channel carrying the selected profile and symmetric sizing information.
   * @param clientNonce the client nonce from the OpenSecureChannel exchange.
   * @param serverNonce the server nonce from the OpenSecureChannel exchange.
   * @return directional client and server key material for the token.
   */
  public static SecurityKeys generateKeyPair(
      SecureChannel channel, ByteString clientNonce, ByteString serverNonce) {

    return SecureChannelStrategies.keyAgreement(channel.getSecurityPolicyProfile())
        .deriveKeys(
            channel.getSecurityPolicyProfile(),
            clientNonce,
            serverNonce,
            channel.getSymmetricBlockSize());
  }

  /**
   * Creates directional key state from AEAD key material derived during ECC/RSA-DH
   * OpenSecureChannel setup.
   *
   * <p>AEAD policies do not derive a symmetric signature key. The AEAD authentication tag is
   * produced by the encryption cipher and carried in the chunk signature footer.
   *
   * @param clientEncryptionKey the key used for chunks sent by the client.
   * @param clientInitializationVector the 12-byte IV base used for chunks sent by the client.
   * @param serverEncryptionKey the key used for chunks sent by the server.
   * @param serverInitializationVector the 12-byte IV base used for chunks sent by the server.
   * @return directional client and server key material for an AEAD token.
   */
  public static SecurityKeys createAeadKeyPair(
      byte[] clientEncryptionKey,
      byte[] clientInitializationVector,
      byte[] serverEncryptionKey,
      byte[] serverInitializationVector) {

    // SecretKeys keeps one shape for all symmetric policies. AEAD profiles do not use a separate
    // HMAC/signature key, so the signature-key slot is intentionally empty.
    byte[] emptySignatureKey = new byte[0];

    return new SecurityKeys(
        new SecretKeys(emptySignatureKey, clientEncryptionKey, clientInitializationVector),
        new SecretKeys(emptySignatureKey, serverEncryptionKey, serverInitializationVector));
  }

  /**
   * Directional symmetric keys derived for a SecureChannel token.
   *
   * <p>The names are protocol directions, not local endpoint roles. Clients send with client keys;
   * servers send with server keys.
   */
  public static class SecurityKeys {
    private final SecretKeys clientKeys;
    private final SecretKeys serverKeys;

    SecurityKeys(SecretKeys clientKeys, SecretKeys serverKeys) {
      this.clientKeys = clientKeys;
      this.serverKeys = serverKeys;
    }

    /** Returns the key material used to protect chunks sent by the client. */
    public SecretKeys getClientKeys() {
      return clientKeys;
    }

    /** Returns the key material used to protect chunks sent by the server. */
    public SecretKeys getServerKeys() {
      return serverKeys;
    }
  }

  /**
   * Symmetric key bytes for one protocol direction.
   *
   * <p>Legacy CBC/HMAC profiles use all three byte arrays directly: signature key, encryption key,
   * and IV. AEAD profiles use the encryption key plus the IV as a nonce base; the signature key is
   * empty because the authentication tag is produced by the AEAD cipher.
   */
  public static class SecretKeys {
    private final byte[] signatureKey;
    private final byte[] encryptionKey;
    private final byte[] initializationVector;
    private final AeadNonceState aeadNonceState = new AeadNonceState();

    SecretKeys(byte[] signatureKey, byte[] encryptionKey, byte[] initializationVector) {
      this.signatureKey = signatureKey.clone();
      this.encryptionKey = encryptionKey.clone();
      this.initializationVector = initializationVector.clone();
    }

    /** Returns the key used for legacy chunk signatures, or an empty key for AEAD profiles. */
    public byte[] getSignatureKey() {
      return signatureKey;
    }

    /** Returns the key used for chunk encryption. */
    public byte[] getEncryptionKey() {
      return encryptionKey;
    }

    /** Returns the IV bytes used directly by legacy ciphers or as the AEAD nonce base. */
    public byte[] getInitializationVector() {
      return initializationVector;
    }

    byte[] reserveAeadNonce(long tokenId, long lastSequenceNumber) throws UaException {
      return aeadNonceState.reserve(initializationVector, tokenId, lastSequenceNumber);
    }

    byte[] createAeadNonce(long tokenId, long lastSequenceNumber) throws UaException {
      return AeadNonceState.createNonce(initializationVector, tokenId, lastSequenceNumber);
    }

    static boolean isAeadRenewalRequired(long lastSequenceNumber) {
      return lastSequenceNumber >= AEAD_RENEWAL_SEQUENCE_NUMBER;
    }

    static boolean isAeadExhausted(long lastSequenceNumber) {
      return lastSequenceNumber == UInteger.MAX_VALUE;
    }
  }

  /**
   * Tracks nonce use for one directional AEAD key set.
   *
   * <p>OPC UA builds each AEAD nonce by XORing the derived 12-byte IV base with the token id and
   * {@code LastSequenceNumber}. The encryption side reserves nonces monotonically so a channel
   * cannot reuse a key/nonce pair; the decryption side only recreates the predicted nonce because
   * tag verification must succeed before the encrypted SequenceHeader can be trusted.
   */
  private static final class AeadNonceState {

    private long tokenId = -1L;
    private long highestLastSequenceNumber = -1L;

    synchronized byte[] reserve(byte[] baseIv, long tokenId, long lastSequenceNumber)
        throws UaException {

      byte[] nonce = createNonce(baseIv, tokenId, lastSequenceNumber);

      if (this.tokenId != tokenId) {
        this.tokenId = tokenId;
        highestLastSequenceNumber = -1L;
      }

      if (lastSequenceNumber <= highestLastSequenceNumber) {
        throw new UaException(
            StatusCodes.Bad_SecurityChecksFailed,
            "AEAD nonce reuse rejected for tokenId="
                + tokenId
                + ", lastSequenceNumber="
                + lastSequenceNumber);
      }

      highestLastSequenceNumber = lastSequenceNumber;

      return nonce;
    }

    static byte[] createNonce(byte[] baseIv, long tokenId, long lastSequenceNumber)
        throws UaException {

      validate(baseIv, tokenId, lastSequenceNumber);

      byte[] nonce = Arrays.copyOf(baseIv, baseIv.length);

      xorUInt32Le(nonce, 0, tokenId);
      xorUInt32Le(nonce, 4, lastSequenceNumber);

      return nonce;
    }

    private static void validate(byte[] baseIv, long tokenId, long lastSequenceNumber)
        throws UaException {

      if (baseIv.length != AEAD_INITIALIZATION_VECTOR_LENGTH) {
        throw new UaException(
            StatusCodes.Bad_SecurityChecksFailed,
            "AEAD initialization vector must be " + AEAD_INITIALIZATION_VECTOR_LENGTH + " bytes");
      }

      if (tokenId < 0 || tokenId > UInteger.MAX_VALUE) {
        throw new UaException(
            StatusCodes.Bad_SecurityChecksFailed, "invalid AEAD token id: " + tokenId);
      }

      if (lastSequenceNumber < 0 || lastSequenceNumber > UInteger.MAX_VALUE) {
        throw new UaException(
            StatusCodes.Bad_SecurityChecksFailed,
            "invalid AEAD LastSequenceNumber: " + lastSequenceNumber);
      }

      if (SecretKeys.isAeadExhausted(lastSequenceNumber)) {
        throw new UaException(
            StatusCodes.Bad_SecurityChecksFailed,
            "AEAD LastSequenceNumber would wrap before SecureChannel renewal: "
                + lastSequenceNumber);
      }
    }

    private static void xorUInt32Le(byte[] bytes, int offset, long value) {
      bytes[offset] ^= (byte) (value & 0xFF);
      bytes[offset + 1] ^= (byte) ((value >>> 8) & 0xFF);
      bytes[offset + 2] ^= (byte) ((value >>> 16) & 0xFF);
      bytes[offset + 3] ^= (byte) ((value >>> 24) & 0xFF);
    }
  }
}
