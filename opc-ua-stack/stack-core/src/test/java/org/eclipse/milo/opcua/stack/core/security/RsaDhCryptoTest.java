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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigInteger;
import java.security.KeyPair;
import java.util.Arrays;
import javax.crypto.interfaces.DHPrivateKey;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.EccKeyAgreementUtil.DerivedKeyMaterial;
import org.eclipse.milo.opcua.stack.core.security.SecurityProviderResolver.ProviderProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.jspecify.annotations.NullMarked;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@NullMarked
class RsaDhCryptoTest {

  // RSA-DH nonce fields carry an RFC 7919 ffdhe3072 public value: exactly 384 bytes, unsigned
  // big-endian, zero-padded on the left. Both provider profiles must agree on the same secret.
  @ParameterizedTest
  @EnumSource(ProviderProfile.class)
  void ffdhe3072PublicValueCodecRoundTripsAndAgreementMatches(ProviderProfile providerProfile)
      throws Exception {

    KeyPair alice = FiniteFieldDhKeyAgreementUtil.generateFfdhe3072KeyPair(providerProfile);
    KeyPair bob = FiniteFieldDhKeyAgreementUtil.generateFfdhe3072KeyPair(providerProfile);
    ByteString aliceWire = FiniteFieldDhKeyAgreementUtil.encodeFfdhe3072(alice.getPublic());
    ByteString bobWire = FiniteFieldDhKeyAgreementUtil.encodeFfdhe3072(bob.getPublic());

    assertEquals(FiniteFieldDhKeyAgreementUtil.FFDHE_3072_PUBLIC_KEY_LENGTH, aliceWire.length());
    assertEquals(FiniteFieldDhKeyAgreementUtil.FFDHE_3072_PUBLIC_KEY_LENGTH, bobWire.length());
    assertEquals(
        aliceWire,
        FiniteFieldDhKeyAgreementUtil.encodeFfdhe3072(
            FiniteFieldDhKeyAgreementUtil.decodeFfdhe3072(aliceWire, providerProfile)));

    byte[] aliceSecret =
        FiniteFieldDhKeyAgreementUtil.agreeFfdhe3072(
            providerProfile,
            alice.getPrivate(),
            FiniteFieldDhKeyAgreementUtil.decodeFfdhe3072(bobWire, providerProfile));
    byte[] bobSecret =
        FiniteFieldDhKeyAgreementUtil.agreeFfdhe3072(
            providerProfile,
            bob.getPrivate(),
            FiniteFieldDhKeyAgreementUtil.decodeFfdhe3072(aliceWire, providerProfile));

    assertEquals(FiniteFieldDhKeyAgreementUtil.FFDHE_3072_PUBLIC_KEY_LENGTH, aliceSecret.length);
    assertArrayEquals(aliceSecret, bobSecret);
  }

  // Part 6 fixes the RSA-DH ffdhe3072 private exponent to 275 bits; provider defaults are not a
  // substitute because peers depend on the named profile's exact algorithm tuple.
  @ParameterizedTest
  @EnumSource(ProviderProfile.class)
  void ffdhe3072KeyGenerationUsesProfilePrivateExponentLength(ProviderProfile providerProfile)
      throws Exception {

    KeyPair keyPair = FiniteFieldDhKeyAgreementUtil.generateFfdhe3072KeyPair(providerProfile);
    DHPrivateKey privateKey = assertInstanceOf(DHPrivateKey.class, keyPair.getPrivate());

    assertEquals(
        FiniteFieldDhKeyAgreementUtil.FFDHE_3072_PRIVATE_KEY_LENGTH_BITS,
        privateKey.getParams().getL());
  }

