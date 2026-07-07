/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part17/7.3">https://reference.opcfoundation.org/v105/Core/docs/Part17/7.3</a>
 */
public class AliasNameVerboseDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=24051");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=24262");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=24353");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=24369");

  private final QualifiedName aliasName;

  private final ExpandedNodeId @Nullable [] referencedNodes;

  private final String @Nullable [] serverUris;

  private final NodeId aliasNameCategoryId;

  public AliasNameVerboseDataType(
      QualifiedName aliasName,
      ExpandedNodeId @Nullable [] referencedNodes,
      String @Nullable [] serverUris,
      NodeId aliasNameCategoryId) {
    this.aliasName = aliasName;
    this.referencedNodes = referencedNodes;
    this.serverUris = serverUris;
    this.aliasNameCategoryId = aliasNameCategoryId;
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

  public String @Nullable [] getServerUris() {
    return serverUris;
  }

  public NodeId getAliasNameCategoryId() {
    return aliasNameCategoryId;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    AliasNameVerboseDataType that = (AliasNameVerboseDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getAliasName(), that.getAliasName());
    eqb.append(getReferencedNodes(), that.getReferencedNodes());
    eqb.append(getServerUris(), that.getServerUris());
    eqb.append(getAliasNameCategoryId(), that.getAliasNameCategoryId());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getAliasName());
    hcb.append(getReferencedNodes());
    hcb.append(getServerUris());
    hcb.append(getAliasNameCategoryId());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", AliasNameVerboseDataType.class.getSimpleName() + "[", "]");
    joiner.add("aliasName=" + getAliasName());
    joiner.add("referencedNodes=" + java.util.Arrays.toString(getReferencedNodes()));
    joiner.add("serverUris=" + java.util.Arrays.toString(getServerUris()));
    joiner.add("aliasNameCategoryId=" + getAliasNameCategoryId());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 24262),
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
              false),
          new StructureField(
              "ServerUris",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "AliasNameCategoryId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<AliasNameVerboseDataType> {
    @Override
    public Class<AliasNameVerboseDataType> getType() {
      return AliasNameVerboseDataType.class;
    }

    @Override
    public AliasNameVerboseDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final QualifiedName aliasName;
      final ExpandedNodeId[] referencedNodes;
      final String[] serverUris;
      final NodeId aliasNameCategoryId;
      aliasName = decoder.decodeQualifiedName("AliasName");
      referencedNodes = decoder.decodeExpandedNodeIdArray("ReferencedNodes");
      serverUris = decoder.decodeStringArray("ServerUris");
      aliasNameCategoryId = decoder.decodeNodeId("AliasNameCategoryId");
      return new AliasNameVerboseDataType(
          aliasName, referencedNodes, serverUris, aliasNameCategoryId);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, AliasNameVerboseDataType value) {
      encoder.encodeQualifiedName("AliasName", value.getAliasName());
      encoder.encodeExpandedNodeIdArray("ReferencedNodes", value.getReferencedNodes());
      encoder.encodeStringArray("ServerUris", value.getServerUris());
      encoder.encodeNodeId("AliasNameCategoryId", value.getAliasNameCategoryId());
    }
  }
}
