/*
 * Copyright (c) 2024 the Eclipse Milo Authors
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package org.eclipse.milo.opcua.sdk.client;

import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.ubyte;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;
import static org.eclipse.milo.opcua.stack.core.util.FutureUtils.failedFuture;

import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import org.eclipse.milo.opcua.stack.core.Stack;
import org.eclipse.milo.opcua.stack.core.StatusCodes;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.encoding.DefaultEncodingContext;
import org.eclipse.milo.opcua.stack.core.encoding.EncodingContext;
import org.eclipse.milo.opcua.stack.core.security.CertificateValidator;
import org.eclipse.milo.opcua.stack.core.security.SecurityPolicy;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.UInteger;
import org.eclipse.milo.opcua.stack.core.types.enumerated.MessageSecurityMode;
import org.eclipse.milo.opcua.stack.core.types.structured.ApplicationDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.FindServersResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.GetEndpointsRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.GetEndpointsResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServerRequest;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisterServerResponse;
import org.eclipse.milo.opcua.stack.core.types.structured.RegisteredServer;
import org.eclipse.milo.opcua.stack.core.types.structured.RequestHeader;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;
import org.eclipse.milo.opcua.stack.core.util.Lists;
import org.eclipse.milo.opcua.stack.transport.client.ClientApplicationContext;
import org.eclipse.milo.opcua.stack.transport.client.OpcClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransport;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfig;
import org.eclipse.milo.opcua.stack.transport.client.tcp.OpcTcpClientTransportConfigBuilder;

public class DiscoveryClient {

  private final ClientApplicationContext applicationContext;
  private final OpcClientTransport transport;

  public DiscoveryClient(EndpointDescription endpoint, OpcClientTransport transport) {
    this.transport = transport;

    applicationContext =
        new ClientApplicationContext() {
          @Override
          public EndpointDescription getEndpoint() {
            return endpoint;
          }

          @Override
          public Optional<KeyPair> getKeyPair() {
            return Optional.empty();
          }

          @Override
          public Optional<X509Certificate> getCertificate() {
            return Optional.empty();
          }

          @Override
          public Optional<X509Certificate[]> getCertificateChain() {
            return Optional.empty();
          }

          @Override
          public CertificateValidator getCertificateValidator() {
            return new CertificateValidator.InsecureCertificateValidator();
          }

          @Override
          public EncodingContext getEncodingContext() {
            return DefaultEncodingContext.INSTANCE;
          }

          @Override
          public UInteger getRequestTimeout() {
            return uint(60_000);
          }
        };
  }

  public DiscoveryClient connect() throws UaException {
    try {
      return connectAsync().get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  public CompletableFuture<DiscoveryClient> connectAsync() {
    return transport.connect(applicationContext).thenApply(c -> DiscoveryClient.this);
  }

  public DiscoveryClient disconnect() throws UaException {
    try {
      return disconnectAsync().get();
    } catch (InterruptedException | ExecutionException e) {
      throw UaException.extract(e).orElse(new UaException(StatusCodes.Bad_UnexpectedError, e));
    }
  }

  public CompletableFuture<DiscoveryClient> disconnectAsync() {
    return transport.disconnect().thenApply(c -> DiscoveryClient.this);
  }

  /**
   * Query the FindServers service at the {@code endpointUrl}.
   *
   * <p>The endpoint URL(s) for each server {@link ApplicationDescription} in the response can then
   * be used in a {@link #getEndpoints(String)} call to discover the endpoints for that server.
   *
   * @param endpointUrl the endpoint URL to find servers at.
   * @param localeIds list of locales to use. The server should return the applicationName in the
   *     ApplicationDescription using one of locales specified. If the server supports more than one
   *     of the requested locales then the server shall use the locale that appears first in this
   *     list. If the server does not support any of the requested locales it chooses an appropriate
   *     default locale. The server chooses an appropriate default locale if this list is empty.
   * @param serverUris list of servers to return. All known servers are returned if the list is
   *     empty.
   * @return the {@link FindServersResponse}s returned by the FindServers service.
   */
  public CompletableFuture<FindServersResponse> findServers(
      String endpointUrl, String[] localeIds, String[] serverUris) {

    RequestHeader requestHeader = newRequestHeader(NodeId.NULL_VALUE, uint(60_000));

    FindServersRequest request =
        new FindServersRequest(requestHeader, endpointUrl, localeIds, serverUris);

    return transport.sendRequestMessage(request).thenApply(FindServersResponse.class::cast);
  }

  /**
   * Query the GetEndpoints service at {@code endpointUrl}.
   *
   * @param endpointUrl the endpoint URL to get endpoints from.
   * @param localeIds list of locales to use. Specifies the locale to use when returning
   *     human-readable strings.
   * @param profileUris list of Transport Profile that the returned Endpoints shall support. All
   *     Endpoints are returned if the list is empty.
   * @return the {@link GetEndpointsResponse} returned by the GetEndpoints service.
   */
  public CompletableFuture<GetEndpointsResponse> getEndpoints(
      String endpointUrl, String[] localeIds, String[] profileUris) {

    RequestHeader header = newRequestHeader(NodeId.NULL_VALUE, uint(60_000));

    GetEndpointsRequest request =
        new GetEndpointsRequest(header, endpointUrl, localeIds, profileUris);

    return transport.sendRequestMessage(request).thenApply(GetEndpointsResponse.class::cast);
  }

  /**
   * Call the RegisterServer service to register {@code server}.
   *
   * @param server the {@link RegisteredServer} to register.
   * @return the {@link RegisterServerResponse} returned by the RegisterServer service.
   */
  public CompletableFuture<RegisterServerResponse> registerServer(RegisteredServer server) {
    RequestHeader header = newRequestHeader(NodeId.NULL_VALUE, uint(60_000));

    var request = new RegisterServerRequest(header, server);

    return transport.sendRequestMessage(request).thenApply(RegisterServerResponse.class::cast);
  }

  public RequestHeader newRequestHeader(NodeId authToken, UInteger requestTimeout) {
    return new RequestHeader(
        authToken, DateTime.now(), uint(0), uint(0), null, requestTimeout, null);
  }

  /**
   * Query the FindServers service at the {@code endpointUrl}.
   *
   * <p>The discovery URL(s) for each server {@link ApplicationDescription} in the response can then
   * be used in a {@link #getEndpoints(String)} call to discover the endpoints for that server.
   *
   * @param endpointUrl the endpoint URL to find servers at.
   * @return a List of {@link ApplicationDescription}s returned by the FindServers service.
   */
  public static CompletableFuture<List<ApplicationDescription>> findServers(String endpointUrl) {
    return findServers(endpointUrl, b -> {});
  }

  /**
   * Query the FindServers service at the {@code endpointUrl}.
   *
   * <p>The discovery URL(s) for each server {@link ApplicationDescription} in the response can then
   * be used in a {@link #getEndpoints(String)} call to discover the endpoints for that server.
   *
   * @param endpointUrl the endpoint URL to find servers at.
   * @param customizer a {@link Consumer} that accepts a {@link OpcTcpClientTransportConfigBuilder}
   *     for customization.
   * @return a List of {@link ApplicationDescription}s returned by the FindServers service.
   */
  public static CompletableFuture<List<ApplicationDescription>> findServers(
      String endpointUrl, Consumer<OpcTcpClientTransportConfigBuilder> customizer) {

    EndpointDescription endpoint =
        new EndpointDescription(
            endpointUrl,
            null,
            null,
            MessageSecurityMode.None,
            SecurityPolicy.None.getUri(),
            null,
            Stack.TCP_UASC_UABINARY_TRANSPORT_URI,
            ubyte(0));

    OpcTcpClientTransportConfigBuilder configBuilder = OpcTcpClientTransportConfig.newBuilder();
    customizer.accept(configBuilder);

    OpcTcpClientTransportConfig config = configBuilder.build();
    var transport = new OpcTcpClientTransport(config);

    DiscoveryClient discoveryClient = new DiscoveryClient(endpoint, transport);

    return discoveryClient
        .connectAsync()
        .thenCompose(c -> c.findServers(endpointUrl, new String[0], new String[0]))
        .whenComplete((e, ex) -> discoveryClient.disconnectAsync())
        .thenApply(response -> Lists.ofNullable(response.getServers()));
  }

  /**
   * Query the GetEndpoints service at {@code endpointUrl}.
   *
   * @param endpointUrl the endpoint URL to get endpoints from.
   * @return a List of {@link EndpointDescription}s returned by the GetEndpoints service.
   */
  public static CompletableFuture<List<EndpointDescription>> getEndpoints(String endpointUrl) {
    return getEndpoints(endpointUrl, b -> {});
  }

  /**
   * Query the GetEndpoints service at {@code endpointUrl}.
   *
   * @param endpointUrl the endpoint URL to get endpoints from.
   * @param customizer a {@link Consumer} that accepts a {@link OpcTcpClientTransportConfigBuilder}
   *     for customization.
   * @return a List of {@link EndpointDescription}s returned by the GetEndpoints service.
   */
  public static CompletableFuture<List<EndpointDescription>> getEndpoints(
      String endpointUrl, Consumer<OpcTcpClientTransportConfigBuilder> customizer) {

    String scheme = EndpointUtil.getScheme(endpointUrl);

    String profileUri;

    switch (Objects.requireNonNullElse(scheme, "").toLowerCase()) {
      case "opc.tcp":
        profileUri = Stack.TCP_UASC_UABINARY_TRANSPORT_URI;
        break;

      case "http":
      case "https":
      case "opc.http":
      case "opc.https":
        profileUri = Stack.HTTPS_UABINARY_TRANSPORT_URI;
        break;

      case "opc.ws":
      case "opc.wss":
        profileUri = Stack.WSS_UASC_UABINARY_TRANSPORT_URI;
        break;

      default:
        return failedFuture(
            new UaException(StatusCodes.Bad_InternalError, "unsupported protocol: " + scheme));
    }

    return getEndpoints(endpointUrl, profileUri, customizer);
  }

  private static CompletableFuture<List<EndpointDescription>> getEndpoints(
      String endpointUrl,
      String profileUri,
      Consumer<OpcTcpClientTransportConfigBuilder> customizer) {

    EndpointDescription endpoint =
        new EndpointDescription(
            endpointUrl,
            null,
            null,
            MessageSecurityMode.None,
            SecurityPolicy.None.getUri(),
            null,
            profileUri,
            ubyte(0));

    OpcTcpClientTransportConfigBuilder configBuilder = OpcTcpClientTransportConfig.newBuilder();
    customizer.accept(configBuilder);

    OpcTcpClientTransportConfig config = configBuilder.build();
    var transport = new OpcTcpClientTransport(config);

    DiscoveryClient discoveryClient = new DiscoveryClient(endpoint, transport);

    return discoveryClient
        .connectAsync()
        .thenCompose(c -> c.getEndpoints(endpointUrl, new String[0], new String[] {profileUri}))
        .whenComplete((e, ex) -> discoveryClient.disconnectAsync())
        .thenApply(response -> Lists.ofNullable(response.getEndpoints()));
  }
}
