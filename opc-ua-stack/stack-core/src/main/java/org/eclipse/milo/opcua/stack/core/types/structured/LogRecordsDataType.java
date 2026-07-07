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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part26/5.10">https://reference.opcfoundation.org/v105/Core/docs/Part26/5.10</a>
 */
public class LogRecordsDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=19745");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=19753");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=19773");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=19803");

  private final LogRecord @Nullable [] logRecordArray;

  public LogRecordsDataType(LogRecord @Nullable [] logRecordArray) {
    this.logRecordArray = logRecordArray;
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

  public LogRecord @Nullable [] getLogRecordArray() {
    return logRecordArray;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    LogRecordsDataType that = (LogRecordsDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getLogRecordArray(), that.getLogRecordArray());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getLogRecordArray());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", LogRecordsDataType.class.getSimpleName() + "[", "]");
    joiner.add("logRecordArray=" + java.util.Arrays.toString(getLogRecordArray()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 19753),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "LogRecordArray",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 19361),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<LogRecordsDataType> {
    @Override
    public Class<LogRecordsDataType> getType() {
      return LogRecordsDataType.class;
    }

    @Override
    public LogRecordsDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final LogRecord[] logRecordArray;
      logRecordArray = (LogRecord[]) decoder.decodeStructArray("LogRecordArray", LogRecord.TYPE_ID);
      return new LogRecordsDataType(logRecordArray);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, LogRecordsDataType value) {
      encoder.encodeStructArray("LogRecordArray", value.getLogRecordArray(), LogRecord.TYPE_ID);
    }
  }
}
