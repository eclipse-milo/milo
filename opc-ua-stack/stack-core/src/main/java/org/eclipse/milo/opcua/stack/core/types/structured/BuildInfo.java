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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.4">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.4</a>
 */
public class BuildInfo extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=338");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=340");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=339");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15361");

  private final @Nullable String productUri;

  private final @Nullable String manufacturerName;

  private final @Nullable String productName;

  private final @Nullable String softwareVersion;

  private final @Nullable String buildNumber;

  private final DateTime buildDate;

  public BuildInfo(
      @Nullable String productUri,
      @Nullable String manufacturerName,
      @Nullable String productName,
      @Nullable String softwareVersion,
      @Nullable String buildNumber,
      DateTime buildDate) {
    this.productUri = productUri;
    this.manufacturerName = manufacturerName;
    this.productName = productName;
    this.softwareVersion = softwareVersion;
    this.buildNumber = buildNumber;
    this.buildDate = buildDate;
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

  public @Nullable String getProductUri() {
    return productUri;
  }

  public @Nullable String getManufacturerName() {
    return manufacturerName;
  }

  public @Nullable String getProductName() {
    return productName;
  }

  public @Nullable String getSoftwareVersion() {
    return softwareVersion;
  }

  public @Nullable String getBuildNumber() {
    return buildNumber;
  }

  public DateTime getBuildDate() {
    return buildDate;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    BuildInfo that = (BuildInfo) object;
    var eqb = new EqualsBuilder();
    eqb.append(getProductUri(), that.getProductUri());
    eqb.append(getManufacturerName(), that.getManufacturerName());
    eqb.append(getProductName(), that.getProductName());
    eqb.append(getSoftwareVersion(), that.getSoftwareVersion());
    eqb.append(getBuildNumber(), that.getBuildNumber());
    eqb.append(getBuildDate(), that.getBuildDate());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getProductUri());
    hcb.append(getManufacturerName());
    hcb.append(getProductName());
    hcb.append(getSoftwareVersion());
    hcb.append(getBuildNumber());
    hcb.append(getBuildDate());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", BuildInfo.class.getSimpleName() + "[", "]");
    joiner.add("productUri='" + getProductUri() + "'");
    joiner.add("manufacturerName='" + getManufacturerName() + "'");
    joiner.add("productName='" + getProductName() + "'");
    joiner.add("softwareVersion='" + getSoftwareVersion() + "'");
    joiner.add("buildNumber='" + getBuildNumber() + "'");
    joiner.add("buildDate=" + getBuildDate());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 340),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ProductUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ManufacturerName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ProductName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SoftwareVersion",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "BuildNumber",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "BuildDate",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 294),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<BuildInfo> {
    @Override
    public Class<BuildInfo> getType() {
      return BuildInfo.class;
    }

    @Override
    public BuildInfo decodeType(EncodingContext context, UaDecoder decoder) {
      final String productUri;
      final String manufacturerName;
      final String productName;
      final String softwareVersion;
      final String buildNumber;
      final DateTime buildDate;
      productUri = decoder.decodeString("ProductUri");
      manufacturerName = decoder.decodeString("ManufacturerName");
      productName = decoder.decodeString("ProductName");
      softwareVersion = decoder.decodeString("SoftwareVersion");
      buildNumber = decoder.decodeString("BuildNumber");
      buildDate = decoder.decodeDateTime("BuildDate");
      return new BuildInfo(
          productUri, manufacturerName, productName, softwareVersion, buildNumber, buildDate);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, BuildInfo value) {
      encoder.encodeString("ProductUri", value.getProductUri());
      encoder.encodeString("ManufacturerName", value.getManufacturerName());
      encoder.encodeString("ProductName", value.getProductName());
      encoder.encodeString("SoftwareVersion", value.getSoftwareVersion());
      encoder.encodeString("BuildNumber", value.getBuildNumber());
      encoder.encodeDateTime("BuildDate", value.getBuildDate());
    }
  }
}
