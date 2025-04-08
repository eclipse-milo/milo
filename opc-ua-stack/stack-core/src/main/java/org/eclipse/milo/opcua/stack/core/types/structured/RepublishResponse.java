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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.6/#5.14.6.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.6/#5.14.6.2</a>
 */
public class RepublishResponse extends Structure implements UaResponseMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=833");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=835");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=834");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15355");

  private final ResponseHeader responseHeader;

  private final NotificationMessage notificationMessage;

  public RepublishResponse(ResponseHeader responseHeader, NotificationMessage notificationMessage) {
    this.responseHeader = responseHeader;
    this.notificationMessage = notificationMessage;
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

  public NotificationMessage getNotificationMessage() {
    return notificationMessage;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    RepublishResponse that = (RepublishResponse) object;
    var eqb = new EqualsBuilder();
    eqb.append(getResponseHeader(), that.getResponseHeader());
    eqb.append(getNotificationMessage(), that.getNotificationMessage());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getResponseHeader());
    hcb.append(getNotificationMessage());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", RepublishResponse.class.getSimpleName() + "[", "]");
    joiner.add("responseHeader=" + getResponseHeader());
    joiner.add("notificationMessage=" + getNotificationMessage());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 835),
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
              "NotificationMessage",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 803),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<RepublishResponse> {
    @Override
    public Class<RepublishResponse> getType() {
      return RepublishResponse.class;
    }

    @Override
    public RepublishResponse decodeType(EncodingContext context, UaDecoder decoder) {
      final ResponseHeader responseHeader;
      final NotificationMessage notificationMessage;
      responseHeader =
          (ResponseHeader) decoder.decodeStruct("ResponseHeader", ResponseHeader.TYPE_ID);
      notificationMessage =
          (NotificationMessage)
              decoder.decodeStruct("NotificationMessage", NotificationMessage.TYPE_ID);
      return new RepublishResponse(responseHeader, notificationMessage);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, RepublishResponse value) {
      encoder.encodeStruct("ResponseHeader", value.getResponseHeader(), ResponseHeader.TYPE_ID);
      encoder.encodeStruct(
          "NotificationMessage", value.getNotificationMessage(), NotificationMessage.TYPE_ID);
    }
  }
}
