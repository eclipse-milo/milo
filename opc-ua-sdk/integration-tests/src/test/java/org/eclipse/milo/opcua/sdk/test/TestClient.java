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

import java.util.Objects;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfigBuilder;

public final class TestClient {

  private TestClient() {}

  public static OpcUaClient create(
      OpcUaServer server, Consumer<OpcUaClientConfigBuilder> configCustomizer) throws UaException {

    return create(server, transportConfigBuilder -> {}, configCustomizer);
  }

  /**
   * Create a test client, allowing customization of both the transport config (e.g. to inject a
   * controllable {@code ExecutorService}) and the client config.
   *
   * @param server the {@link OpcUaServer} to connect to.
   * @param transportCustomizer customizes the {@link OpcTcpClientTransportConfigBuilder}.
   * @param configCustomizer customizes the {@link OpcUaClientConfigBuilder}.
   * @return a configured {@link OpcUaClient}.
   * @throws UaException if the client could not be created.
   */
  public static OpcUaClient create(
      OpcUaServer server,
      Consumer<OpcTcpClientTransportConfigBuilder> transportCustomizer,
      Consumer<OpcUaClientConfigBuilder> configCustomizer)
      throws UaException {

    EndpointConfig endpoint = server.getConfig().getEndpoints().iterator().next();

    return OpcUaClient.create(
        endpoint.getEndpointUrl(),
        endpoints ->
            endpoints.stream()
                .filter(
                    e ->
                        Objects.equals(
                            e.getSecurityPolicyUri(), endpoint.getSecurityPolicy().getUri()))
                .findFirst(),
        transportCustomizer,
        clientConfigBuilder -> {
          clientConfigBuilder
              .setApplicationName(LocalizedText.english("eclipse milo test client"))
              .setApplicationUri("urn:eclipse:milo:test:client")
              .setRequestTimeout(uint(5_000));

          configCustomizer.accept(clientConfigBuilder);
        });
  }
}
