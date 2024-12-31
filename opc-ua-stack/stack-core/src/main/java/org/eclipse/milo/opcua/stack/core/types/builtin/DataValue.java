/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.builtin;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import java.util.Objects;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public final class DataValue {

  private final Variant value;
  private final StatusCode status;
  private final DateTime sourceTime;
  private final UShort sourcePicoseconds;
  private final DateTime serverTime;
  private final UShort serverPicoseconds;

  public DataValue(long statusCode) {
    this(new StatusCode(statusCode));
  }

  public DataValue(StatusCode statusCode) {
    this(Variant.NULL_VALUE, statusCode, DateTime.MIN_VALUE);
  }

  public DataValue(Variant value) {
    this(value, StatusCode.GOOD);
  }

  public DataValue(Variant value, StatusCode status) {
    this(value, status, DateTime.now());
  }

  public DataValue(Variant value, StatusCode status, @Nullable DateTime time) {
    this(value, status, time, time);
  }

  public DataValue(
      Variant value,
      StatusCode status,
      @Nullable DateTime sourceTime,
      @Nullable DateTime serverTime) {
    this(value, status, sourceTime, null, serverTime, null);
  }

  public DataValue(
      @NonNull Variant value,
      @Nullable StatusCode status,
      @Nullable DateTime sourceTime,
      @Nullable UShort sourcePicoseconds,
      @Nullable DateTime serverTime,
      @Nullable UShort serverPicoseconds) {

    this.value = value;
    this.status = status;
    this.sourceTime = sourceTime;
    this.sourcePicoseconds = sourcePicoseconds;
    this.serverTime = serverTime;
    this.serverPicoseconds = serverPicoseconds;
  }

  public Variant getValue() {
    return value;
  }

  @Nullable
  public StatusCode getStatusCode() {
    return status;
  }

  @Nullable
  public DateTime getSourceTime() {
    return sourceTime;
  }

  @Nullable
  public UShort getSourcePicoseconds() {
    return sourcePicoseconds;
  }

  @Nullable
  public DateTime getServerTime() {
    return serverTime;
  }

  @Nullable
  public UShort getServerPicoseconds() {
    return serverPicoseconds;
  }

  public DataValue withStatus(StatusCode status) {
    return new DataValue(value, status, sourceTime, serverTime);
  }

  public DataValue withSourceTime(@Nullable DateTime sourceTime) {
    return new DataValue(value, status, sourceTime, serverTime);
  }

  public DataValue withServerTime(@Nullable DateTime serverTime) {
    return new DataValue(value, status, sourceTime, serverTime);
  }

  @Override
  public String toString() {
    ToStringHelper helper = MoreObjects.toStringHelper(this);

    helper.add("value", value);
    helper.add("status", status);

    if (sourceTime != null) {
      helper.add("sourceTime", sourceTime);
    }
    if (sourcePicoseconds != null) {
      helper.add("sourcePicoseconds", sourcePicoseconds);
    }
    if (serverTime != null) {
      helper.add("serverTime", serverTime);
    }
    if (serverPicoseconds != null) {
      helper.add("serverPicoseconds", serverPicoseconds);
    }

    return helper.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    DataValue dataValue = (DataValue) o;
    return Objects.equals(value, dataValue.value)
        && Objects.equals(status, dataValue.status)
        && Objects.equals(sourceTime, dataValue.sourceTime)
        && Objects.equals(sourcePicoseconds, dataValue.sourcePicoseconds)
        && Objects.equals(serverTime, dataValue.serverTime)
        && Objects.equals(serverPicoseconds, dataValue.serverPicoseconds);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        value, status, sourceTime, sourcePicoseconds, serverTime, serverPicoseconds);
  }

  public DataValue.Builder copy() {
    return new Builder(this);
  }

  public DataValue copy(Consumer<DataValue.Builder> c) {
    Builder builder = new Builder(this);

    c.accept(builder);

    return builder.build();
  }

  public static DataValue.Builder newValue() {
    return new Builder();
  }

  /**
   * Derive a new {@link DataValue} from a given {@link DataValue} with a current server timestamp,
   * if applicable.
   *
   * @param from the {@link DataValue} to derive from.
   * @param timestamps the timestamps to return in the derived value.
   * @return a derived {@link DataValue}.
   */
  public static DataValue derivedValue(DataValue from, TimestampsToReturn timestamps) {
    boolean includeSource =
        timestamps == TimestampsToReturn.Source || timestamps == TimestampsToReturn.Both;
    boolean includeServer =
        timestamps == TimestampsToReturn.Server || timestamps == TimestampsToReturn.Both;

    return new DataValue(
        from.value,
        from.status,
        includeSource ? from.sourceTime : null,
        includeServer ? DateTime.now() : null);
  }

  /**
   * Derive a new {@link DataValue} from a given {@link DataValue} with a current server timestamp,
   * if applicable.
   *
   * <p>The value is assumed to be for a non-value Node attribute, and therefore the source
   * timestamp is not returned.
   *
   * @param from the {@link DataValue} to derive from.
   * @param timestamps the timestamps to return in the derived value.
   * @return a derived {@link DataValue}.
   */
  public static DataValue derivedNonValue(DataValue from, TimestampsToReturn timestamps) {
    boolean includeServer =
        timestamps == TimestampsToReturn.Server || timestamps == TimestampsToReturn.Both;

    return new DataValue(from.value, from.status, null, includeServer ? DateTime.now() : null);
  }

  /**
   * Create a {@link DataValue} containing *only* the Variant. All other fields will be null.
   *
   * @param v the value {@link Variant}.
   * @return a {@link DataValue} containing only the value.
   */
  public static DataValue valueOnly(Variant v) {
    return new DataValue(v, null, null, null);
  }

  public static class Builder {

    public Variant value = Variant.NULL_VALUE;
    public StatusCode status;
    public DateTime sourceTime;
    public UShort sourcePicoseconds;
    public DateTime serverTime;
    public UShort serverPicoseconds;

    public Builder() {}

    public Builder(DataValue other) {
      this.value = other.value;
      this.status = other.status;
      this.sourceTime = other.sourceTime;
      this.sourcePicoseconds = other.sourcePicoseconds;
      this.serverTime = other.serverTime;
      this.serverPicoseconds = other.serverPicoseconds;
    }

    public Builder setValue(Variant value) {
      this.value = value;
      return this;
    }

    public Builder setStatus(StatusCode status) {
      this.status = status;
      return this;
    }

    public Builder setSourceTime(DateTime sourceTime) {
      this.sourceTime = sourceTime;
      return this;
    }

    public Builder setSourcePicoseconds(UShort sourcePicoseconds) {
      this.sourcePicoseconds = sourcePicoseconds;
      return this;
    }

    public Builder setServerTime(DateTime serverTime) {
      this.serverTime = serverTime;
      return this;
    }

    public Builder setServerPicoseconds(UShort serverPicoseconds) {
      this.serverPicoseconds = serverPicoseconds;
      return this;
    }

    public Builder applyTimestamps(AttributeId attributeId, TimestampsToReturn timestamps) {
      boolean includeSource =
          attributeId == AttributeId.Value
              && (timestamps == TimestampsToReturn.Source || timestamps == TimestampsToReturn.Both);

      boolean includeServer =
          timestamps == TimestampsToReturn.Server || timestamps == TimestampsToReturn.Both;

      // Source timestamps can only be removed; they
      // can't be added because we aren't the source.
      if (!includeSource) {
        setSourceTime(null);
        setSourcePicoseconds(null);
      }

      if (!includeServer) {
        setServerTime(null);
        setServerPicoseconds(null);
      } else {
        if (serverTime == null) {
          setServerTime(DateTime.now());
        }
      }

      return this;
    }

    public DataValue build() {
      return new DataValue(
          value, status, sourceTime, sourcePicoseconds, serverTime, serverPicoseconds);
    }
  }
}
