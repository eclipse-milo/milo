/*
 * Copyright (c) 2024 the Eclipse Milo Authors
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.2/#6.3.2.1.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.2/#6.3.2.1.2</a>
 */
public class JsonWriterGroupMessageDataType extends WriterGroupMessageDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=15657");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=15719");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=16017");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=16393");

  private final JsonNetworkMessageContentMask networkMessageContentMask;

  public JsonWriterGroupMessageDataType(JsonNetworkMessageContentMask networkMessageContentMask) {
    this.networkMessageContentMask = networkMessageContentMask;
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

  public JsonNetworkMessageContentMask getNetworkMessageContentMask() {
    return networkMessageContentMask;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    JsonWriterGroupMessageDataType that = (JsonWriterGroupMessageDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getNetworkMessageContentMask(), that.getNetworkMessageContentMask());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getNetworkMessageContentMask());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", JsonWriterGroupMessageDataType.class.getSimpleName() + "[", "]");
    joiner.add("networkMessageContentMask=" + getNetworkMessageContentMask());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 15719),
        new NodeId(0, 15616),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "NetworkMessageContentMask",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15654),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<JsonWriterGroupMessageDataType> {
    @Override
    public Class<JsonWriterGroupMessageDataType> getType() {
      return JsonWriterGroupMessageDataType.class;
    }

    @Override
    public JsonWriterGroupMessageDataType decodeType(EncodingContext context, UaDecoder decoder) {
      JsonNetworkMessageContentMask networkMessageContentMask =
          new JsonNetworkMessageContentMask(decoder.decodeUInt32("NetworkMessageContentMask"));
      return new JsonWriterGroupMessageDataType(networkMessageContentMask);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, JsonWriterGroupMessageDataType value) {
      encoder.encodeUInt32(
          "NetworkMessageContentMask", value.getNetworkMessageContentMask().getValue());
    }
  }
}
