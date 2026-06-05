/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.core.encoding.xml;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.StringReader;
import java.lang.reflect.Array;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaEnumeratedType;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.XmlElement;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDVector;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Round-trip tests for XML-encoded {@link Matrix} values.
 *
 * <p>The XML encoder ({@code OpcUaXmlEncoder.encode*Matrix}) and the four decoder methods ({@code
 * OpcUaXmlDecoder.decodeMatrix}, {@code decodeEnumMatrix}, and the two {@code decodeStructMatrix}
 * overloads) must round-trip: a Matrix encoded to XML must decode back to an equal value. These
 * tests reuse the same matrix fixtures the encoder tests use ({@code
 * org.eclipse.milo.opcua.stack.core.encoding.xml.args.MatrixArguments}).
 *
 * <p>Most builtin types are asserted by full equality. Enumerated types are reduced to {@code
 * Int32} and structured types are decoded back into their {@link UaStructuredType}, mirroring the
 * reductions {@code OpcUaBinaryDecoder} performs, so those cases assert the corresponding reduced
 * value. The {@code XmlElement} builtin case asserts type + dimensions + element count rather than
 * verbatim equality because the scalar XmlElement codec normalizes fragments (see {@code
 * OpcUaXmlSerializationTest}); the {@code ExtensionObject} case decodes both sides' bodies back to
 * their structures, which do survive exactly.
 */
public class OpcUaXmlDecoderMatrixTest {

  private final EncodingContext context = new DefaultEncodingContext();

  // ---------------------------------------------------------------------------
  // decodeMatrix(field, OpcUaDataType) — matrices of builtin types
  // ---------------------------------------------------------------------------

  @ParameterizedTest(name = "decodeMatrix builtin: {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.MatrixArguments#matrixOfBuiltinTypeArguments")
  void builtinMatrixRoundTrips(Matrix matrix, String ignoredExpectedXml) throws Exception {
    String encoded = encode(e -> e.encodeMatrix("Test", matrix));

    Matrix decoded;
    try (var decoder = new OpcUaXmlDecoder(context)) {
      decoder.setInput(new StringReader(encoded));
      decoded = decoder.decodeMatrix("Test", matrix.getDataType().orElseThrow());
    }

    Class<?> elementType = matrix.getElementType().orElseThrow();
    if (elementType == ExtensionObject.class) {
      // An ExtensionObject carries its body as an XmlElement whose fragment the scalar codec
      // normalizes (so the ExtensionObjects don't compare verbatim), but the structures they wrap
      // survive exactly. Decode both sides back to their structures and assert full equality — this
      // verifies values, count, and order, not just that something non-null came back.
      assertEquals(decodeExtensionObjects(matrix), decodeExtensionObjects(decoded));
    } else if (elementType == XmlElement.class) {
      // The scalar XmlElement codec normalizes fragments (self-closing empty tags, dropped xmlns
      // declarations, null/empty fragments), so XmlElements do not round-trip verbatim —
      // OpcUaXmlSerializationTest documents this and compares XML semantically. Since this fidelity
      // limitation lives in the scalar codec, not the Matrix decode under test, assert type +
      // dimensions + element count (the last guards against dropped/duplicated elements).
      assertFalse(decoded.isNull(), "decodeMatrix returned a null Matrix");
      assertArrayEquals(matrix.getDimensions(), decoded.getDimensions());
      assertEquals(elementType, decoded.getElementType().orElseThrow());
      assertEquals(
          Array.getLength(matrix.getElements()),
          Array.getLength(decoded.getElements()),
          "decoded element count must match the original");
    } else {
      // Strongest assertion: the full value must survive the round-trip.
      assertEquals(matrix, decoded);
    }
  }

  /** Decode each ExtensionObject element back into its structure for exact comparison. */
  private Matrix decodeExtensionObjects(Matrix matrix) {
    return matrix.transform(o -> ((ExtensionObject) o).decode(context));
  }

  // ---------------------------------------------------------------------------
  // decodeEnumMatrix(field) — matrices of enumerated types
  // ---------------------------------------------------------------------------

  @ParameterizedTest(name = "decodeEnumMatrix: {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.MatrixArguments#matrixOfEnumeratedTypeArguments")
  void enumMatrixRoundTrips(Matrix matrix, String ignoredExpectedXml) throws Exception {
    String encoded = encode(e -> e.encodeEnumMatrix("Test", matrix));

    Matrix decoded;
    try (var decoder = new OpcUaXmlDecoder(context)) {
      decoder.setInput(new StringReader(encoded));
      decoded = decoder.decodeEnumMatrix("Test");
    }

    // A correct decoder reduces enums to Int32 (cf. OpcUaBinaryDecoder.decodeEnumMatrix), so the
    // expected value has Int32 elements equal to each enum's value, with the dimensions preserved.
    assertFalse(decoded.isNull(), "decodeEnumMatrix returned a null Matrix");
    assertArrayEquals(matrix.getDimensions(), decoded.getDimensions());
    assertEquals(OpcUaDataType.Int32, decoded.getDataType().orElseThrow());

    Matrix expected = matrix.transform(v -> ((UaEnumeratedType) v).getValue());
    assertEquals(expected, decoded);
  }

