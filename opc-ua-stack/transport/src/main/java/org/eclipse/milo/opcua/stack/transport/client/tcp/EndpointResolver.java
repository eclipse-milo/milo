/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client.tcp;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

/**
 * Resolves an {@link EndpointDescription} for an unknown server that sent a ReverseHello.
 *
 * <p>The resolver may return a known endpoint directly (1-shot) or call {@link
 * Discovery#getEndpoints()} to perform online discovery on the inbound channel (2-shot). Both paths
 * return a {@code CompletableFuture<EndpointDescription>}.
 */
public interface EndpointResolver {

  /**
   * Resolve the endpoint to use for a server that sent a ReverseHello.
   *
   * <p>The resolver may return a known endpoint directly (1-shot) or call {@link
   * Discovery#getEndpoints()} to perform online discovery on the inbound channel (2-shot). Both
   * paths return a {@code CompletableFuture<EndpointDescription>}.
   *
   * @param serverUri the server's ApplicationUri from the ReverseHello.
   * @param endpointUrl the server's EndpointUrl from the ReverseHello.
   * @param discovery a capability for performing GetEndpoints on the inbound channel.
   * @return a future that completes with the resolved endpoint.
   */
  CompletableFuture<EndpointDescription> resolve(
      String serverUri, String endpointUrl, Discovery discovery);

  /**
   * A capability for performing OPC UA GetEndpoints discovery on the inbound channel.
   *
   * <p>Calling {@link #getEndpoints()} drives the full Hello/Ack/OpenSecureChannel/ GetEndpoints
   * sequence on the channel. After the call, the channel is consumed and will be closed — the
   * server must reconnect for the actual session.
   */
  @FunctionalInterface
  interface Discovery {

    /**
     * Perform GetEndpoints on the inbound channel.
     *
     * @return a future that completes with the list of discovered endpoints.
     */
    CompletableFuture<List<EndpointDescription>> getEndpoints();
  }

  /**
   * Create a resolver that always returns the given endpoint (1-shot). The {@link Discovery} object
   * is ignored and the channel is not consumed.
   *
   * @param endpoint the endpoint to return.
   * @return a resolver that completes immediately with the given endpoint.
   */
  static EndpointResolver cached(EndpointDescription endpoint) {
    return (serverUri, endpointUrl, discovery) -> CompletableFuture.completedFuture(endpoint);
  }

  /**
   * Create a resolver that performs online discovery and selects an endpoint (2-shot).
   *
   * @param selector a function that selects an endpoint from the server URI and discovered endpoint
   *     list.
   * @return a resolver that calls {@link Discovery#getEndpoints()} and applies the selector.
   */
  static EndpointResolver discover(
      BiFunction<String, List<EndpointDescription>, EndpointDescription> selector) {
    return (serverUri, endpointUrl, discovery) ->
        discovery.getEndpoints().thenApply(endpoints -> selector.apply(serverUri, endpoints));
  }
}
