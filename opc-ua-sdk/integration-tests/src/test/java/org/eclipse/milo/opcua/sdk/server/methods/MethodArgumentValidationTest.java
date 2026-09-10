/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.milo.opcua.sdk.server.methods;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;
import org.eclipse.milo.opcua.sdk.core.Reference;
import org.eclipse.milo.opcua.sdk.server.AccessContext;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.nodes.UaDataTypeNode;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.sdk.test.TestNamespace;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.binary.OpcUaBinaryEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDVector;
import org.eclipse.milo.opcua.stack.core.types.structured.Union;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;

class MethodArgumentValidationTest extends AbstractClientServerTest {

  private static final NodeId VARIANT_BACKED_TYPE_ID = new NodeId(2, "VariantBackedValue");

  record Case(NodeId type, int rank, UInteger[] dimensions, Object value, boolean valid) {
    @Override
    public String toString() {
      return "type=%s rank=%d dimensions=%s value=%s valid=%s"
          .formatted(
              type.toParseableString(),
              rank,
              Arrays.toString(dimensions),
              Arrays.deepToString(new Object[] {value}),
              valid);
    }
  }

  @Override
  protected void configureTestNamespace(TestNamespace namespace) {
    namespace.configure(
        (context, nodeManager) -> {
          var variantTypeNode =
              new UaDataTypeNode(
                  context,
                  VARIANT_BACKED_TYPE_ID,
                  newQualifiedName("VariantBackedValue"),
                  LocalizedText.english("VariantBackedValue"),
                  LocalizedText.NULL_VALUE,
                  uint(0),
                  uint(0),
                  true);
          variantTypeNode.addReference(
              new Reference(
                  VARIANT_BACKED_TYPE_ID,
                  NodeIds.HasSubtype,
                  NodeIds.BaseDataType.expanded(),
                  Reference.Direction.INVERSE));
          nodeManager.addNode(variantTypeNode);

          NodeId typeId = UnionOfScalar.TYPE_ID;
          var typeNode =
              new UaDataTypeNode(
                  context,
                  typeId,
                  newQualifiedName("UnionOfScalar"),
                  LocalizedText.english("UnionOfScalar"),
                  LocalizedText.NULL_VALUE,
                  uint(0),
                  uint(0),
                  false);
          typeNode.addReference(
              new Reference(
                  typeId,
                  NodeIds.HasSubtype,
                  NodeIds.Union.expanded(),
                  Reference.Direction.INVERSE));
          nodeManager.addNode(typeNode);
          server
              .getStaticDataTypeManager()
              .registerType(
                  typeId, new UnionOfScalar.Codec(), UnionOfScalar.BINARY_ENCODING_ID, null, null);
        });
    server.updateDataTypeTree();
  }

  // Unions are Structure subtypes and must be validated by their concrete decoded type.
  @TestFactory
  Stream<DynamicTest> validatesWireDecodedUnions() {
    NodeId typeId = UnionOfScalar.TYPE_ID;
    UnionOfScalar union = new UnionOfScalar(true);
    return Stream.of(
            new Case(typeId, -1, null, union, true),
            new Case(NodeIds.Union, -1, null, union, true),
            new Case(NodeIds.Union, 1, null, new UnionOfScalar[] {union}, true),
            new Case(
                NodeIds.Union,
                2,
                null,
                new Matrix(new UnionOfScalar[] {union}, new int[] {1, 1}),
                true),
            new Case(
                typeId, 2, null, new Matrix(new UnionOfScalar[] {union}, new int[] {1, 1}), true),
            new Case(NodeIds.XVType, -1, null, union, false),
            new Case(NodeIds.Union, -1, null, new XVType(1.0, 2.0f), false))
        .map(c -> DynamicTest.dynamicTest(c.toString(), () -> assertValidation(c)));
  }

  // BaseDataType describes the Variant's payload, not a nested Java Variant wrapper.
  @Test
  void acceptsBaseDataTypeStringThroughPublicInvocation() {
    assertBaseDataTypeValidation(baseValue(-1, "hello"), true);
  }

  @TestFactory
  Stream<DynamicTest> acceptsWireDecodedBaseDataTypeValues() {
    return baseDataTypeValues()
        .map(
            c ->
                DynamicTest.dynamicTest(c.toString(), () -> assertBaseDataTypeValidation(c, true)));
  }

