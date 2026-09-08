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
 * Builds trusted certificate paths and applies OPC UA certificate validation rules for the default
 * client and server certificate validators.
 *
 * <p>{@link org.eclipse.milo.opcua.stack.core.util.validation.CertificateValidationUtil} first
 * builds a path from the presented chain and trust-list certificates, then validates that path
 * against the supplied CRLs and validation checks. Path construction establishes trust and
 * signature chaining; the usage checker and the JDK's PKIX revocation checker enforce the remaining
 * certificate rules.
 *
 * <p><b>Revocation.</b> CRL evaluation is delegated to the JDK's PKIX revocation checker,
 * configured to use CRLs only. Milo supplies the trust list's CRLs and never uses OCSP; the checker
 * is isolated from the JVM-wide {@code ocsp.*} security properties that other components may set.
 * Before validation, CRLs are matched to signing keys in the selected path so same-named CAs can
 * coexist in a trust list. A revocation established by an applicable CRL is always enforced. {@link
 * org.eclipse.milo.opcua.stack.core.util.validation.ValidationCheck#REVOCATION_LISTS} decides what
 * happens when status cannot be established: when present, the path is rejected; when absent, the
 * unknown status is tolerated and the reason is logged. Expired, not yet valid, or wrongly signed
 * CRLs never establish a status. A directly trusted self-signed certificate has no issuer and needs
 * no CRL in either mode.
 *
 * <p>When the supplied CRLs do not cover an issuer, the JDK checker attempts to retrieve a CRL from
 * a distribution point named in the certificate before treating the status as unknown. That
 * retrieval cannot be turned off for an explicitly configured checker ({@code
 * com.sun.security.enableCRLDP} does not apply), runs on the validating thread, and waits up to
 * {@code com.sun.security.crl.timeout} plus {@code com.sun.security.crl.readtimeout} (15 seconds
 * each by default) per distribution point when the host is unreachable. Secure channel and session
 * validation run on the channel's event loop, so an unreachable distribution point stalls every
 * channel on that loop for the duration. Supplying complete CRLs through the trust list avoids the
 * retrieval entirely.
 *
 * <p><b>Validity.</b> Suppressing {@link
 * org.eclipse.milo.opcua.stack.core.util.validation.ValidationCheck#VALIDITY} affects only the
 * trust anchor, which Milo checks itself. The JDK enforces the validity period of every certificate
 * inside a path while building it, so an expired CA-issued certificate is rejected before
 * validation checks are consulted. Relaxing that would require a Milo-owned path validator, which
 * this package deliberately does not provide.
 *
 * <p>Signature verification uses Bouncy Castle for Brainpool keys that the default JCA providers
 * cannot handle. CRL selection and path validation must use compatible providers so a provider
 * limitation cannot discard revocation information.
 */
package org.eclipse.milo.opcua.stack.core.util.validation;
