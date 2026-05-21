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

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfigBuilder;

/**
 * One-shot helper for creating a reverse client when the endpoint is not known up front.
 *
 * <p>The helper waits for one reverse connection, consumes it for {@code GetEndpoints}, selects an
 * endpoint, creates a normal reverse {@link OpcUaClient}, and calls {@link
 * OpcUaClient#connectAsync()} so the returned future completes only after the later production
 * reverse connection has opened a Session.
 *
 * <p>The first reverse connection is only a discovery channel. It is closed after {@code
 * GetEndpoints}; the production Session always waits for a later matching reverse connection. The
 * caller owns the {@link ReverseConnectManager} lifecycle and remains responsible for disconnecting
 * the returned client.
 */
public final class DiscoveryFirstReverseConnectClient {

  private final ReverseConnectManager manager;
  private final ReverseConnectSelector discoverySelector;
  private final ReverseConnectEndpointSelector endpointSelector;
  private final ReverseConnectClientConfigFactory clientConfigFactory;
  private final BiFunction<
          ReverseConnectDiscoveryResult, EndpointDescription, ReverseConnectSelector>
      productionSelectorFactory;
  private final boolean defaultProductionSelectorFactory;
  private final Consumer<OpcTcpClientTransportConfigBuilder> discoveryTransportCustomizer;
  private final Consumer<OpcTcpClientTransportConfigBuilder> productionTransportCustomizer;

  private DiscoveryFirstReverseConnectClient(Builder builder) {
    manager = builder.manager;
    discoverySelector = builder.discoverySelector;
    endpointSelector = builder.endpointSelector;
    clientConfigFactory = builder.clientConfigFactory;
    productionSelectorFactory = builder.productionSelectorFactory;
    defaultProductionSelectorFactory = builder.defaultProductionSelectorFactory;
    discoveryTransportCustomizer = builder.discoveryTransportCustomizer;
    productionTransportCustomizer = builder.productionTransportCustomizer;
  }

  /**
   * Create a builder for a discovery-first reverse client.
   *
   * @param manager the running manager that owns client listener sockets.
   * @return a new builder.
   */
  public static Builder builder(ReverseConnectManager manager) {
    return new Builder(manager);
  }

  /**
   * Run discovery and connect the production reverse client asynchronously.
   *
   * <p>The returned future first waits for the discovery selector to claim a candidate, then runs
   * endpoint discovery, selects an endpoint, creates the production reverse client, and completes
   * after that production client opens its Session. Cancelling the future returned by the discovery
   * stage unregisters the one-shot discovery selector.
   *
   * @return a future that completes with the connected production client.
   */
  public CompletableFuture<OpcUaClient> connectAsync() {
    return ReverseConnectDiscovery.getEndpoints(
            manager, discoverySelector, discoveryTransportCustomizer)
        .thenCompose(this::connectProductionClient);
  }

  private CompletableFuture<OpcUaClient> connectProductionClient(
      ReverseConnectDiscoveryResult discovery) {

    return endpointSelector
        .select(discovery)
        .map(endpoint -> connectProductionClient(discovery, endpoint))
        .orElseGet(
            () ->
                CompletableFuture.failedFuture(
                    new UaException(
                        StatusCodes.Bad_ConfigurationError,
                        "no endpoint selected from Reverse Connect discovery result")));
  }

  private CompletableFuture<OpcUaClient> connectProductionClient(
      ReverseConnectDiscoveryResult discovery, EndpointDescription endpoint) {

    if (defaultProductionSelectorFactory
        && !ReverseConnectProductionSelectors.hasRoutingHint(discovery.candidate())) {
      return CompletableFuture.failedFuture(
          ReverseConnectProductionSelectors.missingRoutingHintsException());
    }

    OpcUaClient client;
    try {
      OpcUaClientConfig clientConfig =
          requireNonNull(clientConfigFactory.create(discovery, endpoint), "clientConfig");
      ReverseConnectSelector productionSelector =
          requireNonNull(
              productionSelectorFactory.apply(discovery, endpoint), "productionSelector");

      client =
          OpcUaClient.createReverseConnect(
              clientConfig, manager, productionSelector, productionTransportCustomizer);
    } catch (Throwable t) {
      return CompletableFuture.failedFuture(t);
    }

    return client
        .connectAsync()
        .whenComplete(
            (c, ex) -> {
              if (ex != null) {
                client.disconnectAsync();
              }
            });
  }

