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

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.EccKeyAgreementUtil;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile;
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
   * Generates an ephemeral key pair for a profile whose OpenSecureChannel nonce fields carry public
   * key agreement values.
   *
   * @param profile the selected security-policy profile.
   * @return a fresh ephemeral key pair for the profile's key-agreement axis.
   * @throws UaException if the profile does not support ephemeral key agreement or generation
   *     fails.
   */
  public static KeyPair generateEphemeralKeyPair(SecurityPolicyProfile profile) throws UaException {

    return SecureChannelStrategies.keyAgreement(profile).generateEphemeral(profile);
  }

  /**
   * Encodes an ephemeral public key into the OpenSecureChannel nonce wire value for {@code
   * profile}.
   *
   * @param profile the selected security-policy profile.
   * @param ephemeralKeyPair the ephemeral key pair whose public key should be sent.
   * @return the encoded nonce/public-key value.
   * @throws UaException if the public key is incompatible with the profile.
   */
  public static ByteString encodeEphemeralPublicKey(
      SecurityPolicyProfile profile, KeyPair ephemeralKeyPair) throws UaException {

    return encodeEphemeralPublicKey(profile, ephemeralKeyPair.getPublic());
  }

  /**
   * Encodes an ephemeral public key into the OpenSecureChannel nonce wire value for {@code
   * profile}.
   *
   * @param profile the selected security-policy profile.
   * @param publicKey the ephemeral public key to encode.
   * @return the encoded nonce/public-key value.
   * @throws UaException if the public key is incompatible with the profile.
   */
  public static ByteString encodeEphemeralPublicKey(
      SecurityPolicyProfile profile, PublicKey publicKey) throws UaException {

    return SecureChannelStrategies.keyAgreement(profile).encodePublicKey(profile, publicKey);
  }

  /**
   * Derives directional symmetric keys from an ephemeral key-agreement OpenSecureChannel exchange.
   *
   * <p>{@code clientNonce} and {@code serverNonce} must be the exact wire values from the request
   * and response. {@code peerNonce} is decoded according to the profile, then combined with {@code
   * localEphemeralKeyPair} to produce the fresh key-agreement secret. Initial tokens derive from
   * that secret directly. Renewed tokens first XOR the fresh secret with the current token's input
   * key material, matching the SecureChannel renewal rule from OPC UA Part 6.
   *
   * @param channel the channel carrying the selected security-policy profile.
   * @param localEphemeralKeyPair the local ephemeral key pair retained for this exchange.
   * @param clientNonce the client nonce/public-key wire value.
   * @param serverNonce the server nonce/public-key wire value.
   * @param peerNonce the peer nonce/public-key wire value to decode and validate.
   * @return directional client and server key material for the token.
   * @throws UaException if peer key decoding, agreement, or HKDF derivation fails.
   */
  public static SecurityKeys generateKeyPair(
      SecureChannel channel,
      KeyPair localEphemeralKeyPair,
      ByteString clientNonce,
      ByteString serverNonce,
      ByteString peerNonce)
      throws UaException {

    SecurityPolicyProfile profile = channel.getSecurityPolicyProfile();
    SecureChannelStrategies.KeyAgreementStrategy keyAgreement =
        SecureChannelStrategies.keyAgreement(profile);

    PublicKey peerPublicKey = keyAgreement.decodePublicKey(profile, peerNonce);
    byte[] sharedSecret =
        keyAgreement.agree(profile, localEphemeralKeyPair.getPrivate(), peerPublicKey);

    byte[] inputKeyMaterial = inputKeyMaterial(channel, sharedSecret);

    try {
      EccKeyAgreementUtil.DerivedKeyMaterial keyMaterial =
          keyAgreement.deriveKeyMaterial(profile, inputKeyMaterial, clientNonce, serverNonce);

      return createAeadKeyPair(
          keyMaterial.clientEncryptionKey(),
          keyMaterial.clientInitializationVector(),
          keyMaterial.serverEncryptionKey(),
          keyMaterial.serverInitializationVector(),
          inputKeyMaterial);
    } finally {
      Arrays.fill(sharedSecret, (byte) 0);
      Arrays.fill(inputKeyMaterial, (byte) 0);
    }
  }

  private static byte[] inputKeyMaterial(SecureChannel channel, byte[] sharedSecret)
      throws UaException {

    ChannelSecurity currentSecurity = channel.getChannelSecurity();
    SecurityKeys currentKeys = currentSecurity != null ? currentSecurity.getCurrentKeys() : null;

    if (currentKeys == null) {
      return sharedSecret.clone();
    }

    byte[] currentInputKeyMaterial = currentKeys.inputKeyMaterial();

    if (currentInputKeyMaterial == null) {
      throw new UaException(
          StatusCodes.Bad_SecurityChecksFailed,
          "missing current input key material for SecureChannel renewal");
    }

    try {
      if (currentInputKeyMaterial.length != sharedSecret.length) {
        throw new UaException(
            StatusCodes.Bad_SecurityChecksFailed,
            "current input key material length does not match fresh shared secret length");
      }

      byte[] renewedInputKeyMaterial = new byte[sharedSecret.length];

      for (int i = 0; i < renewedInputKeyMaterial.length; i++) {
        renewedInputKeyMaterial[i] = (byte) (currentInputKeyMaterial[i] ^ sharedSecret[i]);
      }

      return renewedInputKeyMaterial;
    } finally {
      Arrays.fill(currentInputKeyMaterial, (byte) 0);
    }
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

    return createAeadKeyPair(
        clientEncryptionKey,
        clientInitializationVector,
        serverEncryptionKey,
        serverInitializationVector,
        null);
  }

  private static SecurityKeys createAeadKeyPair(
      byte[] clientEncryptionKey,
      byte[] clientInitializationVector,
      byte[] serverEncryptionKey,
      byte[] serverInitializationVector,
      byte @Nullable [] inputKeyMaterial) {

    // SecretKeys keeps one shape for all symmetric policies. AEAD profiles do not use a separate
    // HMAC/signature key, so the signature-key slot is intentionally empty.
    byte[] emptySignatureKey = new byte[0];

    return new SecurityKeys(
        new SecretKeys(emptySignatureKey, clientEncryptionKey, clientInitializationVector),
        new SecretKeys(emptySignatureKey, serverEncryptionKey, serverInitializationVector),
        inputKeyMaterial);
  }

  /**
   * Directional symmetric keys derived for a SecureChannel token.
   *
   * <p>The names are protocol directions, not local endpoint roles. Clients send with client keys;
   * servers send with server keys.
   *
   * <p>Enhanced AEAD SecureChannel renewals also need the input key material that produced the
   * current token's keys. That value is retained with the token keys, not exposed to chunk codecs,
   * so a Renew exchange can combine the current material with the fresh key-agreement secret before
   * deriving the next token.
   */
  public static class SecurityKeys {
    private final SecretKeys clientKeys;
    private final SecretKeys serverKeys;
    private final byte @Nullable [] inputKeyMaterial;

    SecurityKeys(SecretKeys clientKeys, SecretKeys serverKeys) {
      this(clientKeys, serverKeys, null);
    }

    SecurityKeys(SecretKeys clientKeys, SecretKeys serverKeys, byte @Nullable [] inputKeyMaterial) {

      this.clientKeys = clientKeys;
      this.serverKeys = serverKeys;
      this.inputKeyMaterial = inputKeyMaterial != null ? inputKeyMaterial.clone() : null;
    }

    /** Returns the key material used to protect chunks sent by the client. */
    public SecretKeys getClientKeys() {
      return clientKeys;
    }

    /** Returns the key material used to protect chunks sent by the server. */
    public SecretKeys getServerKeys() {
      return serverKeys;
    }

    /** Returns a defensive copy of the enhanced-token input key material, when retained. */
    byte @Nullable [] inputKeyMaterial() {
      return inputKeyMaterial != null ? inputKeyMaterial.clone() : null;
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
   *
   * <p>Part 6 6.7.2.4 lets the non-legacy {@code SequenceNumber} reach {@code UInt32.MaxValue} and
   * then wrap to a value below 1024, so a token may legitimately span that wrap. Because every
   * token derives a fresh IV base, nonce uniqueness only has to hold for the lifetime of a single
   * token. The encode side therefore enforces uniqueness per token: it accepts the strictly
   * increasing stream up to {@code UInt32.MaxValue} and allows a single wrap as long as the wrapped
   * values stay below the first {@code LastSequenceNumber} this token used. A token installed
   * before the renewal window (one whose first value is at or below the post-wrap values it would
   * reuse) is not allowed to wrap, which is the condition the early-renewal signal exists to avoid.
   * The decode side only reconstructs the predicted nonce, so it accepts any value the wire allows
   * and leaves uniqueness enforcement to the peer that produced the chunk.
   */
  private static final class AeadNonceState {

    private long tokenId = -1L;
    private long firstLastSequenceNumber = -1L;
    private long highestLastSequenceNumber = -1L;
    private boolean wrapped = false;

    synchronized byte[] reserve(byte[] baseIv, long tokenId, long lastSequenceNumber)
        throws UaException {

      byte[] nonce = createNonce(baseIv, tokenId, lastSequenceNumber);

      if (this.tokenId != tokenId) {
        this.tokenId = tokenId;
        firstLastSequenceNumber = lastSequenceNumber;
        highestLastSequenceNumber = -1L;
        wrapped = false;
      }

      if (!wrapped) {
        if (lastSequenceNumber > highestLastSequenceNumber) {
          // Normal monotonic advance within the pre-wrap segment.
          highestLastSequenceNumber = lastSequenceNumber;
          return nonce;
        }

        if (highestLastSequenceNumber == UInteger.MAX_VALUE
            && lastSequenceNumber < firstLastSequenceNumber) {
          // The stream wrapped past UInt32.MaxValue. This token was installed late enough that the
          // post-wrap values it is about to use were never used by this token before the wrap, so
          // the (tokenId, LastSequenceNumber) nonce input stays unique for this token's IV base.
          wrapped = true;
          highestLastSequenceNumber = lastSequenceNumber;
          return nonce;
        }
      } else if (lastSequenceNumber > highestLastSequenceNumber
          && lastSequenceNumber < firstLastSequenceNumber) {
        // Continue monotonically through the post-wrap segment, still below the first value used
        // before the wrap.
        highestLastSequenceNumber = lastSequenceNumber;
        return nonce;
      }

      throw new UaException(
          StatusCodes.Bad_SecurityChecksFailed,
          "AEAD nonce reuse rejected for tokenId="
              + tokenId
              + ", lastSequenceNumber="
              + lastSequenceNumber
              + " (firstLastSequenceNumber="
              + firstLastSequenceNumber
              + ", highestLastSequenceNumber="
              + highestLastSequenceNumber
              + ", wrapped="
              + wrapped
              + "); renew the SecureChannel before the sequence stream wraps");
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

      // UInt32.MaxValue is a valid LastSequenceNumber: Part 6 6.7.2.4 allows the non-legacy stream
      // to reach it and then wrap. Rejecting it here would refuse spec-conformant peers and the
      // local encode side's own final pre-wrap chunk. Per-token nonce uniqueness on the encode side
      // is enforced separately in reserve().
      if (lastSequenceNumber < 0 || lastSequenceNumber > UInteger.MAX_VALUE) {
        throw new UaException(
            StatusCodes.Bad_SecurityChecksFailed,
            "invalid AEAD LastSequenceNumber: " + lastSequenceNumber);
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
