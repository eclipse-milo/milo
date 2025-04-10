/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.tcp;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

public class OpcTcpServerTransportConfigBuilder {

  private ExecutorService executor;
  private EventLoopGroup eventLoop;
  private UInteger helloDeadline = uint(10_000);
  private UInteger minimumSecureChannelLifetime = uint(60_000);
  private UInteger maximumSecureChannelLifetime = uint(60_000 * 60 * 24);
  private Consumer<ServerBootstrap> bootstrapCustomizer = b -> {};
  private Consumer<ChannelPipeline> channelPipelineCustomizer = p -> {};

  public OpcTcpServerTransportConfigBuilder setExecutor(ExecutorService executor) {
    this.executor = executor;
    return this;
  }

  public OpcTcpServerTransportConfigBuilder setEventLoop(EventLoopGroup eventLoop) {
    this.eventLoop = eventLoop;
    return this;
  }

  /**
   * Set a {@link Consumer} that will be given a chance to customize the {@link Bootstrap} used by
   * this transport.
   *
   * @param bootstrapCustomizer a {@link Consumer} that will be given a chance to customize the
   *     {@link ServerBootstrap} used by this transport.
   * @return this {@link OpcTcpServerTransportConfigBuilder}.
   */
  public OpcTcpServerTransportConfigBuilder setBootstrapCustomizer(
      Consumer<ServerBootstrap> bootstrapCustomizer) {

    this.bootstrapCustomizer = bootstrapCustomizer;
    return this;
  }

  /**
   * Set a {@link Consumer} that will be given a chance to customize the {@link ChannelPipeline}
   * used by this transport.
   *
   * @param channelPipelineCustomizer a {@link Consumer} that will be given a chance to customize
   *     the {@link ChannelPipeline} used by this transport.
   * @return this {@link OpcTcpServerTransportConfigBuilder}.
   */
  public OpcTcpServerTransportConfigBuilder setChannelPipelineCustomizer(
      Consumer<ChannelPipeline> channelPipelineCustomizer) {

    this.channelPipelineCustomizer = channelPipelineCustomizer;
    return this;
  }

  public OpcTcpServerTransportConfigBuilder setHelloDeadline(UInteger helloDeadline) {
    this.helloDeadline = helloDeadline;
    return this;
  }

  public OpcTcpServerTransportConfigBuilder setMinimumSecureChannelLifetime(
      UInteger minimumSecureChannelLifetime) {
    this.minimumSecureChannelLifetime = minimumSecureChannelLifetime;
    return this;
  }

  public OpcTcpServerTransportConfigBuilder setMaximumSecureChannelLifetime(
      UInteger maximumSecureChannelLifetime) {
    this.maximumSecureChannelLifetime = maximumSecureChannelLifetime;
    return this;
  }

  public OpcTcpServerTransportConfig build() {
    if (executor == null) {
      executor = Stack.sharedExecutor();
    }
    if (eventLoop == null) {
      eventLoop = Stack.sharedEventLoop();
    }

    return new OpcTcpServerTransportConfigImpl(
        executor,
        eventLoop,
        bootstrapCustomizer,
        channelPipelineCustomizer,
        helloDeadline,
        minimumSecureChannelLifetime,
        maximumSecureChannelLifetime);
  }

  static class OpcTcpServerTransportConfigImpl implements OpcTcpServerTransportConfig {

    private final ExecutorService executor;
    private final EventLoopGroup eventLoop;
    private final UInteger helloDeadline;
    private final UInteger minimumSecureChannelLifetime;
    private final UInteger maximumSecureChannelLifetime;
    private final Consumer<ServerBootstrap> bootstrapCustomizer;
    private final Consumer<ChannelPipeline> channelPipelineCustomizer;

    public OpcTcpServerTransportConfigImpl(
        ExecutorService executor,
        EventLoopGroup eventLoop,
        Consumer<ServerBootstrap> bootstrapCustomizer,
        Consumer<ChannelPipeline> channelPipelineCustomizer,
        UInteger helloDeadline,
        UInteger minimumSecureChannelLifetime,
        UInteger maximumSecureChannelLifetime) {

      this.executor = executor;
      this.eventLoop = eventLoop;
      this.bootstrapCustomizer = bootstrapCustomizer;
      this.channelPipelineCustomizer = channelPipelineCustomizer;
      this.helloDeadline = helloDeadline;
      this.minimumSecureChannelLifetime = minimumSecureChannelLifetime;
      this.maximumSecureChannelLifetime = maximumSecureChannelLifetime;
    }

    @Override
    public ExecutorService getExecutor() {
      return executor;
    }

    @Override
    public EventLoopGroup getEventLoop() {
      return eventLoop;
    }

    @Override
    public Consumer<ServerBootstrap> getBootstrapCustomizer() {
      return bootstrapCustomizer;
    }

    @Override
    public Consumer<ChannelPipeline> getChannelPipelineCustomizer() {
      return channelPipelineCustomizer;
    }

    @Override
    public UInteger getHelloDeadline() {
      return helloDeadline;
    }

    @Override
    public UInteger getMinimumSecureChannelLifetime() {
      return minimumSecureChannelLifetime;
    }

    @Override
    public UInteger getMaximumSecureChannelLifetime() {
      return maximumSecureChannelLifetime;
    }
  }
}
