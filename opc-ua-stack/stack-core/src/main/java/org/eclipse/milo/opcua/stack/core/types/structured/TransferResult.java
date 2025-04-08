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
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.7/#5.14.7.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.7/#5.14.7.2</a>
 */
public class TransferResult extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=836");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=838");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=837");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15356");

  private final StatusCode statusCode;

  private final UInteger @Nullable [] availableSequenceNumbers;

  public TransferResult(StatusCode statusCode, UInteger @Nullable [] availableSequenceNumbers) {
    this.statusCode = statusCode;
    this.availableSequenceNumbers = availableSequenceNumbers;
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

  public StatusCode getStatusCode() {
    return statusCode;
  }

  public UInteger @Nullable [] getAvailableSequenceNumbers() {
    return availableSequenceNumbers;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    TransferResult that = (TransferResult) object;
    var eqb = new EqualsBuilder();
    eqb.append(getStatusCode(), that.getStatusCode());
    eqb.append(getAvailableSequenceNumbers(), that.getAvailableSequenceNumbers());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getStatusCode());
    hcb.append(getAvailableSequenceNumbers());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", TransferResult.class.getSimpleName() + "[", "]");
    joiner.add("statusCode=" + getStatusCode());
    joiner.add(
        "availableSequenceNumbers=" + java.util.Arrays.toString(getAvailableSequenceNumbers()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 838),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "StatusCode",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 19),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AvailableSequenceNumbers",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 289),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<TransferResult> {
    @Override
    public Class<TransferResult> getType() {
      return TransferResult.class;
    }

    @Override
    public TransferResult decodeType(EncodingContext context, UaDecoder decoder) {
      final StatusCode statusCode;
      final UInteger[] availableSequenceNumbers;
      statusCode = decoder.decodeStatusCode("StatusCode");
      availableSequenceNumbers = decoder.decodeUInt32Array("AvailableSequenceNumbers");
      return new TransferResult(statusCode, availableSequenceNumbers);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, TransferResult value) {
      encoder.encodeStatusCode("StatusCode", value.getStatusCode());
      encoder.encodeUInt32Array("AvailableSequenceNumbers", value.getAvailableSequenceNumbers());
    }
  }
}
