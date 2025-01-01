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
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.1">https://reference.opcfoundation.org/v105/Core/docs/Part5/12.3.1</a>
 */
public class AddNodesItem extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=376");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=378");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=377");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15165");

  private final ExpandedNodeId parentNodeId;

  private final NodeId referenceTypeId;

  private final ExpandedNodeId requestedNewNodeId;

  private final QualifiedName browseName;

  private final NodeClass nodeClass;

  private final ExtensionObject nodeAttributes;

  private final ExpandedNodeId typeDefinition;

  public AddNodesItem(
      ExpandedNodeId parentNodeId,
      NodeId referenceTypeId,
      ExpandedNodeId requestedNewNodeId,
      QualifiedName browseName,
      NodeClass nodeClass,
      ExtensionObject nodeAttributes,
      ExpandedNodeId typeDefinition) {
    this.parentNodeId = parentNodeId;
    this.referenceTypeId = referenceTypeId;
    this.requestedNewNodeId = requestedNewNodeId;
    this.browseName = browseName;
    this.nodeClass = nodeClass;
    this.nodeAttributes = nodeAttributes;
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

  public ExpandedNodeId getParentNodeId() {
    return parentNodeId;
  }

  public NodeId getReferenceTypeId() {
    return referenceTypeId;
  }

  public ExpandedNodeId getRequestedNewNodeId() {
    return requestedNewNodeId;
  }

  public QualifiedName getBrowseName() {
    return browseName;
  }

  public NodeClass getNodeClass() {
    return nodeClass;
  }

  public ExtensionObject getNodeAttributes() {
    return nodeAttributes;
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
    AddNodesItem that = (AddNodesItem) object;
    var eqb = new EqualsBuilder();
    eqb.append(getParentNodeId(), that.getParentNodeId());
    eqb.append(getReferenceTypeId(), that.getReferenceTypeId());
    eqb.append(getRequestedNewNodeId(), that.getRequestedNewNodeId());
    eqb.append(getBrowseName(), that.getBrowseName());
    eqb.append(getNodeClass(), that.getNodeClass());
    eqb.append(getNodeAttributes(), that.getNodeAttributes());
    eqb.append(getTypeDefinition(), that.getTypeDefinition());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getParentNodeId());
    hcb.append(getReferenceTypeId());
    hcb.append(getRequestedNewNodeId());
    hcb.append(getBrowseName());
    hcb.append(getNodeClass());
    hcb.append(getNodeAttributes());
    hcb.append(getTypeDefinition());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", AddNodesItem.class.getSimpleName() + "[", "]");
    joiner.add("parentNodeId=" + getParentNodeId());
    joiner.add("referenceTypeId=" + getReferenceTypeId());
    joiner.add("requestedNewNodeId=" + getRequestedNewNodeId());
    joiner.add("browseName=" + getBrowseName());
    joiner.add("nodeClass=" + getNodeClass());
    joiner.add("nodeAttributes=" + getNodeAttributes());
    joiner.add("typeDefinition=" + getTypeDefinition());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 378),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ParentNodeId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 18),
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
              "RequestedNewNodeId",
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
              "NodeClass",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 257),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "NodeAttributes",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 22),
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

  public static final class Codec extends GenericDataTypeCodec<AddNodesItem> {
    @Override
    public Class<AddNodesItem> getType() {
      return AddNodesItem.class;
    }

    @Override
    public AddNodesItem decodeType(EncodingContext context, UaDecoder decoder) {
      ExpandedNodeId parentNodeId = decoder.decodeExpandedNodeId("ParentNodeId");
      NodeId referenceTypeId = decoder.decodeNodeId("ReferenceTypeId");
      ExpandedNodeId requestedNewNodeId = decoder.decodeExpandedNodeId("RequestedNewNodeId");
      QualifiedName browseName = decoder.decodeQualifiedName("BrowseName");
      NodeClass nodeClass = NodeClass.from(decoder.decodeEnum("NodeClass"));
      ExtensionObject nodeAttributes = decoder.decodeExtensionObject("NodeAttributes");
      ExpandedNodeId typeDefinition = decoder.decodeExpandedNodeId("TypeDefinition");
      return new AddNodesItem(
          parentNodeId,
          referenceTypeId,
          requestedNewNodeId,
          browseName,
          nodeClass,
          nodeAttributes,
          typeDefinition);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, AddNodesItem value) {
      encoder.encodeExpandedNodeId("ParentNodeId", value.getParentNodeId());
      encoder.encodeNodeId("ReferenceTypeId", value.getReferenceTypeId());
      encoder.encodeExpandedNodeId("RequestedNewNodeId", value.getRequestedNewNodeId());
      encoder.encodeQualifiedName("BrowseName", value.getBrowseName());
      encoder.encodeEnum("NodeClass", value.getNodeClass());
      encoder.encodeExtensionObject("NodeAttributes", value.getNodeAttributes());
      encoder.encodeExpandedNodeId("TypeDefinition", value.getTypeDefinition());
    }
  }
}
