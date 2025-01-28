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

import java.util.Map;
import org.eclipse.milo.opcua.sdk.core.types.DynamicUnionType;
import org.eclipse.milo.opcua.sdk.core.types.DynamicUnionType.UnionValue;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.util.Lazy;

public class DynamicUnionCodec extends GenericDataTypeCodec<DynamicUnionType> {

  private final Lazy<Map<StructureField, FieldUtil.FieldHint>> fieldHints = new Lazy<>();

  private final StructureDefinition definition;

  private final DataType dataType;
  private final DataTypeTree dataTypeTree;

  public DynamicUnionCodec(DataType dataType, DataTypeTree dataTypeTree) {
    this.dataType = dataType;
    this.dataTypeTree = dataTypeTree;

    definition = (StructureDefinition) requireNonNull(dataType.getDataTypeDefinition());
  }

  @Override
  public Class<DynamicUnionType> getType() {
    return DynamicUnionType.class;
  }

  @Override
  public DynamicUnionType decodeType(EncodingContext context, UaDecoder decoder)
      throws UaSerializationException {

    return switch (definition.getStructureType()) {
      case Union, UnionWithSubtypedValues -> decodeUnion(decoder);
      default ->
          throw new IllegalArgumentException("StructureType: " + definition.getStructureType());
    };
  }

  @Override
  public void encodeType(EncodingContext context, UaEncoder encoder, DynamicUnionType value)
      throws UaSerializationException {

    switch (definition.getStructureType()) {
      case Union, UnionWithSubtypedValues -> encodeUnion(encoder, value);
      default ->
          throw new IllegalArgumentException("StructureType: " + definition.getStructureType());
    }
  }

  private DynamicUnionType decodeUnion(UaDecoder decoder) {
    int switchField = decoder.decodeUInt32("SwitchField").intValue();

    StructureField[] fields = requireNonNullElse(definition.getFields(), new StructureField[0]);

    if (switchField == 0) {
      return DynamicUnionType.newInstance(dataType, null);
    } else if (switchField <= fields.length) {
      StructureField field = fields[switchField - 1];

      Object value = FieldUtil.decodeFieldValue(decoder, definition, field, getFieldHints());

      return DynamicUnionType.newInstance(
          dataType, new UnionValue(requireNonNull(field.getName()), value));
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_DecodingError, "invalid Union SwitchField value: " + switchField);
    }
  }

  private void encodeUnion(UaEncoder encoder, DynamicUnionType union) {
    StructureField[] fields = requireNonNullElse(definition.getFields(), new StructureField[0]);

    if (union.isNull()) {
      encoder.encodeUInt32("SwitchValue", UInteger.valueOf(0));
    } else {
      UnionValue value = union.getValue().orElseThrow();

      for (int i = 0; i < fields.length; i++) {
        StructureField field = fields[i];
        String fieldName = field.getName();

        if (value.fieldName().equals(fieldName)) {
          encoder.encodeUInt32("SwitchValue", UInteger.valueOf(i + 1));

          FieldUtil.encodeFieldValue(
              encoder, definition, field, getFieldHints(), value.fieldValue());

          // Return as soon as a field has been encoded.
          // Unions are only one field, indicated by SwitchValue.
          return;
        }
      }

      // struct contained no members or the name didn't match a field name... encoding failure.
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, "no Union value found");
    }
  }

  private Map<StructureField, FieldUtil.FieldHint> getFieldHints() {
    return fieldHints.get(() -> FieldUtil.createFieldHints(definition, dataTypeTree));
  }
}
