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
 *     href="https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.2.2">https://reference.opcfoundation.org/v105/Core/docs/Part14/6.2.3/#6.2.3.2.2</a>
 */
public abstract class DataTypeSchemaHeader extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("i=15534");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("i=15676");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("i=15950");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("i=16151");

  private final String @Nullable [] namespaces;

  private final StructureDescription @Nullable [] structureDataTypes;

  private final EnumDescription @Nullable [] enumDataTypes;

  private final SimpleTypeDescription @Nullable [] simpleDataTypes;

  public DataTypeSchemaHeader(
      String @Nullable [] namespaces,
      StructureDescription @Nullable [] structureDataTypes,
      EnumDescription @Nullable [] enumDataTypes,
      SimpleTypeDescription @Nullable [] simpleDataTypes) {
    this.namespaces = namespaces;
    this.structureDataTypes = structureDataTypes;
    this.enumDataTypes = enumDataTypes;
    this.simpleDataTypes = simpleDataTypes;
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

  public String @Nullable [] getNamespaces() {
    return namespaces;
  }

  public StructureDescription @Nullable [] getStructureDataTypes() {
    return structureDataTypes;
  }

  public EnumDescription @Nullable [] getEnumDataTypes() {
    return enumDataTypes;
  }

  public SimpleTypeDescription @Nullable [] getSimpleDataTypes() {
    return simpleDataTypes;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    DataTypeSchemaHeader that = (DataTypeSchemaHeader) object;
    var eqb = new EqualsBuilder();
    eqb.append(getNamespaces(), that.getNamespaces());
    eqb.append(getStructureDataTypes(), that.getStructureDataTypes());
    eqb.append(getEnumDataTypes(), that.getEnumDataTypes());
    eqb.append(getSimpleDataTypes(), that.getSimpleDataTypes());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getNamespaces());
    hcb.append(getStructureDataTypes());
    hcb.append(getEnumDataTypes());
    hcb.append(getSimpleDataTypes());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", DataTypeSchemaHeader.class.getSimpleName() + "[", "]");
    joiner.add("namespaces=" + java.util.Arrays.toString(getNamespaces()));
    joiner.add("structureDataTypes=" + java.util.Arrays.toString(getStructureDataTypes()));
    joiner.add("enumDataTypes=" + java.util.Arrays.toString(getEnumDataTypes()));
    joiner.add("simpleDataTypes=" + java.util.Arrays.toString(getSimpleDataTypes()));
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 15676),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Namespaces",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "StructureDataTypes",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15487),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "EnumDataTypes",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15488),
              1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "SimpleDataTypes",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 15005),
              1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }
}
