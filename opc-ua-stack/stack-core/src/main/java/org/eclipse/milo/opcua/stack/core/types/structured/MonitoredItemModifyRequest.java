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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.12.3/#5.12.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.12.3/#5.12.3.2</a>
 */
public class MonitoredItemModifyRequest extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=755");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=757");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=756");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15325");

  private final UInteger monitoredItemId;

  private final MonitoringParameters requestedParameters;

  public MonitoredItemModifyRequest(
      UInteger monitoredItemId, MonitoringParameters requestedParameters) {
    this.monitoredItemId = monitoredItemId;
    this.requestedParameters = requestedParameters;
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

  public UInteger getMonitoredItemId() {
    return monitoredItemId;
  }

  public MonitoringParameters getRequestedParameters() {
    return requestedParameters;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    MonitoredItemModifyRequest that = (MonitoredItemModifyRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getMonitoredItemId(), that.getMonitoredItemId());
    eqb.append(getRequestedParameters(), that.getRequestedParameters());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getMonitoredItemId());
    hcb.append(getRequestedParameters());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", MonitoredItemModifyRequest.class.getSimpleName() + "[", "]");
    joiner.add("monitoredItemId=" + getMonitoredItemId());
    joiner.add("requestedParameters=" + getRequestedParameters());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 757),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "MonitoredItemId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 288),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RequestedParameters",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 740),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<MonitoredItemModifyRequest> {
    @Override
    public Class<MonitoredItemModifyRequest> getType() {
      return MonitoredItemModifyRequest.class;
    }

    @Override
    public MonitoredItemModifyRequest decodeType(EncodingContext context, UaDecoder decoder) {
      UInteger monitoredItemId = decoder.decodeUInt32("MonitoredItemId");
      MonitoringParameters requestedParameters =
          (MonitoringParameters)
              decoder.decodeStruct("RequestedParameters", MonitoringParameters.TYPE_ID);
      return new MonitoredItemModifyRequest(monitoredItemId, requestedParameters);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, MonitoredItemModifyRequest value) {
      encoder.encodeUInt32("MonitoredItemId", value.getMonitoredItemId());
      encoder.encodeStruct(
          "RequestedParameters", value.getRequestedParameters(), MonitoringParameters.TYPE_ID);
    }
  }
}
