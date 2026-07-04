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

import io.netty.channel.EventLoopGroup;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import org.eclipse.milo.opcua.sdk.pubsub.config.MqttConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.PublisherTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberChannel;
import org.eclipse.milo.opcua.sdk.pubsub.transport.SubscriberTransportContext;
import org.eclipse.milo.opcua.sdk.pubsub.transport.TransportProvider;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.jspecify.annotations.Nullable;

/**
 * A {@link TransportProvider} for MQTT broker connections (OPC UA Part 14 §7.3.4), implemented on
 * the HiveMQ MQTT client.
 *
 * <p>One provider instance serves both MQTT transport profiles — {@value
 * #TRANSPORT_PROFILE_URI_UADP} and {@value #TRANSPORT_PROFILE_URI_JSON} — because the message
 * mapping is orthogonal to the transport: the provider is selected for any {@link
 * MqttConnectionConfig} via {@link #supports(PubSubConnectionConfig)}. Register it with {@code
 * PubSubServiceConfig.Builder#transportProvider(TransportProvider)}.
 *
 * <p>One MQTT client is opened per connection and shared by the connection's publisher and
 * subscriber channels; the client disconnects when the last channel closes. The ClientId is the
 * {@code 0:MqttClientId} connection property when present, otherwise the stringified PublisherId,
 * otherwise a random id for publisher-less subscriber connections (§7.3.4.4).
 *
 * <p>MQTT version: the default {@code BestAvailable} mode connects with MQTT 5.0 and falls back to
 * MQTT 3.1.1 when the broker rejects CONNECT with "Unsupported Protocol Version"; pin a version
 * with the {@code 0:MqttVersion} connection property ({@value #MQTT_VERSION_5_0} or {@value
 * #MQTT_VERSION_3_1_1}). MQTT 5.0 publishes carry the Part 14 §7.3.4.9 Content Type ({@code
 * application/opcua+uadp} or {@code application/json}).
 *
 * <p>TLS and credentials come from the connection's {@code BrokerSecurityConfig}: {@code mqtts://}
 * URIs (or {@code tls(true)}) enable TLS with trust/key material built from the configured PEM
 * paths, and username/password map to MQTT simple authentication. v1 limits: {@code wss://} is not
 * supported ({@code Bad_NotSupported}); client keys must be unencrypted PKCS#8; no cipher-suite or
 * TLS-protocol pinning; one credential set per connection — the Part 14 {@code resourceUri} and
 * {@code authenticationProfileUri} per-queue credential lookups are not consulted.
 *
 * <p>Reconnect: automatic with the HiveMQ default backoff; subscriptions are explicitly re-issued
 * on every reconnect. Broker liveness is reported through the context's {@code
 * TransportStateListener}, when supplied: losing an established session notifies a down edge with
 * {@code Bad_ServerNotConnected} (the engine fails the connection to {@code Error}), and a
 * reconnect notifies an up edge after the subscriptions have been re-issued (the engine recovers
 * the connection to {@code Operational}). Sends attempted while disconnected still surface as send
 * failures in the engine's diagnostics.
 *
 * <p>Threading: channels perform network I/O on the HiveMQ client's Netty threads, which run on the
 * service event loop group supplied by the transport context (the client never shuts a
 * user-supplied group down); override it with {@link Builder#nettyExecutor(Executor)}. Received
 * messages are delivered on a single event loop, preserving arrival order.
 */
public final class MqttTransportProvider implements TransportProvider {

  /** The Part 14 Table 206 transport profile URI of MQTT with the UADP message mapping. */
  public static final String TRANSPORT_PROFILE_URI_UADP =
      "http://opcfoundation.org/UA-Profile/Transport/pubsub-mqtt-uadp";

  /** The Part 14 Table 206 transport profile URI of MQTT with the JSON message mapping. */
  public static final String TRANSPORT_PROFILE_URI_JSON =
      "http://opcfoundation.org/UA-Profile/Transport/pubsub-mqtt-json";

  /**
   * The connection property carrying the MQTT ClientId (Part 14 §7.3.4.4); when absent, the
   * stringified PublisherId is used.
   */
  public static final QualifiedName MQTT_CLIENT_ID_PROPERTY = new QualifiedName(0, "MqttClientId");

  /**
   * The connection property pinning the MQTT version (Part 14 §7.3.4.4 Table 201): {@value
   * #MQTT_VERSION_5_0}, {@value #MQTT_VERSION_3_1_1}, or {@value #MQTT_VERSION_BEST_AVAILABLE} (the
   * default).
   */
  public static final QualifiedName MQTT_VERSION_PROPERTY = new QualifiedName(0, "MqttVersion");

  /** {@link #MQTT_VERSION_PROPERTY} value pinning MQTT 5.0. */
  public static final String MQTT_VERSION_5_0 = "5.0";

  /** {@link #MQTT_VERSION_PROPERTY} value pinning MQTT 3.1.1. */
  public static final String MQTT_VERSION_3_1_1 = "3.1.1";

