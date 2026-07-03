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

import java.util.Arrays;
import javax.security.auth.Destroyable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;

/**
 * The split key material for one security token: {@code SigningKey || EncryptingKey || KeyNonce}
 * per Part 14 v1.05 Table 155, held in private wipeable copies.
 *
 * <p>This is the sole wipeable owner of PubSub key bytes (see the package documentation for the
 * zeroization posture and its JVM limits):
 *
 * <ul>
 *   <li><b>Ownership:</b> the creator owns the instance and is responsible for calling {@link
 *       #destroy()} exactly when the key leaves use — the key lifecycle engine retires material
 *       when its token leaves the {prev, current, futures} retention window, on last detach, and on
 *       shutdown, and zeroizes it once retired material can no longer be borrowed.
 *   <li><b>Borrow semantics:</b> accessors return the <em>live</em> internal arrays, not copies;
 *       callers must not modify or retain a returned array beyond the current operation.
 *       Per-message defensive copies would defeat zeroization and cost hot-path allocations.
 *   <li><b>Destroy/borrow race:</b> the owner guarantees no {@link #destroy()} call races an active
 *       borrow (the key lifecycle engine defers zeroization of retired material until every
 *       in-flight encode/decode that could still hold it has drained).
 * </ul>
 */
public final class SecurityKeyMaterial implements Destroyable {

  private final PubSubSecurityPolicy policy;
  private final byte[] signingKey;
  private final byte[] encryptingKey;
  private final byte[] keyNonce;

  private volatile boolean destroyed = false;

  private SecurityKeyMaterial(
      PubSubSecurityPolicy policy, byte[] signingKey, byte[] encryptingKey, byte[] keyNonce) {

    this.policy = policy;
    this.signingKey = signingKey;
    this.encryptingKey = encryptingKey;
    this.keyNonce = keyNonce;
  }

  /**
   * Split one key data ByteString into its SigningKey, EncryptingKey, and KeyNonce parts (Part 14
   * Table 155), copying each into a private wipeable array.
   *
   * @param policy the {@link PubSubSecurityPolicy} that defines the part lengths.
   * @param keyData the key data; its length must be exactly {@link
   *     PubSubSecurityPolicy#getKeyDataLength()} (52 or 68 bytes).
   * @return a new {@link SecurityKeyMaterial}.
   * @throws UaException with {@code Bad_ConfigurationError} if {@code keyData} does not have
   *     exactly the key data length required by {@code policy}.
   */
  public static SecurityKeyMaterial split(PubSubSecurityPolicy policy, ByteString keyData)
      throws UaException {

    if (keyData.length() != policy.getKeyDataLength()) {
      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          "key data for %s must be %d bytes, got %d"
              .formatted(policy, policy.getKeyDataLength(), keyData.length()));
    }

    byte[] bytes = keyData.bytesOrEmpty();

    int signingKeyLength = policy.getSigningKeyLength();
    int encryptingKeyLength = policy.getEncryptingKeyLength();

    byte[] signingKey = Arrays.copyOfRange(bytes, 0, signingKeyLength);
    byte[] encryptingKey =
        Arrays.copyOfRange(bytes, signingKeyLength, signingKeyLength + encryptingKeyLength);
    byte[] keyNonce =
        Arrays.copyOfRange(bytes, signingKeyLength + encryptingKeyLength, bytes.length);

    return new SecurityKeyMaterial(policy, signingKey, encryptingKey, keyNonce);
  }

  /**
   * Get the {@link PubSubSecurityPolicy} this key material belongs to.
   *
   * @return the policy.
   */
  public PubSubSecurityPolicy getPolicy() {
    return policy;
  }

  /**
   * Get the SigningKey (32 bytes).
   *
   * <p>Returns the live internal array; callers must not modify or retain it beyond the current
   * operation.
   *
   * @return the signing key.
   * @throws IllegalStateException if this instance has been {@link #destroy() destroyed}.
   */
  public byte[] getSigningKey() {
    checkNotDestroyed();
    return signingKey;
  }

  /**
   * Get the EncryptingKey (16 or 32 bytes, per the policy).
   *
   * <p>Returns the live internal array; callers must not modify or retain it beyond the current
   * operation.
   *
   * @return the encrypting key.
   * @throws IllegalStateException if this instance has been {@link #destroy() destroyed}.
   */
  public byte[] getEncryptingKey() {
    checkNotDestroyed();
    return encryptingKey;
  }

  /**
   * Get the KeyNonce (4 bytes).
   *
   * <p>Returns the live internal array; callers must not modify or retain it beyond the current
   * operation.
   *
   * @return the key nonce.
   * @throws IllegalStateException if this instance has been {@link #destroy() destroyed}.
   */
  public byte[] getKeyNonce() {
    checkNotDestroyed();
    return keyNonce;
  }

  /**
   * Best-effort wipe: mark this instance destroyed, then zero all three key arrays; accessors throw
   * {@link IllegalStateException} afterward.
   *
   * <p>The destroyed flag is set BEFORE the arrays are wiped so that an accessor racing a destroy
   * that slipped past the owner's borrow guarantee fails fast with the exception instead of
   * silently handing a zeroed key to a cipher.
   *
   * <p>Idempotent. See the package documentation for why wiping is best-effort defense-in-depth,
   * not a guarantee.
   */
  @Override
  public void destroy() {
    destroyed = true;
    Arrays.fill(signingKey, (byte) 0);
    Arrays.fill(encryptingKey, (byte) 0);
    Arrays.fill(keyNonce, (byte) 0);
  }

  @Override
  public boolean isDestroyed() {
    return destroyed;
  }

  private void checkNotDestroyed() {
    if (destroyed) {
      throw new IllegalStateException("SecurityKeyMaterial has been destroyed");
    }
  }

  @Override
  public String toString() {
    return "SecurityKeyMaterial{policy=%s, destroyed=%s}".formatted(policy, destroyed);
  }
}
