/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import java.util.UUID;
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part26/5.6.2">https://reference.opcfoundation.org/v105/Core/docs/Part26/5.6.2</a>
 */
public class SpanContextDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=19746");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=19754");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=19774");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=19804");

  private final UUID traceId;

  private final ULong spanId;

  public SpanContextDataType(UUID traceId, ULong spanId) {
    this.traceId = traceId;
    this.spanId = spanId;
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

  public UUID getTraceId() {
    return traceId;
  }

  public ULong getSpanId() {
    return spanId;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    SpanContextDataType that = (SpanContextDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getTraceId(), that.getTraceId());
    eqb.append(getSpanId(), that.getSpanId());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getTraceId());
    hcb.append(getSpanId());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", SpanContextDataType.class.getSimpleName() + "[", "]");
    joiner.add("traceId=" + getTraceId());
    joiner.add("spanId=" + getSpanId());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 19754),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "TraceId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SpanId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 9),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<SpanContextDataType> {
    @Override
    public Class<SpanContextDataType> getType() {
      return SpanContextDataType.class;
    }

    @Override
    public SpanContextDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final UUID traceId;
      final ULong spanId;
      traceId = decoder.decodeGuid("TraceId");
      spanId = decoder.decodeUInt64("SpanId");
      return new SpanContextDataType(traceId, spanId);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, SpanContextDataType value) {
      encoder.encodeGuid("TraceId", value.getTraceId());
      encoder.encodeUInt64("SpanId", value.getSpanId());
    }
  }
}