  private static ReverseConnectSelector defaultProductionSelector(
      ReverseConnectDiscoveryResult discovery, EndpointDescription endpoint) {

    return ReverseConnectProductionSelectors.matchDiscoveryRoutingHints(discovery.candidate());
  }

  /**
   * Builder for {@link DiscoveryFirstReverseConnectClient}.
   *
   * <p>By default the helper accepts any discovery candidate, prefers an endpoint whose URL matches
   * the discovery {@code ReverseHello}, requires a no-security endpoint that allows anonymous
   * activation, builds a basic anonymous client config from the selected endpoint and full
   * discovery endpoint list, and requires the discovery {@code ReverseHello} to provide at least
   * one production routing hint.
   */
  public static final class Builder {

    private final ReverseConnectManager manager;

    private ReverseConnectSelector discoverySelector = ReverseConnectSelector.any();
    private ReverseConnectEndpointSelector endpointSelector =
        ReverseConnectEndpointSelectors.preferReverseHelloEndpointUrl(
            ReverseConnectEndpointSelectors::isNoSecurityAndAnonymous);
    private ReverseConnectClientConfigFactory clientConfigFactory =
        (discovery, endpoint) ->
            OpcUaClientConfig.builder()
                .setEndpoint(endpoint)
                .setDiscoveryEndpoints(discovery.endpoints())
                .setSessionEndpointValidationEnabled(false)
                .build();
    private BiFunction<ReverseConnectDiscoveryResult, EndpointDescription, ReverseConnectSelector>
        productionSelectorFactory = DiscoveryFirstReverseConnectClient::defaultProductionSelector;
    private boolean defaultProductionSelectorFactory = true;
    private Consumer<OpcTcpClientTransportConfigBuilder> discoveryTransportCustomizer = b -> {};
    private Consumer<OpcTcpClientTransportConfigBuilder> productionTransportCustomizer = b -> {};

    private Builder(ReverseConnectManager manager) {
      this.manager = requireNonNull(manager, "manager");
    }

    /**
     * Set the selector used to claim the discovery connection.
     *
     * <p>This selector is evaluated before endpoint discovery, using only the pre-SecureChannel
     * {@code ReverseHello} metadata in the candidate snapshot. Use it for coarse routing and
     * admission, not as a substitute for endpoint or certificate validation.
     *
     * @param discoverySelector the discovery selector.
     * @return this builder.
     */
    public Builder setDiscoverySelector(ReverseConnectSelector discoverySelector) {
      this.discoverySelector = requireNonNull(discoverySelector, "discoverySelector");
      return this;
    }

    /**
     * Set the endpoint selector applied to the {@code GetEndpoints} response.
     *
     * <p>The selector receives the discovery candidate snapshot and all discovered endpoints. The
     * default selector chooses a no-security endpoint that supports anonymous Session activation,
     * preferring a URL match with the discovery {@code ReverseHello}.
     *
     * @param endpointSelector the endpoint selector.
     * @return this builder.
     */
    public Builder setEndpointSelector(ReverseConnectEndpointSelector endpointSelector) {
      this.endpointSelector = requireNonNull(endpointSelector, "endpointSelector");
      return this;
    }

    /**
     * Set a simple client-config factory for the selected endpoint.
     *
     * <p>This overload is useful when the client configuration depends only on the selected
     * endpoint. Use {@link #setClientConfig(ReverseConnectClientConfigFactory)} when the config
     * also needs the full discovery endpoint list, the claim-time candidate snapshot, certificates,
     * or an identity provider that depends on discovery context.
     *
     * @param clientConfigFactory the endpoint-to-config factory.
     * @return this builder.
     */
    public Builder setClientConfig(
        Function<EndpointDescription, OpcUaClientConfig> clientConfigFactory) {

      requireNonNull(clientConfigFactory, "clientConfigFactory");
      this.clientConfigFactory = (discovery, endpoint) -> clientConfigFactory.apply(endpoint);
      return this;
    }

