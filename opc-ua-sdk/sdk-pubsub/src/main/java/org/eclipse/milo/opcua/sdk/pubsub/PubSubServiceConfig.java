/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub;

import io.netty.channel.EventLoopGroup;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportProvider;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.MessageMappingProvider;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.jspecify.annotations.Nullable;

/**
 * Environment configuration for a {@link PubSubService}: executors, event loops, encoding context,
 * and extension providers.
 *
 * <p>Environment concerns are deliberately separate from {@link PubSubBindings}: a {@link
 * PubSubServiceConfig} describes the runtime environment and is reusable across config graphs,
 * while bindings are specific to the names in one {@code PubSubConfig}.
 */
public final class PubSubServiceConfig {

  private final EncodingContext encodingContext;
  private final ScheduledExecutorService scheduledExecutor;
  private final ExecutorService transportExecutor;
  private final EventLoopGroup eventLoopGroup;
  private final boolean eventLoopPerConnection;
  private final List<TransportProvider> transportProviders;
  private final List<MessageMappingProvider> messageMappingProviders;

  private PubSubServiceConfig(
      EncodingContext encodingContext,
      ScheduledExecutorService scheduledExecutor,
      ExecutorService transportExecutor,
      EventLoopGroup eventLoopGroup,
      boolean eventLoopPerConnection,
      List<TransportProvider> transportProviders,
      List<MessageMappingProvider> messageMappingProviders) {

    this.encodingContext = encodingContext;
    this.scheduledExecutor = scheduledExecutor;
    this.transportExecutor = transportExecutor;
    this.eventLoopGroup = eventLoopGroup;
    this.eventLoopPerConnection = eventLoopPerConnection;
    this.transportProviders = List.copyOf(transportProviders);
    this.messageMappingProviders = List.copyOf(messageMappingProviders);
  }

  /**
   * Get the {@link EncodingContext} used to encode and decode field values and ExtensionObject
   * escape hatches.
   *
   * @return the {@link EncodingContext}.
   */
  public EncodingContext getEncodingContext() {
    return encodingContext;
  }

  /**
   * Get the executor on which publish-cycle and other timed deadlines are scheduled.
   *
   * @return the scheduled executor.
   */
  public ScheduledExecutorService getScheduledExecutor() {
    return scheduledExecutor;
  }

  /**
   * Get the executor on which received messages are decoded and listeners are notified.
   *
   * @return the transport executor.
   */
  public ExecutorService getTransportExecutor() {
    return transportExecutor;
  }

  /**
   * Get the Netty {@link EventLoopGroup} used for transport I/O.
   *
   * <p>Ignored for connections that get their own event loop when {@link
   * #isEventLoopPerConnection()} is enabled.
   *
   * @return the transport {@link EventLoopGroup}.
   */
  public EventLoopGroup getEventLoopGroup() {
    return eventLoopGroup;
  }

  /**
   * Get whether each connection gets its own single-threaded event loop, isolating fast connections
   * from slow ones. Per-connection event loops are shut down with the service.
   *
   * @return {@code true} if each connection gets its own event loop.
   */
  public boolean isEventLoopPerConnection() {
    return eventLoopPerConnection;
  }

  /**
   * Get the additional {@link TransportProvider}s available to the service, e.g. from
   * sdk-pubsub-mqtt. Built-in providers are always available and are not listed here.
   *
   * @return the additional {@link TransportProvider}s; possibly empty.
   */
  public List<TransportProvider> getTransportProviders() {
    return transportProviders;
  }

  /**
   * Get the additional {@link MessageMappingProvider}s available to the service. Built-in providers
   * are always available and are not listed here.
   *
   * @return the additional {@link MessageMappingProvider}s; possibly empty.
   */
  public List<MessageMappingProvider> getMessageMappingProviders() {
    return messageMappingProviders;
  }

  /**
   * Create a new {@link Builder} initialized with the values of this config.
   *
   * @return a new {@link Builder} initialized with the values of this config.
   */
  public Builder toBuilder() {
    Builder builder = new Builder();
    builder.encodingContext = encodingContext;
    builder.scheduledExecutor = scheduledExecutor;
    builder.transportExecutor = transportExecutor;
    builder.eventLoopGroup = eventLoopGroup;
    builder.eventLoopPerConnection = eventLoopPerConnection;
    builder.transportProviders.addAll(transportProviders);
    builder.messageMappingProviders.addAll(messageMappingProviders);
    return builder;
  }

