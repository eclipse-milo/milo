/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.transport.TransportProfile;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.UserTokenPolicy;
import org.junit.jupiter.api.Test;

/** Tests session lifecycle behavior that is local to {@link Session}. */
class SessionTest {

  @Test
  void closeNotifiesLifecycleListenersOnce() {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    ScheduledExecutorService scheduledExecutor = mock(ScheduledExecutorService.class);
    ScheduledFuture<?> timeoutFuture = mock(ScheduledFuture.class);

    OpcUaServerConfig config = mock(OpcUaServerConfig.class);
    when(config.getExecutor()).thenReturn(executor);

    OpcUaServer server = mock(OpcUaServer.class);
    when(server.getConfig()).thenReturn(config);
    when(server.getScheduledExecutorService()).thenReturn(scheduledExecutor);
    doReturn(timeoutFuture)
        .when(scheduledExecutor)
        .schedule(any(Runnable.class), anyLong(), eq(TimeUnit.NANOSECONDS));

    try {
      Session session =
          new Session(
              server,
              new NodeId(1, "session"),
              "session",
              Duration.ofMinutes(1),
              clientDescription(),
              "urn:eclipse:milo:test",
              uint(0),
              endpointDescription(),
              1L,
              new SecurityConfiguration(
                  SecurityPolicy.None, MessageSecurityMode.None, null, null, null, null, null));

      AtomicInteger closed = new AtomicInteger();
      session.addLifecycleListener((s, subscriptionsDeleted) -> closed.incrementAndGet());

      session.close(false);
      session.close(false);

      assertEquals(1, closed.get());
      verify(timeoutFuture, times(1)).cancel(false);
    } finally {
      executor.shutdownNow();
    }
  }

  private static ApplicationDescription clientDescription() {
    return new ApplicationDescription(
        "urn:eclipse:milo:test:client",
        "urn:eclipse:milo:test",
        LocalizedText.english("test client"),
        ApplicationType.Client,
        null,
        null,
        null);
  }

  private static EndpointDescription endpointDescription() {
    return new EndpointDescription(
        "opc.tcp://localhost:12685",
        clientDescription(),
        ByteString.NULL_VALUE,
        MessageSecurityMode.None,
        SecurityPolicy.None.getUri(),
        new UserTokenPolicy[0],
        TransportProfile.TCP_UASC_UABINARY.getUri(),
        ubyte(0));
  }
}
