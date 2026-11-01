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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.junit.jupiter.api.Test;

class DiagnosticTest {

  // Part 4 §7.12: "The namespaceUri shall not be the standard OPC UA namespace. There are no
  // symbolicIds provided for standard StatusCodes."
  @Test
  void symbolicRejectsTheOpcUaNamespace() {
    assertThrows(
        IllegalArgumentException.class,
        () -> Diagnostic.symbolic("http://opcfoundation.org/UA/", "E_RUN_MODE"));

    assertDoesNotThrow(
        () -> Diagnostic.symbolic("urn:acme:plc", "E_RUN_MODE"), "control: a vendor namespace");
  }

  // Part 4 §7.12: the symbolicId is defined within the context of a namespace.
  @Test
  void symbolicRejectsAnEmptyNamespace() {
    assertThrows(IllegalArgumentException.class, () -> Diagnostic.symbolic("", "E_RUN_MODE"));
  }

  // Part 4 §7.12: "The maximum length of this string is 32 characters." Characters, not bytes:
  // 32 two-byte characters are allowed.
  @Test
  void symbolicAcceptsThirtyTwoCharactersAndRejectsThirtyThree() {
    String thirtyTwo = "é".repeat(32);

    assertDoesNotThrow(() -> Diagnostic.symbolic("urn:acme:plc", thirtyTwo));
    assertThrows(
        IllegalArgumentException.class, () -> Diagnostic.symbolic("urn:acme:plc", thirtyTwo + "x"));
  }

  @Test
  void symbolicRejectsAnEmptySymbolicId() {
    assertThrows(IllegalArgumentException.class, () -> Diagnostic.symbolic("urn:acme:plc", ""));
  }

  // A locale-less text lets the client apply its own locale handling; Diagnostic.text(String)
  // must not silently claim English the way new LocalizedText(String) does.
  @Test
  void textFromStringHasNoLocale() {
    Diagnostic diagnostic = Diagnostic.text("Speed must not exceed 3000 rpm");

    assertEquals(
        new LocalizedText(null, "Speed must not exceed 3000 rpm"), diagnostic.localizedText());
    assertNull(diagnostic.symbolicId());
  }

  // Diagnostics decoded from another server may break the factory rules, and a gateway forwards
  // them as they are.
  @Test
  void canonicalConstructorAcceptsValuesTheFactoriesReject() {
    assertDoesNotThrow(
        () ->
            new Diagnostic(
                "http://opcfoundation.org/UA/",
                "x".repeat(64),
                null,
                null,
                new StatusCode(StatusCodes.Bad_InternalError),
                null));
  }

  @Test
  void withMethodsKeepTheOtherFields() {
    var innerStatus = new StatusCode(StatusCodes.Bad_Timeout);
    Diagnostic inner = Diagnostic.text("device timeout");

    Diagnostic diagnostic =
        Diagnostic.symbolic("urn:acme:plc", "E_RUN_MODE")
            .withText("blocked in RUN mode")
            .withAdditionalInfo("slot 3")
            .withInner(innerStatus, inner);

    assertEquals(
        new Diagnostic(
            "urn:acme:plc",
            "E_RUN_MODE",
            new LocalizedText(null, "blocked in RUN mode"),
            "slot 3",
            innerStatus,
            inner),
        diagnostic);
  }
}
