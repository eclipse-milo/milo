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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/6.6.5">https://reference.opcfoundation.org/v105/Core/docs/Part11/6.6.5</a>
 */
public class HistoryModifiedEvent extends HistoryEvent implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=32824");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=32825");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=32829");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=32833");

  private final ModificationInfo @Nullable [] modificationInfos;

  public HistoryModifiedEvent(
      HistoryEventFieldList @Nullable [] events, ModificationInfo @Nullable [] modificationInfos) {
    super(events);
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
    HistoryModifiedEvent that = (HistoryModifiedEvent) object;
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
    var joiner = new StringJoiner(", ", HistoryModifiedEvent.class.getSimpleName() + "[", "]");
    joiner.add("modificationInfos=" + java.util.Arrays.toString(getModificationInfos()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 32825),
        new NodeId(0, 659),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Events",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 920),
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

  public static final class Codec extends GenericDataTypeCodec<HistoryModifiedEvent> {
    @Override
    public Class<HistoryModifiedEvent> getType() {
      return HistoryModifiedEvent.class;
    }

    @Override
    public HistoryModifiedEvent decodeType(EncodingContext context, UaDecoder decoder) {
      final HistoryEventFieldList[] events;
      final ModificationInfo[] modificationInfos;
      events =
          (HistoryEventFieldList[])
              decoder.decodeStructArray("Events", HistoryEventFieldList.TYPE_ID);
      modificationInfos =
          (ModificationInfo[])
              decoder.decodeStructArray("ModificationInfos", ModificationInfo.TYPE_ID);
      return new HistoryModifiedEvent(events, modificationInfos);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, HistoryModifiedEvent value) {
      encoder.encodeStructArray("Events", value.getEvents(), HistoryEventFieldList.TYPE_ID);
      encoder.encodeStructArray(
          "ModificationInfos", value.getModificationInfos(), ModificationInfo.TYPE_ID);
    }
  }
}
