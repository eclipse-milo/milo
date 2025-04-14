/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;

class ListsTest {

  @Test
  void ofNullable() {
    assertEquals(0, Lists.ofNullable((Object[]) null).size());
    assertEquals(0, Lists.ofNullable().size());
    assertEquals(1, Lists.ofNullable(new String[] {"foo"}).size());
  }

  @Test
  void partition() {
    {
      List<List<Integer>> partitions = Lists.partition(List.of(1, 2, 3), 1).toList();
      assertEquals(3, partitions.size());
      assertEquals(1, partitions.get(0).size());
      assertEquals(1, partitions.get(1).size());
      assertEquals(1, partitions.get(2).size());
    }

    {
      List<List<Integer>> partitions = Lists.partition(List.of(1, 2, 3), 2).toList();
      assertEquals(2, partitions.size());
      assertEquals(2, partitions.get(0).size());
      assertEquals(1, partitions.get(1).size());
    }

    {
      List<List<Integer>> partitions = Lists.partition(List.of(1, 2, 3), 3).toList();
      assertEquals(1, partitions.size());
      assertEquals(3, partitions.get(0).size());
    }
  }
}
