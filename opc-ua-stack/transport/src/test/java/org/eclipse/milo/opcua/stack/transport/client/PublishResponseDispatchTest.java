/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import io.netty.channel.Channel;
import io.netty.channel.embedded.EmbeddedChannel;
import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.stack.core.types.UaResponseMessageType;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.PublishResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.util.Unit;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.junit.jupiter.api.Test;

class PublishResponseDispatchTest {

  // Once the pending request is removed, only response dispatch owns completion of its future.
  @Test
  void rejectedPublishResponseDispatchStillCompletesPendingRequest() throws Exception {
    try (Fixture fixture = new Fixture()) {
      CompletableFuture<UaResponseMessageType> pending = fixture.sendPublish();
      fixture.executor.reject = true;
      PublishResponse response = response();
      fixture.transport.handleResponse(1, response);
      fixture.transport.handleChannelInactive(fixture.channel);
      assertSame(response, pending.get(5, TimeUnit.SECONDS));
    }
  }

  // Rejection during a queue continuation must not lose this response or block later responses.
  @Test
  void queuedPublishResponsesAndLaterResponsesSurviveExecutorRejection() throws Exception {
    try (Fixture fixture = new Fixture()) {
      CompletableFuture<UaResponseMessageType> first = fixture.sendPublish();
      CompletableFuture<UaResponseMessageType> second = fixture.sendPublish();
      CompletableFuture<UaResponseMessageType> third = fixture.sendPublish();
      PublishResponse response = response();
      fixture.transport.handleResponse(1, response);
      fixture.transport.handleResponse(2, response);
      fixture.transport.handleResponse(3, response);
      assertEquals(1, fixture.executor.tasks.size());
      fixture.executor.reject = true;
      fixture.executor.tasks.remove().run();
      assertSame(response, first.get(5, TimeUnit.SECONDS));
      assertSame(response, second.get(5, TimeUnit.SECONDS));
      assertSame(response, third.get(5, TimeUnit.SECONDS));
      CompletableFuture<UaResponseMessageType> later = fixture.sendPublish();
      fixture.transport.handleResponse(4, response);
      assertSame(response, later.get(5, TimeUnit.SECONDS));
    }
  }

  private static PublishResponse response() {
    return new PublishResponse(null, uint(1), null, false, null, null, null);
  }

  private static final class Fixture implements AutoCloseable {
    final ControlledExecutor executor = new ControlledExecutor();
    final EmbeddedChannel channel = new EmbeddedChannel();
    final Transport transport =
        new Transport(
            OpcTcpClientTransportConfig.newBuilder().setExecutor(executor).build(), channel);

    CompletableFuture<UaResponseMessageType> sendPublish() {
      var header =
          new RequestHeader(
              NodeId.NULL_VALUE, DateTime.now(), uint(0), uint(0), null, uint(0), null);
      return transport.sendRequestMessage(new PublishRequest(header, null));
    }

    @Override
    public void close() {
      channel.finishAndReleaseAll();
      executor.shutdown();
    }
  }

  private static final class Transport extends AbstractUascClientTransport {
    private final Channel channel;

    Transport(OpcClientTransportConfig config, Channel channel) {
      super(config);
      this.channel = channel;
    }

    @Override
    protected CompletableFuture<Channel> getChannel() {
      return CompletableFuture.completedFuture(channel);
    }

    @Override
    public OpcClientTransportConfig getConfig() {
      return config;
    }

    @Override
    public CompletableFuture<Unit> connect(ClientApplicationContext context) {
      return CompletableFuture.completedFuture(Unit.VALUE);
    }

    @Override
    public CompletableFuture<Unit> disconnect() {
      return CompletableFuture.completedFuture(Unit.VALUE);
    }
  }

  private static final class ControlledExecutor extends AbstractExecutorService {
    final Queue<Runnable> tasks = new ArrayDeque<>();
    boolean reject;

    @Override
    public void execute(Runnable task) {
      if (reject) {
        throw new RejectedExecutionException("saturated");
      }
      tasks.add(task);
    }

    @Override
    public void shutdown() {
      reject = true;
    }

    @Override
    public List<Runnable> shutdownNow() {
      shutdown();
      return List.copyOf(tasks);
    }

    @Override
    public boolean isShutdown() {
      return reject;
    }

    @Override
    public boolean isTerminated() {
      return reject;
    }

    @Override
    public boolean awaitTermination(long timeout, TimeUnit unit) {
      return reject;
    }
  }
}
