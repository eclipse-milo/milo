/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.2">https://reference.opcfoundation.org/v105/Core/docs/Part18/4.4.2</a>
 */
public class EndpointType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=15528");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=15671");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=15949");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=16150");

  private final @Nullable String endpointUrl;

  private final MessageSecurityMode securityMode;

  private final @Nullable String securityPolicyUri;

  private final @Nullable String transportProfileUri;

  public EndpointType(
      @Nullable String endpointUrl,
      MessageSecurityMode securityMode,
      @Nullable String securityPolicyUri,
      @Nullable String transportProfileUri) {
    this.endpointUrl = endpointUrl;
    this.securityMode = securityMode;
    this.securityPolicyUri = securityPolicyUri;
    this.transportProfileUri = transportProfileUri;
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

  public @Nullable String getEndpointUrl() {
    return endpointUrl;
  }

  public MessageSecurityMode getSecurityMode() {
    return securityMode;
  }

  public @Nullable String getSecurityPolicyUri() {
    return securityPolicyUri;
  }

  public @Nullable String getTransportProfileUri() {
    return transportProfileUri;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    EndpointType that = (EndpointType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getEndpointUrl(), that.getEndpointUrl());
    eqb.append(getSecurityMode(), that.getSecurityMode());
    eqb.append(getSecurityPolicyUri(), that.getSecurityPolicyUri());
    eqb.append(getTransportProfileUri(), that.getTransportProfileUri());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getEndpointUrl());
    hcb.append(getSecurityMode());
    hcb.append(getSecurityPolicyUri());
    hcb.append(getTransportProfileUri());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", EndpointType.class.getSimpleName() + "[", "]");
    joiner.add("endpointUrl='" + getEndpointUrl() + "'");
    joiner.add("securityMode=" + getSecurityMode());
    joiner.add("securityPolicyUri='" + getSecurityPolicyUri() + "'");
    joiner.add("transportProfileUri='" + getTransportProfileUri() + "'");
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 15671),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "EndpointUrl",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SecurityMode",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 302),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SecurityPolicyUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TransportProfileUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<EndpointType> {
    @Override
    public Class<EndpointType> getType() {
      return EndpointType.class;
    }

    @Override
    public EndpointType decodeType(EncodingContext context, UaDecoder decoder) {
      final String endpointUrl;
      final MessageSecurityMode securityMode;
      final String securityPolicyUri;
      final String transportProfileUri;
      endpointUrl = decoder.decodeString("EndpointUrl");
      securityMode = MessageSecurityMode.from(decoder.decodeEnum("SecurityMode"));
      securityPolicyUri = decoder.decodeString("SecurityPolicyUri");
      transportProfileUri = decoder.decodeString("TransportProfileUri");
      return new EndpointType(endpointUrl, securityMode, securityPolicyUri, transportProfileUri);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, EndpointType value) {
      encoder.encodeString("EndpointUrl", value.getEndpointUrl());
      encoder.encodeEnum("SecurityMode", value.getSecurityMode());
      encoder.encodeString("SecurityPolicyUri", value.getSecurityPolicyUri());
      encoder.encodeString("TransportProfileUri", value.getTransportProfileUri());
    }
  }
}
