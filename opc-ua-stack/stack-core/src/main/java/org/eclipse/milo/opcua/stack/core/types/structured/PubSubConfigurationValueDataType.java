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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.4">https://reference.opcfoundation.org/v105/Core/docs/Part14/9.1.3/#9.1.3.7.4</a>
 */
public class PubSubConfigurationValueDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=25520");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=25532");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=25548");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=25564");

  private final PubSubConfigurationRefDataType configurationElement;

  private final @Nullable String name;

  private final Variant identifier;

  public PubSubConfigurationValueDataType(
      PubSubConfigurationRefDataType configurationElement,
      @Nullable String name,
      Variant identifier) {
    this.configurationElement = configurationElement;
    this.name = name;
    this.identifier = identifier;
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

  public PubSubConfigurationRefDataType getConfigurationElement() {
    return configurationElement;
  }

  public @Nullable String getName() {
    return name;
  }

  public Variant getIdentifier() {
    return identifier;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    PubSubConfigurationValueDataType that = (PubSubConfigurationValueDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getConfigurationElement(), that.getConfigurationElement());
    eqb.append(getName(), that.getName());
    eqb.append(getIdentifier(), that.getIdentifier());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getConfigurationElement());
    hcb.append(getName());
    hcb.append(getIdentifier());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner =
        new StringJoiner(", ", PubSubConfigurationValueDataType.class.getSimpleName() + "[", "]");
    joiner.add("configurationElement=" + getConfigurationElement());
    joiner.add("name='" + getName() + "'");
    joiner.add("identifier=" + getIdentifier());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 25532),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ConfigurationElement",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 25519),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Name",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Identifier",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 24),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<PubSubConfigurationValueDataType> {
    @Override
    public Class<PubSubConfigurationValueDataType> getType() {
      return PubSubConfigurationValueDataType.class;
    }

    @Override
    public PubSubConfigurationValueDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final PubSubConfigurationRefDataType configurationElement;
      final String name;
      final Variant identifier;
      configurationElement =
          (PubSubConfigurationRefDataType)
              decoder.decodeStruct("ConfigurationElement", PubSubConfigurationRefDataType.TYPE_ID);
      name = decoder.decodeString("Name");
      identifier = decoder.decodeVariant("Identifier");
      return new PubSubConfigurationValueDataType(configurationElement, name, identifier);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, PubSubConfigurationValueDataType value) {
      encoder.encodeStruct(
          "ConfigurationElement",
          value.getConfigurationElement(),
          PubSubConfigurationRefDataType.TYPE_ID);
      encoder.encodeString("Name", value.getName());
      encoder.encodeVariant("Identifier", value.getIdentifier());
    }
  }
}
