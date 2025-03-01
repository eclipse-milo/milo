package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.4/#5.9.4.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.4/#5.9.4.2</a>
 */
public class TranslateBrowsePathsToNodeIdsRequest extends Structure
    implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=552");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=554");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=553");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15193");

  private final RequestHeader requestHeader;

  private final BrowsePath @Nullable [] browsePaths;

  public TranslateBrowsePathsToNodeIdsRequest(
      RequestHeader requestHeader, BrowsePath @Nullable [] browsePaths) {
    this.requestHeader = requestHeader;
    this.browsePaths = browsePaths;
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

  public RequestHeader getRequestHeader() {
    return requestHeader;
  }

  public BrowsePath @Nullable [] getBrowsePaths() {
    return browsePaths;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    TranslateBrowsePathsToNodeIdsRequest that = (TranslateBrowsePathsToNodeIdsRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getBrowsePaths(), that.getBrowsePaths());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getBrowsePaths());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(
            ", ", TranslateBrowsePathsToNodeIdsRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("browsePaths=" + java.util.Arrays.toString(getBrowsePaths()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 554),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "RequestHeader",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 389),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "BrowsePaths",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 543),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec
      extends GenericDataTypeCodec<TranslateBrowsePathsToNodeIdsRequest> {
    @Override
    public Class<TranslateBrowsePathsToNodeIdsRequest> getType() {
      return TranslateBrowsePathsToNodeIdsRequest.class;
    }

    @Override
    public TranslateBrowsePathsToNodeIdsRequest decodeType(
        EncodingContext context, UaDecoder decoder) {
      final RequestHeader requestHeader;
      final BrowsePath[] browsePaths;
      requestHeader = (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      browsePaths = (BrowsePath[]) decoder.decodeStructArray("BrowsePaths", BrowsePath.TYPE_ID);
      return new TranslateBrowsePathsToNodeIdsRequest(requestHeader, browsePaths);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, TranslateBrowsePathsToNodeIdsRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeStructArray("BrowsePaths", value.getBrowsePaths(), BrowsePath.TYPE_ID);
    }
  }
}
