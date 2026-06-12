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

import java.util.List;
import java.util.Map;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.jspecify.annotations.Nullable;

/**
 * A PubSub connection: a transport-specific communication channel carrying writer and reader
 * groups.
 *
 * <p>All transport <em>config</em> types live in this module (the interface is sealed); transport
 * <em>implementations</em> are modular and resolved at runtime by transport profile URI.
 *
 * <p><b>API evolution:</b> although this interface is sealed, new permitted subtypes may be added
 * in future releases. Adding a permit is source-compatible only for callers whose switches include
 * a {@code default} branch, so switches over the current permits should include one.
 */
public sealed interface PubSubConnectionConfig permits UdpConnectionConfig, MqttConnectionConfig {

  /**
   * Get the name of this connection, unique among the connections of a {@code PubSubConfig}.
   *
   * @return the connection name.
   */
  String name();

  /**
   * Check if this connection is enabled.
   *
   * @return {@code true} if this connection is enabled.
   */
  boolean enabled();

  /**
   * Get the PublisherId used by this connection's writer groups.
   *
   * @return the {@link PublisherId}, or {@code null} for a subscriber-only connection. That a
   *     PublisherId is present when writer groups are configured is validated when the full
   *     configuration is assembled, not by the connection builders.
   */
  @Nullable PublisherId publisherId();

  /**
   * Get the writer groups of this connection.
   *
   * @return the {@link WriterGroupConfig}s of this connection.
   */
  List<WriterGroupConfig> writerGroups();

  /**
   * Get the reader groups of this connection.
   *
   * @return the {@link ReaderGroupConfig}s of this connection.
   */
  List<ReaderGroupConfig> readerGroups();

  /**
   * Get the connection properties, mapped to and from the Part 14 KeyValuePair array.
   *
   * @return the connection properties, in insertion order.
   */
  Map<QualifiedName, Variant> properties();

  /**
   * Get the raw transport settings escape hatch.
   *
   * @return an {@link ExtensionObject} that overrides the typed transport settings when mapping to
   *     the Part 14 configuration model, or {@code null} if not set.
   */
  @Nullable ExtensionObject rawTransportSettings();

  /**
   * Create a builder for a UDP connection.
   *
   * @param name the connection name.
   * @return a new {@link UdpConnectionConfig.Builder}.
   */
  static UdpConnectionConfig.Builder udp(String name) {
    return UdpConnectionConfig.builder(name);
  }

  /**
   * Create a builder for an MQTT connection.
   *
   * @param name the connection name.
   * @return a new {@link MqttConnectionConfig.Builder}.
   */
  static MqttConnectionConfig.Builder mqtt(String name) {
    return MqttConnectionConfig.builder(name);
  }
}
