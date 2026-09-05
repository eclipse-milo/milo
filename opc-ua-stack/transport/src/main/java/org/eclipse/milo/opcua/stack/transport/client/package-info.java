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
 * Client-side transport layer: connection-oriented transports that carry OPC UA request and
 * response messages between the SDK client and a server.
 *
 * <p>{@link org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport} is the entry point:
 * callers connect, disconnect, and exchange messages through {@code sendRequestMessage}. {@link
 * org.eclipse.milo.opcua.stack.transport.client.AbstractUascClientTransport} implements the request
 * bookkeeping shared by every UASC (binary protocol) transport; concrete transports (opc.tcp in the
 * {@code tcp} subpackage, WebSocket and reverse-connect variants in other modules) supply the
 * channel by implementing {@code getChannel()}.
 *
 * <h2>Channel lifecycle</h2>
 *
 * <p>A transport outlives the Netty channels it uses. {@code getChannel()} may complete later
 * rather than immediately: a reconnecting transport is between channels, and a reverse-connect
 * transport waits for the server to dial in. A request submitted during such a gap parks until the
 * next channel arrives, so the death of one channel must not indiscriminately fail requests that
 * are parked for its replacement. Channel failure is therefore scoped: an Error message or {@code
 * channelInactive} on a channel fails the requests that channel was carrying, and parked requests
 * survive. The ordering that makes this safe: Netty fires {@code channelInactive} from a task
 * queued after the channel's {@code closeFuture} listeners have run, and a reverse-connect
 * transport rearms onto its next connection from one of those listeners, so requests parked for the
 * replacement channel can already exist when the dead channel's requests are failed.
 *
 * <h2>Request lifecycle</h2>
 *
 * <p>Every pending request must eventually terminate; indefinite hangs are never acceptable. Each
 * request reaches exactly one terminal state through one of these paths: a response arrives; its
 * write fails; the channel that carried it fails; its request timeout (from the header's timeout
 * hint) elapses; or channel acquisition fails. Two rules close the remaining gaps: a parked request
 * may only survive a channel failure if it has a timeout hint (otherwise nothing else could ever
 * complete it, and it fails with the channel), and a subclass's {@code getChannel()} future must be
 * completed exceptionally when the transport reaches a terminal or disconnected state rather than
 * left forever pending.
 *
 * <h2>Threading</h2>
 *
 * <p>Request futures normally complete on the transport config's executor. If that executor rejects
 * work, completion runs on the submitting thread so requests do not remain pending after their
 * response or failure has been consumed. Caller continuations should return promptly because this
 * fallback may run on an I/O or timer thread. Publish responses pass through a dedicated {@code
 * ExecutionQueue} that preserves their order across normal dispatch and rejection fallback.
 */
package org.eclipse.milo.opcua.stack.transport.client;
