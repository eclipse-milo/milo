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

import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;

/**
 * Builds the production client configuration from discovery-first Reverse Connect context.
 *
 * <p>The factory is called after one reverse connection has been consumed for {@code GetEndpoints}
 * and an endpoint has been selected. Implementations should return a normal {@link
 * OpcUaClientConfig} whose endpoint, certificates, identity provider, and validation settings are
 * appropriate for the later production Session connection.
 */
@FunctionalInterface
public interface ReverseConnectClientConfigFactory {

  /**
   * Build a client configuration for the selected endpoint.
   *
   * @param discovery the discovery result that produced {@code endpoint}.
   * @param endpoint the selected production endpoint.
   * @return the client configuration used to create the production reverse client.
   * @throws Exception if the configuration cannot be built.
   */
  OpcUaClientConfig create(ReverseConnectDiscoveryResult discovery, EndpointDescription endpoint)
      throws Exception;
}
