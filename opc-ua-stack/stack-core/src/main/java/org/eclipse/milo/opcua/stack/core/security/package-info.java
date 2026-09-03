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
 * <h2>Current enhanced profile families</h2>
 *
 * <p>Current ECC policies split authentication, key agreement, and chunk protection into separate
 * profile axes. The NIST P-256 and Brainpool P-256 profiles use SHA-256 ECDSA application
 * certificates, 64-byte {@code x||y} ECDH ephemeral public keys, and HKDF-SHA-256. The NIST P-384
 * and Brainpool P-384 profiles use SHA-384 ECDSA application certificates, 96-byte {@code x||y}
 * ECDH ephemeral public keys, and HKDF-SHA-384. The Curve25519 profiles use Ed25519 application
 * certificates and X25519 ephemeral keys. The Curve448 profiles use Ed448 application certificates,
 * X448 ephemeral keys, and HKDF-SHA-384. NIST, Curve25519, and Curve448 profiles can resolve to
 * either the JDK or Bouncy Castle provider profile when the needed transformations are present;
 * Brainpool profiles require Bouncy Castle for portable curve support. All of these profiles
 * protect normal service chunks with AEAD ciphers, either AES-GCM or ChaCha20-Poly1305. RSA-DH
 * profiles use RSA application certificates for OpenSecureChannel authentication, RFC 7919
 * ffdhe3072 ephemeral public values in the nonce fields, HKDF-SHA-256 key derivation, and the same
 * AEAD chunk-protection boundary.
 *
 * <h2>Supported ECC policy URIs</h2>
 *
 * <p>SecurityPolicy URIs use the base form {@code
 * http://opcfoundation.org/UA/SecurityPolicy#<Name>}. The stack recognizes only the AEAD-suffixed
 * ECC policy names: each supported curve ({@code ECC_nistP256}, {@code ECC_nistP384}, {@code
 * ECC_brainpoolP256r1}, {@code ECC_brainpoolP384r1}, {@code ECC_curve25519}, {@code ECC_curve448})
 * is paired with one of two chunk-protection suffixes, {@code _AesGcm} or {@code _ChaChaPoly}. For
 * example, {@code http://opcfoundation.org/UA/SecurityPolicy#ECC_nistP256_AesGcm} resolves through
 * {@link org.eclipse.milo.opcua.stack.core.security.SecurityPolicy#fromUri(String)} and {@link
 * org.eclipse.milo.opcua.stack.core.security.SecurityPolicy#fromUriSafe(String)}, while {@code
 * http://opcfoundation.org/UA/SecurityPolicy#ECC_nistP256} does not.
 *
 * <p>The original OPC UA 1.05 suffix-less ECC URIs ({@code #ECC_nistP256}, {@code #ECC_nistP384},
 * {@code #ECC_brainpoolP256r1}, {@code #ECC_brainpoolP384r1}, {@code #ECC_curve25519}, {@code
 * #ECC_curve448}) are deliberately not exposed and remain unknown to both {@code fromUri} and
 * {@code fromUriSafe}. A peer that advertises only those suffix-less URIs will therefore find no
 * common ECC policy: such endpoints are skipped during client-side endpoint selection, and an
 * attempt to open a SecureChannel with one is rejected at the channel layer with {@code
 * Bad_SecurityPolicyRejected}. This is the same handling as any other unknown URI, so it is not a
 * behavioral regression relative to the base RSA-era policies. To negotiate ECC, both peers must
 * advertise the AEAD-suffixed URIs above.
 *
 * <p>For enhanced OpenSecureChannel policies, the OPC UA nonce fields carry ephemeral public keys
 * instead of random nonce bytes. That means endpoint advertisement, certificate selection, provider
 * resolution, nonce validation, and key derivation all need to agree on the same profile metadata.
 * Do not infer this from the policy URI string in a transport handler; use the profile model.
 *
 * <h2>Certificate and trust material</h2>
 *
 * <p>A {@link org.eclipse.milo.opcua.stack.core.security.CertificateGroup} is the unit of local
 * identity and trust. It holds the application certificates and key pairs of the certificate types
 * it supports, the {@link org.eclipse.milo.opcua.stack.core.security.TrustListManager} used to
 * validate peers, the {@link org.eclipse.milo.opcua.stack.core.security.CertificateValidator} that
 * applies that trust list, and the {@link
 * org.eclipse.milo.opcua.stack.core.security.CertificateQuarantine} holding what the validator
 * rejected. A group has no identity of its own and no factory; {@link
 * org.eclipse.milo.opcua.stack.core.security.DefaultCertificateGroup} is the store-backed
 * implementation, and {@code DefaultCertificateGroup.forIdentity} wraps a single key pair and
 * certificate chain as a group of one.
 *
 * <p>Servers and clients use groups differently. A server owns one or more groups and registers
 * each under the {@link org.eclipse.milo.opcua.stack.core.types.builtin.NodeId} of its {@code
 * CertificateGroupType} node in a {@link
 * org.eclipse.milo.opcua.stack.core.security.CertificateManager}; endpoint configuration, Push
 * management, and the address space name groups by that id, and the manager resolves ids to groups
 * and groups to ids. The manager also answers the thumbprint lookups a SecureChannel or session
 * handshake needs. Registration order is the precedence between groups. A client configures exactly
 * one group and never names it; there is no manager on the client. A client with only a key pair
 * and certificate on hand does not construct a group itself: the client config builder wraps them
 * in a group of one whose trust list and quarantine are empty, and server validation is the
 * client's configured validator.
 *
 * <p>Provisioning is a factory concern: {@link
 * org.eclipse.milo.opcua.stack.core.security.CertificateFactory#createMissingCertificates} creates
 * and installs material for every supported type a group reports absent, and never replaces an
 * entry the group cannot read. Applications whose certificates are issued externally, for example
 * by a GDS, install them through {@code CertificateGroup.updateCertificate} and never call the
 * factory.
 *
 * <p>Rejections are per group. A certificate rejected while validating against one group's trust
 * list lands only in that group's quarantine, so trusting it from another group's management
 * surface is not possible by accident. {@code CertificateManager.getRejectedCertificates()} is the
 * server-wide rejected list that {@code GetRejectedList} returns: the de-duplicated union over
 * {@code getCertificateGroups()}.
 *
 * <p>{@link org.eclipse.milo.opcua.stack.core.security.CertificateIdentity} represents one usable
 * identity of a group: its group, certificate type, key pair, and chain. {@link
 * org.eclipse.milo.opcua.stack.core.security.CertificateIdentitySelectionContext} names the
 * candidate groups, in precedence order, from which a {@link
 * org.eclipse.milo.opcua.stack.core.security.CertificateIdentitySelector} picks an identity for
 * endpoint advertisement or client connection setup; a server passes the endpoint's registered
 * group and a client passes its one group. {@link
 * org.eclipse.milo.opcua.stack.core.security.CertificateCompatibility} contains the
 * profile-specific certificate type, public key, and key-usage checks used before an identity is
 * advertised or selected for a secured connection, and the certificate type inference used by the
 * group-of-one helper.
 *
 * <p>Operations that consume multiple trust lists should capture one {@link
 * org.eclipse.milo.opcua.stack.core.security.TrustListSnapshot}, and operations that read and then
 * change trust lists should do so through {@code TrustListManager.update}. Built-in trust list
 * managers capture snapshots and apply updates atomically; custom managers should override those
 * two methods when they need the same guarantee.
 *
 * <p>{@link org.eclipse.milo.opcua.stack.core.security.KeyStoreCertificateStore} persists local
 * identities with a read-modify-replace boundary. Each mutation incorporates KeyStore entries
 * committed before its disk read, including application-owned aliases, then atomically replaces the
 * file. Separate store instances and processes do not share a write lock, so applications must
 * coordinate writes that overlap.
 *
 * <p>{@link org.eclipse.milo.opcua.stack.core.security.DefaultCertificateManager} provides a
 * thread-safe mutable group registry. Applications own the groups and their backing stores,
 * trust-list managers, and quarantines. Removing or replacing a group returns it to the application
 * without closing its resources, so the application must coordinate final closure with any lookups
 * already in progress.
 *
 * <h2>Runtime boundaries</h2>
 *
 * <p>Endpoint advertisement and SecureChannel creation should combine certificate availability,
 * provider capability, and policy profile support before advertising or using a secured endpoint.
 * {@link org.eclipse.milo.opcua.stack.core.security.SecurityProviderResolver} owns provider-profile
 * selection so policy metadata does not depend on JVM provider order. The stack can recognize a
 * policy even when the current runtime cannot execute the corresponding SecureChannel strategy.
 *
 * <p>Enhanced SecureChannel setup uses a small set of primitive helpers at this layer. {@link
 * org.eclipse.milo.opcua.stack.core.security.EccPublicKeyCodec} translates between OPC UA's ECC
 * nonce-field wire bytes and JCA public keys, {@link
 * org.eclipse.milo.opcua.stack.core.security.FiniteFieldDhKeyAgreementUtil} owns RSA-DH ffdhe3072
 * key generation, public-value validation, and agreement, {@link
 * org.eclipse.milo.opcua.stack.core.security.EccSignatureUtil} owns the ECC signature wire formats,
 * and {@link org.eclipse.milo.opcua.stack.core.security.EccKeyAgreementUtil} derives the
 * directional client/server key material consumed by channel-security state. {@link
 * org.eclipse.milo.opcua.stack.core.security.AeadCipherUtil} keeps AES-GCM and ChaCha20-Poly1305
 * cipher creation behind the same provider-selection boundary. {@link
 * org.eclipse.milo.opcua.stack.core.security.EccEncryptedSecret} owns the enhanced user-token
 * secret payload format used above SecureChannel during session activation. {@link
 * org.eclipse.milo.opcua.stack.core.security.ChannelBoundSignatureData} owns the session signature
 * byte layout that binds CreateSession/ActivateSession to SecureChannel-enhancement context.
 * Transport handlers should go through SecureChannel strategies rather than calling those helpers
 * directly unless they are implementing a new strategy boundary.
 *
 * <p>{@link org.eclipse.milo.opcua.stack.core.security.EnhancedUserTokenAdditionalHeader} owns the
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
