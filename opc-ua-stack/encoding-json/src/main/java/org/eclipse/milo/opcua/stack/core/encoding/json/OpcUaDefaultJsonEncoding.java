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

import java.io.StringWriter;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
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
  public ExtensionObject encode(
      EncodingContext context, UaStructuredType struct, NodeId encodingId) {

    DataTypeCodec codec = context.getDataTypeManager().getCodec(encodingId);

    if (codec == null) {
      throw new UaSerializationException(
          StatusCodes.Bad_DecodingError, "no codec registered for encodingId=" + encodingId);
    }

    var stringWriter = new StringWriter();
    var encoder = new OpcUaJsonEncoder(context, stringWriter);
    encoder.encodeStruct(null, struct, codec);

    return ExtensionObject.of(stringWriter.toString(), encodingId);
  }

  @Override
  public UaStructuredType decode(
      EncodingContext context, ExtensionObject encoded, NodeId encodingId) {

    if (encoded instanceof ExtensionObject.Json xo) {
      DataTypeCodec codec = context.getDataTypeManager().getCodec(encodingId);

      if (codec == null) {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError, "no codec registered for encodingId=" + encodingId);
      }

      var decoder = new OpcUaJsonDecoder(context, xo.getBody());

      return decoder.decodeStruct(null, codec);
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_DecodingError, "not JSON encoded: " + encoded);
    }
  }
}
