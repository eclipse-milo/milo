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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.function.Function;
import org.eclipse.milo.opcua.stack.core.BuiltinDataType;
import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

/**
 * Matrix is a container for multidimensional array values.
 *
 * <p>Allowed value types:
 *
 * <ul>
 *   <li>any builtin type
 *   <li>UaStructuredType
 *   <li>UaEnumeratedType
 *   <li>OptionSetUInteger subclasses
 * </ul>
 */
public class Matrix {

  private final Object flatArray;
  private final int[] dimensions;
  private final BuiltinDataType builtinDataType;
  private final ExpandedNodeId dataTypeId;

  public Matrix(@Nullable Object nestedArray) {
    if (nestedArray == null) {
      this.flatArray = null;
      this.dimensions = new int[0];
      this.builtinDataType = null;
      this.dataTypeId = null;
    } else {
      this.flatArray = ArrayUtil.flatten(nestedArray);
      this.dimensions = ArrayUtil.getDimensions(nestedArray);
      this.builtinDataType = deriveBuiltinType(flatArray);
      this.dataTypeId = deriveDataTypeId(flatArray);
    }

    assert flatArray == null || (dimensions.length > 1 && builtinDataType != null);
  }

  public Matrix(Object flatArray, int[] dimensions) {
    this.flatArray = flatArray;
    this.dimensions = dimensions;
    this.builtinDataType = deriveBuiltinType(flatArray);
    this.dataTypeId = deriveDataTypeId(flatArray);

    assert dimensions.length > 1 && builtinDataType != null;
  }

  public Matrix(Object flatArray, int[] dimensions, BuiltinDataType builtinDataType) {
    this.flatArray = flatArray;
    this.dimensions = dimensions;
    this.builtinDataType = builtinDataType;
    this.dataTypeId = deriveDataTypeId(flatArray);

    assert flatArray != null && dimensions.length > 1 && builtinDataType != null;
  }

  /**
   * Get the elements of this Matrix in a flattened array.
   *
   * @return the elements of this Matrix in a flattened array.
   */
  public @Nullable Object getElements() {
    return flatArray;
  }

  /**
   * Get the dimensions of this Matrix.
   *
   * <p>The returned array is zero length if this Matrix contains a null value.
   *
   * @return the dimensions of this Matrix.
   */
  public int[] getDimensions() {
    return dimensions;
  }

  /**
   * Get the value rank (number of dimensions) of this Matrix.
   *
   * <p>The returned value is -1 if this Matrix contains a null value.
   *
   * @return the value rank of this Matrix.
   */
  public int getValueRank() {
    return flatArray != null ? dimensions.length : -1;
  }

  /**
   * Get the {@link BuiltinDataType} of the elements of this Matrix.
   *
   * <p>Empty only if this Matrix contains a {@code null} value.
   *
   * @return the {@link BuiltinDataType} of the elements of this Matrix.
   */
  public Optional<BuiltinDataType> getBuiltinDataType() {
    return Optional.ofNullable(builtinDataType);
  }

  /**
   * Get the {@link ExpandedNodeId} of the DataType of the elements of this Matrix.
   *
   * <p>If this Matrix contains elements of {@link UaEnumeratedType} or {@link UaStructuredType}
   * then the {@link ExpandedNodeId} of the DataType is returned, not its reduction to built-in
   * types Int32 or ExtensionObject.
   *
   * <p>Empty only if this Matrix contains a {@code null} value.
   *
   * @return the {@link ExpandedNodeId} of the DataType of the elements of this Matrix.
   */
  public Optional<ExpandedNodeId> getDataTypeId() {
    return Optional.ofNullable(dataTypeId);
  }

  /**
   * @return {@code true} if this Matrix contains a null value.
   */
  public boolean isNull() {
    return flatArray == null;
  }

  /**
   * @return {@code true} if this Matrix contains a non-null value.
   */
  public boolean isNotNull() {
    return !isNull();
  }

