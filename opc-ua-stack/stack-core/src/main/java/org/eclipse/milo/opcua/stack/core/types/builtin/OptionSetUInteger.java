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

import java.util.Collection;
import java.util.Set;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UNumber;

public abstract class OptionSetUInteger<F extends Enum<F> & OptionSetUInteger.BitIndex>
    extends UNumber {

  protected final UNumber value;

  OptionSetUInteger(UNumber value) {
    this.value = value;
  }

  public UNumber getValue() {
    return value;
  }

  @Override
  public final int intValue() {
    return value.intValue();
  }

  @Override
  public final long longValue() {
    return value.longValue();
  }

  @Override
  public final float floatValue() {
    return value.floatValue();
  }

  @Override
  public final double doubleValue() {
    return value.doubleValue();
  }

  /**
   * Returns the value of the bit at {@code field}'s index.
   *
   * @param field the field
   * @return {@code true} if the bit at {@code field}'s index is set.
   */
  public final boolean get(F field) {
    return get(field.getBitIndex());
  }

  /**
   * Returns the value of the bit at {@code bitIndex}.
   *
   * @param bitIndex the bit index.
   * @return {@code true} if the bit at {@code bitIndex} is set.
   */
  public final boolean get(int bitIndex) {
    return ((longValue() >>> bitIndex) & 1) == 1;
  }

  /**
   * Check if all bit {@code fields} are set.
   *
   * @param fields the fields to check.
   * @return {@code true} if all bit {@code fields} are set.
   */
  @SafeVarargs
  public final boolean containsAll(F... fields) {
    boolean b = true;
    for (F f : fields) {
      b &= get(f);
    }
    return b;
  }

  /**
   * Check if all bit {@code fields} are set.
   *
   * @param fields the fields to check.
   * @return {@code true} if all bit {@code fields} are set.
   */
  public final boolean containsAll(Collection<F> fields) {
    boolean b = true;
    for (F f : fields) {
      b &= get(f);
    }
    return b;
  }

  /**
   * Get the {@link Set} of fields that have their bit set.
   *
   * @return the {@link Set} of fields that have their bit set.
   */
  public abstract Set<F> toSet();

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    OptionSetUInteger<?> that = (OptionSetUInteger<?>) o;
    return value.equals(that.value);
  }

  @Override
  public int hashCode() {
    return value.hashCode();
  }

  /** Identifiers a type that has a corresponding bit index. */
  public interface BitIndex {

    /**
     * @return the index of some bit that corresponds with this type.
     */
    int getBitIndex();
  }
}
