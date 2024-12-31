/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.util;

import com.google.common.base.Preconditions;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.function.Function;
import java.util.stream.IntStream;

public class ArrayUtil {

  /**
   * Flatten a multi-dimensional array into a one-dimensional array.
   *
   * @param array the array to flatten.
   * @return a 1-dimensional array.
   */
  public static Object flatten(Object array) {
    Preconditions.checkArgument(array.getClass().isArray(), "array");

    Class<?> type = getType(array);
    int[] dimensions = getDimensions(array);
    int length = length(dimensions);

    Object flattened = Array.newInstance(type, length);

    flatten(array, flattened, dimensions, 0);

    return flattened;
  }

  private static void flatten(Object array, Object flattened, int[] dimensions, int offset) {
    if (dimensions.length == 1) {
      for (int i = 0; i < dimensions[0]; i++) {
        Object element = Array.get(array, i);
        Array.set(flattened, i + offset, element);
      }
    } else {
      int[] tail = Arrays.copyOfRange(dimensions, 1, dimensions.length);

      for (int i = 0; i < dimensions[0]; i++) {
        Object a = Array.get(array, i);
        flatten(a, flattened, tail, offset + i * length(tail));
      }
    }
  }

  /**
   * Un-flatten a one-dimensional array into an multi-dimensional array based on the provided
   * dimensions.
   *
   * @param array the 1-dimensional array to un-flatten.
   * @param dimensions the dimensions to un-flatten to.
   * @return a multi-dimensional array of the provided dimensions.
   */
  public static Object unflatten(Object array, int[] dimensions) {
    Class<?> type = getType(array);

    return unflatten(type, array, dimensions, 0);
  }

  private static Object unflatten(Class<?> type, Object array, int[] dimensions, int offset) {
    Object a;
    if (dimensions.length == 1) {
      a = Array.newInstance(type, dimensions[0]);

      for (int i = 0; i < dimensions[0]; i++) {
        Array.set(a, i, Array.get(array, offset + i));
      }
    } else {
      a = Array.newInstance(type, dimensions);

      int[] tail = Arrays.copyOfRange(dimensions, 1, dimensions.length);

      for (int i = 0; i < dimensions[0]; i++) {
        Object element = unflatten(type, array, tail, offset + i * length(tail));
        Array.set(a, i, element);
      }
    }
    return a;
  }

  public static int[] getDimensions(Object array) {
    IntStream dimensions = IntStream.empty();
    Class<?> type = array.getClass();

    while (type.isArray()) {
      int length = array != null ? Array.getLength(array) : 0;
      dimensions = IntStream.concat(dimensions, IntStream.of(length));

      array = length > 0 ? Array.get(array, 0) : null;
      type = type.getComponentType();
    }

    return dimensions.toArray();
  }

  public static Class<?> getType(Object array) {
    Class<?> type = array.getClass();

    while (type.isArray()) {
      type = type.getComponentType();
    }

    return type;
  }

  private static int length(int[] tail) {
    int product = 1;
    for (int aTail : tail) {
      product *= aTail;
    }
    return product;
  }

  /**
   * Transform the values in an array from one type to another using a transformation function.
   *
   * <p>Handles multi-dimensional arrays by flattening before the transformation and un-flattening
   * afterwards.
   *
   * @param array the original array.
   * @param transform a function that transforms from {@code F} to {@code T}.
   * @param toType the destination type.
   * @return a new array of the same size and dimensions with all elements transformed from {@code
   *     F} to {@code T} using {@code transform}.
   */
  public static <F, T> Object transformArray(
      Object array, Function<F, T> transform, Class<T> toType) {
    int[] dimensions = ArrayUtil.getDimensions(array);

    Object flatArray = dimensions.length > 1 ? flatten(array) : array;

    Object transformedArray = transformFlatArray(flatArray, transform, toType);

    return dimensions.length > 1 ? unflatten(transformedArray, dimensions) : transformedArray;
  }

  private static <F, T> Object transformFlatArray(
      Object flatArray, Function<F, T> transform, Class<T> toType) {
    int length = Array.getLength(flatArray);
    Object array = Array.newInstance(toType, length);
    for (int i = 0; i < length; i++) {
      @SuppressWarnings("unchecked")
      Object transformed = transform.apply((F) Array.get(flatArray, i));
      Array.set(array, i, transformed);
    }
    return array;
  }
}
