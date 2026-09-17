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

import static java.util.Objects.requireNonNullElse;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;
import org.eclipse.milo.opcua.sdk.core.ValueRanks;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.util.ArrayUtil;
import org.jspecify.annotations.Nullable;

/**
 * Validates the input values of a Method call against its declared input {@link Argument}s.
 *
 * <p>The count, shape (rank and dimensions) and DataType of every supplied value are checked
 * against the declaration, and structure values are decoded so the caller receives {@link
 * UaStructuredType} values rather than raw {@link ExtensionObject}s. Trailing declared inputs
 * beyond a required count may be omitted; an omitted input is absent from the returned array while
 * a supplied null keeps its position.
 *
 * <p>{@link AbstractMethodInvocationHandler} uses this before invoking application code. Handlers
 * that do not extend it can use it directly:
 *
 * <pre>{@code
 * var validator = new MethodArgumentValidator(server);
 * Variant[] values = validator.validate(inputArguments, request.getInputArguments());
 * }</pre>
 *
 * Instances are stateless and safe to share.
 */
public final class MethodArgumentValidator {

  private final OpcUaServer server;

  /**
   * @param server the server whose DataType tree, namespace table and encoding context resolve
   *     argument DataTypes and decode structure values.
   */
  public MethodArgumentValidator(OpcUaServer server) {
    this.server = server;
  }

  /**
   * Validate {@code inputValues} against {@code inputArguments}, requiring every declared input.
   *
   * @param inputArguments the declared input arguments.
   * @param inputValues the values supplied by the caller; {@code null} means none.
   * @return the validated values, as described by {@link #validate(Argument[], int, Variant[])}.
   * @throws UaException as described by {@link #validate(Argument[], int, Variant[])}.
   */
  public Variant[] validate(Argument[] inputArguments, Variant @Nullable [] inputValues)
      throws UaException {
    return validate(inputArguments, inputArguments.length, inputValues);
  }

