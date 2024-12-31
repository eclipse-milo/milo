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

/** A utility to assist code-generated classes in implementing hashCode() methods. */
public class HashCodeBuilder {

  private static final int CONSTANT = 37;

  private int total = 17;

  public HashCodeBuilder append(boolean value) {
    total = total * CONSTANT + Boolean.hashCode(value);
    return this;
  }

  public HashCodeBuilder append(boolean[] value) {
    if (value == null) {
      total = total * CONSTANT;
    } else {
      for (boolean b : value) {
        append(b);
      }
    }
    return this;
  }

  public HashCodeBuilder append(byte value) {
    total = total * CONSTANT + Byte.hashCode(value);
    return this;
  }

  public HashCodeBuilder append(byte[] value) {
    if (value == null) {
      total = total * CONSTANT;
    } else {
      for (byte b : value) {
        append(b);
      }
    }
    return this;
  }

  public HashCodeBuilder append(char value) {
    total = total * CONSTANT + Character.hashCode(value);
    return this;
  }

  public HashCodeBuilder append(char[] value) {
    if (value == null) {
      total = total * CONSTANT;
    } else {
      for (char c : value) {
        append(c);
      }
    }
    return this;
  }

  public HashCodeBuilder append(short value) {
    total = total * CONSTANT + Short.hashCode(value);
    return this;
  }

  public HashCodeBuilder append(short[] value) {
    if (value == null) {
      total = total * CONSTANT;
    } else {
      for (short s : value) {
        append(s);
      }
    }
    return this;
  }

  public HashCodeBuilder append(int value) {
    total = total * CONSTANT + Integer.hashCode(value);
    return this;
  }

  public HashCodeBuilder append(int[] value) {
    if (value == null) {
      total = total * CONSTANT;
    } else {
      for (int i : value) {
        append(i);
      }
    }
    return this;
  }

  public HashCodeBuilder append(long value) {
    total = total * CONSTANT + Long.hashCode(value);
    return this;
  }

  public HashCodeBuilder append(long[] value) {
    if (value == null) {
      total = total * CONSTANT;
    } else {
      for (long l : value) {
        append(l);
      }
    }
    return this;
  }

  public HashCodeBuilder append(float value) {
    total = total * CONSTANT + Float.hashCode(value);
    return this;
  }

  public HashCodeBuilder append(float[] value) {
    if (value == null) {
      total = total * CONSTANT;
    } else {
      for (float f : value) {
        append(f);
      }
    }
    return this;
  }

  public HashCodeBuilder append(double value) {
    total = total * CONSTANT + Double.hashCode(value);
    return this;
  }

  public HashCodeBuilder append(double[] value) {
    if (value == null) {
      total = total * CONSTANT;
    } else {
      for (double d : value) {
        append(d);
      }
    }
    return this;
  }

  public HashCodeBuilder append(Object value) {
    if (value == null) {
      total = total * CONSTANT;
    } else if (value.getClass().isArray()) {
      if (value instanceof boolean[]) {
        append((boolean[]) value);
      } else if (value instanceof byte[]) {
        append((byte[]) value);
      } else if (value instanceof char[]) {
        append((char[]) value);
      } else if (value instanceof short[]) {
        append((short[]) value);
      } else if (value instanceof int[]) {
        append((int[]) value);
      } else if (value instanceof long[]) {
        append((long[]) value);
      } else if (value instanceof float[]) {
        append((float[]) value);
      } else if (value instanceof double[]) {
        append((double[]) value);
      } else {
        append((Object[]) value);
      }
    } else {
      total = total * CONSTANT + value.hashCode();
    }
    return this;
  }

  public HashCodeBuilder append(Object[] value) {
    if (value == null) {
      total = total * CONSTANT;
    } else {
      for (Object o : value) {
        append(o);
      }
    }
    return this;
  }

  public HashCodeBuilder appendSuper(int superHashCode) {
    total = total * CONSTANT + superHashCode;
    return this;
  }

  public int build() {
    return total;
  }
}
