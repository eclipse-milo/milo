/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.internal;

import io.netty.buffer.Unpooled;
import java.time.Duration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsEvent;
import org.eclipse.milo.opcua.sdk.pubsub.config.MqttConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherStatusMode;
import org.eclipse.milo.opcua.sdk.pubsub.config.WriterGroupConfig;
import org.eclipse.milo.opcua.sdk.pubsub.json.JsonStatusCodec;
import org.eclipse.milo.opcua.sdk.pubsub.transport.BrokerTopics;
import org.eclipse.milo.opcua.sdk.pubsub.transport.MessageAddress;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.jspecify.annotations.Nullable;

/** Publishes retained broker status messages for JSON-over-MQTT publisher connections. */
final class BrokerStatusPublisher {

  static final Duration CYCLIC_REPORT_INTERVAL = Duration.ofSeconds(1);

  private static final int MAX_SEND_RETRIES = 5;

  private static final long RETRY_BASE_DELAY_MILLIS = 1_000;

  private final PubSubServiceImpl service;
  private final ConnectionRuntime connection;
  private final Object lock = new Object();

  private volatile boolean disposed = false;
  private @Nullable ScheduledFuture<?> cyclicTask;
  private @Nullable ScheduledFuture<?> retryTask;
  private long cyclicGeneration = 0L;
  private long cyclicSubmittedGeneration = 0L;
  private long publishGeneration = 0L;
  private long pendingRetryGeneration = 0L;
  private int retryAttempts = 0;

  BrokerStatusPublisher(PubSubServiceImpl service, ConnectionRuntime connection) {
    this.service = service;
    this.connection = connection;
  }

  void onConnectionState(PubSubState state) {
    synchronized (lock) {
      if (disposed) {
        return;
      }

      cancelRetryTask();

      StatusStream stream = statusStream();
      if (stream == null) {
        cancelCyclicTask();
        return;
      }

      if (state == PubSubState.Operational && stream.cyclic()) {
        publish(stream, PubSubState.Operational);
        if (cyclicTask == null) {
          long generation = ++cyclicGeneration;
          long periodNanos = CYCLIC_REPORT_INTERVAL.toNanos();
          cyclicTask =
              service
                  .getScheduledExecutor()
                  .scheduleAtFixedRate(
                      () -> submitCyclic(stream, generation),
                      periodNanos,
                      periodNanos,
                      TimeUnit.NANOSECONDS);
        }
      } else {
        cancelCyclicTask();
        publish(stream, state);
      }
    }
  }

  void dispose() {
    synchronized (lock) {
      disposed = true;
      cancelCyclicTask();
      cancelRetryTask();
    }
  }

  private void submitCyclic(StatusStream stream, long generation) {
    synchronized (lock) {
      if (disposed || generation != cyclicGeneration || cyclicSubmittedGeneration == generation) {
        return;
      }
      cyclicSubmittedGeneration = generation;
    }

    try {
      connection.submitToDispatchQueue(() -> publishCyclic(stream, generation));
    } catch (RejectedExecutionException e) {
      clearCyclicSubmitted(generation);
    }
  }

  private void publishCyclic(StatusStream stream, long generation) {
    synchronized (lock) {
      try {
        if (!disposed
            && generation == cyclicGeneration
            && connection.state() == PubSubState.Operational) {
          publish(stream, PubSubState.Operational);
        }
      } finally {
        clearCyclicSubmittedLocked(generation);
      }
    }
  }

  private void clearCyclicSubmitted(long generation) {
    synchronized (lock) {
      clearCyclicSubmittedLocked(generation);
    }
  }

  private void clearCyclicSubmittedLocked(long generation) {
    if (cyclicSubmittedGeneration == generation) {
      cyclicSubmittedGeneration = 0L;
    }
  }

  private void cancelCyclicTask() {
    if (cyclicTask != null) {
      cyclicTask.cancel(false);
      cyclicTask = null;
    }
    cyclicGeneration++;
    cyclicSubmittedGeneration = 0L;
  }

  private void cancelRetryTask() {
    if (retryTask != null) {
      retryTask.cancel(false);
      retryTask = null;
    }
    publishGeneration++;
    pendingRetryGeneration = 0L;
    retryAttempts = 0;
  }

  private void publish(StatusStream stream, PubSubState state) {
    long generation = ++publishGeneration;
    cancelPendingRetryLocked();

    PublisherChannel channel = connection.publisherChannel();
    PublisherId publisherId = connection.config().publisherId();
    if (channel == null || publisherId == null) {
      return;
    }

    boolean cyclic = stream.cyclic() && state == PubSubState.Operational;
    DateTime timestamp = cyclic ? DateTime.now() : null;
    DateTime nextReportTime =
        cyclic ? new DateTime(timestamp.getJavaInstant().plus(CYCLIC_REPORT_INTERVAL)) : null;

    byte[] payload;
    try {
      payload = JsonStatusCodec.encode(publisherId, state, cyclic, timestamp, nextReportTime);
    } catch (Exception e) {
      service
          .getDiagnostics()
          .error(
              connection.path(),
              DiagnosticsCollector.statusCodeOf(e),
              "failed to encode PubSub status message: " + e.getMessage(),
              e,
              PubSubDiagnosticsEvent.Kind.OTHER_ERROR);
      return;
    }

    MessageAddress address =
        MessageAddress.of(
            stream.topic(),
            BrokerTransportQualityOfService.AtLeastOnce,
            true,
            MessageAddress.Kind.STATUS,
            MessageAddress.CONTENT_TYPE_JSON);

    try {
      channel
          .send(Unpooled.wrappedBuffer(payload), address)
          .whenComplete((v, ex) -> onSendComplete(stream, state, generation, ex));
      service.getDiagnostics().networkMessageSent(connection.path());
    } catch (RuntimeException e) {
      if (isCurrentPublish(generation)) {
        service
            .getDiagnostics()
            .error(
                connection.path(),
                DiagnosticsCollector.statusCodeOf(e),
                "failed to send PubSub status message: " + e.getMessage(),
                e,
                PubSubDiagnosticsEvent.Kind.SEND_FAILURE);
        scheduleRetry(stream, state, generation);
      }
    }
  }

