/*
 * Copyright (c) 2026 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.types.enumerated.ConfigurationUpdateType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.6">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.6</a>
 */
public class ConfigurationUpdateTargetType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=15538");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=16541");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=16590");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=16635");

  private final @Nullable String path;

  private final ConfigurationUpdateType updateType;

  public ConfigurationUpdateTargetType(@Nullable String path, ConfigurationUpdateType updateType) {
    this.path = path;
    this.updateType = updateType;
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

  public @Nullable String getPath() {
    return path;
  }

  public ConfigurationUpdateType getUpdateType() {
    return updateType;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ConfigurationUpdateTargetType that = (ConfigurationUpdateTargetType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getPath(), that.getPath());
    eqb.append(getUpdateType(), that.getUpdateType());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getPath());
    hcb.append(getUpdateType());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", ConfigurationUpdateTargetType.class.getSimpleName() + "[", "]");
    joiner.add("path='" + getPath() + "'");
    joiner.add("updateType=" + getUpdateType());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 16541),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Path",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UpdateType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15539),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ConfigurationUpdateTargetType> {
    @Override
    public Class<ConfigurationUpdateTargetType> getType() {
      return ConfigurationUpdateTargetType.class;
    }

    @Override
    public ConfigurationUpdateTargetType decodeType(EncodingContext context, UaDecoder decoder) {
      final String path;
      final ConfigurationUpdateType updateType;
      path = decoder.decodeString("Path");
      updateType = ConfigurationUpdateType.from(decoder.decodeEnum("UpdateType"));
      return new ConfigurationUpdateTargetType(path, updateType);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, ConfigurationUpdateTargetType value) {
      encoder.encodeString("Path", value.getPath());
      encoder.encodeEnum("UpdateType", value.getUpdateType());
    }
  }
}
