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

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.MqttClientBuilderBase;
import com.hivemq.client.mqtt.MqttClientExecutorConfig;
import com.hivemq.client.mqtt.MqttClientSslConfig;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.lifecycle.MqttClientConnectedContext;
import com.hivemq.client.mqtt.lifecycle.MqttClientDisconnectedContext;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3ClientBuilder;
import com.hivemq.client.mqtt.mqtt3.message.auth.Mqtt3SimpleAuth;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5ClientBuilder;
import com.hivemq.client.mqtt.mqtt5.exceptions.Mqtt5ConnAckException;
import com.hivemq.client.mqtt.mqtt5.message.auth.Mqtt5SimpleAuth;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAckReasonCode;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishBuilder;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishResult;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MqttConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PublisherId;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportStateListener;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.BrokerTransportQualityOfService;
import org.jspecify.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * One HiveMQ MQTT client per PubSub connection, shared by the connection's publisher and subscriber
 * channels and reference counted by {@link MqttTransportProvider}.
 *
 * <p>Version negotiation per Part 14 §7.3.4.4 and the {@code Protocol PubSub MQTT V5} conformance
 * unit: the default {@code BestAvailable} mode connects with MQTT 5.0 and falls back to MQTT 3.1.1
 * when the broker answers CONNECT with "Unsupported Protocol Version"; the {@code 0:MqttVersion}
 * connection property pins {@code "5.0"} or {@code "3.1.1"} explicitly.
 *
 * <p>Reconnect: the client reconnects automatically with the HiveMQ default backoff (1 s initial,
 * doubled per attempt, capped at 120 s, ±25% jitter). On every (re)connect the session explicitly
 * re-issues all registered subscriptions — MQTT 5 sessions are created with the default session
 * expiry of 0 and MQTT 3.1.1 sessions with clean session, so explicit resubscription keeps both
 * versions uniform. HiveMQ's own resubscribe-on-reconnect is disabled: it would retain the previous
 * connection's subscribed-publish flows alongside the explicitly re-issued ones, delivering each
 * message once per flow. Sends while disconnected fail fast and surface through the engine's
 * send-failure diagnostics.
 *
 * <p>Transport state: channels register {@link TransportStateListener}s with the session. A
 * disconnect of a previously connected session notifies {@code onTransportDown} with {@code
 * Bad_ServerNotConnected} (a session that never connected is not an outage, so first-connect
 * retries and the BestAvailable fallback churn stay silent); every (re)connect notifies {@code
 * onTransportUp} once the re-issued subscriptions have settled. Nothing is notified after {@link
 * #disconnect()}.
 *
 * <p>Threading: all client operations are asynchronous and non-blocking; mutable session state
 * (subscriptions, the active client adapter) is guarded by {@code lock}, which is never held across
 * I/O. Transport state notifications are delivered under a dedicated monitor ({@code
 * notificationLock}) that keeps down and up edges in a consistent order across the HiveMQ callback
 * threads: a stale up edge is never delivered after the down edge that made it stale.
 */
final class MqttClientSession {

  private static final Logger LOGGER = LoggerFactory.getLogger(MqttClientSession.class);

  /** The MQTT version negotiation modes of Part 14 §7.3.4.4 Table 201. */
  enum VersionMode {
    BEST_AVAILABLE,
    MQTT_5,
    MQTT_3_1_1
  }

  /**
   * One topic-filter subscription registered by the subscriber channel: re-issued by the session on
   * every (re)connect.
   *
   * @param topicFilter the MQTT topic filter.
   * @param qos the subscription QoS.
   * @param consumer the consumer invoked with (topic, payload) for every received publish.
   * @param executor the executor the consumer is invoked on.
   */
  record SubscriptionEntry(
      String topicFilter, MqttQos qos, BiConsumer<String, byte[]> consumer, Executor executor) {}

  private final Object lock = new Object();

  /**
   * Serializes transport-state notification delivery and makes the up edge's stale-check atomic
   * with its delivery: {@link #onDisconnected} clears {@code connected} (under {@link #lock})
   * before taking this monitor to deliver the down edge, so an up edge that reads {@code connected}
   * under this monitor either observes the clear and is dropped as stale, or is delivered before
   * the racing down edge — never after it, which would "recover" a connection whose broker is still
   * down. Lock ordering: this monitor may be taken around {@link #lock}, never the reverse;
   * listeners are quick by contract (they only hand off to their own executor), so no I/O runs
   * while it is held.
   */
  private final Object notificationLock = new Object();

  /** Subscriptions to (re-)issue on every connect. Guarded by {@link #lock}. */
  private final List<SubscriptionEntry> subscriptions = new ArrayList<>();

  /**
   * Transport state listeners registered by the channels; notified outside {@link #lock}. Both
   * channels of a connection may register the same instance — removal drops one occurrence, giving
   * refcount-like behavior, and duplicate notifications are listener-tolerated by contract.
   */
  private final List<TransportStateListener> transportStateListeners = new CopyOnWriteArrayList<>();

  /** The active client; swapped at most once, by the BestAvailable 3.1.1 fallback. */
  private volatile ClientAdapter adapter;

  /** Set once {@link #disconnect()} has been called; stops reconnects and resubscriptions. */
  private volatile boolean closed = false;

  /**
   * Whether {@link #onConnected} has run for the current connection (cleared again by {@link
   * #onDisconnected}). Subscription issuance decisions key on this flag, made and acted on under
   * {@link #lock} — not on the adapter's own state, which turns connected before the connected
   * listener runs and would let {@link #addSubscriptions(List)} and {@link #onConnected} both issue
   * the same entry for the same connection. Guarded by {@link #lock}.
   */
  private boolean connected = false;

  /** Whether the BestAvailable fallback to MQTT 3.1.1 has been taken. Guarded by {@link #lock}. */
  private boolean fellBack = false;

  private final MqttConnectionConfig config;
  private final MqttBrokerAddress brokerAddress;
  private final VersionMode versionMode;
  private final String clientId;
  private final MqttClientExecutorConfig executorConfig;
  private final @Nullable MqttClientSslConfig sslConfig;
  private final @Nullable Mqtt5SimpleAuth simpleAuth5;
  private final @Nullable Mqtt3SimpleAuth simpleAuth3;

  private MqttClientSession(MqttConnectionConfig config, Executor nettyExecutor)
      throws UaException {

    this.config = config;

    brokerAddress = MqttBrokerAddress.parse(config);
    versionMode = versionMode(config);
    clientId = clientId(config);

    executorConfig = MqttClientExecutorConfig.builder().nettyExecutor(nettyExecutor).build();

    BrokerSecurityConfig security = config.getBrokerSecurity();

    sslConfig = brokerAddress.tls() ? MqttTlsSupport.sslConfig(security) : null;

    String username = security != null ? security.getUsername() : null;
    char[] password = security != null ? security.getPassword() : null;
    byte[] passwordBytes = password != null ? passwordBytes(password) : null;

    simpleAuth5 = simpleAuth5(username, passwordBytes);
    simpleAuth3 = simpleAuth3(username, passwordBytes);

    if (versionMode == VersionMode.MQTT_3_1_1 && username == null && passwordBytes != null) {
      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          "connection '%s': MQTT 3.1.1 requires a username when a password is configured"
              .formatted(config.name()));
    }

    adapter = versionMode == VersionMode.MQTT_3_1_1 ? new Mqtt3Adapter() : new Mqtt5Adapter();
  }

  /**
   * Create a session for {@code config}; the session is configured but not yet connected.
   *
   * @param config the connection config.
   * @param nettyExecutor the executor HiveMQ uses for Netty I/O (never shut down by the client).
   * @return a new {@link MqttClientSession}.
   * @throws UaException if the broker URI, connection properties, or security material are invalid.
   */
  static MqttClientSession create(MqttConnectionConfig config, Executor nettyExecutor)
      throws UaException {
    return new MqttClientSession(config, nettyExecutor);
  }

  MqttConnectionConfig config() {
    return config;
  }

  /** Initiate the (async) initial connect; failures are retried by automatic reconnect. */
  void start() {
    LOGGER.debug(
        "connection '{}': connecting to {}:{} (clientId={}, mode={}, tls={})",
        config.name(),
        brokerAddress.host(),
        brokerAddress.port(),
        clientId,
        versionMode,
        brokerAddress.tls());

    connectAdapter(adapter);
  }

  /**
   * Publish one message, failing fast when the client is not currently connected.
   *
   * <p>The connected check is load-bearing, not just a fast error path: the HiveMQ client's "async"
   * publish blocks the <i>calling</i> thread until the client's first successful connect ({@code
   * MqttPublishFlowables.add} waits for the outgoing publish flow to be subscribed, which happens
   * at connect). The engine publishes from its shared scheduled executor, which must never block on
   * broker availability.
   *
   * @return a future that completes when the broker (QoS &gt; 0) or the transport (QoS 0) has
   *     accepted the message, or completes exceptionally on failure or while disconnected.
   */
  CompletableFuture<Void> publish(
      String topic, MqttQos qos, boolean retain, @Nullable String contentType, byte[] payload) {

    ClientAdapter adapter = this.adapter;

    if (!adapter.isConnected()) {
      return CompletableFuture.failedFuture(
          new UaException(
              StatusCodes.Bad_ServerNotConnected,
              "connection '%s': not connected to the broker".formatted(config.name())));
    }

    return adapter.publish(topic, qos, retain, contentType, payload);
  }

  /**
   * Register subscriptions and issue them if the client is currently connected; they are re-issued
   * on every subsequent (re)connect.
   */
  void addSubscriptions(List<SubscriptionEntry> entries) {
    ClientAdapter adapter;
    boolean issueNow;
    synchronized (lock) {
      // the issue-now decision is atomic with onConnected's snapshot: entries registered
      // before onConnected snapshots are issued there only, entries registered after are
      // issued here only — never both for the same connection
      subscriptions.addAll(entries);
      adapter = this.adapter;
      issueNow = connected;
    }
    if (issueNow) {
      entries.forEach(entry -> subscribeEntry(adapter, entry));
    }
  }

  /** Unregister subscriptions and unsubscribe their topic filters, best-effort. */
  void removeSubscriptions(List<SubscriptionEntry> entries) {
    ClientAdapter adapter;
    boolean connected;
    synchronized (lock) {
      subscriptions.removeAll(entries);
      adapter = this.adapter;
      connected = this.connected;
    }
    if (!connected) {
      // nothing to unsubscribe: a (re)connect starts a clean broker session and the entries
      // are no longer registered for resubscription
      return;
    }
    for (SubscriptionEntry entry : entries) {
      adapter
          .unsubscribe(entry.topicFilter())
          .whenComplete(
              (v, ex) -> {
                if (ex != null) {
                  LOGGER.debug(
                      "connection '{}': unsubscribe from '{}' failed: {}",
                      config.name(),
                      entry.topicFilter(),
                      ex.getMessage());
                }
              });
    }
  }

  /**
   * Register a listener notified about the liveness of this session's broker connection. Channels
   * register their context's listener on open and remove it on close.
   */
  void addTransportStateListener(TransportStateListener listener) {
    transportStateListeners.add(listener);
  }

  /** Remove one registration of {@code listener}; removing a never-added listener is a no-op. */
  void removeTransportStateListener(TransportStateListener listener) {
    transportStateListeners.remove(listener);
  }

  /**
   * Disconnect from the broker and stop reconnecting. A failure to disconnect (e.g. the client was
   * never connected) completes the returned future normally.
   */
  CompletableFuture<Void> disconnect() {
    ClientAdapter adapter;
    synchronized (lock) {
      // the close mark and the adapter read are atomic with the BestAvailable fallback's
      // closed-check-then-swap, so a concurrent fallback either aborts or its replacement
      // adapter is the one disconnected here
      closed = true;
      adapter = this.adapter;
    }

    return adapter
        .disconnect()
        .handle(
            (v, ex) -> {
              if (ex != null) {
                LOGGER.debug(
                    "connection '{}': disconnect completed exceptionally: {}",
                    config.name(),
                    ex.getMessage());
              }
              return null;
            });
  }

  private void connectAdapter(ClientAdapter adapter) {
    adapter
        .connect()
        .whenComplete(
            (v, ex) -> {
              if (ex == null) {
                LOGGER.debug("connection '{}': connected to broker", config.name());
              } else if (!closed) {
                // automatic reconnect retries; BestAvailable fallback is handled by the
                // disconnected listener
                LOGGER.debug("connection '{}': connect failed: {}", config.name(), ex.getMessage());
              }
            });
  }

  /** Re-issue every registered subscription; invoked by HiveMQ on every (re)connect. */
  private void onConnected(MqttClientConnectedContext context) {
    boolean closed;
    List<SubscriptionEntry> entries;
    ClientAdapter adapter;
    synchronized (lock) {
      closed = this.closed;
      if (!closed) {
        connected = true;
      }
      entries = List.copyOf(subscriptions);
      adapter = this.adapter;
    }

    if (closed) {
      // disconnect() neither cancels an in-flight CONNECT nor does anything before one
      // completes, so a session closed while connecting would otherwise stay connected
      // forever; converge to disconnected now that the connect has resolved
      LOGGER.debug("connection '{}': connected after close; disconnecting", config.name());
      adapter
          .disconnect()
          .whenComplete(
              (v, ex) -> {
                if (ex != null) {
                  LOGGER.debug(
                      "connection '{}': disconnect after close failed: {}",
                      config.name(),
                      ex.getMessage());
                }
              });
      return;
    }

    LOGGER.info(
        "connection '{}': connected to {}:{}; subscribing {} topic filter(s)",
        config.name(),
        brokerAddress.host(),
        brokerAddress.port(),
        entries.size());

    // the up edge honors "reconnect => resubscribe => Operational" causally: it is notified
    // once every re-issued subscription has settled (failures included, logged by
    // subscribeEntry); with no entries it is notified immediately
    CompletableFuture<?>[] resubscriptions =
        entries.stream()
            .map(entry -> subscribeEntry(adapter, entry).handle((v, ex) -> null))
            .toArray(CompletableFuture[]::new);

    CompletableFuture.allOf(resubscriptions)
        .whenComplete(
            (v, ex) -> {
              // the up edge is connected-gated, symmetric with the down edge: if the broker
              // dropped the session (or it was closed) while the resubscriptions were in
              // flight, onDisconnected delivers the down edge and this pending up edge is
              // stale — delivering it after the down edge would recover a connection whose
              // broker is still down; notificationLock makes the gate atomic with delivery,
              // so the up edge is either dropped here or delivered before the racing down
              // edge, never after it; a later real reconnect produces its own edge
              synchronized (notificationLock) {
                boolean stillConnected;
                synchronized (lock) {
                  stillConnected = connected && !this.closed;
                }
                if (stillConnected) {
                  notifyTransportUp();
                } else {
                  LOGGER.debug("connection '{}': dropping stale transport-up edge", config.name());
                }
              }
            });
  }

  private void notifyTransportUp() {
    for (TransportStateListener listener : transportStateListeners) {
      try {
        listener.onTransportUp();
      } catch (RuntimeException e) {
        LOGGER.warn("connection '{}': transport state listener failed", config.name(), e);
      }
    }
  }

  private void notifyTransportDown(StatusCode statusCode) {
    for (TransportStateListener listener : transportStateListeners) {
      try {
        listener.onTransportDown(statusCode);
      } catch (RuntimeException e) {
        LOGGER.warn("connection '{}': transport state listener failed", config.name(), e);
      }
    }
  }

  private void onDisconnected(MqttClientDisconnectedContext context) {
    boolean wasConnected;
    synchronized (lock) {
      wasConnected = connected;
      connected = false;
    }

    // HiveMQ would otherwise retain this connection's subscribed-publish flows across the
    // (expired) session and auto-resubscribe them on reconnect; onConnected re-issues every
    // registered subscription itself, and a retained flow plus a re-issued one would deliver
    // each matching message twice
    context.getReconnector().resubscribeIfSessionExpired(false);

    if (closed) {
      context.getReconnector().reconnect(false);
      return;
    }

    Throwable cause = context.getCause();

    if (versionMode == VersionMode.BEST_AVAILABLE && isUnsupportedProtocolVersion(cause)) {
      boolean fallBack = false;
      synchronized (lock) {
        if (!fellBack) {
          fellBack = true;
          fallBack = true;
        }
      }
      if (fallBack) {
        context.getReconnector().reconnect(false);
        LOGGER.info(
            "connection '{}': broker does not support MQTT 5.0; falling back to MQTT 3.1.1",
            config.name());
        fallBackToMqtt311();
        return;
      }
    }

    LOGGER.warn(
        "connection '{}': disconnected from {}:{} ({}): {}",
        config.name(),
        brokerAddress.host(),
        brokerAddress.port(),
        context.getSource(),
        cause.getMessage());

    // the down edge is connected-gated: a session that never connected is not an outage,
    // so first-connect retries stay silent; notified outside the session lock but under
    // notificationLock — connected was cleared above, so a pending up edge that has not yet
    // been delivered is guaranteed to observe the clear and drop itself as stale
    if (wasConnected) {
      synchronized (notificationLock) {
        notifyTransportDown(new StatusCode(StatusCodes.Bad_ServerNotConnected));
      }
    }
  }

  private void fallBackToMqtt311() {
    if (simpleAuth5 != null && simpleAuth3 == null) {
      LOGGER.error(
          "connection '{}': cannot fall back to MQTT 3.1.1: a password without a username is"
              + " configured, which MQTT 3.1.1 does not support",
          config.name());
      return;
    }

    ClientAdapter newAdapter = new Mqtt3Adapter();
    synchronized (lock) {
      if (closed) {
        return;
      }
      adapter = newAdapter;
    }
    connectAdapter(newAdapter);
  }

  private CompletableFuture<Void> subscribeEntry(ClientAdapter adapter, SubscriptionEntry entry) {
    return adapter
        .subscribe(entry)
        .whenComplete(
            (v, ex) -> {
              if (ex != null) {
                LOGGER.warn(
                    "connection '{}': subscribe to '{}' failed: {}",
                    config.name(),
                    entry.topicFilter(),
                    ex.getMessage());
              } else {
                LOGGER.debug(
                    "connection '{}': subscribed to '{}' at {}",
                    config.name(),
                    entry.topicFilter(),
                    entry.qos());
              }
            });
  }

  private static boolean isUnsupportedProtocolVersion(Throwable cause) {
    return cause instanceof Mqtt5ConnAckException connAck
        && connAck.getMqttMessage().getReasonCode()
            == Mqtt5ConnAckReasonCode.UNSUPPORTED_PROTOCOL_VERSION;
  }

  private <B extends MqttClientBuilderBase<B>> B applyCommonConfig(B builder) {
    B b =
        builder
            .identifier(clientId)
            .serverHost(brokerAddress.host())
            .serverPort(brokerAddress.port())
            .executorConfig(executorConfig)
            .automaticReconnectWithDefaultConfig()
            .addConnectedListener(this::onConnected)
            .addDisconnectedListener(this::onDisconnected);

    if (sslConfig != null) {
      b = b.sslConfig(sslConfig);
    }

    return b;
  }

  // region static config derivation

  /**
   * The MQTT version negotiation mode: the {@code 0:MqttVersion} connection property when present,
   * otherwise {@code BestAvailable} (Part 14 §7.3.4.4 Table 201).
   */
  static VersionMode versionMode(MqttConnectionConfig config) throws UaException {
    Variant variant = config.properties().get(MqttTransportProvider.MQTT_VERSION_PROPERTY);
    if (variant == null) {
      return VersionMode.BEST_AVAILABLE;
    }

    if (variant.value() instanceof String version) {
      switch (version) {
        case MqttTransportProvider.MQTT_VERSION_5_0:
          return VersionMode.MQTT_5;
        case MqttTransportProvider.MQTT_VERSION_3_1_1:
          return VersionMode.MQTT_3_1_1;
        case MqttTransportProvider.MQTT_VERSION_BEST_AVAILABLE:
          return VersionMode.BEST_AVAILABLE;
        default:
          // fall through to the error below
      }
    }

    throw new UaException(
        StatusCodes.Bad_ConfigurationError,
        "connection '%s': invalid 0:MqttVersion property value: %s (expected \"5.0\", \"3.1.1\","
                .formatted(config.name(), variant.value())
            + " or \"BestAvailable\")");
  }

  /**
   * The MQTT ClientId per Part 14 §7.3.4.4: the {@code 0:MqttClientId} connection property when
   * present, otherwise the stringified PublisherId, otherwise a random id for publisher-less
   * subscriber connections.
   */
  static String clientId(MqttConnectionConfig config) throws UaException {
    Variant variant = config.properties().get(MqttTransportProvider.MQTT_CLIENT_ID_PROPERTY);
    if (variant != null) {
      if (variant.value() instanceof String clientId && !clientId.isEmpty()) {
        return clientId;
      }
      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          "connection '%s': invalid 0:MqttClientId property value: %s"
              .formatted(config.name(), variant.value()));
    }

    PublisherId publisherId = config.publisherId();
    if (publisherId != null) {
      return publisherId.toCanonicalString();
    }

    return "milo-" + UUID.randomUUID().toString().substring(0, 8);
  }

  /** The §7.3.4.5 MQTT QoS of an engine-resolved delivery guarantee. */
  static MqttQos toMqttQos(BrokerTransportQualityOfService deliveryGuarantee) {
    return switch (deliveryGuarantee) {
      case AtLeastOnce -> MqttQos.AT_LEAST_ONCE;
      case ExactlyOnce -> MqttQos.EXACTLY_ONCE;
      case NotSpecified, BestEffort, AtMostOnce -> MqttQos.AT_MOST_ONCE;
    };
  }

  private static @Nullable Mqtt5SimpleAuth simpleAuth5(
      @Nullable String username, byte @Nullable [] password) {

    if (username != null && password != null) {
      return Mqtt5SimpleAuth.builder().username(username).password(password).build();
    } else if (username != null) {
      return Mqtt5SimpleAuth.builder().username(username).build();
    } else if (password != null) {
      return Mqtt5SimpleAuth.builder().password(password).build();
    } else {
      return null;
    }
  }

  private static @Nullable Mqtt3SimpleAuth simpleAuth3(
      @Nullable String username, byte @Nullable [] password) {

    if (username != null && password != null) {
      return Mqtt3SimpleAuth.builder().username(username).password(password).build();
    } else if (username != null) {
      return Mqtt3SimpleAuth.builder().username(username).build();
    } else {
      // MQTT 3.1.1 cannot carry a password without a username
      return null;
    }
  }

  /** UTF-8 encode a password, zeroing the intermediate char[] copy. */
  private static byte[] passwordBytes(char[] password) {
    ByteBuffer buffer = StandardCharsets.UTF_8.encode(CharBuffer.wrap(password));
    var bytes = new byte[buffer.remaining()];
    buffer.get(bytes);
    if (buffer.hasArray()) {
      Arrays.fill(buffer.array(), (byte) 0);
    }
    Arrays.fill(password, '\0');
    return bytes;
  }

  // endregion

  // region client adapters

  /** The narrow, version-independent client surface the session uses. */
  private interface ClientAdapter {

    CompletableFuture<Void> connect();

    boolean isConnected();

    CompletableFuture<Void> publish(
        String topic, MqttQos qos, boolean retain, @Nullable String contentType, byte[] payload);

    CompletableFuture<Void> subscribe(SubscriptionEntry entry);

    CompletableFuture<Void> unsubscribe(String topicFilter);

    CompletableFuture<Void> disconnect();
  }

  private final class Mqtt5Adapter implements ClientAdapter {

    private final Mqtt5AsyncClient client;

    Mqtt5Adapter() {
      Mqtt5ClientBuilder builder = applyCommonConfig(MqttClient.builder().useMqttVersion5());
      if (simpleAuth5 != null) {
        builder = builder.simpleAuth(simpleAuth5);
      }
      client = builder.buildAsync();
    }

    @Override
    public CompletableFuture<Void> connect() {
      return client.connect().thenApply(connAck -> (Void) null);
    }

    @Override
    public boolean isConnected() {
      return client.getState().isConnected();
    }

    @Override
    public CompletableFuture<Void> publish(
        String topic, MqttQos qos, boolean retain, @Nullable String contentType, byte[] payload) {

      Mqtt5PublishBuilder.Send.Complete<CompletableFuture<Mqtt5PublishResult>> builder =
          client.publishWith().topic(topic).qos(qos).retain(retain).payload(payload);

      if (contentType != null) {
        builder = builder.contentType(contentType);
      }

      return builder
          .send()
          .thenCompose(
              result ->
                  result
                      .getError()
                      .<CompletableFuture<Void>>map(CompletableFuture::failedFuture)
                      .orElseGet(() -> CompletableFuture.completedFuture(null)));
    }

    @Override
    public CompletableFuture<Void> subscribe(SubscriptionEntry entry) {
      return client
          .subscribeWith()
          .topicFilter(entry.topicFilter())
          .qos(entry.qos())
          .callback(
              publish ->
                  entry
                      .consumer()
                      .accept(publish.getTopic().toString(), publish.getPayloadAsBytes()))
          .executor(entry.executor())
          .send()
          .thenApply(subAck -> (Void) null);
    }

    @Override
    public CompletableFuture<Void> unsubscribe(String topicFilter) {
      return client
          .unsubscribeWith()
          .topicFilter(topicFilter)
          .send()
          .thenApply(unsubAck -> (Void) null);
    }

    @Override
    public CompletableFuture<Void> disconnect() {
      return client.disconnect();
    }
  }

  private final class Mqtt3Adapter implements ClientAdapter {

    private final Mqtt3AsyncClient client;

    Mqtt3Adapter() {
      Mqtt3ClientBuilder builder = applyCommonConfig(MqttClient.builder().useMqttVersion3());
      if (simpleAuth3 != null) {
        builder = builder.simpleAuth(simpleAuth3);
      }
      client = builder.buildAsync();
    }

    @Override
    public CompletableFuture<Void> connect() {
      return client.connect().thenApply(connAck -> (Void) null);
    }

    @Override
    public boolean isConnected() {
      return client.getState().isConnected();
    }

    @Override
    public CompletableFuture<Void> publish(
        String topic, MqttQos qos, boolean retain, @Nullable String contentType, byte[] payload) {

      // MQTT 3.1.1 has no message properties; the content type is silently discarded (§7.3.4.4)
      return client
          .publishWith()
          .topic(topic)
          .qos(qos)
          .retain(retain)
          .payload(payload)
          .send()
          .thenApply(publish -> (Void) null);
    }

    @Override
    public CompletableFuture<Void> subscribe(SubscriptionEntry entry) {
      return client
          .subscribeWith()
          .topicFilter(entry.topicFilter())
          .qos(entry.qos())
          .callback(
              publish ->
                  entry
                      .consumer()
                      .accept(publish.getTopic().toString(), publish.getPayloadAsBytes()))
          .executor(entry.executor())
          .send()
          .thenApply(subAck -> (Void) null);
    }

    @Override
    public CompletableFuture<Void> unsubscribe(String topicFilter) {
      return client
          .unsubscribeWith()
          .topicFilter(topicFilter)
          .send()
          .thenApply(unsubAck -> (Void) null);
    }

    @Override
    public CompletableFuture<Void> disconnect() {
      return client.disconnect();
    }
  }

  // endregion
}
