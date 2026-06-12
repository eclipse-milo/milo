/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.transport;

import org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConnectionConfig;
import org.eclipse.milo.opcua.stack.core.UaException;

/**
 * SPI for PubSub message transports, e.g. UDP or MQTT.
 *
 * <p>A provider is selected for a connection when it both advertises the connection's transport
 * profile URI and reports {@link #supports(PubSubConnectionConfig)} for the concrete connection
 * config. The UDP transport is built in; additional providers are registered via {@code
 * PubSubServiceConfig.Builder#transportProvider(TransportProvider)}.
 */
public interface TransportProvider {

  /**
   * Get the transport profile URI this provider implements, e.g. {@code
   * "http://opcfoundation.org/UA-Profile/Transport/pubsub-udp-uadp"}.
   *
   * @return the transport profile URI this provider implements.
   */
  String transportProfileUri();

  /**
   * Check whether this provider can open channels for the given connection.
   *
   * @param connection the connection config to check.
   * @return {@code true} if this provider can open channels for {@code connection}.
   */
  boolean supports(PubSubConnectionConfig connection);

  /**
   * Open a publisher channel for the connection described by {@code context}.
   *
   * @param context the transport context for the connection.
   * @return an open {@link PublisherChannel}.
   * @throws UaException if the channel could not be opened.
   */
  PublisherChannel openPublisher(PublisherTransportContext context) throws UaException;

  /**
   * Open a subscriber channel for the connection described by {@code context}, delivering received
   * messages to the context's message consumer.
   *
   * @param context the transport context for the connection.
   * @return an open {@link SubscriberChannel}.
   * @throws UaException if the channel could not be opened.
   */
  SubscriberChannel openSubscriber(SubscriberTransportContext context) throws UaException;
}
