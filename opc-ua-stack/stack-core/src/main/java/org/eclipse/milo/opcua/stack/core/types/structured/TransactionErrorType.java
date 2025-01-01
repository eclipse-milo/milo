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
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.12">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.10.12</a>
 */
public class TransactionErrorType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=32285");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=32382");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=32386");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=32390");

  private final NodeId targetId;

  private final StatusCode error;

  private final LocalizedText message;

  public TransactionErrorType(NodeId targetId, StatusCode error, LocalizedText message) {
    this.targetId = targetId;
    this.error = error;
    this.message = message;
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

  public NodeId getTargetId() {
    return targetId;
  }

  public StatusCode getError() {
    return error;
  }

  public LocalizedText getMessage() {
    return message;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    TransactionErrorType that = (TransactionErrorType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getTargetId(), that.getTargetId());
    eqb.append(getError(), that.getError());
    eqb.append(getMessage(), that.getMessage());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getTargetId());
    hcb.append(getError());
    hcb.append(getMessage());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", TransactionErrorType.class.getSimpleName() + "[", "]");
    joiner.add("targetId=" + getTargetId());
    joiner.add("error=" + getError());
    joiner.add("message=" + getMessage());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 32382),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "TargetId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Error",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 19),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Message",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 21),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<TransactionErrorType> {
    @Override
    public Class<TransactionErrorType> getType() {
      return TransactionErrorType.class;
    }

    @Override
    public TransactionErrorType decodeType(EncodingContext context, UaDecoder decoder) {
      NodeId targetId = decoder.decodeNodeId("TargetId");
      StatusCode error = decoder.decodeStatusCode("Error");
      LocalizedText message = decoder.decodeLocalizedText("Message");
      return new TransactionErrorType(targetId, error, message);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, TransactionErrorType value) {
      encoder.encodeNodeId("TargetId", value.getTargetId());
      encoder.encodeStatusCode("Error", value.getError());
      encoder.encodeLocalizedText("Message", value.getMessage());
    }
  }
}
