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
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part11/6.6.4">https://reference.opcfoundation.org/v105/Core/docs/Part11/6.6.4</a>
 */
public class HistoryEventFieldList extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=920");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=922");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=921");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15349");

  private final Variant @Nullable [] eventFields;

  public HistoryEventFieldList(Variant @Nullable [] eventFields) {
    this.eventFields = eventFields;
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

  public Variant @Nullable [] getEventFields() {
    return eventFields;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    HistoryEventFieldList that = (HistoryEventFieldList) object;
    var eqb = new EqualsBuilder();
    eqb.append(getEventFields(), that.getEventFields());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getEventFields());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", HistoryEventFieldList.class.getSimpleName() + "[", "]");
    joiner.add("eventFields=" + java.util.Arrays.toString(getEventFields()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 922),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "EventFields",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 24),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<HistoryEventFieldList> {
    @Override
    public Class<HistoryEventFieldList> getType() {
      return HistoryEventFieldList.class;
    }

    @Override
    public HistoryEventFieldList decodeType(EncodingContext context, UaDecoder decoder) {
      final Variant[] eventFields;
      eventFields = decoder.decodeVariantArray("EventFields");
      return new HistoryEventFieldList(eventFields);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, HistoryEventFieldList value) {
      encoder.encodeVariantArray("EventFields", value.getEventFields());
    }
  }
}
