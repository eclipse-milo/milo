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
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/6.6.3">https://reference.opcfoundation.org/v105/Core/docs/Part11/6.6.3</a>
 */
public class HistoryModifiedData extends HistoryData implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=11217");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=11227");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=11219");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15272");

  private final ModificationInfo @Nullable [] modificationInfos;

  public HistoryModifiedData(
      DataValue @Nullable [] dataValues, ModificationInfo @Nullable [] modificationInfos) {
    super(dataValues);
    this.modificationInfos = modificationInfos;
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

  public ModificationInfo @Nullable [] getModificationInfos() {
    return modificationInfos;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    HistoryModifiedData that = (HistoryModifiedData) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getModificationInfos(), that.getModificationInfos());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getModificationInfos());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", HistoryModifiedData.class.getSimpleName() + "[", "]");
    joiner.add("modificationInfos=" + java.util.Arrays.toString(getModificationInfos()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 11227),
        new NodeId(0, 656),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "DataValues",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 23),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ModificationInfos",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 11216),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<HistoryModifiedData> {
    @Override
    public Class<HistoryModifiedData> getType() {
      return HistoryModifiedData.class;
    }

    @Override
    public HistoryModifiedData decodeType(EncodingContext context, UaDecoder decoder) {
      final DataValue[] dataValues;
      final ModificationInfo[] modificationInfos;
      dataValues = decoder.decodeDataValueArray("DataValues");
      modificationInfos =
          (ModificationInfo[])
              decoder.decodeStructArray("ModificationInfos", ModificationInfo.TYPE_ID);
      return new HistoryModifiedData(dataValues, modificationInfos);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, HistoryModifiedData value) {
      encoder.encodeDataValueArray("DataValues", value.getDataValues());
      encoder.encodeStructArray(
          "ModificationInfos", value.getModificationInfos(), ModificationInfo.TYPE_ID);
    }
  }
}
