/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.security;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.XECPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.NamedParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.XECPrivateKeySpec;
import java.util.HexFormat;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.EccKeyAgreementUtil.DerivedKeyMaterial;
import org.eclipse.milo.opcua.stack.core.security.SecurityProviderResolver.ProviderProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

class EccCryptoTest {

  // OPC UA peers expect fixed-width P1363 ECDSA signatures on the wire; DER output would be
  // locally valid crypto but fail interoperability.
  @ParameterizedTest
  @EnumSource(ProviderProfile.class)
  void ecdsaP256P1363SignsAndVerifies(ProviderProfile providerProfile) throws Exception {
    KeyPair keyPair = EccKeyAgreementUtil.generateNistP256KeyPair(providerProfile);
    byte[] message = hex("0102030405060708090a0b0c0d0e0f");

    byte[] signature =
        EccSignatureUtil.signEcdsaP256Sha256P1363(
            providerProfile, keyPair.getPrivate(), ByteBuffer.wrap(message));

    assertEquals(EccSignatureUtil.ECDSA_P256_SHA256_P1363_SIGNATURE_LENGTH, signature.length);
    assertDoesNotThrow(
        () ->
            EccSignatureUtil.verifyEcdsaP256Sha256P1363(
                providerProfile, keyPair.getPublic(), signature, ByteBuffer.wrap(message)));

    signature[0] ^= 0x01;

    UaException exception =
        assertThrows(
            UaException.class,
            () ->
                EccSignatureUtil.verifyEcdsaP256Sha256P1363(
                    providerProfile, keyPair.getPublic(), signature, ByteBuffer.wrap(message)));

    assertEquals(StatusCodes.Bad_SecurityChecksFailed, exception.getStatusCode().getValue());
  }

  // The Curve25519 profile authenticates OpenSecureChannel chunks with Ed25519 application
  // certificates, so provider selection must produce normal Ed25519 signatures and reject tamper.
  @ParameterizedTest
  @EnumSource(ProviderProfile.class)
  void ed25519SignsAndVerifies(ProviderProfile providerProfile) throws Exception {
    KeyPairGenerator generator =
        SecurityProviderSupport.withProviderProfile(
            providerProfile, "Ed25519", p -> KeyPairGenerator.getInstance("Ed25519", p));
    KeyPair keyPair = generator.generateKeyPair();
    byte[] message = hex("0a0b0c0d0e0f");

    byte[] signature =
        EccSignatureUtil.signEd25519(
            providerProfile, keyPair.getPrivate(), ByteBuffer.wrap(message));

    assertEquals(EccSignatureUtil.ED25519_SIGNATURE_LENGTH, signature.length);
    assertDoesNotThrow(
        () ->
            EccSignatureUtil.verifyEd25519(
                providerProfile, keyPair.getPublic(), signature, ByteBuffer.wrap(message)));

    signature[signature.length - 1] ^= 0x01;

    assertThrows(
        UaException.class,
        () ->
            EccSignatureUtil.verifyEd25519(
                providerProfile, keyPair.getPublic(), signature, ByteBuffer.wrap(message)));
  }

  // RFC 8032 fixes the byte-level signature result; this catches accidental pre-hashing or wrong
  // provider transformations that a generated round trip might miss.
  @Test
  void ed25519MatchesRfc8032EmptyMessageVector() throws Exception {
    PrivateKey privateKey =
        ed25519PrivateKey(
            hex("9d61b19deffd5a60ba844af492ec2cc4" + "4449c5697b326919703bac031cae7f60"));
    PublicKey publicKey =
        ed25519PublicKey(
            hex("d75a980182b10ab7d54bfed3c964073a" + "0ee172f3daa62325af021a68f707511a"));

    byte[] signature =
        EccSignatureUtil.signEd25519(ProviderProfile.JDK, privateKey, ByteBuffer.wrap(new byte[0]));

    assertArrayEquals(
        hex(
            "e5564300c360ac729086e2cc806e828a"
                + "84877f1eb8e5d974d873e06522490155"
                + "5fb8821590a33bacc61e39701cf9b46b"
                + "d25bf5f0595bbe24655141438e7a100b"),
        signature);
    assertDoesNotThrow(
        () ->
            EccSignatureUtil.verifyEd25519(
                ProviderProfile.JDK, publicKey, signature, ByteBuffer.wrap(new byte[0])));
  }

