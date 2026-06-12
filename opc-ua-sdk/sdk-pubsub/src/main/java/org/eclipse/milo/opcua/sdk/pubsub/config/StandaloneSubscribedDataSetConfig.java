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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.jspecify.annotations.Nullable;

/**
 * A subscribed dataset, with its metadata, defined once and referenced by DataSetReaders via {@link
 * StandaloneSubscribedDataSetRef}.
 *
 * <p>Corresponds to the Part 14 {@code StandaloneSubscribedDataSetDataType}.
 */
public final class StandaloneSubscribedDataSetConfig {

  private final String name;
  private final DataSetMetaDataConfig metaData;
  private final SubscribedDataSetSpec subscribedDataSet;
  private final Map<QualifiedName, Variant> properties;

  private StandaloneSubscribedDataSetConfig(Builder builder) {
    this.name = builder.name;
    this.metaData = Objects.requireNonNull(builder.metaData);
    this.subscribedDataSet = Objects.requireNonNull(builder.subscribedDataSet);
    this.properties = Collections.unmodifiableMap(new LinkedHashMap<>(builder.properties));
  }

  /**
   * Get the name of this standalone subscribed dataset.
   *
   * @return the name of this standalone subscribed dataset.
   */
  public String getName() {
    return name;
  }

  /**
   * Get the metadata describing the subscribed dataset.
   *
   * @return the metadata describing the subscribed dataset.
   */
  public DataSetMetaDataConfig getMetaData() {
    return metaData;
  }

  /**
   * Get the spec describing how received dataset fields are consumed.
   *
   * @return the subscribed dataset spec.
   */
  public SubscribedDataSetSpec getSubscribedDataSet() {
    return subscribedDataSet;
  }

  /**
   * Get the additional properties of this standalone subscribed dataset, in insertion order.
   *
   * @return an unmodifiable view of the additional properties.
   */
  public Map<QualifiedName, Variant> getProperties() {
    return properties;
  }

  /**
   * Get a {@link StandaloneSubscribedDataSetRef} referencing this dataset by name.
   *
   * @return a {@link StandaloneSubscribedDataSetRef} referencing this dataset.
   */
  public StandaloneSubscribedDataSetRef ref() {
    return new StandaloneSubscribedDataSetRef(name);
  }

  /**
   * Create a {@link Builder} pre-populated with the values of this config.
   *
   * @return a new {@link Builder}.
   */
  public Builder toBuilder() {
    Builder builder = new Builder(name);
    builder.metaData = metaData;
    builder.subscribedDataSet = subscribedDataSet;
    builder.properties.putAll(properties);
    return builder;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof StandaloneSubscribedDataSetConfig that)) {
      return false;
    }
    return name.equals(that.name)
        && metaData.equals(that.metaData)
        && subscribedDataSet.equals(that.subscribedDataSet)
        && properties.equals(that.properties);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, metaData, subscribedDataSet, properties);
  }

  @Override
  public String toString() {
    return "StandaloneSubscribedDataSetConfig{name='%s', metaData=%s, subscribedDataSet=%s, properties=%s}"
        .formatted(name, metaData, subscribedDataSet, properties);
  }

  /**
   * Create a new {@link Builder}.
   *
   * @param name the name of the standalone subscribed dataset.
   * @return a new {@link Builder}.
   */
  public static Builder builder(String name) {
    return new Builder(name);
  }

  /** A builder of {@link StandaloneSubscribedDataSetConfig} instances. */
  public static final class Builder {

    private final String name;
    private @Nullable DataSetMetaDataConfig metaData;
    private @Nullable SubscribedDataSetSpec subscribedDataSet;
    private final Map<QualifiedName, Variant> properties = new LinkedHashMap<>();

    private Builder(String name) {
      this.name = name;
    }

    /**
     * Set the metadata describing the subscribed dataset. Required.
     *
     * @param metaData the metadata describing the subscribed dataset.
     * @return this {@link Builder}.
     */
    public Builder metaData(DataSetMetaDataConfig metaData) {
      this.metaData = metaData;
      return this;
    }

    /**
     * Set the spec describing how received dataset fields are consumed. Required.
     *
     * @param spec the subscribed dataset spec.
     * @return this {@link Builder}.
     */
    public Builder subscribedDataSet(SubscribedDataSetSpec spec) {
      this.subscribedDataSet = spec;
      return this;
    }

    /**
     * Add an additional property of the standalone subscribed dataset.
     *
     * @param name the name of the property.
     * @param value the value of the property.
     * @return this {@link Builder}.
     */
    public Builder property(QualifiedName name, Variant value) {
      properties.put(name, value);
      return this;
    }

    /**
     * Build a {@link StandaloneSubscribedDataSetConfig} from the configured values.
     *
     * @return a new {@link StandaloneSubscribedDataSetConfig}.
     * @throws PubSubConfigValidationException if the name is empty, the metadata or subscribed
     *     dataset spec is missing, or the spec is a self-reference.
     */
    public StandaloneSubscribedDataSetConfig build() {
      if (name.isEmpty()) {
        throw new PubSubConfigValidationException(
            "StandaloneSubscribedDataSetConfig: name must not be empty");
      }
      if (metaData == null) {
        throw new PubSubConfigValidationException(
            "StandaloneSubscribedDataSetConfig '%s': metaData is required".formatted(name));
      }
      if (subscribedDataSet == null) {
        throw new PubSubConfigValidationException(
            "StandaloneSubscribedDataSetConfig '%s': subscribedDataSet is required"
                .formatted(name));
      }
      if (subscribedDataSet instanceof StandaloneSubscribedDataSetRef ref
          && ref.name().equals(name)) {
        throw new PubSubConfigValidationException(
            "StandaloneSubscribedDataSetConfig '%s': subscribedDataSet must not reference itself"
                .formatted(name));
      }
      return new StandaloneSubscribedDataSetConfig(this);
    }
  }
}
