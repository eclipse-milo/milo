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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part17/D.2.1">https://reference.opcfoundation.org/v105/Core/docs/Part17/D.2.1</a>
 */
public class AliasCategoryUpdateDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=24052");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=24338");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=24354");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=24370");

  private final PortableNodeId category;

  private final UInteger lastChange;

  public AliasCategoryUpdateDataType(PortableNodeId category, UInteger lastChange) {
    this.category = category;
    this.lastChange = lastChange;
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

  public PortableNodeId getCategory() {
    return category;
  }

  public UInteger getLastChange() {
    return lastChange;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    AliasCategoryUpdateDataType that = (AliasCategoryUpdateDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getCategory(), that.getCategory());
    eqb.append(getLastChange(), that.getLastChange());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getCategory());
    hcb.append(getLastChange());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", AliasCategoryUpdateDataType.class.getSimpleName() + "[", "]");
    joiner.add("category=" + getCategory());
    joiner.add("lastChange=" + getLastChange());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 24338),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Category",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 24106),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "LastChange",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 20998),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<AliasCategoryUpdateDataType> {
    @Override
    public Class<AliasCategoryUpdateDataType> getType() {
      return AliasCategoryUpdateDataType.class;
    }

    @Override
    public AliasCategoryUpdateDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final PortableNodeId category;
      final UInteger lastChange;
      category = (PortableNodeId) decoder.decodeStruct("Category", PortableNodeId.TYPE_ID);
      lastChange = decoder.decodeUInt32("LastChange");
      return new AliasCategoryUpdateDataType(category, lastChange);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, AliasCategoryUpdateDataType value) {
      encoder.encodeStruct("Category", value.getCategory(), PortableNodeId.TYPE_ID);
      encoder.encodeUInt32("LastChange", value.getLastChange());
    }
  }
}
