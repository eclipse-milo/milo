/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.dtd.generic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class StructTest {

  // Member.equals compares array values deeply, so the hash has to be derived from the elements
  // too. A codec produces arrays for every non-scalar field, so this is the common case.
  @Test
  void membersWithEqualArraysShareAHashCode() {
    Struct a = arrayContainer();
    Struct b = arrayContainer();

    assertEquals(a, b);
    assertEquals(a.hashCode(), b.hashCode());

    Set<Struct> set = new HashSet<>();
    set.add(a);
    set.add(b);
    assertEquals(1, set.size(), "equal Structs were stored as distinct elements");

    Map<Struct, String> map = new HashMap<>();
    map.put(a, "v");
    assertEquals("v", map.get(b), "an equal Struct did not find its entry");
  }

  // Control for the test above: the hash must come from the elements, not from a constant.
  @Test
  void membersWithDifferentArraysAreNotEqual() {
    Struct a = Struct.builder("S").addMember("xs", new int[] {1, 2, 3}).build();
    Struct b = Struct.builder("S").addMember("xs", new int[] {1, 2, 4}).build();

    assertNotEquals(a, b);
  }

  private static Struct arrayContainer() {
    return Struct.builder("ArrayContainer")
        .addMember("IntArray", new Integer[] {1, 2, 3})
        .addMember("Scalar", 4)
        .addMember("PrimitiveArray", new int[] {5, 6})
        .addMember("StringArray", new String[] {"a", "b"})
        .build();
  }
}
