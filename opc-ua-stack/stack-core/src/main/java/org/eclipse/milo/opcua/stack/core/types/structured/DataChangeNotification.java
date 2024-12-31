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
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.25.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.25.2</a>
 */
public class DataChangeNotification extends NotificationData implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=809");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=811");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=810");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15345");

  private final MonitoredItemNotification @Nullable [] monitoredItems;

  private final DiagnosticInfo @Nullable [] diagnosticInfos;

  public DataChangeNotification(
      MonitoredItemNotification @Nullable [] monitoredItems,
      DiagnosticInfo @Nullable [] diagnosticInfos) {
    this.monitoredItems = monitoredItems;
    this.diagnosticInfos = diagnosticInfos;
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

  public MonitoredItemNotification @Nullable [] getMonitoredItems() {
    return monitoredItems;
  }

  public DiagnosticInfo @Nullable [] getDiagnosticInfos() {
    return diagnosticInfos;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    DataChangeNotification that = (DataChangeNotification) object;
    var eqb = new EqualsBuilder();
    eqb.append(getMonitoredItems(), that.getMonitoredItems());
    eqb.append(getDiagnosticInfos(), that.getDiagnosticInfos());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getMonitoredItems());
    hcb.append(getDiagnosticInfos());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", DataChangeNotification.class.getSimpleName() + "[", "]");
    joiner.add("monitoredItems=" + java.util.Arrays.toString(getMonitoredItems()));
    joiner.add("diagnosticInfos=" + java.util.Arrays.toString(getDiagnosticInfos()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 811),
        new NodeId(0, 945),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "MonitoredItems",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 806),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DiagnosticInfos",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 25),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<DataChangeNotification> {
    @Override
    public Class<DataChangeNotification> getType() {
      return DataChangeNotification.class;
    }

    @Override
    public DataChangeNotification decodeType(EncodingContext context, UaDecoder decoder) {
      MonitoredItemNotification[] monitoredItems =
          (MonitoredItemNotification[])
              decoder.decodeStructArray("MonitoredItems", MonitoredItemNotification.TYPE_ID);
      DiagnosticInfo[] diagnosticInfos = decoder.decodeDiagnosticInfoArray("DiagnosticInfos");
      return new DataChangeNotification(monitoredItems, diagnosticInfos);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, DataChangeNotification value) {
      encoder.encodeStructArray(
          "MonitoredItems", value.getMonitoredItems(), MonitoredItemNotification.TYPE_ID);
      encoder.encodeDiagnosticInfoArray("DiagnosticInfos", value.getDiagnosticInfos());
    }
  }
}
