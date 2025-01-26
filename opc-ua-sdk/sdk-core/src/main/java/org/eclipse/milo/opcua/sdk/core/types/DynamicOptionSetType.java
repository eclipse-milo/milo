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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumField;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

public final class DynamicOptionSetType extends DynamicType implements UaStructuredType {

  private final Lazy<Map<Integer, EnumField>> lazyFieldMap = new Lazy<>();

  private final DataType dataType;
  private final LinkedHashMap<String, Object> members;

  public DynamicOptionSetType(DataType dataType) {
    this(dataType, new LinkedHashMap<>());
  }

  public DynamicOptionSetType(DataType dataType, LinkedHashMap<String, Object> members) {
    this.dataType = dataType;
    this.members = members;
  }

  @Override
  public ExpandedNodeId getTypeId() {
    return dataType.getNodeId().expanded();
  }

  @Override
  public DataType getDataType() {
    return dataType;
  }

  @Override
  public EnumDefinition getDataTypeDefinition() {
    return (EnumDefinition) requireNonNull(dataType.getDataTypeDefinition());
  }

  public LinkedHashMap<String, Object> getMembers() {
    return members;
  }

  public ByteString getValue() {
    return (ByteString) getMembers().get("Value");
  }

  public ByteString getValidBits() {
    return (ByteString) getMembers().get("ValidBits");
  }

  public void setValue(ByteString value) {
    getMembers().put("Value", value);
  }

  public void setValidBits(ByteString validBits) {
    getMembers().put("ValidBits", validBits);
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
}
