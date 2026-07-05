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
 * Transport extension SPI: {@code TransportProvider} identifies itself by transport profile URI and
 * opens {@code PublisherChannel}/{@code SubscriberChannel} instances from context records that
 * carry the connection config, resolved address, executors, and encoding context. For broker
 * transports the engine resolves {@code MessageAddress} values for data, metadata, and status
 * messages before handing payloads to the channel. Built-in implementations live in subpackages;
 * external modules (e.g. MQTT) plug in through this SPI.
 */
@NullMarked
package org.eclipse.milo.opcua.sdk.pubsub.transport;

import org.jspecify.annotations.NullMarked;
