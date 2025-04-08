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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part8/6.6.1">https://reference.opcfoundation.org/v105/Core/docs/Part8/6.6.1</a>
 */
public class AnnotationDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=32434");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=32560");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=32572");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=32584");

  private final @Nullable String annotation;

  private final @Nullable String discipline;

  private final @Nullable String uri;

  public AnnotationDataType(
      @Nullable String annotation, @Nullable String discipline, @Nullable String uri) {
    this.annotation = annotation;
    this.discipline = discipline;
    this.uri = uri;
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

  public @Nullable String getAnnotation() {
    return annotation;
  }

  public @Nullable String getDiscipline() {
    return discipline;
  }

  public @Nullable String getUri() {
    return uri;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    AnnotationDataType that = (AnnotationDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getAnnotation(), that.getAnnotation());
    eqb.append(getDiscipline(), that.getDiscipline());
    eqb.append(getUri(), that.getUri());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getAnnotation());
    hcb.append(getDiscipline());
    hcb.append(getUri());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", AnnotationDataType.class.getSimpleName() + "[", "]");
    joiner.add("annotation='" + getAnnotation() + "'");
    joiner.add("discipline='" + getDiscipline() + "'");
    joiner.add("uri='" + getUri() + "'");
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 32560),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Annotation",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Discipline",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Uri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<AnnotationDataType> {
    @Override
    public Class<AnnotationDataType> getType() {
      return AnnotationDataType.class;
    }

    @Override
    public AnnotationDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final String annotation;
      final String discipline;
      final String uri;
      annotation = decoder.decodeString("Annotation");
      discipline = decoder.decodeString("Discipline");
      uri = decoder.decodeString("Uri");
      return new AnnotationDataType(annotation, discipline, uri);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, AnnotationDataType value) {
      encoder.encodeString("Annotation", value.getAnnotation());
      encoder.encodeString("Discipline", value.getDiscipline());
      encoder.encodeString("Uri", value.getUri());
    }
  }
}
