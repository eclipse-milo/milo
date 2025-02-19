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

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.eclipse.milo.opcua.stack.core.util.TypeUtil;
import org.jspecify.annotations.Nullable;

public final class Variant {

  public static final Variant NULL_VALUE = new Variant(null);

  private final Object value;

  /**
   * Create a new Variant with a given value.
   *
   * <p>This constructor offers no type safety and does not verify that {@code value} is allowed.
   * See {@link #of(Object)} for an alternative that will verify the value, or use one of the
   * type-safe static factory methods instead.
   *
   * <p>Variants can contain arrays of Variants, but they cannot directly contain another Variant.
   *
   * <p>DiagnosticInfo types only have meaning when returned in a response message with an
   * associated StatusCode and table of strings. As a result, Variants cannot contain instances of
   * DiagnosticInfo.
   *
   * @param value the value this Variant holds.
   */
  public Variant(@Nullable Object value) {
    this.value = value;
  }

  public Optional<ExpandedNodeId> getDataTypeId() {
    if (value == null) return Optional.empty();

    if (value instanceof UaStructuredType) {
      return Optional.of(((UaStructuredType) value).getTypeId());
    } else if (value instanceof UaEnumeratedType) {
      return Optional.of(((UaEnumeratedType) value).getTypeId());
    } else if (value instanceof Matrix) {
      return ((Matrix) value).getDataTypeId();
    } else {
      Class<?> clazz = value.getClass().isArray() ? ArrayUtil.getType(value) : value.getClass();

      int typeId = TypeUtil.getBuiltinTypeId(clazz);

      return typeId == -1 ? Optional.empty() : Optional.of(new NodeId(0, typeId).expanded());
    }
  }

  public Optional<OpcUaDataType> getDataType() {
    if (value == null) {
      return Optional.empty();
    }

    Class<?> type = value.getClass().isArray() ? ArrayUtil.getType(value) : value.getClass();

    if (UaEnumeratedType.class.isAssignableFrom(type)) {
      return Optional.of(OpcUaDataType.Int32);
    } else if (UaStructuredType.class.isAssignableFrom(type)) {
      return Optional.of(OpcUaDataType.ExtensionObject);
    } else if (OptionSetUInteger.class.isAssignableFrom(type)) {
      if (OptionSetUI8.class.isAssignableFrom(type)) {
        return Optional.of(OpcUaDataType.Byte);
      } else if (OptionSetUI16.class.isAssignableFrom(type)) {
        return Optional.of(OpcUaDataType.UInt16);
      } else if (OptionSetUI32.class.isAssignableFrom(type)) {
        return Optional.of(OpcUaDataType.UInt32);
      } else if (OptionSetUI64.class.isAssignableFrom(type)) {
        return Optional.of(OpcUaDataType.UInt64);
      } else {
        throw new RuntimeException("unknown OptionSetUInteger subclass: " + type);
      }
    } else if (Matrix.class.isAssignableFrom(type)) {
      return ((Matrix) value).getDataType();
    } else {
      return Optional.ofNullable(OpcUaDataType.fromBackingClass(type));
    }
  }

  public @Nullable Object getValue() {
    return value;
  }

  public boolean isNull() {
    return value == null;
  }

  public boolean isNotNull() {
    return !isNull();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Variant variant = (Variant) o;

    return Objects.deepEquals(value, variant.value);
  }

  @Override
  public int hashCode() {
    return Objects.hash(valueHash());
  }

  private int valueHash() {
    if (value instanceof Object[]) {
      return Arrays.deepHashCode((Object[]) value);
    } else if (value instanceof boolean[]) {
      return Arrays.hashCode((boolean[]) value);
    } else if (value instanceof byte[]) {
      return Arrays.hashCode((byte[]) value);
    } else if (value instanceof short[]) {
      return Arrays.hashCode((short[]) value);
    } else if (value instanceof int[]) {
      return Arrays.hashCode((int[]) value);
    } else if (value instanceof long[]) {
      return Arrays.hashCode((long[]) value);
    } else if (value instanceof float[]) {
      return Arrays.hashCode((float[]) value);
    } else if (value instanceof double[]) {
      return Arrays.hashCode((double[]) value);
    } else {
      return Objects.hashCode(value);
    }
  }

