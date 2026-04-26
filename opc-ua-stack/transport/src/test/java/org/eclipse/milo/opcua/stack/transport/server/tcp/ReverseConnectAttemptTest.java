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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.netty.channel.embedded.EmbeddedChannel;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.stack.transport.server.uasc.SecureChannelOpenedEvent;
import org.junit.jupiter.api.Test;

class ReverseConnectAttemptTest {

  @Test
  void fastSecureChannelOpenedEventIsObservedAfterTcpConnectCompletes() throws Exception {
    var outcomes = new LinkedBlockingQueue<ReverseConnectAttempt.Outcome>();
    var attempt = new ReverseConnectAttempt(outcomes::offer);
    var channel = new EmbeddedChannel(new ReverseConnectAttempt.Observer(attempt));

    channel.pipeline().fireUserEventTriggered(new SecureChannelOpenedEvent(123L));
    assertTrue(outcomes.isEmpty(), "secure-channel-open outcome waits for TCP connect success");

    attempt.tcpConnectSucceeded(channel);

    assertSame(channel, attempt.connectedFuture().get(5, TimeUnit.SECONDS));

    var outcome =
        assertInstanceOf(
            ReverseConnectAttempt.SecureChannelOpened.class, outcomes.poll(5, TimeUnit.SECONDS));
    assertSame(channel, outcome.channel());
    assertEquals(123L, outcome.secureChannelId());

    channel.finishAndReleaseAll();
  }

  @Test
  void closeBeforeSecureChannelReportsCloseBeforeSecureChannel() throws Exception {
    var outcomes = new LinkedBlockingQueue<ReverseConnectAttempt.Outcome>();
    var attempt = new ReverseConnectAttempt(outcomes::offer);
    var channel = new EmbeddedChannel(new ReverseConnectAttempt.Observer(attempt));

    attempt.tcpConnectSucceeded(channel);
    channel.close();

    var outcome =
        assertInstanceOf(
            ReverseConnectAttempt.CloseBeforeSecureChannel.class,
            attempt.terminalOutcomeFuture().get(5, TimeUnit.SECONDS));
    assertSame(channel, outcome.channel());
    assertSame(outcome, outcomes.poll(5, TimeUnit.SECONDS));

    channel.finishAndReleaseAll();
  }

  @Test
  void closeAfterSecureChannelReportsCloseAfterSecureChannel() throws Exception {
    var outcomes = new LinkedBlockingQueue<ReverseConnectAttempt.Outcome>();
    var attempt = new ReverseConnectAttempt(outcomes::offer);
    var channel = new EmbeddedChannel(new ReverseConnectAttempt.Observer(attempt));

    attempt.tcpConnectSucceeded(channel);
    channel.pipeline().fireUserEventTriggered(new SecureChannelOpenedEvent(456L));
    assertInstanceOf(
        ReverseConnectAttempt.SecureChannelOpened.class, outcomes.poll(5, TimeUnit.SECONDS));

    channel.close();

    var outcome =
        assertInstanceOf(
            ReverseConnectAttempt.CloseAfterSecureChannel.class,
            attempt.terminalOutcomeFuture().get(5, TimeUnit.SECONDS));
    assertSame(channel, outcome.channel());
    assertSame(outcome, outcomes.poll(5, TimeUnit.SECONDS));

    channel.finishAndReleaseAll();
  }
}