  private void onSendComplete(
      StatusStream stream, PubSubState state, long generation, @Nullable Throwable ex) {

    if (ex != null) {
      if (isCurrentPublish(generation)) {
        service
            .getDiagnostics()
            .error(
                connection.path(),
                DiagnosticsCollector.statusCodeOf(ex),
                "failed to send PubSub status message: " + ex.getMessage(),
                ex,
                PubSubDiagnosticsEvent.Kind.SEND_FAILURE);
        scheduleRetry(stream, state, generation);
      }
    } else {
      synchronized (lock) {
        if (!disposed && publishGeneration == generation) {
          retryAttempts = 0;
          cancelPendingRetryLocked();
        }
      }
    }
  }

  private boolean isCurrentPublish(long generation) {
    synchronized (lock) {
      return !disposed && publishGeneration == generation;
    }
  }

  private void cancelPendingRetryLocked() {
    if (retryTask != null) {
      retryTask.cancel(false);
      retryTask = null;
    }
    pendingRetryGeneration = 0L;
  }

  private void scheduleRetry(StatusStream stream, PubSubState state, long generation) {
    synchronized (lock) {
      if (disposed
          || publishGeneration != generation
          || retryTask != null
          || retryAttempts >= MAX_SEND_RETRIES) {
        return;
      }

      int attempt = ++retryAttempts;
      pendingRetryGeneration = generation;
      long delayMillis = RETRY_BASE_DELAY_MILLIS << Math.max(0, attempt - 1);
      try {
        retryTask =
            service
                .getScheduledExecutor()
                .schedule(
                    () -> submitRetry(stream, state, generation),
                    delayMillis,
                    TimeUnit.MILLISECONDS);
      } catch (RejectedExecutionException e) {
        clearRetryIfCurrent(generation);
      }
    }
  }

  private void submitRetry(StatusStream stream, PubSubState state, long generation) {
    try {
      connection.submitToDispatchQueue(() -> retryPublish(stream, state, generation));
    } catch (RejectedExecutionException e) {
      clearRetryIfCurrent(generation);
    }
  }

  private void retryPublish(StatusStream stream, PubSubState state, long generation) {
    synchronized (lock) {
      if (pendingRetryGeneration != generation) {
        return;
      }
      retryTask = null;
      pendingRetryGeneration = 0L;
      if (!disposed && publishGeneration == generation && connection.state() == state) {
        publish(stream, state);
      }
    }
  }

  private void clearRetryIfCurrent(long generation) {
    synchronized (lock) {
      if (pendingRetryGeneration == generation) {
        retryTask = null;
        pendingRetryGeneration = 0L;
      }
    }
  }

  private @Nullable StatusStream statusStream() {
    PublisherStatusMode mode = publisherStatusMode(connection.config());
    if (mode == PublisherStatusMode.DISABLED) {
      return null;
    }

    PublisherId publisherId = connection.config().publisherId();
    if (publisherId == null) {
      return null;
    }

    Set<String> configuredMappings =
        configuredWriterGroupMappings(connection.config().writerGroups());
    Set<String> activeMappings = writerGroupMappings(connection.writerGroupRuntimes());

    Set<String> mappings = activeMappings.isEmpty() ? configuredMappings : activeMappings;
    if (mappings.isEmpty()) {
      return null;
    }
    if (!mappings.contains(PubSubServiceImpl.MAPPING_JSON)) {
      return null;
    }

    boolean willSelected =
        mode == PublisherStatusMode.WILL
            || (mode == PublisherStatusMode.AUTO
                && configuredMappings.size() == 1
                && configuredMappings.contains(PubSubServiceImpl.MAPPING_JSON));
    boolean cyclic = mode == PublisherStatusMode.CYCLIC || !willSelected;
    String topic =
        BrokerTopics.statusTopic(
            BrokerTopics.topicPrefix(connection.config()),
            PubSubServiceImpl.MAPPING_JSON,
            publisherId);

    return new StatusStream(topic, cyclic);
  }

  private static PublisherStatusMode publisherStatusMode(PubSubConnectionConfig connection) {
    if (connection instanceof MqttConnectionConfig mqttConnection) {
      return mqttConnection.getPublisherStatusMode();
    }
    return PublisherStatusMode.AUTO;
  }

  private static Set<String> writerGroupMappings(Iterable<WriterGroupRuntime> groups) {
    var mappings = new LinkedHashSet<String>();
    for (WriterGroupRuntime group : groups) {
      if (group.state() != PubSubState.Disabled) {
        mappings.add(group.mappingName());
      }
    }
    return Set.copyOf(mappings);
  }

  private static Set<String> configuredWriterGroupMappings(Iterable<WriterGroupConfig> groups) {
    var mappings = new LinkedHashSet<String>();
    for (WriterGroupConfig group : groups) {
      mappings.add(PubSubServiceImpl.mappingNameOf(group.getMessageSettings()));
    }
    return Set.copyOf(mappings);
  }

  private record StatusStream(String topic, boolean cyclic) {}
}
