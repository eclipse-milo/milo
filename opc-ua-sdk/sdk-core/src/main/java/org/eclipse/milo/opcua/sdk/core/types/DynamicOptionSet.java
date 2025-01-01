/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.types;

import static java.util.Objects.requireNonNullElse;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumField;
import org.eclipse.milo.opcua.stack.core.util.Lazy;
import org.jspecify.annotations.Nullable;

public class DynamicOptionSet extends DynamicStruct {

  private final Lazy<Map<Integer, EnumField>> lazyFieldMap = new Lazy<>();

  public DynamicOptionSet(DataType dataType) {
    this(dataType, new LinkedHashMap<>());
  }

  public DynamicOptionSet(DataType dataType, LinkedHashMap<String, Object> members) {
    super(dataType, members);
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

  public @Nullable LocalizedText getDisplayName(int bitIndex) {
    EnumField enumField = getFieldMap().get(bitIndex);

    return enumField != null ? enumField.getDisplayName() : null;
  }

  private Map<Integer, EnumField> getFieldMap() {
    return lazyFieldMap.get(
        () -> {
          Map<Integer, EnumField> fieldMap = Collections.synchronizedMap(new HashMap<>());

          EnumDefinition definition = (EnumDefinition) getDataType().getDataTypeDefinition();
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
    return new StringJoiner(", ", DynamicOptionSet.class.getSimpleName() + "{", "}")
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
