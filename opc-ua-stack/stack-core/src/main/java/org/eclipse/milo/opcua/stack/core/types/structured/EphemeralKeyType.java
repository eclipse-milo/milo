/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.15">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.15</a>
 */
public class EphemeralKeyType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=17548");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=17549");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=17553");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=17557");

  private final ByteString publicKey;

  private final ByteString signature;

  public EphemeralKeyType(ByteString publicKey, ByteString signature) {
    this.publicKey = publicKey;
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

  public ByteString getPublicKey() {
    return publicKey;
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
    EphemeralKeyType that = (EphemeralKeyType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getPublicKey(), that.getPublicKey());
    eqb.append(getSignature(), that.getSignature());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getPublicKey());
    hcb.append(getSignature());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", EphemeralKeyType.class.getSimpleName() + "[", "]");
    joiner.add("publicKey=" + getPublicKey());
    joiner.add("signature=" + getSignature());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 17549),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "PublicKey",
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

  public static final class Codec extends GenericDataTypeCodec<EphemeralKeyType> {
    @Override
    public Class<EphemeralKeyType> getType() {
      return EphemeralKeyType.class;
    }

    @Override
    public EphemeralKeyType decodeType(EncodingContext context, UaDecoder decoder) {
      ByteString publicKey = decoder.decodeByteString("PublicKey");
      ByteString signature = decoder.decodeByteString("Signature");
      return new EphemeralKeyType(publicKey, signature);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, EphemeralKeyType value) {
      encoder.encodeByteString("PublicKey", value.getPublicKey());
      encoder.encodeByteString("Signature", value.getSignature());
    }
  }
}
