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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.6/#6.5.6.1">https://reference.opcfoundation.org/v105/Core/docs/Part11/6.5.6/#6.5.6.1</a>
 */
public class ReadAnnotationDataDetails extends HistoryReadDetails implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=23497");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=23500");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=23506");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=23512");

  private final DateTime @Nullable [] reqTimes;

  public ReadAnnotationDataDetails(DateTime @Nullable [] reqTimes) {
    this.reqTimes = reqTimes;
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

  public DateTime @Nullable [] getReqTimes() {
    return reqTimes;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ReadAnnotationDataDetails that = (ReadAnnotationDataDetails) object;
    var eqb = new EqualsBuilder();
    eqb.append(getReqTimes(), that.getReqTimes());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getReqTimes());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ReadAnnotationDataDetails.class.getSimpleName() + "[", "]");
    joiner.add("reqTimes=" + java.util.Arrays.toString(getReqTimes()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 23500),
        new NodeId(0, 641),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ReqTimes",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 294),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ReadAnnotationDataDetails> {
    @Override
    public Class<ReadAnnotationDataDetails> getType() {
      return ReadAnnotationDataDetails.class;
    }

    @Override
    public ReadAnnotationDataDetails decodeType(EncodingContext context, UaDecoder decoder) {
      final DateTime[] reqTimes;
      reqTimes = decoder.decodeDateTimeArray("ReqTimes");
      return new ReadAnnotationDataDetails(reqTimes);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, ReadAnnotationDataDetails value) {
      encoder.encodeDateTimeArray("ReqTimes", value.getReqTimes());
    }
  }
}
