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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.4">https://reference.opcfoundation.org/v105/Core/docs/Part18/5.2.4</a>
 */
public class UserManagementDataType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=24281");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=24292");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=24296");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=24300");

  private final @Nullable String userName;

  private final UserConfigurationMask userConfiguration;

  private final @Nullable String description;

  public UserManagementDataType(
      @Nullable String userName,
      UserConfigurationMask userConfiguration,
      @Nullable String description) {
    this.userName = userName;
    this.userConfiguration = userConfiguration;
    this.description = description;
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

  public @Nullable String getUserName() {
    return userName;
  }

  public UserConfigurationMask getUserConfiguration() {
    return userConfiguration;
  }

  public @Nullable String getDescription() {
    return description;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    UserManagementDataType that = (UserManagementDataType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getUserName(), that.getUserName());
    eqb.append(getUserConfiguration(), that.getUserConfiguration());
    eqb.append(getDescription(), that.getDescription());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getUserName());
    hcb.append(getUserConfiguration());
    hcb.append(getDescription());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", UserManagementDataType.class.getSimpleName() + "[", "]");
    joiner.add("userName='" + getUserName() + "'");
    joiner.add("userConfiguration=" + getUserConfiguration());
    joiner.add("description='" + getDescription() + "'");
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 24292),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "UserName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UserConfiguration",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 24279),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "Description",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<UserManagementDataType> {
    @Override
    public Class<UserManagementDataType> getType() {
      return UserManagementDataType.class;
    }

    @Override
    public UserManagementDataType decodeType(EncodingContext context, UaDecoder decoder) {
      String userName = decoder.decodeString("UserName");
      UserConfigurationMask userConfiguration =
          new UserConfigurationMask(decoder.decodeUInt32("UserConfiguration"));
      String description = decoder.decodeString("Description");
      return new UserManagementDataType(userName, userConfiguration, description);
    }

    @Override
    public void encodeType(
        EncodingContext context, UaEncoder encoder, UserManagementDataType value) {
      encoder.encodeString("UserName", value.getUserName());
      encoder.encodeUInt32("UserConfiguration", value.getUserConfiguration().getValue());
      encoder.encodeString("Description", value.getDescription());
    }
  }
}
