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

import java.security.cert.X509Certificate;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Inputs used when choosing a local certificate identity for a policy-sensitive operation.
 *
 * <p>The {@link Purpose} identifies the runtime boundary that requested the identity. Endpoint
 * advertisement selects the local server certificate to publish in an endpoint description, while
 * client connection setup selects the local client certificate to use after an endpoint has been
 * chosen.
 *
 * <p>{@code candidateGroups} lists the groups an identity may be selected from, in precedence
 * order: when several groups hold an equally suitable identity, the earlier group wins.
 *
 * @param purpose the operation that needs an identity.
 * @param candidateGroups the certificate groups to select from, in precedence order.
 * @param securityPolicyProfile the security-policy profile that will use the identity.
 * @param certificateTypeId the preferred certificate type, or {@code null} if the policy should
 *     decide.
 * @param explicitCertificate an explicitly configured certificate that pins selection to the
 *     candidate identity holding it, or {@code null}. Selectors return empty when the pinned
 *     certificate is held by no candidate group so callers can report the misconfiguration.
 */
@NullMarked
public record CertificateIdentitySelectionContext(
    Purpose purpose,
    List<CertificateGroup> candidateGroups,
    SecurityPolicyProfile securityPolicyProfile,
    @Nullable NodeId certificateTypeId,
    @Nullable X509Certificate explicitCertificate) {

  public CertificateIdentitySelectionContext {
    candidateGroups = List.copyOf(candidateGroups);
  }

  /**
   * Create a context for server endpoint advertisement.
   *
   * <p>The selected identity is expected to provide the certificate bytes advertised in the
   * endpoint description and the matching key material used later if a client opens a SecureChannel
   * to that endpoint.
   *
   * @param candidateGroups the certificate groups to select from, in precedence order.
   * @param securityPolicyProfile the endpoint security-policy profile.
   * @param certificateTypeId the preferred certificate type, or {@code null}.
   * @param explicitCertificate the explicitly configured endpoint certificate, or {@code null}.
   * @return a selection context for endpoint advertisement.
   */
  public static CertificateIdentitySelectionContext forEndpointAdvertisement(
      List<CertificateGroup> candidateGroups,
      SecurityPolicyProfile securityPolicyProfile,
      @Nullable NodeId certificateTypeId,
      @Nullable X509Certificate explicitCertificate) {

    return new CertificateIdentitySelectionContext(
        Purpose.ENDPOINT_ADVERTISEMENT,
        candidateGroups,
        securityPolicyProfile,
        certificateTypeId,
        explicitCertificate);
  }

  /**
   * Create a context for client connection setup.
   *
   * <p>The selected identity is expected to provide the local client application certificate and
   * key material used when opening a SecureChannel and creating a session for the chosen endpoint.
   *
   * @param candidateGroups the certificate groups to select from, in precedence order.
   * @param securityPolicyProfile the selected endpoint security-policy profile.
   * @param certificateTypeId the preferred certificate type, or {@code null}.
   * @return a selection context for client connection setup.
   */
  public static CertificateIdentitySelectionContext forClientConnectionSetup(
      List<CertificateGroup> candidateGroups,
      SecurityPolicyProfile securityPolicyProfile,
      @Nullable NodeId certificateTypeId) {

    return new CertificateIdentitySelectionContext(
        Purpose.CLIENT_CONNECTION_SETUP,
        candidateGroups,
        securityPolicyProfile,
        certificateTypeId,
        null);
  }

  /**
   * The runtime boundary that needs a selected certificate identity.
   *
   * <p>The purpose is caller context for selectors and diagnostics. It does not replace policy,
   * candidate group, or certificate type inputs; those fields still describe the identity
   * constraints and preferences.
   */
  public enum Purpose {
    /**
     * Selects a server application identity for endpoint advertisement.
     *
     * <p>This purpose is used before an endpoint is published. The selected certificate becomes the
     * endpoint's advertised server certificate, so later SecureChannel setup can resolve the same
     * local key material by receiver thumbprint.
     */
    ENDPOINT_ADVERTISEMENT,

    /**
     * Selects a client application identity for a chosen endpoint.
     *
     * <p>This purpose is used after endpoint selection and before connection setup needs local
     * certificate material. The selected identity should be compatible with the endpoint's security
     * policy, so the client does not blindly reuse an unrelated legacy certificate.
     */
    CLIENT_CONNECTION_SETUP
  }
}