  @Override
  public boolean equals(@Nullable Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof PubSubServiceConfig that)) {
      return false;
    }
    return encodingContext.equals(that.encodingContext)
        && scheduledExecutor.equals(that.scheduledExecutor)
        && transportExecutor.equals(that.transportExecutor)
        && eventLoopGroup.equals(that.eventLoopGroup)
        && eventLoopPerConnection == that.eventLoopPerConnection
        && transportProviders.equals(that.transportProviders)
        && messageMappingProviders.equals(that.messageMappingProviders);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        encodingContext,
        scheduledExecutor,
        transportExecutor,
        eventLoopGroup,
        eventLoopPerConnection,
        transportProviders,
        messageMappingProviders);
  }

  /**
   * Create a new {@link Builder}.
   *
   * @return a new {@link Builder}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Create a {@link PubSubServiceConfig} with default values.
   *
   * @return a {@link PubSubServiceConfig} with default values.
   */
  public static PubSubServiceConfig defaults() {
    return builder().build();
  }

  /** A builder of {@link PubSubServiceConfig} instances. */
  public static final class Builder {

    private @Nullable EncodingContext encodingContext;
    private @Nullable ScheduledExecutorService scheduledExecutor;
    private @Nullable ExecutorService transportExecutor;
    private @Nullable EventLoopGroup eventLoopGroup;
    private boolean eventLoopPerConnection = false;
    private final List<TransportProvider> transportProviders = new ArrayList<>();
    private final List<MessageMappingProvider> messageMappingProviders = new ArrayList<>();

    private Builder() {}

    /**
     * Set the {@link EncodingContext} used to encode and decode field values and ExtensionObject
     * escape hatches.
     *
     * <p>Defaults to a fresh {@link DefaultEncodingContext} instance.
     *
     * @param encodingContext the {@link EncodingContext}.
     * @return this {@link Builder}.
     */
    public Builder encodingContext(EncodingContext encodingContext) {
      this.encodingContext = encodingContext;
      return this;
    }

    /**
     * Set the executor on which publish-cycle and other timed deadlines are scheduled.
     *
     * <p>Defaults to {@link Stack#sharedScheduledExecutor()}.
     *
     * @param executor the scheduled executor.
     * @return this {@link Builder}.
     */
    public Builder scheduledExecutor(ScheduledExecutorService executor) {
      this.scheduledExecutor = executor;
      return this;
    }

    /**
     * Set the executor on which received messages are decoded and listeners are notified.
     *
     * <p>Defaults to {@link Stack#sharedExecutor()}.
     *
     * @param executor the transport executor.
     * @return this {@link Builder}.
     */
    public Builder transportExecutor(ExecutorService executor) {
      this.transportExecutor = executor;
      return this;
    }

    /**
     * Set the Netty {@link EventLoopGroup} used for transport I/O.
     *
     * <p>Defaults to {@link Stack#sharedEventLoop()}.
     *
     * @param eventLoopGroup the transport {@link EventLoopGroup}.
     * @return this {@link Builder}.
     */
    public Builder eventLoopGroup(EventLoopGroup eventLoopGroup) {
      this.eventLoopGroup = eventLoopGroup;
      return this;
    }

    /**
     * Set whether each connection gets its own single-threaded event loop, isolating fast
     * connections from slow ones; off by default. Hard real-time and TSN remain out of scope.
     *
     * @param enabled {@code true} to give each connection its own event loop.
     * @return this {@link Builder}.
     */
    public Builder eventLoopPerConnection(boolean enabled) {
      this.eventLoopPerConnection = enabled;
      return this;
    }

    /**
     * Add an additional {@link TransportProvider}, e.g. from sdk-pubsub-mqtt. May be called
     * multiple times; built-in providers are always available.
     *
     * @param provider the {@link TransportProvider} to add.
     * @return this {@link Builder}.
     */
    public Builder transportProvider(TransportProvider provider) {
      this.transportProviders.add(provider);
      return this;
    }

    /**
     * Add an additional {@link MessageMappingProvider}. May be called multiple times; built-in
     * providers are always available.
     *
     * <p>Providers are resolved by {@link MessageMappingProvider#mappingName()}. Both built-in
     * mappings — {@code "uadp"} and {@code "json"} (see {@link
     * org.eclipse.milo.opcua.sdk.pubsub.json.JsonMessageMappingProvider}) — are always available;
     * registering a provider for them is never required. A registered provider whose name matches
     * either built-in mapping shadows it on the data plane. Configurations referencing a mapping
     * name with no provider (i.e. a custom name with no registered provider) fail startup with
     * {@code Bad_ConfigurationError}.
     *
     * @param provider the {@link MessageMappingProvider} to add.
     * @return this {@link Builder}.
     */
    public Builder messageMappingProvider(MessageMappingProvider provider) {
      this.messageMappingProviders.add(provider);
      return this;
    }

    /**
     * Build a {@link PubSubServiceConfig} from the values configured on this {@link Builder},
     * applying defaults for any values not configured.
     *
     * @return a new {@link PubSubServiceConfig}.
     */
    public PubSubServiceConfig build() {
      EncodingContext encodingContext = this.encodingContext;
      if (encodingContext == null) {
        encodingContext = new DefaultEncodingContext();
      }

      ScheduledExecutorService scheduledExecutor = this.scheduledExecutor;
      if (scheduledExecutor == null) {
        scheduledExecutor = Stack.sharedScheduledExecutor();
      }

      ExecutorService transportExecutor = this.transportExecutor;
      if (transportExecutor == null) {
        transportExecutor = Stack.sharedExecutor();
      }

      EventLoopGroup eventLoopGroup = this.eventLoopGroup;
      if (eventLoopGroup == null) {
        eventLoopGroup = Stack.sharedEventLoop();
      }

      return new PubSubServiceConfig(
          encodingContext,
          scheduledExecutor,
          transportExecutor,
          eventLoopGroup,
          eventLoopPerConnection,
          transportProviders,
          messageMappingProviders);
    }
  }
}
