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
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.8/#6.2.8.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.8/#6.2.8.2.1</a>
 */
public class ReaderGroupDataType extends PubSubGroupDataType implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=15520");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=21153");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=21177");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=21201");

  private final ReaderGroupTransportDataType transportSettings;

  private final ReaderGroupMessageDataType messageSettings;

  private final DataSetReaderDataType @Nullable [] dataSetReaders;

  public ReaderGroupDataType(
      @Nullable String name,
      Boolean enabled,
      MessageSecurityMode securityMode,
      @Nullable String securityGroupId,
      EndpointDescription @Nullable [] securityKeyServices,
      UInteger maxNetworkMessageSize,
      KeyValuePair @Nullable [] groupProperties,
      ReaderGroupTransportDataType transportSettings,
      ReaderGroupMessageDataType messageSettings,
      DataSetReaderDataType @Nullable [] dataSetReaders) {
    super(
        name,
        enabled,
        securityMode,
        securityGroupId,
        securityKeyServices,
        maxNetworkMessageSize,
        groupProperties);
    this.transportSettings = transportSettings;
    this.messageSettings = messageSettings;
    this.dataSetReaders = dataSetReaders;
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

  public ReaderGroupTransportDataType getTransportSettings() {
    return transportSettings;
  }

  public ReaderGroupMessageDataType getMessageSettings() {
    return messageSettings;
  }

  public DataSetReaderDataType @Nullable [] getDataSetReaders() {
    return dataSetReaders;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ReaderGroupDataType that = (ReaderGroupDataType) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getTransportSettings(), that.getTransportSettings());
    eqb.append(getMessageSettings(), that.getMessageSettings());
    eqb.append(getDataSetReaders(), that.getDataSetReaders());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getTransportSettings());
    hcb.append(getMessageSettings());
    hcb.append(getDataSetReaders());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ReaderGroupDataType.class.getSimpleName() + "[", "]");
    joiner.add("transportSettings=" + getTransportSettings());
    joiner.add("messageSettings=" + getMessageSettings());
    joiner.add("dataSetReaders=" + java.util.Arrays.toString(getDataSetReaders()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 21153),
        new NodeId(0, 15609),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Name",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Enabled",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SecurityMode",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 302),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SecurityGroupId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SecurityKeyServices",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 312),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MaxNetworkMessageSize",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "GroupProperties",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14533),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TransportSettings",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15621),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MessageSettings",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15622),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DataSetReaders",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15623),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ReaderGroupDataType> {
    @Override
    public Class<ReaderGroupDataType> getType() {
      return ReaderGroupDataType.class;
    }

    @Override
    public ReaderGroupDataType decodeType(EncodingContext context, UaDecoder decoder) {
      String name = decoder.decodeString("Name");
      Boolean enabled = decoder.decodeBoolean("Enabled");
      MessageSecurityMode securityMode =
          MessageSecurityMode.from(decoder.decodeEnum("SecurityMode"));
      String securityGroupId = decoder.decodeString("SecurityGroupId");
      EndpointDescription[] securityKeyServices =
          (EndpointDescription[])
              decoder.decodeStructArray("SecurityKeyServices", EndpointDescription.TYPE_ID);
      UInteger maxNetworkMessageSize = decoder.decodeUInt32("MaxNetworkMessageSize");
      KeyValuePair[] groupProperties =
          (KeyValuePair[]) decoder.decodeStructArray("GroupProperties", KeyValuePair.TYPE_ID);
      ReaderGroupTransportDataType transportSettings =
          (ReaderGroupTransportDataType)
              decoder.decodeStruct("TransportSettings", ReaderGroupTransportDataType.TYPE_ID);
      ReaderGroupMessageDataType messageSettings =
          (ReaderGroupMessageDataType)
              decoder.decodeStruct("MessageSettings", ReaderGroupMessageDataType.TYPE_ID);
      DataSetReaderDataType[] dataSetReaders =
          (DataSetReaderDataType[])
              decoder.decodeStructArray("DataSetReaders", DataSetReaderDataType.TYPE_ID);
      return new ReaderGroupDataType(
          name,
          enabled,
          securityMode,
          securityGroupId,
          securityKeyServices,
          maxNetworkMessageSize,
          groupProperties,
          transportSettings,
          messageSettings,
          dataSetReaders);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, ReaderGroupDataType value) {
      encoder.encodeString("Name", value.getName());
      encoder.encodeBoolean("Enabled", value.getEnabled());
      encoder.encodeEnum("SecurityMode", value.getSecurityMode());
      encoder.encodeString("SecurityGroupId", value.getSecurityGroupId());
      encoder.encodeStructArray(
          "SecurityKeyServices", value.getSecurityKeyServices(), EndpointDescription.TYPE_ID);
      encoder.encodeUInt32("MaxNetworkMessageSize", value.getMaxNetworkMessageSize());
      encoder.encodeStructArray(
          "GroupProperties", value.getGroupProperties(), KeyValuePair.TYPE_ID);
      encoder.encodeStruct(
          "TransportSettings", value.getTransportSettings(), ReaderGroupTransportDataType.TYPE_ID);
      encoder.encodeStruct(
          "MessageSettings", value.getMessageSettings(), ReaderGroupMessageDataType.TYPE_ID);
      encoder.encodeStructArray(
          "DataSetReaders", value.getDataSetReaders(), DataSetReaderDataType.TYPE_ID);
    }
  }
}
