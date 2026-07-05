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

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubDiagnosticsEvent;
import org.eclipse.milo.opcua.sdk.pubsub.PubSubHandle;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerTransportSettings;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.transport.BrokerQualityOfService;
import org.eclipse.milo.opcua.sdk.pubsub.transport.BrokerTopics;
import org.eclipse.milo.opcua.sdk.pubsub.transport.MessageAddress;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.EncodedNetworkMessage;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.MessageMappingProvider;
import org.eclipse.milo.opcua.sdk.pubsub.uadp.MetaDataEncodeContext;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UShort;
import org.eclipse.milo.opcua.stack.core.types.enumerated.PubSubState;
import org.eclipse.milo.opcua.stack.core.types.structured.DataSetMetaDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DataSetMetaData publication for one broker connection (OPC UA Part 14 §6.4.2.5.5/.6, §5.2.3),
 * owned by the connection's {@link ConnectionRuntime}; UDP connections have none (UADP discovery
 * handles metadata there).
 *
 * <p>Per writer, retained metadata is published to the writer's metadata queue — the configured
 * {@code metaDataQueueName} (writer level over group level) or the Part 14 §7.3.4.7.4 derived topic
 * — encoded by the group's message mapping via {@link
 * MessageMappingProvider#encodeMetaData(MetaDataEncodeContext, DataSetMetaDataType, UShort)}:
 *
 * <ul>
 *   <li>once when the writer activates (startup, enable, reconfigure restart) — initiated before
 *       the writer can contribute data DataSetMessages, satisfying the §5.2.3 metadata-before-data
 *       ordering best-effort;
 *   <li>once when a reconfiguration changes the metadata of a live writer's dataset without
 *       restarting the writer (safety net; dataset changes normally restart referencing writers);
 *   <li>periodically when the effective {@code MetaDataUpdateTime} is positive (writer-level
 *       settings over group-level, which is Milo-local); zero means on-change only, relying on the
 *       retain flag for infinite retention.
 * </ul>
 *
 * <p>The {@link #lastPublished} on-change baseline records only <i>confirmed</i> sends: a failed
 * send leaves the baseline untouched and is retried with bounded backoff until the first success,
 * covering the common case of the activation publish racing the transport's asynchronous broker
 * connect (broker channels fail fast until connected). After the bounded retries are exhausted the
 * periodic task and the reconfigure on-change check remain as retry opportunities. Broker
 * reconnects republish through the activation hook: a broker outage reported via {@code
 * TransportStateListener} fails the connection, deactivating its writers (clearing their
 * baselines), and the recovery on reconnect reactivates them, republishing every writer's retained
 * metadata.
 *
 * <p>Sequence numbers come from the service's per-PublisherId announcement counter (Part 14
 * §7.2.4.6.3 Table 168 scope); a stream of one writer's metadata messages is strictly increasing
 * but may have gaps where other writers consumed values.
 *
 * <p><b>Threading:</b> activation/deactivation hooks run under the engine lock; periodic timers and
 * retries are scheduled on the service scheduled executor but hop immediately to the connection's
 * dispatch queue, matching the reconfigure check. Mutable state is guarded by this publisher's own
 * lock; the engine lock is never acquired from inside it. Sends are initiated inline while holding
 * these locks, which is safe only because {@link PublisherChannel#send(io.netty.buffer.ByteBuf,
 * MessageAddress)} pins a non-blocking contract on implementations. Publication failures are
 * recorded in diagnostics, never thrown: metadata publication is auxiliary and must not fail the
 * writer.
 */
final class MetaDataPublisher {

  private static final Logger LOGGER = LoggerFactory.getLogger(MetaDataPublisher.class);

  /** Bounded retry of failed sends: 1, 2, 4, 8, 16 s, then give up until the next trigger. */
  private static final int MAX_SEND_RETRIES = 5;

  private static final long RETRY_BASE_DELAY_MILLIS = 1_000;

  private final PubSubServiceImpl service;
  private final ConnectionRuntime connection;

  private final Object lock = new Object();

  private volatile boolean disposed = false;

  /** Periodic publication tasks per writer. Guarded by {@link #lock}. */
  private final Map<PubSubHandle, ScheduledFuture<?>> periodicTasks = new HashMap<>();

  /** Periodic publication activation generation per writer path. Guarded by {@link #lock}. */
  private final Map<String, Long> periodicGenerations = new HashMap<>();

  /** Queued/running periodic publication generation per writer path. Guarded by {@link #lock}. */
  private final Map<String, Long> periodicSubmissions = new HashMap<>();

  /**
   * The metadata last <i>successfully</i> published per writer path, by-change comparison. Updated
   * only when a send confirms success, so failed sends are retried rather than recorded. Guarded by
   * {@link #lock}.
   */
  private final Map<String, DataSetMetaDataType> lastPublished = new HashMap<>();