  /**
   * Validate {@code inputValues} against {@code inputArguments}, requiring only the first {@code
   * requiredCount} declared inputs.
   *
   * <p>The returned array is a copy of {@code inputValues}, with the same length. Values for
   * arguments with a structured DataType carry the decoded {@link UaStructuredType} value (or, for
   * array arguments, an array of the DataType's registered class, e.g. {@code XVType[]}) rather
   * than the raw {@link ExtensionObject}(s). A null ExtensionObject, whether scalar or an array
   * element, is delivered as {@code null}. A null Matrix is delivered as a null value. Matrix
   * values retain their original representation; their elements are decoded only for validation. An
   * empty value for an argument of ValueRank 2 or greater is delivered as an empty {@link Matrix}
   * of the declared rank, because its wire form carries no dimensions.
   *
   * @param inputArguments the declared input arguments.
   * @param requiredCount the number of leading inputs a caller must supply, between zero and {@code
   *     inputArguments.length} inclusive.
   * @param inputValues the values supplied by the caller; {@code null} means none.
   * @return the validated values.
   * @throws InvalidArgumentException with Bad_TypeMismatch for each value whose shape or DataType
   *     does not match its declaration.
   * @throws UaException with Bad_ArgumentsMissing if fewer than {@code requiredCount} values were
   *     supplied, Bad_TooManyArguments if more than {@code inputArguments.length} were supplied, or
   *     Bad_InternalError if {@code requiredCount} is outside its valid range.
   */
  public Variant[] validate(
      Argument[] inputArguments, int requiredCount, Variant @Nullable [] inputValues)
      throws UaException {

    // Defensive copy: values for structure-typed arguments are substituted with their decoded
    // values below, and the caller's array should not be modified.
    Variant[] values = requireNonNullElse(inputValues, new Variant[0]).clone();

    if (requiredCount < 0 || requiredCount > inputArguments.length) {
      throw new UaException(StatusCodes.Bad_InternalError, "Invalid required input count");
    }
    if (values.length < requiredCount) {
      throw new UaException(StatusCodes.Bad_ArgumentsMissing);
    }
    if (values.length > inputArguments.length) {
      throw new UaException(StatusCodes.Bad_TooManyArguments);
    }

    DataTypeTree dataTypeTree = server.getDataTypeTree();
    StatusCode[] checkResults = new StatusCode[values.length];

    for (int i = 0; i < values.length; i++) {
      Argument argument = inputArguments[i];
      Variant variant = values[i];
      Object value = variant.value();

      if (value instanceof Matrix matrix && matrix.isNull()) {
        // A null Matrix is just a null value; deliver it as one.
        values[i] = Variant.NULL_VALUE;
        value = null;
      }

      // Check the shape first; it is cheap and avoids decoding elements of a mismatched value.
      boolean dataTypeMatch = shapeMatches(argument, value);

      if (dataTypeMatch && value != null) {
        NodeId argDataTypeId = argument.getDataType();
        boolean argIsStructType =
            NodeIds.Structure.equals(argDataTypeId) || dataTypeTree.isStructType(argDataTypeId);

        if (dataTypeTree.getBackingClass(argDataTypeId) == Variant.class) {
          // Variant-backed declarations accept payload types, not just the wrapper class.
          // Check Matrix elements without replacing the original representation.
          try {
            Variant.of(elementsOf(value));
          } catch (IllegalArgumentException | ClassCastException e) {
            dataTypeMatch = false;
          }
        } else if (argIsStructType) {
          try {
            if (value instanceof ExtensionObject xo) {
              UaStructuredType decoded = decodeStructure(xo);
              dataTypeMatch = structureTypeMatches(dataTypeTree, argDataTypeId, decoded);
              if (dataTypeMatch) {
                // Substitute the decoded value so implementations receive the
                // UaStructuredType rather than the raw ExtensionObject.
                values[i] = new Variant(decoded);
              }
            } else if (value instanceof ExtensionObject[] xos) {
              UaStructuredType[] decodedElements =
                  decodeMatchingStructures(dataTypeTree, argDataTypeId, xos);
              dataTypeMatch = decodedElements != null;
              if (dataTypeMatch) {
                // Substitute a correctly-typed array of the decoded values, so that e.g. an
                // argument of DataType XVType is delivered as XVType[], even when empty.
                values[i] = new Variant(typedStructArray(argDataTypeId, decodedElements));
              }
            } else if (value instanceof UaStructuredType structValue) {
              dataTypeMatch = structureTypeMatches(dataTypeTree, argDataTypeId, structValue);
            } else {
              // Validate each element without changing the value passed on.
              dataTypeMatch =
                  structureElementsMatch(dataTypeTree, argDataTypeId, elementsOf(value));
            }
          } catch (UaSerializationException e) {
            dataTypeMatch = false;
          }
        } else {
          NodeId valueDataTypeId =
              variant
                  .getDataTypeId()
                  .flatMap(xni -> xni.toNodeId(server.getNamespaceTable()))
                  .orElse(NodeId.NULL_VALUE);

          if (!argDataTypeId.equals(valueDataTypeId)) {
            Class<?> elementType = ArrayUtil.getBoxedType(elementsOf(value));
            dataTypeMatch = dataTypeTree.isAssignable(argDataTypeId, elementType);
          }
        }
      }

      if (dataTypeMatch) {
        values[i] = restoreMatrixRank(argument, values[i]);
        checkResults[i] = StatusCode.GOOD;
      } else {
        checkResults[i] = new StatusCode(StatusCodes.Bad_TypeMismatch);
      }
    }

    if (Arrays.stream(checkResults).anyMatch(StatusCode::isBad)) {
      throw new InvalidArgumentException(checkResults);
    }

    return values;
  }

  /** The flat elements of a Matrix, or {@code value} itself for any other value. */
  private static @Nullable Object elementsOf(@Nullable Object value) {
    return value instanceof Matrix matrix ? matrix.getElements() : value;
  }

  // Part 3, 8.6: ArrayDimensions specifies maxima; zero means unknown.
  private static boolean shapeMatches(Argument argument, @Nullable Object value) {
    if (value == null) {
      return true;
    }

    // ByteString and String have scalar semantics, i.e. rank -1.
    int rank =
        value instanceof Matrix matrix ? matrix.getValueRank() : ArrayUtil.getValueRank(value);
    int valueRank = argument.getValueRank();
    boolean emptyArray = isEmptyArray(value);

    boolean rankMatches =
        switch (valueRank) {
          case ValueRanks.ScalarOrOneDimension -> rank == ValueRanks.Scalar || rank == 1;
          case ValueRanks.Any -> true;
          case ValueRanks.Scalar -> rank == ValueRanks.Scalar;
          case ValueRanks.OneOrMoreDimensions -> rank >= 1;
          default -> valueRank > 0 && (rank == valueRank || emptyArray);
        };

    if (!rankMatches) {
      return false;
    }

    UInteger[] maxima = argument.getArrayDimensions();

    // An empty array has no element to exceed a maximum, and its single dimension does not line up
    // with the dimensions declared for a higher rank.
    if (valueRank > 0 && !emptyArray && maxima != null && maxima.length > 0) {
      int[] dimensions =
          value instanceof Matrix matrix ? matrix.getDimensions() : ArrayUtil.getDimensions(value);

      if (maxima.length != dimensions.length) {
        return false;
      }
      for (int i = 0; i < dimensions.length; i++) {
        long maximum = maxima[i].longValue();
        if (maximum != 0 && dimensions[i] > maximum) {
          return false;
        }
      }
    }

    return true;
  }

