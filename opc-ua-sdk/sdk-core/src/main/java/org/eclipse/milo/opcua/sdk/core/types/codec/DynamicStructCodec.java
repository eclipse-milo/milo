/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.types.codec;

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;

import java.util.LinkedHashMap;
import java.util.Map;
import org.eclipse.milo.opcua.sdk.core.types.DynamicStructType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.util.Lazy;
import org.jspecify.annotations.NonNull;

public class DynamicStructCodec extends GenericDataTypeCodec<DynamicStructType> {

  private final Lazy<Map<StructureField, FieldUtil.FieldHint>> fieldHints = new Lazy<>();

  private final StructureDefinition definition;

  private final DataType dataType;
  private final DataTypeTree dataTypeTree;

  public DynamicStructCodec(DataType dataType, DataTypeTree dataTypeTree) {
    this.dataType = dataType;
    this.dataTypeTree = dataTypeTree;

    this.definition = (StructureDefinition) requireNonNull(dataType.getDataTypeDefinition());
  }

  @Override
  public Class<DynamicStructType> getType() {
    return DynamicStructType.class;
  }

  @Override
  public DynamicStructType decodeType(EncodingContext context, UaDecoder decoder)
      throws UaSerializationException {

    return switch (definition.getStructureType()) {
      case Structure, StructureWithOptionalFields, StructureWithSubtypedValues ->
          decodeStruct(decoder);
      default ->
          throw new IllegalArgumentException("StructureType: " + definition.getStructureType());
    };
  }

  @Override
  public void encodeType(EncodingContext context, UaEncoder encoder, DynamicStructType value)
      throws UaSerializationException {

    switch (definition.getStructureType()) {
      case Structure, StructureWithOptionalFields, StructureWithSubtypedValues ->
          encodeStruct(encoder, value);
      default ->
          throw new IllegalArgumentException("StructureType: " + definition.getStructureType());
    }
  }

  private @NonNull DynamicStructType decodeStruct(UaDecoder decoder) {
    LinkedHashMap<String, Object> members = new LinkedHashMap<>();

    long switchField = 0xFFFFFFFFL;
    if (definition.getStructureType() == StructureType.StructureWithOptionalFields) {
      switchField = decoder.decodeUInt32("SwitchField").longValue();
    }

    StructureField[] fields = requireNonNullElse(definition.getFields(), new StructureField[0]);

    if (definition.getStructureType() == StructureType.StructureWithOptionalFields) {
      int optionalFieldIndex = 0;
      for (StructureField field : fields) {
        if (!field.getIsOptional() || (switchField >>> optionalFieldIndex++ & 1L) == 1L) {
          Object value = FieldUtil.decodeFieldValue(decoder, definition, field, getFieldHints());

          members.put(requireNonNull(field.getName()), value);
        }
      }
    } else {
      for (StructureField field : fields) {
        Object value = FieldUtil.decodeFieldValue(decoder, definition, field, getFieldHints());

        members.put(requireNonNull(field.getName()), value);
      }
    }

    return new DynamicStructType(dataType, members);
  }

  private void encodeStruct(UaEncoder encoder, DynamicStructType struct) {
    StructureField[] fields = requireNonNullElse(definition.getFields(), new StructureField[0]);

    var switchField = 0L;
    if (definition.getStructureType() == StructureType.StructureWithOptionalFields) {
      int optionalFieldIndex = 0;
      for (StructureField field : fields) {
        if (field.getIsOptional()
            && struct.getMembers().containsKey(requireNonNull(field.getName()))) {
          switchField = switchField | (1L << optionalFieldIndex++);
        }
      }
      encoder.encodeUInt32("SwitchField", UInteger.valueOf(switchField));
    }

    if (definition.getStructureType() == StructureType.StructureWithOptionalFields) {
      int optionalFieldIndex = 0;
      for (StructureField field : fields) {
        if (!field.getIsOptional() || ((switchField >>> optionalFieldIndex++) & 1L) == 1L) {
          Object value = struct.getMembers().get(field.getName());
          FieldUtil.encodeFieldValue(encoder, definition, field, getFieldHints(), value);
        }
      }
    } else {
      for (StructureField field : fields) {
        Object value = struct.getMembers().get(field.getName());
        FieldUtil.encodeFieldValue(encoder, definition, field, getFieldHints(), value);
      }
    }
  }

  private Map<StructureField, FieldUtil.FieldHint> getFieldHints() {
    return fieldHints.get(() -> FieldUtil.createFieldHints(definition, dataTypeTree));
  }
}
