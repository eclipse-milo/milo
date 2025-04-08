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
import org.eclipse.milo.opcua.stack.core.types.UaRequestMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.5.6/#5.5.6.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.5.6/#5.5.6.2</a>
 */
public class RegisterServer2Request extends Structure implements UaRequestMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=12193");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=12211");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=12199");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15107");

  private final RequestHeader requestHeader;

  private final RegisteredServer server;

  private final ExtensionObject @Nullable [] discoveryConfiguration;

  public RegisterServer2Request(
      RequestHeader requestHeader,
      RegisteredServer server,
      ExtensionObject @Nullable [] discoveryConfiguration) {
    this.requestHeader = requestHeader;
    this.server = server;
    this.discoveryConfiguration = discoveryConfiguration;
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

  public RequestHeader getRequestHeader() {
    return requestHeader;
  }

  public RegisteredServer getServer() {
    return server;
  }

  public ExtensionObject @Nullable [] getDiscoveryConfiguration() {
    return discoveryConfiguration;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    RegisterServer2Request that = (RegisterServer2Request) object;
    var eqb = new EqualsBuilder();
    eqb.append(getRequestHeader(), that.getRequestHeader());
    eqb.append(getServer(), that.getServer());
    eqb.append(getDiscoveryConfiguration(), that.getDiscoveryConfiguration());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getRequestHeader());
    hcb.append(getServer());
    hcb.append(getDiscoveryConfiguration());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", RegisterServer2Request.class.getSimpleName() + "[", "]");
    joiner.add("requestHeader=" + getRequestHeader());
    joiner.add("server=" + getServer());
    joiner.add("discoveryConfiguration=" + java.util.Arrays.toString(getDiscoveryConfiguration()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 12211),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "RequestHeader",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 389),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Server",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 432),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DiscoveryConfiguration",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 22),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<RegisterServer2Request> {
    @Override
    public Class<RegisterServer2Request> getType() {
      return RegisterServer2Request.class;
    }

    @Override
    public RegisterServer2Request decodeType(EncodingContext context, UaDecoder decoder) {
      final RequestHeader requestHeader;
      final RegisteredServer server;
      final ExtensionObject[] discoveryConfiguration;
      requestHeader = (RequestHeader) decoder.decodeStruct("RequestHeader", RequestHeader.TYPE_ID);
      server = (RegisteredServer) decoder.decodeStruct("Server", RegisteredServer.TYPE_ID);
      discoveryConfiguration = decoder.decodeExtensionObjectArray("DiscoveryConfiguration");
      return new RegisterServer2Request(requestHeader, server, discoveryConfiguration);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, RegisterServer2Request value) {
      encoder.encodeStruct("RequestHeader", value.getRequestHeader(), RequestHeader.TYPE_ID);
      encoder.encodeStruct("Server", value.getServer(), RegisteredServer.TYPE_ID);
      encoder.encodeExtensionObjectArray(
          "DiscoveryConfiguration", value.getDiscoveryConfiguration());
    }
  }
}