  // P-256 public keys are encoded as raw x||y coordinates in nonce fields, not as
  // SubjectPublicKeyInfo
  // or X9.62 points; the fixed base-point bytes guard the exact coordinate order.
  @ParameterizedTest
  @EnumSource(ProviderProfile.class)
  void nistP256PublicKeyCodecRoundTripsAndAgreementMatches(ProviderProfile providerProfile)
      throws Exception {
    ByteString basePoint =
        ByteString.of(
            hex(
                "6b17d1f2e12c4247f8bce6e563a440f277037d812deb33a0f4a13945d898c296"
                    + "4fe342e2fe1a7f9b8ee7eb4a7c0f9e162bce33576b315ececbb6406837bf51f5"));
    KeyPair alice = EccKeyAgreementUtil.generateNistP256KeyPair(providerProfile);
    KeyPair bob = EccKeyAgreementUtil.generateNistP256KeyPair(providerProfile);
    ByteString aliceWire = EccPublicKeyCodec.encodeNistP256(alice.getPublic());
    ByteString bobWire = EccPublicKeyCodec.encodeNistP256(bob.getPublic());

    assertEquals(
        basePoint,
        EccPublicKeyCodec.encodeNistP256(
            EccPublicKeyCodec.decodeNistP256(basePoint, providerProfile)));
    assertEquals(EccPublicKeyCodec.NIST_P256_PUBLIC_KEY_LENGTH, aliceWire.length());
    assertEquals(
        aliceWire,
        EccPublicKeyCodec.encodeNistP256(
            EccPublicKeyCodec.decodeNistP256(aliceWire, providerProfile)));

    byte[] aliceSecret =
        EccKeyAgreementUtil.agreeNistP256(
            providerProfile,
            alice.getPrivate(),
            EccPublicKeyCodec.decodeNistP256(bobWire, providerProfile));
    byte[] bobSecret =
        EccKeyAgreementUtil.agreeNistP256(
            providerProfile,
            bob.getPrivate(),
            EccPublicKeyCodec.decodeNistP256(aliceWire, providerProfile));

    assertEquals(32, aliceSecret.length);
    assertArrayEquals(aliceSecret, bobSecret);
  }

  // Malformed P-256 nonce bytes must be rejected before key installation so off-curve peer input
  // cannot reach SecureChannel key derivation.
  @Test
  void nistP256PublicKeyCodecRejectsMalformedWireValues() {
    assertThrows(
        UaException.class,
        () -> EccPublicKeyCodec.decodeNistP256(ByteString.of(new byte[63]), ProviderProfile.JDK));

    byte[] offCurve = new byte[EccPublicKeyCodec.NIST_P256_PUBLIC_KEY_LENGTH];
    offCurve[31] = 1;
    offCurve[63] = 1;

    UaException exception =
        assertThrows(
            UaException.class,
            () -> EccPublicKeyCodec.decodeNistP256(ByteString.of(offCurve), ProviderProfile.JDK));

    assertEquals(StatusCodes.Bad_NonceInvalid, exception.getStatusCode().getValue());
  }

  // RFC 7748 fixes the shared secret for this scalar/public-key pair; this protects little-endian
  // X25519 public-key handling.
  @Test
  void x25519MatchesRfc7748AgreementVector() throws Exception {
    PrivateKey alicePrivate =
        x25519PrivateKey(
            hex("77076d0a7318a57d3c16c17251b26645" + "df4c2f87ebc0992ab177fba51db92c2a"));
    PublicKey bobPublic =
        EccPublicKeyCodec.decodeX25519(
            ByteString.of(
                hex("de9edb7d7b7dc1b4d35b61c2ece43537" + "3f8343c85b78674dadfc7e146f882b4f")),
            ProviderProfile.JDK);

    byte[] sharedSecret =
        EccKeyAgreementUtil.agreeX25519(ProviderProfile.JDK, alicePrivate, bobPublic);

    assertArrayEquals(
        hex("4a5d9d5ba4ce2dE1728e3bf480350f25" + "e07e21c947d19e3376f09b3c1e161742"), sharedSecret);
  }

