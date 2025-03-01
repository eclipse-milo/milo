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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.25.4">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.25.4</a>
 */
public class StatusChangeNotification extends NotificationData implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=818");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=820");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=819");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15350");

  private final StatusCode status;

  private final DiagnosticInfo diagnosticInfo;

  public StatusChangeNotification(StatusCode status, DiagnosticInfo diagnosticInfo) {
    this.status = status;
    this.diagnosticInfo = diagnosticInfo;
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

  public StatusCode getStatus() {
    return status;
  }

  public DiagnosticInfo getDiagnosticInfo() {
    return diagnosticInfo;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    StatusChangeNotification that = (StatusChangeNotification) object;
    var eqb = new EqualsBuilder();
    eqb.append(getStatus(), that.getStatus());
    eqb.append(getDiagnosticInfo(), that.getDiagnosticInfo());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getStatus());
    hcb.append(getDiagnosticInfo());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", StatusChangeNotification.class.getSimpleName() + "[", "]");
    joiner.add("status=" + getStatus());
    joiner.add("diagnosticInfo=" + getDiagnosticInfo());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 820),
        new NodeId(0, 945),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Status",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 19),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DiagnosticInfo",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 25),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<StatusChangeNotification> {
    @Override
    public Class<StatusChangeNotification> getType() {
      return StatusChangeNotification.class;
    }

    @Override
    public StatusChangeNotification decodeType(EncodingContext context, UaDecoder decoder) {
      final StatusCode status;
      final DiagnosticInfo diagnosticInfo;
      status = decoder.decodeStatusCode("Status");
      diagnosticInfo = decoder.decodeDiagnosticInfo("DiagnosticInfo");
      return new StatusChangeNotification(status, diagnosticInfo);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, StatusChangeNotification value) {
      encoder.encodeStatusCode("Status", value.getStatus());
      encoder.encodeDiagnosticInfo("DiagnosticInfo", value.getDiagnosticInfo());
    }
  }
}
