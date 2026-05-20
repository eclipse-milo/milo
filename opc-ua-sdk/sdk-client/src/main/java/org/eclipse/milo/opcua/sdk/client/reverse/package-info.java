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
 * Client-side Reverse Connect control-plane support.
 *
 * <p>The package owns the listener and manager responsibilities that happen before normal client
 * UA-TCP handshaking begins. A {@link
 * org.eclipse.milo.opcua.sdk.client.reverse.ReverseConnectManager} binds one or more client
 * listener sockets, accepts server-opened TCP channels, requires the first message to be {@code
 * ReverseHello}, applies pre-SecureChannel admission policy, and either claims the channel for a
 * matching selector or holds it briefly as a pending candidate.
 *
 * <p>{@code ReverseHello} is only a routing and resource-admission hint. The types here
 * deliberately stop before SecureChannel and Session creation; server identity remains the
 * responsibility of the normal Milo client certificate, endpoint, security policy, and Session
 * validation flow after a claimed channel rejoins the client transport pipeline.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.client.reverse;

import org.jspecify.annotations.NullMarked;
