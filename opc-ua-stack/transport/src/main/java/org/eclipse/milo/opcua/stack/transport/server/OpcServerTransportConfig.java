/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

public interface OpcServerTransportConfig {

  /**
   * Get the {@link ExecutorService} to be used by this transport.
   *
   * @return the {@link ExecutorService} to be used by this transport.
   */
  ExecutorService getExecutor();

  /**
   * Get the {@link EventLoopGroup} to be used by this transport.
   *
   * @return the {@link EventLoopGroup} to be used by this transport.
   */
  EventLoopGroup getEventLoop();

  /**
   * Get a {@link Consumer} that will be given a chance to customize the {@link ServerBootstrap}
   * used by this transport.
   *
   * @return a {@link Consumer} that will be given a chance to customize the {@link ServerBootstrap}
   *     used by this transport.
   */
  Consumer<ServerBootstrap> getBootstrapCustomizer();

  /**
   * Get a {@link Consumer} that will be given a chance to customize the {@link ChannelPipeline}
   * used by this transport.
   *
   * @return a {@link Consumer} that will be given a chance to customize the {@link ChannelPipeline}
   *     used by this transport.
   */
  Consumer<ChannelPipeline> getChannelPipelineCustomizer();
}
