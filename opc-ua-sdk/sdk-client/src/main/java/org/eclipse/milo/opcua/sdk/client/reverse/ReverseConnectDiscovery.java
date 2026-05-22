/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.reverse;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfigBuilder;

/**
 * Helpers for running {@code GetEndpoints} over one claimed Reverse Connect candidate.
 *
 * <p>The helper registers a one-shot selector with a running {@link ReverseConnectManager}, claims
 * the first matching inbound candidate, consumes that connection with {@link DiscoveryClient}, and
 * returns the candidate snapshot alongside the discovered endpoints. The claimed discovery
 * connection is closed by {@link DiscoveryClient#getEndpoints(ReverseConnectConnection)} and is not
 * reused for the production Session.
 *
 * <p>This is the lower-level primitive behind {@link DiscoveryFirstReverseConnectClient} and {@link
 * ReverseConnectAcceptor}. Applications that use it directly are responsible for selecting an
 * endpoint, creating the production reverse client, and registering a selector for a later matching
 * reverse connection.
 */
public final class ReverseConnectDiscovery {

  private ReverseConnectDiscovery() {}

  /**
   * Wait for a matching reverse connection and query its endpoints.
   *
   * <p>The manager must already be listening. If the returned future is cancelled before a
   * candidate is claimed, the one-shot selector registration is closed.
   *
   * @param manager the running manager that owns client listener sockets.
   * @param selector the selector used to claim the discovery candidate.
   * @return a future that completes with the candidate snapshot and discovered endpoints.
   */
  public static CompletableFuture<ReverseConnectDiscoveryResult> getEndpoints(
      ReverseConnectManager manager, ReverseConnectSelector selector) {

    return getEndpoints(manager, selector, b -> {});
  }

  /**
   * Wait for a matching reverse connection and query its endpoints.
   *
   * <p>The transport customizer configures only the provisional discovery transport. Production
   * client transport settings are applied later when creating the real reverse client. If the
   * returned future is cancelled before a candidate is claimed, the one-shot selector registration
   * is closed.
   *
   * @param manager the running manager that owns client listener sockets.
   * @param selector the selector used to claim the discovery candidate.
   * @param configureTransport a Consumer that receives an {@link
   *     OpcTcpClientTransportConfigBuilder} for the discovery transport.
   * @return a future that completes with the candidate snapshot and discovered endpoints.
   */
  public static CompletableFuture<ReverseConnectDiscoveryResult> getEndpoints(
      ReverseConnectManager manager,
      ReverseConnectSelector selector,
      Consumer<OpcTcpClientTransportConfigBuilder> configureTransport) {

    requireNonNull(manager, "manager");
    requireNonNull(selector, "selector");
    requireNonNull(configureTransport, "configureTransport");

    ReverseConnectRegistration registration = manager.registerSelector(selector);
    AtomicReference<ReverseConnectConnection> claimedConnection = new AtomicReference<>();

    CompletableFuture<ReverseConnectDiscoveryResult> result = new CompletableFuture<>();
    result.whenComplete(
        (r, ex) -> {
          registration.close();
          if (ex != null) {
            // If cancellation arrives after a connection was claimed, the in-flight discovery
            // pipeline holds the socket and will not observe the cancellation on its own. Closing
            // the connection here makes the in-flight GetEndpoints fail promptly instead of
            // running to completion on a future that no caller is reading.
            ReverseConnectConnection connection = claimedConnection.getAndSet(null);
            if (connection != null) {
              connection.close();
            }
          }
        });

    registration
        .connectionFuture()
        .thenCompose(
            connection -> {
              claimedConnection.set(connection);
              return DiscoveryClient.getEndpoints(connection, configureTransport)
                  .thenApply(endpoints -> result(connection.snapshot(), endpoints));
            })
        .whenComplete(
            (discovery, ex) -> {
              claimedConnection.set(null);
              if (ex != null) {
                result.completeExceptionally(ex);
              } else {
                result.complete(discovery);
              }
            });

    return result;
  }

  private static ReverseConnectDiscoveryResult result(
      ReverseConnectCandidateSnapshot candidate, List<EndpointDescription> endpoints) {

    return new ReverseConnectDiscoveryResult(candidate, List.copyOf(endpoints));
  }
}
