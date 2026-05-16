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
 * Server-side SDK entry points and configuration for hosting OPC UA services.
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.server.OpcUaServer} owns the server lifecycle. It binds
 * configured {@link org.eclipse.milo.opcua.sdk.server.EndpointConfig endpoints} to transport
 * implementations, exposes the service sets used by clients, and provides the transport layer with
 * access to encoding, certificate, session, and namespace state through its application context.
 *
 * <h2>Endpoint advertisement</h2>
 *
 * <p>Endpoint advertisement starts from the {@link
 * org.eclipse.milo.opcua.sdk.server.EndpointConfig} instances in {@link
 * org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig}. A secure endpoint may either advertise a
 * fixed certificate supplied directly on the endpoint config, or it may use {@link
 * org.eclipse.milo.opcua.sdk.server.EndpointCertificateConfig} to select a compatible local
 * identity from the configured {@link
 * org.eclipse.milo.opcua.stack.core.security.CertificateManager}. Endpoints whose security policy
 * or certificate request cannot be served by the current runtime are omitted from discovery
 * advertisements.
 *
 * <h2>Runtime boundaries</h2>
 *
 * <p>The SDK server package coordinates high-level server configuration and lifecycle. Certificate
 * storage and trust decisions remain owned by the stack security APIs, transport binding remains
 * owned by the transport package, and request handling is delegated to service-set implementations.
 * Code added to this package should preserve those boundaries and keep endpoint validation aligned
 * with the certificates and security policies that the transport layer can actually serve.
 */
package org.eclipse.milo.opcua.sdk.server;
