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
 * signature chaining; the usage and revocation checkers enforce the remaining certificate rules.
 * CRLs are matched to signing keys in the selected path so same-named CAs can coexist in a trust
 * list.
 *
 * <p>Signature verification uses Bouncy Castle for Brainpool keys that the default JCA providers
 * cannot handle. CRL selection and path validation must use compatible providers so a provider
 * limitation cannot discard revocation information. The validation checks distinguish enforcing
 * revocation from requiring available CRLs; when CRLs are optional, a missing CRL can be tolerated
 * while a revoked certificate is still rejected.
 */
package org.eclipse.milo.opcua.stack.core.util.validation;
