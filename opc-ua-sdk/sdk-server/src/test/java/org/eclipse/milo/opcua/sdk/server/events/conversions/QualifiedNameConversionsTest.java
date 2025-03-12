/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events.conversions;

import static org.eclipse.milo.opcua.sdk.server.events.conversions.QualifiedNameConversions.qualifiedNameToLocalizedText;
import static org.eclipse.milo.opcua.sdk.server.events.conversions.QualifiedNameConversions.qualifiedNameToString;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.junit.jupiter.api.Test;

public class QualifiedNameConversionsTest {

  @Test
  public void testQualifiedNameToString() {
    QualifiedName name = new QualifiedName(0, "foo");

    assertEquals("foo", qualifiedNameToString(name));
  }

  @Test
  public void testQualifiedNameToLocalizedText() {
    QualifiedName name = new QualifiedName(0, "foo");

    LocalizedText text = qualifiedNameToLocalizedText(name);

    assertEquals("", text.locale());
    assertEquals("foo", text.text());
  }
}
