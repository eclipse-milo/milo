/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.test;

import java.util.Arrays;
import org.eclipse.milo.opcua.stack.core.OpcUaDataType;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.GenericDataTypeCodec;
import org.eclipse.milo.opcua.stack.core.encoding.UaDecoder;
import org.eclipse.milo.opcua.stack.core.encoding.UaEncoder;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExpandedNodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.XVType;

public class MatrixTestType implements UaStructuredType {

  public static final ExpandedNodeId TYPE_ID =
      ExpandedNodeId.parse("nsu=urn:eclipse:milo:test;s=MatrixTestType.TypeId");
  public static final ExpandedNodeId BINARY_ENCODING_ID =
      ExpandedNodeId.parse("nsu=urn:eclipse:milo:test;s=MatrixTestType.BinaryEncoding");
  public static final ExpandedNodeId JSON_ENCODING_ID =
      ExpandedNodeId.parse("nsu=urn:eclipse:milo:test;s=MatrixTestType.JsonEncoding");

  private final Integer[][] builtinMatrix;
  private final ApplicationType[][] enumMatrix;
  private final XVType[][] structMatrix;

  public MatrixTestType(
      Integer[][] builtinMatrix, ApplicationType[][] enumMatrix, XVType[][] structMatrix) {
    this.builtinMatrix = builtinMatrix;
    this.enumMatrix = enumMatrix;
    this.structMatrix = structMatrix;
  }

  public Integer[][] getBuiltinMatrix() {
    return builtinMatrix;
  }

  public ApplicationType[][] getEnumMatrix() {
    return enumMatrix;
  }

  public XVType[][] getStructMatrix() {
    return structMatrix;
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
  public String toString() {
    return "MatrixTestType{"
        + "builtinMatrix="
        + Arrays.deepToString(builtinMatrix)
        + ", enumMatrix="
        + Arrays.deepToString(enumMatrix)
        + ", structMatrix="
        + Arrays.deepToString(structMatrix)
        + '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MatrixTestType that = (MatrixTestType) o;
    return Arrays.deepEquals(builtinMatrix, that.builtinMatrix)
        && Arrays.deepEquals(enumMatrix, that.enumMatrix)
        && Arrays.deepEquals(structMatrix, that.structMatrix);
  }

  @Override
  public int hashCode() {
    int result = Arrays.hashCode(builtinMatrix);
    result = 31 * result + Arrays.hashCode(enumMatrix);
    result = 31 * result + Arrays.hashCode(structMatrix);
    return result;
  }

  public static class Codec extends GenericDataTypeCodec<MatrixTestType> {

    @Override
    public Class<MatrixTestType> getType() {
      return MatrixTestType.class;
    }

    @Override
    public MatrixTestType decodeType(EncodingContext context, UaDecoder decoder)
        throws UaSerializationException {
      Integer[][] builtinMatrix =
          (Integer[][])
              decoder.decodeMatrix("BuiltinMatrix", OpcUaDataType.Int32).nestedArrayValue();
      ApplicationType[][] enumMatrix =
          (ApplicationType[][])
              decoder
                  .decodeEnumMatrix("EnumMatrix")
                  .transform(e -> ApplicationType.from((Integer) e))
                  .nestedArrayValue();
      XVType[][] structMatrix =
          (XVType[][])
              decoder.decodeStructMatrix("StructMatrix", XVType.TYPE_ID).nestedArrayValue();

      return new MatrixTestType(builtinMatrix, enumMatrix, structMatrix);
    }

    @Override
    public void encodeType(EncodingContext context, UaEncoder encoder, MatrixTestType value)
        throws UaSerializationException {
      encoder.encodeMatrix("BuiltinMatrix", new Matrix(value.builtinMatrix));
      encoder.encodeEnumMatrix("EnumMatrix", new Matrix(value.enumMatrix));
      encoder.encodeStructMatrix("StructMatrix", new Matrix(value.structMatrix), XVType.TYPE_ID);
    }
  }
}
