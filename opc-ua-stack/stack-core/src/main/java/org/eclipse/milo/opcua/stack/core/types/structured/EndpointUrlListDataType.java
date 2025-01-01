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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.20">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.20</a>
 */
public class EndpointUrlListDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=11943");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=11957");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=11949");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15363");

  private final String @Nullable [] endpointUrlList;

  public EndpointUrlListDataType(String @Nullable [] endpointUrlList) {
    this.endpointUrlList = endpointUrlList;
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

  public String @Nullable [] getEndpointUrlList() {
    return endpointUrlList;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    EndpointUrlListDataType that = (EndpointUrlListDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getEndpointUrlList(), that.getEndpointUrlList());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getEndpointUrlList());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", EndpointUrlListDataType.class.getSimpleName() + "[", "]");
    joiner.add("endpointUrlList=" + java.util.Arrays.toString(getEndpointUrlList()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 11957),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "EndpointUrlList",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<EndpointUrlListDataType> {
    @Override
    public Class<EndpointUrlListDataType> getType() {
      return EndpointUrlListDataType.class;
    }

    @Override
    public EndpointUrlListDataType decodeType(EncodingContext context, UaDecoder decoder) {
      String[] endpointUrlList = decoder.decodeStringArray("EndpointUrlList");
      return new EndpointUrlListDataType(endpointUrlList);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, EndpointUrlListDataType value) {
      encoder.encodeStringArray("EndpointUrlList", value.getEndpointUrlList());
    }
  }
}