  /**
   * Create a new Matrix by applying the transform function {@code f} to each of the elements in
   * this Matrix.
   *
   * @param f the transform function applied to each element.
   * @return a new Matrix containing transformed elements.
   */
  public Matrix transform(Function<Object, Object> f) {
    if (flatArray == null) {
      return new Matrix(null);
    } else {
      Object transformedArray = null;

      int length = Array.getLength(flatArray);
      for (int i = 0; i < length; i++) {
        Object e = Array.get(flatArray, i);
        Object t = f.apply(e);
        if (transformedArray == null) {
          transformedArray = Array.newInstance(t.getClass(), length);
        }
        Array.set(transformedArray, i, t);
      }

      return new Matrix(transformedArray, dimensions);
    }
  }

  /**
   * Get a copy of nested (un-flattened) multidimensional array value contained by this Matrix.
   *
   * @return a copy of nested (un-flattened) multidimensional array value contained by this Matrix.
   */
  public Object nestedArrayValue() {
    return ArrayUtil.unflatten(flatArray, dimensions);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Matrix matrix = (Matrix) o;
    return Objects.deepEquals(flatArray, matrix.flatArray)
        && Arrays.equals(dimensions, matrix.dimensions)
        && builtinDataType == matrix.builtinDataType;
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(flatArray, builtinDataType);
    result = 31 * result + Arrays.hashCode(dimensions);
    return result;
  }

  @Override
  public String toString() {
    StringJoiner joiner =
        new StringJoiner(", ", Matrix.class.getSimpleName() + "{", "}")
            .add("builtinDataType=" + builtinDataType)
            .add("dataType=" + dataTypeId.toParseableString())
            .add("dimensions=" + Arrays.toString(dimensions));

    Class<?> clazz = ArrayUtil.getType(flatArray);

    if (clazz.isPrimitive()) {
      if (clazz == boolean.class) {
        joiner.add("flatArray=" + Arrays.toString((boolean[]) flatArray));
      } else if (clazz == byte.class) {
        joiner.add("flatArray=" + Arrays.toString((byte[]) flatArray));
      } else if (clazz == short.class) {
        joiner.add("flatArray=" + Arrays.toString((short[]) flatArray));
      } else if (clazz == int.class) {
        joiner.add("flatArray=" + Arrays.toString((int[]) flatArray));
      } else if (clazz == long.class) {
        joiner.add("flatArray=" + Arrays.toString((long[]) flatArray));
      } else if (clazz == float.class) {
        joiner.add("flatArray=" + Arrays.toString((float[]) flatArray));
      } else if (clazz == double.class) {
        joiner.add("flatArray=" + Arrays.toString((double[]) flatArray));
      } else {
        joiner.add("flatArray=" + flatArray);
      }
    } else {
      joiner.add("flatArray=" + Arrays.toString((Object[]) flatArray));
    }

    return joiner.toString();
  }

  private static @Nullable BuiltinDataType deriveBuiltinType(Object flatArray) {
    Class<?> type = ArrayUtil.getType(flatArray);

    return deriveBuiltinType(type);
  }

  private static @Nullable BuiltinDataType deriveBuiltinType(Class<?> type) {
    if (UaEnumeratedType.class.isAssignableFrom(type)) {
      return BuiltinDataType.Int32;
    } else if (UaStructuredType.class.isAssignableFrom(type)) {
      return BuiltinDataType.ExtensionObject;
    } else if (OptionSetUInteger.class.isAssignableFrom(type)) {
      if (OptionSetUI8.class.isAssignableFrom(type)) {
        return BuiltinDataType.Byte;
      } else if (OptionSetUI16.class.isAssignableFrom(type)) {
        return BuiltinDataType.UInt16;
      } else if (OptionSetUI32.class.isAssignableFrom(type)) {
        return BuiltinDataType.UInt32;
      } else if (OptionSetUI64.class.isAssignableFrom(type)) {
        return BuiltinDataType.UInt64;
      } else {
        throw new RuntimeException("unknown OptionSetUInteger subclass: " + type);
      }
    } else {
      return BuiltinDataType.fromBackingClass(type);
    }
  }

