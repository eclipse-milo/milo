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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;

/**
 * An immutable snapshot of the field values of a PublishedDataSet, produced by a {@link
 * PublishedDataSetSource} for one publish cycle.
 *
 * <p>Values are ordered by the field order of the {@link PublishedDataSetReadContext} the snapshot
 * was built against; that order is the wire order. Fields not supplied to the builder are published
 * as a {@link DataValue} with status {@code Bad_NoData}.
 */
public final class DataSetSnapshot {

  private final List<DataValue> values;

  private DataSetSnapshot(List<DataValue> values) {
    this.values = values;
  }

  /**
   * Get the field values of this snapshot, ordered by the field order of the read context.
   *
   * @return the field values, one per field of the dataset.
   */
  public List<DataValue> values() {
    return values;
  }

  @Override
  public String toString() {
    return "DataSetSnapshot{values=%s}".formatted(values);
  }

  /**
   * Create a new {@link Builder} for a snapshot of the dataset described by {@code context}.
   *
   * @param context the read context describing the dataset and its fields.
   * @return a new {@link Builder}.
   */
  public static Builder builder(PublishedDataSetReadContext context) {
    return new Builder(context);
  }

  /** A builder of {@link DataSetSnapshot} instances. */
  public static final class Builder {

    private final PublishedDataSetReadContext context;
    private final Map<String, DataValue> fieldValues = new HashMap<>();

    private Builder(PublishedDataSetReadContext context) {
      this.context = context;
    }

    /**
     * Supply the value of the field named {@code fieldName}.
     *
     * @param fieldName the name of a field defined by the read context.
     * @param value the value of the field.
     * @return this {@link Builder}.
     * @throws IllegalArgumentException if {@code fieldName} is not a field of the dataset described
     *     by the read context.
     */
    public Builder field(String fieldName, DataValue value) {
      boolean known =
          context.fields().stream().anyMatch(field -> field.getName().equals(fieldName));

      if (!known) {
        throw new IllegalArgumentException(
            "field '%s' is not defined by dataset '%s'"
                .formatted(fieldName, context.dataSet().name()));
      }

      fieldValues.put(fieldName, value);
      return this;
    }

    /**
     * Build a {@link DataSetSnapshot} from the supplied field values. Fields not supplied get a
     * {@link DataValue} with status {@code Bad_NoData}.
     *
     * @return a new {@link DataSetSnapshot}.
     */
    public DataSetSnapshot build() {
      var values = new ArrayList<DataValue>(context.fields().size());

      for (FieldDefinition field : context.fields()) {
        DataValue value = fieldValues.get(field.getName());
        if (value == null) {
          value = new DataValue(StatusCodes.Bad_NoData);
        }
        values.add(value);
      }

      return new DataSetSnapshot(List.copyOf(values));
    }
  }
}