  // ---------------------------------------------------------------------------
  // decodeStructMatrix(field, NodeId / ExpandedNodeId) — matrices of structured types
  // ---------------------------------------------------------------------------

  @ParameterizedTest(name = "decodeStructMatrix(NodeId): {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.MatrixArguments#matrixOfStructuredTypeArguments")
  void structMatrixRoundTripsWithNodeId(Matrix matrix, String ignoredExpectedXml) throws Exception {
    NodeId dataTypeId =
        matrix.getDataTypeId().orElseThrow().toNodeId(context.getNamespaceTable()).orElseThrow();

    String encoded = encode(e -> e.encodeStructMatrix("Test", matrix, dataTypeId));

    Matrix decoded;
    try (var decoder = new OpcUaXmlDecoder(context)) {
      decoder.setInput(new StringReader(encoded));
      decoded = decoder.decodeStructMatrix("Test", dataTypeId);
    }

    // A correct decoder decodes the ExtensionObject elements back into their structures, so the
    // elements must be UaStructuredType (not raw ExtensionObject) and the value must round-trip.
    assertFalse(decoded.isNull(), "decodeStructMatrix(NodeId) returned a null Matrix");
    assertArrayEquals(matrix.getDimensions(), decoded.getDimensions());
    assertTrue(
        UaStructuredType.class.isAssignableFrom(decoded.getElementType().orElseThrow()),
        "expected struct elements, got " + decoded.getElementType().orElseThrow());
    assertEquals(matrix, decoded);
  }

  @ParameterizedTest(name = "decodeStructMatrix(ExpandedNodeId): {0}")
  @MethodSource(
      "org.eclipse.milo.opcua.stack.core.encoding.xml.args.MatrixArguments#matrixOfStructuredTypeArguments")
  void structMatrixRoundTripsWithExpandedNodeId(Matrix matrix, String ignoredExpectedXml)
      throws Exception {
    ExpandedNodeId dataTypeId = matrix.getDataTypeId().orElseThrow();

    String encoded = encode(e -> e.encodeStructMatrix("Test", matrix, dataTypeId));

    Matrix decoded;
    try (var decoder = new OpcUaXmlDecoder(context)) {
      decoder.setInput(new StringReader(encoded));
      decoded = decoder.decodeStructMatrix("Test", dataTypeId);
    }

    assertFalse(decoded.isNull(), "decodeStructMatrix(ExpandedNodeId) returned a null Matrix");
    assertArrayEquals(matrix.getDimensions(), decoded.getDimensions());
    assertTrue(
        UaStructuredType.class.isAssignableFrom(decoded.getElementType().orElseThrow()),
        "expected struct elements, got " + decoded.getElementType().orElseThrow());
    assertEquals(matrix, decoded);
  }

  // ---------------------------------------------------------------------------
  // Explicit, self-contained value round-trips (no fixture indirection) so the
  // data loss is obvious at a glance.
  // ---------------------------------------------------------------------------

  @Test
  void int32MatrixValueRoundTrips() throws Exception {
    Matrix original = Matrix.ofInt32(new int[][] {{1, 2, 3}, {4, 5, 6}});

    String encoded = encode(e -> e.encodeMatrix("Test", original));

    Matrix decoded;
    try (var decoder = new OpcUaXmlDecoder(context)) {
      decoder.setInput(new StringReader(encoded));
      decoded = decoder.decodeMatrix("Test", OpcUaDataType.Int32);
    }

    assertEquals(original, decoded);
  }

  @Test
  void stringMatrixValueRoundTrips() throws Exception {
    Matrix original = Matrix.ofString(new String[][] {{"a", "b"}, {"c", "d"}});

    String encoded = encode(e -> e.encodeMatrix("Test", original));

    Matrix decoded;
    try (var decoder = new OpcUaXmlDecoder(context)) {
      decoder.setInput(new StringReader(encoded));
      decoded = decoder.decodeMatrix("Test", OpcUaDataType.String);
    }

    assertEquals(original, decoded);
  }

  @Test
  void decodesIndentedMatrixXml() throws Exception {
    // Pretty-printed XML (e.g. copied from a UANodeSet) has whitespace text nodes between elements.
    // The decoder must locate <Dimensions>/<Elements> by element type, not by raw child position.
    String indented =
        """
        <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
          <uax:Dimensions>
            <uax:Int32>2</uax:Int32>
            <uax:Int32>3</uax:Int32>
          </uax:Dimensions>
          <uax:Elements>
            <uax:Int32>1</uax:Int32>
            <uax:Int32>2</uax:Int32>
            <uax:Int32>3</uax:Int32>
            <uax:Int32>4</uax:Int32>
            <uax:Int32>5</uax:Int32>
            <uax:Int32>6</uax:Int32>
          </uax:Elements>
        </Test>
        """;

    Matrix decoded;
    try (var decoder = new OpcUaXmlDecoder(context)) {
      decoder.setInput(new StringReader(indented));
      decoded = decoder.decodeMatrix("Test", OpcUaDataType.Int32);
    }

    assertEquals(Matrix.ofInt32(new int[][] {{1, 2, 3}, {4, 5, 6}}), decoded);
  }

