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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.12.2/#5.12.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.12.2/#5.12.2.2</a>
 */
public class MonitoredItemCreateResult extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=746");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=748");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=747");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15322");

  private final StatusCode statusCode;

  private final UInteger monitoredItemId;

  private final Double revisedSamplingInterval;

  private final UInteger revisedQueueSize;

  private final ExtensionObject filterResult;

  public MonitoredItemCreateResult(
      StatusCode statusCode,
      UInteger monitoredItemId,
      Double revisedSamplingInterval,
      UInteger revisedQueueSize,
      ExtensionObject filterResult) {
    this.statusCode = statusCode;
    this.monitoredItemId = monitoredItemId;
    this.revisedSamplingInterval = revisedSamplingInterval;
    this.revisedQueueSize = revisedQueueSize;
    this.filterResult = filterResult;
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

  public StatusCode getStatusCode() {
    return statusCode;
  }

  public UInteger getMonitoredItemId() {
    return monitoredItemId;
  }

  public Double getRevisedSamplingInterval() {
    return revisedSamplingInterval;
  }

  public UInteger getRevisedQueueSize() {
    return revisedQueueSize;
  }

  public ExtensionObject getFilterResult() {
    return filterResult;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    MonitoredItemCreateResult that = (MonitoredItemCreateResult) object;
    var eqb = new EqualsBuilder();
    eqb.append(getStatusCode(), that.getStatusCode());
    eqb.append(getMonitoredItemId(), that.getMonitoredItemId());
    eqb.append(getRevisedSamplingInterval(), that.getRevisedSamplingInterval());
    eqb.append(getRevisedQueueSize(), that.getRevisedQueueSize());
    eqb.append(getFilterResult(), that.getFilterResult());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getStatusCode());
    hcb.append(getMonitoredItemId());
    hcb.append(getRevisedSamplingInterval());
    hcb.append(getRevisedQueueSize());
    hcb.append(getFilterResult());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", MonitoredItemCreateResult.class.getSimpleName() + "[", "]");
    joiner.add("statusCode=" + getStatusCode());
    joiner.add("monitoredItemId=" + getMonitoredItemId());
    joiner.add("revisedSamplingInterval=" + getRevisedSamplingInterval());
    joiner.add("revisedQueueSize=" + getRevisedQueueSize());
    joiner.add("filterResult=" + getFilterResult());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 748),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "StatusCode",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 19),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MonitoredItemId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 288),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RevisedSamplingInterval",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RevisedQueueSize",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 289),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "FilterResult",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 22),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<MonitoredItemCreateResult> {
    @Override
    public Class<MonitoredItemCreateResult> getType() {
      return MonitoredItemCreateResult.class;
    }

    @Override
    public MonitoredItemCreateResult decodeType(EncodingContext context, UaDecoder decoder) {
      StatusCode statusCode = decoder.decodeStatusCode("StatusCode");
      UInteger monitoredItemId = decoder.decodeUInt32("MonitoredItemId");
      Double revisedSamplingInterval = decoder.decodeDouble("RevisedSamplingInterval");
      UInteger revisedQueueSize = decoder.decodeUInt32("RevisedQueueSize");
      ExtensionObject filterResult = decoder.decodeExtensionObject("FilterResult");
      return new MonitoredItemCreateResult(
          statusCode, monitoredItemId, revisedSamplingInterval, revisedQueueSize, filterResult);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, MonitoredItemCreateResult value) {
      encoder.encodeStatusCode("StatusCode", value.getStatusCode());
      encoder.encodeUInt32("MonitoredItemId", value.getMonitoredItemId());
      encoder.encodeDouble("RevisedSamplingInterval", value.getRevisedSamplingInterval());
      encoder.encodeUInt32("RevisedQueueSize", value.getRevisedQueueSize());
      encoder.encodeExtensionObject("FilterResult", value.getFilterResult());
    }
  }
}
