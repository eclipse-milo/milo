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

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.8.4/#5.8.4.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.8.4/#5.8.4.2</a>
 */
public class BrowsePathTarget extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=546");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=548");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=547");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15191");

  private final ExpandedNodeId targetId;

  private final UInteger remainingPathIndex;

  public BrowsePathTarget(ExpandedNodeId targetId, UInteger remainingPathIndex) {
    this.targetId = targetId;
    this.remainingPathIndex = remainingPathIndex;
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

  public ExpandedNodeId getTargetId() {
    return targetId;
  }

  public UInteger getRemainingPathIndex() {
    return remainingPathIndex;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    BrowsePathTarget that = (BrowsePathTarget) object;
    var eqb = new EqualsBuilder();
    eqb.append(getTargetId(), that.getTargetId());
    eqb.append(getRemainingPathIndex(), that.getRemainingPathIndex());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getTargetId());
    hcb.append(getRemainingPathIndex());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", BrowsePathTarget.class.getSimpleName() + "[", "]");
    joiner.add("targetId=" + getTargetId());
    joiner.add("remainingPathIndex=" + getRemainingPathIndex());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 548),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "TargetId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 18),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "RemainingPathIndex",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17588),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<BrowsePathTarget> {
    @Override
    public Class<BrowsePathTarget> getType() {
      return BrowsePathTarget.class;
    }

    @Override
    public BrowsePathTarget decodeType(EncodingContext context, UaDecoder decoder) {
      ExpandedNodeId targetId = decoder.decodeExpandedNodeId("TargetId");
      UInteger remainingPathIndex = decoder.decodeUInt32("RemainingPathIndex");
      return new BrowsePathTarget(targetId, remainingPathIndex);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, BrowsePathTarget value) {
      encoder.encodeExpandedNodeId("TargetId", value.getTargetId());
      encoder.encodeUInt32("RemainingPathIndex", value.getRemainingPathIndex());
    }
  }
}
