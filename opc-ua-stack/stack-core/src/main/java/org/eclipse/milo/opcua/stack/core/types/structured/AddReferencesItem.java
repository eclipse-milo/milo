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
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.2">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.2</a>
 */
public class AddReferencesItem extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=379");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=381");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=380");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15169");

  private final NodeId sourceNodeId;

  private final NodeId referenceTypeId;

  private final Boolean isForward;

  private final @Nullable String targetServerUri;

  private final ExpandedNodeId targetNodeId;

  private final NodeClass targetNodeClass;

  public AddReferencesItem(
      NodeId sourceNodeId,
      NodeId referenceTypeId,
      Boolean isForward,
      @Nullable String targetServerUri,
      ExpandedNodeId targetNodeId,
      NodeClass targetNodeClass) {
    this.sourceNodeId = sourceNodeId;
    this.referenceTypeId = referenceTypeId;
    this.isForward = isForward;
    this.targetServerUri = targetServerUri;
    this.targetNodeId = targetNodeId;
    this.targetNodeClass = targetNodeClass;
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

  public NodeId getSourceNodeId() {
    return sourceNodeId;
  }

  public NodeId getReferenceTypeId() {
    return referenceTypeId;
  }

  public Boolean getIsForward() {
    return isForward;
  }

  public @Nullable String getTargetServerUri() {
    return targetServerUri;
  }

  public ExpandedNodeId getTargetNodeId() {
    return targetNodeId;
  }

  public NodeClass getTargetNodeClass() {
    return targetNodeClass;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    AddReferencesItem that = (AddReferencesItem) object;
    var eqb = new EqualsBuilder();
    eqb.append(getSourceNodeId(), that.getSourceNodeId());
    eqb.append(getReferenceTypeId(), that.getReferenceTypeId());
    eqb.append(getIsForward(), that.getIsForward());
    eqb.append(getTargetServerUri(), that.getTargetServerUri());
    eqb.append(getTargetNodeId(), that.getTargetNodeId());
    eqb.append(getTargetNodeClass(), that.getTargetNodeClass());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getSourceNodeId());
    hcb.append(getReferenceTypeId());
    hcb.append(getIsForward());
    hcb.append(getTargetServerUri());
    hcb.append(getTargetNodeId());
    hcb.append(getTargetNodeClass());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", AddReferencesItem.class.getSimpleName() + "[", "]");
    joiner.add("sourceNodeId=" + getSourceNodeId());
    joiner.add("referenceTypeId=" + getReferenceTypeId());
    joiner.add("isForward=" + getIsForward());
    joiner.add("targetServerUri='" + getTargetServerUri() + "'");
    joiner.add("targetNodeId=" + getTargetNodeId());
    joiner.add("targetNodeClass=" + getTargetNodeClass());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 381),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "SourceNodeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ReferenceTypeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "IsForward",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TargetServerUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TargetNodeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 18),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TargetNodeClass",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 257),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<AddReferencesItem> {
    @Override
    public Class<AddReferencesItem> getType() {
      return AddReferencesItem.class;
    }

    @Override
    public AddReferencesItem decodeType(EncodingContext context, UaDecoder decoder) {
      NodeId sourceNodeId = decoder.decodeNodeId("SourceNodeId");
      NodeId referenceTypeId = decoder.decodeNodeId("ReferenceTypeId");
      Boolean isForward = decoder.decodeBoolean("IsForward");
      String targetServerUri = decoder.decodeString("TargetServerUri");
      ExpandedNodeId targetNodeId = decoder.decodeExpandedNodeId("TargetNodeId");
      NodeClass targetNodeClass = NodeClass.from(decoder.decodeEnum("TargetNodeClass"));
      return new AddReferencesItem(
          sourceNodeId, referenceTypeId, isForward, targetServerUri, targetNodeId, targetNodeClass);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, AddReferencesItem value) {
      encoder.encodeNodeId("SourceNodeId", value.getSourceNodeId());
      encoder.encodeNodeId("ReferenceTypeId", value.getReferenceTypeId());
      encoder.encodeBoolean("IsForward", value.getIsForward());
      encoder.encodeString("TargetServerUri", value.getTargetServerUri());
      encoder.encodeExpandedNodeId("TargetNodeId", value.getTargetNodeId());
      encoder.encodeEnum("TargetNodeClass", value.getTargetNodeClass());
    }
  }
}