  @Override
  public String toString() {
    ToStringHelper helper = MoreObjects.toStringHelper(this);

    helper.add("value", value);

    return helper.toString();
  }

  // region Static factory methods

  /**
   * Create a Variant containing an arbitrary Object with minimal checks.
   *
   * <p>{@code value} is only inspected to see if it's an explicitly forbidden type for a Variant to
   * contain, not that is a valid type. Consider using one of the type-safe factory methods if
   * possible.
   *
   * <p>Variants can contain arrays of Variants, but they cannot directly contain another Variant.
   *
   * <p>DiagnosticInfo types only have meaning when returned in a response message with an
   * associated StatusCode and table of strings. As a result, Variants cannot contain instances of
   * DiagnosticInfo.
   *
   * @param value the value the Variant will contain.
   * @return a new Variant containing {@code value}.
   */
  public static Variant of(@Nullable Object value) {
    var variant = new Variant(value);

    if (value != null) {
      boolean clazzIsArray = value.getClass().isArray();

      Class<?> clazz = clazzIsArray ? ArrayUtil.getType(value) : value.getClass();

      checkArgument(clazzIsArray || !Variant.class.equals(clazz), "Variant cannot contain Variant");
      checkArgument(!DiagnosticInfo.class.equals(clazz), "Variant cannot contain DiagnosticInfo");
      checkArgument(
          variant.getDataType().isPresent(), "Variant cannot contain %s", value.getClass());
    }

    return variant;
  }

  /**
   * Create an empty Variant, i.e. a Variant with a {@code null} value.
   *
   * @return an empty Variant, i.e. a Variant with a {@code null} value.
   */
  public static Variant ofNull() {
    return NULL_VALUE;
  }

