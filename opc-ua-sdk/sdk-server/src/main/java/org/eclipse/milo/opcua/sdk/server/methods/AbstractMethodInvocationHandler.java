/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server.methods;

import static java.util.Objects.requireNonNullElse;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import org.eclipse.milo.opcua.sdk.core.typetree.DataType;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.sdk.server.AccessContext;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.sdk.server.Session;
import org.eclipse.milo.opcua.sdk.server.nodes.UaMethodNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
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
      Variant[] inputArgumentValues =
          requireNonNullElse(request.getInputArguments(), new Variant[0]);

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
        Object value = variant.getValue();

        boolean dataTypeMatch = true;

        if (value != null) {
          NodeId argDataTypeId = argument.getDataType();

          NodeId valueDataTypeId =
              variant
                  .getDataTypeId()
                  .flatMap(xni -> xni.toNodeId(node.getNodeContext().getNamespaceTable()))
                  .orElse(NodeId.NULL_VALUE);

          if (!argDataTypeId.equals(valueDataTypeId)) {
            DataTypeTree dataTypeTree = node.getNodeContext().getServer().getDataTypeTree();

            if (dataTypeTree.isStructType(argDataTypeId)) {
              ExtensionObject xo = (ExtensionObject) value;
              Object decoded = xo.decode(node.getNodeContext().getServer().getEncodingContext());

              if (decoded instanceof UaStructuredType structuredType) {
                valueDataTypeId =
                    structuredType
                        .getTypeId()
                        .toNodeId(node.getNodeContext().getNamespaceTable())
                        .orElse(NodeId.NULL_VALUE);

                DataType argType = dataTypeTree.getType(argDataTypeId);
                boolean isAbstract = argType != null && argType.isAbstract();

                if (isAbstract) {
                  dataTypeMatch = dataTypeTree.isSubtypeOf(valueDataTypeId, argDataTypeId);
                } else {
                  dataTypeMatch = Objects.equals(valueDataTypeId, argDataTypeId);
                }
              } else {
                dataTypeMatch = false;
              }

            } else {
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
   *     verified to be of the type specified by its {@link Argument}.
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
