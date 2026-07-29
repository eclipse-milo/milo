/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.builtin.unsigned;

import java.io.ObjectStreamException;
import org.jspecify.annotations.NonNull;

/**
 * The <code>unsigned short</code> type
 *
 * @author Lukas Eder
 * @author Jens Nerche
 */
public final class UShort extends UNumber implements Comparable<UShort> {

  private static final Class<UShort> CLASS = UShort.class;
  private static final String CLASS_NAME = CLASS.getName();

  /** System property name for the property to set the size of the pre-cache. */
  private static final String PRECACHE_PROPERTY = CLASS_NAME + ".precacheSize";

  /** Default size for the value cache. */
  private static final int DEFAULT_PRECACHE_SIZE = 256;

  /** Generated UID */
  private static final long serialVersionUID = -6821055240959745390L;

  /**
   * Cached values.
   *
   * <p>Declared before {@link #MIN} and {@link #MAX} so that their initializers can use the cache.
   */
  private static final UShort[] VALUES = mkValues();

  /** A constant holding the minimum value an <code>unsigned short</code> can have, 0. */
  public static final int MIN_VALUE = 0x0000;

  /**
   * A constant holding the maximum value an <code>unsigned short</code> can have, 2<sup>16</sup>-1.
   */
  public static final int MAX_VALUE = 0xffff;

  /** A constant holding the minimum value an <code>unsigned short</code> can have as UShort, 0. */
  public static final UShort MIN = valueOf(MIN_VALUE);

  /**
   * A constant holding the maximum value an <code>unsigned short</code> can have as UShort,
   * 2<sup>16</sup>-1.
   */
  public static final UShort MAX = valueOf(MAX_VALUE);

  /** The value modelling the content of this <code>unsigned short</code> */
  private final int value;

  /**
   * Figure out the size of the precache.
   *
   * @return The parsed value of the system property {@link #PRECACHE_PROPERTY} or {@link
   *     #DEFAULT_PRECACHE_SIZE} if the property is not set, not a number or retrieving results in a
   *     {@link SecurityException}. If the parsed value is zero or negative no cache will be
   *     created. The value is capped at {@link #MAX_VALUE}+1, the number of distinct <code>
   *     unsigned short
   *     </code> values.
   */
  private static int getPrecacheSize() {
    String prop;
    long propParsed;

    try {
      prop = System.getProperty(PRECACHE_PROPERTY);
    } catch (SecurityException e) {
      // security manager stopped us so use default
      return DEFAULT_PRECACHE_SIZE;
    }
    if (prop == null) return DEFAULT_PRECACHE_SIZE;
    if (prop.isEmpty()) return DEFAULT_PRECACHE_SIZE;
    try {
      propParsed = Long.parseLong(prop);
    } catch (NumberFormatException e) {
      // not a valid number
      return DEFAULT_PRECACHE_SIZE;
    }
    // treat negative value as no cache...
    if (propParsed < 0) return 0;
    return (int) Math.min(propParsed, MAX_VALUE + 1L);
  }

  /**
   * Generate a cached value for initial unsigned short values.
   *
   * @return Array of cached values for UShort, or null if caching is disabled.
   */
  private static UShort[] mkValues() {
    int precacheSize = getPrecacheSize();

    if (precacheSize <= 0) return null;

    UShort[] ret = new UShort[precacheSize];
    for (int i = 0; i < precacheSize; i++) ret[i] = new UShort(i);
    return ret;
  }

  /**
   * Retrieve a cached value.
   *
   * @param value Cached value to retrieve
   * @return Cached value if one exists. Null otherwise.
   */
  private static UShort getCached(int value) {
    if (VALUES != null && value >= 0 && value < VALUES.length) return VALUES[value];
    return null;
  }

  /**
   * Create an <code>unsigned short</code>
   *
   * @throws NumberFormatException If <code>value</code> does not contain a parsable <code>
   *     unsigned short</code>.
   */
  public static UShort valueOf(String value) throws NumberFormatException {
    return valueOf(Integer.parseInt(value));
  }

  /**
   * Create an <code>unsigned short</code> by masking it with <code>0xFFFF</code> i.e. <code>
   * (short) -1</code> becomes <code>(ushort) 65535</code>
   */
  public static UShort valueOf(short value) {
    int masked = value & MAX_VALUE;

    UShort cached = getCached(masked);

    return cached != null ? cached : new UShort(masked);
  }

  /**
   * Create an <code>unsigned short</code>
   *
   * @throws NumberFormatException If <code>value</code> is not in the range of an <code>
   *     unsigned short</code>
   */
  public static UShort valueOf(int value) throws NumberFormatException {
    UShort cached = getCached(value);

    // out-of-range values are never cached, so new UShort(int) still range checks them
    return cached != null ? cached : new UShort(value);
  }

  /**
   * Create an <code>unsigned short</code>
   *
   * @throws NumberFormatException If <code>value</code> is not in the range of an <code>
   *     unsigned short</code>
   */
  public static UShort valueOf(long value) throws NumberFormatException {
    if (value < MIN_VALUE || value > MAX_VALUE) {
      throw new NumberFormatException("Value is out of range : " + value);
    }
    return valueOf((int) value);
  }

  /**
   * Create an <code>unsigned short</code>
   *
   * @throws NumberFormatException If <code>value</code> is not in the range of an <code>
   *     unsigned short</code>
   */
  private UShort(int value) throws NumberFormatException {
    this.value = value;
    rangeCheck();
  }

  private void rangeCheck() throws NumberFormatException {
    if (value < MIN_VALUE || value > MAX_VALUE) {
      throw new NumberFormatException("Value is out of range : " + value);
    }
  }

  /**
   * Replace version read through deserialization with cached version.
   *
   * @return cached instance of this object's value if one exists, otherwise this object
   */
  private Object readResolve() throws ObjectStreamException {
    // the value read could be invalid so check it
    rangeCheck();

    UShort cached = getCached(value);

    return cached != null ? cached : this;
  }

  @Override
  public int intValue() {
    return value;
  }

  @Override
  public long longValue() {
    return value;
  }

  @Override
  public float floatValue() {
    return value;
  }

  @Override
  public double doubleValue() {
    return value;
  }

  @Override
  public int hashCode() {
    return Integer.valueOf(value).hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof UShort) {
      return value == ((UShort) obj).value;
    }

    return false;
  }

  @Override
  public String toString() {
    return Integer.toString(value);
  }

  @Override
  public int compareTo(@NonNull UShort o) {
    return Integer.compare(value, o.value);
  }

  public UShort add(UShort val) throws NumberFormatException {
    return valueOf(value + val.value);
  }

  public UShort add(int val) throws NumberFormatException {
    return valueOf(value + val);
  }

  public UShort subtract(final UShort val) {
    return valueOf(value - val.value);
  }

  public UShort subtract(final int val) {
    return valueOf(value - val);
  }
}
