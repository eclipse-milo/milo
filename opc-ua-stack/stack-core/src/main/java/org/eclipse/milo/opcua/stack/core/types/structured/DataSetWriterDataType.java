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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.4/#6.2.4.5.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.4/#6.2.4.5.1</a>
 */
public class DataSetWriterDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=15597");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15682");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15955");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=16156");

  private final @Nullable String name;

  private final Boolean enabled;

  private final UShort dataSetWriterId;

  private final DataSetFieldContentMask dataSetFieldContentMask;

  private final UInteger keyFrameCount;

  private final @Nullable String dataSetName;

  private final KeyValuePair @Nullable [] dataSetWriterProperties;

  private final DataSetWriterTransportDataType transportSettings;

  private final DataSetWriterMessageDataType messageSettings;

  public DataSetWriterDataType(
      @Nullable String name,
      Boolean enabled,
      UShort dataSetWriterId,
      DataSetFieldContentMask dataSetFieldContentMask,
      UInteger keyFrameCount,
      @Nullable String dataSetName,
      KeyValuePair @Nullable [] dataSetWriterProperties,
      DataSetWriterTransportDataType transportSettings,
      DataSetWriterMessageDataType messageSettings) {
    this.name = name;
    this.enabled = enabled;
    this.dataSetWriterId = dataSetWriterId;
    this.dataSetFieldContentMask = dataSetFieldContentMask;
    this.keyFrameCount = keyFrameCount;
    this.dataSetName = dataSetName;
    this.dataSetWriterProperties = dataSetWriterProperties;
    this.transportSettings = transportSettings;
    this.messageSettings = messageSettings;
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

  public @Nullable String getName() {
    return name;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public UShort getDataSetWriterId() {
    return dataSetWriterId;
  }

  public DataSetFieldContentMask getDataSetFieldContentMask() {
    return dataSetFieldContentMask;
  }

  public UInteger getKeyFrameCount() {
    return keyFrameCount;
  }

  public @Nullable String getDataSetName() {
    return dataSetName;
  }

  public KeyValuePair @Nullable [] getDataSetWriterProperties() {
    return dataSetWriterProperties;
  }

  public DataSetWriterTransportDataType getTransportSettings() {
    return transportSettings;
  }

  public DataSetWriterMessageDataType getMessageSettings() {
    return messageSettings;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    DataSetWriterDataType that = (DataSetWriterDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getName(), that.getName());
    eqb.append(getEnabled(), that.getEnabled());
    eqb.append(getDataSetWriterId(), that.getDataSetWriterId());
    eqb.append(getDataSetFieldContentMask(), that.getDataSetFieldContentMask());
    eqb.append(getKeyFrameCount(), that.getKeyFrameCount());
    eqb.append(getDataSetName(), that.getDataSetName());
    eqb.append(getDataSetWriterProperties(), that.getDataSetWriterProperties());
    eqb.append(getTransportSettings(), that.getTransportSettings());
    eqb.append(getMessageSettings(), that.getMessageSettings());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getName());
    hcb.append(getEnabled());
    hcb.append(getDataSetWriterId());
    hcb.append(getDataSetFieldContentMask());
    hcb.append(getKeyFrameCount());
    hcb.append(getDataSetName());
    hcb.append(getDataSetWriterProperties());
    hcb.append(getTransportSettings());
    hcb.append(getMessageSettings());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", DataSetWriterDataType.class.getSimpleName() + "[", "]");
    joiner.add("name='" + getName() + "'");
    joiner.add("enabled=" + getEnabled());
    joiner.add("dataSetWriterId=" + getDataSetWriterId());
    joiner.add("dataSetFieldContentMask=" + getDataSetFieldContentMask());
    joiner.add("keyFrameCount=" + getKeyFrameCount());
    joiner.add("dataSetName='" + getDataSetName() + "'");
    joiner.add(
        "dataSetWriterProperties=" + java.util.Arrays.toString(getDataSetWriterProperties()));
    joiner.add("transportSettings=" + getTransportSettings());
    joiner.add("messageSettings=" + getMessageSettings());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 15682),
        new NodeId(0, 22),
        StructureType.StructureWithSubtypedValues,
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
              "DataSetWriterId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 5),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DataSetFieldContentMask",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15583),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "KeyFrameCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DataSetName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DataSetWriterProperties",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14533),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TransportSettings",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15598),
              -1,
              null,
              UInteger.valueOf(0),
              true),
          new StructureField(
              "MessageSettings",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15605),
              -1,
              null,
              UInteger.valueOf(0),
              true)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<DataSetWriterDataType> {
    @Override
    public Class<DataSetWriterDataType> getType() {
      return DataSetWriterDataType.class;
    }

    @Override
    public DataSetWriterDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final String name;
      final Boolean enabled;
      final UShort dataSetWriterId;
      final DataSetFieldContentMask dataSetFieldContentMask;
      final UInteger keyFrameCount;
      final String dataSetName;
      final KeyValuePair[] dataSetWriterProperties;
      final DataSetWriterTransportDataType transportSettings;
      final DataSetWriterMessageDataType messageSettings;
      name = decoder.decodeString("Name");
      enabled = decoder.decodeBoolean("Enabled");
      dataSetWriterId = decoder.decodeUInt16("DataSetWriterId");
      dataSetFieldContentMask =
          new DataSetFieldContentMask(decoder.decodeUInt32("DataSetFieldContentMask"));
      keyFrameCount = decoder.decodeUInt32("KeyFrameCount");
      dataSetName = decoder.decodeString("DataSetName");
      dataSetWriterProperties =
          (KeyValuePair[])
              decoder.decodeStructArray("DataSetWriterProperties", KeyValuePair.TYPE_ID);
      {
        ExtensionObject xo = decoder.decodeExtensionObject("TransportSettings");
        transportSettings = (DataSetWriterTransportDataType) xo.decode(context);
      }
      {
        ExtensionObject xo = decoder.decodeExtensionObject("MessageSettings");
        messageSettings = (DataSetWriterMessageDataType) xo.decode(context);
      }
      return new DataSetWriterDataType(
          name,
          enabled,
          dataSetWriterId,
          dataSetFieldContentMask,
          keyFrameCount,
          dataSetName,
          dataSetWriterProperties,
          transportSettings,
          messageSettings);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, DataSetWriterDataType value) {
      encoder.encodeString("Name", value.getName());
      encoder.encodeBoolean("Enabled", value.getEnabled());
      encoder.encodeUInt16("DataSetWriterId", value.getDataSetWriterId());
      encoder.encodeUInt32(
          "DataSetFieldContentMask", value.getDataSetFieldContentMask().getValue());
      encoder.encodeUInt32("KeyFrameCount", value.getKeyFrameCount());
      encoder.encodeString("DataSetName", value.getDataSetName());
      encoder.encodeStructArray(
          "DataSetWriterProperties", value.getDataSetWriterProperties(), KeyValuePair.TYPE_ID);
      {
        ExtensionObject xo = ExtensionObject.encode(context, value.getTransportSettings());
        encoder.encodeExtensionObject("TransportSettings", xo);
      }
      {
        ExtensionObject xo = ExtensionObject.encode(context, value.getMessageSettings());
        encoder.encodeExtensionObject("MessageSettings", xo);
      }
    }
  }
}