  // Local calls retain concrete Java structures and enums, which wire decoding reduces to builtins.
  @TestFactory
  Stream<DynamicTest> acceptsLocalBaseDataTypeValues() {
    var xv = new XVType(1.0, 2.0f);
    return Stream.concat(
            baseDataTypeValues(),
            Stream.of(
                baseValue(1, new XVType[] {null, xv}),
                baseValue(1, new UaStructuredType[] {null, xv}),
                baseValue(2, new Matrix(new XVType[] {null, xv}, new int[] {1, 2})),
                baseValue(2, new Matrix(new UaStructuredType[] {null, xv}, new int[] {1, 2})),
                // Local only: Part 6 5.2.2.16 encodes an empty Matrix as an empty array with no
                // dimensions, so the wire form arrives as a one-dimensional empty array.
                baseValue(2, new Matrix(new String[0], new int[] {0, 2})),
                baseValue(2, new Matrix(new XVType[0], new int[] {0, 2})),
                baseValue(-1, Matrix.ofNull())))
        .map(
            c ->
                DynamicTest.dynamicTest(
                    c.toString(), () -> assertBaseDataTypeValidation(c, false)));
  }

  static Stream<Case> baseDataTypeValues() {
    var xv = new XVType(1.0, 2.0f);
    var vector = new ThreeDVector(1.0, 2.0, 3.0);
    ExtensionObject nullStructure = ExtensionObject.of(ByteString.NULL_VALUE, NodeId.NULL_VALUE);
    ExtensionObject encoded = ExtensionObject.encode(DefaultEncodingContext.INSTANCE, xv);
    return Stream.of(
        baseValue(-1, "hello"),
        baseValue(-1, 42),
        baseValue(-1, 42.5),
        baseValue(-1, uint(42)),
        baseValue(-1, ByteString.of(new byte[] {1, 2})),
        baseValue(-1, xv),
        baseValue(-1, ServerState.Running),
        baseValue(-1, null),
        baseValue(-3, "hello"),
        baseValue(-3, new String[] {"hello"}),
        baseValue(-2, "hello"),
        baseValue(-2, new String[] {"hello"}),
        baseValue(-2, new Matrix(new String[] {"hello"}, new int[] {1, 1})),
        baseValue(0, new String[0]),
        baseValue(0, new Matrix(new String[] {"hello"}, new int[] {1, 1})),
        baseValue(1, new String[] {null, "hello"}),
        baseValue(1, new String[0]),
        baseValue(1, new String[] {null}),
        baseValue(1, new int[] {1, 2}),
        baseValue(1, new Double[] {1.0, 2.0}),
        baseValue(1, new XVType[] {xv}),
        baseValue(1, new XVType[0]),
        baseValue(1, new UaStructuredType[] {xv, vector}),
        baseValue(1, new ExtensionObject[] {nullStructure, encoded}),
        baseValue(1, new ExtensionObject[] {nullStructure}),
        baseValue(1, new ServerState[] {ServerState.Running, ServerState.Suspended}),
        baseValue(1, new ServerState[0]),
        baseValue(
            1, new Variant[] {Variant.ofString("hello"), Variant.ofInt32(42), Variant.NULL_VALUE}),
        baseValue(1, null),
        baseValue(2, new Matrix(new String[] {null, "hello"}, new int[] {1, 2})),
        baseValue(2, new Matrix(new int[] {1, 2}, new int[] {1, 2})),
        baseValue(2, new Matrix(new XVType[] {xv}, new int[] {1, 1})),
        baseValue(2, new Matrix(new UaStructuredType[] {xv, vector}, new int[] {1, 2})),
        baseValue(2, new Matrix(new ExtensionObject[] {nullStructure, encoded}, new int[] {1, 2})),
        baseValue(2, new Matrix(new ServerState[] {ServerState.Running}, new int[] {1, 1})),
        baseValue(
            2,
            new Matrix(
                new Variant[] {Variant.ofString("hello"), Variant.ofInt32(42)}, new int[] {1, 2})),
        baseValue(2, null));
  }

  private static Case baseValue(int rank, Object value) {
    return new Case(NodeIds.BaseDataType, rank, null, value, true);
  }

  private void assertBaseDataTypeValidation(Case c, boolean wire) {
    Variant input = wire ? wireValue(c.value) : new Variant(c.value);
    RecordingHandler handler = assertValidation(c, input);
    Variant expected =
        c.value instanceof Matrix matrix && matrix.isNull() ? Variant.NULL_VALUE : input;
    assertSame(
        expected, handler.received[0], "BaseDataType must preserve the payload representation");
  }

