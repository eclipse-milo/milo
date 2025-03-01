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
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.10.3/#5.10.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.10.3/#5.10.3.1</a>
 */
public class NodeTypeDescription extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=573");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=575");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=574");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15201");

  private final ExpandedNodeId typeDefinitionNode;

  private final Boolean includeSubTypes;

  private final QueryDataDescription @Nullable [] dataToReturn;

  public NodeTypeDescription(
      ExpandedNodeId typeDefinitionNode,
      Boolean includeSubTypes,
      QueryDataDescription @Nullable [] dataToReturn) {
    this.typeDefinitionNode = typeDefinitionNode;
    this.includeSubTypes = includeSubTypes;
    this.dataToReturn = dataToReturn;
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

  public ExpandedNodeId getTypeDefinitionNode() {
    return typeDefinitionNode;
  }

  public Boolean getIncludeSubTypes() {
    return includeSubTypes;
  }

  public QueryDataDescription @Nullable [] getDataToReturn() {
    return dataToReturn;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    NodeTypeDescription that = (NodeTypeDescription) object;
    var eqb = new EqualsBuilder();
    eqb.append(getTypeDefinitionNode(), that.getTypeDefinitionNode());
    eqb.append(getIncludeSubTypes(), that.getIncludeSubTypes());
    eqb.append(getDataToReturn(), that.getDataToReturn());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getTypeDefinitionNode());
    hcb.append(getIncludeSubTypes());
    hcb.append(getDataToReturn());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", NodeTypeDescription.class.getSimpleName() + "[", "]");
    joiner.add("typeDefinitionNode=" + getTypeDefinitionNode());
    joiner.add("includeSubTypes=" + getIncludeSubTypes());
    joiner.add("dataToReturn=" + java.util.Arrays.toString(getDataToReturn()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 575),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "TypeDefinitionNode",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 18),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "IncludeSubTypes",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DataToReturn",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 570),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<NodeTypeDescription> {
    @Override
    public Class<NodeTypeDescription> getType() {
      return NodeTypeDescription.class;
    }

    @Override
    public NodeTypeDescription decodeType(EncodingContext context, UaDecoder decoder) {
      final ExpandedNodeId typeDefinitionNode;
      final Boolean includeSubTypes;
      final QueryDataDescription[] dataToReturn;
      typeDefinitionNode = decoder.decodeExpandedNodeId("TypeDefinitionNode");
      includeSubTypes = decoder.decodeBoolean("IncludeSubTypes");
      dataToReturn =
          (QueryDataDescription[])
              decoder.decodeStructArray("DataToReturn", QueryDataDescription.TYPE_ID);
      return new NodeTypeDescription(typeDefinitionNode, includeSubTypes, dataToReturn);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, NodeTypeDescription value) {
      encoder.encodeExpandedNodeId("TypeDefinitionNode", value.getTypeDefinitionNode());
      encoder.encodeBoolean("IncludeSubTypes", value.getIncludeSubTypes());
      encoder.encodeStructArray(
          "DataToReturn", value.getDataToReturn(), QueryDataDescription.TYPE_ID);
    }
  }
}
