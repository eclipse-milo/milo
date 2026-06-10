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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part17/D.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part17/D.2.2</a>
 */
public class AliasUpdateDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=24053");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=24339");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=24355");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=24371");

  private final @Nullable String applicationUri;

  private final AliasCategoryUpdateDataType @Nullable [] categories;

  public AliasUpdateDataType(
      @Nullable String applicationUri, AliasCategoryUpdateDataType @Nullable [] categories) {
    this.applicationUri = applicationUri;
    this.categories = categories;
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

  public @Nullable String getApplicationUri() {
    return applicationUri;
  }

  public AliasCategoryUpdateDataType @Nullable [] getCategories() {
    return categories;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    AliasUpdateDataType that = (AliasUpdateDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getApplicationUri(), that.getApplicationUri());
    eqb.append(getCategories(), that.getCategories());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getApplicationUri());
    hcb.append(getCategories());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", AliasUpdateDataType.class.getSimpleName() + "[", "]");
    joiner.add("applicationUri='" + getApplicationUri() + "'");
    joiner.add("categories=" + java.util.Arrays.toString(getCategories()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 24339),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ApplicationUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Categories",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 24052),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<AliasUpdateDataType> {
    @Override
    public Class<AliasUpdateDataType> getType() {
      return AliasUpdateDataType.class;
    }

    @Override
    public AliasUpdateDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final String applicationUri;
      final AliasCategoryUpdateDataType[] categories;
      applicationUri = decoder.decodeString("ApplicationUri");
      categories =
          (AliasCategoryUpdateDataType[])
              decoder.decodeStructArray("Categories", AliasCategoryUpdateDataType.TYPE_ID);
      return new AliasUpdateDataType(applicationUri, categories);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, AliasUpdateDataType value) {
      encoder.encodeString("ApplicationUri", value.getApplicationUri());
      encoder.encodeStructArray(
          "Categories", value.getCategories(), AliasCategoryUpdateDataType.TYPE_ID);
    }
  }
}
