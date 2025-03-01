package org.eclipse.milo.opcua.stack.core.types.structured;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.13">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.13</a>
 */
public class SignedSoftwareCertificate extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=344");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=346");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=345");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15136");

  private final ByteString certificateData;

  private final ByteString signature;

  public SignedSoftwareCertificate(ByteString certificateData, ByteString signature) {
    this.certificateData = certificateData;
    this.signature = signature;
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

  public ByteString getCertificateData() {
    return certificateData;
  }

  public ByteString getSignature() {
    return signature;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    SignedSoftwareCertificate that = (SignedSoftwareCertificate) object;
    var eqb = new EqualsBuilder();
    eqb.append(getCertificateData(), that.getCertificateData());
    eqb.append(getSignature(), that.getSignature());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getCertificateData());
    hcb.append(getSignature());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", SignedSoftwareCertificate.class.getSimpleName() + "[", "]");
    joiner.add("certificateData=" + getCertificateData());
    joiner.add("signature=" + getSignature());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 346),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "CertificateData",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Signature",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<SignedSoftwareCertificate> {
    @Override
    public Class<SignedSoftwareCertificate> getType() {
      return SignedSoftwareCertificate.class;
    }

    @Override
    public SignedSoftwareCertificate decodeType(EncodingContext context, UaDecoder decoder) {
      final ByteString certificateData;
      final ByteString signature;
      certificateData = decoder.decodeByteString("CertificateData");
      signature = decoder.decodeByteString("Signature");
      return new SignedSoftwareCertificate(certificateData, signature);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, SignedSoftwareCertificate value) {
      encoder.encodeByteString("CertificateData", value.getCertificateData());
      encoder.encodeByteString("Signature", value.getSignature());
    }
  }
}
