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
 * Runnable examples for Milo's OPC UA Part 14 PubSub SDK.
 *
 * <p>Examples are organized by transport and integration style:
 *
 * <ul>
 *   <li>{@code org.eclipse.milo.examples.pubsub.udp} — standalone publishers and subscribers
 *       exchanging UADP NetworkMessages over UDP.
 *   <li>{@code org.eclipse.milo.examples.pubsub.mqtt} — publishers and subscribers exchanging UADP
 *       or JSON NetworkMessages via an MQTT broker.
 *   <li>{@code org.eclipse.milo.examples.pubsub.server} — PubSub integrated with an {@code
 *       OpcUaServer} address space, publishing from and subscribing into server nodes.
 * </ul>
 *
 * <p>Each example's class-level Javadoc documents what it demonstrates, which terminal to start
 * first, the exact {@code mvn exec:java} command to run it, and the output to expect.
 */
package org.eclipse.milo.examples.pubsub;
