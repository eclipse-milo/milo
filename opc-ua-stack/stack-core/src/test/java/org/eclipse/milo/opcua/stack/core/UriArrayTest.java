/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.junit.jupiter.api.Test;

class UriArrayTest {

  @Test
  void add() {
    var array = new UriArray() {};
    assertEquals(0, array.add("foo").intValue());
    assertEquals(1, array.add("bar").intValue());
    // duplicate URI not added twice, returns same index as before
    assertEquals(1, array.add("bar").intValue());
  }

  @Test
  void get() {
    var array = new UriArray() {};
    array.add("foo");
    assertEquals("foo", array.get(0));
    array.add("bar");
    assertEquals("bar", array.get(1));
  }

  @Test
  void getIndex() {
    var array = new UriArray() {};
    array.add("foo");
    array.add("bar");

    UShort indexOfFoo = array.getIndex("foo");
    assertNotNull(indexOfFoo);
    assertEquals(0, indexOfFoo.intValue());

    UShort indexOfBar = array.getIndex("bar");
    assertNotNull(indexOfBar);
    assertEquals(1, indexOfBar.intValue());
  }

  @Test
  void set() {
    var array = new UriArray() {};
    array.add("foo");
    array.add("bar");
    assertEquals("bar", array.get(1));

    array.set(1, "baz");
    assertEquals("baz", array.get(1));
  }

  @Test
  void update() {
    var array = new UriArray() {};
    array.add("foo");
    array.add("bar");
    assertEquals("bar", array.get(1));

    array.update(m -> m.put(ushort(1), "baz"));
    assertEquals("baz", array.get(1));
  }

  @Test
  void toArray() {
    var array = new UriArray() {};
    array.add("foo");
    array.add("bar");
    array.add("baz");

    assertArrayEquals(new String[] {"foo", "bar", "baz"}, array.toArray());
  }
}
