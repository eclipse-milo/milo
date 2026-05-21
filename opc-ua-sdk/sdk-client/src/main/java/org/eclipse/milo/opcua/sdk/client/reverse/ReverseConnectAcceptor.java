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

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.client.DiscoveryClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.transport.client.ChannelStateObservable;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfigBuilder;
import org.jspecify.annotations.NonNull;

/**
 * Dynamic discovery-first acceptor for shared Reverse Connect listeners.
 *
 * <p>The acceptor listens for pending candidates from a {@link ReverseConnectManager}. For each
 * accepted key, it claims the first candidate for discovery, selects an endpoint, creates a normal
 * reverse {@link OpcUaClient}, and connects that client on a later matching reverse connection.
 * Repeated candidates with the same key are ignored so the production client can claim the later
 * connection instead of starting another discovery flow.
 *
 * <p>The acceptor owns only discovery and client creation. It does not own the manager lifecycle
 * and it does not disconnect clients delivered to {@link ReverseConnectAcceptorClientListener};
 * applications should retain and close those clients according to their own lifecycle.
 */
public final class ReverseConnectAcceptor implements AutoCloseable {

  private final ReverseConnectManager manager;
  private final ReverseConnectSelector discoverySelector;
  private final ReverseConnectEndpointSelector endpointSelector;
  private final ReverseConnectClientConfigFactory clientConfigFactory;
  private final BiFunction<
          ReverseConnectDiscoveryResult, EndpointDescription, ReverseConnectSelector>
      productionSelectorFactory;
  private final Function<ReverseConnectCandidateSnapshot, String> keyFunction;
  private final ReverseConnectAcceptorClientListener clientListener;
  private final ReverseConnectAcceptorErrorListener errorListener;
  private final Consumer<OpcTcpClientTransportConfigBuilder> discoveryTransportCustomizer;
  private final Consumer<OpcTcpClientTransportConfigBuilder> productionTransportCustomizer;
  private final Set<String> activeKeys = ConcurrentHashMap.newKeySet();
  private final ReverseConnectListener listener = new AcceptorListener();
  private final AtomicBoolean running = new AtomicBoolean(false);

  private ReverseConnectAcceptor(Builder builder) {
    manager = builder.manager;
    discoverySelector = builder.discoverySelector;
    endpointSelector = builder.endpointSelector;
    clientConfigFactory = builder.clientConfigFactory;
    productionSelectorFactory = builder.productionSelectorFactory;
    keyFunction = builder.keyFunction;
    clientListener = builder.clientListener;
    errorListener = builder.errorListener;
    discoveryTransportCustomizer = builder.discoveryTransportCustomizer;
    productionTransportCustomizer = builder.productionTransportCustomizer;
  }

  /**
   * Create a builder for a dynamic acceptor.
   *
   * @param manager the manager that owns client listener sockets.
   * @return a new builder.
   */
  public static Builder builder(ReverseConnectManager manager) {
    return new Builder(manager);
  }

  /**
   * Start observing pending reverse-connect candidates.
   *
   * <p>The method is idempotent. On the first start it registers a manager listener and also scans
   * the manager snapshot for candidates that were already pending, so candidates accepted during
   * application setup can still start discovery.
   */
  public void start() {
    if (running.compareAndSet(false, true)) {
      manager.addListener(listener);
      for (ReverseConnectCandidateSnapshot candidate : manager.snapshot().pendingCandidates()) {
        if (!running.get()) {
          return;
        }
        onCandidatePending(candidate);
      }
    }
  }

  /**
   * Stop observing new candidates.
   *
   * <p>The method is idempotent. It removes the manager listener but does not cancel discovery
   * requests or production client connections that are already in progress, and it does not
   * disconnect clients that were previously delivered to the client listener.
   */
  public void stop() {
    if (running.compareAndSet(true, false)) {
      manager.removeListener(listener);
    }
  }

  @Override
  public void close() {
    stop();
  }

  private void onCandidatePending(ReverseConnectCandidateSnapshot candidate) {
    String key;
    try {
      if (!discoverySelector.matches(candidate)) {
        return;
      }

      key = requireNonNull(keyFunction.apply(candidate), "keyFunction result");
    } catch (Throwable t) {
      errorListener.onError(candidate, t);
      return;
    }

    if (!activeKeys.add(key)) {
      return;
    }

    ReverseConnectConnection connection = manager.claim(candidate.id()).orElse(null);

    if (connection == null) {
      activeKeys.remove(key);
      return;
    }

    DiscoveryClient.getEndpoints(connection, discoveryTransportCustomizer)
        .thenApply(endpoints -> new ReverseConnectDiscoveryResult(connection.snapshot(), endpoints))
        .thenCompose(discovery -> connectProductionClient(key, discovery))
        .whenComplete(
            (connected, ex) -> {
              if (ex != null) {
                activeKeys.remove(key);
                errorListener.onError(candidate, unwrap(ex));
              }
            });
  }