  // Accepting BaseDataType must still enforce declared shapes and narrower data types.
  @TestFactory
  Stream<DynamicTest> rejectsIncompatibleBaseDataTypeShapesAndNarrowerTypes() {
    return Stream.of(
            new Case(NodeIds.BaseDataType, -1, null, new String[] {"hello"}, false),
            new Case(NodeIds.BaseDataType, 1, null, "hello", false),
            new Case(
                NodeIds.BaseDataType,
                -3,
                null,
                new Matrix(new String[] {"hello"}, new int[] {1, 1}),
                false),
            new Case(NodeIds.BaseDataType, 0, null, "hello", false),
            new Case(
                NodeIds.BaseDataType,
                3,
                null,
                new Matrix(new String[] {"hello"}, new int[] {1, 1}),
                false),
            new Case(
                NodeIds.BaseDataType, 1, new UInteger[] {uint(1)}, new String[] {"a", "b"}, false),
            new Case(
                NodeIds.BaseDataType,
                2,
                new UInteger[] {uint(1), uint(1)},
                new Matrix(new String[] {"a", "b"}, new int[] {1, 2}),
                false),
            new Case(NodeIds.Int32, -1, null, "hello", false),
            new Case(NodeIds.Number, 1, null, new String[] {"hello"}, false),
            new Case(NodeIds.Integer, -1, null, 1.0, false),
            new Case(NodeIds.Integer, -1, null, uint(1), false),
            new Case(NodeIds.UInteger, -1, null, 1, false),
            new Case(NodeIds.XVType, -1, null, "hello", false))
        .map(c -> DynamicTest.dynamicTest(c.toString(), () -> assertValidation(c)));
  }

  // The unchecked Variant constructor can carry objects which have no valid UA Variant encoding.
  @TestFactory
  Stream<DynamicTest> rejectsUnsupportedLocalBaseDataTypePayloads() {
    return Stream.of(
            new Object(),
            new Object[] {"hello"},
            Variant.ofString("hello"),
            DiagnosticInfo.NULL_VALUE,
            new Matrix(new DiagnosticInfo[] {DiagnosticInfo.NULL_VALUE}, new int[] {1, 1}),
            new Matrix(new Object[] {"hello"}, new int[] {1, 1}, OpcUaDataType.ExtensionObject))
        .map(value -> new Case(NodeIds.BaseDataType, -2, null, value, false))
        .map(
            c ->
                DynamicTest.dynamicTest(
                    c.toString(), () -> assertValidation(c, new Variant(c.value))));
  }

  // Vendor abstract types with Variant backing must accept payloads while retaining shape checks.
  @TestFactory
  Stream<DynamicTest> validatesVariantBackedAbstractDataTypeValues() {
    return Stream.of(
            new Case(VARIANT_BACKED_TYPE_ID, -1, null, "hello", true),
            new Case(VARIANT_BACKED_TYPE_ID, -1, null, 42, true),
            new Case(VARIANT_BACKED_TYPE_ID, 1, null, new String[] {"hello"}, true),
            new Case(
                VARIANT_BACKED_TYPE_ID,
                2,
                null,
                new Matrix(new int[] {1, 2}, new int[] {1, 2}),
                true),
            new Case(VARIANT_BACKED_TYPE_ID, -1, null, new XVType(1.0, 2.0f), true),
            new Case(VARIANT_BACKED_TYPE_ID, -1, null, ServerState.Running, true),
            new Case(VARIANT_BACKED_TYPE_ID, -1, null, null, true),
            new Case(VARIANT_BACKED_TYPE_ID, -1, null, new String[] {"hello"}, false),
            new Case(
                VARIANT_BACKED_TYPE_ID,
                1,
                new UInteger[] {uint(1)},
                new String[] {"a", "b"},
                false))
        .map(c -> DynamicTest.dynamicTest(c.toString(), () -> assertValidation(c)));
  }

  // Variant-backed declarations still reject Java values without a UA Variant representation.
  @Test
  void rejectsUnsupportedLocalVariantBackedAbstractDataTypeValue() {
    Object value = new Object();
    assertValidation(new Case(VARIANT_BACKED_TYPE_ID, -1, null, value, false), new Variant(value));
  }

  // An unchecked local Matrix can carry invalid element classes; return UA statuses, not Java
  // errors.
  @Test
  void rejectsLocalMatrixElementsInBaseDataTypeMatrix() {
    var nested = new Matrix(new int[] {42}, new int[] {1, 1});
    var matrix = new Matrix(new Matrix[] {nested}, new int[] {1, 1}, OpcUaDataType.Variant);
    assertValidation(new Case(NodeIds.BaseDataType, 2, null, matrix, false), new Variant(matrix));
  }