  // X25519 public keys in nonce fields are exactly 32 RFC 7748 bytes and must agree across the
  // selected provider profiles.
  @ParameterizedTest
  @EnumSource(ProviderProfile.class)
  void x25519PublicKeyCodecRoundTripsAndAgreementMatches(ProviderProfile providerProfile)
      throws Exception {
    KeyPair alice = EccKeyAgreementUtil.generateX25519KeyPair(providerProfile);
    KeyPair bob = EccKeyAgreementUtil.generateX25519KeyPair(providerProfile);
    ByteString aliceWire = EccPublicKeyCodec.encodeX25519(alice.getPublic());
    ByteString bobWire = EccPublicKeyCodec.encodeX25519(bob.getPublic());

    assertEquals(EccPublicKeyCodec.X25519_PUBLIC_KEY_LENGTH, aliceWire.length());
    assertEquals(
        aliceWire,
        EccPublicKeyCodec.encodeX25519(EccPublicKeyCodec.decodeX25519(aliceWire, providerProfile)));

    byte[] aliceSecret =
        EccKeyAgreementUtil.agreeX25519(
            providerProfile,
            alice.getPrivate(),
            EccPublicKeyCodec.decodeX25519(bobWire, providerProfile));
    byte[] bobSecret =
        EccKeyAgreementUtil.agreeX25519(
            providerProfile,
            bob.getPrivate(),
            EccPublicKeyCodec.decodeX25519(aliceWire, providerProfile));

    assertArrayEquals(aliceSecret, bobSecret);
  }

  // X25519 receive-side decoding ignores the final byte's top bit. Without this normalization a
  // peer's legal non-canonical public value can produce provider-specific agreement results.
  @ParameterizedTest
  @EnumSource(ProviderProfile.class)
  void x25519PublicKeyCodecMasksReceivedHighBit(ProviderProfile providerProfile) throws Exception {
    KeyPair local = EccKeyAgreementUtil.generateX25519KeyPair(providerProfile);
    KeyPair peer = EccKeyAgreementUtil.generateX25519KeyPair(providerProfile);
    byte[] normalizedWire = EccPublicKeyCodec.encodeX25519(peer.getPublic()).bytesOrEmpty();
    byte[] highBitWire = normalizedWire.clone();
    highBitWire[highBitWire.length - 1] |= (byte) 0x80;

    PublicKey normalizedPeer =
        EccPublicKeyCodec.decodeX25519(ByteString.of(normalizedWire), providerProfile);
    PublicKey highBitPeer =
        EccPublicKeyCodec.decodeX25519(ByteString.of(highBitWire), providerProfile);

    assertEquals(ByteString.of(normalizedWire), EccPublicKeyCodec.encodeX25519(highBitPeer));
    assertArrayEquals(
        EccKeyAgreementUtil.agreeX25519(providerProfile, local.getPrivate(), normalizedPeer),
        EccKeyAgreementUtil.agreeX25519(providerProfile, local.getPrivate(), highBitPeer));
  }

  // JDK XEC keys can represent X25519 or X448. The encoder must reject a non-X25519 key even if
  // its public value happens to fit into 32 bytes.
  @Test
  void x25519PublicKeyCodecRejectsOtherXecCurves() {
    assertThrows(UaException.class, () -> EccPublicKeyCodec.encodeX25519(new X448PublicKey()));
  }

  // Wrong-length X25519 nonce fields are malformed, and all-zero shared secrets indicate invalid
  // or small-subgroup peer input.
  @Test
  void x25519RejectsWrongLengthAndAllZeroSharedSecret() throws Exception {
    assertThrows(
        UaException.class,
        () -> EccPublicKeyCodec.decodeX25519(ByteString.of(new byte[31]), ProviderProfile.JDK));

    KeyPair keyPair = EccKeyAgreementUtil.generateX25519KeyPair(ProviderProfile.JDK);
    PublicKey zeroPublic =
        EccPublicKeyCodec.decodeX25519(
            ByteString.of(new byte[EccPublicKeyCodec.X25519_PUBLIC_KEY_LENGTH]),
            ProviderProfile.JDK);

    assertThrows(
        UaException.class,
        () ->
            EccKeyAgreementUtil.agreeX25519(ProviderProfile.JDK, keyPair.getPrivate(), zeroPublic));
  }

