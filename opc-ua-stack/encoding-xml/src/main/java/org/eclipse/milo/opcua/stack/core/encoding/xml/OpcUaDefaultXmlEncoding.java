/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
  public Object encode(EncodingContext context, Object struct, NodeId encodingId) {

    DataTypeCodec codec = context.getDataTypeManager().getCodec(encodingId);

    if (codec != null) {
      // We have to use encoder.writeStruct() instead of codec.encode() because
      // XML-encoded structs are wrapped in a container element with the struct name.
      OpcUaXmlEncoder encoder = new OpcUaXmlEncoder(context);
      encoder.encodeStruct(null, struct, codec);

      return new XmlElement(encoder.getDocumentXml());
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_EncodingError, "no codec registered for encodingId=" + encodingId);
    }
  }

  @Override
  public Object decode(EncodingContext context, Object body, NodeId encodingId) {

    DataTypeCodec codec = context.getDataTypeManager().getCodec(encodingId);

    if (codec != null) {
      XmlElement xmlBody = (XmlElement) body;
      String xml = xmlBody.getFragmentOrEmpty();

      OpcUaXmlDecoder decoder = new OpcUaXmlDecoder(context);
      try {
        decoder.setInput(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
      } catch (IOException | SAXException e) {
        throw new UaSerializationException(StatusCodes.Bad_DecodingError, e);
      }

      // We have to use decoder.readStruct() instead of codec.decode() because
      // XML-encoded structs are wrapped in a container element with the struct name.
      return decoder.decodeStruct(null, codec);
    } else {
      throw new UaSerializationException(
          StatusCodes.Bad_DecodingError, "no codec registered for encodingId=" + encodingId);
    }
  }
}
