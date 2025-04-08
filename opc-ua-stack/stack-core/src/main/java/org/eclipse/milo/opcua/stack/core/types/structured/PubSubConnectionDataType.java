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
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.7/#6.2.7.5.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.7/#6.2.7.5.1</a>
 */
public class PubSubConnectionDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=15617");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15694");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15992");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=16281");

  private final @Nullable String name;

  private final Boolean enabled;

  private final Variant publisherId;

  private final @Nullable String transportProfileUri;

  private final NetworkAddressDataType address;

  private final KeyValuePair @Nullable [] connectionProperties;

  private final ConnectionTransportDataType transportSettings;

  private final WriterGroupDataType @Nullable [] writerGroups;

  private final ReaderGroupDataType @Nullable [] readerGroups;

  public PubSubConnectionDataType(
      @Nullable String name,
      Boolean enabled,
      Variant publisherId,
      @Nullable String transportProfileUri,
      NetworkAddressDataType address,
      KeyValuePair @Nullable [] connectionProperties,
      ConnectionTransportDataType transportSettings,
      WriterGroupDataType @Nullable [] writerGroups,
      ReaderGroupDataType @Nullable [] readerGroups) {
    this.name = name;
    this.enabled = enabled;
    this.publisherId = publisherId;
    this.transportProfileUri = transportProfileUri;
    this.address = address;
    this.connectionProperties = connectionProperties;
    this.transportSettings = transportSettings;
    this.writerGroups = writerGroups;
    this.readerGroups = readerGroups;
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

  public Variant getPublisherId() {
    return publisherId;
  }

  public @Nullable String getTransportProfileUri() {
    return transportProfileUri;
  }

  public NetworkAddressDataType getAddress() {
    return address;
  }

  public KeyValuePair @Nullable [] getConnectionProperties() {
    return connectionProperties;
  }

  public ConnectionTransportDataType getTransportSettings() {
    return transportSettings;
  }

  public WriterGroupDataType @Nullable [] getWriterGroups() {
    return writerGroups;
  }

  public ReaderGroupDataType @Nullable [] getReaderGroups() {
    return readerGroups;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    PubSubConnectionDataType that = (PubSubConnectionDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getName(), that.getName());
    eqb.append(getEnabled(), that.getEnabled());
    eqb.append(getPublisherId(), that.getPublisherId());
    eqb.append(getTransportProfileUri(), that.getTransportProfileUri());
    eqb.append(getAddress(), that.getAddress());
    eqb.append(getConnectionProperties(), that.getConnectionProperties());
    eqb.append(getTransportSettings(), that.getTransportSettings());
    eqb.append(getWriterGroups(), that.getWriterGroups());
    eqb.append(getReaderGroups(), that.getReaderGroups());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getName());
    hcb.append(getEnabled());
    hcb.append(getPublisherId());
    hcb.append(getTransportProfileUri());
    hcb.append(getAddress());
    hcb.append(getConnectionProperties());
    hcb.append(getTransportSettings());
    hcb.append(getWriterGroups());
    hcb.append(getReaderGroups());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", PubSubConnectionDataType.class.getSimpleName() + "[", "]");
    joiner.add("name='" + getName() + "'");
    joiner.add("enabled=" + getEnabled());
    joiner.add("publisherId=" + getPublisherId());
    joiner.add("transportProfileUri='" + getTransportProfileUri() + "'");
    joiner.add("address=" + getAddress());
    joiner.add("connectionProperties=" + java.util.Arrays.toString(getConnectionProperties()));
    joiner.add("transportSettings=" + getTransportSettings());
    joiner.add("writerGroups=" + java.util.Arrays.toString(getWriterGroups()));
    joiner.add("readerGroups=" + java.util.Arrays.toString(getReaderGroups()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 15694),
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
              "PublisherId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 24),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TransportProfileUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Address",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15502),
              -1,
              null,
              UInteger.valueOf(0),
              true),
          new StructureField(
              "ConnectionProperties",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14533),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TransportSettings",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15618),
              -1,
              null,
              UInteger.valueOf(0),
              true),
          new StructureField(
              "WriterGroups",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15480),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ReaderGroups",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15520),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<PubSubConnectionDataType> {
    @Override
    public Class<PubSubConnectionDataType> getType() {
      return PubSubConnectionDataType.class;
    }

    @Override
    public PubSubConnectionDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final String name;
      final Boolean enabled;
      final Variant publisherId;
      final String transportProfileUri;
      final NetworkAddressDataType address;
      final KeyValuePair[] connectionProperties;
      final ConnectionTransportDataType transportSettings;
      final WriterGroupDataType[] writerGroups;
      final ReaderGroupDataType[] readerGroups;
      name = decoder.decodeString("Name");
      enabled = decoder.decodeBoolean("Enabled");
      publisherId = decoder.decodeVariant("PublisherId");
      transportProfileUri = decoder.decodeString("TransportProfileUri");
      {
        ExtensionObject xo = decoder.decodeExtensionObject("Address");
        address = (NetworkAddressDataType) xo.decode(context);
      }
      connectionProperties =
          (KeyValuePair[]) decoder.decodeStructArray("ConnectionProperties", KeyValuePair.TYPE_ID);
      {
        ExtensionObject xo = decoder.decodeExtensionObject("TransportSettings");
        transportSettings = (ConnectionTransportDataType) xo.decode(context);
      }
      writerGroups =
          (WriterGroupDataType[])
              decoder.decodeStructArray("WriterGroups", WriterGroupDataType.TYPE_ID);
      readerGroups =
          (ReaderGroupDataType[])
              decoder.decodeStructArray("ReaderGroups", ReaderGroupDataType.TYPE_ID);
      return new PubSubConnectionDataType(
          name,
          enabled,
          publisherId,
          transportProfileUri,
          address,
          connectionProperties,
          transportSettings,
          writerGroups,
          readerGroups);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, PubSubConnectionDataType value) {
      encoder.encodeString("Name", value.getName());
      encoder.encodeBoolean("Enabled", value.getEnabled());
      encoder.encodeVariant("PublisherId", value.getPublisherId());
      encoder.encodeString("TransportProfileUri", value.getTransportProfileUri());
      {
        ExtensionObject xo = ExtensionObject.encode(context, value.getAddress());
        encoder.encodeExtensionObject("Address", xo);
      }
      encoder.encodeStructArray(
          "ConnectionProperties", value.getConnectionProperties(), KeyValuePair.TYPE_ID);
      {
        ExtensionObject xo = ExtensionObject.encode(context, value.getTransportSettings());
        encoder.encodeExtensionObject("TransportSettings", xo);
      }
      encoder.encodeStructArray(
          "WriterGroups", value.getWriterGroups(), WriterGroupDataType.TYPE_ID);
      encoder.encodeStructArray(
          "ReaderGroups", value.getReaderGroups(), ReaderGroupDataType.TYPE_ID);
    }
  }
}
