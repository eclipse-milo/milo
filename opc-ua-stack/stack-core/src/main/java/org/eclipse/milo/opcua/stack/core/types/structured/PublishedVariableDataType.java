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
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.7.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.7.1</a>
 */
public class PublishedVariableDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=14273");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=14323");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=14319");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15060");

  private final NodeId publishedVariable;

  private final UInteger attributeId;

  private final Double samplingIntervalHint;

  private final UInteger deadbandType;

  private final Double deadbandValue;

  private final String indexRange;

  private final Variant substituteValue;

  private final QualifiedName @Nullable [] metaDataProperties;

  public PublishedVariableDataType(
      NodeId publishedVariable,
      UInteger attributeId,
      Double samplingIntervalHint,
      UInteger deadbandType,
      Double deadbandValue,
      String indexRange,
      Variant substituteValue,
      QualifiedName @Nullable [] metaDataProperties) {
    this.publishedVariable = publishedVariable;
    this.attributeId = attributeId;
    this.samplingIntervalHint = samplingIntervalHint;
    this.deadbandType = deadbandType;
    this.deadbandValue = deadbandValue;
    this.indexRange = indexRange;
    this.substituteValue = substituteValue;
    this.metaDataProperties = metaDataProperties;
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

  public NodeId getPublishedVariable() {
    return publishedVariable;
  }

  public UInteger getAttributeId() {
    return attributeId;
  }

  public Double getSamplingIntervalHint() {
    return samplingIntervalHint;
  }

  public UInteger getDeadbandType() {
    return deadbandType;
  }

  public Double getDeadbandValue() {
    return deadbandValue;
  }

  public String getIndexRange() {
    return indexRange;
  }

  public Variant getSubstituteValue() {
    return substituteValue;
  }

  public QualifiedName @Nullable [] getMetaDataProperties() {
    return metaDataProperties;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    PublishedVariableDataType that = (PublishedVariableDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getPublishedVariable(), that.getPublishedVariable());
    eqb.append(getAttributeId(), that.getAttributeId());
    eqb.append(getSamplingIntervalHint(), that.getSamplingIntervalHint());
    eqb.append(getDeadbandType(), that.getDeadbandType());
    eqb.append(getDeadbandValue(), that.getDeadbandValue());
    eqb.append(getIndexRange(), that.getIndexRange());
    eqb.append(getSubstituteValue(), that.getSubstituteValue());
    eqb.append(getMetaDataProperties(), that.getMetaDataProperties());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getPublishedVariable());
    hcb.append(getAttributeId());
    hcb.append(getSamplingIntervalHint());
    hcb.append(getDeadbandType());
    hcb.append(getDeadbandValue());
    hcb.append(getIndexRange());
    hcb.append(getSubstituteValue());
    hcb.append(getMetaDataProperties());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", PublishedVariableDataType.class.getSimpleName() + "[", "]");
    joiner.add("publishedVariable=" + getPublishedVariable());
    joiner.add("attributeId=" + getAttributeId());
    joiner.add("samplingIntervalHint=" + getSamplingIntervalHint());
    joiner.add("deadbandType=" + getDeadbandType());
    joiner.add("deadbandValue=" + getDeadbandValue());
    joiner.add("indexRange='" + getIndexRange() + "'");
    joiner.add("substituteValue=" + getSubstituteValue());
    joiner.add("metaDataProperties=" + java.util.Arrays.toString(getMetaDataProperties()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 14323),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "PublishedVariable",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AttributeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 288),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SamplingIntervalHint",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DeadbandType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DeadbandValue",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 11),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "IndexRange",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 291),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SubstituteValue",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 24),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MetaDataProperties",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 20),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<PublishedVariableDataType> {
    @Override
    public Class<PublishedVariableDataType> getType() {
      return PublishedVariableDataType.class;
    }

    @Override
    public PublishedVariableDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId publishedVariable;
      final UInteger attributeId;
      final Double samplingIntervalHint;
      final UInteger deadbandType;
      final Double deadbandValue;
      final String indexRange;
      final Variant substituteValue;
      final QualifiedName[] metaDataProperties;
      publishedVariable = decoder.decodeNodeId("PublishedVariable");
      attributeId = decoder.decodeUInt32("AttributeId");
      samplingIntervalHint = decoder.decodeDouble("SamplingIntervalHint");
      deadbandType = decoder.decodeUInt32("DeadbandType");
      deadbandValue = decoder.decodeDouble("DeadbandValue");
      indexRange = decoder.decodeString("IndexRange");
      substituteValue = decoder.decodeVariant("SubstituteValue");
      metaDataProperties = decoder.decodeQualifiedNameArray("MetaDataProperties");
      return new PublishedVariableDataType(
          publishedVariable,
          attributeId,
          samplingIntervalHint,
          deadbandType,
          deadbandValue,
          indexRange,
          substituteValue,
          metaDataProperties);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, PublishedVariableDataType value) {
      encoder.encodeNodeId("PublishedVariable", value.getPublishedVariable());
      encoder.encodeUInt32("AttributeId", value.getAttributeId());
      encoder.encodeDouble("SamplingIntervalHint", value.getSamplingIntervalHint());
      encoder.encodeUInt32("DeadbandType", value.getDeadbandType());
      encoder.encodeDouble("DeadbandValue", value.getDeadbandValue());
      encoder.encodeString("IndexRange", value.getIndexRange());
      encoder.encodeVariant("SubstituteValue", value.getSubstituteValue());
      encoder.encodeQualifiedNameArray("MetaDataProperties", value.getMetaDataProperties());
    }
  }
}
