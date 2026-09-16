/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.security;

import static java.util.Objects.requireNonNull;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default deterministic selector for local certificate identities.
 *
 * <p>Candidates are the identities of the context's candidate groups, in candidate-group order,
 * limited to those that are locally compatible with the security policy profile (see {@link
 * CertificateCompatibility#checkLocalCompatible(SecurityPolicyProfile, CertificateIdentity)}, which
 * enforces functional requirements but does not reject legacy RSA identities for omitting the
 * strict KeyUsage bit set). An explicitly configured certificate pins selection to the candidate
 * holding it; if no candidate holds that certificate, or the candidate is not compatible with the
 * requested profile, the selector returns empty so the caller can report a misconfigured
 * certificate. Without an explicit certificate, the selector prefers an exact certificate type
 * request, then the type preferred by the security policy profile, and finally the earliest
 * candidate.
 *
 * <p>An explicitly configured certificate that is excluded for incompatibility is logged at {@code
 * WARN} with the reason, so it is never silently lost. A routine non-match among the other
 * candidate identities is an expected part of successful selection and is logged at {@code DEBUG}.
 */
@NullMarked
public final class DefaultCertificateIdentitySelector implements CertificateIdentitySelector {

  private static final Logger LOGGER =
      LoggerFactory.getLogger(DefaultCertificateIdentitySelector.class);

  private DefaultCertificateIdentitySelector() {}

  /**
   * Create the default certificate identity selector.
   *
   * @return a selector using the stack default ordering rules.
   */
  public static DefaultCertificateIdentitySelector create() {
    return new DefaultCertificateIdentitySelector();
  }

  @Override
  public Optional<CertificateIdentity> select(CertificateIdentitySelectionContext context)
      throws UaException {

    requireNonNull(context, "context");

    ByteString explicitThumbprint = explicitThumbprint(context);

    List<CertificateIdentity> candidates = new ArrayList<>();

    for (CertificateGroup certificateGroup : context.candidateGroups()) {
      for (CertificateIdentity identity : certificateGroup.getCertificateIdentities()) {
        try {
          // Local selection only enforces functional requirements (certificate type, public-key
          // family/curve, ECC keyCertSign for self-signed). It does not gate legacy RSA identities
          // on the strict KeyUsage bit set, which externally provisioned certificates commonly
          // omit.
          CertificateCompatibility.checkLocalCompatible(context.securityPolicyProfile(), identity);
          candidates.add(identity);
        } catch (UaException | RuntimeException e) {
          // Never let an incompatible identity vanish without an actionable reason; an explicitly
          // configured certificate being rejected is especially worth surfacing.
          if (explicitThumbprint != null && explicitThumbprint.equals(identity.thumbprint())) {
            LOGGER.warn(
                "Explicitly configured certificate is not compatible with security policy {}: {}",
                context.securityPolicyProfile().securityPolicy().getUri(),
                e.getMessage());
          } else {
            LOGGER.debug(
                "Certificate identity is not compatible with security policy {}: {}",
                context.securityPolicyProfile().securityPolicy().getUri(),
                e.getMessage());
          }
        }
      }
    }

    if (explicitThumbprint != null) {
      return selectExplicitIdentity(context, explicitThumbprint, candidates);
    }

    // Stream.min keeps the earliest of equally ranked candidates, so candidate-group order is the
    // final tie-breaker.
    return candidates.stream().min(selectionOrder(context));
  }

  private static Optional<CertificateIdentity> selectExplicitIdentity(
      CertificateIdentitySelectionContext context,
      ByteString explicitThumbprint,
      List<CertificateIdentity> candidates)
      throws UaException {

    List<CertificateIdentity> pinned = new ArrayList<>();

    for (CertificateIdentity candidate : candidates) {
      if (explicitThumbprint.equals(candidate.thumbprint())) {
        pinned.add(candidate);
      }
    }

    return pinned.stream().min(selectionOrder(context));
  }

  private static @Nullable ByteString explicitThumbprint(
      CertificateIdentitySelectionContext context) throws UaException {

    X509Certificate explicitCertificate = context.explicitCertificate();

    return explicitCertificate != null ? CertificateUtil.thumbprint(explicitCertificate) : null;
  }

  private static Comparator<CertificateIdentity> selectionOrder(
      CertificateIdentitySelectionContext context) {

    NodeId requestedCertificateTypeId = context.certificateTypeId();
    NodeId policyPreferredCertificateTypeId =
        context.securityPolicyProfile().preferredCertificateTypeId().orElse(null);

    return Comparator.comparingInt(
            (CertificateIdentity identity) ->
                matchesCertificateType(requestedCertificateTypeId, identity) ? 0 : 1)
        .thenComparingInt(
            identity -> matchesCertificateType(policyPreferredCertificateTypeId, identity) ? 0 : 1);
  }

  private static boolean matchesCertificateType(
      @Nullable NodeId certificateTypeId, CertificateIdentity identity) {

    return certificateTypeId != null && certificateTypeId.equals(identity.certificateTypeId());
  }
}
