/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.sdk.server.AccessContext;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.UaSerializationException;
import org.eclipse.milo.opcua.stack.core.encoding.DataTypeCodec;
import org.eclipse.milo.opcua.stack.core.types.UaStructuredType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.Matrix;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;

/**
 * A partial implementation of {@link MethodInvocationHandler} that handles checking the Executable
 * and UserExecutable attributes as well as validating the supplied input values against the input
 * {@link Argument}s.
 */
public abstract class AbstractMethodInvocationHandler implements MethodInvocationHandler {

  private final UaMethodNode node;

  /**
   * @param node the {@link UaMethodNode} this handler will be installed on.
   */
  public AbstractMethodInvocationHandler(UaMethodNode node) {
    this.node = node;
  }

  public UaMethodNode getNode() {
    return node;
  }

  @Override
  public final CallMethodResult invoke(AccessContext accessContext, CallMethodRequest request) {
    try {
      // Defensive copy: values for structure-typed arguments may be substituted with their
      // decoded values below, and the request's array should not be modified.
      Variant[] inputArgumentValues =
          requireNonNullElse(request.getInputArguments(), new Variant[0]).clone();

      if (inputArgumentValues.length < getInputArguments().length) {
        throw new UaException(StatusCodes.Bad_ArgumentsMissing);
      }
      if (inputArgumentValues.length > getInputArguments().length) {
        throw new UaException(StatusCodes.Bad_TooManyArguments);
      }

      StatusCode[] inputDataTypeCheckResults = new StatusCode[inputArgumentValues.length];

      for (int i = 0; i < inputArgumentValues.length; i++) {
        Argument argument = getInputArguments()[i];

        Variant variant = inputArgumentValues[i];
        Object value = variant.value();

        boolean dataTypeMatch = true;

        if (value != null) {
          NodeId argDataTypeId = argument.getDataType();

          DataTypeTree dataTypeTree = node.getNodeContext().getServer().getDataTypeTree();

          boolean argIsStructType =
              NodeIds.Structure.equals(argDataTypeId) || dataTypeTree.isStructType(argDataTypeId);

          if (argIsStructType) {
            try {
              if (value instanceof ExtensionObject xo) {
                UaStructuredType decoded =
                    xo.decode(node.getNodeContext().getServer().getStaticEncodingContext());

                dataTypeMatch = structureTypeMatches(dataTypeTree, argDataTypeId, decoded);

                if (dataTypeMatch) {
                  // Substitute the decoded value so implementations receive the
                  // UaStructuredType rather than the raw ExtensionObject.
                  inputArgumentValues[i] = new Variant(decoded);
                }
              } else if (value instanceof ExtensionObject[] xos) {
                var decodedElements = new UaStructuredType[xos.length];

                for (int j = 0; dataTypeMatch && j < xos.length; j++) {
                  ExtensionObject xo = xos[j];

                  if (xo == null || xo.isNull()) {
                    // A struct array with null elements has no typed representation.
                    dataTypeMatch = false;
                  } else {
                    UaStructuredType decoded =
                        xo.decode(node.getNodeContext().getServer().getStaticEncodingContext());

                    dataTypeMatch = structureTypeMatches(dataTypeTree, argDataTypeId, decoded);

                    decodedElements[j] = decoded;
                  }
                }

                if (dataTypeMatch) {
                  // Substitute a correctly-typed array of the decoded values, so that e.g. an
                  // argument of DataType XVType is delivered as XVType[], even when empty.
                  inputArgumentValues[i] =
                      new Variant(typedStructArray(argDataTypeId, decodedElements));
                }
              } else if (value instanceof UaStructuredType structValue) {
                dataTypeMatch = structureTypeMatches(dataTypeTree, argDataTypeId, structValue);
              } else if (value instanceof UaStructuredType[] structValues) {
                for (int j = 0; dataTypeMatch && j < structValues.length; j++) {
                  UaStructuredType structValue = structValues[j];

                  dataTypeMatch =
                      structValue != null
                          && structureTypeMatches(dataTypeTree, argDataTypeId, structValue);
                }
              } else if (value instanceof Matrix) {
                // TODO decoding a Matrix of ExtensionObject into its struct elements is not
                //  supported; accept it only if the DataType ids already match exactly.
                NodeId valueDataTypeId =
                    variant
                        .getDataTypeId()
                        .flatMap(xni -> xni.toNodeId(node.getNodeContext().getNamespaceTable()))
                        .orElse(NodeId.NULL_VALUE);

                dataTypeMatch = argDataTypeId.equals(valueDataTypeId);
              } else {
                dataTypeMatch = false;
              }
            } catch (UaSerializationException e) {
              dataTypeMatch = false;
            }
          } else {
            NodeId valueDataTypeId =
                variant
                    .getDataTypeId()
                    .flatMap(xni -> xni.toNodeId(node.getNodeContext().getNamespaceTable()))
                    .orElse(NodeId.NULL_VALUE);

            if (!argDataTypeId.equals(valueDataTypeId)) {
              dataTypeMatch = dataTypeTree.isAssignable(argDataTypeId, value.getClass());
            }
          }
        }

        int valueRank = argument.getValueRank();

        if (valueRank == -1) {
          // scalar
          if (value != null && (value.getClass().isArray() || value instanceof Matrix)) {
            dataTypeMatch = false;
          }
        } else if (valueRank == 1) {
          // one dimension
          if (value != null && !value.getClass().isArray()) {
            dataTypeMatch = false;
          }
        } else if (valueRank == 0) {
          // one or more dimension
          if (value != null && !(value.getClass().isArray() || value instanceof Matrix)) {
            dataTypeMatch = false;
          }
        } else if (valueRank > 1) {
          // matrix (2+ dimensions)
          if (value != null && !(value instanceof Matrix)) {
            dataTypeMatch = false;
          }
        }

        if (dataTypeMatch) {
          inputDataTypeCheckResults[i] = StatusCode.GOOD;
        } else {
          inputDataTypeCheckResults[i] = new StatusCode(StatusCodes.Bad_TypeMismatch);
        }
      }

      if (Arrays.stream(inputDataTypeCheckResults).anyMatch(StatusCode::isBad)) {
        throw new InvalidArgumentException(inputDataTypeCheckResults);
      }

      validateInputArgumentValues(inputArgumentValues);

      InvocationContext invocationContext =
          new InvocationContext() {
            @Override
            public OpcUaServer getServer() {
              return node.getNodeContext().getServer();
            }

            @Override
            public NodeId getObjectId() {
              return request.getObjectId();
            }

            @Override
            public UaMethodNode getMethodNode() {
              return node;
            }

            @Override
            public Optional<Session> getSession() {
              return accessContext.getSession();
            }
          };

      Variant[] outputValues = invoke(invocationContext, inputArgumentValues);

      return new CallMethodResult(
          StatusCode.GOOD, new StatusCode[0], new DiagnosticInfo[0], outputValues);
    } catch (InvalidArgumentException e) {
      return new CallMethodResult(
          e.getStatusCode(),
          e.getInputArgumentResults(),
          e.getInputArgumentDiagnosticInfos(),
          new Variant[0]);
    } catch (UaException e) {
      return new CallMethodResult(
          e.getStatusCode(), new StatusCode[0], new DiagnosticInfo[0], new Variant[0]);
    }
  }

