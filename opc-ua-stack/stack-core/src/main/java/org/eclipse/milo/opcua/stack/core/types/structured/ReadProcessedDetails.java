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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
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
 *     href="https://reference.opcfoundation.org/v104/Core/docs/Part11/6.4.4/#6.4.4.1">https://reference.opcfoundation.org/v104/Core/docs/Part11/6.4.4/#6.4.4.1</a>
 */
public class ReadProcessedDetails extends HistoryReadDetails implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=650");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=652");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=651");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15264");

  private final DateTime startTime;

  private final DateTime endTime;

  private final Double processingInterval;

  private final NodeId @Nullable [] aggregateType;

  private final AggregateConfiguration aggregateConfiguration;

  public ReadProcessedDetails(
      DateTime startTime,
      DateTime endTime,
      Double processingInterval,
      NodeId @Nullable [] aggregateType,
      AggregateConfiguration aggregateConfiguration) {
    this.startTime = startTime;
    this.endTime = endTime;
    this.processingInterval = processingInterval;
    this.aggregateType = aggregateType;
    this.aggregateConfiguration = aggregateConfiguration;
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

  public DateTime getStartTime() {
    return startTime;
  }

  public DateTime getEndTime() {
    return endTime;
  }

  public Double getProcessingInterval() {
    return processingInterval;
  }

  public NodeId @Nullable [] getAggregateType() {
    return aggregateType;
  }

  public AggregateConfiguration getAggregateConfiguration() {
    return aggregateConfiguration;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ReadProcessedDetails that = (ReadProcessedDetails) object;
    var eqb = new EqualsBuilder();
    eqb.append(getStartTime(), that.getStartTime());
    eqb.append(getEndTime(), that.getEndTime());
    eqb.append(getProcessingInterval(), that.getProcessingInterval());
    eqb.append(getAggregateType(), that.getAggregateType());
    eqb.append(getAggregateConfiguration(), that.getAggregateConfiguration());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getStartTime());
    hcb.append(getEndTime());
    hcb.append(getProcessingInterval());
    hcb.append(getAggregateType());
    hcb.append(getAggregateConfiguration());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ReadProcessedDetails.class.getSimpleName() + "[", "]");
    joiner.add("startTime=" + getStartTime());
    joiner.add("endTime=" + getEndTime());
    joiner.add("processingInterval=" + getProcessingInterval());
    joiner.add("aggregateType=" + java.util.Arrays.toString(getAggregateType()));
    joiner.add("aggregateConfiguration=" + getAggregateConfiguration());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 652),
        new NodeId(0, 641),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "StartTime",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 294),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "EndTime",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 294),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ProcessingInterval",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AggregateType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AggregateConfiguration",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 948),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ReadProcessedDetails> {
    @Override
    public Class<ReadProcessedDetails> getType() {
      return ReadProcessedDetails.class;
    }

    @Override
    public ReadProcessedDetails decodeType(EncodingContext context, UaDecoder decoder) {
      DateTime startTime = decoder.decodeDateTime("StartTime");
      DateTime endTime = decoder.decodeDateTime("EndTime");
      Double processingInterval = decoder.decodeDouble("ProcessingInterval");
      NodeId[] aggregateType = decoder.decodeNodeIdArray("AggregateType");
      AggregateConfiguration aggregateConfiguration =
          (AggregateConfiguration)
              decoder.decodeStruct("AggregateConfiguration", AggregateConfiguration.TYPE_ID);
      return new ReadProcessedDetails(
          startTime, endTime, processingInterval, aggregateType, aggregateConfiguration);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, ReadProcessedDetails value) {
      encoder.encodeDateTime("StartTime", value.getStartTime());
      encoder.encodeDateTime("EndTime", value.getEndTime());
      encoder.encodeDouble("ProcessingInterval", value.getProcessingInterval());
      encoder.encodeNodeIdArray("AggregateType", value.getAggregateType());
      encoder.encodeStruct(
          "AggregateConfiguration",
          value.getAggregateConfiguration(),
          AggregateConfiguration.TYPE_ID);
    }
  }
}
