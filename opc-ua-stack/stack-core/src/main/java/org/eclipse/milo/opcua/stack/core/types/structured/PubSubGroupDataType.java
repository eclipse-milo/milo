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
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.5/#6.2.5.7">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.5/#6.2.5.7</a>
 */
public abstract class PubSubGroupDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=15609");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=15689");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=15988");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=16159");

  private final @Nullable String name;

  private final Boolean enabled;

  private final MessageSecurityMode securityMode;

  private final @Nullable String securityGroupId;

  private final EndpointDescription @Nullable [] securityKeyServices;

  private final UInteger maxNetworkMessageSize;

  private final KeyValuePair @Nullable [] groupProperties;

  public PubSubGroupDataType(
      @Nullable String name,
      Boolean enabled,
      MessageSecurityMode securityMode,
      @Nullable String securityGroupId,
      EndpointDescription @Nullable [] securityKeyServices,
      UInteger maxNetworkMessageSize,
      KeyValuePair @Nullable [] groupProperties) {
    this.name = name;
    this.enabled = enabled;
    this.securityMode = securityMode;
    this.securityGroupId = securityGroupId;
    this.securityKeyServices = securityKeyServices;
    this.maxNetworkMessageSize = maxNetworkMessageSize;
    this.groupProperties = groupProperties;
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

  public @Nullable String getName() {
    return name;
  }

  public Boolean getEnabled() {
    return enabled;
  }

  public MessageSecurityMode getSecurityMode() {
    return securityMode;
  }

  public @Nullable String getSecurityGroupId() {
    return securityGroupId;
  }

  public EndpointDescription @Nullable [] getSecurityKeyServices() {
    return securityKeyServices;
  }

  public UInteger getMaxNetworkMessageSize() {
    return maxNetworkMessageSize;
  }

  public KeyValuePair @Nullable [] getGroupProperties() {
    return groupProperties;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    PubSubGroupDataType that = (PubSubGroupDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getName(), that.getName());
    eqb.append(getEnabled(), that.getEnabled());
    eqb.append(getSecurityMode(), that.getSecurityMode());
    eqb.append(getSecurityGroupId(), that.getSecurityGroupId());
    eqb.append(getSecurityKeyServices(), that.getSecurityKeyServices());
    eqb.append(getMaxNetworkMessageSize(), that.getMaxNetworkMessageSize());
    eqb.append(getGroupProperties(), that.getGroupProperties());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getName());
    hcb.append(getEnabled());
    hcb.append(getSecurityMode());
    hcb.append(getSecurityGroupId());
    hcb.append(getSecurityKeyServices());
    hcb.append(getMaxNetworkMessageSize());
    hcb.append(getGroupProperties());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", PubSubGroupDataType.class.getSimpleName() + "[", "]");
    joiner.add("name='" + getName() + "'");
    joiner.add("enabled=" + getEnabled());
    joiner.add("securityMode=" + getSecurityMode());
    joiner.add("securityGroupId='" + getSecurityGroupId() + "'");
    joiner.add("securityKeyServices=" + java.util.Arrays.toString(getSecurityKeyServices()));
    joiner.add("maxNetworkMessageSize=" + getMaxNetworkMessageSize());
    joiner.add("groupProperties=" + java.util.Arrays.toString(getGroupProperties()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 15689),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Name",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
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
              false),
          new StructureField(
              "SecurityMode",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 302),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SecurityGroupId",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SecurityKeyServices",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 312),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MaxNetworkMessageSize",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "GroupProperties",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 14533),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }
}
