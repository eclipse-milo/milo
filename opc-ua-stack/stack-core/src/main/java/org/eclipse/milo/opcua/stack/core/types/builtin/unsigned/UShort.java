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

import org.jspecify.annotations.NonNull;

/**
 * The <code>unsigned short</code> type
 *
 * @author Lukas Eder
 * @author Jens Nerche
 */
public final class UShort extends UNumber implements Comparable<UShort> {

  /** Generated UID */
  private static final long serialVersionUID = -6821055240959745390L;

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
   * Create an <code>unsigned short</code>
   *
   * @throws NumberFormatException If <code>value</code> does not contain a parsable <code>
   *     unsigned short</code>.
   */
  public static UShort valueOf(String value) throws NumberFormatException {
    return new UShort(value);
  }

  /**
   * Create an <code>unsigned short</code> by masking it with <code>0xFFFF</code> i.e. <code>
   * (short) -1</code> becomes <code>(ushort) 65535</code>
   */
  public static UShort valueOf(short value) {
    return new UShort(value);
  }

  /**
   * Create an <code>unsigned short</code>
   *
   * @throws NumberFormatException If <code>value</code> is not in the range of an <code>
   *     unsigned short</code>
   */
  public static UShort valueOf(int value) throws NumberFormatException {
    return new UShort(value);
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
    return new UShort((int) value);
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

  /**
   * Create an <code>unsigned short</code> by masking it with <code>0xFFFF</code> i.e. <code>
   * (short) -1</code> becomes <code>(ushort) 65535</code>
   */
  private UShort(short value) {
    this.value = value & MAX_VALUE;
  }

  /**
   * Create an <code>unsigned short</code>
   *
   * @throws NumberFormatException If <code>value</code> does not contain a parsable <code>
   *     unsigned short</code>.
   */
  private UShort(String value) throws NumberFormatException {
    this.value = Integer.parseInt(value);
    rangeCheck();
  }

  private void rangeCheck() throws NumberFormatException {
    if (value < MIN_VALUE || value > MAX_VALUE) {
      throw new NumberFormatException("Value is out of range : " + value);
    }
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
