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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.4/#5.9.4.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.9.4/#5.9.4.2</a>
 */
public class BrowsePath extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=543");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=545");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=544");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15190");

  private final NodeId startingNode;

  private final RelativePath relativePath;

  public BrowsePath(NodeId startingNode, RelativePath relativePath) {
    this.startingNode = startingNode;
    this.relativePath = relativePath;
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

  public NodeId getStartingNode() {
    return startingNode;
  }

  public RelativePath getRelativePath() {
    return relativePath;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    BrowsePath that = (BrowsePath) object;
    var eqb = new EqualsBuilder();
    eqb.append(getStartingNode(), that.getStartingNode());
    eqb.append(getRelativePath(), that.getRelativePath());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getStartingNode());
    hcb.append(getRelativePath());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", BrowsePath.class.getSimpleName() + "[", "]");
    joiner.add("startingNode=" + getStartingNode());
    joiner.add("relativePath=" + getRelativePath());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 545),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "StartingNode",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RelativePath",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 540),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<BrowsePath> {
    @Override
    public Class<BrowsePath> getType() {
      return BrowsePath.class;
    }

    @Override
    public BrowsePath decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId startingNode;
      final RelativePath relativePath;
      startingNode = decoder.decodeNodeId("StartingNode");
      relativePath = (RelativePath) decoder.decodeStruct("RelativePath", RelativePath.TYPE_ID);
      return new BrowsePath(startingNode, relativePath);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, BrowsePath value) {
      encoder.encodeNodeId("StartingNode", value.getStartingNode());
      encoder.encodeStruct("RelativePath", value.getRelativePath(), RelativePath.TYPE_ID);
    }
  }
}
