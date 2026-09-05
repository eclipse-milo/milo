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
 * Establishes and renews client SecureChannels and carries service messages over UASC. The client
 * message handler owns its connection's chunk encoder, decoder, and partial-response buffers.
 * OpenSecureChannel and symmetric service traffic share the directional sequence stream across
 * token renewals.
 *
 * <p>Intermediate response chunks remain retained until a final or Abort chunk arrives. The core
 * decoder then validates security and sequence state and takes ownership of those buffers. An
 * authenticated Abort fails the affected request without invalidating the channel's sequence
 * stream. An Abort may follow the maximum allowed number of intermediate chunks; its size and
 * security checks still apply, and the decoder releases the accumulated message. Disconnect,
 * handler removal, and exception cleanup release any partial message that can no longer complete.
 *
 * <p>Channel state and buffer ownership follow the Netty event loop. The transport receives decoded
 * responses and owns their request-future completion; this package handles framing, cryptographic
 * state, handshake completion, and renewal scheduling.
 */
package org.eclipse.milo.opcua.stack.transport.client.uasc;
