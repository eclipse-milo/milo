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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.2/#6.3.2.4.3">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.3.2/#6.3.2.4.3</a>
 */
public class JsonDataSetReaderMessageDataType extends DataSetReaderMessageDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=15665");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15725");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=16019");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=16404");

  private final JsonNetworkMessageContentMask networkMessageContentMask;

  private final JsonDataSetMessageContentMask dataSetMessageContentMask;

  public JsonDataSetReaderMessageDataType(
      JsonNetworkMessageContentMask networkMessageContentMask,
      JsonDataSetMessageContentMask dataSetMessageContentMask) {
    this.networkMessageContentMask = networkMessageContentMask;
    this.dataSetMessageContentMask = dataSetMessageContentMask;
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

  public JsonNetworkMessageContentMask getNetworkMessageContentMask() {
    return networkMessageContentMask;
  }

  public JsonDataSetMessageContentMask getDataSetMessageContentMask() {
    return dataSetMessageContentMask;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    JsonDataSetReaderMessageDataType that = (JsonDataSetReaderMessageDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getNetworkMessageContentMask(), that.getNetworkMessageContentMask());
    eqb.append(getDataSetMessageContentMask(), that.getDataSetMessageContentMask());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getNetworkMessageContentMask());
    hcb.append(getDataSetMessageContentMask());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", JsonDataSetReaderMessageDataType.class.getSimpleName() + "[", "]");
    joiner.add("networkMessageContentMask=" + getNetworkMessageContentMask());
    joiner.add("dataSetMessageContentMask=" + getDataSetMessageContentMask());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 15725),
        new NodeId(0, 15629),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "NetworkMessageContentMask",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15654),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DataSetMessageContentMask",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15658),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<JsonDataSetReaderMessageDataType> {
    @Override
    public Class<JsonDataSetReaderMessageDataType> getType() {
      return JsonDataSetReaderMessageDataType.class;
    }

    @Override
    public JsonDataSetReaderMessageDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final JsonNetworkMessageContentMask networkMessageContentMask;
      final JsonDataSetMessageContentMask dataSetMessageContentMask;
      networkMessageContentMask =
          new JsonNetworkMessageContentMask(decoder.decodeUInt32("NetworkMessageContentMask"));
      dataSetMessageContentMask =
          new JsonDataSetMessageContentMask(decoder.decodeUInt32("DataSetMessageContentMask"));
      return new JsonDataSetReaderMessageDataType(
          networkMessageContentMask, dataSetMessageContentMask);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, JsonDataSetReaderMessageDataType value) {
      encoder.encodeUInt32(
          "NetworkMessageContentMask", value.getNetworkMessageContentMask().getValue());
      encoder.encodeUInt32(
          "DataSetMessageContentMask", value.getDataSetMessageContentMask().getValue());
    }
  }
}