  /** Pending failed-send retry tasks per writer path. Guarded by {@link #lock}. */
  private final Map<String, ScheduledFuture<?>> retryTasks = new HashMap<>();

  /** Pending failed-send retry generation per writer path. Guarded by {@link #lock}. */
  private final Map<String, Long> retryGenerations = new HashMap<>();

  /** Failed-send retries already scheduled per writer path. Guarded by {@link #lock}. */
  private final Map<String, Integer> retryAttempts = new HashMap<>();

  /** Guarded by {@link #lock}. */
  private long nextTimerGeneration = 0L;

  MetaDataPublisher(PubSubServiceImpl service, ConnectionRuntime connection) {
    this.service = service;
    this.connection = connection;
  }

  /**
   * Publish the writer's retained metadata and start its periodic publication task when an update
   * time is configured. Called under the engine lock when the writer activates; the group has
   * already activated, so its mapping is resolved and the publisher channel is open.
   */
  void onWriterActivated(WriterGroupRuntime group, DataSetWriterRuntime writer) {
    synchronized (lock) {
      if (disposed) {
        return;
      }

      publish(group, writer);

      Duration updateTime = effectiveMetaDataUpdateTime(group, writer);
      if (updateTime.compareTo(Duration.ZERO) > 0 && !periodicTasks.containsKey(writer.handle())) {
        long generation = ++nextTimerGeneration;
        periodicGenerations.put(writer.path(), generation);
        long periodNanos = updateTime.toNanos();
        ScheduledFuture<?> task =
            service
                .getScheduledExecutor()
                .scheduleAtFixedRate(
                    () -> submitPublishPeriodic(group, writer, generation),
                    periodNanos,
                    periodNanos,
                    TimeUnit.NANOSECONDS);
        periodicTasks.put(writer.handle(), task);
      }
    }
  }

  /**
   * Stop the writer's periodic publication and pending retry tasks. Called under the engine lock.
   */
  void onWriterDeactivated(DataSetWriterRuntime writer) {
    synchronized (lock) {
      ScheduledFuture<?> task = periodicTasks.remove(writer.handle());
      if (task != null) {
        task.cancel(false);
      }
      periodicGenerations.remove(writer.path());
      periodicSubmissions.remove(writer.path());
      ScheduledFuture<?> retryTask = retryTasks.remove(writer.path());
      if (retryTask != null) {
        retryTask.cancel(false);
      }
      retryGenerations.remove(writer.path());
      retryAttempts.remove(writer.path());
      lastPublished.remove(writer.path());
    }
  }

  /**
   * Schedule the on-change check after a reconfiguration: metadata of a live writer's dataset that
   * changed without a writer restart is republished once, best-effort before the changed data
   * (§5.2.3). Called under the engine lock; the check itself runs on the connection's dispatch
   * queue, off the engine lock.
   */
  void onConfigurationApplied() {
    try {
      connection.submitToDispatchQueue(this::publishChangedMetaData);
    } catch (RejectedExecutionException e) {
      // executor shut down; nothing to publish
    }
  }

  /** Release all resources of this publisher. The publisher is unusable afterwards. */
  void dispose() {
    synchronized (lock) {
      disposed = true;

      periodicTasks.values().forEach(task -> task.cancel(false));
      periodicTasks.clear();
      periodicGenerations.clear();
      periodicSubmissions.clear();
      retryTasks.values().forEach(task -> task.cancel(false));
      retryTasks.clear();
      retryGenerations.clear();
      retryAttempts.clear();
      lastPublished.clear();
    }
  }

  private void submitPublishPeriodic(
      WriterGroupRuntime group, DataSetWriterRuntime writer, long generation) {

    String writerPath = writer.path();
    synchronized (lock) {
      if (disposed
          || !isActive(writer.state())
          || periodicGenerations.getOrDefault(writerPath, 0L) != generation
          || periodicSubmissions.containsKey(writerPath)) {
        return;
      }
      periodicSubmissions.put(writerPath, generation);
    }

    try {
      connection.submitToDispatchQueue(() -> publishPeriodic(group, writer, generation));
    } catch (RejectedExecutionException e) {
      clearPeriodicSubmissionIfCurrent(writerPath, generation);
    }
  }

  /** One periodic publication; runs on the connection's dispatch queue. */
  private void publishPeriodic(
      WriterGroupRuntime group, DataSetWriterRuntime writer, long generation) {

    String writerPath = writer.path();
    synchronized (lock) {
      try {
        if (!disposed
            && isActive(writer.state())
            && periodicGenerations.getOrDefault(writerPath, 0L) == generation) {
          publish(group, writer);
        }
      } finally {
        clearPeriodicSubmissionIfCurrentLocked(writerPath, generation);
      }
    }
  }

