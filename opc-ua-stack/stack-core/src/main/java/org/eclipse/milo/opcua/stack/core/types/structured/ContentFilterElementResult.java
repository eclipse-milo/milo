package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.7.2</a>
 */
public class ContentFilterElementResult extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=604");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=606");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=605");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15211");

  private final StatusCode statusCode;

  private final StatusCode @Nullable [] operandStatusCodes;

  private final DiagnosticInfo @Nullable [] operandDiagnosticInfos;

  public ContentFilterElementResult(
      StatusCode statusCode,
      StatusCode @Nullable [] operandStatusCodes,
      DiagnosticInfo @Nullable [] operandDiagnosticInfos) {
    this.statusCode = statusCode;
    this.operandStatusCodes = operandStatusCodes;
    this.operandDiagnosticInfos = operandDiagnosticInfos;
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

  public StatusCode getStatusCode() {
    return statusCode;
  }

  public StatusCode @Nullable [] getOperandStatusCodes() {
    return operandStatusCodes;
  }

  public DiagnosticInfo @Nullable [] getOperandDiagnosticInfos() {
    return operandDiagnosticInfos;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ContentFilterElementResult that = (ContentFilterElementResult) object;
    var eqb = new EqualsBuilder();
    eqb.append(getStatusCode(), that.getStatusCode());
    eqb.append(getOperandStatusCodes(), that.getOperandStatusCodes());
    eqb.append(getOperandDiagnosticInfos(), that.getOperandDiagnosticInfos());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getStatusCode());
    hcb.append(getOperandStatusCodes());
    hcb.append(getOperandDiagnosticInfos());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", ContentFilterElementResult.class.getSimpleName() + "[", "]");
    joiner.add("statusCode=" + getStatusCode());
    joiner.add("operandStatusCodes=" + java.util.Arrays.toString(getOperandStatusCodes()));
    joiner.add("operandDiagnosticInfos=" + java.util.Arrays.toString(getOperandDiagnosticInfos()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 606),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "StatusCode",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 19),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "OperandStatusCodes",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 19),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "OperandDiagnosticInfos",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 25),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ContentFilterElementResult> {
    @Override
    public Class<ContentFilterElementResult> getType() {
      return ContentFilterElementResult.class;
    }

    @Override
    public ContentFilterElementResult decodeType(EncodingContext context, UaDecoder decoder) {
      final StatusCode statusCode;
      final StatusCode[] operandStatusCodes;
      final DiagnosticInfo[] operandDiagnosticInfos;
      statusCode = decoder.decodeStatusCode("StatusCode");
      operandStatusCodes = decoder.decodeStatusCodeArray("OperandStatusCodes");
      operandDiagnosticInfos = decoder.decodeDiagnosticInfoArray("OperandDiagnosticInfos");
      return new ContentFilterElementResult(statusCode, operandStatusCodes, operandDiagnosticInfos);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, ContentFilterElementResult value) {
      encoder.encodeStatusCode("StatusCode", value.getStatusCode());
      encoder.encodeStatusCodeArray("OperandStatusCodes", value.getOperandStatusCodes());
      encoder.encodeDiagnosticInfoArray(
          "OperandDiagnosticInfos", value.getOperandDiagnosticInfos());
    }
  }
}
