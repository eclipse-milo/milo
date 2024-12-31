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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.2.5">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.2.5</a>
 */
public class ConfigurationVersionDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=14593");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=14847");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=14803");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15049");

  private final UInteger majorVersion;

  private final UInteger minorVersion;

  public ConfigurationVersionDataType(UInteger majorVersion, UInteger minorVersion) {
    this.majorVersion = majorVersion;
    this.minorVersion = minorVersion;
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

  public UInteger getMajorVersion() {
    return majorVersion;
  }

  public UInteger getMinorVersion() {
    return minorVersion;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ConfigurationVersionDataType that = (ConfigurationVersionDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getMajorVersion(), that.getMajorVersion());
    eqb.append(getMinorVersion(), that.getMinorVersion());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getMajorVersion());
    hcb.append(getMinorVersion());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", ConfigurationVersionDataType.class.getSimpleName() + "[", "]");
    joiner.add("majorVersion=" + getMajorVersion());
    joiner.add("minorVersion=" + getMinorVersion());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 14847),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "MajorVersion",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 20998),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MinorVersion",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 20998),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ConfigurationVersionDataType> {
    @Override
    public Class<ConfigurationVersionDataType> getType() {
      return ConfigurationVersionDataType.class;
    }

    @Override
    public ConfigurationVersionDataType decodeType(EncodingContext context, UaDecoder decoder) {
      UInteger majorVersion = decoder.decodeUInt32("MajorVersion");
      UInteger minorVersion = decoder.decodeUInt32("MinorVersion");
      return new ConfigurationVersionDataType(majorVersion, minorVersion);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, ConfigurationVersionDataType value) {
      encoder.encodeUInt32("MajorVersion", value.getMajorVersion());
      encoder.encodeUInt32("MinorVersion", value.getMinorVersion());
    }
  }
}
