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
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.2/#5.3.2.4">https://reference.opcfoundation.org/v105/Core/docs/Part22/5.3.2/#5.3.2.4</a>
 */
public class LldpTlvType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=18955");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=19081");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=19102");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=19301");

  private final UInteger tlvType;

  private final ByteString tlvInfo;

  public LldpTlvType(UInteger tlvType, ByteString tlvInfo) {
    this.tlvType = tlvType;
    this.tlvInfo = tlvInfo;
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

  public UInteger getTlvType() {
    return tlvType;
  }

  public ByteString getTlvInfo() {
    return tlvInfo;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    LldpTlvType that = (LldpTlvType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getTlvType(), that.getTlvType());
    eqb.append(getTlvInfo(), that.getTlvInfo());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getTlvType());
    hcb.append(getTlvInfo());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", LldpTlvType.class.getSimpleName() + "[", "]");
    joiner.add("tlvType=" + getTlvType());
    joiner.add("tlvInfo=" + getTlvInfo());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 19081),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "TlvType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TlvInfo",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<LldpTlvType> {
    @Override
    public Class<LldpTlvType> getType() {
      return LldpTlvType.class;
    }

    @Override
    public LldpTlvType decodeType(EncodingContext context, UaDecoder decoder) {
      final UInteger tlvType;
      final ByteString tlvInfo;
      tlvType = decoder.decodeUInt32("TlvType");
      tlvInfo = decoder.decodeByteString("TlvInfo");
      return new LldpTlvType(tlvType, tlvInfo);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, LldpTlvType value) {
      encoder.encodeUInt32("TlvType", value.getTlvType());
      encoder.encodeByteString("TlvInfo", value.getTlvInfo());
    }
  }
}
