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

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.ByteString;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ApplicationType;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.CreateSessionResponse;
import org.junit.jupiter.api.Test;

/**
 * Verifies the server's behavior at the session limit.
 *
 * <p>OPC UA Part 4, 5.7.2 specifies that when the session limit is reached the server shall close
 * the oldest Session that has not yet been activated, rather than rejecting the new CreateSession
 * request outright. Only if every existing Session has already been activated should the server
 * respond with {@code Bad_TooManySessions}. The limit must also be honored under concurrent
 * CreateSession requests.
 */
public class SessionEvictionTest {

  private static OpcUaServerConfigLimits maxSessions(int n) {
    return new OpcUaServerConfigLimits() {
      @Override
      public UInteger getMaxSessions() {
        return uint(n);
      }
    };
  }

  @Test
  void evictsOldestUnactivatedSessionWhenAtLimit() throws Exception {
    OpcUaServer server = TestServer.create(maxSessions(3)).getServer();
    server.startup().get();

    OpcUaClient client = TestClient.create(server, cfg -> {});
    try {
      // The connected client holds one *activated* session.
      client.connect();
      assertEquals(1, currentSessionCount(server));

      // Create two unactivated sessions, oldest first, filling the limit of 3. The server orders
      // eviction by connection time (wall clock), so a deliberate pause guarantees the two
      // sessions get strictly different, correctly-ordered timestamps regardless of the
      // platform's clock granularity (which can be coarser than a back-to-back round-trip).
      NodeId oldest = createSession(client, "unactivated-oldest").getSessionId();
      Thread.sleep(50);
      NodeId newer = createSession(client, "unactivated-newer").getSessionId();
      assertEquals(3, currentSessionCount(server));

      // Sanity-check the precondition the eviction assertion below relies on.
      assertTrue(
          connectTime(server, oldest) < connectTime(server, newer),
          "expected the first-created session to have the earlier connection time");

      // A further CreateSession at the limit must succeed by evicting the OLDEST
      // unactivated session rather than throwing Bad_TooManySessions.
      CreateSessionResponse response = createSession(client, "unactivated-newest");
      assertTrue(response.getResponseHeader().getServiceResult().isGood());

      // The count is unchanged: exactly one session was evicted to make room.
      assertEquals(3, currentSessionCount(server));

      Set<NodeId> remaining =
          server.getSessionManager().getAllSessions().stream()
              .map(Session::getSessionId)
              .collect(Collectors.toSet());

      // The oldest unactivated session was the one evicted...
      assertFalse(remaining.contains(oldest), "oldest unactivated session should be evicted");
      // ...the newer unactivated session survived...
      assertTrue(remaining.contains(newer), "newer unactivated session should not be evicted");
      assertTrue(remaining.contains(response.getSessionId()), "new session should be present");

      // ...and the activated session was untouched; it remains usable.
      assertDoesNotThrow(client::readOperationLimits);
    } finally {
      try {
        client.disconnectAsync().get(2, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(2, TimeUnit.SECONDS);
      }
    }
  }

  @Test
  void rejectsNewSessionWhenAllSessionsActivated() throws Exception {
    OpcUaServer server = TestServer.create(maxSessions(2)).getServer();
    server.startup().get();

    OpcUaClient client1 = TestClient.create(server, cfg -> {});
    OpcUaClient client2 = TestClient.create(server, cfg -> {});
    try {
      // Fill the limit with two activated sessions.
      client1.connect();
      client2.connect();
      assertEquals(2, currentSessionCount(server));

      // With no unactivated session to evict, a new CreateSession must be rejected.
      ExecutionException ex =
          assertThrows(ExecutionException.class, () -> createSession(client1, "rejected"));

      Throwable cause = ex.getCause();
      assertTrue(cause instanceof UaException, "expected UaException, got " + cause);
      assertEquals(
          StatusCodes.Bad_TooManySessions, ((UaException) cause).getStatusCode().getValue());
    } finally {
      try {
        client1.disconnectAsync().get(2, TimeUnit.SECONDS);
        client2.disconnectAsync().get(2, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(2, TimeUnit.SECONDS);
      }
    }
  }

  @Test
  void doesNotExceedSessionLimitUnderConcurrentCreate() throws Exception {
    int limit = 5;
    OpcUaServer server = TestServer.create(maxSessions(limit)).getServer();
    server.startup().get();

    OpcUaClient client = TestClient.create(server, cfg -> {});
    ExecutorService executor = Executors.newFixedThreadPool(8);
    try {
      // One activated session plus enough unactivated sessions to reach the limit.
      client.connect();
      for (int i = 0; i < limit - 1; i++) {
        createSession(client, "pending-" + i);
      }
      assertEquals(limit, currentSessionCount(server));

      // Fire a burst of concurrent CreateSession requests while at the limit. Each one
      // can be admitted by evicting an unactivated session, but the configured limit
      // (a resource-protection bound, OPC UA Part 4 Section 5.7.2) must never be exceeded.
      List<Future<?>> futures = new ArrayList<>();
      for (int i = 0; i < 32; i++) {
        String name = "burst-" + i;
        futures.add(executor.submit(() -> createSession(client, name)));
      }
      for (Future<?> future : futures) {
        try {
          future.get();
        } catch (ExecutionException ignored) {
          // A request may legitimately fail; we only care that the limit is never exceeded.
        }
      }

      assertTrue(
          currentSessionCount(server) <= limit,
          "session count " + currentSessionCount(server) + " exceeded the limit of " + limit);
    } finally {
      executor.shutdownNow();
      try {
        client.disconnectAsync().get(2, TimeUnit.SECONDS);
      } finally {
        server.shutdown().get(2, TimeUnit.SECONDS);
      }
    }
  }

  private static int currentSessionCount(OpcUaServer server) {
    return server.getSessionManager().getCurrentSessionCount().intValue();
  }

  /** The connection time, in 100ns ticks, of the session with the given id. */
  private static long connectTime(OpcUaServer server, NodeId sessionId) {
    return server.getSessionManager().getAllSessions().stream()
        .filter(s -> s.getSessionId().equals(sessionId))
        .map(s -> s.getConnectionTime().getUtcTime())
        .findFirst()
        .orElseThrow(() -> new AssertionError("session not found: " + sessionId));
  }

  /**
   * Send a raw {@link CreateSessionRequest} over the client's transport without activating the
   * resulting session. The endpoint used by the test client has {@code SecurityPolicy.None}, so no
   * client nonce or certificate is required.
   */
  private static CreateSessionResponse createSession(OpcUaClient client, String sessionName)
      throws Exception {
    UInteger maxResponseMessageSize = client.getConfig().getMaxResponseMessageSize();

    ApplicationDescription clientDescription =
        new ApplicationDescription(
            client.getConfig().getApplicationUri(),
            client.getConfig().getProductUri(),
            client.getConfig().getApplicationName(),
            ApplicationType.Client,
            null,
            null,
            null);

    CreateSessionRequest request =
        new CreateSessionRequest(
            client.newRequestHeader(),
            clientDescription,
            null,
            client.getConfig().getEndpoint().getEndpointUrl(),
            sessionName,
            ByteString.NULL_VALUE,
            ByteString.NULL_VALUE,
            60_000.0,
            maxResponseMessageSize);

    return (CreateSessionResponse) client.getTransport().sendRequestMessage(request).get();
  }
}
