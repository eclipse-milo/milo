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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.nio.ByteBuffer;
import java.security.KeyPair;
import java.security.PublicKey;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.util.PShaUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class SecureChannelStrategiesTest {

  // RSA strategy resolution must agree with profile sizes or the symmetric codecs frame chunks
  // with keys, IVs, or signatures that no peer can verify.
  @ParameterizedTest
  @EnumSource(
      value = SecurityPolicy.class,
      names = {
        "Basic128Rsa15",
        "Basic256",
        "Basic256Sha256",
        "Aes128_Sha256_RsaOaep",
        "Aes256_Sha256_RsaPss"
      })
  void rsaProfilesDeriveLegacyKeyMaterialSizedByProfile(SecurityPolicy securityPolicy) {
    SecurityPolicyProfile profile = securityPolicy.getProfile();
    ByteString clientNonce = nonce(profile.secureChannelNonceLength(), 0x00);
    ByteString serverNonce = nonce(profile.secureChannelNonceLength(), 0x40);

    ChannelSecurity.SecurityKeys keys =
        SecureChannelStrategies.keyAgreement(profile)
            .deriveKeys(profile, clientNonce, serverNonce, profile.symmetricBlockSize());
    SecureChannelStrategies.ChunkProtectionStrategy chunks =
        SecureChannelStrategies.chunkProtection(profile);

    assertEquals(
        profile.symmetricSignatureKeySize(), keys.getClientKeys().getSignatureKey().length);
    assertEquals(
        profile.symmetricEncryptionKeySize(), keys.getClientKeys().getEncryptionKey().length);
    assertEquals(
        profile.symmetricBlockSize(), keys.getClientKeys().getInitializationVector().length);
    assertEquals(
        profile.symmetricSignatureKeySize(), keys.getServerKeys().getSignatureKey().length);
    assertEquals(
        profile.symmetricEncryptionKeySize(), keys.getServerKeys().getEncryptionKey().length);
    assertEquals(
        profile.symmetricBlockSize(), keys.getServerKeys().getInitializationVector().length);
    assertEquals(profile.symmetricBlockSize(), chunks.cipherTextBlockSize(profile));
    assertEquals(profile.symmetricBlockSize(), chunks.plainTextBlockSize(profile));
    assertEquals(profile.symmetricSignatureSize(), chunks.signatureSize(profile));
  }

  // Recognized ECC policies expose their primitive strategy pieces, while the legacy
  // ChannelSecurity derivation entry point remains guarded because ECC needs retained ephemeral
  // private-key state before keys can be installed.
  @Test
  void eccProfilesExposePrimitiveStrategiesButRemainPartiallyGuarded() throws UaException {
    SecurityPolicyProfile profile = SecurityPolicy.ECC_nistP256_AesGcm.getProfile();
    ServerSecureChannel channel = new ServerSecureChannel();
    channel.setSecurityPolicy(SecurityPolicy.ECC_nistP256_AesGcm);
    SecureChannelStrategies.KeyAgreementStrategy keyAgreement =
        SecureChannelStrategies.keyAgreement(profile);
    KeyPair local = keyAgreement.generateEphemeral(profile);
    KeyPair peer = keyAgreement.generateEphemeral(profile);
    ByteString peerWire = keyAgreement.encodePublicKey(profile, peer.getPublic());
    PublicKey decodedPeer = keyAgreement.decodePublicKey(profile, peerWire);

    assertEquals(profile.secureChannelNonceLength(), peerWire.length());
    assertEquals(32, keyAgreement.agree(profile, local.getPrivate(), decodedPeer).length);

    assertThrows(
        UaRuntimeException.class,
        () -> keyAgreement.deriveKeys(profile, ByteString.NULL_VALUE, ByteString.NULL_VALUE, 0));

    assertThrows(
        UaException.class,
        () ->
            SecureChannelStrategies.chunkProtection(profile)
                .sign(channel, null, ByteBuffer.allocate(0)));
  }

  // The client/server nonce order is part of the UASC wire contract for legacy RSA policies.
  @Test
  void rsaNoncePShaKeyDerivationPreservesLegacyLayout() {
    ServerSecureChannel channel = new ServerSecureChannel();
    channel.setSecurityPolicy(SecurityPolicy.Basic256Sha256);

    ByteString clientNonce =
        ByteString.of(
            new byte[] {
              0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07,
              0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f,
              0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17,
              0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f
            });
    ByteString serverNonce =
        ByteString.of(
            new byte[] {
              0x20, 0x21, 0x22, 0x23, 0x24, 0x25, 0x26, 0x27,
              0x28, 0x29, 0x2a, 0x2b, 0x2c, 0x2d, 0x2e, 0x2f,
              0x30, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37,
              0x38, 0x39, 0x3a, 0x3b, 0x3c, 0x3d, 0x3e, 0x3f
            });

    ChannelSecurity.SecurityKeys keys =
        ChannelSecurity.generateKeyPair(channel, clientNonce, serverNonce);

    assertArrayEquals(
        expectedKey(serverNonce, clientNonce, 0, 32), keys.getClientKeys().getSignatureKey());
    assertArrayEquals(
        expectedKey(serverNonce, clientNonce, 32, 32), keys.getClientKeys().getEncryptionKey());
    assertArrayEquals(
        expectedKey(serverNonce, clientNonce, 64, channel.getSymmetricBlockSize()),
        keys.getClientKeys().getInitializationVector());

    assertArrayEquals(
        expectedKey(clientNonce, serverNonce, 0, 32), keys.getServerKeys().getSignatureKey());
    assertArrayEquals(
        expectedKey(clientNonce, serverNonce, 32, 32), keys.getServerKeys().getEncryptionKey());
    assertArrayEquals(
        expectedKey(clientNonce, serverNonce, 64, channel.getSymmetricBlockSize()),
        keys.getServerKeys().getInitializationVector());
  }

  private static ByteString nonce(int length, int seed) {
    byte[] bytes = new byte[length];
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) (seed + i);
    }
    return ByteString.of(bytes);
  }

  private static byte[] expectedKey(ByteString secret, ByteString seed, int offset, int length) {
    return PShaUtil.createPSha256Key(secret.bytes(), seed.bytes(), offset, length);
  }
}
