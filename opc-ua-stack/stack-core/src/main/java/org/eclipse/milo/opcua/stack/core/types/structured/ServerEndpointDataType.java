/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.23">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.23</a>
 */
public class ServerEndpointDataType extends EndpointDataType implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=15558");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=16545");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=16594");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=16643");

  private final String @Nullable [] endpointUrls;

  private final String @Nullable [] securitySettingNames;

  private final String transportProfileUri;

  private final String @Nullable [] userTokenSettingNames;

  private final String @Nullable [] reverseConnectUrls;

  public ServerEndpointDataType(
      @Nullable String name,
      KeyValuePair @Nullable [] recordProperties,
      String @Nullable [] discoveryUrls,
      @Nullable String networkName,
      UShort port,
      String @Nullable [] endpointUrls,
      String @Nullable [] securitySettingNames,
      String transportProfileUri,
      String @Nullable [] userTokenSettingNames,
      String @Nullable [] reverseConnectUrls) {
    super(name, recordProperties, discoveryUrls, networkName, port);
    this.endpointUrls = endpointUrls;
    this.securitySettingNames = securitySettingNames;
    this.transportProfileUri = transportProfileUri;
    this.userTokenSettingNames = userTokenSettingNames;
    this.reverseConnectUrls = reverseConnectUrls;
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

  public String @Nullable [] getEndpointUrls() {
    return endpointUrls;
  }

  public String @Nullable [] getSecuritySettingNames() {
    return securitySettingNames;
  }

  public String getTransportProfileUri() {
    return transportProfileUri;
  }

  public String @Nullable [] getUserTokenSettingNames() {
    return userTokenSettingNames;
  }

  public String @Nullable [] getReverseConnectUrls() {
    return reverseConnectUrls;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ServerEndpointDataType that = (ServerEndpointDataType) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getEndpointUrls(), that.getEndpointUrls());
    eqb.append(getSecuritySettingNames(), that.getSecuritySettingNames());
    eqb.append(getTransportProfileUri(), that.getTransportProfileUri());
    eqb.append(getUserTokenSettingNames(), that.getUserTokenSettingNames());
    eqb.append(getReverseConnectUrls(), that.getReverseConnectUrls());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getEndpointUrls());
    hcb.append(getSecuritySettingNames());
    hcb.append(getTransportProfileUri());
    hcb.append(getUserTokenSettingNames());
    hcb.append(getReverseConnectUrls());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ServerEndpointDataType.class.getSimpleName() + "[", "]");
    joiner.add("endpointUrls=" + java.util.Arrays.toString(getEndpointUrls()));
    joiner.add("securitySettingNames=" + java.util.Arrays.toString(getSecuritySettingNames()));
    joiner.add("transportProfileUri='" + getTransportProfileUri() + "'");
    joiner.add("userTokenSettingNames=" + java.util.Arrays.toString(getUserTokenSettingNames()));
    joiner.add("reverseConnectUrls=" + java.util.Arrays.toString(getReverseConnectUrls()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 16545),
        new NodeId(0, 15557),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Name",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RecordProperties",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14533),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DiscoveryUrls",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 23751),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "NetworkName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Port",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 5),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "EndpointUrls",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 23751),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SecuritySettingNames",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TransportProfileUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 23751),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UserTokenSettingNames",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ReverseConnectUrls",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ServerEndpointDataType> {
    @Override
    public Class<ServerEndpointDataType> getType() {
      return ServerEndpointDataType.class;
    }

    @Override
    public ServerEndpointDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final String name;
      final KeyValuePair[] recordProperties;
      final String[] discoveryUrls;
      final String networkName;
      final UShort port;
      final String[] endpointUrls;
      final String[] securitySettingNames;
      final String transportProfileUri;
      final String[] userTokenSettingNames;
      final String[] reverseConnectUrls;
      name = decoder.decodeString("Name");
      recordProperties =
          (KeyValuePair[]) decoder.decodeStructArray("RecordProperties", KeyValuePair.TYPE_ID);
      discoveryUrls = decoder.decodeStringArray("DiscoveryUrls");
      networkName = decoder.decodeString("NetworkName");
      port = decoder.decodeUInt16("Port");
      endpointUrls = decoder.decodeStringArray("EndpointUrls");
      securitySettingNames = decoder.decodeStringArray("SecuritySettingNames");
      transportProfileUri = decoder.decodeString("TransportProfileUri");
      userTokenSettingNames = decoder.decodeStringArray("UserTokenSettingNames");
      reverseConnectUrls = decoder.decodeStringArray("ReverseConnectUrls");
      return new ServerEndpointDataType(
          name,
          recordProperties,
          discoveryUrls,
          networkName,
          port,
          endpointUrls,
          securitySettingNames,
          transportProfileUri,
          userTokenSettingNames,
          reverseConnectUrls);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, ServerEndpointDataType value) {
      encoder.encodeString("Name", value.getName());
      encoder.encodeStructArray(
          "RecordProperties", value.getRecordProperties(), KeyValuePair.TYPE_ID);
      encoder.encodeStringArray("DiscoveryUrls", value.getDiscoveryUrls());
      encoder.encodeString("NetworkName", value.getNetworkName());
      encoder.encodeUInt16("Port", value.getPort());
      encoder.encodeStringArray("EndpointUrls", value.getEndpointUrls());
      encoder.encodeStringArray("SecuritySettingNames", value.getSecuritySettingNames());
      encoder.encodeString("TransportProfileUri", value.getTransportProfileUri());
      encoder.encodeStringArray("UserTokenSettingNames", value.getUserTokenSettingNames());
      encoder.encodeStringArray("ReverseConnectUrls", value.getReverseConnectUrls());
    }
  }
}
