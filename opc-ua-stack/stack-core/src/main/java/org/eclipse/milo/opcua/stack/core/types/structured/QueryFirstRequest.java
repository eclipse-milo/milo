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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.10.3/#5.10.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.10.3/#5.10.3.1</a>
 */
public class QueryFirstRequest extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=613");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=615");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=614");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15244");

  private final RequestHeader requestHeader;

  private final ViewDescription view;

  private final NodeTypeDescription @Nullable [] nodeTypes;

  private final ContentFilter filter;

  private final UInteger maxDataSetsToReturn;

  private final UInteger maxReferencesToReturn;

  public QueryFirstRequest(
      RequestHeader requestHeader,
      ViewDescription view,
      NodeTypeDescription @Nullable [] nodeTypes,
      ContentFilter filter,
      UInteger maxDataSetsToReturn,
      UInteger maxReferencesToReturn) {
    this.requestHeader = requestHeader;
    this.view = view;
    this.nodeTypes = nodeTypes;
    this.filter = filter;
    this.maxDataSetsToReturn = maxDataSetsToReturn;
    this.maxReferencesToReturn = maxReferencesToReturn;
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

  public ViewDescription getView() {
    return view;
  }

  public NodeTypeDescription @Nullable [] getNodeTypes() {
    return nodeTypes;
  }

  public ContentFilter getFilter() {
    return filter;
  }

  public UInteger getMaxDataSetsToReturn() {
    return maxDataSetsToReturn;
  }

  public UInteger getMaxReferencesToReturn() {
    return maxReferencesToReturn;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    QueryFirstRequest that = (QueryFirstRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getView(), that.getView());
    eqb.append(getNodeTypes(), that.getNodeTypes());
    eqb.append(getFilter(), that.getFilter());
    eqb.append(getMaxDataSetsToReturn(), that.getMaxDataSetsToReturn());
    eqb.append(getMaxReferencesToReturn(), that.getMaxReferencesToReturn());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getView());
    hcb.append(getNodeTypes());
    hcb.append(getFilter());
    hcb.append(getMaxDataSetsToReturn());
    hcb.append(getMaxReferencesToReturn());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", QueryFirstRequest.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("view=" + getView());
    joiner.add("nodeTypes=" + java.util.Arrays.toString(getNodeTypes()));
    joiner.add("filter=" + getFilter());
    joiner.add("maxDataSetsToReturn=" + getMaxDataSetsToReturn());
    joiner.add("maxReferencesToReturn=" + getMaxReferencesToReturn());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 615),
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
              "View",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 511),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "NodeTypes",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 573),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Filter",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 586),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MaxDataSetsToReturn",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 289),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MaxReferencesToReturn",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 289),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<QueryFirstRequest> {
    @Override
    public Class<QueryFirstRequest> getType() {
      return QueryFirstRequest.class;
    }

    @Override
    public QueryFirstRequest decodeType(EncodingContext context, UaDecoder decoder) {
      final RequestHeader requestHeader;
      final ViewDescription view;
      final NodeTypeDescription[] nodeTypes;
      final ContentFilter filter;
      final UInteger maxDataSetsToReturn;
      final UInteger maxReferencesToReturn;
      requestHeader = (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      view = (ViewDescription) decoder.decodeStruct("View", ViewDescription.TYPE_ID);
      nodeTypes =
          (NodeTypeDescription[])
              decoder.decodeStructArray("NodeTypes", NodeTypeDescription.TYPE_ID);
      filter = (ContentFilter) decoder.decodeStruct("Filter", ContentFilter.TYPE_ID);
      maxDataSetsToReturn = decoder.decodeUInt32("MaxDataSetsToReturn");
      maxReferencesToReturn = decoder.decodeUInt32("MaxReferencesToReturn");
      return new QueryFirstRequest(
          requestHeader, view, nodeTypes, filter, maxDataSetsToReturn, maxReferencesToReturn);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, QueryFirstRequest value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeStruct("View", value.getView(), ViewDescription.TYPE_ID);
      encoder.encodeStructArray("NodeTypes", value.getNodeTypes(), NodeTypeDescription.TYPE_ID);
      encoder.encodeStruct("Filter", value.getFilter(), ContentFilter.TYPE_ID);
      encoder.encodeUInt32("MaxDataSetsToReturn", value.getMaxDataSetsToReturn());
      encoder.encodeUInt32("MaxReferencesToReturn", value.getMaxReferencesToReturn());
    }
  }
}
