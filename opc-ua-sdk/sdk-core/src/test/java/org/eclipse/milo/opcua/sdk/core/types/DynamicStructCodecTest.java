/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.core.types;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ulong;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ushort;
import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.types.util.DynamicEncodingContext;
import org.eclipse.milo.opcua.sdk.core.types.util.StaticEncodingContext;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UByte;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.ULong;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelExType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessLevelType;
import org.eclipse.milo.opcua.stack.core.types.structured.AccessRestrictionType;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.eclipse.milo.opcua.test.types.ConcreteTestType;
import org.eclipse.milo.opcua.test.types.ConcreteTestTypeEx;
import org.eclipse.milo.opcua.test.types.StructWithAbstractArrayFields;
import org.eclipse.milo.opcua.test.types.StructWithAbstractMatrixFields;
import org.eclipse.milo.opcua.test.types.StructWithAbstractScalarFields;
import org.eclipse.milo.opcua.test.types.StructWithBuiltinArrayFields;
import org.eclipse.milo.opcua.test.types.StructWithBuiltinArrayFieldsEx;
import org.eclipse.milo.opcua.test.types.StructWithBuiltinMatrixFields;
import org.eclipse.milo.opcua.test.types.StructWithBuiltinMatrixFieldsEx;
import org.eclipse.milo.opcua.test.types.StructWithBuiltinScalarFields;
import org.eclipse.milo.opcua.test.types.StructWithBuiltinScalarFieldsEx;
import org.eclipse.milo.opcua.test.types.StructWithOptionalArrayFields;
import org.eclipse.milo.opcua.test.types.StructWithOptionalScalarFields;
import org.eclipse.milo.opcua.test.types.StructWithStructureArrayFields;
import org.eclipse.milo.opcua.test.types.StructWithStructureMatrixFields;
import org.eclipse.milo.opcua.test.types.StructWithStructureScalarFields;
import org.eclipse.milo.opcua.test.types.TestEnumType;
import org.eclipse.milo.opcua.test.types.UnionOfArray;
import org.eclipse.milo.opcua.test.types.UnionOfMatrix;
import org.eclipse.milo.opcua.test.types.UnionOfScalar;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class DynamicStructCodecTest {

  private final DynamicEncodingContext dynamicEncodingContext = new DynamicEncodingContext();
  private final StaticEncodingContext staticEncodingContext = new StaticEncodingContext();

  @Test
  void structWithScalarFields() {
    var struct =
        new StructWithBuiltinScalarFields(
            false,
            (byte) 0,
            ubyte(0),
            (short) 0,
            ushort(0),
            0,
            uint(0),
            0L,
            ulong(0L),
            0.0f,
            0.0d,
            "",
            DateTime.MIN_DATE_TIME,
            UUID.fromString("00000000-0000-0000-0000-000000000000"),
            ByteString.of(new byte[] {0}),
            XmlElement.of(""),
            new NodeId(0, 0),
            new ExpandedNodeId(ushort(0), null, uint(0)),
            StatusCode.GOOD,
            new QualifiedName(0, ""),
            LocalizedText.NULL_VALUE,
            new DataValue(new Variant(0)),
            Variant.ofInt32(0));

    var encoded1 = ExtensionObject.encode(staticEncodingContext, struct);
    DynamicStruct decoded = (DynamicStruct) encoded1.decode(dynamicEncodingContext);
    var encoded2 = ExtensionObject.encode(dynamicEncodingContext, decoded);

    assertEquals(encoded1, encoded2);

    var members = new LinkedHashMap<String, Object>();
    members.put("Boolean", struct.getBoolean());
    members.put("SByte", struct.getSByte());
    members.put("Byte", struct.getByte());
    members.put("Int16", struct.getInt16());
    members.put("UInt16", struct.getUInt16());
    members.put("Int32", struct.getInt32());
    members.put("UInt32", struct.getUInt32());
    members.put("Int64", struct.getInt64());
    members.put("UInt64", struct.getUInt64());
    members.put("Float", struct.getFloat());
    members.put("Double", struct.getDouble());
    members.put("String", struct.getString());
    members.put("DateTime", struct.getDateTime());
    members.put("Guid", struct.getGuid());
    members.put("ByteString", struct.getByteString());
    members.put("XmlElement", struct.getXmlElement());
    members.put("NodeId", struct.getNodeId());
    members.put("ExpandedNodeId", struct.getExpandedNodeId());
    members.put("StatusCode", struct.getStatusCode());
    members.put("QualifiedName", struct.getQualifiedName());
    members.put("LocalizedText", struct.getLocalizedText());
    members.put("DataValue", struct.getDataValue());
    members.put("Variant", struct.getVariant());
    DynamicStruct expected = new DynamicStruct(decoded.getDataType(), members);

    assertEquals(expected, decoded);
  }

  @Test
  void structWithScalarFieldsEx() {
    var struct =
        new StructWithBuiltinScalarFieldsEx(
            false,
            (byte) 0,
            ubyte(0),
            (short) 0,
            ushort(0),
            0,
            uint(0),
            0L,
            ulong(0L),
            0.0f,
            0.0d,
            "",
            DateTime.MIN_DATE_TIME,
            UUID.fromString("00000000-0000-0000-0000-000000000000"),
            ByteString.of(new byte[] {0}),
            XmlElement.of(""),
            new NodeId(0, 0),
            new ExpandedNodeId(ushort(0), null, uint(0)),
            StatusCode.GOOD,
            new QualifiedName(0, ""),
            LocalizedText.NULL_VALUE,
            new DataValue(new Variant(0)),
            Variant.ofInt32(0),
            0.0,
            ApplicationType.Server,
            TestEnumType.A,
            new XVType(0.0d, 0.0f),
            new ConcreteTestType((short) 0, 0.0, "", false),
            UnionOfScalar.ofBoolean(false),
            UnionOfArray.ofBoolean(new Boolean[] {false, false}),
            AccessLevelType.of(),
            AccessRestrictionType.of(),
            AccessLevelExType.of(),
            ulong(0));

    var encoded1 = ExtensionObject.encode(staticEncodingContext, struct);
    DynamicStruct decoded = (DynamicStruct) encoded1.decode(dynamicEncodingContext);
    var encoded2 = ExtensionObject.encode(dynamicEncodingContext, decoded);

    assertEquals(encoded1, encoded2);

    var members = new LinkedHashMap<String, Object>();
    members.put("Boolean", struct.getBoolean());
    members.put("SByte", struct.getSByte());
    members.put("Byte", struct.getByte());
    members.put("Int16", struct.getInt16());
    members.put("UInt16", struct.getUInt16());
    members.put("Int32", struct.getInt32());
    members.put("UInt32", struct.getUInt32());
    members.put("Int64", struct.getInt64());
    members.put("UInt64", struct.getUInt64());
    members.put("Float", struct.getFloat());
    members.put("Double", struct.getDouble());
    members.put("String", struct.getString());
    members.put("DateTime", struct.getDateTime());
    members.put("Guid", struct.getGuid());
    members.put("ByteString", struct.getByteString());
    members.put("XmlElement", struct.getXmlElement());
    members.put("NodeId", struct.getNodeId());
    members.put("ExpandedNodeId", struct.getExpandedNodeId());
    members.put("StatusCode", struct.getStatusCode());
    members.put("QualifiedName", struct.getQualifiedName());
    members.put("LocalizedText", struct.getLocalizedText());
    members.put("DataValue", struct.getDataValue());
    members.put("Variant", struct.getVariant());
    members.put("Duration", struct.getDuration());

    // Ideally we wouldn't just grab the same value decoded since it's the object we're testing but
    // these are difficult to construct right now without easy access to a corresponding DataType
    // for each field.
    members.put(
        "ApplicationType",
        new DynamicEnum(
            DynamicEncodingContext.APPLICATION_TYPE_DATA_TYPE, ApplicationType.Server.getValue()));
    members.put("TestEnumType", new DynamicEnum(dynamicEncodingContext.testEnumType, 0));
    members.put("XVType", decoded.getMembers().get("XVType"));
    members.put("ConcreteTestType", decoded.getMembers().get("ConcreteTestType"));
    members.put(
        "UnionOfScalar", DynamicUnion.of(dynamicEncodingContext.unionOfScalar, "Boolean", false));
    members.put("UnionOfArray", decoded.getMembers().get("UnionOfArray"));
    members.put("OptionSetUI8", decoded.getMembers().get("OptionSetUI8"));
    members.put("OptionSetUI16", decoded.getMembers().get("OptionSetUI16"));
    members.put("OptionSetUI32", decoded.getMembers().get("OptionSetUI32"));
    members.put("OptionSetUI64", decoded.getMembers().get("OptionSetUI64"));

    DynamicStruct expected = new DynamicStruct(decoded.getDataType(), members);

    assertEquals(expected, decoded);
  }

  @Test
  void structWithArrayFields() {
    var struct =
        new StructWithBuiltinArrayFields(
            new Boolean[] {false, false},
            new Byte[] {0, 0},
            new UByte[] {ubyte(0), ubyte(0)},
            new Short[] {0, 0},
            new UShort[] {ushort(0), ushort(0)},
            new Integer[] {0, 0},
            new UInteger[] {uint(0), uint(0)},
            new Long[] {0L, 0L},
            new ULong[] {ulong(0L), ulong(0L)},
            new Float[] {0.0f, 0.0f},
            new Double[] {0.0d, 0.0d},
            new String[] {"", ""},
            new DateTime[] {DateTime.MIN_DATE_TIME, DateTime.MIN_DATE_TIME},
            new UUID[] {
              UUID.fromString("00000000-0000-0000-0000-000000000000"),
              UUID.fromString("00000000-0000-0000-0000-000000000000")
            },
            new ByteString[] {ByteString.of(new byte[] {0}), ByteString.of(new byte[] {0})},
            new XmlElement[] {XmlElement.of(""), XmlElement.of("")},
            new NodeId[] {new NodeId(0, 0), new NodeId(0, 0)},
            new ExpandedNodeId[] {
              new ExpandedNodeId(ushort(0), null, uint(0)),
              new ExpandedNodeId(ushort(0), null, uint(0))
            },
            new StatusCode[] {StatusCode.GOOD, StatusCode.GOOD},
            new QualifiedName[] {new QualifiedName(0, ""), new QualifiedName(0, "")},
            new LocalizedText[] {LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE},
            new DataValue[] {new DataValue(new Variant(0)), new DataValue(new Variant(0))},
            new Variant[] {Variant.ofInt32(0), Variant.ofInt32(0)});

    var encoded1 = ExtensionObject.encode(staticEncodingContext, struct);
    DynamicStruct decoded = (DynamicStruct) encoded1.decode(dynamicEncodingContext);
    var encoded2 = ExtensionObject.encode(dynamicEncodingContext, decoded);

    assertEquals(encoded1, encoded2);
  }

  @Test
  void structWithArrayFieldsEx() {
    var struct =
        new StructWithBuiltinArrayFieldsEx(
            new Boolean[] {false, false},
            new Byte[] {0, 0},
            new UByte[] {ubyte(0), ubyte(0)},
            new Short[] {0, 0},
            new UShort[] {ushort(0), ushort(0)},
            new Integer[] {0, 0},
            new UInteger[] {uint(0), uint(0)},
            new Long[] {0L, 0L},
            new ULong[] {ulong(0L), ulong(0L)},
            new Float[] {0.0f, 0.0f},
            new Double[] {0.0d, 0.0d},
            new String[] {"", ""},
            new DateTime[] {DateTime.MIN_DATE_TIME, DateTime.MIN_DATE_TIME},
            new UUID[] {
              UUID.fromString("00000000-0000-0000-0000-000000000000"),
              UUID.fromString("00000000-0000-0000-0000-000000000000")
            },
            new ByteString[] {ByteString.of(new byte[] {0}), ByteString.of(new byte[] {0})},
            new XmlElement[] {XmlElement.of(""), XmlElement.of("")},
            new NodeId[] {new NodeId(0, 0), new NodeId(0, 0)},
            new ExpandedNodeId[] {
              new ExpandedNodeId(ushort(0), null, uint(0)),
              new ExpandedNodeId(ushort(0), null, uint(0))
            },
            new StatusCode[] {StatusCode.GOOD, StatusCode.GOOD},
            new QualifiedName[] {new QualifiedName(0, ""), new QualifiedName(0, "")},
            new LocalizedText[] {LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE},
            new DataValue[] {new DataValue(new Variant(0)), new DataValue(new Variant(0))},
            new Variant[] {Variant.ofInt32(0), Variant.ofInt32(0)},
            new Double[] {0.0, 0.0},
            new ApplicationType[] {ApplicationType.Server, ApplicationType.Client},
            new TestEnumType[] {TestEnumType.A, TestEnumType.B},
            new XVType[] {new XVType(0.0d, 0.0f), new XVType(0.0d, 0.0f)},
            new ConcreteTestType[] {
              new ConcreteTestType((short) 0, 0.0, "", false),
              new ConcreteTestType((short) 0, 0.0, "", false)
            },
            new UnionOfScalar[] {UnionOfScalar.ofBoolean(false), UnionOfScalar.ofByte(ubyte(0))},
            new UnionOfArray[] {
              UnionOfArray.ofBoolean(new Boolean[] {false, false}),
              UnionOfArray.ofSByte(new Byte[] {0, 0})
            },
            new AccessLevelType[] {AccessLevelType.of(), AccessLevelType.of()},
            new AccessRestrictionType[] {AccessRestrictionType.of(), AccessRestrictionType.of()},
            new AccessLevelExType[] {AccessLevelExType.of(), AccessLevelExType.of()},
            new ULong[] {ulong(0L), ulong(0L)});

    var encoded1 = ExtensionObject.encode(staticEncodingContext, struct);
    DynamicStruct decoded = (DynamicStruct) encoded1.decode(dynamicEncodingContext);
    var encoded2 = ExtensionObject.encode(dynamicEncodingContext, decoded);

    assertEquals(encoded1, encoded2);
  }

  @Test
  void structWithAbstractScalarFields() {
    var struct =
        new StructWithAbstractScalarFields(
            0,
            new ConcreteTestType((short) 0, 0.0, "", false),
            new ConcreteTestTypeEx((short) 0, 0.0, "", false, uint(0)));

    var encoded1 = ExtensionObject.encode(staticEncodingContext, struct);
    DynamicStruct decoded = (DynamicStruct) encoded1.decode(dynamicEncodingContext);
    var encoded2 = ExtensionObject.encode(dynamicEncodingContext, decoded);

    assertEquals(encoded1, encoded2);
  }

  @Test
  void structWithAbstractArrayFields() {
    var struct =
        new StructWithAbstractArrayFields(
            new Integer[] {0, 0},
            new ConcreteTestType[] {
              new ConcreteTestType((short) 0, 0.0, "", false),
              new ConcreteTestType((short) 0, 0.0, "", false)
            },
            new ConcreteTestTypeEx[] {
              new ConcreteTestTypeEx((short) 0, 0.0, "", false, uint(0)),
              new ConcreteTestTypeEx((short) 0, 0.0, "", false, uint(0))
            });

    var encoded1 = ExtensionObject.encode(staticEncodingContext, struct);
    DynamicStruct decoded = (DynamicStruct) encoded1.decode(dynamicEncodingContext);
    var encoded2 = ExtensionObject.encode(dynamicEncodingContext, decoded);

    assertEquals(encoded1, encoded2);
  }

  @Test
  void structWithAbstractMatrixFields() {
    var struct =
        new StructWithAbstractMatrixFields(
            Matrix.ofInt32(new Integer[][] {{0, 0}, {0, 0}}),
            Matrix.ofStruct(
                new ConcreteTestType[][] {
                  {
                    new ConcreteTestType((short) 0, 0.0, "", false),
                    new ConcreteTestType((short) 0, 0.0, "", false)
                  },
                  {
                    new ConcreteTestType((short) 0, 0.0, "", false),
                    new ConcreteTestType((short) 0, 0.0, "", false)
                  }
                }),
            Matrix.ofStruct(
                new ConcreteTestTypeEx[][] {
                  {
                    new ConcreteTestTypeEx((short) 0, 0.0, "", false, uint(0)),
                    new ConcreteTestTypeEx((short) 0, 0.0, "", false, uint(0))
                  },
                  {
                    new ConcreteTestTypeEx((short) 0, 0.0, "", false, uint(0)),
                    new ConcreteTestTypeEx((short) 0, 0.0, "", false, uint(0))
                  }
                }));

    var encoded1 = ExtensionObject.encode(staticEncodingContext, struct);
    DynamicStruct decoded = (DynamicStruct) encoded1.decode(dynamicEncodingContext);
    var encoded2 = ExtensionObject.encode(dynamicEncodingContext, decoded);

    assertEquals(encoded1, encoded2);
  }

  @ParameterizedTest
  @MethodSource("structWithOptionalScalarFieldsProvider")
  void structWithOptionalScalarFields(StructWithOptionalScalarFields struct) {
    var encoded1 = ExtensionObject.encode(staticEncodingContext, struct);
    DynamicStruct decoded = (DynamicStruct) encoded1.decode(dynamicEncodingContext);
    var encoded2 = ExtensionObject.encode(dynamicEncodingContext, decoded);

    assertEquals(encoded1, encoded2);
  }

  @ParameterizedTest
  @MethodSource("structWithOptionalArrayFieldsProvider")
  void structWithOptionalArrayFields(StructWithOptionalArrayFields struct) {
    var encoded1 = ExtensionObject.encode(staticEncodingContext, struct);
    DynamicStruct decoded = (DynamicStruct) encoded1.decode(dynamicEncodingContext);
    var encoded2 = ExtensionObject.encode(dynamicEncodingContext, decoded);

    assertEquals(encoded1, encoded2);
  }

  @Test
  void structWithMatrixFields() {
    var struct =
        new StructWithBuiltinMatrixFields(
            Matrix.ofBoolean(new Boolean[][] {{false, false}, {false, false}}),
            Matrix.ofSByte(new Byte[][] {{0, 0}, {0, 0}}),
            Matrix.ofByte(new UByte[][] {{ubyte(0), ubyte(0)}, {ubyte(0), ubyte(0)}}),
            Matrix.ofInt16(new Short[][] {{0, 0}, {0, 0}}),
            Matrix.ofUInt16(new UShort[][] {{ushort(0), ushort(0)}, {ushort(0), ushort(0)}}),
            Matrix.ofInt32(new Integer[][] {{0, 0}, {0, 0}}),
            Matrix.ofUInt32(new UInteger[][] {{uint(0), uint(0)}, {uint(0), uint(0)}}),
            Matrix.ofInt64(new Long[][] {{0L, 0L}, {0L, 0L}}),
            Matrix.ofUInt64(new ULong[][] {{ulong(0L), ulong(0L)}, {ulong(0L), ulong(0L)}}),
            Matrix.ofFloat(new Float[][] {{0.0f, 0.0f}, {0.0f, 0.0f}}),
            Matrix.ofDouble(new Double[][] {{0.0d, 0.0d}, {0.0d, 0.0d}}),
            Matrix.ofString(new String[][] {{"", ""}, {"", ""}}),
            Matrix.ofDateTime(
                new DateTime[][] {
                  {DateTime.MIN_DATE_TIME, DateTime.MIN_DATE_TIME},
                  {DateTime.MIN_DATE_TIME, DateTime.MIN_DATE_TIME}
                }),
            Matrix.ofGuid(
                new UUID[][] {
                  {
                    UUID.fromString("00000000-0000-0000-0000-000000000000"),
                    UUID.fromString("00000000-0000-0000-0000-000000000000")
                  },
                  {
                    UUID.fromString("00000000-0000-0000-0000-000000000000"),
                    UUID.fromString("00000000-0000-0000-0000-000000000000")
                  }
                }),
            Matrix.ofByteString(
                new ByteString[][] {
                  {ByteString.of(new byte[] {0}), ByteString.of(new byte[] {0})},
                  {ByteString.of(new byte[] {0}), ByteString.of(new byte[] {0})}
                }),
            Matrix.ofXmlElement(
                new XmlElement[][] {
                  {XmlElement.of(""), XmlElement.of("")},
                  {XmlElement.of(""), XmlElement.of("")}
                }),
            Matrix.ofNodeId(
                new NodeId[][] {
                  {new NodeId(0, 0), new NodeId(0, 0)},
                  {new NodeId(0, 0), new NodeId(0, 0)}
                }),
            Matrix.ofExpandedNodeId(
                new ExpandedNodeId[][] {
                  {
                    new ExpandedNodeId(ushort(0), null, uint(0)),
                    new ExpandedNodeId(ushort(0), null, uint(0))
                  },
                  {
                    new ExpandedNodeId(ushort(0), null, uint(0)),
                    new ExpandedNodeId(ushort(0), null, uint(0))
                  }
                }),
            Matrix.ofStatusCode(
                new StatusCode[][] {
                  {StatusCode.GOOD, StatusCode.GOOD},
                  {StatusCode.GOOD, StatusCode.GOOD}
                }),
            Matrix.ofQualifiedName(
                new QualifiedName[][] {
                  {new QualifiedName(0, ""), new QualifiedName(0, "")},
                  {new QualifiedName(0, ""), new QualifiedName(0, "")}
                }),
            Matrix.ofLocalizedText(
                new LocalizedText[][] {
                  {LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE},
                  {LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE}
                }),
            Matrix.ofDataValue(
                new DataValue[][] {
                  {new DataValue(new Variant(0)), new DataValue(new Variant(0))},
                  {new DataValue(new Variant(0)), new DataValue(new Variant(0))}
                }),
            Matrix.ofVariant(
                new Variant[][] {
                  {Variant.ofInt32(0), Variant.ofInt32(0)},
                  {Variant.ofInt32(0), Variant.ofInt32(0)}
                }));

    var encoded1 = ExtensionObject.encode(staticEncodingContext, struct);
    DynamicStruct decoded = (DynamicStruct) encoded1.decode(dynamicEncodingContext);
    var encoded2 = ExtensionObject.encode(dynamicEncodingContext, decoded);

    assertEquals(encoded1, encoded2);
  }

  @Test
  void structWithMatrixFieldsEx() {
    var struct =
        new StructWithBuiltinMatrixFieldsEx(
            Matrix.ofBoolean(new Boolean[][] {{false, false}, {false, false}}),
            Matrix.ofSByte(new Byte[][] {{0, 0}, {0, 0}}),
            Matrix.ofByte(new UByte[][] {{ubyte(0), ubyte(0)}, {ubyte(0), ubyte(0)}}),
            Matrix.ofInt16(new Short[][] {{0, 0}, {0, 0}}),
            Matrix.ofUInt16(new UShort[][] {{ushort(0), ushort(0)}, {ushort(0), ushort(0)}}),
            Matrix.ofInt32(new Integer[][] {{0, 0}, {0, 0}}),
            Matrix.ofUInt32(new UInteger[][] {{uint(0), uint(0)}, {uint(0), uint(0)}}),
            Matrix.ofInt64(new Long[][] {{0L, 0L}, {0L, 0L}}),
            Matrix.ofUInt64(new ULong[][] {{ulong(0L), ulong(0L)}, {ulong(0L), ulong(0L)}}),
            Matrix.ofFloat(new Float[][] {{0.0f, 0.0f}, {0.0f, 0.0f}}),
            Matrix.ofDouble(new Double[][] {{0.0d, 0.0d}, {0.0d, 0.0d}}),
            Matrix.ofString(new String[][] {{"", ""}, {"", ""}}),
            Matrix.ofDateTime(
                new DateTime[][] {
                  {DateTime.MIN_DATE_TIME, DateTime.MIN_DATE_TIME},
                  {DateTime.MIN_DATE_TIME, DateTime.MIN_DATE_TIME}
                }),
            Matrix.ofGuid(
                new UUID[][] {
                  {
                    UUID.fromString("00000000-0000-0000-0000-000000000000"),
                    UUID.fromString("00000000-0000-0000-0000-000000000000")
                  },
                  {
                    UUID.fromString("00000000-0000-0000-0000-000000000000"),
                    UUID.fromString("00000000-0000-0000-0000-000000000000")
                  }
                }),
            Matrix.ofByteString(
                new ByteString[][] {
                  {ByteString.of(new byte[] {0}), ByteString.of(new byte[] {0})},
                  {ByteString.of(new byte[] {0}), ByteString.of(new byte[] {0})}
                }),
            Matrix.ofXmlElement(
                new XmlElement[][] {
                  {XmlElement.of(""), XmlElement.of("")},
                  {XmlElement.of(""), XmlElement.of("")}
                }),
            Matrix.ofNodeId(
                new NodeId[][] {
                  {new NodeId(0, 0), new NodeId(0, 0)},
                  {new NodeId(0, 0), new NodeId(0, 0)}
                }),
            Matrix.ofExpandedNodeId(
                new ExpandedNodeId[][] {
                  {
                    new ExpandedNodeId(ushort(0), null, uint(0)),
                    new ExpandedNodeId(ushort(0), null, uint(0))
                  },
                  {
                    new ExpandedNodeId(ushort(0), null, uint(0)),
                    new ExpandedNodeId(ushort(0), null, uint(0))
                  }
                }),
            Matrix.ofStatusCode(
                new StatusCode[][] {
                  {StatusCode.GOOD, StatusCode.GOOD},
                  {StatusCode.GOOD, StatusCode.GOOD}
                }),
            Matrix.ofQualifiedName(
                new QualifiedName[][] {
                  {new QualifiedName(0, ""), new QualifiedName(0, "")},
                  {new QualifiedName(0, ""), new QualifiedName(0, "")}
                }),
            Matrix.ofLocalizedText(
                new LocalizedText[][] {
                  {LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE},
                  {LocalizedText.NULL_VALUE, LocalizedText.NULL_VALUE}
                }),
            Matrix.ofDataValue(
                new DataValue[][] {
                  {new DataValue(new Variant(0)), new DataValue(new Variant(0))},
                  {new DataValue(new Variant(0)), new DataValue(new Variant(0))}
                }),
            Matrix.ofVariant(
                new Variant[][] {
                  {Variant.ofInt32(0), Variant.ofInt32(0)},
                  {Variant.ofInt32(0), Variant.ofInt32(0)}
                }),
            Matrix.ofDouble(new Double[][] {{0.0, 0.0}, {0.0, 0.0}}),
            Matrix.ofEnum(
                new ApplicationType[][] {
                  {ApplicationType.Server, ApplicationType.Client},
                  {ApplicationType.Server, ApplicationType.Client}
                }),
            Matrix.ofEnum(
                new TestEnumType[][] {
                  {TestEnumType.A, TestEnumType.B},
                  {TestEnumType.A, TestEnumType.B}
                }),
            Matrix.ofStruct(
                new XVType[][] {
                  {new XVType(0.0d, 0.0f), new XVType(0.0d, 0.0f)},
                  {new XVType(0.0d, 0.0f), new XVType(0.0d, 0.0f)}
                }),
            Matrix.ofStruct(
                new ConcreteTestType[][] {
                  {
                    new ConcreteTestType((short) 0, 0.0, "", false),
                    new ConcreteTestType((short) 0, 0.0, "", false)
                  },
                  {
                    new ConcreteTestType((short) 0, 0.0, "", false),
                    new ConcreteTestType((short) 0, 0.0, "", false)
                  }
                }),
            Matrix.ofStruct(
                new UnionOfScalar[][] {
                  {UnionOfScalar.ofBoolean(false), UnionOfScalar.ofByte(ubyte(0))},
                  {UnionOfScalar.ofBoolean(false), UnionOfScalar.ofByte(ubyte(0))}
                }),
            Matrix.ofStruct(
                new UnionOfArray[][] {
                  {
                    UnionOfArray.ofBoolean(new Boolean[] {false, false}),
                    UnionOfArray.ofSByte(new Byte[] {0, 0})
                  },
                  {
                    UnionOfArray.ofBoolean(new Boolean[] {false, false}),
                    UnionOfArray.ofSByte(new Byte[] {0, 0})
                  }
                }),
            Matrix.ofOptionSetUI(
                new AccessLevelType[][] {
                  {AccessLevelType.of(), AccessLevelType.of()},
                  {AccessLevelType.of(), AccessLevelType.of()}
                }),
            Matrix.ofOptionSetUI(
                new AccessRestrictionType[][] {
                  {AccessRestrictionType.of(), AccessRestrictionType.of()},
                  {AccessRestrictionType.of(), AccessRestrictionType.of()}
                }),
            Matrix.ofOptionSetUI(
                new AccessLevelExType[][] {
                  {AccessLevelExType.of(), AccessLevelExType.of()},
                  {AccessLevelExType.of(), AccessLevelExType.of()}
                }),
            Matrix.ofUInt64(new ULong[][] {{ulong(0), ulong(0)}, {ulong(0), ulong(0)}}));

    var encoded1 = ExtensionObject.encode(staticEncodingContext, struct);
    DynamicStruct decoded = (DynamicStruct) encoded1.decode(dynamicEncodingContext);
    var encoded2 = ExtensionObject.encode(dynamicEncodingContext, decoded);

    assertEquals(encoded1, encoded2);
  }

  @Test
  void structWithStructureScalarFields() {
    var struct =
        new StructWithStructureScalarFields(
            ExtensionObject.encode(staticEncodingContext, new XVType(0.0d, 0.0f)),
            ExtensionObject.encode(
                staticEncodingContext, new ConcreteTestType((short) 0, 0.0, "", false)),
            ExtensionObject.encode(
                staticEncodingContext, new ConcreteTestTypeEx((short) 0, 0.0, "", false, uint(0))));

    var encoded1 = ExtensionObject.encode(staticEncodingContext, struct);
    DynamicStruct decoded = (DynamicStruct) encoded1.decode(dynamicEncodingContext);
    var encoded2 = ExtensionObject.encode(dynamicEncodingContext, decoded);

    assertEquals(encoded1, encoded2);
  }

  @Test
  void structWithStructureArrayFields() {
    var xo1 = ExtensionObject.encode(staticEncodingContext, new XVType(0.0d, 0.0f));
    var xo2 =
        ExtensionObject.encode(
            staticEncodingContext, new ConcreteTestType((short) 0, 0.0, "", false));
    var xo3 =
        ExtensionObject.encode(
            staticEncodingContext, new ConcreteTestTypeEx((short) 0, 0.0, "", false, uint(0)));
    var struct =
        new StructWithStructureArrayFields(
            new ExtensionObject[] {xo1, xo1},
            new ExtensionObject[] {xo2, xo2},
            new ExtensionObject[] {xo3, xo3});

    var encoded1 = ExtensionObject.encode(staticEncodingContext, struct);
    DynamicStruct decoded = (DynamicStruct) encoded1.decode(dynamicEncodingContext);
    var encoded2 = ExtensionObject.encode(dynamicEncodingContext, decoded);

    assertEquals(encoded1, encoded2);
  }

  @Test
  void structWithStructureMatrixFields() {
    var xo1 = ExtensionObject.encode(staticEncodingContext, new XVType(0.0d, 0.0f));
    var xo2 =
        ExtensionObject.encode(
            staticEncodingContext, new ConcreteTestType((short) 0, 0.0, "", false));
    var xo3 =
        ExtensionObject.encode(
            staticEncodingContext, new ConcreteTestTypeEx((short) 0, 0.0, "", false, uint(0)));
    var struct =
        new StructWithStructureMatrixFields(
            Matrix.ofExtensionObject(new ExtensionObject[][] {{xo1, xo1}, {xo1, xo1}}),
            Matrix.ofExtensionObject(new ExtensionObject[][] {{xo2, xo2}, {xo2, xo2}}),
            Matrix.ofExtensionObject(new ExtensionObject[][] {{xo3, xo3}, {xo3, xo3}}));

    var encoded1 = ExtensionObject.encode(staticEncodingContext, struct);
    DynamicStruct decoded = (DynamicStruct) encoded1.decode(dynamicEncodingContext);
    var encoded2 = ExtensionObject.encode(dynamicEncodingContext, decoded);

    assertEquals(encoded1, encoded2);
  }

  @ParameterizedTest
  @MethodSource("unionOfScalarProvider")
  void unionOfScalar(UnionOfScalar union) {
    var encoded1 = ExtensionObject.encode(staticEncodingContext, union);
    DynamicStruct decoded = (DynamicStruct) encoded1.decode(dynamicEncodingContext);
    var encoded2 = ExtensionObject.encode(dynamicEncodingContext, decoded);

    assertEquals(encoded1, encoded2);
  }

  @ParameterizedTest
  @MethodSource("unionOfArrayProvider")
  void unionOfArray(UnionOfArray union) {
    var encoded1 = ExtensionObject.encode(staticEncodingContext, union);
    DynamicStruct decoded = (DynamicStruct) encoded1.decode(dynamicEncodingContext);
    var encoded2 = ExtensionObject.encode(dynamicEncodingContext, decoded);

    assertEquals(encoded1, encoded2);
  }

  @ParameterizedTest
  @MethodSource("unionOfMatrixProvider")
  void unionOfMatrix(UnionOfMatrix union) {
    var encoded1 = ExtensionObject.encode(staticEncodingContext, union);
    DynamicStruct decoded = (DynamicStruct) encoded1.decode(dynamicEncodingContext);
    var encoded2 = ExtensionObject.encode(dynamicEncodingContext, decoded);

    assertEquals(encoded1, encoded2);
  }

  private static Stream<Arguments> structWithOptionalScalarFieldsProvider() {
    return Stream.of(
        Arguments.of(
            new StructWithOptionalScalarFields(
                "",
                "",
                0,
                0,
                0.0,
                0.0,
                new ConcreteTestType((short) 0, 0.0, "", false),
                new ConcreteTestType((short) 0, 0.0, "", false))),
        Arguments.of(
            new StructWithOptionalScalarFields(
                "",
                null,
                0,
                null,
                0.0,
                null,
                new ConcreteTestType((short) 0, 0.0, "", false),
                null)));
  }

  private static Stream<Arguments> structWithOptionalArrayFieldsProvider() {
    return Stream.of(
        Arguments.of(
            new StructWithOptionalArrayFields(
                new Integer[] {0, 0},
                new Integer[] {0, 0},
                new String[] {"", ""},
                new String[] {"", ""},
                new Double[] {0.0, 0.0},
                new Double[] {0.0, 0.0},
                new ConcreteTestType[] {
                  new ConcreteTestType((short) 0, 0.0, "", false),
                  new ConcreteTestType((short) 0, 0.0, "", false)
                },
                new ConcreteTestType[] {
                  new ConcreteTestType((short) 0, 0.0, "", false),
                  new ConcreteTestType((short) 0, 0.0, "", false)
                })),
        Arguments.of(
            new StructWithOptionalArrayFields(
                new Integer[] {0, 0},
                null,
                new String[] {"", ""},
                null,
                new Double[] {0.0, 0.0},
                null,
                new ConcreteTestType[] {
                  new ConcreteTestType((short) 0, 0.0, "", false),
                  new ConcreteTestType((short) 0, 0.0, "", false)
                },
                null)));
  }

  private static Stream<Arguments> unionOfScalarProvider() {
    return Stream.of(
        Arguments.of(UnionOfScalar.ofNull()),
        Arguments.of(UnionOfScalar.ofBoolean(false)),
        Arguments.of(UnionOfScalar.ofSByte((byte) 0)),
        Arguments.of(UnionOfScalar.ofByte(ubyte(0))));
  }

  private static Stream<Arguments> unionOfArrayProvider() {
    return Stream.of(
        Arguments.of(UnionOfArray.ofNull()),
        Arguments.of(UnionOfArray.ofBoolean(new Boolean[] {false, false})),
        Arguments.of(UnionOfArray.ofSByte(new Byte[] {0, 0})),
        Arguments.of(UnionOfArray.ofByte(new UByte[] {ubyte(0), ubyte(0)})));
  }

  private static Stream<Arguments> unionOfMatrixProvider() {
    return Stream.of(
        Arguments.of(UnionOfMatrix.ofNull()),
        Arguments.of(
            UnionOfMatrix.ofBoolean(
                Matrix.ofBoolean(new Boolean[][] {{false, false}, {false, false}}))),
        Arguments.of(UnionOfMatrix.ofSByte(Matrix.ofSByte(new Byte[][] {{0, 0}, {0, 0}}))),
        Arguments.of(
            UnionOfMatrix.ofByte(
                Matrix.ofByte(new UByte[][] {{ubyte(0), ubyte(0)}, {ubyte(0), ubyte(0)}}))));
  }
}
