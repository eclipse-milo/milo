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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.6/#6.2.6.7.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.6/#6.2.6.7.1</a>
 */
public class WriterGroupDataType extends PubSubGroupDataType implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=15480");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=21150");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=21174");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=21198");

  private final UShort writerGroupId;

  private final Double publishingInterval;

  private final Double keepAliveTime;

  private final UByte priority;

  private final String @Nullable [] localeIds;

  private final @Nullable String headerLayoutUri;

  private final WriterGroupTransportDataType transportSettings;

  private final WriterGroupMessageDataType messageSettings;

  private final DataSetWriterDataType @Nullable [] dataSetWriters;

  public WriterGroupDataType(
      @Nullable String name,
      Boolean enabled,
      MessageSecurityMode securityMode,
      @Nullable String securityGroupId,
      EndpointDescription @Nullable [] securityKeyServices,
      UInteger maxNetworkMessageSize,
      KeyValuePair @Nullable [] groupProperties,
      UShort writerGroupId,
      Double publishingInterval,
      Double keepAliveTime,
      UByte priority,
      String @Nullable [] localeIds,
      @Nullable String headerLayoutUri,
      WriterGroupTransportDataType transportSettings,
      WriterGroupMessageDataType messageSettings,
      DataSetWriterDataType @Nullable [] dataSetWriters) {
    super(
        name,
        enabled,
        securityMode,
        securityGroupId,
        securityKeyServices,
        maxNetworkMessageSize,
        groupProperties);
    this.writerGroupId = writerGroupId;
    this.publishingInterval = publishingInterval;
    this.keepAliveTime = keepAliveTime;
    this.priority = priority;
    this.localeIds = localeIds;
    this.headerLayoutUri = headerLayoutUri;
    this.transportSettings = transportSettings;
    this.messageSettings = messageSettings;
    this.dataSetWriters = dataSetWriters;
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

  public UShort getWriterGroupId() {
    return writerGroupId;
  }

  public Double getPublishingInterval() {
    return publishingInterval;
  }

  public Double getKeepAliveTime() {
    return keepAliveTime;
  }

  public UByte getPriority() {
    return priority;
  }

  public String @Nullable [] getLocaleIds() {
    return localeIds;
  }

  public @Nullable String getHeaderLayoutUri() {
    return headerLayoutUri;
  }

  public WriterGroupTransportDataType getTransportSettings() {
    return transportSettings;
  }

  public WriterGroupMessageDataType getMessageSettings() {
    return messageSettings;
  }

  public DataSetWriterDataType @Nullable [] getDataSetWriters() {
    return dataSetWriters;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    WriterGroupDataType that = (WriterGroupDataType) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getWriterGroupId(), that.getWriterGroupId());
    eqb.append(getPublishingInterval(), that.getPublishingInterval());
    eqb.append(getKeepAliveTime(), that.getKeepAliveTime());
    eqb.append(getPriority(), that.getPriority());
    eqb.append(getLocaleIds(), that.getLocaleIds());
    eqb.append(getHeaderLayoutUri(), that.getHeaderLayoutUri());
    eqb.append(getTransportSettings(), that.getTransportSettings());
    eqb.append(getMessageSettings(), that.getMessageSettings());
    eqb.append(getDataSetWriters(), that.getDataSetWriters());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getWriterGroupId());
    hcb.append(getPublishingInterval());
    hcb.append(getKeepAliveTime());
    hcb.append(getPriority());
    hcb.append(getLocaleIds());
    hcb.append(getHeaderLayoutUri());
    hcb.append(getTransportSettings());
    hcb.append(getMessageSettings());
    hcb.append(getDataSetWriters());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", WriterGroupDataType.class.getSimpleName() + "[", "]");
    joiner.add("writerGroupId=" + getWriterGroupId());
    joiner.add("publishingInterval=" + getPublishingInterval());
    joiner.add("keepAliveTime=" + getKeepAliveTime());
    joiner.add("priority=" + getPriority());
    joiner.add("localeIds=" + java.util.Arrays.toString(getLocaleIds()));
    joiner.add("headerLayoutUri='" + getHeaderLayoutUri() + "'");
    joiner.add("transportSettings=" + getTransportSettings());
    joiner.add("messageSettings=" + getMessageSettings());
    joiner.add("dataSetWriters=" + java.util.Arrays.toString(getDataSetWriters()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 21150),
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
              "WriterGroupId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 5),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "PublishingInterval",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "KeepAliveTime",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Priority",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 3),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "LocaleIds",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 295),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "HeaderLayoutUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TransportSettings",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15611),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MessageSettings",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15616),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DataSetWriters",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15597),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<WriterGroupDataType> {
    @Override
    public Class<WriterGroupDataType> getType() {
      return WriterGroupDataType.class;
    }

    @Override
    public WriterGroupDataType decodeType(EncodingContext context, UaDecoder decoder) {
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
      UShort writerGroupId = decoder.decodeUInt16("WriterGroupId");
      Double publishingInterval = decoder.decodeDouble("PublishingInterval");
      Double keepAliveTime = decoder.decodeDouble("KeepAliveTime");
      UByte priority = decoder.decodeByte("Priority");
      String[] localeIds = decoder.decodeStringArray("LocaleIds");
      String headerLayoutUri = decoder.decodeString("HeaderLayoutUri");
      WriterGroupTransportDataType transportSettings =
          (WriterGroupTransportDataType)
              decoder.decodeStruct("TransportSettings", WriterGroupTransportDataType.TYPE_ID);
      WriterGroupMessageDataType messageSettings =
          (WriterGroupMessageDataType)
              decoder.decodeStruct("MessageSettings", WriterGroupMessageDataType.TYPE_ID);
      DataSetWriterDataType[] dataSetWriters =
          (DataSetWriterDataType[])
              decoder.decodeStructArray("DataSetWriters", DataSetWriterDataType.TYPE_ID);
      return new WriterGroupDataType(
          name,
          enabled,
          securityMode,
          securityGroupId,
          securityKeyServices,
          maxNetworkMessageSize,
          groupProperties,
          writerGroupId,
          publishingInterval,
          keepAliveTime,
          priority,
          localeIds,
          headerLayoutUri,
          transportSettings,
          messageSettings,
          dataSetWriters);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, WriterGroupDataType value) {
      encoder.encodeString("Name", value.getName());
      encoder.encodeBoolean("Enabled", value.getEnabled());
      encoder.encodeEnum("SecurityMode", value.getSecurityMode());
      encoder.encodeString("SecurityGroupId", value.getSecurityGroupId());
      encoder.encodeStructArray(
          "SecurityKeyServices", value.getSecurityKeyServices(), EndpointDescription.TYPE_ID);
      encoder.encodeUInt32("MaxNetworkMessageSize", value.getMaxNetworkMessageSize());
      encoder.encodeStructArray(
          "GroupProperties", value.getGroupProperties(), KeyValuePair.TYPE_ID);
      encoder.encodeUInt16("WriterGroupId", value.getWriterGroupId());
      encoder.encodeDouble("PublishingInterval", value.getPublishingInterval());
      encoder.encodeDouble("KeepAliveTime", value.getKeepAliveTime());
      encoder.encodeByte("Priority", value.getPriority());
      encoder.encodeStringArray("LocaleIds", value.getLocaleIds());
      encoder.encodeString("HeaderLayoutUri", value.getHeaderLayoutUri());
      encoder.encodeStruct(
          "TransportSettings", value.getTransportSettings(), WriterGroupTransportDataType.TYPE_ID);
      encoder.encodeStruct(
          "MessageSettings", value.getMessageSettings(), WriterGroupMessageDataType.TYPE_ID);
      encoder.encodeStructArray(
          "DataSetWriters", value.getDataSetWriters(), DataSetWriterDataType.TYPE_ID);
    }
  }
}
