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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part26/5.6.3">https://reference.opcfoundation.org/v105/Core/docs/Part26/5.6.3</a>
 */
public class TraceContextDataType extends SpanContextDataType implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=19747");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=19755");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=19775");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=19805");

  private final ULong parentSpanId;

  private final @Nullable String parentIdentifier;

  public TraceContextDataType(
      UUID traceId, ULong spanId, ULong parentSpanId, @Nullable String parentIdentifier) {
    super(traceId, spanId);
    this.parentSpanId = parentSpanId;
    this.parentIdentifier = parentIdentifier;
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

  public ULong getParentSpanId() {
    return parentSpanId;
  }

  public @Nullable String getParentIdentifier() {
    return parentIdentifier;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    TraceContextDataType that = (TraceContextDataType) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getParentSpanId(), that.getParentSpanId());
    eqb.append(getParentIdentifier(), that.getParentIdentifier());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getParentSpanId());
    hcb.append(getParentIdentifier());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", TraceContextDataType.class.getSimpleName() + "[", "]");
    joiner.add("parentSpanId=" + getParentSpanId());
    joiner.add("parentIdentifier='" + getParentIdentifier() + "'");
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 19755),
        new NodeId(0, 19746),
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
              false),
          new StructureField(
              "ParentSpanId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 9),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ParentIdentifier",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<TraceContextDataType> {
    @Override
    public Class<TraceContextDataType> getType() {
      return TraceContextDataType.class;
    }

    @Override
    public TraceContextDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final UUID traceId;
      final ULong spanId;
      final ULong parentSpanId;
      final String parentIdentifier;
      traceId = decoder.decodeGuid("TraceId");
      spanId = decoder.decodeUInt64("SpanId");
      parentSpanId = decoder.decodeUInt64("ParentSpanId");
      parentIdentifier = decoder.decodeString("ParentIdentifier");
      return new TraceContextDataType(traceId, spanId, parentSpanId, parentIdentifier);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, TraceContextDataType value) {
      encoder.encodeGuid("TraceId", value.getTraceId());
      encoder.encodeUInt64("SpanId", value.getSpanId());
      encoder.encodeUInt64("ParentSpanId", value.getParentSpanId());
      encoder.encodeString("ParentIdentifier", value.getParentIdentifier());
    }
  }
}
