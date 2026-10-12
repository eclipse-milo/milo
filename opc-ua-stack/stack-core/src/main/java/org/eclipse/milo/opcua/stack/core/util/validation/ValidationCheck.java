/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util.validation;

import java.util.EnumSet;
import java.util.Set;

/**
 * Validation checks that are allowed to be suppressed (i.e. they are optional) according to the
 * spec.
 *
 * <p>See OPC UA (v1.05) Part 4, Section 6.1.3.
 */
public enum ValidationCheck {

  /**
   * Check that the end-entity certificate contains a particular hostname or IP address in its SANs.
   *
   * <p>This check is only done in clients, against the server they are connecting to.
   */
  HOSTNAME,

  /**
   * The validity period of the trust anchor is checked.
   *
   * <p>The trust anchor is a trusted self-signed application certificate, or the root CA of a
   * trusted path. Certificates issued by a CA are outside this check's control: the PKIX path
   * builder enforces their validity period unconditionally, and an expired one fails path
   * construction with {@code Bad_CertificateTimeInvalid} (or {@code
   * Bad_CertificateIssuerTimeInvalid} for an issuer) whether or not this check is present.
   */
  VALIDITY,

  /** The KeyUsage extension must be present and checked for end-entity certificates. */
  KEY_USAGE_END_ENTITY,

  /** The ExtendedKeyUsage extension must be present and checked for end-entity certificates. */
  EXTENDED_KEY_USAGE_END_ENTITY,

  /**
   * Formerly controlled whether a certificate listed as revoked by an available CRL was rejected.
   *
   * <p>A revocation established by an applicable CRL is now always enforced, so this check has no
   * effect. Use {@link #REVOCATION_LISTS} to additionally require that revocation status can be
   * established for every certificate in the path.
   *
   * @deprecated since 1.2.0; revocation is always enforced. This constant will be removed.
   */
  @Deprecated(since = "1.2.0", forRemoval = true)
  REVOCATION,

  /**
   * Revocation status must be established for every CA-issued certificate in the path.
   *
   * <p>When present, validation fails with {@code Bad_CertificateRevocationUnknown} or {@code
   * Bad_CertificateIssuerRevocationUnknown} if no usable CRL is available for an issuer in the
   * path. When absent, an unknown revocation status is tolerated and logged; a revocation
   * established by an available CRL is still enforced. A directly trusted self-signed certificate
   * has no issuer and needs no CRL in either case.
   */
  REVOCATION_LISTS,

  /**
   * Check that a remote application's end-entity certificate has a SubjectAlternativeName URI that
   * matches the application URI in its ApplicationDescription.
   *
   * <p>This check is not technically optional or suppressible according to the spec, but because
   * Java's certificate parsing routine turns invalid URIs into a null/empty String this check fails
   * more often than it should, since many applications will use whitespace in their URI without
   * properly URL-encoding it.
   */
  APPLICATION_URI;

  /**
   * A set that includes none of the optional {@link ValidationCheck}s.
   *
   * <p>This set *does* still include {@link #APPLICATION_URI}, which is technically not optional.
   */
  public static final Set<ValidationCheck> NO_OPTIONAL_CHECKS = Set.of(APPLICATION_URI);

  /**
   * A set that includes all {@link ValidationCheck}s.
   *
   * <p>This set includes {@link #REVOCATION_LISTS}, so revocation status must be established for
   * every CA-issued certificate in the path and a missing or unusable CRL fails validation.
   */
  public static final Set<ValidationCheck> ALL_OPTIONAL_CHECKS =
      Set.copyOf(EnumSet.allOf(ValidationCheck.class));
}
