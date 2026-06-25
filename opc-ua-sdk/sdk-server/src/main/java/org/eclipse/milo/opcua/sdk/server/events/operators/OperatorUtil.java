/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.events.operators;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.function.LongBinaryOperator;
import org.eclipse.milo.opcua.sdk.server.events.OperatorContext;
import org.eclipse.milo.opcua.sdk.server.events.ValidationException;
import org.eclipse.milo.opcua.sdk.server.events.conversions.ImplicitConversions;
import org.eclipse.milo.opcua.sdk.server.model.objects.BaseEventTypeNode;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.structured.FilterOperand;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

/**
 * Shared support for ContentFilter operators whose semantics depend on OPC UA conversion,
 * precedence, ordered comparison, or integer-width rules.
 *
 * <p>Keeping this behavior centralized avoids small differences between operators that the OPC UA
 * specification defines in terms of the same conversion and comparison tables.
 *
 * @see <a href="https://reference.opcfoundation.org/specs/OPC-10000-4/7.7.3">OPC UA Part 4, 7.7.3
 *     FilterOperator</a>
 */
final class OperatorUtil {

  private OperatorUtil() {}

  static void validateMinOperandCount(FilterOperand[] operands, int min)
      throws ValidationException {
    if (operands.length < min) {
      throw new ValidationException(StatusCodes.Bad_FilterOperandCountMismatch);
    }
  }

  @Nullable
  static Object resolve(OperatorContext context, BaseEventTypeNode eventNode, FilterOperand operand)
      throws UaException {

    return context.resolve(operand, eventNode);
  }

  @Nullable
  static BinaryOperands convertToCommonType(@NonNull Object operand0, @NonNull Object operand1) {
    // Part 4 Table 121 allows length-one arrays to be treated as scalar values.
    operand0 = toScalarIfSingleElementArray(operand0);
    operand1 = toScalarIfSingleElementArray(operand1);

    if (operand0 == null || operand1 == null) {
      return null;
    }

    OpcUaDataType dt0 = getType(operand0);
    OpcUaDataType dt1 = getType(operand1);

    if (dt0 == null || dt1 == null) {
      return null;
    }

    int p0 = ImplicitConversions.getPrecedence(dt0);
    int p1 = ImplicitConversions.getPrecedence(dt1);

    if (p0 == p1) {
      if (dt0 == dt1) {
        Object converted0 = operand0.getClass().isArray() ? convert(operand0, dt0) : operand0;
        Object converted1 = operand1.getClass().isArray() ? convert(operand1, dt1) : operand1;

        return converted0 != null && converted1 != null
            ? new BinaryOperands(dt0, converted0, converted1)
            : null;
      } else {
        return null;
      }
    } else if (p0 >= p1) {
      Object converted0 = operand0.getClass().isArray() ? convert(operand0, dt0) : operand0;
      Object converted1 = convert(operand1, dt0);

      return converted0 != null && converted1 != null
          ? new BinaryOperands(dt0, converted0, converted1)
          : null;
    } else {
      Object converted0 = convert(operand0, dt1);
      Object converted1 = operand1.getClass().isArray() ? convert(operand1, dt1) : operand1;

      return converted0 != null && converted1 != null
          ? new BinaryOperands(dt1, converted0, converted1)
          : null;
    }
  }

  @Nullable
  static Integer compareOrdered(@NonNull Object operand0, @NonNull Object operand1) {
    BinaryOperands operands = convertToCommonType(operand0, operand1);

    if (operands == null) {
      return null;
    }

    return compareOrdered(operands.dataType(), operands.operand0(), operands.operand1());
  }

  /**
   * Compares two already-resolved operand values for equality following OPC UA implicit-conversion
   * and three-valued NULL semantics.
   *
   * @return {@code null} if either value is {@code null} (indeterminate), {@code false} if the
   *     values have no common type, otherwise whether the operands are equal after being converted
   *     to their common type.
   */
  @Nullable
  static Boolean equalValues(@Nullable Object value0, @Nullable Object value1) {
    if (value0 == null || value1 == null) {
      return null;
    }

    BinaryOperands commonOperands = convertToCommonType(value0, value1);

    if (commonOperands == null) {
      return false;
    }

    return equal(commonOperands.operand0(), commonOperands.operand1());
  }

  private static boolean equal(@NonNull Object operand0, @NonNull Object operand1) {
    if (operand0.getClass().isArray()) {
      return Objects.deepEquals(operand0, operand1);
    } else if (operand0 instanceof Float f0 && operand1 instanceof Float f1) {
      // IEEE floating-point equality: NaN is never equal to itself and +0.0 equals -0.0, unlike
      // Float.equals/Objects.equals which compare bit patterns.
      return f0.floatValue() == f1.floatValue();
    } else if (operand0 instanceof Double d0 && operand1 instanceof Double d1) {
      return d0.doubleValue() == d1.doubleValue();
    } else {
      return Objects.equals(operand0, operand1);
    }
  }

