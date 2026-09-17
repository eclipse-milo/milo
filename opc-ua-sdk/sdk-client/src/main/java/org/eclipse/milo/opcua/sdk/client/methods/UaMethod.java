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

import static java.util.Objects.requireNonNullElse;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaMethodNode;
import org.eclipse.milo.opcua.sdk.client.nodes.UaObjectNode;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.structured.Argument;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;

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
    return callResult(inputs).requireGood();
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
    return callResultAsync(inputs)
        .thenCompose(
            result -> {
              try {
                return CompletableFuture.completedFuture(result.requireGood());
              } catch (UaException e) {
                return CompletableFuture.failedFuture(e);
              }
            });
  }

  /**
   * Call this method and return the complete outcome, using the client's default request options.
   *
   * <p>An operation-level status that is not Good is reported in the result, not thrown.
   *
   * @param inputs an array of {@link Variant}s containing the input values.
   * @return the complete outcome of the call.
   * @throws UaException if a service-level error occurs.
   */
  public MethodCallResult<Variant[]> callResult(Variant[] inputs) throws UaException {
    return callResult(MethodCallOptions.DEFAULT, inputs);
  }

  /**
   * Call this method with per-call options and return the complete outcome.
   *
   * <p>An operation-level status that is not Good is reported in the result, not thrown.
   *
   * @param options the ReturnDiagnostics mask and timeout for this call.
   * @param inputs an array of {@link Variant}s containing the input values.
   * @return the complete outcome of the call.
   * @throws UaException if a service-level error occurs.
   */
  public MethodCallResult<Variant[]> callResult(MethodCallOptions options, Variant[] inputs)
      throws UaException {
    try {
      return callResultAsync(options, inputs).get();
    } catch (ExecutionException e) {
      throw unwrap(e.getCause());
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }

  /**
   * Call this method and return the complete outcome, using the client's default request options.
   *
   * <p>This method completes asynchronously. An operation-level status that is not Good is reported
   * in the result, not as an exceptional completion.
   *
   * @param inputs an array of {@link Variant}s containing the input values.
   * @return a {@link CompletableFuture} that completes successfully with the outcome of the call or
   *     completes exceptionally if a service-level error occurs.
   */
  public CompletableFuture<MethodCallResult<Variant[]>> callResultAsync(Variant[] inputs) {
    return callResultAsync(MethodCallOptions.DEFAULT, inputs);
  }

  /**
   * Call this method with per-call options and return the complete outcome.
   *
   * <p>This method completes asynchronously. An operation-level status that is not Good is reported
   * in the result, not as an exceptional completion.
   *
   * @param options the ReturnDiagnostics mask and timeout for this call.
   * @param inputs an array of {@link Variant}s containing the input values.
   * @return a {@link CompletableFuture} that completes successfully with the outcome of the call or
   *     completes exceptionally if a service-level error occurs.
   */
  public CompletableFuture<MethodCallResult<Variant[]>> callResultAsync(
      MethodCallOptions options, Variant[] inputs) {

    var request = new CallMethodRequest(objectNode.getNodeId(), methodNode.getNodeId(), inputs);

    return client
        .callAsync(List.of(request), options)
        .thenCompose(
            response -> {
              CallMethodResult[] results =
                  requireNonNullElse(response.getResults(), new CallMethodResult[0]);

              if (results.length != 1) {
                return CompletableFuture.failedFuture(
                    new UaException(
                        StatusCodes.Bad_UnexpectedError,
                        "expected 1 result, received " + results.length));
              }

              return CompletableFuture.completedFuture(
                  MethodCallResult.of(results[0], response.getResponseHeader()));
            });
  }

  /**
   * Rethrow the failure of a blocking call as the {@link UaException} found in its cause chain, so
   * a {@link UaMethodException} keeps its type and payload.
   */
  private static UaException unwrap(Throwable cause) {
    return UaException.extract(cause).orElseGet(() -> new UaException(cause));
  }
}
