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
import org.eclipse.milo.opcua.stack.core.security.SecurityAlgorithm;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.structured.ChannelSecurityToken;
import org.eclipse.milo.opcua.stack.core.util.PShaUtil;
import org.jspecify.annotations.Nullable;

public class ChannelSecurity {

  private final SecurityKeys currentKeys;
  private final ChannelSecurityToken currentToken;

  private final SecurityKeys previousKeys;
  private final ChannelSecurityToken previousToken;

  public ChannelSecurity(SecurityKeys currentSecurityKeys, ChannelSecurityToken currentToken) {
    this(currentSecurityKeys, currentToken, null, null);
  }

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

  public SecurityKeys getCurrentKeys() {
    return currentKeys;
  }

  public ChannelSecurityToken getCurrentToken() {
    return currentToken;
  }

  public Optional<SecurityKeys> getPreviousKeys() {
    return Optional.ofNullable(previousKeys);
  }

  public Optional<ChannelSecurityToken> getPreviousToken() {
    return Optional.ofNullable(previousToken);
  }

  public static SecurityKeys generateKeyPair(
      SecureChannel channel, ByteString clientNonce, ByteString serverNonce) {

    SecurityAlgorithm keyDerivation = channel.getSecurityPolicy().getKeyDerivationAlgorithm();

    int signatureKeySize = channel.getSymmetricSignatureKeySize();
    int encryptionKeySize = channel.getSymmetricEncryptionKeySize();
    int cipherTextBlockSize = channel.getSymmetricBlockSize();

    assert (clientNonce != null);
    assert (serverNonce != null);

    byte[] clientSignatureKey =
        (keyDerivation == SecurityAlgorithm.PSha1)
            ? PShaUtil.createPSha1Key(serverNonce.bytes(), clientNonce.bytes(), 0, signatureKeySize)
            : PShaUtil.createPSha256Key(
                serverNonce.bytes(), clientNonce.bytes(), 0, signatureKeySize);

    byte[] clientEncryptionKey =
        (keyDerivation == SecurityAlgorithm.PSha1)
            ? PShaUtil.createPSha1Key(
                serverNonce.bytes(), clientNonce.bytes(), signatureKeySize, encryptionKeySize)
            : PShaUtil.createPSha256Key(
                serverNonce.bytes(), clientNonce.bytes(), signatureKeySize, encryptionKeySize);

    byte[] clientInitializationVector =
        (keyDerivation == SecurityAlgorithm.PSha1)
            ? PShaUtil.createPSha1Key(
                serverNonce.bytes(),
                clientNonce.bytes(),
                signatureKeySize + encryptionKeySize,
                cipherTextBlockSize)
            : PShaUtil.createPSha256Key(
                serverNonce.bytes(),
                clientNonce.bytes(),
                signatureKeySize + encryptionKeySize,
                cipherTextBlockSize);

    byte[] serverSignatureKey =
        (keyDerivation == SecurityAlgorithm.PSha1)
            ? PShaUtil.createPSha1Key(clientNonce.bytes(), serverNonce.bytes(), 0, signatureKeySize)
            : PShaUtil.createPSha256Key(
                clientNonce.bytes(), serverNonce.bytes(), 0, signatureKeySize);

    byte[] serverEncryptionKey =
        (keyDerivation == SecurityAlgorithm.PSha1)
            ? PShaUtil.createPSha1Key(
                clientNonce.bytes(), serverNonce.bytes(), signatureKeySize, encryptionKeySize)
            : PShaUtil.createPSha256Key(
                clientNonce.bytes(), serverNonce.bytes(), signatureKeySize, encryptionKeySize);

    byte[] serverInitializationVector =
        (keyDerivation == SecurityAlgorithm.PSha1)
            ? PShaUtil.createPSha1Key(
                clientNonce.bytes(),
                serverNonce.bytes(),
                signatureKeySize + encryptionKeySize,
                cipherTextBlockSize)
            : PShaUtil.createPSha256Key(
                clientNonce.bytes(),
                serverNonce.bytes(),
                signatureKeySize + encryptionKeySize,
                cipherTextBlockSize);

    return new SecurityKeys(
        new SecretKeys(clientSignatureKey, clientEncryptionKey, clientInitializationVector),
        new SecretKeys(serverSignatureKey, serverEncryptionKey, serverInitializationVector));
  }

  public static class SecurityKeys {
    private final SecretKeys clientKeys;
    private final SecretKeys serverKeys;

    SecurityKeys(SecretKeys clientKeys, SecretKeys serverKeys) {
      this.clientKeys = clientKeys;
      this.serverKeys = serverKeys;
    }

    public SecretKeys getClientKeys() {
      return clientKeys;
    }

    public SecretKeys getServerKeys() {
      return serverKeys;
    }
  }

  public static class SecretKeys {
    private final byte[] signatureKey;
    private final byte[] encryptionKey;
    private final byte[] initializationVector;

    SecretKeys(byte[] signatureKey, byte[] encryptionKey, byte[] initializationVector) {
      this.signatureKey = signatureKey;
      this.encryptionKey = encryptionKey;
      this.initializationVector = initializationVector;
    }

    public byte[] getSignatureKey() {
      return signatureKey;
    }

    public byte[] getEncryptionKey() {
      return encryptionKey;
    }

    public byte[] getInitializationVector() {
      return initializationVector;
    }
  }
}
