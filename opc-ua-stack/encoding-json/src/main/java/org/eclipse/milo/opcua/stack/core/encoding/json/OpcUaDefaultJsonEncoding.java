/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.json;

import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.DataTypeEncoding;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;

public class OpcUaDefaultJsonEncoding implements DataTypeEncoding {

  public static final QualifiedName ENCODING_NAME = new QualifiedName(0, "Default JSON");

  public static OpcUaDefaultJsonEncoding getInstance() {
    return InstanceHolder.INSTANCE;
  }

  private static class InstanceHolder {
    private static final OpcUaDefaultJsonEncoding INSTANCE = new OpcUaDefaultJsonEncoding();
  }

  private OpcUaDefaultJsonEncoding() {}

  @Override
  public QualifiedName getEncodingName() {
    return ENCODING_NAME;
  }

  @Override
  public ExtensionObject encode(EncodingContext context, UaStructuredType struct) {
    // Note: JSON encoding uses the DataType NodeId, not a separate encoding NodeId.
    NodeId typeId;
    try {
      typeId = struct.getTypeId().toNodeIdOrThrow(context.getNamespaceTable());
    } catch (UaException e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }

    DataTypeCodec codec = context.getDataTypeManager().getCodec(typeId);

    if (codec == null) {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingError,
          "no codec registered for typeId=" + typeId.toParseableString());
    }

    try (var encoder = new OpcUaJsonEncoder(context)) {
      encoder.encodeStruct(null, struct, codec);

      return ExtensionObject.of(encoder.getOutputString(), typeId);
    } catch (Exception e) {
      throw new UaSerializationException(StatusCodes.Bad_EncodingError, e);
    }
  }

  @Override
  public UaStructuredType decode(EncodingContext context, ExtensionObject encoded) {
    if (encoded instanceof ExtensionObject.Json xo) {
      DataTypeCodec codec = context.getDataTypeManager().getCodec(encoded.getEncodingOrTypeId());

      if (codec == null) {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError,
            "no codec registered for encodingId="
                + encoded.getEncodingOrTypeId().toParseableString());
      }

      var decoder = new OpcUaJsonDecoder(context, xo.getBody());

      return decoder.decodeStruct(null, codec);
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_DecodingError, "not JSON encoded: " + encoded);
    }
  }
}
