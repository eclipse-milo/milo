/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.types;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.HashMap;
import java.util.Map;
import org.eclipse.milo.opcua.sdk.core.types.DynamicUnionType.UnionValue;
import org.eclipse.milo.opcua.sdk.core.types.util.AbstractDataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.junit.jupiter.api.Test;

class DynamicUnionTypeTest {

  private static final NodeId DATA_TYPE_ID = new NodeId(2, "MyUnion");

  // equals() compares the DataType by NodeId because DataType implementations are not required to
  // define equals()/hashCode(), so two DataType instances describing the same DataType - obtained
  // from a rebuilt DataTypeTree, say - must not change how a DynamicUnionType hashes.
  @Test
  void equalUnionsHashTheSame() {
    DynamicUnionType u1 = new DynamicUnionType(dataType(), new UnionValue("foo", 42));
    DynamicUnionType u2 = new DynamicUnionType(dataType(), new UnionValue("foo", 42));

    assertEquals(u1, u2);
    assertEquals(u1.hashCode(), u2.hashCode());

    Map<DynamicUnionType, String> map = new HashMap<>();
    map.put(u1, "value");

    assertEquals("value", map.get(u2));
  }

  @Test
  void unionsWithDifferentValuesAreNotEqual() {
    DataType dataType = dataType();

    assertNotEquals(
        new DynamicUnionType(dataType, new UnionValue("foo", 42)),
        new DynamicUnionType(dataType, new UnionValue("foo", 43)));

    assertNotEquals(
        new DynamicUnionType(dataType, new UnionValue("foo", 42)),
        new DynamicUnionType(dataType, new UnionValue("bar", 42)));
  }

  /** A new DataType instance, equivalent to but not identical with any other one returned here. */
  private static DataType dataType() {
    return new AbstractDataType(DATA_TYPE_ID, new QualifiedName(2, "MyUnion"), null, false) {};
  }
}