    /**
     * Set a client-config factory that can inspect discovery context.
     *
     * <p>The factory runs after endpoint selection and before the production reverse client is
     * created. It should return a normal {@link OpcUaClientConfig} for the later Session
     * connection, including any certificates, identity provider, discovery endpoints, and endpoint
     * validation settings required by the selected endpoint.
     *
     * @param clientConfigFactory the discovery-aware config factory.
     * @return this builder.
     */
    public Builder setClientConfig(ReverseConnectClientConfigFactory clientConfigFactory) {
      this.clientConfigFactory = requireNonNull(clientConfigFactory, "clientConfigFactory");
      return this;
    }

    /**
     * Set the selector used by the production reverse client.
     *
     * <p>The default selector matches the discovery candidate's {@code ServerUri} and {@code
     * EndpointUrl}, and requires at least one of those routing hints to be present. Override this
     * when the server is expected to advertise different routing hints for the production
     * connection or to route candidates without {@code ReverseHello} hints.
     *
     * @param productionSelectorFactory the production selector factory.
     * @return this builder.
     */
    public Builder setProductionSelector(
        BiFunction<ReverseConnectDiscoveryResult, EndpointDescription, ReverseConnectSelector>
            productionSelectorFactory) {

      this.productionSelectorFactory =
          requireNonNull(productionSelectorFactory, "productionSelectorFactory");
      defaultProductionSelectorFactory = false;
      return this;
    }

    /**
     * Set the selector factory used by the production reverse client.
     *
     * <p>This is an alias for {@link #setProductionSelector(BiFunction)} with a name that makes the
     * factory role explicit.
     *
     * @param productionSelectorFactory the production selector factory.
     * @return this builder.
     */
    public Builder setProductionSelectorFactory(
        BiFunction<ReverseConnectDiscoveryResult, EndpointDescription, ReverseConnectSelector>
            productionSelectorFactory) {

      return setProductionSelector(productionSelectorFactory);
    }

    /**
     * Customize the provisional discovery transport.
     *
     * <p>The customizer affects only the temporary transport used for {@code GetEndpoints}. It does
     * not configure the production client transport.
     *
     * @param discoveryTransportCustomizer the discovery transport customizer.
     * @return this builder.
     */
    public Builder setDiscoveryTransport(
        Consumer<OpcTcpClientTransportConfigBuilder> discoveryTransportCustomizer) {

      this.discoveryTransportCustomizer =
          requireNonNull(discoveryTransportCustomizer, "discoveryTransportCustomizer");
      return this;
    }

    /**
     * Customize the provisional discovery transport.
     *
     * <p>This is an alias for {@link #setDiscoveryTransport(Consumer)} that uses the same
     * terminology as the lower-level client factory methods.
     *
     * @param discoveryTransportCustomizer the discovery transport customizer.
     * @return this builder.
     */
    public Builder setDiscoveryTransportCustomizer(
        Consumer<OpcTcpClientTransportConfigBuilder> discoveryTransportCustomizer) {

      return setDiscoveryTransport(discoveryTransportCustomizer);
    }

    /**
     * Customize the production reverse transport.
     *
     * <p>The customizer is applied when creating the normal reverse client that opens the
     * production Session. It does not change the provisional discovery transport.
     *
     * @param productionTransportCustomizer the production transport customizer.
     * @return this builder.
     */
    public Builder setProductionTransport(
        Consumer<OpcTcpClientTransportConfigBuilder> productionTransportCustomizer) {

      this.productionTransportCustomizer =
          requireNonNull(productionTransportCustomizer, "productionTransportCustomizer");
      return this;
    }

    /**
     * Customize the production reverse transport.
     *
     * <p>This is an alias for {@link #setProductionTransport(Consumer)} that uses the same
     * terminology as the lower-level client factory methods.
     *
     * @param productionTransportCustomizer the production transport customizer.
     * @return this builder.
     */
    public Builder setProductionTransportCustomizer(
        Consumer<OpcTcpClientTransportConfigBuilder> productionTransportCustomizer) {

      return setProductionTransport(productionTransportCustomizer);
    }

    /**
     * Build the helper.
     *
     * @return a discovery-first reverse client helper.
     */
    public DiscoveryFirstReverseConnectClient build() {
      return new DiscoveryFirstReverseConnectClient(this);
    }

    /**
     * Build the helper and start the discovery-first connect flow.
     *
     * @return a future that completes with the connected production client.
     */
    public CompletableFuture<OpcUaClient> connectAsync() {
      return build().connectAsync();
    }
  }
}
