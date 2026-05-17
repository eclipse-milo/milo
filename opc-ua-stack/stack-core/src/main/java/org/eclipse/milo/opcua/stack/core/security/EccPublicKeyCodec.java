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

import static java.util.Objects.requireNonNull;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.ECPublicKey;
import java.security.interfaces.XECPublicKey;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.EllipticCurve;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.NamedParameterSpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.spec.XECPublicKeySpec;
import java.util.Arrays;
import org.bouncycastle.crypto.params.X25519PublicKeyParameters;
import org.bouncycastle.crypto.util.SubjectPublicKeyInfoFactory;
import org.bouncycastle.jcajce.interfaces.XDHPublicKey;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityProviderResolver.ProviderProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.jspecify.annotations.NullMarked;

/**
 * Wire codecs for ECC ephemeral public keys carried in OpenSecureChannel nonce fields.
 *
 * <p>OPC UA ECC policies do not encode these public keys as X.509 {@code SubjectPublicKeyInfo}
 * values. NIST P-256 and Brainpool P-384r1 keys are sent as fixed-width big-endian {@code x || y}
 * coordinates with no leading X9.62 {@code 0x04} point marker: 64 bytes for P-256 and 96 bytes for
 * Brainpool P-384r1. X25519 keys are sent as the 32-byte RFC 7748 public value; when receiving
 * X25519 bytes, the final byte's most-significant bit is ignored as required by the curve
 * definition. The JCA XEC interfaces can also represent other curves such as X448, so encoding
 * checks both the key shape and curve identity. Decode methods validate the wire shape before
 * returning a provider-backed {@link PublicKey} suitable for key agreement.
 */
@NullMarked
public final class EccPublicKeyCodec {

  public static final int NIST_P256_PUBLIC_KEY_LENGTH = 64;
  public static final int BRAINPOOL_P384R1_PUBLIC_KEY_LENGTH = 96;
  public static final int X25519_PUBLIC_KEY_LENGTH = 32;

  private static final int NIST_P256_COORDINATE_LENGTH = 32;
  private static final int BRAINPOOL_P384R1_COORDINATE_LENGTH = 48;
  private static final String BRAINPOOL_P384R1_CURVE_NAME = "brainpoolP384r1";
  private static final ECParameterSpec NIST_P256 = nistP256ParameterSpec();
  private static final ECParameterSpec BRAINPOOL_P384R1 = brainpoolP384r1ParameterSpec();
  private static final NamedParameterSpec X25519 = new NamedParameterSpec("X25519");

  private EccPublicKeyCodec() {}

  /**
   * Encode a NIST P-256 public key as fixed-width {@code x || y} coordinates.
   *
   * <p>The returned bytes are the exact OpenSecureChannel wire value for profiles that use P-256
   * ECDH ephemeral keys.
   *
   * @param publicKey the P-256 public key.
   * @return the OPC UA wire encoding.
   * @throws UaException if the key is not a valid P-256 public key.
   */
  public static ByteString encodeNistP256(PublicKey publicKey) throws UaException {
    return encodeEc(
        publicKey,
        NIST_P256,
        NIST_P256_COORDINATE_LENGTH,
        NIST_P256_PUBLIC_KEY_LENGTH,
        "NIST P-256");
  }

  /**
   * Encode a Brainpool P-384r1 public key as fixed-width {@code x || y} coordinates.
   *
   * <p>The returned bytes are the exact OpenSecureChannel wire value for profiles that use
   * Brainpool P-384r1 ECDH ephemeral keys. Current Brainpool policies resolve to the Bouncy Castle
   * provider profile because the JDK EC providers do not consistently expose this curve.
   *
   * @param publicKey the Brainpool P-384r1 public key.
   * @return the OPC UA wire encoding.
   * @throws UaException if the key is not a valid Brainpool P-384r1 public key.
   */
  public static ByteString encodeBrainpoolP384r1(PublicKey publicKey) throws UaException {
    return encodeEc(
        publicKey,
        BRAINPOOL_P384R1,
        BRAINPOOL_P384R1_COORDINATE_LENGTH,
        BRAINPOOL_P384R1_PUBLIC_KEY_LENGTH,
        "Brainpool P-384r1");
  }

