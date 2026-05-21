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
 * Client-side Reverse Connect support.
 *
 * <p>The package owns the client-side listener and manager responsibilities that happen before
 * normal client UA-TCP handshaking begins. A {@link
 * org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectManager} binds one or more client
 * listener sockets, accepts server-opened TCP channels, requires the first message to be {@code
 * ReverseHello}, applies pre-SecureChannel admission policy, and either claims the channel for a
 * matching selector or holds it briefly as a pending candidate.
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.client.reverse.ReverseTcpClientTransport} is the bridge back
 * to the normal client SDK path. It waits for the manager to hand off a claimed channel, installs
 * the standard client UASC pipeline, and then lets the Session FSM create and maintain the Session
 * as it would for outbound TCP.
 *
 * <p>{@code ReverseHello} is only a routing and resource-admission hint. The types here
 * deliberately keep server identity validation in the normal Milo client certificate, endpoint,
 * security policy, SecureChannel, and Session validation flow after a claimed channel rejoins the
 * client transport pipeline.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.client.reverse;

import org.jspecify.annotations.NullMarked;
