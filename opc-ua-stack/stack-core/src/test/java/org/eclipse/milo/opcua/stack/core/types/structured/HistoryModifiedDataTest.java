/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.structured;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.HistoryUpdateType;
import org.junit.jupiter.api.Test;

class HistoryModifiedDataTest {

  @Test
  void testEqualsAndHashCode() {
    var hmd1 =
        new HistoryModifiedData(
            new DataValue[] {new DataValue(new Variant(0), StatusCode.GOOD, DateTime.NULL_VALUE)},
            new ModificationInfo[] {
              new ModificationInfo(DateTime.NULL_VALUE, HistoryUpdateType.Insert, "foo")
            });

    var hmd2 =
        new HistoryModifiedData(
            new DataValue[] {new DataValue(new Variant(0), StatusCode.GOOD, DateTime.NULL_VALUE)},
            new ModificationInfo[] {
              new ModificationInfo(DateTime.NULL_VALUE, HistoryUpdateType.Insert, "foo")
            });

    assertEquals(hmd1, hmd2);
    assertEquals(hmd1.hashCode(), hmd2.hashCode());
  }
}
