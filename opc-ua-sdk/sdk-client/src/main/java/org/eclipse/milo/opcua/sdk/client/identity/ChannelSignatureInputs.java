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

import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.jspecify.annotations.NullMarked;

/**
 * Raw SecureChannel-bound inputs an identity provider needs to build a channel-bound user-token
 * signature for a SecureChannel-enhancement (ECC or RSA-DH) user-token policy.
 *
 * <p>The SDK resolves these from the live SecureChannel and CreateSession exchange before invoking
 * {@link IdentityProvider#getIdentityToken(IdentityProviderContext)} and exposes them through
 * {@link IdentityProviderContext#getChannelSignatureInputs()}. They map directly onto the OPC UA
 * Part 4 §6.1.8 Table 101 {@code UserTokenSignature} inputs (the {@code ServerNonce} input is the
 * context's {@link IdentityProviderContext#getServerNonce()}). Providers pass them to {@link
 * org.eclipse.milo.opcua.stack.core.security.ChannelBoundSignatureData#userTokenSignatureData}.
 *
 * <p>Legacy user-token policies do not need these inputs; the context populates them regardless,
 * but only the enhancement layouts consume the channel thumbprint and channel certificates.
 *
 * @param channelThumbprint the first OpenSecureChannel response signature, or {@link
 *     ByteString#NULL_VALUE} when the carrying SecureChannel is unsecured ({@code SecurityMode}
 *     {@code None}) or captured no thumbprint. It is never Java {@code null}; provider authors must
 *     test {@link ByteString#isNullOrEmpty()} (not {@code == null}) to detect the unsecured case.
 * @param clientNonce the original client nonce from CreateSession.
 * @param serverCertificate the CreateSession server certificate, signed verbatim as received.
 * @param serverChannelCertificate the server certificate used by the SecureChannel.
 * @param clientCertificate the CreateSession client (application) certificate.
 */
@NullMarked
public record ChannelSignatureInputs(
    ByteString channelThumbprint,
    ByteString clientNonce,
    ByteString serverCertificate,
    ByteString serverChannelCertificate,
    ByteString clientCertificate) {}
