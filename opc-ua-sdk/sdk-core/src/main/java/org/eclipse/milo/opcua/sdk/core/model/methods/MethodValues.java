package org.eclipse.milo.opcua.sdk.core.model.methods;

import java.lang.reflect.Array;
import java.util.function.IntFunction;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaArgumentConversionException;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.OptionSetUInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UNumber;
import org.jspecify.annotations.Nullable;

/** Shared wire conversion for this library's Method descriptors. */
final class MethodValues {
  private MethodValues() {}

  static void count(Variant[] values, int required, int total) throws UaException {
    if (values.length < required) throw new UaException(StatusCodes.Bad_ArgumentsMissing);
    if (values.length > total) throw new UaException(StatusCodes.Bad_TooManyArguments);
  }

  static @Nullable Object decode(
      EncodingContext context,
      Variant[] values,
      int index,
      Class<?> type,
      int rank,
      long[] dimensions,
      @Nullable IntFunction<?> enumeration)
      throws UaException {
    try {
      Object value =
          index < values.length && values[index] != null ? values[index].getValue() : null;
      return convert(context, value, type, rank, dimensions, enumeration, false);
    } catch (Exception failure) {
      throw new UaArgumentConversionException(index, failure);
    }
  }

  static Variant encode(
      EncodingContext context,
      @Nullable Object value,
      int index,
      Class<?> type,
      int rank,
      long[] dimensions,
      @Nullable IntFunction<?> enumeration)
      throws UaException {
    try {
      return new Variant(convert(context, value, type, rank, dimensions, enumeration, true));
    } catch (Exception failure) {
      throw new UaArgumentConversionException(index, failure);
    }
  }

  private static @Nullable Object convert(
      EncodingContext context,
      @Nullable Object value,
      Class<?> type,
      int rank,
      long[] maxima,
      @Nullable IntFunction<?> enumeration,
      boolean encode)
      throws Exception {
    boolean flexible = rank != -1 && rank != 1;
    if (value == null) return null;
    if (flexible && encode && !(value instanceof Variant))
      throw new IllegalArgumentException("expected a wire-facing Variant");
    Object payload =
        value instanceof Variant variant && (flexible || type == Variant.class)
            ? variant.getValue()
            : value;
    if (payload == null || payload instanceof Matrix matrix && matrix.isNull()) return null;
    int actualRank =
        payload instanceof Matrix matrix
            ? matrix.getValueRank()
            : payload.getClass().isArray() ? 1 : -1;
    Object elements = payload instanceof Matrix matrix ? matrix.getElements() : payload;
    boolean empty = actualRank > 0 && Array.getLength(elements) == 0;
    if (!(actualRank == rank
        || rank == -2
        || rank == -3 && (actualRank == -1 || actualRank == 1)
        || rank == 0 && actualRank > 0
        || empty && rank > 0)) throw new IllegalArgumentException("incompatible argument rank");
    if (actualRank > 0 && !empty && maxima.length != 0) {
      int[] dimensions =
          payload instanceof Matrix matrix
              ? matrix.getDimensions()
              : new int[] {Array.getLength(elements)};
      if (maxima.length != dimensions.length)
        throw new IllegalArgumentException("incompatible argument dimensions");
      for (int i = 0; i < maxima.length; i++)
        if (maxima[i] != 0 && dimensions[i] > maxima[i])
          throw new IllegalArgumentException("argument dimension exceeds maximum");
    }
    // Flexible ranks deliberately retain a wire-facing Variant on both SDK surfaces.
    if (flexible) {
      if (actualRank < 0) scalar(context, elements, type, enumeration, false);
      else
        for (int i = 0; i < Array.getLength(elements); i++)
          scalar(context, Array.get(elements, i), type, enumeration, false);
      return encode ? payload : new Variant(payload);
    }
    if (actualRank < 0) return scalar(context, payload, type, enumeration, encode);
    Class<?> component = encode ? wireType(type, enumeration != null) : type;
    Object result = Array.newInstance(component, Array.getLength(elements));
    for (int i = 0; i < Array.getLength(elements); i++) {
      Object element = Array.get(elements, i);
      if (element != null && element.getClass().isArray())
        throw new IllegalArgumentException("nested array; use Matrix");
      Object converted =
          encode && type == Variant.class
              ? element instanceof Variant ? element : new Variant(element)
              : scalar(context, element, type, enumeration, encode);
      if (converted == null && !nullableElement(type))
        throw new IllegalArgumentException("null array element");
      Array.set(result, i, converted);
    }
    return result;
  }

  private static @Nullable Object scalar(
      EncodingContext context,
      @Nullable Object value,
      Class<?> type,
      @Nullable IntFunction<?> enumeration,
      boolean encode)
      throws Exception {
    if (value == null) return null;
    if (type == Variant.class)
      return encode && value instanceof Variant variant
          ? variant.getValue()
          : encode ? value : value instanceof Variant ? value : new Variant(value);
    if (encode) {
      Object checked = type.cast(value);
      if (checked instanceof UaEnumeratedType e) return e.getValue();
      if (checked instanceof OptionSetUInteger<?> options) return options.getValue();
      if (checked instanceof UaStructuredType structure)
        return ExtensionObject.encode(context, structure);
      return checked;
    }
    Object decoded = value;
    if (value instanceof ExtensionObject extension && type != ExtensionObject.class)
      decoded = extension.isNull() ? null : extension.decode(context);
    if (decoded == null) return null;
    if (enumeration != null && decoded instanceof Integer number) {
      Object found = enumeration.apply(number);
      if (found == null) throw new IllegalArgumentException("unknown enum value");
      return type.cast(found);
    }
    if (OptionSetUInteger.class.isAssignableFrom(type)
        && decoded instanceof UNumber
        && !type.isInstance(decoded))
      return type.getConstructor(decoded.getClass()).newInstance(decoded);
    return type.cast(decoded);
  }

  private static Class<?> wireType(Class<?> type, boolean enumeration) throws Exception {
    if (enumeration) return Integer.class;
    if (UaStructuredType.class.isAssignableFrom(type)) return ExtensionObject.class;
    if (OptionSetUInteger.class.isAssignableFrom(type))
      return type.getMethod("getValue").getReturnType();
    return type;
  }

  private static boolean nullableElement(Class<?> type) {
    return type == String.class
        || type == Variant.class
        || type == ExtensionObject.class
        || UaStructuredType.class.isAssignableFrom(type);
  }
}
