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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.15/#12.3.15.3">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.15/#12.3.15.3</a>
 */
public class UserNameIdentityToken extends UserIdentityToken implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=322");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=324");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=323");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=0;i=15142");

  private final @Nullable String userName;

  private final ByteString password;

  private final @Nullable String encryptionAlgorithm;

  public UserNameIdentityToken(
      @Nullable String policyId,
      @Nullable String userName,
      ByteString password,
      @Nullable String encryptionAlgorithm) {
    super(policyId);
    this.userName = userName;
    this.password = password;
    this.encryptionAlgorithm = encryptionAlgorithm;
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

  public @Nullable String getUserName() {
    return userName;
  }

  public ByteString getPassword() {
    return password;
  }

  public @Nullable String getEncryptionAlgorithm() {
    return encryptionAlgorithm;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    UserNameIdentityToken that = (UserNameIdentityToken) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getUserName(), that.getUserName());
    eqb.append(getPassword(), that.getPassword());
    eqb.append(getEncryptionAlgorithm(), that.getEncryptionAlgorithm());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getUserName());
    hcb.append(getPassword());
    hcb.append(getEncryptionAlgorithm());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", UserNameIdentityToken.class.getSimpleName() + "[", "]");
    joiner.add("userName='" + getUserName() + "'");
    joiner.add("password=" + getPassword());
    joiner.add("encryptionAlgorithm='" + getEncryptionAlgorithm() + "'");
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 324),
        new NodeId(0, 316),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "PolicyId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UserName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Password",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "EncryptionAlgorithm",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<UserNameIdentityToken> {
    @Override
    public Class<UserNameIdentityToken> getType() {
      return UserNameIdentityToken.class;
    }

    @Override
    public UserNameIdentityToken decodeType(EncodingContext context, UaDecoder decoder) {
      final String policyId;
      final String userName;
      final ByteString password;
      final String encryptionAlgorithm;
      policyId = decoder.decodeString("PolicyId");
      userName = decoder.decodeString("UserName");
      password = decoder.decodeByteString("Password");
      encryptionAlgorithm = decoder.decodeString("EncryptionAlgorithm");
      return new UserNameIdentityToken(policyId, userName, password, encryptionAlgorithm);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, UserNameIdentityToken value) {
      encoder.encodeString("PolicyId", value.getPolicyId());
      encoder.encodeString("UserName", value.getUserName());
      encoder.encodeByteString("Password", value.getPassword());
      encoder.encodeString("EncryptionAlgorithm", value.getEncryptionAlgorithm());
    }
  }
}
