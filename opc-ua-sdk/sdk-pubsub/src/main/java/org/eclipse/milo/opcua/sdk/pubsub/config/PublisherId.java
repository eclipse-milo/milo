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

import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;

/**
 * The identifier of a publisher within a PubSub network.
 *
 * <p>Part 14 restricts the PublisherId to one of the unsigned integer types or String; each
 * permitted record corresponds to one of the allowed wire representations.
 *
 * <p>This interface is sealed; adding a permitted implementation is a source-compatible change only
 * for callers that switch over instances using a {@code default} branch. Always include a {@code
 * default} branch when switching over {@link PublisherId} instances.
 */
public sealed interface PublisherId
    permits PublisherId.ByteId,
        PublisherId.UInt16Id,
        PublisherId.UInt32Id,
        PublisherId.UInt64Id,
        PublisherId.StringId {

  /**
   * Create a {@link PublisherId} backed by a {@link UByte} value.
   *
   * @param value the publisher id value.
   * @return a new {@link PublisherId}.
   */
  static PublisherId ubyte(UByte value) {
    return new ByteId(value);
  }

  /**
   * Create a {@link PublisherId} backed by a {@link UShort} value.
   *
   * @param value the publisher id value.
   * @return a new {@link PublisherId}.
   */
  static PublisherId uint16(UShort value) {
    return new UInt16Id(value);
  }

  /**
   * Create a {@link PublisherId} backed by a {@link UInteger} value.
   *
   * @param value the publisher id value.
   * @return a new {@link PublisherId}.
   */
  static PublisherId uint32(UInteger value) {
    return new UInt32Id(value);
  }

  /**
   * Create a {@link PublisherId} backed by a {@link ULong} value.
   *
   * @param value the publisher id value.
   * @return a new {@link PublisherId}.
   */
  static PublisherId uint64(ULong value) {
    return new UInt64Id(value);
  }

  /**
   * Create a {@link PublisherId} backed by a {@link String} value.
   *
   * @param value the publisher id value.
   * @return a new {@link PublisherId}.
   */
  static PublisherId string(String value) {
    return new StringId(value);
  }

  /**
   * Get the {@link Variant} representation of this {@link PublisherId}, as used by the Part 14
   * configuration datatypes.
   *
   * @return the {@link Variant} representation of this {@link PublisherId}.
   */
  Variant toVariant();

  /**
   * Get the canonical String form of this {@link PublisherId}: the value itself for a String id,
   * the decimal representation without leading zeros for the unsigned integer ids.
   *
   * <p>This is the form mandated wherever Part 14 carries a PublisherId as a String — the JSON
   * message mapping (§7.2.5.3 Table 184) and the standardized broker topic tree (§7.3.4.7.1) — and
   * the form used to match ids of differing types: ids of different types are considered equal when
   * their canonical String forms are equal.
   *
   * @return the canonical String form of this {@link PublisherId}.
   */
  default String toCanonicalString() {
    if (this instanceof ByteId id) {
      return id.value().toString();
    } else if (this instanceof UInt16Id id) {
      return id.value().toString();
    } else if (this instanceof UInt32Id id) {
      return id.value().toString();
    } else if (this instanceof UInt64Id id) {
      return id.value().toString();
    } else if (this instanceof StringId id) {
      return id.value();
    } else {
      // unreachable: the interface is sealed over the cases above
      throw new IllegalStateException("unknown PublisherId type: " + getClass().getName());
    }
  }

  /**
   * Derive a {@link PublisherId} from the {@link Variant} representation used by the Part 14
   * configuration datatypes.
   *
   * @param variant a {@link Variant} containing a UByte, UInt16, UInt32, UInt64, or String value.
   * @return the corresponding {@link PublisherId}.
   * @throws PubSubConfigValidationException if the Variant value is not one of the types permitted
   *     by Part 14.
   */
  static PublisherId fromVariant(Variant variant) {
    Object value = variant.value();

    if (value instanceof UByte v) {
      return new ByteId(v);
    } else if (value instanceof UShort v) {
      return new UInt16Id(v);
    } else if (value instanceof UInteger v) {
      return new UInt32Id(v);
    } else if (value instanceof ULong v) {
      return new UInt64Id(v);
    } else if (value instanceof String v) {
      return new StringId(v);
    } else {
      throw new PubSubConfigValidationException(
          "publisherId: unsupported Variant value: "
              + (value == null ? "null" : value.getClass().getName()));
    }
  }

  /**
   * A {@link PublisherId} backed by a {@link UByte} value.
   *
   * @param value the publisher id value.
   */
  record ByteId(UByte value) implements PublisherId {
    @Override
    public Variant toVariant() {
      return Variant.ofByte(value);
    }
  }

  /**
   * A {@link PublisherId} backed by a {@link UShort} value.
   *
   * @param value the publisher id value.
   */
  record UInt16Id(UShort value) implements PublisherId {
    @Override
    public Variant toVariant() {
      return Variant.ofUInt16(value);
    }
  }

  /**
   * A {@link PublisherId} backed by a {@link UInteger} value.
   *
   * @param value the publisher id value.
   */
  record UInt32Id(UInteger value) implements PublisherId {
    @Override
    public Variant toVariant() {
      return Variant.ofUInt32(value);
    }
  }

  /**
   * A {@link PublisherId} backed by a {@link ULong} value.
   *
   * @param value the publisher id value.
   */
  record UInt64Id(ULong value) implements PublisherId {
    @Override
    public Variant toVariant() {
      return Variant.ofUInt64(value);
    }
  }

  /**
   * A {@link PublisherId} backed by a {@link String} value.
   *
   * @param value the publisher id value.
   */
  record StringId(String value) implements PublisherId {
    @Override
    public Variant toVariant() {
      return Variant.ofString(value);
    }
  }
}
