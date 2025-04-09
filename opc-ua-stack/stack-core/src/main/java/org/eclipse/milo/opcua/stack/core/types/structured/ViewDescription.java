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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part4/7.45">https://reference.opcfoundation.org/v105/Core/docs/Part4/7.45</a>
 */
public class ViewDescription extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=511");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=513");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=512");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15179");

  private final NodeId viewId;

  private final DateTime timestamp;

  private final UInteger viewVersion;

  public ViewDescription(NodeId viewId, DateTime timestamp, UInteger viewVersion) {
    this.viewId = viewId;
    this.timestamp = timestamp;
    this.viewVersion = viewVersion;
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

  public NodeId getViewId() {
    return viewId;
  }

  public DateTime getTimestamp() {
    return timestamp;
  }

  public UInteger getViewVersion() {
    return viewVersion;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ViewDescription that = (ViewDescription) object;
    var eqb = new EqualsBuilder();
    eqb.append(getViewId(), that.getViewId());
    eqb.append(getTimestamp(), that.getTimestamp());
    eqb.append(getViewVersion(), that.getViewVersion());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getViewId());
    hcb.append(getTimestamp());
    hcb.append(getViewVersion());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ViewDescription.class.getSimpleName() + "[", "]");
    joiner.add("viewId=" + getViewId());
    joiner.add("timestamp=" + getTimestamp());
    joiner.add("viewVersion=" + getViewVersion());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 513),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ViewId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 17),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Timestamp",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 294),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ViewVersion",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ViewDescription> {
    @Override
    public Class<ViewDescription> getType() {
      return ViewDescription.class;
    }

    @Override
    public ViewDescription decodeType(EncodingContext context, UaDecoder decoder) {
      final NodeId viewId;
      final DateTime timestamp;
      final UInteger viewVersion;
      viewId = decoder.decodeNodeId("ViewId");
      timestamp = decoder.decodeDateTime("Timestamp");
      viewVersion = decoder.decodeUInt32("ViewVersion");
      return new ViewDescription(viewId, timestamp, viewVersion);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, ViewDescription value) {
      encoder.encodeNodeId("ViewId", value.getViewId());
      encoder.encodeDateTime("Timestamp", value.getTimestamp());
      encoder.encodeUInt32("ViewVersion", value.getViewVersion());
    }
  }
}
