/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.security;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

/**
 * A {@link SecurityKeyProvider} pinning a single static key that never rotates.
 *
 * <p>Intended for interop with peers using pre-shared static keys (S2OPC, OPC Labs
 * OpcCmd/UADemoPublisher {@code static:} URIs) and for tests. The returned {@link SecurityKeySet}
 * carries {@link Duration#ZERO} for both {@code timeToNextKey} and {@code keyLifetime} — the static
 * sentinel documented on {@link SecurityKeySet}: consumers schedule no rotation and apply no
 * expiry.
 *
 * <p>{@code getKeys} ignores the {@code securityGroupId} argument: the provider is bound
 * per-SecurityGroup via {@code PubSubBindings.securityKeys(...)}, so the group association is
 * external to the provider.
 */
public final class StaticSecurityKeyProvider implements SecurityKeyProvider {

  private static final String SIGNING_KEY_FILE = "signingKey.key";
  private static final String ENCRYPT_KEY_FILE = "encryptKey.key";
  private static final String KEY_NONCE_FILE = "keyNonce.key";

  private final SecurityKeySet keySet;

  private StaticSecurityKeyProvider(SecurityKeySet keySet) {
    this.keySet = keySet;
  }

  /**
   * Create a provider pinning {@code keyData} under token id 1.
   *
   * @param policy the {@link PubSubSecurityPolicy} the key data belongs to.
   * @param keyData the key data ({@code SigningKey || EncryptingKey || KeyNonce}, Part 14 Table
   *     155); must be exactly {@link PubSubSecurityPolicy#getKeyDataLength()} bytes.
   * @return a new {@link StaticSecurityKeyProvider}.
   * @throws UaException with {@code Bad_ConfigurationError} if {@code keyData} has the wrong
   *     length.
   */
  public static StaticSecurityKeyProvider of(PubSubSecurityPolicy policy, ByteString keyData)
      throws UaException {

    return of(policy, uint(1), keyData);
  }

  /**
   * Create a provider pinning {@code keyData} under the given token id.
   *
   * @param policy the {@link PubSubSecurityPolicy} the key data belongs to.
   * @param tokenId the SecurityTokenId the pinned key is published under.
   * @param keyData the key data ({@code SigningKey || EncryptingKey || KeyNonce}, Part 14 Table
   *     155); must be exactly {@link PubSubSecurityPolicy#getKeyDataLength()} bytes.
   * @return a new {@link StaticSecurityKeyProvider}.
   * @throws UaException with {@code Bad_ConfigurationError} if {@code keyData} has the wrong
   *     length.
   */
  public static StaticSecurityKeyProvider of(
      PubSubSecurityPolicy policy, UInteger tokenId, ByteString keyData) throws UaException {

    if (keyData.length() != policy.getKeyDataLength()) {
      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          "key data for %s must be %d bytes, got %d"
              .formatted(policy, policy.getKeyDataLength(), keyData.length()));
    }

    return new StaticSecurityKeyProvider(
        new SecurityKeySet(
            policy.getUri(), tokenId, List.of(keyData), Duration.ZERO, Duration.ZERO));
  }

  /**
   * Load a static key from a directory using the S2OPC/OPC Labs file convention: {@code
   * signingKey.key} (32 bytes), {@code encryptKey.key} (16 or 32 bytes), and {@code keyNonce.key}
   * (4 bytes), each containing raw key bytes.
   *
   * <p>The policy is inferred from the length of {@code encryptKey.key}: 16 bytes selects {@link
   * PubSubSecurityPolicy#PubSubAes128Ctr}, 32 bytes selects {@link
   * PubSubSecurityPolicy#PubSubAes256Ctr}.
   *
   * @param directory the directory containing the three key part files.
   * @return a new {@link StaticSecurityKeyProvider} pinning the loaded key under token id 1.
   * @throws UaException with {@code Bad_ConfigurationError}, naming the offending file, if a file
   *     is missing, unreadable, or has an unexpected length.
   */
  public static StaticSecurityKeyProvider loadS2OpcDirectory(Path directory) throws UaException {
    byte[] encryptKey = readKeyFile(directory.resolve(ENCRYPT_KEY_FILE));

    PubSubSecurityPolicy policy =
        switch (encryptKey.length) {
          case 16 -> PubSubSecurityPolicy.PubSubAes128Ctr;
          case 32 -> PubSubSecurityPolicy.PubSubAes256Ctr;
          default ->
              throw new UaException(
                  StatusCodes.Bad_ConfigurationError,
                  "%s: expected 16 or 32 bytes, got %d"
                      .formatted(directory.resolve(ENCRYPT_KEY_FILE), encryptKey.length));
        };

    byte[] signingKey =
        readKeyFile(directory.resolve(SIGNING_KEY_FILE), policy.getSigningKeyLength());
    byte[] keyNonce = readKeyFile(directory.resolve(KEY_NONCE_FILE), policy.getKeyNonceLength());

    byte[] keyData = new byte[signingKey.length + encryptKey.length + keyNonce.length];
    System.arraycopy(signingKey, 0, keyData, 0, signingKey.length);
    System.arraycopy(encryptKey, 0, keyData, signingKey.length, encryptKey.length);
    System.arraycopy(keyNonce, 0, keyData, signingKey.length + encryptKey.length, keyNonce.length);

    return of(policy, ByteString.of(keyData));
  }

  private static byte[] readKeyFile(Path file, int expectedLength) throws UaException {
    byte[] bytes = readKeyFile(file);
    if (bytes.length != expectedLength) {
      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          "%s: expected %d bytes, got %d".formatted(file, expectedLength, bytes.length));
    }
    return bytes;
  }

  private static byte[] readKeyFile(Path file) throws UaException {
    try {
      return Files.readAllBytes(file);
    } catch (IOException e) {
      throw new UaException(
          StatusCodes.Bad_ConfigurationError, "%s: could not read key file".formatted(file), e);
    }
  }

  @Override
  public CompletableFuture<SecurityKeySet> getKeys(
      String securityGroupId, UInteger startingTokenId, UInteger requestedKeyCount) {

    return CompletableFuture.completedFuture(keySet);
  }
}
