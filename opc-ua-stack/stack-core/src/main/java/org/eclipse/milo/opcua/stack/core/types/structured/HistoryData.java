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
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v104/Core/docs/Part11/6.5.2">https://reference.opcfoundation.org/v104/Core/docs/Part11/6.5.2</a>
 */
public class HistoryData extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=656");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=658");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=657");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15270");

  private final DataValue @Nullable [] dataValues;

  public HistoryData(DataValue @Nullable [] dataValues) {
    this.dataValues = dataValues;
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

  public DataValue @Nullable [] getDataValues() {
    return dataValues;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    HistoryData that = (HistoryData) object;
    var eqb = new EqualsBuilder();
    eqb.append(getDataValues(), that.getDataValues());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getDataValues());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", HistoryData.class.getSimpleName() + "[", "]");
    joiner.add("dataValues=" + java.util.Arrays.toString(getDataValues()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 658),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "DataValues",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 23),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<HistoryData> {
    @Override
    public Class<HistoryData> getType() {
      return HistoryData.class;
    }

    @Override
    public HistoryData decodeType(EncodingContext context, UaDecoder decoder) {
      DataValue[] dataValues = decoder.decodeDataValueArray("DataValues");
      return new HistoryData(dataValues);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, HistoryData value) {
      encoder.encodeDataValueArray("DataValues", value.getDataValues());
    }
  }
}
