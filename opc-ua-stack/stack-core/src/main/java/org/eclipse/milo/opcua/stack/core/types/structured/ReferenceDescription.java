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
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.NodeClass;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.30">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.30</a>
 */
public class ReferenceDescription extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=518");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=520");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=519");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15182");

  private final NodeId referenceTypeId;

  private final Boolean isForward;

  private final ExpandedNodeId nodeId;

  private final QualifiedName browseName;

  private final LocalizedText displayName;

  private final NodeClass nodeClass;

  private final ExpandedNodeId typeDefinition;

  public ReferenceDescription(
      NodeId referenceTypeId,
      Boolean isForward,
      ExpandedNodeId nodeId,
      QualifiedName browseName,
      LocalizedText displayName,
      NodeClass nodeClass,
      ExpandedNodeId typeDefinition) {
    this.referenceTypeId = referenceTypeId;
    this.isForward = isForward;
    this.nodeId = nodeId;
    this.browseName = browseName;
    this.displayName = displayName;
    this.nodeClass = nodeClass;
    this.typeDefinition = typeDefinition;
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

  public NodeId getReferenceTypeId() {
    return referenceTypeId;
  }

  public Boolean getIsForward() {
    return isForward;
  }

  public ExpandedNodeId getNodeId() {
    return nodeId;
  }

  public QualifiedName getBrowseName() {
    return browseName;
  }

  public LocalizedText getDisplayName() {
    return displayName;
  }

  public NodeClass getNodeClass() {
    return nodeClass;
  }

  public ExpandedNodeId getTypeDefinition() {
    return typeDefinition;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ReferenceDescription that = (ReferenceDescription) object;
    var eqb = new EqualsBuilder();
    eqb.append(getReferenceTypeId(), that.getReferenceTypeId());
    eqb.append(getIsForward(), that.getIsForward());
    eqb.append(getNodeId(), that.getNodeId());
    eqb.append(getBrowseName(), that.getBrowseName());
    eqb.append(getDisplayName(), that.getDisplayName());
    eqb.append(getNodeClass(), that.getNodeClass());
    eqb.append(getTypeDefinition(), that.getTypeDefinition());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getReferenceTypeId());
    hcb.append(getIsForward());
    hcb.append(getNodeId());
    hcb.append(getBrowseName());
    hcb.append(getDisplayName());
    hcb.append(getNodeClass());
    hcb.append(getTypeDefinition());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ReferenceDescription.class.getSimpleName() + "[", "]");
    joiner.add("referenceTypeId=" + getReferenceTypeId());
    joiner.add("isForward=" + getIsForward());
    joiner.add("nodeId=" + getNodeId());
    joiner.add("browseName=" + getBrowseName());
    joiner.add("displayName=" + getDisplayName());
    joiner.add("nodeClass=" + getNodeClass());
    joiner.add("typeDefinition=" + getTypeDefinition());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 520),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
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
              "NodeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 18),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "BrowseName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 20),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DisplayName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 21),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "NodeClass",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 257),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "TypeDefinition",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 18),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ReferenceDescription> {
    @Override
    public Class<ReferenceDescription> getType() {
      return ReferenceDescription.class;
    }

    @Override
    public ReferenceDescription decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId referenceTypeId;
      final Boolean isForward;
      final ExpandedNodeId nodeId;
      final QualifiedName browseName;
      final LocalizedText displayName;
      final NodeClass nodeClass;
      final ExpandedNodeId typeDefinition;
      referenceTypeId = decoder.decodeNodeId("ReferenceTypeId");
      isForward = decoder.decodeBoolean("IsForward");
      nodeId = decoder.decodeExpandedNodeId("NodeId");
      browseName = decoder.decodeQualifiedName("BrowseName");
      displayName = decoder.decodeLocalizedText("DisplayName");
      nodeClass = NodeClass.from(decoder.decodeEnum("NodeClass"));
      typeDefinition = decoder.decodeExpandedNodeId("TypeDefinition");
      return new ReferenceDescription(
          referenceTypeId, isForward, nodeId, browseName, displayName, nodeClass, typeDefinition);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, ReferenceDescription value) {
      encoder.encodeNodeId("ReferenceTypeId", value.getReferenceTypeId());
      encoder.encodeBoolean("IsForward", value.getIsForward());
      encoder.encodeExpandedNodeId("NodeId", value.getNodeId());
      encoder.encodeQualifiedName("BrowseName", value.getBrowseName());
      encoder.encodeLocalizedText("DisplayName", value.getDisplayName());
      encoder.encodeEnum("NodeClass", value.getNodeClass());
      encoder.encodeExpandedNodeId("TypeDefinition", value.getTypeDefinition());
    }
  }
}
