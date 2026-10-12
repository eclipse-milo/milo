/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.gds;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.jspecify.annotations.Nullable;

/**
 * Typed access to the output arguments of a method call.
 *
 * <p>Every accessor validates the value it returns and reports a mismatch as {@link
 * StatusCodes#Bad_UnexpectedError} naming the method, the argument index, the expected type, and
 * the types actually received, so a server that deviates from the spec's argument list fails with a
 * diagnosable error rather than a {@link ClassCastException}.
 */
final class MethodOutputs {

  private final String methodName;
  private final Variant[] outputs;

  private MethodOutputs(String methodName, Variant[] outputs) {
    this.methodName = methodName;
    this.outputs = outputs;
  }

  /**
   * Wrap {@code outputs}, requiring at least {@code requiredCount} values.
   *
   * <p>Surplus trailing outputs are tolerated; accessors only read the indexes a decoder asks for.
   */
  static MethodOutputs of(String methodName, Variant[] outputs, int requiredCount)
      throws UaException {

    if (outputs.length < requiredCount) {
      throw new UaException(
          StatusCodes.Bad_UnexpectedError,
          String.format(
              "%s: expected %d output argument(s) but received %d: %s",
              methodName, requiredCount, outputs.length, describe(outputs)));
    }

    return new MethodOutputs(methodName, outputs);
  }

  /** Get the scalar output at {@code index}, which must be present and of type {@code type}. */
  <T> T scalar(int index, Class<T> type) throws UaException {
    T value = nullableScalar(index, type);

    if (value == null) {
      throw mismatch(index, type.getSimpleName());
    }

    return value;
  }

  /**
   * Get the scalar output at {@code index}, which may be null but otherwise must be {@code type}.
   */
  <T> @Nullable T nullableScalar(int index, Class<T> type) throws UaException {
    Object value = value(index);

    if (value == null) {
      return null;
    } else if (type.isInstance(value)) {
      return type.cast(value);
    } else {
      throw mismatch(index, type.getSimpleName());
    }
  }

  /** Get the array output at {@code index}; a null array is returned as an empty one. */
  <T> T[] array(int index, Class<T> componentType) throws UaException {
    Object value = value(index);

    if (value == null) {
      @SuppressWarnings("unchecked")
      T[] empty = (T[]) Array.newInstance(componentType, 0);
      return empty;
    } else if (componentType.arrayType().isInstance(value)) {
      @SuppressWarnings("unchecked")
      T[] array = (T[]) value;
      return array;
    } else {
      throw mismatch(index, componentType.getSimpleName() + "[]");
    }
  }

  /**
   * Get the structure output at {@code index}, decoding it with {@code context} if it arrived as an
   * {@link ExtensionObject}.
   */
  <T extends UaStructuredType> T struct(int index, Class<T> type, EncodingContext context)
      throws UaException {

    Object value = value(index);

    if (value == null) {
      throw mismatch(index, type.getSimpleName());
    }

    return decodeStruct(index, value, type, context);
  }

  /**
   * Get the structure array output at {@code index}, decoding elements with {@code context} if they
   * arrived as {@link ExtensionObject}s. A null array is returned as an empty one.
   */
  <T extends UaStructuredType> T[] structArray(int index, Class<T> type, EncodingContext context)
      throws UaException {

    Object value = value(index);

    if (value != null && !(value instanceof Object[])) {
      throw mismatch(index, type.getSimpleName() + "[]");
    }

    @Nullable Object[] elements = value != null ? (@Nullable Object[]) value : new Object[0];

    @SuppressWarnings("unchecked")
    T[] decoded = (T[]) Array.newInstance(type, elements.length);

    for (int i = 0; i < elements.length; i++) {
      Object element = elements[i];
      if (element == null) {
        throw mismatch(index, type.getSimpleName() + "[]");
      }
      decoded[i] = decodeStruct(index, element, type, context);
    }

    return decoded;
  }

  private <T extends UaStructuredType> T decodeStruct(
      int index, Object value, Class<T> type, EncodingContext context) throws UaException {

    Object struct = value;

    if (value instanceof ExtensionObject xo) {
      try {
        struct = xo.decode(context);
      } catch (UaSerializationException e) {
        throw new UaException(
            StatusCodes.Bad_DecodingError,
            String.format(
                "%s: output %d could not be decoded as %s",
                methodName, index, type.getSimpleName()),
            e);
      }
    }

    if (type.isInstance(struct)) {
      return type.cast(struct);
    } else {
      throw mismatch(index, type.getSimpleName());
    }
  }

  private @Nullable Object value(int index) {
    return index < outputs.length ? outputs[index].value() : null;
  }

  private UaException mismatch(int index, String expectedType) {
    return new UaException(
        StatusCodes.Bad_UnexpectedError,
        String.format(
            "%s: expected output %d to be %s, received %s",
            methodName, index, expectedType, describe(outputs)));
  }

  private static String describe(Variant[] outputs) {
    return Arrays.stream(outputs)
        .map(
            v -> {
              Object value = v.value();
              if (value == null) {
                return "null";
              } else if (value instanceof ExtensionObject xo) {
                return "ExtensionObject(" + xo.getEncodingOrTypeId().toParseableString() + ")";
              } else {
                return value.getClass().getSimpleName();
              }
            })
        .collect(Collectors.joining(", ", "[", "]"));
  }
}
