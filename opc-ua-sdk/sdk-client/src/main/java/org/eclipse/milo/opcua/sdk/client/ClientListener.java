/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

/**
 * A callback notified when an on-demand client is created for a {@code
 * MultiplexedReverseConnectListener} after an unknown server connects via ReverseHello.
 */
@FunctionalInterface
public interface ClientListener {

  /**
   * Called on the listener's configured transport executor when a new client has been created for a
   * server that connected via ReverseHello.
   *
   * @param client the newly created client.
   */
  void onClientCreated(OpcUaClient client);
}
