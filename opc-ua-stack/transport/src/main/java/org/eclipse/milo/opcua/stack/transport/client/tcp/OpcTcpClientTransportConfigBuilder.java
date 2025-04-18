/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client.tcp;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.util.HashedWheelTimer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

public class OpcTcpClientTransportConfigBuilder {

  private UInteger connectTimeout = uint(5_000);
  private UInteger acknowledgeTimeout = uint(5_000);
  private UInteger channelLifetime = uint(60 * 60 * 1000);

  private ExecutorService executor;
  private ScheduledExecutorService scheduledExecutor;
  private EventLoopGroup eventLoop;
  private HashedWheelTimer wheelTimer;
  private Consumer<Bootstrap> bootstrapCustomizer = b -> {};
  private Consumer<ChannelPipeline> channelPipelineCustomizer = p -> {};

  public OpcTcpClientTransportConfigBuilder setConnectTimeout(UInteger connectTimeout) {
    this.connectTimeout = connectTimeout;
    return this;
  }

  public OpcTcpClientTransportConfigBuilder setAcknowledgeTimeout(UInteger acknowledgeTimeout) {
    this.acknowledgeTimeout = acknowledgeTimeout;
    return this;
  }

  public OpcTcpClientTransportConfigBuilder setChannelLifetime(UInteger channelLifetime) {
    this.channelLifetime = channelLifetime;
    return this;
  }

  public OpcTcpClientTransportConfigBuilder setExecutor(ExecutorService executor) {
    this.executor = executor;
    return this;
  }

  public OpcTcpClientTransportConfigBuilder setScheduledExecutor(
      ScheduledExecutorService scheduledExecutor) {
    this.scheduledExecutor = scheduledExecutor;
    return this;
  }

  public OpcTcpClientTransportConfigBuilder setEventLoop(EventLoopGroup eventLoop) {
    this.eventLoop = eventLoop;
    return this;
  }

  public OpcTcpClientTransportConfigBuilder setWheelTimer(HashedWheelTimer wheelTimer) {
    this.wheelTimer = wheelTimer;
    return this;
  }

  /**
   * Set a {@link Consumer} that will be given a chance to customize the {@link Bootstrap} used by
   * this transport.
   *
   * @param bootstrapCustomizer a {@link Consumer} that will be given a chance to customize the
   *     {@link Bootstrap} used by this transport.
   * @return this {@link OpcTcpClientTransportConfigBuilder}.
   */
  public OpcTcpClientTransportConfigBuilder setBootstrapCustomizer(
      Consumer<Bootstrap> bootstrapCustomizer) {

    this.bootstrapCustomizer = bootstrapCustomizer;
    return this;
  }

  /**
   * Set a {@link Consumer} that will be given a chance to customize the {@link ChannelPipeline}
   * used by this transport.
   *
   * @param channelPipelineCustomizer a {@link Consumer} that will be given a chance to customize
   *     the {@link ChannelPipeline} used by this transport.
   * @return this {@link OpcTcpClientTransportConfigBuilder}.
   */
  public OpcTcpClientTransportConfigBuilder setChannelPipelineCustomizer(
      Consumer<ChannelPipeline> channelPipelineCustomizer) {

    this.channelPipelineCustomizer = channelPipelineCustomizer;
    return this;
  }

  public OpcTcpClientTransportConfig build() {
    if (executor == null) {
      executor = Stack.sharedExecutor();
    }
    if (scheduledExecutor == null) {
      scheduledExecutor = Stack.sharedScheduledExecutor();
    }
    if (eventLoop == null) {
      eventLoop = Stack.sharedEventLoop();
    }
    if (wheelTimer == null) {
      wheelTimer = Stack.sharedWheelTimer();
    }

    return new OpcTcpClientTransportConfigImpl(
        connectTimeout,
        acknowledgeTimeout,
        channelLifetime,
        executor,
        scheduledExecutor,
        eventLoop,
        wheelTimer,
        bootstrapCustomizer,
        channelPipelineCustomizer);
  }

  static class OpcTcpClientTransportConfigImpl implements OpcTcpClientTransportConfig {

    private final UInteger connectTimeout;
    private final UInteger acknowledgeTimeout;
    private final UInteger channelLifetime;
    private final ExecutorService executor;
    private final ScheduledExecutorService scheduledExecutor;
    private final EventLoopGroup eventLoop;
    private final HashedWheelTimer wheelTimer;
    private final Consumer<Bootstrap> bootstrapCustomizer;
    private final Consumer<ChannelPipeline> channelPipelineCustomizer;

    public OpcTcpClientTransportConfigImpl(
        UInteger connectTimeout,
        UInteger acknowledgeTimeout,
        UInteger channelLifetime,
        ExecutorService executor,
        ScheduledExecutorService scheduledExecutor,
        EventLoopGroup eventLoop,
        HashedWheelTimer wheelTimer,
        Consumer<Bootstrap> bootstrapCustomizer,
        Consumer<ChannelPipeline> channelPipelineCustomizer) {

      this.connectTimeout = connectTimeout;
      this.acknowledgeTimeout = acknowledgeTimeout;
      this.channelLifetime = channelLifetime;
      this.executor = executor;
      this.scheduledExecutor = scheduledExecutor;
      this.eventLoop = eventLoop;
      this.wheelTimer = wheelTimer;
      this.bootstrapCustomizer = bootstrapCustomizer;
      this.channelPipelineCustomizer = channelPipelineCustomizer;
    }

    @Override
    public UInteger getConnectTimeout() {
      return connectTimeout;
    }

    @Override
    public UInteger getAcknowledgeTimeout() {
      return acknowledgeTimeout;
    }

    @Override
    public UInteger getChannelLifetime() {
      return channelLifetime;
    }

    @Override
    public ExecutorService getExecutor() {
      return executor;
    }

    @Override
    public ScheduledExecutorService getScheduledExecutor() {
      return scheduledExecutor;
    }

    @Override
    public EventLoopGroup getEventLoop() {
      return eventLoop;
    }

    @Override
    public HashedWheelTimer getWheelTimer() {
      return wheelTimer;
    }

    @Override
    public Consumer<Bootstrap> getBootstrapCustomizer() {
      return bootstrapCustomizer;
    }

    @Override
    public Consumer<ChannelPipeline> getChannelPipelineCustomizer() {
      return channelPipelineCustomizer;
    }
  }
}
