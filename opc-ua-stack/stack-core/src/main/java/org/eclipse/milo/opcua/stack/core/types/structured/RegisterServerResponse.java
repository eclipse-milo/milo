package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.5.5/#5.5.5.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.5.5/#5.5.5.2</a>
 */
public class RegisterServerResponse extends Structure implements UaResponseMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=438");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=440");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=439");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15104");

  private final ResponseHeader responseHeader;

  public RegisterServerResponse(ResponseHeader responseHeader) {
    this.responseHeader = responseHeader;
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

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    RegisterServerResponse that = (RegisterServerResponse) object;
    var eqb = new EqualsBuilder();
    eqb.append(getResponseHeader(), that.getResponseHeader());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getResponseHeader());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", RegisterServerResponse.class.getSimpleName() + "[", "]");
    joiner.add("responseHeader=" + getResponseHeader());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 440),
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
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<RegisterServerResponse> {
    @Override
    public Class<RegisterServerResponse> getType() {
      return RegisterServerResponse.class;
    }

    @Override
    public RegisterServerResponse decodeType(EncodingContext context, UaDecoder decoder) {
      final ResponseHeader responseHeader;
      responseHeader =
          (ResponseHeader) decoder.decodeStruct("ResponseHeader", ResponseHeader.TYPE_ID);
      return new RegisterServerResponse(responseHeader);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, RegisterServerResponse value) {
      encoder.encodeStruct("ResponseHeader", value.getResponseHeader(), ResponseHeader.TYPE_ID);
    }
  }
}
