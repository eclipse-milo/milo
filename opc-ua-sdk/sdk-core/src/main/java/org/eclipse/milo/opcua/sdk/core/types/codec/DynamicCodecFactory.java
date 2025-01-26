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

import org.eclipse.milo.opcua.sdk.core.types.DynamicStructType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.types.structured.DataTypeDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.EnumDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;

/**
 * A {@link DataTypeCodec} factory that creates codecs that decodes and encodes types based on
 * {@link DynamicStructType}.
 */
public class DynamicCodecFactory {

  public static DataTypeCodec create(DataType dataType, DataTypeTree dataTypeTree) {
    DataTypeDefinition definition = dataType.getDataTypeDefinition();

    if (definition instanceof EnumDefinition) {
      // If we're asked to create a DataTypeCodec and the definition is an EnumDefinition,
      // that means it's an OptionSet subclass. True enumerations are encoded/decoded as
      // integers, so they don't have a corresponding codec.
      return new DynamicOptionSetCodec(dataType);
    } else if (definition instanceof StructureDefinition structureDefinition) {
      return switch (structureDefinition.getStructureType()) {
        case Structure, StructureWithOptionalFields, StructureWithSubtypedValues ->
            new DynamicStructCodec(dataType, dataTypeTree);
        case Union, UnionWithSubtypedValues -> new DynamicUnionCodec(dataType, dataTypeTree);
      };
    } else {
      throw new RuntimeException("unknown DataTypeDefinition: " + definition);
    }
  }
}
