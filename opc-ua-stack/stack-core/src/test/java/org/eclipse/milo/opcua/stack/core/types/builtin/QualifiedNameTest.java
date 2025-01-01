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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class QualifiedNameTest {

  @Test
  public void parseableStringSymmetry() {
    assertSymmetry("0:foo");
    assertSymmetry("0:foo:bar");
  }

  @Test
  public void isNull() {
    assertTrue(new QualifiedName(0, null).isNull());
    assertTrue(new QualifiedName(0, "").isNull());
  }

  @Test
  public void nullEquality() {
    assertEquals(new QualifiedName(0, ""), new QualifiedName(0, ""));
    assertEquals(new QualifiedName(0, null), new QualifiedName(0, null));
    assertEquals(new QualifiedName(0, null), new QualifiedName(0, ""));
    assertEquals(new QualifiedName(0, ""), new QualifiedName(0, null));
  }

  private void assertSymmetry(String string) {
    String reString = QualifiedName.parse(string).toParseableString();
    assertEquals(string, reString);
  }
}
