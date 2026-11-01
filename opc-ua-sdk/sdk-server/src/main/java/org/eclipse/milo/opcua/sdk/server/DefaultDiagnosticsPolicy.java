/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import java.util.Set;
import org.eclipse.milo.opcua.stack.core.Diagnostic;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/** The policy returned by {@link DiagnosticsPolicy#defaults()}. */
@NullMarked
final class DefaultDiagnosticsPolicy implements DiagnosticsPolicy {

  static final DefaultDiagnosticsPolicy INSTANCE = new DefaultDiagnosticsPolicy();

  /** The code bits of the security-related statuses, without info bits. */
  private static final Set<Long> SECURITY_RELATED =
      Set.of(
          StatusCodes.Bad_UserAccessDenied,
          StatusCodes.Bad_SecurityChecksFailed,
          StatusCodes.Bad_RequestNotAllowed,
          StatusCodes.Bad_IdentityTokenInvalid,
          StatusCodes.Bad_IdentityTokenRejected,
          StatusCodes.Bad_IdentityChangeNotSupported,
          StatusCodes.Bad_UserSignatureInvalid,
          StatusCodes.Bad_ApplicationSignatureInvalid,
          StatusCodes.Bad_NonceInvalid,
          StatusCodes.Bad_TicketRequired,
          StatusCodes.Bad_TicketInvalid,
          StatusCodes.Bad_SecurityPolicyRejected,
          StatusCodes.Bad_SecurityModeRejected,
          StatusCodes.Bad_SecurityModeInsufficient,
          StatusCodes.Bad_SecureChannelIdInvalid,
          StatusCodes.Bad_SecureChannelTokenUnknown,
          StatusCodes.Bad_TcpSecureChannelUnknown,
          StatusCodes.Bad_NoValidCertificates,
          StatusCodes.Bad_CertificateInvalid,
          StatusCodes.Bad_CertificatePolicyCheckFailed,
          StatusCodes.Bad_CertificateTimeInvalid,
          StatusCodes.Bad_CertificateIssuerTimeInvalid,
          StatusCodes.Bad_CertificateHostNameInvalid,
          StatusCodes.Bad_CertificateUriInvalid,
          StatusCodes.Bad_CertificateUseNotAllowed,
          StatusCodes.Bad_CertificateIssuerUseNotAllowed,
          StatusCodes.Bad_CertificateUntrusted,
          StatusCodes.Bad_CertificateRevocationUnknown,
          StatusCodes.Bad_CertificateIssuerRevocationUnknown,
          StatusCodes.Bad_CertificateRevoked,
          StatusCodes.Bad_CertificateIssuerRevoked,
          StatusCodes.Bad_CertificateChainIncomplete);

  private DefaultDiagnosticsPolicy() {}

  @Override
  public @Nullable Diagnostic filter(
      DiagnosticsRequest request, StatusCode status, Diagnostic diagnostic) {

    if (request.session() == null) {
      return null;
    }

    if (isSecurityRelated(status)) {
      return null;
    }

    for (Diagnostic d = diagnostic; d != null; d = d.innerDiagnostic()) {
      StatusCode innerStatusCode = d.innerStatusCode();

      if (innerStatusCode != null && isSecurityRelated(innerStatusCode)) {
        return null;
      }
    }

    return diagnostic;
  }

  private static boolean isSecurityRelated(StatusCode status) {
    return SECURITY_RELATED.contains(status.getValue() & 0xFFFF0000L);
  }
}
