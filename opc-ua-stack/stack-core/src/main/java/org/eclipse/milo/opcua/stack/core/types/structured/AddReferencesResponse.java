package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.8.3/#5.8.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.8.3/#5.8.3.2</a>
 */
public class AddReferencesResponse extends Structure implements UaResponseMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=495");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=497");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=496");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15171");

  private final ResponseHeader responseHeader;

  private final StatusCode @Nullable [] results;

  private final DiagnosticInfo @Nullable [] diagnosticInfos;

  public AddReferencesResponse(
      ResponseHeader responseHeader,
      StatusCode @Nullable [] results,
      DiagnosticInfo @Nullable [] diagnosticInfos) {
    this.responseHeader = responseHeader;
    this.results = results;
    this.diagnosticInfos = diagnosticInfos;
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

  public StatusCode @Nullable [] getResults() {
    return results;
  }

  public DiagnosticInfo @Nullable [] getDiagnosticInfos() {
    return diagnosticInfos;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    AddReferencesResponse that = (AddReferencesResponse) object;
    var eqb = new EqualsBuilder();
    eqb.append(getResponseHeader(), that.getResponseHeader());
    eqb.append(getResults(), that.getResults());
    eqb.append(getDiagnosticInfos(), that.getDiagnosticInfos());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getResponseHeader());
    hcb.append(getResults());
    hcb.append(getDiagnosticInfos());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", AddReferencesResponse.class.getSimpleName() + "[", "]");
    joiner.add("responseHeader=" + getResponseHeader());
    joiner.add("results=" + java.util.Arrays.toString(getResults()));
    joiner.add("diagnosticInfos=" + java.util.Arrays.toString(getDiagnosticInfos()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 497),
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
              "Results",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 19),
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
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<AddReferencesResponse> {
    @Override
    public Class<AddReferencesResponse> getType() {
      return AddReferencesResponse.class;
    }

    @Override
    public AddReferencesResponse decodeType(EncodingContext context, UaDecoder decoder) {
      final ResponseHeader responseHeader;
      final StatusCode[] results;
      final DiagnosticInfo[] diagnosticInfos;
      responseHeader =
          (ResponseHeader) decoder.decodeStruct("ResponseHeader", ResponseHeader.TYPE_ID);
      results = decoder.decodeStatusCodeArray("Results");
      diagnosticInfos = decoder.decodeDiagnosticInfoArray("DiagnosticInfos");
      return new AddReferencesResponse(responseHeader, results, diagnosticInfos);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, AddReferencesResponse value) {
      encoder.encodeStruct("ResponseHeader", value.getResponseHeader(), ResponseHeader.TYPE_ID);
      encoder.encodeStatusCodeArray("Results", value.getResults());
      encoder.encodeDiagnosticInfoArray("DiagnosticInfos", value.getDiagnosticInfos());
    }
  }
}
