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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.Provider;
import java.security.Security;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.security.spec.MGF1ParameterSpec;
import java.security.spec.PSSParameterSpec;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import javax.crypto.Cipher;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile;
import org.eclipse.milo.opcua.stack.core.util.CipherFactory;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateBuilder;
import org.eclipse.milo.opcua.stack.core.util.SelfSignedCertificateGenerator;
import org.eclipse.milo.opcua.stack.core.util.SignatureUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.parallel.Isolated;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@Isolated("Changes the JVM security provider order")
class RsaCryptoInteropTest {

  private static final Provider BC = new BouncyCastleProvider();
  private static final byte[] MESSAGE =
      "OPC UA RSA interoperability".getBytes(StandardCharsets.UTF_8);
  private static final List<SecurityPolicy> RSA_POLICIES =
      List.of(
          SecurityPolicy.Basic128Rsa15,
          SecurityPolicy.Basic256,
          SecurityPolicy.Basic256Sha256,
          SecurityPolicy.Aes128_Sha256_RsaOaep,
          SecurityPolicy.Aes256_Sha256_RsaPss);
  private static KeyPair keyPair;
  private static X509Certificate certificate;
  private Provider previousBc;

  @BeforeAll
  static void createCertificate() throws Exception {
    keyPair = SelfSignedCertificateGenerator.generateRsaKeyPair(2048);
    certificate = new SelfSignedCertificateBuilder(keyPair).build();
  }

  @BeforeEach
  void saveProviders() {
    previousBc = Security.getProvider("BC");
    Security.removeProvider("BC");
  }

  @AfterEach
  void restoreProviders() {
    Security.removeProvider("BC");
    if (previousBc != null) {
      Security.addProvider(previousBc);
    }
  }

  // A peer using Milo's former BC names/defaults must accept new signatures and vice versa.
  // Testing both entry points protects session signatures and OpenSecureChannel verification.
  @ParameterizedTest
  @MethodSource("rsaConfigurations")
  void signaturesInteroperateWithLegacyBouncyCastle(
      SecurityPolicy policy, boolean bcFirst, boolean secureChannel) throws Exception {
    configureProvider(bcFirst);
    SecurityPolicyProfile profile = policy.getProfile();
    Signature peer = Signature.getInstance(legacySignatureName(policy), BC);
    peer.initVerify(certificate);
    peer.update(MESSAGE);
    byte[] signed =
        secureChannel
            ? SecureChannelStrategies.authentication(profile)
                .sign(profile, keyPair.getPrivate(), ByteBuffer.wrap(MESSAGE))
            : SignatureUtil.sign(
                profile.asymmetricSignatureAlgorithm(),
                keyPair.getPrivate(),
                ByteBuffer.wrap(MESSAGE));
    assertTrue(peer.verify(signed));

    peer.initSign(keyPair.getPrivate());
    peer.update(MESSAGE);
    byte[] peerSignature = peer.sign();
    verify(profile, secureChannel, MESSAGE, peerSignature);
    byte[] tampered = MESSAGE.clone();
    tampered[0] ^= 1;
    assertThrows(UaException.class, () -> verify(profile, secureChannel, tampered, peerSignature));
  }

  // BC's old OAEP alias uses SHA-256 for MGF1. A same-provider round trip would miss an
  // accidental SunJCE MGF1/SHA-1 default, so decrypt each implementation's ciphertext in the other.
  @ParameterizedTest
  @MethodSource("rsaConfigurations")
  void encryptionInteroperatesWithLegacyBouncyCastle(
      SecurityPolicy policy, boolean bcFirst, boolean secureChannel) throws Exception {
    configureProvider(bcFirst);
    SecurityPolicyProfile profile = policy.getProfile();
    Cipher encrypt =
        secureChannel
            ? SecureChannelStrategies.asymmetricEncryption(profile)
                .getEncryptionCipher(profile, certificate)
            : CipherFactory.createForEncryption(
                profile.asymmetricEncryptionAlgorithm(), keyPair.getPublic());
    Cipher decrypt =
        secureChannel
            ? SecureChannelStrategies.asymmetricEncryption(profile)
                .getDecryptionCipher(profile, keyPair.getPrivate())
            : CipherFactory.createForDecryption(
                profile.asymmetricEncryptionAlgorithm(), keyPair.getPrivate());
    assertProviderSelection(bcFirst, encrypt.getProvider());
    assertProviderSelection(bcFirst, decrypt.getProvider());

    Cipher peer = Cipher.getInstance(legacyCipherName(policy), BC);
    peer.init(Cipher.DECRYPT_MODE, keyPair.getPrivate());
    assertArrayEquals(MESSAGE, peer.doFinal(encrypt.doFinal(MESSAGE)));
    peer.init(Cipher.ENCRYPT_MODE, keyPair.getPublic());
    assertArrayEquals(MESSAGE, decrypt.doFinal(peer.doFinal(MESSAGE)));
  }