  // Malformed RSA-DH public values must be rejected before JCA agreement is attempted.
  @ParameterizedTest
  @EnumSource(ProviderProfile.class)
  void ffdhe3072PublicValueCodecRejectsMalformedValues(ProviderProfile providerProfile) {
    assertThrows(
        UaException.class,
        () ->
            FiniteFieldDhKeyAgreementUtil.decodeFfdhe3072(
                ByteString.of(
                    new byte[FiniteFieldDhKeyAgreementUtil.FFDHE_3072_PUBLIC_KEY_LENGTH - 1]),
                providerProfile));

    byte[] zero = new byte[FiniteFieldDhKeyAgreementUtil.FFDHE_3072_PUBLIC_KEY_LENGTH];
    UaException zeroException =
        assertThrows(
            UaException.class,
            () ->
                FiniteFieldDhKeyAgreementUtil.decodeFfdhe3072(
                    ByteString.of(zero), providerProfile));
    assertEquals(StatusCodes.Bad_NonceInvalid, zeroException.getStatusCode().getValue());

    byte[] one = new byte[FiniteFieldDhKeyAgreementUtil.FFDHE_3072_PUBLIC_KEY_LENGTH];
    one[one.length - 1] = 1;
    assertThrows(
        UaException.class,
        () -> FiniteFieldDhKeyAgreementUtil.decodeFfdhe3072(ByteString.of(one), providerProfile));

    byte[] p = fixedWidth(FiniteFieldDhKeyAgreementUtil.ffdhe3072Prime());
    assertThrows(
        UaException.class,
        () -> FiniteFieldDhKeyAgreementUtil.decodeFfdhe3072(ByteString.of(p), providerProfile));
  }

  // RSA_DH_AesGcm uses the same Part 6 directional salt layout as the ECC AEAD policies, but with
  // 384-byte finite-field DH public values and HKDF-SHA256.
  @ParameterizedTest
  @EnumSource(ProviderProfile.class)
  void rsaDhAeadHkdfKeyMaterialUsesFfdhe3072NonceSizing(ProviderProfile providerProfile)
      throws Exception {

    int publicKeyLength = FiniteFieldDhKeyAgreementUtil.FFDHE_3072_PUBLIC_KEY_LENGTH;
    byte[] sharedSecret = ascending(publicKeyLength, 0x20);
    ByteString clientNonce = ByteString.of(ascending(publicKeyLength, 0x00));
    ByteString serverNonce = ByteString.of(ascending(publicKeyLength, 0x80));

    DerivedKeyMaterial aesMaterial =
        EccKeyAgreementUtil.deriveAeadKeyMaterial(
            providerProfile,
            SecurityPolicy.RSA_DH_AesGcm.getProfile(),
            sharedSecret,
            clientNonce,
            serverNonce);
    DerivedKeyMaterial chachaMaterial =
        EccKeyAgreementUtil.deriveAeadKeyMaterial(
            providerProfile,
            SecurityPolicy.RSA_DH_ChaChaPoly.getProfile(),
            sharedSecret,
            clientNonce,
            serverNonce);

    assertEquals(16, aesMaterial.clientEncryptionKey().length);
    assertEquals(12, aesMaterial.clientInitializationVector().length);
    assertEquals(16, aesMaterial.serverEncryptionKey().length);
    assertEquals(12, aesMaterial.serverInitializationVector().length);
    assertEquals(32, chachaMaterial.clientEncryptionKey().length);
    assertEquals(12, chachaMaterial.clientInitializationVector().length);
    assertEquals(
        2 + "opcua-client".length() + publicKeyLength + publicKeyLength,
        EccKeyAgreementUtil.hkdfSalt("opcua-client", 28, clientNonce, serverNonce).length);
  }

  private static byte[] ascending(int length, int start) {
    byte[] bytes = new byte[length];

    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = (byte) (start + i);
    }

    return bytes;
  }

  private static byte[] fixedWidth(BigInteger value) {
    byte[] bytes = value.toByteArray();
    if (bytes.length > 1 && bytes[0] == 0) {
      bytes = Arrays.copyOfRange(bytes, 1, bytes.length);
    }

    byte[] fixed = new byte[FiniteFieldDhKeyAgreementUtil.FFDHE_3072_PUBLIC_KEY_LENGTH];
    System.arraycopy(bytes, 0, fixed, fixed.length - bytes.length, bytes.length);

    return fixed;
  }
}
