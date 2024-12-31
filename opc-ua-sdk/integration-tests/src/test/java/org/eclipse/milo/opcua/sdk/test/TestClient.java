/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.test;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;

public final class TestClient {

  private TestClient() {}

  public static OpcUaClient create(OpcUaServer server) throws UaException {
    EndpointConfig endpoint = server.getConfig().getEndpoints().iterator().next();

    return OpcUaClient.create(
        endpoint.getEndpointUrl(),
        endpoints ->
            endpoints.stream()
                .filter(e -> e.getSecurityPolicyUri().equals(endpoint.getSecurityPolicy().getUri()))
                .findFirst(),
        transportConfigBuilder -> {},
        clientConfigBuilder ->
            clientConfigBuilder
                .setApplicationName(LocalizedText.english("eclipse milo test client"))
                .setApplicationUri("urn:eclipse:milo:test:client")
                .setRequestTimeout(uint(5_000)));
  }
}
