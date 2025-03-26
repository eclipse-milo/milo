/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.builtin;

import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record DataValue(
    Variant value,
    StatusCode statusCode,
    @Nullable DateTime sourceTime,
    @Nullable UShort sourcePicoseconds,
    @Nullable DateTime serverTime,
    @Nullable UShort serverPicoseconds) {

  public DataValue(long statusCode) {
    this(new StatusCode(statusCode));
  }

  public DataValue(StatusCode statusCode) {
    this(Variant.NULL_VALUE, statusCode, null, DateTime.now());
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

  public Variant getValue() {
    return value;
  }

  public StatusCode getStatusCode() {
    return statusCode;
  }

  public @Nullable DateTime getSourceTime() {
    return sourceTime;
  }

  public @Nullable UShort getSourcePicoseconds() {
    return sourcePicoseconds;
  }

  public @Nullable DateTime getServerTime() {
    return serverTime;
  }

  public @Nullable UShort getServerPicoseconds() {
    return serverPicoseconds;
  }

  public DataValue withStatus(StatusCode status) {
    return new DataValue(value, status, sourceTime, serverTime);
  }

  public DataValue withSourceTime(@Nullable DateTime sourceTime) {
    return new DataValue(value, statusCode, sourceTime, serverTime);
  }

  public DataValue withServerTime(@Nullable DateTime serverTime) {
    return new DataValue(value, statusCode, sourceTime, serverTime);
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
        from.statusCode,
        includeSource ? from.sourceTime : null,
        includeSource ? from.sourcePicoseconds : null,
        includeServer ? DateTime.now() : null,
        null);
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

    return new DataValue(from.value, from.statusCode, null, includeServer ? DateTime.now() : null);
  }

  /**
   * Create a {@link DataValue} containing only a value.
   *
   * <p>{@link StatusCode#GOOD} is implied, and other fields will be null.
   *
   * @param v the value {@link Variant}.
   * @return a {@link DataValue} containing only the value.
   */
  public static DataValue valueOnly(Variant v) {
    return new DataValue(v, StatusCode.GOOD, null, null, null, null);
  }

  public static class Builder {

    public Variant value = Variant.NULL_VALUE;
    public StatusCode status = StatusCode.GOOD;
    public @Nullable DateTime sourceTime;
    public @Nullable UShort sourcePicoseconds;
    public @Nullable DateTime serverTime;
    public @Nullable UShort serverPicoseconds;

    public Builder() {}

    public Builder(DataValue other) {
      this.value = other.value;
      this.status = other.statusCode;
      this.sourceTime = other.sourceTime;
      this.sourcePicoseconds = other.sourcePicoseconds;
      this.serverTime = other.serverTime;
      this.serverPicoseconds = other.serverPicoseconds;
    }

    public Builder setValue(Variant value) {
      this.value = value;
      return this;
    }

    public Builder setStatus(long statusCode) {
      this.status = new StatusCode(statusCode);
      return this;
    }

    public Builder setStatus(StatusCode status) {
      this.status = status;
      return this;
    }

    public Builder setSourceTime(@Nullable DateTime sourceTime) {
      this.sourceTime = sourceTime;
      return this;
    }

    public Builder setSourcePicoseconds(@Nullable UShort sourcePicoseconds) {
      this.sourcePicoseconds = sourcePicoseconds;
      return this;
    }

    public Builder setServerTime(@Nullable DateTime serverTime) {
      this.serverTime = serverTime;
      return this;
    }

    public Builder setServerPicoseconds(@Nullable UShort serverPicoseconds) {
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
          setServerPicoseconds(null);
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
