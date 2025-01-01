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
import org.eclipse.milo.opcua.stack.core.types.structured.StructureDefinition;
import org.eclipse.milo.opcua.stack.core.types.structured.StructureField;
import org.eclipse.milo.opcua.stack.core.util.codegen.EqualsBuilder;
import org.eclipse.milo.opcua.stack.core.util.codegen.HashCodeBuilder;
import org.jspecify.annotations.Nullable;

public class ConcreteTestType extends AbstractTestType implements UaStructuredType {
  public static final ExpandedNodeId TYPE_ID = ExpandedNodeId.parse("ns=1;i=3006");

  public static final ExpandedNodeId BINARY_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5001");

  public static final ExpandedNodeId JSON_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5003");

  public static final ExpandedNodeId XML_ENCODING_ID = ExpandedNodeId.parse("ns=1;i=5002");

  private final Boolean booleanField;

  public ConcreteTestType(
      Short int16Field, Double doubleField, @Nullable String stringField, Boolean booleanField) {
    super(int16Field, doubleField, stringField);
    this.booleanField = booleanField;
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
  public ExpandedNodeId getJsonEncodingId() {
    return JSON_ENCODING_ID;
  }

  @Override
  public ExpandedNodeId getXmlEncodingId() {
    return XML_ENCODING_ID;
  }

  public Boolean getBooleanField() {
    return booleanField;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    } else if (object == null || getClass() != object.getClass()) {
      return false;
    }
    ConcreteTestType that = (ConcreteTestType) object;
    var eqb = new EqualsBuilder();
    eqb.appendSuper(super.equals(object));
    eqb.append(getBooleanField(), that.getBooleanField());
    return eqb.build();
  }

  @Override
  public int hashCode() {
    var hcb = new HashCodeBuilder();
    hcb.append(getBooleanField());
    hcb.appendSuper(super.hashCode());
    return hcb.build();
  }

  @Override
  public String toString() {
    var joiner = new StringJoiner(", ", ConcreteTestType.class.getSimpleName() + "[", "]");
    joiner.add("booleanField=" + getBooleanField());
    return joiner.toString();
  }

  public static StructureDefinition definition(NamespaceTable namespaceTable) {
    return new StructureDefinition(
        ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=5001")
            .toNodeId(namespaceTable)
            .orElseThrow(),
        ExpandedNodeId.parse("nsu=https://github.com/eclipse/milo/DataTypeTest;i=3003")
            .toNodeId(namespaceTable)
            .orElseThrow(),
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
              false),
          new StructureField(
              "BooleanField",
              LocalizedText.NULL_VALUE,
              new NodeId(0, 1),
              -1,
              null,
              UInteger.valueOf(0),
              false)
        });
  }

  public static final class Codec extends GenericDataTypeCodec<ConcreteTestType> {
    @Override
    public Class<ConcreteTestType> getType() {
      return ConcreteTestType.class;
    }

    @Override
    public ConcreteTestType decodeType(EncodingContext context, UaDecoder decoder) {
      final Short int16Field;
      final Double doubleField;
      final String stringField;
      final Boolean booleanField;
      int16Field = decoder.decodeInt16("Int16Field");
      doubleField = decoder.decodeDouble("DoubleField");
      stringField = decoder.decodeString("StringField");
      booleanField = decoder.decodeBoolean("BooleanField");
      return new ConcreteTestType(int16Field, doubleField, stringField, booleanField);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, ConcreteTestType value) {
      encoder.encodeInt16("Int16Field", value.getInt16Field());
      encoder.encodeDouble("DoubleField", value.getDoubleField());
      encoder.encodeString("StringField", value.getStringField());
      encoder.encodeBoolean("BooleanField", value.getBooleanField());
    }
  }
}
