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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.12/#6.2.12.1">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.12/#6.2.12.1</a>
 */
public class PubSubConfigurationDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=15530");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=21154");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=21178");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=21202");

  private final PublishedDataSetDataType @Nullable [] publishedDataSets;

  private final PubSubConnectionDataType @Nullable [] connections;

  private final Boolean enabled;

  public PubSubConfigurationDataType(
      PublishedDataSetDataType @Nullable [] publishedDataSets,
      PubSubConnectionDataType @Nullable [] connections,
      Boolean enabled) {
    this.publishedDataSets = publishedDataSets;
    this.connections = connections;
    this.enabled = enabled;
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

  public PublishedDataSetDataType @Nullable [] getPublishedDataSets() {
    return publishedDataSets;
  }

  public PubSubConnectionDataType @Nullable [] getConnections() {
    return connections;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    PubSubConfigurationDataType that = (PubSubConfigurationDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getPublishedDataSets(), that.getPublishedDataSets());
    eqb.append(getConnections(), that.getConnections());
    eqb.append(getEnabled(), that.getEnabled());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getPublishedDataSets());
    hcb.append(getConnections());
    hcb.append(getEnabled());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", PubSubConfigurationDataType.class.getSimpleName() + "[", "]");
    joiner.add("publishedDataSets=" + java.util.Arrays.toString(getPublishedDataSets()));
    joiner.add("connections=" + java.util.Arrays.toString(getConnections()));
    joiner.add("enabled=" + getEnabled());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 21154),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "PublishedDataSets",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15578),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Connections",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15617),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Enabled",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<PubSubConfigurationDataType> {
    @Override
    public Class<PubSubConfigurationDataType> getType() {
      return PubSubConfigurationDataType.class;
    }

    @Override
    public PubSubConfigurationDataType decodeType(EncodingContext context, UaDecoder decoder) {
      PublishedDataSetDataType[] publishedDataSets =
          (PublishedDataSetDataType[])
              decoder.decodeStructArray("PublishedDataSets", PublishedDataSetDataType.TYPE_ID);
      PubSubConnectionDataType[] connections =
          (PubSubConnectionDataType[])
              decoder.decodeStructArray("Connections", PubSubConnectionDataType.TYPE_ID);
      Boolean enabled = decoder.decodeBoolean("Enabled");
      return new PubSubConfigurationDataType(publishedDataSets, connections, enabled);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, PubSubConfigurationDataType value) {
      encoder.encodeStructArray(
          "PublishedDataSets", value.getPublishedDataSets(), PublishedDataSetDataType.TYPE_ID);
      encoder.encodeStructArray(
          "Connections", value.getConnections(), PubSubConnectionDataType.TYPE_ID);
      encoder.encodeBoolean("Enabled", value.getEnabled());
    }
  }
}
