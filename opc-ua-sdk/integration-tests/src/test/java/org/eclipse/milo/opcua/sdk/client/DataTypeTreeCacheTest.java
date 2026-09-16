/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.eclipse.milo.opcua.sdk.client.typetree.DataTypeTreeBuilder;
import org.eclipse.milo.opcua.sdk.client.typetree.DataTypeTreeFactory;
import org.eclipse.milo.opcua.sdk.core.typetree.DataTypeTree;
import org.eclipse.milo.opcua.sdk.test.AbstractClientServerTest;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Regression tests for https://github.com/eclipse-milo/milo/issues/1812: resetting the cached
 * DataTypeTree must never block behind an in-progress build, because the reset happens in a
 * SessionInitializer during session establishment and blocking there wedges reconnection.
 */
public class DataTypeTreeCacheTest extends AbstractClientServerTest {

  // Tests share a per-class client; the tree cache must be empty at the start of each test so
  // the factory installed by the test is actually invoked, regardless of test execution order.
  @BeforeEach
  void resetCachedTree() {
    client.resetDataTypeTree();
  }

  @AfterEach
  void restoreDefaultFactory() {
    client.setDataTypeTreeFactory(DataTypeTreeFactory.eager());
    client.resetDataTypeTree();
  }

  @Test
  void resetDoesNotBlockWhileBuildInProgress() throws Exception {
    var buildStarted = new CountDownLatch(1);
    var buildRelease = new CountDownLatch(1);

    client.setDataTypeTreeFactory(
        c -> {
          buildStarted.countDown();
          try {
            buildRelease.await();
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new UaException(StatusCodes.Bad_UnexpectedError, e);
          }
          return DataTypeTreeBuilder.build(c);
        });

    ExecutorService executor = Executors.newCachedThreadPool();
    try {
      Future<DataTypeTree> building = executor.submit(() -> client.getDataTypeTree());

      assertTrue(buildStarted.await(5, TimeUnit.SECONDS));

      var resetDone = new CountDownLatch(1);
      executor.submit(
          () -> {
            client.resetDataTypeTree();
            resetDone.countDown();
          });

      assertTrue(
          resetDone.await(2, TimeUnit.SECONDS),
          "resetDataTypeTree() blocked behind an in-progress build");

      buildRelease.countDown();

      assertNotNull(building.get(10, TimeUnit.SECONDS));
    } finally {
      executor.shutdownNow();
    }
  }

  @Test
  void failedBuildIsNotCached() throws Exception {
    var attempts = new AtomicInteger(0);

    client.setDataTypeTreeFactory(
        c -> {
          if (attempts.incrementAndGet() == 1) {
            throw new UaException(StatusCodes.Bad_SessionClosed, "simulated session loss");
          }
          return DataTypeTreeBuilder.build(c);
        });

    assertThrows(UaException.class, () -> client.getDataTypeTree());

    assertNotNull(client.getDataTypeTree());
    assertEquals(2, attempts.get());
  }
}
