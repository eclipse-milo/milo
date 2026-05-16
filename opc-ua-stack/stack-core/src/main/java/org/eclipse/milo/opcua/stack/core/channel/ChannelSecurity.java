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

import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
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

  /** Symmetric signing, encryption, and initialization-vector bytes for one sending direction. */
  public static class SecretKeys {
    private final byte[] signatureKey;
    private final byte[] encryptionKey;
    private final byte[] initializationVector;

    SecretKeys(byte[] signatureKey, byte[] encryptionKey, byte[] initializationVector) {
      this.signatureKey = signatureKey;
      this.encryptionKey = encryptionKey;
      this.initializationVector = initializationVector;
    }

    /** Returns the key used for chunk signatures or message authentication. */
    public byte[] getSignatureKey() {
      return signatureKey;
    }

    /** Returns the key used for chunk encryption. */
    public byte[] getEncryptionKey() {
      return encryptionKey;
    }

    /** Returns the initialization vector bytes used by chunk encryption. */
    public byte[] getInitializationVector() {
      return initializationVector;
    }
  }
}
