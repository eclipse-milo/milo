/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.test.types;

import java.util.StringJoiner;
import org.eclipse.milo.opcua.stack.core.NamespaceTable;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.StructureType;
import org.eclipse.milo.opcua.stack.core.types.structured.Structure;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

public abstract class AbstractTestType extends Structure implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=1;i=3003");

  private final Short int16Field;

  private final Double doubleField;

  private final @Nullable String stringField;

  public AbstractTestType(Short int16Field, Double doubleField, @Nullable String stringField) {
    this.int16Field = int16Field;
    this.doubleField = doubleField;
    this.stringField = stringField;
  }

  @Override
  public ExpandedNodeId getTypeId() {
    return TYPE_ID;
  }

  public Short getInt16Field() {
    return int16Field;
  }

  public Double getDoubleField() {
    return doubleField;
  }

  public @Nullable String getStringField() {
    return stringField;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    AbstractTestType that = (AbstractTestType) object;
    var eqb = new EqualsBuilder();
    eqb.append(getInt16Field(), that.getInt16Field());
    eqb.append(getDoubleField(), that.getDoubleField());
    eqb.append(getStringField(), that.getStringField());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getInt16Field());
    hcb.append(getDoubleField());
    hcb.append(getStringField());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", AbstractTestType.class.getSimpleName() + "[", "]");
    joiner.add("int16Field=" + getInt16Field());
    joiner.add("doubleField=" + getDoubleField());
    joiner.add("stringField='" + getStringField() + "'");
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        new NodeId(0, 0),
        new NodeId(0, 22),
        StructureType.Structure,
        new StructureField[] {
          new StructureField(
              "Int16Field",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 4),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "DoubleField",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 11),
              -1,
              null,
              UInteger.valueOf(0),
              false),
          new StructureField(
              "StringField",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 12),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }
}
