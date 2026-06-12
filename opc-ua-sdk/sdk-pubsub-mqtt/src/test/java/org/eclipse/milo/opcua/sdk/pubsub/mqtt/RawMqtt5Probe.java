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

import static org.junit.jupiter.api.Assertions.fail;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import java.time.Duration;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * A raw MQTT 5 client used to observe the broker-side effects of the transport under test:
 * topic-level traffic, QoS, retain flags, and content types, independent of the Milo channels.
 *
 * <p>No automatic reconnect is configured, so a broker-initiated disconnect (e.g. ClientId
 * takeover) is terminal and observable via {@link #awaitDisconnected(Duration)}.
 */
final class RawMqtt5Probe implements AutoCloseable {

  private final Mqtt5AsyncClient client;
  private final BlockingQueue<Mqtt5Publish> messages;
  private final CountDownLatch disconnected;

  private RawMqtt5Probe(
      Mqtt5AsyncClient client, BlockingQueue<Mqtt5Publish> messages, CountDownLatch disconnected) {

    this.client = client;
    this.messages = messages;
    this.disconnected = disconnected;
  }

  /** Connect a probe with a random client id. */
  static RawMqtt5Probe connect(int port) throws Exception {
    return connect(port, "probe-" + UUID.randomUUID().toString().substring(0, 8));
  }

  /** Connect a probe with an explicit client id. */
  static RawMqtt5Probe connect(int port, String clientId) throws Exception {
    var messages = new LinkedBlockingQueue<Mqtt5Publish>();
    var disconnected = new CountDownLatch(1);

    Mqtt5AsyncClient client =
        MqttClient.builder()
            .useMqttVersion5()
            .identifier(clientId)
            .serverHost("127.0.0.1")
            .serverPort(port)
            .addDisconnectedListener(context -> disconnected.countDown())
            .buildAsync();

    client.connect().get(10, TimeUnit.SECONDS);

    return new RawMqtt5Probe(client, messages, disconnected);
  }

  /** Subscribe to {@code topicFilter}; received publishes are queued for the await methods. */
  void subscribe(String topicFilter, MqttQos qos) throws Exception {
    client
        .subscribeWith()
        .topicFilter(topicFilter)
        .qos(qos)
        .callback(messages::add)
        .send()
        .get(10, TimeUnit.SECONDS);
  }

  /** Wait for the next received publish. */
  Mqtt5Publish awaitMessage(Duration timeout) throws InterruptedException {
    return awaitMessage(publish -> true, timeout);
  }

  /** Wait for a received publish matching {@code predicate}, discarding non-matching ones. */
  Mqtt5Publish awaitMessage(Predicate<Mqtt5Publish> predicate, Duration timeout)
      throws InterruptedException {

    long deadline = System.nanoTime() + timeout.toNanos();
    while (true) {
      long remaining = deadline - System.nanoTime();
      if (remaining <= 0) {
        return fail("timed out waiting for a matching publish on the probe client");
      }
      Mqtt5Publish publish = messages.poll(Math.min(remaining, 100_000_000L), TimeUnit.NANOSECONDS);
      if (publish != null && predicate.test(publish)) {
        return publish;
      }
    }
  }

  /** Count the publishes received within {@code window}, without failing. */
  int countMessages(int atLeast, Duration window) throws InterruptedException {
    long deadline = System.nanoTime() + window.toNanos();
    int count = 0;
    while (count < atLeast) {
      long remaining = deadline - System.nanoTime();
      if (remaining <= 0) {
        break;
      }
      Mqtt5Publish publish = messages.poll(Math.min(remaining, 100_000_000L), TimeUnit.NANOSECONDS);
      if (publish != null) {
        count++;
      }
    }
    return count;
  }

  /** True once the broker (or {@link #close()}) disconnected this client. */
  boolean awaitDisconnected(Duration timeout) throws InterruptedException {
    return disconnected.await(timeout.toNanos(), TimeUnit.NANOSECONDS);
  }

  @Override
  public void close() {
    try {
      client.disconnect().get(5, TimeUnit.SECONDS);
    } catch (Exception e) {
      // already disconnected (e.g. by a ClientId takeover); nothing to clean up
    }
  }
}
