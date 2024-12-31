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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.8">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.8</a>
 */
public class SamplingIntervalDiagnosticsDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=856");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=858");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=857");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15365");

  private final Double samplingInterval;

  private final UInteger monitoredItemCount;

  private final UInteger maxMonitoredItemCount;

  private final UInteger disabledMonitoredItemCount;

  public SamplingIntervalDiagnosticsDataType(
      Double samplingInterval,
      UInteger monitoredItemCount,
      UInteger maxMonitoredItemCount,
      UInteger disabledMonitoredItemCount) {
    this.samplingInterval = samplingInterval;
    this.monitoredItemCount = monitoredItemCount;
    this.maxMonitoredItemCount = maxMonitoredItemCount;
    this.disabledMonitoredItemCount = disabledMonitoredItemCount;
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

  public Double getSamplingInterval() {
    return samplingInterval;
  }

  public UInteger getMonitoredItemCount() {
    return monitoredItemCount;
  }

  public UInteger getMaxMonitoredItemCount() {
    return maxMonitoredItemCount;
  }

  public UInteger getDisabledMonitoredItemCount() {
    return disabledMonitoredItemCount;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    SamplingIntervalDiagnosticsDataType that = (SamplingIntervalDiagnosticsDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getSamplingInterval(), that.getSamplingInterval());
    eqb.append(getMonitoredItemCount(), that.getMonitoredItemCount());
    eqb.append(getMaxMonitoredItemCount(), that.getMaxMonitoredItemCount());
    eqb.append(getDisabledMonitoredItemCount(), that.getDisabledMonitoredItemCount());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getSamplingInterval());
    hcb.append(getMonitoredItemCount());
    hcb.append(getMaxMonitoredItemCount());
    hcb.append(getDisabledMonitoredItemCount());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(
            ", ", SamplingIntervalDiagnosticsDataType.class.getSimpleName() + "[", "]");
    joiner.add("samplingInterval=" + getSamplingInterval());
    joiner.add("monitoredItemCount=" + getMonitoredItemCount());
    joiner.add("maxMonitoredItemCount=" + getMaxMonitoredItemCount());
    joiner.add("disabledMonitoredItemCount=" + getDisabledMonitoredItemCount());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 858),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "SamplingInterval",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MonitoredItemCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MaxMonitoredItemCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DisabledMonitoredItemCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec
      extends GenericDataTypeCodec<SamplingIntervalDiagnosticsDataType> {
    @Override
    public Class<SamplingIntervalDiagnosticsDataType> getType() {
      return SamplingIntervalDiagnosticsDataType.class;
    }

    @Override
    public SamplingIntervalDiagnosticsDataType decodeType(
        EncodingContext context, UaDecoder decoder) {
      Double samplingInterval = decoder.decodeDouble("SamplingInterval");
      UInteger monitoredItemCount = decoder.decodeUInt32("MonitoredItemCount");
      UInteger maxMonitoredItemCount = decoder.decodeUInt32("MaxMonitoredItemCount");
      UInteger disabledMonitoredItemCount = decoder.decodeUInt32("DisabledMonitoredItemCount");
      return new SamplingIntervalDiagnosticsDataType(
          samplingInterval, monitoredItemCount, maxMonitoredItemCount, disabledMonitoredItemCount);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, SamplingIntervalDiagnosticsDataType value) {
      encoder.encodeDouble("SamplingInterval", value.getSamplingInterval());
      encoder.encodeUInt32("MonitoredItemCount", value.getMonitoredItemCount());
      encoder.encodeUInt32("MaxMonitoredItemCount", value.getMaxMonitoredItemCount());
      encoder.encodeUInt32("DisabledMonitoredItemCount", value.getDisabledMonitoredItemCount());
    }
  }
}
