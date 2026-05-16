/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.identity;

import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.EccUserTokenAdditionalHeader;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.SignatureData;
import org.eclipse.milo.opcua.stack.core.types.structured.UserIdentityToken;

/**
 * Supplies the identity token used when a client activates a session.
 *
 * <p>The SDK asks the configured provider whether CreateSession needs any identity-related
 * additional header, then asks for the token to send in ActivateSession. Providers that only need
 * the endpoint and server nonce can implement the legacy two-argument method. Providers that depend
 * on CreateSession output, such as ECC username-token providers, should override the context-based
 * method.
 */
public interface IdentityProvider {

  /**
   * Return the user-token security policy this provider expects to use for {@code endpoint}, if it
   * can be determined before ActivateSession.
   *
   * <p>The default keeps existing providers source-compatible and means no CreateSession
   * additional-header negotiation is required.
   *
   * @param endpoint the {@link EndpointDescription} being connected to.
   * @return the selected user-token security policy, or empty if no additional negotiation is
   *     needed or the provider cannot determine it early.
   * @throws Exception if the endpoint does not advertise a token policy usable by this provider.
   */
  default Optional<SecurityPolicy> getUserTokenSecurityPolicy(EndpointDescription endpoint)
      throws Exception {

    return Optional.empty();
  }

  /**
   * Return the ECC username-token security policy that needs CreateSession additional-header key
   * negotiation, if this provider will use one.
   *
   * <p>This is narrower than {@link #getUserTokenSecurityPolicy(EndpointDescription)}. Certificate
   * and other token types may use ECC signatures without requiring username-secret ECDH key
   * material.
   *
   * @param endpoint the {@link EndpointDescription} being connected to.
   * @return the selected ECC username-token security policy, or empty if no ECC username-secret
   *     negotiation is required.
   * @throws Exception if the endpoint does not advertise a token policy usable by this provider.
   */
  default Optional<SecurityPolicy> getEccUserTokenSecurityPolicy(EndpointDescription endpoint)
      throws Exception {

    return Optional.empty();
  }

  /**
   * Return the CreateSession additional header needed by this provider, if any.
   *
   * @param context the encoding context used to encode generated structures.
   * @param endpoint the {@link EndpointDescription} being connected to.
   * @return an encoded additional header, or {@code null} when no header is required.
   * @throws Exception if the header cannot be built for the selected endpoint.
   */
  default ExtensionObject getCreateSessionAdditionalHeader(
      EncodingContext context, EndpointDescription endpoint) throws Exception {

    Optional<SecurityPolicy> securityPolicy = getEccUserTokenSecurityPolicy(endpoint);

    if (securityPolicy.isPresent()) {
      return EccUserTokenAdditionalHeader.createRequest(context, securityPolicy.get());
    } else {
      return null;
    }
  }

  /**
   * Return the {@link UserIdentityToken} and {@link SignatureData} (if applicable for the token) to
   * use when activating a session.
   *
   * <p>The default implementation preserves compatibility with existing identity providers that
   * only need the endpoint and server nonce.
   *
   * @param context the identity-provider context for the session activation.
   * @return a {@link SignedIdentityToken} containing the {@link UserIdentityToken} and {@link
   *     SignatureData}.
   * @throws Exception if the provider cannot create a usable identity token.
   */
  default SignedIdentityToken getIdentityToken(IdentityProviderContext context) throws Exception {
    return getIdentityToken(context.getEndpoint(), context.getServerNonce());
  }

  /**
   * Return the {@link UserIdentityToken} and {@link SignatureData} (if applicable for the token) to
   * use when activating a session.
   *
   * @param endpoint the {@link EndpointDescription} being connected to.
   * @return a {@link SignedIdentityToken} containing the {@link UserIdentityToken} and {@link
   *     SignatureData}.
   * @throws Exception if the provider cannot create a usable identity token.
   */
  SignedIdentityToken getIdentityToken(EndpointDescription endpoint, ByteString serverNonce)
      throws Exception;
}
