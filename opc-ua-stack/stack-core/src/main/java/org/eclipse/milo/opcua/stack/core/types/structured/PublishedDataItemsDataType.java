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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.7.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.7.2</a>
 */
public class PublishedDataItemsDataType extends PublishedDataSetSourceDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=15581");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15679");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15953");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=16154");

  private final PublishedVariableDataType @Nullable [] publishedData;

  public PublishedDataItemsDataType(PublishedVariableDataType @Nullable [] publishedData) {
    this.publishedData = publishedData;
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

  public PublishedVariableDataType @Nullable [] getPublishedData() {
    return publishedData;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    PublishedDataItemsDataType that = (PublishedDataItemsDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getPublishedData(), that.getPublishedData());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getPublishedData());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", PublishedDataItemsDataType.class.getSimpleName() + "[", "]");
    joiner.add("publishedData=" + java.util.Arrays.toString(getPublishedData()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 15679),
        new NodeId(0, 15580),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "PublishedData",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14273),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<PublishedDataItemsDataType> {
    @Override
    public Class<PublishedDataItemsDataType> getType() {
      return PublishedDataItemsDataType.class;
    }

    @Override
    public PublishedDataItemsDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final PublishedVariableDataType[] publishedData;
      publishedData =
          (PublishedVariableDataType[])
              decoder.decodeStructArray("PublishedData", PublishedVariableDataType.TYPE_ID);
      return new PublishedDataItemsDataType(publishedData);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, PublishedDataItemsDataType value) {
      encoder.encodeStructArray(
          "PublishedData", value.getPublishedData(), PublishedVariableDataType.TYPE_ID);
    }
  }
}
