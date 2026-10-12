package org.eclipse.milo.opcua.stack.core.types.builtin;

import static org.junit.jupiter.api.Assertions.*;

import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.structured.Range;
import org.eclipse.milo.opcua.stack.core.types.structured.ThreeDVector;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ExtensionObjectTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionObjectTest.class);

  @Test
  void ofByteString() {
    ByteString byteString = new ByteString(new byte[] {1, 2, 3});
    ExtensionObject extensionObject = ExtensionObject.of(byteString, NodeId.NULL_VALUE);

    LOGGER.debug("{}", extensionObject);
    assertEquals(byteString, extensionObject.getBody());
    assertInstanceOf(ExtensionObject.Binary.class, extensionObject);
  }

  @Test
  void ofXmlElement() {
    XmlElement xmlElement = new XmlElement("<test>Test</test>");
    ExtensionObject extensionObject = ExtensionObject.of(xmlElement, NodeId.NULL_VALUE);

    LOGGER.debug("{}", extensionObject);
    assertEquals(xmlElement, extensionObject.getBody());
    assertInstanceOf(ExtensionObject.Xml.class, extensionObject);
  }

  @Test
  void ofJsonString() {
    String jsonString = "{\"test\": \"Test\"}";
    ExtensionObject extensionObject = ExtensionObject.of(jsonString, NodeId.NULL_VALUE);

    LOGGER.debug("{}", extensionObject);
    assertEquals(jsonString, extensionObject.getBody());
    assertInstanceOf(ExtensionObject.Json.class, extensionObject);
  }

  @Nested
  class ValueConversion {

    private final EncodingContext context = DefaultEncodingContext.INSTANCE;
    private final ThreeDVector vector = new ThreeDVector(1.0, 2.0, 3.0);
    private final Range range = new Range(0.0, 10.0);

    // A Variant carries a structure as an ExtensionObject; the array and Matrix forms must keep
    // their length, positions and dimensions so index range and element order survive the trip.
    @Test
    void structuredValuesRoundTripInEveryShape() {
      ThreeDVector[] array = {vector, new ThreeDVector(4.0, 5.0, 6.0)};
      Matrix matrix = Matrix.ofStruct(new ThreeDVector[][] {{vector}, {vector}});

      assertEquals(vector, ExtensionObject.decodeValue(context, encoded(vector)));
      assertArrayEquals(
          array, (ThreeDVector[]) ExtensionObject.decodeValue(context, encoded(array)));
      assertEquals(matrix, ExtensionObject.decodeValue(context, encoded(matrix)));
    }

    // Part 6 allows a null element in an array of ExtensionObjects. Encoding must not fail on it
    // and decoding must leave the position null rather than drop or shift elements.
    @Test
    void nullElementsKeepTheirPositions() {
      ThreeDVector[] array = {null, vector};

      ExtensionObject[] encoded = (ExtensionObject[]) ExtensionObject.encodeValue(context, array);
      assertNull(encoded[0]);
      assertNotNull(encoded[1]);

      assertArrayEquals(array, (ThreeDVector[]) ExtensionObject.decodeValue(context, encoded));
    }

    // A caller reading a Variable declared as Argument[] expects an Argument[] back, not an
    // UaStructuredType[] it has to copy; mixed element types cannot be narrowed that way.
    @Test
    void decodedArraysAreTypedByTheirElements() {
      Object same = ExtensionObject.decodeValue(context, encoded(new UaStructuredType[] {vector}));
      Object mixed =
          ExtensionObject.decodeValue(context, encoded(new UaStructuredType[] {vector, range}));
      Object empty = ExtensionObject.decodeValue(context, new ExtensionObject[0]);

      assertEquals(ThreeDVector[].class, same.getClass());
      assertEquals(UaStructuredType[].class, mixed.getClass());
      assertEquals(UaStructuredType[].class, empty.getClass());
    }

    // The datatype id is the only type information an empty or null-first Matrix carries, so the
    // conversion must copy it instead of re-deriving it from elements that are not there.
    @Test
    void matrixConversionPreservesDimensionsAndDataTypeId() {
      Matrix matrix =
          new Matrix(
              new ThreeDVector[] {null, vector},
              new int[] {1, 2},
              OpcUaDataType.ExtensionObject,
              ThreeDVector.TYPE_ID);

      Matrix encoded = (Matrix) ExtensionObject.encodeValue(context, matrix);
      assertInstanceOf(ExtensionObject[].class, encoded.getElements());
      assertArrayEquals(matrix.getDimensions(), encoded.getDimensions());
      assertEquals(matrix.getDataTypeId(), encoded.getDataTypeId());

      Matrix decoded = (Matrix) ExtensionObject.decodeValue(context, encoded);
      assertEquals(matrix, decoded);
      assertEquals(matrix.getDataTypeId(), decoded.getDataTypeId());
    }

    // Callers hand these methods whatever a Variant holds. Values with nothing to convert,
    // including values already in the target form, must come back untouched rather than fail.
    @Test
    void valuesWithoutStructuresPassThroughUnchanged() {
      Matrix nullMatrix = Matrix.ofNull();
      ExtensionObject encoded = ExtensionObject.encode(context, vector);

      assertNull(ExtensionObject.encodeValue(context, null));
      assertNull(ExtensionObject.decodeValue(context, null));
      assertSame(nullMatrix, ExtensionObject.encodeValue(context, nullMatrix));
      assertSame(nullMatrix, ExtensionObject.decodeValue(context, nullMatrix));
      assertEquals(42, ExtensionObject.encodeValue(context, 42));
      assertEquals(42, ExtensionObject.decodeValue(context, 42));
      assertSame(encoded, ExtensionObject.encodeValue(context, encoded));
      assertSame(vector, ExtensionObject.decodeValue(context, vector));
    }

    private Object encoded(Object value) {
      return ExtensionObject.encodeValue(context, value);
    }
  }
}
