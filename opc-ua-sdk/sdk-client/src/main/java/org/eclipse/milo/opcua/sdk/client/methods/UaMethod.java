/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.methods;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaObjectNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;

/** A callable method belonging to an ObjectNode. */
public class UaMethod {

  private final OpcUaClient client;
  private final UaObjectNode objectNode;
  private final UaMethodNode methodNode;
  private final Argument[] inputArguments;
  private final Argument[] outputArguments;

  public UaMethod(
      OpcUaClient client,
      UaObjectNode objectNode,
      UaMethodNode methodNode,
      Argument[] inputArguments,
      Argument[] outputArguments) {

    this.client = client;
    this.objectNode = objectNode;
    this.methodNode = methodNode;
    this.inputArguments = inputArguments;
    this.outputArguments = outputArguments;
  }

  /**
   * Get the {@link UaObjectNode} this method belongs to.
   *
   * @return the {@link UaObjectNode} this method belongs to.
   */
  public UaObjectNode getObjectNode() {
    return objectNode;
  }

  /**
   * Get the {@link UaMethodNode} for this method.
   *
   * @return the {@link UaMethodNode} for this method.
   */
  public UaMethodNode getMethodNode() {
    return methodNode;
  }

  /**
   * Get the input arguments for this method.
   *
   * <p>The array will be empty if there are no inputs.
   *
   * @return the input arguments for this method.
   */
  public Argument[] getInputArguments() {
    return inputArguments;
  }

  /**
   * Get the output arguments for this method.
   *
   * <p>The array will be empty if there are no outputs.
   *
   * @return the output arguments for this method.
   */
  public Argument[] getOutputArguments() {
    return outputArguments;
  }

  /**
   * Call this method, passing the values in {@code inputs} for the input arguments.
   *
   * @param inputs an array of {@link Variant}s containing the input values.
   * @return an array of {@link Variant}s containing the output values.
   * @throws UaException if a service-level error occurs.
   * @throws UaMethodException if the operation-level result was not good.
   */
  public Variant[] call(Variant[] inputs) throws UaMethodException, UaException {
    try {
      return callAsync(inputs).get();
    } catch (ExecutionException | InterruptedException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  /**
   * Call this method, passing the values in {@code inputs} for the input arguments.
   *
   * <p>This method completes asynchronously.
   *
   * @param inputs an array of {@link Variant}s containing the input values.
   * @return a {@link CompletableFuture} that completes successfully with the output values or
   *     completes exceptionally if an operation- or service-level error occurs.
   */
  public CompletableFuture<Variant[]> callAsync(Variant[] inputs) {
    var request = new CallMethodRequest(objectNode.getNodeId(), methodNode.getNodeId(), inputs);

    return client
        .callAsync(List.of(request))
        .thenApply(response -> requireNonNull(response.getResults())[0])
        .thenCompose(
            result -> {
              StatusCode statusCode = result.getStatusCode();

              if (statusCode.isGood()) {
                return CompletableFuture.completedFuture(result.getOutputArguments());
              } else {
                return CompletableFuture.failedFuture(
                    new UaMethodException(
                        statusCode,
                        result.getInputArgumentResults(),
                        result.getInputArgumentDiagnosticInfos()));
              }
            });
  }
}
