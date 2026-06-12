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

import java.util.Objects;
import java.util.Optional;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.OverrideValueHandling;
import org.jspecify.annotations.Nullable;

/**
 * Describes the target variable a received dataset field is written to, including optional index
 * ranges and override-value behavior.
 *
 * <p>Corresponds to the target-side of the Part 14 {@code FieldTargetDataType}.
 */
public final class TargetVariableConfig {

  private final NodeFieldAddress target;
  private final @Nullable String receiverIndexRange;
  private final @Nullable String writeIndexRange;
  private final @Nullable Variant overrideValue;
  private final OverrideValueHandling overrideHandling;

  private TargetVariableConfig(Builder builder) {
    this.target = Objects.requireNonNull(builder.target);
    this.receiverIndexRange = builder.receiverIndexRange;
    this.writeIndexRange = builder.writeIndexRange;
    this.overrideValue = builder.overrideValue;
    this.overrideHandling = builder.overrideHandling;
  }

  /**
   * Get the address of the target variable written when a field is received.
   *
   * @return the target variable address.
   */
  public NodeFieldAddress getTarget() {
    return target;
  }

  /**
   * Get the index range applied to the received field value before writing.
   *
   * @return the receiver index range, if configured.
   */
  public Optional<String> getReceiverIndexRange() {
    return Optional.ofNullable(receiverIndexRange);
  }

  /**
   * Get the index range applied to the target variable when writing.
   *
   * @return the write index range, if configured.
   */
  public Optional<String> getWriteIndexRange() {
    return Optional.ofNullable(writeIndexRange);
  }

  /**
   * Get the value written when {@link #getOverrideHandling()} is {@link
   * OverrideValueHandling#OverrideValue} and the received field is bad.
   *
   * @return the override value, if configured.
   */
  public Optional<Variant> getOverrideValue() {
    return Optional.ofNullable(overrideValue);
  }

  /**
   * Get the behavior applied when a received field value is bad.
   *
   * @return the override value handling; defaults to {@link OverrideValueHandling#Disabled}.
   */
  public OverrideValueHandling getOverrideHandling() {
    return overrideHandling;
  }

  /**
   * Create a {@link Builder} pre-populated with the values of this config.
   *
   * @return a new {@link Builder}.
   */
  public Builder toBuilder() {
    Builder builder = new Builder();
    builder.target = target;
    builder.receiverIndexRange = receiverIndexRange;
    builder.writeIndexRange = writeIndexRange;
    builder.overrideValue = overrideValue;
    builder.overrideHandling = overrideHandling;
    return builder;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof TargetVariableConfig that)) {
      return false;
    }
    return target.equals(that.target)
        && Objects.equals(receiverIndexRange, that.receiverIndexRange)
        && Objects.equals(writeIndexRange, that.writeIndexRange)
        && Objects.equals(overrideValue, that.overrideValue)
        && overrideHandling == that.overrideHandling;
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        target, receiverIndexRange, writeIndexRange, overrideValue, overrideHandling);
  }

  @Override
  public String toString() {
    return "TargetVariableConfig{target=%s, receiverIndexRange=%s, writeIndexRange=%s, overrideValue=%s, overrideHandling=%s}"
        .formatted(target, receiverIndexRange, writeIndexRange, overrideValue, overrideHandling);
  }

  /**
   * Create a new {@link Builder}.
   *
   * @return a new {@link Builder}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /** A builder of {@link TargetVariableConfig} instances. */
  public static final class Builder {

    private @Nullable NodeFieldAddress target;
    private @Nullable String receiverIndexRange;
    private @Nullable String writeIndexRange;
    private @Nullable Variant overrideValue;
    private OverrideValueHandling overrideHandling = OverrideValueHandling.Disabled;

    private Builder() {}

    /**
     * Set the address of the target variable. Required.
     *
     * @param target the target variable address.
     * @return this {@link Builder}.
     */
    public Builder target(NodeFieldAddress target) {
      this.target = target;
      return this;
    }

    /**
     * Set the index range applied to the received field value before writing.
     *
     * @param numericRange a NumericRange string.
     * @return this {@link Builder}.
     */
    public Builder receiverIndexRange(String numericRange) {
      this.receiverIndexRange = numericRange;
      return this;
    }

    /**
     * Set the index range applied to the target variable when writing.
     *
     * @param numericRange a NumericRange string.
     * @return this {@link Builder}.
     */
    public Builder writeIndexRange(String numericRange) {
      this.writeIndexRange = numericRange;
      return this;
    }

    /**
     * Set the value written when override handling is {@link OverrideValueHandling#OverrideValue}
     * and the received field is bad.
     *
     * @param value the override value.
     * @return this {@link Builder}.
     */
    public Builder overrideValue(Variant value) {
      this.overrideValue = value;
      return this;
    }

    /**
     * Set the behavior applied when a received field value is bad.
     *
     * @param handling the override value handling.
     * @return this {@link Builder}.
     */
    public Builder overrideHandling(OverrideValueHandling handling) {
      this.overrideHandling = handling;
      return this;
    }

    /**
     * Build a {@link TargetVariableConfig} from the configured values.
     *
     * @return a new {@link TargetVariableConfig}.
     * @throws PubSubConfigValidationException if no target is configured.
     */
    public TargetVariableConfig build() {
      if (target == null) {
        throw new PubSubConfigValidationException("TargetVariableConfig: target is required");
      }
      return new TargetVariableConfig(this);
    }
  }
}
