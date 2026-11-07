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
 * org.eclipse.milo.opcua.sdk.server.OpcUaServerConfig}. The application registers its {@link
 * org.eclipse.milo.opcua.stack.core.security.CertificateGroup}s under {@code CertificateGroupType}
 * NodeIds in the configured {@link org.eclipse.milo.opcua.stack.core.security.CertificateManager},
 * and endpoints reference registered groups by those ids. A secure endpoint may advertise a fixed
 * certificate supplied directly on the endpoint config, use {@link
 * org.eclipse.milo.opcua.sdk.server.EndpointCertificateConfig} to name the group and certificate
 * type to select a compatible local identity from, or, with neither configured, select from the
 * group registered as the DefaultApplicationGroup. An endpoint that names a group the manager does
 * not have is omitted. A SecurityPolicy.None endpoint whose UserName or IssuedToken policies
 * encrypt secrets with a legacy RSA policy selects a certificate the same way, against the token
 * policies' profiles. An implicit selection failure keeps the None endpoint advertised without a
 * certificate, while an unsatisfied explicit certificate request omits it. Other endpoints whose
 * security policy or certificate request cannot be served by the current runtime are omitted from
 * discovery advertisements.
 *
 * <h2>Lifecycle extensions</h2>
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.server.OpcUaServer#addLifecycleParticipant(
 * org.eclipse.milo.opcua.sdk.server.Lifecycle)} transfers lifecycle ownership of an application
 * component to the server before startup. The server starts these participants in registration
 * order after its standard namespaces and event facilities are available, then exposes passive and
 * reverse-connect endpoints. Startup rollback and terminal shutdown stop only successfully started
 * participants, in reverse order. On normal shutdown, transports and sessions are quiesced first;
 * participant shutdown therefore runs without external server visibility but before the standard
 * address space and event facilities are torn down. Shutdown waits for the actual startup work,
 * even if an application cancels or completes the public startup result.
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.server.LifecycleManager} applies the same ownership rule
 * within composite components: failed child startup unwinds successfully started children in
 * reverse order. A managed namespace therefore releases its base address-space registrations when a
 * later child fails. Each failing child cleans up its own partial startup before propagating the
 * failure.
 *
 * <h2>Node storage</h2>
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.server.NodeManager} owns node identity and reference
 * storage. The built-in manager serializes mutations, batch commits, and identity-conditional
 * cleanup on its monitor. It resolves node attributes and invokes reference filters outside that
 * monitor because attribute observers may call back into the manager while holding a node's
 * monitor. Application managers that use the default batch and cleanup primitives need a single
 * writer.
 *
 * <h2>Monitored-item sampling</h2>
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.server.ManagedAddressSpace} revises sampling intervals for
 * each monitored Node and Attribute through one hook shared by creation and modification. The
 * default revision matches its sampling manager's periodic intervals. A custom implementation may
 * select other intervals per item, but must also route those items to the component that delivers
 * values at the reported interval. In particular, report-by-exception items with a revised interval
 * of zero belong to a push implementation outside the sampling manager. That component also owns
 * keeping their read-access results current.
 *
 * <h2>Session response preparation</h2>
 *
 * <p>A Session owns a timeout as soon as it is constructed. Failed CreateSession preparation must
 * close that provisional Session before returning an error. ActivateSession prepares its response
 * additional header before committing identity, nonce, locale, or channel changes. A failed
 * negotiation therefore leaves an existing Session usable on its previous channel and leaves an
 * initial Session unactivated. Enhanced username-token keys consumed during validation remain
 * single-use even if later response preparation fails.
 *
 * <p>Session timeout checks move from the scheduler to the configured server executor. Closing a
 * Session cancels its timer and prevents queued checks from replacing it. Only expiration that wins
 * closure increments the timeout diagnostic; subscription cleanup and lifecycle notification run
 * once, outside the lock coordinating closure and timer replacement.
 *
 * <h2>Diagnostics returned to clients</h2>
 *
 * <p>A client asks for diagnostics with the {@code returnDiagnostics} mask of each request header.
 * The server explains a status with a resolved {@link org.eclipse.milo.opcua.stack.core.Diagnostic}
 * and never handles string-table indexes itself. Diagnostics reach the response in three ways:
 *
 * <ul>
 *   <li>Code throws a {@link org.eclipse.milo.opcua.stack.core.UaException} that carries one. The
 *       SDK records it when an attribute filter, value delegate or node fails a Read or Write in a
 *       {@link org.eclipse.milo.opcua.sdk.server.ManagedAddressSpace}, when an {@code
 *       AbstractMethodInvocationHandler} or the Method lookup fails a Call, and when a service set
 *       fails a whole request, where it becomes the ServiceFault's service diagnostic. A {@code
 *       UaException} from the client SDK carries the diagnostic of the server that sent it, so an
 *       address space that rethrows one forwards that diagnostic.
 *   <li>An {@link org.eclipse.milo.opcua.sdk.server.AddressSpace} that builds its own results
 *       reports through the {@link org.eclipse.milo.opcua.sdk.server.OperationDiagnostics} of its
 *       operation context. Composites pass the parent context's collector to every child, so all
 *       layers report into the one collector of the request. Operations are identified by the
 *       identity of the request item each address space received.
 *   <li>Call and HistoryUpdate also collect per-argument and per-value diagnostics through {@link
 *       org.eclipse.milo.opcua.sdk.server.CallOperationDiagnostics} and {@link
 *       org.eclipse.milo.opcua.sdk.server.HistoryUpdateOperationDiagnostics}.
 * </ul>
 *
 * <p>After the results are collated, on the request thread, each service set passes every collected
 * diagnostic through the configured {@link org.eclipse.milo.opcua.sdk.server.DiagnosticsPolicy} and
 * encodes the response's lists and string table with one {@link
 * org.eclipse.milo.opcua.stack.core.DiagnosticEncoder}. A request that asks for no operation-level
 * fields gets {@link org.eclipse.milo.opcua.sdk.server.OperationDiagnostics#none()} collectors and
 * a response without diagnostics or string table. Diagnostics are never sent inside notifications,
 * because after a Republish it is unclear which string table their indexes refer to.
 *
 * <p>The word "diagnostics" also names the Part 5 diagnostics model in the {@code diagnostics}
 * subpackage: Session and server counters exposed as nodes. That model is unrelated to the
 * diagnostics returned in responses.
 *
 * <h2>Reference storage</h2>
 *
 * <p>{@link org.eclipse.milo.opcua.sdk.server.AddressSpaceManager} aggregates references from every
 * registered NodeManager. Reference ownership is independent of node ownership: either endpoint's
 * manager, or another registered manager, can store an association. Removing an association through
 * the address-space manager removes both directions from all registered stores, matching the scope
 * used for discovery.
 *
 * <h2>Runtime boundaries</h2>
 *
 * <p>The SDK server package coordinates high-level server configuration and lifecycle. Certificate
 * storage and trust decisions remain owned by the stack security APIs, transport binding remains
 * owned by the transport package, and request handling is delegated to service-set implementations.
 * Code added to this package should preserve those boundaries and keep endpoint validation aligned
 * with the certificates and security policies that the transport layer can actually serve.
 *
 * <p>Sessions retain the server certificate and key pair they were created with, separately from
 * the current channel configuration. When a retained Session moves to a replacement SecureChannel,
 * application signatures still include that original certificate. Enhanced signatures also include
 * the replacement channel's certificate and thumbprint. A certificate rotation can remove
 * old-thumbprint lookup without rewriting the security information of retained Sessions.
 */
package org.eclipse.milo.opcua.sdk.server;
