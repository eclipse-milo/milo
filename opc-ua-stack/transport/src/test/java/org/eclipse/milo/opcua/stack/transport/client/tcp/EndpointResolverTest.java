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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.junit.jupiter.api.Test;

class EndpointResolverTest {

  private static final String SERVER_URI = "urn:test:server";
  private static final String ENDPOINT_URL = "opc.tcp://localhost:4840";

  private static EndpointDescription createEndpoint(String endpointUrl) {
    return new EndpointDescription(endpointUrl, null, null, null, null, null, null, null);
  }

  /** Stub Discovery that returns a fixed list without consuming a real channel. */
  private static EndpointResolver.Discovery stubDiscovery(List<EndpointDescription> endpoints) {
    return () -> CompletableFuture.completedFuture(endpoints);
  }

  @Test
  void cachedResolverReturnsEndpoint() throws Exception {
    var endpoint = createEndpoint(ENDPOINT_URL);

    EndpointResolver resolver = EndpointResolver.cached(endpoint);

    // Discovery should not be called — use a flag to verify.
    var discoveryCalled = new AtomicBoolean(false);
    EndpointResolver.Discovery discovery =
        () -> {
          discoveryCalled.set(true);
          return CompletableFuture.completedFuture(List.of());
        };

    CompletableFuture<EndpointDescription> future =
        resolver.resolve(SERVER_URI, ENDPOINT_URL, discovery);

    assertTrue(future.isDone());
    assertEquals(endpoint, future.get());
    assertFalse(discoveryCalled.get(), "Discovery should not be called for cached resolver");
  }

  @Test
  void discoverResolverCallsDiscoveryAndSelectsEndpoint() throws Exception {
    var expected = createEndpoint(ENDPOINT_URL);
    var other = createEndpoint("opc.tcp://localhost:4841");

    EndpointResolver resolver =
        EndpointResolver.discover((serverUri, endpoints) -> endpoints.get(0));

    EndpointResolver.Discovery discovery = stubDiscovery(List.of(expected, other));

    CompletableFuture<EndpointDescription> future =
        resolver.resolve(SERVER_URI, ENDPOINT_URL, discovery);

    assertTrue(future.isDone());
    assertEquals(expected, future.get());
  }

  @Test
  void customResolverReturnsCachedEndpoint() throws Exception {
    var endpoint = createEndpoint(ENDPOINT_URL);

    EndpointResolver resolver =
        (serverUri, endpointUrl, discovery) -> CompletableFuture.completedFuture(endpoint);

    CompletableFuture<EndpointDescription> future =
        resolver.resolve(SERVER_URI, ENDPOINT_URL, stubDiscovery(List.of()));

    assertTrue(future.isDone());
    assertEquals(endpoint, future.get());
  }

  @Test
  void customResolverCallsDiscovery() throws Exception {
    var expected = createEndpoint(ENDPOINT_URL);

    EndpointResolver resolver =
        (serverUri, endpointUrl, discovery) ->
            discovery.getEndpoints().thenApply(endpoints -> endpoints.get(0));

    EndpointResolver.Discovery discovery = stubDiscovery(List.of(expected));

    CompletableFuture<EndpointDescription> future =
        resolver.resolve(SERVER_URI, ENDPOINT_URL, discovery);

    assertTrue(future.isDone());
    assertEquals(expected, future.get());
  }

  @Test
  void resolverExceptionPropagates() {
    var cause = new RuntimeException("resolve failed");

    EndpointResolver resolver =
        (serverUri, endpointUrl, discovery) -> CompletableFuture.failedFuture(cause);

    CompletableFuture<EndpointDescription> future =
        resolver.resolve(SERVER_URI, ENDPOINT_URL, stubDiscovery(List.of()));

    assertTrue(future.isCompletedExceptionally());

    ExecutionException ex = assertThrows(ExecutionException.class, future::get);
    assertEquals(cause, ex.getCause());
  }

  @Test
  void resolverThrownExceptionPropagates() {
    var cause = new RuntimeException("resolver threw");

    EndpointResolver resolver =
        (serverUri, endpointUrl, discovery) -> {
          throw cause;
        };

    RuntimeException ex =
        assertThrows(
            RuntimeException.class,
            () -> resolver.resolve(SERVER_URI, ENDPOINT_URL, stubDiscovery(List.of())));
    assertEquals(cause, ex);
  }
}
