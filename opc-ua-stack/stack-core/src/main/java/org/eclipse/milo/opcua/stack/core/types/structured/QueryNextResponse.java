package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.10.4/#5.10.4.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.10.4/#5.10.4.2</a>
 */
public class QueryNextResponse extends Structure implements UaResponseMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=622");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=624");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=623");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15255");

  private final ResponseHeader responseHeader;

  private final QueryDataSet @Nullable [] queryDataSets;

  private final ByteString revisedContinuationPoint;

  public QueryNextResponse(
      ResponseHeader responseHeader,
      QueryDataSet @Nullable [] queryDataSets,
      ByteString revisedContinuationPoint) {
    this.responseHeader = responseHeader;
    this.queryDataSets = queryDataSets;
    this.revisedContinuationPoint = revisedContinuationPoint;
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

  public ResponseHeader getResponseHeader() {
    return responseHeader;
  }

  public QueryDataSet @Nullable [] getQueryDataSets() {
    return queryDataSets;
  }

  public ByteString getRevisedContinuationPoint() {
    return revisedContinuationPoint;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    QueryNextResponse that = (QueryNextResponse) object;
    var eqb = new EqualsBuilder();
    eqb.append(getResponseHeader(), that.getResponseHeader());
    eqb.append(getQueryDataSets(), that.getQueryDataSets());
    eqb.append(getRevisedContinuationPoint(), that.getRevisedContinuationPoint());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getResponseHeader());
    hcb.append(getQueryDataSets());
    hcb.append(getRevisedContinuationPoint());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", QueryNextResponse.class.getSimpleName() + "[", "]");
    joiner.add("responseHeader=" + getResponseHeader());
    joiner.add("queryDataSets=" + java.util.Arrays.toString(getQueryDataSets()));
    joiner.add("revisedContinuationPoint=" + getRevisedContinuationPoint());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 624),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ResponseHeader",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 392),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "QueryDataSets",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 577),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RevisedContinuationPoint",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 521),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<QueryNextResponse> {
    @Override
    public Class<QueryNextResponse> getType() {
      return QueryNextResponse.class;
    }

    @Override
    public QueryNextResponse decodeType(EncodingContext context, UaDecoder decoder) {
      final ResponseHeader responseHeader;
      final QueryDataSet[] queryDataSets;
      final ByteString revisedContinuationPoint;
      responseHeader =
          (ResponseHeader) decoder.decodeStruct("ResponseHeader", ResponseHeader.TYPE_ID);
      queryDataSets =
          (QueryDataSet[]) decoder.decodeStructArray("QueryDataSets", QueryDataSet.TYPE_ID);
      revisedContinuationPoint = decoder.decodeByteString("RevisedContinuationPoint");
      return new QueryNextResponse(responseHeader, queryDataSets, revisedContinuationPoint);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, QueryNextResponse value) {
      encoder.encodeStruct("ResponseHeader", value.getResponseHeader(), ResponseHeader.TYPE_ID);
      encoder.encodeStructArray("QueryDataSets", value.getQueryDataSets(), QueryDataSet.TYPE_ID);
      encoder.encodeByteString("RevisedContinuationPoint", value.getRevisedContinuationPoint());
    }
  }
}
