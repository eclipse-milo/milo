/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.gds;

import static java.util.Objects.requireNonNullElse;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.concurrent.CompletableFuture.failedFuture;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.methods.UaMethodException;
import org.eclipse.milo.opcua.stack.core.NodeIds;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathResult;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathTarget;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CallMethodResult;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePath;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePathElement;
import org.jspecify.annotations.Nullable;

/** Shared service-call plumbing for {@link GdsClient} and {@link TrustListReader}. */
final class ClientCalls {

  private ClientCalls() {}

  /** Decodes a method's output arguments into a typed result. */
  @FunctionalInterface
  interface OutputDecoder<T> {
    T decode(MethodOutputs outputs) throws UaException;
  }

  /**
   * Invoke one method with a single Call request.
   *
   * <p>A Bad operation-level result fails the future with a {@link UaMethodException} carrying the
   * server's {@link StatusCode}; a Good result with at least {@code requiredOutputs} output
   * arguments is decoded by {@code decoder}.
   */
  static <T> CompletableFuture<T> call(
      OpcUaClient client,
      NodeId objectId,
      NodeId methodId,
      String methodName,
      Variant[] inputs,
      int requiredOutputs,
      OutputDecoder<T> decoder) {

    var request = new CallMethodRequest(objectId, methodId, inputs);

    return client
        .callAsync(List.of(request))
        .thenCompose(
            response -> {
              CallMethodResult[] results =
                  requireNonNullElse(response.getResults(), new CallMethodResult[0]);

              if (results.length != 1) {
                return failedFuture(
                    new UaException(
                        StatusCodes.Bad_UnexpectedError,
                        methodName + ": expected 1 result, received " + results.length));
              }

              CallMethodResult result = results[0];
              StatusCode statusCode = result.getStatusCode();

              if (!statusCode.isGood()) {
                return failedFuture(
                    new UaMethodException(
                        statusCode,
                        result.getInputArgumentResults(),
                        result.getInputArgumentDiagnosticInfos()));
              }

              try {
                Variant[] outputs = requireNonNullElse(result.getOutputArguments(), new Variant[0]);

                return completedFuture(
                    decoder.decode(MethodOutputs.of(methodName, outputs, requiredOutputs)));
              } catch (UaException e) {
                return failedFuture(e);
              }
            });
  }

  /**
   * Read the Value attribute of the namespace 0 Properties named {@code propertyNames} under {@code
   * objectId}, using one TranslateBrowsePaths request and one Read request.
   *
   * @return a list aligned with {@code propertyNames}; an entry is {@code null} when the Property
   *     does not exist on the object.
   */
  static CompletableFuture<List<@Nullable DataValue>> readProperties(
      OpcUaClient client, NodeId objectId, List<String> propertyNames) {

    List<BrowsePath> browsePaths =
        propertyNames.stream()
            .map(
                name ->
                    new BrowsePath(
                        objectId,
                        new RelativePath(
                            new RelativePathElement[] {
                              new RelativePathElement(
                                  NodeIds.HasProperty, false, false, new QualifiedName(0, name))
                            })))
            .toList();

    return client
        .translateBrowsePathsAsync(browsePaths)
        .thenCompose(
            response -> {
              BrowsePathResult[] results =
                  requireNonNullElse(response.getResults(), new BrowsePathResult[0]);

              if (results.length != propertyNames.size()) {
                return failedFuture(
                    new UaException(
                        StatusCodes.Bad_UnexpectedError,
                        "TranslateBrowsePaths: expected "
                            + propertyNames.size()
                            + " results, received "
                            + results.length));
              }

              List<@Nullable NodeId> propertyIds = new ArrayList<>();
              List<NodeId> idsToRead = new ArrayList<>();

              for (BrowsePathResult result : results) {
                NodeId propertyId = null;

                if (result.getStatusCode().isGood()) {
                  BrowsePathTarget[] targets =
                      requireNonNullElse(result.getTargets(), new BrowsePathTarget[0]);

                  if (targets.length > 0) {
                    propertyId =
                        targets[0].getTargetId().toNodeId(client.getNamespaceTable()).orElse(null);
                  }
                }

                propertyIds.add(propertyId);

                if (propertyId != null) {
                  idsToRead.add(propertyId);
                }
              }

              if (idsToRead.isEmpty()) {
                return completedFuture(Arrays.asList(new DataValue[propertyNames.size()]));
              }

              return client
                  .readValuesAsync(0.0, TimestampsToReturn.Neither, idsToRead)
                  .thenApply(
                      values -> {
                        List<@Nullable DataValue> aligned = new ArrayList<>();
                        int next = 0;

                        for (NodeId propertyId : propertyIds) {
                          aligned.add(propertyId != null ? values.get(next++) : null);
                        }

                        return aligned;
                      });
            });
  }

  /**
   * Wait for {@code future}, rethrowing a failed {@link UaException} (including {@link
   * UaMethodException}) as is and wrapping any other failure.
   */
  static <T> T await(CompletableFuture<T> future) throws UaException {
    try {
      return future.get();
    } catch (ExecutionException e) {
      Throwable cause = e.getCause();

      if (cause instanceof UaException ux) {
        throw ux;
      } else {
        throw new UaException(cause);
      }
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    }
  }
}
