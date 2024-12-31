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

import static java.util.Objects.requireNonNull;
import static java.util.Objects.requireNonNullElse;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseNextRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseNextResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowseResult;
import org.eclipse.milo.opcua.stack.core.types.structured.ReferenceDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.ViewDescription;

/**
 * "Helper" functions for doing a Browse followed by as many BrowseNext calls as are necessary to
 * retrieve all the references.
 */
public class BrowseHelper {

  private BrowseHelper() {}

  public static CompletableFuture<List<ReferenceDescription>> browse(
      OpcUaClient client, BrowseDescription browseDescription, UInteger maxReferencesPerNode) {

    return client
        .getSessionAsync()
        .thenCompose(session -> browse(client, session, browseDescription, maxReferencesPerNode));
  }

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

    return client
        .getTransport()
        .sendRequestMessage(browseRequest)
        .thenApply(BrowseResponse.class::cast)
        .thenCompose(
            response -> {
              BrowseResult result = requireNonNull(response.getResults())[0];

              List<ReferenceDescription> references =
                  Collections.synchronizedList(new ArrayList<>());

              return maybeBrowseNext(client, session, references, result);
            });
  }

  private static CompletableFuture<List<ReferenceDescription>> maybeBrowseNext(
      OpcUaClient client,
      OpcUaSession session,
      List<ReferenceDescription> references,
      BrowseResult result) {

    if (result.getStatusCode().isGood()) {
      Collections.addAll(
          references, requireNonNullElse(result.getReferences(), new ReferenceDescription[0]));

      ByteString nextContinuationPoint = result.getContinuationPoint();

      if (nextContinuationPoint == null || nextContinuationPoint.isNull()) {
        return CompletableFuture.completedFuture(references);
      } else {
        return browseNext(client, session, nextContinuationPoint, references);
      }
    } else {
      return CompletableFuture.completedFuture(references);
    }
  }

  private static CompletableFuture<List<ReferenceDescription>> browseNext(
      OpcUaClient client,
      OpcUaSession session,
      ByteString continuationPoint,
      List<ReferenceDescription> references) {

    BrowseNextRequest browseNextRequest =
        new BrowseNextRequest(
            client.newRequestHeader(
                session.getAuthenticationToken(), client.getConfig().getRequestTimeout()),
            false,
            new ByteString[] {continuationPoint});

    return client
        .getTransport()
        .sendRequestMessage(browseNextRequest)
        .thenApply(BrowseNextResponse.class::cast)
        .thenCompose(
            response -> {
              BrowseResult result = requireNonNull(response.getResults())[0];

              return maybeBrowseNext(client, session, references, result);
            });
  }
}
