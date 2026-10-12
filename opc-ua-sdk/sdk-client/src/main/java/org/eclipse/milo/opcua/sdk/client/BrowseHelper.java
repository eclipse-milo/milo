/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static java.util.Objects.requireNonNullElse;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseNextRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseNextResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ViewDescription;
import org.jspecify.annotations.Nullable;

/**
 * "Helper" functions for doing a Browse followed by as many BrowseNext calls as are necessary to
 * retrieve all the references.
 *
 * <p>The returned future completes with every reference the Server returned, including the
 * references from pages whose StatusCode is Uncertain, such as {@code
 * Uncertain_NotAllNodesAvailable}.
 *
 * <p>The future completes exceptionally with a {@link UaException} if the Browse or a BrowseNext
 * fails at the service level, or if a Browse or BrowseNext result has a Bad StatusCode, such as
 * {@code Bad_NodeIdUnknown} or {@code Bad_ContinuationPointInvalid}. The exception carries that
 * StatusCode. References from earlier pages are not returned in that case.
 *
 * <p>ServiceFaults from these requests are reported to the client's {@link ServiceFaultListener}s,
 * the same as for {@link OpcUaClient#sendRequestAsync}.
 */
public class BrowseHelper {

  private BrowseHelper() {}

  /**
   * Browse a Node on the client's current Session and get all of its references.
   *
   * @param client the {@link OpcUaClient} to browse with.
   * @param browseDescription the {@link BrowseDescription} of the Node to browse.
   * @param maxReferencesPerNode the maximum number of references the Server returns per Browse or
   *     BrowseNext response. 0 lets the Server decide.
   * @return a CompletableFuture that completes with all the references, or completes exceptionally
   *     with a {@link UaException} if a service-level error occurs or a result has a Bad
   *     StatusCode.
   */
  public static CompletableFuture<List<ReferenceDescription>> browse(
      OpcUaClient client, BrowseDescription browseDescription, UInteger maxReferencesPerNode) {

    return client
        .getSessionAsync()
        .thenCompose(session -> browse(client, session, browseDescription, maxReferencesPerNode));
  }

  /**
   * Browse a Node on {@code session} and get all of its references.
   *
   * @param client the {@link OpcUaClient} to browse with.
   * @param session the {@link OpcUaSession} to send the Browse and every BrowseNext on.
   * @param browseDescription the {@link BrowseDescription} of the Node to browse.
   * @param maxReferencesPerNode the maximum number of references the Server returns per Browse or
   *     BrowseNext response. 0 lets the Server decide.
   * @return a CompletableFuture that completes with all the references, or completes exceptionally
   *     with a {@link UaException} if a service-level error occurs or a result has a Bad
   *     StatusCode.
   */
  public static CompletableFuture<List<ReferenceDescription>> browse(
      OpcUaClient client,
      OpcUaSession session,
      BrowseDescription browseDescription,
      UInteger maxReferencesPerNode) {

    BrowseRequest browseRequest =
        new BrowseRequest(
            client.newRequestHeader(
                session.getAuthenticationToken(), client.getConfig().getRequestTimeout()),
            new ViewDescription(NodeId.NULL_VALUE, DateTime.MIN_VALUE, uint(0)),
            maxReferencesPerNode,
            new BrowseDescription[] {browseDescription});

    NodeId nodeId = browseDescription.getNodeId();

    return client
        .sendRequestAsync(browseRequest)
        .thenApply(BrowseResponse.class::cast)
        .thenCompose(
            response -> {
              List<ReferenceDescription> references =
                  Collections.synchronizedList(new ArrayList<>());

              return firstResult(response.getResults(), "Browse")
                  .thenCompose(
                      result -> maybeBrowseNext(client, session, nodeId, references, result));
            });
  }

  private static CompletableFuture<List<ReferenceDescription>> maybeBrowseNext(
      OpcUaClient client,
      OpcUaSession session,
      NodeId nodeId,
      List<ReferenceDescription> references,
      BrowseResult result) {

    StatusCode statusCode = result.getStatusCode();
    ByteString continuationPoint = result.getContinuationPoint();
    boolean hasContinuationPoint = continuationPoint != null && !continuationPoint.isNullOrEmpty();

    if (statusCode.isBad()) {
      var failure =
          new UaException(
              statusCode,
              "Browse of %s failed: %s".formatted(nodeId.toParseableString(), statusCode));

      if (hasContinuationPoint) {
        return releaseAndFail(client, session, continuationPoint, failure);
      } else {
        return CompletableFuture.failedFuture(failure);
      }
    }

    // Good or Uncertain: keep this page and keep paging.
    Collections.addAll(
        references, requireNonNullElse(result.getReferences(), new ReferenceDescription[0]));

    if (hasContinuationPoint) {
      return browseNext(client, session, nodeId, continuationPoint, references);
    } else {
      return CompletableFuture.completedFuture(references);
    }
  }

  private static CompletableFuture<List<ReferenceDescription>> browseNext(
      OpcUaClient client,
      OpcUaSession session,
      NodeId nodeId,
      ByteString continuationPoint,
      List<ReferenceDescription> references) {

    return sendBrowseNext(client, session, false, continuationPoint)
        .thenCompose(response -> firstResult(response.getResults(), "BrowseNext"))
        .thenCompose(result -> maybeBrowseNext(client, session, nodeId, references, result));
  }

  /**
   * Release {@code continuationPoint}, then fail with {@code failure} whether or not the release
   * succeeds.
   */
  private static CompletableFuture<List<ReferenceDescription>> releaseAndFail(
      OpcUaClient client, OpcUaSession session, ByteString continuationPoint, UaException failure) {

    return sendBrowseNext(client, session, true, continuationPoint)
        .handle(
            (response, ex) -> {
              if (ex != null) {
                failure.addSuppressed(ex);
              }
              return CompletableFuture.<List<ReferenceDescription>>failedFuture(failure);
            })
        .thenCompose(f -> f);
  }

  private static CompletableFuture<BrowseNextResponse> sendBrowseNext(
      OpcUaClient client,
      OpcUaSession session,
      boolean releaseContinuationPoints,
      ByteString continuationPoint) {

    BrowseNextRequest browseNextRequest =
        new BrowseNextRequest(
            client.newRequestHeader(
                session.getAuthenticationToken(), client.getConfig().getRequestTimeout()),
            releaseContinuationPoints,
            new ByteString[] {continuationPoint});

    return client.sendRequestAsync(browseNextRequest).thenApply(BrowseNextResponse.class::cast);
  }

  private static CompletableFuture<BrowseResult> firstResult(
      BrowseResult @Nullable [] results, String service) {

    if (results == null || results.length == 0) {
      return CompletableFuture.failedFuture(
          new UaException(StatusCodes.Bad_UnexpectedError, service + " returned no results"));
    } else {
      return CompletableFuture.completedFuture(results[0]);
    }
  }
}