  @Nullable
  static TernaryOperands convertToCommonType(
      @NonNull Object operand0, @NonNull Object operand1, @NonNull Object operand2) {

    // Between uses one common type for all operands, but the same length-one array scalarization
    // from Part 4 Table 121 still applies before selecting that type.
    operand0 = toScalarIfSingleElementArray(operand0);
    operand1 = toScalarIfSingleElementArray(operand1);
    operand2 = toScalarIfSingleElementArray(operand2);

    if (operand0 == null || operand1 == null || operand2 == null) {
      return null;
    }

    OpcUaDataType dt0 = getType(operand0);
    OpcUaDataType dt1 = getType(operand1);
    OpcUaDataType dt2 = getType(operand2);

    if (dt0 == null || dt1 == null || dt2 == null) {
      return null;
    }

    OpcUaDataType targetType = highestPrecedence(dt0, dt1, dt2);

    Object converted0 = dt0 == targetType ? operand0 : convert(operand0, targetType);
    Object converted1 = dt1 == targetType ? operand1 : convert(operand1, targetType);
    Object converted2 = dt2 == targetType ? operand2 : convert(operand2, targetType);

    if (converted0 == null || converted1 == null || converted2 == null) {
      return null;
    }

    return new TernaryOperands(targetType, converted0, converted1, converted2);
  }

  @Nullable
  static Integer compareOrdered(
      OpcUaDataType dataType, @NonNull Object operand0, @NonNull Object operand1) {

    if (operand0 instanceof Number n0 && operand1 instanceof Number n1) {
      return switch (dataType) {
        case SByte, Byte, Int16, UInt16, Int32, UInt32, Int64 ->
            Long.compare(n0.longValue(), n1.longValue());
        case UInt64 -> Long.compareUnsigned(n0.longValue(), n1.longValue());
        case Float -> compareFloat(n0.floatValue(), n1.floatValue());
        case Double -> compareDouble(n0.doubleValue(), n1.doubleValue());
        default -> null;
      };
    } else if (dataType == OpcUaDataType.DateTime
        && operand0 instanceof DateTime dt0
        && operand1 instanceof DateTime dt1) {

      return Long.compare(dt0.utcTime(), dt1.utcTime());
    } else {
      return null;
    }
  }

  @Nullable
  private static Integer compareFloat(float operand0, float operand1) {
    // NaN is not an ordered value for ContentFilter comparisons.
    if (Float.isNaN(operand0) || Float.isNaN(operand1)) {
      return null;
    } else if (operand0 < operand1) {
      return -1;
    } else if (operand0 > operand1) {
      return 1;
    } else {
      // Numerically equal, including +0.0 == -0.0 (unlike Float.compare, which orders -0.0 first).
      return 0;
    }
  }

  @Nullable
  private static Integer compareDouble(double operand0, double operand1) {
    // NaN is not an ordered value for ContentFilter comparisons.
    if (Double.isNaN(operand0) || Double.isNaN(operand1)) {
      return null;
    } else if (operand0 < operand1) {
      return -1;
    } else if (operand0 > operand1) {
      return 1;
    } else {
      // Numerically equal, including +0.0 == -0.0 (unlike Double.compare, which orders -0.0 first).
      return 0;
    }
  }

  @Nullable
  static Object bitwise(
      @NonNull Object operand0, @NonNull Object operand1, LongBinaryOperator operator) {

    operand0 = toScalarIfSingleElementArray(operand0);
    operand1 = toScalarIfSingleElementArray(operand1);

    if (operand0 == null || operand1 == null) {
      return null;
    }

    OpcUaDataType dt0 = getType(operand0);
    OpcUaDataType dt1 = getType(operand1);

    if (dt0 == null || dt1 == null || !isIntegerType(dt0) || !isIntegerType(dt1)) {
      return null;
    }

    if (operand0 instanceof Number n0 && operand1 instanceof Number n1) {
      // Bitwise operators use the largest operand size from Part 4 Table 118, not the general data
      // precedence table used by comparison/equality operators.
      OpcUaDataType targetType = largestIntegerType(dt0, dt1);
      long result = operator.applyAsLong(n0.longValue(), n1.longValue());

      return castBitwiseResult(targetType, result);
    } else {
      return null;
    }
  }

  @Nullable
  static OpcUaDataType getType(@NonNull Object value) {
    if (value.getClass().isArray()) {
      return OpcUaDataType.fromBackingClass(ArrayUtil.getType(value));
    } else {
      return OpcUaDataType.fromBackingClass(value.getClass());
    }
  }

  @Nullable
  static Object toScalarIfSingleElementArray(@Nullable Object value) {
    if (value != null && value.getClass().isArray()) {
      Object flattened = ArrayUtil.flatten(value);

      if (Array.getLength(flattened) == 1) {
        return Array.get(flattened, 0);
      }
    }

    return value;
  }

