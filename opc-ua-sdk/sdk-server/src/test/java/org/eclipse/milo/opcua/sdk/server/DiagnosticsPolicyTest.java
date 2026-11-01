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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

import java.util.stream.LongStream;
import org.eclipse.milo.opcua.sdk.server.servicesets.Service;
import org.eclipse.milo.opcua.stack.core.Diagnostic;
import org.eclipse.milo.opcua.stack.core.ReturnDiagnostics.Level;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

class DiagnosticsPolicyTest {

  private static final Diagnostic DIAGNOSTIC =
      Diagnostic.symbolic("urn:acme:plc", "E_RUN_MODE").withText("Blocked in RUN mode");

  private static final StatusCode NOT_WRITABLE = new StatusCode(StatusCodes.Bad_NotWritable);

  private final Session session = mock(Session.class);

  @Test
  void defaultsSendsDiagnosticsInAnActivatedSession() {
    Diagnostic sent =
        DiagnosticsPolicy.defaults().filter(request(session), NOT_WRITABLE, DIAGNOSTIC);

    assertEquals(DIAGNOSTIC, sent);
  }

  // Part 2 §6.19: diagnostics that describe the structure of the server are for authenticated
  // clients only. Without an activated Session the client is not authenticated.
  @Test
  void defaultsSendsNothingWithoutAnActivatedSession() {
    assertNull(DiagnosticsPolicy.defaults().filter(request(null), NOT_WRITABLE, DIAGNOSTIC));
  }

  static LongStream securityStatuses() {
    return LongStream.of(
        StatusCodes.Bad_UserAccessDenied,
        StatusCodes.Bad_SecurityChecksFailed,
        StatusCodes.Bad_IdentityTokenInvalid,
        StatusCodes.Bad_IdentityTokenRejected,
        StatusCodes.Bad_UserSignatureInvalid,
        StatusCodes.Bad_SecurityPolicyRejected,
        StatusCodes.Bad_SecureChannelIdInvalid,
        StatusCodes.Bad_CertificateUntrusted,
        StatusCodes.Bad_CertificateRevoked,
        StatusCodes.Bad_NoValidCertificates);
  }

  // Part 4 §7.12: "The DiagnosticInfo shall not contain any security related information."
  // Explaining a failed security check helps whoever is probing it.
  @ParameterizedTest
  @MethodSource("securityStatuses")
  void defaultsSendsNothingForASecurityStatus(long status) {
    assertNull(
        DiagnosticsPolicy.defaults().filter(request(session), new StatusCode(status), DIAGNOSTIC));
  }

  // Info bits such as an overflow flag do not change what a status means.
  @Test
  void defaultsIgnoresInfoBitsWhenMatchingSecurityStatuses() {
    var withInfoBits = new StatusCode(StatusCodes.Bad_UserAccessDenied | 0x0480L);

    assertNull(DiagnosticsPolicy.defaults().filter(request(session), withInfoBits, DIAGNOSTIC));
  }

  // A security failure in an underlying system is still a security failure.
  @Test
  void defaultsSendsNothingWhenAnInnerStatusIsSecurityRelated() {
    Diagnostic wrapped =
        Diagnostic.text("upstream rejected the write")
            .withInner(
                new StatusCode(StatusCodes.Bad_InternalError),
                Diagnostic.text("upstream detail")
                    .withInner(new StatusCode(StatusCodes.Bad_CertificateInvalid), null));

    assertNull(DiagnosticsPolicy.defaults().filter(request(session), NOT_WRITABLE, wrapped));
  }

  @Test
  void noneSendsNothing() {
    assertNull(DiagnosticsPolicy.none().filter(request(session), NOT_WRITABLE, DIAGNOSTIC));
  }

  private static DiagnosticsRequest request(Session session) {
    return new DiagnosticsRequest(session, Service.ATTRIBUTE_WRITE, Level.OPERATION);
  }
}