  /**
   * Decode fixed-width {@code x || y} coordinates into a NIST P-256 public key.
   *
   * <p>The point must be on the P-256 curve. The returned key is created through the requested
   * provider profile, so subsequent key-agreement operations use the same provider family.
   *
   * @param wire the OPC UA wire encoding.
   * @param providerProfile the provider profile used to create the returned key.
   * @return a P-256 public key.
   * @throws UaException if the bytes are the wrong length, off-curve, or unsupported.
   */
  public static PublicKey decodeNistP256(ByteString wire, ProviderProfile providerProfile)
      throws UaException {

    return decodeEc(
        wire,
        providerProfile,
        NIST_P256,
        NIST_P256_COORDINATE_LENGTH,
        NIST_P256_PUBLIC_KEY_LENGTH,
        "NIST P-256");
  }

  /**
   * Decode fixed-width {@code x || y} coordinates into a Brainpool P-384r1 public key.
   *
   * <p>The point must be on the Brainpool P-384r1 curve. The returned key is created through the
   * requested provider profile, so subsequent key-agreement operations use the same provider
   * family. Callers normally obtain that provider profile from {@link SecurityProviderResolver}
   * rather than selecting a JCA provider directly.
   *
   * @param wire the OPC UA wire encoding.
   * @param providerProfile the provider profile used to create the returned key.
   * @return a Brainpool P-384r1 public key.
   * @throws UaException if the bytes are the wrong length, off-curve, or unsupported.
   */
  public static PublicKey decodeBrainpoolP384r1(ByteString wire, ProviderProfile providerProfile)
      throws UaException {

    return decodeEc(
        wire,
        providerProfile,
        BRAINPOOL_P384R1,
        BRAINPOOL_P384R1_COORDINATE_LENGTH,
        BRAINPOOL_P384R1_PUBLIC_KEY_LENGTH,
        "Brainpool P-384r1");
  }

  /**
   * Encode an X25519 public key as its 32-byte RFC 7748 public value.
   *
   * <p>The JDK and Bouncy Castle expose X25519 public values through different key interfaces; this
   * method normalizes either representation to the protocol byte layout.
   *
   * @param publicKey the X25519 public key.
   * @return the OPC UA wire encoding.
   * @throws UaException if the key is not an X25519 public key.
   */
  public static ByteString encodeX25519(PublicKey publicKey) throws UaException {
    requireNonNull(publicKey, "publicKey");

    if (publicKey instanceof XDHPublicKey xdhPublicKey) {
      byte[] encoded = xdhPublicKey.getUEncoding();
      if (encoded.length == X25519_PUBLIC_KEY_LENGTH) {
        return ByteString.of(encoded);
      }
    }

    if (publicKey instanceof XECPublicKey xecPublicKey && isX25519(xecPublicKey)) {
      return ByteString.of(x25519LittleEndian(xecPublicKey.getU()));
    }

    throw new UaException(StatusCodes.Bad_SecurityChecksFailed, "expected X25519 public key");
  }