  // Part 3 8.6: ranks constrain shape, and dimensions are maxima, not exact lengths.
  @TestFactory
  Stream<DynamicTest> validatesWireDecodedShapes() {
    return shapes().map(c -> DynamicTest.dynamicTest(c.toString(), () -> assertValidation(c)));
  }

  static Stream<Case> shapes() {
    return Stream.of(
        shape(-1, 1, true),
        shape(-1, new Integer[] {1}, false),
        shape(1, 1, false),
        shape(1, new Integer[0], true),
        shape(1, new Matrix(new Integer[] {1, 2}, new int[] {1, 2}), false),
        shape(-3, 1, true),
        shape(-3, new Integer[] {1}, true),
        shape(-3, new Matrix(new Integer[] {1, 2}, new int[] {1, 2}), false),
        shape(-2, 1, true),
        shape(-2, new Integer[] {1}, true),
        shape(-2, new Matrix(new Integer[] {1, 2}, new int[] {1, 2}), true),
        shape(0, 1, false),
        shape(0, new Integer[0], true),
        shape(0, new Integer[] {1}, true),
        shape(0, new Matrix(new Integer[] {1, 2}, new int[] {1, 2}), true),
        shape(2, 1, false),
        shape(2, new Integer[] {1}, false),
        shape(2, new Matrix(new Integer[] {1, 2}, new int[] {1, 2}), true),
        shape(3, new Matrix(new Integer[] {1, 2}, new int[] {1, 2}), false),
        shape(3, new Matrix(new Integer[] {1, 2}, new int[] {1, 1, 2}), true),
        shape(-1, null, true),
        shape(1, null, true),
        shape(2, null, true),
        shape(-3, null, true),
        shape(-2, null, true),
        shape(0, null, true),
        shape(1, new UInteger[] {uint(2)}, new Integer[] {1}, true),
        shape(1, new UInteger[] {uint(2)}, new Integer[0], true),
        shape(2, new UInteger[] {uint(1), uint(1)}, null, true),
        shape(1, new UInteger[] {uint(2)}, new Integer[] {1, 2}, true),
        shape(1, new UInteger[] {uint(1)}, new Integer[] {1, 2}, false),
        shape(1, new UInteger[] {UInteger.MAX}, new Integer[] {1, 2}, true),
        shape(
            2,
            new UInteger[] {uint(0), uint(2)},
            new Matrix(new Integer[6], new int[] {3, 2}),
            true),
        shape(
            2,
            new UInteger[] {uint(0), uint(1)},
            new Matrix(new Integer[6], new int[] {3, 2}),
            false),
        new Case(NodeIds.ByteString, -1, null, ByteString.of(new byte[] {1, 2}), true),
        new Case(NodeIds.ByteString, 1, null, ByteString.of(new byte[] {1, 2}), false),
        new Case(NodeIds.ByteString, -1, null, ByteString.NULL_VALUE, true),
        new Case(
            NodeIds.ByteString,
            1,
            new UInteger[] {uint(1)},
            new ByteString[] {ByteString.of(new byte[] {1, 2})},
            true),
        new Case(NodeIds.Number, 1, null, new Integer[] {1, 2}, true),
        new Case(
            NodeIds.Duration, 2, null, new Matrix(new Double[] {1.0}, new int[] {1, 1}), true));
  }

  private static Case shape(int rank, Object value, boolean valid) {
    return shape(rank, null, value, valid);
  }

  private static Case shape(int rank, UInteger[] dimensions, Object value, boolean valid) {
    return new Case(NodeIds.Int32, rank, dimensions, value, valid);
  }

  // Wire decoding loses the concrete structure type of each ExtensionObject, including in matrices.
  @TestFactory
  Stream<DynamicTest> validatesEveryWireDecodedStructure() {
    return structures().map(c -> DynamicTest.dynamicTest(c.toString(), () -> assertValidation(c)));
  }

