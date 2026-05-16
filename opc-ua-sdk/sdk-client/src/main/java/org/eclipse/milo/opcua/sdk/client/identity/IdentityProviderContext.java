/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.identity;

import static java.util.Objects.requireNonNull;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.jspecify.annotations.Nullable;

/**
 * Session activation context available to identity providers.
 *
 * <p>The SDK creates this context after CreateSession and before ActivateSession. The endpoint and
 * server nonce are always present. The optional fields are populated when the selected token policy
 * needs extra material, such as an ECC username-token policy that requires the server's receiver
 * public key and the client's application certificate identity to produce an {@code
 * EccEncryptedSecret}.
 */
public final class IdentityProviderContext {

  private final EndpointDescription endpoint;
  private final ByteString serverNonce;
  private final @Nullable SecurityPolicy userTokenSecurityPolicy;
  private final @Nullable ByteString receiverEccPublicKey;
  private final @Nullable KeyPair clientApplicationKeyPair;
  private final X509Certificate @Nullable [] clientCertificateChain;

  /**
   * Create a context for providers that only need the endpoint and server nonce.
   *
   * @param endpoint the endpoint being activated.
   * @param serverNonce the server nonce returned by CreateSession or the latest ActivateSession.
   */
  public IdentityProviderContext(EndpointDescription endpoint, ByteString serverNonce) {
    this(endpoint, serverNonce, null, null, null, null);
  }

  /**
   * Create a context with ECC receiver key material and client application identity.
   *
   * @param endpoint the endpoint being activated.
   * @param serverNonce the server nonce returned by CreateSession or the latest ActivateSession.
   * @param receiverEccPublicKey the server session public key returned for ECC username-token
   *     encryption, or {@code null}.
   * @param clientApplicationKeyPair the client application key pair selected for the token policy,
   *     or {@code null}.
   * @param clientCertificateChain the client application certificate chain selected for the token
   *     policy, or {@code null}.
   */
  public IdentityProviderContext(
      EndpointDescription endpoint,
      ByteString serverNonce,
      @Nullable ByteString receiverEccPublicKey,
      @Nullable KeyPair clientApplicationKeyPair,
      X509Certificate @Nullable [] clientCertificateChain) {

    this(
        endpoint,
        serverNonce,
        null,
        receiverEccPublicKey,
        clientApplicationKeyPair,
        clientCertificateChain);
  }

  /**
   * Create a context with the selected user-token security policy and any policy-specific material.
   *
   * @param endpoint the endpoint being activated.
   * @param serverNonce the server nonce returned by CreateSession or the latest ActivateSession.
   * @param userTokenSecurityPolicy the selected user-token security policy, or {@code null}.
   * @param receiverEccPublicKey the server session public key returned for ECC username-token
   *     encryption, or {@code null}.
   * @param clientApplicationKeyPair the client application key pair selected for the token policy,
   *     or {@code null}.
   * @param clientCertificateChain the client application certificate chain selected for the token
   *     policy, or {@code null}.
   */
  public IdentityProviderContext(
      EndpointDescription endpoint,
      ByteString serverNonce,
      @Nullable SecurityPolicy userTokenSecurityPolicy,
      @Nullable ByteString receiverEccPublicKey,
      @Nullable KeyPair clientApplicationKeyPair,
      X509Certificate @Nullable [] clientCertificateChain) {

    this.endpoint = requireNonNull(endpoint, "endpoint");
    this.serverNonce = requireNonNull(serverNonce, "serverNonce");
    this.userTokenSecurityPolicy = userTokenSecurityPolicy;
    this.receiverEccPublicKey = receiverEccPublicKey;
    this.clientApplicationKeyPair = clientApplicationKeyPair;
    this.clientCertificateChain =
        clientCertificateChain != null
            ? Arrays.copyOf(clientCertificateChain, clientCertificateChain.length)
            : null;
  }

  public EndpointDescription getEndpoint() {
    return endpoint;
  }

  /**
   * Get the server nonce that the identity token must bind to.
   *
   * @return the server nonce returned by CreateSession or the latest ActivateSession.
   */
  public ByteString getServerNonce() {
    return serverNonce;
  }

  /**
   * Get the selected user-token security policy, if one was determined before token creation.
   *
   * @return the selected user-token security policy.
   */
  public Optional<SecurityPolicy> getUserTokenSecurityPolicy() {
    return Optional.ofNullable(userTokenSecurityPolicy);
  }

  /**
   * Get the receiver public key used by ECC username-token encryption.
   *
   * @return the server session public key returned in the CreateSession additional header.
   */
  public Optional<ByteString> getReceiverEccPublicKey() {
    return Optional.ofNullable(receiverEccPublicKey);
  }

  /**
   * Get the client application key pair selected for the token policy.
   *
   * @return the client application key pair.
   */
  public Optional<KeyPair> getClientApplicationKeyPair() {
    return Optional.ofNullable(clientApplicationKeyPair);
  }

  /**
   * Get the client application certificate chain selected for the token policy.
   *
   * @return a defensive copy of the selected certificate chain.
   */
  public Optional<X509Certificate[]> getClientCertificateChain() {
    return Optional.ofNullable(clientCertificateChain)
        .map(chain -> Arrays.copyOf(chain, chain.length));
  }
}