  /**
   * Create a Variant containing a Boolean value.
   *
   * @param value the boolean value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofBoolean(boolean value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a Boolean value.
   *
   * @param value the Boolean value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofBoolean(Boolean value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a SByte value.
   *
   * @param value the SByte value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofSByte(byte value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a SByte value.
   *
   * @param value the SByte value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofSByte(Byte value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a Byte value.
   *
   * @param value the Byte value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofByte(UByte value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing an Int16 value.
   *
   * @param value the Int16 value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofInt16(short value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing an Int16 value.
   *
   * @param value the Int16 value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofInt16(Short value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a UInt16 value.
   *
   * @param value the UInt16 value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofUInt16(UShort value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing an Int32 value.
   *
   * @param value the Int32 value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofInt32(int value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing an Int32 value.
   *
   * @param value the Int32 value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofInt32(Integer value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a UInt32 value.
   *
   * @param value the UInt32 value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofUInt32(UInteger value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing an Int64 value.
   *
   * @param value the Int64 value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofInt64(long value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing an Int64 value.
   *
   * @param value the Int64 value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofInt64(Long value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a UInt64 value.
   *
   * @param value the UInt64 value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofUInt64(ULong value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a Float value.
   *
   * @param value the Float value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofFloat(float value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a Float value.
   *
   * @param value the Float value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofFloat(Float value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a Double value.
   *
   * @param value the Double value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofDouble(double value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a Double value.
   *
   * @param value the Double value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofDouble(Double value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a String value.
   *
   * @param value the String value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofString(String value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a DateTime value.
   *
   * @param value the DateTime value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofDateTime(DateTime value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a Guid value.
   *
   * @param value the Guid value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofGuid(UUID value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a ByteString value.
   *
   * @param value the ByteString value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofByteString(ByteString value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a XmlElement value.
   *
   * @param value the XmlElement value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofXmlElement(XmlElement value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a NodeId value.
   *
   * @param value the NodeId value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofNodeId(NodeId value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing an ExpandedNodeId value.
   *
   * @param value the ExpandedNodeId value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofExpandedNodeId(ExpandedNodeId value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a StatusCode value.
   *
   * @param value the StatusCode value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofStatusCode(StatusCode value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a QualifiedName value.
   *
   * @param value the QualifiedName value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofQualifiedName(QualifiedName value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a LocalizedText value.
   *
   * @param value the LocalizedText value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofLocalizedText(LocalizedText value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing an ExtensionObject value.
   *
   * @param value the ExtensionObject value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofExtensionObject(ExtensionObject value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a DataValue value.
   *
   * @param value the DataValue value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofDataValue(DataValue value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a Boolean array value.
   *
   * @param value the Boolean array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofBooleanArray(boolean[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a Boolean array value.
   *
   * @param value the Boolean array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofBooleanArray(Boolean[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a SByte array value.
   *
   * @param value the SByte array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofSByteArray(byte[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a SByte array value.
   *
   * @param value the SByte array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofSByteArray(Byte[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a Byte array value.
   *
   * @param value the Byte array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofByteArray(UByte[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing an Int16 array value.
   *
   * @param value the Int16 array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofInt16Array(short[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing an Int16 array value.
   *
   * @param value the Int16 array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofInt16Array(Short[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a UInt16 array value.
   *
   * @param value the UInt16 array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofUInt16Array(UShort[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing an Int32 array value.
   *
   * @param value the Int32 array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofInt32Array(int[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing an Int32 array value.
   *
   * @param value the Int32 array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofInt32Array(Integer[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a UInt32 array value.
   *
   * @param value the UInt32 array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofUInt32Array(UInteger[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing an Int64 array value.
   *
   * @param value the Int64 array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofInt64Array(long[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing an Int64 array value.
   *
   * @param value the Int64 array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofInt64Array(Long[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a UInt64 array value.
   *
   * @param value the UInt64 array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofUInt64Array(ULong[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a Float array value.
   *
   * @param value the Float array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofFloatArray(float[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a Float array value.
   *
   * @param value the Float array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofFloatArray(Float[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a Double array value.
   *
   * @param value the Double array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofDoubleArray(double[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a Double array value.
   *
   * @param value the Double array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofDoubleArray(Double[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a String array value.
   *
   * @param value the String array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofStringArray(String[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a DateTime array value.
   *
   * @param value the DateTime array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofDateTimeArray(DateTime[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a Guid array value.
   *
   * @param value the Guid array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofGuidArray(UUID[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a ByteString array value.
   *
   * @param value the ByteString array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofByteStringArray(ByteString[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a XmlElement array value.
   *
   * @param value the XmlElement array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofXmlElementArray(XmlElement[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a NodeId array value.
   *
   * @param value the NodeId array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofNodeIdArray(NodeId[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a ExpandedNodeId array value.
   *
   * @param value the ExpandedNodeId array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofExpandedNodeIdArray(ExpandedNodeId[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a StatusCode array value.
   *
   * @param value the StatusCode array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofStatusCodeArray(StatusCode[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a QualifiedName array value.
   *
   * @param value the QualifiedName array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofQualifiedNameArray(QualifiedName[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a LocalizedText array value.
   *
   * @param value the LocalizedText array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofLocalizedTextArray(LocalizedText[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing an ExtensionObject array value.
   *
   * @param value the ExtensionObject array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofExtensionObjectArray(ExtensionObject[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a DataValue array value.
   *
   * @param value the DataValue array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofDataValueArray(DataValue[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a Variant array value.
   *
   * @param value the Variant array value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofVariantArray(Variant[] value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a Matrix value.
   *
   * @param value the Matrix value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofMatrix(Matrix value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a UaEnumeratedType value.
   *
   * @param value the UaEnumeratedType value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofEnum(UaEnumeratedType value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing a UaStructuredType value.
   *
   * @param value the UaStructuredType value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofStruct(UaStructuredType value) {
    return new Variant(value);
  }

  /**
   * Create a Variant containing an OptionSetUInteger value.
   *
   * @param value the OptionSetUInteger value.
   * @return a new Variant containing {@code value}.
   */
  public static Variant ofOptionSetUI(OptionSetUInteger<?> value) {
    return new Variant(value);
  }

  // endregion

}
