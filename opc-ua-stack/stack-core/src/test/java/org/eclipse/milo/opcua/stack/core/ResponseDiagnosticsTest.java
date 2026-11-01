/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.ResponseHeader;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;

class ResponseDiagnosticsTest {

  @Test
  void serviceResolvesTheHeaderDiagnostic() {
    var serviceDiagnostics = new DiagnosticInfo(0, 1, -1, 2, null, null, null);

    ResponseDiagnostics diagnostics =
        ResponseDiagnostics.of(
            header(serviceDiagnostics, new String[] {"urn:acme:plc", "E_RUN_MODE", "RUN mode"}));

    assertEquals(
        Diagnostic.symbolic("urn:acme:plc", "E_RUN_MODE").withText("RUN mode"),
        diagnostics.service().orElseThrow());
  }

  // A conforming server sends no service diagnostics unless they were requested.
  @Test
  void serviceIsEmptyWithoutServiceDiagnostics() {
    assertTrue(ResponseDiagnostics.of(header(null, null)).service().isEmpty());
    assertTrue(
        ResponseDiagnostics.of(header(DiagnosticInfo.NULL_VALUE, null)).service().isEmpty(),
        "an entry with no fields has nothing to report");
  }

  // A bad index from a server must lose that one field, never the response or the other fields.
  @Test
  void indexOutsideTheStringTableResolvesThatFieldToNull() {
    var info =
        new DiagnosticInfo(0, 7, -1, -5, "info", new StatusCode(StatusCodes.Bad_Timeout), null);

    Diagnostic resolved =
        ResponseDiagnostics.of(header(null, new String[] {"urn:acme:plc"})).resolve(info);

    assertEquals(
        new Diagnostic(
            "urn:acme:plc", null, null, "info", new StatusCode(StatusCodes.Bad_Timeout), null),
        resolved);
  }

  @Test
  void indexesWithoutAStringTableResolveToNull() {
    var info = new DiagnosticInfo(0, 1, 2, 3, null, null, null);

    assertNull(ResponseDiagnostics.of(header(null, null)).resolve(info));
  }

  // A locale is meaningless without the text it qualifies.
  @Test
  void localeWithoutTextIsDropped() {
    var info = new DiagnosticInfo(-1, -1, 0, -1, "info", null, null);

    Diagnostic resolved = ResponseDiagnostics.of(header(null, new String[] {"en"})).resolve(info);

    assertEquals(new Diagnostic(null, null, null, "info", null, null), resolved);
  }

  @Test
  void localizedTextKeepsItsLocale() {
    var info = new DiagnosticInfo(-1, -1, 0, 1, null, null, null);

    Diagnostic resolved =
        ResponseDiagnostics.of(header(null, new String[] {"de", "Fehler"})).resolve(info);

    assertEquals(Diagnostic.text(new LocalizedText("de", "Fehler")), resolved);
  }

  @Test
  void nestedDiagnosticsResolveAgainstTheSameTable() {
    var inner = new DiagnosticInfo(-1, -1, -1, 0, null, null, null);
    var outer = new DiagnosticInfo(-1, -1, -1, 0, null, StatusCode.BAD, inner);

    Diagnostic resolved =
        ResponseDiagnostics.of(header(null, new String[] {"shared"})).resolve(outer);

    assertEquals(
        Diagnostic.text("shared").withInner(StatusCode.BAD, Diagnostic.text("shared")), resolved);
  }

  @Test
  void listResolvesOneEntryPerElementKeepingNulls() {
    var info = new DiagnosticInfo(-1, -1, -1, 0, null, null, null);

    List<@Nullable Diagnostic> resolved =
        ResponseDiagnostics.of(header(null, new String[] {"text"}))
            .resolve(new DiagnosticInfo[] {null, info});

    assertEquals(Arrays.asList(null, Diagnostic.text("text")), resolved);
  }

  @Test
  void nullOrEmptyListResolvesToEmpty() {
    ResponseDiagnostics diagnostics = ResponseDiagnostics.of(header(null, null));

    assertEquals(List.of(), diagnostics.resolve((DiagnosticInfo[]) null));
    assertEquals(List.of(), diagnostics.resolve(new DiagnosticInfo[0]));
  }

  private static ResponseHeader header(
      @Nullable DiagnosticInfo serviceDiagnostics, String @Nullable [] stringTable) {

    return new ResponseHeader(
        DateTime.now(), uint(1), StatusCode.BAD, serviceDiagnostics, stringTable, null);
  }
}
