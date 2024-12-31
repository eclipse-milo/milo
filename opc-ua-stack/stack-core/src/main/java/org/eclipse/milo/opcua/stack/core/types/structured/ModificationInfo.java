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
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.HistoryUpdateType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

/**
 * @see <a
 *     href="https://reference.opcfoundation.org/v104/Core/docs/Part11/6.5.3">https://reference.opcfoundation.org/v104/Core/docs/Part11/6.5.3</a>
 */
public class ModificationInfo extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=0;i=11216");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=11226");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=11218");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=15271");

  private final DateTime modificationTime;

  private final HistoryUpdateType updateType;

  private final @Nullable String userName;

  public ModificationInfo(
      DateTime modificationTime, HistoryUpdateType updateType, @Nullable String userName) {
    this.modificationTime = modificationTime;
    this.updateType = updateType;
    this.userName = userName;
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

  public DateTime getModificationTime() {
    return modificationTime;
  }

  public HistoryUpdateType getUpdateType() {
    return updateType;
  }

  public @Nullable String getUserName() {
    return userName;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ModificationInfo that = (ModificationInfo) object;
    var eqb = new EqualsBuilder();
    eqb.append(getModificationTime(), that.getModificationTime());
    eqb.append(getUpdateType(), that.getUpdateType());
    eqb.append(getUserName(), that.getUserName());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getModificationTime());
    hcb.append(getUpdateType());
    hcb.append(getUserName());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ModificationInfo.class.getSimpleName() + "[", "]");
    joiner.add("modificationTime=" + getModificationTime());
    joiner.add("updateType=" + getUpdateType());
    joiner.add("userName='" + getUserName() + "'");
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 11226),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "ModificationTime",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 294),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UpdateType",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 11234),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "UserName",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ModificationInfo> {
    @Override
    public Class<ModificationInfo> getType() {
      return ModificationInfo.class;
    }

    @Override
    public ModificationInfo decodeType(EncodingContext context, UaDecoder decoder) {
      DateTime modificationTime = decoder.decodeDateTime("ModificationTime");
      HistoryUpdateType updateType = HistoryUpdateType.from(decoder.decodeEnum("UpdateType"));
      String userName = decoder.decodeString("UserName");
      return new ModificationInfo(modificationTime, updateType, userName);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, ModificationInfo value) {
      encoder.encodeDateTime("ModificationTime", value.getModificationTime());
      encoder.encodeEnum("UpdateType", value.getUpdateType());
      encoder.encodeString("UserName", value.getUserName());
    }
  }
}
