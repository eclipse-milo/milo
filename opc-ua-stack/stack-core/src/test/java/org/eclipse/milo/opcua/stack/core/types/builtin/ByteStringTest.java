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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class ByteStringTest {

  @Test
  public void testByteStringEquals() {
    ByteString bs1 = ByteString.of(new byte[] {1, 2, 3, 4});
    ByteString bs2 = ByteString.of(new byte[] {1, 2, 3, 4});
    ByteString bs3 = ByteString.of(new byte[] {1, 2, 3, 4, 5});

    assertEquals(bs1, bs2);

    assertNotEquals(bs1, bs3);
    assertNotEquals(bs2, bs3);
  }

  @Test
  public void nullEquality() {
    assertEquals(new ByteString(null), new ByteString(null));
    assertEquals(new ByteString(new byte[0]), new ByteString(new byte[0]));
    assertEquals(new ByteString(null), new ByteString(new byte[0]));
    assertEquals(new ByteString(new byte[0]), new ByteString(null));
  }

  @Test
  public void equalByteStringsHashTheSame() {
    assertEquals(
        ByteString.of(new byte[] {1, 2, 3, 4}).hashCode(),
        ByteString.of(new byte[] {1, 2, 3, 4}).hashCode());
  }

  // nullEquality() pins that a null and an empty ByteString are equal, so they have to hash the
  // same to be usable in a hash-based collection.
  @Test
  public void nullAndEmptyHashTheSame() {
    ByteString nullValue = new ByteString(null);
    ByteString empty = new ByteString(new byte[0]);

    assertEquals(nullValue.hashCode(), empty.hashCode());

    Set<ByteString> set = new HashSet<>();
    set.add(nullValue);

    assertTrue(set.contains(empty));

    set.add(empty);

    assertEquals(1, set.size());
  }
}