  private void clearPeriodicSubmissionIfCurrent(String writerPath, long generation) {
    synchronized (lock) {
      clearPeriodicSubmissionIfCurrentLocked(writerPath, generation);
    }
  }

  private void clearPeriodicSubmissionIfCurrentLocked(String writerPath, long generation) {
    if (periodicSubmissions.getOrDefault(writerPath, 0L) == generation) {
      periodicSubmissions.remove(writerPath);
    }
  }

  /** The reconfigure on-change check; runs on the connection's dispatch queue. */
  private void publishChangedMetaData() {
    var changed = new ArrayList<DataSetWriterRuntime>();
    var groups = new ArrayList<WriterGroupRuntime>();

    synchronized (lock) {
      if (disposed) {
        return;
      }

      for (WriterGroupRuntime group : connection.writerGroupRuntimes()) {
        for (DataSetWriterRuntime writer : group.writerRuntimes()) {
          if (isActive(writer.state())
              && !writer.metaData().equals(lastPublished.get(writer.path()))) {
            changed.add(writer);
            groups.add(group);
          }
        }
      }

      for (int i = 0; i < changed.size(); i++) {
        publish(groups.get(i), changed.get(i));
      }
    }
  }

  /**
   * Encode and send one retained metadata message for {@code writer}. Guarded by {@link #lock}
   * (callers hold it). Failures are recorded in diagnostics, never thrown; failed sends schedule a
   * bounded retry and leave the {@link #lastPublished} baseline untouched, which is updated (and
   * the sent counter ticked) only when the send confirms success.
   */
  private void publish(WriterGroupRuntime group, DataSetWriterRuntime writer) {
    PublisherChannel channel = connection.publisherChannel();
    MessageMappingProvider mapping = group.mapping();
    PublisherId publisherId = connection.config().publisherId();

    if (channel == null || mapping == null || publisherId == null) {
      LOGGER.debug(
          "metadata publication for '{}' skipped: channel/mapping/publisherId unavailable",
          writer.path());
      return;
    }

    UShort sequenceNumber = service.nextAnnouncementSequenceNumber(publisherId);

    String writerPath = writer.path();
    DataSetMetaDataType metaData = writer.metaData();

    EncodedNetworkMessage encoded;
    try {
      encoded =
          mapping.encodeMetaData(
              MetaDataEncodeContext.of(
                  service.getEncodingContext(), publisherId, group.config(), writer.config()),
              metaData,
              sequenceNumber);
    } catch (Exception e) {
      service
          .getDiagnostics()
          .error(
              writerPath,
              DiagnosticsCollector.statusCodeOf(e),
              "failed to encode DataSetMetaData message: " + e.getMessage(),
              e,
              PubSubDiagnosticsEvent.Kind.OTHER_ERROR);
      return;
    }

    MessageAddress address;
    try {
      address =
          MessageAddress.of(
              BrokerTopics.resolveMetaDataQueueName(
                  connection.config(), group.mappingName(), group.config(), writer.config()),
              BrokerQualityOfService.resolveMetaData(
                  group.config().getBrokerTransport(), writer.config().getBrokerTransport()),
              true,
              MessageAddress.Kind.METADATA,
              MessageAddress.contentTypeOfMapping(group.mappingName()));
    } catch (RuntimeException e) {
      // the encoded buffer has not been handed to the channel yet; it is ours to release
      encoded.data().release();
      service
          .getDiagnostics()
          .error(
              writerPath,
              new StatusCode(StatusCodes.Bad_ConfigurationError),
              "failed to resolve DataSetMetaData address: " + e.getMessage(),
              e,
              PubSubDiagnosticsEvent.Kind.OTHER_ERROR);
      return;
    }

    try {
      channel
          .send(encoded.data(), address)
          .whenComplete(
              (v, ex) -> {
                if (ex != null) {
                  // suppressed as teardown noise when the publisher is disposed or the writer
                  // no longer active — the same guard scheduleRetry applies; the retry itself
                  // is skipped too
                  if (!suppressSendFailure(writer)) {
                    service
                        .getDiagnostics()
                        .error(
                            writerPath,
                            DiagnosticsCollector.statusCodeOf(ex),
                            "failed to send DataSetMetaData message: " + ex.getMessage(),
                            ex,
                            PubSubDiagnosticsEvent.Kind.SEND_FAILURE);
                    scheduleRetry(group, writer);
                  }
                } else {
                  synchronized (lock) {
                    if (!disposed) {
                      retryAttempts.remove(writerPath);
                      retryGenerations.remove(writerPath);
                      retryTasks.remove(writerPath);
                      lastPublished.put(writerPath, metaData);
                    }
                  }
                }
              });

      // the message was handed to the channel: count it sent at the connection path, matching
      // WriterGroupRuntime and DiscoveryRuntime hand-off behavior; metadata
      // messages never tick writer-group counters
      service.getDiagnostics().networkMessageSent(connection.path());
    } catch (RuntimeException e) {
      // a synchronous send failure leaves ownership of the in-flight buffer ambiguous (never
      // double-release it; conforming channels release on every path, see PublisherChannel#send);
      // diagnose and retry like an asynchronous failure — the retained publish is idempotent
      if (!suppressSendFailure(writer)) {
        service
            .getDiagnostics()
            .error(
                writerPath,
                DiagnosticsCollector.statusCodeOf(e),
                "failed to send DataSetMetaData message: " + e.getMessage(),
                e,
                PubSubDiagnosticsEvent.Kind.SEND_FAILURE);
        scheduleRetry(group, writer);
      }
    }
  }

