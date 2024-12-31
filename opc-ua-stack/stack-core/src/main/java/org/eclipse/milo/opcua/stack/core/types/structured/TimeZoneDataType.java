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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.11">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.2.12/#12.2.12.11</a>
 */
public class TimeZoneDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=8912");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=8917");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=8913");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15086");

  private final Short offset;

  private final Boolean daylightSavingInOffset;

  public TimeZoneDataType(Short offset, Boolean daylightSavingInOffset) {
    this.offset = offset;
    this.daylightSavingInOffset = daylightSavingInOffset;
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

  public Short getOffset() {
    return offset;
  }

  public Boolean getDaylightSavingInOffset() {
    return daylightSavingInOffset;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    TimeZoneDataType that = (TimeZoneDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getOffset(), that.getOffset());
    eqb.append(getDaylightSavingInOffset(), that.getDaylightSavingInOffset());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getOffset());
    hcb.append(getDaylightSavingInOffset());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", TimeZoneDataType.class.getSimpleName() + "[", "]");
    joiner.add("offset=" + getOffset());
    joiner.add("daylightSavingInOffset=" + getDaylightSavingInOffset());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 8917),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Offset",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 4),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DaylightSavingInOffset",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<TimeZoneDataType> {
    @Override
    public Class<TimeZoneDataType> getType() {
      return TimeZoneDataType.class;
    }

    @Override
    public TimeZoneDataType decodeType(EncodingContext context, UaDecoder decoder) {
      Short offset = decoder.decodeInt16("Offset");
      Boolean daylightSavingInOffset = decoder.decodeBoolean("DaylightSavingInOffset");
      return new TimeZoneDataType(offset, daylightSavingInOffset);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, TimeZoneDataType value) {
      encoder.encodeInt16("Offset", value.getOffset());
      encoder.encodeBoolean("DaylightSavingInOffset", value.getDaylightSavingInOffset());
    }
  }
}
