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
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part26/5.5">https://reference.opcfoundation.org/v105/Core/docs/Part26/5.5</a>
 */
public class LogRecord extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=19361");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=19379");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=19383");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=19387");

  private final DateTime time;

  private final UShort severity;

  private final @Nullable NodeId eventType;

  private final @Nullable NodeId sourceNode;

  private final @Nullable String sourceName;

  private final LocalizedText message;

  private final @Nullable TraceContextDataType traceContext;

  private final NameValuePair @Nullable [] additionalData;

  public LogRecord(
      DateTime time,
      UShort severity,
      @Nullable NodeId eventType,
      @Nullable NodeId sourceNode,
      @Nullable String sourceName,
      LocalizedText message,
      @Nullable TraceContextDataType traceContext,
      NameValuePair @Nullable [] additionalData) {
    this.time = time;
    this.severity = severity;
    this.eventType = eventType;
    this.sourceNode = sourceNode;
    this.sourceName = sourceName;
    this.message = message;
    this.traceContext = traceContext;
    this.additionalData = additionalData;
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

  public DateTime getTime() {
    return time;
  }

  public UShort getSeverity() {
    return severity;
  }

  public @Nullable NodeId getEventType() {
    return eventType;
  }

  public @Nullable NodeId getSourceNode() {
    return sourceNode;
  }

  public @Nullable String getSourceName() {
    return sourceName;
  }

  public LocalizedText getMessage() {
    return message;
  }

  public @Nullable TraceContextDataType getTraceContext() {
    return traceContext;
  }

  public NameValuePair @Nullable [] getAdditionalData() {
    return additionalData;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    LogRecord that = (LogRecord) object;
    var eqb = new EqualsBuilder();
    eqb.append(getTime(), that.getTime());
    eqb.append(getSeverity(), that.getSeverity());
    eqb.append(getEventType(), that.getEventType());
    eqb.append(getSourceNode(), that.getSourceNode());
    eqb.append(getSourceName(), that.getSourceName());
    eqb.append(getMessage(), that.getMessage());
    eqb.append(getTraceContext(), that.getTraceContext());
    eqb.append(getAdditionalData(), that.getAdditionalData());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getTime());
    hcb.append(getSeverity());
    hcb.append(getEventType());
    hcb.append(getSourceNode());
    hcb.append(getSourceName());
    hcb.append(getMessage());
    hcb.append(getTraceContext());
    hcb.append(getAdditionalData());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", LogRecord.class.getSimpleName() + "[", "]");
    joiner.add("time=" + getTime());
    joiner.add("severity=" + getSeverity());
    joiner.add("eventType=" + getEventType());
    joiner.add("sourceNode=" + getSourceNode());
    joiner.add("sourceName='" + getSourceName() + "'");
    joiner.add("message=" + getMessage());
    joiner.add("traceContext=" + getTraceContext());
    joiner.add("additionalData=" + java.util.Arrays.toString(getAdditionalData()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 19379),
        new NodeId(0, 22),
        StructureType.StructureWithOptionalFields,
        new StructureField[] {
          new StructureField(
              "Time",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 13),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Severity",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 5),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "EventType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              true),
          new StructureField(
              "SourceNode",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              true),
          new StructureField(
              "SourceName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              true),
          new StructureField(
              "Message",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 21),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TraceContext",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 19747),
              -1,
              null,
              UInteger.valueOf(0),
              true),
          new StructureField(
              "AdditionalData",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 19748),
              1,
              null,
              UInteger.valueOf(0),
              true)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<LogRecord> {
    @Override
    public Class<LogRecord> getType() {
      return LogRecord.class;
    }

    @Override
    public LogRecord decodeType(EncodingContext context, UaDecoder decoder) {
      final DateTime time;
      final UShort severity;
      final NodeId eventType;
      final NodeId sourceNode;
      final String sourceName;
      final LocalizedText message;
      final TraceContextDataType traceContext;
      final NameValuePair[] additionalData;
      final long encodingMask = decoder.decodeUInt32("EncodingMask").longValue();
      time = decoder.decodeDateTime("Time");
      severity = decoder.decodeUInt16("Severity");
      if ((encodingMask & (1L << 0)) != 0) {
        eventType = decoder.decodeNodeId("EventType");
      } else {
        eventType = null;
      }
      if ((encodingMask & (1L << 1)) != 0) {
        sourceNode = decoder.decodeNodeId("SourceNode");
      } else {
        sourceNode = null;
      }
      if ((encodingMask & (1L << 2)) != 0) {
        sourceName = decoder.decodeString("SourceName");
      } else {
        sourceName = null;
      }
      message = decoder.decodeLocalizedText("Message");
      if ((encodingMask & (1L << 3)) != 0) {
        traceContext =
            (TraceContextDataType)
                decoder.decodeStruct("TraceContext", TraceContextDataType.TYPE_ID);
      } else {
        traceContext = null;
      }
      if ((encodingMask & (1L << 4)) != 0) {
        additionalData =
            (NameValuePair[]) decoder.decodeStructArray("AdditionalData", NameValuePair.TYPE_ID);
      } else {
        additionalData = null;
      }
      return new LogRecord(
          time, severity, eventType, sourceNode, sourceName, message, traceContext, additionalData);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, LogRecord value) {
      long encodingMask = 0L;
      if (value.getEventType() != null) {
        encodingMask |= (1L << 0);
      }
      if (value.getSourceNode() != null) {
        encodingMask |= (1L << 1);
      }
      if (value.getSourceName() != null) {
        encodingMask |= (1L << 2);
      }
      if (value.getTraceContext() != null) {
        encodingMask |= (1L << 3);
      }
      if (value.getAdditionalData() != null) {
        encodingMask |= (1L << 4);
      }
      encoder.encodeUInt32("EncodingMask", Unsigned.uint(encodingMask));
      encoder.encodeDateTime("Time", value.getTime());
      encoder.encodeUInt16("Severity", value.getSeverity());
      if (value.getEventType() != null) {
        encoder.encodeNodeId("EventType", value.getEventType());
      }
      if (value.getSourceNode() != null) {
        encoder.encodeNodeId("SourceNode", value.getSourceNode());
      }
      if (value.getSourceName() != null) {
        encoder.encodeString("SourceName", value.getSourceName());
      }
      encoder.encodeLocalizedText("Message", value.getMessage());
      if (value.getTraceContext() != null) {
        encoder.encodeStruct("TraceContext", value.getTraceContext(), TraceContextDataType.TYPE_ID);
      }
      if (value.getAdditionalData() != null) {
        encoder.encodeStructArray(
            "AdditionalData", value.getAdditionalData(), NameValuePair.TYPE_ID);
      }
    }
  }
}
