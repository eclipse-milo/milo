/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util.codegen;

/** A utility to assist code-generated classes in implementing equals() methods. */
public class EqualsBuilder {

  private boolean result = true;

  public EqualsBuilder append(boolean lhs, boolean rhs) {
    if (!result) {
      return this;
    }
    result = lhs == rhs;
    return this;
  }

  public EqualsBuilder append(boolean[] lhs, boolean[] rhs) {
    if (!result) {
      return this;
    }
    if (lhs == null && rhs == null) {
      return this;
    }
    if (lhs == null || rhs == null) {
      result = false;
      return this;
    }
    if (lhs.length != rhs.length) {
      result = false;
      return this;
    }
    for (int i = 0; i < lhs.length && result; i++) {
      append(lhs[i], rhs[i]);
    }
    return this;
  }

  public EqualsBuilder append(byte lhs, byte rhs) {
    if (!result) {
      return this;
    }
    result = lhs == rhs;
    return this;
  }

  public EqualsBuilder append(byte[] lhs, byte[] rhs) {
    if (!result) {
      return this;
    }
    if (lhs == null && rhs == null) {
      return this;
    }
    if (lhs == null || rhs == null) {
      result = false;
      return this;
    }
    if (lhs.length != rhs.length) {
      result = false;
      return this;
    }
    for (int i = 0; i < lhs.length && result; i++) {
      append(lhs[i], rhs[i]);
    }
    return this;
  }

  public EqualsBuilder append(char lhs, char rhs) {
    if (!result) {
      return this;
    }
    result = lhs == rhs;
    return this;
  }

  public EqualsBuilder append(char[] lhs, char[] rhs) {
    if (!result) {
      return this;
    }
    if (lhs == null && rhs == null) {
      return this;
    }
    if (lhs == null || rhs == null) {
      result = false;
      return this;
    }
    if (lhs.length != rhs.length) {
      result = false;
      return this;
    }
    for (int i = 0; i < lhs.length && result; i++) {
      append(lhs[i], rhs[i]);
    }
    return this;
  }

  public EqualsBuilder append(short lhs, short rhs) {
    if (!result) {
      return this;
    }
    result = lhs == rhs;
    return this;
  }

  public EqualsBuilder append(short[] lhs, short[] rhs) {
    if (!result) {
      return this;
    }
    if (lhs == null && rhs == null) {
      return this;
    }
    if (lhs == null || rhs == null) {
      result = false;
      return this;
    }
    if (lhs.length != rhs.length) {
      result = false;
      return this;
    }
    for (int i = 0; i < lhs.length && result; i++) {
      append(lhs[i], rhs[i]);
    }
    return this;
  }

  public EqualsBuilder append(int lhs, int rhs) {
    if (!result) {
      return this;
    }
    result = lhs == rhs;
    return this;
  }

  public EqualsBuilder append(int[] lhs, int[] rhs) {
    if (!result) {
      return this;
    }
    if (lhs == null && rhs == null) {
      return this;
    }
    if (lhs == null || rhs == null) {
      result = false;
      return this;
    }
    if (lhs.length != rhs.length) {
      result = false;
      return this;
    }
    for (int i = 0; i < lhs.length && result; i++) {
      append(lhs[i], rhs[i]);
    }
    return this;
  }

  public EqualsBuilder append(long lhs, long rhs) {
    if (!result) {
      return this;
    }
    result = lhs == rhs;
    return this;
  }

  public EqualsBuilder append(long[] lhs, long[] rhs) {
    if (!result) {
      return this;
    }
    if (lhs == null && rhs == null) {
      return this;
    }
    if (lhs == null || rhs == null) {
      result = false;
      return this;
    }
    if (lhs.length != rhs.length) {
      result = false;
      return this;
    }
    for (int i = 0; i < lhs.length && result; i++) {
      append(lhs[i], rhs[i]);
    }
    return this;
  }

  public EqualsBuilder append(float lhs, float rhs) {
    if (!result) {
      return this;
    }
    result = Float.compare(lhs, rhs) == 0;
    return this;
  }

  public EqualsBuilder append(float[] lhs, float[] rhs) {
    if (!result) {
      return this;
    }
    if (lhs == null && rhs == null) {
      return this;
    }
    if (lhs == null || rhs == null) {
      result = false;
      return this;
    }
    if (lhs.length != rhs.length) {
      result = false;
      return this;
    }
    for (int i = 0; i < lhs.length && result; i++) {
      append(lhs[i], rhs[i]);
    }
    return this;
  }

  public EqualsBuilder append(double lhs, double rhs) {
    if (!result) {
      return this;
    }
    result = Double.compare(lhs, rhs) == 0;
    return this;
  }

  public EqualsBuilder append(double[] lhs, double[] rhs) {
    if (!result) {
      return this;
    }
    if (lhs == null && rhs == null) {
      return this;
    }
    if (lhs == null || rhs == null) {
      result = false;
      return this;
    }
    if (lhs.length != rhs.length) {
      result = false;
      return this;
    }
    for (int i = 0; i < lhs.length && result; i++) {
      append(lhs[i], rhs[i]);
    }
    return this;
  }

  public EqualsBuilder append(Object lhs, Object rhs) {
    if (!result) {
      return this;
    }
    if (lhs == rhs) {
      return this;
    }
    if (lhs == null || rhs == null) {
      result = false;
      return this;
    }
    if (lhs.getClass().isArray()) {
      if (lhs instanceof boolean[]) {
        append((boolean[]) lhs, (boolean[]) rhs);
      } else if (lhs instanceof byte[]) {
        append((byte[]) lhs, (byte[]) rhs);
      } else if (lhs instanceof char[]) {
        append((char[]) lhs, (char[]) rhs);
      } else if (lhs instanceof short[]) {
        append((short[]) lhs, (short[]) rhs);
      } else if (lhs instanceof int[]) {
        append((int[]) lhs, (int[]) rhs);
      } else if (lhs instanceof long[]) {
        append((long[]) lhs, (long[]) rhs);
      } else if (lhs instanceof float[]) {
        append((float[]) lhs, (float[]) rhs);
      } else if (lhs instanceof double[]) {
        append((double[]) lhs, (double[]) rhs);
      } else {
        append((Object[]) lhs, (Object[]) rhs);
      }
    } else {
      result = lhs.equals(rhs);
    }
    return this;
  }

  public EqualsBuilder append(Object[] lhs, Object[] rhs) {
    if (!result) {
      return this;
    }
    if (lhs == null && rhs == null) {
      return this;
    }
    if (lhs == null || rhs == null) {
      result = false;
      return this;
    }
    if (lhs.length != rhs.length) {
      result = false;
      return this;
    }
    for (int i = 0; i < lhs.length && result; i++) {
      append(lhs[i], rhs[i]);
    }
    return this;
  }

  public EqualsBuilder appendSuper(boolean superEquals) {
    if (!result) {
      return this;
    }
    result = superEquals;
    return this;
  }

  public boolean build() {
    return result;
  }
}