  @Nullable
  static Boolean toBoolean(@Nullable Object value) {
    value = toScalarIfSingleElementArray(value);

    if (value instanceof Boolean b) {
      return b;
    } else if (value != null) {
      Object converted = convert(value, OpcUaDataType.Boolean);

      return converted instanceof Boolean b ? b : null;
    } else {
      return null;
    }
  }

  @Nullable
  static Object convert(@NonNull Object value, OpcUaDataType targetType) {
    if (value.getClass().isArray()) {
      return convertArray(value, targetType);
    } else if (getType(value) == targetType) {
      return value;
    } else {
      return ImplicitConversions.convert(value, targetType);
    }
  }

  @Nullable
  private static Object convertArray(@NonNull Object array, OpcUaDataType targetType) {
    int[] dimensions = ArrayUtil.getDimensions(array);

    Object flattened = ArrayUtil.flatten(array);
    int length = Array.getLength(flattened);

    Object transformed = Array.newInstance(targetType.getBackingClass(), length);

    for (int i = 0; i < length; i++) {
      Object sourceValue = Array.get(flattened, i);
      if (sourceValue == null) {
        return null;
      }

      // Part 4 Table 121 says any element conversion failure makes the whole array conversion fail.
      Object targetValue =
          getType(sourceValue) == targetType
              ? sourceValue
              : ImplicitConversions.convert(sourceValue, targetType);
      if (targetValue == null) {
        return null;
      }

      Array.set(transformed, i, targetValue);
    }

    return ArrayUtil.unflatten(transformed, dimensions);
  }

  private static OpcUaDataType highestPrecedence(OpcUaDataType... dataTypes) {
    OpcUaDataType highest = dataTypes[0];

    for (int i = 1; i < dataTypes.length; i++) {
      if (ImplicitConversions.getPrecedence(dataTypes[i])
          > ImplicitConversions.getPrecedence(highest)) {

        highest = dataTypes[i];
      }
    }

    return highest;
  }

  private static boolean isIntegerType(OpcUaDataType dataType) {
    return switch (dataType) {
      case SByte, Byte, Int16, UInt16, Int32, UInt32, Int64, UInt64 -> true;
      default -> false;
    };
  }

  private static OpcUaDataType largestIntegerType(
      OpcUaDataType dataType0, OpcUaDataType dataType1) {
    int width0 = integerBitWidth(dataType0);
    int width1 = integerBitWidth(dataType1);

    if (width0 > width1) {
      return dataType0;
    } else if (width1 > width0) {
      return dataType1;
    } else if (isUnsignedIntegerType(dataType0) || isUnsignedIntegerType(dataType1)) {
      return unsignedIntegerType(width0);
    } else {
      return signedIntegerType(width0);
    }
  }

  private static int integerBitWidth(OpcUaDataType dataType) {
    return switch (dataType) {
      case SByte, Byte -> 8;
      case Int16, UInt16 -> 16;
      case Int32, UInt32 -> 32;
      case Int64, UInt64 -> 64;
      default -> throw new IllegalArgumentException("not an integer data type: " + dataType);
    };
  }

  private static boolean isUnsignedIntegerType(OpcUaDataType dataType) {
    return switch (dataType) {
      case Byte, UInt16, UInt32, UInt64 -> true;
      default -> false;
    };
  }

  private static OpcUaDataType signedIntegerType(int bitWidth) {
    return switch (bitWidth) {
      case 8 -> OpcUaDataType.SByte;
      case 16 -> OpcUaDataType.Int16;
      case 32 -> OpcUaDataType.Int32;
      case 64 -> OpcUaDataType.Int64;
      default -> throw new IllegalArgumentException("invalid integer bit width: " + bitWidth);
    };
  }

  private static OpcUaDataType unsignedIntegerType(int bitWidth) {
    return switch (bitWidth) {
      case 8 -> OpcUaDataType.Byte;
      case 16 -> OpcUaDataType.UInt16;
      case 32 -> OpcUaDataType.UInt32;
      case 64 -> OpcUaDataType.UInt64;
      default -> throw new IllegalArgumentException("invalid integer bit width: " + bitWidth);
    };
  }

  private static Object castBitwiseResult(OpcUaDataType dataType, long result) {
    return switch (dataType) {
      case SByte -> (byte) result;
      case Byte -> ubyte((short) (result & 0xffL));
      case Int16 -> (short) result;
      case UInt16 -> ushort((int) (result & 0xffffL));
      case Int32 -> (int) result;
      case UInt32 -> uint(result & 0xffffffffL);
      case Int64 -> result;
      case UInt64 -> ulong(result);
      default -> throw new IllegalArgumentException("not an integer data type: " + dataType);
    };
  }

  record BinaryOperands(OpcUaDataType dataType, Object operand0, Object operand1) {}

  record TernaryOperands(
      OpcUaDataType dataType, Object operand0, Object operand1, Object operand2) {}
}
