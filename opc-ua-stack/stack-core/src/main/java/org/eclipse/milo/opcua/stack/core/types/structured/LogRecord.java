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

import java.util.Objects;
import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
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

  private final UInteger opcUaPresenceMask;

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
    this(
        Unsigned.uint(
            0L
                | (eventType != null ? 1L : 0L)
                | (sourceNode != null ? 2L : 0L)
                | (sourceName != null ? 4L : 0L)
                | (traceContext != null ? 8L : 0L)
                | (additionalData != null ? 16L : 0L)),
        time,
        severity,
        eventType,
        sourceNode,
        sourceName,
        message,
        traceContext,
        additionalData);
  }

  /**
   * Creates a value with explicit optional-field presence. Bits follow inherited then local
   * optional fields.
   */
  public LogRecord(
      UInteger presenceMask,
      DateTime time,
      UShort severity,
      @Nullable NodeId eventType,
      @Nullable NodeId sourceNode,
      @Nullable String sourceName,
      LocalizedText message,
      @Nullable TraceContextDataType traceContext,
      NameValuePair @Nullable [] additionalData) {
    Objects.requireNonNull(presenceMask, "presenceMask");
    if ((presenceMask.longValue() & ~31L) != 0) {
      throw new IllegalArgumentException("Unknown optional-field bits for i=19361");
    }
    if ((presenceMask.longValue() & 1L) == 0 && eventType != null) {
      throw new IllegalArgumentException("Absent optional field has a value: EventType");
    }
    if ((presenceMask.longValue() & 2L) == 0 && sourceNode != null) {
      throw new IllegalArgumentException("Absent optional field has a value: SourceNode");
    }
    if ((presenceMask.longValue() & 4L) == 0 && sourceName != null) {
      throw new IllegalArgumentException("Absent optional field has a value: SourceName");
    }
    if ((presenceMask.longValue() & 8L) == 0 && traceContext != null) {
      throw new IllegalArgumentException("Absent optional field has a value: TraceContext");
    }
    if ((presenceMask.longValue() & 16L) == 0 && additionalData != null) {
      throw new IllegalArgumentException("Absent optional field has a value: AdditionalData");
    }
    this.opcUaPresenceMask = presenceMask;
    this.time = time;
    this.severity = severity;
    this.eventType = eventType;
    this.sourceNode = sourceNode;
    this.sourceName = sourceName;
    this.message = message;
    this.traceContext = traceContext;
    this.additionalData = additionalData;
  }

  /** Returns presence bits for inherited and local optional fields. */
  public UInteger opcUaEncodingMask() {
    return opcUaPresenceMask;
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
    eqb.append(opcUaEncodingMask().longValue() & 31L, that.opcUaEncodingMask().longValue() & 31L);
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
    hcb.append(opcUaEncodingMask().longValue() & 31L);
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
        NodeId.parse("i=19379"),
        NodeId.parse("i=22"),
        StructureType.StructureWithOptionalFields,
        new StructureField[] {
          new StructureField(
              "Time",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=13"),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Severity",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=5"),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "EventType",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=17"),
              -1,
              null,
              UInteger.valueOf(0),
              true),
          new StructureField(
              "SourceNode",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=17"),
              -1,
              null,
              UInteger.valueOf(0),
              true),
          new StructureField(
              "SourceName",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=12"),
              -1,
              null,
              UInteger.valueOf(0),
              true),
          new StructureField(
              "Message",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=21"),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TraceContext",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=19747"),
              -1,
              null,
              UInteger.valueOf(0),
              true),
          new StructureField(
              "AdditionalData",
              LocalizedText.NULL_VALUE,
              NodeId.parse("i=19748"),
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
      if ((encodingMask & ~31L) != 0) {
        throw new UaSerializationException(
            StatusCodes.Bad_DecodingError, "Unknown optional-field bits for i=19361");
      }
      time = decoder.decodeDateTime("Time");
      severity = decoder.decodeUInt16("Severity");
      if ((encodingMask & 1L) != 0) {
        eventType = decoder.decodeNodeId("EventType");
      } else {
        eventType = null;
      }
      if ((encodingMask & 2L) != 0) {
        sourceNode = decoder.decodeNodeId("SourceNode");
      } else {
        sourceNode = null;
      }
      if ((encodingMask & 4L) != 0) {
        sourceName = decoder.decodeString("SourceName");
      } else {
        sourceName = null;
      }
      message = decoder.decodeLocalizedText("Message");
      if ((encodingMask & 8L) != 0) {
        traceContext =
            (TraceContextDataType)
                decoder.decodeStruct("TraceContext", TraceContextDataType.TYPE_ID);
      } else {
        traceContext = null;
      }
      if ((encodingMask & 16L) != 0) {
        additionalData =
            (NameValuePair[]) decoder.decodeStructArray("AdditionalData", NameValuePair.TYPE_ID);
      } else {
        additionalData = null;
      }
      return new LogRecord(
          Unsigned.uint(encodingMask),
          time,
          severity,
          eventType,
          sourceNode,
          sourceName,
          message,
          traceContext,
          additionalData);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, LogRecord value) {
      long encodingMask = value.opcUaEncodingMask().longValue() & 31L;
      encoder.encodeUInt32("EncodingMask", Unsigned.uint(encodingMask));
      encoder.encodeDateTime("Time", value.getTime());
      encoder.encodeUInt16("Severity", value.getSeverity());
      if ((encodingMask & 1L) != 0) {
        encoder.encodeNodeId("EventType", value.getEventType());
      }
      if ((encodingMask & 2L) != 0) {
        encoder.encodeNodeId("SourceNode", value.getSourceNode());
      }
      if ((encodingMask & 4L) != 0) {
        encoder.encodeString("SourceName", value.getSourceName());
      }
      encoder.encodeLocalizedText("Message", value.getMessage());
      if ((encodingMask & 8L) != 0) {
        encoder.encodeStruct("TraceContext", value.getTraceContext(), TraceContextDataType.TYPE_ID);
      }
      if ((encodingMask & 16L) != 0) {
        encoder.encodeStructArray(
            "AdditionalData", value.getAdditionalData(), NameValuePair.TYPE_ID);
      }
    }
  }
}
