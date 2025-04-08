package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrowseDirection;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.2/#5.9.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.2/#5.9.2.2</a>
 */
public class BrowseDescription extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=514");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=516");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=515");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15180");

  private final NodeId nodeId;

  private final BrowseDirection browseDirection;

  private final NodeId referenceTypeId;

  private final Boolean includeSubtypes;

  private final UInteger nodeClassMask;

  private final UInteger resultMask;

  public BrowseDescription(
      NodeId nodeId,
      BrowseDirection browseDirection,
      NodeId referenceTypeId,
      Boolean includeSubtypes,
      UInteger nodeClassMask,
      UInteger resultMask) {
    this.nodeId = nodeId;
    this.browseDirection = browseDirection;
    this.referenceTypeId = referenceTypeId;
    this.includeSubtypes = includeSubtypes;
    this.nodeClassMask = nodeClassMask;
    this.resultMask = resultMask;
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

  public BrowseDirection getBrowseDirection() {
    return browseDirection;
  }

  public NodeId getReferenceTypeId() {
    return referenceTypeId;
  }

  public Boolean getIncludeSubtypes() {
    return includeSubtypes;
  }

  public UInteger getNodeClassMask() {
    return nodeClassMask;
  }

  public UInteger getResultMask() {
    return resultMask;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    BrowseDescription that = (BrowseDescription) object;
    var eqb = new EqualsBuilder();
    eqb.append(getNodeId(), that.getNodeId());
    eqb.append(getBrowseDirection(), that.getBrowseDirection());
    eqb.append(getReferenceTypeId(), that.getReferenceTypeId());
    eqb.append(getIncludeSubtypes(), that.getIncludeSubtypes());
    eqb.append(getNodeClassMask(), that.getNodeClassMask());
    eqb.append(getResultMask(), that.getResultMask());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getNodeId());
    hcb.append(getBrowseDirection());
    hcb.append(getReferenceTypeId());
    hcb.append(getIncludeSubtypes());
    hcb.append(getNodeClassMask());
    hcb.append(getResultMask());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", BrowseDescription.class.getSimpleName() + "[", "]");
    joiner.add("nodeId=" + getNodeId());
    joiner.add("browseDirection=" + getBrowseDirection());
    joiner.add("referenceTypeId=" + getReferenceTypeId());
    joiner.add("includeSubtypes=" + getIncludeSubtypes());
    joiner.add("nodeClassMask=" + getNodeClassMask());
    joiner.add("resultMask=" + getResultMask());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 516),
        new NodeId(0, 22),
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
              "BrowseDirection",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 510),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ReferenceTypeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "IncludeSubtypes",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "NodeClassMask",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ResultMask",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<BrowseDescription> {
    @Override
    public Class<BrowseDescription> getType() {
      return BrowseDescription.class;
    }

    @Override
    public BrowseDescription decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId nodeId;
      final BrowseDirection browseDirection;
      final NodeId referenceTypeId;
      final Boolean includeSubtypes;
      final UInteger nodeClassMask;
      final UInteger resultMask;
      nodeId = decoder.decodeNodeId("NodeId");
      browseDirection = BrowseDirection.from(decoder.decodeEnum("BrowseDirection"));
      referenceTypeId = decoder.decodeNodeId("ReferenceTypeId");
      includeSubtypes = decoder.decodeBoolean("IncludeSubtypes");
      nodeClassMask = decoder.decodeUInt32("NodeClassMask");
      resultMask = decoder.decodeUInt32("ResultMask");
      return new BrowseDescription(
          nodeId, browseDirection, referenceTypeId, includeSubtypes, nodeClassMask, resultMask);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, BrowseDescription value) {
      encoder.encodeNodeId("NodeId", value.getNodeId());
      encoder.encodeEnum("BrowseDirection", value.getBrowseDirection());
      encoder.encodeNodeId("ReferenceTypeId", value.getReferenceTypeId());
      encoder.encodeBoolean("IncludeSubtypes", value.getIncludeSubtypes());
      encoder.encodeUInt32("NodeClassMask", value.getNodeClassMask());
      encoder.encodeUInt32("ResultMask", value.getResultMask());
    }
  }
}
