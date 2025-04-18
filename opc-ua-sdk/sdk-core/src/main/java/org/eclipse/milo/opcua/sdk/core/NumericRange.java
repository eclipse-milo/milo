/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import java.lang.reflect.Array;
import java.util.Arrays;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;

public final class NumericRange {

  private final String range;
  private final Bounds[] bounds;

  public NumericRange(String range, Bounds[] bounds) {
    this.range = range;
    this.bounds = bounds;
  }

  public String getRange() {
    return range;
  }

  public Bounds[] getBounds() {
    return bounds;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this).add("range", range).toString();
  }

  private int getDimensionCount() {
    return bounds.length;
  }

  private Bounds getDimensionBounds(int dimension) {
    return bounds[dimension - 1];
  }

  public static final class Bounds {
    private final int low;
    private final int high;

    private Bounds(int low, int high) throws UaException {
      if (low < 0 || high < 0 || low > high)
        throw new UaException(StatusCodes.Bad_IndexRangeInvalid);

      this.low = low;
      this.high = high;
    }

    public int getLow() {
      return low;
    }

    public int getHigh() {
      return high;
    }

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this).add("low", low).add("high", high).toString();
    }
  }

  public static NumericRange parse(String range) throws UaException {
    Preconditions.checkArgument(
        range != null && !range.isEmpty(), "range must not be null or empty");

    try {
      String[] ss = range.split(",");
      Bounds[] bounds = new Bounds[ss.length];

      for (int i = 0; i < ss.length; i++) {
        String s = ss[i];
        String[] bs = s.split(":");

        if (bs.length == 1) {
          int index = Integer.parseInt(bs[0]);
          bounds[i] = new Bounds(index, index);
        } else if (bs.length == 2) {
          int low = Integer.parseInt(bs[0]);
          int high = Integer.parseInt(bs[1]);

          if (low == high) throw new UaException(StatusCodes.Bad_IndexRangeInvalid);

          bounds[i] = new Bounds(low, high);
        } else {
          throw new UaException(StatusCodes.Bad_IndexRangeInvalid);
        }
      }

      return new NumericRange(range, bounds);
    } catch (Throwable ex) {
      throw new UaException(StatusCodes.Bad_IndexRangeInvalid, ex);
    }
  }

  public static Object readFromValueAtRange(Variant value, NumericRange range) throws UaException {
    return readFromValueAtRange(value.value(), range);
  }

  public static Object readFromValueAtRange(Object array, NumericRange range) throws UaException {
    if (array == null) {
      throw new UaException(StatusCodes.Bad_IndexRangeNoData);
    }

    if (!array.getClass().isArray()) {
      if (!(array instanceof String) && !(array instanceof ByteString)) {
        throw new UaException(StatusCodes.Bad_IndexRangeNoData);
      }
    }

    try {
      return readFromValueAtRange(array, range, 1);
    } catch (Throwable ex) {
      throw new UaException(StatusCodes.Bad_IndexRangeNoData, ex);
    }
  }

  private static Object readFromValueAtRange(Object array, NumericRange range, int dimension)
      throws UaException {
    int dimensionCount = range.getDimensionCount();
    Bounds bounds = range.getDimensionBounds(dimension);
    int low = bounds.getLow();
    int high = bounds.getHigh();

    if (dimension == dimensionCount) {
      if (array.getClass().isArray()) {
        int length = Array.getLength(array);
        if (low >= length) {
          throw new UaException(StatusCodes.Bad_IndexRangeNoData);
        }
        int len = Math.min(high + 1, length) - low;
        Class<?> type = array.getClass().getComponentType();
        Object a = Array.newInstance(type, len);

        for (int i = 0; i < len; i++) {
          Object element = Array.get(array, low + i);
          Array.set(a, i, element);
        }

        return a;
      } else if (array instanceof String s) {
        int length = s.length();
        if (low >= length) {
          throw new UaException(StatusCodes.Bad_IndexRangeNoData);
        }
        int to = Math.min(high + 1, length);
        return s.substring(low, to);
      } else if (array instanceof ByteString bs) {
        int length = bs.length();
        if (low >= length) {
          throw new UaException(StatusCodes.Bad_IndexRangeNoData);
        }
        int to = Math.min(high + 1, length);
        byte[] copy = Arrays.copyOfRange(bs.bytesOrEmpty(), low, to);
        return new ByteString(copy);
      } else {
        throw new UaException(StatusCodes.Bad_IndexRangeNoData);
      }
    } else {
      int len = Math.min(high + 1, Array.getLength(array)) - low;
      Class<?> type = array.getClass().getComponentType();
      Object a = Array.newInstance(type, len);

      for (int i = 0; i < len; i++) {
        Object na = Array.get(array, low + i);
        Object element = readFromValueAtRange(na, range, dimension + 1);
        Array.set(a, i, element);
      }

      return a;
    }
  }

  public static Object writeToValueAtRange(
      Variant currentVariant, Variant updateVariant, NumericRange range) throws UaException {

    Object current = currentVariant.value();
    Object update = updateVariant.value();

    return writeToValueAtRange(current, update, range);
  }

  public static Object writeToValueAtRange(Object current, Object update, NumericRange range)
      throws UaException {

    if (current == null || update == null) {
      throw new UaException(StatusCodes.Bad_IndexRangeNoData);
    }

    try {
      return writeToValueAtRange(current, update, range, 1);
    } catch (Throwable ex) {
      throw new UaException(StatusCodes.Bad_IndexRangeNoData, ex);
    }
  }

  private static Object writeToValueAtRange(
      Object current, Object update, NumericRange range, int dimension) throws UaException {

    int dimensionCount = range.getDimensionCount();
    Bounds bounds = range.getDimensionBounds(dimension);
    int low = bounds.getLow();
    int high = bounds.getHigh();

    if (dimension == dimensionCount) {
      if (current.getClass().isArray()) {
        Class<?> currentType = ArrayUtil.getType(current);
        Class<?> updateType = ArrayUtil.getType(update);

        if (currentType != updateType) {
          throw new UaException(
              StatusCodes.Bad_TypeMismatch,
              String.format("currentType=%s, updateType=%s", current, update));
        }

        int length = Array.getLength(current);
        Object copy = Array.newInstance(currentType, length);

        if (low >= length || high >= length) {
          throw new UaException(StatusCodes.Bad_IndexRangeNoData);
        }

        for (int i = 0; i < length; i++) {
          if (i < low || i > high) {
            Object element = Array.get(current, i);
            Array.set(copy, i, element);
          } else {
            Object element = Array.get(update, i - low);
            Array.set(copy, i, element);
          }
        }

        return copy;
      } else if (current instanceof String cs) {
        String us = (String) update;
        int length = cs.length();
        StringBuilder copy = new StringBuilder();

        if (low >= length || high >= length) {
          throw new UaException(StatusCodes.Bad_IndexRangeNoData);
        }

        for (int i = 0; i < length; i++) {
          if (i < low || i > high) {
            copy.append(cs.charAt(i));
          } else {
            copy.append(us.charAt(i - low));
          }
        }

        return copy.toString();
      } else if (current instanceof ByteString bs) {
        ByteString us = (ByteString) update;
        int length = bs.length();
        byte[] copy = new byte[length];

        if (low >= length || high >= length) {
          throw new UaException(StatusCodes.Bad_IndexRangeNoData);
        }

        for (int i = 0; i < length; i++) {
          if (i < low || i > high) {
            copy[i] = bs.byteAt(i);
          } else {
            copy[i] = us.byteAt(i - low);
          }
        }

        return new ByteString(copy);
      } else {
        throw new UaException(StatusCodes.Bad_IndexRangeNoData);
      }
    } else {
      Class<?> type = current.getClass().getComponentType();
      int length = Array.getLength(current);
      Object copy = Array.newInstance(type, length);

      if (low >= length || high >= length) {
        throw new UaException(StatusCodes.Bad_IndexRangeNoData);
      }

      for (int i = 0; i < length; i++) {
        if (i < low || i > high) {
          Object element = Array.get(current, i);
          Array.set(copy, i, element);
        } else {
          Object c = Array.get(current, i);
          Object u = Array.get(update, i - low);
          Object element = writeToValueAtRange(c, u, range, dimension + 1);
          Array.set(copy, i, element);
        }
      }

      return copy;
    }
  }
}
