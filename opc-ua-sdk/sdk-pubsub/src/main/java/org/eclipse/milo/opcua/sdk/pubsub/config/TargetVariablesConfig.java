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
import java.util.List;
import org.jspecify.annotations.Nullable;

/**
 * A {@link SubscribedDataSetSpec} that maps received dataset fields, selected by id, name, or
 * index, to target variables in an address space.
 *
 * <p>Corresponds to the Part 14 {@code TargetVariablesDataType}. Multiple mappings may share the
 * same {@link FieldSelector}, mirroring the spec's allowance for one field to be written to
 * multiple targets.
 */
public final class TargetVariablesConfig implements SubscribedDataSetSpec {

  private final List<Mapping> mappings;

  private TargetVariablesConfig(Builder builder) {
    this.mappings = List.copyOf(builder.mappings);
  }

  /**
   * Get the field-to-target mappings, in the order they were added.
   *
   * @return the field-to-target mappings.
   */
  public List<Mapping> getMappings() {
    return mappings;
  }

  /**
   * Create a {@link Builder} pre-populated with the values of this config.
   *
   * @return a new {@link Builder}.
   */
  public Builder toBuilder() {
    Builder builder = new Builder();
    builder.mappings.addAll(mappings);
    return builder;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof TargetVariablesConfig that)) {
      return false;
    }
    return mappings.equals(that.mappings);
  }

  @Override
  public int hashCode() {
    return mappings.hashCode();
  }

  @Override
  public String toString() {
    return "TargetVariablesConfig{mappings=" + mappings + "}";
  }

  /**
   * Create a new {@link Builder}.
   *
   * @return a new {@link Builder}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * A mapping from a received dataset field to a target variable.
   *
   * @param selector selects the dataset field.
   * @param target the target variable the field is written to.
   */
  public record Mapping(FieldSelector selector, TargetVariableConfig target) {}

  /** A builder of {@link TargetVariablesConfig} instances. */
  public static final class Builder {

    private final List<Mapping> mappings = new ArrayList<>();

    private Builder() {}

    /**
     * Add a mapping from a dataset field to a target variable.
     *
     * @param selector selects the dataset field.
     * @param target the target variable the field is written to.
     * @return this {@link Builder}.
     */
    public Builder map(FieldSelector selector, TargetVariableConfig target) {
      mappings.add(new Mapping(selector, target));
      return this;
    }

    /**
     * Build a {@link TargetVariablesConfig} from the configured values.
     *
     * @return a new {@link TargetVariablesConfig}.
     */
    public TargetVariablesConfig build() {
      return new TargetVariablesConfig(this);
    }
  }
}
