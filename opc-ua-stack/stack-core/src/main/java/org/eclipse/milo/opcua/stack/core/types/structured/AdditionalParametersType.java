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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.1">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.1</a>
 */
public class AdditionalParametersType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=16313");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=17537");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=17541");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=17547");

  private final KeyValuePair @Nullable [] parameters;

  public AdditionalParametersType(KeyValuePair @Nullable [] parameters) {
    this.parameters = parameters;
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

  public KeyValuePair @Nullable [] getParameters() {
    return parameters;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    AdditionalParametersType that = (AdditionalParametersType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getParameters(), that.getParameters());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getParameters());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", AdditionalParametersType.class.getSimpleName() + "[", "]");
    joiner.add("parameters=" + java.util.Arrays.toString(getParameters()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 17537),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Parameters",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14533),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<AdditionalParametersType> {
    @Override
    public Class<AdditionalParametersType> getType() {
      return AdditionalParametersType.class;
    }

    @Override
    public AdditionalParametersType decodeType(EncodingContext context, UaDecoder decoder) {
      final KeyValuePair[] parameters;
      parameters = (KeyValuePair[]) decoder.decodeStructArray("Parameters", KeyValuePair.TYPE_ID);
      return new AdditionalParametersType(parameters);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, AdditionalParametersType value) {
      encoder.encodeStructArray("Parameters", value.getParameters(), KeyValuePair.TYPE_ID);
    }
  }
}
