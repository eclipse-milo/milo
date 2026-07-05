/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

/**
 * A {@link PublishedDataSetSourceConfig} sourcing dataset values from an ordered list of {@link
 * FieldDefinition}s, supplied with snapshots at runtime by a {@code PublishedDataSetSource} bound
 * to the dataset's ref.
 *
 * <p>Corresponds to the Part 14 {@code PublishedDataItemsDataType} source. Field order defines wire
 * order.
 */
public final class PublishedDataItemsConfig implements PublishedDataSetSourceConfig {

  private final List<FieldDefinition> fields;

  private PublishedDataItemsConfig(Builder builder) {
    this.fields = List.copyOf(builder.fields);
  }

  /**
   * Get the field definitions, in dataset (wire) order.
   *
   * @return the field definitions.
   */
  public List<FieldDefinition> getFields() {
    return fields;
  }

  /**
   * Create a {@link Builder} pre-populated with the values of this config.
   *
   * @return a new {@link Builder}.
   */
  public Builder toBuilder() {
    Builder builder = new Builder();
    builder.fields.addAll(fields);
    return builder;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof PublishedDataItemsConfig that)) {
      return false;
    }
    return fields.equals(that.fields);
  }

  @Override
  public int hashCode() {
    return fields.hashCode();
  }

  @Override
  public String toString() {
    return "PublishedDataItemsConfig{fields=%s}".formatted(fields);
  }

  /**
   * Create a new {@link Builder}.
   *
   * @return a new {@link Builder}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /** A builder of {@link PublishedDataItemsConfig} instances. */
  public static final class Builder {

    private final List<FieldDefinition> fields = new ArrayList<>();

    private Builder() {}

    /**
     * Add a field definition. Field order defines wire order.
     *
     * @param field the field definition to add.
     * @return this {@link Builder}.
     */
    public Builder field(FieldDefinition field) {
      fields.add(field);
      return this;
    }

    /**
     * Build a {@link PublishedDataItemsConfig} from the configured values.
     *
     * @return a new {@link PublishedDataItemsConfig}.
     * @throws PubSubConfigValidationException if two fields share a name or a DataSetFieldId.
     */
    public PublishedDataItemsConfig build() {
      Set<String> fieldNames = new HashSet<>();
      Set<UUID> fieldIds = new HashSet<>();
      for (FieldDefinition field : fields) {
        if (!fieldNames.add(field.getName())) {
          throw new PubSubConfigValidationException(
              "PublishedDataItemsConfig: duplicate field name '%s'".formatted(field.getName()));
        }
        if (!fieldIds.add(field.getDataSetFieldId())) {
          throw new PubSubConfigValidationException(
              "PublishedDataItemsConfig: duplicate dataSetFieldId %s on field '%s'"
                  .formatted(field.getDataSetFieldId(), field.getName()));
        }
      }

      return new PublishedDataItemsConfig(this);
    }
  }
}
