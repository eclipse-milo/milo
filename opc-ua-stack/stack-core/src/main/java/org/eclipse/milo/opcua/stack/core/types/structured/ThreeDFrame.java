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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.30">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.30</a>
 */
public class ThreeDFrame extends Frame implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=18814");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=18823");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=18859");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=19072");

  private final ThreeDCartesianCoordinates cartesianCoordinates;

  private final ThreeDOrientation orientation;

  public ThreeDFrame(
      ThreeDCartesianCoordinates cartesianCoordinates, ThreeDOrientation orientation) {
    this.cartesianCoordinates = cartesianCoordinates;
    this.orientation = orientation;
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

  public ThreeDCartesianCoordinates getCartesianCoordinates() {
    return cartesianCoordinates;
  }

  public ThreeDOrientation getOrientation() {
    return orientation;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ThreeDFrame that = (ThreeDFrame) object;
    var eqb = new EqualsBuilder();
    eqb.append(getCartesianCoordinates(), that.getCartesianCoordinates());
    eqb.append(getOrientation(), that.getOrientation());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getCartesianCoordinates());
    hcb.append(getOrientation());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ThreeDFrame.class.getSimpleName() + "[", "]");
    joiner.add("cartesianCoordinates=" + getCartesianCoordinates());
    joiner.add("orientation=" + getOrientation());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 18823),
        new NodeId(0, 18813),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "CartesianCoordinates",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 18810),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Orientation",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 18812),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ThreeDFrame> {
    @Override
    public Class<ThreeDFrame> getType() {
      return ThreeDFrame.class;
    }

    @Override
    public ThreeDFrame decodeType(EncodingContext context, UaDecoder decoder) {
      final ThreeDCartesianCoordinates cartesianCoordinates;
      final ThreeDOrientation orientation;
      cartesianCoordinates =
          (ThreeDCartesianCoordinates)
              decoder.decodeStruct("CartesianCoordinates", ThreeDCartesianCoordinates.TYPE_ID);
      orientation =
          (ThreeDOrientation) decoder.decodeStruct("Orientation", ThreeDOrientation.TYPE_ID);
      return new ThreeDFrame(cartesianCoordinates, orientation);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, ThreeDFrame value) {
      encoder.encodeStruct(
          "CartesianCoordinates",
          value.getCartesianCoordinates(),
          ThreeDCartesianCoordinates.TYPE_ID);
      encoder.encodeStruct("Orientation", value.getOrientation(), ThreeDOrientation.TYPE_ID);
    }
  }
}
