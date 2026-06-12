/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.pubsub.server;

import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.stack.core.types.structured.PubSubConfiguration2DataType;
import org.jspecify.annotations.Nullable;

/**
 * Persistence for a PubSub configuration, in its Part 14 wire representation, consulted by {@link
 * ServerPubSub#attach(OpcUaServer, org.eclipse.milo.opcua.sdk.pubsub.config.PubSubConfig,
 * ServerPubSubOptions)}.
 *
 * <p>Precedence at attach: if {@link #load()} returns a non-null value, the loaded configuration
 * wins and the configuration passed to {@code attach} is ignored; otherwise the attach
 * configuration is used and {@link #save} is called once with its wire representation.
 *
 * <p>Configuration changes made later through {@link ServerPubSub#runtime()} are <b>not</b> saved
 * automatically in this version.
 */
public interface PubSubConfigurationStore {

  /**
   * Load the persisted configuration, if one exists.
   *
   * @return the persisted configuration, or {@code null} if none has been saved.
   */
  @Nullable PubSubConfiguration2DataType load();

  /**
   * Persist {@code value} as the current configuration.
   *
   * @param value the configuration to persist.
   */
  void save(PubSubConfiguration2DataType value);
}
