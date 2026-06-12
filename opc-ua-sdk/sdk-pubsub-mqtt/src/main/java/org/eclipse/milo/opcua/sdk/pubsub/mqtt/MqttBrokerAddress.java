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

import java.net.URI;
import java.util.Locale;
import org.eclipse.milo.opcua.sdk.pubsub.config.BrokerSecurityConfig;
import org.eclipse.milo.opcua.sdk.pubsub.config.MqttConnectionConfig;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;

/**
 * The broker endpoint resolved from an {@link MqttConnectionConfig}: host, port, and whether the
 * connection uses TLS.
 *
 * <p>Address schemes per OPC UA Part 14 §7.3.4.2: {@code mqtt://} (plain, default port 1883) and
 * {@code mqtts://} (TLS, default port 8883) are supported; {@code wss://} (and the spec-undefined
 * {@code ws://}) are not supported in this version. TLS is also enabled for {@code mqtt://} URIs
 * when the connection's {@link BrokerSecurityConfig#isTls()} is set.
 *
 * @param host the broker host.
 * @param port the broker port.
 * @param tls whether the connection uses TLS.
 */
record MqttBrokerAddress(String host, int port, boolean tls) {

  private static final int DEFAULT_PORT_MQTT = 1883;
  private static final int DEFAULT_PORT_MQTTS = 8883;

  /**
   * Resolve the broker address of {@code connection}.
   *
   * @param connection the connection config.
   * @return the resolved {@link MqttBrokerAddress}.
   * @throws UaException with {@code Bad_NotSupported} for WebSocket schemes, or {@code
   *     Bad_ConfigurationError} for unsupported schemes or a missing host.
   */
  static MqttBrokerAddress parse(MqttConnectionConfig connection) throws UaException {
    URI brokerUri = connection.getBrokerUri();

    String scheme =
        brokerUri.getScheme() == null ? "" : brokerUri.getScheme().toLowerCase(Locale.ROOT);

    BrokerSecurityConfig security = connection.getBrokerSecurity();
    boolean securityTls = security != null && security.isTls();

    boolean tls;
    int defaultPort;
    switch (scheme) {
      case "mqtt" -> {
        tls = securityTls;
        defaultPort = DEFAULT_PORT_MQTT;
      }
      case "mqtts" -> {
        tls = true;
        defaultPort = DEFAULT_PORT_MQTTS;
      }
      case "ws", "wss" ->
          throw new UaException(
              StatusCodes.Bad_NotSupported,
              "connection '%s': MQTT over WebSocket is not supported in this version: %s"
                  .formatted(connection.name(), brokerUri));
      default ->
          throw new UaException(
              StatusCodes.Bad_ConfigurationError,
              "connection '%s': unsupported broker URI scheme: %s"
                  .formatted(connection.name(), brokerUri));
    }

    String host = brokerUri.getHost();
    if (host == null || host.isEmpty()) {
      throw new UaException(
          StatusCodes.Bad_ConfigurationError,
          "connection '%s': broker URI has no host: %s".formatted(connection.name(), brokerUri));
    }

    int port = brokerUri.getPort() != -1 ? brokerUri.getPort() : defaultPort;

    return new MqttBrokerAddress(host, port, tls);
  }
}
