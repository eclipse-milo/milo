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
 * <h2>Current ECC profile families</h2>
 *
 * <p>Current ECC policies split authentication, key agreement, and chunk protection into separate
 * profile axes. The NIST P-256 profiles use ECDSA application certificates and P-256 ECDH ephemeral
 * keys. The Curve25519 profiles use Ed25519 application certificates and X25519 ephemeral keys. The
 * Brainpool P-384 profiles use Brainpool P-384r1 ECDSA certificates, Brainpool P-384r1 ECDH
 * ephemeral keys, and SHA-384-based certificate/thumbprint and HKDF behavior. All of these profiles
 * protect normal service chunks with AEAD ciphers, either AES-GCM or ChaCha20-Poly1305.
 *
 * <p>For ECC OpenSecureChannel, the OPC UA nonce fields carry ephemeral public keys instead of
 * random nonce bytes. That means endpoint advertisement, certificate selection, provider
 * resolution, nonce validation, and key derivation all need to agree on the same profile metadata.
 * Do not infer this from the policy URI string in a transport handler; use the profile model.
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
 * <p>ECC SecureChannel setup uses a small set of primitive helpers at this layer. {@link
 * org.eclipse.milo.opcua.stack.core.security.EccPublicKeyCodec} translates between OPC UA's nonce
 * field wire bytes and JCA public keys, {@link
 * org.eclipse.milo.opcua.stack.core.security.EccSignatureUtil} owns the ECC signature wire formats,
 * and {@link org.eclipse.milo.opcua.stack.core.security.EccKeyAgreementUtil} derives the
 * directional client/server key material consumed by channel-security state. {@link
 * org.eclipse.milo.opcua.stack.core.security.AeadCipherUtil} keeps AES-GCM and ChaCha20-Poly1305
 * cipher creation behind the same provider-selection boundary. {@link
 * org.eclipse.milo.opcua.stack.core.security.EccEncryptedSecret} owns the ECC user-token secret
 * payload format used above SecureChannel during session activation. {@link
 * org.eclipse.milo.opcua.stack.core.security.ChannelBoundSignatureData} owns the session signature
 * byte layout that binds CreateSession/ActivateSession to SecureChannel-enhancement context.
 * Transport handlers should go through SecureChannel strategies rather than calling those helpers
 * directly unless they are implementing a new strategy boundary.
 *
 * <p>{@link org.eclipse.milo.opcua.stack.core.security.EccUserTokenAdditionalHeader} owns the
 * generated {@code AdditionalParametersType}/{@code EphemeralKeyType} negotiation wrapper used by
 * SDK CreateSession/ActivateSession code before the opaque token secret is produced.
 *
 * <h2>Extension guidance</h2>
 *
 * <p>Add new policy metadata through {@code SecurityPolicyProfile} and {@code
 * SecurityPolicyProfiles} so endpoint selection, nonce handling, and channel security continue to
 * share one policy model. Keep certificate storage and trust decisions in the certificate manager,
 * certificate group, and validator APIs rather than duplicating those decisions in transport code.
 */
package org.eclipse.milo.opcua.stack.core.security;
