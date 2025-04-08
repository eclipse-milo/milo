/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.types;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.function.BiFunction;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumField;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

public final class DynamicOptionSetType extends DynamicType implements UaStructuredType {

  private final Lazy<Map<Integer, EnumField>> lazyFieldMap = new Lazy<>();

  private volatile ByteString value;
  private volatile ByteString validBits;

  private final DataType dataType;

  public DynamicOptionSetType(DataType dataType, ByteString value, ByteString validBits) {
    this.dataType = dataType;

    this.value = value;
    this.validBits = validBits;
  }

  @Override
  public ExpandedNodeId getTypeId() {
    return dataType.getNodeId().expanded();
  }

  @Override
  public ExpandedNodeId getBinaryEncodingId() {
    NodeId binaryEncodingId = dataType.getBinaryEncodingId();
    return binaryEncodingId != null ? binaryEncodingId.expanded() : ExpandedNodeId.NULL_VALUE;
  }

  @Override
  public ExpandedNodeId getXmlEncodingId() {
    NodeId xmlEncodingId = dataType.getXmlEncodingId();
    return xmlEncodingId != null ? xmlEncodingId.expanded() : ExpandedNodeId.NULL_VALUE;
  }

  @Override
  public ExpandedNodeId getJsonEncodingId() {
    NodeId jsonEncodingId = dataType.getJsonEncodingId();
    return jsonEncodingId != null ? jsonEncodingId.expanded() : ExpandedNodeId.NULL_VALUE;
  }

  @Override
  public DataType getDataType() {
    return dataType;
  }

  @Override
  public EnumDefinition getDataTypeDefinition() {
    return (EnumDefinition) requireNonNull(dataType.getDataTypeDefinition());
  }

  /**
   * Get the value of the option set.
   *
   * <p>The value is an array of bytes representing the bits in the option set. The length depends
   * on the number of bits, and the number of bytes may be larger than needed for the valid bits.
   *
   * @return the value of the option set.
   */
  public ByteString getValue() {
    return value;
  }

  /**
   * Get the valid bits of the option set.
   *
   * <p>The value is an array of bytes the same length as the value, where each bit represents
   * whether the corresponding bit in the value is valid.
   *
   * @return the valid bits of the option set.
   */
  public ByteString getValidBits() {
    return validBits;
  }

  /**
   * Set the value of the option set.
   *
   * @param value the value of the option set.
   */
  public void setValue(ByteString value) {
    requireNonNull(value);

    this.value = value;
  }

  /**
   * Set the valid bits of the option set.
   *
   * @param validBits the valid bits of the option set.
   */
  public void setValidBits(ByteString validBits) {
    requireNonNull(validBits);

    this.validBits = validBits;
  }

  /**
   * Get the name of the field at the given bit index.
   *
   * @param bitIndex the bit index.
   * @return the name of the field at the given bit index, or {@link Optional#empty()} if no field
   *     exists at the given bit index.
   */
  public Optional<String> getName(int bitIndex) {
    EnumField enumField = getFieldMap().get(bitIndex);

    return Optional.ofNullable(enumField).map(EnumField::getName);
  }

  /**
   * Get the display name of the field at the given bit index.
   *
   * @param bitIndex the bit index.
   * @return the display name of the field at the given bit index, or {@link Optional#empty()} if no
   *     field exists at the given bit index.
   */
  public Optional<LocalizedText> getDisplayName(int bitIndex) {
    EnumField enumField = getFieldMap().get(bitIndex);

    return Optional.ofNullable(enumField).map(EnumField::getDisplayName);
  }

  /**
   * Get the description of the field at the given bit index.
   *
   * @param bitIndex the bit index.
   * @return the description of the field at the given bit index, or {@link Optional#empty()} if no
   *     field exists at the given bit index.
   */
  public Optional<LocalizedText> getDescription(int bitIndex) {
    EnumField enumField = getFieldMap().get(bitIndex);

    return Optional.ofNullable(enumField).map(EnumField::getDescription);
  }

  private Map<Integer, EnumField> getFieldMap() {
    return lazyFieldMap.get(
        () -> {
          Map<Integer, EnumField> fieldMap = Collections.synchronizedMap(new HashMap<>());

          EnumDefinition definition = (EnumDefinition) dataType.getDataTypeDefinition();
          assert definition != null;

          EnumField[] fields = requireNonNullElse(definition.getFields(), new EnumField[0]);

          for (EnumField field : fields) {
            fieldMap.put(field.getValue().intValue(), field);
          }

          return fieldMap;
        });
  }

  /**
   * The value and validBit bit strings are displayed left to right, bit 0 to N.
   *
   * @return a string representation of this {@link DynamicOptionSetType}.
   */
  @Override
  public String toString() {
    return new StringJoiner(", ", DynamicOptionSetType.class.getSimpleName() + "[", "]")
        .add("dataType=" + dataType.getNodeId().toParseableString())
        .add("value=" + toBitString(getValue()))
        .add("validBits=" + toBitString(getValidBits()))
        .toString();
  }

  private static String toBitString(ByteString bs) {
    var sb = new StringBuilder();
    for (byte b : bs.bytesOrEmpty()) {
      for (int i = 0; i < 8; i++) {
        sb.append((b >> i & 1) == 1 ? "1" : "0");
      }
    }
    return sb.toString();
  }

  /**
   * Create a new instance of {@link DynamicOptionSetType} using the given DataType.
   *
   * @param dataType the {@link DataType} of the OptionSet.
   * @param value the value of the OptionSet.
   * @param validBits the valid bits of the OptionSet.
   * @return a new {@link DynamicOptionSetType} with the given DataType.
   */
  public static DynamicOptionSetType newInstance(
      DataType dataType, ByteString value, ByteString validBits) {

    return new DynamicOptionSetType(dataType, value, validBits);
  }

  /**
   * Create a new instance "factory" that produces new {@link DynamicOptionSetType} instances of the
   * given DataType.
   *
   * @param dataType the {@link DataType} of the OptionSet.
   * @return a new instance "factory".
   */
  public static BiFunction<ByteString, ByteString, DynamicOptionSetType> newInstanceFactory(
      DataType dataType) {

    return (value, validBits) -> newInstance(dataType, value, validBits);
  }
}
