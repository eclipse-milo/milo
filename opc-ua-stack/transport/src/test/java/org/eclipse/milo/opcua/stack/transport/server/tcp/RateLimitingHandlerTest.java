/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.stack.transport.server.tcp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Instances are built directly rather than through {@link RateLimitingHandler#getInstance()}: the
 * shared instance latches {@code Stack.ConnectionLimits} the first time it is asked for, and any
 * test that binds a server has already asked for it, which would make these tests order-dependent.
 */
class RateLimitingHandlerTest {

  private static RateLimitingHandler handler(
      int maxAttempts, int windowMs, int maxConnections, int maxConnectionsPerAddress)
      throws Exception {

    Constructor<RateLimitingHandler> ctor =
        RateLimitingHandler.class.getDeclaredConstructor(
            boolean.class, int.class, int.class, int.class, int.class);
    ctor.setAccessible(true);

    return ctor.newInstance(true, maxAttempts, windowMs, maxConnections, maxConnectionsPerAddress);
  }

  /** Stands in for the pipeline: Netty calls channelAccepted() only when accept() returned true. */
  private static boolean connect(
      RateLimitingHandler handler, ChannelHandlerContext ctx, String address, int port) {

    var isa = new InetSocketAddress(address, port);
    boolean accepted = handler.accept(ctx, isa);

    if (accepted) {
      handler.channelAccepted(ctx, isa);
    }

    return accepted;
  }

  private static EmbeddedChannel channel() {
    return new EmbeddedChannel(new ChannelInboundHandlerAdapter());
  }

  private static int totalConnections(RateLimitingHandler handler) throws Exception {
    Field field = RateLimitingHandler.class.getDeclaredField("totalConnections");
    field.setAccessible(true);

    return ((java.util.concurrent.atomic.AtomicInteger) field.get(handler)).get();
  }

  private static int multisetSize(RateLimitingHandler handler) throws Exception {
    Field field = RateLimitingHandler.class.getDeclaredField("connections");
    field.setAccessible(true);

    return ((com.google.common.collect.Multiset<?>) field.get(handler)).size();
  }

  @SuppressWarnings("unchecked")
  private static Map<InetAddress, ?> timestamps(RateLimitingHandler handler) throws Exception {
    Field field = RateLimitingHandler.class.getDeclaredField("timestamps");
    field.setAccessible(true);

    return (Map<InetAddress, ?>) field.get(handler);
  }

  @Test
  void maxConnectionsAppliesToEveryAddress() throws Exception {
    RateLimitingHandler handler = handler(4, 60_000, 10, 100);
    ChannelHandlerContext ctx = channel().pipeline().firstContext();

    // Each address stays within its allowed attempts, so nothing here is rate limited; only the
    // total connection count can reject.
    int accepted = 0;
    for (int address = 1; address <= 20; address++) {
      for (int i = 0; i < 4; i++) {
        if (connect(handler, ctx, "198.51.100." + address, 40000 + i)) {
          accepted++;
        }
      }
    }

    assertEquals(10, accepted, "connections accepted beyond maxConnections");
  }

  @Test
  void maxConnectionsPerAddressAppliesFromTheFirstConnection() throws Exception {
    RateLimitingHandler handler = handler(4, 60_000, 100, 2);
    ChannelHandlerContext ctx = channel().pipeline().firstContext();

    int accepted = 0;
    for (int i = 0; i < 4; i++) {
      if (connect(handler, ctx, "198.51.100.1", 40000 + i)) {
        accepted++;
      }
    }

    assertEquals(2, accepted, "connections accepted beyond maxConnectionsPerAddress");
  }

  @Test
  void aBurstFromOneAddressIsRateLimited() throws Exception {
    // Connection limits set out of reach, so only the rate limit can reject.
    RateLimitingHandler handler = handler(4, 60_000, 1_000_000, 1_000_000);
    ChannelHandlerContext ctx = channel().pipeline().firstContext();

    var results = new StringBuilder();
    for (int i = 0; i < 8; i++) {
      results.append(connect(handler, ctx, "198.51.100.1", 40000 + i) ? 'A' : 'r');
    }

    assertEquals("AAAArrrr", results.toString());
  }

  @Test
  void loopbackIsExempt() throws Exception {
    RateLimitingHandler handler = handler(1, 60_000, 0, 0);
    ChannelHandlerContext ctx = channel().pipeline().firstContext();

    for (int i = 0; i < 8; i++) {
      assertTrue(connect(handler, ctx, "127.0.0.1", 40000 + i), "loopback was rejected");
    }
  }

  @Test
  void aRejectedConnectionRecordsNothing() throws Exception {
    // maxConnections of 0 rejects every address on its first attempt, before timestamps is
    // touched, so there is nothing for the cleanup task to remove later.
    RateLimitingHandler handler = handler(4, 1000, 0, 100);
    ChannelHandlerContext ctx = channel().pipeline().firstContext();

    for (int address = 1; address <= 5; address++) {
      assertFalse(connect(handler, ctx, "198.51.100." + address, 40000), "connection was accepted");
    }

    assertEquals(0, timestamps(handler).size(), "timestamps recorded for a rejected connection");
  }

  @Test
  void closingAConnectionFreesItsSlot() throws Exception {
    RateLimitingHandler handler = handler(4, 60_000, 2, 100);

    EmbeddedChannel first = channel();
    EmbeddedChannel second = channel();

    assertTrue(connect(handler, first.pipeline().firstContext(), "198.51.100.1", 40000));
    assertTrue(connect(handler, second.pipeline().firstContext(), "198.51.100.2", 40000));
    assertEquals(2, totalConnections(handler));
    assertEquals(multisetSize(handler), totalConnections(handler));

    // the cap is now full
    assertFalse(connect(handler, channel().pipeline().firstContext(), "198.51.100.3", 40000));

    first.close();
    first.runPendingTasks();

    assertEquals(1, totalConnections(handler), "closing a connection did not free its slot");
    assertEquals(multisetSize(handler), totalConnections(handler), "counters drifted apart");

    // and the freed slot is usable again
    assertTrue(connect(handler, channel().pipeline().firstContext(), "198.51.100.3", 40000));
    assertEquals(2, totalConnections(handler));
    assertEquals(multisetSize(handler), totalConnections(handler));
  }

  @Test
  void totalMatchesTheMultisetAcrossOpensAndCloses() throws Exception {
    // Several connections per address, closed out of order, so that both the add/increment and the
    // remove/decrement paths run many times and interleave.
    RateLimitingHandler handler = handler(1000, 600_000, 1_000_000, 1_000_000);

    var open = new java.util.ArrayList<EmbeddedChannel>();
    var random = new java.util.Random(1);

    for (int step = 0; step < 2_000; step++) {
      if (open.isEmpty() || random.nextInt(100) < 60) {
        EmbeddedChannel channel = channel();
        String address = "198.51.100." + (1 + random.nextInt(8));
        assertTrue(connect(handler, channel.pipeline().firstContext(), address, 40000 + step));
        open.add(channel);
      } else {
        EmbeddedChannel channel = open.remove(random.nextInt(open.size()));
        channel.close();
        channel.runPendingTasks();
      }

      assertEquals(
          open.size(), totalConnections(handler), "total diverged from the connections held open");
      assertEquals(
          multisetSize(handler), totalConnections(handler), "total diverged from the multiset");
    }

    while (!open.isEmpty()) {
      EmbeddedChannel channel = open.remove(open.size() - 1);
      channel.close();
      channel.runPendingTasks();
    }

    assertEquals(0, totalConnections(handler), "total did not return to zero");
    assertEquals(0, multisetSize(handler));
  }

  @Test
  void acceptReservesTheSlotItChecked() throws Exception {
    // The reservation happens while accept() still holds the lock, so a second caller sees the
    // first one's connection even though channelAccepted() has not run for it yet.
    RateLimitingHandler handler = handler(4, 60_000, 1, 100);
    ChannelHandlerContext ctx = channel().pipeline().firstContext();

    assertTrue(handler.accept(ctx, new InetSocketAddress("198.51.100.1", 40000)));
    assertEquals(1, totalConnections(handler), "accept() returned true without reserving a slot");

    assertFalse(
        handler.accept(ctx, new InetSocketAddress("198.51.100.2", 40000)),
        "the reserved slot was not visible to the next caller");
  }
}
