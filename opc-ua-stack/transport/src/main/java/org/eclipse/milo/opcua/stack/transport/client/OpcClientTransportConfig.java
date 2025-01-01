/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.util.HashedWheelTimer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.Consumer;

public interface OpcClientTransportConfig {

  /**
   * Get the {@link ExecutorService} to be used by this transport.
   *
   * @return the {@link ExecutorService} to be used by this transport.
   */
  ExecutorService getExecutor();

  /**
   * Get the {@link ScheduledExecutorService} to be used by this transport.
   *
   * @return the {@link ScheduledExecutorService} to be used by this transport.
   */
  ScheduledExecutorService getScheduledExecutor();

  /**
   * Get the {@link EventLoopGroup} to be used by this transport.
   *
   * @return the {@link EventLoopGroup} to be used by this transport.
   */
  EventLoopGroup getEventLoop();

  /**
   * Get the {@link HashedWheelTimer} to be used by this transport.
   *
   * @return the {@link HashedWheelTimer} to be used by this transport.
   */
  HashedWheelTimer getWheelTimer();

  /**
   * Get a {@link Consumer} that will be given a chance to customize the {@link Bootstrap} used by
   * this transport.
   *
   * @return a {@link Consumer} that will be given a chance to customize the {@link Bootstrap} used
   *     by this transport.
   */
  Consumer<Bootstrap> getBootstrapCustomizer();

  /**
   * Get a {@link Consumer} that will be given a chance to customize the {@link ChannelPipeline}
   * used by this transport.
   *
   * @return a {@link Consumer} that will be given a chance to customize the {@link ChannelPipeline}
   *     used by this transport.
   */
  Consumer<ChannelPipeline> getChannelPipelineCustomizer();
}
