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

import org.eclipse.milo.opcua.stack.core.Diagnostic;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * Decides which diagnostics a server returns to a client.
 *
 * <p>The server applies the policy to every {@link Diagnostic} it is about to return, after the
 * client's {@code returnDiagnostics} mask has asked for it and before the fields the mask did not
 * request are removed. A policy returns the diagnostic unchanged, a reduced copy, or {@code null}
 * to send nothing. Because it sees the whole diagnostic, it can reduce any field, including
 * localized text. Configure one with {@code OpcUaServerConfigBuilder.setDiagnosticsPolicy}.
 *
 * <p>OPC 10000-2, 6.19 allows security information only to security administrators, and information
 * about the structure of the server only to authenticated clients. A policy can, for example, keep
 * additional info for administrators only:
 *
 * <pre>{@code
 * DiagnosticsPolicy policy =
 *     (request, status, diagnostic) -> {
 *       Diagnostic allowed = DiagnosticsPolicy.defaults().filter(request, status, diagnostic);
 *
 *       if (allowed == null || isSecurityAdmin(request.session())) {
 *         return allowed;
 *       } else {
 *         return allowed.withAdditionalInfo(null);
 *       }
 *     };
 * }</pre>
 *
 * <p>The policy is called on the thread that assembles the response, and may be called concurrently
 * for different requests.
 */
@NullMarked
@FunctionalInterface
public interface DiagnosticsPolicy {

  /**
   * Decide what the client may see of {@code diagnostic}.
   *
   * @param request where the diagnostic is about to be sent.
   * @param status the status the diagnostic explains.
   * @param diagnostic the diagnostic that was reported.
   * @return the diagnostic to send, possibly reduced, or {@code null} to send none.
   */
  @Nullable Diagnostic filter(DiagnosticsRequest request, StatusCode status, Diagnostic diagnostic);

  /**
   * Get the default policy, which follows OPC 10000-2, 6.19.
   *
   * <p>It sends nothing to a client without an activated Session, which covers the discovery
   * services, CreateSession, ActivateSession, and requests that fail before a Session is
   * identified. It also sends nothing when the status, or an inner status of the diagnostic, is
   * security-related, because explaining why a security check failed helps whoever is probing it:
   *
   * <ul>
   *   <li>{@link StatusCodes#Bad_UserAccessDenied}, {@link StatusCodes#Bad_SecurityChecksFailed},
   *       {@link StatusCodes#Bad_RequestNotAllowed};
   *   <li>identity tokens and signatures: {@link StatusCodes#Bad_IdentityTokenInvalid}, {@link
   *       StatusCodes#Bad_IdentityTokenRejected}, {@link
   *       StatusCodes#Bad_IdentityChangeNotSupported}, {@link
   *       StatusCodes#Bad_UserSignatureInvalid}, {@link
   *       StatusCodes#Bad_ApplicationSignatureInvalid}, {@link StatusCodes#Bad_NonceInvalid},
   *       {@link StatusCodes#Bad_TicketRequired}, {@link StatusCodes#Bad_TicketInvalid};
   *   <li>security policies and secure channels: {@link StatusCodes#Bad_SecurityPolicyRejected},
   *       {@link StatusCodes#Bad_SecurityModeRejected}, {@link
   *       StatusCodes#Bad_SecurityModeInsufficient}, {@link
   *       StatusCodes#Bad_SecureChannelIdInvalid}, {@link
   *       StatusCodes#Bad_SecureChannelTokenUnknown}, {@link
   *       StatusCodes#Bad_TcpSecureChannelUnknown};
   *   <li>certificates: every {@code Bad_Certificate} status and {@link
   *       StatusCodes#Bad_NoValidCertificates}.
   * </ul>
   *
   * <p>Every other diagnostic is sent as reported.
   *
   * @return the default policy.
   */
  static DiagnosticsPolicy defaults() {
    return DefaultDiagnosticsPolicy.INSTANCE;
  }

  /**
   * Get a policy that sends no diagnostics at all.
   *
   * @return a policy that always returns {@code null}.
   */
  static DiagnosticsPolicy none() {
    return (request, status, diagnostic) -> null;
  }
}
