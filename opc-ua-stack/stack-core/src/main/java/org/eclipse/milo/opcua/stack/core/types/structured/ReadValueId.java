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
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.29">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.29</a>
 */
public class ReadValueId extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=626");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=628");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=627");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15256");

  private final NodeId nodeId;

  private final UInteger attributeId;

  private final String indexRange;

  private final QualifiedName dataEncoding;

  public ReadValueId(
      NodeId nodeId, UInteger attributeId, String indexRange, QualifiedName dataEncoding) {
    this.nodeId = nodeId;
    this.attributeId = attributeId;
    this.indexRange = indexRange;
    this.dataEncoding = dataEncoding;
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

  public NodeId getNodeId() {
    return nodeId;
  }

  public UInteger getAttributeId() {
    return attributeId;
  }

  public String getIndexRange() {
    return indexRange;
  }

  public QualifiedName getDataEncoding() {
    return dataEncoding;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ReadValueId that = (ReadValueId) object;
    var eqb = new EqualsBuilder();
    eqb.append(getNodeId(), that.getNodeId());
    eqb.append(getAttributeId(), that.getAttributeId());
    eqb.append(getIndexRange(), that.getIndexRange());
    eqb.append(getDataEncoding(), that.getDataEncoding());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getNodeId());
    hcb.append(getAttributeId());
    hcb.append(getIndexRange());
    hcb.append(getDataEncoding());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ReadValueId.class.getSimpleName() + "[", "]");
    joiner.add("nodeId=" + getNodeId());
    joiner.add("attributeId=" + getAttributeId());
    joiner.add("indexRange='" + getIndexRange() + "'");
    joiner.add("dataEncoding=" + getDataEncoding());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 628),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "NodeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AttributeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 288),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "IndexRange",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 291),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DataEncoding",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 20),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ReadValueId> {
    @Override
    public Class<ReadValueId> getType() {
      return ReadValueId.class;
    }

    @Override
    public ReadValueId decodeType(EncodingContext context, UaDecoder decoder) {
      NodeId nodeId = decoder.decodeNodeId("NodeId");
      UInteger attributeId = decoder.decodeUInt32("AttributeId");
      String indexRange = decoder.decodeString("IndexRange");
      QualifiedName dataEncoding = decoder.decodeQualifiedName("DataEncoding");
      return new ReadValueId(nodeId, attributeId, indexRange, dataEncoding);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, ReadValueId value) {
      encoder.encodeNodeId("NodeId", value.getNodeId());
      encoder.encodeUInt32("AttributeId", value.getAttributeId());
      encoder.encodeString("IndexRange", value.getIndexRange());
      encoder.encodeQualifiedName("DataEncoding", value.getDataEncoding());
    }
  }
}
