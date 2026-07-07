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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.22">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.22</a>
 */
public class EndpointDataType extends BaseConfigurationRecordDataType implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=15557");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=16544");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=16593");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=16642");

  private final String @Nullable [] discoveryUrls;

  private final @Nullable String networkName;

  private final UShort port;

  public EndpointDataType(
      @Nullable String name,
      KeyValuePair @Nullable [] recordProperties,
      String @Nullable [] discoveryUrls,
      @Nullable String networkName,
      UShort port) {
    super(name, recordProperties);
    this.discoveryUrls = discoveryUrls;
    this.networkName = networkName;
    this.port = port;
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

  public String @Nullable [] getDiscoveryUrls() {
    return discoveryUrls;
  }

  public @Nullable String getNetworkName() {
    return networkName;
  }

  public UShort getPort() {
    return port;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    EndpointDataType that = (EndpointDataType) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getDiscoveryUrls(), that.getDiscoveryUrls());
    eqb.append(getNetworkName(), that.getNetworkName());
    eqb.append(getPort(), that.getPort());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getDiscoveryUrls());
    hcb.append(getNetworkName());
    hcb.append(getPort());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", EndpointDataType.class.getSimpleName() + "[", "]");
    joiner.add("discoveryUrls=" + java.util.Arrays.toString(getDiscoveryUrls()));
    joiner.add("networkName='" + getNetworkName() + "'");
    joiner.add("port=" + getPort());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 16544),
        new NodeId(0, 15435),
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
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<EndpointDataType> {
    @Override
    public Class<EndpointDataType> getType() {
      return EndpointDataType.class;
    }

    @Override
    public EndpointDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final String name;
      final KeyValuePair[] recordProperties;
      final String[] discoveryUrls;
      final String networkName;
      final UShort port;
      name = decoder.decodeString("Name");
      recordProperties =
          (KeyValuePair[]) decoder.decodeStructArray("RecordProperties", KeyValuePair.TYPE_ID);
      discoveryUrls = decoder.decodeStringArray("DiscoveryUrls");
      networkName = decoder.decodeString("NetworkName");
      port = decoder.decodeUInt16("Port");
      return new EndpointDataType(name, recordProperties, discoveryUrls, networkName, port);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, EndpointDataType value) {
      encoder.encodeString("Name", value.getName());
      encoder.encodeStructArray(
          "RecordProperties", value.getRecordProperties(), KeyValuePair.TYPE_ID);
      encoder.encodeStringArray("DiscoveryUrls", value.getDiscoveryUrls());
      encoder.encodeString("NetworkName", value.getNetworkName());
      encoder.encodeUInt16("Port", value.getPort());
    }
  }
}
