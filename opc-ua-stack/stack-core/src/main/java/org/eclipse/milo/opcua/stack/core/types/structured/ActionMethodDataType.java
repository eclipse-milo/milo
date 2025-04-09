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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.10.5">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.10.5</a>
 */
public class ActionMethodDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=18597");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=18600");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=18612");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=18624");

  private final NodeId objectId;

  private final NodeId methodId;

  public ActionMethodDataType(NodeId objectId, NodeId methodId) {
    this.objectId = objectId;
    this.methodId = methodId;
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

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ActionMethodDataType that = (ActionMethodDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getObjectId(), that.getObjectId());
    eqb.append(getMethodId(), that.getMethodId());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getObjectId());
    hcb.append(getMethodId());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ActionMethodDataType.class.getSimpleName() + "[", "]");
    joiner.add("objectId=" + getObjectId());
    joiner.add("methodId=" + getMethodId());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 18600),
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
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ActionMethodDataType> {
    @Override
    public Class<ActionMethodDataType> getType() {
      return ActionMethodDataType.class;
    }

    @Override
    public ActionMethodDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId objectId;
      final NodeId methodId;
      objectId = decoder.decodeNodeId("ObjectId");
      methodId = decoder.decodeNodeId("MethodId");
      return new ActionMethodDataType(objectId, methodId);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, ActionMethodDataType value) {
      encoder.encodeNodeId("ObjectId", value.getObjectId());
      encoder.encodeNodeId("MethodId", value.getMethodId());
    }
  }
}
