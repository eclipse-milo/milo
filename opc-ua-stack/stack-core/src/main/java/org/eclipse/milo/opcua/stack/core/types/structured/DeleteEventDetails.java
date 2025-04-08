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
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/6.9.7/#6.9.7.1">https://reference.opcfoundation.org/v105/Core/docs/Part11/6.9.7/#6.9.7.1</a>
 */
public class DeleteEventDetails extends HistoryUpdateDetails implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=692");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=694");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=693");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15285");

  private final NodeId nodeId;

  private final ByteString @Nullable [] eventIds;

  public DeleteEventDetails(NodeId nodeId, ByteString @Nullable [] eventIds) {
    this.nodeId = nodeId;
    this.eventIds = eventIds;
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

  public NodeId getNodeId() {
    return nodeId;
  }

  public ByteString @Nullable [] getEventIds() {
    return eventIds;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    DeleteEventDetails that = (DeleteEventDetails) object;
    var eqb = new EqualsBuilder();
    eqb.append(getNodeId(), that.getNodeId());
    eqb.append(getEventIds(), that.getEventIds());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getNodeId());
    hcb.append(getEventIds());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", DeleteEventDetails.class.getSimpleName() + "[", "]");
    joiner.add("nodeId=" + getNodeId());
    joiner.add("eventIds=" + java.util.Arrays.toString(getEventIds()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 694),
        new NodeId(0, 677),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "NodeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "EventIds",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<DeleteEventDetails> {
    @Override
    public Class<DeleteEventDetails> getType() {
      return DeleteEventDetails.class;
    }

    @Override
    public DeleteEventDetails decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId nodeId;
      final ByteString[] eventIds;
      nodeId = decoder.decodeNodeId("NodeId");
      eventIds = decoder.decodeByteStringArray("EventIds");
      return new DeleteEventDetails(nodeId, eventIds);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, DeleteEventDetails value) {
      encoder.encodeNodeId("NodeId", value.getNodeId());
      encoder.encodeByteStringArray("EventIds", value.getEventIds());
    }
  }
}
