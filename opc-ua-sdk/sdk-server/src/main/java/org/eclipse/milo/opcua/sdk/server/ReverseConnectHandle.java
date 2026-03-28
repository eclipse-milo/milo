/*
 * Copyright (c) 2025 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.server;

/**
 * An opaque handle representing a Reverse Connect registration. Returned by {@link
 * ReverseConnectManager#addReverseConnect} and used to remove the registration via {@link
 * ReverseConnectManager#removeReverseConnect}.
 */
public class ReverseConnectHandle {

  private final String clientEndpointUrl;
  private volatile String endpointUrl;
  final boolean autoSpawned;

  ReverseConnectHandle(String clientEndpointUrl, String endpointUrl) {
    this(clientEndpointUrl, endpointUrl, false);
  }

  ReverseConnectHandle(String clientEndpointUrl, String endpointUrl, boolean autoSpawned) {
    this.clientEndpointUrl = clientEndpointUrl;
    this.endpointUrl = endpointUrl;
    this.autoSpawned = autoSpawned;
  }

  void setEndpointUrl(String endpointUrl) {
    this.endpointUrl = endpointUrl;
  }

  /**
   * @return the client endpoint URL this handle is registered for.
   */
  public String getClientEndpointUrl() {
    return clientEndpointUrl;
  }

  /**
   * @return the server endpoint URL sent in ReverseHello messages.
   */
  public String getEndpointUrl() {
    return endpointUrl;
  }
}