  /**
   * Whether a send failure is teardown noise: the publisher is disposed or the writer left the
   * active states (clean shutdown, disable, reconfigure restart) — the exact guard {@link
   * #scheduleRetry} uses. Suppression skips the diagnostics event AND the retry.
   */
  private boolean suppressSendFailure(DataSetWriterRuntime writer) {
    return disposed || !isActive(writer.state());
  }

  /**
   * Schedule a bounded, backed-off retry after a failed send: at most {@link #MAX_SEND_RETRIES}
   * retries per writer (the counter resets on the first success and on writer deactivation), with
   * at most one retry pending at a time. May be called holding {@link #lock} (synchronous send
   * failure) or not (asynchronous completion).
   */
  private void scheduleRetry(WriterGroupRuntime group, DataSetWriterRuntime writer) {
    synchronized (lock) {
      if (disposed || !isActive(writer.state())) {
        return;
      }
      String writerPath = writer.path();
      if (retryTasks.containsKey(writerPath)) {
        return;
      }
      int attempt = retryAttempts.getOrDefault(writerPath, 0);
      if (attempt >= MAX_SEND_RETRIES) {
        return;
      }
      retryAttempts.put(writerPath, attempt + 1);
      long generation = ++nextTimerGeneration;
      retryGenerations.put(writerPath, generation);
      try {
        ScheduledFuture<?> task =
            service
                .getScheduledExecutor()
                .schedule(
                    () -> submitRetryPublish(group, writer, generation),
                    RETRY_BASE_DELAY_MILLIS << attempt,
                    TimeUnit.MILLISECONDS);
        retryTasks.put(writerPath, task);
      } catch (RejectedExecutionException e) {
        retryGenerations.remove(writerPath);
      }
    }
  }

  private void submitRetryPublish(
      WriterGroupRuntime group, DataSetWriterRuntime writer, long generation) {

    try {
      connection.submitToDispatchQueue(() -> retryPublish(group, writer, generation));
    } catch (RejectedExecutionException e) {
      clearRetryIfCurrent(writer.path(), generation);
    }
  }

  /** One failed-send retry; runs on the connection's dispatch queue. */
  private void retryPublish(
      WriterGroupRuntime group, DataSetWriterRuntime writer, long generation) {
    synchronized (lock) {
      String writerPath = writer.path();
      if (retryGenerations.getOrDefault(writerPath, 0L) != generation) {
        return;
      }
      retryTasks.remove(writerPath);
      retryGenerations.remove(writerPath);
      if (disposed || !isActive(writer.state())) {
        return;
      }
      publish(group, writer);
    }
  }

  private void clearRetryIfCurrent(String writerPath, long generation) {
    synchronized (lock) {
      if (retryGenerations.getOrDefault(writerPath, 0L) == generation) {
        retryTasks.remove(writerPath);
        retryGenerations.remove(writerPath);
      }
    }
  }

  /**
   * The effective metadata update time: the writer-level settings' value when present, otherwise
   * the group-level (Milo-local) value, otherwise zero (= on-change only).
   */
  private static Duration effectiveMetaDataUpdateTime(
      WriterGroupRuntime group, DataSetWriterRuntime writer) {

    BrokerTransportSettings writerSettings = writer.config().getBrokerTransport();
    if (writerSettings != null) {
      return writerSettings.getMetaDataUpdateTime();
    }
    BrokerTransportSettings groupSettings = group.config().getBrokerTransport();
    if (groupSettings != null) {
      return groupSettings.getMetaDataUpdateTime();
    }
    return Duration.ZERO;
  }

  private static boolean isActive(PubSubState state) {
    return state == PubSubState.PreOperational
        || state == PubSubState.Operational
        || state == PubSubState.Error;
  }
}
