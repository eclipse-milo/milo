/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.xml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.DataTypeEncoding;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.xml.sax.SAXException;

public class OpcUaDefaultXmlEncoding implements DataTypeEncoding {

  public static final QualifiedName ENCODING_NAME = new QualifiedName(0, "Default XML");

  public static OpcUaDefaultXmlEncoding getInstance() {
    return InstanceHolder.INSTANCE;
  }

  private static class InstanceHolder {
    private static final OpcUaDefaultXmlEncoding INSTANCE = new OpcUaDefaultXmlEncoding();
  }

  @Override
  public QualifiedName getEncodingName() {
    return ENCODING_NAME;
  }

  @Override
  public ExtensionObject encode(EncodingContext context, UaStructuredType struct) {
    NodeId encodingId =
        struct
            .getXmlEncodingId()
            .toNodeId(context.getNamespaceTable())
            .orElseThrow(
                () ->
                    new UaSerializationException(
                        StatusCodes.Bad_EncodingError,
                        "no namespace registered: "
                            + struct.getXmlEncodingId().toParseableString()));

    DataTypeCodec codec = context.getDataTypeManager().getCodec(encodingId);

    if (codec == null) {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingError,
          "no codec registered for encodingId=" + encodingId.toParseableString());
    }

    OpcUaXmlEncoder encoder = new OpcUaXmlEncoder(context);

    encoder.encodeStruct(struct.getEncodingName(), struct, codec);

    return ExtensionObject.of(XmlElement.of(encoder.getDocumentXml()), encodingId);
  }

  @Override
  public UaStructuredType decode(EncodingContext context, ExtensionObject encoded) {
    if (encoded instanceof ExtensionObject.Xml xo) {
      DataTypeCodec codec = context.getDataTypeManager().getCodec(encoded.getEncodingOrTypeId());

      if (codec == null) {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError,
            "no codec registered for encodingId="
                + encoded.getEncodingOrTypeId().toParseableString());
      }

      XmlElement xmlBody = xo.getBody();
      String xml = xmlBody.getFragmentOrEmpty();

      OpcUaXmlDecoder decoder = new OpcUaXmlDecoder(context);
      try {
        decoder.setInput(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
      } catch (IOException | SAXException e) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
      }

      // We have to use decoder.decodeStruct() instead of codec.decode() because
      // XML-encoded structs are wrapped in a container element with the struct name.
      return decoder.decodeStruct(null, codec);
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_DecodingError, "not XML encoded" + encoded);
    }
  }
}
