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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.5/#5.14.5.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.14.5/#5.14.5.2</a>
 */
public class SubscriptionAcknowledgement extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=821");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=823");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=822");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15351");

  private final UInteger subscriptionId;

  private final UInteger sequenceNumber;

  public SubscriptionAcknowledgement(UInteger subscriptionId, UInteger sequenceNumber) {
    this.subscriptionId = subscriptionId;
    this.sequenceNumber = sequenceNumber;
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

  public UInteger getSubscriptionId() {
    return subscriptionId;
  }

  public UInteger getSequenceNumber() {
    return sequenceNumber;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    SubscriptionAcknowledgement that = (SubscriptionAcknowledgement) object;
    var eqb = new EqualsBuilder();
    eqb.append(getSubscriptionId(), that.getSubscriptionId());
    eqb.append(getSequenceNumber(), that.getSequenceNumber());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getSubscriptionId());
    hcb.append(getSequenceNumber());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", SubscriptionAcknowledgement.class.getSimpleName() + "[", "]");
    joiner.add("subscriptionId=" + getSubscriptionId());
    joiner.add("sequenceNumber=" + getSequenceNumber());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 823),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "SubscriptionId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 288),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SequenceNumber",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 289),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<SubscriptionAcknowledgement> {
    @Override
    public Class<SubscriptionAcknowledgement> getType() {
      return SubscriptionAcknowledgement.class;
    }

    @Override
    public SubscriptionAcknowledgement decodeType(EncodingContext context, UaDecoder decoder) {
      final UInteger subscriptionId;
      final UInteger sequenceNumber;
      subscriptionId = decoder.decodeUInt32("SubscriptionId");
      sequenceNumber = decoder.decodeUInt32("SequenceNumber");
      return new SubscriptionAcknowledgement(subscriptionId, sequenceNumber);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, SubscriptionAcknowledgement value) {
      encoder.encodeUInt32("SubscriptionId", value.getSubscriptionId());
      encoder.encodeUInt32("SequenceNumber", value.getSequenceNumber());
    }
  }
}
