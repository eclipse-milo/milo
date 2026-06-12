/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.UUID;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublishedDataSetRef;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link DataSetSnapshot}: field name validation against the read context, value ordering
 * per the context's field order (the wire order), and {@code Bad_NoData} fill for fields not
 * supplied to the builder.
 */
class DataSetSnapshotTest {

  private static final PublishedDataSetRef DATA_SET_REF = new PublishedDataSetRef("PDS");

  private static FieldDefinition field(String name) {
    return FieldDefinition.builder(name)
        .dataType(NodeIds.Int32)
        .dataSetFieldId(new UUID(0L, name.hashCode()))
        .build();
  }

  /** Field order C, A, B is deliberately not alphabetical: order comes from the context. */
  private static PublishedDataSetReadContext context() {
    return new PublishedDataSetReadContext(
        DATA_SET_REF, List.of(field("C"), field("A"), field("B")), null);
  }

  @Test
  void unknownFieldNameIsRejected() {
    DataSetSnapshot.Builder builder = DataSetSnapshot.builder(context());

    IllegalArgumentException e =
        assertThrows(
            IllegalArgumentException.class,
            () -> builder.field("Unknown", new DataValue(Variant.ofInt32(1))));

    assertTrue(e.getMessage().contains("Unknown"), e.getMessage());
    assertTrue(e.getMessage().contains("PDS"), e.getMessage());
  }

  @Test
  void valuesAreOrderedByContextFieldOrder() {
    // supply the fields in a different order than the context declares them
    DataSetSnapshot snapshot =
        DataSetSnapshot.builder(context())
            .field("A", new DataValue(Variant.ofInt32(1)))
            .field("B", new DataValue(Variant.ofInt32(2)))
            .field("C", new DataValue(Variant.ofInt32(3)))
            .build();

    List<DataValue> values = snapshot.values();
    assertEquals(3, values.size());
    assertEquals(Variant.ofInt32(3), values.get(0).getValue()); // C
    assertEquals(Variant.ofInt32(1), values.get(1).getValue()); // A
    assertEquals(Variant.ofInt32(2), values.get(2).getValue()); // B
  }

  @Test
  void absentFieldsAreFilledWithBadNoData() {
    DataSetSnapshot snapshot =
        DataSetSnapshot.builder(context()).field("A", new DataValue(Variant.ofInt32(1))).build();

    List<DataValue> values = snapshot.values();
    assertEquals(3, values.size());

    // C (index 0) and B (index 2) were not supplied
    for (int index : new int[] {0, 2}) {
      DataValue value = values.get(index);
      assertEquals(new StatusCode(StatusCodes.Bad_NoData), value.getStatusCode(), "index " + index);
      assertEquals(Variant.NULL_VALUE, value.getValue(), "index " + index);
    }

    assertEquals(Variant.ofInt32(1), values.get(1).getValue());
    assertEquals(StatusCode.GOOD, values.get(1).getStatusCode());
  }

  @Test
  void emptyBuilderFillsEveryFieldWithBadNoData() {
    DataSetSnapshot snapshot = DataSetSnapshot.builder(context()).build();

    assertEquals(3, snapshot.values().size());
    for (DataValue value : snapshot.values()) {
      assertEquals(new StatusCode(StatusCodes.Bad_NoData), value.getStatusCode());
    }
  }

  @Test
  void suppliedValuesAreCarriedUnchanged() {
    var goodValue = new DataValue(Variant.ofInt32(42));

    DataSetSnapshot snapshot =
        DataSetSnapshot.builder(context())
            .field("C", goodValue)
            .field("A", goodValue)
            .field("B", goodValue)
            .build();

    for (DataValue value : snapshot.values()) {
      assertEquals(goodValue, value);
    }
  }
}