  /**
   * Check that a structure value's DataType matches the DataType of the {@link Argument} it was
   * supplied for.
   *
   * <p>If the Argument's DataType is abstract the value's DataType may be any subtype of it;
   * otherwise the DataTypes must match exactly.
   *
   * @param dataTypeTree the server's {@link DataTypeTree}.
   * @param argDataTypeId the {@link NodeId} of the Argument's DataType.
   * @param structValue the {@link UaStructuredType} value to check.
   * @return {@code true} if {@code structValue}'s DataType matches the Argument's DataType.
   */
  private boolean structureTypeMatches(
      DataTypeTree dataTypeTree, NodeId argDataTypeId, UaStructuredType structValue) {

    NodeId valueDataTypeId =
        structValue
            .getTypeId()
            .toNodeId(node.getNodeContext().getNamespaceTable())
            .orElse(NodeId.NULL_VALUE);

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
   *
   * @param argDataTypeId the {@link NodeId} of the Argument's DataType.
   * @param elements the decoded {@link UaStructuredType} elements.
   * @return an array of the codec's registered class containing {@code elements}.
   */
  private Object typedStructArray(NodeId argDataTypeId, UaStructuredType[] elements) {
    DataTypeCodec codec =
        node.getNodeContext()
            .getServer()
            .getStaticEncodingContext()
            .getDataTypeManager()
            .getCodec(argDataTypeId);

    if (codec == null) {
      return elements;
    }

    Object array = Array.newInstance(codec.getType(), elements.length);

    for (int i = 0; i < elements.length; i++) {
      Array.set(array, i, elements[i]);
    }

    return array;
  }

  /**
   * Get the input {@link Argument}s expected by the Method this handler is installed on.
   *
   * @return the input {@link Argument}s expected by the Method this handler is installed on.
   */
  public abstract Argument[] getInputArguments();

  /**
   * Get the output {@link Argument}s expected by the Method this handler is installed on.
   *
   * @return the output {@link Argument}s expected by the Method this handler is installed on.
   */
  public abstract Argument[] getOutputArguments();

  /**
   * Invoke this method and return the values for its output arguments, if any.
   *
   * <p>The Executable and UserExecutable attributes have already been checked to ensure this method
   * is allowed to execute.
   *
   * @param invocationContext the {@link InvocationContext}.
   * @param inputValues the user-supplied values for the input arguments. Each value has been
   *     verified to be of the type specified by its {@link Argument}. Values for arguments with a
   *     structured DataType carry the decoded {@link UaStructuredType} value (or, for array
   *     arguments, an array of the DataType's registered class, e.g. {@code XVType[]}) rather than
   *     the raw {@link ExtensionObject}(s) received in the request.
   * @return this output values matching this Method's output arguments, if any.
   * @throws UaException if invocation has failed for some reason.
   */
  protected abstract Variant[] invoke(InvocationContext invocationContext, Variant[] inputValues)
      throws UaException;

  /**
   * Validate the input values against the expected input arguments.
   *
   * <p>The DataType of each input value has already been verified; implementations need only verify
   * the value is "valid", if applicable, and throw InvalidArgumentException with a StatusCode of
   * Bad_OutOfRange for any invalid input values.
   *
   * @param inputArgumentValues the input values provided by the client for the current method call.
   *     Values for arguments with a structured DataType carry decoded {@link UaStructuredType}
   *     values rather than raw {@link ExtensionObject}s.
   * @throws InvalidArgumentException if one or more input argument values are invalid.
   */
  protected void validateInputArgumentValues(Variant[] inputArgumentValues)
      throws InvalidArgumentException {}

  /**
   * Extends {@link AccessContext} to provide additional context to implementations of {@link
   * AbstractMethodInvocationHandler}.
   */
  public interface InvocationContext extends AccessContext {

    /**
     * Get the {@link OpcUaServer} instance.
     *
     * @return the {@link OpcUaServer} instance.
     */
    OpcUaServer getServer();

    /**
     * Get the {@link NodeId} of the ObjectNode the method being invoked belongs to.
     *
     * @return the {@link NodeId} of the ObjectNode the method being invoked belongs to.
     */
    NodeId getObjectId();

    /**
     * Get the {@link UaMethodNode} being invoked.
     *
     * @return the {@link UaMethodNode} being invoked.
     */
    UaMethodNode getMethodNode();
  }
}
