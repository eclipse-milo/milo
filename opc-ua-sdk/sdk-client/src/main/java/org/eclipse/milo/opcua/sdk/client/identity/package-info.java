/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

/**
 * Client-side user identity providers for ActivateSession.
 *
 * <p>An {@link org.eclipse.milo.opcua.sdk.client.identity.IdentityProvider} chooses one of the
 * user-token policies advertised by the selected endpoint and turns caller credentials into the
 * {@code UserIdentityToken} sent during ActivateSession. Most callers use one of the built-in
 * providers: anonymous, username/password, X.509 certificate, or a composite provider that tries
 * several providers in order.
 *
 * <h2>Session handoff</h2>
 *
 * <p>Some user-token policies need data learned during CreateSession before the provider can build
 * a token. The SDK passes that data through {@link
 * org.eclipse.milo.opcua.sdk.client.identity.IdentityProviderContext}. Legacy providers can ignore
 * the context and continue using the endpoint and server nonce only. Providers that protect a
 * username secret with an enhanced ECC or RSA-DH policy also receive the server's signed session
 * ephemeral key and the local client certificate identity selected for the user-token security
 * policy.
 *
 * <h2>CreateSession negotiation</h2>
 *
 * <p>Username providers may need an additional CreateSession header before ActivateSession. The
 * {@code IdentityProvider} methods split this into two decisions: the selected user-token security
 * policy, and the narrower enhanced username-token policy that actually requires ephemeral key
 * material. Certificate-token providers may use ECC or RSA-DH security policies, but they must not
 * request username-secret key negotiation.
 *
 * <h2>Extension guidance</h2>
 *
 * <p>Custom providers should keep policy selection and token construction aligned. If a provider
 * returns an enhanced username-token security policy for CreateSession, its context-based {@code
 * getIdentityToken(...)} implementation should consume the returned receiver public key and fail
 * clearly when that material is missing. Providers for token types other than username should
 * return empty from the enhanced username-token negotiation hook.
 */
package org.eclipse.milo.opcua.sdk.client.identity;
