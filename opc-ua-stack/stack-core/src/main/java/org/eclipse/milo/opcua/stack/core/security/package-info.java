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
 * Security policy, certificate, trust-list, and certificate validation APIs for OPC UA clients and
 * servers.
 *
 * <h2>Security policies</h2>
 *
 * <p>{@link org.eclipse.milo.opcua.stack.core.security.SecurityPolicy} is the public identifier for
 * OPC UA SecurityPolicy URIs. Policy behavior is described by {@link
 * org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfile}, which exposes the
 * authentication, key-agreement, and chunk-protection metadata that SecureChannel and endpoint
 * selection code need. Callers that need policy details should resolve a profile through {@link
 * org.eclipse.milo.opcua.stack.core.security.SecurityPolicyProfiles} instead of hard-coding policy
 * switches.
 *
 * <h2>Certificate and trust material</h2>
 *
 * <p>Certificate managers and certificate groups remain the source of local certificate identity
 * and trust material. Certificate validators enforce trust-list and certificate-chain decisions at
 * connection boundaries; callers should keep certificate lookup, trust configuration, and policy
 * selection coordinated through these APIs. {@link
 * org.eclipse.milo.opcua.stack.core.security.CertificateIdentity} represents a concrete local
 * identity selected from those sources for endpoint advertisement or SecureChannel setup. {@link
 * org.eclipse.milo.opcua.stack.core.security.CertificateCompatibility} contains the
 * profile-specific certificate type, public key, and key-usage checks used before an identity is
 * advertised or selected for a secured connection.
 *
 * <h2>Runtime boundaries</h2>
 *
 * <p>Endpoint advertisement and SecureChannel creation should combine certificate availability,
 * provider capability, and policy profile support before advertising or using a secured endpoint.
 * {@link org.eclipse.milo.opcua.stack.core.security.SecurityProviderResolver} owns provider-profile
 * selection so policy metadata does not depend on JVM provider order. The stack can recognize a
 * policy even when the current runtime cannot execute the corresponding SecureChannel strategy.
 *
 * <h2>Extension guidance</h2>
 *
 * <p>Add new policy metadata through {@code SecurityPolicyProfile} and {@code
 * SecurityPolicyProfiles} so endpoint selection, nonce handling, and channel security continue to
 * share one policy model. Keep certificate storage and trust decisions in the certificate manager,
 * certificate group, and validator APIs rather than duplicating those decisions in transport code.
 */
package org.eclipse.milo.opcua.stack.core.security;
