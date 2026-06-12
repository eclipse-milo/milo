/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.examples.pubsub.mqtt;

import com.hivemq.embedded.EmbeddedHiveMQ;
import com.hivemq.embedded.EmbeddedHiveMQBuilder;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runs an embedded HiveMQ Community Edition MQTT broker, bound to {@code 127.0.0.1} with in-memory
 * persistence, until Ctrl-C.
 *
 * <p>This class exists so the MQTT PubSub examples are self-contained: start it in one terminal and
 * the publisher/subscriber examples have a broker to talk to with zero external infrastructure. Any
 * external MQTT broker (Mosquitto, HiveMQ, EMQX, ...) works equally well — pass its URI to the
 * publisher/subscriber examples instead of running this one.
 *
 * <p>Terminal order: start this broker <b>first</b>, wait for the "broker ready" line, then start
 * the MQTT subscriber and publisher examples in their own terminals.
 *
 * <p>Run from the repository root (build the examples module once with {@code mvn -q -pl
 * milo-examples/pubsub-examples -am install -DskipTests} if you haven't already):
 *
 * <pre>{@code
 * mvn -pl milo-examples/pubsub-examples exec:java \
 *     -Dexec.mainClass=org.eclipse.milo.examples.pubsub.mqtt.EmbeddedBrokerExample
 * }</pre>
 *
 * <p>An optional first argument overrides the listen port (default {@code 1883}), e.g. {@code
 * -Dexec.args="1884"}.
 *
 * <p>Expected output: broker bootstrap takes roughly 1.5-8 seconds, then a single line containing
 * {@code broker ready on port 1883} is logged and the broker runs until Ctrl-C.
 *
 * <p>Note: HiveMQ CE does not bootstrap on JDK 25 or later; run this example on JDK 17 or 21 (the
 * rest of the PubSub examples are fine on newer JDKs).
 */
public class EmbeddedBrokerExample {

  static {
    // The broker is built with withoutLoggingBootstrap(), so CE logs flow through this module's
    // slf4j-simple binding; quiet its startup chatter so the "broker ready" line stands out.
    System.setProperty("org.slf4j.simpleLogger.log.com.hivemq", "warn");
  }

  private static final String BIND_ADDRESS = "127.0.0.1";
  private static final int DEFAULT_PORT = 1883;

  public static void main(String[] args) throws Exception {
    int port = DEFAULT_PORT;
    if (args.length > 0) {
      port = Integer.parseInt(args[0]);
      if (port < 1 || port > 65535) {
        throw new IllegalArgumentException("port must be in [1, 65535]: " + port);
      }
    }

    new EmbeddedBrokerExample(port).run();
  }

  private final Logger logger = LoggerFactory.getLogger(getClass());

  private final int port;

  public EmbeddedBrokerExample(int port) {
    this.port = port;
  }

  private void run() throws Exception {
    // HiveMQ CE 2026.5 fails its bootstrap on JDK 25+; fail fast with a clear message
    // instead of letting users puzzle over the resulting stack trace.
    int javaVersion = Runtime.version().feature();
    if (javaVersion >= 25) {
      logger.error(
          "HiveMQ CE does not bootstrap on JDK {}; run this on JDK 17 or 21.", javaVersion);
      System.exit(1);
    }

    // Fail fast with a clear message if the port is already taken; otherwise the broker's
    // own bind failure surfaces as an opaque bootstrap exception.
    try (ServerSocket probe = new ServerSocket()) {
      probe.bind(new InetSocketAddress(BIND_ADDRESS, port));
    } catch (IOException e) {
      logger.error(
          "cannot bind {}:{} (port already in use?): {}", BIND_ADDRESS, port, e.getMessage());
      System.exit(1);
    }

    Path baseDir = Files.createTempDirectory("milo-pubsub-example-broker");

    EmbeddedHiveMQ hiveMq = startBroker(baseDir);

    logger.info("broker ready on port {}", port);

    final var future = new CompletableFuture<Void>();

    Runtime.getRuntime()
        .addShutdownHook(
            new Thread(
                () -> {
                  hiveMq.stop().join();
                  deleteRecursively(baseDir);
                  future.complete(null);
                },
                "broker-shutdown"));

    future.get();
  }

  private EmbeddedHiveMQ startBroker(Path baseDir) throws IOException {
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
                    <bind-address>%s</bind-address>
                </tcp-listener>
            </listeners>
            <persistence>
                <mode>in-memory</mode>
            </persistence>
        </hivemq>
        """
            .formatted(port, BIND_ADDRESS));

    EmbeddedHiveMQ hiveMq =
        EmbeddedHiveMQBuilder.builder()
            .withConfigurationFolder(configFolder)
            .withDataFolder(dataFolder)
            .withExtensionsFolder(extensionsFolder)
            .withoutLoggingBootstrap()
            .build();

    logger.info(
        "starting embedded HiveMQ CE broker on {}:{} (config dir: {})...",
        BIND_ADDRESS,
        port,
        baseDir);

    try {
      hiveMq.start().join();
    } catch (Exception e) {
      logger.error("broker failed to start: {}", e.getMessage(), e);
      System.exit(1);
    }

    return hiveMq;
  }

  private void deleteRecursively(Path dir) {
    try (var paths = Files.walk(dir)) {
      paths
          .sorted(Comparator.reverseOrder())
          .forEach(
              path -> {
                try {
                  Files.delete(path);
                } catch (IOException ignored) {
                  // best-effort cleanup of the per-run temp dir
                }
              });
    } catch (IOException ignored) {
      // best-effort cleanup of the per-run temp dir
    }
  }
}
