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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.test.TestClient;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.junit.jupiter.api.Test;

/**
 * Regression coverage for shutdown races between session listener callbacks and diagnostics
 * namespace teardown.
 *
 * <p>The server shutdown contract is that listener callbacks already in progress are allowed to
 * finish before diagnostics nodes are torn down. At the same time, shutdown must stay re-entrant
 * enough for a listener callback to request shutdown without waiting on itself.
 */
public class ServerShutdownSessionDiagnosticsRaceTest {

  @Test
  void shutdownWaitsForInFlightSessionListenerBeforeNamespaceTeardown() throws Exception {
    OpcUaServer server = TestServer.create().getServer();
    ExecutorService shutdownExecutor = Executors.newFixedThreadPool(2);
    CountDownLatch listenerStarted = new CountDownLatch(1);
    CountDownLatch releaseListener = new CountDownLatch(1);
    CompletableFuture<OpcUaServer> shutdownFuture = null;
    CompletableFuture<OpcUaServer> concurrentShutdownFuture = null;
    OpcUaClient client = null;

    server
        .getSessionManager()
        .addSessionListener(
            new SessionListener() {
              @Override
              public void onSessionCreated(Session session) {
                listenerStarted.countDown();
                try {
                  assertTrue(
                      releaseListener.await(5, TimeUnit.SECONDS), "listener release timed out");
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                  throw new RuntimeException(e);
                }
              }
            });

    try {
      server.startup().get();

      client = TestClient.create(server, cfg -> {});
      client.connectAsync();

      assertTrue(listenerStarted.await(5, TimeUnit.SECONDS), "listener did not start");

      // The first shutdown must wait for the in-flight listener before namespace teardown.
      shutdownFuture =
          CompletableFuture.supplyAsync(
              () -> {
                try {
                  return server.shutdown().get();
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                  throw new CompletionException(e);
                } catch (ExecutionException e) {
                  throw new CompletionException(e);
                }
              },
              shutdownExecutor);

      CompletableFuture<OpcUaServer> future = shutdownFuture;
      assertThrows(TimeoutException.class, () -> future.get(200, TimeUnit.MILLISECONDS));

      // A second caller must observe the same shutdown instead of racing ahead to teardown.
      concurrentShutdownFuture =
          CompletableFuture.supplyAsync(
              () -> {
                try {
                  return server.shutdown().get();
                } catch (InterruptedException e) {
                  Thread.currentThread().interrupt();
                  throw new CompletionException(e);
                } catch (ExecutionException e) {
                  throw new CompletionException(e);
                }
              },
              shutdownExecutor);

      CompletableFuture<OpcUaServer> concurrentFuture = concurrentShutdownFuture;
      assertThrows(TimeoutException.class, () -> concurrentFuture.get(200, TimeUnit.MILLISECONDS));

      releaseListener.countDown();

      assertSame(server, future.get(5, TimeUnit.SECONDS));
      assertSame(server, concurrentFuture.get(5, TimeUnit.SECONDS));
    } finally {
      releaseListener.countDown();

      if (shutdownFuture != null) {
        try {
          shutdownFuture.get(5, TimeUnit.SECONDS);
        } catch (Exception ignored) {
          // The assertion path reports shutdown failures.
        }
      } else {
        server.shutdown().get(5, TimeUnit.SECONDS);
      }
      if (concurrentShutdownFuture != null) {
        try {
          concurrentShutdownFuture.get(5, TimeUnit.SECONDS);
        } catch (Exception ignored) {
          // The assertion path reports shutdown failures.
        }
      }

      if (client != null) {
        try {
          client.disconnectAsync().get(2, TimeUnit.SECONDS);
        } catch (Exception ignored) {
          // The server may already be shut down by the assertion path above.
        }
      }

      shutdownExecutor.shutdownNow();
    }
  }

  @Test
  void shutdownFromSessionListenerDoesNotWaitOnItself() throws Exception {
    OpcUaServer server = TestServer.create().getServer();
    CountDownLatch listenerStarted = new CountDownLatch(1);
    CountDownLatch laterListenerCalled = new CountDownLatch(1);
    CompletableFuture<OpcUaServer> shutdownFuture = new CompletableFuture<>();
    CompletableFuture<OpcUaClient> connectFuture = null;
    OpcUaClient client = null;

    server
        .getSessionManager()
        .addSessionListener(
            new SessionListener() {
              @Override
              public void onSessionCreated(Session session) {
                listenerStarted.countDown();
                try {
                  shutdownFuture.complete(server.shutdown().get(5, TimeUnit.SECONDS));
                } catch (Exception e) {
                  shutdownFuture.completeExceptionally(e);
                }
              }
            });
    server
        .getSessionManager()
        .addSessionListener(
            new SessionListener() {
              @Override
              public void onSessionCreated(Session session) {
                laterListenerCalled.countDown();
              }
            });

    try {
      server.startup().get();

      client = TestClient.create(server, cfg -> {});
      connectFuture = client.connectAsync();

      assertTrue(listenerStarted.await(5, TimeUnit.SECONDS), "listener did not start");
      assertSame(server, shutdownFuture.get(5, TimeUnit.SECONDS));
      // Once the first listener starts shutdown, later listeners in the same notification pass are
      // skipped so they cannot run against partially torn-down diagnostics state.
      assertFalse(
          laterListenerCalled.await(200, TimeUnit.MILLISECONDS),
          "later listener was invoked after shutdown started");
    } finally {
      if (connectFuture != null) {
        try {
          connectFuture.get(2, TimeUnit.SECONDS);
        } catch (Exception ignored) {
          // The listener shuts the server down during the connection attempt.
        }
      }

      if (client != null) {
        try {
          client.disconnectAsync().get(2, TimeUnit.SECONDS);
        } catch (Exception ignored) {
          // The server may already be shut down by the assertion path above.
        }
      }

      try {
        server.shutdown().get(2, TimeUnit.SECONDS);
      } catch (Exception ignored) {
        // The assertion path reports shutdown failures.
      }
    }
  }
}
