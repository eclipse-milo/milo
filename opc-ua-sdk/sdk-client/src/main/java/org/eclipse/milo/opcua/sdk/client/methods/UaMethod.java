/*
 * Copyright (c) 2025 the Eclipse Milo Authors
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
import org.eclipse.milo.opcua.stack.core.Diagnostic;
import org.eclipse.milo.opcua.stack.core.ResponseDiagnostics;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DiagnosticInfo;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.CallResponse;

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
      if (UaException.extract(e).orElse(null) instanceof UaMethodException mx) {
        throw new UaMethodException(
            mx.getCause(),
            mx.getStatusCode(),
            mx.getDiagnostic().orElse(null),
            mx.getInputArgumentResults(),
            mx.getInputArgumentDiagnostics());
      } else {
        throw new UaException(e);
      }
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
        .thenCompose(
            response -> {
              CallMethodResult result = requireNonNull(response.getResults())[0];
              StatusCode statusCode = result.getStatusCode();

              if (statusCode.isGood()) {
                return CompletableFuture.completedFuture(result.getOutputArguments());
              } else {
                return CompletableFuture.failedFuture(methodException(response, result));
              }
            });
  }

  /**
   * Create the {@link UaMethodException} for the first operation of {@code response}, with its
   * diagnostics resolved against the response's string table.
   *
   * @param response the {@link CallResponse} for a single-operation request.
   * @param result the first operation's result.
   * @return the {@link UaMethodException} for {@code result}.
   */
  static UaMethodException methodException(CallResponse response, CallMethodResult result) {
    ResponseDiagnostics diagnostics = ResponseDiagnostics.of(response.getResponseHeader());

    DiagnosticInfo[] operationDiagnostics = response.getDiagnosticInfos();
    Diagnostic diagnostic =
        operationDiagnostics != null && operationDiagnostics.length > 0
            ? diagnostics.resolve(operationDiagnostics[0])
            : null;

    return new UaMethodException(
        result.getStatusCode(),
        diagnostic,
        result.getInputArgumentResults(),
        diagnostics.resolve(result.getInputArgumentDiagnosticInfos()));
  }
}
