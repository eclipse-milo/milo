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
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.pubsub.mqtt;

import org.jspecify.annotations.NullMarked;
