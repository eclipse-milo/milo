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
 * Establishes server SecureChannels and translates UASC chunks into service requests and responses.
 * The asymmetric handler validates OpenSecureChannel messages and installs the symmetric handler
 * after a token is established. Both handlers share the connection's chunk encoder and decoder, so
 * asymmetric renewals and symmetric service messages use the same directional sequence streams.
 *
 * <p>Handlers retain partial-message buffers until a final or Abort chunk completes the message.
 * Both terminal chunk types pass through the core decoder, which authenticates the accumulated
 * chunks and advances sequence state. A valid Abort discards the message while leaving the channel
 * open; malformed or unauthenticated chunks follow the decoding-error path.
 *
 * <p>Buffer ownership stays on the channel's event loop. Accumulation transfers retained buffers to
 * the decoder for message completion, and disconnect, handler removal, and exception cleanup
 * release buffers still owned by the handler. Service dispatch begins only after successful
 * decoding and uses the executor supplied by the server transport configuration.
 */
package org.eclipse.milo.opcua.stack.transport.server.uasc;
