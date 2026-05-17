/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.diagnostics.objects;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.junit.jupiter.api.Test;

class SessionsDiagnosticsSummaryObjectTest {

  @Test
  void sessionNameBrowseNamePreservesValidName() {
    String sessionName = "session";

    assertSame(sessionName, SessionsDiagnosticsSummaryObject.sessionNameBrowseName(sessionName));
  }

  @Test
  void sessionNameBrowseNameTruncatesOverlongName() {
    String sessionName = "a".repeat(SessionsDiagnosticsSummaryObject.MAX_BROWSE_NAME_LENGTH + 1);

    String browseName = SessionsDiagnosticsSummaryObject.sessionNameBrowseName(sessionName);

    assertEquals(SessionsDiagnosticsSummaryObject.MAX_BROWSE_NAME_LENGTH, browseName.length());
    assertDoesNotThrow(() -> new QualifiedName(1, browseName));
  }
}
