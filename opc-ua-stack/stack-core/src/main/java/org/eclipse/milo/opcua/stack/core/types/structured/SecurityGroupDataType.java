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
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.12/#6.2.12.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.12/#6.2.12.2</a>
 */
public class SecurityGroupDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=23601");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=23853");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=23921");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=23989");

  private final @Nullable String name;

  private final String @Nullable [] securityGroupFolder;

  private final Double keyLifetime;

  private final @Nullable String securityPolicyUri;

  private final UInteger maxFutureKeyCount;

  private final UInteger maxPastKeyCount;

  private final @Nullable String securityGroupId;

  private final RolePermissionType @Nullable [] rolePermissions;

  private final KeyValuePair @Nullable [] groupProperties;

  public SecurityGroupDataType(
      @Nullable String name,
      String @Nullable [] securityGroupFolder,
      Double keyLifetime,
      @Nullable String securityPolicyUri,
      UInteger maxFutureKeyCount,
      UInteger maxPastKeyCount,
      @Nullable String securityGroupId,
      RolePermissionType @Nullable [] rolePermissions,
      KeyValuePair @Nullable [] groupProperties) {
    this.name = name;
    this.securityGroupFolder = securityGroupFolder;
    this.keyLifetime = keyLifetime;
    this.securityPolicyUri = securityPolicyUri;
    this.maxFutureKeyCount = maxFutureKeyCount;
    this.maxPastKeyCount = maxPastKeyCount;
    this.securityGroupId = securityGroupId;
    this.rolePermissions = rolePermissions;
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

  public String @Nullable [] getSecurityGroupFolder() {
    return securityGroupFolder;
  }

  public Double getKeyLifetime() {
    return keyLifetime;
  }

  public @Nullable String getSecurityPolicyUri() {
    return securityPolicyUri;
  }

  public UInteger getMaxFutureKeyCount() {
    return maxFutureKeyCount;
  }

  public UInteger getMaxPastKeyCount() {
    return maxPastKeyCount;
  }

  public @Nullable String getSecurityGroupId() {
    return securityGroupId;
  }

  public RolePermissionType @Nullable [] getRolePermissions() {
    return rolePermissions;
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
    SecurityGroupDataType that = (SecurityGroupDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getName(), that.getName());
    eqb.append(getSecurityGroupFolder(), that.getSecurityGroupFolder());
    eqb.append(getKeyLifetime(), that.getKeyLifetime());
    eqb.append(getSecurityPolicyUri(), that.getSecurityPolicyUri());
    eqb.append(getMaxFutureKeyCount(), that.getMaxFutureKeyCount());
    eqb.append(getMaxPastKeyCount(), that.getMaxPastKeyCount());
    eqb.append(getSecurityGroupId(), that.getSecurityGroupId());
    eqb.append(getRolePermissions(), that.getRolePermissions());
    eqb.append(getGroupProperties(), that.getGroupProperties());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getName());
    hcb.append(getSecurityGroupFolder());
    hcb.append(getKeyLifetime());
    hcb.append(getSecurityPolicyUri());
    hcb.append(getMaxFutureKeyCount());
    hcb.append(getMaxPastKeyCount());
    hcb.append(getSecurityGroupId());
    hcb.append(getRolePermissions());
    hcb.append(getGroupProperties());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", SecurityGroupDataType.class.getSimpleName() + "[", "]");
    joiner.add("name='" + getName() + "'");
    joiner.add("securityGroupFolder=" + java.util.Arrays.toString(getSecurityGroupFolder()));
    joiner.add("keyLifetime=" + getKeyLifetime());
    joiner.add("securityPolicyUri='" + getSecurityPolicyUri() + "'");
    joiner.add("maxFutureKeyCount=" + getMaxFutureKeyCount());
    joiner.add("maxPastKeyCount=" + getMaxPastKeyCount());
    joiner.add("securityGroupId='" + getSecurityGroupId() + "'");
    joiner.add("rolePermissions=" + java.util.Arrays.toString(getRolePermissions()));
    joiner.add("groupProperties=" + java.util.Arrays.toString(getGroupProperties()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 23853),
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
              "SecurityGroupFolder",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "KeyLifetime",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 290),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SecurityPolicyUri",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MaxFutureKeyCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "MaxPastKeyCount",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 7),
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
              "RolePermissions",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 96),
              1,
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

  public static final class Codec extends GenericDataTypeCodec<SecurityGroupDataType> {
    @Override
    public Class<SecurityGroupDataType> getType() {
      return SecurityGroupDataType.class;
    }

    @Override
    public SecurityGroupDataType decodeType(EncodingContext context, UaDecoder decoder) {
      final String name;
      final String[] securityGroupFolder;
      final Double keyLifetime;
      final String securityPolicyUri;
      final UInteger maxFutureKeyCount;
      final UInteger maxPastKeyCount;
      final String securityGroupId;
      final RolePermissionType[] rolePermissions;
      final KeyValuePair[] groupProperties;
      name = decoder.decodeString("Name");
      securityGroupFolder = decoder.decodeStringArray("SecurityGroupFolder");
      keyLifetime = decoder.decodeDouble("KeyLifetime");
      securityPolicyUri = decoder.decodeString("SecurityPolicyUri");
      maxFutureKeyCount = decoder.decodeUInt32("MaxFutureKeyCount");
      maxPastKeyCount = decoder.decodeUInt32("MaxPastKeyCount");
      securityGroupId = decoder.decodeString("SecurityGroupId");
      rolePermissions =
          (RolePermissionType[])
              decoder.decodeStructArray("RolePermissions", RolePermissionType.TYPE_ID);
      groupProperties =
          (KeyValuePair[]) decoder.decodeStructArray("GroupProperties", KeyValuePair.TYPE_ID);
      return new SecurityGroupDataType(
          name,
          securityGroupFolder,
          keyLifetime,
          securityPolicyUri,
          maxFutureKeyCount,
          maxPastKeyCount,
          securityGroupId,
          rolePermissions,
          groupProperties);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, SecurityGroupDataType value) {
      encoder.encodeString("Name", value.getName());
      encoder.encodeStringArray("SecurityGroupFolder", value.getSecurityGroupFolder());
      encoder.encodeDouble("KeyLifetime", value.getKeyLifetime());
      encoder.encodeString("SecurityPolicyUri", value.getSecurityPolicyUri());
      encoder.encodeUInt32("MaxFutureKeyCount", value.getMaxFutureKeyCount());
      encoder.encodeUInt32("MaxPastKeyCount", value.getMaxPastKeyCount());
      encoder.encodeString("SecurityGroupId", value.getSecurityGroupId());
      encoder.encodeStructArray(
          "RolePermissions", value.getRolePermissions(), RolePermissionType.TYPE_ID);
      encoder.encodeStructArray(
          "GroupProperties", value.getGroupProperties(), KeyValuePair.TYPE_ID);
    }
  }
}
