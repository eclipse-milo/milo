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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.9.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.9.2</a>
 */
public class PublishedDataSetCustomSourceDataType extends PublishedDataSetSourceDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=25269");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=25529");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=25545");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=25561");

  private final Boolean cyclicDataSet;

  public PublishedDataSetCustomSourceDataType(Boolean cyclicDataSet) {
    this.cyclicDataSet = cyclicDataSet;
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

  public Boolean getCyclicDataSet() {
    return cyclicDataSet;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    PublishedDataSetCustomSourceDataType that = (PublishedDataSetCustomSourceDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getCyclicDataSet(), that.getCyclicDataSet());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getCyclicDataSet());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(
            ", ", PublishedDataSetCustomSourceDataType.class.getSimpleName() + "[", "]");
    joiner.add("cyclicDataSet=" + getCyclicDataSet());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 25529),
        new NodeId(0, 15580),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "CyclicDataSet",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec
      extends GenericDataTypeCodec<PublishedDataSetCustomSourceDataType> {
    @Override
    public Class<PublishedDataSetCustomSourceDataType> getType() {
      return PublishedDataSetCustomSourceDataType.class;
    }

    @Override
    public PublishedDataSetCustomSourceDataType decodeType(
        EncodingContext context, UaDecoder decoder) {
      final Boolean cyclicDataSet;
      cyclicDataSet = decoder.decodeBoolean("CyclicDataSet");
      return new PublishedDataSetCustomSourceDataType(cyclicDataSet);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, PublishedDataSetCustomSourceDataType value) {
      encoder.encodeBoolean("CyclicDataSet", value.getCyclicDataSet());
    }
  }
}
