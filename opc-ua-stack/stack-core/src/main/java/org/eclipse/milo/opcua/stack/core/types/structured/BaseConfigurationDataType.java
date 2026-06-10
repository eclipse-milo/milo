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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.4">https://reference.opcfoundation.org/v105/Core/docs/Part12/7.8.5/#7.8.5.4</a>
 */
public abstract class BaseConfigurationDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=15434");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=16538");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=16587");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=16632");

  private final UInteger configurationVersion;

  private final KeyValuePair @Nullable [] configurationProperties;

  public BaseConfigurationDataType(
      UInteger configurationVersion, KeyValuePair @Nullable [] configurationProperties) {
    this.configurationVersion = configurationVersion;
    this.configurationProperties = configurationProperties;
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

  public UInteger getConfigurationVersion() {
    return configurationVersion;
  }

  public KeyValuePair @Nullable [] getConfigurationProperties() {
    return configurationProperties;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    BaseConfigurationDataType that = (BaseConfigurationDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getConfigurationVersion(), that.getConfigurationVersion());
    eqb.append(getConfigurationProperties(), that.getConfigurationProperties());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getConfigurationVersion());
    hcb.append(getConfigurationProperties());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", BaseConfigurationDataType.class.getSimpleName() + "[", "]");
    joiner.add("configurationVersion=" + getConfigurationVersion());
    joiner.add(
        "configurationProperties=" + java.util.Arrays.toString(getConfigurationProperties()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 16538),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ConfigurationVersion",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 20998),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "ConfigurationProperties",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14533),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }
}
