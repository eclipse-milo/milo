/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.UdpDatagramAddress;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * {@link PubSubConfigurationStore} precedence per pinned decision S7: a non-null {@code load()}
 * result wins over the attach configuration; a null {@code load()} result means the attach
 * configuration is used and saved exactly once; {@code save()} failures are non-fatal.
 */
class ServerPubSubConfigurationStoreTest {

  private static TestPubSubServer testServer;

  @BeforeAll
  static void startServer() {
    testServer = TestPubSubServer.create();
  }

  @AfterAll
  static void stopServer() {
    testServer.close();
  }

  @Test
  void loadedConfigurationWinsOverAttachConfiguration() {
    PubSubConfig storedConfig = configWithConnection("loaded-conn");

    var store = new RecordingStore();
    store.toLoad = storedConfig.toDataType(testServer.getServer().getNamespaceTable());

    ServerPubSubOptions options = ServerPubSubOptions.builder().configurationStore(store).build();

    try (ServerPubSub serverPubSub =
        ServerPubSub.attach(testServer.getServer(), configWithConnection("attach-conn"), options)) {

      // the loaded configuration's components exist; the attach configuration's do not
      assertTrue(serverPubSub.runtime().components().connection("loaded-conn").isPresent());
      assertTrue(serverPubSub.runtime().components().connection("attach-conn").isEmpty());

      // the loaded configuration is not re-saved
      assertEquals(List.of(), store.saved);
    }
  }

  @Test
  void attachConfigurationUsedAndSavedExactlyOnceWhenStoreIsEmpty() {
    var store = new RecordingStore();

    ServerPubSubOptions options = ServerPubSubOptions.builder().configurationStore(store).build();

    PubSubConfig attachConfig = configWithConnection("attach-conn");

    try (ServerPubSub serverPubSub =
        ServerPubSub.attach(testServer.getServer(), attachConfig, options)) {

      assertTrue(serverPubSub.runtime().components().connection("attach-conn").isPresent());

      assertEquals(1, store.saved.size(), "save() called exactly once");

      // the saved wire form carries the seeded VersionTime, never the mapper's uint(0)
      // placeholder (D26/R8); everything else is the mapped attach configuration
      PubSubConfiguration2DataType saved = store.saved.get(0);
      assertTrue(saved.getConfigurationVersion().longValue() != 0L, "seeded VersionTime");
      assertEquals(
          attachConfig.toDataType(testServer.getServer().getNamespaceTable()),
          PubSubConfigurationFace.withConfigurationVersion(saved, Unsigned.uint(0)),
          "save() received the mapped attach configuration");
    }
  }

  @Test
  void saveFailureIsNonFatalToAttach() {
    var store =
        new RecordingStore() {
          @Override
          public void save(PubSubConfiguration2DataType value) {
            super.save(value);
            throw new RuntimeException("simulated save failure");
          }
        };

    ServerPubSubOptions options = ServerPubSubOptions.builder().configurationStore(store).build();

    try (ServerPubSub serverPubSub =
        ServerPubSub.attach(testServer.getServer(), configWithConnection("attach-conn"), options)) {

      // attach succeeded despite the save failure, and save was attempted once
      assertTrue(serverPubSub.runtime().components().connection("attach-conn").isPresent());
      assertEquals(1, store.saved.size());
    }
  }

  @Test
  void loadFailurePropagatesOutOfAttach() {
    var failure = new RuntimeException("simulated load failure");

    var store =
        new RecordingStore() {
          @Override
          public @Nullable PubSubConfiguration2DataType load() {
            throw failure;
          }
        };

    ServerPubSubOptions options = ServerPubSubOptions.builder().configurationStore(store).build();

    RuntimeException e =
        assertThrows(
            RuntimeException.class,
            () ->
                ServerPubSub.attach(
                    testServer.getServer(), configWithConnection("attach-conn"), options));

    assertSame(failure, e);
  }

  // region fixtures

  /**
   * A config whose only distinguishing feature is the name of its single UDP connection; the
   * connection has no groups, so the service is never required to bind anything.
   */
  private static PubSubConfig configWithConnection(String connectionName) {
    return PubSubConfig.builder()
        .connection(
            PubSubConnectionConfig.udp(connectionName)
                .address(UdpDatagramAddress.unicast("127.0.0.1", 14840))
                .build())
        .build();
  }

  /** An in-memory {@link PubSubConfigurationStore} recording every {@code save()} argument. */
  private static class RecordingStore implements PubSubConfigurationStore {

    volatile @Nullable PubSubConfiguration2DataType toLoad;

    final List<PubSubConfiguration2DataType> saved = new CopyOnWriteArrayList<>();

    @Override
    public @Nullable PubSubConfiguration2DataType load() {
      return toLoad;
    }

    @Override
    public void save(PubSubConfiguration2DataType value) {
      saved.add(value);
    }
  }

  // endregion
}
