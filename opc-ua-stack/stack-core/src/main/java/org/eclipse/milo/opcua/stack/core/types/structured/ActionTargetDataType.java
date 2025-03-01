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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.10.3">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.10.3</a>
 */
public class ActionTargetDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=18593");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=18598");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=18610");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=18622");

  private final UShort actionTargetId;

  private final @Nullable String name;

  private final LocalizedText description;

  public ActionTargetDataType(
      UShort actionTargetId, @Nullable String name, LocalizedText description) {
    this.actionTargetId = actionTargetId;
    this.name = name;
    this.description = description;
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

  public UShort getActionTargetId() {
    return actionTargetId;
  }

  public @Nullable String getName() {
    return name;
  }

  public LocalizedText getDescription() {
    return description;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ActionTargetDataType that = (ActionTargetDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getActionTargetId(), that.getActionTargetId());
    eqb.append(getName(), that.getName());
    eqb.append(getDescription(), that.getDescription());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getActionTargetId());
    hcb.append(getName());
    hcb.append(getDescription());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ActionTargetDataType.class.getSimpleName() + "[", "]");
    joiner.add("actionTargetId=" + getActionTargetId());
    joiner.add("name='" + getName() + "'");
    joiner.add("description=" + getDescription());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 18598),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ActionTargetId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 5),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Name",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Description",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 21),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ActionTargetDataType> {
    @Override
    public Class<ActionTargetDataType> getType() {
      return ActionTargetDataType.class;
    }

    @Override
    public ActionTargetDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final UShort actionTargetId;
      final String name;
      final LocalizedText description;
      actionTargetId = decoder.decodeUInt16("ActionTargetId");
      name = decoder.decodeString("Name");
      description = decoder.decodeLocalizedText("Description");
      return new ActionTargetDataType(actionTargetId, name, description);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, ActionTargetDataType value) {
      encoder.encodeUInt16("ActionTargetId", value.getActionTargetId());
      encoder.encodeString("Name", value.getName());
      encoder.encodeLocalizedText("Description", value.getDescription());
    }
  }
}
