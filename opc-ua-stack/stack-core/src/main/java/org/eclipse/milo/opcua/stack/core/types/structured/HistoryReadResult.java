package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.3/#5.11.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.11.3/#5.11.3.2</a>
 */
public class HistoryReadResult extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=638");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=640");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=639");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15260");

  private final StatusCode statusCode;

  private final ByteString continuationPoint;

  private final ExtensionObject historyData;

  public HistoryReadResult(
      StatusCode statusCode, ByteString continuationPoint, ExtensionObject historyData) {
    this.statusCode = statusCode;
    this.continuationPoint = continuationPoint;
    this.historyData = historyData;
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

  public ByteString getContinuationPoint() {
    return continuationPoint;
  }

  public ExtensionObject getHistoryData() {
    return historyData;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    HistoryReadResult that = (HistoryReadResult) object;
    var eqb = new EqualsBuilder();
    eqb.append(getStatusCode(), that.getStatusCode());
    eqb.append(getContinuationPoint(), that.getContinuationPoint());
    eqb.append(getHistoryData(), that.getHistoryData());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getStatusCode());
    hcb.append(getContinuationPoint());
    hcb.append(getHistoryData());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", HistoryReadResult.class.getSimpleName() + "[", "]");
    joiner.add("statusCode=" + getStatusCode());
    joiner.add("continuationPoint=" + getContinuationPoint());
    joiner.add("historyData=" + getHistoryData());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 640),
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
              "ContinuationPoint",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 521),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "HistoryData",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 22),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<HistoryReadResult> {
    @Override
    public Class<HistoryReadResult> getType() {
      return HistoryReadResult.class;
    }

    @Override
    public HistoryReadResult decodeType(EncodingContext context, UaDecoder decoder) {
      final StatusCode statusCode;
      final ByteString continuationPoint;
      final ExtensionObject historyData;
      statusCode = decoder.decodeStatusCode("StatusCode");
      continuationPoint = decoder.decodeByteString("ContinuationPoint");
      historyData = decoder.decodeExtensionObject("HistoryData");
      return new HistoryReadResult(statusCode, continuationPoint, historyData);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, HistoryReadResult value) {
      encoder.encodeStatusCode("StatusCode", value.getStatusCode());
      encoder.encodeByteString("ContinuationPoint", value.getContinuationPoint());
      encoder.encodeExtensionObject("HistoryData", value.getHistoryData());
    }
  }
}
