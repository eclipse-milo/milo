/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.sks;

import static java.util.Objects.requireNonNullElse;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.eclipse.milo.opcua.sdk.client.identity.AnonymousProvider;
import org.eclipse.milo.opcua.sdk.client.identity.IdentityProvider;
import org.eclipse.milo.opcua.sdk.client.identity.UsernameProvider;
import org.eclipse.milo.opcua.sdk.client.identity.X509IdentityProvider;
import org.eclipse.milo.opcua.sdk.pubsub.security.KeyCredential;
import org.eclipse.milo.opcua.sdk.pubsub.security.KeyCredentialStore;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.types.enumerated.UserTokenType;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.jspecify.annotations.Nullable;

/**
 * Selects the session identity for an SKS connection per the config entry's {@code
 * UserIdentityTokens} (Table 40).
 *
 * <p>An empty/null array means Anonymous — authorization is then by application identity. A
 * non-empty array lists the token types that should be used: they are tried in listed order,
 * constrained to types the selected endpoint actually offers, and the entry fails if none is usable
 * so the implementation never silently downgrades past the configured list.
 *
 * <p>USERNAME resolves credentials through the {@link KeyCredentialStore} keyed by the SKS
 * ApplicationUri, following the Part 12 KeyCredential ResourceUri rule.
 */
final class SksIdentitySelector {

  private SksIdentitySelector() {}

  /**
   * Select an identity for a connection to the SKS described by {@code entry}.
   *
   * @param entry the SecurityKeyServices config entry.
   * @param endpoint the discovered endpoint selected for the connection.
   * @param credentialStore the credential store for USERNAME identities; may be null.
   * @param certificateValidator the validator used for the session, reused for user token secret
   *     encryption.
   * @param userIdentityCertificate the certificate for CERTIFICATE identities; may be null.
   * @param userIdentityPrivateKey the private key for CERTIFICATE identities; may be null.
   * @return the selected identity.
   * @throws UaException with {@code Bad_ConfigurationError} if no listed token type is usable.
   */
  static IdentitySelection select(
      EndpointDescription entry,
      EndpointDescription endpoint,
      @Nullable KeyCredentialStore credentialStore,
      CertificateValidator certificateValidator,
      @Nullable X509Certificate userIdentityCertificate,
      @Nullable PrivateKey userIdentityPrivateKey)
      throws UaException {

    UserTokenPolicy[] entryTokens =
        requireNonNullElse(entry.getUserIdentityTokens(), new UserTokenPolicy[0]);

    if (entryTokens.length == 0) {
      return new IdentitySelection(new AnonymousProvider(), UserTokenType.Anonymous, null);
    }

    Set<UserTokenType> offeredTypes = EnumSet.noneOf(UserTokenType.class);
    UserTokenPolicy[] endpointTokens =
        requireNonNullElse(endpoint.getUserIdentityTokens(), new UserTokenPolicy[0]);
    for (UserTokenPolicy policy : endpointTokens) {
      if (policy != null && policy.getTokenType() != null) {
        offeredTypes.add(policy.getTokenType());
      }
    }

    List<String> reasons = new ArrayList<>();

    for (UserTokenPolicy entryToken : entryTokens) {
      UserTokenType tokenType = entryToken != null ? entryToken.getTokenType() : null;
      if (tokenType == null) {
        continue;
      }
      if (!offeredTypes.contains(tokenType)) {
        reasons.add(tokenType + ": not offered by the selected endpoint");
        continue;
      }

      switch (tokenType) {
        case Anonymous -> {
          return new IdentitySelection(new AnonymousProvider(), tokenType, null);
        }

        case UserName -> {
          if (credentialStore == null) {
            reasons.add("UserName: no KeyCredentialStore configured");
            continue;
          }
          String resourceUri =
              entry.getServer() != null ? entry.getServer().getApplicationUri() : null;
          if (resourceUri == null || resourceUri.isEmpty()) {
            reasons.add("UserName: entry has no server.applicationUri to use as ResourceUri");
            continue;
          }
          Optional<KeyCredential> credential = credentialStore.lookup(resourceUri);
          if (credential.isEmpty()) {
            reasons.add("UserName: no credential for ResourceUri " + resourceUri);
            continue;
          }
          // The Supplier<byte[]> form is required: UsernameProvider zeroes the supplied bytes
          // after use, and this constructor reuses the session validator for token encryption
          // (the 2-arg constructor installs an insecure validator).
          char[] secret = credential.get().secret();
          var identityProvider =
              new UsernameProvider(
                  credential.get().credentialId(), () -> utf8Bytes(secret), certificateValidator);
          return new IdentitySelection(identityProvider, tokenType, secret);
        }

        case Certificate -> {
          if (userIdentityCertificate == null || userIdentityPrivateKey == null) {
            reasons.add("Certificate: no user identity certificate configured");
            continue;
          }
          return new IdentitySelection(
              new X509IdentityProvider(userIdentityCertificate, userIdentityPrivateKey),
              tokenType,
              null);
        }

        default -> reasons.add(tokenType + ": unsupported token type");
      }
    }

    throw new UaException(
        StatusCodes.Bad_ConfigurationError,
        "no usable user identity token for SKS connection: " + String.join("; ", reasons));
  }

  /**
   * Encode a secret to fresh UTF-8 bytes without going through a {@link String}.
   *
   * <p>The intermediate encode buffer is wiped; the returned array is wiped by the consumer ({@link
   * UsernameProvider} zeroes supplied password bytes after use).
   *
   * @param chars the secret characters; not modified.
   * @return a new UTF-8 encoding of {@code chars}.
   */
  static byte[] utf8Bytes(char[] chars) {
    ByteBuffer buffer = StandardCharsets.UTF_8.encode(CharBuffer.wrap(chars));
    byte[] bytes = new byte[buffer.remaining()];
    buffer.get(bytes);
    if (buffer.hasArray()) {
      Arrays.fill(buffer.array(), (byte) 0);
    }
    return bytes;
  }

  /**
   * A selected identity.
   *
   * @param identityProvider the provider to activate the session with.
   * @param tokenType the token type in use, for diagnostics.
   * @param secret the borrowed credential secret backing a USERNAME identity, or null; the fetch
   *     wipes it via {@link #wipeSecret()} once the session attempt is over.
   */
  record IdentitySelection(
      IdentityProvider identityProvider, UserTokenType tokenType, char @Nullable [] secret) {

    /** Wipe the borrowed credential secret, if any. */
    void wipeSecret() {
      if (secret != null) {
        Arrays.fill(secret, '\0');
      }
    }
  }
}
