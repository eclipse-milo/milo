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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.22.4">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.22.4</a>
 */
public class AggregateFilterResult extends MonitoringFilterResult implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=737");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=739");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=738");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15315");

  private final DateTime revisedStartTime;

  private final Double revisedProcessingInterval;

  private final AggregateConfiguration revisedAggregateConfiguration;

  public AggregateFilterResult(
      DateTime revisedStartTime,
      Double revisedProcessingInterval,
      AggregateConfiguration revisedAggregateConfiguration) {
    this.revisedStartTime = revisedStartTime;
    this.revisedProcessingInterval = revisedProcessingInterval;
    this.revisedAggregateConfiguration = revisedAggregateConfiguration;
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

  public DateTime getRevisedStartTime() {
    return revisedStartTime;
  }

  public Double getRevisedProcessingInterval() {
    return revisedProcessingInterval;
  }

  public AggregateConfiguration getRevisedAggregateConfiguration() {
    return revisedAggregateConfiguration;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    AggregateFilterResult that = (AggregateFilterResult) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRevisedStartTime(), that.getRevisedStartTime());
    eqb.append(getRevisedProcessingInterval(), that.getRevisedProcessingInterval());
    eqb.append(getRevisedAggregateConfiguration(), that.getRevisedAggregateConfiguration());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRevisedStartTime());
    hcb.append(getRevisedProcessingInterval());
    hcb.append(getRevisedAggregateConfiguration());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", AggregateFilterResult.class.getSimpleName() + "[", "]");
    joiner.add("revisedStartTime=" + getRevisedStartTime());
    joiner.add("revisedProcessingInterval=" + getRevisedProcessingInterval());
    joiner.add("revisedAggregateConfiguration=" + getRevisedAggregateConfiguration());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 739),
        new NodeId(0, 731),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "RevisedStartTime",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 294),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RevisedProcessingInterval",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RevisedAggregateConfiguration",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 948),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<AggregateFilterResult> {
    @Override
    public Class<AggregateFilterResult> getType() {
      return AggregateFilterResult.class;
    }

    @Override
    public AggregateFilterResult decodeType(EncodingContext context, UaDecoder decoder) {
      DateTime revisedStartTime = decoder.decodeDateTime("RevisedStartTime");
      Double revisedProcessingInterval = decoder.decodeDouble("RevisedProcessingInterval");
      AggregateConfiguration revisedAggregateConfiguration =
          (AggregateConfiguration)
              decoder.decodeStruct("RevisedAggregateConfiguration", AggregateConfiguration.TYPE_ID);
      return new AggregateFilterResult(
          revisedStartTime, revisedProcessingInterval, revisedAggregateConfiguration);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, AggregateFilterResult value) {
      encoder.encodeDateTime("RevisedStartTime", value.getRevisedStartTime());
      encoder.encodeDouble("RevisedProcessingInterval", value.getRevisedProcessingInterval());
      encoder.encodeStruct(
          "RevisedAggregateConfiguration",
          value.getRevisedAggregateConfiguration(),
          AggregateConfiguration.TYPE_ID);
    }
  }
}
