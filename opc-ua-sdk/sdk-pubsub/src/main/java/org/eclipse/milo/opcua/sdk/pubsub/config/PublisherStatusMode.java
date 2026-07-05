/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.config;

/**
 * Publisher-side policy for MQTT JSON PubSub status publishing.
 *
 * <p>The policy controls whether a publisher emits retained JSON {@code ua-status} messages on the
 * Part 14 status topic and whether the MQTT client registers a Last Will status message. It is
 * Milo-local runtime configuration for {@link MqttConnectionConfig}; it does not round-trip through
 * the Part 14 {@code PubSubConfigurationDataType}.
 */
public enum PublisherStatusMode {
  /**
   * Use MQTT Will when a JSON status stream can be represented by the connection's single MQTT
   * Will; otherwise publish cyclic JSON status when a JSON status stream exists.
   */
  AUTO,

  /** Do not publish retained status messages and do not configure an MQTT status Will. */
  DISABLED,

  /**
   * Require MQTT Will based status. Startup fails with {@code Bad_ConfigurationError} when the MQTT
   * connection cannot represent its publisher status with one retained Will message.
   */
  WILL,

  /**
   * Publish cyclic Operational status messages with {@code NextReportTime} and do not configure an
   * MQTT status Will.
   */
  CYCLIC
}
