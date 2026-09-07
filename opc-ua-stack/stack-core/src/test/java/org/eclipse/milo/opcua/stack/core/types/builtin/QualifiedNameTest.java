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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class QualifiedNameTest {

  @Test
  void parseableStringSymmetry() {
    assertSymmetry("0:foo");
    assertSymmetry("0:foo:bar");
  }

  // The namespace index is a UInt16, so every index up to 65535 must survive a round trip. Parsing
  // it as a short silently yields index 0 for anything above 32767.
  @Test
  void parseableStringSymmetryAcrossNamespaceRange() {
    assertSymmetry("1:foo");
    assertSymmetry("32767:foo");
    assertSymmetry("32768:foo");
    assertSymmetry("40000:foo");
    assertSymmetry("65535:foo");
  }

  @Test
  void parseNamespaceIndex() {
    assertEquals(ushort(0), QualifiedName.parse("0:foo").namespaceIndex());
    assertEquals(ushort(32768), QualifiedName.parse("32768:foo").namespaceIndex());
    assertEquals(ushort(65535), QualifiedName.parse("65535:foo").namespaceIndex());
    assertEquals("foo", QualifiedName.parse("65535:foo").name());
  }

  // A prefix that is not a namespace index in range is ignored and the name after the separator is
  // kept, which is how an unparseable prefix has always been treated.
  @Test
  void parseIgnoresPrefixThatIsNotANamespaceIndex() {
    assertEquals(new QualifiedName(0, "foo"), QualifiedName.parse("abc:foo"));
    assertEquals(new QualifiedName(0, "foo"), QualifiedName.parse("65536:foo"));
    assertEquals(new QualifiedName(0, "foo"), QualifiedName.parse("99999999999:foo"));
    assertEquals(new QualifiedName(0, "foo"), QualifiedName.parse("-1:foo"));
  }

  @Test
  void parseWithoutSeparator() {
    assertEquals(new QualifiedName(0, "foo"), QualifiedName.parse("foo"));
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
