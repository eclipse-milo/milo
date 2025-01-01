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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.2/#6.4.2.2.3">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.4.2/#6.4.2.2.3</a>
 */
public class BrokerConnectionTransportDataType extends ConnectionTransportDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=15007");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=15479");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=15579");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15726");

  private final @Nullable String resourceUri;

  private final @Nullable String authenticationProfileUri;

  public BrokerConnectionTransportDataType(
      @Nullable String resourceUri, @Nullable String authenticationProfileUri) {
    this.resourceUri = resourceUri;
    this.authenticationProfileUri = authenticationProfileUri;
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

  public @Nullable String getResourceUri() {
    return resourceUri;
  }

  public @Nullable String getAuthenticationProfileUri() {
    return authenticationProfileUri;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    BrokerConnectionTransportDataType that = (BrokerConnectionTransportDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getResourceUri(), that.getResourceUri());
    eqb.append(getAuthenticationProfileUri(), that.getAuthenticationProfileUri());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getResourceUri());
    hcb.append(getAuthenticationProfileUri());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", BrokerConnectionTransportDataType.class.getSimpleName() + "[", "]");
    joiner.add("resourceUri='" + getResourceUri() + "'");
    joiner.add("authenticationProfileUri='" + getAuthenticationProfileUri() + "'");
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 15479),
        new NodeId(0, 15618),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ResourceUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AuthenticationProfileUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<BrokerConnectionTransportDataType> {
    @Override
    public Class<BrokerConnectionTransportDataType> getType() {
      return BrokerConnectionTransportDataType.class;
    }

    @Override
    public BrokerConnectionTransportDataType decodeType(
        EncodingContext context, UaDecoder decoder) {
      String resourceUri = decoder.decodeString("ResourceUri");
      String authenticationProfileUri = decoder.decodeString("AuthenticationProfileUri");
      return new BrokerConnectionTransportDataType(resourceUri, authenticationProfileUri);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, BrokerConnectionTransportDataType value) {
      encoder.encodeString("ResourceUri", value.getResourceUri());
      encoder.encodeString("AuthenticationProfileUri", value.getAuthenticationProfileUri());
    }
  }
}
