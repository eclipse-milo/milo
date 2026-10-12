/*
 * Copyright (c) 2026 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client.reverse;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

/**
 * Callback invoked when a {@link ReverseConnectAcceptor} creates and connects a production client.
 *
 * <p>The callback is the ownership handoff point for accepted dynamic clients. The acceptor has
 * already completed discovery, selected an endpoint, created an {@link OpcUaClient}, and opened its
 * Session; after this callback the application is responsible for retaining, using, and
 * disconnecting the client.
 */
@FunctionalInterface
public interface ReverseConnectAcceptorClientListener {

  /**
   * Observe a connected production client.
   *
   * @param discovery the discovery result that led to the client.
   * @param endpoint the selected endpoint.
   * @param client the connected production client.
   */
  void onClientConnected(
      ReverseConnectDiscoveryResult discovery, EndpointDescription endpoint, OpcUaClient client);
}
