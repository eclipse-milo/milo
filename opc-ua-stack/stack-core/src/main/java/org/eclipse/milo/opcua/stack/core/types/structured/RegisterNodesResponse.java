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
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.8.5/#5.8.5.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.8.5/#5.8.5.2</a>
 */
public class RegisterNodesResponse extends Structure implements UaResponseMessageType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=561");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=563");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=562");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15196");

  private final ResponseHeader responseHeader;

  private final NodeId @Nullable [] registeredNodeIds;

  public RegisterNodesResponse(
      ResponseHeader responseHeader, NodeId @Nullable [] registeredNodeIds) {
    this.responseHeader = responseHeader;
    this.registeredNodeIds = registeredNodeIds;
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

  public ResponseHeader getResponseHeader() {
    return responseHeader;
  }

  public NodeId @Nullable [] getRegisteredNodeIds() {
    return registeredNodeIds;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    RegisterNodesResponse that = (RegisterNodesResponse) object;
    var eqb = new EqualsBuilder();
    eqb.append(getResponseHeader(), that.getResponseHeader());
    eqb.append(getRegisteredNodeIds(), that.getRegisteredNodeIds());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getResponseHeader());
    hcb.append(getRegisteredNodeIds());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", RegisterNodesResponse.class.getSimpleName() + "[", "]");
    joiner.add("responseHeader=" + getResponseHeader());
    joiner.add("registeredNodeIds=" + java.util.Arrays.toString(getRegisteredNodeIds()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 563),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ResponseHeader",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 392),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RegisteredNodeIds",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<RegisterNodesResponse> {
    @Override
    public Class<RegisterNodesResponse> getType() {
      return RegisterNodesResponse.class;
    }

    @Override
    public RegisterNodesResponse decodeType(EncodingContext context, UaDecoder decoder) {
      ResponseHeader responseHeader =
          (ResponseHeader) decoder.decodeStruct("ResponseHeader", ResponseHeader.TYPE_ID);
      NodeId[] registeredNodeIds = decoder.decodeNodeIdArray("RegisteredNodeIds");
      return new RegisterNodesResponse(responseHeader, registeredNodeIds);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, RegisterNodesResponse value) {
      encoder.encodeStruct("ResponseHeader", value.getResponseHeader(), ResponseHeader.TYPE_ID);
      encoder.encodeNodeIdArray("RegisteredNodeIds", value.getRegisteredNodeIds());
    }
  }
}