  static Stream<Case> structures() {
    var xv = new XVType(1.0, 2.0f);
    var vector = new ThreeDVector(1.0, 2.0, 3.0);
    return Stream.of(
        new Case(NodeIds.XVType, -1, null, xv, true),
        new Case(NodeIds.XVType, -1, null, vector, false),
        new Case(NodeIds.XVType, 1, null, new XVType[] {xv}, true),
        new Case(NodeIds.XVType, 1, null, new XVType[0], true),
        new Case(NodeIds.Structure, 1, null, new UaStructuredType[] {xv, vector}, true),
        new Case(NodeIds.Vector, -1, null, vector, true),
        new Case(NodeIds.XVType, 2, null, new Matrix(new XVType[] {xv}, new int[] {1, 1}), true),
        new Case(
            NodeIds.Structure,
            2,
            null,
            new Matrix(new UaStructuredType[] {xv, vector}, new int[] {1, 2}),
            true),
        new Case(
            NodeIds.XVType,
            2,
            null,
            new Matrix(new UaStructuredType[] {xv, vector}, new int[] {1, 2}),
            false),
        new Case(
            NodeIds.XVType,
            1,
            null,
            new ExtensionObject[] {
              ExtensionObject.encode(DefaultEncodingContext.INSTANCE, xv),
              ExtensionObject.of(ByteString.NULL_VALUE, NodeId.NULL_VALUE)
            },
            true),
        new Case(
            NodeIds.XVType,
            -1,
            null,
            ExtensionObject.of(ByteString.NULL_VALUE, NodeId.NULL_VALUE),
            true),
        new Case(
            NodeIds.XVType,
            -1,
            null,
            ExtensionObject.of(
                ByteString.of(new byte[] {1}), NodeIds.XVType_Encoding_DefaultBinary),
            false),
        new Case(
            NodeIds.XVType,
            -1,
            null,
            ExtensionObject.of(ByteString.of(new byte[] {1}), new NodeId(2, "unknown")),
            false),
        new Case(NodeIds.XVType, -1, null, 1, false),
        new Case(NodeIds.XVType, 2, null, new Matrix(new Integer[] {1}, new int[] {1, 1}), false),
        new Case(
            NodeIds.Vector,
            2,
            null,
            new Matrix(new ThreeDVector[] {vector}, new int[] {1, 1}),
            true),
        new Case(
            NodeIds.XVType,
            2,
            null,
            new Matrix(
                new ExtensionObject[] {
                  ExtensionObject.of(ByteString.NULL_VALUE, NodeId.NULL_VALUE)
                },
                new int[] {1, 1}),
            true));
  }

  private void assertValidation(Case c) {
    assertValidation(c, wireValue(c.value));
  }

  private RecordingHandler assertValidation(Case c, Variant input) {
    var argument = new Argument("Input", c.type, c.rank, c.dimensions, LocalizedText.NULL_VALUE);
    var handler = new RecordingHandler(argument);
    CallMethodRequest request = handler.request(input);

    CallMethodResult result = handler.invoke(AccessContext.INTERNAL, request);

    assertAll(
        () ->
            assertEquals(
                c.valid ? StatusCode.GOOD : StatusCode.of(StatusCodes.Bad_InvalidArgument),
                result.getStatusCode()),
        () ->
            assertArrayEquals(
                c.valid
                    ? new StatusCode[0]
                    : new StatusCode[] {
                      StatusCode.of(StatusCodes.Bad_TypeMismatch), StatusCode.GOOD
                    },
                result.getInputArgumentResults(),
                "input results: " + Arrays.toString(result.getInputArgumentResults())),
        () -> assertEquals(c.valid ? 1 : 0, handler.callbackCount, "callback count"),
        () -> assertEquals(c.valid ? 1 : 0, handler.validationCount, "value validation count"));
    assertSame(input, request.getInputArguments()[0], "validation must not mutate the request");
    assertEquals(0, result.getOutputArguments().length);
    assertEquals(0, result.getInputArgumentDiagnosticInfos().length);
    if (c.valid && input.value() instanceof Matrix matrix && !matrix.isNull()) {
      assertSame(input, handler.received[0], "matrix validation must not replace wire values");
    }
    return handler;
  }

  // Validation must preserve session identity and the object on which the method was called.
  @Test
  void preservesSessionContext() {
    var handler =
        new RecordingHandler(
            new Argument("Input", NodeIds.Int32, -1, null, LocalizedText.NULL_VALUE));
    Session session = mock(Session.class);
    handler.expectedSession = Optional.of(session);

    CallMethodResult result =
        handler.invoke(() -> Optional.of(session), handler.request(wireValue(1)));

    assertEquals(StatusCode.GOOD, result.getStatusCode());
  }

