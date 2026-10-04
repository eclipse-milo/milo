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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.test.TestServer;
import org.junit.jupiter.api.Test;

/** Verifies ownership and startup rollback for the Wiki's server lifecycle recipe. */
public class WikiServerLifecycleTest {

  @Test
  void registeredNamespaceStartsAndStopsWithTheServer() throws Exception {
    OpcUaServer server = TestServer.create().getServer();
    var events = new ArrayList<String>();
    ManagedNamespaceWithLifecycle namespace =
        new ManagedNamespaceWithLifecycle(server, "urn:eclipse:milo:wiki:lifecycle") {
          {
            getLifecycleManager()
                .addLifecycle(
                    new Lifecycle() {
                      @Override
                      public void startup() {
                        events.add("start");
                      }

                      @Override
                      public void shutdown() {
                        events.add("stop");
                      }
                    });
          }
        };
    // wiki:lifecycle:start
    server.addLifecycleParticipant(namespace);
    try {
      server.startup().get(10, TimeUnit.SECONDS);
      if (server.getBoundEndpoints().isEmpty()) {
        throw new IllegalStateException("server has no bound endpoint");
      }
      // Application work runs here while the server owns the namespace lifecycle.
    } finally {
      server.shutdown().get(10, TimeUnit.SECONDS);
    }
    // wiki:lifecycle:end
    assertEquals(List.of("start", "stop"), events);
    assertTrue(server.getBoundEndpoints().isEmpty());
    server.startup().get(10, TimeUnit.SECONDS);
    assertTrue(
        server.getBoundEndpoints().isEmpty(),
        "repeated startup does not restart a terminal server");
  }

  // A failing participant owns its partial startup cleanup; only completed participants roll back.
  @Test
  void failedParticipantRollsBackEarlierParticipantsInReverseOrder() throws Exception {
    OpcUaServer server = TestServer.create().getServer();
    var events = new ArrayList<String>();
    server.addLifecycleParticipant(participant("one", events, false));
    server.addLifecycleParticipant(participant("two", events, false));
    server.addLifecycleParticipant(participant("fail", events, true));
    try {
      assertThrows(ExecutionException.class, () -> server.startup().get(10, TimeUnit.SECONDS));
      assertEquals(List.of("start one", "start two", "start fail", "stop two", "stop one"), events);
      assertTrue(server.getBoundEndpoints().isEmpty());
      assertFalse(events.contains("stop fail"));
    } finally {
      server.shutdown().get(10, TimeUnit.SECONDS);
    }
    assertEquals(1, events.stream().filter("stop one"::equals).count());
  }

  private static Lifecycle participant(String name, List<String> events, boolean fail) {
    return new Lifecycle() {
      @Override
      public void startup() {
        events.add("start " + name);
        if (fail) throw new IllegalStateException("fixture startup failure");
      }

      @Override
      public void shutdown() {
        events.add("stop " + name);
      }
    };
  }
}