  private CompletableFuture<OpcUaClient> connectProductionClient(
      String key, ReverseConnectDiscoveryResult discovery) {

    return endpointSelector
        .select(discovery)
        .map(endpoint -> connectProductionClient(key, discovery, endpoint))
        .orElseGet(
            () ->
                CompletableFuture.failedFuture(
                    new UaException(
                        StatusCodes.Bad_ConfigurationError,
                        "no endpoint selected from Reverse Connect discovery result")));
  }

  private CompletableFuture<OpcUaClient> connectProductionClient(
      String key, ReverseConnectDiscoveryResult discovery, EndpointDescription endpoint) {

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
              } else {
                releaseKeyWhenProductionTransportDisconnects(key, c);
                clientListener.onClientConnected(discovery, endpoint, c);
              }
            });
  }

  private void releaseKeyWhenProductionTransportDisconnects(String key, OpcUaClient client) {
    if (client.getTransport() instanceof ChannelStateObservable observable) {
      ChannelStateObservable.TransitionListener listener =
          new ChannelStateObservable.TransitionListener() {
            @Override
            public void onStateTransition(boolean connected) {
              if (connected) {
                activeKeys.add(key);
              } else {
                releaseKeyAndProcessPendingCandidates(key);
              }
            }
          };

      observable.addTransitionListener(listener);
    }
  }

  private void releaseKeyAndProcessPendingCandidates(String key) {
    if (!activeKeys.remove(key) || !running.get()) {
      return;
    }

    for (ReverseConnectCandidateSnapshot candidate : manager.snapshot().pendingCandidates()) {
      if (!running.get()) {
        return;
      }

      onCandidatePending(candidate);
    }
  }

  private static ReverseConnectSelector productionSelector(
      ReverseConnectDiscoveryResult discovery) {
    ReverseConnectCandidateSnapshot candidate = discovery.candidate();

    return snapshot ->
        java.util.Objects.equals(candidate.serverUri(), snapshot.serverUri())
            && java.util.Objects.equals(candidate.endpointUrl(), snapshot.endpointUrl());
  }

  private static String defaultKey(ReverseConnectCandidateSnapshot candidate) {
    if (candidate.serverUri() != null) {
      return candidate.serverUri();
    }
    if (candidate.endpointUrl() != null) {
      return candidate.endpointUrl();
    }
    return candidate.id().toString();
  }

  private static Throwable unwrap(Throwable failure) {
    Throwable cause = failure;
    while (cause instanceof java.util.concurrent.CompletionException && cause.getCause() != null) {
      cause = cause.getCause();
    }
    return cause;
  }

  private final class AcceptorListener implements ReverseConnectListener {

    @Override
    public void onCandidatePending(@NonNull ReverseConnectCandidateSnapshot snapshot) {
      if (running.get()) {
        ReverseConnectAcceptor.this.onCandidatePending(snapshot);
      }
    }
  }

  /**
   * Builder for {@link ReverseConnectAcceptor}.
   *
   * <p>By default the acceptor considers any pending candidate, deduplicates discovery by {@code
   * ServerUri} then endpoint URL then candidate id, selects a no-security anonymous endpoint, and
   * matches the production client with the discovery candidate's {@code ServerUri} and {@code
   * EndpointUrl}.
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
        productionSelectorFactory = (discovery, endpoint) -> productionSelector(discovery);
    private Function<ReverseConnectCandidateSnapshot, String> keyFunction =
        ReverseConnectAcceptor::defaultKey;
    private ReverseConnectAcceptorClientListener clientListener = (d, e, c) -> {};
    private ReverseConnectAcceptorErrorListener errorListener = (c, e) -> {};
    private Consumer<OpcTcpClientTransportConfigBuilder> discoveryTransportCustomizer = b -> {};
    private Consumer<OpcTcpClientTransportConfigBuilder> productionTransportCustomizer = b -> {};

    private Builder(ReverseConnectManager manager) {
      this.manager = requireNonNull(manager, "manager");
    }

    /**
     * Set the selector that chooses which pending candidates start discovery.
     *
     * <p>The selector sees only candidate metadata decoded from {@code ReverseHello}. Use it to
     * keep unwanted candidates out of the acceptor before the claimed connection is consumed for
     * discovery.
     *
     * @param discoverySelector the discovery selector.
     * @return this builder.
     */
    public Builder setDiscoverySelector(ReverseConnectSelector discoverySelector) {
      this.discoverySelector = requireNonNull(discoverySelector, "discoverySelector");
      return this;
    }

    /**
     * Set the endpoint selector applied to each discovery result.
     *
     * <p>The default selector chooses a no-security endpoint that supports anonymous Session
     * activation, preferring a URL match with the discovery {@code ReverseHello}. Applications that
     * configure certificates or non-anonymous identity providers should supply a selector that
     * matches that client configuration.
     *
     * @param endpointSelector the endpoint selector.
     * @return this builder.
     */
    public Builder setEndpointSelector(ReverseConnectEndpointSelector endpointSelector) {
      this.endpointSelector = requireNonNull(endpointSelector, "endpointSelector");
      return this;
    }

    /**
     * Set a simple client-config factory for selected endpoints.
     *
     * <p>This overload is appropriate when the config depends only on the selected endpoint. Use
     * {@link #setClientConfig(ReverseConnectClientConfigFactory)} when the config should include
     * the full discovery endpoint list or candidate metadata.
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
     * Set a discovery-aware client-config factory.
     *
     * <p>The factory runs once per accepted discovery key, after endpoint selection and before the
     * production reverse client is created. It should return a normal {@link OpcUaClientConfig} for
     * the later Session connection.
     *
     * @param clientConfigFactory the client-config factory.
     * @return this builder.
     */
    public Builder setClientConfig(ReverseConnectClientConfigFactory clientConfigFactory) {
      this.clientConfigFactory = requireNonNull(clientConfigFactory, "clientConfigFactory");
      return this;
    }

    /**
     * Set the selector factory used by production reverse clients.
     *
     * <p>The default selector matches the discovery candidate's {@code ServerUri} and {@code
     * EndpointUrl}. Override this when the server is expected to use different routing hints for
     * the later production connection.
     *
     * @param productionSelectorFactory the production selector factory.
     * @return this builder.
     */
    public Builder setProductionSelector(
        BiFunction<ReverseConnectDiscoveryResult, EndpointDescription, ReverseConnectSelector>
            productionSelectorFactory) {

      this.productionSelectorFactory =
          requireNonNull(productionSelectorFactory, "productionSelectorFactory");
      return this;
    }

    /**
     * Set the selector factory used by production reverse clients.
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
     * Set the key used to deduplicate discovery flows.
     *
     * <p>The default key is {@code ServerUri}, then endpoint URL, then candidate id. Use this
     * method when one server URI can legitimately produce independent logical clients, or when an
     * application wants to collapse several advertised endpoint URLs into one logical server.
     *
     * @param keyFunction the candidate key function.
     * @return this builder.
     */
    public Builder setKeyFunction(Function<ReverseConnectCandidateSnapshot, String> keyFunction) {
      this.keyFunction = requireNonNull(keyFunction, "keyFunction");
      return this;
    }

    /**
     * Set the callback for connected production clients.
     *
     * <p>The acceptor does not retain or disconnect clients after invoking this callback. The
     * application should store the client if it needs to issue service calls or close it later.
     *
     * @param clientListener the connected-client callback.
     * @return this builder.
     */
    public Builder setClientListener(ReverseConnectAcceptorClientListener clientListener) {
      this.clientListener = requireNonNull(clientListener, "clientListener");
      return this;
    }

    /**
     * Set the callback for failed acceptor flows.
     *
     * <p>The callback is invoked for discovery selection failures, endpoint-selection failures,
     * client-config failures, and production connection failures. If a key fails before a client is
     * delivered, the acceptor releases that key so a later candidate can retry.
     *
     * @param errorListener the error callback.
     * @return this builder.
     */
    public Builder setErrorListener(ReverseConnectAcceptorErrorListener errorListener) {
      this.errorListener = requireNonNull(errorListener, "errorListener");
      return this;
    }

    /**
     * Customize provisional discovery transports.
     *
     * <p>The customizer affects only the temporary transport used for {@code GetEndpoints}.
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
     * Customize provisional discovery transports.
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
     * Customize production reverse transports.
     *
     * <p>The customizer is applied to each production reverse client created by the acceptor. It
     * does not change the provisional discovery transport.
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
     * Customize production reverse transports.
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
     * Build the acceptor.
     *
     * @return a Reverse Connect acceptor.
     */
    public ReverseConnectAcceptor build() {
      return new ReverseConnectAcceptor(this);
    }
  }
}
