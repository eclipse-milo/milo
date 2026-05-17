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

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.Signature;
import java.security.spec.ECGenParameterSpec;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.Mac;
import javax.crypto.SecretKeyFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.AuthAxis;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.ChunkProtectionAxis;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile.KeyAgreementAxis;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Resolves the provider profile that can satisfy a security policy.
 *
 * <p>The default resolver prefers Bouncy Castle for enhanced policies and falls back to the
 * built-in JDK/JCA providers when their primitives are available. Policies that do not require
 * enhanced authentication, key agreement, or AEAD chunk protection resolve to {@link
 * ProviderProfile#JDK}. Resolver instances are thread-safe and cache successful policy resolutions;
 * create a new resolver after changing provider preferences or JVM provider configuration.
 *
 * <p>A provider profile is a capability boundary, not just a provider name lookup. Some policy
 * families can use either Bouncy Castle or the JDK when all required transformations are present.
 * NIST ECC, Curve25519/Curve448, and RSA-DH profiles are in that portable group when the provider
 * can create the required curve or ffdhe3072 keys and perform the matching HKDF/AEAD operations.
 * Brainpool policies are intentionally Bouncy Castle-only because Java's built-in providers do not
 * provide portable Brainpool curve support for ECDSA and ECDH.
 */
@NullMarked
public final class SecurityProviderResolver {

  private static final String BC_PROVIDER_NAME = "BC";

  private final List<ProviderProfile> providerProfiles;
  private final ConcurrentMap<SecurityPolicy, ProviderProfile> profileCache =
      new ConcurrentHashMap<>();

  private SecurityProviderResolver(List<ProviderProfile> providerProfiles) {
    this.providerProfiles = List.copyOf(providerProfiles);
  }

  /**
   * Create a resolver that prefers Bouncy Castle and then tries the built-in JDK/JCA providers.
   *
   * @return the default resolver.
   */
  public static SecurityProviderResolver create() {
    return withProviderProfiles(List.of(ProviderProfile.BOUNCY_CASTLE, ProviderProfile.JDK));
  }

  /**
   * Create a resolver with an explicit provider-profile preference order.
   *
   * @param providerProfiles the provider profiles to try, in order.
   * @return a resolver that uses {@code providerProfiles}.
   */
  public static SecurityProviderResolver withProviderProfiles(
      List<ProviderProfile> providerProfiles) {
    return new SecurityProviderResolver(providerProfiles);
  }

  /**
   * Resolve the provider profile that can satisfy {@code profile}.
   *
   * @param profile the security-policy profile to resolve.
   * @return the provider profile to use.
   * @throws UaException if no configured provider profile can satisfy {@code profile}.
   */
  public ProviderProfile resolve(SecurityPolicyProfile profile) throws UaException {
    requireNonNull(profile, "profile");

    SecurityPolicy securityPolicy = profile.securityPolicy();
    ProviderProfile cached = profileCache.get(securityPolicy);

    if (cached != null) {
      return cached;
    }

    ProviderProfile resolved = find(profile);
    ProviderProfile previous = profileCache.putIfAbsent(securityPolicy, resolved);

    return previous != null ? previous : resolved;
  }

  private ProviderProfile find(SecurityPolicyProfile profile) throws UaException {
    if (!needsNewProviderProfile(profile)) {
      return ProviderProfile.JDK;
    }

    List<String> attempts = new ArrayList<>();

    for (ProviderProfile providerProfile : providerProfiles) {
      try {
        validate(providerProfile, profile);

        return providerProfile;
      } catch (GeneralSecurityException | RuntimeException e) {
        attempts.add(formatAttempt(providerProfile, e));
      }
    }

    throw new UaException(
        StatusCodes.Bad_ConfigurationError, unsupportedProfileMessage(profile, attempts));
  }

  private static boolean needsNewProviderProfile(SecurityPolicyProfile profile) {
    return switch (profile.authAxis()) {
      case ECDSA_NIST_P256_SHA256,
          ECDSA_NIST_P384_SHA384,
          ECDSA_BRAINPOOL_P256R1_SHA256,
          ECDSA_BRAINPOOL_P384R1_SHA384,
          ED25519,
          ED448 ->
          true;
      default ->
          switch (profile.keyAgreementAxis()) {
            case ECDH_NIST_P256,
                ECDH_NIST_P384,
                ECDH_BRAINPOOL_P256R1,
                ECDH_BRAINPOOL_P384R1,
                X25519,
                X448,
                FFDH_3072 ->
                true;
            default ->
                switch (profile.chunkProtectionAxis()) {
                  case AES_GCM, CHACHA20_POLY1305 -> true;
                  default -> false;
                };
          };
    };
  }

  private static void validate(ProviderProfile providerProfile, SecurityPolicyProfile profile)
      throws GeneralSecurityException {

    switch (providerProfile) {
      case BOUNCY_CASTLE -> validateBouncyCastle(profile);
      case JDK -> validateJdk(profile);
    }
  }

  private static void validateBouncyCastle(SecurityPolicyProfile profile)
      throws GeneralSecurityException {

    Provider provider = Security.getProvider(BC_PROVIDER_NAME);
    if (provider == null) {
      provider = new BouncyCastleProvider();
    }

    validateAuth(profile.authAxis(), ProviderProfile.BOUNCY_CASTLE, provider);
    validateKeyAgreement(profile.keyAgreementAxis(), ProviderProfile.BOUNCY_CASTLE, provider);
    validateHkdf(profile.keyAgreementAxis(), ProviderProfile.BOUNCY_CASTLE, provider);
    validateChunkProtection(profile.chunkProtectionAxis(), ProviderProfile.BOUNCY_CASTLE, provider);
  }

  private static void validateJdk(SecurityPolicyProfile profile) throws GeneralSecurityException {
    validateAuth(profile.authAxis(), ProviderProfile.JDK, null);
    validateKeyAgreement(profile.keyAgreementAxis(), ProviderProfile.JDK, null);
    validateHkdf(profile.keyAgreementAxis(), ProviderProfile.JDK, null);
    validateChunkProtection(profile.chunkProtectionAxis(), ProviderProfile.JDK, null);
  }

  private static void validateAuth(
      AuthAxis axis, ProviderProfile providerProfile, @Nullable Provider provider)
      throws GeneralSecurityException {

    switch (axis) {
      case RSA_PKCS1_SHA256 -> {
        if (providerProfile == ProviderProfile.BOUNCY_CASTLE) {
          Signature.getInstance("SHA256withRSA", requireNonNull(provider, "provider"));
        } else {
          requireJdkProvider("SHA256withRSA", p -> Signature.getInstance("SHA256withRSA", p));
        }
      }
      case ECDSA_NIST_P256_SHA256 -> {
        if (providerProfile == ProviderProfile.BOUNCY_CASTLE) {
          Provider bouncyCastleProvider = requireNonNull(provider, "provider");

          Signature.getInstance("SHA256WITHPLAIN-ECDSA", bouncyCastleProvider);
          validateEc("secp256r1", bouncyCastleProvider);
        } else {
          requireJdkProvider(
              "SHA256withECDSAinP1363Format",
              p -> {
                Signature.getInstance("SHA256withECDSAinP1363Format", p);
                validateEc("secp256r1", p);
              });
        }
      }
      case ECDSA_NIST_P384_SHA384 -> {
        if (providerProfile == ProviderProfile.BOUNCY_CASTLE) {
          Provider bouncyCastleProvider = requireNonNull(provider, "provider");

          Signature.getInstance("SHA384WITHPLAIN-ECDSA", bouncyCastleProvider);
          validateEc("secp384r1", bouncyCastleProvider);
        } else {
          requireJdkProvider(
              "SHA384withECDSAinP1363Format",
              p -> {
                Signature.getInstance("SHA384withECDSAinP1363Format", p);
                validateEc("secp384r1", p);
              });
        }
      }
      case ECDSA_BRAINPOOL_P256R1_SHA256 -> {
        if (providerProfile == ProviderProfile.BOUNCY_CASTLE) {
          Provider bouncyCastleProvider = requireNonNull(provider, "provider");

          Signature.getInstance("SHA256WITHPLAIN-ECDSA", bouncyCastleProvider);
          validateEc("brainpoolP256r1", bouncyCastleProvider);
        } else {
          throw new GeneralSecurityException(
              "Brainpool P-256 ECDSA requires Bouncy Castle provider profile");
        }
      }
      case ECDSA_BRAINPOOL_P384R1_SHA384 -> {
        if (providerProfile == ProviderProfile.BOUNCY_CASTLE) {
          Provider bouncyCastleProvider = requireNonNull(provider, "provider");

          Signature.getInstance("SHA384WITHPLAIN-ECDSA", bouncyCastleProvider);
          validateEc("brainpoolP384r1", bouncyCastleProvider);
        } else {
          throw new GeneralSecurityException(
              "Brainpool P-384 ECDSA requires Bouncy Castle provider profile");
        }
      }
      case ED25519 -> {
        if (providerProfile == ProviderProfile.BOUNCY_CASTLE) {
          Provider bouncyCastleProvider = requireNonNull(provider, "provider");

          Signature.getInstance("Ed25519", bouncyCastleProvider);
          KeyPairGenerator.getInstance("Ed25519", bouncyCastleProvider);
        } else {
          requireJdkProvider(
              "Ed25519",
              p -> {
                Signature.getInstance("Ed25519", p);
                KeyPairGenerator.getInstance("Ed25519", p);
              });
        }
      }
      case ED448 -> {
        if (providerProfile == ProviderProfile.BOUNCY_CASTLE) {
          Provider bouncyCastleProvider = requireNonNull(provider, "provider");

          Signature.getInstance("Ed448", bouncyCastleProvider);
          KeyPairGenerator.getInstance("Ed448", bouncyCastleProvider);
        } else {
          requireJdkProvider(
              "Ed448",
              p -> {
                Signature.getInstance("Ed448", p);
                KeyPairGenerator.getInstance("Ed448", p);
              });
        }
      }
      default -> {}
    }
  }

  private static void validateKeyAgreement(
      KeyAgreementAxis axis, ProviderProfile providerProfile, @Nullable Provider provider)
      throws GeneralSecurityException {

    switch (axis) {
      case ECDH_NIST_P256 -> {
        if (providerProfile == ProviderProfile.BOUNCY_CASTLE) {
          Provider bouncyCastleProvider = requireNonNull(provider, "provider");

          KeyAgreement.getInstance("ECDH", bouncyCastleProvider);
          validateEc("secp256r1", bouncyCastleProvider);
        } else {
          requireJdkProvider(
              "ECDH",
              p -> {
                KeyAgreement.getInstance("ECDH", p);
                validateEc("secp256r1", p);
              });
        }
      }
      case ECDH_NIST_P384 -> {
        if (providerProfile == ProviderProfile.BOUNCY_CASTLE) {
          Provider bouncyCastleProvider = requireNonNull(provider, "provider");

          KeyAgreement.getInstance("ECDH", bouncyCastleProvider);
          validateEc("secp384r1", bouncyCastleProvider);
        } else {
          requireJdkProvider(
              "ECDH",
              p -> {
                KeyAgreement.getInstance("ECDH", p);
                validateEc("secp384r1", p);
              });
        }
      }
      case ECDH_BRAINPOOL_P256R1 -> {
        if (providerProfile == ProviderProfile.BOUNCY_CASTLE) {
          Provider bouncyCastleProvider = requireNonNull(provider, "provider");

          KeyAgreement.getInstance("ECDH", bouncyCastleProvider);
          validateEc("brainpoolP256r1", bouncyCastleProvider);
        } else {
          throw new GeneralSecurityException(
              "Brainpool P-256 ECDH requires Bouncy Castle provider profile");
        }
      }
      case ECDH_BRAINPOOL_P384R1 -> {
        if (providerProfile == ProviderProfile.BOUNCY_CASTLE) {
          Provider bouncyCastleProvider = requireNonNull(provider, "provider");

          KeyAgreement.getInstance("ECDH", bouncyCastleProvider);
          validateEc("brainpoolP384r1", bouncyCastleProvider);
        } else {
          throw new GeneralSecurityException(
              "Brainpool P-384 ECDH requires Bouncy Castle provider profile");
        }
      }
      case X25519 -> {
        if (providerProfile == ProviderProfile.BOUNCY_CASTLE) {
          Provider bouncyCastleProvider = requireNonNull(provider, "provider");

          KeyAgreement.getInstance("X25519", bouncyCastleProvider);
          KeyPairGenerator.getInstance("X25519", bouncyCastleProvider);
        } else {
          requireJdkProvider(
              "X25519",
              p -> {
                KeyAgreement.getInstance("X25519", p);
                KeyPairGenerator.getInstance("X25519", p);
              });
        }
      }
      case X448 -> {
        if (providerProfile == ProviderProfile.BOUNCY_CASTLE) {
          Provider bouncyCastleProvider = requireNonNull(provider, "provider");

          KeyAgreement.getInstance("X448", bouncyCastleProvider);
          KeyPairGenerator.getInstance("X448", bouncyCastleProvider);
        } else {
          requireJdkProvider(
              "X448",
              p -> {
                KeyAgreement.getInstance("X448", p);
                KeyPairGenerator.getInstance("X448", p);
              });
        }
      }
      case FFDH_3072 -> {
        if (providerProfile == ProviderProfile.BOUNCY_CASTLE) {
          Provider bouncyCastleProvider = requireNonNull(provider, "provider");

          validateFfdhe3072(bouncyCastleProvider);
        } else {
          requireJdkProvider("DiffieHellman", SecurityProviderResolver::validateFfdhe3072);
        }
      }
      default -> {}
    }
  }

  private static void validateHkdf(
      KeyAgreementAxis axis, ProviderProfile providerProfile, @Nullable Provider provider)
      throws GeneralSecurityException {

    switch (axis) {
      case ECDH_NIST_P256, ECDH_BRAINPOOL_P256R1, X25519, FFDH_3072 -> {
        if (providerProfile == ProviderProfile.BOUNCY_CASTLE) {
          Provider bouncyCastleProvider = requireNonNull(provider, "provider");

          SecretKeyFactory.getInstance("HKDF-SHA256", bouncyCastleProvider);
        } else {
          requireJdkProvider("HmacSHA256", p -> Mac.getInstance("HmacSHA256", p));
        }
      }
      case ECDH_NIST_P384, ECDH_BRAINPOOL_P384R1, X448 -> {
        if (providerProfile == ProviderProfile.BOUNCY_CASTLE) {
          Provider bouncyCastleProvider = requireNonNull(provider, "provider");

          SecretKeyFactory.getInstance("HKDF-SHA384", bouncyCastleProvider);
        } else {
          requireJdkProvider("HmacSHA384", p -> Mac.getInstance("HmacSHA384", p));
        }
      }
      default -> {}
    }
  }

  private static void validateChunkProtection(
      ChunkProtectionAxis axis, ProviderProfile providerProfile, @Nullable Provider provider)
      throws GeneralSecurityException {

    switch (axis) {
      case AES_GCM -> validateCipher("AES/GCM/NoPadding", providerProfile, provider);
      case CHACHA20_POLY1305 -> validateCipher("ChaCha20-Poly1305", providerProfile, provider);
      default -> {}
    }
  }

  private static void validateCipher(
      String transformation, ProviderProfile providerProfile, @Nullable Provider provider)
      throws GeneralSecurityException {

    if (providerProfile == ProviderProfile.BOUNCY_CASTLE) {
      Provider bouncyCastleProvider = requireNonNull(provider, "provider");

      Cipher.getInstance(transformation, bouncyCastleProvider);
    } else {
      requireJdkProvider(transformation, p -> Cipher.getInstance(transformation, p));
    }
  }

  private static void validateEc(String curveName, Provider provider)
      throws GeneralSecurityException {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC", provider);

    keyPairGenerator.initialize(new ECGenParameterSpec(curveName));
  }

  private static void validateFfdhe3072(Provider provider) throws GeneralSecurityException {
    KeyPairGenerator keyPairGenerator = dhKeyPairGenerator(provider);
    keyPairGenerator.initialize(FiniteFieldDhKeyAgreementUtil.ffdhe3072ParameterSpec());
    validateDhKeyFactory(provider);
    validateDhKeyAgreement(provider);
  }

  private static KeyPairGenerator dhKeyPairGenerator(Provider provider)
      throws GeneralSecurityException {
    try {
      return KeyPairGenerator.getInstance("DiffieHellman", provider);
    } catch (NoSuchAlgorithmException e) {
      return KeyPairGenerator.getInstance("DH", provider);
    }
  }

  private static void validateDhKeyAgreement(Provider provider) throws GeneralSecurityException {
    try {
      KeyAgreement.getInstance("DiffieHellman", provider);
    } catch (NoSuchAlgorithmException e) {
      KeyAgreement.getInstance("DH", provider);
    }
  }

  private static void validateDhKeyFactory(Provider provider) throws GeneralSecurityException {
    try {
      KeyFactory.getInstance("DiffieHellman", provider);
    } catch (NoSuchAlgorithmException e) {
      KeyFactory.getInstance("DH", provider);
    }
  }

  private static void requireJdkProvider(String name, ProviderCheck check)
      throws GeneralSecurityException {

    List<String> attempts = new ArrayList<>();

    for (Provider provider : Security.getProviders()) {
      if (!BC_PROVIDER_NAME.equals(provider.getName())) {
        try {
          check.check(provider);

          return;
        } catch (GeneralSecurityException | RuntimeException e) {
          attempts.add(provider.getName() + ": " + e.getClass().getSimpleName());
        }
      }
    }

    throw new GeneralSecurityException(
        "no built-in JDK/JCA provider for " + name + "; attempted " + attempts);
  }

  private String unsupportedProfileMessage(SecurityPolicyProfile profile, List<String> attempts) {
    StringBuilder message =
        new StringBuilder()
            .append("no provider profile found for ")
            .append(profile.securityPolicy().getUri());

    if (providerProfiles.isEmpty()) {
      message.append("; configured provider profiles: <none>");
    } else {
      message.append("; configured provider profiles: ").append(providerProfiles);
    }

    if (!attempts.isEmpty()) {
      message.append("; attempted: ").append(String.join("; ", attempts));
    }

    return message.toString();
  }

  private static String formatAttempt(ProviderProfile providerProfile, Exception exception) {
    String message = exception.getMessage();

    return providerProfile
        + " failed: "
        + exception.getClass().getSimpleName()
        + (message == null || message.isBlank() ? "" : " (" + message + ")");
  }

  /** A provider family that can satisfy a security policy. */
  public enum ProviderProfile {
    /** Bouncy Castle provider profile. */
    BOUNCY_CASTLE,

    /** Built-in JDK/JCA provider profile. */
    JDK
  }

  @FunctionalInterface
  private interface ProviderCheck {
    void check(Provider provider) throws GeneralSecurityException;
  }
}