  /**
   * Whether {@code value} is a zero-length one-dimensional array, the form an empty value of any
   * rank arrives in.
   *
   * <p>OPC 10000-6, 5.2.2.16 and 5.3.1.17 carry an empty Matrix as an empty array with no
   * ArrayDimensions, so an empty value has no rank of its own on the wire.
   */
  private static boolean isEmptyArray(@Nullable Object value) {
    return value != null && ArrayUtil.getValueRank(value) == 1 && Array.getLength(value) == 0;
  }

  /**
   * Give an empty value supplied for an Argument of ValueRank 2 or greater the Matrix
   * representation its declared rank calls for, with a zero length in every dimension.
   */
  private static Variant restoreMatrixRank(Argument argument, Variant variant) {
    int valueRank = argument.getValueRank();
    Object value = variant.value();

    if (valueRank >= 2 && isEmptyArray(value)) {
      return new Variant(new Matrix(value, new int[valueRank]));
    } else {
      return variant;
    }
  }

  private @Nullable UaStructuredType decodeStructure(@Nullable ExtensionObject xo) {
    if (xo == null || xo.isNull()) {
      return null;
    }
    return xo.decode(server.getStaticEncodingContext());
  }

  /**
   * Decode each element of {@code xos} and check it against the Argument's DataType, stopping at
   * the first mismatch.
   *
   * @return the decoded elements, or {@code null} if any element does not match.
   */
  private UaStructuredType @Nullable [] decodeMatchingStructures(
      DataTypeTree dataTypeTree, NodeId argDataTypeId, ExtensionObject[] xos) {

    var decodedElements = new UaStructuredType[xos.length];

    for (int j = 0; j < xos.length; j++) {
      decodedElements[j] = decodeStructure(xos[j]);
      if (!structureTypeMatches(dataTypeTree, argDataTypeId, decodedElements[j])) {
        return null;
      }
    }

    return decodedElements;
  }

  /**
   * Check that every element of a structure array matches the Argument's DataType. Null elements
   * match; {@link ExtensionObject} elements are decoded first. Any other value does not match.
   */
  private boolean structureElementsMatch(
      DataTypeTree dataTypeTree, NodeId argDataTypeId, @Nullable Object elements) {

    if (elements instanceof ExtensionObject[] xos) {
      return decodeMatchingStructures(dataTypeTree, argDataTypeId, xos) != null;
    } else if (elements instanceof UaStructuredType[] structs) {
      return Arrays.stream(structs)
          .allMatch(s -> structureTypeMatches(dataTypeTree, argDataTypeId, s));
    } else {
      return false;
    }
  }

  /**
   * Check that a structure value's DataType matches the DataType of the {@link Argument} it was
   * supplied for.
   *
   * <p>If the Argument's DataType is abstract the value's DataType may be any subtype of it;
   * otherwise the DataTypes must match exactly.
   */
  private boolean structureTypeMatches(
      DataTypeTree dataTypeTree, NodeId argDataTypeId, @Nullable UaStructuredType structValue) {

    if (structValue == null) {
      return true;
    }

    NodeId valueDataTypeId =
        structValue.getTypeId().toNodeId(server.getNamespaceTable()).orElse(NodeId.NULL_VALUE);

    DataType argType = dataTypeTree.getType(argDataTypeId);
    boolean isAbstract = argType != null && argType.isAbstract();

    if (isAbstract) {
      return dataTypeTree.isSubtypeOf(valueDataTypeId, argDataTypeId);
    } else {
      return Objects.equals(valueDataTypeId, argDataTypeId);
    }
  }

  /**
   * Create an array of the class registered for {@code argDataTypeId} containing {@code elements}.
   *
   * <p>Follows the {@code OpcUaBinaryDecoder#decodeStructArray} precedent: the element class comes
   * from the registered {@link DataTypeCodec}, so even an empty array is correctly typed. If no
   * codec is registered, e.g. because the DataType is abstract, the {@link UaStructuredType} array
   * is returned as-is.
   */
  private Object typedStructArray(NodeId argDataTypeId, UaStructuredType[] elements) {
    DataTypeCodec codec =
        server.getStaticEncodingContext().getDataTypeManager().getCodec(argDataTypeId);

    if (codec == null) {
      return elements;
    }

    Object array = Array.newInstance(codec.getType(), elements.length);
    for (int i = 0; i < elements.length; i++) {
      Array.set(array, i, elements[i]);
    }
    return array;
  }
}
