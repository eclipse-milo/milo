package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.10.3/#5.10.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.10.3/#5.10.3.1</a>
 */
public class QueryFirstResponse extends Structure implements UaResponseMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=616");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=618");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=617");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15252");

  private final ResponseHeader responseHeader;

  private final QueryDataSet @Nullable [] queryDataSets;

  private final ByteString continuationPoint;

  private final ParsingResult @Nullable [] parsingResults;

  private final DiagnosticInfo @Nullable [] diagnosticInfos;

  private final ContentFilterResult filterResult;

  public QueryFirstResponse(
      ResponseHeader responseHeader,
      QueryDataSet @Nullable [] queryDataSets,
      ByteString continuationPoint,
      ParsingResult @Nullable [] parsingResults,
      DiagnosticInfo @Nullable [] diagnosticInfos,
      ContentFilterResult filterResult) {
    this.responseHeader = responseHeader;
    this.queryDataSets = queryDataSets;
    this.continuationPoint = continuationPoint;
    this.parsingResults = parsingResults;
    this.diagnosticInfos = diagnosticInfos;
    this.filterResult = filterResult;
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

  public ByteString getContinuationPoint() {
    return continuationPoint;
  }

  public ParsingResult @Nullable [] getParsingResults() {
    return parsingResults;
  }

  public DiagnosticInfo @Nullable [] getDiagnosticInfos() {
    return diagnosticInfos;
  }

  public ContentFilterResult getFilterResult() {
    return filterResult;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    QueryFirstResponse that = (QueryFirstResponse) object;
    var eqb = new EqualsBuilder();
    eqb.append(getResponseHeader(), that.getResponseHeader());
    eqb.append(getQueryDataSets(), that.getQueryDataSets());
    eqb.append(getContinuationPoint(), that.getContinuationPoint());
    eqb.append(getParsingResults(), that.getParsingResults());
    eqb.append(getDiagnosticInfos(), that.getDiagnosticInfos());
    eqb.append(getFilterResult(), that.getFilterResult());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getResponseHeader());
    hcb.append(getQueryDataSets());
    hcb.append(getContinuationPoint());
    hcb.append(getParsingResults());
    hcb.append(getDiagnosticInfos());
    hcb.append(getFilterResult());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", QueryFirstResponse.class.getSimpleName() + "[", "]");
    joiner.add("responseHeader=" + getResponseHeader());
    joiner.add("queryDataSets=" + java.util.Arrays.toString(getQueryDataSets()));
    joiner.add("continuationPoint=" + getContinuationPoint());
    joiner.add("parsingResults=" + java.util.Arrays.toString(getParsingResults()));
    joiner.add("diagnosticInfos=" + java.util.Arrays.toString(getDiagnosticInfos()));
    joiner.add("filterResult=" + getFilterResult());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 618),
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
              "ContinuationPoint",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 521),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ParsingResults",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 610),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DiagnosticInfos",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 25),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "FilterResult",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 607),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<QueryFirstResponse> {
    @Override
    public Class<QueryFirstResponse> getType() {
      return QueryFirstResponse.class;
    }

    @Override
    public QueryFirstResponse decodeType(EncodingContext context, UaDecoder decoder) {
      final ResponseHeader responseHeader;
      final QueryDataSet[] queryDataSets;
      final ByteString continuationPoint;
      final ParsingResult[] parsingResults;
      final DiagnosticInfo[] diagnosticInfos;
      final ContentFilterResult filterResult;
      responseHeader =
          (ResponseHeader) decoder.decodeStruct("ResponseHeader", ResponseHeader.TYPE_ID);
      queryDataSets =
          (QueryDataSet[]) decoder.decodeStructArray("QueryDataSets", QueryDataSet.TYPE_ID);
      continuationPoint = decoder.decodeByteString("ContinuationPoint");
      parsingResults =
          (ParsingResult[]) decoder.decodeStructArray("ParsingResults", ParsingResult.TYPE_ID);
      diagnosticInfos = decoder.decodeDiagnosticInfoArray("DiagnosticInfos");
      filterResult =
          (ContentFilterResult) decoder.decodeStruct("FilterResult", ContentFilterResult.TYPE_ID);
      return new QueryFirstResponse(
          responseHeader,
          queryDataSets,
          continuationPoint,
          parsingResults,
          diagnosticInfos,
          filterResult);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, QueryFirstResponse value) {
      encoder.encodeStruct("ResponseHeader", value.getResponseHeader(), ResponseHeader.TYPE_ID);
      encoder.encodeStructArray("QueryDataSets", value.getQueryDataSets(), QueryDataSet.TYPE_ID);
      encoder.encodeByteString("ContinuationPoint", value.getContinuationPoint());
      encoder.encodeStructArray("ParsingResults", value.getParsingResults(), ParsingResult.TYPE_ID);
      encoder.encodeDiagnosticInfoArray("DiagnosticInfos", value.getDiagnosticInfos());
      encoder.encodeStruct("FilterResult", value.getFilterResult(), ContentFilterResult.TYPE_ID);
    }
  }
}
