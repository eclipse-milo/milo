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
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.8.4">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.8.4</a>
 */
public class PublishedEventsDataType extends PublishedDataSetSourceDataType
    implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=15582");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=15681");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=15954");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=16155");

  private final NodeId eventNotifier;

  private final SimpleAttributeOperand @Nullable [] selectedFields;

  private final ContentFilter filter;

  public PublishedEventsDataType(
      NodeId eventNotifier,
      SimpleAttributeOperand @Nullable [] selectedFields,
      ContentFilter filter) {
    this.eventNotifier = eventNotifier;
    this.selectedFields = selectedFields;
    this.filter = filter;
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

  public NodeId getEventNotifier() {
    return eventNotifier;
  }

  public SimpleAttributeOperand @Nullable [] getSelectedFields() {
    return selectedFields;
  }

  public ContentFilter getFilter() {
    return filter;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    PublishedEventsDataType that = (PublishedEventsDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getEventNotifier(), that.getEventNotifier());
    eqb.append(getSelectedFields(), that.getSelectedFields());
    eqb.append(getFilter(), that.getFilter());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getEventNotifier());
    hcb.append(getSelectedFields());
    hcb.append(getFilter());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", PublishedEventsDataType.class.getSimpleName() + "[", "]");
    joiner.add("eventNotifier=" + getEventNotifier());
    joiner.add("selectedFields=" + java.util.Arrays.toString(getSelectedFields()));
    joiner.add("filter=" + getFilter());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 15681),
        new NodeId(0, 15580),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "EventNotifier",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SelectedFields",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 601),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Filter",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 586),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<PublishedEventsDataType> {
    @Override
    public Class<PublishedEventsDataType> getType() {
      return PublishedEventsDataType.class;
    }

    @Override
    public PublishedEventsDataType decodeType(EncodingContext context, UaDecoder decoder) {
      NodeId eventNotifier = decoder.decodeNodeId("EventNotifier");
      SimpleAttributeOperand[] selectedFields =
          (SimpleAttributeOperand[])
              decoder.decodeStructArray("SelectedFields", SimpleAttributeOperand.TYPE_ID);
      ContentFilter filter = (ContentFilter) decoder.decodeStruct("Filter", ContentFilter.TYPE_ID);
      return new PublishedEventsDataType(eventNotifier, selectedFields, filter);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, PublishedEventsDataType value) {
      encoder.encodeNodeId("EventNotifier", value.getEventNotifier());
      encoder.encodeStructArray(
          "SelectedFields", value.getSelectedFields(), SimpleAttributeOperand.TYPE_ID);
      encoder.encodeStruct("Filter", value.getFilter(), ContentFilter.TYPE_ID);
    }
  }
}
