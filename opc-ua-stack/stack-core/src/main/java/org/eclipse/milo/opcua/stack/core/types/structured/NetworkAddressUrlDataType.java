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
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.7/#6.2.7.5.4">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.7/#6.2.7.5.4</a>
 */
public class NetworkAddressUrlDataType extends NetworkAddressDataType implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=15510");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=21152");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=21176");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=21200");

  private final @Nullable String url;

  public NetworkAddressUrlDataType(@Nullable String networkInterface, @Nullable String url) {
    super(networkInterface);
    this.url = url;
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

  public @Nullable String getUrl() {
    return url;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    NetworkAddressUrlDataType that = (NetworkAddressUrlDataType) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getUrl(), that.getUrl());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getUrl());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", NetworkAddressUrlDataType.class.getSimpleName() + "[", "]");
    joiner.add("url='" + getUrl() + "'");
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 21152),
        new NodeId(0, 15502),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "NetworkInterface",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Url",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<NetworkAddressUrlDataType> {
    @Override
    public Class<NetworkAddressUrlDataType> getType() {
      return NetworkAddressUrlDataType.class;
    }

    @Override
    public NetworkAddressUrlDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final String networkInterface;
      final String url;
      networkInterface = decoder.decodeString("NetworkInterface");
      url = decoder.decodeString("Url");
      return new NetworkAddressUrlDataType(networkInterface, url);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, NetworkAddressUrlDataType value) {
      encoder.encodeString("NetworkInterface", value.getNetworkInterface());
      encoder.encodeString("Url", value.getUrl());
    }
  }
}
