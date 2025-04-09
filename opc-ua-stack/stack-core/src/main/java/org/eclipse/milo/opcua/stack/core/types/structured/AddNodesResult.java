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
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.8.2/#5.8.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.8.2/#5.8.2.2</a>
 */
public class AddNodesResult extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=483");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=485");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=484");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15166");

  private final StatusCode statusCode;

  private final NodeId addedNodeId;

  public AddNodesResult(StatusCode statusCode, NodeId addedNodeId) {
    this.statusCode = statusCode;
    this.addedNodeId = addedNodeId;
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

  public StatusCode getStatusCode() {
    return statusCode;
  }

  public NodeId getAddedNodeId() {
    return addedNodeId;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    AddNodesResult that = (AddNodesResult) object;
    var eqb = new EqualsBuilder();
    eqb.append(getStatusCode(), that.getStatusCode());
    eqb.append(getAddedNodeId(), that.getAddedNodeId());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getStatusCode());
    hcb.append(getAddedNodeId());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", AddNodesResult.class.getSimpleName() + "[", "]");
    joiner.add("statusCode=" + getStatusCode());
    joiner.add("addedNodeId=" + getAddedNodeId());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 485),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "StatusCode",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 19),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AddedNodeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<AddNodesResult> {
    @Override
    public Class<AddNodesResult> getType() {
      return AddNodesResult.class;
    }

    @Override
    public AddNodesResult decodeType(EncodingContext context, UaDecoder decoder) {
      final StatusCode statusCode;
      final NodeId addedNodeId;
      statusCode = decoder.decodeStatusCode("StatusCode");
      addedNodeId = decoder.decodeNodeId("AddedNodeId");
      return new AddNodesResult(statusCode, addedNodeId);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, AddNodesResult value) {
      encoder.encodeStatusCode("StatusCode", value.getStatusCode());
      encoder.encodeNodeId("AddedNodeId", value.getAddedNodeId());
    }
  }
}