  @Test
  void decodesNullMatrix() throws Exception {
    // A field with neither <Dimensions> nor <Elements> children decodes to a null Matrix rather
    // than throwing or returning a non-null empty Matrix.
    Matrix decoded;
    try (var decoder = new OpcUaXmlDecoder(context)) {
      decoder.setInput(new StringReader("<Test/>"));
      decoded = decoder.decodeMatrix("Test", OpcUaDataType.Int32);
    }

    assertTrue(decoded.isNull());
  }

  // ---------------------------------------------------------------------------
  // Malformed Matrix XML must fail instead of constructing an invalid Matrix.
  // ---------------------------------------------------------------------------

  @Test
  void decodeMatrixRejectsMismatchedDimensions() throws Exception {
    // OPC 10000-6 5.3.1.17 requires the product of Dimensions to match the number of
    // flattened Elements. A 2x2 matrix must therefore contain exactly 4 elements.
    String xml =
        """
        <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
          <uax:Dimensions>
            <uax:Int32>2</uax:Int32>
            <uax:Int32>2</uax:Int32>
          </uax:Dimensions>
          <uax:Elements>
            <uax:Int32>1</uax:Int32>
          </uax:Elements>
        </Test>
        """;

    try (var decoder = new OpcUaXmlDecoder(context)) {
      decoder.setInput(new StringReader(xml));

      assertThrows(
          UaSerializationException.class,
          () -> decoder.decodeMatrix("Test", OpcUaDataType.Int32),
          "Matrix dimensions [2, 2] describe 4 elements, but the XML contains only 1");
    }
  }

  @Test
  void decodeMatrixRejectsNonPositiveDimensions() throws Exception {
    // OPC 10000-6 5.3.1.17 says all Matrix dimensions shall be greater than zero.
    // A zero dimension is not a valid compact representation of an empty Matrix.
    String xml =
        """
        <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
          <uax:Dimensions>
            <uax:Int32>0</uax:Int32>
            <uax:Int32>1</uax:Int32>
          </uax:Dimensions>
          <uax:Elements>
            <uax:Int32>1</uax:Int32>
          </uax:Elements>
        </Test>
        """;

    try (var decoder = new OpcUaXmlDecoder(context)) {
      decoder.setInput(new StringReader(xml));

      assertThrows(
          UaSerializationException.class,
          () -> decoder.decodeMatrix("Test", OpcUaDataType.Int32),
          "Matrix dimensions must be greater than zero");
    }
  }

  @Test
  void decodeMatrixRejectsElementWithWrongXmlTypeName() throws Exception {
    // OPC 10000-6 5.3.4 says array element names shall be the type name. Since the
    // caller requested an Int32 matrix, each element in <Elements> must be <Int32>.
    String xml =
        """
        <Test xmlns:uax="http://opcfoundation.org/UA/2008/02/Types.xsd">
          <uax:Dimensions>
            <uax:Int32>1</uax:Int32>
            <uax:Int32>1</uax:Int32>
          </uax:Dimensions>
          <uax:Elements>
            <uax:String>123</uax:String>
          </uax:Elements>
        </Test>
        """;

    try (var decoder = new OpcUaXmlDecoder(context)) {
      decoder.setInput(new StringReader(xml));

      assertThrows(
          UaSerializationException.class,
          () -> decoder.decodeMatrix("Test", OpcUaDataType.Int32),
          "An Int32 Matrix must not accept elements named String");
    }
  }

  @Test
  void decodeStructMatrixRejectsUnexpectedStructType() throws Exception {
    // The dataTypeId argument is the declared/expected structure type for the field.
    // A decoder should not silently accept ExtensionObjects for a different structure type.
    Matrix threeDVectorMatrix =
        Matrix.ofStruct(new ThreeDVector[][] {{new ThreeDVector(1.0, 2.0, 3.0)}});
    String encoded =
        encode(e -> e.encodeStructMatrix("Test", threeDVectorMatrix, ThreeDVector.TYPE_ID));

    try (var decoder = new OpcUaXmlDecoder(context)) {
      decoder.setInput(new StringReader(encoded));

      assertThrows(
          UaSerializationException.class,
          () -> decoder.decodeStructMatrix("Test", XVType.TYPE_ID),
          "decodeStructMatrix(..., XVType.TYPE_ID) must reject ThreeDVector elements");
    }
  }

  // ---------------------------------------------------------------------------

  private interface EncodeAction {
    void accept(OpcUaXmlEncoder encoder) throws Exception;
  }

  private String encode(EncodeAction action) throws Exception {
    try (var encoder = new OpcUaXmlEncoder(context)) {
      action.accept(encoder);
      return encoder.getOutputString();
    }
  }
}