  // OPC UA uses direction-specific HKDF salts that include both ephemeral public keys; swapping the
  // order would give each side different client/server keys.
  @Test
  void eccAeadHkdfKeyMaterialUsesOpcUaSaltLayout() throws Exception {
    byte[] sharedSecret = ascending(32, 0x20);
    ByteString clientNonce = ByteString.of(ascending(64, 0x00));
    ByteString serverNonce = ByteString.of(ascending(64, 0x40));

    DerivedKeyMaterial keyMaterial =
        EccKeyAgreementUtil.deriveEccAeadKeyMaterial(
            ProviderProfile.JDK,
            SecurityPolicy.ECC_nistP256_AesGcm.getProfile(),
            sharedSecret,
            clientNonce,
            serverNonce);

    assertEquals(16, keyMaterial.clientEncryptionKey().length);
    assertEquals(12, keyMaterial.clientInitializationVector().length);
    assertEquals(16, keyMaterial.serverEncryptionKey().length);
    assertEquals(12, keyMaterial.serverInitializationVector().length);
    assertArrayEquals(
        hex(
            "1c006f706375612d636c69656e74"
                + "000102030405060708090a0b0c0d0e0f"
                + "101112131415161718191a1b1c1d1e1f"
                + "202122232425262728292a2b2c2d2e2f"
                + "303132333435363738393a3b3c3d3e3f"
                + "404142434445464748494a4b4c4d4e4f"
                + "505152535455565758595a5b5c5d5e5f"
                + "606162636465666768696a6b6c6d6e6f"
                + "707172737475767778797a7b7c7d7e7f"),
        EccKeyAgreementUtil.hkdfSalt("opcua-client", 28, clientNonce, serverNonce));
    assertArrayEquals(
        hex(
            "1c006f706375612d736572766572"
                + "404142434445464748494a4b4c4d4e4f"
                + "505152535455565758595a5b5c5d5e5f"
                + "606162636465666768696a6b6c6d6e6f"
                + "707172737475767778797a7b7c7d7e7f"
                + "000102030405060708090a0b0c0d0e0f"
                + "101112131415161718191a1b1c1d1e1f"
                + "202122232425262728292a2b2c2d2e2f"
                + "303132333435363738393a3b3c3d3e3f"),
        EccKeyAgreementUtil.hkdfSalt("opcua-server", 28, serverNonce, clientNonce));
    assertArrayEquals(hex("d345535a3951dcb8ca8ad736e6ea74e2"), keyMaterial.clientEncryptionKey());
    assertArrayEquals(hex("b1ac39abcc179b49ffcb60c4"), keyMaterial.clientInitializationVector());
    assertArrayEquals(hex("4eb0d1af3f8721226f74735f7c9fee07"), keyMaterial.serverEncryptionKey());
    assertArrayEquals(hex("19cc7fe176cc8cb966070c37"), keyMaterial.serverInitializationVector());
  }

  // RSA nonce profiles still use P_SHA; the ECC HKDF helper must not silently accept them.
  @Test
  void eccAeadHkdfKeyMaterialRejectsNonEccAgreementAxis() {
    assertThrows(
        UaException.class,
        () ->
            EccKeyAgreementUtil.deriveEccAeadKeyMaterial(
                ProviderProfile.JDK,
                SecurityPolicy.Basic256Sha256.getProfile(),
                new byte[32],
                ByteString.of(new byte[32]),
                ByteString.of(new byte[32])));
  }

  private static PrivateKey ed25519PrivateKey(byte[] seed) throws Exception {
    return KeyFactory.getInstance("Ed25519")
        .generatePrivate(
            new PKCS8EncodedKeySpec(concat(hex("302e020100300506032b657004220420"), seed)));
  }

  private static PublicKey ed25519PublicKey(byte[] publicKey) throws Exception {
    return KeyFactory.getInstance("Ed25519")
        .generatePublic(new X509EncodedKeySpec(concat(hex("302a300506032b6570032100"), publicKey)));
  }

  private static PrivateKey x25519PrivateKey(byte[] scalar) throws Exception {
    return KeyFactory.getInstance("X25519")
        .generatePrivate(new XECPrivateKeySpec(new NamedParameterSpec("X25519"), scalar));
  }

  private static final class X448PublicKey implements XECPublicKey {

    @Override
    public AlgorithmParameterSpec getParams() {
      return new NamedParameterSpec("X448");
    }

    @Override
    public BigInteger getU() {
      return BigInteger.ONE;
    }

    @Override
    public String getAlgorithm() {
      return "XDH";
    }

    @Override
    public String getFormat() {
      return "X.509";
    }

    @Override
    public byte[] getEncoded() {
      return new byte[0];
    }
  }

  private static byte[] ascending(int length, int start) {
    byte[] bytes = new byte[length];

    for (int i = 0; i < length; i++) {
      bytes[i] = (byte) (start + i);
    }

    return bytes;
  }

  private static byte[] concat(byte[] a, byte[] b) {
    byte[] c = new byte[a.length + b.length];

    System.arraycopy(a, 0, c, 0, a.length);
    System.arraycopy(b, 0, c, a.length, b.length);

    return c;
  }

  private static byte[] hex(String s) {
    return HexFormat.of().parseHex(s.replaceAll("\\s+", ""));
  }
}