  // Local callers can supply primitive arrays without wire decoding.
  @Test
  void acceptsLocalPrimitiveMatrix() {
    var handler =
        new RecordingHandler(
            new Argument("Input", NodeIds.Duration, 2, null, LocalizedText.NULL_VALUE));
    CallMethodRequest request =
        handler.request(new Variant(new Matrix(new double[] {1.0}, new int[] {1, 1})));

    assertEquals(StatusCode.GOOD, handler.invoke(AccessContext.INTERNAL, request).getStatusCode());
  }

  // A null Matrix carries no value or shape, so it is accepted for any rank and delivered as null.
  @TestFactory
  Stream<DynamicTest> deliversNullMatrixAsNull() {
    return Stream.of(-1, 1, 2)
        .map(
            rank ->
                DynamicTest.dynamicTest(
                    "rank=" + rank,
                    () -> {
                      var handler =
                          new RecordingHandler(
                              new Argument(
                                  "Input", NodeIds.Int32, rank, null, LocalizedText.NULL_VALUE));
                      CallMethodRequest request = handler.request(new Variant(Matrix.ofNull()));

                      CallMethodResult result = handler.invoke(AccessContext.INTERNAL, request);

                      assertEquals(StatusCode.GOOD, result.getStatusCode());
                      assertEquals(Variant.NULL_VALUE, handler.received[0]);
                    }));
  }

  private Variant wireValue(Object value) {
    ByteBuf buffer = Unpooled.buffer();
    try {
      EncodingContext context = server.getStaticEncodingContext();
      new OpcUaBinaryEncoder(context).setBuffer(buffer).encodeVariant(new Variant(value));
      return new OpcUaBinaryDecoder(context).setBuffer(buffer).decodeVariant();
    } finally {
      buffer.release();
    }
  }

  private static class UnionOfScalar extends Union {
    static final NodeId TYPE_ID = new NodeId(2, "ValidationUnion");
    static final NodeId BINARY_ENCODING_ID = new NodeId(2, "ValidationUnion.Binary");
    private final boolean value;

    UnionOfScalar(boolean value) {
      this.value = value;
    }

    @Override
    public ExpandedNodeId getTypeId() {
      return TYPE_ID.expanded();
    }

    @Override
    public ExpandedNodeId getBinaryEncodingId() {
      return BINARY_ENCODING_ID.expanded();
    }

    private static class Codec extends GenericDataTypeCodec<UnionOfScalar> {
      @Override
      public Class<UnionOfScalar> getType() {
        return UnionOfScalar.class;
      }

      @Override
      public UnionOfScalar decodeType(EncodingContext context, UaDecoder decoder) {
        if (decoder.decodeUInt32("SwitchField").intValue() != 1) {
          throw new UaSerializationException(StatusCodes.Bad_DecodingError, "invalid union switch");
        }
        return new UnionOfScalar(decoder.decodeBoolean("Value"));
      }

      @Override
      public void encodeType(EncodingContext context, UaEncoder encoder, UnionOfScalar union) {
        encoder.encodeUInt32("SwitchField", uint(1));
        encoder.encodeBoolean("Value", union.value);
      }
    }
  }

  private class RecordingHandler extends AbstractMethodInvocationHandler {
    private final Argument argument;
    private Optional<Session> expectedSession = Optional.empty();
    private Variant[] received;
    private int callbackCount;
    private int validationCount;

    RecordingHandler(Argument argument) {
      super(
          UaMethodNode.builder(testNamespace.getNodeContext())
              .setNodeId(newNodeId("validation"))
              .setBrowseName(newQualifiedName("validation"))
              .setDisplayName(LocalizedText.english("validation"))
              .build());
      this.argument = argument;
    }

    @Override
    public Argument[] getInputArguments() {
      return new Argument[] {
        argument, new Argument("Control", NodeIds.Int32, -1, null, LocalizedText.NULL_VALUE)
      };
    }

    @Override
    public Argument[] getOutputArguments() {
      return new Argument[0];
    }

    CallMethodRequest request(Variant input) {
      return new CallMethodRequest(
          NodeIds.ObjectsFolder, getNode().getNodeId(), new Variant[] {input, Variant.ofInt32(42)});
    }

    @Override
    protected void validateInputArgumentValues(Variant[] values) {
      validationCount++;
    }

    @Override
    protected Variant[] invoke(InvocationContext context, Variant[] values) {
      callbackCount++;
      assertSame(server, context.getServer());
      assertSame(getNode(), context.getMethodNode());
      assertEquals(NodeIds.ObjectsFolder, context.getObjectId());
      assertEquals(expectedSession, context.getSession());
      received = values;
      return new Variant[0];
    }
  }
}
