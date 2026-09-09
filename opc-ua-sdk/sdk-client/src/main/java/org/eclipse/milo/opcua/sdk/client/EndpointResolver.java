/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfigBuilder;

/**
 * Rediscovers and reselects the client's endpoint after a SecureChannel establishment failure.
 *
 * <p>Configure this on {@link OpcUaClientConfigBuilder#setEndpointResolver} alongside the endpoint
 * it must reselect. The client invokes it after SecureChannel establishment fails on a secured
 * endpoint, regardless of the status code or whether the server sends an error, subject to {@link
 * #getRefreshCooldown()}. TCP connection and Hello/Acknowledge failures do not trigger resolution.
 * The client connects with the result on its next attempt. A refresh must select the same endpoint,
 * including its user token policies; only the server certificate is expected to differ.
 * Applications can also call {@link #resolve()} themselves to obtain the initial endpoint.
 */
@FunctionalInterface
public interface EndpointResolver {

  /**
   * Discover and select an endpoint asynchronously.
   *
   * <p>Implementations must return promptly and release their resources on completion or
   * cancellation. Milo runs at most one resolution at a time and discards results after disconnect.
   * Discovery never changes certificate trust; the connection still uses the client's configured
   * validator.
   *
   * @return a cancellable future containing the selected endpoint and discovery response.
   */
  CompletableFuture<EndpointConfiguration> resolve();

  /**
   * Get the positive deadline for each resolution, including custom asynchronous callbacks.
   *
   * @return the resolution timeout, defaulting to ten seconds.
   */
  default Duration getTimeout() {
    return Duration.ofSeconds(10);
  }

  /**
   * Get the minimum interval between refreshes after a qualifying handshake failure.
   *
   * <p>The first refresh after {@code connect()}, or after a channel becomes ready, runs
   * immediately on a qualifying failure; later refreshes wait at least this long, doubling after
   * the first. The interval limits discovery load while a server keeps rejecting the client or
   * advertises unchanged information.
   *
   * @return the non-negative refresh cooldown, defaulting to thirty seconds.
   */
  default Duration getRefreshCooldown() {
    return Duration.ofSeconds(30);
  }

  /**
   * Create a strategy that owns a temporary TCP discovery transport per call.
   *
   * @param discoveryUrl the independently configured GetEndpoints URL.
   * @param selectEndpoint the endpoint selection and optional host-override function.
   * @param configureTransport configures discovery connect and handshake timeouts and resources.
   * @param timeout the positive total discovery deadline, including connection and GetEndpoints.
   * @return the discovery strategy.
   */
  static EndpointResolver create(
      String discoveryUrl,
      Function<List<EndpointDescription>, Optional<EndpointDescription>> selectEndpoint,
      Consumer<OpcTcpClientTransportConfigBuilder> configureTransport,
      Duration timeout) {
    if (timeout.toMillis() <= 0) {
      throw new IllegalArgumentException("timeout must be at least one millisecond");
    }
    return new EndpointResolver() {
      @Override
      public Duration getTimeout() {
        return timeout;
      }

      @Override
      public CompletableFuture<EndpointConfiguration> resolve() {
        // Filter GetEndpoints by the URL's transport profile, as DiscoveryClient.getEndpoints does,
        // so selection never sees endpoints this transport cannot connect to.
        String profileUri;
        try {
          profileUri = DiscoveryClient.transportProfileUri(discoveryUrl);
        } catch (UaException e) {
          return CompletableFuture.failedFuture(e);
        }
        var builder = OpcTcpClientTransportConfig.newBuilder();
        configureTransport.accept(builder);
        var transport = new OpcTcpClientTransport(builder.build());
        var discovery =
            new DiscoveryClient(
                DiscoveryClient.newDiscoveryEndpoint(discoveryUrl, profileUri), transport);
        var result = new CompletableFuture<EndpointConfiguration>();
        // Timeout/cancellation also closes a transport still connecting or awaiting GetEndpoints.
        result.whenComplete(
            (value, error) -> {
              if (error != null) discovery.disconnectAsync();
            });
        CompletableFuture<DiscoveryClient> connection = discovery.connectAsync();
        result.orTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS);
        connection
            .thenCompose(
                c -> c.getEndpoints(discoveryUrl, new String[0], new String[] {profileUri}))
            .thenApply(
                response -> {
                  List<EndpointDescription> endpoints =
                      response.getEndpoints() == null
                          ? List.of()
                          : List.of(response.getEndpoints());
                  EndpointDescription selected =
                      selectEndpoint
                          .apply(endpoints)
                          .orElseThrow(
                              () ->
                                  new CompletionException(
                                      new UaException(
                                          StatusCodes.Bad_ConfigurationError,
                                          "no endpoint selected")));
                  return new EndpointConfiguration(selected, endpoints);
                })
            .whenComplete(
                (value, error) ->
                    discovery
                        .disconnectAsync()
                        .whenComplete(
                            (ignored, closeError) -> {
                              if (error != null) result.completeExceptionally(error);
                              else if (closeError != null) result.completeExceptionally(closeError);
                              else result.complete(value);
                            }));
        return result;
      }
    };
  }
}
