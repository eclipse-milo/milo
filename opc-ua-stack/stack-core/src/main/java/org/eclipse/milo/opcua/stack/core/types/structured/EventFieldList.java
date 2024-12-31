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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.25.3">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.25.3</a>
 */
public class EventFieldList extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=917");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=919");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=918");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15348");

  private final UInteger clientHandle;

  private final Variant @Nullable [] eventFields;

  public EventFieldList(UInteger clientHandle, Variant @Nullable [] eventFields) {
    this.clientHandle = clientHandle;
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

  public UInteger getClientHandle() {
    return clientHandle;
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
    EventFieldList that = (EventFieldList) object;
    var eqb = new EqualsBuilder();
    eqb.append(getClientHandle(), that.getClientHandle());
    eqb.append(getEventFields(), that.getEventFields());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getClientHandle());
    hcb.append(getEventFields());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", EventFieldList.class.getSimpleName() + "[", "]");
    joiner.add("clientHandle=" + getClientHandle());
    joiner.add("eventFields=" + java.util.Arrays.toString(getEventFields()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 919),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ClientHandle",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 288),
              -1,
              null,
              UInteger.valueOf(0),
              false),
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

  public static final class Codec extends GenericDataTypeCodec<EventFieldList> {
    @Override
    public Class<EventFieldList> getType() {
      return EventFieldList.class;
    }

    @Override
    public EventFieldList decodeType(EncodingContext context, UaDecoder decoder) {
      UInteger clientHandle = decoder.decodeUInt32("ClientHandle");
      Variant[] eventFields = decoder.decodeVariantArray("EventFields");
      return new EventFieldList(clientHandle, eventFields);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, EventFieldList value) {
      encoder.encodeUInt32("ClientHandle", value.getClientHandle());
      encoder.encodeVariantArray("EventFields", value.getEventFields());
    }
  }
}
