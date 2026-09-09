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
 * Owns the application-facing client, Session lifecycle, and subscription recovery across
 * successive protocol connections. The configured endpoint is the one the client connects to; an
 * optional {@link org.eclipse.milo.opcua.sdk.client.EndpointResolver} lets the client refresh that
 * endpoint's description after SecureChannel establishment fails on a secured endpoint, as can
 * happen when the server's application certificate is replaced.
 *
 * <p>The resolver returns one {@link org.eclipse.milo.opcua.sdk.client.EndpointConfiguration}
 * containing the selected endpoint and the unmodified discovery list. Selection can apply an
 * advertised-host override, while Session endpoint validation still compares the original
 * advertised list against CreateSession. A refresh must preserve the selected URL, server
 * application URI, transport, security policy, mode and user token policies, since discovery is
 * unauthenticated and the token policies decide how credentials are protected. User identity and
 * certificate validation remain client configuration.
 *
 * <p>Refresh sits beside the transport's reconnect loop rather than inside it. Transports that
 * implement {@code ChannelStateObservable} report each failed attempt; the client classifies the
 * SecureChannel handshake phase independently of the status code, runs at most one resolution at a
 * time within the resolver's cooldown and timeout, and swaps in the result. The transport's next
 * attempt reads the refreshed endpoint. Outstanding resolutions are cancelled on disconnect and
 * late results are discarded. Applications implementing a resolver must release its resources when
 * its future is cancelled.
 *
 * <p>Connection metadata is separate from established security bindings. SecureChannels retain
 * their original certificates for their whole lifetime. Sessions retain their creation endpoint,
 * client certificate and server certificate. Reactivation uses those Session inputs together with
 * the new channel's certificate and thumbprint; failed reactivation follows normal Session and
 * subscription recovery. Neither discovery nor a server error grants trust in a new certificate.
 */
package org.eclipse.milo.opcua.sdk.client;