  // PSS signatures with the wrong MGF1 digest or salt length are not OPC UA RSA-PSS signatures.
  @ParameterizedTest
  @MethodSource("invalidPssParameters")
  void rejectsPssSignaturesWithWrongParameters(boolean secureChannel, PSSParameterSpec parameters)
      throws Exception {
    Signature peer = Signature.getInstance("RSASSA-PSS", "SunRsaSign");
    peer.initSign(keyPair.getPrivate());
    peer.setParameter(parameters);
    peer.update(MESSAGE);
    byte[] signature = peer.sign();
    assertThrows(
        UaException.class,
        () ->
            verify(
                SecurityPolicy.Aes256_Sha256_RsaPss.getProfile(),
                secureChannel,
                MESSAGE,
                signature));
  }

  private static void verify(
      SecurityPolicyProfile profile, boolean secureChannel, byte[] message, byte[] signature)
      throws UaException {
    if (secureChannel) {
      SecureChannelStrategies.authentication(profile)
          .verify(profile, certificate, signature, ByteBuffer.wrap(message));
    } else {
      SignatureUtil.verify(profile.asymmetricSignatureAlgorithm(), certificate, message, signature);
    }
  }

  private static void configureProvider(boolean bcFirst) throws Exception {
    if (bcFirst) {
      Security.insertProviderAt(BC, 1);
    } else {
      assertNull(Security.getProvider("BC"));
    }
    assertProviderSelection(bcFirst, Signature.getInstance("RSASSA-PSS").getProvider());
  }

  // The JDK provider name varies with java.security configuration; only BC's position matters.
  private static void assertProviderSelection(boolean bcFirst, Provider provider) {
    if (bcFirst) {
      assertEquals("BC", provider.getName());
    } else {
      assertNotEquals("BC", provider.getName());
    }
  }

  private static String legacySignatureName(SecurityPolicy policy) {
    return switch (policy) {
      case Basic128Rsa15, Basic256 -> "SHA1withRSA";
      case Basic256Sha256, Aes128_Sha256_RsaOaep -> "SHA256withRSA";
      case Aes256_Sha256_RsaPss -> "SHA256withRSA/PSS";
      default -> throw new IllegalArgumentException(policy.name());
    };
  }

  private static String legacyCipherName(SecurityPolicy policy) {
    return switch (policy) {
      case Basic128Rsa15 -> "RSA/ECB/PKCS1Padding";
      case Basic256, Basic256Sha256, Aes128_Sha256_RsaOaep -> "RSA/ECB/OAEPWithSHA-1AndMGF1Padding";
      case Aes256_Sha256_RsaPss -> "RSA/ECB/OAEPWithSHA256AndMGF1Padding";
      default -> throw new IllegalArgumentException(policy.name());
    };
  }

  private static Stream<Arguments> rsaConfigurations() {
    List<Arguments> configurations = new ArrayList<>();
    for (SecurityPolicy policy : RSA_POLICIES) {
      for (boolean bcFirst : List.of(false, true)) {
        for (boolean secureChannel : List.of(false, true)) {
          configurations.add(Arguments.of(policy, bcFirst, secureChannel));
        }
      }
    }
    return configurations.stream();
  }

  private static Stream<Arguments> invalidPssParameters() {
    return Stream.of(false, true)
        .flatMap(
            secureChannel ->
                Stream.of(
                    Arguments.of(
                        secureChannel,
                        new PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA1, 32, 1)),
                    Arguments.of(
                        secureChannel,
                        new PSSParameterSpec("SHA-256", "MGF1", MGF1ParameterSpec.SHA256, 20, 1))));
  }
}
