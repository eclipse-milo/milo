/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.builtin;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class QualifiedNameTest {

  @Test
  void parseableStringSymmetry() {
    assertSymmetry("0:foo");
    assertSymmetry("0:foo:bar");
  }

  @Test
  void isNull() {
    assertTrue(new QualifiedName(0, null).isNull());
    assertTrue(new QualifiedName(0, "").isNull());
  }

  @Test
  void nullEquality() {
    assertEquals(new QualifiedName(0, ""), new QualifiedName(0, ""));
    assertEquals(new QualifiedName(0, null), new QualifiedName(0, null));
    assertEquals(new QualifiedName(0, null), new QualifiedName(0, ""));
    assertEquals(new QualifiedName(0, ""), new QualifiedName(0, null));
  }

  @Test
  void testNameSizeLimit() {
    String name = "a".repeat(512);
    QualifiedName qn = new QualifiedName(0, name);
    assertEquals(name, qn.name());

    String longName = "a".repeat(513);
    assertThrows(IllegalArgumentException.class, () -> new QualifiedName(0, longName));
  }

  private void assertSymmetry(String string) {
    String reString = QualifiedName.parse(string).toParseableString();
    assertEquals(string, reString);
  }
}