  /** {@link #MQTT_VERSION_PROPERTY} value selecting MQTT 5.0 with fallback to 3.1.1. */
  public static final String MQTT_VERSION_BEST_AVAILABLE = "BestAvailable";

  /**
   * Per-connection client sessions, shared by the connection's channels and reference counted.
   * Keyed by config identity: each engine connection runtime opens its channels with one config
   * instance, and a rebuilt connection (reconfigure) must not share a disconnecting client. Guarded
   * by itself.
   */
  private final Map<MqttConnectionConfig, SessionHolder> sessions = new IdentityHashMap<>();

  private final @Nullable Executor nettyExecutor;

  private MqttTransportProvider(Builder builder) {
    this.nettyExecutor = builder.nettyExecutor;
  }

  /**
   * Create a new {@link MqttTransportProvider} with default configuration.
   *
   * @return a new {@link MqttTransportProvider}.
   */
  public static MqttTransportProvider create() {
    return builder().build();
  }

  /**
   * Create a new {@link Builder}.
   *
   * @return a new {@link Builder}.
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * {@inheritDoc}
   *
   * <p>This provider implements both MQTT transport profiles; the returned value is declarative
   * only (the engine selects providers via {@link #supports(PubSubConnectionConfig)}). See also
   * {@link #TRANSPORT_PROFILE_URI_JSON}.
   */
  @Override
  public String transportProfileUri() {
    return TRANSPORT_PROFILE_URI_UADP;
  }

  @Override
  public boolean supports(PubSubConnectionConfig connection) {
    return connection instanceof MqttConnectionConfig;
  }

  @Override
  public PublisherChannel openPublisher(PublisherTransportContext context) throws UaException {
    MqttConnectionConfig connection = mqttConnection(context.connection());

    MqttClientSession session = acquireSession(connection, context.eventLoopGroup());
    try {
      return new MqttPublisherChannel(this, session, context.transportStateListener());
    } catch (RuntimeException e) {
      releaseSession(session);
      throw e;
    }
  }

  @Override
  public SubscriberChannel openSubscriber(SubscriberTransportContext context) throws UaException {
    MqttConnectionConfig connection = mqttConnection(context.connection());

    MqttClientSession session = acquireSession(connection, context.eventLoopGroup());
    try {
      return MqttSubscriberChannel.open(this, session, connection, context);
    } catch (RuntimeException e) {
      releaseSession(session);
      throw e;
    }
  }

  /**
   * Acquire the shared client session of {@code config}, creating it (and initiating its async
   * connect) on first acquisition.
   */
  private MqttClientSession acquireSession(
      MqttConnectionConfig config, EventLoopGroup eventLoopGroup) throws UaException {

    synchronized (sessions) {
      SessionHolder holder = sessions.get(config);
      if (holder == null) {
        Executor executor = nettyExecutor != null ? nettyExecutor : eventLoopGroup;
        MqttClientSession session = MqttClientSession.create(config, executor);
        holder = new SessionHolder(session);
        sessions.put(config, holder);
        session.start();
      }
      holder.refCount++;
      return holder.session;
    }
  }

  /**
   * Release one reference to {@code session}; the last release disconnects the client.
   *
   * @return a future that completes when the release (and disconnect, when last) has completed.
   */
  CompletableFuture<Void> releaseSession(MqttClientSession session) {
    synchronized (sessions) {
      SessionHolder holder = sessions.get(session.config());
      if (holder == null || holder.session != session) {
        return CompletableFuture.completedFuture(null);
      }
      holder.refCount--;
      if (holder.refCount <= 0) {
        sessions.remove(session.config());
        return session.disconnect();
      }
      return CompletableFuture.completedFuture(null);
    }
  }

  private static MqttConnectionConfig mqttConnection(PubSubConnectionConfig connection)
      throws UaException {

    if (connection instanceof MqttConnectionConfig mqttConnection) {
      return mqttConnection;
    } else {
      throw new UaException(
          StatusCodes.Bad_InvalidArgument,
          "connection '" + connection.name() + "' is not a MqttConnectionConfig");
    }
  }

  private static final class SessionHolder {

    final MqttClientSession session;
    int refCount = 0;

    SessionHolder(MqttClientSession session) {
      this.session = session;
    }
  }

  /** A builder of {@link MqttTransportProvider} instances. */
  public static final class Builder {

    private @Nullable Executor nettyExecutor;

    private Builder() {}

    /**
     * Override the executor the HiveMQ client uses for Netty I/O.
     *
     * @param nettyExecutor the executor; defaults to the service event loop group supplied by the
     *     transport context. A supplied executor is never shut down by the client or this provider.
     * @return this {@link Builder}.
     */
    public Builder nettyExecutor(Executor nettyExecutor) {
      this.nettyExecutor = nettyExecutor;
      return this;
    }

    /**
     * Build a new {@link MqttTransportProvider} from the values configured on this builder.
     *
     * @return a new {@link MqttTransportProvider}.
     */
    public MqttTransportProvider build() {
      return new MqttTransportProvider(this);
    }
  }
}
