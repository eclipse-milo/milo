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

import static java.util.Objects.requireNonNull;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaRuntimeException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.ChannelSecurityToken;
import org.eclipse.milo.opcua.stack.core.util.PShaUtil;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@NullMarked
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

  // Recognized enhanced policies expose their primitive strategy pieces, while the legacy
  // ChannelSecurity derivation entry point remains guarded because ephemeral policies need retained
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
                .sign(channel, emptySecurityKeys(), ByteBuffer.allocate(0)));
  }

  // Enhanced SecureChannel setup carries ephemeral public keys in ClientNonce/ServerNonce and both
  // sides must derive the same directional AEAD key material from those exact wire values.
  @ParameterizedTest
  @EnumSource(
      value = SecurityPolicy.class,
      names = {
        "ECC_nistP256_AesGcm",
        "ECC_nistP256_ChaChaPoly",
        "ECC_nistP384_AesGcm",
        "ECC_nistP384_ChaChaPoly",
        "ECC_brainpoolP256r1_AesGcm",
        "ECC_brainpoolP256r1_ChaChaPoly",
        "ECC_curve25519_AesGcm",
        "ECC_curve25519_ChaChaPoly",
        "ECC_curve448_AesGcm",
        "ECC_curve448_ChaChaPoly",
        "ECC_brainpoolP384r1_AesGcm",
        "ECC_brainpoolP384r1_ChaChaPoly",
        "RSA_DH_AesGcm",
        "RSA_DH_ChaChaPoly"
      })
  void ephemeralOpenSecureChannelDerivesMatchingAeadKeys(SecurityPolicy securityPolicy)
      throws Exception {

    SecurityPolicyProfile profile = securityPolicy.getProfile();
    ServerSecureChannel channel = new ServerSecureChannel();
    channel.setSecurityPolicy(securityPolicy);

    KeyPair clientEphemeralKeyPair = ChannelSecurity.generateEphemeralKeyPair(profile);
    KeyPair serverEphemeralKeyPair = ChannelSecurity.generateEphemeralKeyPair(profile);
    ByteString clientNonce =
        ChannelSecurity.encodeEphemeralPublicKey(profile, clientEphemeralKeyPair);
    ByteString serverNonce =
        ChannelSecurity.encodeEphemeralPublicKey(profile, serverEphemeralKeyPair);

    ChannelSecurity.SecurityKeys clientKeys =
        ChannelSecurity.generateKeyPair(
            channel, clientEphemeralKeyPair, clientNonce, serverNonce, serverNonce);
    ChannelSecurity.SecurityKeys serverKeys =
        ChannelSecurity.generateKeyPair(
            channel, serverEphemeralKeyPair, clientNonce, serverNonce, clientNonce);

    assertEquals(profile.secureChannelNonceLength(), clientNonce.length());
    assertEquals(profile.secureChannelNonceLength(), serverNonce.length());
    assertArrayEquals(
        clientKeys.getClientKeys().getEncryptionKey(),
        serverKeys.getClientKeys().getEncryptionKey());
    assertArrayEquals(
        clientKeys.getClientKeys().getInitializationVector(),
        serverKeys.getClientKeys().getInitializationVector());
    assertArrayEquals(
        clientKeys.getServerKeys().getEncryptionKey(),
        serverKeys.getServerKeys().getEncryptionKey());
    assertArrayEquals(
        clientKeys.getServerKeys().getInitializationVector(),
        serverKeys.getServerKeys().getInitializationVector());
  }

  // Part 6 renewal derives the next token from current IKM XOR fresh IKM. Deriving from the fresh
  // agreement alone works against itself but fails against a peer that applies the renewal rule.
  @ParameterizedTest
  @EnumSource(
      value = SecurityPolicy.class,
      names = {"ECC_nistP256_AesGcm", "RSA_DH_AesGcm"})
  void ephemeralOpenSecureChannelRenewalCombinesCurrentAndFreshInputKeyMaterial(
      SecurityPolicy securityPolicy) throws Exception {

    EphemeralExchange issue = ephemeralExchange(securityPolicy.getProfile());
    EphemeralExchange renew = ephemeralExchange(securityPolicy.getProfile());
    ChannelSecurity.SecurityKeys issuedKeys =
        deriveClientKeys(secureChannel(securityPolicy), issue);
    ChannelSecurity.SecurityKeys freshOnlyKeys =
        deriveClientKeys(secureChannel(securityPolicy), renew);
    ChannelSecurity.SecurityKeys clientRenewedKeys =
        deriveClientKeys(secureChannel(securityPolicy, issuedKeys), renew);
    ChannelSecurity.SecurityKeys serverRenewedKeys =
        deriveServerKeys(secureChannel(securityPolicy, issuedKeys), renew);

    assertArrayEquals(
        xor(inputKeyMaterial(issuedKeys), inputKeyMaterial(freshOnlyKeys)),
        inputKeyMaterial(clientRenewedKeys));
    assertArrayEquals(
        clientRenewedKeys.getClientKeys().getEncryptionKey(),
        serverRenewedKeys.getClientKeys().getEncryptionKey());
    assertArrayEquals(
        clientRenewedKeys.getServerKeys().getEncryptionKey(),
        serverRenewedKeys.getServerKeys().getEncryptionKey());
    assertFalse(
        Arrays.equals(
            freshOnlyKeys.getClientKeys().getEncryptionKey(),
            clientRenewedKeys.getClientKeys().getEncryptionKey()));
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

  // AEAD chunk security fails catastrophically if a token reuses a nonce, so the directional key
  // state must own IV construction and reuse rejection.
  @Test
  void aeadNonceStateBuildsProfileIvAndRejectsReuse() throws UaException {
    ChannelSecurity.SecretKeys keys =
        new ChannelSecurity.SecretKeys(new byte[0], new byte[16], nonce(12, 0x00).bytesOrEmpty());

    byte[] nonce = keys.reserveAeadNonce(0x01020304L, 0x05060708L);

    assertArrayEquals(
        new byte[] {0x04, 0x02, 0x00, 0x02, 0x0c, 0x02, 0x00, 0x02, 0x08, 0x09, 0x0a, 0x0b}, nonce);

    assertThrows(UaException.class, () -> keys.reserveAeadNonce(0x01020304L, 0x05060708L));

    assertFalse(
        ChannelSecurity.SecretKeys.isAeadRenewalRequired(
            UInteger.MAX_VALUE - ChannelSecurity.AEAD_RENEWAL_MARGIN - 1));
    assertTrue(
        ChannelSecurity.SecretKeys.isAeadRenewalRequired(
            UInteger.MAX_VALUE - ChannelSecurity.AEAD_RENEWAL_MARGIN));
    assertFalse(
        ChannelSecurity.SecretKeys.isAeadExhausted(
            UInteger.MAX_VALUE - ChannelSecurity.AEAD_RENEWAL_MARGIN));
    assertTrue(ChannelSecurity.SecretKeys.isAeadExhausted(UInteger.MAX_VALUE));
  }

  // Part 6 6.7.2.4 allows the non-legacy stream to reach UInt32.MaxValue, so reserving a nonce at
  // MAX must succeed; rejecting it would refuse the encoder's own final pre-wrap chunk and any
  // spec-conformant peer. createAeadNonce mirrors that on the decode side.
  @Test
  void aeadNonceStateAcceptsMaxSequenceNumber() throws UaException {
    ChannelSecurity.SecretKeys keys =
        new ChannelSecurity.SecretKeys(new byte[0], new byte[16], nonce(12, 0x00).bytesOrEmpty());

    assertDoesNotThrow(() -> keys.reserveAeadNonce(0x01020304L, UInteger.MAX_VALUE));
    assertDoesNotThrow(() -> keys.createAeadNonce(0x01020304L, UInteger.MAX_VALUE));
  }

  // A token installed before the renewal window started its nonce stream at a low
  // LastSequenceNumber, so it cannot follow the MAX->0 wrap without reusing those low values; the
  // encode side must reject that wrap and rely on renewal having installed a fresh token.
  @Test
  void aeadNonceStateRejectsWrapForTokenInstalledEarly() throws UaException {
    ChannelSecurity.SecretKeys keys =
        new ChannelSecurity.SecretKeys(new byte[0], new byte[16], nonce(12, 0x00).bytesOrEmpty());

    // First value this token used is 0, so post-wrap value 0 would reuse it.
    assertDoesNotThrow(() -> keys.reserveAeadNonce(0x01020304L, 0L));
    keys.reserveAeadNonce(0x01020304L, UInteger.MAX_VALUE);

    assertThrows(UaException.class, () -> keys.reserveAeadNonce(0x01020304L, 0L));
  }

  // A token installed inside the renewal window started its nonce stream at a high
  // LastSequenceNumber, so the low post-wrap values are fresh for that token's IV base, and the
  // MAX->0 wrap is safe. This is what makes the early-renewal feature able to extend channel life
  // across the wrap.
  @Test
  void aeadNonceStateAllowsSingleWrapForTokenInstalledInRenewalWindow() throws UaException {
    ChannelSecurity.SecretKeys keys =
        new ChannelSecurity.SecretKeys(new byte[0], new byte[16], nonce(12, 0x00).bytesOrEmpty());

    long firstValue = UInteger.MAX_VALUE - 1L;

    assertDoesNotThrow(() -> keys.reserveAeadNonce(0x01020304L, firstValue));
    assertDoesNotThrow(() -> keys.reserveAeadNonce(0x01020304L, UInteger.MAX_VALUE));

    // Post-wrap values below the token's first value are unique for this token.
    assertDoesNotThrow(() -> keys.reserveAeadNonce(0x01020304L, 0L));
    assertDoesNotThrow(() -> keys.reserveAeadNonce(0x01020304L, 1L));

    // Reaching the token's first value again would reuse a nonce, so it is rejected.
    assertThrows(UaException.class, () -> keys.reserveAeadNonce(0x01020304L, firstValue));
  }

  // The encode and decode sides must agree on the nonce for every chunk, including across the
  // MAX->0 wrap that Part 6 6.7.2.4 permits. A peer (decode side) that only reconstructs the
  // predicted nonce must accept exactly what the local encode side reserves, so a token that renews
  // before the wrap keeps producing verifiable chunks instead of either side rejecting the
  // boundary.
  @Test
  void aeadEncoderAndDecoderAgreeOnNonceAcrossMaxToZeroWrap() throws UaException {
    long tokenId = 0x01020304L;
    byte[] baseIv = nonce(12, 0x10).bytesOrEmpty();

    ChannelSecurity.SecretKeys encryptKeys =
        new ChannelSecurity.SecretKeys(new byte[0], new byte[16], baseIv);
    ChannelSecurity.SecretKeys decryptKeys =
        new ChannelSecurity.SecretKeys(new byte[0], new byte[16], baseIv);

    // A token installed inside the renewal window: first value MAX-2, then through the wrap.
    long[] stream = {UInteger.MAX_VALUE - 2L, UInteger.MAX_VALUE - 1L, UInteger.MAX_VALUE, 0L, 1L};

    for (long lastSequenceNumber : stream) {
      byte[] reserved = encryptKeys.reserveAeadNonce(tokenId, lastSequenceNumber);
      byte[] predicted = decryptKeys.createAeadNonce(tokenId, lastSequenceNumber);

      assertArrayEquals(
          reserved,
          predicted,
          "decode-side nonce must match encode-side nonce at lastSequenceNumber="
              + lastSequenceNumber);
    }
  }

  // The client transport uses this signal to start SecureChannel renewal while there is still an
  // AEAD counter safety window left for in-flight chunks.
  @Test
  void aeadRenewalSignalTripsBeforeExhaustion() throws Exception {
    ChunkEncoder encoder =
        new ChunkEncoder(new ChannelParameters(8192, 8192, 8192, 0, 8192, 8192, 8192, 0));
    ServerSecureChannel channel = new ServerSecureChannel();
    channel.setSecurityPolicy(SecurityPolicy.ECC_nistP256_AesGcm);

    setNonLegacyLastSequenceNumber(encoder, ChannelSecurity.AEAD_RENEWAL_SEQUENCE_NUMBER - 2L);
    assertFalse(encoder.isSecureChannelRenewalRequired(channel));

    setNonLegacyLastSequenceNumber(encoder, ChannelSecurity.AEAD_RENEWAL_SEQUENCE_NUMBER - 1L);
    assertTrue(encoder.isSecureChannelRenewalRequired(channel));
  }

  // The renewal signal must clear once a renewal installs a fresh token inside the renewal window.
  // Otherwise the never-reset stream counter keeps it latched true, and the client sends an OPN
  // Renew for every remaining chunk - a renew-per-RTT storm for the last ~1024 chunks before the
  // wrap.
  @Test
  void aeadRenewalSignalClearsAfterTokenInstalledInRenewalWindow() throws Exception {
    ChunkEncoder encoder =
        new ChunkEncoder(new ChannelParameters(8192, 8192, 8192, 0, 8192, 8192, 8192, 0));
    ServerSecureChannel channel = new ServerSecureChannel();
    channel.setSecurityPolicy(SecurityPolicy.ECC_nistP256_AesGcm);

    // Token installed at stream position 0 (before the window): the signal trips inside the window.
    setNonLegacyLastSequenceNumber(encoder, ChannelSecurity.AEAD_RENEWAL_SEQUENCE_NUMBER);
    assertTrue(encoder.isSecureChannelRenewalRequired(channel));

    // A renewal installs a fresh token whose first chunk is inside the window; the signal clears so
    // no further renewals are triggered while this token carries the stream across the wrap.
    setNonLegacyTokenInstallSequenceNumber(
        encoder, ChannelSecurity.AEAD_RENEWAL_SEQUENCE_NUMBER + 1L);
    assertFalse(encoder.isSecureChannelRenewalRequired(channel));

    // Advancing further toward the wrap does not re-trip it for this freshly installed token.
    setNonLegacyLastSequenceNumber(encoder, UInteger.MAX_VALUE - 1L);
    assertFalse(encoder.isSecureChannelRenewalRequired(channel));
  }

  // The non-legacy sequence stream is shared across the asymmetric encoder, the symmetric encoder,
  // and the renewal signal. Allocation is confined to a single ChunkEncoder monitor; without that
  // monitor two threads could read the same counter and emit a duplicate SequenceNumber (a fatal
  // UASC error on the peer, and AEAD nonce reuse) or skip one (an AEAD nonce gap). Two threads
  // allocating from one encoder must therefore observe every number in 0..(2*perThread - 1) exactly
  // once.
  @Test
  void nonLegacySequenceAllocationIsContiguousUnderConcurrency() throws Exception {
    int perThread = 100_000;

    ChunkEncoder encoder =
        new ChunkEncoder(new ChannelParameters(8192, 8192, 8192, 0, 8192, 8192, 8192, 0));

    Method nextNonLegacySequenceNumbers =
        ChunkEncoder.class.getDeclaredMethod("nextNonLegacySequenceNumbers");
    nextNonLegacySequenceNumbers.setAccessible(true);

    Method current = nextNonLegacySequenceNumbers.getReturnType().getDeclaredMethod("current");
    current.setAccessible(true);

    ConcurrentLinkedQueue<Long> currents = new ConcurrentLinkedQueue<>();
    AtomicReference<Throwable> failure = new AtomicReference<>();
    CountDownLatch start = new CountDownLatch(1);

    Runnable worker =
        () -> {
          try {
            start.await();

            for (int i = 0; i < perThread; i++) {
              Object sequenceNumbers = nextNonLegacySequenceNumbers.invoke(encoder);
              currents.add((Long) current.invoke(sequenceNumbers));
            }
          } catch (Throwable t) {
            failure.compareAndSet(null, t);
          }
        };

    Thread t1 = new Thread(worker, "allocate-1");
    Thread t2 = new Thread(worker, "allocate-2");
    t1.start();
    t2.start();
    start.countDown();
    t1.join();
    t2.join();

    if (failure.get() != null) {
      fail("concurrent sequence allocation failed", failure.get());
    }

    List<Long> sorted = new ArrayList<>(currents);
    sorted.sort(Long::compareTo);

    int total = 2 * perThread;
    assertEquals(total, sorted.size());

    List<Long> expected = new ArrayList<>(total);
    for (long n = 0; n < total; n++) {
      expected.add(n);
    }

    // Exact equality proves both uniqueness (no reused number) and contiguity (no skipped number).
    assertEquals(expected, sorted);
  }

  // SecureChannelEnhancements sign the first OpenSecureChannel response over its own bytes plus the
  // request signature; verifying only the response bytes recreates the interop failure.
  @ParameterizedTest
  @EnumSource(
      value = SecurityPolicy.class,
      names = {
        "ECC_nistP256_AesGcm",
        "ECC_nistP256_ChaChaPoly",
        "ECC_nistP384_AesGcm",
        "ECC_nistP384_ChaChaPoly",
        "ECC_brainpoolP256r1_AesGcm",
        "ECC_brainpoolP256r1_ChaChaPoly",
        "ECC_curve25519_AesGcm",
        "ECC_curve25519_ChaChaPoly",
        "ECC_curve448_AesGcm",
        "ECC_curve448_ChaChaPoly",
        "ECC_brainpoolP384r1_AesGcm",
        "ECC_brainpoolP384r1_ChaChaPoly",
        "RSA_DH_AesGcm",
        "RSA_DH_ChaChaPoly"
      })
  void asymmetricAuthenticationBindsAdditionalOpenSecureChannelBytes(SecurityPolicy securityPolicy)
      throws Exception {

    SecurityPolicyProfile profile = securityPolicy.getProfile();
    KeyPair keyPair = applicationKeyPair(securityPolicy);
    X509Certificate certificate = applicationCertificate(keyPair);

    ByteBuffer responseBytes = ByteBuffer.wrap(new byte[] {0x01, 0x02, 0x03});
    ByteBuffer requestSignature = ByteBuffer.wrap(new byte[] {0x04, 0x05});

    byte[] signature =
        SecureChannelStrategies.authentication(profile)
            .sign(profile, keyPair.getPrivate(), responseBytes, requestSignature);

    SecureChannelStrategies.authentication(profile)
        .verify(
            profile,
            certificate,
            signature,
            ByteBuffer.wrap(new byte[] {0x01, 0x02, 0x03}),
            ByteBuffer.wrap(new byte[] {0x04, 0x05}));

    assertThrows(
        UaException.class,
        () ->
            SecureChannelStrategies.authentication(profile)
                .verify(
                    profile,
                    certificate,
                    signature,
                    ByteBuffer.wrap(new byte[] {0x01, 0x02, 0x03})));
  }

  private static ByteString nonce(int length, int seed) {
    byte[] bytes = new byte[length];
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) (seed + i);
    }
    return ByteString.of(bytes);
  }

  private static byte[] expectedKey(ByteString secret, ByteString seed, int offset, int length) {
    return PShaUtil.createPSha256Key(secret.bytesOrEmpty(), seed.bytesOrEmpty(), offset, length);
  }

  private static ChannelSecurity.SecurityKeys emptySecurityKeys() {
    byte[] empty = new byte[0];

    return new ChannelSecurity.SecurityKeys(
        new ChannelSecurity.SecretKeys(empty, empty, empty),
        new ChannelSecurity.SecretKeys(empty, empty, empty));
  }

  private static ServerSecureChannel secureChannel(SecurityPolicy securityPolicy) {
    ServerSecureChannel channel = new ServerSecureChannel();
    channel.setSecurityPolicy(securityPolicy);

    return channel;
  }

  private static ServerSecureChannel secureChannel(
      SecurityPolicy securityPolicy, ChannelSecurity.SecurityKeys currentKeys) {

    ServerSecureChannel channel = secureChannel(securityPolicy);
    channel.setChannelSecurity(new ChannelSecurity(currentKeys, channelSecurityToken()));

    return channel;
  }

  private static ChannelSecurity.SecurityKeys deriveClientKeys(
      ServerSecureChannel channel, EphemeralExchange exchange) throws Exception {

    return ChannelSecurity.generateKeyPair(
        channel,
        exchange.clientKeyPair(),
        exchange.clientNonce(),
        exchange.serverNonce(),
        exchange.serverNonce());
  }

  private static ChannelSecurity.SecurityKeys deriveServerKeys(
      ServerSecureChannel channel, EphemeralExchange exchange) throws Exception {

    return ChannelSecurity.generateKeyPair(
        channel,
        exchange.serverKeyPair(),
        exchange.clientNonce(),
        exchange.serverNonce(),
        exchange.clientNonce());
  }

  private static EphemeralExchange ephemeralExchange(SecurityPolicyProfile profile)
      throws UaException {

    KeyPair clientEphemeralKeyPair = ChannelSecurity.generateEphemeralKeyPair(profile);
    KeyPair serverEphemeralKeyPair = ChannelSecurity.generateEphemeralKeyPair(profile);

    return new EphemeralExchange(
        clientEphemeralKeyPair,
        serverEphemeralKeyPair,
        ChannelSecurity.encodeEphemeralPublicKey(profile, clientEphemeralKeyPair),
        ChannelSecurity.encodeEphemeralPublicKey(profile, serverEphemeralKeyPair));
  }

  private static ChannelSecurityToken channelSecurityToken() {
    return new ChannelSecurityToken(uint(1), uint(1), DateTime.now(), uint(60_000));
  }

  private static byte[] inputKeyMaterial(ChannelSecurity.SecurityKeys securityKeys) {
    return requireNonNull(securityKeys.inputKeyMaterial());
  }

  private static byte[] xor(byte[] left, byte[] right) {
    byte[] result = new byte[left.length];

    for (int i = 0; i < result.length; i++) {
      result[i] = (byte) (left[i] ^ right[i]);
    }

    return result;
  }

  private static KeyPair applicationKeyPair(SecurityPolicy securityPolicy) throws Exception {
    return switch (securityPolicy.getProfile().authAxis()) {
      case ECDSA_NIST_P256_SHA256 -> SelfSignedCertificateGenerator.generateNistP256KeyPair();
      case ECDSA_NIST_P384_SHA384 -> SelfSignedCertificateGenerator.generateNistP384KeyPair();
      case ECDSA_BRAINPOOL_P256R1_SHA256 ->
          SelfSignedCertificateGenerator.generateBrainpoolP256r1KeyPair();
      case ECDSA_BRAINPOOL_P384R1_SHA384 ->
          SelfSignedCertificateGenerator.generateBrainpoolP384r1KeyPair();
      case ED25519 -> SelfSignedCertificateGenerator.generateEd25519KeyPair();
      case ED448 -> SelfSignedCertificateGenerator.generateEd448KeyPair();
      case RSA_PKCS1_SHA256 -> SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
      default -> throw new IllegalArgumentException("securityPolicy: " + securityPolicy);
    };
  }

  private static X509Certificate applicationCertificate(KeyPair keyPair) throws Exception {
    return new SelfSignedCertificateBuilder(keyPair)
        .setCommonName("SecureChannelStrategiesTest")
        .setOrganization("Eclipse Milo")
        .setApplicationUri("urn:eclipse:milo:test")
        .addDnsName("localhost")
        .build();
  }

  private static void setNonLegacyLastSequenceNumber(ChunkEncoder encoder, long lastSequenceNumber)
      throws Exception {

    // Jump close to UInt32.MaxValue without emitting billions of chunks; the public behavior under
    // test is the renewal signal, not sequence-number incrementation itself.
    Field field = ChunkEncoder.class.getDeclaredField("nonLegacyLastSequenceNumber");
    field.setAccessible(true);
    field.setLong(encoder, lastSequenceNumber);
  }

  private static void setNonLegacyTokenInstallSequenceNumber(
      ChunkEncoder encoder, long installSequenceNumber) throws Exception {

    // Simulate a renewal having installed a fresh token at the given stream position, which the
    // encoder normally records when it sees a new token id while encoding a chunk's security
    // header.
    Field field = ChunkEncoder.class.getDeclaredField("nonLegacyTokenInstallSequenceNumber");
    field.setAccessible(true);
    field.setLong(encoder, installSequenceNumber);
  }

  private record EphemeralExchange(
      KeyPair clientKeyPair,
      KeyPair serverKeyPair,
      ByteString clientNonce,
      ByteString serverNonce) {}
}
