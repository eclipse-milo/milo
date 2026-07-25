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

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import java.util.function.Function;
import org.eclipse.milo.opcua.sdk.client.DiscoveryClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfig;
import org.eclipse.milo.opcua.sdk.client.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.sdk.server.EndpointConfig;
import org.eclipse.milo.opcua.sdk.server.OpcUaServer;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.LocalizedText;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
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
          applyDefaults(clientConfigBuilder);

          configCustomizer.accept(clientConfigBuilder);
        });
  }

  /**
   * Create a test client whose {@link OpcClientTransport} is built by {@code transportFactory}, so
   * a test can substitute or intercept the layer that hands the SDK the Server's answers.
   *
   * <p>{@link OpcUaClient#create(String, java.util.function.Function, Consumer, Consumer)}
   * constructs the transport itself and offers no seam for one, so this assembles the same
   * configuration and uses the {@link OpcUaClient#OpcUaClient(OpcUaClientConfig,
   * OpcClientTransport)} constructor instead. It is the only way to script a response the service
   * sets cannot express, e.g. an answer to a PublishRequest that is not a PublishResponse.
   *
   * @param server the {@link OpcUaServer} to connect to.
   * @param transportFactory builds the transport from a default {@link
   *     OpcTcpClientTransportConfig}.
   * @param configCustomizer customizes the {@link OpcUaClientConfigBuilder}.
   * @return a configured {@link OpcUaClient}.
   * @throws UaException if the endpoints could not be retrieved or the client could not be created.
   */
  public static OpcUaClient createWithTransport(
      OpcUaServer server,
      Function<OpcTcpClientTransportConfig, OpcClientTransport> transportFactory,
      Consumer<OpcUaClientConfigBuilder> configCustomizer)
      throws UaException {

    EndpointConfig endpointConfig = server.getConfig().getEndpoints().iterator().next();

    List<EndpointDescription> endpoints;
    try {
      endpoints = DiscoveryClient.getEndpoints(endpointConfig.getEndpointUrl()).get();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new UaException(StatusCodes.Bad_UnexpectedError, e);
    } catch (ExecutionException e) {
      throw new UaException(e.getCause());
    }

    EndpointDescription endpoint =
        endpoints.stream()
            .filter(
                e ->
                    Objects.equals(
                        e.getSecurityPolicyUri(), endpointConfig.getSecurityPolicy().getUri()))
            .findFirst()
            .orElseThrow(
                () -> new UaException(StatusCodes.Bad_ConfigurationError, "no endpoint selected"));

    OpcUaClientConfigBuilder clientConfigBuilder =
        OpcUaClientConfig.builder()
            .setEndpoint(endpoint)
            .setDiscoveryEndpoints(endpoints)
            .setSessionEndpointValidationEnabled(false);

    applyDefaults(clientConfigBuilder);

    configCustomizer.accept(clientConfigBuilder);

    OpcTcpClientTransportConfig transportConfig = OpcTcpClientTransportConfig.newBuilder().build();

    return new OpcUaClient(clientConfigBuilder.build(), transportFactory.apply(transportConfig));
  }

  private static void applyDefaults(OpcUaClientConfigBuilder clientConfigBuilder) {
    clientConfigBuilder
        .setApplicationName(LocalizedText.english("eclipse milo test client"))
        .setApplicationUri("urn:eclipse:milo:test:client")
        .setRequestTimeout(uint(5_000));
  }
}
