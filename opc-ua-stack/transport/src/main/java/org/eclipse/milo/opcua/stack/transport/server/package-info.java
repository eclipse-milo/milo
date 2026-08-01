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
 * Server-side transport SPI: the boundary between transport implementations and the server
 * application that consumes their service requests.
 *
 * <p>A transport implementation ({@link
 * org.eclipse.milo.opcua.stack.transport.server.OpcServerTransport}, created by {@link
 * org.eclipse.milo.opcua.stack.transport.server.OpcServerTransportFactory}) accepts connections and
 * delivers each decoded request to the application through {@link
 * org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext#handleServiceRequest}. The
 * application supplies everything security-relevant the transport needs: the advertised {@link
 * org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription}s, the {@link
 * org.eclipse.milo.opcua.stack.core.security.CertificateManager}, and the encoding context.
 *
 * <h2>Endpoint selection and propagation</h2>
 *
 * <p>OPC UA does not transmit an EndpointDescription identifier during OpenSecureChannel, so the
 * endpoint a SecureChannel belongs to must be derived from wire-observable inputs, captured by
 * {@link org.eclipse.milo.opcua.stack.transport.server.EndpointSelectionKey}. During the initial
 * OpenSecureChannel the transport resolves its key through {@link
 * org.eclipse.milo.opcua.stack.transport.server.ServerApplicationContext#selectEndpoint}, which
 * yields exactly one endpoint or none -- never an ordering-dependent choice among several. The
 * selected endpoint then accompanies every inbound request via {@link
 * org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext#getEndpoint}, so the
 * application layers (Session creation and activation in particular) consume the channel's
 * selection instead of re-deriving it.
 *
 * <p>An unsecured (SecurityPolicy.None) channel that matches no explicit None endpoint carries no
 * endpoint selection at all: it is a discovery-only channel, on which the application may still
 * choose to serve Discovery services but must not create Sessions.
 *
 * <p>{@link org.eclipse.milo.opcua.stack.transport.server.ServiceRequest} is the concrete carrier
 * of a request and its transport details; {@link
 * org.eclipse.milo.opcua.stack.transport.server.ServiceRequestContext} is the read-only view handed
 * to the application.
 */
package org.eclipse.milo.opcua.stack.transport.server;
