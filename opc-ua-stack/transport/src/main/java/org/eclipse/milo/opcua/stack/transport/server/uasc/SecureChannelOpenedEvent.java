/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.uasc;

/**
 * A Netty user event fired by {@link UascServerAsymmetricHandler} after a successful
 * OpenSecureChannel response is sent.
 *
 * <p>This event is used by the Reverse Connect FSM to detect when a client has opened a
 * SecureChannel on a reverse-connect socket, allowing the FSM to transition from RheSent to Active.
 *
 * @param secureChannelId the id of the opened secure channel.
 */
public record SecureChannelOpenedEvent(long secureChannelId) {}
