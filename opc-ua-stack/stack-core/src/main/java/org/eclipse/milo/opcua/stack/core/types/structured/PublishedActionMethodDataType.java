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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.10.6">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.10.6</a>
 */
public class PublishedActionMethodDataType extends PublishedActionDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=18793");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=18795");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=18937");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=18945");

  private final ActionMethodDataType @Nullable [] actionMethods;

  public PublishedActionMethodDataType(
      DataSetMetaDataType requestDataSetMetaData,
      ActionTargetDataType @Nullable [] actionTargets,
      ActionMethodDataType @Nullable [] actionMethods) {
    super(requestDataSetMetaData, actionTargets);
    this.actionMethods = actionMethods;
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

  public ActionMethodDataType @Nullable [] getActionMethods() {
    return actionMethods;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    PublishedActionMethodDataType that = (PublishedActionMethodDataType) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getActionMethods(), that.getActionMethods());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getActionMethods());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", PublishedActionMethodDataType.class.getSimpleName() + "[", "]");
    joiner.add("actionMethods=" + java.util.Arrays.toString(getActionMethods()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 18795),
        new NodeId(0, 18594),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "RequestDataSetMetaData",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14523),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ActionTargets",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 18593),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ActionMethods",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 18597),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<PublishedActionMethodDataType> {
    @Override
    public Class<PublishedActionMethodDataType> getType() {
      return PublishedActionMethodDataType.class;
    }

    @Override
    public PublishedActionMethodDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final DataSetMetaDataType requestDataSetMetaData;
      final ActionTargetDataType[] actionTargets;
      final ActionMethodDataType[] actionMethods;
      requestDataSetMetaData =
          (DataSetMetaDataType)
              decoder.decodeStruct("RequestDataSetMetaData", DataSetMetaDataType.TYPE_ID);
      actionTargets =
          (ActionTargetDataType[])
              decoder.decodeStructArray("ActionTargets", ActionTargetDataType.TYPE_ID);
      actionMethods =
          (ActionMethodDataType[])
              decoder.decodeStructArray("ActionMethods", ActionMethodDataType.TYPE_ID);
      return new PublishedActionMethodDataType(
          requestDataSetMetaData, actionTargets, actionMethods);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, PublishedActionMethodDataType value) {
      encoder.encodeStruct(
          "RequestDataSetMetaData", value.getRequestDataSetMetaData(), DataSetMetaDataType.TYPE_ID);
      encoder.encodeStructArray(
          "ActionTargets", value.getActionTargets(), ActionTargetDataType.TYPE_ID);
      encoder.encodeStructArray(
          "ActionMethods", value.getActionMethods(), ActionMethodDataType.TYPE_ID);
    }
  }
}