  private static @Nullable ExpandedNodeId deriveDataTypeId(Object flatArray) {
    Class<?> type = ArrayUtil.getType(flatArray);

    if (UaEnumeratedType.class.isAssignableFrom(type)) {
      Object e = Array.get(flatArray, 0);
      if (e instanceof UaEnumeratedType) {
        return ((UaEnumeratedType) e).getTypeId();
      } else {
        return null;
      }
    } else if (UaStructuredType.class.isAssignableFrom(type)) {
      Object e = Array.get(flatArray, 0);
      if (e instanceof UaStructuredType) {
        return ((UaStructuredType) e).getTypeId();
      } else {
        return null;
      }
    } else {
      BuiltinDataType builtinDataType = deriveBuiltinType(type);
      if (builtinDataType == null) {
        return null;
      } else {
        return builtinDataType.getNodeId().expanded();
      }
    }
  }

  // region Static factory methods

  /**
   * Create a Matrix containing a null value.
   *
   * @return a Matrix containing a null value.
   */
  public static Matrix ofNull() {
    return new Matrix(null);
  }

  /**
   * Create a Matrix containing a multidimensional Boolean value.
   *
   * @param value the multidimensional Boolean value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofBoolean(Boolean[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Boolean value.
   *
   * @param value the multidimensional Boolean value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofBoolean(boolean[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Boolean value.
   *
   * @param value the multidimensional Boolean value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofBoolean(Boolean[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Boolean value.
   *
   * @param value the multidimensional Boolean value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofBoolean(boolean[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional SByte value.
   *
   * @param value the multidimensional SByte value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofSByte(Byte[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional SByte value.
   *
   * @param value the multidimensional SByte value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofSByte(byte[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional SByte value.
   *
   * @param value the multidimensional SByte value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofSByte(Byte[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional SByte value.
   *
   * @param value the multidimensional SByte value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofSByte(byte[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Byte value.
   *
   * @param value the multidimensional Byte value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofByte(UByte[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Byte value.
   *
   * @param value the multidimensional Byte value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofByte(UByte[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Int16 value.
   *
   * @param value the multidimensional Int16 value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofInt16(Short[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Int16 value.
   *
   * @param value the multidimensional Int16 value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofInt16(short[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Int16 value.
   *
   * @param value the multidimensional Int16 value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofInt16(Short[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Int16 value.
   *
   * @param value the multidimensional Int16 value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofInt16(short[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional UInt16 value.
   *
   * @param value the multidimensional UInt16 value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofUInt16(UShort[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional UInt16 value.
   *
   * @param value the multidimensional UInt16 value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofUInt16(UShort[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Int32 value.
   *
   * @param value the multidimensional Int32 value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofInt32(Integer[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Int32 value.
   *
   * @param value the multidimensional Int32 value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofInt32(int[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Int32 value.
   *
   * @param value the multidimensional Int32 value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofInt32(Integer[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Int32 value.
   *
   * @param value the multidimensional Int32 value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofInt32(int[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional UInt32 value.
   *
   * @param value the multidimensional UInt32 value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofUInt32(UInteger[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional UInt32 value.
   *
   * @param value the multidimensional UInt32 value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofUInt32(UInteger[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Int64 value.
   *
   * @param value the multidimensional Int64 value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofInt64(Long[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Int64 value.
   *
   * @param value the multidimensional Int64 value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofInt64(long[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Int64 value.
   *
   * @param value the multidimensional Int64 value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofInt64(Long[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Int64 value.
   *
   * @param value the multidimensional Int64 value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofInt64(long[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional UInt64 value.
   *
   * @param value the multidimensional UInt64 value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofUInt64(ULong[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional UInt64 value.
   *
   * @param value the multidimensional UInt64 value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofUInt64(ULong[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Float value.
   *
   * @param value the multidimensional Float value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofFloat(Float[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Float value.
   *
   * @param value the multidimensional Float value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofFloat(float[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Float value.
   *
   * @param value the multidimensional Float value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofFloat(Float[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Float value.
   *
   * @param value the multidimensional Float value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofFloat(float[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Double value.
   *
   * @param value the multidimensional Double value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofDouble(Double[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Double value.
   *
   * @param value the multidimensional Double value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofDouble(double[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Double value.
   *
   * @param value the multidimensional Double value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofDouble(Double[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Double value.
   *
   * @param value the multidimensional Double value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofDouble(double[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional String value.
   *
   * @param value the multidimensional String value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofString(String[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional String value.
   *
   * @param value the multidimensional String value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofString(String[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional DateTime value.
   *
   * @param value the multidimensional DateTime value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofDateTime(DateTime[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional DateTime value.
   *
   * @param value the multidimensional DateTime value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofDateTime(DateTime[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Guid value.
   *
   * @param value the multidimensional Guid value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofGuid(UUID[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Guid value.
   *
   * @param value the multidimensional Guid value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofGuid(UUID[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional ByteString value.
   *
   * @param value the multidimensional ByteString value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofByteString(ByteString[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional ByteString value.
   *
   * @param value the multidimensional ByteString value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofByteString(ByteString[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional XmlElement value.
   *
   * @param value the multidimensional XmlElement value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofXmlElement(XmlElement[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional XmlElement value.
   *
   * @param value the multidimensional XmlElement value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofXmlElement(XmlElement[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional NodeId value.
   *
   * @param value the multidimensional NodeId value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofNodeId(NodeId[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional NodeId value.
   *
   * @param value the multidimensional NodeId value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofNodeId(NodeId[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional ExpandedNodeId value.
   *
   * @param value the multidimensional ExpandedNodeId value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofExpandedNodeId(ExpandedNodeId[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional ExpandedNodeId value.
   *
   * @param value the multidimensional ExpandedNodeId value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofExpandedNodeId(ExpandedNodeId[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional StatusCode value.
   *
   * @param value the multidimensional StatusCode value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofStatusCode(StatusCode[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional StatusCode value.
   *
   * @param value the multidimensional StatusCode value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofStatusCode(StatusCode[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional QualifiedName value.
   *
   * @param value the multidimensional QualifiedName value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofQualifiedName(QualifiedName[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional QualifiedName value.
   *
   * @param value the multidimensional QualifiedName value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofQualifiedName(QualifiedName[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional LocalizedText value.
   *
   * @param value the multidimensional LocalizedText value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofLocalizedText(LocalizedText[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional LocalizedText value.
   *
   * @param value the multidimensional LocalizedText value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofLocalizedText(LocalizedText[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional ExtensionObject value.
   *
   * @param value the multidimensional ExtensionObject value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofExtensionObject(ExtensionObject[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional ExtensionObject value.
   *
   * @param value the multidimensional ExtensionObject value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofExtensionObject(ExtensionObject[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional DataValue value.
   *
   * @param value the multidimensional DataValue value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofDataValue(DataValue[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional DataValue value.
   *
   * @param value the multidimensional DataValue value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofDataValue(DataValue[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Variant value.
   *
   * @param value the multidimensional Variant value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofVariant(Variant[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional Variant value.
   *
   * @param value the multidimensional Variant value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofVariant(Variant[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional DiagnosticInfo value.
   *
   * @param value the multidimensional DiagnosticInfo value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofDiagnosticInfo(DiagnosticInfo[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional DiagnosticInfo value.
   *
   * @param value the multidimensional DiagnosticInfo value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofDiagnosticInfo(DiagnosticInfo[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional UaEnumeratedType value.
   *
   * @param value the multidimensional UaEnumeratedType value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofEnum(UaEnumeratedType[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional UaEnumeratedType value.
   *
   * @param value the multidimensional UaEnumeratedType value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofEnum(UaEnumeratedType[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional UaStructuredType value.
   *
   * @param value the multidimensional UaStructuredType value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofStruct(UaStructuredType[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional UaStructuredType value.
   *
   * @param value the multidimensional UaStructuredType value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofStruct(UaStructuredType[][][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional OptionSetUInteger value.
   *
   * @param value the multidimensional OptionSetUInteger value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofOptionSetUI(OptionSetUInteger<?>[][] value) {
    return new Matrix(value);
  }

  /**
   * Create a Matrix containing a multidimensional OptionSetUInteger value.
   *
   * @param value the multidimensional OptionSetUInteger value.
   * @return a new Matrix containing {@code value}.
   */
  public static Matrix ofOptionSetUI(OptionSetUInteger<?>[][][] value) {
    return new Matrix(value);
  }

  // endregion

}
