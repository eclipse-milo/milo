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
 * <p>See OPC UA (v1.03) Part 4, Section 6.1.3.
 */
public enum ValidationCheck {

  /**
   * Check that the end-entity certificate contains a particular hostname or IP address in its SANs.
   *
   * <p>This check is only done in clients, against the server they are connecting to.
   */
  HOSTNAME,

  /** Certificate validity and expiration is checked. */
  VALIDITY,

  /** The KeyUsage extension must be present and checked for end-entity certificates. */
  KEY_USAGE_END_ENTITY,

  /** The ExtendedKeyUsage extension must be present and checked for end-entity certificates. */
  EXTENDED_KEY_USAGE_END_ENTITY,

  /** Revocation checking must happen. */
  REVOCATION,

  /** CRLs for every non-end-entity CA must be found. */
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

  /** A set the includes all {@link ValidationCheck}s. */
  public static final Set<ValidationCheck> ALL_OPTIONAL_CHECKS =
      Set.copyOf(EnumSet.allOf(ValidationCheck.class));
}
