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
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part17/7.2">https://reference.opcfoundation.org/v105/Core/docs/Part17/7.2</a>
 */
public class AliasNameDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=23468");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=23499");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=23505");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=23511");

  private final QualifiedName aliasName;

  private final ExpandedNodeId @Nullable [] referencedNodes;

  public AliasNameDataType(QualifiedName aliasName, ExpandedNodeId @Nullable [] referencedNodes) {
    this.aliasName = aliasName;
    this.referencedNodes = referencedNodes;
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

  public QualifiedName getAliasName() {
    return aliasName;
  }

  public ExpandedNodeId @Nullable [] getReferencedNodes() {
    return referencedNodes;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    AliasNameDataType that = (AliasNameDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getAliasName(), that.getAliasName());
    eqb.append(getReferencedNodes(), that.getReferencedNodes());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getAliasName());
    hcb.append(getReferencedNodes());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", AliasNameDataType.class.getSimpleName() + "[", "]");
    joiner.add("aliasName=" + getAliasName());
    joiner.add("referencedNodes=" + java.util.Arrays.toString(getReferencedNodes()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 23499),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "AliasName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 20),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ReferencedNodes",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 18),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<AliasNameDataType> {
    @Override
    public Class<AliasNameDataType> getType() {
      return AliasNameDataType.class;
    }

    @Override
    public AliasNameDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final QualifiedName aliasName;
      final ExpandedNodeId[] referencedNodes;
      aliasName = decoder.decodeQualifiedName("AliasName");
      referencedNodes = decoder.decodeExpandedNodeIdArray("ReferencedNodes");
      return new AliasNameDataType(aliasName, referencedNodes);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, AliasNameDataType value) {
      encoder.encodeQualifiedName("AliasName", value.getAliasName());
      encoder.encodeExpandedNodeIdArray("ReferencedNodes", value.getReferencedNodes());
    }
  }
}
