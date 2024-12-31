/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.tcp;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import io.netty.channel.EventLoopGroup;
import java.util.concurrent.ExecutorService;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;

public class OpcTcpServerTransportConfigBuilder {

  private ExecutorService executor;
  private EventLoopGroup eventLoop;
  private UInteger helloDeadline = uint(10_000);
  private UInteger minimumSecureChannelLifetime = uint(60_000);
  private UInteger maximumSecureChannelLifetime = uint(60_000 * 60 * 24);

  public OpcTcpServerTransportConfigBuilder setExecutor(ExecutorService executor) {
    this.executor = executor;
    return this;
  }

  public OpcTcpServerTransportConfigBuilder setEventLoop(EventLoopGroup eventLoop) {
    this.eventLoop = eventLoop;
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

    public OpcTcpServerTransportConfigImpl(
        ExecutorService executor,
        EventLoopGroup eventLoop,
        UInteger helloDeadline,
        UInteger minimumSecureChannelLifetime,
        UInteger maximumSecureChannelLifetime) {

      this.executor = executor;
      this.eventLoop = eventLoop;
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
