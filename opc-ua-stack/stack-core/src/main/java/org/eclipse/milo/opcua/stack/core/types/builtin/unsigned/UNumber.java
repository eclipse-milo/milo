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

import java.math.BigInteger;

/**
 * A base type for unsigned numbers.
 *
 * @author Lukas Eder
 */
public abstract class UNumber extends Number {

  /** Generated UID */
  private static final long serialVersionUID = -7666221938815339843L;

  /**
   * Get this number as a {@link BigInteger}. This is a convenience method for calling <code>
   * new BigInteger(toString())</code>
   */
  public BigInteger toBigInteger() {
    return new BigInteger(toString());
  }

  /**
   * Figure out the size of a value precache from a system property.
   *
   * @param propertyName the name of the system property holding the requested size.
   * @param defaultSize the size to use if the property is not set, empty, not a number, or cannot
   *     be read.
   * @param maxSize the maximum size the parsed value is capped at.
   * @return the precache size; if the parsed value is zero or negative no cache should be created.
   */
  static int getPrecacheSize(String propertyName, int defaultSize, long maxSize) {
    String prop;
    long propParsed;

    try {
      prop = System.getProperty(propertyName);
    } catch (SecurityException e) {
      // security manager stopped us so use default
      return defaultSize;
    }
    if (prop == null) return defaultSize;
    if (prop.isEmpty()) return defaultSize;
    try {
      propParsed = Long.parseLong(prop);
    } catch (NumberFormatException e) {
      // not a valid number
      return defaultSize;
    }
    // treat negative value as no cache...
    if (propParsed < 0) return 0;
    return (int) Math.min(propParsed, maxSize);
  }
}
