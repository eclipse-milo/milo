/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.mqtt;

import com.hivemq.embedded.EmbeddedHiveMQ;
import com.hivemq.embedded.EmbeddedHiveMQBuilder;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

/**
 * An embedded HiveMQ Community Edition broker for integration tests: plaintext TCP on a loopback
 * ephemeral port (or an explicitly pinned port for restart tests), in-memory persistence, no
 * logging bootstrap (CE logs flow through the test classpath's slf4j-simple binding).
 *
 * <p>Lifecycle is per test class ({@code @BeforeAll}/{@code @AfterAll}): broker boot costs roughly
 * 1.5-8 s. Note that CE leaves a few non-daemon executor threads alive after {@link #stop()}; the
 * surefire fork exits anyway, but tests must not assert on JVM exit.
 *
 * <p>Environment requirements: the module pom pins test-scoped jctools-core 4.0.6 (CE fails
 * bootstrap on the client's mediated 2.1.2), and CE 2026.5 does not bootstrap on JDK 25 — run on
 * the project toolchain (17/21).
 */
final class EmbeddedTestBroker implements AutoCloseable {

  static {
    // best-effort: quiet CE startup chatter when this class loads before the first
    // slf4j-simple logger is created in the test JVM (cosmetic only)
    System.setProperty("org.slf4j.simpleLogger.log.com.hivemq", "warn");
  }

  private final EmbeddedHiveMQ hiveMq;
  private final int port;

  private EmbeddedTestBroker(EmbeddedHiveMQ hiveMq, int port) {
    this.hiveMq = hiveMq;
    this.port = port;
  }

  /**
   * Start a broker on an ephemeral loopback port.
   *
   * @param baseDir a directory exclusively owned by this broker instance (e.g. a {@code @TempDir}
   *     or a unique subdirectory of one); the config/data/extensions folders are created inside it.
   * @return the started broker.
   */
  static EmbeddedTestBroker start(Path baseDir) throws Exception {
    return start(baseDir, 0);
  }

  /**
   * Start a broker bound to {@code port} (0 = ephemeral). Pass the previously discovered port to
   * restart a logically-same broker on the same address after {@link #stop()}; use a fresh {@code
   * baseDir} for the new instance.
   *
   * @param baseDir a directory exclusively owned by this broker instance.
   * @param port the TCP port to bind, or 0 for an OS-assigned ephemeral port.
   * @return the started broker.
   */
  static EmbeddedTestBroker start(Path baseDir, int port) throws Exception {
    Path configFolder = Files.createDirectories(baseDir.resolve("conf"));
    Path dataFolder = Files.createDirectories(baseDir.resolve("data"));
    Path extensionsFolder = Files.createDirectories(baseDir.resolve("extensions"));

    // NB: never put literal ${...} placeholders in this config; CE's env-var substitution
    // calls System.exit(1) on unresolved placeholders.
    Files.writeString(
        configFolder.resolve("config.xml"),
        """
        <?xml version="1.0"?>
        <hivemq>
            <listeners>
                <tcp-listener>
                    <port>%d</port>
                    <bind-address>127.0.0.1</bind-address>
                </tcp-listener>
            </listeners>
            <persistence>
                <mode>in-memory</mode>
            </persistence>
        </hivemq>
        """
            .formatted(port));

    EmbeddedHiveMQ hiveMq =
        EmbeddedHiveMQBuilder.builder()
            .withConfigurationFolder(configFolder)
            .withDataFolder(dataFolder)
            .withExtensionsFolder(extensionsFolder)
            .withoutLoggingBootstrap()
            .build();

    hiveMq.start().join();

    int boundPort = discoverPort(hiveMq);

    // a typo'd or unreadable config silently falls back to CE's default listener on fixed
    // port 1883; asserting on the discovered port catches that here rather than in tests
    if (port != 0 && boundPort != port) {
      hiveMq.stop().join();
      throw new IllegalStateException(
          "broker bound port " + boundPort + " instead of requested " + port);
    }

    return new EmbeddedTestBroker(hiveMq, boundPort);
  }

  /** The actual TCP port the broker is listening on. */
  int port() {
    return port;
  }

  /** Stop the broker; in-memory persistence means all state (retained messages) is lost. */
  void stop() {
    hiveMq.stop().join();
  }

  @Override
  public void close() {
    stop();
  }

  /**
   * Discover the bound listener port via reflection on CE internals: {@code EmbeddedHiveMQImpl
   * .getInjector()} is package-private, and {@code getInstance} must be looked up on the {@code
   * com.google.inject.Injector} <em>interface</em> (the implementation class is package-private, so
   * an impl-class lookup fails with IllegalAccessException).
   */
  private static int discoverPort(EmbeddedHiveMQ hiveMq) throws Exception {
    Method getInjector = hiveMq.getClass().getDeclaredMethod("getInjector");
    getInjector.setAccessible(true);
    Object injector = getInjector.invoke(hiveMq);

    Class<?> serviceClass =
        Class.forName(
            "com.hivemq.configuration.service.impl.listener.ListenerConfigurationService");
    Method getInstance =
        Class.forName("com.google.inject.Injector").getMethod("getInstance", Class.class);
    Object service = getInstance.invoke(injector, serviceClass);

    List<?> listeners = (List<?>) serviceClass.getMethod("getTcpListeners").invoke(service);
    Object listener = listeners.get(0);
    return (int) listener.getClass().getMethod("getPort").invoke(listener);
  }
}
