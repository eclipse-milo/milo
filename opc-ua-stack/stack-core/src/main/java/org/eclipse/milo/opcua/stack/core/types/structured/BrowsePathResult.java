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
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/5.8.4/#5.8.4.2">https://reference.opcfoundation.org/v105/Core/docs/Part4/5.8.4/#5.8.4.2</a>
 */
public class BrowsePathResult extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=549");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=551");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=550");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15192");

  private final StatusCode statusCode;

  private final BrowsePathTarget @Nullable [] targets;

  public BrowsePathResult(StatusCode statusCode, BrowsePathTarget @Nullable [] targets) {
    this.statusCode = statusCode;
    this.targets = targets;
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

  public StatusCode getStatusCode() {
    return statusCode;
  }

  public BrowsePathTarget @Nullable [] getTargets() {
    return targets;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    BrowsePathResult that = (BrowsePathResult) object;
    var eqb = new EqualsBuilder();
    eqb.append(getStatusCode(), that.getStatusCode());
    eqb.append(getTargets(), that.getTargets());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getStatusCode());
    hcb.append(getTargets());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", BrowsePathResult.class.getSimpleName() + "[", "]");
    joiner.add("statusCode=" + getStatusCode());
    joiner.add("targets=" + java.util.Arrays.toString(getTargets()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 551),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "StatusCode",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 19),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Targets",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 546),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<BrowsePathResult> {
    @Override
    public Class<BrowsePathResult> getType() {
      return BrowsePathResult.class;
    }

    @Override
    public BrowsePathResult decodeType(EncodingContext context, UaDecoder decoder) {
      StatusCode statusCode = decoder.decodeStatusCode("StatusCode");
      BrowsePathTarget[] targets =
          (BrowsePathTarget[]) decoder.decodeStructArray("Targets", BrowsePathTarget.TYPE_ID);
      return new BrowsePathResult(statusCode, targets);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, BrowsePathResult value) {
      encoder.encodeStatusCode("StatusCode", value.getStatusCode());
      encoder.encodeStructArray("Targets", value.getTargets(), BrowsePathTarget.TYPE_ID);
    }
  }
}
