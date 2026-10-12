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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/** Tests the state transitions of {@link AbstractLifecycle}, especially around startup failure. */
class AbstractLifecycleTest {

  // A component whose onStartup throws has (per its own contract) cleaned up after itself; if
  // the lifecycle stayed RUNNING, running-state guards throughout the component would pass and
  // operate on the torn-down state.
  @Test
  void failedStartupLeavesTheLifecycleNotRunning() {
    var lifecycle = new RecordingLifecycle(true);

    assertThrows(IllegalStateException.class, lifecycle::startup);

    assertFalse(lifecycle.isRunning());
    assertTrue(lifecycle.isNotRunning());
  }

  // Callers dispose components uniformly; disposing one whose startup failed must neither throw
  // nor re-run shutdown logic against state the startup failure already rolled back.
  @Test
  void shutdownAfterFailedStartupIsANoOp() {
    var lifecycle = new RecordingLifecycle(true);

    assertThrows(IllegalStateException.class, lifecycle::startup);

    lifecycle.shutdown();

    assertFalse(lifecycle.onShutdownCalled);
  }

  // A lifecycle whose startup failed is spent, like one that was shut down: the one-shot
  // NEW -> RUNNING -> STOPPED progression does not restart.
  @Test
  void startupAfterFailedStartupThrows() {
    var lifecycle = new RecordingLifecycle(true);

    assertThrows(IllegalStateException.class, lifecycle::startup);

    assertThrows(IllegalStateException.class, lifecycle::startup);
  }

  @Test
  void successfulStartupRunsAndShutdownStops() {
    var lifecycle = new RecordingLifecycle(false);

    lifecycle.startup();
    assertTrue(lifecycle.isRunning());

    lifecycle.shutdown();
    assertTrue(lifecycle.isNotRunning());
    assertTrue(lifecycle.onShutdownCalled);
  }

  private static class RecordingLifecycle extends AbstractLifecycle {

    volatile boolean onShutdownCalled = false;

    private final boolean failOnStartup;

    RecordingLifecycle(boolean failOnStartup) {
      this.failOnStartup = failOnStartup;
    }

    @Override
    protected void onStartup() {
      if (failOnStartup) {
        throw new IllegalStateException("startup failure");
      }
    }

    @Override
    protected void onShutdown() {
      onShutdownCalled = true;
    }
  }
}
