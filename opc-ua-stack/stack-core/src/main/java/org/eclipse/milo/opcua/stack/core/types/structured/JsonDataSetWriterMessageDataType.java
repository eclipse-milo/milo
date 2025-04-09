/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.2/#6.3.2.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.2/#6.3.2.3.2</a>
 */
public class JsonDataSetWriterMessageDataType extends DataSetWriterMessageDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=15664");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=15724");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=16018");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=16394");

  private final JsonDataSetMessageContentMask dataSetMessageContentMask;

  public JsonDataSetWriterMessageDataType(JsonDataSetMessageContentMask dataSetMessageContentMask) {
    this.dataSetMessageContentMask = dataSetMessageContentMask;
  }

  @Override
  public ExpandedNodeId getTypeId() {
    return TYPE_ID;
  }

  @Override
  public ExpandedNodeId getBinaryEncodingId() {
    return BINARY_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getXmlEncodingId() {
    return XML_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getJsonEncodingId() {
    return JSON_ENCODING_ID;
  }

  public JsonDataSetMessageContentMask getDataSetMessageContentMask() {
    return dataSetMessageContentMask;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    JsonDataSetWriterMessageDataType that = (JsonDataSetWriterMessageDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getDataSetMessageContentMask(), that.getDataSetMessageContentMask());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getDataSetMessageContentMask());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", JsonDataSetWriterMessageDataType.class.getSimpleName() + "[", "]");
    joiner.add("dataSetMessageContentMask=" + getDataSetMessageContentMask());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 15724),
        new NodeId(0, 15605),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "DataSetMessageContentMask",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15658),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<JsonDataSetWriterMessageDataType> {
    @Override
    public Class<JsonDataSetWriterMessageDataType> getType() {
      return JsonDataSetWriterMessageDataType.class;
    }

    @Override
    public JsonDataSetWriterMessageDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final JsonDataSetMessageContentMask dataSetMessageContentMask;
      dataSetMessageContentMask =
          new JsonDataSetMessageContentMask(decoder.decodeUInt32("DataSetMessageContentMask"));
      return new JsonDataSetWriterMessageDataType(dataSetMessageContentMask);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, JsonDataSetWriterMessageDataType value) {
      encoder.encodeUInt32(
          "DataSetMessageContentMask", value.getDataSetMessageContentMask().getValue());
    }
  }
}
