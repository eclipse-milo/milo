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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.function.ToLongFunction;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetReceivedEvent;
import org.eclipse.milo.opcua.sdk.pubsub.DataSetSnapshot;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnostics;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubService;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubServiceConfig;
import org.eclipse.milo.opcua.sdk.pubsub.PublishedDataSetSource;
import org.eclipse.milo.opcua.sdk.pubsub.config.FieldDefinition;
import org.eclipse.milo.opcua.sdk.pubsub.config.KeyFieldAddress;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.jspecify.annotations.Nullable;

/** Shared helpers for the MQTT transport integration tests. */
final class PubSubMqttTestSupport {

  /** Generous default await timeout: broker connects are async with reconnect backoff. */
  static final Duration TIMEOUT = Duration.ofSeconds(15);

  private PubSubMqttTestSupport() {}

  /** A {@link PubSubServiceConfig} with a fresh {@link MqttTransportProvider} registered. */
  static PubSubServiceConfig mqttServiceConfig() {
    return PubSubServiceConfig.builder().transportProvider(MqttTransportProvider.create()).build();
  }

  /**
   * Pick a currently free loopback TCP port by binding and closing an ephemeral server socket. Used
   * as a guaranteed-unreachable broker address; the small race is accepted.
   */
  static int freeTcpPort() throws IOException {
    try (ServerSocket socket = new ServerSocket(0, 1, InetAddress.getLoopbackAddress())) {
      return socket.getLocalPort();
    }
  }

  /** A source that reads the current values of an AtomicReference-backed map by field key. */
  static PublishedDataSetSource mapSource(AtomicReference<Map<String, DataValue>> values) {
    return context -> {
      DataSetSnapshot.Builder builder = DataSetSnapshot.builder(context);
      Map<String, DataValue> currentValues = values.get();
      for (FieldDefinition field : context.fields()) {
        String key =
            field.getSource() instanceof KeyFieldAddress keyAddress
                ? keyAddress.key()
                : field.getName();
        DataValue value = currentValues.get(key);
        if (value != null) {
          builder.field(field.getName(), value);
        }
      }
      return builder.build();
    };
  }

  /** Wait for an event matching {@code predicate}, discarding non-matching events. */
  static DataSetReceivedEvent awaitEvent(
      BlockingQueue<DataSetReceivedEvent> events, Predicate<DataSetReceivedEvent> predicate)
      throws InterruptedException {

    return awaitEvent(events, predicate, TIMEOUT);
  }

  /** Wait for an event matching {@code predicate}, discarding non-matching events. */
  static DataSetReceivedEvent awaitEvent(
      BlockingQueue<DataSetReceivedEvent> events,
      Predicate<DataSetReceivedEvent> predicate,
      Duration timeout)
      throws InterruptedException {

    long deadline = System.nanoTime() + timeout.toNanos();
    while (true) {
      long remaining = deadline - System.nanoTime();
      if (remaining <= 0) {
        return fail("timed out waiting for a matching DataSetReceivedEvent");
      }
      DataSetReceivedEvent event =
          events.poll(Math.min(remaining, 100_000_000L), TimeUnit.NANOSECONDS);
      if (event != null && predicate.test(event)) {
        return event;
      }
    }
  }

  /** Poll {@code condition} until it holds or {@link #TIMEOUT} expires. */
  static void awaitTrue(BooleanSupplier condition, String description) throws InterruptedException {

    awaitTrue(condition, description, TIMEOUT);
  }

  /** Poll {@code condition} until it holds or the deadline expires. */
  static void awaitTrue(BooleanSupplier condition, String description, Duration timeout)
      throws InterruptedException {

    long deadline = System.nanoTime() + timeout.toNanos();
    while (!condition.getAsBoolean()) {
      if (System.nanoTime() >= deadline) {
        fail("timed out waiting for: " + description);
      }
      Thread.sleep(25);
    }
  }

  static long counter(
      PubSubService service,
      String path,
      ToLongFunction<PubSubDiagnostics.ComponentDiagnostics> counter) {

    PubSubDiagnostics.ComponentDiagnostics diagnostics =
        service.diagnostics().component(path).orElse(null);

    return diagnostics == null ? 0L : counter.applyAsLong(diagnostics);
  }

  static @Nullable StatusCode lastError(PubSubService service, String path) {
    PubSubDiagnostics.ComponentDiagnostics diagnostics =
        service.diagnostics().component(path).orElse(null);

    return diagnostics == null ? null : diagnostics.lastError();
  }

  /** Assert that {@code startup()} fails and return the {@link StatusCode} of its UaException. */
  static StatusCode assertStartupFails(PubSubService service) {
    ExecutionException e =
        assertThrows(
            ExecutionException.class,
            () -> service.startup().get(TIMEOUT.toSeconds(), TimeUnit.SECONDS));

    Throwable cause = e.getCause();
    while (cause != null && !(cause instanceof UaException)) {
      cause = cause.getCause();
    }
    assertNotNull(cause, "expected a UaException cause, got: " + e);
    return ((UaException) cause).getStatusCode();
  }

  /** Assert that {@code future} completes exceptionally with a UaException and return it. */
  static UaException assertUaFailure(CompletableFuture<?> future) {
    ExecutionException e =
        assertThrows(
            ExecutionException.class, () -> future.get(TIMEOUT.toSeconds(), TimeUnit.SECONDS));

    Throwable cause = e.getCause();
    while (cause != null && !(cause instanceof UaException)) {
      cause = cause.getCause();
    }
    assertNotNull(cause, "expected a UaException cause, got: " + e);
    return (UaException) cause;
  }
}
