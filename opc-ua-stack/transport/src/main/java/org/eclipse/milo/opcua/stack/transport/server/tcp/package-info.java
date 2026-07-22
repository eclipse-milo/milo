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
 * Server-side UA-TCP transport support for passive listeners and Reverse Connect attempts.
 *
 * <p>The normal server path is listener-oriented: {@link
 * org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerTransport} binds one or more local
 * TCP sockets, accepts client-opened channels, applies listener-specific protections such as {@link
 * org.eclipse.milo.opcua.stack.transport.server.tcp.RateLimitingHandler}, and installs the server
 * UASC {@code Hello}/{@code Acknowledge} pipeline through {@link
 * org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerChannelInitializer}. After {@code
 * Hello} is accepted, control moves into the shared server UASC package, where SecureChannel and
 * service request handling are independent of how the TCP socket was created.
 *
 * <p>Reverse Connect changes only the direction of the initial TCP connection. {@link
 * org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnector} opens an outbound
 * socket to a client reverse listener, sends {@code ReverseHello(serverUri, endpointUrl)}, and then
 * installs the same server-side Hello path used by passive listener channels. The client still
 * sends {@code Hello}, opens the SecureChannel, validates server identity, and drives Session
 * creation through the normal client transport flow; {@code ReverseHello} is only an
 * unauthenticated routing and admission hint.
 *
 * <p>The package keeps the low-level socket mechanics separate from later SDK concerns. Target
 * registries, retry scheduling, public server configuration, and {@code OpcUaServer} lifecycle
 * integration belong in higher layers. The types here provide the primitives those layers can
 * schedule and observe: connection parameters, attempt futures, attempt state transitions, Netty
 * pipeline handoff, and connector-owned channel cleanup.
 *
 * <h2>Reverse-connect attempt state machine</h2>
 *
 * <p>A low-level attempt emits the non-terminal progression {@link
 * org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnectAttemptState#CONNECTING}
 * to {@link
 * org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnectAttemptState#CONNECTED}
 * to {@link
 * org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnectAttemptState#REVERSE_HELLO_SENT}
 * to {@link
 * org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnectAttemptState#HELLO_HANDLER_INSTALLED}.
 * When the client sends {@code Hello}, the attempt enters terminal {@link
 * org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnectAttemptState#HANDOFF}
 * and the channel future completes with the channel now running the normal server UASC path.
 * Terminal failure states are {@link
 * org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnectAttemptState#CLIENT_ERROR}
 * when the client listener returns {@code ERR/F}, {@link
 * org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnectAttemptState#FAILED}
 * for connect, encode, write, protocol, or premature-close failures, {@link
 * org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnectAttemptState#CANCELLED}
 * for caller cancellation, and {@link
 * org.eclipse.milo.opcua.stack.transport.server.tcp.OpcTcpServerReverseConnectAttemptState#CLOSED}
 * for connector or attempt shutdown before handoff.
 *
 * <p>Pipeline mutation and protocol I/O are performed on each Netty channel's event loop. Small
 * synchronized sections protect connector bookkeeping such as open attempts, open channels, and
 * shutdown state; observers must stay lightweight because attempt events may be emitted from an
 * event-loop thread.
 */
package org.eclipse.milo.opcua.stack.transport.server.tcp;
