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
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.12.2/#5.12.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.12.2/#5.12.2.2</a>
 */
public class CallMethodRequest extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=704");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=706");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=705");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15289");

  private final NodeId objectId;

  private final NodeId methodId;

  private final Variant @Nullable [] inputArguments;

  public CallMethodRequest(NodeId objectId, NodeId methodId, Variant @Nullable [] inputArguments) {
    this.objectId = objectId;
    this.methodId = methodId;
    this.inputArguments = inputArguments;
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

  public NodeId getObjectId() {
    return objectId;
  }

  public NodeId getMethodId() {
    return methodId;
  }

  public Variant @Nullable [] getInputArguments() {
    return inputArguments;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    CallMethodRequest that = (CallMethodRequest) object;
    var eqb = new EqualsBuilder();
    eqb.append(getObjectId(), that.getObjectId());
    eqb.append(getMethodId(), that.getMethodId());
    eqb.append(getInputArguments(), that.getInputArguments());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getObjectId());
    hcb.append(getMethodId());
    hcb.append(getInputArguments());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", CallMethodRequest.class.getSimpleName() + "[", "]");
    joiner.add("objectId=" + getObjectId());
    joiner.add("methodId=" + getMethodId());
    joiner.add("inputArguments=" + java.util.Arrays.toString(getInputArguments()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 706),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ObjectId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MethodId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "InputArguments",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 24),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<CallMethodRequest> {
    @Override
    public Class<CallMethodRequest> getType() {
      return CallMethodRequest.class;
    }

    @Override
    public CallMethodRequest decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId objectId;
      final NodeId methodId;
      final Variant[] inputArguments;
      objectId = decoder.decodeNodeId("ObjectId");
      methodId = decoder.decodeNodeId("MethodId");
      inputArguments = decoder.decodeVariantArray("InputArguments");
      return new CallMethodRequest(objectId, methodId, inputArguments);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, CallMethodRequest value) {
      encoder.encodeNodeId("ObjectId", value.getObjectId());
      encoder.encodeNodeId("MethodId", value.getMethodId());
      encoder.encodeVariantArray("InputArguments", value.getInputArguments());
    }
  }
}
