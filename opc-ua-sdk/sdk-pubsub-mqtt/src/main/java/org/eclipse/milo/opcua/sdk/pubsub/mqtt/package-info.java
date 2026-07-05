/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

/**
 * MQTT broker transport for OPC UA PubSub (OPC UA Part 14 §7.3.4), implemented on the HiveMQ MQTT
 * client: {@code MqttTransportProvider} plugs into the {@code sdk-pubsub} transport SPI and serves
 * both the {@code pubsub-mqtt-uadp} and {@code pubsub-mqtt-json} transport profiles. Register it
 * via {@code PubSubServiceConfig.Builder#transportProvider(TransportProvider)} for connections
 * configured with {@code MqttConnectionConfig}.
 *
 * <p>The transport also owns MQTT broker behavior around retained publication: data uses the
 * configured or derived data topics, metadata is published retained for broker discovery, and
 * JSON-mapped publisher status is published retained on the status topic. Where a single JSON
 * status stream can represent the connection, the MQTT CONNECT includes a retained Last Will so
 * subscribers can observe remote publisher death through {@code PublisherStatusReceivedEvent}
 * without waiting for data-plane silence.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.pubsub.mqtt;

import org.jspecify.annotations.NullMarked;
