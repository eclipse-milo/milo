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
 * ServerPubSubOptions)} (pinned decision R8).
 *
 * <p>Precedence at attach: if {@link #load()} returns a non-null value, the loaded configuration
 * wins and the configuration passed to {@code attach} is ignored; otherwise the attach
 * configuration is used and {@link #save} is called once with its wire representation.
 *
 * <p>Save cadence: {@link #save} is called at attach (when nothing was loaded) and after every
 * successful configuration apply through the managed runtime — an owner {@code
 * ServerPubSub.runtime()} reconfigure or a remote {@code CloseAndUpdate} that applied changes.
 * Operations that do not mutate the configuration never save: Enable/Disable (state, not
 * configuration), ReserveIds (a reservation is registry state), and a match-only or failed
 * CloseAndUpdate.
 *
 * <p>The saved value's {@code ConfigurationVersion} carries the maintained VersionTime — the same
 * value exposed by the ns0 {@code ConfigurationVersion} property — and a non-zero stored version
 * seeds that clock at the next attach.
 *
 * <p>Failure contract: a {@link #save} failure after a successful apply never fails the operation
 * that triggered it — the change is already applied and its results are reported; the failure is
 * WARN-logged, surfaced via {@link ServerPubSub#lastConfigurationSaveError()} (cleared by the next
 * successful save), and retried at the next successful apply (there is no retry timer).
 *
 * <p>The store persists the Part 14 <b>wire</b> form: local-only configuration the wire cannot
 * carry — broker credentials ({@code BrokerSecurityConfig}), reader metadata policies, local
 * security-policy-URI overrides, standalone-dataset extras — is not persisted and must be
 * re-supplied via the attach configuration or options after a restart.
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
