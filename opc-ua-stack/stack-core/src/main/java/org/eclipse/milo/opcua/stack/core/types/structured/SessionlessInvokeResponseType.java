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

public class SessionlessInvokeResponseType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=20999");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=21001");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=21000");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15092");

  private final String @Nullable [] namespaceUris;

  private final String @Nullable [] serverUris;

  private final UInteger serviceId;

  public SessionlessInvokeResponseType(
      String @Nullable [] namespaceUris, String @Nullable [] serverUris, UInteger serviceId) {
    this.namespaceUris = namespaceUris;
    this.serverUris = serverUris;
    this.serviceId = serviceId;
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

  public String @Nullable [] getNamespaceUris() {
    return namespaceUris;
  }

  public String @Nullable [] getServerUris() {
    return serverUris;
  }

  public UInteger getServiceId() {
    return serviceId;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    SessionlessInvokeResponseType that = (SessionlessInvokeResponseType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getNamespaceUris(), that.getNamespaceUris());
    eqb.append(getServerUris(), that.getServerUris());
    eqb.append(getServiceId(), that.getServiceId());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getNamespaceUris());
    hcb.append(getServerUris());
    hcb.append(getServiceId());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", SessionlessInvokeResponseType.class.getSimpleName() + "[", "]");
    joiner.add("namespaceUris=" + java.util.Arrays.toString(getNamespaceUris()));
    joiner.add("serverUris=" + java.util.Arrays.toString(getServerUris()));
    joiner.add("serviceId=" + getServiceId());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 21001),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "NamespaceUris",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServerUris",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ServiceId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<SessionlessInvokeResponseType> {
    @Override
    public Class<SessionlessInvokeResponseType> getType() {
      return SessionlessInvokeResponseType.class;
    }

    @Override
    public SessionlessInvokeResponseType decodeType(EncodingContext context, UaDecoder decoder) {
      final String[] namespaceUris;
      final String[] serverUris;
      final UInteger serviceId;
      namespaceUris = decoder.decodeStringArray("NamespaceUris");
      serverUris = decoder.decodeStringArray("ServerUris");
      serviceId = decoder.decodeUInt32("ServiceId");
      return new SessionlessInvokeResponseType(namespaceUris, serverUris, serviceId);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, SessionlessInvokeResponseType value) {
      encoder.encodeStringArray("NamespaceUris", value.getNamespaceUris());
      encoder.encodeStringArray("ServerUris", value.getServerUris());
      encoder.encodeUInt32("ServiceId", value.getServiceId());
    }
  }
}
