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
 * <h2>Client workflows</h2>
 *
 * <p>Applications that know their expected servers ahead of time can create clients with a shared
 * manager and a {@link org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectSelector}. The
 * client transport registers the selector during connect, waits for one matching candidate, and
 * rejoins the normal client Session path after the channel is claimed.
 *
 * <p>Dynamic applications can observe pending candidates, claim one exact candidate with {@link
 * org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectManager#claim(java.util.UUID)}, resolve
 * application-specific client configuration, and create an {@link
 * org.eclipse.milo.opcua.sdk.client.OpcUaClient} from the resulting {@link
 * org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectConnection}. A direct claimed connection
 * is one-shot: after it is consumed, reconnect requires a new reverse-connect candidate.
 *
 * <h2>Transport handoff</h2>
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.client.reverse.ReverseTcpClientTransport} is the bridge back
 * to the normal client SDK path. It waits for the manager to hand off a claimed channel, or
 * consumes one pre-claimed connection directly, installs the standard client UASC pipeline, and
 * then lets the Session FSM create and maintain the Session as it would for outbound TCP.
 *
 * <h2>Security boundary</h2>
 *
 * <p>{@code ReverseHello} is only a routing and resource-admission hint. The types here
 * deliberately keep server identity validation in the normal Milo client certificate, endpoint,
 * security policy, SecureChannel, and Session validation flow after a claimed channel rejoins the
 * client transport pipeline.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.client.reverse;

import org.jspecify.annotations.NullMarked;
