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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.util.CertificateUtil;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Default deterministic selector for local certificate identities.
 *
 * <p>The selector limits candidates to the requested certificate group when one is provided, then
 * prefers an explicitly configured certificate when it is present in the manager, an exact
 * certificate type request, the type preferred by the security policy profile, and finally stable
 * certificate group/type ordering.
 */
@NullMarked
public final class DefaultCertificateIdentitySelector implements CertificateIdentitySelector {

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

    List<CertificateIdentity> candidates =
        context.certificateManager().getCertificateIdentities().stream()
            .filter(identity -> matchesCertificateGroup(context.certificateGroupId(), identity))
            .filter(
                identity ->
                    CertificateCompatibility.isCompatible(
                        context.securityPolicyProfile(), identity))
            .toList();

    if (candidates.isEmpty()) {
      return Optional.empty();
    }

    Optional<CertificateIdentity> explicitIdentity = selectExplicitIdentity(context, candidates);

    if (explicitIdentity.isPresent()) {
      return explicitIdentity;
    }

    return candidates.stream().min(selectionOrder(context));
  }

  private static Optional<CertificateIdentity> selectExplicitIdentity(
      CertificateIdentitySelectionContext context, List<CertificateIdentity> candidates)
      throws UaException {

    X509Certificate explicitCertificate = context.explicitCertificate();

    if (explicitCertificate == null) {
      return Optional.empty();
    }

    ByteString explicitThumbprint = CertificateUtil.thumbprint(explicitCertificate);
    CertificateIdentity selected = null;
    Comparator<CertificateIdentity> order = selectionOrder(context);

    for (CertificateIdentity candidate : candidates) {
      if (explicitThumbprint.equals(candidate.thumbprint())
          && (selected == null || order.compare(candidate, selected) < 0)) {
        selected = candidate;
      }
    }

    return Optional.ofNullable(selected);
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
            identity -> matchesCertificateType(policyPreferredCertificateTypeId, identity) ? 0 : 1)
        .thenComparing(CertificateIdentityOrdering.STABLE);
  }

  private static boolean matchesCertificateGroup(
      @Nullable NodeId certificateGroupId, CertificateIdentity identity) {

    return certificateGroupId == null || certificateGroupId.equals(identity.certificateGroupId());
  }

  private static boolean matchesCertificateType(
      @Nullable NodeId certificateTypeId, CertificateIdentity identity) {

    return certificateTypeId != null && certificateTypeId.equals(identity.certificateTypeId());
  }
}