  /**
   * Decode a 32-byte RFC 7748 public value into an X25519 public key.
   *
   * <p>The returned key is suitable for X25519 agreement. The X25519 receive rule clears the
   * most-significant bit of the final byte before the provider key is created. Small-subgroup
   * rejection happens after agreement by checking for an all-zero shared secret.
   *
   * @param wire the OPC UA wire encoding.
   * @param providerProfile the provider profile used to create the returned key.
   * @return an X25519 public key.
   * @throws UaException if the bytes are the wrong length or unsupported.
   */
  public static PublicKey decodeX25519(ByteString wire, ProviderProfile providerProfile)
      throws UaException {

    requireNonNull(wire, "wire");
    requireNonNull(providerProfile, "providerProfile");

    byte[] wireBytes = wire.bytesOrEmpty();
    if (wireBytes.length != X25519_PUBLIC_KEY_LENGTH) {
      throw new UaException(
          StatusCodes.Bad_NonceInvalid,
          "X25519 public key must be " + X25519_PUBLIC_KEY_LENGTH + " bytes");
    }

    byte[] publicValue = normalizeX25519ReceivedPublicValue(wireBytes);

    try {
      KeyFactory keyFactory =
          SecurityProviderSupport.withProviderProfile(
              providerProfile, "X25519 KeyFactory", p -> KeyFactory.getInstance("X25519", p));

      return switch (providerProfile) {
        case BOUNCY_CASTLE ->
            keyFactory.generatePublic(new X509EncodedKeySpec(x25519Spki(publicValue)));
        case JDK ->
            keyFactory.generatePublic(
                new XECPublicKeySpec(X25519, littleEndianUnsigned(publicValue)));
      };
    } catch (InvalidKeySpecException | IOException e) {
      throw new UaException(StatusCodes.Bad_NonceInvalid, e);
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_ConfigurationError, e);
    }
  }

  private static ByteString encodeEc(
      PublicKey publicKey,
      ECParameterSpec spec,
      int coordinateLength,
      int publicKeyLength,
      String curveLabel)
      throws UaException {

    requireNonNull(publicKey, "publicKey");

    if (!(publicKey instanceof ECPublicKey ecPublicKey)) {
      throw new UaException(StatusCodes.Bad_SecurityChecksFailed, "expected EC public key");
    }

    ECPoint point = ecPublicKey.getW();

    validatePoint(point, spec, curveLabel);

    byte[] encoded = new byte[publicKeyLength];
    System.arraycopy(
        fixedWidthUnsigned(point.getAffineX(), coordinateLength), 0, encoded, 0, coordinateLength);
    System.arraycopy(
        fixedWidthUnsigned(point.getAffineY(), coordinateLength),
        0,
        encoded,
        coordinateLength,
        coordinateLength);

    return ByteString.of(encoded);
  }

  private static PublicKey decodeEc(
      ByteString wire,
      ProviderProfile providerProfile,
      ECParameterSpec spec,
      int coordinateLength,
      int publicKeyLength,
      String curveLabel)
      throws UaException {

    requireNonNull(wire, "wire");
    requireNonNull(providerProfile, "providerProfile");

    byte[] bytes = wire.bytesOrEmpty();
    if (bytes.length != publicKeyLength) {
      throw new UaException(
          StatusCodes.Bad_NonceInvalid,
          curveLabel + " public key must be " + publicKeyLength + " bytes");
    }

    BigInteger x = unsignedBigInteger(bytes, 0, coordinateLength);
    BigInteger y = unsignedBigInteger(bytes, coordinateLength, coordinateLength);
    ECPoint point = new ECPoint(x, y);

    validatePoint(point, spec, curveLabel);

    try {
      KeyFactory keyFactory =
          SecurityProviderSupport.withProviderProfile(
              providerProfile, "EC KeyFactory", p -> KeyFactory.getInstance("EC", p));

      return keyFactory.generatePublic(new ECPublicKeySpec(point, spec));
    } catch (InvalidKeySpecException e) {
      throw new UaException(StatusCodes.Bad_NonceInvalid, e);
    } catch (GeneralSecurityException e) {
      throw new UaException(StatusCodes.Bad_ConfigurationError, e);
    }
  }

  private static void validatePoint(ECPoint point, ECParameterSpec spec, String curveLabel)
      throws UaException {

    if (ECPoint.POINT_INFINITY.equals(point)) {
      throw new UaException(StatusCodes.Bad_NonceInvalid, curveLabel + " point at infinity");
    }

    if (!(spec.getCurve().getField() instanceof ECFieldFp field)) {
      throw new UaException(StatusCodes.Bad_ConfigurationError, curveLabel + " field is not prime");
    }

    BigInteger p = field.getP();
    BigInteger x = point.getAffineX();
    BigInteger y = point.getAffineY();

    if (x.signum() < 0 || y.signum() < 0 || x.compareTo(p) >= 0 || y.compareTo(p) >= 0) {
      throw new UaException(StatusCodes.Bad_NonceInvalid, curveLabel + " point outside field");
    }

    BigInteger left = y.modPow(BigInteger.TWO, p);
    BigInteger right =
        x.modPow(BigInteger.valueOf(3), p)
            .add(spec.getCurve().getA().multiply(x))
            .add(spec.getCurve().getB())
            .mod(p);

    if (!left.equals(right)) {
      throw new UaException(StatusCodes.Bad_NonceInvalid, curveLabel + " point is off curve");
    }
  }

  private static byte[] fixedWidthUnsigned(BigInteger value, int width) throws UaException {
    byte[] bytes = value.toByteArray();

    if (bytes.length > 1 && bytes[0] == 0) {
      bytes = Arrays.copyOfRange(bytes, 1, bytes.length);
    }

    if (bytes.length > width) {
      throw new UaException(StatusCodes.Bad_NonceInvalid, "coordinate does not fit wire width");
    }

    byte[] fixed = new byte[width];

    System.arraycopy(bytes, 0, fixed, width - bytes.length, bytes.length);

    return fixed;
  }

  private static BigInteger unsignedBigInteger(byte[] bytes, int offset, int length) {
    return new BigInteger(1, Arrays.copyOfRange(bytes, offset, offset + length));
  }

  private static BigInteger littleEndianUnsigned(byte[] bytes) {
    byte[] reversed = bytes.clone();

    reverse(reversed);

    return new BigInteger(1, reversed);
  }

  private static byte[] x25519Spki(byte[] bytes) throws IOException {
    return SubjectPublicKeyInfoFactory.createSubjectPublicKeyInfo(
            new X25519PublicKeyParameters(bytes))
        .getEncoded();
  }

  private static byte[] normalizeX25519ReceivedPublicValue(byte[] bytes) {
    byte[] publicValue = bytes.clone();

    publicValue[X25519_PUBLIC_KEY_LENGTH - 1] =
        (byte) (publicValue[X25519_PUBLIC_KEY_LENGTH - 1] & 0x7F);

    return publicValue;
  }

  private static boolean isX25519(XECPublicKey publicKey) {
    return publicKey.getParams() instanceof NamedParameterSpec params
        && "X25519".equalsIgnoreCase(params.getName());
  }

  private static ECParameterSpec nistP256ParameterSpec() {
    BigInteger p =
        new BigInteger("FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF", 16);
    BigInteger a =
        new BigInteger("FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC", 16);
    BigInteger b =
        new BigInteger("5AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B", 16);
    BigInteger gx =
        new BigInteger("6B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C296", 16);
    BigInteger gy =
        new BigInteger("4FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5", 16);
    BigInteger n =
        new BigInteger("FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC632551", 16);

    return new ECParameterSpec(
        new EllipticCurve(new ECFieldFp(p), a, b), new ECPoint(gx, gy), n, 1);
  }

  private static ECParameterSpec brainpoolP384r1ParameterSpec() {
    ECNamedCurveParameterSpec namedSpec =
        ECNamedCurveTable.getParameterSpec(BRAINPOOL_P384R1_CURVE_NAME);

    if (namedSpec == null) {
      throw new ExceptionInInitializerError("unknown EC curve: " + BRAINPOOL_P384R1_CURVE_NAME);
    }

    org.bouncycastle.math.ec.ECPoint generator = namedSpec.getG().normalize();
    BigInteger p = namedSpec.getCurve().getField().getCharacteristic();
    BigInteger a = namedSpec.getCurve().getA().toBigInteger();
    BigInteger b = namedSpec.getCurve().getB().toBigInteger();
    BigInteger gx = generator.getAffineXCoord().toBigInteger();
    BigInteger gy = generator.getAffineYCoord().toBigInteger();

    return new ECParameterSpec(
        new EllipticCurve(new ECFieldFp(p), a, b),
        new ECPoint(gx, gy),
        namedSpec.getN(),
        namedSpec.getH().intValueExact());
  }

  private static byte[] x25519LittleEndian(BigInteger value) throws UaException {
    if (value.signum() < 0) {
      throw new UaException(StatusCodes.Bad_NonceInvalid, "negative X25519 public value");
    }

    byte[] bigEndian = value.toByteArray();
    if (bigEndian.length > 1 && bigEndian[0] == 0) {
      bigEndian = Arrays.copyOfRange(bigEndian, 1, bigEndian.length);
    }

    if (bigEndian.length > X25519_PUBLIC_KEY_LENGTH) {
      throw new UaException(StatusCodes.Bad_NonceInvalid, "X25519 public value is too large");
    }

    byte[] littleEndian = new byte[X25519_PUBLIC_KEY_LENGTH];
    for (int i = 0; i < bigEndian.length; i++) {
      littleEndian[i] = bigEndian[bigEndian.length - 1 - i];
    }

    return littleEndian;
  }

  private static void reverse(byte[] bytes) {
    for (int left = 0, right = bytes.length - 1; left < right; left++, right--) {
      byte temp = bytes[left];
      bytes[left] = bytes[right];
      bytes[right] = temp;
    }
  }
}
